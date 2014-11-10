package rp3.marketforce;

import rp3.marketforce.R;
import rp3.app.BaseActivity;
import rp3.marketforce.cliente.ClientDetailActivity;
import rp3.marketforce.cliente.ClientDetailFragment;
import rp3.marketforce.cliente.ClientListFragment;
import rp3.marketforce.models.Cliente;
import rp3.widget.SlidingPaneLayout;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;


public class SearchableActivity extends BaseActivity
	implements ClientListFragment.ClienteListFragmentListener {

	private boolean mTwoPane;
	private String query;
	private static final int PARALLAX_SIZE = 0;
	
//	private MenuItem menuItemActionEdit;
//    private MenuItem menuItemActionDiscard;
    private long selectedClientId;
    private Cliente client;
    
	private ClientDetailFragment clientDetailFragment;
	private ClientListFragment clientListFragment;
	private SlidingPaneLayout slidingPane;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_client);				
		
		setHomeAsUpEnabled(true, true);	
		
		slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane_clientes);
		slidingPane.setParallaxDistance(PARALLAX_SIZE);
		slidingPane.setShadowResource(R.drawable.sliding_pane_shadow);
		slidingPane.setSlidingEnabled(true);
		slidingPane.openPane();
		
		 Intent intent = getIntent();
		    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
		    	query = intent.getStringExtra(SearchManager.QUERY);
//		    	executeSearch(query);
		    }	
		
		
		if(!hasFragment(R.id.content_transaction_list)){		
			clientListFragment = ClientListFragment.newInstance(false, query);
			setFragment(R.id.content_transaction_list, clientListFragment);			
		}else{
			clientListFragment = (ClientListFragment) getFragment(R.id.content_transaction_list);
		}
		
	    if (findViewById(R.id.content_transaction_detail) != null) {
            mTwoPane = true;
            
//            ((ClientListFragment) getCurrentFragmentManager()
//                    .findFragmentById(R.id.content_transaction_list))
//                    .setActivateOnItemClick(true);
        }
	    				
	    // Get the intent, verify the action and get the query
	       	    

	}

	private void executeSearch(String query)
	{
		
//		if(hasFragment(R.id.content_transaction_list))
//		{
			clientListFragment.searchTransactions(query);
//		}else
//		{
//			Log.e("TAG","NULL");
//		}
	}
	
	@Override
	public void onClienteSelected(Cliente cliente) {
		
		selectedClientId = cliente.getID();
		client = cliente;
		
		if (mTwoPane) {      			
			clientDetailFragment = ClientDetailFragment.newInstance(cliente);
			setVisibleEditActionButtons( selectedClientId != 0 );
			
			
			getCurrentFragmentManager().beginTransaction()
            .replace(R.id.content_transaction_detail, 
            		clientDetailFragment)
            .commit();
			slidingPane.closePane();

        } else {     
//            Intent detailIntent = new Intent(this, ClientDetailActivity.class);
//            detailIntent.putExtra(ClientDetailFragment.ARG_ITEM_ID, id);
//            detailIntent.putExtra(ClientDetailFragment.ARG_PARENT_SOURCE, ClientDetailFragment.PARENT_SOURCE_SEARCH);
//            startActivity(detailIntent);
        	
        	startActivity(ClientDetailActivity.newIntent(this, selectedClientId) );
        }  
	}

	
	private void setVisibleEditActionButtons(boolean visible){
//    	menuItemActionEdit.setVisible(visible);
//    	menuItemActionDiscard.setVisible(visible);
    }
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.searchable, menu);		
		
//		menuItemActionEdit = menu.findItem(R.id.action_edit);
//		menuItemActionDiscard = menu.findItem(R.id.action_discard);	    
	    
		boolean visibleActionDetail = mTwoPane && selectedClientId != 0;
        setVisibleEditActionButtons(visibleActionDetail);
        
		MenuItem searchViewItem = menu.findItem(R.id.action_search);
	    SearchView searchView = (SearchView) searchViewItem.getActionView();
	    
	    int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
	  	 EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
	  	 searchPlate.setHintTextColor(getResources().getColor(R.color.color_hint));
	  	 searchPlate.setTextColor(getResources().getColor(R.color.apptheme_color));
	  	 searchPlate.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);

	    searchView.setIconifiedByDefault(false);
	    searchView.setQuery(query, false);
	    searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				executeSearch(query);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	    
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
        switch (item.getItemId()) {
            case android.R.id.home: 
            	if(!slidingPane.isOpen())
				{
					slidingPane.openPane();
					return true;
				}
            	else
            	{
            		this.finish();
            	}
                return true;
            case R.id.action_edit:
    			
//    			Intent intent = new Intent(this,TransactionEditActivity.class);
//    			intent.putExtra(TransactionEditActivity.EXTRA_TRANSACTIONID, selectedTransactionId);    			
//    			startActivity(intent);
    			
    			return true;
    		case R.id.action_discard:    			
//    			transactionDetailFragment.beginDelete();    			
    			return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onFinalizaConsulta() {
		if(mTwoPane && client != null){			
			onClienteSelected(client);
		}
	}
}
