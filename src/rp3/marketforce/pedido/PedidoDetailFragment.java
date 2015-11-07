package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.Pedido;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoDetailFragment extends BaseFragment {


    public static final String STATE_CLIENT_ID = "clientId";
    private static final String ARG_ITEM_ID = "id";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private long clientId;
    private Pedido pedido;
    private PedidoDetailFragmentListener detailFragmentListener;
    private NumberFormat numberFormat;

    public static PedidoDetailFragment newInstance(Pedido pedido) {
        Bundle arguments = new Bundle();
        arguments.putLong(PedidoDetailFragment.ARG_ITEM_ID, pedido.getID());
        PedidoDetailFragment fragment = new PedidoDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public interface PedidoDetailFragmentListener{
        void onPermisoChanged(Pedido permiso);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        if (getParentFragment() == null)
            setRetainInstance(true);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            clientId = getArguments().getLong(ARG_ITEM_ID);
        } else if (savedInstanceState != null) {
            clientId = savedInstanceState.getLong(STATE_CLIENT_ID);
        }

        if (clientId != 0) {
            super.setContentView(R.layout.fragment_pedido_detail);
        } else {
            super.setContentView(R.layout.base_content_no_selected_item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (clientId != 0) {
            pedido = Pedido.getPedido(getDataBase(), clientId);
        }

        if(pedido==null) return;
        ((TextView) getRootView().findViewById(R.id.pedido_cliente)).setText(pedido.getCliente().getNombreCompleto());
        ((TextView) getRootView().findViewById(R.id.pedido_email)).setText(pedido.getEmail());
        PedidoDetalleAdapter adapter = new PedidoDetalleAdapter(this.getContext(), pedido.getPedidoDetalles());
        ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setAdapter(adapter);

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(CrearPedidoFragment.getPedidoCantidad(pedido.getPedidoDetalles()) + "");
        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText("$ " + numberFormat.format(pedido.getValorTotal()));

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getParentFragment()!=null){
            detailFragmentListener = (PedidoDetailFragmentListener)getParentFragment();
        }else{
            detailFragmentListener = (PedidoDetailFragmentListener) activity;
            setRetainInstance(true);
        }
    }
}