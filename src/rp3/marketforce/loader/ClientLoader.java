package rp3.marketforce.loader;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.Cliente;
import android.content.Context;

public class ClientLoader extends
		rp3.content.SimpleObjectLoader<List<Cliente>> {

	private DataBase db;
	private boolean flag;
	private String search;

	public ClientLoader(Context context, DataBase db, boolean flag, String search) {
		super(context);
		this.db = db;
		this.flag = flag;
		this.search = search;
	}

	@Override
	public List<Cliente> loadInBackground() {
		List<Cliente> result = null;
		
		if(flag)
		   result = Cliente.getCliente(db, false);
		else
			result  = Cliente.getClientSearch(db, search);
		
		
		return result;
	}
	
}