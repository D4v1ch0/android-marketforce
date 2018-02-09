package rp3.auna.cliente;

import rp3.app.BaseFragment;
import rp3.auna.MainActivity;
import rp3.auna.ruta.CrearVisitaActivity;
import rp3.auna.ruta.CrearVisitaFragment;
import rp3.auna.ventanueva.CrearLlamadaActivity;
import rp3.auna.ventanueva.CrearLlamadaFragment;
import rp3.auna.ventanueva.DetailClienteActivity;
import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.cliente.ClientDetailFragment.ClienteDetailFragmentListener;
import rp3.auna.cliente.ClientListFragment.ClienteListFragmentListener;
import rp3.auna.models.Cliente;
import rp3.auna.ruta.MapaActivity;
import rp3.util.ConnectionUtils;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

public class ClientFragment extends BaseFragment implements ClienteListFragmentListener, ClienteDetailFragmentListener {

	private static final String TAG = ClientFragment.class.getSimpleName();
	private static final int PARALLAX_SIZE = 0;
	
	private ClientListFragment transactionListFragment;
	private ClientDetailFragment transactionDetailFragment;
	private SlidingPaneLayout slidingPane;

    private Menu menu;
	public boolean mTwoPane = false;
    public boolean isActiveListFragment = true;
	private long selectedClientId;
	private boolean isContact = false;
    
	public static ClientFragment newInstance(int transactionTypeId) {
		ClientFragment fragment = new ClientFragment();
		return fragment;
    }

