package rp3.marketforce.cliente;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.ComprasCliente;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.Convert;

/**
 * Created by magno_000 on 05/05/2017.
 */

public class ComprasClienteFragment extends BaseFragment {
    public static String ARG_CLIENTE = "idcliente";

    private long idCliente;
    private NumberFormat numberFormat;

    public static ComprasClienteFragment newInstance(long idCliente)
    {
        ComprasClienteFragment fragment = new ComprasClienteFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CLIENTE, idCliente);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_compras_cliente);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        idCliente = getArguments().getLong(ARG_CLIENTE, 0);
        Cliente cli = Cliente.getClienteID(getDataBase(), idCliente, false);

        ((TextView) rootView.findViewById(R.id.compras_cliente_cliente)).setText(cli.getNombreCompleto());

        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_COMPRAS_CLIENTE);
        bundle.putString(ARG_CLIENTE, cli.getIdExterno());
        requestSync(bundle);

        showDialogProgress("Cargando", "Consultando Compras de Cliente");
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_COMPRAS_CLIENTE)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                String desc = data.getString(ARG_CLIENTE);
                ShowComprasCliente(desc);
            }
        }
    }

    public void ShowComprasCliente(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            Calendar cal = Calendar.getInstance();
            List<ComprasCliente> listComprasCliente = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ComprasCliente comprasCliente = new ComprasCliente();
                comprasCliente.setCantidad(jsonObject.getInt("Cantidad"));
                comprasCliente.setItem(jsonObject.getString("Item"));
                Producto prod = Producto.getProductoSingleByCodigoExterno(getDataBase(), comprasCliente.getItem());
                if(prod != null)
                    comprasCliente.setDescripcion(prod.getDescripcion());
                comprasCliente.setFechaCompra(Convert.getDateFromDotNetTicks(jsonObject.getLong("FechaUltimaCompraTicks")));
                comprasCliente.setPrecio(jsonObject.getDouble("Precio"));
                comprasCliente.setStock(jsonObject.getInt("StockFisico"));
                listComprasCliente.add(comprasCliente);
            }

            ComprasClienteAdapter adapter = new ComprasClienteAdapter(getContext(), listComprasCliente);
            ((ListView) getRootView().findViewById(R.id.compras_cliente_lista)).setAdapter(adapter);
        } catch (Exception ex) {

        }
    }
}
