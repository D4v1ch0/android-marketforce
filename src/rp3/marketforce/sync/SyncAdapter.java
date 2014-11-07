package rp3.marketforce.sync;

import rp3.db.sqlite.DataBase;
import rp3.sync.SyncAudit;
import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

public class SyncAdapter extends rp3.content.SyncAdapter {
	
	public static String SYNC_TYPE_GENERAL = "general";
	public static String SYNC_TYPE_ENVIAR_UBICACION = "sendlocation";
	public static String SYNC_TYPE_CLIENTE_UPDATE = "clienteupdate";
	
	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);		
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {		
		super.onPerformSync(account, extras, authority, provider, syncResult);	
		
//		android.os.Debug.waitForDebugger();
		String syncType = extras.getString(ARG_SYNC_TYPE);
		
		DataBase db = null;		
		int result = 0;
		
		try{
			db = DataBase.newDataBase(rp3.marketforce.db.DbOpenHelper.class);
			
			if(syncType == null || syncType.equals(SYNC_TYPE_GENERAL)){								
				db.beginTransaction();
				
				result = rp3.sync.GeopoliticalStructure.executeSync(db);				
				addDefaultMessage(result);
				
				if(result == SYNC_EVENT_SUCCESS){
					result = rp3.sync.GeneralValue.executeSync(db);
					addDefaultMessage(result);
				}
				
				if(result == SYNC_EVENT_SUCCESS){
					result = rp3.sync.IdentificationType.executeSync(db);
					addDefaultMessage(result);
				}
				
				if(result == SYNC_EVENT_SUCCESS){
					result = rp3.marketforce.sync.Cliente.executeSync(db);				
					addDefaultMessage(result);
					if(result == SYNC_EVENT_SUCCESS){
						SyncAudit.insert(SYNC_TYPE_CLIENTE_UPDATE, SYNC_EVENT_SUCCESS);
					}
				}
				
				if(result == SYNC_EVENT_SUCCESS){
					result = rp3.marketforce.sync.Rutas.executeSync(db,null,null);				
					addDefaultMessage(result);
				}
				
				if(result == SYNC_EVENT_SUCCESS){
					result = rp3.marketforce.sync.Canal.executeSync(db);				
					addDefaultMessage(result);
				}

				if(result == SYNC_EVENT_SUCCESS){
					result = rp3.marketforce.sync.TipoCliente.executeSync(db);				
					addDefaultMessage(result);
				}
				/*
				 * Se comenta carga de fotos ya que se la hara mediante un lazy loader.
				 * Para esto se cargara tambien en el modelo Cliente la url de la foto para poder cargarla
				 * */
				
				//if(result == SYNC_EVENT_SUCCESS){
				//	result = rp3.marketforce.sync.ClienteFoto.executeSync(db,null);				
				//	addDefaultMessage(result);
				//}
								
				db.commitTransaction();
				
			}else if(syncType.equals(SYNC_TYPE_ENVIAR_UBICACION)){
				try{
					double latitud = extras.getDouble(EnviarUbicacion.ARG_LATITUD);
					double longitud = extras.getDouble(EnviarUbicacion.ARG_LONGITUD);
					
					EnviarUbicacion.executeSync(longitud, latitud);				
					
					
				}catch(Exception e){
					Log.e("Sync Adapter", e.getMessage());
				}
			}else if(syncType.equals(SYNC_TYPE_CLIENTE_UPDATE)){
				long id = extras.getLong(ClienteActualizacion.ARG_CLIENTE_ID);
				result = ClienteActualizacion.executeSync(db, id);
				addDefaultMessage(result);
			}
			
			SyncAudit.insert(syncType, result);
				
		}catch (Exception e) {			
			Log.e(TAG, "E: " + e.getMessage());
			addDefaultMessage(SYNC_EVENT_ERROR);
			SyncAudit.insert(syncType, SYNC_EVENT_ERROR);
		} 
		finally{			
			db.endTransaction();
			db.close();
			
			notifySyncFinish();
		}								
	}
	
}