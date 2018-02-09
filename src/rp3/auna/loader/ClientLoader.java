package rp3.auna.loader;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.auna.models.Cliente;
import android.content.Context;
import android.util.Log;

public class ClientLoader extends
		rp3.content.SimpleObjectLoader<List<Cliente>> {

	private static final String TAG = ClientLoader.class.getSimpleName();
	private DataBase db;
	private boolean flag;
	private String search;
	private boolean withContacts;

	public ClientLoader(Context context, DataBase db, boolean flag, String search, boolean withContacts) {
		super(context);
		this.db = db;
		this.flag = flag;
		this.search = search;
		this.withContacts = withContacts;
	}

	@Override
	public List<Cliente> loadInBackground() {
		List<Cliente> result = null;
		
		if(flag)
			if(withContacts) {
				Log.d(TAG, "Con contactos...");
				result = Cliente.getClientAndContacts(db);
                Log.d(TAG,"Cantidad con contactos:"+result.size());
			}
			else {
				Log.d(TAG, "Sin contactos...");
				result = Cliente.getCliente(db);
				for(Cliente cliente:result){
					Log.d(TAG,cliente.toString());
				}
                Log.d(TAG,"Cantidad sin contactos:"+result.size());
			}
		else
			result  = Cliente.getClientSearch(db, search);
		
		
		return result;
	}
	
}