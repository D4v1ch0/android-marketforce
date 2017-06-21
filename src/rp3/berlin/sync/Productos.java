package rp3.berlin.sync;

import android.os.Bundle;

import org.json.JSONArray;
import org.ksoap2.transport.HttpResponseException;

import java.util.Calendar;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.berlin.Contants;
import rp3.berlin.pedido.ProductoListFragment;
import rp3.sync.SyncAudit;

/**
 * Created by magno_000 on 19/10/2015.
 */
public class Productos {
    public static Bundle executeSync(DataBase db, int pagina, int tamano){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetProductos");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.berlin.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            /*if(fechaUlt.getTimeInMillis() > 0)
                webService.addParameter("@ultimaFechaActualizacion", fecha);*/
            webService.addParameter("@pagina", pagina);
            webService.addParameter("@tamano", tamano);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("Productos", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncPromociones(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetProductosPromociones");
        try
        {
            webService.addParameter("@idPuntoOperacion", PreferenceManager.getInt(Contants.KEY_ID_PUNTO_OPERACION, 0));
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("Promociones", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncConteo(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetProductosCount");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.berlin.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            /*if(fechaUlt.getTimeInMillis() > 0)
                webService.addParameter("@ultimaFechaActualizacion", fecha);*/
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            resp.putInt("Conteo", webService.getIntegerResponse());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncCategorias(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetCategorias");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.berlin.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            //webService.addParameter("@ultimaFechaActualizacion", fecha);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("Categorias", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);


        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncSubCategorias(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetSubcategorias");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.berlin.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            //webService.addParameter("@ultimaFechaActualizacion", fecha);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED){
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("SubCategorias", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);


        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncSecuencia(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetSecuencia");
        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("Secuencias", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }
    public static Bundle executeSyncMatrices(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetMatriz");
        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("Matrices", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncSustitutos(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetAlternativos");
        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("Sustitutos", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncDescuento(String linea, String cliente, String tipoOrden, String familia, String item){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetMatrizDescuento");
        try
        {
            webService.addParameter("@linea", linea);
            webService.addParameter("@cliente", cliente);
            webService.addParameter("@tipoOrden", tipoOrden);
            webService.addParameter("@familia", familia);
            item = item.replace(" ", "%20");
            webService.addParameter("@item", item);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            if(types != null)
                resp.putString(Agente.KEY_DESCUENTO, types.toString());
            else
                resp.putString(Agente.KEY_DESCUENTO, "");
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncStock(String item){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetStock");
        try
        {
            item = item.replace(" ", "%20");
            webService.addParameter("@item", item);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            if(types != null)
                resp.putString(ProductoListFragment.ARG_ITEM, types.toString());
            else
                resp.putString(ProductoListFragment.ARG_ITEM, "");
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncImportacion(String item){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetImportaciones");
        try
        {
            item = item.replace(" ", "%20");
            webService.addParameter("@item", item);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            if(types != null)
                resp.putString(ProductoListFragment.ARG_ITEM, types.toString());
            else
                resp.putString(ProductoListFragment.ARG_ITEM, "");
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncConteoPrecios(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetPreciosCount");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.berlin.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            /*if(fechaUlt.getTimeInMillis() > 0)
                webService.addParameter("@ultimaFechaActualizacion", fecha);*/
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            resp.putInt("ConteoPrecios", webService.getIntegerResponse());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncPrecios(DataBase db, int pagina, int tamano){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetLibroPrecios");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.berlin.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            /*if(fechaUlt.getTimeInMillis() > 0)
                webService.addParameter("@ultimaFechaActualizacion", fecha);*/
            webService.addParameter("@pagina", pagina);
            webService.addParameter("@tamano", tamano);
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("Precios", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);
        }finally{
            webService.close();
        }
        return resp;
    }

    public static Bundle executeSyncSeries(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetSeries");
        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
                    resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                }
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
                return resp;
            } catch (Exception e) {
                resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
                return resp;
            }

            JSONArray types = webService.getJSONArrayResponse();
            resp.putString("Series", types.toString());
            resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);

            //rp3.marketforce.models.pedido.Producto.deleteAll(db, Contract.Canal.TABLE_NAME);
        }finally{
            webService.close();
        }
        return resp;
    }

}