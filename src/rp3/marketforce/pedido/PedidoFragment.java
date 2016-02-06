package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    private static final int DIALOG_ANULACION = 2;

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
                getActivity().setTitle("Pedidos");
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

        RefreshMenu();
    }

    private void RefreshMenu(){
        Pedido ped = Pedido.getPedido(getDataBase(), selectedClientId);
        if(!mTwoPane){
            menu.findItem(R.id.action_arqueo_caja).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_crear_pedido).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_anular_pedido).setVisible(!isActiveListFragment);
            menu.findItem(R.id.action_sincronizar_productos).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_anular_pedido).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C"));
            menu.findItem(R.id.action_nota_credito).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && ped.getTipoDocumento().equalsIgnoreCase("FA"));
            //menu.findItem(R.id.action_nota_credito).setVisible(false);
        }
        else{

            menu.findItem(R.id.action_arqueo_caja).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_crear_pedido).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_anular_pedido).setVisible(selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C"));
            menu.findItem(R.id.action_nota_credito).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && ped.getTipoDocumento().equalsIgnoreCase("FA"));
            //menu.findItem(R.id.action_nota_credito).setVisible(false);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_arqueo_caja:
                Intent intent = new Intent(getContext(), ArqueoCajaActivity.class);
                startActivity(intent);
                break;
            case R.id.action_anular_pedido:
                if(transactionDetailFragment != null)
                {
                    showDialogFragment(AnularTransaccionFragment.newInstance(selectedClientId), "Anular", "Anular Transacción");
                }
                break;
            case R.id.action_nota_credito:
                if(PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO, 0) != 0) {
                    Intent intent2 = new Intent(getContext(), CrearPedidoActivity.class);
                    intent2.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "NC");
                    intent2.putExtra(CrearPedidoActivity.ARG_IDPEDIDO, selectedClientId);
                    startActivity(intent2);
                    break;
                }
                else
                {
                    Toast.makeText(this.getContext(), "Su usuario no tiene asignado una caja.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_crear_pedido:
                if(PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA, 0) != 0) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getContext(),
                            android.R.layout.select_dialog_item);
                    arrayAdapter.add("Factura");
                    //arrayAdapter.add("Nota de Crédito");

                    builderSingle.setAdapter(
                            arrayAdapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            Intent intent = new Intent(getContext(), CrearPedidoActivity.class);
                                            intent.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "FA");
                                            startActivity(intent);
                                            break;
                                        case 1:
                                            Intent intent2 = new Intent(getContext(), CrearPedidoActivity.class);
                                            intent2.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "NC");
                                            startActivity(intent2);
                                            break;
                                    }

                                }
                            });
                    builderSingle.show();

                }
                else
                {
                    Toast.makeText(this.getContext(), "Su usuario no tiene asignado una caja.", Toast.LENGTH_LONG).show();
                }
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
        switch (id) {
            case DIALOG_SYNC_PRODUCTOS:
                showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PRODUCTOS);
                requestSync(bundle);
                break;

        }

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
