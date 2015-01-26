package rp3.marketforce.ruta;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.db.DbOpenHelper;
import rp3.marketforce.models.Agenda;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;

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
    
//    @Override
//	public void onDeleteSuccess(Transaction transaction) {		
////		startActivity(new Intent(this,TransactionListActivity.class));
//		finish();
//	}
    
}
