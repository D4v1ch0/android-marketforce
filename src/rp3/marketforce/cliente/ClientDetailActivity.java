package rp3.marketforce.cliente;

import rp3.marketforce.MainActivity;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientDetailFragment.ClienteDetailFragmentListener;
import rp3.marketforce.db.DbOpenHelper;
import rp3.marketforce.models.Cliente;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

public class ClientDetailActivity extends rp3.app.BaseActivity implements ClienteDetailFragmentListener {

	private long transactionId;
	private final String STATE_TRANSACTIONID = "transactionId";
	private ClientDetailFragment transactionDetailFragment;
	
	public final static String EXTRA_TRANSACTIONID = "transactionId";
	
	public static Intent newIntent(Context c, long id){
		Intent intent = new Intent(c, ClientDetailActivity.class);
		intent.putExtra(EXTRA_TRANSACTIONID, id);
//		intent.putExtra(TransactionDetailFragment.ARG_PARENT_SOURCE, 
//        		TransactionDetailFragment.PARENT_SOURCE_LIST);        
        return intent;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        setDataBaseParameters(DbOpenHelper.class);               
        
        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {            
            
            transactionId = getIntent().getLongExtra(EXTRA_TRANSACTIONID,0);                                    
            transactionDetailFragment = ClientDetailFragment.newInstance(Cliente.getClienteID(getDataBase(), transactionId, false));            
            
            getCurrentFragmentManager().beginTransaction()
                    .add(R.id.content_transaction_detail, transactionDetailFragment)
                    .commit();
        }
        else{
        	transactionId = savedInstanceState.getLong(STATE_TRANSACTIONID);
        	transactionDetailFragment = (ClientDetailFragment)getCurrentFragmentManager().findFragmentById(R.id.content_transaction_detail);
        }
    }    
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    	outState.putLong(STATE_TRANSACTIONID,transactionId);    	
    }

	@Override
	public void onClienteChanged(Cliente cliente) {
		finish();		
	}    
    
//    @Override
//	public void onDeleteSuccess(Transaction transaction) {		
////		startActivity(new Intent(this,TransactionListActivity.class));
//		finish();
//	}
    
}
