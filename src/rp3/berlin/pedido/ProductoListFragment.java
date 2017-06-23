package rp3.berlin.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.loader.LibroPrecioLoader;
import rp3.berlin.loader.ProductoLoader;
import rp3.berlin.models.Cliente;
import rp3.berlin.models.pedido.AgenteDescuento;
import rp3.berlin.models.pedido.Alternativo;
import rp3.berlin.models.pedido.LibroPrecio;
import rp3.berlin.models.pedido.PedidoDetalle;
import rp3.berlin.models.pedido.Producto;
import rp3.berlin.sync.Agente;
import rp3.berlin.sync.SyncAdapter;

/**
 * Created by magno_000 on 20/10/2015.
 */
public class ProductoListFragment extends BaseFragment implements ProductFragment.ProductAcceptListener {

    public static final String ARG_PRODUCTO = "Producto";
    public static final String ARG_BUSQUEDA = "busqueda";
    public static final String ARG_SERIE = "serie";
    public static final String ARG_CLIENTE = "cliente";
    public static final String ARG_TIPO_ORDEN = "tipo_orden";
    public static final String ARG_LINEA = "linea";
    public static final String ARG_FAMILIA = "familia";
    public static final String ARG_LISTA_PRECIO = "lista_precio";
    public static final String ARG_ITEM= "item";

    public static final int DIALOG_STOCK = 1;

    private JSONObject jsonObject;
    private LoaderProductos loaderProductos;
    private ProductoAdapter adapter;
    private boolean currentTransactionBoolean;
    private ListView headerList;
    private String currentTransactionSearch, serie;
    private ProductFragment productFragment;
    private int idSubCategoria = -1;
    private long idCliente;
    private String tipoBusqueda = "default", mensajeSust = "";
    private Producto seleccionado;
    private Cliente cliente;
    private List<Producto> list_alternativo;
    private boolean fueraProduccion = false;
    private String tipoOrden;
    private List<LibroPrecio> precio;
    private double descuento = 0;
    private LoaderPrecio loaderPrecios;

