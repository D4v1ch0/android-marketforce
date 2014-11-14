package rp3.marketforce.loader;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.Agenda;
import android.content.Context;

public class RutasLoader extends
		rp3.content.SimpleObjectLoader<List<Agenda>> {

	private DataBase db;
	private boolean flag;
	private String search;

	public RutasLoader(Context context, DataBase db,boolean flag,String search) {
		super(context);
		this.db = db;
		this.flag = flag;
		this.search = search;
	}

	@Override
	public List<Agenda> loadInBackground() {
		List<Agenda> result = null;
		
		if(flag || search.equalsIgnoreCase(""))
		   result = Agenda.getAgenda(db);
		else
			result = Agenda.getAgendaSearch(db, search);
		
		return result;
	}
	
}