package rp3.marketforce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rp3.data.MessageCollection;
import rp3.marketforce.headerlistview.HeaderListView;
import rp3.marketforce.loader.ClientLoader;
import rp3.marketforce.models.Cliente;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class ClientListFragment extends rp3.app.BaseFragment {
		    
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    
    public static final String ARG_TRANSACTIONTYPEID = "transactionType";
    public static final String ARG_TRANSACTIONTYPEBO = "transactionTypeBo";
    
//    private static final int LOADER_MODE_SEARCH_TEXT = 1;
    private static final int LOADER_MODE_SEARCH_TRANSACTIONTYPE = 2;
    
    private static final String LOADER_ARG_SEARCH_MODE = "LOADER_ARG_SEARCH_MODE";
//    private static final String LOADER_ARG_SEARCH_TEXT = "LOADER_ARG_SEARCH_TEXT";
    private static final String LOADER_ARG_SEARCH_TRANSACTIONTYPEID = "LOADER_ARG_SEARCH_TRANSACTIONTYPEID";
    
    private static final int HEADERLIST_ID = 44;
    public static final int ORDER_BY_NAME = 5;
    public static final int ORDER_BY_LAST_NAME = 6;
    
    TransactionListFragmentListener transactionListFragmentCallback;
    private ClientListAdapter adapter;  
    private boolean currentTransactionBoolean;
    private String currentTransactionSearch;
    private HeaderListView headerList;
    public static List<String> headersortList;
    private static int id_select;
    public static int itemList_click_section = -1;
    public static int itemList_click_row = -1;
    public static int itemClientID = -1;
    public LinearLayout linearLayout_rootParent;
    
    private LoaderCliente loaderCliente;
    
    private Menu menu;
    
    private List<rp3.marketforce.models.Cliente> lista;
    private ArrayList<ArrayList<rp3.marketforce.models.Cliente>> list_order;
    
    public static ClientListFragment newInstance(boolean flag , String transactionTypeId) {
    	ClientListFragment fragment = new ClientListFragment();
		Bundle args = new Bundle();
		args.putString(ARG_TRANSACTIONTYPEID, transactionTypeId);
		args.putBoolean(ARG_TRANSACTIONTYPEBO, flag);
		fragment.setArguments(args);
		return fragment;
    }
    
    public interface TransactionListFragmentListener {
        public void onTransactionSelected(long id);
    }

    public ClientListFragment() {
    }
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        if(getParentFragment()!=null){        	
        	transactionListFragmentCallback = (TransactionListFragmentListener)getParentFragment();
        }else{
        	transactionListFragmentCallback = (TransactionListFragmentListener) activity;
        }
        
//        // Activities containing this fragment must implement its callbacks.
//        if (!(activity instanceof Callbacks)) {
//            throw new IllegalStateException("Activity must implement fragment's callbacks.");
//        }
//
//        mCallbacks = (Callbacks) activity;
        
        
//        headerList.getListView().setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
//        headerList.setId(HEADERLIST_ID);
//          lista = rp3.marketforce.models.Cliente.getCliente(getDataBase(),false);
    }   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        
        if(savedInstanceState == null)
        {
        	currentTransactionBoolean = getArguments().getBoolean(ARG_TRANSACTIONTYPEBO);
		    currentTransactionSearch = getArguments().getString(ARG_TRANSACTIONTYPEID);
		     
//		     super.setContentView(R.layout.layout_headerlist_client_list,R.menu.fragment_client_menu);
		     super.setContentView(R.layout.layout_headerlist_client_list);
		   
		     id_select = R.id.item_order_name;
		     
//		     DisplayMetrics metrics = getResources().getDisplayMetrics();
//			 int densityDpi = (int)(metrics.density * 160f);
//			Log.e("DPI",""+densityDpi);
//		     
//		     switch (getResources().getDisplayMetrics().densityDpi) {
//		          case DisplayMetrics.DENSITY_LOW:
//		        	  Log.e("Densidad","DENSITY_LOW");
//		                     break;
//		          case DisplayMetrics.DENSITY_MEDIUM:
//		        	  Log.e("Densidad","DENSITY_MEDIUM");
//		                      break;
//		          case DisplayMetrics.DENSITY_HIGH:
//		        	  Log.e("Densidad","DENSITY_HIGH");
//		                      break;
//		          case DisplayMetrics.DENSITY_XHIGH:
//		        	  Log.e("Densidad","DENSITY_XHIGH");
//		                      break;
//		          case DisplayMetrics.DENSITY_XXHIGH:
//		        	  Log.e("Densidad","DENSITY_XXHIGH");
//		                      break;
//		     }
		     
		     
		     loaderCliente = new LoaderCliente();
		     
		     if(currentTransactionBoolean)
		     {
		    	 Bundle args = new Bundle();
				 args.putString(LoaderCliente.STRING_SEARCH, "");
				 args.putBoolean(LoaderCliente.STRING_BOOLEAN, true);
			     getLoaderManager().initLoader(0, args, loaderCliente);
		    	 
		     }
		     else{
			     Bundle args = new Bundle();
				 args.putString(LoaderCliente.STRING_SEARCH, currentTransactionSearch);
				 args.putBoolean(LoaderCliente.STRING_BOOLEAN, false);
			     getLoaderManager().initLoader(0, args, loaderCliente);
		     }
		     
        }
    }
    
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	
    	linearLayout_rootParent = (LinearLayout) rootView.findViewById(R.id.linearLayout_headerlist_client_list);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {    	
    	super.onActivityCreated(savedInstanceState);
    	
//    	Bundle b = new Bundle();
//    	b.putInt(LOADER_ARG_SEARCH_MODE, LOADER_MODE_SEARCH_TRANSACTIONTYPE);
//    	b.putInt(LOADER_ARG_SEARCH_TRANSACTIONTYPEID, currentTransactionTypeId);
    	
//    	executeLoader(0, b, this);
    }
    
    @Override
    public void onStart() {    	
    	super.onStart();    	    	
    }
         
    
    public void searchTransactions(String termSearch)
    {
//    	Bundle b = new Bundle();
//    	b.putInt(LOADER_ARG_SEARCH_MODE, LOADER_MODE_SEARCH_TEXT);
//    	b.putString(LOADER_ARG_SEARCH_TEXT, termSearch);
        Bundle args = new Bundle();
		args.putString(LoaderCliente.STRING_SEARCH, termSearch);
		args.putBoolean(LoaderCliente.STRING_BOOLEAN, false);
	    getLoaderManager().restartLoader(0, args, loaderCliente);
    	
//    	executeLoader(0, b, this);
    }      
    
    public void loadTransactions(int transactionType)
    {
    	Bundle b = new Bundle();
    	b.putInt(LOADER_ARG_SEARCH_MODE, LOADER_MODE_SEARCH_TRANSACTIONTYPE);
    	b.putInt(LOADER_ARG_SEARCH_TRANSACTIONTYPEID, transactionType);
//    	executeLoader(0, b, this);
    }
    
	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
