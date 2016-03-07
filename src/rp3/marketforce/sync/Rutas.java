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
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.*;
import rp3.marketforce.models.Agenda;
import rp3.sync.SyncAudit;
import rp3.util.Convert;
import rp3.util.CursorUtils;
import rp3.util.DateTime;
import android.util.Log;

public class Rutas {

		public static int executeSync(DataBase db, Long inicio, Long fin, boolean inList){
			WebService webService = new WebService("MartketForce","Agenda");
            Calendar fechaUlt = Calendar.getInstance();
            fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.marketforce.sync.SyncAdapter.SYNC_TYPE_ACT_AGENDA, SyncAdapter.SYNC_EVENT_SUCCESS));
            fechaUlt.add(Calendar.MINUTE, -15);
			long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());
			
			if(inList)
			{
				if((inicio == null || inicio == 0) && !(fin == null || fin == 0))
				{
					Date date = Convert.getDateFromTicks(fin);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DATE, - 7);
					inicio = Convert.getDotNetTicksFromDate(cal.getTime());
					fin = Convert.getDotNetTicksFromDate(Convert.getDateFromTicks(fin));	
				}
				else
				{
					Date date = Convert.getDateFromTicks(inicio);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DATE, 7);
					inicio = Convert.getDotNetTicksFromDate(Convert.getDateFromTicks(inicio));
					fin = Convert.getDotNetTicksFromDate(cal.getTime());
				}
			}
			else
			{
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
			}
			
			webService.addParameter("@fechainicio", inicio);
			webService.addParameter("@fechafin", fin);
			if(!inList)
				webService.addParameter("@ultimaactualizacion", fecha);
			
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
						rp3.marketforce.models.Agenda agenda = rp3.marketforce.models.Agenda.getAgendaServer(db, (int) type.getLong("IdAgenda"));
						if(agenda == null)
							agenda = new Agenda();

                        if(type.getString("EstadoAgenda").equalsIgnoreCase(Contants.ESTADO_ELIMINADO))
                        {
                            rp3.marketforce.models.Agenda.deleteAgendaIdServer(db, (int) type.getLong("IdAgenda"));
                        }
                        else {

                            agenda.setIdAgenda((int) type.getLong("IdAgenda"));
                            agenda.setIdRuta(type.getInt("IdRuta"));
                            agenda.setIdCliente(type.getInt("IdCliente"));
                            agenda.setIdClienteDireccion(type.getInt("IdClienteDireccion"));
                            if (!type.isNull("IdClienteContacto")) {
                                agenda.setIdContacto(type.getInt("IdClienteContacto"));
                            }
                            if (!type.isNull("Observacion")) {
                                agenda.setObservaciones(type.getString("Observacion"));
                            }

                            agenda.setFechaInicio(Convert.getDateFromDotNetTicks(type.getLong("FechaInicioTicks")));
                            agenda.setFechaFin(Convert.getDateFromDotNetTicks(type.getLong("FechaFinTicks")));
                            if (!type.isNull("FechaInicioGestionTicks")) {
                                agenda.setFechaInicioReal(Convert.getDateFromDotNetTicks(type.getLong("FechaInicioGestionTicks")));
                                agenda.setFechaFinReal(Convert.getDateFromDotNetTicks(type.getLong("FechaFinGestionTicks")));
                            }
                            agenda.setCiudad(type.getString("Ciudad"));
                            agenda.setNombreCompleto(type.getString("NombresCompletos").trim().replace("null",""));
                            agenda.setDireccion(type.getString("Direccion"));

                            agenda.setEstadoAgenda(type.getString("EstadoAgenda"));
                            agenda.setEnviado(true);

                            rp3.marketforce.models.AgendaTarea.deleteTareas(db, agenda.getIdRuta(), agenda.getIdAgenda());


                            if (!rp3.marketforce.models.Agenda.existAgendaServer(db, agenda.getIdAgenda())) {
                                rp3.marketforce.models.Agenda.insert(db, agenda);
                            } else {
                                rp3.marketforce.models.Agenda.update(db, agenda);
                            }

                            JSONArray strs = type.getJSONArray("AgendaTareas");

                            for (int j = 0; j < strs.length(); j++) {
                                JSONObject str = strs.getJSONObject(j);

								boolean existe = false;

								if(agenda.getAgendaTareas() != null) {
									for (AgendaTarea tarea : agenda.getAgendaTareas()) {
										if (tarea.getIdTarea() == str.getInt("IdTarea"))
											existe = true;
									}
								}

								if(!existe) {
									rp3.marketforce.models.AgendaTarea agendaTarea = new rp3.marketforce.models.AgendaTarea();

									agendaTarea.setIdTarea(str.getInt("IdTarea"));
									agendaTarea.setIdRuta(str.getInt("IdRuta"));
									agendaTarea.setIdAgenda(str.getInt("IdAgenda"));
									//agendaTarea.setNombreTarea(str.getString("Nombre"));
									agendaTarea.setEstadoTarea(str.getString("EstadoTarea"));
									//agendaTarea.setTipoTarea(str.getString("TipoTarea"));

									rp3.marketforce.models.AgendaTarea.insert(db, agendaTarea);
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
