package rp3.marketforce.pedido;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
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
    public static final int DIALOG_SAVE_CANCEL = 1;
    public static final int DIALOG_SAVE_ACCEPT = 2;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private long idCliente = 0;
    private Pedido pedido;
    public ProductFragment productFragment;
    private String code;
    private PedidoDetalleAdapter adapter;

    public static CrearPedidoFragment newInstance(long id_pedido)
    {
        CrearPedidoFragment fragment = new CrearPedidoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PEDIDO, id_pedido);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pedido = new Pedido();
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
        if(code != null)
        {
            productFragment = null;
            try {
                productFragment = ProductFragment.newInstance(code);
                showDialogFragment(productFragment, "Producto");
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
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
        Pedido pedido = new Pedido();
        if(idCliente != 0)
            pedido = Pedido.getPedido(getDataBase(), idCliente);
        else
            pedido.setFechaCreacion(Calendar.getInstance().getTime());

        Cliente cli = list_cliente.get(list_nombres.indexOf(cliente_auto.getText().toString()));
        pedido.setIdCliente((int) cli.getIdCliente());
        if(((EditText) getRootView().findViewById(R.id.pedido_email)).length() > 0)
            pedido.setEmail(((EditText) getRootView().findViewById(R.id.pedido_email)).getText().toString());

        double valorTotal = 0;

        for(PedidoDetalle detalle : pedido.getPedidoDetalles())
        {
            valorTotal = valorTotal + detalle.getValorTotal();
        }

        pedido.setValorTotal(valorTotal);
        if(pendiente)
            pedido.setEstado("P");
        else
            pedido.setEstado("C");

        if(pedido.getID() == 0)
            Pedido.insert(getDataBase(), pedido);
        else
            Pedido.update(getDataBase(), pedido);

        for(PedidoDetalle detalle : pedido.getPedidoDetalles())
        {
            detalle.setIdPedido(pedido.getIdPedido());
            detalle.set_idPedido((int)pedido.getID());
            if(detalle.getID() == 0)
                PedidoDetalle.insert(getDataBase(), detalle);
            else
                PedidoDetalle.update(getDataBase(), detalle);
        }



        if(ConnectionUtils.isNetAvailable(getActivity()))
        {
            Bundle bundle = new Bundle();
            if(idCliente != 0)
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE_FULL);
            else {
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_CREATE);
            }
            bundle.putLong(ARG_PEDIDO, pedido.getID());
            //requestSync(bundle);
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
                scanQR();
            }
        });

        if (getArguments().containsKey(ARG_PEDIDO) && getArguments().getLong(ARG_PEDIDO) != 0 && !rotated) {
            idCliente = getArguments().getLong(ARG_PEDIDO);
            setDatosPedidos();
        }
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

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this.getContext(), "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
                code = contents;
            }
        }
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

    }

    @Override
    public void onAcceptSuccess(PedidoDetalle transaction) {
        if(pedido.getPedidoDetalles() == null)
            pedido.setPedidoDetalles(new ArrayList<PedidoDetalle>());
        pedido.getPedidoDetalles().add(transaction);

        if(adapter == null) {
            adapter = new PedidoDetalleAdapter(this.getContext(), pedido.getPedidoDetalles());
            ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setAdapter(adapter);
        }
        else
            adapter.setList(pedido.getPedidoDetalles());

        adapter.notifyDataSetChanged();

    }
}
