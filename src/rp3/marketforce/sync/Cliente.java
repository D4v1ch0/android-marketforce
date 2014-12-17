package rp3.marketforce.sync;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.configuration.PreferenceManager;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.db.Contract.Contacto;
import rp3.marketforce.utils.Utils;
import rp3.sync.SyncAudit;
import rp3.util.BitmapUtils;
import rp3.util.Convert;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class Cliente {
	
		public static int executeSync(DataBase db){
			WebService webService = new WebService("MartketForce","GetClientes");
			
			try
			{			
				webService.addCurrentAuthToken();
				long fecha = rp3.util.Convert.getDotNetTicksFromDate(SyncAudit.getLastSyncDate(rp3.marketforce.sync.SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE, SyncAdapter.SYNC_EVENT_SUCCESS));
				webService.addLongParameter("@ultimaactualizacion", fecha);
				
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
				
				//rp3.marketforce.models.Cliente.deleteAll(db, Contract.Cliente.TABLE_NAME);
				//rp3.marketforce.models.ClienteDireccion.deleteAll(db, Contract.ClienteDireccion.TABLE_NAME);
				//rp3.marketforce.models.Cliente.ClientExt.deleteAll(db, Contract.ClientExt.TABLE_NAME);
				
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
						cl.setIdTipoIdentificacion(type.getInt("IdTipoIdentificacion"));
						cl.setIdentificacion(type.getString("Identificacion"));
						cl.setIdTipoCliente(type.getInt("IdTipoCliente"));
						cl.setNombre1(type.getString("Nombre1"));
						cl.setNombre2(type.getString("Nombre2"));
						cl.setFechaNacimiento(Convert.getDateFromDotNetTicks(type.getLong("FechaNacimientoTicks")));
						cl.setNombreCompleto(type.getString("NombresCompletos"));
						cl.setURLFoto(type.getString("Foto"));
						cl.setTipoPersona(type.getString("TipoPersona"));
						cl.setActividadEconomica(type.getString("ActividadEconomica"));
						cl.setPaginaWeb(type.getString("PaginaWeb"));
						cl.setRazonSocial(type.getString("RazonSocial"));
																	
						JSONArray strs = type.getJSONArray("ClienteDirecciones");
						
						rp3.marketforce.models.ClienteDireccion.deleteClienteDireccionIdCliente(db, cl.getID());
						
						for(int j=0; j < strs.length(); j++){
							JSONObject str = strs.getJSONObject(j);
							rp3.marketforce.models.ClienteDireccion  clienteDir = new rp3.marketforce.models.ClienteDireccion();
							
							clienteDir.setDireccion(""+str.getString("Direccion"));							
							
							if(!str.isNull("IdCiudad"))
								clienteDir.setIdCiudad(str.getInt("IdCiudad"));
							clienteDir.setIdCliente(str.getLong("IdCliente"));							
							clienteDir.setIdClienteDireccion(str.getInt("IdClienteDireccion"));
							if(!str.isNull("Latitud"))
								clienteDir.setLatitud(str.getDouble("Latitud"));
							if(!str.isNull("Longitud"))
								clienteDir.setLongitud(str.getDouble("Longitud"));
							clienteDir.setReferencia(""+str.getString("Referencia"));
							clienteDir.setTelefono1(""+str.getString("Telefono1"));
							clienteDir.setTelefono2(""+str.getString("Telefono2"));
							clienteDir.setTipoDireccion(""+str.getString("TipoDireccion"));
							
							if(str.getBoolean("EsPrincipal")){
								clienteDir.setEsPrincipal(true);
								cl.setDireccion(clienteDir.getDireccion());
								cl.setTelefono(clienteDir.getTelefono1());
							}								
							else
							   clienteDir.setEsPrincipal(false);
							
							rp3.marketforce.models.ClienteDireccion.insert(db, clienteDir);
						}
						
						strs = type.getJSONArray("ClienteContactos");
						
						rp3.marketforce.models.Contacto.deleteContactoIdCliente(db, cl.getID());
						
						for(int j=0; j < strs.length(); j++){
							JSONObject str = strs.getJSONObject(j);
							rp3.marketforce.models.Contacto  clienteCont = new rp3.marketforce.models.Contacto();
							
							clienteCont.setIdContacto(str.getLong("IdClienteContacto"));							
							
							clienteCont.setIdCliente(str.getLong("IdCliente"));							
							clienteCont.setIdClienteDireccion(str.getInt("IdClienteDireccion"));
							clienteCont.setNombre(""+str.getString("Nombre"));
							clienteCont.setApellido(""+str.getString("Apellido"));
							clienteCont.setCargo(""+str.getString("Cargo"));
							clienteCont.setTelefono1(""+str.getString("Telefono1"));
							clienteCont.setTelefono2(""+str.getString("Telefono2"));
							clienteCont.setCorreo(""+str.getString("CorreoElectronico"));
							if(!str.isNull("Foto"))
								clienteCont.setURLFoto(""+str.getString("Foto"));
							
							rp3.marketforce.models.Contacto.insert(db, clienteCont);
						}
						if(rp3.marketforce.models.Cliente.getClienteID(db, cl.getID(), false) == null)
						{
							rp3.marketforce.models.Cliente.insert(db, cl);
						}
						else
						{
							rp3.marketforce.models.Cliente.update(db, cl, rp3.marketforce.models.Cliente.ACTION_SYNC);
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
		
		public static int executeSyncCreate(DataBase db, String cliente){
			WebService webService = new WebService("MartketForce","CreateCliente");			
			
			int id = 0;
			rp3.marketforce.models.Cliente cl = new rp3.marketforce.models.Cliente();
			JSONObject jObject = new JSONObject();
			JSONArray jArray = new JSONArray();
			try
			{
				jObject = new JSONObject(cliente);
				jArray.put(jObject);
			}
			catch(Exception ex)
			{
				
			}
			
			webService.addParameter("Clientes", jArray);
			
			try
			{			
				webService.addCurrentAuthToken();
				
				try {
					webService.invokeWebService();
					JSONObject codigos = webService.getJSONObjectResponse();
					JSONArray responseArray = codigos.getJSONArray("Codigos");
					for(int s = 0; s < responseArray.length(); s ++)
					{
						JSONObject type = responseArray.getJSONObject(s);
						id = type.getInt("IdServer");
					}
					
					JSONObject type = jObject;
					
					cl.setNombre1(type.getString("Nombre1"));
					cl.setApellido1(type.getString("Apellido1"));
					cl.setTipoPersona(type.getString("TipoPersona"));
					if(cl.getTipoPersona().equals("N"))
					{
						cl.setApellido2(type.getString("Apellido2"));
						cl.setCorreoElectronico(type.getString("CorreoElectronico"));
						cl.setEstadoCivil(type.getString("EstadoCivil"));
						cl.setGenero(type.getString("Genero"));
						cl.setNombre2(type.getString("Nombre2"));
						String nombres = cl.getNombre1() + " " + cl.getNombre2();
						String apellidos = cl.getApellido1() + " " + cl.getApellido2();
						cl.setNombreCompleto(nombres.trim() + " " + apellidos.trim());
					}
					else
					{
						cl.setActividadEconomica(type.getString("ActividadEconomica"));
						cl.setCorreoElectronico(type.getString("CorreoElectronico"));
						cl.setPaginaWeb(type.getString("PaginaWeb"));
						cl.setRazonSocial(type.getString("RazonSocial"));
						cl.setNombreCompleto(cl.getNombre1());
						cl.setEstadoCivil(type.getString("EstadoCivil"));
						cl.setGenero(type.getString("Genero"));
					}
					
					cl.setIdCanal(type.getInt("IdCanal"));
					cl.setID(id);
					cl.setIdTipoIdentificacion(type.getInt("IdTipoIdentificacion"));
					cl.setIdentificacion(type.getString("Identificacion"));
					cl.setIdTipoCliente(type.getInt("IdTipoCliente"));
					//cl.setFechaNacimiento(Convert.getDateFromDotNetTicks(type.getLong("FechaNacimientoTicks")));
					
																
					JSONArray strs = type.getJSONArray("ClienteDirecciones");
					
					rp3.marketforce.models.ClienteDireccion.deleteClienteDireccionIdCliente(db, cl.getID());
					
					for(int j=0; j < strs.length(); j++){
						JSONObject str = strs.getJSONObject(j);
						rp3.marketforce.models.ClienteDireccion  clienteDir = new rp3.marketforce.models.ClienteDireccion();
						
						clienteDir.setDireccion(""+str.getString("Direccion"));							
						
						//clienteDir.setIdCiudad(str.getInt("IdCiudad"));
						clienteDir.setIdCliente(id);							
						clienteDir.setIdClienteDireccion(str.getInt("IdClienteDireccion"));
						if(!str.isNull("Latitud"))
							clienteDir.setLatitud(str.getDouble("Latitud"));
						if(!str.isNull("Longitud"))
							clienteDir.setLongitud(str.getDouble("Longitud"));
						clienteDir.setReferencia(""+str.getString("Referencia"));
						clienteDir.setTelefono1(""+str.getString("Telefono1"));
						clienteDir.setTelefono2(""+str.getString("Telefono2"));
						clienteDir.setTipoDireccion(""+str.getString("TipoDireccion"));
						
						if(str.getBoolean("EsPrincipal")){
							clienteDir.setEsPrincipal(true);
							cl.setDireccion(clienteDir.getDireccion());
							cl.setTelefono(clienteDir.getTelefono1());
						}								
						else
						   clienteDir.setEsPrincipal(false);
						
						rp3.marketforce.models.ClienteDireccion.insert(db, clienteDir);
					}
					
					strs = type.getJSONArray("ClienteContactos");
					
					rp3.marketforce.models.Contacto.deleteContactoIdCliente(db, cl.getID());
					List<rp3.marketforce.models.Contacto> contactos = new ArrayList<rp3.marketforce.models.Contacto>();
					
					for(int j=0; j < strs.length(); j++){
						JSONObject str = strs.getJSONObject(j);
						rp3.marketforce.models.Contacto  clienteCont = new rp3.marketforce.models.Contacto();
						
						clienteCont.setIdContacto(str.getLong("IdClienteContacto"));							
						
						clienteCont.setIdCliente(id);							
						clienteCont.setIdClienteDireccion(str.getInt("IdClienteDireccion"));
						clienteCont.setNombre(""+str.getString("Nombre"));
						clienteCont.setApellido(""+str.getString("Apellido"));
						clienteCont.setCargo(""+str.getString("Cargo"));
						clienteCont.setTelefono1(""+str.getString("Telefono1"));
						clienteCont.setTelefono2(""+str.getString("Telefono2"));
						clienteCont.setCorreo(""+str.getString("CorreoElectronico"));
						
						rp3.marketforce.models.Contacto.insert(db, clienteCont);
						contactos.add(clienteCont);
					}
					rp3.marketforce.models.Cliente.insert(db, cl);
					cl.setContactos(contactos);

					
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
			
			webService = new WebService("MartketForce","SetFotos");			
			
			JSONObject jFotos = new JSONObject();
			try
			{				
				jFotos.put("IdCliente", id);
				jFotos.put("IdContacto", "");
				jFotos.put("Nombre", cl.getNombre1() + "_" + cl.getApellido1() + "_" + id + ".jpg" );
				jFotos.put("Contenido", Utils.CroppedBitmapToBase64(jObject.getString("Foto")));
				
				webService.addParameter("clientefoto", jFotos);
				webService.addCurrentAuthToken();
				webService.invokeWebService();
				cl.setURLFoto(cl.getNombre1() + "_" + cl.getApellido1() + "_" + id + ".jpg");
				rp3.marketforce.models.Cliente.update(db, cl);
				
				for(int r = 0; r < cl.getContactos().size(); r ++)
				{
					jFotos = new JSONObject();
					webService = new WebService("MartketForce","SetFotos");
					jFotos.put("IdCliente", id);
					jFotos.put("IdContacto", cl.getContactos().get(r).getIdContacto());
					jFotos.put("Nombre", cl.getContactos().get(r).getNombre() + "_" + cl.getContactos().get(r).getApellido() + "_" + id + "_" + cl.getContactos().get(r).getIdContacto() + ".jpg" );
					jFotos.put("Contenido", Utils.CroppedBitmapToBase64(jObject.getString("Foto")));
					webService.addParameter("clientefoto", jFotos);
					webService.addCurrentAuthToken();
					webService.invokeWebService();
					rp3.marketforce.models.Contacto ct = cl.getContactos().get(r);
					ct.setURLFoto(cl.getContactos().get(r).getNombre() + "_" + cl.getContactos().get(r).getApellido() + "_" + id + "_" + cl.getContactos().get(r).getIdContacto() + ".jpg");
					rp3.marketforce.models.Contacto.update(db, ct);
				}
			} catch (HttpResponseException e) {
				if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
					return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
				return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
			} catch (Exception e) {
				return SyncAdapter.SYNC_EVENT_ERROR;
			}finally{
				webService.close();
			}		
				
				
			
			return SyncAdapter.SYNC_EVENT_SUCCESS;		
		}
		
	
}
