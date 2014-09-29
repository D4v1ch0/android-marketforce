package rp3.marketforce.cliente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.headerlistview.HeaderListView;
import rp3.marketforce.loader.ClientLoader;
import rp3.marketforce.models.Cliente;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class ClientListFragment extends rp3.app.BaseFragment {
		        
    
    public static final String ARG_TRANSACTIONTYPEID = "transactionType";
    public static final String ARG_TRANSACTIONTYPEBO = "transactionTypeBo";    
    
    private static final int HEADERLIST_ID = 44;
    public static final int ORDER_BY_NAME = 5;
    public static final int ORDER_BY_LAST_NAME = 6;
    
    ClienteListFragmentListener clienteListFragmentCallback;
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
    
    public interface ClienteListFragmentListener {
        public void onClienteSelected(long id);
        public void onFinalizaConsulta();
    }

    public ClientListFragment() {
    }
    
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        if(getParentFragment()!=null){        	
        	clienteListFragmentCallback = (ClienteListFragmentListener)getParentFragment();
        }else{
        	clienteListFragmentCallback = (ClienteListFragmentListener) activity;
        }        
    }   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null)
        {
        	currentTransactionBoolean = getArguments().getBoolean(ARG_TRANSACTIONTYPEBO);
		    currentTransactionSearch = getArguments().getString(ARG_TRANSACTIONTYPEID);
		     
		     super.setContentView(R.layout.layout_headerlist_client_list);
		   
		     id_select = R.id.item_order_name;
		     
		     loaderCliente = new LoaderCliente();
		     
		     if(currentTransactionBoolean){		    	 
		    	 ejecutarConsulta();
		     }
		     else{
			     Bundle args = new Bundle();
				 args.putString(LoaderCliente.STRING_SEARCH, currentTransactionSearch);
				 args.putBoolean(LoaderCliente.STRING_BOOLEAN, false);
			     getLoaderManager().initLoader(0, args, loaderCliente);
		     }
		     
        }
    }
    
    public void ejecutarConsulta(){
		 Bundle args = new Bundle();
		 args.putString(LoaderCliente.STRING_SEARCH, "");
		 args.putBoolean(LoaderCliente.STRING_BOOLEAN, true);
	     executeLoader(0, args, loaderCliente);	     	     	    
    }
    
    public void actualizarCliente(Cliente cliente){
    	for(ArrayList<Cliente> l: list_order){
	    	for(Cliente c: l){
	    		if(c.getID() == cliente.getID()){
	    			c.setDireccion(cliente.getDireccion());
	    			c.setTelefono(cliente.getTelefono());
	    			c.setCorreoElectronico(cliente.getCorreoElectronico());
	    			
	    			adapter.notifyDataSetChanged();    			
	    			
	    			break;
	    		}
	    	}    	
    	}
    }
    
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	
    	linearLayout_rootParent = (LinearLayout) rootView.findViewById(R.id.linearLayout_headerlist_client_list);
    }
    
    
    @Override
    public void onStart() {    	
    	super.onStart();    	    	
    }
         
    
    public void searchTransactions(String termSearch){
        Bundle args = new Bundle();
		args.putString(LoaderCliente.STRING_SEARCH, termSearch);
		args.putBoolean(LoaderCliente.STRING_BOOLEAN, false);
	    getLoaderManager().restartLoader(0, args, loaderCliente);
    	
//    	executeLoader(0, b, this);
    }      
    
      
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    this.menu = menu;
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
						cliente.setTelefono(lista.get(y).getTelefono());
						cliente.setDireccion(lista.get(y).getDireccion());
						cliente.setCorreoElectronico(lista.get(y).getCorreoElectronico());												
						
						list_aux.add(cliente);
					}
				}
				
				list_order.add(list_aux);
			}
				adapter = new ClientListAdapter(this.getActivity(), list_order, headersortList, ORDER_BY_NAME, clienteListFragmentCallback);
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
						cliente.setTelefono(lista.get(y).getTelefono());
						cliente.setDireccion(lista.get(y).getDireccion());
						cliente.setCorreoElectronico(lista.get(y).getCorreoElectronico());												
						
						list_aux.add(cliente);
					}
				}
				
				list_order.add(list_aux);
			}
				adapter = new ClientListAdapter(this.getActivity(), list_order, headersortList, ORDER_BY_LAST_NAME, clienteListFragmentCallback);
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
			clienteListFragmentCallback.onFinalizaConsulta();
		}

		@Override
		public void onLoaderReset(Loader<List<Cliente>> arg0) {	
			
		}
	}

	
}