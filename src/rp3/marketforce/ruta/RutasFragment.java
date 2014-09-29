package rp3.marketforce.ruta;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.R.color;
import rp3.marketforce.R.drawable;
import rp3.marketforce.R.id;
import rp3.marketforce.R.layout;
import rp3.marketforce.R.menu;
import rp3.marketforce.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class RutasFragment extends BaseFragment implements RutasListFragment.TransactionListFragmentListener{

	public static final String ARG_TRANSACTIONTYPEID = "transactionTypeId";
	
	private boolean mTwoPane = false;
	private long selectedTransactionId;
	
//	private MenuItem menuItemActionEdit;
//    private MenuItem menuItemActionDiscard;
	
	private RutasListFragment rutasListFragment;
	private RutasDetailFragment rutasDetailfragment;
    
	public static RutasFragment newInstance(int transactionTypeId) {
		RutasFragment fragment = new RutasFragment();
		return fragment;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
//		setContentView(R.layout.fragment_client,R.menu.fragment_client);
		setContentView(R.layout.fragment_rutas, R.menu.fragment_ruta_menu);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);		
		
//		setContentView(R.layout.activity_transaction_twopane,R.menu.fragment_client);
				
		rutasListFragment = RutasListFragment.newInstance();					
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
						
		if(getChildFragmentManager().findFragmentById(R.id.transaction_detail) == null){			
			if(rootView.findViewById(R.id.content_transaction_list)!=null){
				setFragment(R.id.content_transaction_list, rutasListFragment );
			}			
		}
		
		if (rootView.findViewById(R.id.content_transaction_detail) != null) {     
			mTwoPane = true;
        }
					
//		if(mTwoPane){
//			rutasListFragment.setActivateOnItemClick(true);			
//		}
	}	
	
	@Override
	public void onAfterCreateOptionsMenu(Menu menu) {	
		
  	 SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_ruta));
  	  
  	 int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
  	 EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
  	 searchPlate.setHintTextColor(getResources().getColor(R.color.color_hint));
  	searchPlate.setHint(getActivity().getResources().getString(R.string.hint_search_transaction_rutas));
  	 searchPlate.setTextColor(getResources().getColor(R.color.apptheme_color));
  	 searchPlate.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
  	 
	 searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				if(rutasListFragment != null)
					rutasListFragment.searchTransactions(query);
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch(item.getItemId())
	    	{
	    		case R.id.action_search_ruta:    	
	    			return true;
	    	}
	    	return super.onOptionsItemSelected(item);
	    }
		
	@Override
	public void onTransactionSelected(long id) {
		if (mTwoPane) {
           
        	selectedTransactionId = id;
        	rutasDetailfragment = RutasDetailFragment.newInstance(selectedTransactionId); 
        	
        	setFragment(R.id.content_transaction_detail, rutasDetailfragment);        	

        } else {
        	//waitUpdate = true;        	            
            startActivity(RutasDetailActivity.newIntent(this.getActivity(), id) );
            this.cancelAnimationTransition();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	///End TransactionListFragmentListener
	
}
