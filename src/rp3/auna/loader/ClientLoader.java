package rp3.auna.loader;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.auna.models.Cliente;
import android.content.Context;

public class ClientLoader extends
		rp3.content.SimpleObjectLoader<List<Cliente>> {

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
			if(withContacts)
				result = Cliente.getClientAndContacts(db);
			else
				result = Cliente.getCliente(db);
		else
			result  = Cliente.getClientSearch(db, search);
		
		
		return result;
	}
	
}