package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;
import rp3.data.models.GeopoliticalStructure;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.db.Contract;
import rp3.marketforce.loader.LibroPrecioLoader;
import rp3.marketforce.loader.ProductoLoader;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.AgenteDescuento;
import rp3.marketforce.models.pedido.Alternativo;
import rp3.marketforce.models.pedido.Importacion;
import rp3.marketforce.models.pedido.LibroPrecio;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.models.pedido.Stock;
import rp3.marketforce.models.pedido.VentaPerdida;
import rp3.marketforce.sync.Agente;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.Convert;

/**
 * Created by magno_000 on 27/04/2017.
 */

public class BodegaFragment extends BaseFragment {
    public static final String ARG_ITEM= "item";

    private String item;
    private Producto prod;
    private SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private ProductoAutoCompleteAdapter adapter;

    public static BodegaFragment newInstance(String item)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ITEM, item);
        BodegaFragment fragment = new BodegaFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_bodega);

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
        item = getArguments().getString(ARG_ITEM, "");
        if(!item.trim().equalsIgnoreCase(""))
        {
            ((EditText) getRootView().findViewById(R.id.codigo_producto)).setText(item);
            prod = Producto.getProductoSingleByCodigoExterno(getDataBase(), item);
            ((TextView) getRootView().findViewById(R.id.consulta_producto)).setText(prod.getCodigoExterno() + " - " + prod.getDescripcion());
            ((TextView) getRootView().findViewById(R.id.consulta_producto)).setVisibility(View.VISIBLE);
            GetStock(item);
        }

        adapter = new ProductoAutoCompleteAdapter(this.getContext(), getDataBase());
        ((AutoCompleteTextView)getRootView().findViewById(R.id.codigo_producto)).setAdapter(adapter);
        ((AutoCompleteTextView)getRootView().findViewById(R.id.codigo_producto)).setThreshold(3);
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ((AutoCompleteTextView)getRootView().findViewById(R.id.codigo_producto)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    adapter.getSelected(((TextView) view).getText().toString());
                }
            });


        } else {
            ((AutoCompleteTextView)getRootView().findViewById(R.id.codigo_producto)).setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {

                @Override
                public void onDismiss() {
                    adapter.getSelected(((AutoCompleteTextView)getRootView().findViewById(R.id.codigo_producto)).getText().toString());
                }
            });
        }

        ((Button) getRootView().findViewById(R.id.consultar_stock)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((EditText) getRootView().findViewById(R.id.codigo_producto)).length() > 0)
                {
                    if(prod != null)
                    {
                        if(!prod.getCodigoExterno().equalsIgnoreCase(((EditText) getRootView().findViewById(R.id.codigo_producto)).getText().toString()))
                        {
                            getRootView().findViewById(R.id.importaciones_layout).setVisibility(View.GONE);
                        }
                    }
                    prod = Producto.getProductoSingleByCodigoExterno(getDataBase(), ((EditText) getRootView().findViewById(R.id.codigo_producto)).getText().toString());
                    if(prod != null)
                    {
                        ((TextView) getRootView().findViewById(R.id.consulta_producto)).setText(prod.getCodigoExterno() + " - " + prod.getDescripcion());
                        ((TextView) getRootView().findViewById(R.id.consulta_producto)).setVisibility(View.VISIBLE);
                        GetStock(prod.getCodigoExterno());
                    }
                    else
                    {
                        VentaPerdida ventaPerdida = new VentaPerdida();
                        ventaPerdida.setCodigoProducto(((EditText) getRootView().findViewById(R.id.codigo_producto)).getText().toString());
                        ventaPerdida.setFecha(Calendar.getInstance().getTime());
                        ventaPerdida.setPendiente(true);
                        VentaPerdida.insert(getDataBase(), ventaPerdida);
                        showDialogMessage("Código de producto ingresado no existe.");
                    }
                }
                else
                {
                    showDialogMessage("Debe ingresar el código del producto a consultar.");
                }
            }
        });

        ((Button) getRootView().findViewById(R.id.consultar_importaciones)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((EditText) getRootView().findViewById(R.id.codigo_producto)).length() > 0)
                {
                    if(prod != null)
                    {
                        if(!prod.getCodigoExterno().equalsIgnoreCase(((EditText) getRootView().findViewById(R.id.codigo_producto)).getText().toString()))
                        {
                            getRootView().findViewById(R.id.stock_layout).setVisibility(View.GONE);
                        }
                    }
                    prod = Producto.getProductoSingleByCodigoExterno(getDataBase(), ((EditText) getRootView().findViewById(R.id.codigo_producto)).getText().toString());
                    if(prod != null)
                    {
                        ((TextView) getRootView().findViewById(R.id.consulta_producto)).setText(prod.getCodigoExterno() + " - " + prod.getDescripcion());
                        ((TextView) getRootView().findViewById(R.id.consulta_producto)).setVisibility(View.VISIBLE);
                        GetImportaciones(prod.getCodigoExterno());
                    }
                    else
                    {
                        VentaPerdida ventaPerdida = new VentaPerdida();
                        ventaPerdida.setCodigoProducto(((EditText) getRootView().findViewById(R.id.codigo_producto)).getText().toString());
                        ventaPerdida.setFecha(Calendar.getInstance().getTime());
                        ventaPerdida.setPendiente(true);
                        VentaPerdida.insert(getDataBase(), ventaPerdida);
                        showDialogMessage("Código de producto ingresado no existe.");
                    }
                }
                else
                {
                    showDialogMessage("Debe ingresar el código del producto a consultar.");
                }
            }
        });

    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GET_STOCK_CONSULTA)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                String desc = data.getString(ARG_ITEM);
                ShowStock(desc);
            }
        }
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GET_IMPORTACIONES)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                String desc = data.getString(ARG_ITEM);
                ShowImportaciones(desc);
            }
        }
    }

    public void GetStock(String codigo)
    {
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GET_STOCK_CONSULTA);
        bundle.putString(ARG_ITEM, codigo);
        requestSync(bundle);

        showDialogProgress("Cargando", "Consultando Stock");
    }

    public void GetImportaciones(String codigo)
    {
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GET_IMPORTACIONES);
        bundle.putString(ARG_ITEM, codigo);
        requestSync(bundle);

        showDialogProgress("Cargando", "Consultando Importaciones");
    }

    public void ShowStock(String json)
    {
        try {
            JSONArray jsonArray = new JSONArray(json);
            Calendar cal = Calendar.getInstance();
            List<Stock> listStock = new ArrayList<>();
            ((TextView)getRootView().findViewById(R.id.consulta_fecha)).setText(format1.format(cal.getTime()));
            getRootView().findViewById(R.id.fecha_layout).setVisibility(View.VISIBLE);
            getRootView().findViewById(R.id.stock_layout).setVisibility(View.VISIBLE);
            if(jsonArray.length() > 0) {
                Stock.deleteStockItem(getDataBase(), prod.getCodigoExterno());
                getRootView().findViewById(R.id.stock_list).setVisibility(View.VISIBLE);
                getRootView().findViewById(R.id.stock_empty).setVisibility(View.GONE);
            }
            else
            {
                getRootView().findViewById(R.id.stock_list).setVisibility(View.GONE);
                getRootView().findViewById(R.id.stock_empty).setVisibility(View.VISIBLE);
            }
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Stock stock = new Stock();
                stock.setIdBodega(jsonObject.getString("IdBodega"));
                stock.setItem(jsonObject.getString("Item"));
                stock.setStockFisico(jsonObject.getDouble("StockFisico"));
                stock.setStockDisponible(jsonObject.getDouble("StockDisponible"));
                stock.setFecha(cal.getTime());
                stock.setBodegaDescripcion(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_BODEGAS_BERLIN, stock.getIdBodega()).getValue());
                Stock.insert(getDataBase(), stock);
                listStock.add(stock);
            }

            StockAdapter adapter = new StockAdapter(getContext(), listStock);
            ((ListView)getRootView().findViewById(R.id.stock_list)).setAdapter(adapter);
        }
        catch (Exception ex)
        {

        }
    }

    public void ShowImportaciones(String json)
    {
        try {
            JSONArray jsonArray = new JSONArray(json);
            Calendar cal = Calendar.getInstance();
            List<Importacion> listImportacion = new ArrayList<>();
            ((TextView)getRootView().findViewById(R.id.consulta_fecha)).setText(format1.format(cal.getTime()));
            getRootView().findViewById(R.id.fecha_layout).setVisibility(View.VISIBLE);
            getRootView().findViewById(R.id.importaciones_layout).setVisibility(View.VISIBLE);
            if(jsonArray.length() > 0) {
                getRootView().findViewById(R.id.importaciones_list).setVisibility(View.VISIBLE);
                getRootView().findViewById(R.id.importaciones_empty).setVisibility(View.GONE);
            }
            else
            {
                getRootView().findViewById(R.id.importaciones_list).setVisibility(View.GONE);
                getRootView().findViewById(R.id.importaciones_empty).setVisibility(View.VISIBLE);
            }
            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Importacion importacion = new Importacion();
                importacion.setOrden(jsonObject.getString("Orden"));
                importacion.setItem(jsonObject.getString("Item"));
                importacion.setIdPais(jsonObject.getString("IdPais"));
                importacion.setTipoOC(jsonObject.getString("TipoOC"));
                importacion.setProveedor(jsonObject.getString("Proveedor"));
                importacion.setFechaPedido(Convert.getDateFromDotNetTicks(jsonObject.getLong("FechaPedidoTicks")));
                importacion.setFechaPlanRecepcion(Convert.getDateFromDotNetTicks(jsonObject.getLong("FechaPlanRecepcionTicks")));
                importacion.setCantidad(jsonObject.getDouble("Cantidad"));
                listImportacion.add(importacion);
            }

            ImportacionAdapter adapter = new ImportacionAdapter(getContext(), listImportacion);
            ((ListView)getRootView().findViewById(R.id.importaciones_list)).setAdapter(adapter);
        }
        catch (Exception ex)
        {

        }
    }
}
