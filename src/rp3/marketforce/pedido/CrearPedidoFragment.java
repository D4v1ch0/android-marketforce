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
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.CrearClienteActivity;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;


/**
 * Created by magno_000 on 13/10/2015.
 */
public class CrearPedidoFragment extends BaseFragment implements ProductFragment.ProductAcceptListener, PagosListFragment.PagosAcceptListener{

    boolean rotated = false;
    private AutoCompleteTextView cliente_auto;
    private ArrayList<String> list_nombres;
    private List<Cliente> list_cliente;

    public static String ARG_PEDIDO = "cliente";
    public static String ARG_AGENDA = "agenda";
    public static String ARG_TIPO_DOCUMENTO = "tipo_documento";
    public static final int REQUEST_BUSQUEDA = 3;
    public static final int REQUEST_CLIENTE = 4;

    public static final int DIALOG_SAVE_CANCEL = 1;
    public static final int DIALOG_SAVE_ACCEPT = 2;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private long idCliente = 0;
    private long idAgenda = 0;
    private String tipo = "FA";
    private Pedido pedido;
    public ProductFragment productFragment;
    private String code;
    private PedidoDetalleAdapter adapter;
    private NumberFormat numberFormat;
    double descuentos = 0, subtotal = 0, valorTotal = 0, impuestos = 0, base0 = 0, baseImponible = 0, redondeo = 0;

