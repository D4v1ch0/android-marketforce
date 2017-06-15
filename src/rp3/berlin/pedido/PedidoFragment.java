package rp3.berlin.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.db.Contract;
import rp3.berlin.models.pedido.Alternativo;
import rp3.berlin.models.pedido.Categoria;
import rp3.berlin.models.pedido.ControlCaja;
import rp3.berlin.models.pedido.LibroPrecio;
import rp3.berlin.models.pedido.MatrizPrecio;
import rp3.berlin.models.pedido.Pago;
import rp3.berlin.models.pedido.Pedido;
import rp3.berlin.models.pedido.Producto;
import rp3.berlin.models.pedido.ProductoCodigo;
import rp3.berlin.models.pedido.SecuenciaMatriz;
import rp3.berlin.models.pedido.Serie;
import rp3.berlin.models.pedido.SubCategoria;
import rp3.berlin.sync.Productos;
import rp3.berlin.sync.SyncAdapter;
import rp3.berlin.utils.PrintHelper;
import rp3.util.ConnectionUtils;
import rp3.util.Convert;
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
    private boolean allProducts = false;
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
            //menu.findItem(R.id.action_arqueo_caja).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_crear_pedido).setVisible(isActiveListFragment);
            menu.findItem(R.id.action_sincronizar_productos).setVisible(isActiveListFragment);
            //menu.findItem(R.id.action_cancelar_transaccion).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && control != null && !ped.isTieneNotaCreditoRP3POS() && pagoNC == null
            //        && ref == null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calPed) == 0 && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA) && ped.getTipoDocumento().equalsIgnoreCase("NC"));
            //menu.findItem(R.id.action_anular_pedido).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && control != null && !ped.isTieneNotaCreditoRP3POS() && pagoNC == null
            //        && ref == null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calPed) == 0 && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            menu.findItem(R.id.action_edit).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("P") && ped.getTipoDocumento().equalsIgnoreCase("PD") && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_PEDIDO, true));
            //menu.findItem(R.id.action_aperturar_caja).setVisible(isActiveListFragment && control == null && PreferenceManager.getInt(Contants.KEY_ID_CAJA,0) != 0);
            //menu.findItem(R.id.action_cerrar_caja).setVisible(isActiveListFragment && control != null);
            menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            //menu.findItem(R.id.action_ver_pagos).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getTipoDocumento().equalsIgnoreCase("FA"));
            //menu.findItem(R.id.action_reimpresion).setVisible(!isActiveListFragment && selectedClientId!=0 && control != null);
            menu.findItem(R.id.action_cotización_a_factura).setVisible(!isActiveListFragment && selectedClientId!=0 && ped.getTipoDocumento().equalsIgnoreCase("CT") && ref == null && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_PEDIDO, true));
            //menu.findItem(R.id.action_nota_credito).setVisible(false);
        }
        else{
            //menu.findItem(R.id.action_search).setVisible(isActiveListFragment);
            //menu.findItem(R.id.action_arqueo_caja).setVisible(isActiveListFragment);
            //menu.findItem(R.id.action_crear_pedido).setVisible(control != null && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            //menu.findItem(R.id.action_cancelar_transaccion).setVisible(selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && control != null && !ped.isTieneNotaCreditoRP3POS() && pagoNC == null
            //        && ref == null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calPed) == 0 && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA) && ped.getTipoDocumento().equalsIgnoreCase("NC"));
            //menu.findItem(R.id.action_anular_pedido).setVisible(selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("C") && control != null && ref == null && CalendarUtils.DayDiffTruncate(Calendar.getInstance(), calPed) == 0 && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_NOTA_CREDITO, true) && !ped.isTieneNotaCreditoRP3POS() && control.getIdCaja() == PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            menu.findItem(R.id.action_edit).setVisible(selectedClientId!=0 && ped.getEstado().equalsIgnoreCase("P") && ped.getTipoDocumento().equalsIgnoreCase("PD") && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_PEDIDO, true));
            //menu.findItem(R.id.action_aperturar_caja).setVisible(control == null && PreferenceManager.getInt(Contants.KEY_ID_CAJA, 0) != 0);
            //menu.findItem(R.id.action_cerrar_caja).setVisible(control != null);
            //menu.findItem(R.id.action_reimpresion).setVisible(selectedClientId != 0 && control != null);
            //menu.findItem(R.id.action_ver_pagos).setVisible(selectedClientId != 0 && ped.getTipoDocumento().equalsIgnoreCase("FA"));
            menu.findItem(R.id.action_cotización_a_factura).setVisible( selectedClientId != 0 && ped.getTipoDocumento().equalsIgnoreCase("CT") && PreferenceManager.getBoolean(Contants.KEY_TRANSACCION_PEDIDO, true));
            //menu.findItem(R.id.action_nota_credito).setVisible(false);

        }
        Log.e("CARGA PEDIDOS", "Despues de creacion de menu: " + Calendar.getInstance().getTime().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            /*case R.id.action_arqueo_caja:
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
                break;*/
            case R.id.action_cotización_a_factura:

                Intent intent3 = new Intent(getContext(), CrearPedidoActivity.class);
                intent3.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "PD");
                intent3.putExtra(CrearPedidoActivity.ARG_IDPEDIDO, selectedClientId);
                startActivity(intent3);
                break;
            case R.id.action_edit:
                Intent intent2 = new Intent(getContext(), CrearPedidoActivity.class);
                intent2.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, "PD");
                intent2.putExtra(CrearPedidoActivity.ARG_IDPEDIDO, selectedClientId);
                startActivity(intent2);
                break;
            case R.id.action_saldos_bodega:
                BodegaFragment bodegaFragment = BodegaFragment.newInstance("");
                showDialogFragment(bodegaFragment, "Saldos de Bodega", "Saldos de Bodega");
                break;
            case R.id.action_consulta_precio:
                Intent intent4 = new Intent(getContext(), ConsultaPrecioActivity.class);
                startActivity(intent4);
                break;
            case R.id.action_crear_pedido:
                int conteoProductos = Producto.conteoProductos(getDataBase());
                if(conteoProductos > 0) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());

                    final SimpleGeneralValueAdapter adapter = new SimpleGeneralValueAdapter(this.getContext(), GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_TIPOS_TRANSACCION, "NC"));
                    builderSingle.setTitle("Seleccione tipo de transacción");

                    builderSingle.setAdapter(
                            adapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (PreferenceManager.getBoolean(adapter.getGeneralValue(which).getCode(), true)) {
                                        PedidoParametrosFragment pedidoParametrosFragment = PedidoParametrosFragment.newInstance(adapter.getGeneralValue(which).getCode());
                                        showDialogFragment(pedidoParametrosFragment, "Cabecera", adapter.getGeneralValue(which).getValue());
                                    } else {
                                        Toast.makeText(getContext(), "No tiene permisos para realizar esta transacción.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    builderSingle.show();
                }
                else
                {
                    showDialogMessage("Sin Productos", "Debe sincronizar los productos para realizar transacciones.");
                }

                break;
            case R.id.action_sincronizar_productos:
                if (!ConnectionUtils.isNetAvailable(this.getContext())) {
                    Toast.makeText(this.getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                } else {
                    int conteo = Producto.conteoProductos(getDataBase());
                    if (conteo <= 0) {
                        allProducts = true;
                        showDialogConfirmation(DIALOG_SYNC_PRODUCTOS, R.string.message_sin_productos, R.string.action_sincronizar_productos);
                    } else {
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
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
            //En el caso de que se haga un bulk insert, se inicializa parametros
            // you can use INSERT only
            String sql = "", sqlCodigos = "", sqlProdBusq = "";
            DataBase db = null;
            SQLiteStatement stmt = null, stmtSearch = null, stmtCod = null;

            //Se llama a conteo de productos
            publishProgress(new Integer[]{1, 0, R.string.message_descarga_productos});
            Bundle conteo = Productos.executeSyncConteo(getDataBase());
            int total = 0, descargados = 0, pagina = 0;
            if (conteo != null && conteo.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS)
                total = conteo.getInt("Conteo", 0);
            else {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            publishProgress(new Integer[]{total, 0, R.string.message_descarga_productos});

            //Elimino Productos
            Producto.deleteAll(getDataBase(), Contract.Producto.TABLE_NAME);
            Producto.ProductoExt.deleteAll(getDataBase(), Contract.ProductoExt.TABLE_NAME);
            ProductoCodigo.deleteAll(getDataBase(), Contract.ProductoCodigo.TABLE_NAME);
            sql = QueryDir.getQuery(Contract.Producto.BULK_INSERT);
            sqlCodigos = QueryDir.getQuery(Contract.ProductoCodigo.BULK_INSERT);
            sqlProdBusq = QueryDir.getQuery(Contract.ProductoExt.BULK_INSERT);
            db = getDataBase();
            db.beginTransactionNonExclusive();
            stmt = db.compileStatement(sql);
            stmtCod = db.compileStatement(sqlCodigos);
            stmtSearch = db.compileStatement(sqlProdBusq);
            while (total != 0 && total > descargados) {
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

                                stmt.bindLong(1, descargados - 1);
                                stmt.bindLong(2, type.getInt("Id"));
                                stmt.bindDouble(3, type.getDouble("P"));
                                stmt.bindString(4, type.getString("U"));
                                if (type.isNull("IdS"))
                                    stmt.bindLong(5, 0);
                                else
                                    stmt.bindLong(5, type.getInt("IdS"));

                                stmt.bindString(6, type.getString("Ex"));
                                if (!type.isNull("PDO"))
                                    stmt.bindDouble(7, Float.parseFloat(type.getString("PDO")));
                                else
                                    stmt.bindDouble(7, 0);

                                stmt.bindDouble(8, Float.parseFloat(type.getString("PD")));
                                stmt.bindDouble(9, Float.parseFloat(type.getString("PCD")));
                                stmt.bindDouble(10, Float.parseFloat(type.getString("PI")));
                                stmt.bindDouble(11, Float.parseFloat(type.getString("PCI")));
                                stmt.bindLong(12, type.getInt("B"));
                                stmt.bindString(13, type.getString("GC"));
                                stmt.bindString(14, type.getString("F"));
                                stmt.bindString(15, type.getString("L"));
                                if (!type.isNull("A"))
                                    stmt.bindString(16, type.getString("A"));
                                else
                                    stmt.bindString(16, "(Sin Descripción)");
                                if (!type.isNull("AV"))
                                    stmt.bindString(17, type.getString("AV"));
                                else
                                    stmt.bindString(17, "");

                                stmtSearch.bindLong(1, descargados - 1);
                                stmtSearch.bindString(2, type.getString("D"));
                                stmtSearch.bindString(3, type.getString("Ex").trim().replace(" ",""));
                                stmtSearch.bindString(4, type.getString("Ex").trim().replace(" ",""));
                                stmtSearch.bindString(5, type.getString("GE").trim());

                                stmtSearch.execute();
                                stmtSearch.clearBindings();

                                stmt.execute();
                                stmt.clearBindings();

                            } else {
                                Producto.deleteProducto(getDataBase(), type.getInt("Id"));
                            }


                        }
                    } catch (JSONException e) {
                        try {
                            pagina--;
                            //db.setTransactionSuccessful();
                            //db.endTransaction();
                            Log.e("Entro", "Error: " + e.toString());
                            Log.e("Entro", "JSON ERROR. Se quedo en pagina " + pagina);
                            //return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                        }
                        catch (Exception ex)
                        {
                            return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                        }

                    }
                } else {
                    try {
                        pagina--;
                        //db.setTransactionSuccessful();
                        //db.endTransaction();
                        Log.e("Entro", "Se quedo en pagina " + pagina);
                        //return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                    }
                    catch (Exception ex)
                    {
                        return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                    }

                }
            }
            try {
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            catch (Exception ex)
            {}
            db.endTransaction();
            //Alternos
            publishProgress(new Integer[]{1, 0, R.string.message_descarga_sustitutos});
            Bundle bundle = Productos.executeSyncSustitutos(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("Sustitutos"));
                    int length = types.length();
                    Alternativo.deleteAll(getDataBase(), Contract.Alternativo.TABLE_NAME);
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[]{length, i, R.string.message_descarga_sustitutos});

                        JSONObject type = types.getJSONObject(i);

                        Alternativo alternativo = new Alternativo();

                        alternativo.setItem(type.getString("I"));
                        alternativo.setAlterno(type.getString("A"));
                        alternativo.setFechaIngreso(Convert.getDateFromDotNetTicks(type.getLong("FIT")));
                        alternativo.setFechaVencimiento(Convert.getDateFromDotNetTicks(type.getLong("FVT")));
                        Alternativo.insert(getDataBase(), alternativo);
                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            } else {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            //Series
            publishProgress(new Integer[]{1, 0, R.string.message_descarga_series});
            bundle = Productos.executeSyncSeries(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("Series"));
                    int length = types.length();
                    Serie.deleteAll(getDataBase(), Contract.Serie.TABLE_NAME);
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[]{length, i, R.string.message_descarga_series});

                        JSONObject type = types.getJSONObject(i);

                        Serie serie = new Serie();

                        serie.setSerie(type.getString("IdSerie"));
                        serie.setGrupoEstadistico(type.getString("IdGrupoEstadistico").trim());
                        Serie.insert(getDataBase(), serie);
                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            } else {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }

            /*publishProgress(new Integer[]{1, 0, R.string.message_descarga_promociones});
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
                        if (!type.isNull("FormaPagoAplica") && type.getString("FormaPagoAplica").trim().length() > 0)
                            productoPromocion.setFormaPagoAplica(type.getString("FormaPagoAplica"));
                        ProductoPromocion.insert(getDataBase(), productoPromocion);
                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            } else {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }*/
            //Secuencias
            publishProgress(new Integer[]{1, 0, R.string.message_descarga_secuencias});
            bundle = Productos.executeSyncSecuencia(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("Secuencias"));
                    int length = types.length();
                    SecuenciaMatriz.deleteAll(getDataBase(), Contract.SecuenciaMatriz.TABLE_NAME);
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[]{length, i, R.string.message_descarga_secuencias});

                        JSONObject type = types.getJSONObject(i);

                        SecuenciaMatriz secuenciaMatriz = new SecuenciaMatriz();

                        secuenciaMatriz.setIdJerarquia(type.getInt("IdJerarquia"));
                        secuenciaMatriz.setMatriz(type.getString("Matriz"));
                        secuenciaMatriz.setFechaEfectiva(Convert.getDateFromDotNetTicks(type.getLong("FechaEfectivaTicks")));
                        secuenciaMatriz.setFechaVencimiento(Convert.getDateFromDotNetTicks(type.getLong("FechaVencimientoTicks")));
                        SecuenciaMatriz.insert(getDataBase(), secuenciaMatriz);
                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            } else {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            //Matrices
            publishProgress(new Integer[]{1, 0, R.string.message_descarga_matrices});
            bundle = Productos.executeSyncMatrices(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("Matrices"));
                    int length = types.length();
                    MatrizPrecio.deleteAll(getDataBase(), Contract.MatrizPrecio.TABLE_NAME);
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[]{length, i, R.string.message_descarga_matrices});

                        JSONObject type = types.getJSONObject(i);

                        MatrizPrecio matrizPrecio = new MatrizPrecio();

                        matrizPrecio.setIdCliente(type.getString("IC").trim());
                        matrizPrecio.setIdLibro(type.getString("IL"));
                        matrizPrecio.setIdListaPrecio(type.getString("ILP").trim());
                        matrizPrecio.setIdMatriz(type.getString("IM"));
                        matrizPrecio.setParametroDesc(type.getInt("P"));
                        matrizPrecio.setSecuencia(type.getInt("S"));
                        matrizPrecio.setFechaEfectiva(Convert.getDateFromDotNetTicks(type.getLong("ET")));
                        matrizPrecio.setFechaVencimiento(Convert.getDateFromDotNetTicks(type.getLong("VT")));
                        MatrizPrecio.insert(getDataBase(), matrizPrecio);
                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            } else {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }

            //Se llama a conteo de precios
            publishProgress(new Integer[]{1, 0, R.string.message_descarga_precios});
            conteo = Productos.executeSyncConteoPrecios(getDataBase());
            total = 0; descargados = 0; pagina = 0;
            if (conteo != null && conteo.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS)
                total = conteo.getInt("ConteoPrecios", 0);
            else {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            publishProgress(new Integer[]{total, 0, R.string.message_descarga_precios});

            //Elimino Precios
            LibroPrecio.deleteAll(getDataBase(), Contract.LibroPrecio.TABLE_NAME);
            sql = QueryDir.getQuery(Contract.LibroPrecio.BULK_INSERT);
            db = getDataBase();
            db.beginTransactionNonExclusive();
            stmt = db.compileStatement(sql);
            while (total != 0 && total > descargados) {
                bundle = Productos.executeSyncPrecios(getDataBase(), pagina, 3000);
                pagina++;
                if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                    try {
                        JSONArray types = new JSONArray(bundle.getString("Precios"));
                        int length = types.length();
                        for (int i = 0; i < length; i++) {
                            descargados++;
                            publishProgress(new Integer[]{total, descargados, R.string.message_descarga_precios});

                            JSONObject type = types.getJSONObject(i);


                            stmt.bindLong(1, descargados - 1);
                            stmt.bindString(2, type.getString("IL"));
                            stmt.bindString(3, type.getString("I"));
                            stmt.bindString(4, type.getString("D"));
                            if (!type.isNull("P"))
                                stmt.bindDouble(5, type.getDouble("P"));
                            else
                                stmt.bindDouble(5, 0);

                            stmt.bindString(6, type.getString("M"));
                            stmt.bindLong(7, Convert.getDateFromDotNetTicks(type.getLong("ET")).getTime());
                            stmt.bindLong(8, Convert.getDateFromDotNetTicks(type.getLong("VT")).getTime());
                            stmt.bindDouble(9, Float.parseFloat(type.getString("VE")));
                            stmt.bindLong(10, type.getInt("T"));
                            stmt.bindString(11, type.getString("L"));

                            stmt.execute();
                            stmt.clearBindings();


                        }
                    } catch (JSONException e) {
                        try {
                            pagina--;
                            //db.setTransactionSuccessful();
                            //db.endTransaction();
                            Log.e("Entro", "Error: " + e.toString());
                            Log.e("Entro", "JSON ERROR. Se quedo en pagina " + pagina);
                            //return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                        }
                        catch (Exception ex)
                        {
                            return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                        }

                    }
                } else {
                    try {
                        pagina--;
                        //db.setTransactionSuccessful();
                        //db.endTransaction();
                        Log.e("Entro", "Se quedo en pagina " + pagina);
                        //return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                    }
                    catch (Exception ex)
                    {
                        return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                    }

                }
            }
            try {
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            catch (Exception ex)
            {}
            db.endTransaction();

            publishProgress(new Integer[]{1, 0, R.string.message_descarga_categorias});
            bundle = Productos.executeSyncCategorias(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("Categorias"));
                    int length = types.length();
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[]{length, i, R.string.message_actualizar_categorias});

                        JSONObject type = types.getJSONObject(i);

                        if (type.getString("Estado").equalsIgnoreCase("A")) {

                            rp3.berlin.models.pedido.Categoria categoria = rp3.berlin.models.pedido.Categoria.getCategoria(getDataBase(), type.getInt("IdCategoria"));

                            categoria.setIdCategoria(type.getInt("IdCategoria"));
                            categoria.setDescripcion(type.getString("Descripcion"));

                            if (categoria.getID() == 0)
                                rp3.berlin.models.pedido.Categoria.insert(getDataBase(), categoria);
                            else
                                rp3.berlin.models.pedido.Categoria.update(getDataBase(), categoria);
                        }
                        else
                        {
                            Categoria.deleteCategoria(db, type.getInt("IdCategoria"));
                        }


                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                }
            } else {
                return getString(rp3.core.R.string.message_error_sync_connection_http_error);
            }
            publishProgress(new Integer[]{1, 0, R.string.message_descarga_subcategorias});
            bundle = Productos.executeSyncSubCategorias(getDataBase());
            if (bundle != null && bundle.getInt(SyncAdapter.ARG_SYNC_TYPE) == rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS) {
                try {
                    JSONArray types = new JSONArray(bundle.getString("SubCategorias"));
                    int length = types.length();
                    for (int i = 0; i < length; i++) {
                        publishProgress(new Integer[]{length, i, R.string.message_actualizar_subcategorias});
                        JSONObject type = types.getJSONObject(i);

                        if (type.getString("Estado").equalsIgnoreCase("A")) {
                            rp3.berlin.models.pedido.SubCategoria categoria = rp3.berlin.models.pedido.SubCategoria.getSubCategoria(getDataBase(), type.getInt("IdSubCategoria"));

                            categoria.setIdSubCategoria(type.getInt("IdSubCategoria"));
                            categoria.setIdCategoria(type.getInt("IdCategoria"));
                            categoria.setDescripcion(type.getString("Descripcion"));

                            if (categoria.getID() == 0)
                                rp3.berlin.models.pedido.SubCategoria.insert(getDataBase(), categoria);
                            else
                                rp3.berlin.models.pedido.SubCategoria.update(getDataBase(), categoria);
                        }
                        else
                        {
                            SubCategoria.deleteSubCategoria(db, type.getInt("IdCategoria"));
                        }
                    }
                } catch (JSONException e) {
                    return getString(rp3.core.R.string.message_error_sync_connection_http_error);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
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
