package rp3.marketforce.sync;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.Calendar;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.*;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.pedido.ProductoCodigo;
import rp3.sync.SyncAudit;

/**
 * Created by magno_000 on 19/10/2015.
 */
public class Productos {
    public static int executeSync(DataBase db){
        WebService webService = new WebService("MartketForce","GetProductos");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.marketforce.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            webService.addParameter("@ultimaFechaActualizacion", fecha);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONArray types = webService.getJSONArrayResponse();

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);

            for(int i=0; i < types.length(); i++){
                try {

                    JSONObject type = types.getJSONObject(i);

                    rp3.marketforce.models.pedido.Producto producto = rp3.marketforce.models.pedido.Producto.getProductoIdServer(db, type.getInt("IdProducto"));

                    producto.setIdProducto(type.getInt("IdProducto"));
                    producto.setDescripcion(type.getString("Descripcion"));
                    producto.setUrlFoto(type.getString("URLFoto"));
                    producto.setValorUnitario(type.getDouble("Precio"));
                    producto.setCodigoExterno(type.getString("IdExterno"));
                    producto.setPrecioDescuento(Float.parseFloat(type.getString("PrecioConDescuento")));
                    producto.setPrecioImpuesto(Float.parseFloat(type.getString("PrecioConImpuesto")));
                    producto.setPorcentajeDescuento(Float.parseFloat(type.getString("PorcentajeDescuento")));
                    producto.setPorcentajeImpuesto(Float.parseFloat(type.getString("PorcentajeImpuesto")));
                    producto.setIdBeneficio(type.getInt("IdBeneficio"));
                    if(type.isNull("IdSubCategoria"))
                        producto.setIdSubCategoria(0);
                    else
                        producto.setIdSubCategoria(type.getInt("IdSubCategoria"));

                    if(producto.getID() == 0)
                        rp3.marketforce.models.pedido.Producto.insert(db, producto);
                    else
                        rp3.marketforce.models.pedido.Producto.update(db, producto);

                    ProductoCodigo.deleteCodigos(db, producto.getCodigoExterno());
                    JSONArray strs = type.getJSONArray("ProductoCodigo");

                    for (int j = 0; j < strs.length(); j++) {
                        JSONObject str = strs.getJSONObject(j);

                        ProductoCodigo productoCodigo = new ProductoCodigo();
                        productoCodigo.setCodigoExterno(str.getString("IdExterno"));
                        productoCodigo.setCodigo(str.getString("Codigo"));
                        ProductoCodigo.insert(db, productoCodigo);
                    }


                } catch (JSONException e) {
                    Log.e("Entro", "Error: " + e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
            }
        }finally{
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncCategorias(DataBase db){
        WebService webService = new WebService("MartketForce","GetCategorias");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.marketforce.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            //webService.addParameter("@ultimaFechaActualizacion", fecha);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONArray types = webService.getJSONArrayResponse();

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);

            for(int i=0; i < types.length(); i++){
                try {

                    JSONObject type = types.getJSONObject(i);

                    rp3.marketforce.models.pedido.Categoria categoria = rp3.marketforce.models.pedido.Categoria.getCategoria(db, type.getInt("IdCategoria"));

                    categoria.setIdCategoria(type.getInt("IdCategoria"));
                    categoria.setDescripcion(type.getString("Descripcion"));

                    if(categoria.getID() == 0)
                        rp3.marketforce.models.pedido.Categoria.insert(db, categoria);
                    else
                        rp3.marketforce.models.pedido.Categoria.update(db, categoria);

                } catch (JSONException e) {
                    Log.e("Entro", "Error: " + e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                }
            }
        }finally{
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncSubCategorias(DataBase db){
        WebService webService = new WebService("MartketForce","GetSubcategorias");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.marketforce.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            //webService.addParameter("@ultimaFechaActualizacion", fecha);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONArray types = webService.getJSONArrayResponse();

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);

            for(int i=0; i < types.length(); i++){
                try {

                    JSONObject type = types.getJSONObject(i);

                    rp3.marketforce.models.pedido.SubCategoria categoria = rp3.marketforce.models.pedido.SubCategoria.getSubCategoria(db, type.getInt("IdSubCategoria"));

                    categoria.setIdSubCategoria(type.getInt("IdSubCategoria"));
                    categoria.setIdCategoria(type.getInt("IdCategoria"));
                    categoria.setDescripcion(type.getString("Descripcion"));

                    if(categoria.getID() == 0)
                        rp3.marketforce.models.pedido.SubCategoria.insert(db, categoria);
                    else
                        rp3.marketforce.models.pedido.SubCategoria.update(db, categoria);

                } catch (JSONException e) {
                    Log.e("Entro", "Error: " + e.toString());
                    return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }finally{
            webService.close();
        }
        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
