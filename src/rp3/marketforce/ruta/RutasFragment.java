package rp3.marketforce.ruta;

import java.util.ArrayList;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class RutasFragment extends BaseFragment implements RutasListFragment.TransactionListFragmentListener{

	public static final String ARG_TRANSACTIONTYPEID = "transactionTypeId";
	private static final int PARALLAX_SIZE = 0;
	
	public boolean mTwoPane = false;
	private long selectedTransactionId;
	private String textSearch;
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
			
		rutasListFragment = RutasListFragment.newInstance();					
	}
	
	@Override
	public void onStart() {		
		super.onStart();
		
		if(selectedTransactionId != 0){
			if(!mTwoPane)			
				slidingPane.closePane();			
		}		
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
			}

			@Override
			public void onPanelOpened(View panel) {
				getActivity().invalidateOptionsMenu();
				//rutasListFragment.searchTransactions("");
			}

			@Override
			public void onPanelClosed(View panel) {
				getActivity().invalidateOptionsMenu();
			}});
		
		if(!hasFragment(R.id.content_transaction_list))		
			setFragment(R.id.content_transaction_list, rutasListFragment );	
		
		if(slidingPane.isOpen() && 
				rootView.findViewById(R.id.content_transaction_list).getLayoutParams().width != LayoutParams.MATCH_PARENT)		
			mTwoPane = true;			
		else
			mTwoPane = false;					
	}	
	
	@SuppressLint("NewApi")
	@Override
	public void onAfterCreateOptionsMenu(Menu menu) {	
		
		final String overflowDesc = "overflow";
        final ViewGroup decor = (ViewGroup) getActivity().getWindow().getDecorView();
        decor.postDelayed(new Runnable() {

            @Override
            public void run() {
                // The List that contains the matching views
                final ArrayList<View> outViews = new ArrayList<View>();
                // Traverse the view-hierarchy and locate the overflow button
                decor.findViewsWithText(outViews, overflowDesc,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                // Guard against any errors
                if (outViews.isEmpty()) {
                    return;
                }
                // Do something with the view
                final ImageButton overflow = (ImageButton) outViews.get(0);
                overflow.setImageResource(R.drawable.ic_rutas);

            }

        }, 500);
		
  	 SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_ruta));
  	 if(slidingPane!=null){
	  	 if(!slidingPane.isOpen())
	  	 {
	  		 searchView.setVisibility(View.GONE);
	  		 menu.removeItem(R.id.action_search_ruta);
	  		 menu.removeItem(R.id.action_crear_visita);
	  	 }
	  	 else
	  	 {
	  		 searchView.setVisibility(View.VISIBLE);
	  		 menu.removeItem(R.id.action_como_llegar);
	  		 menu.removeItem(R.id.action_ver_posicion);
	  	 }
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
				if(TextUtils.isEmpty(newText) && !TextUtils.isEmpty(textSearch)){
					try
					{
						rutasListFragment.searchTransactions("");
					}
					catch(Exception ex)
					{
						
					}
				}
				textSearch = newText;
				
				return true;
				
			}
		});
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {		
		return super.onContextItemSelected(item);
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch(item.getItemId())
	    	{
	    		case R.id.action_search_ruta:    	
	    			return true;
	    		case R.id.action_crear_visita:
	    			Intent intent = new Intent(getActivity(), CrearVisitaActivity.class);
	    			startActivity(intent);
	    			return true;
	    		case R.id.action_ver_posicion:   
	    			Intent intent2 = new Intent(getActivity(), MapaActivity.class);
	    			intent2.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_POSICION);
	    			intent2.putExtra(MapaActivity.ARG_AGENDA, selectedTransactionId);
	    			startActivity(intent2);
	    			return true;
	    		case R.id.action_ver_ruta:
	    			Intent intent3 = new Intent(getActivity(), MapaActivity.class);
	    			intent3.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_RUTAS);
	    			startActivity(intent3);
	    			return true;
	    		case R.id.action_como_llegar:
	    			Intent intent4 = new Intent(getActivity(), MapaActivity.class);
	    			intent4.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_LLEGAR);
	    			intent4.putExtra(MapaActivity.ARG_AGENDA, selectedTransactionId);
	    			startActivity(intent4);
	    			return true;
	    	}
	    	return super.onOptionsItemSelected(item);
	    }
		
	@Override
	public void onTransactionSelected(long id) {
		if(!mTwoPane)
			slidingPane.closePane();
		
		selectedTransactionId = id;
    	rutasDetailfragment = RutasDetailFragment.newInstance(selectedTransactionId);     	
    	setFragment(R.id.content_transaction_detail, rutasDetailfragment);           					
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	@Override
	public boolean allowSelectedItem() {		
		return mTwoPane;
	}	
	
}
