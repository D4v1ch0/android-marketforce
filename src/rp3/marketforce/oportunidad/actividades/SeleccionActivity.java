package rp3.marketforce.oportunidad.actividades;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.AgendaTareaOpciones;
import rp3.marketforce.models.oportunidad.OportunidadTarea;
import rp3.marketforce.models.oportunidad.OportunidadTareaActividad;

public class SeleccionActivity extends ActividadActivity {
	
	Actividad ata;
	private OportunidadTareaActividad act;

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
	    	ata = Actividad.getActividadSimple(getDataBase(), id_tarea);
	    }
	    else
	    {
	    	ata = Actividad.getActividadSimple(getDataBase(), id_tarea, id_padre);
	    }
	    
	    setTextViewText(R.id.label_pregunta_actividad, ata.getDescripcion());
	    
	    List<AgendaTareaOpciones> ag_opcs = AgendaTareaOpciones.getOpciones(getDataBase(), ata.getIdTarea(), ata.getIdTareaActividad());
		List<String> opciones = new ArrayList<String>();
        opciones.add("");
		
		for(AgendaTareaOpciones opcion: ag_opcs)
		{
			opciones.add(opcion.getDescripcion());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,opciones);
		setSpinnerAdapter(R.id.actividad_seleccion_respuesta, adapter);
		
		act = OportunidadTareaActividad.getActividadSimple(getDataBase(), id_etapa, id_oportunidad, id_tarea, ata.getIdTareaActividad());
		if(act != null)
		{
			setSpinnerSelectionByPosition(R.id.actividad_seleccion_respuesta, opciones.indexOf(act.getResultado()));
		}
		else
		{
			act = initActividad(ata.getIdTareaActividad());
		}
		
		if(soloVista)
		{
			((Spinner) findViewById(R.id.actividad_seleccion_respuesta)).setEnabled(false); 
		}
	}

	@Override
	public void aceptarCambios(View v) {
		String respuesta = getSpinnerGeneralValueSelectedCode(R.id.actividad_seleccion_respuesta);
		act.setResultado(respuesta);
        if(act.getID() == 0)
            OportunidadTareaActividad.insert(getDataBase(), act);
        else
            OportunidadTareaActividad.update(getDataBase(), act);
		if(id_padre == 0)
		{
			OportunidadTarea agt = OportunidadTarea.getTarea(getDataBase(), id_oportunidad, id_etapa, id_tarea);
			agt.setEstado("R");
			AgendaTarea.update(getDataBase(), agt);
		}
		finish();
		
	}

}
