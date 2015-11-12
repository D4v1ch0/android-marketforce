package rp3.marketforce.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import rp3.db.QueryDir;

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
			}
		}
	}

	public void UpgradeToVersion(SQLiteDatabase database, int version)
	{
		database.execSQL(QueryDir.getQuery(TO_VERSION + version));
	}

	public void UpgradeToVersion3(SQLiteDatabase database)
	{

	}

	public void UpgradeToVersion4(SQLiteDatabase database)
	{

	}

	public void UpgradeToVersion5(SQLiteDatabase database)
	{

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
}
