package rp3.marketforce.sync;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.*;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.FormaPago;
import rp3.util.Convert;

/**
 * Created by magno_000 on 24/04/2015.
 */
public class Caja {
    public static int executeSync(DataBase db){
        WebService webService = new WebService("MartketForce","GetCaja");

        try
        {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
                JSONObject jObject = webService.getJSONObjectResponse();

                rp3.marketforce.models.Caja.deleteAll(db, Contract.Caja.TABLE_NAME);
                FormaPago.deleteAll(db, Contract.FormaPago.TABLE_NAME);

                rp3.marketforce.models.Caja caja = new rp3.marketforce.models.Caja();
                caja.setMaximoDiasApertura(jObject.getInt("MaximoDiasApertura"));
                caja.setNombre(jObject.getString("Nombre"));
                caja.setSecuenciaRecibo(jObject.getInt("SecuenciaRecibo"));

                JSONObject jObjectControl = jObject.getJSONObject("CajaControl");
                caja.setMontoCierre(jObjectControl.getDouble("MontoCierre"));
                caja.setIdCajaControl(jObjectControl.getInt("IdControlCaja"));
                caja.setMontoApertura(jObjectControl.getDouble("MontoApertura"));
                //caja.setFechaApertura(Convert.getDateFromDotNetTicks(jObjectControl.getLong("FechaAperturaTicks")));
                //caja.setFechaCierre(Convert.getDateFromDotNetTicks(jObjectControl.getLong("FechaCierreTicks")));

                JSONArray jsonArray = jObject.getJSONArray("FormasPago");
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    FormaPago fp = new FormaPago();
                    JSONObject jFormaPago = jsonArray.getJSONObject(i);
                    fp.setIdFormaPago(jFormaPago.getInt("IdFormaPago"));
                    fp.setNombre(jFormaPago.getString("Nombre"));

                    FormaPago.insert(db, fp);
                }

                rp3.marketforce.models.Caja.insert(db, caja);

            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

        }finally{
            webService.close();
        }

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
