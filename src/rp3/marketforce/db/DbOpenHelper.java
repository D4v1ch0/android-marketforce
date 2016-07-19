package rp3.marketforce.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import rp3.db.QueryDir;
import rp3.marketforce.models.pedido.Pedido;
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
				case 6: UpgradeToVersion6(db); break;
				case 7: UpgradeToVersion(db, i); break;
				case 8: UpgradeToVersion8(db); break;
				case 9: UpgradeToVersion9(db); break;
			}
		}
	}

	public void UpgradeToVersion(SQLiteDatabase database, int version) {
		database.execSQL(QueryDir.getQuery(TO_VERSION + version));
	}

	public void UpgradeToVersion3(SQLiteDatabase database) {
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

	public void UpgradeToVersion6(SQLiteDatabase database)
	{
		database.execSQL(QueryDir.getQuery(TO_VERSION + "6-1"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "6-2"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "6-3"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "6-4"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "6-5"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "6-6"));
	}

	private void UpgradeToVersion8(SQLiteDatabase database) {
		database.execSQL(QueryDir.getQuery(TO_VERSION + "8-1"));
		database.execSQL(QueryDir.getQuery(TO_VERSION + "8-2"));
	}

	private void UpgradeToVersion9(SQLiteDatabase db) {
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-1"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-2"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-3"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-4"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-5"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-6"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-7"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-8"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-9"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-10"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-11"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-12"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-13"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-14"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-15"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-16"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-17"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-18"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-19"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-20"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-21"));
		db.execSQL(QueryDir.getQuery(TO_VERSION + "9-22"));
	}
}
