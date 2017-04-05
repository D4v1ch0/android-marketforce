package rp3.marketforce.ruta;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.Manifest;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.oportunidad.AgendaProspectoFragment;
import rp3.marketforce.resumen.AgenteDetalleFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.Utils;
import rp3.util.ConnectionUtils;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class RutasFragment extends BaseFragment implements RutasListFragment.TransactionListFragmentListener, ContactsAgendaFragment.SaveContactsListener, EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
	public static final String ARG_TRANSACTIONTYPEID = "transactionTypeId";
	private static final int PARALLAX_SIZE = 0;
	
	public boolean mTwoPane = false;
	private long selectedTransactionId;
	private String textSearch;
//	private MenuItem menuItemActionEdit;
//    private MenuItem menuItemActionDiscard;

	private RutasListFragment rutasListFragment;
	private RutasDetailFragment rutasDetailfragment;
    private AgendaProspectoFragment prospectoDetailfragment;
	private ObservacionesFragment obsFragment;
    private MotivoNoVisitaFragment motivoNoVisitaFragment;
	private SlidingPaneLayout slidingPane;
	private boolean openPane = true;
    private Menu menuRutas;
    private boolean isMainFragment = true;
    public SimpleDateFormat format1 = new SimpleDateFormat("EEEE");
    public SimpleDateFormat format2 = new SimpleDateFormat("dd");
    public SimpleDateFormat format3 = new SimpleDateFormat("MMMM");

    //Parametros para Google Calendar
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };

    public static RutasFragment newInstance(int transactionTypeId) {
		RutasFragment fragment = new RutasFragment();
		return fragment;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		setContentView(R.layout.fragment_client,R.menu.fragment_client);

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
        setHasOptionsMenu(true);
			
		rutasListFragment = RutasListFragment.newInstance();
		obsFragment = null;
        setContentView(R.layout.fragment_rutas, R.menu.fragment_ruta_menu);
	}
	
	@Override
	public void onStart() {		
		super.onStart();
		/*if(selectedTransactionId != 0 && openPane){
            isMainFragment = false;
			if(!mTwoPane)			
				slidingPane.closePane();			
		}
		else
			openPane = true;*/
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
                try {
                    isMainFragment = true;
                    RefreshMenu();
                    if(selectedTransactionId != 0)
                        rutasListFragment.Refresh();
                }
                catch(Exception ex)
                {}

			}

			@Override
			public void onPanelClosed(View panel) {
                isMainFragment = false;
                RefreshMenu();
			}});
		
		if(!hasFragment(R.id.content_transaction_list))
		{
			setFragment(R.id.content_transaction_list, rutasListFragment );
		}
		
		if(obsFragment != null && !obsFragment.closed)
		{
			obsFragment.dismiss();
			this.showDialogFragment(obsFragment, "");
		}
		
		if(slidingPane.isOpen() && 
				rootView.findViewById(R.id.content_transaction_list).getLayoutParams().width != LayoutParams.MATCH_PARENT) {
            mTwoPane = true;
        }
		else
			mTwoPane = false;

        /*FloatingActionButton fabGoogle = (FloatingActionButton) getRootView().findViewById(R.id.fab_ruta);
        fabGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SyncGoogle();
            }
        });*/
	}	

	@Override
	public void onAfterCreateOptionsMenu(Menu menu) {
        menuRutas = menu;
        SearchView searchView = null;
        MenuItem prob = menu.findItem(R.id.action_search_ruta);
        if(prob != null)
		    searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_ruta));

        if(searchView != null) {
            int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
            searchPlate.setHintTextColor(getResources().getColor(R.color.color_hint));
            searchPlate.setHint(getActivity().getResources().getString(R.string.hint_search_transaction_rutas));
            searchPlate.setTextColor(getResources().getColor(R.color.apptheme_color));
            searchPlate.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);

            searchView.setOnQueryTextListener(new OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (rutasListFragment != null)
                        rutasListFragment.searchTransactions(query);
                    return true;
                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText) && !TextUtils.isEmpty(textSearch)) {
                        try {
                            rutasListFragment.searchTransactions("");
                        } catch (Exception ex) {

                        }
                    }
                    textSearch = newText;

                    return true;

                }
            });
        }

        if(menu.findItem(R.id.submenu_rutas) != null)
            RefreshMenu();
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {		
		return super.onContextItemSelected(item);
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch(item.getItemId())
	    	{
                case R.id.action_sync_google:
                    SyncGoogle();
                    return true;
	    		case R.id.action_search_ruta:    	
	    			return true;
	    		case R.id.action_crear_visita:
	    			Intent intent = new Intent(getActivity(), CrearVisitaActivity.class);
	    			startActivity(intent);
	    			openPane = false;
	    			return true;
                /*case R.id.action_asignar_pedido:
                    Agenda agdPed = Agenda.getAgenda(getDataBase(), selectedTransactionId);
                    Intent intent5 = new Intent(getActivity(), CrearPedidoActivity.class);
                    intent5.putExtra(CrearPedidoActivity.ARG_IDAGENDA, selectedTransactionId);
                    intent5.putExtra(CrearPedidoActivity.ARG_IDPEDIDO, agdPed.getPedido().getID());
                    startActivity(intent5);
                    openPane = false;
                    return true;*/
	    		case R.id.action_ver_posicion:
                    if(!ConnectionUtils.isNetAvailable(getContext()))
                    {
                        Toast.makeText(getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                    }
                    else if(selectedTransactionId != 0)
	    			{
		    			Intent intent2 = new Intent(getActivity(), MapaActivity.class);
		    			intent2.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_POSICION);
		    			intent2.putExtra(MapaActivity.ARG_AGENDA, selectedTransactionId);
		    			startActivity(intent2);
	    			}
	    			else
	    			{
	    				Toast.makeText(getContext(), "Debe seleccionar una agenda.", Toast.LENGTH_LONG).show();
	    			}
	    			return true;
	    		case R.id.action_ver_ruta:
                    if(!ConnectionUtils.isNetAvailable(getContext()))
                    {
                        Toast.makeText(getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent intent3 = new Intent(getActivity(), MapaActivity.class);
                        intent3.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_RUTAS);
                        if (selectedTransactionId != 0 && !slidingPane.isOpen())
                            intent3.putExtra(MapaActivity.ARG_AGENDA, selectedTransactionId);
                        startActivity(intent3);
                    }
	    			return true;
	    		case R.id.action_como_llegar:
                    if(!ConnectionUtils.isNetAvailable(getContext()))
                    {
                        Toast.makeText(getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                    }
                    else if(selectedTransactionId != 0)
	    			{
		    			Intent intent4 = new Intent(getActivity(), MapaActivity.class);
		    			intent4.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_LLEGAR);
		    			intent4.putExtra(MapaActivity.ARG_AGENDA, selectedTransactionId);
		    			startActivity(intent4);
	    			}
	    			else
	    			{
	    				Toast.makeText(getContext(), R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
	    			}
	    			return true;
                case R.id.action_suspender_agenda:
                    if(selectedTransactionId != 0)
                    {
                        if(PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR, 0) != 0)
                        {
                            Agenda agdNot = Agenda.getAgenda(getDataBase(), selectedTransactionId);
                            if(agdNot == null)
                                agdNot = Agenda.getAgendaClienteNull(getDataBase(), selectedTransactionId);
                            Bundle bundle = new Bundle();
                            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION);
                            bundle.putInt(AgenteDetalleFragment.ARG_AGENTE, PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR));
                            bundle.putString(AgenteDetalleFragment.ARG_TITLE, "Anular Agenda");
                            bundle.putString(AgenteDetalleFragment.ARG_MESSAGE,
                                    "Se solicita anulación de agenda del " + format1.format(agdNot.getFechaInicio()) + ", " + format2.format(agdNot.getFechaInicio()) + " de "
                                            + format3.format(agdNot.getFechaInicio()) + ", hecha al cliente " + agdNot.getNombreCompleto());
                            requestSync(bundle);
                            Toast.makeText(getContext(), R.string.message_notificacion_enviada, Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
                    }
                    return true;
	    		case R.id.action_cambiar_contacto:
	    			if(selectedTransactionId != 0)
	    			{
	    				if(!Agenda.getAgendaEstado(getDataBase(), selectedTransactionId).equalsIgnoreCase(Contants.ESTADO_NO_VISITADO))
	    					this.showDialogFragment(ContactsAgendaFragment.newInstance(selectedTransactionId), ContactsAgendaFragment.TAG);
	    				else
	    					Toast.makeText(getContext(), R.string.warning_modificar_contacto_no_visitada, Toast.LENGTH_LONG).show();
	    			}
	    			else
	    			{
	    				Toast.makeText(getContext(), R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
	    			}
	    			return true;
	    		case R.id.action_no_visita:
	    			if(selectedTransactionId != 0)
	    			{
	    				if(Agenda.getAgendaEstado(getDataBase(), selectedTransactionId).equalsIgnoreCase(Contants.ESTADO_PENDIENTE) ||
	    				   Agenda.getAgendaEstado(getDataBase(), selectedTransactionId).equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
                           motivoNoVisitaFragment = MotivoNoVisitaFragment.newInstance(selectedTransactionId);
	    					this.showDialogFragment(motivoNoVisitaFragment, MotivoNoVisitaFragment.TAG);
	    			}
	    			else
	    			{
	    				Toast.makeText(getContext(), R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
	    			}
	    			return true;
                case R.id.action_crear_agenda:
                    if(selectedTransactionId != 0) {
                        Intent intent2 = new Intent(getActivity(), CrearVisitaActivity.class);
                        intent2.putExtra(CrearVisitaFragment.ARG_IDAGENDA, (int) selectedTransactionId);
                        intent2.putExtra(CrearVisitaFragment.ARG_FROM, "Agenda");
                        startActivity(intent2);
                    }
                    else
                    {
                        Toast.makeText(getContext(), R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
                    }
                    return true;
	    		case R.id.action_reprogramar:
	    			if(selectedTransactionId != 0)
	    			{
	    				if(Agenda.getAgendaEstado(getDataBase(), selectedTransactionId).equalsIgnoreCase(Contants.ESTADO_PENDIENTE) ||
	    				   Agenda.getAgendaEstado(getDataBase(), selectedTransactionId).equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
	    				{
	    					Intent reprogramar = new Intent(getContext(), ReprogramarActivity.class);
	    					reprogramar.putExtra(ReprogramarActivity.ARG_AGENDA, selectedTransactionId);
                			startActivity(reprogramar);
	    				}
	    			}
	    			else
	    			{
	    				Toast.makeText(getContext(), R.string.warning_seleccionar_agenda, Toast.LENGTH_LONG).show();
	    			}
	    	}
	    	return super.onOptionsItemSelected(item);
	    }
	 
	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(requestCode == ObservacionesFragment.PHOTO_1 || requestCode == ObservacionesFragment.PHOTO_2 || requestCode == ObservacionesFragment.PHOTO_3)
		 {
			 rutasDetailfragment.obsFragment.onActivityResult(requestCode, resultCode, data);
		 }
         else
         {
             switch(requestCode) {
                 case REQUEST_GOOGLE_PLAY_SERVICES:
                     if (resultCode != RESULT_OK) {
                         /*mOutputText.setText(
                                 "This app requires Google Play Services. Please install " +
                                         "Google Play Services on your device and relaunch this app.");*/
                     } else {
                         getResultsFromApi();
                     }
                     break;
                 case REQUEST_ACCOUNT_PICKER:
                     if (resultCode == RESULT_OK && data != null &&
                             data.getExtras() != null) {
                         String accountName =
                                 data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                         if (accountName != null) {
                             SharedPreferences settings =
                                     this.getActivity().getPreferences(Context.MODE_PRIVATE);
                             SharedPreferences.Editor editor = settings.edit();
                             editor.putString(PREF_ACCOUNT_NAME, accountName);
                             editor.apply();
                             mCredential.setSelectedAccountName(accountName);
                             getResultsFromApi();
                         }
                     }
                     break;
                 case REQUEST_AUTHORIZATION:
                     if (resultCode == RESULT_OK) {
                         getResultsFromApi();
                     }
                     break;
                 default:
                     if(motivoNoVisitaFragment != null)
                        motivoNoVisitaFragment.onActivityResult(requestCode, resultCode, data);
                     break;
             }
         }
	}

    @Override
	public void onTransactionSelected(long id) {
		if(!mTwoPane) {
            isMainFragment = false;
            slidingPane.closePane();
        }

        selectedTransactionId = id;
        RefreshMenu();

    	rutasDetailfragment = RutasDetailFragment.newInstance(selectedTransactionId);     	
    	setFragment(R.id.content_transaction_detail, rutasDetailfragment);           					
	}

    @Override
    public void onProspectoSelected(long id) {
        if(!mTwoPane) {
            isMainFragment = false;
            slidingPane.closePane();
        }

        selectedTransactionId = id;
        RefreshMenu();

        prospectoDetailfragment = AgendaProspectoFragment.newInstance(selectedTransactionId);
        setFragment(R.id.content_transaction_detail, prospectoDetailfragment);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean allowSelectedItem() {		
		return mTwoPane;
	}

	@Override
	public void Refresh() {
		rutasDetailfragment.onResume();
		rutasListFragment.Refresh();
		
	}

    public void RefreshMenu()
    {
        menuRutas.setGroupVisible(R.id.submenu_rutas, true);
        menuRutas.findItem(R.id.submenu_rutas).setVisible(true);
        Agenda agenda = Agenda.getAgenda(getDataBase(), selectedTransactionId);
        if(!mTwoPane)
        {
            if(!isMainFragment)
            {
                menuRutas.findItem(R.id.action_search_ruta).setVisible(false);
                menuRutas.findItem(R.id.action_crear_visita).setVisible(false);
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(agenda != null);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(agenda != null);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_suspender_agenda).setVisible(true);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(true);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_crear_agenda).setVisible(true);
                        //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(true);
                        //if(agenda.getPedido() != null && agenda.getPedido().getID() != 0)
                        //    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setTitle("Editar Pedido");
                    }
                }
                if(selectedTransactionId != 0)
                {
                    String estado = Agenda.getAgendaEstado(getDataBase(), selectedTransactionId);
                    if(estado.equalsIgnoreCase(Contants.ESTADO_NO_VISITADO) || estado.equalsIgnoreCase(Contants.ESTADO_VISITADO)) {
                        for(int i = 0; i < menuRutas.size(); i ++)
                        {
                            if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                            {
                                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(false);
                                menuRutas.findItem(R.id.submenu_agenda).setVisible(false);
                                //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                            }
                        }
                    }
                    else
                        menuRutas.findItem(R.id.submenu_agenda).setVisible(true);
                    if(!estado.equalsIgnoreCase(Contants.ESTADO_PENDIENTE) && !estado.equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
                    {
                        for(int i = 0; i < menuRutas.size(); i ++)
                        {
                            if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                            {
                                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(false);
                                menuRutas.getItem(i).getSubMenu().findItem(R.id.action_suspender_agenda).setVisible(false);
                                //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                            }
                        }
                    }
                    if(estado.equalsIgnoreCase(Contants.ESTADO_GESTIONANDO))
                    {
                        for(int i = 0; i < menuRutas.size(); i ++)
                        {
                            if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                            {
                                //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(true);
                            }
                        }
                    }
                }
                if(rutasDetailfragment != null)
                    rutasDetailfragment.reDoMenu = true;
            }
            else
            {
                menuRutas.findItem(R.id.action_search_ruta).setVisible(true);
                menuRutas.findItem(R.id.action_crear_visita).setVisible(true);
                menuRutas.findItem(R.id.submenu_agenda).setVisible(false);
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_rutas)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_como_llegar).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_ver_posicion).setVisible(false);
                    }

                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_suspender_agenda).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_crear_agenda).setVisible(false);
                        //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                    }
                }
                if(rutasDetailfragment != null)
                    rutasDetailfragment.reDoMenu = false;
            }
        }
        else
        {
            menuRutas.findItem(R.id.action_search_ruta).setVisible(true);
            menuRutas.findItem(R.id.action_crear_visita).setVisible(true);
            for(int i = 0; i < menuRutas.size(); i ++)
            {
                if(menuRutas.getItem(i).getItemId() == R.id.submenu_rutas)
                {
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_como_llegar).setVisible(true);
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_ver_posicion).setVisible(true);
                }
                if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                {
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(agenda != null);
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(agenda != null);
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(true);
                    //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(agenda != null);
                }
            }
            if(selectedTransactionId != 0)
            {
                String estado = Agenda.getAgendaEstado(getDataBase(), selectedTransactionId);
                if(estado.equalsIgnoreCase(Contants.ESTADO_NO_VISITADO) || estado.equalsIgnoreCase(Contants.ESTADO_VISITADO)) {
                    for(int i = 0; i < menuRutas.size(); i ++)
                    {
                        if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                        {
                            menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                            menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(false);
                            menuRutas.findItem(R.id.submenu_agenda).setVisible(false);
                            //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                        }
                    }
                }
                else
                    menuRutas.findItem(R.id.submenu_agenda).setVisible(true);
                if(!estado.equalsIgnoreCase(Contants.ESTADO_PENDIENTE) && !estado.equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
                {
                    for(int i = 0; i < menuRutas.size(); i ++)
                    {
                        if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                        {
                            menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                            menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(false);
                            menuRutas.getItem(i).getSubMenu().findItem(R.id.action_suspender_agenda).setVisible(false);
                            //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                        }
                    }
                }
            }
            else
            {
                menuRutas.findItem(R.id.submenu_agenda).setVisible(false);
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_rutas)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_como_llegar).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_ver_posicion).setVisible(false);
                    }
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_agenda)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_cambiar_contacto).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_reprogramar).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_no_visita).setVisible(false);
                        //menuRutas.getItem(i).getSubMenu().findItem(R.id.action_asignar_pedido).setVisible(false);
                    }
                }
            }
            if(rutasDetailfragment != null)
                rutasDetailfragment.reDoMenu = true;
        }
    }

    //region Google Calendar
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    private void SyncGoogle()
    {
        mCredential = GoogleAccountCredential.usingOAuth2(
                getContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        getResultsFromApi();
    }

    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!ConnectionUtils.isNetAvailable(this.getContext())) {
            Toast.makeText(getContext(),"No network connection available.", Toast.LENGTH_LONG).show();
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this.getContext(), android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = this.getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this.getContext());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this.getContext());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                this.getActivity(),
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = new AndroidJsonFactory();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            Agenda.deleteAgendaSyncGoogle(getDataBase(), PreferenceManager.getInt(Contants.KEY_CLIENTE_DEFAULT));

            for (Event event : items) {
                Agenda agenda = Agenda.getAgendaSyncGoogle(getDataBase(), event.getId());
                if(agenda.getID() != 0)
                    agenda = Agenda.getAgenda(getDataBase(), agenda.getID());
                DateTime start = event.getStart().getDateTime();
                if(start == null)
                    start = event.getStart().getDate();
                DateTime end = null;
                if(event.getEnd() != null)
                {
                    end = event.getEnd().getDateTime();
                }

                Calendar cal_hoy = Calendar.getInstance();

                Calendar cal = Calendar.getInstance();
                Calendar calFin = Calendar.getInstance();

                cal.setTimeInMillis(start.getValue());
                if(end != null)
                {
                    calFin.setTimeInMillis(end.getValue());
                }
                else
                {
                    calFin.setTimeInMillis(cal.getTimeInMillis());
                    calFin.set(Calendar.HOUR_OF_DAY, 23);
                    calFin.set(Calendar.MINUTE, 59);
                }


                agenda.setFechaInicio(cal.getTime());
                agenda.setFechaFin(calFin.getTime());

                Cliente defaultCliente = Cliente.getClienteIDServer(getDataBase(), PreferenceManager.getInt(Contants.KEY_CLIENTE_DEFAULT), true);
                if(defaultCliente == null)
                {
                    showDialogMessage("El cliente default no lo tiene configurado en su ruta. Por favor, consulte con su administrador del sistema.");
                }
                agenda.setCliente(defaultCliente);
                agenda.setClienteDireccion(agenda.getCliente().getClienteDirecciones().get(0));
                agenda.setCiudad(agenda.getCliente().getClienteDirecciones().get(0).getCiudadDescripcion());
                agenda.setDireccion(agenda.getCliente().getClienteDirecciones().get(0).getDireccion());
                agenda.setEstadoAgenda(Contants.ESTADO_PENDIENTE);
                agenda.setEstadoAgendaDescripcion(Contants.DESC_PENDIENTE);
                agenda.setIdCliente((int) agenda.getCliente().getIdCliente());
                agenda.set_idCliente(agenda.getCliente().getID());
                agenda.setIdClienteDireccion(agenda.getClienteDireccion().getIdClienteDireccion());
                agenda.set_idClienteDireccion(agenda.getClienteDireccion().getID());
                agenda.setIdRuta(0);
                agenda.setNombreCompleto(agenda.getCliente().getNombreCompleto().trim());
                agenda.setObservaciones(event.getSummary());
                Calendar fc = Calendar.getInstance();
                fc.set(Calendar.MILLISECOND, 0);
                agenda.setFechaCreacion(fc.getTime());
                //agenda.setID(0);
                agenda.setIdAgenda(0);
                agenda.setFoto1Ext(event.getId());

                agenda.setEnviado(false);
                if(agenda.getID() == 0)
                    Agenda.insert(getDataBase(), agenda);
                else
                    Agenda.update(getDataBase(), agenda);
            }
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_AGENDAS);
            requestSync(bundle);
            return eventStrings;
        }


        @Override
        protected void onPreExecute() {
            showDialogProgress("Sincronizando", "Sincronizando con cuenta de Google");
        }

        @Override
        protected void onPostExecute(List<String> output) {
            if (output == null || output.size() == 0) {
                //mOutputText.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                //mOutputText.setText(TextUtils.join("\n", output));
            }
            closeDialogProgress();
            rutasListFragment.onResume();
        }

        @Override
        protected void onCancelled() {
            closeDialogProgress();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            RutasFragment.REQUEST_AUTHORIZATION);
                } else {
                    //mOutputText.setText("The following error occurred:\n"
                    //        + mLastError.getMessage());
                }
            } else {
                //mOutputText.setText("Request cancelled.");
            }
        }
    }


    //endregion
}
