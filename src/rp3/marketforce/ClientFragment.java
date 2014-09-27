package rp3.marketforce;

import rp3.app.BaseFragment;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

public class ClientFragment extends BaseFragment implements ClientListFragment.TransactionListFragmentListener {

	public static final String ARG_TRANSACTIONTYPEID = "transactionTypeId";
	
	private ClientListFragment transactionListFragment;
	private ClientDetailFragment transactionDetailFragment;
	
	public static boolean mTwoPane = false;
	private long selectedTransactionId;
	
//	private MenuItem menuItemActionEdit;
//    private MenuItem menuItemActionDiscard;
    
	public static ClientFragment newInstance(int transactionTypeId) {
		ClientFragment fragment = new ClientFragment();
		return fragment;
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
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
						
		if(getChildFragmentManager().findFragmentById(R.id.transaction_detail) == null){			
			if(rootView.findViewById(R.id.content_transaction_list)!=null){
				setFragment(R.id.content_transaction_list, transactionListFragment );
			}			
		}
		
		if (rootView.findViewById(R.id.content_transaction_detail) != null) {     
			mTwoPane = true;
        }
					
		if(mTwoPane){
			transactionListFragment.setActivateOnItemClick(true);			
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
	 
	private void setVisibleEditActionButtons(boolean visible){
//    	menuItemActionEdit.setVisible(visible);
//    	menuItemActionDiscard.setVisible(visible);
    }
    
//    private void clearDetailContent(){
//    	transactionDetailFragment = ClientDetailFragment.newInstance(0);
//    	
//    	setFragment(R.id.content_transaction_detail, transactionDetailFragment);    	
//    }   

	
	@Override
	public void onTransactionSelected(long id) {
		if (mTwoPane) {
           
        	selectedTransactionId = id;
        	setVisibleEditActionButtons( selectedTransactionId != 0 );
        	transactionDetailFragment = ClientDetailFragment.newInstance(selectedTransactionId); 
        	setFragment(R.id.content_transaction_detail, transactionDetailFragment);        	

        } else {
        	//waitUpdate = true;        	            
            startActivity(ClientDetailActivity.newIntent(this.getActivity(), id) );
            this.cancelAnimationTransition();
        }
	}	
	
	///End TransactionListFragmentListener
	
}
