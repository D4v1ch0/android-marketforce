package rp3.marketforce.sync;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import java.util.Calendar;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.DiaLaboral;
import rp3.marketforce.models.DiaNoLaboral;
import rp3.sync.SyncAudit;
import rp3.util.Convert;

/**
 * Created by magno_000 on 08/06/2015.
 */
public class Calendario {

    public static int executeSync(DataBase db) {
        WebService webService = new WebService("MartketForce", "GetCalendario");


        try {
            webService.addCurrentAuthToken();

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONObject typeCal = webService.getJSONObjectResponse();

            DiaLaboral.deleteAll(db, Contract.DiaLaboral.TABLE_NAME);
            DiaNoLaboral.deleteAll(db, Contract.DiaNoLaboral.TABLE_NAME);

            try {
                JSONArray types = typeCal.getJSONArray("DiasLaborales");

                for (int i = 0; i < types.length(); i++) {

                    JSONObject type = types.getJSONObject(i);
                    DiaLaboral dia = new rp3.marketforce.models.DiaLaboral();


                    dia.setEsLaboral(type.getBoolean("EsLaboral"));
                    dia.setOrden(type.getInt("Orden"));
                    dia.setIdDia(type.getInt("IdDia"));
                    dia.setHoraInicio1(type.getString("HoraInicio1"));
                    dia.setHoraFin1(type.getString("HoraFin1"));
                    if (!type.isNull("HoraInicio2"))
                        dia.setHoraInicio2(type.getString("HoraInicio2"));
                    if (!type.isNull("HoraFin2"))
                        dia.setHoraFin2(type.getString("HoraFin2"));

                    rp3.marketforce.models.DiaLaboral.insert(db, dia);


                }

                types = typeCal.getJSONArray("DiasNoLaborables");

                for (int i = 0; i < types.length(); i++) {

                    JSONObject type = types.getJSONObject(i);
                    DiaNoLaboral dia = new rp3.marketforce.models.DiaNoLaboral();

                    if (!type.isNull("EsParcial"))
                        dia.setEsParcial(type.getBoolean("EsParcial"));
                    dia.setEsteAnio(type.getBoolean("EsteAño"));
                    if (!type.isNull("FechaTicks"))
                        dia.setFecha(Convert.getDateFromDotNetTicks(type.getLong("FechaTicks")));
                    if (!type.isNull("HoraInicio"))
                        dia.setHoraInicio(type.getString("HoraInicio"));
                    if (!type.isNull("HoraFin"))
                        dia.setHoraFin(type.getString("HoraFin"));

                    rp3.marketforce.models.DiaNoLaboral.insert(db, dia);


                }

            } catch (JSONException e) {
                Log.e("Error", e.toString());
                return rp3.content.SyncAdapter.SYNC_EVENT_ERROR;
            }
        } finally {
            webService.close();
        }

        return rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
