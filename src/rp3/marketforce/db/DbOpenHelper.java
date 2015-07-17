package rp3.marketforce.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.marcacion.Justificacion;

public class DbOpenHelper extends rp3.db.sqlite.DataBaseOpenHelper {

	public DbOpenHelper(Context context) {
		super(context);		
	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.e("Tag", "OnUpgrade SQLite");
        for(int i = oldVersion + 1; i <= newVersion; i++ )
        {
            switch (i)
            {
                case 2: UpgradeToVersion2(db); break;
            }
        }
    }

    public void UpgradeToVersion2(SQLiteDatabase database)
    {
        database.execSQL(QueryDir.getQuery(TO_VERSION + 2));
    }
}
