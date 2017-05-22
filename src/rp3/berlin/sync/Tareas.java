package rp3.berlin.sync;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.berlin.Contants;
import rp3.berlin.db.Contract;
import rp3.berlin.models.Campo;
import rp3.sync.SyncAudit;
import rp3.util.Convert;

import android.util.Log;

import java.util.Calendar;

public class Tareas {

	public static int executeSync(DataBase db){
		WebService webService = new WebService("MartketForce","GetTareas");
        Calendar fechaUlt = Calendar.getInstance();
        fechaUlt.setTime(SyncAudit.getLastSyncDate(rp3.berlin.sync.SyncAdapter.SYNC_TYPE_TODO, SyncAdapter.SYNC_EVENT_SUCCESS));
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
						rp3.berlin.models.Tarea tarea = new rp3.berlin.models.Tarea();

                        if(type.getString("Estado").equalsIgnoreCase(Contants.ESTADO_ELIMINADO))
                        {
                            rp3.berlin.models.Tarea toDelete = rp3.berlin.models.Tarea.getTareaId(db,type.getInt("IdTarea"));
                            if(toDelete != null)
                            {
                                rp3.berlin.models.Tarea.delete(db, toDelete);
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

                            rp3.berlin.models.Tarea tareaUpdate = rp3.berlin.models.Tarea.getTareaId(db, tarea.getIdTarea());

                            if(tareaUpdate != null)
                                rp3.berlin.models.Tarea.update(db, tarea);
                            else
                                rp3.berlin.models.Tarea.insert(db, tarea);

                            rp3.berlin.models.Actividad.deleteFromTareas(db, tarea.getIdTarea());

                            JSONArray strs = type.getJSONArray("TareaActividades");

                            for (int j = 0; j < strs.length(); j++) {
                                JSONObject str = strs.getJSONObject(j);
                                rp3.berlin.models.Actividad actividad = new rp3.berlin.models.Actividad();

                                actividad.setIdTarea(str.getInt("IdTarea"));
                                actividad.setDescripcion(str.getString("Descripcion"));
                                if (!str.isNull("IdTareaActividadPadre"))
                                    actividad.setIdTareaActividadPadre(str.getInt("IdTareaActividadPadre"));
                                actividad.setIdTareaActividad(str.getInt("IdTareaActividad"));
                                actividad.setTipo(str.getString("Tipo"));
                                actividad.setOrden(str.getInt("Orden"));
                                actividad.setIdTipoActividad(str.getInt("IdTipoActividad"));
                                if(str.isNull("Limite"))
                                    actividad.setLimite(0);
                                else
                                    actividad.setLimite(str.getInt("Limite"));


                                rp3.berlin.models.Actividad.insert(db, actividad);

                                rp3.berlin.models.AgendaTareaOpciones.deleteOpciones(db, tarea.getIdTarea(), actividad.getIdTareaActividad());

                                JSONArray opcs = str.getJSONArray("TareaOpciones");

                                for (int k = 0; k < opcs.length(); k++) {
                                    JSONObject opc = opcs.getJSONObject(k);
                                    rp3.berlin.models.AgendaTareaOpciones opcion = new rp3.berlin.models.AgendaTareaOpciones();

                                    opcion.setIdTarea(opc.getInt("IdTarea"));
                                    opcion.setDescripcion(opc.getString("Descripcion"));
                                    opcion.setIdTareaActividad(opc.getInt("IdTareaActividad"));
                                    opcion.setOrden(opc.getInt("Orden"));

                                    rp3.berlin.models.AgendaTareaOpciones.insert(db, opcion);
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

    public static int executeSyncTareaActualizacion(DataBase db, int id){
        WebService webService = new WebService("MartketForce","GetTareasActualizacion");

        try
        {
            webService.addCurrentAuthToken();
            webService.addParameter("@idTarea", id);

            try {
                webService.invokeWebService();
            } catch (HttpResponseException e) {
                if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                    return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
            } catch (Exception e) {
                return SyncAdapter.SYNC_EVENT_ERROR;
            }

            JSONObject type = webService.getJSONObjectResponse();

            Campo.deleteAll(db, Contract.Campos.TABLE_NAME);
            try {
                PreferenceManager.setValue(Contants.KEY_PERMITIR_CREACION, type.getBoolean(Contants.KEY_PERMITIR_CREACION));
                PreferenceManager.setValue(Contants.KEY_PERMITIR_MODIFICACION, type.getBoolean(Contants.KEY_PERMITIR_MODIFICACION));
                PreferenceManager.setValue(Contants.KEY_SIEMPRE_EDITAR, type.getBoolean(Contants.KEY_SIEMPRE_EDITAR));
                PreferenceManager.setValue(Contants.KEY_SOLO_FALTANTES, type.getBoolean(Contants.KEY_SOLO_FALTANTES));

                JSONArray strs = type.getJSONArray("Campos");

                for (int j = 0; j < strs.length(); j++) {
                    JSONObject str = strs.getJSONObject(j);
                    Campo campo = new rp3.berlin.models.Campo();

                    campo.setIdCampo(str.getString("IdCampo"));
                    campo.setCreacion(str.getBoolean("C"));
                    campo.setModificacion(str.getBoolean("M"));
                    campo.setGestion(str.getBoolean("G"));
                    if(!str.isNull("O"))
                        campo.setObligatorio(str.getBoolean("O"));

                    rp3.berlin.models.Campo.insert(db, campo);

                }
            }

            catch (JSONException e) {
                Log.e("Error", e.toString());
                return SyncAdapter.SYNC_EVENT_ERROR;
            }
        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }
}
