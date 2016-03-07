package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientDetailFragment;
import rp3.marketforce.cliente.ClientListFragment;
import rp3.marketforce.cliente.CrearClienteActivity;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.ruta.MapaActivity;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import rp3.widget.SlidingPaneLayout;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoFragment extends BaseFragment implements PedidoListFragment.PedidoListFragmentListener,PedidoDetailFragment.PedidoDetailFragmentListener {

    private static final int PARALLAX_SIZE = 0;
    private static final int DIALOG_SYNC_PRODUCTOS = 1;

    private PedidoListFragment transactionListFragment;
    private PedidoDetailFragment transactionDetailFragment;
    private SlidingPaneLayout slidingPane;

    private Menu menu;
    public boolean mTwoPane = false;
    public boolean isActiveListFragment = true;
    private long selectedClientId;
    private boolean isContact = false;

    public static PedidoFragment newInstance(int transactionTypeId) {
        PedidoFragment fragment = new PedidoFragment();
        return fragment;
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_client,R.menu.fragment_pedido_menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        transactionListFragment = PedidoListFragment.newInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
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

        if(selectedClientId != 0){
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

        if(null!=searchManager ) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }
        searchView.setIconifiedByDefault(false);
        RefreshMenu();
    }

    private void RefreshMenu(){
        Pedido ped = Pedido.getPedido(getDataBase(), selectedClientId);
        if(!mTwoPane){
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_crear_pedido).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_editar_pedido).setVisible(!isActiveListFragment);
            menu.findItem(R.id.action_editar_pedido).setVisible(selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("P"));
        }
        else{

            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_crear_pedido).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_editar_pedido).setVisible(selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("P"));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_editar_pedido:
                Intent intent2 = new Intent(getActivity(), CrearPedidoActivity.class);
                intent2.putExtra(CrearPedidoActivity.ARG_IDPEDIDO, selectedClientId);
                startActivity(intent2);
                break;
            case R.id.action_crear_pedido:
                Intent intent = new Intent(this.getActivity(), CrearPedidoActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sincronizar_productos:
                if(!ConnectionUtils.isNetAvailable(this.getContext()))
                {
                    Toast.makeText(this.getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                }
                else {
                    if(Producto.getProductos(getDataBase()).size() <= 0)
                    {
                        showDialogConfirmation(DIALOG_SYNC_PRODUCTOS, R.string.message_sin_productos, R.string.action_sincronizar_productos);
                    }
                    else
                    {
                        showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PRODUCTOS);
                        requestSync(bundle);
                    }
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PRODUCTOS);
        requestSync(bundle);
    }

    @Override
    public void onPermisoChanged(Pedido permiso) {

    }

    @Override
    public void onPermisoSelected(Pedido pedido) {
        selectedClientId = pedido.getID();

        if(!mTwoPane) {
            slidingPane.closePane();
            isActiveListFragment = false;
        }

        RefreshMenu();

        transactionDetailFragment = PedidoDetailFragment.newInstance(pedido);
        setFragment(R.id.content_transaction_detail, transactionDetailFragment);
    }

    @Override
    public void onFinalizaConsulta() {

    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);
        if(data != null && data.getString(SyncAdapter.ARG_SYNC_TYPE).equalsIgnoreCase(SyncAdapter.SYNC_TYPE_PRODUCTOS))
        {
            closeDialogProgress();
        }
    }

    @Override
    public boolean allowSelectedItem() {
        return mTwoPane;
    }
}
