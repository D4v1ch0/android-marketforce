package rp3.marketforce.pedido;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.CrearClienteActivity;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.models.pedido.ProductoPromocion;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.PrintHelper;
import rp3.runtime.Session;
import rp3.util.ConnectionUtils;
import rp3.util.NumberUtils;
import rp3.util.Screen;
import rp3.util.StringUtils;


/**
 * Created by magno_000 on 13/10/2015.
 */
public class CrearPedidoFragment extends BaseFragment implements ProductFragment.ProductAcceptListener, PagosListFragment.PagosAcceptListener, CodeReaderFragment.ProductCodeAcceptListener{

    public final static int CODE_PRINT = 1;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    boolean rotated = false, activated = false;
    private AutoCompleteTextView cliente_auto;
    private ArrayList<String> list_nombres;
    private List<Cliente> list_cliente;

    public static String ARG_PEDIDO = "cliente";
    public static String ARG_CLIENTE = "idcliente";
    public static String ARG_SERIE = "serie";
    public static String ARG_AGENDA = "agenda";
    public static String ARG_TIPO_DOCUMENTO = "tipo_documento";
    public static final int REQUEST_BUSQUEDA = 3;
    public static final int REQUEST_CLIENTE = 4;
    public static final int REQUEST_REPRINT= 5;

    public static final int DIALOG_SAVE_CANCEL = 1;
    public static final int DIALOG_SAVE_ACCEPT = 2;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private Cliente consumidorFinal = new Cliente();
    private long idCliente = 0;
    private long idAgenda = 0;
    private boolean saved = false;
    private String tipo = "FA";
    private Pedido pedido;
    public ProductFragment productFragment;
    private String code, serie;
    private PedidoDetalleAdapter adapter;
    private NumberFormat numberFormat;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
    double descuentos = 0, subtotal = 0, valorTotal = 0, impuestos = 0, base0 = 0, baseImponible = 0, redondeo = 0, neto = 0;
    private Menu menu;
    private PagosListFragment fragment;

