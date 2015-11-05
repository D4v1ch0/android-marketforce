package rp3.marketforce.pedido;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;


/**
 * Created by magno_000 on 13/10/2015.
 */
public class CrearPedidoFragment extends BaseFragment implements ProductFragment.ProductAcceptListener{

    boolean rotated = false;
    private AutoCompleteTextView cliente_auto;
    private ArrayList<String> list_nombres;
    private List<Cliente> list_cliente;

    public static String ARG_PEDIDO = "cliente";
    public static String ARG_AGENDA = "agenda";
    public static final int REQUEST_BUSQUEDA = 3;

    public static final int DIALOG_SAVE_CANCEL = 1;
    public static final int DIALOG_SAVE_ACCEPT = 2;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private long idCliente = 0;
    private long idAgenda = 0;
    private Pedido pedido;
    public ProductFragment productFragment;
    private String code;
    private PedidoDetalleAdapter adapter;
    private NumberFormat numberFormat;

    public static CrearPedidoFragment newInstance(long id_pedido, long id_agenda)
    {
        CrearPedidoFragment fragment = new CrearPedidoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PEDIDO, id_pedido);
        args.putLong(ARG_AGENDA, id_agenda);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pedido = new Pedido();
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tryEnableGooglePlayServices(true);
        setContentView(R.layout.fragment_crear_pedido, R.menu.fragment_crear_cliente);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(code != null) {
            try {
                productFragment = ProductFragment.newInstance(code);
                productFragment.setCancelable(false);
                showDialogFragment(productFragment, "Producto", "Agregar Producto");
                code = null;
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:
                if(Validaciones())
                {
                    showDialogConfirmation(DIALOG_SAVE_ACCEPT, R.string.message_guardar_pedido_accept);
                }
                break;
            case R.id.action_cancel:
                showDialogConfirmation(DIALOG_SAVE_CANCEL, R.string.message_guardar_pedido);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNegativeConfirmation(int id) {
        super.onNegativeConfirmation(id);
        switch (id)
        {
            case DIALOG_SAVE_ACCEPT:
                break;
            case DIALOG_SAVE_CANCEL:
                finish();
                break;
        }
    }

    @Override
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        switch (id)
        {
            case DIALOG_SAVE_ACCEPT:
                Grabar(false);
                finish();
                break;
            case DIALOG_SAVE_CANCEL:
                if(Validaciones())
                {
                    Grabar(true);
                    finish();
                }
                break;
        }
    }

    private void Grabar(boolean pendiente) {
        if (idCliente == 0)
            pedido.setFechaCreacion(Calendar.getInstance().getTime());

        Cliente cli = list_cliente.get(list_nombres.indexOf(cliente_auto.getText().toString()));
        pedido.setIdCliente((int) cli.getIdCliente());
        if (((EditText) getRootView().findViewById(R.id.pedido_email)).length() > 0)
            pedido.setEmail(((EditText) getRootView().findViewById(R.id.pedido_email)).getText().toString());

        double valorTotal = 0;
        for (PedidoDetalle detalle : pedido.getPedidoDetalles()) {
            valorTotal = valorTotal + detalle.getValorTotal();
        }

        pedido.set_idAgenda((int) idAgenda);

        pedido.setValorTotal(valorTotal);
        if (pendiente)
            pedido.setEstado("P");
        else
            pedido.setEstado("C");

        if (pedido.getID() == 0)
            Pedido.insert(getDataBase(), pedido);
        else
            Pedido.update(getDataBase(), pedido);

        if (pedido.getIdPedido() != 0)
            PedidoDetalle.deleteDetallesByIdPedido(getDataBase(), pedido.getIdPedido());
        else
            PedidoDetalle.deleteDetallesByIdPedidoInt(getDataBase(), (int) pedido.getID());

        for (PedidoDetalle detalle : pedido.getPedidoDetalles()) {
            detalle.setIdPedido(pedido.getIdPedido());
            detalle.set_idPedido((int) pedido.getID());
            PedidoDetalle.insert(getDataBase(), detalle);
        }


        if (ConnectionUtils.isNetAvailable(getActivity())) {
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_PEDIDO);
            bundle.putLong(ARG_PEDIDO, pedido.getID());
            requestSync(bundle);
        }
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        cliente_auto = (AutoCompleteTextView) rootView.findViewById(R.id.pedido_cliente);
        list_nombres = new ArrayList<String>();

        list_cliente = Cliente.getCliente(getDataBase());
        for (Cliente cli : list_cliente) {
            list_nombres.add(cli.getNombreCompleto().trim());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_small_text, list_nombres);

        cliente_auto.setAdapter(adapter);
        cliente_auto.setThreshold(1);

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cliente_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int pos, long id) {
                    int position = list_nombres.indexOf(adapter.getItem(pos));
                    if (position != -1 && ((TextView) rootView.findViewById(R.id.pedido_email)).length() <= 0) {
                        ((TextView) rootView.findViewById(R.id.pedido_email)).setText(list_cliente.get(position).getCorreoElectronico());
                    }
                }
            });


        } else {
            cliente_auto.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {

                @Override
                public void onDismiss() {
                    int position = list_nombres.indexOf(cliente_auto.getText().toString());
                    if (position != -1 && ((TextView) rootView.findViewById(R.id.pedido_email)).length() <= 0) {
                        ((TextView) rootView.findViewById(R.id.pedido_email)).setText(list_cliente.get(position).getCorreoElectronico());
                    }
                }
            });
        }

        ((Button) rootView.findViewById(R.id.pedido_agregar_producto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.select_dialog_item);
                arrayAdapter.add("Desde Código QR");
                arrayAdapter.add("Desde Búsqueda de Productos");
                arrayAdapter.add("Desde Categorías");

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        scanQR();
                                        break;
                                    case 1:
                                        Intent intent = new Intent(getContext(), ProductoListActivity.class);
                                        startActivityForResult(intent, REQUEST_BUSQUEDA);
                                        break;
                                    case 2:
                                        Intent intent2 = new Intent(getContext(), CategoriaActivity.class);
                                        startActivityForResult(intent2, REQUEST_BUSQUEDA);
                                        break;
                                }

                            }
                        });
                builderSingle.show();

            }
        });

        if (getArguments().containsKey(ARG_PEDIDO) && getArguments().getLong(ARG_PEDIDO) != 0 && !rotated) {
            idCliente = getArguments().getLong(ARG_PEDIDO);
            setDatosPedidos();
        }

        if (getArguments().containsKey(ARG_AGENDA) && getArguments().getLong(ARG_AGENDA) != 0 && !rotated) {
            idAgenda = getArguments().getLong(ARG_AGENDA);

            Agenda agd = Agenda.getAgenda(getDataBase(), idAgenda);
            cliente_auto.setText(agd.getCliente().getNombreCompleto());
            cliente_auto.setEnabled(false);
            ((TextView) rootView.findViewById(R.id.pedido_email)).setText(agd.getCliente().getCorreoElectronico());
        }

        ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("d", pedido.getPedidoDetalles().get(position).getDescripcion());
                    jsonObject.put("p", pedido.getPedidoDetalles().get(position).getValorUnitario());
                    jsonObject.put("id", pedido.getPedidoDetalles().get(position).getIdProducto());
                    jsonObject.put("f", pedido.getPedidoDetalles().get(position).getUrlFoto());
                    jsonObject.put("c", pedido.getPedidoDetalles().get(position).getCantidad());

                    productFragment = ProductFragment.newInstance(jsonObject.toString());
                    productFragment.setCancelable(false);
                    showDialogFragment(productFragment, "Producto", "Editar Producto");
                    code = null;
                }
                catch (Exception ex)
                {

                }

            }
        });
    }

    public void scanQR() {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            Toast.makeText(this.getContext(), "No Scanner Found", Toast.LENGTH_LONG).show();
        }
    }

    private void setDatosPedidos() {
        pedido = Pedido.getPedido(getDataBase(), idCliente);
        adapter = new PedidoDetalleAdapter(this.getContext(), pedido.getPedidoDetalles());
        ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setAdapter(adapter);

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(pedido.getPedidoDetalles().size() + "");
        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText("$ " + numberFormat.format(pedido.getValorTotal()));
        ((TextView) getRootView().findViewById(R.id.pedido_cliente)).setText(pedido.getCliente().getNombreCompleto());
        ((TextView) getRootView().findViewById(R.id.pedido_email)).setText(pedido.getEmail());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 0:
                if (resultCode == RESULT_OK) {
                    //get the extras that are returned from the intent
                    String contents = data.getStringExtra("SCAN_RESULT");
                    code = contents;
                }
                break;
            case REQUEST_BUSQUEDA:
                if(resultCode == RESULT_OK)
                {
                    try {
                        JSONObject jsonObject = new JSONObject(data.getExtras().getString(ProductoListFragment.ARG_PRODUCTO));
                        PedidoDetalle detalle = new PedidoDetalle();
                        detalle.setDescripcion(jsonObject.getString("d"));
                        detalle.setValorUnitario(jsonObject.getDouble("p"));
                        detalle.setIdProducto(jsonObject.getInt("id"));
                        detalle.setUrlFoto(jsonObject.getString("f"));
                        detalle.setCantidad(jsonObject.getInt("c"));
                        detalle.setValorTotal(detalle.getValorUnitario() * detalle.getCantidad());
                        onAcceptSuccess(detalle);
                    }
                    catch (Exception ex)
                    {}

                }
        }
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
    }

    public boolean Validaciones()
    {
        if(((AutoCompleteTextView) getRootView().findViewById(R.id.pedido_cliente)).length() <= 0)
        {
            Toast.makeText(this.getContext(), "Debe ingresar un cliente", Toast.LENGTH_LONG).show();
            return false;
        }
        if(pedido.getPedidoDetalles() == null || pedido.getPedidoDetalles().size() <= 0)
        {
            Toast.makeText(this.getContext(), "Debe ingresar al menos un item", Toast.LENGTH_LONG).show();
            return false;
        }
        if (list_nombres.indexOf(cliente_auto.getText().toString()) == -1) {
            Toast.makeText(getContext(), "Nombre de Cliente o Contacto incorrecto.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    @Override
    public void onDeleteSuccess(PedidoDetalle transaction) {
        int exists = -1;
        for(int i = 0; i < pedido.getPedidoDetalles().size(); i++)
        {
            if(pedido.getPedidoDetalles().get(i).getIdProducto() == transaction.getIdProducto())
                exists = i;
        }

        if(exists != -1)
            pedido.getPedidoDetalles().remove(exists);

        adapter.setList(pedido.getPedidoDetalles());

        adapter.notifyDataSetChanged();

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(pedido.getPedidoDetalles().size() + "");

        double valorTotal = 0;
        for(PedidoDetalle detalle : pedido.getPedidoDetalles())
        {
            valorTotal = valorTotal + detalle.getValorTotal();
        }

        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText("$ " + numberFormat.format(valorTotal));
    }

    @Override
    public void onAcceptSuccess(PedidoDetalle transaction) {
        int exists = -1;

        if(pedido.getPedidoDetalles() == null)
            pedido.setPedidoDetalles(new ArrayList<PedidoDetalle>());

        for(int i = 0; i < pedido.getPedidoDetalles().size(); i++)
        {
            if(pedido.getPedidoDetalles().get(i).getIdProducto() == transaction.getIdProducto())
                exists = i;
        }

        if(exists == -1)
            pedido.getPedidoDetalles().add(transaction);
        else
            pedido.getPedidoDetalles().set(exists, transaction);

        if(adapter == null) {
            adapter = new PedidoDetalleAdapter(this.getContext(), pedido.getPedidoDetalles());
            ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setAdapter(adapter);
        }
        else
            adapter.setList(pedido.getPedidoDetalles());

        adapter.notifyDataSetChanged();

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(pedido.getPedidoDetalles().size() + "");

        double valorTotal = 0;
        for(PedidoDetalle detalle : pedido.getPedidoDetalles())
        {
            valorTotal = valorTotal + detalle.getValorTotal();
        }

        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText("$ " + numberFormat.format(valorTotal));

    }
}
