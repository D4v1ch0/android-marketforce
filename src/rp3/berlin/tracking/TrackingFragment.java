package rp3.berlin.tracking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;

import rp3.app.BaseFragment;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.pedido.ControlCaja;
import rp3.berlin.models.pedido.Pago;
import rp3.berlin.models.pedido.Pedido;
import rp3.berlin.models.pedido.PedidoView;
import rp3.berlin.models.pedido.Producto;
import rp3.berlin.pedido.AnularTransaccionFragment;
import rp3.berlin.pedido.BodegaFragment;
import rp3.berlin.pedido.ConsultaPrecioActivity;
import rp3.berlin.pedido.CrearPedidoActivity;
import rp3.berlin.pedido.PedidoParametrosFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;
import rp3.widget.SlidingPaneLayout;

/**
 * Created by Gustavo Meregildo on 24/08/2017.
 */

public class TrackingFragment extends BaseFragment implements TrackingListFragment.TrackingListFragmentListener {

    private static final int PARALLAX_SIZE = 0;

    private TrackingListFragment transactionListFragment;
    private TrackingDetailFragment transactionDetailFragment;
    private SlidingPaneLayout slidingPane;

    private Menu menu;
    public boolean mTwoPane = false;
    public boolean isActiveListFragment = true;
    private long selectedClientId;
    private boolean isContact = false;
    private boolean allProducts = false;
    private String textSearch;
    private AnularTransaccionFragment anulaFragment;

    public static rp3.berlin.tracking.TrackingFragment newInstance(int transactionTypeId) {
        rp3.berlin.tracking.TrackingFragment fragment = new rp3.berlin.tracking.TrackingFragment();
        return fragment;
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_client, R.menu.fragment_tracking);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        transactionListFragment = TrackingListFragment.newInstance();

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
                getActivity().setTitle("Transacciones");
                //transactionListFragment.searchTransactions(textSearch);
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

        /*if(selectedClientId != 0){
            if(!mTwoPane)
                slidingPane.closePane();
        }*/
    }

    @Override
    public void onAfterCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        RefreshMenu();
    }

    public void RefreshMenu(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_clean:
                //Limpiar
                break;
            case R.id.action_filtro:
                if(transactionListFragment != null)
                    transactionListFragment.ConsultarPedidos();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);
    }

    @Override
    public void onPedidoSelected(PedidoView pedido) {

    }

    @Override
    public void onFinalizaConsulta() {

    }

    @Override
    public boolean allowSelectedItem() {
        return false;
    }
}
