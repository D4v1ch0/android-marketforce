package rp3.auna.util.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Random;

import rp3.auna.Contants;
import rp3.auna.models.Agente;
import rp3.auna.models.ventanueva.AlarmJvs;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.util.helper.Parcelables;
import rp3.auna.util.service.SchedulingService;
import rp3.auna.util.session.SessionManager;
import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;
import rp3.runtime.Session;
import rp3.util.NotificationPusher;

/**
 * Created by Jesus Villa on 26/12/2017.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive...");
        try {
            Log.d(TAG, "onReceive: " + intent.toString());
            final Bundle bundle = intent.getExtras();
            Session.Start(context);
            if(Session.getUser()!=null){
                if(Session.getUser().isLogged()){
                    rp3.configuration.Configuration.TryInitializeConfiguration(context);
                    AlarmJvs alarmJvs = Parcelables.toParcelableAlarm(bundle.getByteArray(AlarmJvs.TAG));
                    if(alarmJvs!=null){
                        Log.d(TAG,"alarmjvs !=null");
                        Log.d(TAG,alarmJvs.toString());
                        //region Verificar que sea el Agente
                        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
                        if(idAgente==alarmJvs.getIdAgente()){
                            Log.d(TAG,"Si es para este agente...");
                            //region Verificar para al Supervisor de llamada
                            if(alarmJvs.getType()==22){
                                int idLlamada = (alarmJvs.getIdentificador()-2000);
                                Log.d(TAG,"type ==22,Se enviara una notificacion al supervisor que no se realizo llamada a la hora programada...");
                                LlamadaVta llamadaVta = LlamadaVta.getLlamadaId(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),idLlamada);
                                if(llamadaVta!=null){
                                    Log.d(TAG,"llamadaVta!=null...");
                                    Log.d(TAG,llamadaVta.toString());
                                    if(llamadaVta.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE)){
                                        Log.d(TAG,"Es tipo para notificar al supervisor de una llamada");
                                        Agente agente = Agente.getAgente(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
                                        if(agente!=null){
                                            sendNotification("El Agente: "+agente.getNombre()+" no realizo una llamada");
                                        }else{
                                            sendNotification("Llamada no realizada");
                                        }
                                    }
                                }else{
                                    Log.d(TAG,"llamadaVta==null...");
                                }

                            }
                            //endregion

                            //region Verificar para al Supervisor de Visita
                            else if(alarmJvs.getType()==11){
                                if( SessionManager.getInstance(context).getVisitaSession()!=null){
                                    Log.d(TAG,"Tiene una visita habierta en gestion...");
                                }else{
                                    Log.d(TAG,"No tiene visita en gestion, alertar a supervisor...");
                                    int idVisita = (alarmJvs.getIdentificador()-1000);
                                    Log.d(TAG,"type == 11 Se enviara una notificacion al supervisor que no se realizo visita a la hora programada...");
                                    VisitaVta visitaVta = VisitaVta.getVisitaId(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),idVisita);
                                    if(visitaVta!=null){
                                        Log.d(TAG,"visitaVta!=nul...");
                                        if(visitaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE)){
                                            Agente agente = Agente.getAgente(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
                                            if(agente!=null){
                                                sendNotification("El Agente: "+agente.getNombre()+" no realizo una visita");
                                            }else{
                                                sendNotification("Visita no realizada");
                                            }
                                        }
                                    }else{
                                        Log.d(TAG,"visitaVta==null...");
                                    }
                                }
                            }
                            //endregion

                            //region  Verificar Alerta de Visita
                            else if(alarmJvs.getType()==1){
                                Log.d(TAG,"type = 1 Se debe alertar que una Visita esta a punto de comenzar...");
                                int idVisita = (int) (alarmJvs.getIdentificadorTemp()-100);
                                Log.d(TAG,"idvisita:"+idVisita);
                                VisitaVta visitaVta1 = SessionManager.getInstance(context).getVisitaSession();
                                if(visitaVta1!=null){
                                    Log.d(TAG,"Tiene una visita habierta en gestion...");
                                }else{
                                    Log.d(TAG,"No tiene una visita en gestion notificar cn normalidad...");

                                    sendAlertAgenda(idVisita+100,context,"Alerta de Visita",alarmJvs.getMensaje());
                                    /*VisitaVta visitaVta = VisitaVta.getVisitaId(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),idVisita);
                                    if(visitaVta!=null){
                                        Log.d(TAG,"Si hay una visita...");
                                        if(visitaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE)){
                                            sendAlertAgenda(idVisita+100,context,"Alerta de Visita",alarmJvs.getMensaje());
                                        }else{
                                            Log.d(TAG,"La visita ya no esta pendiente...");
                                        }
                                    }else{
                                        Log.d(TAG,"No hay visita con ese Id...");
                                    }*/
                                }

                            }
                            //endregion

                            //region Verificar Alerta de Llamada
                            else if(alarmJvs.getType()==2){
                                Log.d(TAG,"type == 2 Se debe alertar que una Llamada esta a punto de comenzar...");
                                int idLlamada = (int) (alarmJvs.getIdentificadorTemp()-200);
                                sendAlertAgenda((idLlamada)+200,context,"Alerta de Llamada",alarmJvs.getMensaje());
                                /*if(alarmJvs.getSync()==1){
                                    Log.d(TAG," Es una llamada sincronizada...");
                                }else{
                                    Log.d(TAG," Es una llamada desde sqlite...");

                                }*/

                                /*LlamadaVta llamadaVta = LlamadaVta.getLlamadaId(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),idLlamada);
                                if(llamadaVta!=null){
                                    Log.d(TAG,"llamada!=null...");
                                    if(llamadaVta.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE)){
                                        Log.d(TAG,"Llamada esta aun pendiente...");
                                        sendAlertAgenda((idLlamada)+200,context,"Alerta de Llamada",alarmJvs.getMensaje());
                                    }else{
                                        Log.d(TAG,"Llamada ya no esta pendiente....");
                                    }
                                }else{
                                    Log.d(TAG,"llamada == null...");
                                }*/
                            }
                            //endregion
                        }else{
                            Log.d(TAG,"No es para este agente...");
                        }

                        //endregion
                    }else{
                        Log.d(TAG,"alarjvs ==null...");
                    }
                }
            }
        }catch (Exception e){
            Log.d(TAG,"exception");
            e.printStackTrace();
        }
    }

    public void sendNotification(String mensaje){
        Log.d(TAG,"sendNotification:"+mensaje);
        if(PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR,0)!=0){
            Bundle todo = new Bundle();
            Log.d(TAG,"Id del supervisor a enviarle:"+PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR));
            Log.d(TAG,"Id del Agente que envia:"+ PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            todo.putInt("id_agente", PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR));
            todo.putString("titulo",PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR)+",RP3 Market Force");
            todo.putString("mensaje",mensaje);
            todo.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION);
            rp3.sync.SyncUtils.requestSync(todo);
        }
    }

    public void sendAlertAgenda(int code, Context context,String title, String message){
        NotificationPusher.pushNotification(code, context, message, title);
    }
/*final Intent serviceIntent = new Intent(context, SchedulingService.class);
                        serviceIntent.putExtra(AlarmJvs.TAG, bundle.getByteArray(AlarmJvs.TAG));
                        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // Start the service, keeping the device awake while it is launching.
                        startWakefulService(context, serviceIntent);*/
}
