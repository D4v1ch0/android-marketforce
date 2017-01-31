package rp3.auna.oportunidad.actividades;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.List;

import rp3.auna.R;
import rp3.auna.models.Actividad;
import rp3.auna.models.AgendaTareaOpciones;
import rp3.auna.models.oportunidad.OportunidadTarea;
import rp3.auna.models.oportunidad.OportunidadTareaActividad;

public class MultipleActivity extends ActividadActivity {
	
	Actividad ata;
	LinearLayout Grupo;
	String[] respuestas;
	private OportunidadTareaActividad act;
    private boolean actSinGrupo;

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		int numero = getIntent().getExtras().getInt(ARG_NUMERO, 1);
		int tema = getIntent().getExtras().getInt(ARG_THEME, R.style.MyAppTheme);
		setTheme(tema);
        actSinGrupo = getIntent().getExtras().getBoolean(ARG_SIN_GRUPO, false);
		if(tema != R.style.MyAppTheme)
		{
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_multiple_actividad);
	    
	    Grupo = (LinearLayout) findViewById(R.id.actividad_multiple_respuesta);
	    
	    if(id_padre == 0)
	    {
            if(actSinGrupo)
                ata = Actividad.getActividadSimple(getDataBase(), id_tarea, id_actividad);
            else
                ata = Actividad.getActividadSimple(getDataBase(), id_actividad);
	    }
	    else
	    {
	    	ata = Actividad.getActividadSimple(getDataBase(), id_tarea, id_actividad);
	    }
	    
	    setTextViewText(R.id.label_pregunta_actividad, ata.getDescripcion());
	    setTextViewText(R.id.detail_activity_number, numero + "");
	    act = OportunidadTareaActividad.getActividadSimple(getDataBase(), id_etapa, id_oportunidad, id_tarea, ata.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null)
				respuestas = act.getResultado().split(",");
		}
		else
		{
            if(id_padre == 0)
                act = initActividad(ata.getIdTareaActividad());
            else
                act = initActividadInsert(ata.getIdTareaActividad());
		}
	    
	    List<AgendaTareaOpciones> ag_opcs = AgendaTareaOpciones.getOpciones(getDataBase(), act.getIdTarea(), ata.getIdTareaActividad());
	    for(AgendaTareaOpciones opcion: ag_opcs)
		{
	    	CheckBox setter = new CheckBox(this);
	    	setter.setButtonDrawable(R.drawable.custom_checkbox);
	    	if(respuestas != null)
	    		setter.setChecked(existeRespuesta(opcion.getDescripcion()));
	    	setter.setText(opcion.getDescripcion());
	    	setter.setPadding(30, 15, 0, 15);
	    	if(soloVista)
			{
				setter.setEnabled(false); 
			}
			Grupo.addView(setter);
		}
	}
	@Override
	public void aceptarCambios(View v) {
		String respuesta = "", idsresultados = "";
        List<AgendaTareaOpciones> ag_opcs = AgendaTareaOpciones.getOpciones(getDataBase(), act.getIdTarea(), ata.getIdTareaActividad());
		for(int i = 0; i < Grupo.getChildCount();i++)
		{
			if(((CheckBox)Grupo.getChildAt(i)).isChecked())
			{
				respuesta = respuesta + ((CheckBox)Grupo.getChildAt(i)).getText() + ",";
                for(AgendaTareaOpciones opc: ag_opcs)
                {
                    if(((CheckBox)Grupo.getChildAt(i)).getText().toString().equalsIgnoreCase(opc.getDescripcion()))
                    {
                        idsresultados = idsresultados + opc.getOrden() + ",";
                    }
                }
			}
		}
		if(respuesta.length()>0)
			respuesta = respuesta.substring(0, respuesta.length()-1);
        if(idsresultados.length()>0)
            idsresultados = idsresultados.substring(0, idsresultados.length()-1);
		
		act.setResultado(respuesta);
        act.setIdsResultado(idsresultados);

		if(id_padre == 0)
		{
            if(act.getID() == 0)
                OportunidadTareaActividad.insert(getDataBase(), act);
            else
                OportunidadTareaActividad.update(getDataBase(), act);
            if(!actSinGrupo) {
                OportunidadTarea agt = OportunidadTarea.getTarea(getDataBase(), id_oportunidad, id_etapa, id_tarea);
                agt.setEstado("R");
                OportunidadTarea.update(getDataBase(), agt);
            }
		}
        else
            OportunidadTareaActividad.update(getDataBase(), act);
		finish();
		
	}
	
	public boolean existeRespuesta(String resp)
	{
		for(String r : respuestas)
		{
			if(r.equalsIgnoreCase(resp))
			{
				return true;
			}
		}
		return false;
	}

}
