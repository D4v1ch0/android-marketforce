package rp3.auna;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.AlarmJvs;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.sync.ventanueva.LlamadaVta;
import rp3.auna.sync.ventanueva.ProspectoVta;
import rp3.auna.sync.ventanueva.VisitaVta;
import rp3.auna.util.helper.Alarm;
import rp3.auna.util.session.SessionManager;
import rp3.configuration.Configuration;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleCallback;
import rp3.data.Constants;
import rp3.data.MessageCollection;
import rp3.auna.content.EnviarUbicacionReceiver;
import rp3.auna.db.Contract;
import rp3.auna.db.DbOpenHelper;
import rp3.auna.models.Actividad;
import rp3.auna.models.Agenda;
import rp3.auna.models.AgendaTarea;
import rp3.auna.models.AgendaTareaActividades;
import rp3.auna.models.Canal;
import rp3.auna.models.Cliente;
import rp3.auna.models.ClienteDireccion;
import rp3.auna.models.Contacto;
import rp3.auna.models.Tarea;
import rp3.auna.models.Ubicacion;
import rp3.auna.models.pedido.ControlCaja;
import rp3.auna.models.pedido.Pago;
import rp3.auna.models.pedido.Pedido;
import rp3.auna.models.pedido.PedidoDetalle;
import rp3.auna.models.pedido.Producto;
import rp3.auna.sync.SyncAdapter;
import rp3.runtime.Session;
import rp3.sync.SyncAudit;
import rp3.util.ConnectionUtils;
import rp3.util.Convert;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

public class StartActivity extends rp3.app.StartActivity{

    private static final String TAG = StartActivity.class.getSimpleName();
    public final static int RECOVER_DB = 1;
    public final static int OFFLINE_MESSAGE = 200;

    public StartActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        try{
            if(PreferenceManager.getBoolean(Contants.KEY_FIRST_TIME, true) || PreferenceManager.getBoolean(Contants.KEY_SECOND_TIME, true))
            {
                Log.d(TAG,"KEY_FIRST_TIME || KEY_SECOND_TIME...");
                Log.d(TAG,"Primera vez, iniciarlizar server activity...");
                //Configuration.reinitializeConfiguration(context, DbOpenHelper.class);
                startActivity(new Intent(this, ServerActivity.class));
                finish();
            }
            else{
                Log.d(TAG,"Tercera vez que abre la aplicacion..,");
                Configuration.reinitializeConfiguration(context, DbOpenHelper.class);
                /*if(PreferenceManager.getString(Constants.KEY_LAST_LOGIN,"").equalsIgnoreCase(Session.getUser().getLogonName()) &&
                        PreferenceManager.getString(Constants.KEY_LAST_PASS,"").equalsIgnoreCase(Session.getUser().getPassword())){
                    if(Session.IsLogged()){
                        Log.d(TAG,"Session is Loggeg...");
                        Configuration.TryInitializeConfiguration(context, DbOpenHelper.class);
                    }else{
                        Log.d(TAG,"No ha iniciado session...");
                        Configuration.reinitializeConfiguration(context, DbOpenHelper.class);
                    }

                    Log.d(TAG,"Ya Hay uno logeado...");
                }else{
                    Log.d(TAG,"No hay nadie logeado reinitizaciliar...");
                    Configuration.reinitializeConfiguration(context, DbOpenHelper.class);
                }
                Log.d(TAG,"!KEY_FIRST_TIME || !KEY_FIRST_TIME...");
                */
            }

            //Configuration.reinitializeConfiguration(context, DbOpenHelper.class);
            Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);

