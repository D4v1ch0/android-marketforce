package rp3.auna.ruta;

import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.db.DbOpenHelper;
import rp3.auna.models.Agenda;
import rp3.auna.resumen.AgenteDetalleFragment;
import rp3.auna.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class RutasDetailActivity extends rp3.app.BaseActivity implements ContactsAgendaFragment.SaveContactsListener{

	private static final String TAG = RutasDetailActivity.class.getSimpleName();
	private long transactionId;
	private final String STATE_TRANSACTIONID = "transactionId";
	private RutasDetailFragment rutasDetailFragment;
	
	public final static String EXTRA_TRANSACTIONID = "transactionId";

	public SimpleDateFormat format1 = new SimpleDateFormat("EEEE");
	public SimpleDateFormat format2 = new SimpleDateFormat("dd");
	public SimpleDateFormat format3 = new SimpleDateFormat("MMMM");
	
	public static Intent newIntent(Context c, long id){
		Intent intent = new Intent(c, RutasDetailActivity.class);
		intent.putExtra(EXTRA_TRANSACTIONID, id);
        return intent;
	}
	
	@Override
	protected void onResume() {
		Log.d(TAG,"onResume...");
		if(rutasDetailFragment != null)
			rutasDetailFragment.onResume();
		super.onResume();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
        setContentView(R.layout.activity_transaction_detail, R.menu.fragment_ruta_menu);
        setDataBaseParameters(DbOpenHelper.class);               
        
        setHomeAsUpEnabled(true, true);
        setTitle("Agenda");

        if (savedInstanceState == null) {            
            
            transactionId = getIntent().getLongExtra(EXTRA_TRANSACTIONID,0);                                    
            rutasDetailFragment = RutasDetailFragment.newInstance(transactionId);            
            
            getCurrentFragmentManager().beginTransaction()
                    .add(R.id.content_transaction_detail, rutasDetailFragment)
                    .commit();
        }
        else{
        	transactionId = savedInstanceState.getLong(STATE_TRANSACTIONID);
        	rutasDetailFragment = (RutasDetailFragment)getCurrentFragmentManager().findFragmentById(R.id.content_transaction_detail);
        }
    }    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putLong(STATE_TRANSACTIONID,transactionId);
		Log.d(TAG,"onSaveInstanceState...");
    }    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId())
    	{
			/*case R.id.action_asignar_pedido:
				Agenda agdPed = Agenda.getAgenda(getDataBase(), transactionId);
				Intent intent5 = new Intent(this, CrearPedidoActivity.class);
				intent5.putExtra(CrearPedidoActivity.ARG_IDAGENDA, transactionId);
				intent5.putExtra(CrearPedidoActivity.ARG_IDPEDIDO, agdPed.getPedido().getID());
				startActivity(intent5);
				return true;*/
    		case R.id.action_ver_posicion:
                if(!ConnectionUtils.isNetAvailable(this))
                {
                    Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                }
                else if(transactionId != 0)
    			{
	    			Intent intent2 = new Intent(this, MapaActivity.class);
	    			intent2.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_POSICION);
	    			intent2.putExtra(MapaActivity.ARG_AGENDA, transactionId);
	    			startActivity(intent2);
    			}
    			else
    			{
    				Toast.makeText(this, "Debe seleccionar una agenda.", Toast.LENGTH_LONG).show();
    			}
    			return true;
    		case R.id.action_ver_ruta:
                if(!ConnectionUtils.isNetAvailable(this))
                {
                    Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent3 = new Intent(this, MapaActivity.class);
                    intent3.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_RUTAS);
                    intent3.putExtra(MapaActivity.ARG_AGENDA, transactionId);
                    startActivity(intent3);
                }
    			return true;
    		case R.id.action_como_llegar:
                if(!ConnectionUtils.isNetAvailable(this))
                {
                    Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                }
                else if(transactionId != 0)
    			{
	    			Intent intent4 = new Intent(this, MapaActivity.class);
	    			intent4.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_LLEGAR);
	    			intent4.putExtra(MapaActivity.ARG_AGENDA, transactionId);
	    			startActivity(intent4);
    			}
    			else
    			{
    				Toast.makeText(this, R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
    			}
    			return true;
			case R.id.action_suspender_agenda:
				if(transactionId != 0)
				{
					if(PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR, 0) != 0)
					{
						Agenda agdNot = Agenda.getAgenda(getDataBase(), transactionId);
						if(agdNot == null)
							agdNot = Agenda.getAgendaClienteNull(getDataBase(), transactionId);
						Bundle bundle = new Bundle();
						bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION);
						bundle.putInt(AgenteDetalleFragment.ARG_AGENTE, PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR));
						bundle.putString(AgenteDetalleFragment.ARG_TITLE, "Anular Agenda");
						bundle.putString(AgenteDetalleFragment.ARG_MESSAGE,
								"Se solicita anulación de agenda del " + format1.format(agdNot.getFechaInicio()) + ", " + format2.format(agdNot.getFechaInicio()) + " de "
										+ format3.format(agdNot.getFechaInicio()) + ", hecha al cliente " + agdNot.getNombreCompleto());
						requestSync(bundle);
						Toast.makeText(this, R.string.message_notificacion_enviada, Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(this, R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
				}
				return true;
    		case R.id.action_cambiar_contacto:
    			if(transactionId != 0)
    			{
    				if(!Agenda.getAgendaEstado(getDataBase(), transactionId).equalsIgnoreCase(Contants.ESTADO_NO_VISITADO))
    					this.showDialogFragment(ContactsAgendaFragment.newInstance(transactionId), ContactsAgendaFragment.TAG);
    				else
    					Toast.makeText(this, R.string.warning_modificar_contacto_no_visitada, Toast.LENGTH_LONG).show();
    			}
    			else
    			{
    				Toast.makeText(this, R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
    			}
    			return true;
    		case R.id.action_no_visita:
    			if(transactionId != 0)
    			{
    				if(Agenda.getAgendaEstado(getDataBase(), transactionId).equalsIgnoreCase(Contants.ESTADO_PENDIENTE) ||
    				   Agenda.getAgendaEstado(getDataBase(), transactionId).equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
    					this.showDialogFragment(MotivoNoVisitaFragment.newInstance(transactionId), MotivoNoVisitaFragment.TAG);
    			}
    			else
    			{
    				Toast.makeText(this, R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
    			}
    			return true;
			case R.id.action_crear_agenda:
				if(transactionId != 0) {
					Intent intent8 = new Intent(this, CrearVisitaActivity.class);
					intent8.putExtra(CrearVisitaFragment.ARG_IDAGENDA, (int) transactionId);
					intent8.putExtra(CrearVisitaFragment.ARG_FROM, "Agenda");
					startActivity(intent8);
				}
				else
				{
					Toast.makeText(this, R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
				}
				return true;
    		case R.id.action_reprogramar:
    			if(transactionId != 0)
    			{
    				if(Agenda.getAgendaEstado(getDataBase(), transactionId).equalsIgnoreCase(Contants.ESTADO_PENDIENTE) ||
    				   Agenda.getAgendaEstado(getDataBase(), transactionId).equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
    				{
    					Intent reprogramar = new Intent(this, ReprogramarActivity.class);
    					reprogramar.putExtra(ReprogramarActivity.ARG_AGENDA, transactionId);
            			startActivity(reprogramar);
    				}
    			}
    			else
    			{
    				Toast.makeText(this, R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
    			}
    	}
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public void Refresh() {
		rutasDetailFragment.onResume();
		Log.d(TAG,"refresh...");
	}

	/**
	 *
	 * Ciclo de vida
	 *
	 */

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart...");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}

//    @Override
//	public void onDeleteSuccess(Transaction transaction) {		
////		startActivity(new Intent(this,TransactionListActivity.class));
//		finish();
//	}
    
}
