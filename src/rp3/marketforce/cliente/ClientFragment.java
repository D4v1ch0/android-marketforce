package rp3.marketforce.cliente;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientDetailFragment.ClienteDetailFragmentListener;
import rp3.marketforce.cliente.ClientListFragment.ClienteListFragmentListener;
import rp3.marketforce.models.Cliente;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

public class ClientFragment extends BaseFragment implements ClienteListFragmentListener, ClienteDetailFragmentListener {

	public static final String ARG_TRANSACTIONTYPEID = "transactionTypeId";
	private static final int PARALLAX_SIZE = 500;
	
	private ClientListFragment transactionListFragment;
	private ClientDetailFragment transactionDetailFragment;
	private SlidingPaneLayout slidingPane;
	
	public static boolean mTwoPane = false;
	private long selectedClientId;	
    
	public static ClientFragment newInstance(int transactionTypeId) {
		ClientFragment fragment = new ClientFragment();
		return fragment;
    }
	
	@Override
	public void onFragmentResult(String tagName, int resultCode, Bundle data) {		
		super.onFragmentResult(tagName, resultCode, data);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setContentView(R.layout.fragment_client,R.menu.fragment_client_menu);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);		
		transactionListFragment = ClientListFragment.newInstance(true, null);					
		
	}
	
	@Override
	public void onResume() {		
		super.onResume();				
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
						
		slidingPane = (SlidingPaneLayout) rootView.findViewById(R.id.sliding_pane_clientes);
		slidingPane.setParallaxDistance(PARALLAX_SIZE);
		slidingPane.setShadowResource(R.drawable.sliding_pane_shadow);
		slidingPane.setSlidingEnabled(true);
		slidingPane.openPane();
		
		slidingPane.setPanelSlideListener(new PanelSlideListener(){

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPanelOpened(View panel) {
				
			}

			@Override
			public void onPanelClosed(View panel) {

			}});
		
		if(getChildFragmentManager().findFragmentById(R.id.transaction_detail) == null){			
			if(rootView.findViewById(R.id.content_transaction_list)!=null){
				setFragment(R.id.content_transaction_list, transactionListFragment );
			}			
		}
		
		if (rootView.findViewById(R.id.content_transaction_detail) != null) {     
			mTwoPane = true;
        }	
		else
		{
			mTwoPane = false;
		}
		
	}	
	
	@Override
	public void onAfterCreateOptionsMenu(Menu menu) {	
		
      SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
      //SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
  	  SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
      // Assumes current activity is the searchable activity
  	  
  	 int searchicon = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
  	 ImageView searchIcon = (ImageView)searchView.findViewById(searchicon);
     searchIcon.setImageResource(R.drawable.ic_action_search);
  	 int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
  	 EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
  	 searchPlate.setHintTextColor(getResources().getColor(R.color.color_hint));
  	 searchPlate.setTextColor(getResources().getColor(R.color.apptheme_color));
  	 searchPlate.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);
  	
     if(null!=searchManager ) {   
          searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
      }
      searchView.setIconifiedByDefault(false);
//      
//      menuItemActionEdit = menu.findItem(R.id.action_edit);
//      menuItemActionDiscard = menu.findItem(R.id.action_discard);
//      boolean visibleActionDetail = mTwoPane && selectedTransactionId != 0;
//      setVisibleEditActionButtons(visibleActionDetail);
	}
	 
	@Override
	public void onClienteSelected(long id) {
		if (mTwoPane) {
			slidingPane.closePane();
			selectedClientId = id;        	
        	transactionDetailFragment = ClientDetailFragment.newInstance(selectedClientId); 
        	setFragment(R.id.content_transaction_detail, transactionDetailFragment);
        	

        } else {      	            
            startActivity(ClientDetailActivity.newIntent(this.getActivity(), id) );
            this.cancelAnimationTransition();
        }
	}
	
	

	@Override
	public void onClienteChanged(Cliente cliente) {
		transactionListFragment.actualizarCliente(cliente);
		onClienteSelected(selectedClientId);
	}

	@Override
	public void onFinalizaConsulta() {		
	}	
	
}