            if(PreferenceManager.getString(Contants.KEY_ANDROID_ID, "").equalsIgnoreCase(""))
            {
                Log.d(TAG,"KEY_ANDROID_ID is null...");
                PreferenceManager.setValue(Contants.KEY_ANDROID_ID, Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setServiceRecurring(){
        Log.d(TAG,"setServiceRecurring...");
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
        int minIntTrack = PreferenceManager.getInt(Contants.KEY_ALARMA_INTERVALO,5);
        Log.d(TAG,"Minutos de intervalo para el tracking:"+minIntTrack);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //am.cancel(pi); // cancel any existing alarms
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis() + (i1 * 1000 * 5),
                1000 * 60 * minIntTrack, pi);

    }


    @Override
    public void onContinue() {
        Log.d(TAG,"onContinue desde el starAuna...");
        super.onContinue();
        rp3.auna.models.ventanueva.VisitaVta visitaVta = SessionManager.getInstance(this).getVisitaSession();
        if(visitaVta!=null){
            if(visitaVta.getEstado()==1){
                callNextActivity();
            }else if(visitaVta.getEstado()==3){
                //Iniciar la visita fisica (Subir Documentos)
                /*Intent intent = new Intent(this, VisitaMediaActivity.class);
                intent.putExtra("Estado",1);
                intent.putExtra("VisitaId",visitaVta.getVisitaId());
                startActivityForResult(intent,REQUEST_VISITA_PAGO_FISICO_DOCUMENTOS);*/
            }else if(visitaVta.getEstado()==6){
                callNextActivity();
            }
        }else{
            SessionManager.getInstance(this).removeVisitaSession();
            //region innecesario
            /*File file2 = new File(Environment.getExternalStorageDirectory() + "/testM.db");
        if(file2.exists() && !PreferenceManager.getBoolean(Contants.KEY_DATABASE_RESTORE, false))
        {
            showDialogConfirmation(RECOVER_DB,R.string.message_recupera_db, R.string.title_recupera_db);
            return;
        }
        else
            PreferenceManager.setValue(Contants.KEY_DATABASE_RESTORE, true);*/

            String proof = PreferenceManager.getString(Constants.KEY_LAST_LOGIN,"");
            String proof2 = PreferenceManager.getString(Constants.KEY_LAST_PASS,"");
            String peer = Session.getUser().getLogonName();
            String peer2 = Session.getUser().getPassword();
            Canal.getCanal(getDataBase(), "1");
//endregion

            //region Validar Login Antiguo
        /*if(!PreferenceManager.getString(Constants.KEY_LAST_LOGIN,"").equalsIgnoreCase(Session.getUser().getLogonName()) ||
                !PreferenceManager.getString(Constants.KEY_LAST_PASS,"").equalsIgnoreCase(Session.getUser().getPassword()))
        {
            Log.d(TAG,"login an pass !=null...eliminar todo...");
            Log.d(TAG,"LogonName:"+Session.getUser().getLogonName()+" Password:"+Session.getUser().getPassword());
            //region DB TbGeneral
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
            Pedido.deleteAll(getDataBase(), Contract.Pedido.TABLE_NAME);
            Pedido.PedidoExt.deleteAll(getDataBase(), Contract.PedidoExt.TABLE_NAME);
            PedidoDetalle.deleteAll(getDataBase(), Contract.PedidoDetalle.TABLE_NAME);
            Pago.deleteAll(getDataBase(), Contract.Pago.TABLE_NAME);
            Producto.deleteAll(getDataBase(), Contract.Producto.TABLE_NAME);
            Producto.ProductoExt.deleteAll(getDataBase(), Contract.ProductoExt.TABLE_NAME);
            ControlCaja.deleteAll(getDataBase(), Contract.ControlCaja.TABLE_NAME);
            //endregion

            //region DB VentaNueva
            ProspectoVtaDb.deleteAll(getDataBase(),Contract.ProspectoVta.TABLE_NAME,true);
            rp3.auna.models.ventanueva.LlamadaVta.deleteAll(getDataBase(),Contract.LlamadaVta.TABLE_NAME,true);
            rp3.auna.models.ventanueva.VisitaVta.deleteAll(getDataBase(),Contract.VisitaVta.TABLE_NAME,true);
            List<AlarmJvs> list = AlarmJvs.getLlamadasAll(getDataBase());
            for (AlarmJvs jvs:list){
                jvs.cancelAlarm(this);
                AlarmJvs.delete(getDataBase(),jvs);
            }
            List<AlarmJvs> list1 = AlarmJvs.getLlamadasSupervisorAll(getDataBase());
            for (AlarmJvs jvs:list1){
                jvs.cancelAlarm(this);
                AlarmJvs.delete(getDataBase(),jvs);
            }
            List<AlarmJvs> list2 = AlarmJvs.getVisitasAll(getDataBase());
            for (AlarmJvs jvs:list2){
                jvs.cancelAlarm(this);
                AlarmJvs.delete(getDataBase(),jvs);
            }
            List<AlarmJvs> list3 = AlarmJvs.getVisitasSupervisorAll(getDataBase());
            for (AlarmJvs jvs:list3){
                jvs.cancelAlarm(this);
                AlarmJvs.delete(getDataBase(),jvs);
            }
            //endregion

            //GeopoliticalStructure.deleteAll(getDataBase(), rp3.data.models.Contract.GeopoliticalStructure.TABLE_NAME);
            //GeopoliticalStructureExt.deleteAll(getDataBase(), rp3.data.models.Contract.GeopoliticalStructureExt.TABLE_NAME);
            PreferenceManager.setValue(Contants.KEY_IDAGENTE, 0);
            PreferenceManager.setValue(Contants.KEY_IDRUTA, 0);
            PreferenceManager.setValue(Contants.KEY_ES_SUPERVISOR, false);
            PreferenceManager.setValue(Contants.KEY_ES_AGENTE, false);
            PreferenceManager.setValue(Contants.KEY_ES_ADMINISTRADOR, false);
            PreferenceManager.setValue(Contants.KEY_CARGO, "");
            SyncAudit.clearAudit();
        }*/
            //endregion

            if(PreferenceManager.getString(Constants.KEY_LAST_LOGIN,"").equalsIgnoreCase(Session.getUser().getLogonName()) &&
                    PreferenceManager.getString(Constants.KEY_LAST_PASS,"").equalsIgnoreCase(Session.getUser().getPassword())){
                Log.d(TAG,"Values Login y pass == SessionUser Logon y Pass...");
                //region Validar Login Nuevo
                //region DB TbGeneral
               /* Agenda.deleteAll(getDataBase(), Contract.Agenda.TABLE_NAME);
                Agenda.AgendaExt.deleteAll(getDataBase(), Contract.AgendaExt.TABLE_NAME);
                Tarea.deleteAll(getDataBase(), Contract.Tareas.TABLE_NAME);
                Cliente.deleteAll(getDataBase(), Contract.Cliente.TABLE_NAME);
                Cliente.ClientExt.deleteAll(getDataBase(), Contract.ClientExt.TABLE_NAME);
                ClienteDireccion.deleteAll(getDataBase(), Contract.ClienteDireccion.TABLE_NAME);*/
                Contacto.deleteAll(getDataBase(), Contract.Contacto.TABLE_NAME);
                Contacto.ContactoExt.deleteAll(getDataBase(), Contract.ContactoExt.TABLE_NAME);
                Actividad.deleteAll(getDataBase(), Contract.Actividades.TABLE_NAME);
                AgendaTarea.deleteAll(getDataBase(), Contract.AgendaTarea.TABLE_NAME);
                AgendaTareaActividades.deleteAll(getDataBase(), Contract.AgendaTareaActividades.TABLE_NAME);
                Ubicacion.deleteAll(getDataBase(), Contract.Ubicacion.TABLE_NAME);
                /*Pedido.deleteAll(getDataBase(), Contract.Pedido.TABLE_NAME);
                Pedido.PedidoExt.deleteAll(getDataBase(), Contract.PedidoExt.TABLE_NAME);
                PedidoDetalle.deleteAll(getDataBase(), Contract.PedidoDetalle.TABLE_NAME);
                Pago.deleteAll(getDataBase(), Contract.Pago.TABLE_NAME);
                Producto.deleteAll(getDataBase(), Contract.Producto.TABLE_NAME);
                Producto.ProductoExt.deleteAll(getDataBase(), Contract.ProductoExt.TABLE_NAME);
                ControlCaja.deleteAll(getDataBase(), Contract.ControlCaja.TABLE_NAME);*/
                //endregion

                //region DB VentaNueva
                ProspectoVtaDb.deleteAll(getDataBase(),Contract.ProspectoVta.TABLE_NAME,true);
                rp3.auna.models.ventanueva.LlamadaVta.deleteAll(getDataBase(),Contract.LlamadaVta.TABLE_NAME,true);
                rp3.auna.models.ventanueva.VisitaVta.deleteAll(getDataBase(),Contract.VisitaVta.TABLE_NAME,true);
                List<AlarmJvs> list = AlarmJvs.getLlamadasAll(getDataBase());
                for (AlarmJvs jvs:list){
                    jvs.cancelAlarm(this);
                    AlarmJvs.delete(getDataBase(),jvs);
                }
                List<AlarmJvs> list1 = AlarmJvs.getLlamadasSupervisorAll(getDataBase());
                for (AlarmJvs jvs:list1){
                    jvs.cancelAlarm(this);
                    AlarmJvs.delete(getDataBase(),jvs);
                }
                List<AlarmJvs> list2 = AlarmJvs.getVisitasAll(getDataBase());
                for (AlarmJvs jvs:list2){
                    jvs.cancelAlarm(this);
                    AlarmJvs.delete(getDataBase(),jvs);
                }
                List<AlarmJvs> list3 = AlarmJvs.getVisitasSupervisorAll(getDataBase());
                for (AlarmJvs jvs:list3){
                    jvs.cancelAlarm(this);
                    AlarmJvs.delete(getDataBase(),jvs);
                }
                //endregion

                //GeopoliticalStructure.deleteAll(getDataBase(), rp3.data.models.Contract.GeopoliticalStructure.TABLE_NAME);
                //GeopoliticalStructureExt.deleteAll(getDataBase(), rp3.data.models.Contract.GeopoliticalStructureExt.TABLE_NAME);
                 /*PreferenceManager.setValue(Contants.KEY_IDAGENTE, 0);
                 PreferenceManager.setValue(Contants.KEY_IDRUTA, 0);
                 PreferenceManager.setValue(Contants.KEY_ES_SUPERVISOR, false);
                 PreferenceManager.setValue(Contants.KEY_ES_AGENTE, false);
                 PreferenceManager.setValue(Contants.KEY_ES_ADMINISTRADOR, false);
                 PreferenceManager.setValue(Contants.KEY_CARGO, "");*/
                //SyncAudit.clearAudit();
                //endregion

                PreferenceManager.setValue(Constants.KEY_LAST_LOGIN, Session.getUser().getLogonName());
                PreferenceManager.setValue(Constants.KEY_LAST_PASS, Session.getUser().getPassword());
                //List<Cliente> arf = Cliente.getCliente(getDataBase());

                Long days = SyncAudit.getDaysOfLastSync(SyncAdapter.SYNC_TYPE_GENERAL, SyncAdapter.SYNC_EVENT_SUCCESS);
                if(days == null || days > 0){
                    Log.d(TAG,"days == null || days > 0...SYNC GENERAL");
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
                    requestSync(bundle);
                }else{
                    Log.d(TAG,"!days == null || days > 0... NO SYNC GENERAL NEXT ACTIVITY igual le meto");
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_GENERAL);
                    requestSync(bundle);
                    //callNextActivity();
                }
            }else{
                callLoginActivity();
            }
        }

    }

