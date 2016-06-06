package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONObject;

import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.loader.ProductoLoader;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.models.pedido.ProductoPromocion;

/**
 * Created by magno_000 on 20/10/2015.
 */
public class ProductoListFragment extends BaseFragment implements ProductFragment.ProductAcceptListener {

    public static final String ARG_PRODUCTO = "Producto";
    public static final String ARG_BUSQUEDA = "busqueda";

    private JSONObject jsonObject;
    private LoaderProductos loaderProductos;
    private ProductoAdapter adapter;
    private boolean currentTransactionBoolean;
    private ListView headerList;
    private String currentTransactionSearch;
    private ProductFragment productFragment;
    private int idSubCategoria = -1;
    private String tipoBusqueda = "default";

    public static ProductoListFragment newInstance(int idCategoria)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(CategoriaFragment.ARG_IDCATEGORIA, idCategoria);
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

            loaderProductos = new LoaderProductos();

        }
    }

    public void ejecutarConsulta(){
        Bundle args = new Bundle();
        args.putInt(LoaderProductos.INT_CATEGORIA, idSubCategoria);
        args.putString(LoaderProductos.STRING_BUSQUEDA, tipoBusqueda);
        executeLoader(0, args, loaderProductos);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
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
                    executeLoader(0, args, loaderProductos);
                    return true;
                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText)) {
                        try {
                            Bundle args = new Bundle();
                            args.putString(LoaderProductos.STRING_SEARCH, "");
                            args.putInt(LoaderProductos.INT_CATEGORIA, idSubCategoria);
                            args.putString(LoaderProductos.STRING_BUSQUEDA, tipoBusqueda);
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
        public static final String INT_CATEGORIA = "int_categoria";
        private String Search, tipo;
        private int idSubCategoria;

        @Override
        public Loader<List<Producto>> onCreateLoader(int arg0,
                                                   Bundle bundle) {

            Search = bundle.getString(STRING_SEARCH);
            idSubCategoria = bundle.getInt(INT_CATEGORIA);
            tipo = bundle.getString(STRING_BUSQUEDA);
            return new ProductoLoader(getActivity(), getDataBase(), Search, idSubCategoria, tipo);

        }

        @Override
        public void onLoadFinished(Loader<List<Producto>> arg0,
                                   List<Producto> data) {

            headerList.setSelector(getActivity().getResources().getDrawable(R.drawable.bkg));


            adapter = new ProductoAdapter(getContext(), data);
            headerList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            headerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @SuppressLint("ResourceAsColor")
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                    Producto prod = adapter.getItem(position);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("d", prod.getDescripcion());
                        jsonObject.put("p", prod.getValorUnitario());
                        jsonObject.put("id", prod.getIdProducto());
                        jsonObject.put("f", prod.getUrlFoto());
                        jsonObject.put("ce", prod.getCodigoExterno());
                        jsonObject.put("b", prod.getIdBeneficio());
                        jsonObject.put("pdo", prod.getPorcentajeDescuentoOro() + "");
                        ProductoPromocion productoPromocion = ProductoPromocion.getProductoPromocion(getDataBase(), prod.getIdProducto());
                        if(productoPromocion == null) {
                            jsonObject.put("pd", 0 + "");
                            jsonObject.put("ib", 0 + "");
                        }
                        else {
                            jsonObject.put("pd", productoPromocion.getPorcentajeDescuento() + "");
                            jsonObject.put("ib", productoPromocion.getIdBeneficio() + "");
                        }
                        jsonObject.put("pi", prod.getPorcentajeImpuesto());
                        jsonObject.put("vd", prod.getPrecioDescuento());
                        jsonObject.put("vi", prod.getPrecioImpuesto());

                        productFragment = ProductFragment.newInstance(jsonObject.toString());
                        productFragment.setCancelable(false);
                        showDialogFragment(productFragment, "Producto", "Editar Producto");
                    }
                    catch (Exception ex)
                    {

                    }

                }
            });
            if(data.size() == 0)
                getRootView().findViewById(R.id.list_productos_ninguno).setVisibility(View.VISIBLE);
            else
                getRootView().findViewById(R.id.list_productos_ninguno).setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<List<Producto>> arg0) {

        }
    }
}