    /**Callback del Long Cliente seleccionado*/
	@Override
	public void onLongClienteSelected(final Cliente cliente, final View view) {
		Log.d(TAG,"onLongClienteSelected...");
		PopupMenu popup = new PopupMenu(getContext(), view);
		//Log.d(TAG,"HeaderList:"+headerList.getListView().getAdapter().getCount());
		popup.getMenuInflater()
				.inflate(R.menu.list_item_client_menu, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				switch(item.getItemId())
				{
					case R.id.item_menu_clientes_crear_visita:
						Log.d(TAG,"click item crear visita");
						Intent intent2 = new Intent(getActivity(), rp3.auna.ventanueva.CrearVisitaActivity.class);
						intent2.putExtra(rp3.auna.ventanueva.CrearVisitaFragment.ARG_CLIENTE, view.getId());
						intent2.putExtra(rp3.auna.ventanueva.CrearVisitaFragment.ARG_CLIENTE, "Cliente");
						intent2.putExtra(rp3.auna.ventanueva.CrearVisitaFragment.ARG_CLIENTE, cliente.getIdCliente());
						startActivity(intent2);
						//((MainActivity)getActivity()).clientDetailFragment=null;
						Log.d(TAG,"item crear visita...");
						/*adapter.setAction(true);*/
						break;
					//Item creación de llamadas

					case R.id.item_menu_clientes_crear_llamada:
						Log.d(TAG,"click item crear llamada");
						Intent intent3 = new Intent(getActivity(), CrearLlamadaActivity.class);
						intent3.putExtra(CrearLlamadaFragment.ARG_IDAGENDA, view.getId());
						intent3.putExtra(CrearLlamadaFragment.ARG_FROM, "Cliente");
						intent3.putExtra(CrearLlamadaFragment.ARG_CLIENTE,cliente.getIdCliente());
						startActivity(intent3);
						//((MainActivity)getActivity()).clientDetailFragment  = null;
						/*adapter.setAction(true);*/
						break;
					/*case R.id.item_menu_clientes_ver_detalle:
						Log.v(TAG,"click item_menu_clientes_ver_detalle");
						selectedClientId = cliente.getIdCliente();

						if(!mTwoPane) {
							slidingPane.closePane();
							isActiveListFragment = false;
						}
						if(cliente.getTipoPersona().equalsIgnoreCase("C"))
							isContact = true;
						else
							isContact = false;
						RefreshMenu();
						Log.d(TAG,"transactionDefailtFragment...");
						transactionDetailFragment = ClientDetailFragment.newInstance(cliente);
						//ClientDetailFragment clientDetailFragment  = ClientDetailFragment.newInstance(cliente);
						Log.d(TAG,"inicio detail cliente...");
						setFragment(R.id.content_transaction_detail,transactionDetailFragment);
						//manager.beginTransaction().replace(R.id.content_transaction_detail,transactionDetailFragment).commit();
						//setFragment(R.id.content_transaction_detail, transactionDetailFragment);
						//transactionDetailFragment = null;
						///((MainActivity)getActivity()).selectedItem = 20;
						//setFragment(R.id.content_transaction_detail, clientDetailFragment );
						/*Intent intent4 = new Intent(getActivity(), DetailClienteActivity.class);
						intent4.putExtra("IdCliente",selectedClientId);
						startActivity(intent4);*/
						//break;*/
				}
				return true;
			}
		});
		popup.show();

	}

	@Override
	public void onFragmentResult(String tagName, int resultCode, Bundle data) {		
		super.onFragmentResult(tagName, resultCode, data);
		Log.d(TAG,"onFragmentResult...");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG,"onAttach...");
		setContentView(R.layout.fragment_client,R.menu.fragment_client_menu);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
		//setRetainInstance(true);
		transactionListFragment = ClientListFragment.newInstance(true, null);					
		
	}

	@SuppressLint("NewApi")
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
		Log.d(TAG,"onFragmentCreateView...");
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
					
		if(!hasFragment(R.id.content_transaction_list)){
			Log.d(TAG,"!hasFragment Ejecuta de nuevo el fragmentDetail...");
			setFragment(R.id.content_transaction_list, transactionListFragment );
		}

		
		slidingPane.setPanelSlideListener(new PanelSlideListener(){

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPanelOpened(View panel) {
                isActiveListFragment = true;
                //getActivity().getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
				RefreshMenu();
			}

			@Override
			public void onPanelClosed(View panel) {
                isActiveListFragment = false;
               // if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2) {
                   // getActivity().getActionBar().setHomeButtonEnabled(true);
                //}
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
		Log.d(TAG,"onStart...");
		if(selectedClientId != 0){
			if(!mTwoPane)	{
				slidingPane.closePane();

			}

		}

	}
	
	@Override
	public void onAfterCreateOptionsMenu(Menu menu) {	
	  this.menu = menu;
		Log.d(TAG,"onAfterCreateOptionsMenu...");
      SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
      //SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
  	  SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

  	  
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
      RefreshMenu();
	}

    private void RefreshMenu(){
		Log.d(TAG,"RefreshMenu...");
        if(!mTwoPane){
			Log.d(TAG,"!mTwoPane..");
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            menu.findItem(R.id.submenu_rutas).setVisible(!isActiveListFragment);
            if(PreferenceManager.getBoolean(Contants.KEY_PERMITIR_CREACION)){
				Log.d(TAG,"KEY_PERMITIR_CREACION = true...");
				menu.findItem(R.id.action_crear_cliente).setVisible(isActiveListFragment);
			}
            else{
				Log.d(TAG,"KEY_PERMITIR_CREACION = false");
				menu.findItem(R.id.action_crear_cliente).setVisible(false);
			}
            if(PreferenceManager.getBoolean(Contants.KEY_PERMITIR_MODIFICACION)){
				Log.d(TAG,"KEY_PERMITIR_MODIFICACION = true...");
				menu.findItem(R.id.action_editar_cliente).setVisible(!isActiveListFragment && !isContact);
			}
            else{
				Log.d(TAG,"KEY_PERMITIR_MODIFICACION = false...");
				menu.findItem(R.id.action_editar_cliente).setVisible(false);
			}
        }
        else{
			Log.d(TAG,"mTwoPane..");
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            if(PreferenceManager.getBoolean(Contants.KEY_PERMITIR_CREACION)){
				Log.d(TAG,"KEY_PERMITIR_CREACION = true...");
				menu.findItem(R.id.action_crear_cliente).setVisible(isActiveListFragment);
			}
            else{
				Log.d(TAG,"KEY_PERMITIR_CREACION = false...");
				menu.findItem(R.id.action_crear_cliente).setVisible(false);
			}
            if(PreferenceManager.getBoolean(Contants.KEY_PERMITIR_MODIFICACION)){
				Log.d(TAG,"KEY_PERMITIR_MODIFICACION = true...");
				menu.findItem(R.id.action_editar_cliente).setVisible(selectedClientId!=0 && !isContact);
			}
            else{
				Log.d(TAG,"KEY_PERMITIR_MODIFICACION = false...");
				menu.findItem(R.id.action_editar_cliente).setVisible(false);
			}
        }
    }
	 
	@Override
	public void onClienteSelected(Cliente cl) {
		Log.d(TAG,"onClienteSelected..."+cl.toString());
		/*selectedClientId = cl.getID();

		if(!mTwoPane) {
			slidingPane.closePane();
			isActiveListFragment = false;
		}
		if(cl.getTipoPersona().equalsIgnoreCase("C"))
			isContact = true;
		else
			isContact = false;
        RefreshMenu();
		Log.d(TAG,"transactionDefailtFragment...");
		transactionDetailFragment = ClientDetailFragment.newInstance(cl);
		setFragment(R.id.content_transaction_detail, transactionDetailFragment);*/
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
            case R.id.action_editar_cliente:
            	Log.d(TAG,"action_editar_cliente...");
                Intent intent2 = new Intent(getActivity(), CrearClienteActivity.class);
                intent2.putExtra(CrearClienteActivity.ARG_IDCLIENTE, selectedClientId);
                startActivity(intent2);
                break;
            case R.id.action_crear_cliente:
            	Log.d(TAG,"action_crear_cliente...");
                Intent intent = new Intent(this.getActivity(), CrearClienteActivity.class);
                startActivity(intent);
                break;
            //case R.id.action_import_contacts:
            //    showDialogFragment(ImportChooseFragment.newInstance(), "Import");
            //    break;
            case R.id.action_ver_posicion:
            	Log.d(TAG,"action_ver_posicion...");
                if (!ConnectionUtils.isNetAvailable(getContext())) {
                    Toast.makeText(getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                } else if (selectedClientId != 0) {
                    Intent intent3 = new Intent(getActivity(), MapaActivity.class);
                    intent3.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_POSICION_CLIENTE);
                    intent3.putExtra(MapaActivity.ARG_AGENDA, selectedClientId);
                    startActivity(intent3);
                } else {
                    Toast.makeText(getContext(), "Debe seleccionar una cliente.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_como_llegar:
                if (!ConnectionUtils.isNetAvailable(getContext())) {
                    Toast.makeText(getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                } else if (selectedClientId != 0) {
                    Intent intent4 = new Intent(getActivity(), MapaActivity.class);
                    intent4.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_LLEGAR_CLIENTE);
                    intent4.putExtra(MapaActivity.ARG_AGENDA, selectedClientId);
                    startActivity(intent4);
                } else {
                    Toast.makeText(getContext(), R.string.warning_seleccionar_cliente, Toast.LENGTH_LONG).show();
                }
                return true;
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
		Log.d(TAG,"onClienteChanged");
		transactionListFragment.actualizarCliente(cliente);
		onClienteSelected(cliente);
	}

	@Override
	public void onFinalizaConsulta() {		
	}



	/**
	 *
	 * Ciclo de vida
	 *
	 */

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"onResume...");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}
	
}