    public void onSyncComplete(Bundle data, final MessageCollection messages) {
        Log.d(TAG,"onSyncComplete...");
        if (!data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && !ConnectionUtils.isNetAvailable(this)) {
            Log.d(TAG,"!data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && !ConnectionUtils.isNetAvailable(this)...");
            callNextActivity();
        } else if (data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_GENERAL) ||
                data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_SOLO_RESUMEN)) {
            Log.d(TAG,"data SYNCD GENERAL O RESUMEN...");
            if (messages.hasErrorMessage()){
                Log.d(TAG,"messages.hasErrorMessage()...");
                if (Session.IsLogged()) {
                    Log.d(TAG,"Session.IsLogged()...");
                    String cargo = PreferenceManager.getString(Contants.KEY_CARGO,null);
                    if(cargo==null){
                        Log.d(TAG,"el cargo es null...");
                    }else{
                        Log.d(TAG,"el cargo es:"+cargo);
                    }
                    callNextActivity();
                } else {
                    Log.d(TAG,"Session.NoIsLogged()...");
                    showDialogMessage(messages, new SimpleCallback() {
                        @Override
                        public void onExecute(Object... params) {
                            if (!messages.hasErrorMessage()){
                                Log.d(TAG,"No se encontro errores...callNextActivity...");
                                callNextActivity();
                            }
                            else{
                                Log.d(TAG,"Finish...");
                                finish();
                            }
                        }
                    });
                }
            }
            else {
                String cargo = PreferenceManager.getString(Contants.KEY_CARGO,null);
                if(cargo==null){
                    Log.d(TAG,"el cargo es null...");
                }else{
                    Log.d(TAG,"el cargo es:"+cargo);
                }
                Log.d(TAG,"!messages.hasErrorMessage()...");
                callNextActivity();
            }

        }else{
            Log.d(TAG,"Data sync type no is General ni resumen...");
            callNextActivity();
        }
    }

    @Override
    public void onPositiveConfirmation(int id) {
        Log.d(TAG,"onPositiveConfirmation...");
        super.onPositiveConfirmation(id);
        //restoreDatabase();
        PreferenceManager.setValue(Contants.KEY_DATABASE_RESTORE, true);
        onContinue();
    }

    @Override
    public void onNegativeConfirmation(int id) {
        Log.d(TAG,"onNegativeConfirmation...");
        super.onNegativeConfirmation(id);
        PreferenceManager.setValue(Contants.KEY_DATABASE_RESTORE, true);
        onContinue();
    }

    //	@Override
