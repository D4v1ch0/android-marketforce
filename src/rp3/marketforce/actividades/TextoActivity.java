package rp3.marketforce.actividades;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class TextoActivity extends ActividadActivity {

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
	    setContentView(R.layout.layout_texto_activitidad);
	    
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
	    	setTextViewText(R.id.actividad_texto_respuesta, ata.getResultado());
	    }
	}

	@Override
	public void aceptarCambios(View v) {
		ata.setResultado(getTextViewString(R.id.actividad_texto_respuesta));
		AgendaTareaActividades.update(getDataBase(), ata);
		if(id_padre == 0)
		{
			AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), ata.getIdAgenda(), ata.getIdRuta(), ata.getIdTarea());
			agt.setEstadoTarea("R");
			AgendaTarea.update(getDataBase(), agt);
		}
		finish();
		
	}

}
