package rp3.marketforce.ruta;

import rp3.app.BaseFragment;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.util.ConnectionUtils;
import rp3.util.Screen;
import rp3.widget.SlidingPaneLayout;
import rp3.widget.SlidingPaneLayout.PanelSlideListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.lang.reflect.Field;

public class RutasFragment extends BaseFragment implements RutasListFragment.TransactionListFragmentListener, ContactsAgendaFragment.SaveContactsListener{

	public static final String ARG_TRANSACTIONTYPEID = "transactionTypeId";
	private static final int PARALLAX_SIZE = 0;
	
	public boolean mTwoPane = false;
	private long selectedTransactionId;
	private String textSearch;
//	private MenuItem menuItemActionEdit;
//    private MenuItem menuItemActionDiscard;
	
	private RutasListFragment rutasListFragment;
	private RutasDetailFragment rutasDetailfragment;
	private ObservacionesFragment obsFragment;
	private SlidingPaneLayout slidingPane;
	private boolean openPane = true;
    private Menu menuRutas;
    private boolean isMainFragment = true;


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
		if(selectedTransactionId != 0 && openPane){
			if(!mTwoPane)			
				slidingPane.closePane();			
		}
		else
			openPane = true;
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
	    		case R.id.action_search_ruta:    	
	    			return true;
	    		case R.id.action_crear_visita:
	    			Intent intent = new Intent(getActivity(), CrearVisitaActivity.class);
	    			startActivity(intent);
	    			openPane = false;
	    			return true;
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
	    					this.showDialogFragment(MotivoNoVisitaFragment.newInstance(selectedTransactionId), MotivoNoVisitaFragment.TAG);
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
	}

    @Override
	public void onTransactionSelected(long id) {
		if(!mTwoPane) {
            isMainFragment = false;
            slidingPane.closePane();
        }

        RefreshMenu();
		
		selectedTransactionId = id;
    	rutasDetailfragment = RutasDetailFragment.newInstance(selectedTransactionId);     	
    	setFragment(R.id.content_transaction_detail, rutasDetailfragment);           					
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
        if(!mTwoPane)
        {
            if(!isMainFragment)
            {
                menuRutas.findItem(R.id.action_search_ruta).setVisible(false);
                menuRutas.findItem(R.id.action_crear_visita).setVisible(false);
                menuRutas.findItem(R.id.action_cambiar_contacto).setVisible(true);
                menuRutas.findItem(R.id.action_no_visita).setVisible(true);
                menuRutas.findItem(R.id.action_reprogramar).setVisible(true);
                if(selectedTransactionId != 0)
                {
                    String estado = Agenda.getAgendaEstado(getDataBase(), selectedTransactionId);
                    if(estado.equalsIgnoreCase(Contants.ESTADO_NO_VISITADO) || estado.equalsIgnoreCase(Contants.ESTADO_VISITADO))
                        menuRutas.findItem(R.id.action_cambiar_contacto).setVisible(false);
                    if(!estado.equalsIgnoreCase(Contants.ESTADO_PENDIENTE) && !estado.equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
                    {
                        menuRutas.findItem(R.id.action_no_visita).setVisible(false);
                        menuRutas.findItem(R.id.action_reprogramar).setVisible(false);
                    }
                }
                if(rutasDetailfragment != null)
                    rutasDetailfragment.reDoMenu = true;
            }
            else
            {
                menuRutas.findItem(R.id.action_search_ruta).setVisible(true);
                menuRutas.findItem(R.id.action_crear_visita).setVisible(true);
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_rutas)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_como_llegar).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_ver_posicion).setVisible(false);
                    }
                }
                menuRutas.findItem(R.id.action_cambiar_contacto).setVisible(false);
                menuRutas.findItem(R.id.action_no_visita).setVisible(false);
                menuRutas.findItem(R.id.action_reprogramar).setVisible(false);
                if(rutasDetailfragment != null)
                    rutasDetailfragment.reDoMenu = false;
            }
        }
        else
        {
            menuRutas.findItem(R.id.action_search_ruta).setVisible(true);
            menuRutas.findItem(R.id.action_crear_visita).setVisible(true);
            menuRutas.findItem(R.id.action_cambiar_contacto).setVisible(true);
            menuRutas.findItem(R.id.action_no_visita).setVisible(true);
            menuRutas.findItem(R.id.action_reprogramar).setVisible(true);
            for(int i = 0; i < menuRutas.size(); i ++)
            {
                if(menuRutas.getItem(i).getItemId() == R.id.submenu_rutas)
                {
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_como_llegar).setVisible(true);
                    menuRutas.getItem(i).getSubMenu().findItem(R.id.action_ver_posicion).setVisible(true);
                }
            }
            if(selectedTransactionId != 0)
            {
                String estado = Agenda.getAgendaEstado(getDataBase(), selectedTransactionId);
                if(estado.equalsIgnoreCase(Contants.ESTADO_NO_VISITADO) || estado.equalsIgnoreCase(Contants.ESTADO_VISITADO))
                    menuRutas.findItem(R.id.action_cambiar_contacto).setVisible(false);
                if(!estado.equalsIgnoreCase(Contants.ESTADO_PENDIENTE) && !estado.equalsIgnoreCase(Contants.ESTADO_REPROGRAMADO))
                {
                    menuRutas.findItem(R.id.action_no_visita).setVisible(false);
                    menuRutas.findItem(R.id.action_reprogramar).setVisible(false);
                }
            }
            else
            {
                menuRutas.findItem(R.id.action_cambiar_contacto).setVisible(false);
                menuRutas.findItem(R.id.action_no_visita).setVisible(false);
                menuRutas.findItem(R.id.action_reprogramar).setVisible(false);
                for(int i = 0; i < menuRutas.size(); i ++)
                {
                    if(menuRutas.getItem(i).getItemId() == R.id.submenu_rutas)
                    {
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_como_llegar).setVisible(false);
                        menuRutas.getItem(i).getSubMenu().findItem(R.id.action_ver_posicion).setVisible(false);
                    }
                }
            }
            if(rutasDetailfragment != null)
                rutasDetailfragment.reDoMenu = true;
        }
    }
}