//	public void onVerifyRequestSignIn() {
//		callNextActivity();
//	}
    @SuppressLint("NewApi")
    private void restoreDatabase() {
        File file = new File(Environment.getExternalStorageDirectory() + "/testM.db");
        file.setExecutable(true);
        file.setReadable(true);
        file.setWritable(true);

        File file2 = this.getDatabasePath("Rp3MarketForce.db");
        file2.setExecutable(true);
        file2.setReadable(true);
        file2.setWritable(true);

        try {
            if(!file2.exists()) {
                File fileDir = this.getDatabasePath("extra");
                fileDir.mkdirs();
                file2.createNewFile();
            }
            InputStream in = new FileInputStream(file);
            OutputStream out = new FileOutputStream(file2);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.delete();

        File file3 = new File(Environment.getExternalStorageDirectory() + "/testK.db");
        file2.setExecutable(true);
        file2.setReadable(true);
        file2.setWritable(true);

        try {
            if(file3.exists()) {
                InputStream in = new FileInputStream(file3);
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String read;

                while((read=br.readLine()) != null) {
                    //System.out.println(read);
                    sb.append(read);
                }

                br.close();
                in.close();
                file3.delete();

                String[] locks = sb.toString().split(";");
                PreferenceManager.setValue(Constants.KEY_LAST_LOGIN, locks[0]);
                PreferenceManager.setValue(Constants.KEY_LAST_PASS, locks[1]);
            }
        } catch (Exception e) {

        }

    }

    private void callNextActivity(){
        Log.d(TAG,"callNextActivity...");
        setServiceRecurring();
        Intent intent = new Intent(this,Main2Activity.class);
        //startActivity(MainActivity.newIntent(this));
        startActivity(intent);
        finish();
        //setServiceRecurring();
    }

    private void validateVisitaSession(){
        Log.d(TAG,"validateVisitaSession...");
        //Iniciar la Cotizacion Inicial
        rp3.auna.models.ventanueva.VisitaVta visitaVta = SessionManager.getInstance(this).getVisitaSession();
        if(visitaVta!=null){
            if(visitaVta.getEstado()==1){
                callNextActivity();
            }else if(visitaVta.getEstado()==3){
                //Iniciar la visita fisica (Subir Documentos)
                /*Intent intent = new Intent(this, VisitaMediaActivity.class);
                intent.putExtra("Estado",1);
                intent.putExtra("VisitaId",visitaVta.getVisitaId());
                startActivityForResult(intent,REQUEST_VISITA_PAGO_FISICO_DOCUMENTOS);*/
            }else if(visitaVta.getEstado()==6){
                callNextActivity();
            }
        }else{
            SessionManager.getInstance(this).removeVisitaSession();
        }

    }

    //region Ciclo de vida

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG,"onBackPressed...");
    }

    //endregion
}
