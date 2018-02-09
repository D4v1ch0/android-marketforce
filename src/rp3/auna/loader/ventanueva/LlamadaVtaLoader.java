package rp3.auna.loader.ventanueva;

import android.content.Context;
import android.util.Log;

import java.util.List;

import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.db.sqlite.DataBase;

/**
 * Created by Jesus Villa on 04/10/2017.
 */

public class LlamadaVtaLoader extends
        rp3.content.SimpleObjectLoader<List<LlamadaVta>> {

    private static final String TAG = LlamadaVtaLoader.class.getSimpleName();
    private DataBase db;
    private boolean flag;
    private String search;

    public LlamadaVtaLoader(Context context, DataBase db, boolean flag, String search) {
        super(context);
        this.db = db;
        this.flag = flag;
        this.search = search;
    }

    @Override
    public List<LlamadaVta> loadInBackground() {
        List<LlamadaVta> result = null;
            result = LlamadaVta.getLlamadasAll(db);
        if(result!=null){
            Log.d(TAG,"resultsize:"+result.size());
        }else{
            Log.d(TAG,"result==null...");
        }
        return result;
    }
}