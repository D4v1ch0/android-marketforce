package rp3.auna.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.models.GeneralValue;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.pedido.Pedido;
import rp3.util.Screen;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoDetailFragment extends BaseFragment {

    private static final String TAG = PedidoDetailFragment.class.getSimpleName();
    public static final String STATE_CLIENT_ID = "clientId";
    private static final String ARG_ITEM_ID = "id";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private long clientId;
    private Pedido pedido;
    private PedidoFragment detailFragmentListener;
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
        Log.d(TAG,"onCreate...");
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
        Log.d(TAG,"onResume...");
        detailFragmentListener.RefreshMenu();
        if (clientId != 0) {
            pedido = Pedido.getPedido(getDataBase(), clientId, true);
        }

        if(pedido==null) return;
        ((TextView) getRootView().findViewById(R.id.pedido_cliente)).setText(pedido.getNombre());
        ((TextView) getRootView().findViewById(R.id.pedido_email)).setText(pedido.getEmail());
        PedidoDetalleAdapter adapter = new PedidoDetalleAdapter(this.getContext(), pedido.getPedidoDetalles());
        ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setAdapter(adapter);
        try {
            GeneralValue imp = GeneralValue.getGeneralValue(getDataBase(), Contants.POS_PORCIMP);
            if(imp != null)
                ((TextView) getRootView().findViewById(R.id.pedido_impuestos_label)).setText("Impuestos " + imp.getValue() + "%");
        }
        catch (Exception ex)
        {}


        if(pedido.getTipoDocumento().equalsIgnoreCase("FA"))
            this.getActivity().setTitle("Factura No. " + pedido.getNumeroDocumento());
        if(pedido.getTipoDocumento().equalsIgnoreCase("NC"))
            this.getActivity().setTitle("Nota de Crédito No. " + pedido.getNumeroDocumento());
        if(pedido.getTipoDocumento().equalsIgnoreCase("CT"))
            this.getActivity().setTitle("Cotización No. " + pedido.getNumeroDocumento());
        if(pedido.getTipoDocumento().equalsIgnoreCase("PD"))
            this.getActivity().setTitle("Pedido No. " + pedido.getNumeroDocumento());

        if (pedido.getEstado().equals("N")) {
            ((ImageView) getRootView().findViewById(R.id.pedido_estado)).setImageDrawable(this.getResources().getDrawable(R.drawable.circle_pending));
        } else if (pedido.getEstado().equals("C")) {
            ((ImageView) getRootView().findViewById(R.id.pedido_estado)).setImageDrawable(this.getResources().getDrawable(R.drawable.circle_visited));
        } else {
            ((ImageView) getRootView().findViewById(R.id.pedido_estado)).setImageDrawable(this.getResources().getDrawable(R.drawable.circle_unvisited));
        }

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(CrearPedidoFragment.getPedidoCantidad(pedido.getPedidoDetalles()) + "");
        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getValorTotal()));
        ((TextView) getRootView().findViewById(R.id.pedido_descuentos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getTotalDescuentos()));
        ((TextView) getRootView().findViewById(R.id.pedido_impuestos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getTotalImpuestos()));
        ((TextView) getRootView().findViewById(R.id.pedido_base_cero)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getBaseImponibleCero()));
        ((TextView) getRootView().findViewById(R.id.pedido_base_imponible)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getBaseImponible()));
        ((TextView) getRootView().findViewById(R.id.pedido_subtotal)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getSubtotal()));
        ((TextView) getRootView().findViewById(R.id.pedido_redondeo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getRedondeo()));
        ((TextView) getRootView().findViewById(R.id.pedido_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getExcedente()));
        ((TextView) getRootView().findViewById(R.id.pedido_neto)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(pedido.getSubtotal() - pedido.getTotalDescuentos()));
        ((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).setText(pedido.getObservaciones());

        if(Screen.isLargeLayoutSize(this.getContext())) {
            if (GeneralValue.getGeneralValue(getDataBase(), Contants.POS_ROUNDSPECI).getValue().equalsIgnoreCase("1")) {
                getRootView().findViewById(R.id.pedido_redondeo).setVisibility(View.VISIBLE);
                getRootView().findViewById(R.id.pedido_redondeo_label).setVisibility(View.VISIBLE);
                getRootView().findViewById(R.id.pedido_redondeo_line).setVisibility(View.VISIBLE);
                getRootView().findViewById(R.id.pedido_cantidad).setVisibility(View.GONE);
                getRootView().findViewById(R.id.pedido_cantidad_label).setVisibility(View.GONE);
                getRootView().findViewById(R.id.pedido_cantidad_line).setVisibility(View.GONE);
            } else {
                getRootView().findViewById(R.id.pedido_cantidad).setVisibility(View.VISIBLE);
                getRootView().findViewById(R.id.pedido_cantidad_label).setVisibility(View.VISIBLE);
                getRootView().findViewById(R.id.pedido_cantidad_line).setVisibility(View.VISIBLE);
                getRootView().findViewById(R.id.pedido_redondeo).setVisibility(View.GONE);
                getRootView().findViewById(R.id.pedido_redondeo_label).setVisibility(View.GONE);
                getRootView().findViewById(R.id.pedido_redondeo_line).setVisibility(View.GONE);
            }
        }

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getParentFragment()!=null){
            detailFragmentListener = (PedidoFragment)getParentFragment();
        }else{
            //detailFragmentListener = (PedidoFragment) activity;
            setRetainInstance(true);
        }
    }

    /**
     *
     * Ciclo de vida
     *
     */

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}