package rp3.berlin.sync;

import android.os.AsyncTask;

import rp3.db.sqlite.DataBase;

/**
 * Created by magno_000 on 18/03/2015.
 */
public class AsyncUpdater {
    public static class UpdateAgenda extends AsyncTask<Integer, Void, Integer> {
        DataBase db;
        @Override
        protected Integer doInBackground(Integer... ints) {
            return Agenda.executeSync(db, ints[0]);
        }
        protected void onPreExecute() {
            db = DataBase.newDataBase(rp3.berlin.db.DbOpenHelper.class);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            db.close();
            super.onPostExecute(integer);
        }
    }
}