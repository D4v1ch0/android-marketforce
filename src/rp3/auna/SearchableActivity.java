package rp3.auna;

import rp3.configuration.PreferenceManager;
import rp3.app.BaseActivity;
import rp3.auna.cliente.ClientDetailFragment;
import rp3.auna.cliente.ClientDetailFragment.ClienteDetailFragmentListener;
import rp3.auna.cliente.ClientListFragment;
import rp3.auna.cliente.CrearClienteActivity;
import rp3.auna.models.Cliente;
import rp3.auna.ruta.MapaActivity;
import rp3.util.ConnectionUtils;
import rp3.widget.SlidingPaneLayout;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;


public class SearchableActivity extends BaseActivity
	implements ClientListFragment.ClienteListFragmentListener, ClienteDetailFragmentListener {

	private static final String TAG = SearchableActivity.class.getSimpleName();
	private boolean mTwoPane;
	private String query;
	private static final int PARALLAX_SIZE = 0;
	private static final String STATE_SELECTED_ID = "id";
	private static final String STATE_SELECTED_TIPO_PERSONA = "tipoPersona";
	
//	private MenuItem menuItemActionEdit;
//    private MenuItem menuItemActionDiscard;
    private long selectedClientId;
    private String tipoPersona;
    
	private ClientDetailFragment clientDetailFragment;
	private ClientListFragment clientListFragment;
	private SlidingPaneLayout slidingPane;
	public boolean isActiveListFragment = true;
	private Menu menu;
	private boolean isContact = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
		setContentView(R.layout.fragment_client);				
		
		setHomeAsUpEnabled(true, true);	
		
		slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane_clientes);
		slidingPane.setParallaxDistance(PARALLAX_SIZE);
		slidingPane.setShadowResource(R.drawable.sliding_pane_shadow);
		slidingPane.setSlidingEnabled(true);
		slidingPane.openPane();
		
		 Intent intent = getIntent();
		    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
				Log.d(TAG,"Intent.ACTION_SEARCH.equals(intent.getAction())...");
		    	query = intent.getStringExtra(SearchManager.QUERY);
//		    	executeSearch(query);
		    }	
		
		
		if(!hasFragment(R.id.content_transaction_list)){
			Log.d(TAG,"!hasFragment(R.id.content_transaction_list)...");
			clientListFragment = ClientListFragment.newInstance(false, query);
			setFragment(R.id.content_transaction_list, clientListFragment);			
		}else{
			Log.d(TAG,"hasFragment(R.id.content_transaction_list)...");
			clientListFragment = (ClientListFragment) getFragment(R.id.content_transaction_list);
		}
		
		if(hasFragment(R.id.content_transaction_detail)){
			Log.d(TAG,"hasFragment(R.id.content_transaction_detail)...");
			clientDetailFragment = (ClientDetailFragment)getFragment(R.id.content_transaction_detail);
		}
		
		if(slidingPane.isOpen() && 
				findViewById(R.id.content_transaction_list).getLayoutParams().width != LayoutParams.MATCH_PARENT)
			mTwoPane = true;
		else
			mTwoPane = false;

		slidingPane.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener(){

			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPanelOpened(View panel) {
				isActiveListFragment = true;
				Log.d(TAG,"onPanelOpened...");
				//getActivity().getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
				RefreshMenu();
			}

			@Override
			public void onPanelClosed(View panel) {
				isActiveListFragment = false;
				Log.d(TAG,"onPanelClosed...");
				// if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2) {
				// getActivity().getActionBar().setHomeButtonEnabled(true);
				//}
				RefreshMenu();
			}});
		
		if(savedInstanceState!=null){
			selectedClientId = savedInstanceState.getLong(STATE_SELECTED_ID);
			tipoPersona = savedInstanceState.getString(STATE_SELECTED_TIPO_PERSONA);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG,"onSaveInstanceState...");
		super.onSaveInstanceState(outState);
		
		outState.putLong(STATE_SELECTED_ID,selectedClientId);
		outState.putString(STATE_SELECTED_TIPO_PERSONA,tipoPersona);
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
		Log.d(TAG,"onStart...");
		
		if(selectedClientId != 0){
			if(!mTwoPane)			
				slidingPane.closePane();
		}		
	}
	
	private void executeSearch(String query)
	{
		
//		if(hasFragment(R.id.content_transaction_list))
//		{
			clientListFragment.searchTransactions(query);
//		}else
//		{
//			Log.e("TAG","NULL");
//		}
	}

	private void RefreshMenu(){
		Log.d(TAG,"RefreshMenu...");
		if(!mTwoPane){
			Log.d(TAG,"!mTwoPane...");
			menu.findItem(R.id.action_search).setVisible(false);
			menu.findItem(R.id.submenu_rutas).setVisible(!isActiveListFragment);
			if(PreferenceManager.getBoolean(Contants.KEY_PERMITIR_CREACION)) {
				menu.findItem(R.id.action_crear_cliente).setVisible(isActiveListFragment);
				Log.d(TAG, "PreferenceManager.getBoolean(Contants.KEY_PERMITIR_CREACION)...");
			}
			else {
				Log.d(TAG,"!PreferenceManager.getBoolean(Contants.KEY_PERMITIR_CREACION)...");
				menu.findItem(R.id.action_crear_cliente).setVisible(false);
			}

			if(PreferenceManager.getBoolean(Contants.KEY_PERMITIR_MODIFICACION))
			{
				Log.d(TAG,"PreferenceManager.getBoolean(Contants.KEY_PERMITIR_MODIFICACION)...");
				menu.findItem(R.id.action_editar_cliente).setVisible(!isActiveListFragment && !isContact);
			}
			else
			{
				Log.d(TAG,"!PreferenceManager.getBoolean(Contants.KEY_PERMITIR_MODIFICACION)...");
				menu.findItem(R.id.action_editar_cliente).setVisible(false);
			}
		}
		else{Log.d(TAG,"mTwoPane...");
			menu.findItem(R.id.action_search).setVisible(false);
			if(PreferenceManager.getBoolean(Contants.KEY_PERMITIR_CREACION))
			{
				Log.d(TAG,"PreferenceManager.getBoolean(Contants.KEY_PERMITIR_CREACION)...");
				menu.findItem(R.id.action_crear_cliente).setVisible(isActiveListFragment);
			}
			else
			{
				Log.d(TAG,"!PreferenceManager.getBoolean(Contants.KEY_PERMITIR_CREACION)...");
				menu.findItem(R.id.action_crear_cliente).setVisible(false);
			}

			if(PreferenceManager.getBoolean(Contants.KEY_PERMITIR_MODIFICACION))
			{
				Log.d(TAG,"PreferenceManager.getBoolean(Contants.KEY_PERMITIR_MODIFICACION)...");
				menu.findItem(R.id.action_editar_cliente).setVisible(selectedClientId!=0 && !isContact);
			}
			else
			{
				Log.d(TAG,"!PreferenceManager.getBoolean(Contants.KEY_PERMITIR_MODIFICACION)...");
				menu.findItem(R.id.action_editar_cliente).setVisible(false);
			}

		}
	}
	
	@Override
	public void onClienteSelected(Cliente cliente) {
		Log.d(TAG,"onClienteSelected..."+cliente.toString());
		selectedClientId = cliente.getID();
		tipoPersona = cliente.getTipoPersona();

		clientDetailFragment = ClientDetailFragment.newInstance(cliente);
		setVisibleEditActionButtons(selectedClientId != 0 );
		setFragment(R.id.content_transaction_detail, clientDetailFragment);	
		
		if(!mTwoPane)
			slidingPane.closePane();

	}

	
	private void setVisibleEditActionButtons(boolean visible){
//    	menuItemActionEdit.setVisible(visible);
//    	menuItemActionDiscard.setVisible(visible);
    }
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.menu = menu;
		getMenuInflater().inflate(R.menu.fragment_client_menu, menu);

