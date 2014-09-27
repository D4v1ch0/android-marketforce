package rp3.marketforce.sync;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import android.util.Log;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.runtime.Session;
import rp3.util.Convert;

public class Rutas {

		public static int executeSync(DataBase db){
			WebService webService = new WebService("MartketForce","Agenda");
			webService.addParameter("@logonname", Session.getUser().getLogonName());
			webService.addParameter("@fechainicio", 635451264000000000l);
			webService.addParameter("@fechafin", 635477183990000000l);
			try
			{			
				webService.addCurrentAuthToken();
				
				try {
					webService.invokeWebService();	
				} catch (HttpResponseException e) {
					if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
						return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
					return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
				} catch (Exception e) {
					return SyncAdapter.SYNC_EVENT_ERROR;
				}
				
				JSONArray types = webService.getJSONArrayResponse();			
				
				rp3.marketforce.models.Agenda.deleteAll(db, Contract.Agenda.TABLE_NAME);
//				rp3.marketforce.models.AgendaTarea.deleteAll(db, Contract.AgendaTarea.TABLE_NAME);
//				
				long TICKS_AT_EPOCH = 621355968000000000L;
				long TICKS_PER_MILLISECOND = 10000;
				
				for(int i=0; i < types.length(); i++){
					
					try {
						JSONObject type = types.getJSONObject(i);
						rp3.marketforce.models.Agenda agenda = new rp3.marketforce.models.Agenda();
						agenda.setID(type.getLong("IdAgenda"));
						agenda.setIdRuta(type.getInt("IdRuta"));
						agenda.setIdCliente(type.getInt("IdCliente"));
						agenda.setIdClienteDireccion(type.getInt("IdClienteDireccion"));
//						agenda.setIdProgramacionRuta(type.getInt("IdProgramacionRuta"));
						Date date = new Date((type.getLong("FechaInicioTicks") - TICKS_AT_EPOCH) / TICKS_PER_MILLISECOND);
						agenda.setFechaInicio(date);
						agenda.setFechaFin(date);
						agenda.setCiudad(type.getString("Ciudad"));
						agenda.setNombreCompleto(type.getString("NombresCompletos"));
						agenda.setDireccion(type.getString("Direccion"));
						
//						agenda.setEstadoAgenda(type.getString(""));
						
						rp3.marketforce.models.Agenda.insert(db, agenda);
						
//						JSONArray strs = type.getJSONArray("");
//						
//						for(int j=0; j < strs.length(); j++){
//							JSONObject str = strs.getJSONObject(j);
//							rp3.marketforce.models.AgendaTarea agendaTarea = new rp3.marketforce.models.AgendaTarea();						
//							agendaTarea.setID(str.getLong(""));
//							agendaTarea.setIdRuta(str.getInt(""));
//							agendaTarea.setIdAgenda(str.getInt(""));
//							agendaTarea.setIdCliente(str.getInt(""));
//							agendaTarea.setIdClienteDireccion(str.getInt(""));
//							agendaTarea.setIdProgramacionRuta(str.getInt(""));
//							agendaTarea.setNombreTarea(str.getString(""));
//							agendaTarea.setTipoTarea(str.getInt(""));
//							
//							rp3.marketforce.models.AgendaTarea.insert(db, agendaTarea);
//						}
						
					} catch (JSONException e) {
						Log.e("Error", e.toString());
						return SyncAdapter.SYNC_EVENT_ERROR;
					}
					
				}
			}finally{
				webService.close();
			}
			
			return SyncAdapter.SYNC_EVENT_SUCCESS;		
		}
	
}
