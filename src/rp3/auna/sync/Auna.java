package rp3.auna.sync;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.List;

import rp3.auna.actividades.ActualizacionFragment;
import rp3.auna.actividades.CotizacionActivity;
import rp3.auna.models.marcacion.Justificacion;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.*;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;

/**
 * Created by magno_000 on 30/01/2017.
 */
public class Auna {
    private static final String TAG = Auna.class.getSimpleName();
    public static Bundle executeCotizacion(String parametros) {
        Bundle bundle = new Bundle();
        WebService webService = new WebService("MartketForce", "GetCotizacion");
        webService.setTimeOut(20000);

        JSONArray jObject = null;
        try {
            jObject = new JSONArray(parametros);
        } catch (Exception ex) {

        }

        webService.addParameter("afiliados", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                bundle.putString(CotizacionActivity.ARG_RESPONSE, webService.getJSONArrayResponse().toString());
                bundle.putInt("Status", SyncAdapter.SYNC_EVENT_SUCCESS);

            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_HTTP_ERROR);;
            } catch (Exception e) {
                bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_ERROR);
            }

        } finally {
            webService.close();
        }


        return bundle;
    }

    public static Bundle executeSolicitud(String parametros) {
        Bundle bundle = new Bundle();
        WebService webService = new WebService("MartketForce", "GetSolicitud");
        webService.setTimeOut(20000);

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(parametros);
        } catch (Exception ex) {
            Log.d(TAG,"Exception:"+ex.getMessage());
        }

        webService.addParameter("model", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                bundle.putString(ActualizacionFragment.ARG_RESPONSE, webService.getJSONObjectResponse().toString());
                bundle.putInt("Status", SyncAdapter.SYNC_EVENT_SUCCESS);

            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_HTTP_ERROR);;
            } catch (Exception e) {
                bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_ERROR);
            }

        } finally {
            webService.close();
        }


        return bundle;
    }

    public static Bundle executePago(String parametros) {
        Bundle bundle = new Bundle();
        WebService webService = new WebService("MartketForce", "RegistrarPago");
        webService.setTimeOut(20000);

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(parametros);
        } catch (Exception ex) {

        }

        webService.addParameter("model", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                bundle.putString(ActualizacionFragment.ARG_RESPONSE, webService.getJSONObjectResponse().toString());
                bundle.putInt("Status", SyncAdapter.SYNC_EVENT_SUCCESS);

            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
                bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_HTTP_ERROR);;
            } catch (Exception e) {
                bundle.putInt("Status", rp3.auna.sync.SyncAdapter.SYNC_EVENT_ERROR);
            }

        } finally {
            webService.close();
        }


        return bundle;
    }


}
