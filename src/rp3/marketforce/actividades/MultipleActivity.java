package rp3.marketforce.actividades;

import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.AgendaTareaOpciones;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class MultipleActivity extends  ActividadActivity {
	
	AgendaTareaActividades ata;
	LinearLayout Grupo;
	String[] respuestas;
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
	    	ata = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad);
	    }
	    else
	    {
	    	ata = AgendaTareaActividades.getActividadSimpleFromParent(getDataBase(), id_ruta, id_agenda, id_tarea, id_actividad, id_padre);
	    }
	    
	    setTextViewText(R.id.label_pregunta_actividad, ata.getDescripcion());
	    setTextViewText(R.id.detail_activity_number, numero + "");
	    respuestas = ata.getResultado().split(",");
	    List<AgendaTareaOpciones> ag_opcs = AgendaTareaOpciones.getOpciones(getDataBase(), ata.getIdAgenda(), ata.getIdTarea(), ata.getIdTareaActividad());
	    for(AgendaTareaOpciones opcion: ag_opcs)
		{
	    	CheckBox setter = new CheckBox(this);
	    	setter.setButtonDrawable(R.drawable.custom_checkbox);
	    	setter.setChecked(existeRespuesta(opcion.getDescripcion()));
	    	setter.setText(opcion.getDescripcion());
	    	setter.setPadding(30, 15, 0, 15);
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
		
		ata.setResultado(respuesta);
		AgendaTareaActividades.update(getDataBase(), ata);
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
