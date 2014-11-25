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
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.util.Convert;

public class Agenda {

	public static int executeSync(DataBase db, int idAgenda){
		WebService webService = new WebService("MartketForce","UpdateAgenda");			
		
		rp3.marketforce.models.Agenda agendaUpload = rp3.marketforce.models.Agenda.getAgendaUpload(db, idAgenda);
		
		JSONObject jObject = new JSONObject();
		try
		{
			jObject.put("IdAgenda", agendaUpload.getIdAgenda());
			jObject.put("IdRuta", agendaUpload.getIdRuta());
			//jObject.put("IdClienteContacto", agendaUpload.getIdContacto());
			jObject.put("EstadoAgenda", agendaUpload.getEstadoAgenda());
			//jObject.put("IdCliente", agendaUpload.getIdCliente());
			jObject.put("FechaInicioGestionTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaInicioReal()));
			jObject.put("FechaFinGestionTicks", Convert.getDotNetTicksFromDate(agendaUpload.getFechaFinReal()));
			
			JSONArray jArrayTareas = new JSONArray();
			for(AgendaTarea agt : agendaUpload.getAgendaTareas())
			{
				JSONObject jObjectTarea = new JSONObject();
				//jObjectTarea.put("IdAgenda", agt.getIdAgenda());
				//jObjectTarea.put("IdRuta", agt.getIdRuta());
				jObjectTarea.put("IdTarea", agt.getIdTarea());
				jObjectTarea.put("EstadoTarea", agt.getEstadoTarea());
				//jObjectTarea.put("TipoTarea", agt.getTipoTarea());
				
				JSONArray jArrayActividades = new JSONArray();
				for(AgendaTareaActividades ata : agt.getActividades())
				{
					JSONObject jObjectActividad = new JSONObject();
					//jObjectActividad.put("IdAgenda", ata.getIdAgenda());
					if(ata.getResultado().equals("null"))
						jObjectActividad.put("Resultado", " ");
					else if(ata.getResultado().equals("true"))
						jObjectActividad.put("Resultado", "Sí");
					else if(ata.getResultado().equals("false"))
						jObjectActividad.put("Resultado", "No");
					else
						jObjectActividad.put("Resultado", ata.getResultado());
						
					//jObjectActividad.put("IdRuta", ata.getIdRuta());
					jObjectActividad.put("IdTareaActividad", ata.getIdTareaActividad());
					//jObjectActividad.put("IdTareaActividadPadre", ata.getIdTareaActividadPadre());
					jObjectActividad.put("IdTarea", ata.getIdTarea());
					jObjectActividad.put("IdTareaOpcion", ata.getIdTareaOpcion());
					//jObjectActividad.put("Tipo", ata.getTipo());
					//jObjectActividad.put("IdTipoActividad", ata.getIdTipoActividad());
					
					jArrayActividades.put(jObjectActividad);
				}
				
				jObjectTarea.put("AgendaTareaActividades", jArrayActividades);
				
				jArrayTareas.put(jObjectTarea);
			}
			
			jObject.put("AgendaTareas", jArrayTareas);
		}
		catch(Exception ex)
		{
			
		}
		
		webService.addParameter("agenda", jObject);
		
		try
		{			
			webService.addCurrentAuthToken();
			
			try {
				webService.invokeWebService();
				String error = webService.getStringResponse();
				agendaUpload.setEnviado(true);
				rp3.marketforce.models.Agenda.update(db, agendaUpload);
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