//            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        
//        	OrderBy(ORDER_BY_NAME);
        	
    }
   
//    @Override
//    public void onListItemClick(ListView listView, View view, int position, long id) {
//        super.onListItemClick(listView, view, position, id);
//
//        transactionListFragmentCallback.onTransactionSelected(id);        
//    }
	
//	private void mTrasaction()
//	{
//		ClientListFragment.transactionListFragmentCallback.onTransactionSelected(itemClientID);
//	}
//    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if (mActivatedPosition != ListView.INVALID_POSITION) {
//            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);   
//        }
        
    }
    
//    @Override
//    public void onResume() {
//    	super.onResume();
//    	
//    	if(ClientFragment. mTwoPane)
//    	{
//	    	if(adapter != null)
//	    	{
//	        	adapter.notifyDataSetChanged();
//	        	mTrasaction();
//	    	}
//    	}
//    	
//    }
    
    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
//        getListView().setChoiceMode(activateOnItemClick
//                ? ListView.CHOICE_MODE_SINGLE
//                : ListView.CHOICE_MODE_NONE);
    }

//    private void setActivatedPosition(int position) {
//        	    
//        if (position == ListView.INVALID_POSITION) {
//            getListView().setItemChecked(mActivatedPosition, false);
//        } else {
//            getListView().setItemChecked(position, true);
//        }
//
//        mActivatedPosition = position;
//    }
          
