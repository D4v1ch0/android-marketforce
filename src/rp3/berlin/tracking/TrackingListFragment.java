package rp3.berlin.tracking;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.pedido.PedidoView;
import rp3.berlin.pedido.PedidoAdapter;
import rp3.berlin.sync.SyncAdapter;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.MessageCollection;
import rp3.util.Convert;

/**
 * Created by Gustavo Meregildo on 24/08/2017.
 */

public class TrackingListFragment extends BaseFragment {

    public static String ARG_PEDIDOS = "pedidos";
    public static String ARG_INICIO = "inicio";
    public static String ARG_FIN = "fin";
    public static String ARG_CLIENTE = "cliente";
    public static String ARG_ESTADO = "estado";
    public static String ARG_INFOR = "infor";

    TrackingListFragmentListener permisoListFragmentCallback;
    private Calendar desde, hasta;
    private SimpleDateFormat format1;
    private boolean isContacts = false;
    private List<String> list_infor, list_estados;
    private List<PedidoView> pedidos;

    public static TrackingListFragment newInstance() {
        TrackingListFragment fragment = new TrackingListFragment();
        return fragment;
    }
    public interface TrackingListFragmentListener {
        public void onPedidoSelected(PedidoView pedido);
        public void onFinalizaConsulta();
        public boolean allowSelectedItem();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(getParentFragment()!=null){
            permisoListFragmentCallback = (TrackingListFragmentListener)getParentFragment();
        }else{
            permisoListFragmentCallback = (TrackingListFragmentListener) activity;
            setRetainInstance(true);
        }

        super.setContentView(R.layout.fragment_pedido_view_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        ejecutarConsulta();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void searchTransactions(){
        //Realizo nueva búsqueda
    }

    public void ejecutarConsulta(){
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        format1 = new SimpleDateFormat("dd/MM/yyyy");
        list_infor = new ArrayList<String>();
        list_estados = new ArrayList<String>();
        desde = Calendar.getInstance();
        hasta = Calendar.getInstance();

        ((TextView) rootView.findViewById(R.id.pedido_view_desde)).setText(format1.format(desde.getTime()));
        ((TextView) rootView.findViewById(R.id.pedido_view_hasta)).setText(format1.format(hasta.getTime()));
        ((TextView) rootView.findViewById(R.id.pedido_view_desde)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDatePicker(1, desde);
            }
        });

        ((TextView) rootView.findViewById(R.id.pedido_view_hasta)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDatePicker(2, hasta);
            }
        });

        list_estados.add("Todos");
        list_estados.add("Pendiente");
        list_estados.add("Cerrado");
        list_estados.add("Por Aprobar");
        list_estados.add("Rechazado");

        list_infor.add("Todos");
        list_infor.add("Si");
        list_infor.add("No");

        final ArrayAdapter<String> estadosAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, list_estados);
        ((Spinner) rootView.findViewById(R.id.pedido_view_estado)).setAdapter(estadosAdapter);
        final ArrayAdapter<String> inforAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, list_infor);
        ((Spinner) rootView.findViewById(R.id.pedido_view_infor)).setAdapter(inforAdapter);

        desde.set(Calendar.HOUR_OF_DAY, 0);
        desde.set(Calendar.MINUTE, 0);
        desde.set(Calendar.SECOND, 0);

        hasta.set(Calendar.HOUR_OF_DAY, 23);
        hasta.set(Calendar.MINUTE, 59);
        hasta.set(Calendar.SECOND, 59);

        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GET_PEDIDOS);
        bundle.putLong(ARG_INICIO, Convert.getDotNetTicksFromDate(desde.getTime()));
        bundle.putLong(ARG_FIN, Convert.getDotNetTicksFromDate(hasta.getTime()));
        bundle.putString(ARG_CLIENTE, "");
        bundle.putString(ARG_ESTADO, "");
        bundle.putString(ARG_INFOR, "");
        requestSync(bundle);

        showDialogProgress("Cargando", "Consultando Pedidos");
    }

    public void ConsultarPedidos()
    {
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GET_PEDIDOS);
        bundle.putLong(ARG_INICIO, Convert.getDotNetTicksFromDate(desde.getTime()));
        bundle.putLong(ARG_FIN, Convert.getDotNetTicksFromDate(hasta.getTime()));
        if(((EditText) getRootView().findViewById(R.id.pedido_view_cliente)).length() > 0)
            bundle.putString(ARG_CLIENTE, ((EditText) getRootView().findViewById(R.id.pedido_view_cliente)).getText().toString());
        else
            bundle.putString(ARG_CLIENTE, "");

        int position_estado = ((Spinner) getRootView().findViewById(R.id.pedido_view_estado)).getSelectedItemPosition();
        int position_infor = ((Spinner) getRootView().findViewById(R.id.pedido_view_infor)).getSelectedItemPosition();
        if(position_estado != - 1)
            bundle.putString(ARG_ESTADO, list_estados.get(position_estado));
        else
            bundle.putString(ARG_ESTADO, "");

        if(position_infor != -1)
            bundle.putString(ARG_INFOR, list_infor.get(position_infor));
        else
            bundle.putString(ARG_INFOR, "");

        requestSync(bundle);

        showDialogProgress("Cargando", "Consultando Pedidos");
    }

    @Override
    public void onDailogDatePickerChange(int id, Calendar c) {
        switch (id)
        {
            case 1:
                desde = c;
                ((TextView) getRootView().findViewById(R.id.pedido_view_desde)).setText(format1.format(desde.getTime()));
                break;
            case 2:
                hasta = c;
                ((TextView) getRootView().findViewById(R.id.pedido_view_hasta)).setText(format1.format(hasta.getTime()));
                break;
        }
        super.onDailogDatePickerChange(id, c);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GET_PEDIDOS)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                String desc = data.getString(ARG_PEDIDOS);
                ShowPedidos(desc);
            }
        }
    }

    public void ShowPedidos(String json)
    {
        try {
            JSONArray jsonArray = new JSONArray(json);
            Calendar cal = Calendar.getInstance();
            pedidos = new ArrayList<>();

            if(jsonArray.length() > 0)
                getRootView().findViewById(R.id.list_permisos_ninguno).setVisibility(View.GONE);
            else
                getRootView().findViewById(R.id.list_permisos_ninguno).setVisibility(View.VISIBLE);
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PedidoView pedidoView = new PedidoView();
                pedidoView.setIdPedido(jsonObject.getInt("IdPedido"));
                pedidoView.setAgente(jsonObject.getString("UsrIng"));
                pedidoView.setEstado(jsonObject.getString("EstadoDescripcion"));
                pedidoView.setNombreCliente(jsonObject.getString("Cliente"));
                pedidoView.setFechaIngreso(Convert.getDateFromDotNetTicks(jsonObject.getLong("FecIngTicks")));

                if(jsonObject.isNull("Orden"))
                    pedidoView.setOrden("");
                else
                    pedidoView.setOrden(jsonObject.getString("Orden"));

                if(jsonObject.isNull("EstadoInfor"))
                    pedidoView.setEstadoInfor("");
                else
                    pedidoView.setEstadoInfor(jsonObject.getString("EstadoInfor"));

                pedidos.add(pedidoView);
            }

            TrackingAdapter adapter = new TrackingAdapter(getContext(), pedidos);
            ((ListView)getRootView().findViewById(R.id.linearLayout_headerlist_ruta_list)).setAdapter(adapter);
        }
        catch (Exception ex)
        {

        }
    }
}
