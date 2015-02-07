package rp3.marketforce.cliente;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientDetailFragment.ClienteDetailFragmentListener;
import rp3.marketforce.cliente.ClientListFragment.ClienteListFragmentListener;
import rp3.marketforce.cliente.ClientListFragment.LoaderCliente;
import rp3.marketforce.models.Cliente;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;
import android.R.anim;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

public class ClientFragment extends BaseFragment implements ClienteListFragmentListener, ClienteDetailFragmentListener {
	
	private static final int PARALLAX_SIZE = 0;
	
	private ClientListFragment transactionListFragment;
	private ClientDetailFragment transactionDetailFragment;
	private SlidingPaneLayout slidingPane;
	
	public boolean mTwoPane = false;
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
		slidingPane.setSlidingEnabled(false);										
		slidingPane.openPane();
		
		if(slidingPane.isOpen() && 
				rootView.findViewById(R.id.content_transaction_list).getLayoutParams().width != LayoutParams.MATCH_PARENT)		
			mTwoPane = true;			
		else
			mTwoPane = false;				
					
		if(!hasFragment(R.id.content_transaction_list))
			setFragment(R.id.content_transaction_list, transactionListFragment );
		
		slidingPane.setPanelSlideListener(new PanelSlideListener(){

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPanelOpened(View panel) {
				getActivity().invalidateOptionsMenu();
				if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2) {
					getActivity().getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
				}
				
			}

			@Override
			public void onPanelClosed(View panel) {
				getActivity().invalidateOptionsMenu();
				getActivity().getActionBar().setHomeButtonEnabled(false);
				getActivity().getActionBar().setHomeButtonEnabled(true);
			}});
		
//		if(getChildFragmentManager().findFragmentById(R.id.transaction_detail) == null){			
//			if(rootView.findViewById(R.id.content_transaction_list)!=null){
//				
//			}			
//		}		
	}	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		if(selectedClientId != 0){
			if(!mTwoPane)			
				slidingPane.closePane();			
		}
	}
	
	@Override
	public void onAfterCreateOptionsMenu(Menu menu) {	
		
      SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
      //SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
  	  SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
  	  if(slidingPane!=null){
	  	  if(!slidingPane.isOpen())
	 	 {
	 		 searchView.setVisibility(View.GONE);
	 		 menu.removeItem(R.id.action_search);
	 		 menu.removeItem(R.id.action_crear_cliente);
	 	 }
	 	 else
	 	 {
	 		 searchView.setVisibility(View.VISIBLE);
	 	 }
  	  }
  	  
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

	}
	 
	@Override
	public void onClienteSelected(Cliente cl) {
		selectedClientId = cl.getID();
		
		if(!mTwoPane)
			slidingPane.closePane();
		
		transactionDetailFragment = ClientDetailFragment.newInstance(cl);
		setFragment(R.id.content_transaction_detail, transactionDetailFragment);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.action_crear_cliente:
			Intent intent = new Intent(this.getActivity(), CrearClienteActivity.class);
			startActivity(intent);
			break;
		case R.id.action_import_contacts:
			showDialogFragment(ImportChooseFragment.newInstance(), "Import");
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean allowSelectedItem() {		
		return mTwoPane;
	}

	@Override
	public void onClienteChanged(Cliente cliente) {
		transactionListFragment.actualizarCliente(cliente);
		onClienteSelected(cliente);
	}

	@Override
	public void onFinalizaConsulta() {		
	}	
	
}