//    public void clearActivatedPosition()
//    {    	
//    	getListView().setItemChecked(ListView.INVALID_POSITION, true);    	
//    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
		super.onCreateLoader(id, args);
		
//		int mode = args.getInt(LOADER_ARG_SEARCH_MODE);								
//		
//		SimpleCursorLoader loader;
//		if(mode == LOADER_MODE_SEARCH_TEXT){
//			final String termSearch = args.getString(LOADER_ARG_SEARCH_TEXT);
//			loader = new SimpleCursorLoader(this.getActivity()){
//				@Override
//				public Cursor loadInBackground() {																	
//					return Cliente.g(getDataBase(), termSearch);
//				}
//			};
//		}
//		else{
//			final int transactionTypeId = args.getInt(LOADER_ARG_SEARCH_TRANSACTIONTYPEID);
//			loader = new SimpleCursorLoader(this.getActivity()){
//				@Override
//				public Cursor loadInBackground() {								
//					return rp3.agendacomercial.models.Cliente.getCliente(getDataBase(), "");	
//					}									
//			};				
//		}
//		
//		return loader;
//		Log.e("Se ejecuto ","loader");
//		lista = rp3.agendacomercial.models.Cliente.getCliente(getDataBase(), "");
		
		return null;
	}

//	@Override
//	public void onLoadFinished(Loader<Cursor> loader, Cursor c) {		
//		super.onLoadFinished(loader, c);
////		adapter.swapCursor(c);
//		notifyListChanged();
//	}

