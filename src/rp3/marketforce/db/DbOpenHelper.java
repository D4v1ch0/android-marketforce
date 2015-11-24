package rp3.marketforce.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import rp3.db.QueryDir;
import rp3.sync.SyncAudit;
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
				case 2: UpgradeToVersion(db, i); break;
				case 3: UpgradeToVersion3(db); break;
				case 4: UpgradeToVersion4(db); break;
				case 5: UpgradeToVersion5(db); break;
				case 6: UpgradeToVersion(db, i); break;
				case 7: UpgradeToVersion(db, i); break;
			}
		}
	}

	public void UpgradeToVersion(SQLiteDatabase database, int version)
	{
		database.execSQL(QueryDir.getQuery(TO_VERSION + version));
	}

	public void UpgradeToVersion3(SQLiteDatabase database)
	{
		database.execSQL(QueryDir.getQuery(TO_VERSION + 3));
	}

	public void UpgradeToVersion4(SQLiteDatabase database)
	{
		try {
			database.execSQL(QueryDir.getQuery(TO_VERSION + "4-1"));
			database.execSQL(QueryDir.getQuery(TO_VERSION + "4-2"));
		}
		catch(Exception ex){}
	}
	public void UpgradeToVersion5(SQLiteDatabase database)
	{
		database.execSQL(QueryDir.getQuery(TO_VERSION + "5-1"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "5-2"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "5-3"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "5-4"));
		SyncAudit.clearAudit();
	}
}
