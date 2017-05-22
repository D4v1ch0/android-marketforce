package rp3.berlin.loader;

import android.content.Context;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.berlin.models.oportunidad.Oportunidad;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class OportunidadLoader extends
        rp3.content.SimpleObjectLoader<List<Oportunidad>> {

    private DataBase db;
    private boolean flag;
    private String search;

    public OportunidadLoader(Context context, DataBase db, boolean flag, String search) {
        super(context);
        this.db = db;
        this.flag = flag;
        this.search = search;
    }

    @Override
    public List<Oportunidad> loadInBackground() {
        List<Oportunidad> result = null;

        if(search == null || search.length() == 0)
            result = Oportunidad.getOportunidades(db);
        else
            result = Oportunidad.getOportunidadesSearch(db, search);


        return result;
    }
}