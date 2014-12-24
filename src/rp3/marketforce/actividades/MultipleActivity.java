package rp3.marketforce.actividades;

import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.AgendaTareaOpciones;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MultipleActivity extends  ActividadActivity {
	
	Actividad ata;
	LinearLayout Grupo;
	String[] respuestas;
	private AgendaTareaActividades act;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		int numero = getIntent().getExtras().getInt(ARG_NUMERO, 1);
		int tema = getIntent().getExtras().getInt(ARG_THEME, R.style.MyAppTheme);
		setTheme(tema);
		if(tema != R.style.MyAppTheme)
		{
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_multiple_actividad);
	    
	    Grupo = (LinearLayout) findViewById(R.id.actividad_multiple_respuesta);
	    
	    if(id_padre == 0)
	    {
	    	ata = Actividad.getActividadSimple(getDataBase(), id_tarea);
	    }
	    else
	    {
	    	ata = Actividad.getActividadSimple(getDataBase(), id_tarea, id_actividad);
	    }
	    
	    setTextViewText(R.id.label_pregunta_actividad, ata.getDescripcion());
	    setTextViewText(R.id.detail_activity_number, numero + "");
	    act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_tarea, ata.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null)
				respuestas = act.getResultado().split(",");
		}
		else
		{
			act = initActividad(ata.getIdTareaActividad());
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
		String respuesta = "";
		for(int i = 0; i < Grupo.getChildCount();i++)
		{
			if(((CheckBox)Grupo.getChildAt(i)).isChecked())
			{
				respuesta = respuesta + ((CheckBox)Grupo.getChildAt(i)).getText() + ",";
			}
		}
		if(respuesta.length()>0)
			respuesta = respuesta.substring(0, respuesta.length()-1);
		
		act.setResultado(respuesta);
		AgendaTareaActividades.update(getDataBase(), act);
		if(id_padre == 0)
		{
			AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), id_agenda, id_ruta, id_tarea);
			agt.setEstadoTarea("R");
			AgendaTarea.update(getDataBase(), agt);
		}
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