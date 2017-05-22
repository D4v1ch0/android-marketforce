package rp3.berlin.loader;

import android.content.Context;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.berlin.models.marcacion.Justificacion;

/**
 * Created by magno_000 on 22/06/2015.
 */
public class PermisoLoader extends
        rp3.content.SimpleObjectLoader<List<Justificacion>> {

    private DataBase db;

    public PermisoLoader(Context context, DataBase db) {
        super(context);
        this.db = db;
    }

    @Override
    public List<Justificacion> loadInBackground() {
        List<Justificacion> result = null;
            result  = Justificacion.getPermisosPendientesAprobar(db);
        return result;
    }

}