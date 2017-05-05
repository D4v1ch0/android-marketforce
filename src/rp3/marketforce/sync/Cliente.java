package rp3.marketforce.sync;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.data.Constants;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.cliente.EstadoCuentaFragment;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.utils.Utils;
import rp3.sync.SyncAudit;
import rp3.util.Convert;

import android.os.Bundle;
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
						
						rp3.marketforce.models.Cliente cl = rp3.marketforce.models.Cliente.getClienteIDServer(db, type.getLong("IdCliente"), false);
						if(cl == null)
							cl = new rp3.marketforce.models.Cliente();
                        if(type.getString("Estado").equalsIgnoreCase(Contants.ESTADO_ELIMINADO)) {
							rp3.marketforce.models.Cliente.delete(db, cl);
						}
                        else
                        {
                            if(!type.isNull("Apellido1"))
                                cl.setApellido1(type.getString("Apellido1"));
                            else
                                cl.setApellido1("");
                            if(!type.isNull("Apellido2"))
                                cl.setApellido2(type.getString("Apellido2"));
                            else
                                cl.setApellido2("");
                            if(!type.isNull("EstadoCivil"))
                                cl.setEstadoCivil(type.getString("EstadoCivil"));
                            else
                                cl.setEstadoCivil("");

                            cl.setCalificacion(type.getInt("Calificacion"));

                            if(!type.isNull("CorreoElectronico"))
                                cl.setCorreoElectronico(type.getString("CorreoElectronico"));
                            else
                                cl.setCorreoElectronico("");

                            if(!type.isNull("Genero"))
                                cl.setGenero(type.getString("Genero"));
                            else
                                cl.setGenero("");

                            if(!type.isNull("IdCanal"))
                                cl.setIdCanal(type.getInt("IdCanal"));
                            else
                                cl.setIdCanal(0);

                            cl.setIdCliente(type.getLong("IdCliente"));
                            cl.setIdTipoIdentificacion(type.getInt("IdTipoIdentificacion"));
                            if(!type.isNull("Identificacion"))
                                cl.setIdentificacion(type.getString("Identificacion"));
                            else
                                cl.setIdentificacion("");

                            if(!type.isNull("IdTipoCliente"))
                                cl.setIdTipoCliente(type.getInt("IdTipoCliente"));
                            else
                                cl.setIdTipoCliente(0);

                            cl.setNombre1(type.getString("Nombre1"));
                            if(!type.isNull("Nombre2"))
                                cl.setNombre2(type.getString("Nombre2"));
                            else
                                cl.setNombre2("");

                            cl.setFechaNacimiento(Convert.getDateFromDotNetTicks(type.getLong("FechaNacimientoTicks")));
                            cl.setNombreCompleto(type.getString("NombresCompletos").trim().replace("null", ""));
                            cl.setURLFoto(type.getString("Foto"));
							if(!type.isNull("ExentoImpuesto"))
								cl.setExentoImpuesto(type.getBoolean("ExentoImpuesto"));
							else
								cl.setExentoImpuesto(false);
							if(!type.isNull("CiudadanoOro"))
								cl.setCiudadanoOro(type.getBoolean("CiudadanoOro"));
							else
								cl.setCiudadanoOro(false);

                            cl.setTipoPersona(type.getString("TipoPersona"));
                            if(!type.isNull("ActividadEconomica"))
                                cl.setActividadEconomica(type.getString("ActividadEconomica"));
                            else
                                cl.setActividadEconomica("");
                            if(!type.isNull("PaginaWeb"))
                                cl.setPaginaWeb(type.getString("PaginaWeb"));
                            else
                                cl.setPaginaWeb("");
                            if(!type.isNull("RazonSocial"))
                                cl.setRazonSocial(type.getString("RazonSocial"));
                            else
                                cl.setRazonSocial("");
							if(!type.isNull("Tarjeta"))
								cl.setTarjeta(type.getString("Tarjeta"));
							else
								cl.setTarjeta("");

							//Campos de Berlín
							if(!type.isNull("IdExterno"))
								cl.setIdExterno(type.getString("IdExterno"));
							else
								cl.setIdExterno("");
							if(!type.isNull("CanalPartner"))
								cl.setCanalPartner(type.getString("CanalPartner"));
							else
								cl.setCanalPartner("");
							if(!type.isNull("TipoPartner"))
								cl.setTipoPartner(type.getString("TipoPartner"));
							else
								cl.setTipoPartner("");
							if(!type.isNull("LimiteCredito"))
								cl.setLimiteCredito(type.getDouble("LimiteCredito"));
							else
								cl.setLimiteCredito(0);
							if(!type.isNull("ListaPrecio"))
								cl.setListPrecio(type.getString("ListaPrecio"));
							else
								cl.setListPrecio("");
							if(!type.isNull("Aviso"))
								cl.setAviso(type.getString("Aviso"));
							else
								cl.setAviso("");
							if(!type.isNull("IndiceSolvencia"))
								cl.setIndiceSolvencia(type.getString("IndiceSolvencia"));
							else
								cl.setIndiceSolvencia("");
							if(!type.isNull("Tarjeta"))
								cl.setCondicionPago(type.getString("CondicionPago"));
							else
								cl.setCondicionPago("");

                            JSONArray strs = type.getJSONArray("ClienteDirecciones");

                            rp3.marketforce.models.ClienteDireccion.deleteClienteDireccionIdCliente(db, cl.getIdCliente());

                            for (int j = 0; j < strs.length(); j++) {
                                JSONObject str = strs.getJSONObject(j);
                                rp3.marketforce.models.ClienteDireccion clienteDir = new rp3.marketforce.models.ClienteDireccion();

                                if(str.getString("Estado").equalsIgnoreCase(Contants.ESTADO_ELIMINADO))
                                {
                                    rp3.marketforce.models.ClienteDireccion toDelete = rp3.marketforce.models.ClienteDireccion.getClienteDireccionIdDireccion(db, str.getLong("IdCliente"), str.getInt("IdClienteDireccion"));
                                    if(toDelete != null)
                                    {
                                        rp3.marketforce.models.ClienteDireccion.delete(db, toDelete);
                                    }
                                }
                                else {
                                    clienteDir.setDireccion("" + str.getString("Direccion"));

                                    if (!str.isNull("IdCiudad"))
                                        clienteDir.setIdCiudad(str.getInt("IdCiudad"));
                                    clienteDir.setIdCliente(str.getLong("IdCliente"));
                                    clienteDir.setIdClienteDireccion(str.getInt("IdClienteDireccion"));
                                    if (!str.isNull("Latitud"))
                                        clienteDir.setLatitud(str.getDouble("Latitud"));
                                    if (!str.isNull("Longitud"))
                                        clienteDir.setLongitud(str.getDouble("Longitud"));

                                    if(!str.isNull("Referencia"))
                                        clienteDir.setReferencia(str.getString("Referencia"));
                                    else
                                        clienteDir.setReferencia("");

                                    if(!str.isNull("Telefono1"))
                                        clienteDir.setTelefono1(str.getString("Telefono1"));
                                    else
                                        clienteDir.setTelefono1("");

                                    if(!str.isNull("Telefono2"))
                                        clienteDir.setTelefono2(str.getString("Telefono2"));
                                    else
                                        clienteDir.setTelefono2("");

                                    clienteDir.setTipoDireccion("" + str.getString("TipoDireccion"));
                                    if(!str.isNull("CiudadDescripcion") && !str.getString("CiudadDescripcion").equalsIgnoreCase("null"))
                                        clienteDir.setCiudadDescripcion("" + str.getString("CiudadDescripcion"));

                                    if (str.getBoolean("EsPrincipal")) {
                                        clienteDir.setEsPrincipal(true);
                                        cl.setDireccion(clienteDir.getDireccion());
                                        cl.setTelefono(clienteDir.getTelefono1());
                                    } else
                                        clienteDir.setEsPrincipal(false);

                                    clienteDir.set_idCliente(0);
                                    rp3.marketforce.models.ClienteDireccion.insert(db, clienteDir);
                                }
                            }

                            strs = type.getJSONArray("ClienteContactos");

                            //rp3.marketforce.models.Contacto.deleteContactoIdCliente(db, cl.getIdCliente());

                            for (int j = 0; j < strs.length(); j++) {
                                JSONObject str = strs.getJSONObject(j);
								rp3.marketforce.models.Contacto clienteCont = rp3.marketforce.models.Contacto.getContactoId(db, str.getLong("IdClienteContacto"), str.getLong("IdCliente"));

                                if(str.getString("Estado").equalsIgnoreCase(Contants.ESTADO_ELIMINADO))
                                {
                                    if(clienteCont.getID() != 0)
                                    {
                                        rp3.marketforce.models.Contacto.delete(db, clienteCont);
                                    }
                                }
                                else {

                                    clienteCont.setIdContacto(str.getLong("IdClienteContacto"));

                                    clienteCont.setIdCliente(str.getLong("IdCliente"));
                                    clienteCont.setIdClienteDireccion(str.getInt("IdClienteDireccion"));
                                    clienteCont.setNombre("" + str.getString("Nombre"));
                                    clienteCont.setApellido("" + str.getString("Apellido"));
                                    clienteCont.setCargo("" + str.getString("Cargo"));
                                    if(!str.isNull("Telefono1"))
                                        clienteCont.setTelefono1(str.getString("Telefono1"));
                                    else
                                        clienteCont.setTelefono1("");
                                    if(!str.isNull("Telefono2"))
                                        clienteCont.setTelefono2(str.getString("Telefono2"));
                                    else
                                        clienteCont.setTelefono2("");
                                    if(!str.isNull("CorreoElectronico"))
                                        clienteCont.setCorreo(str.getString("CorreoElectronico"));
                                    else
                                        clienteCont.setCorreo("");

                                    if (!str.isNull("Foto"))
                                        clienteCont.setURLFoto("" + str.getString("Foto"));

									if(clienteCont.getID() != 0)
										rp3.marketforce.models.Contacto.update(db, clienteCont);
									else
										rp3.marketforce.models.Contacto.insert(db, clienteCont);
                                }
                            }
                            if (rp3.marketforce.models.Cliente.getClienteIDServer(db, cl.getIdCliente(), false) == null) {
                                rp3.marketforce.models.Cliente.insert(db, cl);
                            } else {
                                rp3.marketforce.models.Cliente.update(db, cl);
                            }
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

    public static int executeSyncDeletes(DataBase db){
        WebService webService = new WebService("MartketForce","GetIdClientes");

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
            List<Integer> listIds = new ArrayList<Integer>();

            for(int i=0; i < types.length(); i++){

                try {

                    listIds.add(types.getInt(i));

                } catch (JSONException e) {
                    Log.e("Entro","Error: "+e.toString());
                    return SyncAdapter.SYNC_EVENT_ERROR;
                }

            }
            rp3.marketforce.models.Cliente.deleteClientes(db, listIds);
        }finally{
            webService.close();
        }

        return SyncAdapter.SYNC_EVENT_SUCCESS;
    }
		
		public static int executeSyncCreate(DataBase db, long cliente){
			WebService webService = new WebService("MartketForce","CreateCliente");			
			
			int id = 0;
			rp3.marketforce.models.Cliente cl = rp3.marketforce.models.Cliente.getClienteID(db, cliente, true);
			JSONObject jObject = new JSONObject();
			JSONArray jArray = new JSONArray();
			try
			{
				jObject = new JSONObject();
				
				jObject.put("Apellido1", cl.getApellido1());
				jObject.put("Apellido2", cl.getApellido2());
				jObject.put("CorreoElectronico", cl.getCorreoElectronico());
				jObject.put("EstadoCivil", cl.getEstadoCivil());
				jObject.put("Genero", cl.getGenero());
				jObject.put("IdCanal", cl.getIdCanal());
				jObject.put("IdInterno", cl.getID());
				jObject.put("IdTipoIdentificacion", cl.getTipoIdentificacionId());
				jObject.put("Identificacion", cl.getIdentificacion());
				jObject.put("IdTipoCliente", cl.getIdTipoCliente());
				jObject.put("Nombre1", cl.getNombre1());
				jObject.put("Nombre2", cl.getNombre2());
				if(cl.getFechaNacimiento() != null && cl.getFechaNacimiento().getTime() != 0)
					jObject.put("FechaNacimientoTicks", Convert.getDotNetTicksFromDate(cl.getFechaNacimiento()));
				jObject.put("NombresCompletos", cl.getNombreCompleto());
				jObject.put("Foto", cl.getURLFoto());
				jObject.put("TipoPersona", cl.getTipoPersona());
				jObject.put("ActividadEconomica", cl.getActividadEconomica());
				jObject.put("PaginaWeb", cl.getPaginaWeb());
				jObject.put("RazonSocial", cl.getRazonSocial());
				jObject.put("ExentoImpuesto", cl.getExentoImpuesto());
				jObject.put("CiudadanoOro", cl.isCiudadanoOro());
				jObject.put("Tarjeta", cl.getTarjeta());
				
				JSONArray jArrayDirecciones = new JSONArray();
				for(int i = 0; i < cl.getClienteDirecciones().size(); i++)
				{
					JSONObject jObjectDir = new JSONObject();
					jObjectDir.put("Direccion", cl.getClienteDirecciones().get(i).getDireccion());
					jObjectDir.put("IdInterno", cl.getClienteDirecciones().get(i).getID());
					jObjectDir.put("Latitud", cl.getClienteDirecciones().get(i).getLatitud());
					jObjectDir.put("Longitud", cl.getClienteDirecciones().get(i).getLongitud());
					jObjectDir.put("Referencia", cl.getClienteDirecciones().get(i).getReferencia());
                    if(cl.getClienteDirecciones().get(i).getIdCiudad() == 0) {
                        jObjectDir.put("IdCiudad", "null");
                        jObjectDir.put("CiudadDescripcion", cl.getClienteDirecciones().get(i).getCiudadDescripcion());
                    }
                    else
                        jObjectDir.put("IdCiudad", cl.getClienteDirecciones().get(i).getIdCiudad());
                    //if(cl.getClienteDirecciones().get(i).getCiudadDescripcion() != null && !cl.getClienteDirecciones().get(i).getCiudadDescripcion().equalsIgnoreCase("null"))

					jObjectDir.put("Telefono1", cl.getClienteDirecciones().get(i).getTelefono1());
					jObjectDir.put("Telefono2", cl.getClienteDirecciones().get(i).getTelefono2());
					jObjectDir.put("TipoDireccion", cl.getClienteDirecciones().get(i).getTipoDireccion());
					jObjectDir.put("EsPrincipal", cl.getClienteDirecciones().get(i).getEsPrincipal());
					jArrayDirecciones.put(jObjectDir);
				}
				jObject.put("ClienteDirecciones", jArrayDirecciones);
				
				JSONArray jArrayContactos = new JSONArray();
				for(int i = 0; i < cl.getContactos().size(); i++)
				{
					JSONObject jObjectCont = new JSONObject();
					jObjectCont.put("IdInterno", cl.getContactos().get(i).getID());
					jObjectCont.put("IdClienteDireccion", cl.getContactos().get(i).getIdClienteDireccion());
					jObjectCont.put("Nombre", cl.getContactos().get(i).getNombre());
					jObjectCont.put("Apellido", cl.getContactos().get(i).getApellido());
					jObjectCont.put("Cargo", cl.getContactos().get(i).getCargo());
					jObjectCont.put("Telefono1", cl.getContactos().get(i).getTelefono1());
					jObjectCont.put("Telefono2", cl.getContactos().get(i).getTelefono2());
					jObjectCont.put("CorreoElectronico", cl.getContactos().get(i).getCorreo());
					jObjectCont.put("Foto", cl.getContactos().get(i).getURLFoto());
					jArrayContactos.put(jObjectCont);
				}
				jObject.put("ClienteContactos", jArrayContactos);
				
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
						
						cl.setIdCliente(id);
						rp3.marketforce.models.Cliente.update(db, cl);
						JSONArray direcciones = type.getJSONArray("Direcciones");
						for(int i = 0; i < direcciones.length(); i++)
						{
							JSONObject jObjectDir = direcciones.getJSONObject(i);
							ClienteDireccion cliDir = cl.getClienteDirecciones().get(i);
							cliDir.setIdCliente(id);
							cliDir.setIdClienteDireccion(jObjectDir.getInt("IdServer"));
							ClienteDireccion.update(db, cliDir);
						}
						JSONArray contactos = type.getJSONArray("Contactos");
						for(int i = 0; i < contactos.length(); i++)
						{
							JSONObject jObjectCont = contactos.getJSONObject(i);
							rp3.marketforce.models.Contacto cliCon = cl.getContactos().get(i);
							cliCon.setIdCliente(id);
							cliCon.setIdContacto(jObjectCont.getInt("IdServer"));
							rp3.marketforce.models.Contacto.update(db, cliCon);
						}
					}
										
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
				String foto = Utils.CroppedBitmapToBase64(jObject.getString("Foto"));
				if(foto != null)
				{
					jFotos.put("Contenido", foto);
					
					webService.addParameter("clientefoto", jFotos);
					webService.addCurrentAuthToken();
					webService.invokeWebService();
					String nom_foto = webService.getStringResponse();
					nom_foto = nom_foto.replaceAll("\"", "");
					cl.setURLFoto(nom_foto);
					rp3.marketforce.models.Cliente.update(db, cl);
				}
				
				for(int r = 0; r < cl.getContactos().size(); r ++)
				{
					jFotos = new JSONObject();
					webService = new WebService("MartketForce","SetFotos");
					jFotos.put("IdCliente", id);
					jFotos.put("IdContacto", cl.getContactos().get(r).getIdContacto());
					jFotos.put("Nombre", cl.getContactos().get(r).getNombre() + "_" + cl.getContactos().get(r).getApellido() + "_" + id + "_" + cl.getContactos().get(r).getIdContacto() + ".jpg" );
					foto = Utils.CroppedBitmapToBase64(cl.getContactos().get(r).getURLFoto());
					if(foto != null)
					{
						jFotos.put("Contenido", foto);
						webService.addParameter("clientefoto", jFotos);
						webService.addCurrentAuthToken();
						webService.invokeWebService();
						String nom_foto = webService.getStringResponse();
						rp3.marketforce.models.Contacto ct = cl.getContactos().get(r);
						nom_foto = nom_foto.replaceAll("\"", "");
						ct.setURLFoto(nom_foto);
						rp3.marketforce.models.Contacto.update(db, ct);
					}
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
		
		public static int executeSyncUpdateFull(DataBase db, long cliente){
			WebService webService = new WebService("MartketForce","UpdateClienteFull");			
			
			rp3.marketforce.models.Cliente cl = rp3.marketforce.models.Cliente.getClienteID(db, cliente, true);
			JSONObject jObject = new JSONObject();
			JSONArray jArray = new JSONArray();
			try
			{
				jObject = new JSONObject();
				
				jObject.put("Apellido1", cl.getApellido1());
				jObject.put("Apellido2", cl.getApellido2());
				jObject.put("CorreoElectronico", cl.getCorreoElectronico());
				jObject.put("EstadoCivil", cl.getEstadoCivil());
				jObject.put("Genero", cl.getGenero());
				jObject.put("IdCanal", cl.getIdCanal());
				jObject.put("IdInterno", cl.getID());
				jObject.put("IdCliente", cl.getIdCliente());
				jObject.put("IdTipoIdentificacion", cl.getTipoIdentificacionId());
				jObject.put("Identificacion", cl.getIdentificacion());
				jObject.put("IdTipoCliente", cl.getIdTipoCliente());
				jObject.put("Nombre1", cl.getNombre1());
				jObject.put("Nombre2", cl.getNombre2());
				if(cl.getFechaNacimiento() != null && cl.getFechaNacimiento().getTime() != 0)
					jObject.put("FechaNacimientoTicks", Convert.getDotNetTicksFromDate(cl.getFechaNacimiento()));
				jObject.put("NombresCompletos", cl.getNombreCompleto());
				jObject.put("Foto", cl.getURLFoto());
				jObject.put("TipoPersona", cl.getTipoPersona());
				jObject.put("ActividadEconomica", cl.getActividadEconomica());
				jObject.put("PaginaWeb", cl.getPaginaWeb());
				jObject.put("RazonSocial", cl.getRazonSocial());
				jObject.put("ExentoImpuesto", cl.getExentoImpuesto());
				jObject.put("CiudadanoOro", cl.isCiudadanoOro());
				jObject.put("Tarjeta", cl.getTarjeta());
				
				JSONArray jArrayDirecciones = new JSONArray();
				for(int i = 0; i < cl.getClienteDirecciones().size(); i++)
				{
					JSONObject jObjectDir = new JSONObject();
					jObjectDir.put("Direccion", cl.getClienteDirecciones().get(i).getDireccion());
					jObjectDir.put("IdInterno", cl.getClienteDirecciones().get(i).getID());
					jObjectDir.put("IdClienteDireccion", cl.getClienteDirecciones().get(i).getIdClienteDireccion());
					jObjectDir.put("Latitud", cl.getClienteDirecciones().get(i).getLatitud());
					jObjectDir.put("Longitud", cl.getClienteDirecciones().get(i).getLongitud());
					jObjectDir.put("Referencia", cl.getClienteDirecciones().get(i).getReferencia());
                    if(cl.getClienteDirecciones().get(i).getIdCiudad() == 0) {
                        jObjectDir.put("IdCiudad", "null");
                        jObjectDir.put("CiudadDescripcion", cl.getClienteDirecciones().get(i).getCiudadDescripcion());
                    }
                    else
                        jObjectDir.put("IdCiudad", cl.getClienteDirecciones().get(i).getIdCiudad());
					jObjectDir.put("Telefono1", cl.getClienteDirecciones().get(i).getTelefono1());
					jObjectDir.put("Telefono2", cl.getClienteDirecciones().get(i).getTelefono2());
					jObjectDir.put("TipoDireccion", cl.getClienteDirecciones().get(i).getTipoDireccion());
					jObjectDir.put("EsPrincipal", cl.getClienteDirecciones().get(i).getEsPrincipal());
					jArrayDirecciones.put(jObjectDir);
				}
				jObject.put("ClienteDirecciones", jArrayDirecciones);
				
				JSONArray jArrayContactos = new JSONArray();
				for(int i = 0; i < cl.getContactos().size(); i++)
				{
					JSONObject jObjectCont = new JSONObject();
					jObjectCont.put("IdInterno", cl.getContactos().get(i).getID());
					jObjectCont.put("IdClienteContacto", cl.getContactos().get(i).getIdContacto());
					jObjectCont.put("IdClienteDireccion", cl.getContactos().get(i).getIdClienteDireccion());
					jObjectCont.put("Nombre", cl.getContactos().get(i).getNombre());
					jObjectCont.put("Apellido", cl.getContactos().get(i).getApellido());
					jObjectCont.put("Cargo", cl.getContactos().get(i).getCargo());
					jObjectCont.put("Telefono1", cl.getContactos().get(i).getTelefono1());
					jObjectCont.put("Telefono2", cl.getContactos().get(i).getTelefono2());
					jObjectCont.put("CorreoElectronico", cl.getContactos().get(i).getCorreo());
					jObjectCont.put("Foto", cl.getContactos().get(i).getURLFoto());
					jObjectCont.put("Estado", "A");
					jObjectCont.put("EstadoTabla", Contants.GENERAL_TABLE_ESTADO);
					jArrayContactos.put(jObjectCont);
				}
				jObject.put("ClienteContactos", jArrayContactos);
				
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
						JSONArray direcciones = type.getJSONArray("Direcciones");
						for(int i = 0; i < direcciones.length(); i++)
						{
							JSONObject jObjectDir = direcciones.getJSONObject(i);
							ClienteDireccion cliDir = cl.getClienteDirecciones().get(i);
							if(cliDir.get_idCliente() == jObjectDir.getInt("IdInterno"))
							{
								cliDir.setIdCliente(cl.getIdCliente());
								cliDir.setIdClienteDireccion(jObjectDir.getInt("IdServer"));
								ClienteDireccion.update(db, cliDir);
							}
						}
						JSONArray contactos = type.getJSONArray("Contactos");
						for(int i = 0; i < contactos.length(); i++)
						{
							JSONObject jObjectCont = contactos.getJSONObject(i);
							rp3.marketforce.models.Contacto cliCon = cl.getContactos().get(i);
							if(cliCon.get_idCliente() == jObjectCont.getInt("IdInterno"))
							{
								cliCon.setIdCliente(cl.getIdCliente());
								cliCon.setIdContacto(jObjectCont.getInt("IdServer"));
								rp3.marketforce.models.Contacto.update(db, cliCon);
							}
						}
					}
										
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
				jFotos.put("IdCliente", cl.getIdCliente());
				jFotos.put("IdContacto", "");
				jFotos.put("Nombre", cl.getNombre1() + "_" + cl.getApellido1() + "_" + cl.getIdCliente() + ".jpg" );
				String foto = Utils.CroppedBitmapToBase64(jObject.getString("Foto"));
				if(foto != null)
				{
					jFotos.put("Contenido", foto);
					
					webService.addParameter("clientefoto", jFotos);
					webService.addCurrentAuthToken();
					webService.invokeWebService();
					String nom_foto = webService.getStringResponse();
					nom_foto = nom_foto.replaceAll("\"", "");
					cl.setURLFoto(nom_foto);
					rp3.marketforce.models.Cliente.update(db, cl);
				}
				
				for(int r = 0; r < cl.getContactos().size(); r ++)
				{
					jFotos = new JSONObject();
					webService = new WebService("MartketForce","SetFotos");
					jFotos.put("IdCliente", cl.getIdCliente());
					jFotos.put("IdContacto", cl.getContactos().get(r).getIdContacto());
					jFotos.put("Nombre", cl.getContactos().get(r).getNombre() + "_" + cl.getContactos().get(r).getApellido() + "_" + cl.getIdCliente() + "_" + cl.getContactos().get(r).getIdContacto() + ".jpg" );
					foto = Utils.CroppedBitmapToBase64(cl.getContactos().get(r).getURLFoto());
					if(foto != null)
					{
						jFotos.put("Contenido", foto);
						webService.addParameter("clientefoto", jFotos);
						webService.addCurrentAuthToken();
						webService.invokeWebService();
						String nom_foto = webService.getStringResponse();
						rp3.marketforce.models.Contacto ct = cl.getContactos().get(r);
						nom_foto = nom_foto.replaceAll("\"", "");
						ct.setURLFoto(nom_foto);
						rp3.marketforce.models.Contacto.update(db, ct);
					}
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
		
		public static int executeSyncPendientes(DataBase db){
			WebService webService = new WebService("MartketForce","UpdateClienteFull");			
			
			List<rp3.marketforce.models.Cliente> clientes = rp3.marketforce.models.Cliente.getClientePendientes(db, true);
			if(clientes.size() == 0)
				return SyncAdapter.SYNC_EVENT_SUCCESS;
			JSONObject jObject = new JSONObject();
			JSONArray jArray = new JSONArray();
			for(int s = 0; s < clientes.size(); s ++)
			{
				rp3.marketforce.models.Cliente cl = clientes.get(s);
				try
				{
					jObject = new JSONObject();
					
					jObject.put("Apellido1", cl.getApellido1());
					jObject.put("Apellido2", cl.getApellido2());
					jObject.put("CorreoElectronico", cl.getCorreoElectronico());
					jObject.put("EstadoCivil", cl.getEstadoCivil());
					jObject.put("Genero", cl.getGenero());
					jObject.put("IdCanal", cl.getIdCanal());
					jObject.put("IdInterno", cl.getID());
					jObject.put("IdCliente", cl.getIdCliente());
					jObject.put("IdTipoIdentificacion", cl.getTipoIdentificacionId());
					jObject.put("Identificacion", cl.getIdentificacion());
					jObject.put("IdTipoCliente", cl.getIdTipoCliente());
					jObject.put("Nombre1", cl.getNombre1());
					jObject.put("Nombre2", cl.getNombre2());
					if(cl.getFechaNacimiento() != null && cl.getFechaNacimiento().getTime() != 0)
						jObject.put("FechaNacimientoTicks", Convert.getDotNetTicksFromDate(cl.getFechaNacimiento()));
					jObject.put("NombresCompletos", cl.getNombreCompleto());
					jObject.put("Foto", cl.getURLFoto());
					jObject.put("TipoPersona", cl.getTipoPersona());
					jObject.put("ActividadEconomica", cl.getActividadEconomica());
					jObject.put("PaginaWeb", cl.getPaginaWeb());
					jObject.put("RazonSocial", cl.getRazonSocial());
					jObject.put("ExentoImpuesto", cl.getExentoImpuesto());
					jObject.put("CiudadanoOro", cl.isCiudadanoOro());
					jObject.put("Tarjeta", cl.getTarjeta());

					JSONArray jArrayDirecciones = new JSONArray();
					for(int i = 0; i < cl.getClienteDirecciones().size(); i++)
					{
						JSONObject jObjectDir = new JSONObject();
						jObjectDir.put("Direccion", cl.getClienteDirecciones().get(i).getDireccion());
						jObjectDir.put("IdInterno", cl.getClienteDirecciones().get(i).getID());
						jObjectDir.put("IdClienteDireccion", cl.getClienteDirecciones().get(i).getIdClienteDireccion());
						jObjectDir.put("Latitud", cl.getClienteDirecciones().get(i).getLatitud());
						jObjectDir.put("Longitud", cl.getClienteDirecciones().get(i).getLongitud());
						jObjectDir.put("Referencia", cl.getClienteDirecciones().get(i).getReferencia());
                        if(cl.getClienteDirecciones().get(i).getIdCiudad() == 0) {
                            jObjectDir.put("IdCiudad", "null");
                            jObjectDir.put("CiudadDescripcion", cl.getClienteDirecciones().get(i).getCiudadDescripcion());
                        }
                        else
                            jObjectDir.put("IdCiudad", cl.getClienteDirecciones().get(i).getIdCiudad());
						jObjectDir.put("Telefono1", cl.getClienteDirecciones().get(i).getTelefono1());
						jObjectDir.put("Telefono2", cl.getClienteDirecciones().get(i).getTelefono2());
						jObjectDir.put("TipoDireccion", cl.getClienteDirecciones().get(i).getTipoDireccion());
						jObjectDir.put("EsPrincipal", cl.getClienteDirecciones().get(i).getEsPrincipal());
						jArrayDirecciones.put(jObjectDir);
					}
					jObject.put("ClienteDirecciones", jArrayDirecciones);
					
					JSONArray jArrayContactos = new JSONArray();
					for(int i = 0; i < cl.getContactos().size(); i++)
					{
						JSONObject jObjectCont = new JSONObject();
						jObjectCont.put("IdInterno", cl.getContactos().get(i).getID());
						jObjectCont.put("IdClienteContacto", cl.getContactos().get(i).getIdContacto());
						jObjectCont.put("IdClienteDireccion", cl.getContactos().get(i).getIdClienteDireccion());
						jObjectCont.put("Nombre", cl.getContactos().get(i).getNombre());
						jObjectCont.put("Apellido", cl.getContactos().get(i).getApellido());
						jObjectCont.put("Cargo", cl.getContactos().get(i).getCargo());
						jObjectCont.put("Telefono1", cl.getContactos().get(i).getTelefono1());
						jObjectCont.put("Telefono2", cl.getContactos().get(i).getTelefono2());
						jObjectCont.put("CorreoElectronico", cl.getContactos().get(i).getCorreo());
						jObjectCont.put("Foto", cl.getContactos().get(i).getURLFoto());
						jObjectCont.put("Estado", "A");
						jObjectCont.put("EstadoTabla", Contants.GENERAL_TABLE_ESTADO);
						jArrayContactos.put(jObjectCont);
					}
					jObject.put("ClienteContactos", jArrayContactos);
					
					jArray.put(jObject);
				}
				catch(Exception ex)
				{
					
				}
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
						for(rp3.marketforce.models.Cliente cl : clientes)
						{
							if(cl.getIdCliente() == type.getInt("IdServer"))
							{
								JSONArray direcciones = type.getJSONArray("Direcciones");
								cl.setPendiente(false);
								rp3.marketforce.models.Cliente.update(db, cl);
								for(int i = 0; i < direcciones.length(); i++)
								{
									JSONObject jObjectDir = direcciones.getJSONObject(i);
									ClienteDireccion cliDir = cl.getClienteDirecciones().get(i);
									if(cliDir.get_idCliente() == jObjectDir.getInt("IdInterno"))
									{
										cliDir.setIdCliente(cl.getIdCliente());
										cliDir.setIdClienteDireccion(jObjectDir.getInt("IdServer"));
										ClienteDireccion.update(db, cliDir);
									}
								}
								JSONArray contactos = type.getJSONArray("Contactos");
								for(int i = 0; i < contactos.length(); i++)
								{
									JSONObject jObjectCont = contactos.getJSONObject(i);
									rp3.marketforce.models.Contacto cliCon = cl.getContactos().get(i);
									if(cliCon.get_idCliente() == jObjectCont.getInt("IdInterno"))
									{
										cliCon.setIdCliente(cl.getIdCliente());
										cliCon.setIdContacto(jObjectCont.getInt("IdServer"));
										rp3.marketforce.models.Contacto.update(db, cliCon);
									}
								}
							}
						}
					}

                    webService = new WebService("MartketForce","SetFotos");

                    for(int s = 0; s < clientes.size(); s ++) {
                        rp3.marketforce.models.Cliente cl = clientes.get(s);
                        JSONObject jFotos = new JSONObject();
                        try {
                            jFotos.put("IdCliente", cl.getIdCliente());
                            jFotos.put("IdContacto", "");
                            jFotos.put("Nombre", cl.getNombre1() + "_" + cl.getApellido1() + "_" + cl.getIdCliente() + ".jpg");
                            String foto = Utils.CroppedBitmapToBase64(jObject.getString("Foto"));
                            if (foto != null) {
                                jFotos.put("Contenido", foto);

                                webService.addParameter("clientefoto", jFotos);
                                webService.addCurrentAuthToken();
                                webService.invokeWebService();
                                String nom_foto = webService.getStringResponse();
                                nom_foto = nom_foto.replaceAll("\"", "");
                                cl.setURLFoto(nom_foto);
                                rp3.marketforce.models.Cliente.update(db, cl);
                            }

                            for (int r = 0; r < cl.getContactos().size(); r++) {
                                jFotos = new JSONObject();
                                webService = new WebService("MartketForce", "SetFotos");
                                jFotos.put("IdCliente", cl.getIdCliente());
                                jFotos.put("IdContacto", cl.getContactos().get(r).getIdContacto());
                                jFotos.put("Nombre", cl.getContactos().get(r).getNombre() + "_" + cl.getContactos().get(r).getApellido() + "_" + cl.getIdCliente() + "_" + cl.getContactos().get(r).getIdContacto() + ".jpg");
                                foto = Utils.CroppedBitmapToBase64(cl.getContactos().get(r).getURLFoto());
                                if (foto != null) {
                                    jFotos.put("Contenido", foto);
                                    webService.addParameter("clientefoto", jFotos);
                                    webService.addCurrentAuthToken();
                                    webService.invokeWebService();
                                    String nom_foto = webService.getStringResponse();
                                    rp3.marketforce.models.Contacto ct = cl.getContactos().get(r);
                                    nom_foto = nom_foto.replaceAll("\"", "");
                                    ct.setURLFoto(nom_foto);
                                    rp3.marketforce.models.Contacto.update(db, ct);
                                }
                            }
                        } catch (HttpResponseException e) {
                            if (e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
                                return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
                            return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
                        } catch (Exception e) {
                            return SyncAdapter.SYNC_EVENT_ERROR;
                        } finally {
                            webService.close();
                        }
                    }

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
		
		public static int executeSyncInserts(DataBase db){
			WebService webService = new WebService("MartketForce","CreateCliente");			
			
			int id = 0;
			List<rp3.marketforce.models.Cliente> clientes = rp3.marketforce.models.Cliente.getClienteInserts(db, true);
			List<rp3.marketforce.models.Cliente> clientesConId = new ArrayList<rp3.marketforce.models.Cliente>();
			if(clientes.size() == 0)
				return SyncAdapter.SYNC_EVENT_SUCCESS;
			JSONObject jObject = new JSONObject();
			JSONArray jArray = new JSONArray();
			for(int s = 0 ; s < clientes.size(); s ++)
			{
				rp3.marketforce.models.Cliente cl = clientes.get(s);
				try
				{
					jObject = new JSONObject();

					jObject.put("Apellido1", cl.getApellido1());
					jObject.put("Apellido2", cl.getApellido2());
					jObject.put("CorreoElectronico", cl.getCorreoElectronico());
					jObject.put("EstadoCivil", cl.getEstadoCivil());
					jObject.put("Genero", cl.getGenero());
					jObject.put("IdCanal", cl.getIdCanal());
					jObject.put("IdInterno", cl.getID());
					jObject.put("IdTipoIdentificacion", cl.getTipoIdentificacionId());
					jObject.put("Identificacion", cl.getIdentificacion());
					jObject.put("IdTipoCliente", cl.getIdTipoCliente());
					jObject.put("Nombre1", cl.getNombre1());
					jObject.put("Nombre2", cl.getNombre2());
					if(cl.getFechaNacimiento() != null && cl.getFechaNacimiento().getTime() != 0)
						jObject.put("FechaNacimientoTicks", Convert.getDotNetTicksFromDate(cl.getFechaNacimiento()));
					jObject.put("NombresCompletos", cl.getNombreCompleto());
					jObject.put("Foto", cl.getURLFoto());
					jObject.put("TipoPersona", cl.getTipoPersona());
					jObject.put("ActividadEconomica", cl.getActividadEconomica());
					jObject.put("PaginaWeb", cl.getPaginaWeb());
					jObject.put("RazonSocial", cl.getRazonSocial());
					jObject.put("ExentoImpuesto", cl.getExentoImpuesto());
					jObject.put("CiudadanoOro", cl.isCiudadanoOro());
					jObject.put("Tarjeta", cl.getTarjeta());
					
					JSONArray jArrayDirecciones = new JSONArray();
					for(int i = 0; i < cl.getClienteDirecciones().size(); i++)
					{
						JSONObject jObjectDir = new JSONObject();
						jObjectDir.put("Direccion", cl.getClienteDirecciones().get(i).getDireccion());
						jObjectDir.put("IdInterno", cl.getClienteDirecciones().get(i).getID());
						jObjectDir.put("Latitud", cl.getClienteDirecciones().get(i).getLatitud());
						jObjectDir.put("Longitud", cl.getClienteDirecciones().get(i).getLongitud());
						jObjectDir.put("Referencia", cl.getClienteDirecciones().get(i).getReferencia());
                        if(cl.getClienteDirecciones().get(i).getIdCiudad() == 0) {
                            jObjectDir.put("IdCiudad", "null");
                            jObjectDir.put("CiudadDescripcion", cl.getClienteDirecciones().get(i).getCiudadDescripcion());
                        }
                        else
                            jObjectDir.put("IdCiudad", cl.getClienteDirecciones().get(i).getIdCiudad());

						jObjectDir.put("Telefono1", cl.getClienteDirecciones().get(i).getTelefono1());
						jObjectDir.put("Telefono2", cl.getClienteDirecciones().get(i).getTelefono2());
						jObjectDir.put("TipoDireccion", cl.getClienteDirecciones().get(i).getTipoDireccion());
						jObjectDir.put("EsPrincipal", cl.getClienteDirecciones().get(i).getEsPrincipal());
						jArrayDirecciones.put(jObjectDir);
					}
					jObject.put("ClienteDirecciones", jArrayDirecciones);
					
					JSONArray jArrayContactos = new JSONArray();
					for(int i = 0; i < cl.getContactos().size(); i++)
					{
						JSONObject jObjectCont = new JSONObject();
						jObjectCont.put("IdInterno", cl.getContactos().get(i).getID());
						jObjectCont.put("IdClienteDireccion", cl.getContactos().get(i).getIdClienteDireccion());
						jObjectCont.put("Nombre", cl.getContactos().get(i).getNombre());
						jObjectCont.put("Apellido", cl.getContactos().get(i).getApellido());
						jObjectCont.put("Cargo", cl.getContactos().get(i).getCargo());
						jObjectCont.put("Telefono1", cl.getContactos().get(i).getTelefono1());
						jObjectCont.put("Telefono2", cl.getContactos().get(i).getTelefono2());
						jObjectCont.put("CorreoElectronico", cl.getContactos().get(i).getCorreo());
						jObjectCont.put("Foto", cl.getContactos().get(i).getURLFoto());
						jArrayContactos.put(jObjectCont);
					}
					jObject.put("ClienteContactos", jArrayContactos);
					
					jArray.put(jObject);
				}
				catch(Exception ex)
				{
					
				}
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
						for(rp3.marketforce.models.Cliente cl : clientes)
						{
							if(cl.getID() == type.getInt("IdInterno"))
							{
								id = type.getInt("IdServer");
								
								cl.setIdCliente(id);
								cl.setPendiente(false);
								rp3.marketforce.models.Cliente.update(db, cl);
								List<rp3.marketforce.models.Agenda> agds = rp3.marketforce.models.Agenda.getAgendaClienteInterno(db, cl.getID());
								for(rp3.marketforce.models.Agenda agd : agds)
								{
									agd.setIdCliente(id);
									agd.setIdClienteDireccion(1);
									rp3.marketforce.models.Agenda.update(db, agd);
								}
								clientesConId.add(cl);
								JSONArray direcciones = type.getJSONArray("Direcciones");
								for(int i = 0; i < direcciones.length(); i++)
								{
									JSONObject jObjectDir = direcciones.getJSONObject(i);
									ClienteDireccion cliDir = cl.getClienteDirecciones().get(i);
									cliDir.setIdCliente(id);
									cliDir.setIdClienteDireccion(jObjectDir.getInt("IdServer"));
									ClienteDireccion.update(db, cliDir);
								}
								JSONArray contactos = type.getJSONArray("Contactos");
								for(int i = 0; i < contactos.length(); i++)
								{
									JSONObject jObjectCont = contactos.getJSONObject(i);
									rp3.marketforce.models.Contacto cliCon = cl.getContactos().get(i);
									cliCon.setIdCliente(id);
									cliCon.setIdContacto(jObjectCont.getInt("IdServer"));
									rp3.marketforce.models.Contacto.update(db, cliCon);
								}
							}
						}
					}
										
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
			
			for(int i = 0; i < clientesConId.size(); i ++)
			{
				rp3.marketforce.models.Cliente cl = clientesConId.get(i);
				webService = new WebService("MartketForce","SetFotos");			
				
				JSONObject jFotos = new JSONObject();
				try
				{				
					jFotos.put("IdCliente", cl.getIdCliente());
					jFotos.put("IdContacto", "");
					jFotos.put("Nombre", cl.getNombre1() + "_" + cl.getApellido1() + "_" + cl.getIdCliente() + ".jpg" );
					String foto = Utils.CroppedBitmapToBase64(cl.getURLFoto());
					if(foto != null)
					{
						jFotos.put("Contenido", foto);
						
						webService.addParameter("clientefoto", jFotos);
						webService.addCurrentAuthToken();
						webService.invokeWebService();
						String nom_foto = webService.getStringResponse();
						nom_foto = nom_foto.replaceAll("\"", "");
						cl.setURLFoto(nom_foto);
						rp3.marketforce.models.Cliente.update(db, cl);
					}
					
					for(int r = 0; r < cl.getContactos().size(); r ++)
					{
						jFotos = new JSONObject();
						webService = new WebService("MartketForce","SetFotos");
						jFotos.put("IdCliente", cl.getIdCliente());
						jFotos.put("IdContacto", cl.getContactos().get(r).getIdContacto());
						jFotos.put("Nombre", cl.getContactos().get(r).getNombre() + "_" + cl.getContactos().get(r).getApellido() + "_" + cl.getIdCliente() + "_" + cl.getContactos().get(r).getIdContacto() + ".jpg" );
						foto = Utils.CroppedBitmapToBase64(cl.getContactos().get(r).getURLFoto());
						if(foto != null)
						{
							jFotos.put("Contenido", foto);
							webService.addParameter("clientefoto", jFotos);
							webService.addCurrentAuthToken();
							webService.invokeWebService();
							String nom_foto = webService.getStringResponse();
							rp3.marketforce.models.Contacto ct = cl.getContactos().get(r);
							nom_foto = nom_foto.replaceAll("\"", "");
							ct.setURLFoto(nom_foto);
							rp3.marketforce.models.Contacto.update(db, ct);
						}
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
			}
			
			return SyncAdapter.SYNC_EVENT_SUCCESS;		
		}


	public static Bundle executeSyncEstadoCuenta(String cliente){
		Bundle resp = new Bundle();
		WebService webService = new WebService("MartketForce","GetEstadoCuenta");
		try
		{
			cliente = cliente.replace(" ", "%20");
			webService.addParameter("@cliente", cliente);
			webService.addCurrentAuthToken();

			try {
				webService.invokeWebService();
			} catch (HttpResponseException e) {
				if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED) {
					resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE, rp3.content.SyncAdapter.SYNC_EVENT_AUTH_ERROR);
				}
				resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_HTTP_ERROR);
				return resp;
			} catch (Exception e) {
				resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_ERROR);
				return resp;
			}

			JSONArray types = webService.getJSONArrayResponse();
			if(types != null)
				resp.putString(EstadoCuentaFragment.ARG_CLIENTE, types.toString());
			else
				resp.putString(EstadoCuentaFragment.ARG_CLIENTE, "");
			resp.putInt(rp3.content.SyncAdapter.ARG_SYNC_TYPE,rp3.content.SyncAdapter.SYNC_EVENT_SUCCESS);
		}finally{
			webService.close();
		}
		return resp;
	}
}
