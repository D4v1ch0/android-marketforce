package rp3.auna.sync;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import rp3.connection.HttpConnection;
import rp3.connection.WebService;
import rp3.content.SyncAdapter;
import rp3.db.sqlite.DataBase;
import rp3.util.BitmapUtils;
import rp3.util.FileUtils;

public class ClienteFoto {

	public final static String ARG_CLIENTE_ID = "idCliente";
	
	public static int executeSync(DataBase db, Long idCliente){
		WebService webService = new WebService("MartketForce","GetFoto");				
		try
		{			
			
			
			List<Long> clientes = null;
			if(idCliente == null){
				clientes = rp3.auna.models.Cliente.getIDSCliente(db);
			}else{
				clientes = new ArrayList<Long>();
				clientes.add(idCliente);
			}
			
			for(Long id: clientes){
				String fileName = rp3.auna.models.Cliente.getFotoFileNameFormat(id);
				
				FileUtils.deleteFromInternalStorage(fileName);
			}
			
			webService.addCurrentAuthToken();
			
			for(Long id: clientes){				
				webService.setParameter("@idcliente", id);
				String fileName = rp3.auna.models.Cliente.getFotoFileNameFormat(id);
				try {
					webService.invokeWebService();
					
				} catch (HttpResponseException e) {
					if(e.getStatusCode() == HttpConnection.HTTP_STATUS_UNAUTHORIZED)
						return SyncAdapter.SYNC_EVENT_AUTH_ERROR;
					return SyncAdapter.SYNC_EVENT_HTTP_ERROR;
				} catch (Exception e) {
					return SyncAdapter.SYNC_EVENT_ERROR;
				}
								
				FileUtils.saveInternalStorage(
						fileName,
						BitmapUtils.decodeBitmapFromBase64(webService.getStringResponse()));
			}
			
			
			
		}finally{
			webService.close();
		}
		
		return SyncAdapter.SYNC_EVENT_SUCCESS;
	}
}
