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
				case 3: UpgradeToVersion3(db); break;
			}
		}
	}

	public void UpgradeToVersion3(SQLiteDatabase database)
	{
		database.execSQL(QueryDir.getQuery(TO_VERSION + 3));
	}
}
