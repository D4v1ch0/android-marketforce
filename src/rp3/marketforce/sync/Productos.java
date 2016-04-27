package rp3.marketforce.sync;

import android.os.Bundle;
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
    public static Bundle executeSync(DataBase db){
        Bundle resp = new Bundle();
        WebService webService = new WebService("MartketForce","GetProductos");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.marketforce.sync.SyncAdapter.SYNC_TYPE_PRODUCTOS, rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS));
        fechaUlt.add(Calendar.MINUTE, -30);
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
        try
        {
            if(fechaUlt.getTimeInMillis() > 0)
                webService.addParameter("@ultimaFechaActualizacion", fecha);
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

    public static Bundle executeSyncCategorias(DataBase db){
        Bundle resp = new Bundle();
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
}