//		menuItemActionEdit = menu.findItem(R.id.action_edit);
//		menuItemActionDiscard = menu.findItem(R.id.action_discard);

		RefreshMenu();
	    
		return true;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {    	
        switch (item.getItemId()) {
            case android.R.id.home: 
            	if(!slidingPane.isOpen())
				{
					slidingPane.openPane();
					return true;
				}
            	else
            	{
            		this.finish();
            	}
                return true;
			case R.id.action_editar_cliente:
				Intent intent2 = new Intent(this, CrearClienteActivity.class);
				intent2.putExtra(CrearClienteActivity.ARG_IDCLIENTE, selectedClientId);
				startActivity(intent2);
				break;
			case R.id.action_crear_cliente:
				Intent intent = new Intent(this, CrearClienteActivity.class);
				startActivity(intent);
				break;
			//case R.id.action_import_contacts:
			//	showDialogFragment(ImportChooseFragment.newInstance(), "Import");
			//	break;
			case R.id.action_ver_posicion:
				if (!ConnectionUtils.isNetAvailable(this)) {
					Log.d(TAG,"!ConnectionUtils.isNetAvailable(this)...");
					Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
				} else if (selectedClientId != 0) {
					Log.d(TAG,"selectedClientId != 0...");
					Intent intent3 = new Intent(this, MapaActivity.class);
					intent3.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_POSICION_CLIENTE);
					intent3.putExtra(MapaActivity.ARG_AGENDA, selectedClientId);
					startActivity(intent3);
				} else {
					Log.d(TAG,"selectedClientId == 0...");
					Toast.makeText(this, "Debe seleccionar una cliente.", Toast.LENGTH_LONG).show();
				}
				return true;
			case R.id.action_como_llegar:
				if (!ConnectionUtils.isNetAvailable(this)) {
					Log.d(TAG,"!ConnectionUtils.isNetAvailable(this)...");
					Toast.makeText(this, "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
				} else if (selectedClientId != 0) {
					Log.d(TAG,"selectedClientId != 0...");
					Intent intent4 = new Intent(this, MapaActivity.class);
					intent4.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_LLEGAR_CLIENTE);
					intent4.putExtra(MapaActivity.ARG_AGENDA, selectedClientId);
					startActivity(intent4);
				} else {
					Log.d(TAG,"selectedClientId == 0...");
					Toast.makeText(this, R.string.warning_seleccionar_cliente, Toast.LENGTH_LONG).show();
				}
				return true;
			default:
				break;
        }
        
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onFinalizaConsulta() {
		Log.d(TAG,"onFinalizaConsulta...");
	}

	@Override
	public boolean allowSelectedItem() {		
		return mTwoPane;
	}

	@Override
	public void onLongClienteSelected(Cliente cliente,View view) {

	}

	@Override
	public void onClienteChanged(Cliente cliente) {
		Log.d(TAG,"onClienteChanged..."+cliente.toString());
		clientListFragment.actualizarCliente(cliente);
		onClienteSelected(cliente);		
	}

	/**
	 *
	 * Ciclo de vida
	 *
	 */


	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume...");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}
}
