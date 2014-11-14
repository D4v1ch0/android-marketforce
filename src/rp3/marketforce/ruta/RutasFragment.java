package rp3.marketforce.ruta;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;

public class RutasFragment extends BaseFragment implements RutasListFragment.TransactionListFragmentListener{

	public static final String ARG_TRANSACTIONTYPEID = "transactionTypeId";
	private static final int PARALLAX_SIZE = 0;
	
	public static boolean mTwoPane = false;
	private long selectedTransactionId;
	
//	private MenuItem menuItemActionEdit;
//    private MenuItem menuItemActionDiscard;
	
	private RutasListFragment rutasListFragment;
	private RutasDetailFragment rutasDetailfragment;
	private SlidingPaneLayout slidingPane;
    
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
		
		slidingPane = (SlidingPaneLayout) rootView.findViewById(R.id.sliding_pane_clientes);
		slidingPane.setParallaxDistance(PARALLAX_SIZE);
		slidingPane.setShadowResource(R.drawable.sliding_pane_shadow);
		slidingPane.setSlidingEnabled(false);
		slidingPane.openPane();
		slidingPane.setPanelSlideListener(new PanelSlideListener(){

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPanelOpened(View panel) {
				getActivity().invalidateOptionsMenu();
				rutasListFragment.searchTransactions("");
			}

			@Override
			public void onPanelClosed(View panel) {
				getActivity().invalidateOptionsMenu();
			}});
						
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
  	 if(!slidingPane.isOpen())
  	 {
  		 searchView.setVisibility(View.GONE);
  		 menu.removeItem(R.id.action_search_ruta);
  	 }
  	 else
  	 {
  		 searchView.setVisibility(View.VISIBLE);
  	 }
  	  
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
				if(newText.equalsIgnoreCase(""));
				try
				{
					rutasListFragment.searchTransactions("");
				}
				catch(Exception ex)
				{
					
				}
				return false;
			}
		});
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
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
            slidingPane.closePane();
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
