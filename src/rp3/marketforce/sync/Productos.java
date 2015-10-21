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

                    if(producto.getID() == 0)
                        rp3.marketforce.models.pedido.Producto.insert(db, producto);
                    else
                        rp3.marketforce.models.pedido.Producto.update(db, producto);

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
}
