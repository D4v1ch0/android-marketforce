package rp3.marketforce.ruta;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.db.DbOpenHelper;
import rp3.marketforce.models.Agenda;
import rp3.util.Screen;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

public class RutasDetailActivity extends rp3.app.BaseActivity{

	private long transactionId;
	private final String STATE_TRANSACTIONID = "transactionId";
	private RutasDetailFragment rutasDetailFragment;
	
	public final static String EXTRA_TRANSACTIONID = "transactionId";
	
	public static Intent newIntent(Context c, long id){
		Intent intent = new Intent(c, RutasDetailActivity.class);
		intent.putExtra(EXTRA_TRANSACTIONID, id);
        return intent;
	}
	
	@Override
	protected void onResume() {
		if(rutasDetailFragment != null)
			rutasDetailFragment.onResume();
		super.onResume();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId())
    	{
    		case R.id.action_ver_posicion:   
    			if(transactionId != 0)
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
    			Intent intent3 = new Intent(this, MapaActivity.class);
    			intent3.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_RUTAS);
    			intent3.putExtra(MapaActivity.ARG_AGENDA, transactionId);
    			startActivity(intent3);
    			return true;
    		case R.id.action_como_llegar:
    			if(transactionId != 0)
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
    
//    @Override
//	public void onDeleteSuccess(Transaction transaction) {		
////		startActivity(new Intent(this,TransactionListActivity.class));
//		finish();
//	}
    
}
