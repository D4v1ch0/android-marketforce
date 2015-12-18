package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
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

        if(pedido.getTipoDocumento().equalsIgnoreCase("FA"))
            this.getActivity().setTitle("Factura No. " + pedido.getNumeroDocumento());
        if(pedido.getTipoDocumento().equalsIgnoreCase("NC"))
            this.getActivity().setTitle("Nota de Crédito No. " + pedido.getNumeroDocumento());

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(CrearPedidoFragment.getPedidoCantidad(pedido.getPedidoDetalles()) + "");
        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getValorTotal()));
        ((TextView) getRootView().findViewById(R.id.pedido_descuentos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getTotalDescuentos()));
        ((TextView) getRootView().findViewById(R.id.pedido_impuestos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getTotalImpuestos()));
        ((TextView) getRootView().findViewById(R.id.pedido_base_cero)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getBaseImponibleCero()));
        ((TextView) getRootView().findViewById(R.id.pedido_base_imponible)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getBaseImponible()));
        ((TextView) getRootView().findViewById(R.id.pedido_subtotal)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getSubtotal()));
        ((TextView) getRootView().findViewById(R.id.pedido_redondeo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getRedondeo()));
        ((TextView) getRootView().findViewById(R.id.pedido_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getExcedente()));
        ((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).setText(pedido.getObservaciones());

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