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
import rp3.marketforce.models.pedido.Banco;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.MarcaTarjeta;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.models.pedido.Tarjeta;
import rp3.marketforce.models.pedido.TarjetaComision;
import rp3.marketforce.models.pedido.TipoDiferido;
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
                    PreferenceManager.setValue(Contants.KEY_SECUENCIA_COTIZACION, jObject.getInt(Contants.KEY_SECUENCIA_COTIZACION));
                    PreferenceManager.setValue(Contants.KEY_SECUENCIA_PEDIDO, jObject.getInt(Contants.KEY_SECUENCIA_PEDIDO));
                    PreferenceManager.setValue(Contants.KEY_EMPRESA, jObject.getString(Contants.KEY_EMPRESA));
                    PreferenceManager.setValue(Contants.KEY_RUC, jObject.getString(Contants.KEY_RUC));
                    PreferenceManager.setValue(Contants.KEY_DIRECCION, jObject.getString(Contants.KEY_DIRECCION));
                    PreferenceManager.setValue(Contants.KEY_TELEFONO, jObject.getString(Contants.KEY_TELEFONO));
                    PreferenceManager.setValue(Contants.KEY_ESTABLECIMIENTO, jObject.getString(Contants.KEY_ESTABLECIMIENTO));
                    PreferenceManager.setValue(Contants.KEY_NOMBRE_PUNTO_OPERACION, jObject.getString(Contants.KEY_NOMBRE_PUNTO_OPERACION));
                    PreferenceManager.setValue(Contants.KEY_SERIE, jObject.getString(Contants.KEY_SERIE));
                    PreferenceManager.setValue(Contants.KEY_ID_ESTABLECIMIENTO, jObject.getInt(Contants.KEY_ID_ESTABLECIMIENTO));
                    PreferenceManager.setValue(Contants.KEY_ID_CAJA, jObject.getInt(Contants.KEY_ID_CAJA));
                    PreferenceManager.setValue(Contants.KEY_CAJA, jObject.getString(Contants.KEY_CAJA));
                    PreferenceManager.setValue(Contants.KEY_ID_PUNTO_OPERACION, jObject.getInt(Contants.KEY_ID_PUNTO_OPERACION));
                    PreferenceManager.setValue(Contants.KEY_ID_EMPRESA, jObject.getInt(Contants.KEY_ID_EMPRESA));
                    //if(!jObject.isNull(Contants.KEY_DESCUENTO_MAXIMO))
                    //    PreferenceManager.setValue(Contants.KEY_DESCUENTO_MAXIMO, jObject.getInt(Contants.KEY_DESCUENTO_MAXIMO));

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
                    FormaPago.DeletePagos(db);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FormaPago formaPago = FormaPago.getFormaPago(db, jsonObject.getInt("IdFormaPago"));
                        formaPago.setIdFormaPago(jsonObject.getInt("IdFormaPago"));
                        formaPago.setDescripcion(jsonObject.getString("Descripcion"));
                        formaPago.setEstado("A");
                        if(formaPago.getID() == 0)
                            FormaPago.insert(db, formaPago);
                        else
                            FormaPago.update(db, formaPago);
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

    public static int executeSyncBanco(DataBase db){
        WebService webService = new WebService("MartketForce","GetBancos");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONArray jsonArray = webService.getJSONArrayResponse();
                if(jsonArray != null) {
                    Banco.deleteAll(db, Contract.Banco.TABLE_NAME);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Banco banco = new Banco();
                        banco.setIdBanco(jsonObject.getInt("IdBanco"));
                        banco.setDescripcion(jsonObject.getString("Descripcion"));
                        Banco.insert(db, banco);
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

    public static int executeSyncMarcaTarjetas(DataBase db){
        WebService webService = new WebService("MartketForce","GetMarcaTarjetas");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONArray jsonArray = webService.getJSONArrayResponse();
                if(jsonArray != null) {
                    MarcaTarjeta.deleteAll(db, Contract.MarcaTarjeta.TABLE_NAME);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MarcaTarjeta marcaTarjeta = new MarcaTarjeta();
                        marcaTarjeta.setIdMarcaTarjeta(jsonObject.getInt("IdMarcaTarjeta"));
                        marcaTarjeta.setDescripcion(jsonObject.getString("Descripcion"));
                        MarcaTarjeta.insert(db, marcaTarjeta);
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

    public static int executeSyncTarjetas(DataBase db){
        WebService webService = new WebService("MartketForce","GetTarjetas");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONArray jsonArray = webService.getJSONArrayResponse();
                if(jsonArray != null) {
                    Tarjeta.deleteAll(db, Contract.Tarjeta.TABLE_NAME);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Tarjeta tarjeta = new Tarjeta();
                        tarjeta.setIdMarcaTarjeta(jsonObject.getInt("IdMarcaTarjeta"));
                        //tarjeta.setInterna(jsonObject.getInt("Interna"));
                        tarjeta.setIdBanco(jsonObject.getInt("IdBanco"));
                        Tarjeta.insert(db, tarjeta);
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

    public static int executeSyncTipoDiferidos(DataBase db){
        WebService webService = new WebService("MartketForce","GetTiposDiferidos");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONArray jsonArray = webService.getJSONArrayResponse();
                if(jsonArray != null) {
                    TipoDiferido.deleteAll(db, Contract.TipoDiferido.TABLE_NAME);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TipoDiferido tipoDiferido = new TipoDiferido();
                        tipoDiferido.setIdTipoDiferido(jsonObject.getInt("IdTipoDiferido"));
                        tipoDiferido.setCuotas(jsonObject.getInt("Cuotas"));
                        tipoDiferido.setTipoCredito(jsonObject.getString("TipoCredito"));
                        tipoDiferido.setDescripcion(jsonObject.getString("Descripcion"));
                        TipoDiferido.insert(db, tipoDiferido);
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

    public static int executeSyncTarjetaComision(DataBase db){
        WebService webService = new WebService("MartketForce","GetTarjetaComisiones");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONArray jsonArray = webService.getJSONArrayResponse();
                if(jsonArray != null) {
                    TarjetaComision.deleteAll(db, Contract.TarjetaComision.TABLE_NAME);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TarjetaComision tarjetaComision = new TarjetaComision();
                        tarjetaComision.setIdBanco(jsonObject.getInt("IdBanco"));
                        tarjetaComision.setIdMarcaTarjeta(jsonObject.getInt("IdMarcaTarjeta"));
                        tarjetaComision.setIdEmpresa(jsonObject.getInt("IdEmpresa"));
                        tarjetaComision.setIdTipoDiferido(jsonObject.getInt("IdTipoDiferido"));
                        TarjetaComision.insert(db, tarjetaComision);
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

    public static int executeSyncTransacciones(DataBase db){
        WebService webService = new WebService("MartketForce","GetTransacciones");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONArray jsonArray = webService.getJSONArrayResponse();
                if(jsonArray != null) {
                    PreferenceManager.setValue(Contants.KEY_TRANSACCION_COTIZACION, false);
                    PreferenceManager.setValue(Contants.KEY_TRANSACCION_FACTURA, false);
                    PreferenceManager.setValue(Contants.KEY_TRANSACCION_NOTA_CREDITO, false);
                    PreferenceManager.setValue(Contants.KEY_TRANSACCION_PEDIDO, false);
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PreferenceManager.setValue(jsonObject.getString("Transaccion"), true);
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
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JSONObject jObject = new JSONObject();
        try {

            jObject.put("IdCaja", PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            jObject.put("IdEstablecimiento", PreferenceManager.getInt(Contants.KEY_ID_ESTABLECIMIENTO));
            jObject.put("IdPuntoOperacion", PreferenceManager.getInt(Contants.KEY_ID_PUNTO_OPERACION));
            jObject.put("IdEmpresa", PreferenceManager.getInt(Contants.KEY_ID_EMPRESA));
            jObject.put("IdControlCaja", controlUpload.getIdControlCaja());
            jObject.put("MontoApertura", df.format(controlUpload.getValorApertura()));
            jObject.put("FechaAperturaTicks", Convert.getDotNetTicksFromDate(controlUpload.getFechaApertura()));
            jObject.put("UsrApertura", Session.getUser().getLogonName());
            if(controlUpload.getFechaCierre() != null && controlUpload.getFechaCierre().getTime() > 0) {
                jObject.put("FechaCierreTicks", Convert.getDotNetTicksFromDate(controlUpload.getFechaCierre()));
                jObject.put("MontoCierre", df.format(controlUpload.getValorCierre()));
                jObject.put("UsrCierre", Session.getUser().getLogonName());
            }

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
        if(ControlCaja.getControlCajaActiva(db) != null)
            return SyncAdapter.SYNC_EVENT_SUCCESS;
        try
        {
            webService.addIntParameter("@idCaja", PreferenceManager.getInt(Contants.KEY_ID_CAJA));
            webService.addIntParameter("@idEmpresa", PreferenceManager.getInt(Contants.KEY_ID_EMPRESA));
            webService.addIntParameter("@idEstablecimiento", PreferenceManager.getInt(Contants.KEY_ID_ESTABLECIMIENTO));
            webService.addIntParameter("@idPuntoOperacion", PreferenceManager.getInt(Contants.KEY_ID_PUNTO_OPERACION));
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONObject jObject = webService.getJSONObjectResponse();
                if(jObject != null && jObject.getInt("IdControlCaja") != 0) {
                    ControlCaja control = new ControlCaja();
                    control.setIdControlCaja(jObject.getInt("IdControlCaja"));
                    control.setIdCaja(jObject.getInt("IdCaja"));
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

    public static int executeSyncCerrarCaja(DataBase db, long idCaja){
        WebService webService = new WebService("MartketForce","CerrarCaja");
        ControlCaja controlCaja = ControlCaja.getControlCaja(db, idCaja);

        try
        {
            webService.addIntParameter("@idControlCaja", controlCaja.getIdControlCaja());
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                ControlCaja.delete(db, controlCaja);
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
