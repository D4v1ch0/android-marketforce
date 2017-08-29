package rp3.berlin.tracking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import rp3.app.BaseFragment;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.pedido.Pedido;
import rp3.berlin.models.pedido.PedidoView;
import rp3.berlin.pedido.CrearPedidoFragment;
import rp3.berlin.pedido.PedidoDetailFragment;
import rp3.berlin.pedido.PedidoDetalleAdapter;
import rp3.berlin.pedido.PedidoFragment;
import rp3.berlin.sync.SyncAdapter;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;
import rp3.util.Convert;
import rp3.util.Screen;

/**
 * Created by Gustavo Meregildo on 24/08/2017.
 */

public class TrackingDetailFragment extends BaseFragment {
    public static final String ARG_ITEM_ID = "id";
    private static final String ARG_CLIENTE = "cliente";
    private static final String ARG_OV = "ov";
    private static final String ARG_FACTURA = "factura";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private long clientId;
    private String cliente, ov, factura;
    private Pedido pedido;
    private TrackingFragment detailFragmentListener;
    private NumberFormat numberFormat;
    private SimpleDateFormat format1;

    public static TrackingDetailFragment newInstance(PedidoView pedido) {
        Bundle arguments = new Bundle();
        arguments.putInt(TrackingDetailFragment.ARG_ITEM_ID, pedido.getIdPedido());
        arguments.putString(TrackingDetailFragment.ARG_CLIENTE, pedido.getNombreCliente());
        arguments.putString(TrackingDetailFragment.ARG_OV, pedido.getOrden());
        arguments.putString(TrackingDetailFragment.ARG_FACTURA, pedido.getFactura());
        TrackingDetailFragment fragment = new TrackingDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
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
            clientId = getArguments().getInt(ARG_ITEM_ID);
        }
        if (getArguments().containsKey(ARG_CLIENTE)) {
            cliente = getArguments().getString(ARG_CLIENTE);
        }
        if (getArguments().containsKey(ARG_FACTURA)) {
            factura = getArguments().getString(ARG_FACTURA);
        }
        if (getArguments().containsKey(ARG_OV)) {
            ov = getArguments().getString(ARG_OV);
        }

        if (clientId != 0) {
            super.setContentView(R.layout.fragment_tracking_detail);
        } else {
            super.setContentView(R.layout.base_content_no_selected_item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        detailFragmentListener.RefreshMenu();

        ((TextView) getRootView().findViewById(R.id.tracking_pedido)).setText(clientId + "");
        ((TextView) getRootView().findViewById(R.id.tracking_cliente)).setText(cliente);
        if(ov != null)
            ((TextView) getRootView().findViewById(R.id.tracking_ov)).setText(ov);
        if(factura != null)
            ((TextView) getRootView().findViewById(R.id.tracking_factura)).setText(factura);

        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GET_INFO_PEDIDOS);
        bundle.putLong(ARG_ITEM_ID, clientId);
        requestSync(bundle);

        showDialogProgress("Cargando", "Consultando Información de Pedido");

    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GET_INFO_PEDIDOS)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                String desc = data.getString(ARG_ITEM_ID);
                ShowPedidos(desc);
            }
        }
    }

    public void ShowPedidos(String json)
    {
        format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            JSONArray jsonArray = new JSONArray(json);
            Calendar cal = Calendar.getInstance();

            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Date fecha = Convert.getDateFromDotNetTicks(jsonObject.getLong("FechaTicks"));
                if(jsonObject.getString("Estado").equalsIgnoreCase("ING"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_creado)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_creado)).setText(format1.format(fecha));
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("CRD"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_cerrado)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_cerrado)).setText(format1.format(fecha));
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("REC"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_recibido)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_recibido)).setText(format1.format(fecha));
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("APR"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_aprobado)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_aprobado)).setText(format1.format(fecha));
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("RCH"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_aprobado)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_aprobado)).setText(format1.format(fecha));
                    ((TextView) getRootView().findViewById(R.id.tracking_estado_aprobado_label)).setText("Rechazado");
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("ENV"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_enviado)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_enviado)).setText(format1.format(fecha));
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("CEI"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_creado_infor)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_creado_infor)).setText(format1.format(fecha));
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("LIB"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_liberado)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_liberado)).setText(format1.format(fecha));
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("LAN"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_lanzado)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_lanzado)).setText(format1.format(fecha));
                }
                if(jsonObject.getString("Estado").equalsIgnoreCase("FAC"))
                {
                    ((ImageView) getRootView().findViewById(R.id.tracking_estado_facturado)).setImageResource(R.drawable.circle_visited);
                    ((TextView) getRootView().findViewById(R.id.tracking_fecha_facturado)).setText(format1.format(fecha));
                }

            }
        }
        catch (Exception ex)
        {

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getParentFragment()!=null){
            detailFragmentListener = (TrackingFragment)getParentFragment();
        }else{
            //detailFragmentListener = (PedidoFragment) activity;
            setRetainInstance(true);
        }
    }
}
