package rp3.marketforce.oportunidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import rp3.app.BaseFragment;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.oportunidad.AgendaOportunidad;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.widget.SlidingPaneLayout;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadFragment extends BaseFragment implements OportunidadListFragment.OportunidadListFragmentListener {

    private static final int PARALLAX_SIZE = 0;
    public static final int FILTER_CODE = 1001;

    private OportunidadListFragment transactionListFragment;
    private OportunidadDetailFragment transactionDetailFragment;
    private Oportunidad selectedOportunidad;
    private SlidingPaneLayout slidingPane;
    private String textSearch;

    private Menu menu;
    private Intent filtroData;
    public boolean mTwoPane = false;
    public boolean isActiveListFragment = true;
    private long selectedOportunidadId;
    private boolean filtro = false;
    private OportunidadBitacoraListFragment subFragment;

    public static OportunidadFragment newInstance(int idCurrentOportunidad) {
        OportunidadFragment fragment = new OportunidadFragment();
        Bundle args = new Bundle();
        args.putInt(OportunidadListFragment.ARG_CODIGOOPORTUNIDAD, idCurrentOportunidad);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_oportunidad, R.menu.fragment_oportunidad_menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentOportunidad = -1;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentOportunidad = bundle.getInt(OportunidadListFragment.ARG_CODIGOOPORTUNIDAD);
        }

        setRetainInstance(true);
        transactionListFragment = OportunidadListFragment.newInstance(true, null,currentOportunidad);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_CODE)
        {

            filtroData = data;
            if(resultCode == Activity.RESULT_OK)
                transactionListFragment.aplicarFiltro(data);
            else
            {
                filtro = false;
                transactionListFragment.filtro = false;
            }
            RefreshMenu();
        }
        else
        {
            if (resultCode == RESULT_OK) {
                if (resultCode == RESULT_OK && null != data) {
                    if(subFragment != null)
                        subFragment.onActivityResult(requestCode, resultCode, data);
                    if(transactionDetailFragment != null && transactionDetailFragment.agenteDetalleFragment != null)
                        transactionDetailFragment.agenteDetalleFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
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
                rootView.findViewById(R.id.content_transaction_list).getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT)
            mTwoPane = true;
        else
            mTwoPane = false;

        if(!hasFragment(R.id.content_transaction_list))
            setFragment(R.id.content_transaction_list, transactionListFragment );

        slidingPane.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener(){

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPanelOpened(View panel) {
                isActiveListFragment = true;
                //getActivity().getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
                RefreshMenu();
                if(transactionListFragment != null)
                    transactionListFragment.onResume();
            }

            @Override
            public void onPanelClosed(View panel) {
                isActiveListFragment = false;
                RefreshMenu();

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

        if(selectedOportunidadId != 0 && !isActiveListFragment){
            if(!mTwoPane)
                slidingPane.closePane();
        }
    }

    @Override
    public void onAfterCreateOptionsMenu(Menu menu) {
        this.menu = menu;
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
        searchPlate.setHint(R.string.hint_search_oportunidad);

        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (transactionListFragment != null)
                    transactionListFragment.searchTransactions(query);
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText) && !TextUtils.isEmpty(textSearch)) {
                    try {
                        transactionListFragment.searchTransactions(newText);
                    } catch (Exception ex) {

                    }
                }
                textSearch = newText;

                return true;

            }
        });
        RefreshMenu();
    }

    private void RefreshMenu() {
        if (!mTwoPane) {
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            if(selectedOportunidad != null)
                menu.findItem(R.id.action_edit).setVisible(!isActiveListFragment && selectedOportunidad.getEstado().equalsIgnoreCase("A"));
            else
                menu.findItem(R.id.action_edit).setVisible(!isActiveListFragment);
            menu.findItem(R.id.action_crear_oportunidad).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_filtro).setVisible(isActiveListFragment && !filtro);
            menu.findItem(R.id.action_quitar_filtro).setVisible(isActiveListFragment && filtro);
            menu.findItem(R.id.action_ver_bitacora).setVisible(!isActiveListFragment);
        } else {
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.action_crear_oportunidad).setVisible(true);
            menu.findItem(R.id.action_filtro).setVisible(!filtro);
            menu.findItem(R.id.action_quitar_filtro).setVisible(filtro);
            if(selectedOportunidad != null) {
                menu.findItem(R.id.action_edit).setVisible(selectedOportunidad.getEstado().equalsIgnoreCase("A"));
                menu.findItem(R.id.action_ver_bitacora).setVisible(true);
            }
            else {
                menu.findItem(R.id.action_edit).setVisible(selectedOportunidadId != 0);
                menu.findItem(R.id.action_ver_bitacora).setVisible(selectedOportunidadId != 0);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_crear_oportunidad:
                if(AgendaOportunidad.getAgendaOportunidadGestionado(getDataBase()).getID() == 0 && Agenda.getCountVisitados(getDataBase(), Contants.ESTADO_GESTIONANDO, 0, Agenda.getLastAgenda(getDataBase())) == 0) {
                    Intent intent2 = new Intent(getContext(), CrearOportunidadActivity.class);
                    startActivity(intent2);
                }
                else
                {
                    showDialogMessage("Para crear un nuevo prospecto, debe finalizar la gestión activa.");
                }
                break;
            case R.id.action_edit:
                Intent intent3 = new Intent(getContext(), CrearOportunidadActivity.class);
                intent3.putExtra(CrearOportunidadActivity.ARG_ID, selectedOportunidadId);
                startActivity(intent3);
                break;
            case R.id.action_ver_bitacora:
                subFragment = OportunidadBitacoraListFragment.newInstance((int) selectedOportunidadId);
                showDialogFragment(subFragment, "Bitácora", selectedOportunidad.getDescripcion());
                break;
            case R.id.action_quitar_filtro:
                filtroData.setClass(getContext(), FiltroOportunidadActivity.class);
                filtro = true;
                startActivityForResult(filtroData, FILTER_CODE);
                break;
            case R.id.action_filtro:
                Intent intent = new Intent(getContext(), FiltroOportunidadActivity.class);
                filtro = true;
                startActivityForResult(intent, FILTER_CODE);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOportunidadSelected(Oportunidad oportunidad) {
        if(!mTwoPane) {
            //isMainFragment = false;
            slidingPane.closePane();
        }

        RefreshMenu();

        selectedOportunidad = oportunidad;
        selectedOportunidadId = oportunidad.getID();
        transactionDetailFragment = OportunidadDetailFragment.newInstance(selectedOportunidadId);
        setFragment(R.id.content_transaction_detail, transactionDetailFragment);

    }

    @Override
    public void onFinalizaConsulta() {

    }

    @Override
    public void onFinalizaGestion() {
        slidingPane.openPane();
    }

    @Override
    public boolean allowSelectedItem() {
        return false;
    }

}
