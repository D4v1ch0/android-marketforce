package rp3.marketforce.sync;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.Convert;
import rp3.util.DateTime;
import android.util.Log;

public class Rutas {

		public static int executeSync(DataBase db, Long inicio, Long fin){
			WebService webService = new WebService("MartketForce","Agenda");			
			//webService.addParameter("@fechainicio", 635451264000000000l);
			//webService.addParameter("@fechafin", 635477183990000000l);	
			
			if(inicio == null){
				Calendar dateIni = DateTime.getCurrentCalendar();
				dateIni.add(Calendar.DATE, -7);
				inicio = Convert.getDotNetTicksFromDate(dateIni.getTime());
				
				Calendar dateFin = DateTime.getCurrentCalendar();
				dateFin.add(Calendar.DATE, 30);
				fin = Convert.getDotNetTicksFromDate(dateFin.getTime());												
			}
			
			webService.addParameter("@fechainicio", inicio);
			webService.addParameter("@fechafin", fin);
			
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
				rp3.marketforce.models.Agenda.AgendaExt.deleteAll(db, Contract.AgendaExt.TABLE_NAME);
				rp3.marketforce.models.AgendaTarea.deleteAll(db, Contract.AgendaTarea.TABLE_NAME);
				
				for(int i=0; i < types.length(); i++){
					
					try {
						JSONObject type = types.getJSONObject(i);
						rp3.marketforce.models.Agenda agenda = new rp3.marketforce.models.Agenda();
						agenda.setID(type.getLong("IdAgenda"));
						agenda.setIdRuta(type.getInt("IdRuta"));
						agenda.setIdCliente(type.getInt("IdCliente"));
						agenda.setIdClienteDireccion(type.getInt("IdClienteDireccion"));
//						agenda.setIdProgramacionRuta(type.getInt("IdProgramacionRuta"));
												
						agenda.setFechaInicio( Convert.getDateFromDotNetTicks(type.getLong("FechaInicioTicks")) );
						agenda.setFechaFin( Convert.getDateFromDotNetTicks(type.getLong("FechaFinTicks")) );
						agenda.setCiudad(type.getString("Ciudad"));
						agenda.setNombreCompleto(type.getString("NombresCompletos"));
						agenda.setDireccion(type.getString("Direccion"));
						
						agenda.setEstadoAgenda(type.getString("EstadoAgenda"));
						
						rp3.marketforce.models.Agenda.insert(db, agenda);
						
						JSONArray strs = type.getJSONArray("AgendaTareas");
						
						for(int j=0; j < strs.length(); j++){
							JSONObject str = strs.getJSONObject(j);
							rp3.marketforce.models.AgendaTarea agendaTarea = new rp3.marketforce.models.AgendaTarea();						
							
							agendaTarea.setIdTarea(str.getInt("IdTarea"));
							agendaTarea.setIdRuta(str.getInt("IdRuta"));
							agendaTarea.setIdAgenda(str.getInt("IdAgenda"));						
							agendaTarea.setNombreTarea(str.getString("Nombre"));
							agendaTarea.setEstadoTarea(str.getString("EstadoTarea"));
							agendaTarea.setTipoTarea(str.getString("TipoTarea"));
							
							rp3.marketforce.models.AgendaTarea.insert(db, agendaTarea);
						}
						
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