    public static CrearPedidoFragment newInstance(long id_pedido, long id_agenda, String tipo, long idCliente, String serie)
    {
        CrearPedidoFragment fragment = new CrearPedidoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PEDIDO, id_pedido);
        args.putLong(ARG_AGENDA, id_agenda);
        args.putString(ARG_TIPO_DOCUMENTO, tipo);
        args.putLong(ARG_CLIENTE, idCliente);
        args.putString(ARG_SERIE, serie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consumidorFinal.setCiudadanoOro(false);
        consumidorFinal.setExentoImpuesto(false);
        consumidorFinal.setNombre1("Consumidor Final");
        consumidorFinal.setNombre2("");
        consumidorFinal.setApellido1("");
        consumidorFinal.setApellido2("");
        consumidorFinal.setNombreCompleto("Consumidor Final");
        pedido = new Pedido();
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tryEnableGooglePlayServices(true);
        tipo = getArguments().getString(ARG_TIPO_DOCUMENTO, "FA");
        if(tipo.equalsIgnoreCase("FA"))
            setContentView(R.layout.fragment_crear_pedido, R.menu.fragment_crear_pedido);
        else
            setContentView(R.layout.fragment_crear_pedido, R.menu.fragment_crear_transaccion);
        setRetainInstance(true);
        //
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
                fragment = PagosListFragment.newInstance(valorTotal);
                fragment.pagos = pedido.getPagos();
                showDialogFragment(fragment, "Pagos", "Pagos");
                break;
            case R.id.action_save:
                if(Validaciones())
                {
                    showDialogConfirmation(DIALOG_SAVE_ACCEPT, R.string.message_guardar_pedido_accept, R.string.title_guardar_transaccion);
                }
                break;
            case R.id.action_cancel:
                showDialogConfirmation(DIALOG_SAVE_CANCEL, R.string.message_guardar_pedido, R.string.title_abandonar_transaccion);
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

                generarFacturaFísica();
                break;
            case DIALOG_SAVE_CANCEL:
                /*if(Validaciones())
                {
                    Grabar(true);
                    finish();
                }*/
                finish();
                break;
        }
    }

    private void Grabar(boolean pendiente) {
        Pedido docRef = null;
        if(tipo.equalsIgnoreCase("NC"))
            docRef = Pedido.getPedido(getDataBase(), pedido.get_idDocumentoRef(), true);

        if (pedido.getID() == 0)
            pedido.setFechaCreacion(Calendar.getInstance().getTime());

        if(!cliente_auto.getText().toString().equalsIgnoreCase("Consumidor Final"))
        {
            Cliente cli = list_cliente.get(list_nombres.indexOf(cliente_auto.getText().toString()));
            pedido.setIdCliente((int) cli.getIdCliente());
            pedido.set_idCliente((int) cli.getID());
        }

        if (((EditText) getRootView().findViewById(R.id.pedido_email)).length() > 0)
            pedido.setEmail(((EditText) getRootView().findViewById(R.id.pedido_email)).getText().toString());

        pedido.set_idAgenda((int) idAgenda);

        ControlCaja control = ControlCaja.getControlCajaActiva(getDataBase());
        //pedido.set_idControlCaja(control.getID());
        pedido.setNombre(cliente_auto.getText().toString());

        pedido.setTipoDocumento(tipo);
        if (tipo.equalsIgnoreCase("FA")) {
            pedido.setNumeroDocumento(getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO)), 3) + "-" + getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_SERIE)), 3) +
                    "-" + getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA) + 1, 9));
        }
        if (tipo.equalsIgnoreCase("NC")) {
            pedido.setNumeroDocumento(getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO)), 3) + "-" + getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_SERIE)), 3) +
                    "-" + getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO) + 1, 9));
        }
        if (tipo.equalsIgnoreCase("PD")) {
            pedido.setNumeroDocumento("PD" + dateFormat.format(Calendar.getInstance().getTime()));
        }
        if (tipo.equalsIgnoreCase("CT")) {
            pedido.setNumeroDocumento("CT" + dateFormat.format(Calendar.getInstance().getTime()));
        }


        pedido.setObservaciones(((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).getText().toString());
        pedido.setValorTotal(NumberUtils.Round(valorTotal, 2));
        pedido.setSubtotal(NumberUtils.Round(subtotal, 2));
        pedido.setTotalDescuentos(NumberUtils.Round(descuentos, 2));
        pedido.setTotalImpuestos(NumberUtils.Round(impuestos, 2));
        pedido.setBaseImponible(NumberUtils.Round(baseImponible, 2));
        pedido.setBaseImponibleCero(NumberUtils.Round(base0, 2));
        pedido.setRedondeo(NumberUtils.Round(redondeo, 2));
        float pagado = 0;
        if (pedido.getPagos() != null)
            for (Pago pago : pedido.getPagos()) {
                pagado = pagado + pago.getValor();
            }
        pedido.setExcedente(NumberUtils.Round(valorTotal - pagado, 2));
        pedido.setSubtotalSinDescuento(NumberUtils.Round(subtotal, 2));

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

        if(!tipo.equalsIgnoreCase("NC")) {
            if (pedido.getIdPedido() != 0) {
                PedidoDetalle.deleteDetallesByIdPedido(getDataBase(), pedido.getIdPedido());
                Pago.deletePagosByIdPedido(getDataBase(), pedido.getIdPedido());
            } else {
                PedidoDetalle.deleteDetallesByIdPedidoInt(getDataBase(), (int) pedido.getID());
                Pago.deletePagosByIdPedidoInt(getDataBase(), (int) pedido.getID());
            }
        }

        for (PedidoDetalle detalle : pedido.getPedidoDetalles()) {
            if((tipo.equalsIgnoreCase("NC"))) {
                PedidoDetalle detalle_nc = new PedidoDetalle();
                detalle_nc.setPorcentajeDescuentoManual(detalle.getPorcentajeDescuentoManual());
                detalle_nc.setBaseImponible(detalle.getBaseImponible());
                detalle_nc.setCodigoExterno(detalle.getCodigoExterno());
                detalle_nc.setBaseImponibleCero(detalle.getBaseImponibleCero());
                detalle_nc.setCantidad(detalle.getCantidad());
                detalle_nc.setDescripcion(detalle.getDescripcion());
                detalle_nc.setIdPedidoDetalle(detalle.getIdPedidoDetalle());
                detalle_nc.setIdProducto(detalle.getIdProducto());
                detalle_nc.setPorcentajeDescuentoOro(detalle.getPorcentajeDescuentoOro());
                detalle_nc.setPorcentajeDescuentoAutomatico(detalle.getPorcentajeDescuentoAutomatico());
                detalle_nc.setPorcentajeImpuesto(detalle.getPorcentajeImpuesto());
                detalle_nc.setSubtotal(detalle.getSubtotal());
                detalle_nc.setSubtotalSinDescuento(detalle.getSubtotalSinDescuento());
                detalle_nc.setSubtotalSinImpuesto(detalle.getSubtotalSinImpuesto());
                detalle_nc.setUrlFoto(detalle.getUrlFoto());
                detalle_nc.setValorDescuentoOro(detalle.getValorDescuentoOro());
                detalle_nc.setValorDescuentoOroTotal(detalle.getValorDescuentoOroTotal());
                detalle_nc.setValorDescuentoAutomatico(detalle.getValorDescuentoAutomatico());
                detalle_nc.setValorDescuentoAutomaticoTotal(detalle.getValorDescuentoAutomaticoTotal());
                detalle_nc.setValorDescuentoManual(detalle.getValorDescuentoManual());
                detalle_nc.setValorDescuentoManualTotal(detalle.getValorDescuentoManualTotal());
                detalle_nc.setValorImpuesto(detalle.getValorImpuesto());
                detalle_nc.setValorImpuestoTotal(detalle.getValorImpuestoTotal());
                detalle_nc.setValorUnitario(detalle.getValorUnitario());
                detalle_nc.setValorTotal(detalle.getValorTotal());

                detalle_nc.setIdVendedor(detalle.getIdVendedor());
                detalle_nc.set_idPedido((int) pedido.getID());
                detalle_nc.setIdPedido(0);
                PedidoDetalle.insert(getDataBase(), detalle_nc);

                for(PedidoDetalle detalleRef : docRef.getPedidoDetalles())
                {
                    if(detalle_nc.getIdProducto() == detalleRef.getIdProducto())
                    {
                        detalleRef.setCantidadDevolucion(detalle_nc.getCantidad() + detalleRef.getCantidadDevolucion());
                        PedidoDetalle.update(getDataBase(), detalleRef);
                    }
                }

            }
            else {
                detalle.setIdPedido(pedido.getIdPedido());
                detalle.set_idPedido((int) pedido.getID());
                PedidoDetalle.insert(getDataBase(), detalle);
            }
        }

        if(tipo.equalsIgnoreCase("FA")) {
            for (Pago pago : pedido.getPagos()) {
                if (pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo"))
                    pago.setValor(pago.getValor() + ((float) pedido.getExcedente()));
                pago.setIdPedido(pedido.getIdPedido());
                pago.set_idPedido((int) pedido.getID());
                Pago.insert(getDataBase(), pago);
            }
        }


        if (ConnectionUtils.isNetAvailable(getActivity())) {
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PEDIDO_PENDIENTES);
            bundle.putLong(ARG_PEDIDO, pedido.getID());
            requestSync(bundle);
        }

        if (pedido.getTipoDocumento().equalsIgnoreCase("FA"))
            PreferenceManager.setValue(Contants.KEY_SECUENCIA_FACTURA, PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA) + 1);
        if (pedido.getTipoDocumento().equalsIgnoreCase("NC"))
            PreferenceManager.setValue(Contants.KEY_SECUENCIA_NOTA_CREDITO, PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO) + 1);
        if (pedido.getTipoDocumento().equalsIgnoreCase("PD"))
            PreferenceManager.setValue(Contants.KEY_SECUENCIA_PEDIDO, PreferenceManager.getInt(Contants.KEY_SECUENCIA_PEDIDO) + 1);
        if (pedido.getTipoDocumento().equalsIgnoreCase("CT"))
            PreferenceManager.setValue(Contants.KEY_SECUENCIA_COTIZACION, PreferenceManager.getInt(Contants.KEY_SECUENCIA_COTIZACION) + 1);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        idCliente = getArguments().getLong(ARG_CLIENTE, 0);
        serie = getArguments().getString(ARG_SERIE, "");
        cliente_auto = (AutoCompleteTextView) rootView.findViewById(R.id.pedido_cliente);
        list_nombres = new ArrayList<String>();

        try {
            GeneralValue imp = GeneralValue.getGeneralValue(getDataBase(), Contants.POS_PORCIMP);
            if(imp != null)
                ((TextView) rootView.findViewById(R.id.pedido_impuestos_label)).setText("Impuestos " + imp.getValue() + "%");
        }
        catch(Exception ex)
        {}



        list_cliente = Cliente.getCliente(getDataBase());
        list_cliente.add(consumidorFinal);
        for (Cliente cli : list_cliente) {
            list_nombres.add(cli.getIdExterno() + " - " + cli.getNombreCompleto().trim());
        }
        //Se agrega Consumidor Final
        //list_nombres.add("Consumidor Final");
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
                    if (position != -1 && pedido != null && pedido.getPedidoDetalles() != null) {
                        ValidarDescuentos(list_cliente.get(position).isCiudadanoOro());
                        if (list_cliente.get(position).getExentoImpuesto()) {
                            ValidarExentoImpuestos();
                        } else {
                            ValidarImpuestos();
                        }
                        setTransaccionValues();
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
                    if (position != -1 && pedido != null && pedido.getPedidoDetalles() != null) {
                        ValidarDescuentos(list_cliente.get(position).isCiudadanoOro());
                        if (list_cliente.get(position).getExentoImpuesto()) {
                            ValidarExentoImpuestos();
                        } else {
                            ValidarImpuestos();
                        }
                        setTransaccionValues();
                    }

                }
            });
        }

        /*rootView.findViewById(R.id.pedido_crear_cliente).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CrearClienteActivity.class);
                startActivityForResult(intent, REQUEST_CLIENTE);
            }
        });*/

        ((ImageView) rootView.findViewById(R.id.actividad_voice_to_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        ((Button) rootView.findViewById(R.id.pedido_agregar_producto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductoListActivity.class);
                intent.putExtra(ProductoListFragment.ARG_SERIE, serie);
                intent.putExtra(ProductoListFragment.ARG_CLIENTE, idCliente);
                startActivityForResult(intent, REQUEST_BUSQUEDA);
            }
        });

        if (getArguments().containsKey(ARG_TIPO_DOCUMENTO) && !rotated) {
            tipo = getArguments().getString(ARG_TIPO_DOCUMENTO);
            if (tipo.equalsIgnoreCase("PD"))
                this.getActivity().setTitle("Nuevo Pedido");
            if (tipo.equalsIgnoreCase("CT"))
                this.getActivity().setTitle("Nueva Cotización");
        }

        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));
        ((TextView) getRootView().findViewById(R.id.pedido_descuentos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));
        ((TextView) getRootView().findViewById(R.id.pedido_impuestos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));
        ((TextView) getRootView().findViewById(R.id.pedido_base_cero)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));
        ((TextView) getRootView().findViewById(R.id.pedido_base_imponible)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));
        ((TextView) getRootView().findViewById(R.id.pedido_subtotal)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));
        ((TextView) getRootView().findViewById(R.id.pedido_redondeo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));
        ((TextView) getRootView().findViewById(R.id.pedido_neto)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));
        ((TextView) getRootView().findViewById(R.id.pedido_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(0));

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

        if (getArguments().containsKey(ARG_PEDIDO) && getArguments().getLong(ARG_PEDIDO) != 0 && !rotated) {
            idCliente = getArguments().getLong(ARG_PEDIDO);
            setDatosPedidos();
        }

        if (getArguments().containsKey(ARG_AGENDA) && getArguments().getLong(ARG_AGENDA) != 0 && !rotated) {
            idAgenda = getArguments().getLong(ARG_AGENDA);

            Agenda agd = Agenda.getAgenda(getDataBase(), idAgenda);
            cliente_auto.setText(agd.getCliente().getNombreCompleto().trim());
            cliente_auto.setEnabled(false);
            ((TextView) rootView.findViewById(R.id.pedido_email)).setText(agd.getCliente().getCorreoElectronico());
        }

        ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    Producto prod = Producto.getProductoIdServer(getDataBase(), pedido.getPedidoDetalles().get(position).getIdProducto());
                    jsonObject.put("v", pedido.getPedidoDetalles().get(position).getValorTotal());
                    jsonObject.put("pdm", pedido.getPedidoDetalles().get(position).getPorcentajeDescuentoManual());
                    jsonObject.put("vdm", pedido.getPedidoDetalles().get(position).getValorDescuentoManual());
                    jsonObject.put("vdmt", pedido.getPedidoDetalles().get(position).getValorDescuentoManualTotal());
                    jsonObject.put("pdo", pedido.getPedidoDetalles().get(position).getPorcentajeDescuentoOro());
                    jsonObject.put("pda", pedido.getPedidoDetalles().get(position).getPorcentajeDescuentoAutomatico());
                    jsonObject.put("vda", pedido.getPedidoDetalles().get(position).getValorDescuentoAutomatico());
                    jsonObject.put("vdat", pedido.getPedidoDetalles().get(position).getValorDescuentoAutomaticoTotal());
                    jsonObject.put("pi", pedido.getPedidoDetalles().get(position).getPorcentajeImpuesto());
                    jsonObject.put("vi", pedido.getPedidoDetalles().get(position).getValorImpuesto() + pedido.getPedidoDetalles().get(position).getValorUnitario());
                    jsonObject.put("vit", pedido.getPedidoDetalles().get(position).getValorImpuestoTotal());
                    jsonObject.put("bi", pedido.getPedidoDetalles().get(position).getBaseImponible());
                    jsonObject.put("bic", pedido.getPedidoDetalles().get(position).getBaseImponibleCero());
                    jsonObject.put("s", pedido.getPedidoDetalles().get(position).getSubtotal());
                    jsonObject.put("cod", pedido.getPedidoDetalles().get(position).getCodigoExterno());
                    jsonObject.put("d", pedido.getPedidoDetalles().get(position).getDescripcion());
                    jsonObject.put("p", pedido.getPedidoDetalles().get(position).getValorUnitario());
                    jsonObject.put("id", pedido.getPedidoDetalles().get(position).getIdProducto());
                    jsonObject.put("f", pedido.getPedidoDetalles().get(position).getUrlFoto());
                    jsonObject.put("c", pedido.getPedidoDetalles().get(position).getCantidad());
                    jsonObject.put("vd", prod.getPrecioDescuento());
                    jsonObject.put("pd", pedido.getPedidoDetalles().get(position).getPorcentajeDescuentoAutomatico());
                    jsonObject.put("ib", prod.getIdBeneficio());
                    jsonObject.put("co", pedido.getPedidoDetalles().get(position).getCantidadOriginal());
                    jsonObject.put("ven", pedido.getPedidoDetalles().get(position).getIdVendedor());
                    if (pedido.getPedidoDetalles().get(position).getUsrDescManual() != null && !pedido.getPedidoDetalles().get(position).getUsrDescManual().equalsIgnoreCase(""))
                        jsonObject.put("udm", pedido.getPedidoDetalles().get(position).getUsrDescManual());
                    jsonObject.put("tipo", tipo);

                    productFragment = ProductFragment.newInstance(jsonObject.toString());
                    productFragment.setCancelable(false);
                    showDialogFragment(productFragment, "Producto", "Editar Producto");
                    code = null;
                } catch (Exception ex) {

                }

            }
        });
        if(idCliente != 0)
        {
            //Cargo Info de Cliente
            for(Cliente cli: list_cliente)
                if(cli.getID() == idCliente)
                {
                    cliente_auto.setText(cli.getIdExterno() + " - " + cli.getNombreCompleto());
                    if(cli.getCorreoElectronico() != null)
                        ((EditText) getRootView().findViewById(R.id.pedido_email)).setText(cli.getCorreoElectronico());
                    if(cli.getAviso()!= null && cli.getAviso().trim().length() > 0)
                    {
                        //Muestro aviso en el caso que tenga
                        GeneralValue aviso = GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_AVISOS_BERLIN, cli.getAviso());
                        if(aviso != null)
                            showDialogMessage(aviso.getValue());
                    }
                    cliente_auto.setEnabled(false);
                    ((EditText) getRootView().findViewById(R.id.pedido_email)).setEnabled(false);
                }
        }
        if (pedido.getPedidoDetalles() == null)
            pedido.setPedidoDetalles(new ArrayList<PedidoDetalle>());
        if(savedInstanceState != null)
            this.adapter = null;
        if(pedido != null && pedido.getPedidoDetalles() != null)
            setTransaccionValues();
    }

    public static String getSecuencia(int numero, int spaces) {
        String numText = numero + "";
        int faltanCeros = spaces - numText.length();
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
        if(pedido != null && pedido.getPedidoDetalles() == null)
            pedido = Pedido.getPedido(getDataBase(), idCliente, true);
        Cliente cl = null;
        if(pedido.get_idCliente() != 0) {
            cl = Cliente.getClienteID(getDataBase(), pedido.get_idCliente(), false);
            cliente_auto.setText(cl.getNombreCompleto().trim());
            cliente_auto.setEnabled(false);
            ((TextView) getRootView().findViewById(R.id.pedido_email)).setText(cl.getCorreoElectronico());
            getRootView().findViewById(R.id.pedido_crear_cliente).setVisibility(View.GONE);
        }
        else
        {
            cliente_auto.setText("Consumidor Final");
            cliente_auto.setEnabled(true);
        }

        list_cliente = Cliente.getCliente(getDataBase());
        list_nombres = new ArrayList<String>();
        for (Cliente cli : list_cliente) {
            list_nombres.add(cli.getNombreCompleto().trim());
        }
        list_nombres.add("Consumidor Final");

        //Validar Devoluciones
        List<PedidoDetalle> detalleList = pedido.getPedidoDetalles();
        for(int i = detalleList.size() - 1; i >= 0; i--)
        {
            if(detalleList.get(i).getCantidadDevolucion() == detalleList.get(i).getCantidad())
                pedido.getPedidoDetalles().remove(i);
            else
            {
                detalleList.get(i).setCantidad(detalleList.get(i).getCantidad() - detalleList.get(i).getCantidadDevolucion());
                detalleList.get(i).setCantidadOriginal(detalleList.get(i).getCantidad());
                detalleList.get(i).setSubtotal(detalleList.get(i).getValorUnitario() * detalleList.get(i).getCantidad());
                //detalleList.get(i).setValorDescuentoOro(detalleList.get(i).getValorUnitario() * detalleList.get(i).getPorcentajeDescuentoOro());
                detalleList.get(i).setValorDescuentoOroTotal(detalleList.get(i).getValorDescuentoOro() * detalleList.get(i).getCantidad());
                detalleList.get(i).setValorDescuentoAutomaticoTotal(detalleList.get(i).getValorDescuentoAutomatico() * detalleList.get(i).getCantidad());
                detalleList.get(i).setValorDescuentoManualTotal(detalleList.get(i).getValorDescuentoManual() * detalleList.get(i).getCantidad());
                detalleList.get(i).setValorImpuestoTotal(detalleList.get(i).getValorImpuesto() * detalleList.get(i).getCantidad());
                detalleList.get(i).setProducto(Producto.getProductoIdServer(getDataBase(), detalleList.get(i).getIdProducto()));
                detalleList.get(i).setValorTotal(detalleList.get(i).getSubtotal() - detalleList.get(i).getValorDescuentoOroTotal() - detalleList.get(i).getValorDescuentoAutomaticoTotal() - detalleList.get(i).getValorDescuentoManualTotal() + detalleList.get(i).getValorImpuestoTotal());
                detalleList.get(i).setBaseImponible(detalleList.get(i).getPorcentajeImpuesto() == 0 ? 0 : detalleList.get(i).getSubtotal() - detalleList.get(i).getValorDescuentoOroTotal() - detalleList.get(i).getValorDescuentoAutomaticoTotal() - detalleList.get(i).getValorDescuentoManualTotal());
                detalleList.get(i).setBaseImponibleCero(detalleList.get(i).getPorcentajeImpuesto() == 0 ? detalleList.get(i).getSubtotal() - detalleList.get(i).getValorDescuentoOroTotal() - detalleList.get(i).getValorDescuentoAutomaticoTotal() - detalleList.get(i).getValorDescuentoManualTotal() : 0);
                detalleList.get(i).setSubtotalSinDescuento(detalleList.get(i).getSubtotal());
                detalleList.get(i).setSubtotalSinImpuesto(detalleList.get(i).getSubtotal() - detalleList.get(i).getValorDescuentoOroTotal() - detalleList.get(i).getValorDescuentoAutomaticoTotal() - detalleList.get(i).getValorDescuentoManualTotal());
            }
        }

        if (pedido != null && pedido.getPedidoDetalles() != null && cl != null) {
            ValidarDescuentos(cl.isCiudadanoOro());
            if (cl.getExentoImpuesto()) {
                ValidarExentoImpuestos();
            } else {
                ValidarImpuestos();
            }
        }


        adapter = new PedidoDetalleAdapter(this.getContext(), pedido.getPedidoDetalles());
        adapter.setIsDetail(false);
        ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setAdapter(adapter);

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(getPedidoCantidad(pedido.getPedidoDetalles()) + "");

        descuentos = 0; subtotal = 0; valorTotal = 0; impuestos = 0; base0 = 0; baseImponible = 0; redondeo = 0; neto = 0;
        for (PedidoDetalle detalle : pedido.getPedidoDetalles()) {
            valorTotal = valorTotal + detalle.getValorTotal();
            subtotal = subtotal + detalle.getSubtotal();
            descuentos = descuentos + detalle.getValorDescuentoManualTotal() + detalle.getValorDescuentoAutomaticoTotal() + detalle.getValorDescuentoOroTotal();
            impuestos = impuestos + detalle.getValorImpuestoTotal();
            base0 = base0 + detalle.getBaseImponibleCero();
            baseImponible = baseImponible + detalle.getBaseImponible();
            neto = neto + (detalle.getSubtotal() - (detalle.getValorDescuentoManualTotal() + detalle.getValorDescuentoAutomaticoTotal() + detalle.getValorDescuentoOroTotal()));
        }
        double a_redondondear = Double.parseDouble(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_ROUNDNUM).getValue());
        double residuo = valorTotal % (a_redondondear * 2);
        if(residuo >= a_redondondear)
            redondeo = ((a_redondondear * 2) - residuo);
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
        ((TextView) getRootView().findViewById(R.id.pedido_neto)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(neto));

        float pagado = 0;
        if (pedido.getPagos() != null)
            for (Pago pago : pedido.getPagos()) {
                pagado = pagado + pago.getValor();
            }

        ((TextView) getRootView().findViewById(R.id.pedido_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(valorTotal - pagado));


        if(pedido != null)
        {
            pedido.set_idDocumentoRef(idCliente);
            pedido.setIdDocumentoRef(pedido.getIdPedido());
        }
        idCliente = 0;
        pedido.setID(0);
        pedido.setIdPedido(0);

        if(tipo.equalsIgnoreCase("NC")) {
            getRootView().findViewById(R.id.pedido_agregar_producto).setVisibility(View.VISIBLE);
        }


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
            case AgregarPagoFragment.REQ_CODE_SPEECH_INPUT_PAGO:
                if (resultCode == RESULT_OK && null != data) {
                    if(fragment != null)
                        fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    setTextViewText(R.id.actividad_texto_respuesta, StringUtils.getStringCapSentence(result.get(0)));
                }
                break;
            case CODE_PRINT:
                finish();
                Intent intent = new Intent(getContext(), CrearPedidoActivity.class);
                intent.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "FA");
                startActivity(intent);
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
                        detalle.setPorcentajeDescuentoOro(jsonObject.getDouble("pdo"));
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
                        detalle.setSubtotalSinDescuento(jsonObject.getDouble("ssd"));
                        detalle.setSubtotalSinImpuesto(jsonObject.getDouble("ssi"));
                        detalle.setCodigoExterno(jsonObject.getString("cod"));
                        detalle.setIdBeneficio(jsonObject.getInt("ib"));
                        detalle.setIdVendedor(jsonObject.getString("ven"));
                        if(!jsonObject.isNull("udm"))
                            detalle.setUsrDescManual(jsonObject.getString("udm"));
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
                    if(pedido != null && pedido.getPedidoDetalles() != null) {
                        ValidarDescuentos(cl.isCiudadanoOro());
                        if (cl.getExentoImpuesto()) {
                            ValidarExentoImpuestos();
                        } else {
                            ValidarImpuestos();
                        }
                        setTransaccionValues();
                    }
                    cliente_auto.setText(cl.getNombreCompleto().trim());
                    //cliente_auto.setEnabled(false);
                    ((TextView) getRootView().findViewById(R.id.pedido_email)).setText(cl.getCorreoElectronico());
                    list_cliente = Cliente.getCliente(getDataBase());
                    list_cliente.add(consumidorFinal);
                    list_nombres = new ArrayList<String>();
                    for (Cliente cli : list_cliente) {
                        list_nombres.add(cli.getNombreCompleto().trim());
                    }
                    //list_nombres.add("Consumidor Final");
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
        if(tipo.equalsIgnoreCase("FA")) {
            if (pedido.getPagos() == null) {
                Toast.makeText(getContext(), "Debe de ingresar pago.", Toast.LENGTH_LONG).show();
                return false;
            }
            float pagado = 0;
            if (pedido.getPagos() != null)
                for (Pago pago : pedido.getPagos()) {
                    pagado = pagado + pago.getValor();
                }

            if ((valorTotal - pagado) > 0.005) {
                Toast.makeText(getContext(), "Saldo insuficiente. Debe de ingresar pagos.", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        //Validaciones de Consumidor Final
        if(cliente_auto.getText().toString().equalsIgnoreCase("Consumidor Final"))
        {
            if(!tipo.equalsIgnoreCase("FA"))
            {
                Toast.makeText(getContext(), "No se puede registrar esta transacción a un Consumidor Final.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(valorTotal > Double.parseDouble(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_FINALMOUNT).getValue()))
            {
                Toast.makeText(getContext(), "No se puede facturar a un Consumidor Final un valor mayor a " + GeneralValue.getGeneralValue(getDataBase(), Contants.POS_FINALMOUNT).getValue(), Toast.LENGTH_LONG).show();
                return false;
            }
        }


        return true;
    }

    @Override
    public void onAfterCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        //menu.findItem(R.id.action_forma_pago).setVisible(tipo.equalsIgnoreCase("FA"));
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

        setTransaccionValues();
    }

    @Override
    public void onAcceptSuccess(PedidoDetalle transaction) {
        int exists = -1;

        if(transaction.getCodigoExterno() == null && transaction.getProducto() != null)
            transaction.setCodigoExterno(transaction.getProducto().getCodigoExterno());

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

        int position = list_nombres.indexOf(cliente_auto.getText().toString());
        if (position != -1) {
            if(pedido != null && pedido.getPedidoDetalles() != null) {
                ValidarDescuentos(list_cliente.get(position).isCiudadanoOro());
                if (list_cliente.get(position).getExentoImpuesto()) {
                    ValidarExentoImpuestos();
                } else {
                    ValidarImpuestos();
                }
            }
        }

        setTransaccionValues();
        //prueba
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

    public void generarFacturaFísica() {
        pedido = Pedido.getPedido(getDataBase(), pedido.getID(), true);
        String toPrint = PrintHelper.generarFacturaFísica(pedido, false);

        GeneralValue printParameter = GeneralValue.getGeneralValue(getDataBase(), Contants.POS_PRINTMODE);
        if(printParameter != null) {
            if (printParameter.getValue().equalsIgnoreCase("1")) {
                try {
                    PortInfo portInfo = null;
                    List<PortInfo> portList = StarIOPort.searchPrinter("BT:");
                    for (PortInfo port : portList) {
                        if (port.getPortName().contains("BT:STAR"))
                            portInfo = port;
                    }

                    if (portInfo == null) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                                .setTitle(R.string.title_error_impresión)
                                .setMessage(R.string.warning_impresora_no_vinculada)
                                .setPositiveButton(R.string.action_reintentar, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        generarFacturaFísica();
                                    }
                                })
                                .setCancelable(false);
                        dialog.show();
                        return;
                    } else {
                        StarIOPort port = StarIOPort.getPort(portInfo.getPortName(), "portable;", 10000);
                        if (PrintHelper.isPrinterReady(port.retreiveStatus()) != -1) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.title_error_impresión)
                                    .setMessage(PrintHelper.isPrinterReady(port.retreiveStatus()))
                                    .setPositiveButton(R.string.action_reintentar, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            generarFacturaFísica();
                                        }
                                    })
                                    .setCancelable(false);
                            dialog.show();
                            return;
                        } else {
                            byte[] command = toPrint.getBytes(Charset.forName("UTF-8"));
                            port.writePort(command, 0, command.length);
                            byte[] cut = {27, 100, 3};
                            port.writePort(cut, 0, cut.length);
                        }

                    }

                } catch (StarIOPortException e) {
                    e.printStackTrace();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.title_error_impresión)
                            .setMessage(R.string.warning_error_desconocido)
                            .setPositiveButton(R.string.action_reintentar, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    generarFacturaFísica();
                                }
                            })
                            .setCancelable(false);
                    dialog.show();
                    return;
                }
            } else if (printParameter.getValue().equalsIgnoreCase("2")) {
                Intent print = new Intent(Intent.ACTION_SEND);
                print.addCategory(Intent.CATEGORY_DEFAULT);
                print.putExtra(Intent.EXTRA_TEXT, toPrint);
                print.setType("text/plain");
                startActivityForResult(Intent.createChooser(print, "Imprimir"), CODE_PRINT);
            }
        }

        if(tipo.equalsIgnoreCase("FA")) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.title_cambio)
                    .setMessage("Su cambio es de: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(-pedido.getExcedente()))
                    .setPositiveButton(R.string.action_accept, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                            Intent intent = new Intent(getContext(), CrearPedidoActivity.class);
                            intent.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "FA");
                            startActivity(intent);
                        }
                    })
                    .setCancelable(false);
            dialog.show();
        }
        else {
            finish();
            if(!tipo.equalsIgnoreCase("NC")) {
                Intent intent = new Intent(getContext(), CrearPedidoActivity.class);
                intent.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, tipo);
                startActivity(intent);
            }
        }
    }

    private void setTransaccionValues()
    {
        if (adapter == null) {
            adapter = new PedidoDetalleAdapter(this.getContext(), pedido.getPedidoDetalles());
            ((ListView) getRootView().findViewById(R.id.pedido_detalles)).setAdapter(adapter);
        } else
            adapter.setList(pedido.getPedidoDetalles());

        adapter.notifyDataSetChanged();

        ((TextView) getRootView().findViewById(R.id.pedido_cantidad)).setText(getPedidoCantidad(pedido.getPedidoDetalles()) + "");

        descuentos = 0; subtotal = 0; valorTotal = 0; impuestos = 0; base0 = 0; baseImponible = 0; redondeo = 0; neto = 0;
        for (PedidoDetalle detalle : pedido.getPedidoDetalles()) {
            valorTotal = valorTotal + detalle.getValorTotal();
            subtotal = subtotal + detalle.getSubtotal();
            descuentos = descuentos + detalle.getValorDescuentoManualTotal() + detalle.getValorDescuentoAutomaticoTotal() + detalle.getValorDescuentoOroTotal();
            impuestos = impuestos + detalle.getValorImpuestoTotal();
            base0 = base0 + detalle.getBaseImponibleCero();
            baseImponible = baseImponible + detalle.getBaseImponible();
            neto = neto + (detalle.getSubtotal() - (detalle.getValorDescuentoManualTotal() + detalle.getValorDescuentoAutomaticoTotal() + detalle.getValorDescuentoOroTotal()));
        }
        if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_ROUNDSPECI).getValue().equalsIgnoreCase("1")) {
            double a_redondondear = Double.parseDouble(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_ROUNDNUM).getValue());
            double residuo = valorTotal % (a_redondondear * 2);
            if (residuo >= a_redondondear)
                redondeo = ((a_redondondear * 2) - residuo);
            else
                redondeo = -residuo;
        }
        else
        {
            double a_redondondear = 0.005;
            double residuo = valorTotal % (a_redondondear * 2);
            if (residuo >= a_redondondear)
                redondeo = ((a_redondondear * 2) - residuo);
            else
                redondeo = -residuo;
        }


        valorTotal = valorTotal + redondeo;

        ((TextView) getRootView().findViewById(R.id.pedido_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(valorTotal));
        ((TextView) getRootView().findViewById(R.id.pedido_descuentos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(descuentos));
        ((TextView) getRootView().findViewById(R.id.pedido_impuestos)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(impuestos));
        ((TextView) getRootView().findViewById(R.id.pedido_base_cero)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(base0));
        ((TextView) getRootView().findViewById(R.id.pedido_base_imponible)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(baseImponible));
        ((TextView) getRootView().findViewById(R.id.pedido_subtotal)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(subtotal));
        ((TextView) getRootView().findViewById(R.id.pedido_redondeo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(redondeo));
        ((TextView) getRootView().findViewById(R.id.pedido_neto)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(neto));

        float pagado = 0;
        if (pedido.getPagos() != null)
            for (Pago pago : pedido.getPagos()) {
                pagado = pagado + pago.getValor();
            }

        ((TextView) getRootView().findViewById(R.id.pedido_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(valorTotal - pagado));
    }

    @Override
    public void onAcceptCodeSuccess(PedidoDetalle transaction) {
        int exists = -1;

        if(transaction.getCodigoExterno() == null && transaction.getProducto() != null)
            transaction.setCodigoExterno(transaction.getProducto().getCodigoExterno());

        if (pedido.getPedidoDetalles() == null)
            pedido.setPedidoDetalles(new ArrayList<PedidoDetalle>());

        for (int i = 0; i < pedido.getPedidoDetalles().size(); i++) {
            if (pedido.getPedidoDetalles().get(i).getIdProducto() == transaction.getIdProducto())
                exists = i;
        }

        if (exists == -1)
            pedido.getPedidoDetalles().add(transaction);
        else {
            PedidoDetalle actual = pedido.getPedidoDetalles().get(exists);
            actual.setCantidad(actual.getCantidad() + transaction.getCantidad());
            actual.setSubtotal(actual.getValorUnitario() * actual.getCantidad());
            actual.setValorDescuentoManualTotal(actual.getValorDescuentoManual() * actual.getCantidad());
            actual.setValorImpuestoTotal(actual.getValorImpuesto() * actual.getCantidad());
            actual.setValorTotal(actual.getSubtotal() - actual.getValorDescuentoAutomaticoTotal() - actual.getValorDescuentoManualTotal() + actual.getValorImpuestoTotal());
            actual.setBaseImponible(actual.getPorcentajeImpuesto() == 0 ? 0 : actual.getSubtotal() - actual.getValorDescuentoAutomaticoTotal() - actual.getValorDescuentoManualTotal());
            actual.setBaseImponibleCero(actual.getPorcentajeImpuesto() == 0 ? actual.getSubtotal() - actual.getValorDescuentoAutomaticoTotal() - actual.getValorDescuentoManualTotal() : 0);
            actual.setSubtotalSinDescuento(actual.getSubtotal());
            actual.setSubtotalSinImpuesto(actual.getSubtotal() - actual.getValorDescuentoAutomaticoTotal() - actual.getValorDescuentoManualTotal());
            pedido.getPedidoDetalles().set(exists, actual);
        }

        int position = list_nombres.indexOf(cliente_auto.getText().toString());
        if (position != -1) {
            if(pedido != null && pedido.getPedidoDetalles() != null) {
                ValidarDescuentos(list_cliente.get(position).isCiudadanoOro());
                if (list_cliente.get(position).getExentoImpuesto()) {
                    ValidarExentoImpuestos();
                } else {
                    ValidarImpuestos();
                }
            }
        }

        setTransaccionValues();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable Ahora");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void ValidarExentoImpuestos() {

        for (PedidoDetalle det : pedido.getPedidoDetalles()) {
            det.setValorImpuestoTotal(0);
            det.setValorTotal(det.getSubtotal() - det.getValorDescuentoAutomaticoTotal() - det.getValorDescuentoManualTotal() - det.getValorDescuentoOroTotal());
            det.setBaseImponible(0);
            det.setBaseImponibleCero(det.getSubtotal() - det.getValorDescuentoAutomaticoTotal() - det.getValorDescuentoManualTotal() - det.getValorDescuentoOroTotal());
        }

    }

    private void ValidarImpuestos() {
        for (PedidoDetalle det : pedido.getPedidoDetalles()) {
            if(det.getValorImpuesto() != 0) {
                det.setValorImpuestoTotal(det.getValorImpuesto() * det.getCantidad());
                det.setValorTotal(det.getSubtotal() - det.getValorDescuentoAutomaticoTotal() - det.getValorDescuentoManualTotal() - det.getValorDescuentoOroTotal() + det.getValorImpuestoTotal());
                det.setBaseImponible(det.getSubtotal() - det.getValorDescuentoAutomaticoTotal() - det.getValorDescuentoManualTotal() - det.getValorDescuentoOroTotal());
                det.setBaseImponibleCero(0);
            }
        }
    }

    private void ValidarDescuentos(boolean isOro) {
        for (PedidoDetalle det : pedido.getPedidoDetalles()) {
            if(isOro) {
                det.setValorDescuentoOro(det.getValorUnitario() * det.getPorcentajeDescuentoOro());
                det.setValorDescuentoOroTotal(det.getValorDescuentoOro() * det.getCantidad());
                det.setValorDescuentoAutomatico((det.getValorUnitario() - det.getValorDescuentoOro()) * det.getPorcentajeDescuentoAutomatico());
                det.setValorDescuentoAutomaticoTotal(det.getValorDescuentoAutomatico() * det.getCantidad());
                det.setValorDescuentoManual((det.getValorUnitario() - det.getValorDescuentoAutomatico() - det.getValorDescuentoOro()) * det.getPorcentajeDescuentoManual());
                det.setValorDescuentoManualTotal(det.getValorDescuentoManual() * det.getCantidad());
            }
            else
            {
                det.setValorDescuentoOro(0);
                det.setValorDescuentoOroTotal(0);
                det.setValorDescuentoAutomatico(det.getValorUnitario() * det.getPorcentajeDescuentoAutomatico());
                det.setValorDescuentoAutomaticoTotal(det.getValorDescuentoAutomatico() * det.getCantidad());
                det.setValorDescuentoManual((det.getValorUnitario() - det.getValorDescuentoAutomatico()) * det.getPorcentajeDescuentoManual());
                det.setValorDescuentoManualTotal(det.getValorDescuentoManual() * det.getCantidad());
            }
            det.setValorImpuesto((det.getValorUnitario()- det.getValorDescuentoManual() - det.getValorDescuentoAutomatico() - det.getValorDescuentoOro()) * det.getPorcentajeImpuesto());
            det.setValorImpuestoTotal(det.getValorImpuesto() * det.getCantidad());
            det.setProducto(Producto.getProductoIdServer(getDataBase(), det.getIdProducto()));
            det.setValorTotal(det.getSubtotal() - det.getValorDescuentoAutomaticoTotal() - det.getValorDescuentoManualTotal() - det.getValorDescuentoOroTotal() + det.getValorImpuestoTotal());
            det.setBaseImponible(det.getPorcentajeImpuesto() == 0 ? 0 : det.getSubtotal() - det.getValorDescuentoAutomaticoTotal() - det.getValorDescuentoManualTotal() - det.getValorDescuentoOroTotal());
            det.setBaseImponibleCero(det.getPorcentajeImpuesto() == 0 ? det.getSubtotal() - det.getValorDescuentoAutomaticoTotal() - det.getValorDescuentoManualTotal() - det.getValorDescuentoOroTotal() : 0);
            det.setSubtotalSinDescuento(det.getSubtotal());
            det.setSubtotalSinImpuesto(det.getSubtotal() - det.getValorDescuentoAutomaticoTotal() - det.getValorDescuentoManualTotal() - det.getValorDescuentoOroTotal());
        }
    }

    public double getNewSaldo(List<Integer> idsFormaPagos)
    {
        if(pedido != null && pedido.getPedidoDetalles() != null) {
            for (PedidoDetalle det : pedido.getPedidoDetalles()) {
                //Obtengo promociones del producto
                List<ProductoPromocion> promociones = ProductoPromocion.getProductoPromocionAll(getDataBase(), det.getIdProducto());

                //Reviso primero si tiene promociones
                //Si tiene, se procesa, caso contrario, paso al siguiente detalle
                if (promociones.size() > 0) {
                    boolean isPromo = false;
                    //Chequeo cada promocion
                    for (ProductoPromocion promo : promociones) {

                        //Por cada promocion veo si tiene un descuento por forma de pago
                        if (promo.getFormaPagoAplica() != null && !promo.getFormaPagoAplica().equalsIgnoreCase("null")) {
                            //En el caso de que la tenga, recorro el arreglo de formas de pago hechas y comparo para ver si tiene ingresada cualquiera de las indicadas
                            for (int fp : promo.getIdFormaPago()) {
                                //En el caso de que exista algun descuento del producto por forma de pago, comparo el descuento que tiene el detalle con el descuento
                                //que ofrece la promocion
                                if (idsFormaPagos.contains(fp)) {
                                    isPromo = true;
                                    //Si el descuento ofrecido por la promocion es mayor, cambio el descuento y el idBeneficio, caso contrario, sigue igual
                                    if (promo.getPorcentajeDescuento() > det.getPorcentajeDescuentoAutomatico()) {
                                        det.setPorcentajeDescuentoAutomatico(promo.getPorcentajeDescuento());
                                        det.setIdBeneficio(promo.getIdBeneficio());
                                    }
                                }
                            }
                        } else {
                            //Caso contrario, chequeo si el detalle ya tiene un descuento, incluido dentro de las promociones consultadas.
                            if (promo.getIdBeneficio() != det.getIdBeneficio() && !isPromo) {
                                det.setPorcentajeDescuentoAutomatico(promo.getPorcentajeDescuento());
                                det.setIdBeneficio(promo.getIdBeneficio());
                            }
                            isPromo = true;

                        }

                    }
                    //Si existe, paso al siguiente detalle, caso contrario, encero el descuento y el idBeneficio
                    if (!isPromo) {
                        det.setPorcentajeDescuentoAutomatico(0);
                        det.setIdBeneficio(0);
                    }
                }
                //Terminado el proceso paso al siguiente detalle
            }

            //Terminado el proceso con todos los descuentos, los vuelvo a procesar y a setear.
            int position = list_nombres.indexOf(cliente_auto.getText().toString());
            if (position != -1) {
                if (pedido != null && pedido.getPedidoDetalles() != null) {
                    ValidarDescuentos(list_cliente.get(position).isCiudadanoOro());
                    if (list_cliente.get(position).getExentoImpuesto()) {
                        ValidarExentoImpuestos();
                    } else {
                        ValidarImpuestos();
                    }
                }
            }
            else
            {
                ValidarDescuentos(false);
                ValidarImpuestos();
            }
            setTransaccionValues();
        }
        //Retorno el saldo para que se presente en la forma de pago.
        return valorTotal;
    }
}
