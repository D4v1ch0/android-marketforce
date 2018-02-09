package rp3.auna.loader.ventanueva;

import android.content.Context;
import android.util.Log;

import java.util.List;

import rp3.auna.models.ventanueva.VisitaVta;
import rp3.db.sqlite.DataBase;

/**
 * Created by Jesus Villa on 03/12/2017.
 */

public class VisitaVtaLoader extends
        rp3.content.SimpleObjectLoader<List<VisitaVta>> {

    private static final String TAG = VisitaVtaLoader.class.getSimpleName();
    private DataBase db;
    private boolean flag;
    private String search;

    public VisitaVtaLoader(Context context, DataBase db, boolean flag, String search) {
        super(context);
        this.db = db;
        this.flag = flag;
        this.search = search;
    }

    @Override
    public List<VisitaVta> loadInBackground() {
        List<VisitaVta> result = null;
        result = VisitaVta.getAll(db);
        if(result!=null){
            Log.d(TAG,"resultsize:"+result.size());
        }else{
            Log.d(TAG,"result==null...");
        }
        return result;
    }
}