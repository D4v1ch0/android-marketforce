package rp3.marketforce.loader;

import android.content.Context;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.oportunidad.Oportunidad;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadLoader extends
        rp3.content.SimpleObjectLoader<List<Oportunidad>> {

    private DataBase db;
    private boolean flag;
    private String search;
    private boolean withFilter;

    public OportunidadLoader(Context context, DataBase db, boolean flag, String search, boolean withFilter) {
        super(context);
        this.db = db;
        this.flag = flag;
        this.search = search;
        this.withFilter = withFilter;
    }

    @Override
    public List<Oportunidad> loadInBackground() {
        List<Oportunidad> result = null;
            result = Oportunidad.getAgenda(db);


        return result;
    }
}