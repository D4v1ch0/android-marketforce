package rp3.marketforce.cliente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rp3.configuration.Configuration;
import rp3.data.MessageCollection;
import rp3.marketforce.R;
import rp3.marketforce.headerlistview.HeaderListView;
import rp3.marketforce.loader.ClientLoader;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.ruta.CrearVisitaActivity;
import rp3.marketforce.ruta.CrearVisitaFragment;
import rp3.marketforce.ruta.RutasListAdapter;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import rp3.util.Convert;
import rp3.util.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ClientListFragment extends rp3.app.BaseFragment {
		        
    
    public static final String ARG_TRANSACTIONTYPEID = "transactionType";
    public static final String ARG_TRANSACTIONTYPEBO = "transactionTypeBo";    
    
    private static final int HEADERLIST_ID = 44;
    public static final int ORDER_BY_NAME = 5;
    public static final int ORDER_BY_LAST_NAME = 6;
    public static final String ARG_CODIGOCLIENTE = "idCliente";

    public static final int SHOWPROGRESS = 1;
    public static final int NOSHOWPROGRESS = 0;

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
    public SwipeRefreshLayout refreshLayout;
    
    private LoaderCliente loaderCliente;
    private boolean isContacts = false;
    public static int idCurrentCliente = -1;

    private int orderBy;

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

    public static ClientListFragment newInstance(boolean flag , String transactionTypeId, int idCurrentCliente) {
        ClientListFragment fragment = new ClientListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSACTIONTYPEID, transactionTypeId);
        args.putBoolean(ARG_TRANSACTIONTYPEBO, flag);
        args.putInt(ARG_CODIGOCLIENTE, idCurrentCliente);
        fragment.setArguments(args);
        return fragment;
    }
    public interface ClienteListFragmentListener {
        public void onClienteSelected(Cliente cliente);
        public void onFinalizaConsulta();
        public boolean allowSelectedItem();        
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
            setRetainInstance(true);
        }

        super.setContentView(R.layout.layout_headerlist_client_list);
    }   
	
	@Override
	public void onResume() {
        super.onResume();
        Resume(true);
    }

    private void Resume(boolean cargaProgress){
        if (currentTransactionBoolean) {
            ejecutarConsulta(cargaProgress);
        } else {
            Bundle args = new Bundle();
            args.putString(LoaderCliente.STRING_SEARCH, currentTransactionSearch);
            args.putBoolean(LoaderCliente.STRING_BOOLEAN, false);
            if(loaderCliente == null)
                loaderCliente = new LoaderCliente();
            int cargaProg = NOSHOWPROGRESS;
            if(cargaProgress)
                cargaProg = SHOWPROGRESS;
            getLoaderManager().initLoader(cargaProg, args, loaderCliente);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderBy = ORDER_BY_NAME;
        if(savedInstanceState == null)
        {
        	currentTransactionBoolean = getArguments().getBoolean(ARG_TRANSACTIONTYPEBO);
		    currentTransactionSearch = getArguments().getString(ARG_TRANSACTIONTYPEID);
            idCurrentCliente = getArguments().getInt(ARG_CODIGOCLIENTE);
		    id_select = R.id.item_order_name;

		    loaderCliente = new LoaderCliente();

        }
    }
    
    public void ejecutarConsulta(boolean cargaProgress){
		 Bundle args = new Bundle();
		 args.putString(LoaderCliente.STRING_SEARCH, "");
		 args.putBoolean(LoaderCliente.STRING_BOOLEAN, true);
         int cargaProg = cargaProgress?1:0;

	     executeLoader(cargaProg, args, loaderCliente);
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

    	if(headerList!=null && headerList.getParent() == null){

            if(refreshLayout == null)
                refreshLayout = new SwipeRefreshLayout(this.getContext());
            refreshLayout.setRefreshing(false);
            refreshLayout.addView(headerList);
            linearLayout_rootParent.removeView(refreshLayout);
            linearLayout_rootParent.addView(refreshLayout);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (ConnectionUtils.isNetAvailable(getContext())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_CLIENTES);
                        requestSync(bundle);
                    } else {
                        Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                        refreshLayout.setRefreshing(false);
                    }
                }
            });
            headerList.getListView().setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
            headerList.setId(HEADERLIST_ID);
            headerList.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                    if (firstVisibleItem == 0 && visibleItemCount != 0) {
                        refreshLayout.setEnabled(true);
                    } else {
                        refreshLayout.setEnabled(false);
                    }

                }
            });
            headerList.setAdapter(adapter);

        }
    }
    
     @Override
    public void onSaveInstanceState(Bundle arg0) {    	
    	super.onSaveInstanceState(arg0);
    	
    	linearLayout_rootParent.removeView(refreshLayout);
    }
    
    @Override
    public void onDetach() {    	
    	super.onDetach();    	    	
    }        
         
    
    public void searchTransactions(String termSearch){
        Bundle args = new Bundle();
		args.putString(LoaderCliente.STRING_SEARCH, termSearch);
		args.putBoolean(LoaderCliente.STRING_BOOLEAN, false);
	    getLoaderManager().restartLoader(NOSHOWPROGRESS, args, loaderCliente);
    	
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
     	 
     	 if(item.getItemId() == R.id.item_order_name || item.getItemId() == R.id.item_order_last_name){
     		 callOrderBy(item.getItemId());
     		 return true;
     	 }
     	 if(item.getItemId() == R.id.action_contacts)
     	 {
     		 if(isContacts)
     		 {
     			 isContacts = false;
     			 menu.findItem(R.id.action_contacts).setChecked(false);
     		 }
     		 else
     		 {
     			 isContacts = true;
     			 menu.findItem(R.id.action_contacts).setChecked(true);
     		 }
     		 
     		Bundle args = new Bundle();
    		args.putString(LoaderCliente.STRING_SEARCH, "");
    		args.putBoolean(LoaderCliente.STRING_BOOLEAN, true);
    	    getLoaderManager().restartLoader(SHOWPROGRESS, args, loaderCliente);
     		 
     		 return true;
     	 }
	    	
     	 return super.onOptionsItemSelected(item);
	 }
	 	
	 private void callOrderBy(int id){
		 switch(id)
	    	{
	    		case R.id.item_order_name:
	    			
	    			id_select = R.id.item_order_name;
                    orderBy = ORDER_BY_NAME;
                    //showDialogProgress(getResources().getString(R.string.loadingTitleWait),getResources().getString(R.string.loadingMessageClient));
	    		   OrderBy(orderBy);
	    		   //closeDialogProgress();
	    		   break;
		    			
	               
	                
	    		case R.id.item_order_last_name:
	    			
	    			id_select = R.id.item_order_last_name;
                    orderBy = ORDER_BY_LAST_NAME;
                    //showDialogProgress(getResources().getString(R.string.loadingTitleWait),getResources().getString(R.string.loadingMessageClient));
	    			OrderBy(orderBy);
                    //closeDialogProgress();
	    			break;
	    				
	              
	    	}
	 }

	 private ArrayList<Integer> getPositionCliente(int idCliente, ArrayList<ArrayList <Cliente>> lista){
         ArrayList<Integer> pos = new ArrayList<>();
         int posicionGeneral = 0;
         int posSeccion = 0;
         int posRow = 0;

         if(lista.size() == 0){
             return pos;
         }

         if(idCliente==-1)
         {
             pos.add(posSeccion);
             pos.add(posRow);
             pos.add(posicionGeneral);
             return pos;
         }

         for (ArrayList<Cliente> arrayCliente: lista) {
             posRow = 0;
             for (Cliente cliente: arrayCliente) {
                 if(idCliente == cliente.getIdCliente())
                 {
                     pos.add(posSeccion);
                     pos.add(posRow);
                     pos.add(posicionGeneral);
                     return pos;
                 }
                 posRow++;
                 posicionGeneral++;
             }
             posicionGeneral++;
             posSeccion++;
         }
         return pos;
     }

	 private int getPosOrderBy(ArrayList<Cliente> list_aux, Cliente cliente){
         int pos = 0;
         String cadenaUno = orderBy == ORDER_BY_NAME?cliente.getNombre1().toUpperCase().toString():( orderBy == ORDER_BY_LAST_NAME?cliente.getApellido1().toUpperCase().toString():"");
         String cadenaDos = "";

         String cadenaUnoSub = orderBy == ORDER_BY_NAME?cliente.getApellido1().toUpperCase().toString():( orderBy == ORDER_BY_LAST_NAME?cliente.getNombre1().toUpperCase().toString():"");
         String cadenaDosSub = "";
         for (Cliente tmpCliente :list_aux) {
             cadenaDos = orderBy == ORDER_BY_NAME?tmpCliente.getNombre1().toUpperCase().toString():( orderBy == ORDER_BY_LAST_NAME?tmpCliente.getApellido1().toUpperCase().toString():"");
             cadenaDosSub = orderBy == ORDER_BY_NAME?tmpCliente.getApellido1().toUpperCase().toString():( orderBy == ORDER_BY_LAST_NAME?tmpCliente.getNombre1().toUpperCase().toString():"");

             if(cadenaUno.compareTo(cadenaDos) == 0)
             {
                 if(cadenaUnoSub.compareTo(cadenaDosSub) < 0)
                 {
                     break;
                 }
             }
             else if(cadenaUno.compareTo(cadenaDos) < 0)
             {
                 break;
             }
             pos++;
         }
         return pos;
     }

	@SuppressLint("SimpleDateFormat")
	private void OrderBy(int option) {

        try {


            if (lista == null)
                return;

            if (lista.size() == 0)
                return;

            headerList = new HeaderListView(getActivity());
            if (refreshLayout == null)
                refreshLayout = new SwipeRefreshLayout(this.getContext());

            refreshLayout.addView(headerList);
            linearLayout_rootParent.removeView(refreshLayout);
            linearLayout_rootParent.addView(refreshLayout);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (ConnectionUtils.isNetAvailable(getContext())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_CLIENTES);
                        requestSync(bundle);
                    } else {
                        Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                        refreshLayout.setRefreshing(false);
                    }
                }
            });
            headerList.getListView().setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));
            headerList.setId(HEADERLIST_ID);
            headerList.getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                    if (firstVisibleItem == 0 && visibleItemCount != 0) {
                        refreshLayout.setEnabled(true);
                    } else {
                        refreshLayout.setEnabled(false);
                    }

                }
            });


            headersortList = new ArrayList<String>();

            list_order = new ArrayList<ArrayList<rp3.marketforce.models.Cliente>>();


            switch (option) {
                case ORDER_BY_NAME:

                    for (int x = 0; x < lista.size(); x++)
                        if (!headersortList.contains("" + lista.get(x).getNombre1().toUpperCase().charAt(0)))
                            headersortList.add("" + lista.get(x).getNombre1().toUpperCase().charAt(0));

                    Collections.sort(headersortList);

                    for (int x = 0; x < headersortList.size(); x++) {
                        ArrayList<rp3.marketforce.models.Cliente> list_aux = new ArrayList<rp3.marketforce.models.Cliente>();

                        for (int y = 0; y < lista.size(); y++) {
                            if (headersortList.get(x).equals("" + lista.get(y).getNombre1().toUpperCase().charAt(0))) {
                                rp3.marketforce.models.Cliente cliente = new rp3.marketforce.models.Cliente();

                                cliente.setID(lista.get(y).getID());
                                cliente.setIdCliente(lista.get(y).getIdCliente());
                                cliente.setNombre1(lista.get(y).getNombre1());
                                cliente.setNombre2(lista.get(y).getNombre2());
                                cliente.setApellido1(lista.get(y).getApellido1());
                                cliente.setApellido2(lista.get(y).getApellido2());
                                cliente.setTelefono(lista.get(y).getTelefono());
                                cliente.setDireccion(lista.get(y).getDireccion());
                                cliente.setCorreoElectronico(lista.get(y).getCorreoElectronico());
                                cliente.setTipoPersona(lista.get(y).getTipoPersona());

                                //Permite ordenar
                                int pos = getPosOrderBy(list_aux,cliente);
                                list_aux.add(pos,cliente);
                                //list_aux.add(cliente);
                            }
                        }

                        list_order.add(list_aux);
                    }

                    if(adapter == null) {
                        adapter = new ClientListAdapter(this.getActivity(), list_order, headersortList, ORDER_BY_NAME, clienteListFragmentCallback);

                        headerList.setAdapter(adapter);
                    }
                    else
                        adapter.swapElements(list_order, headersortList, ORDER_BY_NAME, clienteListFragmentCallback);

                    //adapter.notifyDataSetChanged();
                    if (clienteListFragmentCallback.allowSelectedItem())
                        clienteListFragmentCallback.onClienteSelected(list_order.get(0).get(0));

//			ORDER_IDENTIFICATOR	= ORDER_BY_NAME;

                    break;
                case ORDER_BY_LAST_NAME:

                    for (int x = 0; x < lista.size(); x++) {
                        if (!lista.get(x).getTipoPersona().equalsIgnoreCase("J")) {
                            if (lista.get(x).getApellido1().length() > 0) {
                                if (!headersortList.contains("" + lista.get(x).getApellido1().toUpperCase().charAt(0)))
                                    headersortList.add("" + lista.get(x).getApellido1().toUpperCase().charAt(0));
                            }
                        } else {
                            if (!headersortList.contains("" + lista.get(x).getNombre1().toUpperCase().charAt(0)))
                                headersortList.add("" + lista.get(x).getNombre1().toUpperCase().charAt(0));
                        }
                    }

                    Collections.sort(headersortList);

                    for (int x = 0; x < headersortList.size(); x++) {
                        ArrayList<rp3.marketforce.models.Cliente> list_aux = new ArrayList<rp3.marketforce.models.Cliente>();

                        for (int y = 0; y < lista.size(); y++) {
                            if (lista.get(y).getTipoPersona().equalsIgnoreCase("N") || lista.get(y).getTipoPersona().equalsIgnoreCase("C"))
                                if (lista.get(y).getApellido1().length() > 0 && headersortList.get(x).equals("" + lista.get(y).getApellido1().toUpperCase().charAt(0))) {
                                    rp3.marketforce.models.Cliente cliente = new rp3.marketforce.models.Cliente();

                                    cliente.setID(lista.get(y).getID());
                                    cliente.setIdCliente(lista.get(y).getIdCliente());
                                    cliente.setNombre1(lista.get(y).getNombre1());
                                    cliente.setNombre2(lista.get(y).getNombre2());
                                    cliente.setApellido1(lista.get(y).getApellido1());
                                    cliente.setApellido2(lista.get(y).getApellido2());
                                    cliente.setTelefono(lista.get(y).getTelefono());
                                    cliente.setDireccion(lista.get(y).getDireccion());
                                    cliente.setCorreoElectronico(lista.get(y).getCorreoElectronico());
                                    cliente.setTipoPersona(lista.get(y).getTipoPersona());

                                    int pos = getPosOrderBy(list_aux,cliente);
                                    list_aux.add(pos,cliente);
                                    //list_aux.add(cliente);
                                } else
                            if (lista.get(y).getTipoPersona().equalsIgnoreCase("J") && headersortList.get(x).equals("" + lista.get(y).getNombre1().toUpperCase().charAt(0))) {
                                rp3.marketforce.models.Cliente cliente = new rp3.marketforce.models.Cliente();

                                cliente.setID(lista.get(y).getID());
                                cliente.setIdCliente(lista.get(y).getIdCliente());
                                cliente.setNombre1(lista.get(y).getNombre1());
                                cliente.setNombre2(lista.get(y).getNombre2());
                                cliente.setApellido1(lista.get(y).getApellido1());
                                cliente.setApellido2(lista.get(y).getApellido2());
                                cliente.setTelefono(lista.get(y).getTelefono());
                                cliente.setDireccion(lista.get(y).getDireccion());
                                cliente.setCorreoElectronico(lista.get(y).getCorreoElectronico());
                                cliente.setTipoPersona(lista.get(y).getTipoPersona());

                                int pos = getPosOrderBy(list_aux,cliente);
                                list_aux.add(pos,cliente);
                                //list_aux.add(cliente);
                            }

                        }

                        list_order.add(list_aux);
                    }
                    if(adapter == null) {
                        adapter = new ClientListAdapter(this.getActivity(), list_order, headersortList, ORDER_BY_LAST_NAME, clienteListFragmentCallback);
                        headerList.setAdapter(adapter);
                    }
                    else
                        adapter.swapElements(list_order, headersortList, ORDER_BY_LAST_NAME, clienteListFragmentCallback);
                    //adapter.notifyDataSetChanged();
                    if (clienteListFragmentCallback.allowSelectedItem())
                            clienteListFragmentCallback.onClienteSelected(list_order.get(0).get(0));
//			ORDER_IDENTIFICATOR	= ORDER_BY_LAST_NAME;

                    break;
            }

            setItemSelected();
        }
        catch(Exception ex)
        {

        }

    }

    private void setItemSelected() {
        int seccion = 0;
        int row = 0;
        int posGeneral = 0;
        ArrayList<Integer> pos = getPositionCliente(idCurrentCliente,list_order);
        if(pos.size()>0)
        {
            seccion = pos.get(0);
            row = pos.get(1);
            posGeneral = pos.get(2);

            if(getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE){
                View firstView = headerList.getChildAt(0);
                adapter.onRowItemClick(headerList.getListView(),firstView,seccion,row,0);
            }
            headerList.getListView().setSelection(posGeneral);
        }

        adapter.notifyDataSetChanged();
    }

    public class LoaderCliente implements LoaderCallbacks<List<Cliente>>{
    	
    	public static final String STRING_SEARCH = "string_search";
    	public static final String STRING_BOOLEAN = "string_boolean";
    	private String Search;
    	private boolean flag;

    	@Override
		public Loader<List<Cliente>> onCreateLoader(int cargaProgress,
				Bundle bundle) {				

    		Search = bundle.getString(STRING_SEARCH);
    		flag = bundle.getBoolean(STRING_BOOLEAN);

            if(cargaProgress == SHOWPROGRESS)
                showDialogProgress( getResources().getString(R.string.loadingTitleWait),getResources().getString(R.string.loadingMessageClient));
            //showDefaultLoading();
			return new ClientLoader(getActivity(), getDataBase(), flag, Search, isContacts);
		}

		@Override
		public void onLoadFinished(Loader<List<Cliente>> arg0,
				List<Cliente> data)
		{

			lista = data;
			OrderBy(orderBy);
			clienteListFragmentCallback.onFinalizaConsulta();
			if(adapter != null)
			{

				adapter.notifyDataSetChanged();
				headerList.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
	
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							final View view, final int position, long id) {
						PopupMenu popup = new PopupMenu(getContext(), view);
		                
		                popup.getMenuInflater()
		                    .inflate(R.menu.list_item_client_menu, popup.getMenu());
		                
		                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
		                    public boolean onMenuItemClick(MenuItem item) {
		                        switch(item.getItemId())
		                        {
		                        	case R.id.item_menu_clientes_crear_visita:
		                        		Intent intent2 = new Intent(getActivity(), CrearVisitaActivity.class);
		                        		intent2.putExtra(CrearVisitaFragment.ARG_IDAGENDA, view.getId());
		                        		intent2.putExtra(CrearVisitaFragment.ARG_FROM, "Cliente");
		                        		startActivity(intent2);
		                        		adapter.setAction(true);
		                        	break;
		                        }
		                        return true;
		                    }
		                });
		                popup.show();
						return false;
					}
				});

			}
            closeDialogProgress();
            //hideDefaultLoading();
		}

		@Override
		public void onLoaderReset(Loader<List<Cliente>> arg0) {	

		}
	}

    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);

        try {


            refreshLayout.setRefreshing(false);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (ConnectionUtils.isNetAvailable(getContext())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_CLIENTES);
                        requestSync(bundle);
                    } else {
                        Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
                        refreshLayout.setRefreshing(false);
                    }
                }
            });
            Resume(false);
        }
        catch (Exception ex)
        {}

    }

	
}