//	@Override
//	public void onLoaderReset(Loader<Cursor> loader) {		
////		adapter.swapCursor(null);
//		notifyListChanged();
//	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    this.menu = menu;
//	    id_select = R.id.item_order_name;
//	    menu.findItem(R.id.item_order_name).setChecked(true);
	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	
		 menu.findItem(R.id.item_order_name).setChecked(false);
		 menu.findItem(R.id.item_order_last_name).setChecked(false);
     	 
     	  menu.findItem(id_select).setChecked(true);
		 
     	switch(item.getItemId())
	    	{
	    		case R.id.item_order_name:
	    			
	    			id_select = R.id.item_order_name;
	    			
	    		   OrderBy(ORDER_BY_NAME);
		    			
	                return true;
	                
	    		case R.id.item_order_last_name:
	    			
	    			id_select = R.id.item_order_last_name;
	    				
	    			OrderBy(ORDER_BY_LAST_NAME);
	    				
	                return true;
	    	}
	    	
	    	return super.onOptionsItemSelected(item);
	    }
	 
	@Override
	public void onSyncComplete(Bundle data, MessageCollection messages) {		
		super.onSyncComplete(data, messages);
		
		closeDialogProgress();
		if(messages.hasErrorMessage()){
			showDialogMessage(messages);
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private void OrderBy(int option)
	{
		if(lista == null)
			return;
		
		if(lista.size() == 0)
			return;
		
		if(headerList == null)
		{
			headerList = new HeaderListView(getActivity());
			linearLayout_rootParent.addView(headerList);
	    	headerList.getListView().setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
	    	headerList.setId(HEADERLIST_ID);
    	}
		
		headersortList = new ArrayList<String>(); 
		
		list_order = new ArrayList<ArrayList<rp3.marketforce.models.Cliente>>();  
		
		
		switch (option) {
		case ORDER_BY_NAME:
			
			for(int x = 0 ; x < lista.size() ; x++)
				if(!headersortList.contains(""+lista.get(x).getNombre1().charAt(0)))
					headersortList.add(""+lista.get(x).getNombre1().charAt(0));
			
			Collections.sort(headersortList);
			
			for(int x = 0 ; x < headersortList.size() ; x++)
			{
				ArrayList<rp3.marketforce.models.Cliente> list_aux = new ArrayList<rp3.marketforce.models.Cliente>();
				
				for(int y = 0 ; y < lista.size() ; y++)
				{
					if(headersortList.get(x).equals(""+lista.get(y).getNombre1().charAt(0)))
					{
						rp3.marketforce.models.Cliente cliente = new rp3.marketforce.models.Cliente();
						
						cliente.setID(lista.get(y).getID());						
						cliente.setNombre1(lista.get(y).getNombre1());
						cliente.setNombre2(lista.get(y).getNombre2());
						cliente.setApellido1(lista.get(y).getApellido1());
						cliente.setApellido2(lista.get(y).getApellido2());
						cliente.setFecIng(lista.get(y).getFecIng());
						cliente.setCorreoElectronico(lista.get(y).getCorreoElectronico());
						
						if(lista.get(y).getClienteDireccionesIDPrincipal() != null)
						   cliente.setClienteDireccionesIDPrincipal(lista.get(y).getClienteDireccionesIDPrincipal());
						
						list_aux.add(cliente);
					}
				}
				
				list_order.add(list_aux);
			}
				adapter = new ClientListAdapter(this.getActivity(), list_order, headersortList, ORDER_BY_NAME, transactionListFragmentCallback);
				headerList.setAdapter(adapter);
			
//			ORDER_IDENTIFICATOR	= ORDER_BY_NAME;
			
		break;
		case ORDER_BY_LAST_NAME:
			
			for(int x = 0 ; x < lista.size() ; x++)
				if(!headersortList.contains(""+lista.get(x).getApellido1().charAt(0)))
					headersortList.add(""+lista.get(x).getApellido1().charAt(0));
			
			Collections.sort(headersortList);
			
			for(int x = 0 ; x < headersortList.size() ; x++)
			{
				ArrayList<rp3.marketforce.models.Cliente> list_aux = new ArrayList<rp3.marketforce.models.Cliente>();
				
				for(int y = 0 ; y < lista.size() ; y++)
				{
					if(headersortList.get(x).equals(""+lista.get(y).getApellido1().charAt(0)))
					{
						rp3.marketforce.models.Cliente cliente = new rp3.marketforce.models.Cliente();
						
						cliente.setID(lista.get(y).getID());						
						cliente.setNombre1(lista.get(y).getNombre1());
						cliente.setNombre2(lista.get(y).getNombre2());
						cliente.setApellido1(lista.get(y).getApellido1());
						cliente.setApellido2(lista.get(y).getApellido2());
						cliente.setFecIng(lista.get(y).getFecIng());
						cliente.setCorreoElectronico(lista.get(y).getCorreoElectronico());
						
						if(lista.get(y).getClienteDireccionesIDPrincipal() != null)
							   cliente.setClienteDireccionesIDPrincipal(lista.get(y).getClienteDireccionesIDPrincipal());
						
						list_aux.add(cliente);
					}
				}
				
				list_order.add(list_aux);
			}
				adapter = new ClientListAdapter(this.getActivity(), list_order, headersortList, ORDER_BY_LAST_NAME, transactionListFragmentCallback);
				headerList.setAdapter(adapter);
//			ORDER_IDENTIFICATOR	= ORDER_BY_LAST_NAME;
			
			break;
		}
	}


    public class LoaderCliente implements LoaderCallbacks<List<Cliente>>{
    	
    	public static final String STRING_SEARCH = "string_search";
    	public static final String STRING_BOOLEAN = "string_boolean";
    	private String Search;
    	private boolean flag;

    	@Override
		public Loader<List<Cliente>> onCreateLoader(int arg0,
				Bundle bundle) {				

    		Search = bundle.getString(STRING_SEARCH);
    		flag = bundle.getBoolean(STRING_BOOLEAN);
    		
			return new ClientLoader(getActivity(), getDataBase(), flag, Search);
    		
		}

		@Override
		public void onLoadFinished(Loader<List<Cliente>> arg0,
				List<Cliente> data)
		{								
			lista = data;
			OrderBy(ORDER_BY_NAME);
		}

		@Override
		public void onLoaderReset(Loader<List<Cliente>> arg0) {	
			
		}
	}

	
}
