package rp3.marketforce.actividades;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;

public class CheckboxActivity extends ActividadActivity {
	
	AgendaTareaActividades ata;

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
	    	ata = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad);
	    }
	    else
	    {
	    	ata = AgendaTareaActividades.getActividadSimpleFromParent(getDataBase(), id_ruta, id_agenda, id_tarea, id_actividad, id_padre);
	    }
	    
	    setTextViewText(R.id.label_pregunta_actividad, ata.getDescripcion());
	    if(!ata.getResultado().equalsIgnoreCase("null"))
	    {
	    	if(ata.getResultado().equalsIgnoreCase("true"))
	    	{
	    		((CheckBox) findViewById(R.id.actividad_check_respuesta)).setChecked(true);
	    	}
	    	else
	    	{
	    		((CheckBox) findViewById(R.id.actividad_check_respuesta)).setChecked(false);
	    	}
	    }
	
	    // TODO Auto-generated method stub
	}

	@Override
	public void aceptarCambios(View v) {
		ata.setResultado("" + ((CheckBox) findViewById(R.id.actividad_check_respuesta)).isChecked());
		AgendaTareaActividades.update(getDataBase(), ata);
		if(id_padre == 0)
		{
			AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), id_agenda, id_ruta, id_tarea);
			agt.setEstadoTarea("R");
			AgendaTarea.update(getDataBase(), agt);
		}
		finish();
		
	}

}
