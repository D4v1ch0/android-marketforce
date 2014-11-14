package rp3.marketforce.actividades;

import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.AgendaTareaOpciones;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;

public class SeleccionActivity extends ActividadActivity {
	
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
	    setContentView(R.layout.layout_seleccion_actividad);
	    
	    if(id_padre == 0)
	    {
	    	ata = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_actividad);
	    }
	    else
	    {
	    	ata = AgendaTareaActividades.getActividadSimpleFromParent(getDataBase(), id_ruta, id_agenda, id_tarea, id_actividad, id_padre);
	    }
	    
	    setTextViewText(R.id.label_pregunta_actividad, ata.getDescripcion());
	    
	    List<AgendaTareaOpciones> ag_opcs = AgendaTareaOpciones.getOpciones(getDataBase(), ata.getIdAgenda(), ata.getIdTarea(), ata.getIdTareaActividad());
		List<String> opciones = new ArrayList<String>();
		
		for(AgendaTareaOpciones opcion: ag_opcs)
		{
			opciones.add(opcion.getDescripcion());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opciones);
		setSpinnerAdapter(R.id.actividad_seleccion_respuesta, adapter);
	
	    // TODO Auto-generated method stub
	}

	@Override
	public void aceptarCambios(View v) {
		String respuesta = getSpinnerGeneralValueSelectedCode(R.id.actividad_seleccion_respuesta);
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

}
