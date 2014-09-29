package rp3.marketforce.sync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.ClienteDireccion;
import rp3.util.Convert;

public class ClienteActualizacion {

	public final static String ARG_CLIENTE_ID = "idcliente";
	
	public static int executeSync(DataBase db, long id){
		WebService webService = new WebService("MartketForce","UpdateCliente");
		
		try{
			webService.addCurrentAuthToken();
			
			JSONObject data = new JSONObject();
			
			rp3.marketforce.models.Cliente cliente = rp3.marketforce.models.Cliente.getClienteID(db, id, true);
			try {
				
				data.put("IdCliente", cliente.getID());
				data.put("CorreoElectronico", cliente.getCorreoElectronico());
				data.put("EstadoCivil", cliente.getEstadoCivil());
				data.put("FechaNacimientoTicks", Convert.getDotNetTicksFromDate(cliente.getFechaNacimiento()));
				
				JSONArray direcciones = new JSONArray();
				for(ClienteDireccion direccion: cliente.getClienteDirecciones()){
					JSONObject dataDireccion = new JSONObject();
					dataDireccion.put("IdCliente", direccion.getIdCliente());
					dataDireccion.put("IdClienteDireccion", direccion.getIdClienteDireccion() );
					
					dataDireccion.put("Direccion", direccion.getDireccion());
					dataDireccion.put("Telefono1", direccion.getTelefono1());
					dataDireccion.put("Telefono2", direccion.getTelefono2());
					dataDireccion.put("Referencia", direccion.getReferencia());
					dataDireccion.put("Latitud", direccion.getLatitud());
					dataDireccion.put("Longitud", direccion.getLongitud());
					
					direcciones.put(dataDireccion);
				}
				
				data.put("ClienteDirecciones", direcciones);
				
				webService.addParameter("cliente", data);
				
				
				try {
					webService.invokeWebService();	
				} catch (HttpResponseException e) {
					if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
						return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
					return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
				} catch (Exception e) {
					return SyncAdapter.SYNC_EVENT_ERROR;
				};
				
			} catch (JSONException e) {
				return SyncAdapter.SYNC_EVENT_ERROR;
			}
			
			
		}finally{
			webService.close();
		}
		
		return SyncAdapter.SYNC_EVENT_SUCCESS;		
	}
	
}
