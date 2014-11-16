package rp3.marketforce.sync;

import java.util.Calendar;
import java.util.Date;

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
import rp3.util.CursorUtils;
import rp3.util.DateTime;
import android.util.Log;

public class Rutas {

		public static int executeSync(DataBase db, Long inicio, Long fin){
			WebService webService = new WebService("MartketForce","Agenda");		
			
			//inicio = rp3.marketforce.models.Agenda.getLastAgenda(db);
			//webService.addParameter("@fechainicio", 635451264000000000l);
			//webService.addParameter("@fechafin", 635477183990000000l);	
			
			if(inicio == null || inicio == 0){
				Calendar dateIni = DateTime.getCurrentCalendar();
				dateIni.add(Calendar.DATE, -7);
				inicio = Convert.getDotNetTicksFromDate(dateIni.getTime());											
			}
			else
			{
				inicio = Convert.getDotNetTicksFromDate(Convert.getDateFromTicks(inicio));
			}
			
			Date ini = Convert.getDateFromDotNetTicks(inicio);
			Calendar cal = Calendar.getInstance();
			cal.setTime(ini);
			if(fin == null || fin == 0)
			{
				cal.add(Calendar.DATE, 14);
			}
			else
			{
				cal.add(Calendar.DATE, 7);
			}
			
			fin = Convert.getDotNetTicksFromDate(cal.getTime());
			
			webService.addParameter("@fechainicio", inicio);
			webService.addParameter("@fechafin", fin);
			webService.addParameter("@incluiractividades", true);
			
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
				
				//rp3.marketforce.models.Agenda.deleteAll(db, Contract.Agenda.TABLE_NAME);
				//rp3.marketforce.models.Agenda.AgendaExt.deleteAll(db, Contract.AgendaExt.TABLE_NAME);
				//rp3.marketforce.models.AgendaTarea.deleteAll(db, Contract.AgendaTarea.TABLE_NAME);
				//rp3.marketforce.models.AgendaTareaActividades.deleteAll(db, Contract.AgendaTareaActividades.TABLE_NAME);
				//rp3.marketforce.models.AgendaTareaOpciones.deleteAll(db, Contract.AgendaTareaOpciones.TABLE_NAME);
				
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
						
						rp3.marketforce.models.AgendaTarea.deleteTareas(db, agenda.getIdRuta(), agenda.getID());
						
						rp3.marketforce.models.Agenda getter = rp3.marketforce.models.Agenda.getAgenda(db, agenda.getID());
						if(getter == null)
						{
							rp3.marketforce.models.Agenda.insert(db, agenda);
						}
						else
						{
							rp3.marketforce.models.Agenda.update(db, agenda);
						}
						
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
							rp3.marketforce.models.AgendaTareaActividades.deleteActividades(db, agendaTarea.getIdRuta(), agendaTarea.getIdAgenda(),
									agendaTarea.getIdTarea());
							
							JSONArray acts = str.getJSONArray("AgendaTareaActividades");
							
							for(int k=0; k < acts.length(); k++)
							{
								JSONObject act = acts.getJSONObject(k);
								rp3.marketforce.models.AgendaTareaActividades actividad = new rp3.marketforce.models.AgendaTareaActividades();
								
								actividad.setIdAgenda(act.getInt("IdAgenda"));
								actividad.setDescripcion(act.getString("Descripcion"));
								actividad.setOrden(act.getInt("Orden"));
								actividad.setResultado(act.getString("Resultado"));
								actividad.setIdRuta(act.getInt("IdRuta"));
								actividad.setIdTareaActividad(act.getInt("IdTareaActividad"));
								if(!act.isNull("IdTareaActividadPadre"))
									actividad.setIdTareaActividadPadre(act.getInt("IdTareaActividadPadre"));
								actividad.setIdTarea(act.getInt("IdTarea"));
								if(!act.isNull("IdTareaOpcion"))
									actividad.setIdTareaOpcion(act.getInt("IdTareaOpcion"));
								actividad.setTipo(act.getString("Tipo"));
								actividad.setIdTipoActividad(act.getInt("IdTipoActividad"));
								
								rp3.marketforce.models.AgendaTareaActividades.insert(db, actividad);
								rp3.marketforce.models.AgendaTareaOpciones.deleteOpciones(db, actividad.getIdRuta(), actividad.getIdAgenda(), 
										 actividad.getIdTarea(), actividad.getIdTareaActividad());
								
								JSONArray opcs = act.getJSONArray("AgendaTareaOpciones");
								
								for(int l=0; l < opcs.length(); l++){
									JSONObject opc = opcs.getJSONObject(l);
									rp3.marketforce.models.AgendaTareaOpciones opcion = new rp3.marketforce.models.AgendaTareaOpciones();
									
									opcion.setDescripcion(opc.getString("Descripcion"));
									opcion.setIdAgenda(opc.getInt("IdAgenda"));
									opcion.setIdRuta(opc.getInt("IdRuta"));
									opcion.setIdTareaActividad(opc.getInt("IdTareaActividad"));
									opcion.setIdTarea(opc.getInt("IdTarea"));
									opcion.setOrden(opc.getInt("Orden"));
									
									rp3.marketforce.models.AgendaTareaOpciones.delete(db, opcion);
									rp3.marketforce.models.AgendaTareaOpciones.insert(db, opcion);
								}
							}
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
