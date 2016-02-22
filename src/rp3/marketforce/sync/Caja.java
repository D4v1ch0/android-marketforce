package rp3.marketforce.sync;

import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.runtime.Session;
import rp3.util.Convert;

/**
 * Created by magno_000 on 14/12/2015.
 */
public class Caja {
    public static int executeSync(DataBase db){
        WebService webService = new WebService("MartketForce","GetCaja");

        try
        {
            webService.addStringParameter("@androidID", PreferenceManager.getString(Contants.KEY_ANDROID_ID));
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONObject jObject = webService.getJSONObjectResponse();
                if(jObject != null && !jObject.isNull(Contants.KEY_SECUENCIA_FACTURA)) {
                    PreferenceManager.setValue(Contants.KEY_AUTORIZACION_SRI, jObject.getString(Contants.KEY_AUTORIZACION_SRI));
                    PreferenceManager.setValue(Contants.KEY_SECUENCIA_FACTURA, jObject.getInt(Contants.KEY_SECUENCIA_FACTURA));
                    PreferenceManager.setValue(Contants.KEY_SECUENCIA_NOTA_CREDITO, jObject.getInt(Contants.KEY_SECUENCIA_NOTA_CREDITO));
                    PreferenceManager.setValue(Contants.KEY_EMPRESA, jObject.getString(Contants.KEY_EMPRESA));
                    PreferenceManager.setValue(Contants.KEY_RUC, jObject.getString(Contants.KEY_RUC));
                    PreferenceManager.setValue(Contants.KEY_DIRECCION, jObject.getString(Contants.KEY_DIRECCION));
                    PreferenceManager.setValue(Contants.KEY_TELEFONO, jObject.getString(Contants.KEY_TELEFONO));
                    PreferenceManager.setValue(Contants.KEY_ESTABLECIMIENTO, jObject.getString(Contants.KEY_ESTABLECIMIENTO));
                    PreferenceManager.setValue(Contants.KEY_NOMBRE_PUNTO_OPERACION, jObject.getString(Contants.KEY_NOMBRE_PUNTO_OPERACION));
                    PreferenceManager.setValue(Contants.KEY_SERIE, jObject.getString(Contants.KEY_SERIE));
                    PreferenceManager.setValue(Contants.KEY_ID_ESTABLECIMIENTO, jObject.getInt(Contants.KEY_ID_ESTABLECIMIENTO));
                    PreferenceManager.setValue(Contants.KEY_ID_CAJA, jObject.getInt(Contants.KEY_ID_CAJA));
                    PreferenceManager.setValue(Contants.KEY_ID_PUNTO_OPERACION, jObject.getInt(Contants.KEY_ID_PUNTO_OPERACION));
                    PreferenceManager.setValue(Contants.KEY_ID_EMPRESA, jObject.getInt(Contants.KEY_ID_EMPRESA));
                    if(!jObject.isNull(Contants.KEY_DESCUENTO_MAXIMO))
                        PreferenceManager.setValue(Contants.KEY_DESCUENTO_MAXIMO, jObject.getInt(Contants.KEY_DESCUENTO_MAXIMO));

                }
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return SyncAdapter.SYNC_EVENT_ERROR;
            }

        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncMoneda(DataBase db){
        WebService webService = new WebService("MartketForce","GetMoneda");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONObject jObject = webService.getJSONObjectResponse();
                if(jObject != null) {
                    PreferenceManager.setValue(Contants.KEY_MONEDA_SIMBOLO, jObject.getString(Contants.KEY_MONEDA_SIMBOLO));
                    PreferenceManager.setValue(Contants.KEY_ID_MONEDA, jObject.getInt(Contants.KEY_ID_MONEDA));
                }
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return SyncAdapter.SYNC_EVENT_ERROR;
            }

        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncFormasPago(DataBase db){
        WebService webService = new WebService("MartketForce","GetFormasPago");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONArray jsonArray = webService.getJSONArrayResponse();
                if(jsonArray != null) {
                    FormaPago.deleteAll(db, Contract.FormaPago.TABLE_NAME);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FormaPago formaPago = new FormaPago();
                        formaPago.setIdFormaPago(jsonObject.getInt("IdFormaPago"));
                        formaPago.setDescripcion(jsonObject.getString("Descripcion"));
                        FormaPago.insert(db, formaPago);
                    }
                }
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return SyncAdapter.SYNC_EVENT_ERROR;
            }

        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncInsertControl(DataBase db, long idCaja){
        WebService webService = new WebService("MartketForce","GuardarCaja");

        ControlCaja controlUpload = rp3.marketforce.models.pedido.ControlCaja.getControlCaja(db, idCaja);

        JSONObject jObject = new JSONObject();
        try {

            jObject.put("IdCaja", PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            jObject.put("IdEstablecimiento", PreferenceManager.getInt(Contants.KEY_ID_ESTABLECIMIENTO));
            jObject.put("IdPuntoOperacion", PreferenceManager.getInt(Contants.KEY_ID_PUNTO_OPERACION));
            jObject.put("IdEmpresa", PreferenceManager.getInt(Contants.KEY_ID_EMPRESA));
            jObject.put("IdControlCaja", controlUpload.getIdControlCaja());
            jObject.put("MontoApertura", controlUpload.getValorApertura());
            jObject.put("FechaCierre", Convert.getDotNetTicksFromDate(controlUpload.getFechaCierre()));
            jObject.put("MontoCierre", 0);
            jObject.put("FechaApertura", Convert.getDotNetTicksFromDate(controlUpload.getFechaApertura()));
            jObject.put("UsrApertura", Session.getUser().getLogonName());
            jObject.put("UsrCierre", Session.getUser().getLogonName());

        } catch (Exception ex) {

        }

        webService.addParameter("controlCaja", jObject);

        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                controlUpload.setIdControlCaja(webService.getIntegerResponse());
                ControlCaja.update(db, controlUpload);
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

        } finally {
            webService.close();
        }

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }

    public static int executeSyncGetControl(DataBase db){
        WebService webService = new WebService("MartketForce","GetControl");

        try
        {
            webService.addStringParameter("@idCaja", PreferenceManager.getString(Contants.KEY_ID_CAJA));
            webService.addStringParameter("@idEmpresa", PreferenceManager.getString(Contants.KEY_ID_EMPRESA));
            webService.addStringParameter("@idEstablecimiento", PreferenceManager.getString(Contants.KEY_ID_ESTABLECIMIENTO));
            webService.addStringParameter("@idPuntoOperacion", PreferenceManager.getString(Contants.KEY_ID_PUNTO_OPERACION));
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONObject jObject = webService.getJSONObjectResponse();
                if(jObject != null && !jObject.isNull(Contants.KEY_SECUENCIA_FACTURA)) {
                    ControlCaja control = new ControlCaja();
                    control.setIdAgente(jObject.getInt("IdAgente"));
                    control.setValorApertura(Float.parseFloat(jObject.getString("MontoApertura")));
                    control.setFechaApertura(Convert.getDateFromDotNetTicks(jObject.getLong("FechaAperturaTicks")));

                    ControlCaja.insert(db, control);

                }
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return SyncAdapter.SYNC_EVENT_ERROR;
            }

        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
