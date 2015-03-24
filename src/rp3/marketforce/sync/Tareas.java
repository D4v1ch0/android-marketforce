package rp3.marketforce.sync;


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
import rp3.sync.SyncAudit;
import rp3.util.Convert;

import android.util.Log;

import java.util.Calendar;

public class Tareas {

	public static int executeSync(DataBase db){
		WebService webService = new WebService("MartketForce","GetTareas");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.marketforce.sync.SyncAdapter.SYNC_TYPE_TODO, SyncAdapter.SYNC_EVENT_SUCCESS));
        long fecha = rp3.util.Convert.getDotNetTicksFromDate(fechaUlt.getTime());

		try
		{			
			webService.addCurrentAuthToken();
            webService.addParameter("@ultimaActualizacion", fecha);
			
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
			
			//rp3.marketforce.models.Tarea.deleteAll(db, Contract.Tareas.TABLE_NAME);
			//rp3.marketforce.models.Actividad.deleteAll(db, Contract.Actividades.TABLE_NAME);
			//rp3.marketforce.models.AgendaTareaOpciones.deleteAll(db, Contract.AgendaTareaOpciones.TABLE_NAME);
			//rp3.marketforce.models.AgendaTareaActividades.deleteAll(db, Contract.AgendaTareaActividades.TABLE_NAME);
			//rp3.marketforce.models.AgendaTareaOpciones.deleteAll(db, Contract.AgendaTareaOpciones.TABLE_NAME);
			
			for(int i=0; i < types.length(); i++){
				
				try {					
						JSONObject type = types.getJSONObject(i);
						rp3.marketforce.models.Tarea tarea = new rp3.marketforce.models.Tarea();

                        if(type.getString("Estado").equalsIgnoreCase(Contants.ESTADO_ELIMINADO))
                        {
                            rp3.marketforce.models.Tarea toDelete = rp3.marketforce.models.Tarea.getTareaId(db,type.getInt("IdTarea"));
                            if(toDelete != null)
                            {
                                rp3.marketforce.models.Tarea.delete(db, toDelete);
                            }
                        }
                        else {

                            tarea.setIdTarea(type.getInt("IdTarea"));
                            tarea.setNombreTarea(type.getString("Descripcion"));
                            tarea.setEstadoTarea(type.getString("Estado"));
                            tarea.setTipoTarea(type.getString("TipoTarea"));
                            tarea.setFechaVigenciaDesde(Convert.getDateFromDotNetTicks(type.getLong("FechaVigenciaDesdeTicks")));
                            if(!type.isNull("FechaVigenciaHastaTicks"))
                                tarea.setFechaVigenciaHasta(Convert.getDateFromDotNetTicks(type.getLong("FechaVigenciaHastaTicks")));

                            rp3.marketforce.models.Tarea tareaUpdate = rp3.marketforce.models.Tarea.getTareaId(db, tarea.getIdTarea());

                            if(tareaUpdate != null)
                                rp3.marketforce.models.Tarea.update(db, tarea);
                            else
                                rp3.marketforce.models.Tarea.insert(db, tarea);

                            rp3.marketforce.models.Actividad.deleteFromTareas(db, tarea.getIdTarea());

                            JSONArray strs = type.getJSONArray("TareaActividades");

                            for (int j = 0; j < strs.length(); j++) {
                                JSONObject str = strs.getJSONObject(j);
                                rp3.marketforce.models.Actividad actividad = new rp3.marketforce.models.Actividad();

                                actividad.setIdTarea(str.getInt("IdTarea"));
                                actividad.setDescripcion(str.getString("Descripcion"));
                                if (!str.isNull("IdTareaActividadPadre"))
                                    actividad.setIdTareaActividadPadre(str.getInt("IdTareaActividadPadre"));
                                actividad.setIdTareaActividad(str.getInt("IdTareaActividad"));
                                actividad.setTipo(str.getString("Tipo"));
                                actividad.setOrden(str.getInt("Orden"));
                                actividad.setIdTipoActividad(str.getInt("IdTipoActividad"));

                                rp3.marketforce.models.Actividad.insert(db, actividad);

                                rp3.marketforce.models.AgendaTareaOpciones.deleteOpciones(db, tarea.getIdTarea(), actividad.getIdTareaActividad());

                                JSONArray opcs = str.getJSONArray("TareaOpciones");

                                for (int k = 0; k < opcs.length(); k++) {
                                    JSONObject opc = opcs.getJSONObject(k);
                                    rp3.marketforce.models.AgendaTareaOpciones opcion = new rp3.marketforce.models.AgendaTareaOpciones();

                                    opcion.setIdTarea(opc.getInt("IdTarea"));
                                    opcion.setDescripcion(opc.getString("Descripcion"));
                                    opcion.setIdTareaActividad(opc.getInt("IdTareaActividad"));
                                    opcion.setOrden(opc.getInt("Orden"));

                                    rp3.marketforce.models.AgendaTareaOpciones.insert(db, opcion);
                                }
                            }
                        }
					}
					
				catch (JSONException e) {
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
