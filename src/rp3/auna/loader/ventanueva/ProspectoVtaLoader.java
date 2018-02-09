package rp3.auna.loader.ventanueva;

import android.content.Context;

import java.util.List;

import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.db.sqlite.DataBase;

/**
 * Created by Jesus Villa on 09/10/2017.
 */

public class ProspectoVtaLoader extends
        rp3.content.SimpleObjectLoader<List<ProspectoVtaDb>> {

    private DataBase db;

    public ProspectoVtaLoader(Context context, DataBase db) {
        super(context);
        this.db = db;
    }

    @Override
    public List<ProspectoVtaDb> loadInBackground() {
        List<ProspectoVtaDb> result = null;


            result = ProspectoVtaDb.getAll(db);

        return result;
    }
}
