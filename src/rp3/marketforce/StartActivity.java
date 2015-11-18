package rp3.marketforce;

import java.util.Calendar;
import java.util.Random;

import rp3.configuration.Configuration;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.data.Constants;
import rp3.data.MessageCollection;
import rp3.marketforce.content.EnviarUbicacionReceiver;
import rp3.marketforce.db.Contract;
import rp3.marketforce.db.DbOpenHelper;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.Tarea;
import rp3.marketforce.models.Ubicacion;
import rp3.marketforce.sync.Server;
import rp3.marketforce.sync.SyncAdapter;
import rp3.runtime.Session;
import rp3.sync.SyncAudit;
import rp3.util.ConnectionUtils;

import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class StartActivity extends rp3.app.StartActivity{

    public final static int REINTENTAR_MESSAGE = 100;
    public final static int OFFLINE_MESSAGE = 200;
	
	public StartActivity() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		if(PreferenceManager.getBoolean(Contants.KEY_FIRST_TIME, true) || PreferenceManager.getBoolean(Contants.KEY_SECOND_TIME, true))
		{
			startActivity(new Intent(this, ServerActivity.class));
			finish();
		}
		else
			Configuration.reinitializeConfiguration(context, DbOpenHelper.class);
		Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);	
		
	}
	
	private void setServiceRecurring(){
		Intent i = new Intent(this, EnviarUbicacionReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		
		// Set the alarm to start at 8:30 a.m.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		long time = PreferenceManager.getLong(Contants.KEY_ALARMA_INICIO);
		cal.setTimeInMillis(time);
		calendar.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
		
		String prueba = cal.getTime().toString();
		String prueba2 = calendar.getTime().toString();
				
		Random r = new Random();
		int i1 = r.nextInt(5);
		
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		//am.cancel(pi); // cancel any existing alarms
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
			calendar.getTimeInMillis() + (i1 * 1000 * 5),
			1000 * 60 * PreferenceManager.getInt(Contants.KEY_ALARMA_INTERVALO), pi);

	}


    @Override
	public void onContinue() {	
		
		super.onContinue();
        String proof = PreferenceManager.getString(Constants.KEY_LAST_LOGIN,"");
        String proof2 = PreferenceManager.getString(Constants.KEY_LAST_PASS,"");
        String peer = Session.getUser().getLogonName();
        String peer2 = Session.getUser().getPassword();
        if(!PreferenceManager.getString(Constants.KEY_LAST_LOGIN,"").equalsIgnoreCase(Session.getUser().getLogonName()) ||
                !PreferenceManager.getString(Constants.KEY_LAST_PASS,"").equalsIgnoreCase(Session.getUser().getPassword()))
        {
            Agenda.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
            Agenda.AgendaExt.deleteAll(getDataBase(), Contract.AgendaExt.TABLE_NAME);
            Tarea.deleteAll(getDataBase(), Contract.Tareas.TABLE_NAME);
            Cliente.deleteAll(getDataBase(), Contract.Cliente.TABLE_NAME);
            Cliente.ClientExt.deleteAll(getDataBase(), Contract.ClientExt.TABLE_NAME);
            ClienteDireccion.deleteAll(getDataBase(), Contract.ClienteDireccion.TABLE_NAME);
            Contacto.deleteAll(getDataBase(), Contract.Contacto.TABLE_NAME);
            Contacto.ContactoExt.deleteAll(getDataBase(), Contract.ContactoExt.TABLE_NAME);
            Actividad.deleteAll(getDataBase(), Contract.Actividades.TABLE_NAME);
            AgendaTarea.deleteAll(getDataBase(), Contract.AgendaTarea.TABLE_NAME);
            AgendaTareaActividades.deleteAll(getDataBase(), Contract.AgendaTareaActividades.TABLE_NAME);
            Ubicacion.deleteAll(getDataBase(), Contract.Ubicacion.TABLE_NAME);
            //GeopoliticalStructure.deleteAll(getDataBase(), rp3.data.models.Contract.GeopoliticalStructure.TABLE_NAME);
            //GeopoliticalStructureExt.deleteAll(getDataBase(), rp3.data.models.Contract.GeopoliticalStructureExt.TABLE_NAME);
            PreferenceManager.setValue(Contants.KEY_IDAGENTE, 0);
            PreferenceManager.setValue(Contants.KEY_IDRUTA, 0);
            PreferenceManager.setValue(Contants.KEY_ES_SUPERVISOR, false);
            PreferenceManager.setValue(Contants.KEY_ES_AGENTE, false);
            PreferenceManager.setValue(Contants.KEY_ES_ADMINISTRADOR, false);
            PreferenceManager.setValue(Contants.KEY_CARGO, "");
            SyncAudit.clearAudit();
        }

        PreferenceManager.setValue(Constants.KEY_LAST_LOGIN, Session.getUser().getLogonName());
        PreferenceManager.setValue(Constants.KEY_LAST_PASS, Session.getUser().getPassword());

		Long days = SyncAudit.getDaysOfLastSync(SyncAdapter.SYNC_TYPE_GENERAL, SyncAdapter.SYNC_EVENT_SUCCESS);

		if(days == null || days > 0){
			Bundle bundle = new Bundle();
			bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
			requestSync(bundle);
		}else
			callNextActivity();
	}
	
	public void onSyncComplete(Bundle data, final MessageCollection messages) {
        if (!data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && !ConnectionUtils.isNetAvailable(this)) {
            callNextActivity();
        } else if (data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GENERAL) ||
                data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_SOLO_RESUMEN)) {
            if (messages.hasErrorMessage())
                if (Session.IsLogged()) {
                    callNextActivity();
                } else {
                    showDialogMessage(messages, new SimpleCallback() {
                        @Override
                        public void onExecute(Object... params) {
                            if (!messages.hasErrorMessage())
                                callNextActivity();
                            else
                                finish();
                        }
                    });
                }
            else
                callNextActivity();
        }
    }

    @Override
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
        requestSync(bundle);
    }

    @Override
    public void onNegativeConfirmation(int id) {
        super.onNegativeConfirmation(id);
        finish();
    }

    //	@Override
//	public void onVerifyRequestSignIn() {
//		callNextActivity();
//	}
	
	private void callNextActivity(){
		setServiceRecurring();		
		startActivity(MainActivity.newIntent(this));
		finish();
		setServiceRecurring();
	}
}