    public static CrearPedidoFragment newInstance(long id_pedido, long id_agenda, String tipo)
    {
        CrearPedidoFragment fragment = new CrearPedidoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PEDIDO, id_pedido);
        args.putLong(ARG_AGENDA, id_agenda);
        args.putString(ARG_TIPO_DOCUMENTO, tipo);
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
        setContentView(R.layout.fragment_crear_pedido, R.menu.fragment_crear_pedido);
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
            case R.id.action_forma_pago:
                PagosListFragment fragment = PagosListFragment.newInstance(valorTotal);
                fragment.pagos = pedido.getPagos();
                showDialogFragment(fragment, "Pagos", "Pagos");
                break;
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
                Intent intent = new Intent(getContext(), CrearPedidoActivity.class);
                intent.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "FA");
                startActivity(intent);
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
        pedido.set_idCliente((int) cli.getID());
        if (((EditText) getRootView().findViewById(R.id.pedido_email)).length() > 0)
            pedido.setEmail(((EditText) getRootView().findViewById(R.id.pedido_email)).getText().toString());

        pedido.set_idAgenda((int) idAgenda);

        pedido.setTipoDocumento(tipo);
        if (tipo.equalsIgnoreCase("FA")) {
            pedido.setNumeroDocumento(PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO) + "-" + PreferenceManager.getString(Contants.KEY_SERIE) +
                    "-" + getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA)));
        }
        if (tipo.equalsIgnoreCase("NC")) {
            pedido.setNumeroDocumento(PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO) + "-" + PreferenceManager.getString(Contants.KEY_SERIE) +
                    "-" + getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO)));
        }


        pedido.setObservaciones(((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).getText().toString());
        pedido.setValorTotal(valorTotal);
        pedido.setSubtotal(subtotal);
        pedido.setTotalDescuentos(descuentos);
        pedido.setTotalImpuestos(impuestos);
        pedido.setBaseImponible(baseImponible);
        pedido.setBaseImponibleCero(base0);
        pedido.setRedondeo(redondeo);
        float pagado = 0;
        if (pedido.getPagos() != null)
            for (Pago pago : pedido.getPagos()) {
                pagado = pagado + pago.getValor();
            }
        pedido.setExcedente(valorTotal - pagado);
        pedido.setSubtotalSinDescuento(subtotal + pedido.getTotalImpuestos());

        if (pendiente)
            pedido.setEstado("P");
        else
            pedido.setEstado("C");

        if (pedido.getID() == 0)
            Pedido.insert(getDataBase(), pedido);
        else
            Pedido.update(getDataBase(), pedido);

        if (pedido.getID() == 0)
            pedido.setID(getDataBase().queryMaxInt(Contract.Pedido.TABLE_NAME, Contract.Pedido._ID));

        if (pedido.getIdPedido() != 0) {
            PedidoDetalle.deleteDetallesByIdPedido(getDataBase(), pedido.getIdPedido());
            Pago.deletePagosByIdPedido(getDataBase(), pedido.getIdPedido());
        } else {
            PedidoDetalle.deleteDetallesByIdPedidoInt(getDataBase(), (int) pedido.getID());
            Pago.deletePagosByIdPedidoInt(getDataBase(), (int) pedido.getID());
        }

        for (PedidoDetalle detalle : pedido.getPedidoDetalles()) {
            detalle.setIdPedido(pedido.getIdPedido());
            detalle.set_idPedido((int) pedido.getID());
            PedidoDetalle.insert(getDataBase(), detalle);
        }

        for (Pago pago : pedido.getPagos()) {
            if(pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo"))
                pago.setValor(pago.getValor() + ((float)pedido.getExcedente()));
            pago.setIdPedido(pedido.getIdPedido());
            pago.set_idPedido((int) pedido.getID());
            Pago.insert(getDataBase(), pago);
        }


        if (ConnectionUtils.isNetAvailable(getActivity())) {
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_PEDIDO);
            bundle.putLong(ARG_PEDIDO, pedido.getID());
            requestSync(bundle);
        }

        if (pedido.getTipoDocumento().equalsIgnoreCase("FA"))
            PreferenceManager.setValue(Contants.KEY_SECUENCIA_FACTURA, PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA) + 1);
        if (pedido.getTipoDocumento().equalsIgnoreCase("NC"))
            PreferenceManager.setValue(Contants.KEY_SECUENCIA_NOTA_CREDITO, PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO) + 1);

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

        rootView.findViewById(R.id.pedido_crear_cliente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CrearClienteActivity.class);
                startActivityForResult(intent, REQUEST_CLIENTE);
            }
        });


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
                arrayAdapter.add("Desde SKU");
                arrayAdapter.add("Desde Código de Barras");

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
                                    case 3:
                                        Intent intent3 = new Intent(getContext(), ProductoListActivity.class);
                                        intent3.putExtra(ProductoListFragment.ARG_BUSQUEDA, "sku");
                                        startActivityForResult(intent3, REQUEST_BUSQUEDA);
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
                } catch (Exception ex) {

                }

            }
        });

        if (getArguments().containsKey(ARG_TIPO_DOCUMENTO) && !rotated) {
            tipo = getArguments().getString(ARG_TIPO_DOCUMENTO);
            if(tipo.equalsIgnoreCase("FA"))
                this.getActivity().setTitle("Factura No. " + PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO) + "-" + PreferenceManager.getString(Contants.KEY_SERIE) +
                        "-" + getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA)));
            if(tipo.equalsIgnoreCase("NC"))
                this.getActivity().setTitle("Nota de Crédito No. " + PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO) + "-" + PreferenceManager.getString(Contants.KEY_SERIE) +
                        "-" + getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO)));
        }
    }

    private String getSecuencia(int numero) {
        String numText = numero + "";
        int faltanCeros = 9 - numText.length();
        for(int i = 1; i <= faltanCeros; i++)
        {
            numText = "0" + numText;
        }
        return numText;
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

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(getPedidoCantidad(pedido.getPedidoDetalles()) + "");
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
                    try {
                        String contents = data.getStringExtra("SCAN_RESULT");
                        JSONObject jsonObject = new JSONObject(contents);
                        jsonObject.getString("d");
                        code = contents;
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(this.getContext(), "Código Inválido.", Toast.LENGTH_LONG).show();
                    }

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
                        detalle.setValorTotal(jsonObject.getDouble("v"));
                        detalle.setBaseImponible(jsonObject.getDouble("bi"));
                        detalle.setBaseImponibleCero(jsonObject.getDouble("bic"));
                        detalle.setPorcentajeDescuentoManual(jsonObject.getDouble("pdm"));
                        detalle.setPorcentajeDescuentoAutomatico(jsonObject.getDouble("pda"));
                        detalle.setValorDescuentoAutomatico(jsonObject.getDouble("vda"));
                        detalle.setValorDescuentoManual(jsonObject.getDouble("vdm"));
                        detalle.setValorDescuentoAutomaticoTotal(jsonObject.getDouble("vdat"));
                        detalle.setValorDescuentoManualTotal(jsonObject.getDouble("vdmt"));
                        detalle.setPorcentajeImpuesto(jsonObject.getDouble("pi"));
                        detalle.setValorImpuesto(jsonObject.getDouble("vi"));
                        detalle.setValorImpuestoTotal(jsonObject.getDouble("vit"));
                        detalle.setSubtotal(jsonObject.getDouble("s"));
                        detalle.setCodigoExterno(jsonObject.getString("cod"));
                        onAcceptSuccess(detalle);
                    }
                    catch (Exception ex)
                    {}

                }
                break;
            case REQUEST_CLIENTE:
                if(resultCode == RESULT_OK)
                {
                    Cliente cl = Cliente.getClienteID(getDataBase(), data.getExtras().getLong(CrearClienteActivity.ARG_IDCLIENTE), false);
                    cliente_auto.setText(cl.getNombreCompleto());
                    cliente_auto.setEnabled(false);
                    ((TextView) getRootView().findViewById(R.id.pedido_email)).setText(cl.getCorreoElectronico());
                    list_cliente = Cliente.getCliente(getDataBase());
                    list_nombres = new ArrayList<String>();
                    for (Cliente cli : list_cliente) {
                        list_nombres.add(cli.getNombreCompleto().trim());
                    }
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
        for (int i = 0; i < pedido.getPedidoDetalles().size(); i++) {
            if (pedido.getPedidoDetalles().get(i).getIdProducto() == transaction.getIdProducto())
                exists = i;
        }

        if (exists != -1)
            pedido.getPedidoDetalles().remove(exists);

        adapter.setList(pedido.getPedidoDetalles());

        adapter.notifyDataSetChanged();

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(getPedidoCantidad(pedido.getPedidoDetalles()) + "");

        descuentos = 0; subtotal = 0; valorTotal = 0; impuestos = 0; base0 = 0; baseImponible = 0; redondeo = 0;
        for (PedidoDetalle detalle : pedido.getPedidoDetalles()) {
            valorTotal = valorTotal + detalle.getValorTotal();
            subtotal = subtotal + detalle.getSubtotal();
            descuentos = descuentos + detalle.getValorDescuentoManualTotal() + detalle.getValorDescuentoAutomaticoTotal();
            impuestos = impuestos + detalle.getValorImpuesto();
            base0 = base0 + detalle.getBaseImponibleCero();
            baseImponible = baseImponible + detalle.getBaseImponible();
        }
        double residuo = valorTotal % 100;
        if(residuo >= 50)
            redondeo = (100 - residuo);
        else
            redondeo = - residuo;

        valorTotal = valorTotal + redondeo;


        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(valorTotal));
        ((TextView) getRootView().findViewById(R.id.pedido_descuentos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(descuentos));
        ((TextView) getRootView().findViewById(R.id.pedido_impuestos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(impuestos));
        ((TextView) getRootView().findViewById(R.id.pedido_base_cero)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(base0));
        ((TextView) getRootView().findViewById(R.id.pedido_base_imponible)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(baseImponible));
        ((TextView) getRootView().findViewById(R.id.pedido_subtotal)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(subtotal));
        ((TextView) getRootView().findViewById(R.id.pedido_redondeo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(redondeo));

        float pagado = 0;
        if (pedido.getPagos() != null)
            for (Pago pago : pedido.getPagos()) {
                pagado = pagado + pago.getValor();
            }

        ((TextView) getRootView().findViewById(R.id.pedido_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(valorTotal - pagado));
    }

    @Override
    public void onAcceptSuccess(PedidoDetalle transaction) {
        int exists = -1;

        if (pedido.getPedidoDetalles() == null)
            pedido.setPedidoDetalles(new ArrayList<PedidoDetalle>());

        for (int i = 0; i < pedido.getPedidoDetalles().size(); i++) {
            if (pedido.getPedidoDetalles().get(i).getIdProducto() == transaction.getIdProducto())
                exists = i;
        }

        if (exists == -1)
            pedido.getPedidoDetalles().add(transaction);
        else
            pedido.getPedidoDetalles().set(exists, transaction);

        if (adapter == null) {
            adapter = new PedidoDetalleAdapter(this.getContext(), pedido.getPedidoDetalles());
            ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setAdapter(adapter);
        } else
            adapter.setList(pedido.getPedidoDetalles());

        adapter.notifyDataSetChanged();

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(getPedidoCantidad(pedido.getPedidoDetalles()) + "");

        descuentos = 0; subtotal = 0; valorTotal = 0; impuestos = 0; base0 = 0; baseImponible = 0; redondeo = 0;
        for (PedidoDetalle detalle : pedido.getPedidoDetalles()) {
            valorTotal = valorTotal + detalle.getValorTotal();
            subtotal = subtotal + detalle.getSubtotal();
            descuentos = descuentos + detalle.getValorDescuentoManualTotal() + detalle.getValorDescuentoAutomaticoTotal();
            impuestos = impuestos + detalle.getValorImpuestoTotal();
            base0 = base0 + detalle.getBaseImponibleCero();
            baseImponible = baseImponible + detalle.getBaseImponible();
        }
        double residuo = valorTotal % 100;
        if(residuo >= 50)
            redondeo = (100 - residuo);
        else
            redondeo = - residuo;

        valorTotal = valorTotal + redondeo;

        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(valorTotal));
        ((TextView) getRootView().findViewById(R.id.pedido_descuentos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(descuentos));
        ((TextView) getRootView().findViewById(R.id.pedido_impuestos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(impuestos));
        ((TextView) getRootView().findViewById(R.id.pedido_base_cero)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(base0));
        ((TextView) getRootView().findViewById(R.id.pedido_base_imponible)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(baseImponible));
        ((TextView) getRootView().findViewById(R.id.pedido_subtotal)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(subtotal));
        ((TextView) getRootView().findViewById(R.id.pedido_redondeo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(redondeo));

        float pagado = 0;
        if (pedido.getPagos() != null)
            for (Pago pago : pedido.getPagos()) {
                pagado = pagado + pago.getValor();
            }

        ((TextView) getRootView().findViewById(R.id.pedido_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(valorTotal - pagado));
    }

    public static int getPedidoCantidad(List<PedidoDetalle> detalles)
    {
        int cant = 0;
        for(PedidoDetalle detalle : detalles)
        {
            cant = cant + detalle.getCantidad();
        }
        return cant;
    }

    @Override
    public void onAcceptSuccess(List<Pago> pagos) {
        if(pedido.getPagos() == null)
            pedido.setPagos(new ArrayList<Pago>());

        pedido.setPagos(pagos);

        float pagado = 0;
        for(Pago pago : pedido.getPagos())
        {
            pagado = pagado + pago.getValor();
        }

        ((TextView) getRootView().findViewById(R.id.pedido_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(valorTotal - pagado));
    }
}