    public static ProductoListFragment newInstance(int idCategoria, String serie, long idCliente, String tipoOrden)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(CategoriaFragment.ARG_IDCATEGORIA, idCategoria);
        bundle.putString(ARG_SERIE, serie);
        bundle.putLong(ARG_CLIENTE, idCliente);
        bundle.putString(ARG_TIPO_ORDEN, tipoOrden);
        ProductoListFragment fragment = new ProductoListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ProductoListFragment newInstance(String busqueda)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_BUSQUEDA, busqueda);
        ProductoListFragment fragment = new ProductoListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_product_list, R.menu.fragment_producto_menu);

    }

    @Override
    public void onResume() {
        super.onResume();
        ejecutarConsulta();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null)
        {
            Bundle args = new Bundle();
            args.putString(LoaderProductos.STRING_SEARCH, currentTransactionSearch);
            args.putString(LoaderProductos.STRING_SERIE, serie);

            loaderProductos = new LoaderProductos();

        }
    }

    public void ejecutarConsulta(){
        Bundle args = new Bundle();
        args.putInt(LoaderProductos.INT_CATEGORIA, idSubCategoria);
        args.putString(LoaderProductos.STRING_BUSQUEDA, tipoBusqueda);
        args.putString(LoaderProductos.STRING_SERIE, serie);
        if(loaderProductos == null)
            loaderProductos = new LoaderProductos();
        executeLoader(0, args, loaderProductos);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        serie = getArguments().getString(ARG_SERIE, "");
        tipoOrden = getArguments().getString(ARG_TIPO_ORDEN, "");
        idCliente = getArguments().getLong(ARG_CLIENTE, 0);
        cliente = Cliente.getClienteID(getDataBase(), idCliente, false);
        headerList = (ListView) rootView.findViewById(R.id.list_productos);
        headerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        idSubCategoria = getArguments().getInt(CategoriaFragment.ARG_IDCATEGORIA, -1);
        tipoBusqueda = getArguments().getString(ARG_BUSQUEDA, "default");

        if(adapter != null)
        {
            headerList.setAdapter(adapter);
            setListeners();
        }

    }

    public void setListeners()
    {
        headerList.setAdapter(adapter);
        headerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {

                //mostrar detalle del producto

            }
        });
    }


    @Override
    public void onAfterCreateOptionsMenu(Menu menu) {
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
                    Bundle args = new Bundle();
                    args.putString(LoaderProductos.STRING_SEARCH, query);
                    args.putInt(LoaderProductos.INT_CATEGORIA, idSubCategoria);
                    args.putString(LoaderProductos.STRING_BUSQUEDA, tipoBusqueda);
                    args.putString(LoaderProductos.STRING_SERIE, serie);
                    executeLoader(0, args, loaderProductos);
                    return true;
                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText != null && newText.length() > 2) {
                        try {
                            Bundle args = new Bundle();
                            args.putString(LoaderProductos.STRING_SEARCH, newText);
                            args.putInt(LoaderProductos.INT_CATEGORIA, idSubCategoria);
                            args.putString(LoaderProductos.STRING_BUSQUEDA, tipoBusqueda);
                            args.putString(LoaderProductos.STRING_SERIE, serie);
                            executeLoader(0, args, loaderProductos);
                        } catch (Exception ex) {

                        }
                    }
                    return true;

                }
            });
        }

    }

    @Override
    public void onDeleteSuccess(PedidoDetalle transaction) {

    }

    @Override
    public void onAcceptSuccess(PedidoDetalle transaction) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("d", transaction.getDescripcion());
            jsonObject.put("p", transaction.getValorUnitario());
            jsonObject.put("id", transaction.getIdProducto());
            jsonObject.put("f", transaction.getUrlFoto());
            jsonObject.put("c", transaction.getCantidad());
            jsonObject.put("v", transaction.getValorTotal());
            jsonObject.put("pdo", transaction.getPorcentajeDescuentoOro());
            jsonObject.put("pdm", transaction.getPorcentajeDescuentoManual());
            jsonObject.put("vdm", transaction.getValorDescuentoManual());
            jsonObject.put("vdmt", transaction.getValorDescuentoManualTotal());
            jsonObject.put("pda", transaction.getPorcentajeDescuentoAutomatico());
            jsonObject.put("vda", transaction.getValorDescuentoAutomatico());
            jsonObject.put("vdat", transaction.getValorDescuentoAutomaticoTotal());
            jsonObject.put("pi", transaction.getPorcentajeImpuesto());
            jsonObject.put("vi", transaction.getValorImpuesto());
            jsonObject.put("vit", transaction.getValorImpuestoTotal());
            jsonObject.put("bi", transaction.getBaseImponible());
            jsonObject.put("bic", transaction.getBaseImponibleCero());
            jsonObject.put("s", transaction.getSubtotal());
            jsonObject.put("ssd", transaction.getSubtotalSinDescuento());
            jsonObject.put("ssi", transaction.getSubtotalSinImpuesto());
            jsonObject.put("cod", transaction.getProducto().getCodigoExterno());
            jsonObject.put("ib", transaction.getIdBeneficio());
            jsonObject.put("ven", transaction.getIdVendedor());
            if(transaction.getUsrDescManual() != null && !transaction.getUsrDescManual().equalsIgnoreCase(""))
                jsonObject.put("udm", transaction.getUsrDescManual());

        }
        catch (Exception ex) {
        }
        ((ProductoListActivity)getActivity()).finishOnResult(jsonObject.toString());
    }

    public class LoaderProductos implements LoaderManager.LoaderCallbacks<List<Producto>> {

        public static final String STRING_SEARCH = "string_search";
        public static final String STRING_BUSQUEDA = "tipo_busqueda";
        public static final String STRING_SERIE = "serie";
        public static final String INT_CATEGORIA = "int_categoria";
        private String Search, tipo, serie;
        private int idSubCategoria;

        @Override
        public Loader<List<Producto>> onCreateLoader(int arg0,
                                                   Bundle bundle) {

            Search = bundle.getString(STRING_SEARCH);
            idSubCategoria = bundle.getInt(INT_CATEGORIA);
            tipo = bundle.getString(STRING_BUSQUEDA);
            serie = bundle.getString(STRING_SERIE);
            return new ProductoLoader(getActivity(), getDataBase(), Search, idSubCategoria, tipo, serie);

        }

        @Override
        public void onLoadFinished(Loader<List<Producto>> arg0,
                                   List<Producto> data) {

            headerList.setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));

            if(data.size() == 1 && data.get(0).getID() == 0)
            {
                ((TextView)getRootView().findViewById(R.id.list_productos_ninguno)).setText(data.get(0).getDescripcion());
                getRootView().findViewById(R.id.list_productos_ninguno).setVisibility(View.VISIBLE);
            }
            else {
                adapter = new ProductoAdapter(getContext(), data);
                headerList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                headerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        list_alternativo = new ArrayList<Producto>();
                        fueraProduccion = false;
                        Producto prod = adapter.getItem(position);
                        prod = Producto.getProductoID(getDataBase(), prod.getID());
                        seleccionado = prod;
                        precio = null;
                        if (prod.getAviso().trim().length() == 0)
                            ValidarSustituto(prod);
                        else {
                            GeneralValue generalValue = GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_AVISO_ITEM_BERLIN, prod.getAviso());
                            if (generalValue == null || generalValue.getReference1().equalsIgnoreCase("1"))
                                ValidarSustituto(prod);
                            else {
                                fueraProduccion = true;
                                GetStock(prod);
                            }
                        }
                    }
                });
                if (data.size() == 0)
                    getRootView().findViewById(R.id.list_productos_ninguno).setVisibility(View.VISIBLE);
                else
                    getRootView().findViewById(R.id.list_productos_ninguno).setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Producto>> arg0) {

        }
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GET_DESCUENTO)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                double descuento = 0;
                String desc = data.getString(Agente.KEY_DESCUENTO);
                try {
                    JSONArray jsonArray = new JSONArray(desc);
                    for(int i = 0; i < jsonArray.length(); i ++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        descuento = Double.parseDouble(jsonObject.getString("PorcentajeDescuento"));
                        descuento = descuento / 100;
                    }
                }
                catch (Exception ex)
                {

                }
                ShowProducto(descuento);
            }
        }
        else if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GET_STOCK)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                boolean existeStock = false;
                String desc = data.getString(ARG_ITEM);
                try {
                    JSONArray jsonArray = new JSONArray(desc);
                    for(int i = 0; i < jsonArray.length(); i ++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(Integer.parseInt(jsonObject.getString("StockDisponible"))> 0)
                        {
                            existeStock = true;
                        }
                    }
                }
                catch (Exception ex)
                {

                }
                if(!fueraProduccion) {
                    if (existeStock) {
                        if(evaluatePrecio().getParametroDesc() == 2)
                            GetDescuento(seleccionado);
                        else
                            ShowProducto(0);
                    } else {
                        showDialogConfirmation(DIALOG_STOCK, R.string.message_stock_faltante, R.string.title_stock_faltante);
                    }
                }
                else
                {
                    fueraProduccion = false;
                    if(existeStock)
                        ValidarSustituto(seleccionado);
                    else
                        showDialogMessage("Aviso de Producto","Artículo fuera de producción y sin stock en bodega");
                }
            }
        }
    }

    @Override
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        switch (id)
        {
            case DIALOG_STOCK:
                if(evaluatePrecio().getParametroDesc() == 2)
                    GetDescuento(seleccionado);
                else
                    ShowProducto(0);
        }
    }

    public void GetStock(Producto prod)
    {
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GET_STOCK);
        bundle.putString(ARG_ITEM, prod.getCodigoExterno());
        requestSync(bundle);

        showDialogProgress("Cargando", "Consultando Stock");
    }

    public void GetSustitutos(Producto prod)
    {
        List<Alternativo> lista_alternativos = Alternativo.getAlterno(getDataBase(), prod.getCodigoExterno());
        if(lista_alternativos.size() == 0)
        {
            if(prod.getCodigoExterno() != seleccionado.getCodigoExterno()) {
                boolean existe = false;
                for(Producto producto: list_alternativo)
                {
                    if(producto.getCodigoExterno().equalsIgnoreCase(prod.getCodigoExterno()))
                        existe = true;
                }
                if(!existe)
                    list_alternativo.add(prod);
            }
        }
        else
        {
            for (Alternativo alt: lista_alternativos) {
                Producto producto = Producto.getProductoSingleByCodigoExterno(getDataBase(), alt.getAlterno());
                GetSustitutos(producto);
            }
        }
    }

    public void ValidarSustituto(Producto prod)
    {
        GetSustitutos(prod);
        if(list_alternativo.size() == 0)
        {
            showDialogProgress("Cargando", "Consultando Precio");
            Bundle args = new Bundle();
            args.putString(LoaderPrecio.STRING_CLIENTE, cliente.getIdExterno());
            args.putString(LoaderPrecio.STRING_ITEM, seleccionado.getCodigoExterno());
            args.putString(LoaderPrecio.STRING_LISTA_PRECIO, cliente.getListPrecio());
            args.putString(LoaderPrecio.STRING_WHERE, "ValidarSubstituto");
            if(loaderPrecios == null)
                loaderPrecios = new LoaderPrecio();
            executeLoader(0, args, loaderPrecios);
        }
        if(list_alternativo.size() == 1)
        {
            Toast.makeText(getContext(), "Item " + prod.getCodigoExterno() + " ha sido sustituido por el item " + list_alternativo.get(0).getCodigoExterno(), Toast.LENGTH_LONG).show();
            prod = Producto.getProductoSingleByCodigoExterno(getDataBase(), list_alternativo.get(0).getCodigoExterno());
            seleccionado = prod;
            showDialogProgress("Cargando", "Consultando Precio");
            Bundle args = new Bundle();
            args.putString(LoaderPrecio.STRING_CLIENTE, cliente.getIdExterno());
            args.putString(LoaderPrecio.STRING_ITEM, seleccionado.getCodigoExterno());
            args.putString(LoaderPrecio.STRING_LISTA_PRECIO, cliente.getListPrecio());
            args.putString(LoaderPrecio.STRING_WHERE, "ValidarSubstituto");
            if(loaderPrecios == null)
                loaderPrecios = new LoaderPrecio();
            executeLoader(0, args, loaderPrecios);
        }
        if(list_alternativo.size() > 1)
        {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    getContext(),
                    android.R.layout.select_dialog_item);
            for(Producto alt : list_alternativo)
            {
                arrayAdapter.add(alt.getCodigoExterno());
            }

            builderSingle.setAdapter(
                    arrayAdapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            seleccionado = Producto.getProductoSingleByCodigoExterno(getDataBase(), list_alternativo.get(which).getCodigoExterno());
                            showDialogProgress("Cargando", "Consultando Precio");
                            precio = LibroPrecio.getPrecio(getDataBase(), seleccionado.getCodigoExterno(), cliente.getIdExterno(), cliente.getListPrecio());
                            closeDialogProgress();
                            if (evaluatePrecio().getPrecio() > 0) {
                                GetStock(seleccionado);
                            } else {
                                showDialogMessage("El producto debe tener precio para poder ingresar el registro");
                            }
                        }
                    });
            builderSingle.setTitle("Productos Sustitutos de " + prod.getCodigoExterno());
            builderSingle.show();
        }
    }


    public void GetDescuento(Producto prod)
    {

            //Envio y reviso si existe descuento
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GET_DESCUENTO);
            bundle.putString(ARG_CLIENTE, cliente.getIdExterno());
            bundle.putString(ARG_LINEA, prod.getLinea());
            bundle.putString(ARG_LISTA_PRECIO, prod.getCodigoExterno());
            bundle.putString(ARG_TIPO_ORDEN, tipoOrden);
            bundle.putString(ARG_FAMILIA, prod.getFamilia());
            requestSync(bundle);

            showDialogProgress("Cargando", "Consultando Descuento");
    }

    public void ShowProducto(double descuento)
    {
        this.descuento = descuento;
        Cliente cliente = Cliente.getClienteID(getDataBase(), idCliente, false);
        if(precio == null) {
            showDialogProgress("Cargando", "Consultando Precio");
            Bundle args = new Bundle();
            args.putString(LoaderPrecio.STRING_CLIENTE, cliente.getIdExterno());
            args.putString(LoaderPrecio.STRING_ITEM, seleccionado.getCodigoExterno());
            args.putString(LoaderPrecio.STRING_LISTA_PRECIO, cliente.getListPrecio());
            args.putString(LoaderPrecio.STRING_WHERE, "ShowProducto");
            if(loaderPrecios == null)
                loaderPrecios = new LoaderPrecio();
            executeLoader(0, args, loaderPrecios);
        }
        else
        {
            ShowFragmentProducto();
        }

    }

    private void ShowFragmentProducto()
    {
        if (evaluatePrecio().getPrecio() > 0) {
            try {
                double valor = evaluatePrecio().getPrecio();
                if (descuento != 0)
                    valor = (valor) - (valor * descuento);

                double valorImpuesto = valor;
                if (seleccionado.getPorcentajeImpuesto() > 0) {
                    valorImpuesto = (valor) + (valor * seleccionado.getPorcentajeImpuesto());
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("d", seleccionado.getDescripcion());
                jsonObject.put("p", evaluatePrecio().getPrecio());
                jsonObject.put("id", seleccionado.getIdProducto());
                jsonObject.put("f", seleccionado.getUrlFoto());
                jsonObject.put("ce", seleccionado.getCodigoExterno());
                jsonObject.put("b", seleccionado.getIdBeneficio());
                jsonObject.put("pdo", seleccionado.getPorcentajeDescuentoOro() + "");
                jsonObject.put("pd", descuento + "");
                jsonObject.put("ib", 0 + "");
                jsonObject.put("pi", seleccionado.getPorcentajeImpuesto());
                jsonObject.put("vd", valor);
                jsonObject.put("vi", valorImpuesto);
                jsonObject.put("a", seleccionado.getAplicacion());

                //Envío lista de precio para no cargarla de nuevo
                JSONArray jArrayLibro = new JSONArray();
                for (LibroPrecio libroPrecio : precio) {
                    JSONObject jsonLibro = new JSONObject();
                    jsonLibro.put("i", libroPrecio.getItem());
                    jsonLibro.put("p", libroPrecio.getPrecio());
                    jsonLibro.put("e", libroPrecio.getValorEscalado());
                    jsonLibro.put("f", libroPrecio.getFechaEfectiva().getTime());
                    jArrayLibro.put(jsonLibro);
                }
                jsonObject.put("lp", jArrayLibro);

                //Envío Tope de Descuento para no cargarlo de nuevo
                AgenteDescuento agente = AgenteDescuento.getTopeDescuento(getDataBase(), cliente.getCanalPartner(), seleccionado.getLinea());
                jsonObject.put("t", agente.getTope());


                productFragment = ProductFragment.newInstance(jsonObject.toString());
                productFragment.setCancelable(false);
                showDialogFragment(productFragment, "Producto", "Agregar Producto - " + seleccionado.getCodigoExterno());
            } catch (Exception ex) {

            }
        } else {
            showDialogMessage("El producto debe tener precio para poder ingresar el registro");
        }
    }

    public LibroPrecio evaluatePrecio()
    {
        LibroPrecio resp = new LibroPrecio();
        for(LibroPrecio libroPrecio : precio)
        {
            if(resp.getItem() == null && libroPrecio.getValorEscalado() == 0)
                resp = libroPrecio;
            else
            {
                if(libroPrecio.getValorEscalado() == 0 && libroPrecio.getFechaEfectiva().getTime() > resp.getFechaEfectiva().getTime())
                    resp = libroPrecio;
            }
        }
        return resp;
    }

    public class LoaderPrecio implements LoaderManager.LoaderCallbacks<List<LibroPrecio>> {

        public static final String STRING_CLIENTE = "cliente";
        public static final String STRING_ITEM = "item";
        public static final String STRING_LISTA_PRECIO = "lista_precio";
        public static final String STRING_WHERE = "posicion";
        private String item, cliente, lista_precio, where;

        @Override
        public Loader<List<LibroPrecio>> onCreateLoader(int arg0,
                                                        Bundle bundle) {

            item = bundle.getString(STRING_ITEM);
            cliente = bundle.getString(STRING_CLIENTE);
            lista_precio = bundle.getString(STRING_LISTA_PRECIO);
            where = bundle.getString(STRING_WHERE);
            return new LibroPrecioLoader(getActivity(), getDataBase(), item, cliente, lista_precio);

        }

        @Override
        public void onLoadFinished(Loader<List<LibroPrecio>> arg0,
                                   List<LibroPrecio> data) {

            try {
                precio = data;
                if (where.equalsIgnoreCase("ShowProducto")) {
                    ShowFragmentProducto();
                }
                else if(where.equalsIgnoreCase("ValidarSubstituto"))
                {
                    closeDialogProgress();
                    if (evaluatePrecio().getPrecio() > 0) {
                        GetStock(seleccionado);
                    } else {
                        showDialogMessage("El producto debe tener precio para poder ingresar el registro");
                    }
                }
                closeDialogProgress();
            } catch (Exception ex) {

            }
        }

        @Override
        public void onLoaderReset(Loader<List<LibroPrecio>> arg0) {

        }
    }
}