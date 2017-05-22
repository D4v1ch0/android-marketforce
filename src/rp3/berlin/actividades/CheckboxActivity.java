package rp3.berlin.actividades;

import rp3.berlin.R;
import rp3.berlin.models.Actividad;
import rp3.berlin.models.AgendaTarea;
import rp3.berlin.models.AgendaTareaActividades;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;

public class CheckboxActivity extends ActividadActivity {
	
	Actividad ata;
	AgendaTareaActividades act;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    int tema = getIntent().getExtras().getInt(ARG_THEME, R.style.MyAppTheme);
		setTheme(tema);
		if(tema != R.style.MyAppTheme)
		{
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_checkbox_actividad);
	    
	    if(id_padre == 0)
	    {
	    	ata = Actividad.getActividadSimple(getDataBase(), id_tarea);
	    }
	    else
	    {
	    	ata = Actividad.getActividadSimple(getDataBase(), id_tarea, id_padre);
	    }
	    
	    setTextViewText(R.id.label_pregunta_actividad, ata.getDescripcion());
	    act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_tarea, ata.getIdTareaActividad());
		if(act != null)
		{
			if(!act.getResultado().equalsIgnoreCase("null"))
		    {
		    	if(act.getResultado().equalsIgnoreCase("true"))
		    	{
		    		((CheckBox) findViewById(R.id.actividad_check_respuesta)).setChecked(true);
		    	}
		    	else
		    	{
		    		((CheckBox) findViewById(R.id.actividad_check_respuesta)).setChecked(false);
		    	}
		    }
		
		}
		else
		{
			act = initActividad(ata.getIdTareaActividad());
		}
		
		if(soloVista)
		{
			((CheckBox) findViewById(R.id.actividad_check_respuesta)).setEnabled(false); 
		}
	}

	@Override
	public void aceptarCambios(View v) {
		act.setResultado("" + ((CheckBox) findViewById(R.id.actividad_check_respuesta)).isChecked());
        if(act.getID() == 0)
            AgendaTareaActividades.insert(getDataBase(), act);
        else
            AgendaTareaActividades.update(getDataBase(), act);
		if(id_padre == 0)
		{
			AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), id_agenda, id_ruta, id_tarea);
			agt.setEstadoTarea("R");
			AgendaTarea.update(getDataBase(), agt);
		}
		finish();
		
	}

}
