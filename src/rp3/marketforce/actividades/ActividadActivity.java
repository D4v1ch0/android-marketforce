package rp3.marketforce.actividades;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.ruta.RutasDetailFragment;

public abstract class ActividadActivity extends BaseActivity {
	
	public static String ARG_THEME = "theme";
	public static String ARG_PADRE_ID = "padre";
	public static String ARG_TAREA = "tarea";
	public static String ARG_NUMERO = "numero";
	public static String ARG_VISTA = "vista";
	
	protected int id_actividad;
	protected long id_agenda;
	protected int id_ruta;
	protected int id_padre;
	protected int id_tarea;
	protected boolean soloVista;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getActionBar()!= null)
		{
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		id_actividad = getIntent().getExtras().getInt(RutasDetailFragment.ARG_ITEM_ID, 0);
		id_agenda = getIntent().getExtras().getLong(RutasDetailFragment.ARG_AGENDA_ID, 0);
		id_ruta = getIntent().getExtras().getInt(RutasDetailFragment.ARG_RUTA_ID, 0);
		id_padre = getIntent().getExtras().getInt(ARG_PADRE_ID, 0);
		id_tarea = getIntent().getExtras().getInt(ARG_TAREA, 0);
		soloVista = getIntent().getExtras().getBoolean(ARG_VISTA, false);
	}
	
	public abstract void aceptarCambios(View v);
	
	public void cancelarCambios(View v)
	{
		finish();
	}
	
	protected AgendaTareaActividades initActividad(int idActividad)
	{
		AgendaTareaActividades act = new AgendaTareaActividades();
		act.setIdAgenda((int) id_agenda);
		act.setIdTarea(id_actividad);
		act.setIdRuta(id_ruta);
		act.setIdTareaActividad(idActividad);
		AgendaTareaActividades.insert(getDataBase(), act);
		return act;
	}
}