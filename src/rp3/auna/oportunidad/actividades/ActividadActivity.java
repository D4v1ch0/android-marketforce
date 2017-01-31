package rp3.auna.oportunidad.actividades;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import rp3.app.BaseActivity;
import rp3.auna.R;
import rp3.auna.models.oportunidad.OportunidadTareaActividad;
import rp3.auna.oportunidad.EtapaTareasFragment;

public abstract class ActividadActivity extends BaseActivity {
	
	public static String ARG_THEME = "theme";
    public static String ARG_SIN_GRUPO = "sin_grupo";
	public static String ARG_PADRE_ID = "padre";
    public static String ARG_AGENDA_INT = "agenda_int";
	public static String ARG_TAREA = "tarea";
	public static String ARG_NUMERO = "numero";
	public static String ARG_VISTA = "vista";
	public static String ARG_TITULO = "titulo";
	
	protected int id_actividad;
	protected int id_oportunidad;
    protected long id_agenda_int;
	protected int id_etapa;
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
			getActionBar().setTitle(getIntent().getExtras().getString(ARG_TITULO, "RP3 MarketForce"));
		}
		id_actividad = getIntent().getExtras().getInt(EtapaTareasFragment.ARG_ITEM_ID, 0);
        id_oportunidad = getIntent().getExtras().getInt(EtapaTareasFragment.ARG_OPORTUNIDAD, 0);
        id_etapa = getIntent().getExtras().getInt(EtapaTareasFragment.ARG_ETAPA, 0);
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
	
	protected OportunidadTareaActividad initActividad(int idActividad)
    {
        OportunidadTareaActividad act = new OportunidadTareaActividad();
        act.setIdOportunidad(id_oportunidad);
        act.setIdTarea(id_actividad);
        act.setIdEtapa(id_etapa);
        act.setIdTareaActividad(idActividad);
        //act.set_idAgenda(id_agenda_int);
        //AgendaTareaActividades.insert(getDataBase(), act);
        return act;
    }

    protected OportunidadTareaActividad initActividadInsert(int idActividad)
    {
        OportunidadTareaActividad act = new OportunidadTareaActividad();
        act.setIdOportunidad(id_oportunidad);
        act.setIdTarea(id_actividad);
        act.setIdEtapa(id_etapa);
        act.setIdTareaActividad(idActividad);
        //act.set_idAgenda(id_agenda_int);
        OportunidadTareaActividad.insert(getDataBase(), act);
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

}
