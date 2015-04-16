package rp3.marketforce.actividades;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

public class TextoActivity extends ActividadActivity {

	Actividad ata;
	private AgendaTareaActividades act;
    boolean actSinGrupo;

	
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
	    setContentView(R.layout.layout_texto_activitidad);
	    
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
        if(id_agenda != 0)
	        act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_tarea, ata.getIdTareaActividad());
        else
            act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), id_agenda_int, id_tarea, ata.getIdTareaActividad());
		if(act != null)
		{
			if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase("null"))
		    {
		    	setTextViewText(R.id.actividad_texto_respuesta, act.getResultado());
		    }
		}
		else
		{
            if(id_padre == 0)
			    act = initActividad(ata.getIdTareaActividad());
            else
                act = initActividadInsert(ata.getIdTareaActividad());
		}
		
		if(soloVista)
		{
			((TextView) findViewById(R.id.actividad_texto_respuesta)).setEnabled(false);
            findViewById(R.id.actividad_aceptar).setVisibility(View.GONE);
            findViewById(R.id.actividad_cancelar).setVisibility(View.GONE);
		}
	    
	}

	@Override
	public void aceptarCambios(View v) {
        if(getTextViewString(R.id.actividad_texto_respuesta).equalsIgnoreCase(""))
            act.setResultado(" ");
        else
		    act.setResultado(getTextViewString(R.id.actividad_texto_respuesta));
        //act.setIdsResultado(getTextViewString(R.id.actividad_texto_respuesta));

		if(id_padre == 0)
		{
            if(act.getID() == 0)
                AgendaTareaActividades.insert(getDataBase(), act);
            else
                AgendaTareaActividades.update(getDataBase(), act);
            if(!actSinGrupo) {
                AgendaTarea agt = null;
                if(act.getIdAgenda() != 0)
                    agt = AgendaTarea.getTarea(getDataBase(), act.getIdAgenda(), act.getIdRuta(), ata.getIdTarea());
                else
                    agt = AgendaTarea.getTareaIntern(getDataBase(), act.get_idAgenda(), act.getIdRuta(), ata.getIdTarea());
                agt.setEstadoTarea("R");
                AgendaTarea.update(getDataBase(), agt);
            }
		}
        else
            AgendaTareaActividades.update(getDataBase(), act);
		finish();
		
	}

}
