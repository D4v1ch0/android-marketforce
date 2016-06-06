package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientDetailFragment;
import rp3.marketforce.cliente.ClientListFragment;
import rp3.marketforce.cliente.CrearClienteActivity;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.models.pedido.ProductoCodigo;
import rp3.marketforce.models.pedido.ProductoPromocion;
import rp3.marketforce.ruta.MapaActivity;
import rp3.marketforce.sync.Agenda;
import rp3.marketforce.sync.Productos;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.PrintHelper;
import rp3.util.CalendarUtils;
import rp3.util.ConnectionUtils;
import rp3.util.Convert;
import rp3.util.StringUtils;
import rp3.widget.SlidingPaneLayout;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoFragment extends BaseFragment implements PedidoListFragment.PedidoListFragmentListener,PedidoDetailFragment.PedidoDetailFragmentListener {

    private static final int PARALLAX_SIZE = 0;
    private static final int DIALOG_SYNC_PRODUCTOS = 1;
    private static final int DIALOG_CIERRE = 2;
    private static final int DIALOG_REPRINT = 3;
    private static final int DIALOG_CANCELAR = 4;

    private PedidoListFragment transactionListFragment;
    private PedidoDetailFragment transactionDetailFragment;
    private SlidingPaneLayout slidingPane;

    private Menu menu;
    public boolean mTwoPane = false;
    public boolean isActiveListFragment = true;
    private long selectedClientId;
    private boolean isContact = false;
    private String textSearch;
    private AnularTransaccionFragment anulaFragment;

    public static PedidoFragment newInstance(int transactionTypeId) {
        PedidoFragment fragment = new PedidoFragment();
        return fragment;
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_client, R.menu.fragment_pedido_menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        transactionListFragment = PedidoListFragment.newInstance();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        slidingPane = (SlidingPaneLayout) rootView.findViewById(R.id.sliding_pane_clientes);
        slidingPane.setParallaxDistance(PARALLAX_SIZE);
        slidingPane.setShadowResource(R.drawable.sliding_pane_shadow);
        slidingPane.setSlidingEnabled(false);
        slidingPane.openPane();

        if(slidingPane.isOpen() &&
                rootView.findViewById(R.id.content_transaction_list).getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT)
            mTwoPane = true;
        else
            mTwoPane = false;

        if(!hasFragment(R.id.content_transaction_list))
            setFragment(R.id.content_transaction_list, transactionListFragment );

        slidingPane.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener(){

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPanelOpened(View panel) {
                isActiveListFragment = true;
                //getActivity().getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
                RefreshMenu();
                getActivity().setTitle("Transacciones");
                transactionListFragment.searchTransactions(textSearch);
            }

            @Override
            public void onPanelClosed(View panel) {
                isActiveListFragment = false;
                // if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2) {
                // getActivity().getActionBar().setHomeButtonEnabled(true);
                //}
            }});

//		if(getChildFragmentManager().findFragmentById(R.id.transaction_detail) == null){
//			if(rootView.findViewById(R.id.content_transaction_list)!=null){
//
//			}
//		}
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        /*if(selectedClientId != 0){
            if(!mTwoPane)
                slidingPane.closePane();
        }*/
    }

    @Override
    public void onAfterCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        SearchView searchView = null;
        MenuItem prob = menu.findItem(R.id.action_search);
        if(prob != null)
            searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        if(searchView != null) {
            int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
            searchPlate.setHintTextColor(getResources().getColor(R.color.color_hint));
            searchPlate.setHint(getActivity().getResources().getString(R.string.hint_search_transaction_rutas));
            searchPlate.setTextColor(getResources().getColor(R.color.apptheme_color));
            searchPlate.setBackgroundResource(R.drawable.apptheme_edit_text_holo_light);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (transactionListFragment != null)
                        transactionListFragment.searchTransactions(query);
                    return true;
                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText) && !TextUtils.isEmpty(textSearch)) {
                        try {
                            transactionListFragment.searchTransactions("");
                        } catch (Exception ex) {

                        }
                    }
                    textSearch = newText;

                    return true;

                }
            });
        }
        RefreshMenu();
    }

    public void RefreshMenu(){
        //Log.e("CARGA PEDIDOS", "Antes de Creacion de Menu: " + Calendar.getInstance().getTime().toString());
        Pedido ped = Pedido.getPedido(getDataBase(), selectedClientId, false);
        Pedido ref = Pedido.getPedidoRef(getDataBase(), selectedClientId);
        ControlCaja control = ControlCaja.getControlCajaActiva(getDataBase());
        Calendar calPed = Calendar.getInstance();
        Calendar calApe = Calendar.getInstance();
        Pago pagoNC = null;
        if(control != null)
            calApe.setTime(control.getFechaApertura());
        if(selectedClientId != 0) {
            calPed.setTime(ped.getFechaCreacion());
            pagoNC = Pago.getPagoNC(getDataBase(), ped.getNumeroDocumento());
        }
        if(!mTwoPane){
            menu.findItem(R.id.action_arqueo_caja).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_crear_pedido).setVisible(isActiveListFragment && control != null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calApe) == 0 && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            menu.findItem(R.id.action_sincronizar_productos).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_cancelar_transaccion).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && control != null && !ped.isTieneNotaCreditoRP3POS() && pagoNC == null
                    && ref == null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calPed) == 0 && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA) && ped.getTipoDocumento().equalsIgnoreCase("NC"));
            menu.findItem(R.id.action_anular_pedido).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && control != null && !ped.isTieneNotaCreditoRP3POS() && pagoNC == null
                    && ref == null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calPed) == 0 && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            menu.findItem(R.id.action_nota_credito).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && ped.getTipoDocumento().equalsIgnoreCase("FA") && control != null
                    && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calApe) == 0 && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_NOTA_CREDITO, true) && !ped.isTieneNotaCreditoRP3POS() && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            menu.findItem(R.id.action_aperturar_caja).setVisible(isActiveListFragment && control == null && PreferenceManager.getInt(Contants.KEY_ID_CAJA,0) != 0);
            menu.findItem(R.id.action_cerrar_caja).setVisible(isActiveListFragment && control != null);
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_ver_pagos).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getTipoDocumento().equalsIgnoreCase("FA"));
            menu.findItem(R.id.action_reimpresion).setVisible(!isActiveListFragment && selectedClientId!=0 && control != null);
            menu.findItem(R.id.action_cotización_a_factura).setVisible(!isActiveListFragment && selectedClientId!=0 && control != null && ped.getTipoDocumento().equalsIgnoreCase("CT") && ref == null && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_FACTURA, true) && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            //menu.findItem(R.id.action_nota_credito).setVisible(false);
        }
        else{
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_arqueo_caja).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_crear_pedido).setVisible(isActiveListFragment && control != null && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            menu.findItem(R.id.action_cancelar_transaccion).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && control != null && !ped.isTieneNotaCreditoRP3POS() && pagoNC == null
                    && ref == null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calPed) == 0 && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA) && ped.getTipoDocumento().equalsIgnoreCase("NC"));
            menu.findItem(R.id.action_anular_pedido).setVisible(selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && control != null && ref == null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calPed) == 0 && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_NOTA_CREDITO, true) && !ped.isTieneNotaCreditoRP3POS() && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            menu.findItem(R.id.action_nota_credito).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && ped.getTipoDocumento().equalsIgnoreCase("FA") && control != null && !ped.isTieneNotaCreditoRP3POS() && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            menu.findItem(R.id.action_aperturar_caja).setVisible(isActiveListFragment && control == null && PreferenceManager.getInt(Contants.KEY_ID_CAJA, 0) != 0);
            menu.findItem(R.id.action_cerrar_caja).setVisible(isActiveListFragment && control != null);
            menu.findItem(R.id.action_reimpresion).setVisible(!isActiveListFragment && selectedClientId != 0 && control != null);
            menu.findItem(R.id.action_ver_pagos).setVisible(!isActiveListFragment && selectedClientId != 0 && ped.getTipoDocumento().equalsIgnoreCase("FA"));
            menu.findItem(R.id.action_cotización_a_factura).setVisible(!isActiveListFragment && selectedClientId != 0 && control != null && ped.getTipoDocumento().equalsIgnoreCase("CT") && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_FACTURA, true) && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            //menu.findItem(R.id.action_nota_credito).setVisible(false);

        }
        Log.e("CARGA PEDIDOS", "Despues de creacion de menu: " + Calendar.getInstance().getTime().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_arqueo_caja:
                Intent intent = new Intent(getContext(), ArqueoCajaActivity.class);
                startActivity(intent);
                break;
            case R.id.action_ver_pagos:
                Pedido ped = Pedido.getPedido(getDataBase(), selectedClientId, true);
                PagosListFragment fragment = PagosListFragment.newInstance(ped.getValorTotal());
                fragment.pagos = ped.getPagos();
                fragment.isDetail = true;
                showDialogFragment(fragment, "Formas de Pago", "Formas de Pago");
                break;
            case R.id.action_reimpresion:
                showDialogConfirmation(DIALOG_REPRINT, R.string.message_reimprimir, R.string.action_reimpresion);
                break;
            case R.id.action_aperturar_caja:
                showDialogFragment(new ControlCajaFragment(), "Aperturar Caja", "Aperturar Caja");
                break;
            case R.id.action_cerrar_caja:
                showDialogConfirmation(DIALOG_CIERRE, R.string.message_cierre_caja, R.string.title_cerrar_caja_activa);
                break;
            case R.id.action_cancelar_transaccion:
                showDialogConfirmation(DIALOG_CANCELAR, R.string.message_cancelar_nc, R.string.title_cancelar_nc);
                break;
            case R.id.action_anular_pedido:
                if(transactionDetailFragment != null)
                {
                    anulaFragment = AnularTransaccionFragment.newInstance(selectedClientId);
                    showDialogFragment(anulaFragment, "Anular", "Anular Transacción");
                }
                break;
            case R.id.action_cotización_a_factura:
                if(PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA, -1) != -1) {
                    if(ValidateSecuencia("FA")) {
                        Intent intent2 = new Intent(getContext(), CrearPedidoActivity.class);
                        intent2.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "FA");
                        intent2.putExtra(CrearPedidoActivity.ARG_IDPEDIDO, selectedClientId);
                        startActivity(intent2);
                        break;
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Número de documento de transacción ya existe. Por favor, comunicarse con el administrador del sistema.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this.getContext(), "Su usuario no tiene asignado una caja.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_nota_credito:
                if(PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO, -1) != -1) {
                    if(ValidateSecuencia("NC")) {
                        Intent intent2 = new Intent(getContext(), CrearPedidoActivity.class);
                        intent2.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "NC");
                        intent2.putExtra(CrearPedidoActivity.ARG_IDPEDIDO, selectedClientId);
                        startActivity(intent2);
                        break;
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Número de documento de transacción ya existe. Por favor, comunicarse con el administrador del sistema.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(this.getContext(), "Su usuario no tiene asignado una caja.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_crear_pedido:
                if(PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA, -1) != -1) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());

                    final SimpleGeneralValueAdapter adapter = new SimpleGeneralValueAdapter(this.getContext(), GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_TIPOS_TRANSACCION, "NC"));
                    builderSingle.setTitle("Seleccione tipo de transacción");

                    builderSingle.setAdapter(
                            adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(PreferenceManager.getBoolean(adapter.getGeneralValue(which).getCode(), true)) {
                                        if(ValidateSecuencia(adapter.getGeneralValue(which).getCode()))
                                        {
                                            Intent intent = new Intent(getContext(), CrearPedidoActivity.class);
                                            intent.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, adapter.getGeneralValue(which).getCode());
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), "Número de documento de transacción ya existe. Por favor, comunicarse con el administrador del sistema.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "No tiene permisos para realizar esta transacción.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    builderSingle.show();

                }
                else
                {
                    Toast.makeText(this.getContext(), "Su usuario no tiene asignado una caja.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_sincronizar_productos:
                if(!ConnectionUtils.isNetAvailable(this.getContext()))
                {
                    Toast.makeText(this.getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                }
                else {
                    if(Producto.getProductos(getDataBase()).size() <= 0)
                    {
                        showDialogConfirmation(DIALOG_SYNC_PRODUCTOS, R.string.message_sin_productos, R.string.action_sincronizar_productos);
                    }
                    else
                    {
                        showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait, false, ProgressDialog.STYLE_HORIZONTAL);
                        setDialogProgressMax(1);
                        new UpdateProductos().execute();
                    }
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        switch (id) {
            case DIALOG_SYNC_PRODUCTOS:
                showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait, false, ProgressDialog.STYLE_HORIZONTAL);
                setDialogProgressMax(1);
                new UpdateProductos().execute();
                break;
            case DIALOG_REPRINT:
                generarFacturaFísica();
                break;
            case DIALOG_CIERRE:
                ControlCaja control = ControlCaja.getControlCajaActiva(getDataBase());
                List<Pago> pagos = Pago.getArqueoCaja(getDataBase(), control.getID(), false);

                float valor = 0;
                for(Pago pago: pagos)
                {
                    valor = valor + pago.getValor();
                }
                control.setFechaCierre(Calendar.getInstance().getTime());
                control.setValorCierre(valor);
                ControlCaja.update(getDataBase(), control);
                if (ConnectionUtils.isNetAvailable(getActivity())) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_CAJA);
                    bundle2.putLong(ControlCajaFragment.ARG_CONTROL, control.getID());
                    requestSync(bundle2);
                }
                RefreshMenu();
                break;
            case DIALOG_CANCELAR:
                Pedido ped = Pedido.getPedido(getDataBase(), selectedClientId, false);
                ped.setEstado("N");
                Pedido.update(getDataBase(), ped);
                transactionDetailFragment = PedidoDetailFragment.newInstance(ped);
                setFragment(R.id.content_transaction_detail, transactionDetailFragment);
                Bundle bundle3 = new Bundle();
                bundle3.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CANCELAR_NC);
                bundle3.putLong(CrearPedidoFragment.ARG_PEDIDO, selectedClientId);
                requestSync(bundle3);
                break;
        }

    }

    @Override
    public void onPermisoChanged(Pedido permiso) {
        if(transactionDetailFragment != null)
            transactionDetailFragment.onResume();
    }

    @Override
    public void onPermisoSelected(Pedido pedido) {
        selectedClientId = pedido.getID();

        if (!mTwoPane) {
            slidingPane.closePane();
            isActiveListFragment = false;
        }

        RefreshMenu();

        this.getActivity().setTitle(pedido.getTransaccion().getValue() + " No. " + pedido.getNumeroDocumento());

        transactionDetailFragment = PedidoDetailFragment.newInstance(pedido);
        setFragment(R.id.content_transaction_detail, transactionDetailFragment);
    }

    @Override
    public void onFinalizaConsulta() {

    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);
    }

    @Override
    public boolean allowSelectedItem() {
        return mTwoPane;
    }

    public void generarFacturaFísica() {

        Pedido pedido = Pedido.getPedido(getDataBase(), selectedClientId, true);
        String toPrint = PrintHelper.generarFacturaFísica(pedido, true);

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
                        .setCancelable(true);
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
                            .setCancelable(true);
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
                    .setCancelable(true);
            dialog.show();
            return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

                anulaFragment.onActivityResult(requestCode, resultCode, data);

    }

    private boolean ValidateSecuencia(String tipoDocumento)
    {
        String numeroDocumento = "";
        if(tipoDocumento.equalsIgnoreCase("FA"))
            numeroDocumento = CrearPedidoFragment.getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO)), 3) + "-" + CrearPedidoFragment.getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_SERIE)), 3) +
                    "-" + CrearPedidoFragment.getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_FACTURA) + 1, 9);
        if(tipoDocumento.equalsIgnoreCase("NC"))
            numeroDocumento = CrearPedidoFragment.getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO)), 3) + "-" + CrearPedidoFragment.getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_SERIE)), 3) +
                    "-" + CrearPedidoFragment.getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO) + 1, 9);
        if(tipoDocumento.equalsIgnoreCase("PD"))
            numeroDocumento = CrearPedidoFragment.getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO)), 3) + "-" + CrearPedidoFragment.getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_SERIE)), 3) +
                    "-" + CrearPedidoFragment.getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_PEDIDO) + 1, 9);
        if(tipoDocumento.equalsIgnoreCase("CT"))
            numeroDocumento = CrearPedidoFragment.getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_ESTABLECIMIENTO)), 3) + "-" + CrearPedidoFragment.getSecuencia(Integer.parseInt(PreferenceManager.getString(Contants.KEY_SERIE)), 3) +
                    "-" + CrearPedidoFragment.getSecuencia(PreferenceManager.getInt(Contants.KEY_SECUENCIA_COTIZACION) + 1, 9);

        Pedido ped = Pedido.getPedidoRepetido(getDataBase(), numeroDocumento, tipoDocumento);
        if(ped.getID() == 0)
            return true;
        else
            return false;
    }

    public class UpdateProductos extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            //Se llama a conteo de productos
            publishProgress(new Integer[] {1, 0, R.string.message_descarga_productos});
            Bundle conteo = Productos.executeSyncConteo(getDataBase());
            int total = 0, descargados = 0, pagina = 0;
            if(conteo != null && conteo.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS)
                total = conteo.getInt("Conteo",0);
            else
            {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            publishProgress(new Integer[]{total, 0, R.string.message_descarga_productos});
            while(total != 0 && total > descargados) {
                Bundle bundle = Productos.executeSync(getDataBase(), pagina, 3000);
                pagina++;
                if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                    try {
                        JSONArray types = new JSONArray(bundle.getString("Productos"));
                        int length = types.length();
                        for (int i = 0; i < length; i++) {
                            descargados++;
                            publishProgress(new Integer[]{total, descargados, R.string.message_actualizar_productos});

                            JSONObject type = types.getJSONObject(i);
                            if (type.getString("E").equalsIgnoreCase("A")) {

                                rp3.marketforce.models.pedido.Producto producto = rp3.marketforce.models.pedido.Producto.getProductoIdServer(getDataBase(), type.getInt("Id"));

                                producto.setIdProducto(type.getInt("Id"));
                                producto.setDescripcion(type.getString("D"));
                                producto.setUrlFoto(type.getString("U"));
                                producto.setValorUnitario(type.getDouble("P"));
                                producto.setCodigoExterno(type.getString("Ex"));
                                producto.setPrecioDescuento(Float.parseFloat(type.getString("PCD")));
                                producto.setPrecioImpuesto(Float.parseFloat(type.getString("PCI")));
                                if(!type.isNull("PDO"))
                                    producto.setPorcentajeDescuentoOro(Float.parseFloat(type.getString("PDO")));
                                else
                                    producto.setPorcentajeDescuentoOro(0);
                                producto.setPorcentajeDescuento(Float.parseFloat(type.getString("PD")));
                                producto.setPorcentajeImpuesto(Float.parseFloat(type.getString("PI")));
                                producto.setIdBeneficio(type.getInt("B"));
                                if (type.isNull("IdS"))
                                    producto.setIdSubCategoria(0);
                                else
                                    producto.setIdSubCategoria(type.getInt("IdS"));

                                if (producto.getID() == 0)
                                    rp3.marketforce.models.pedido.Producto.insert(getDataBase(), producto);
                                else
                                    rp3.marketforce.models.pedido.Producto.update(getDataBase(), producto);

                                ProductoCodigo.deleteCodigos(getDataBase(), producto.getCodigoExterno());
                                JSONArray strs = type.getJSONArray("PC");

                                for (int j = 0; j < strs.length(); j++) {
                                    JSONObject str = strs.getJSONObject(j);

                                    ProductoCodigo productoCodigo = new ProductoCodigo();
                                    productoCodigo.setCodigoExterno(str.getString("Ex"));
                                    productoCodigo.setCodigo(str.getString("C"));
                                    ProductoCodigo.insert(getDataBase(), productoCodigo);
                                }
                            } else {
                                Producto.deleteProducto(getDataBase(), type.getInt("Id"));
                            }


                        }
                    } catch (JSONException e) {
                        Log.e("Entro", "Error: " + e.toString());
                        return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                    }
                } else {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            }
            publishProgress(new Integer[] {1, 0, R.string.message_descarga_promociones});
            Bundle bundle = Productos.executeSyncPromociones(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("Promociones"));
                    int length = types.length();
                    ProductoPromocion.deleteAll(getDataBase(), Contract.ProductoPromocion.TABLE_NAME);
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[]{length, i, R.string.message_descarga_promociones});

                        JSONObject type = types.getJSONObject(i);

                        ProductoPromocion productoPromocion = new ProductoPromocion();

                        productoPromocion.setIdProducto(type.getInt("IdProducto"));
                        productoPromocion.setIdBeneficio(type.getInt("IdBeneficio"));
                        productoPromocion.setIdEstablecimiento(type.getInt("IdEstablecimiento"));
                        productoPromocion.setIdPuntoOperacion(type.getInt("IdPuntoOperacion"));
                        productoPromocion.setFechaDesde(Convert.getDateFromDotNetTicks(type.getLong("FechaDesdeTicks")));
                        productoPromocion.setFechaHasta(Convert.getDateFromDotNetTicks(type.getLong("FechaHastaTicks")));
                        productoPromocion.setPorcentajeDescuento(Float.parseFloat(type.getString("PorcentajeDescuento")));
                        if(!type.isNull("FormaPagoAplica") && type.getString("FormaPagoAplica").trim().length() > 0)
                            productoPromocion.setFormaPagoAplica(type.getString("FormaPagoAplica"));
                        ProductoPromocion.insert(getDataBase(), productoPromocion);
                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            }
            else
            {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            publishProgress(new Integer[]{1, 0, R.string.message_descarga_categorias});
            bundle = Productos.executeSyncCategorias(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("Categorias"));
                    int length = types.length();
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[] {length, i, R.string.message_actualizar_categorias});

                        JSONObject type = types.getJSONObject(i);

                        rp3.marketforce.models.pedido.Categoria categoria = rp3.marketforce.models.pedido.Categoria.getCategoria(getDataBase(), type.getInt("IdCategoria"));

                        categoria.setIdCategoria(type.getInt("IdCategoria"));
                        categoria.setDescripcion(type.getString("Descripcion"));

                        if (categoria.getID() == 0)
                            rp3.marketforce.models.pedido.Categoria.insert(getDataBase(), categoria);
                        else
                            rp3.marketforce.models.pedido.Categoria.update(getDataBase(), categoria);


                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            }
            else
            {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            publishProgress(new Integer[] {1, 0, R.string.message_descarga_subcategorias});
            bundle = Productos.executeSyncSubCategorias(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("SubCategorias"));
                    int length = types.length();
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[] {length, i, R.string.message_actualizar_subcategorias});
                        JSONObject type = types.getJSONObject(i);

                        rp3.marketforce.models.pedido.SubCategoria categoria = rp3.marketforce.models.pedido.SubCategoria.getSubCategoria(getDataBase(), type.getInt("IdSubCategoria"));

                        categoria.setIdSubCategoria(type.getInt("IdSubCategoria"));
                        categoria.setIdCategoria(type.getInt("IdCategoria"));
                        categoria.setDescripcion(type.getString("Descripcion"));

                        if (categoria.getID() == 0)
                            rp3.marketforce.models.pedido.SubCategoria.insert(getDataBase(), categoria);
                        else
                            rp3.marketforce.models.pedido.SubCategoria.update(getDataBase(), categoria);
                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else
            {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            setDialogProgressMessage(getString(values[2]));
            setDialogProgressMax(values[0]);
            setDialogProgressNumber(values[1]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            closeDialogProgress();
            if(s != null)
                showDialogMessage(s);
            else {
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PRODUCTOS);
                requestSync(bundle);
            }
            super.onPostExecute(s);
        }
    }
}
