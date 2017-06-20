package rp3.marketforce.actividades;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.ruta.RutasDetailFragment;

public abstract class ActividadActivity extends BaseActivity {
	private static final String TAG = ActividadActivity.class.getSimpleName();
	public static String ARG_THEME = "theme";
    public static String ARG_SIN_GRUPO = "sin_grupo";
	public static String ARG_PADRE_ID = "padre";
    public static String ARG_AGENDA_INT = "agenda_int";
	public static String ARG_TAREA = "tarea";
	public static String ARG_NUMERO = "numero";
	public static String ARG_VISTA = "vista";
	public static String ARG_TITULO = "titulo";
	
	protected int id_actividad;
	protected long id_agenda;
    protected long id_agenda_int;
	protected int id_ruta;
	protected int id_padre;
	protected int id_tarea;
	protected boolean soloVista;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        if(getActionBar()!= null)
		{
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle(getIntent().getExtras().getString(ARG_TITULO, "RP3 MarketForce"));
		}
		id_actividad = getIntent().getExtras().getInt(RutasDetailFragment.ARG_ITEM_ID, 0);
		id_agenda = getIntent().getExtras().getLong(RutasDetailFragment.ARG_AGENDA_ID, 0);
		id_ruta = getIntent().getExtras().getInt(RutasDetailFragment.ARG_RUTA_ID, 0);
        id_agenda_int = getIntent().getExtras().getLong(ARG_AGENDA_INT, 0);
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
        act.set_idAgenda(id_agenda_int);
        //AgendaTareaActividades.insert(getDataBase(), act);
        return act;
    }

    protected AgendaTareaActividades initActividadInsert(int idActividad)
    {
        AgendaTareaActividades act = new AgendaTareaActividades();
        act.setIdAgenda((int) id_agenda);
        act.setIdTarea(id_actividad);
        act.setIdRuta(id_ruta);
        act.setIdTareaActividad(idActividad);
        act.set_idAgenda(id_agenda_int);
        AgendaTareaActividades.insert(getDataBase(), act);
        return act;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        switch(item.getItemId())
        {
            case R.id.action_save:
                aceptarCambios(new View(this));
                break;
            case R.id.action_cancel:
                cancelarCambios(new View(this));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * Ciclo de vida
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }


}
