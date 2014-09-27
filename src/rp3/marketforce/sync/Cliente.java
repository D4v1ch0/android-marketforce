package rp3.marketforce.sync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.runtime.Session;
import rp3.util.Convert;
import android.util.Log;

public class Cliente {
	
		public static int executeSync(DataBase db){
			WebService webService = new WebService("MartketForce","GetClientes");
			webService.addParameter("@logonname", Session.getUser().getLogonName());
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
				
				rp3.marketforce.models.Cliente.deleteAll(db, Contract.Cliente.TABLE_NAME);
				rp3.marketforce.models.ClienteDireccion.deleteAll(db, Contract.ClienteDireccion.TABLE_NAME);
				
				for(int i=0; i < types.length(); i++){
					
					try {
						
						JSONObject type = types.getJSONObject(i);
						
						 rp3.marketforce.models.Cliente cl = new rp3.marketforce.models.Cliente();
						cl.setApellido1(type.getString("Apellido1"));
						cl.setApellido2(type.getString("Apellido2"));
						cl.setCalificacion(type.getInt("Calificacion"));
						cl.setCorreoElectronico(type.getString("CorreoElectronico"));
						cl.setEstadoCivil(type.getString("EstadoCivil"));
						cl.setGenero(type.getString("Genero"));
						cl.setIdCanal(type.getInt("IdCanal"));
						cl.setID(type.getLong("IdCliente"));
						cl.setIdentificacion(type.getString("Identificacion"));
						cl.setIdTipoCliente(type.getInt("IdTipoCliente"));
						cl.setNombre1(type.getString("Nombre1"));
						cl.setNombre2(type.getString("Nombre2"));
						cl.setFechaNacimiento(Convert.getDateFromTicks(type.getLong("FechaNacimientoTicks")));
						cl.setNombreCompleto(type.getString("NombresCompletos"));
					
						rp3.marketforce.models.Cliente.insert(db, cl);
						
						JSONArray strs = type.getJSONArray("ClienteDirecciones");
						
						for(int j=0; j < strs.length(); j++){
							JSONObject str = strs.getJSONObject(j);
							rp3.marketforce.models.ClienteDireccion  clienteDir = new rp3.marketforce.models.ClienteDireccion();
							
							clienteDir.setDireccion(""+str.getString("Direccion"));
							String flag = str.getString("EsPrincipal");
							if(flag.equals("true"))
								clienteDir.setIdPrincipal(1);
							else
							   clienteDir.setIdPrincipal(0);
							
							clienteDir.setIdCiudad(str.getInt("IdCiudad"));
							clienteDir.setIdCliente(str.getLong("IdCliente"));							
							clienteDir.setIdClienteDireccion(str.getInt("IdClienteDireccion"));
							clienteDir.setLatitud(str.getDouble("Latitud"));
							clienteDir.setLongitud(str.getDouble("Longitud"));
							clienteDir.setReferencia(""+str.getString("Referencia"));
							clienteDir.setTelefono1(""+str.getString("Telefono1"));
							clienteDir.setTelefono2(""+str.getString("Telefono2"));
							clienteDir.setTipoDireccion(""+str.getString("TipoDireccion"));
							
							rp3.marketforce.models.ClienteDireccion.insert(db, clienteDir);
						}
					} catch (JSONException e) {
						Log.e("Entro","Error: "+e.toString());
						return SyncAdapter.SYNC_EVENT_ERROR;
					}
					
				}
			}finally{
				webService.close();
			}
			
			return SyncAdapter.SYNC_EVENT_SUCCESS;		
		}
	
}
