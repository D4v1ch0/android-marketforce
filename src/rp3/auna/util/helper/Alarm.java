package rp3.auna.util.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.auna.Contants;
import rp3.auna.adapter.AgendaLlamadaAdapter;
import rp3.auna.content.AgendaReceiver;
import rp3.auna.models.ventanueva.AlarmJvs;
import rp3.configuration.PreferenceManager;
import rp3.db.sqlite.DataBase;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class Alarm {
    private static final String TAG = Alarm.class.getSimpleName();

    public void setMessage(Context context, Date fecha,Bundle todo){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 3);
        calendar.setTime(fecha);
        final Intent myIntent = new Intent(context, AgendaReceiver.class);
        myIntent.putExtras(todo);
        PendingIntent pending_intent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
    }

    public static void setAlarmVisita(Date fecha,DataBase db, Context context, int hour, int minute,int identificador,String mensaje,long identificadorTemp,
                                      int sync){
        AlarmJvs alarmJvs = new AlarmJvs();
        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        alarmJvs.setActive(false);
        DateTime setTime = DateTime.now();
        setTime = setTime.withHourOfDay(hour);
        setTime = setTime.withMinuteOfHour(minute);
        setTime = setTime.withMillisOfSecond(0);
        setTime = setTime.withSecondOfMinute(0);
        Log.d(TAG,"Hora del dia:"+hour+" ,Minuto del dia:"+minute);
        // user set a time in the past (for the next day)
        if (setTime.isBeforeNow()) {
            setTime = setTime.plusDays(1);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        Log.d(TAG,"Día del año:"+calendar.get(Calendar.DAY_OF_YEAR));
        alarmJvs.setDateTime(setTime);
        alarmJvs.setShouldVibrate(true);
        alarmJvs.setName("Alerta de Visita");
        alarmJvs.setType(1);
        alarmJvs.setIdentificador(identificador);
        alarmJvs.setFecha(fecha);
        alarmJvs.setMensaje(mensaje);
        alarmJvs.setIdAgente(idAgente);
        alarmJvs.setIdentificadorTemp(identificadorTemp);
        alarmJvs.setSync(sync);
        alarmJvs.schedule(context);
        try {
            boolean o = AlarmJvs.insert(db,alarmJvs);
            Log.d(TAG,(o)?"se Inserto Alerta de Visita":"No se inserto alerta de Visita");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setAlarmLlamada(Date fecha,DataBase db,Context context,int hour,int minute,int identificador,String mensaje,long identificadorTemp, int sync){
        AlarmJvs alarmJvs = new AlarmJvs();
        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        alarmJvs.setActive(false);
        DateTime setTime = DateTime.now();
        //setTime = setTime.withDayOfYear(fecha.getYear());
        setTime = setTime.withHourOfDay(hour);
        setTime = setTime.withMinuteOfHour(minute);
        setTime = setTime.withSecondOfMinute(0);
        Log.d(TAG,"Hora del dia:"+hour+" ,Minuto del dia:"+minute);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        Log.d(TAG,"Día del año:"+calendar.get(Calendar.DAY_OF_YEAR));
        // user set a time in the past (for the next day)
        if (setTime.isBeforeNow()) {
            setTime = setTime.plusDays(1);
        }
        alarmJvs.setDateTime(setTime);
        alarmJvs.setShouldVibrate(true);
        alarmJvs.setName("Alerta de Llamada");
        alarmJvs.setType(2);
        alarmJvs.setIdentificador(identificador);
        alarmJvs.setFecha(fecha);
        alarmJvs.setMensaje(mensaje);
        alarmJvs.setIdAgente(idAgente);
        alarmJvs.setIdentificadorTemp(identificadorTemp);
        alarmJvs.setSync(sync);
        alarmJvs.schedule(context);
        try {
            boolean o = AlarmJvs.insert(db,alarmJvs);
            Log.d(TAG,(o)?"se Inserto Alerta de Llamada":"No se inserto alerta de llamada");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setAlarmTest(DataBase db,Context context,int hour,int minute,int identificador){
        AlarmJvs alarm = new AlarmJvs();
        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        alarm.setActive(false);
        DateTime setTime = DateTime.now();
        setTime = setTime.withHourOfDay(hour);
        setTime = setTime.withMinuteOfHour(minute);
        setTime = setTime.withSecondOfMinute(0);
    // user set a time in the past (for the next day)
        if (setTime.isBeforeNow()) {
        setTime = setTime.plusDays(1);
    }

        alarm.setDateTime(setTime);
        alarm.setShouldVibrate(true);
        alarm.setName("TimePicker Alarmaaa");
        alarm.setIdAgente(idAgente);
        alarm.schedule(context);
    }

    //Notificar al supervisor
    public static void setAlarmLlamadaSupervisor(Date fecha,DataBase db,Context context,int hour,int minute,int identificador){
        AlarmJvs alarmJvs = new AlarmJvs();
        alarmJvs.setActive(false);
        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        DateTime setTime = DateTime.now();
        Log.d(TAG,"Hora del dia:"+hour+" ,Minuto del dia:"+minute);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        Log.d(TAG,"Día del año:"+calendar.get(Calendar.DAY_OF_YEAR));
        setTime = setTime.withHourOfDay(hour);
        setTime = setTime.withMinuteOfHour(minute);
        setTime = setTime.withSecondOfMinute(0);
        // user set a time in the past (for the next day)
        if (setTime.isBeforeNow()) {
            setTime = setTime.plusDays(1);
        }
        alarmJvs.setDateTime(setTime);
        alarmJvs.setShouldVibrate(true);
        alarmJvs.setName("Notificar Llamada no realizada");
        alarmJvs.setType(22);
        alarmJvs.setIdentificador(identificador);
        alarmJvs.setFecha(fecha);
        alarmJvs.setIdAgente(idAgente);
        alarmJvs.setIdentificadorTemp(identificador);
        alarmJvs.setSync(1);
        alarmJvs.schedule(context);
        try {
            boolean o = AlarmJvs.insert(db,alarmJvs);
            Log.d(TAG,(o)?"se Inserto Notificar de Llamada":"No se inserto Notificar de llamada");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //Notificar al supervisor
    public static void setAlarmVisitaSupervisor(Date fecha,DataBase db,Context context,int hour,int minute,int identificador){
        AlarmJvs alarmJvs = new AlarmJvs();
        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        alarmJvs.setActive(false);
        DateTime setTime = DateTime.now();
        setTime = setTime.withHourOfDay(hour);
        setTime = setTime.withMinuteOfHour(minute);
        setTime = setTime.withSecondOfMinute(0);
        Log.d(TAG,"Hora del dia:"+hour+" ,Minuto del dia:"+minute);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        Log.d(TAG,"Día del año:"+calendar.get(Calendar.DAY_OF_YEAR));
        // user set a time in the past (for the next day)
        if (setTime.isBeforeNow()) {
            setTime = setTime.plusDays(1);
        }
        alarmJvs.setDateTime(setTime);
        alarmJvs.setShouldVibrate(true);
        alarmJvs.setName("Notificar Visita no realizada");
        alarmJvs.setType(11);
        alarmJvs.setIdentificador(identificador);
        alarmJvs.setFecha(fecha);
        alarmJvs.setIdAgente(idAgente);
        alarmJvs.setIdentificadorTemp(identificador);
        alarmJvs.setSync(1);
        alarmJvs.schedule(context);
        try {
            boolean o = AlarmJvs.insert(db,alarmJvs);
            Log.d(TAG,(o)?"se Inserto Notificar de visita":"No se inserto notificar de visita");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //Eliminar Todas las Alarmas
    public static void removeAllAlarms(DataBase dataBase,Context context){
        Log.d(TAG,"removeAllAlarms...");
        List<AlarmJvs> list = AlarmJvs.getLlamadasAll(dataBase);
        Calendar calendar = Calendar.getInstance();
        int sameDay = calendar.get(Calendar.DAY_OF_YEAR);

        DateTime dateTimeNow = new DateTime(calendar.getTimeInMillis());
        int diaHoy = dateTimeNow.getDayOfMonth();
        int mesHoy = dateTimeNow.getMonthOfYear();
        Log.d(TAG,"Día actual del mes:"+diaHoy+" mes actual:"+mesHoy);
        for (AlarmJvs jvs:list){
            DateTime dateTimeNow1 = new DateTime(jvs.getFecha().getTime());
            int day = dateTimeNow1.getDayOfMonth();
            int mes = dateTimeNow1.getMonthOfYear();
            System.out.println(jvs);
            Log.d(TAG,"día actual de la alerta llamada:"+day+" mes actual de la llamada:"+mes);
            if(sameDay==day && mes == mesHoy){
                Log.d(TAG,"llamada es del día de hoy, eliminar...");
                jvs.cancelAlarm(context);
                AlarmJvs.delete(dataBase,jvs);
            }else{
                Log.d(TAG,"llamada No es del día de hoy , persistir...");
            }
        }
        List<AlarmJvs> list1 = AlarmJvs.getLlamadasSupervisorAll(dataBase);
        for (AlarmJvs jvs:list1){
            DateTime dateTimeNow1 = new DateTime(jvs.getFecha().getTime());
            int day = dateTimeNow1.getDayOfMonth();
            int mes = dateTimeNow1.getMonthOfYear();
            System.out.println(jvs);
            Log.d(TAG,"día actual de la alerta llamada supervisor:"+day+" mesactual:"+mes);
            if(sameDay==day && mes == mesHoy){
                Log.d(TAG,"llamada supervisor es del día de hoy, eliminar...");
                jvs.cancelAlarm(context);
                AlarmJvs.delete(dataBase,jvs);
            }else{
                Log.d(TAG,"llamada supervisor No es del día de hoy , persistir...");
            }
        }
        List<AlarmJvs> list2 = AlarmJvs.getVisitasAll(dataBase);
        for (AlarmJvs jvs:list2){
            DateTime dateTimeNow1 = new DateTime(jvs.getFecha().getTime());
            int day = dateTimeNow1.getDayOfMonth();
            int mes = dateTimeNow1.getMonthOfYear();
            System.out.println(jvs);
            Log.d(TAG,"día actual de la alerta visita:"+day+" mesactual:"+mes);
            if(sameDay==day && mes == mesHoy){
                Log.d(TAG,"visita es del día de hoy, eliminar...");
                jvs.cancelAlarm(context);
                AlarmJvs.delete(dataBase,jvs);
            }else{
                Log.d(TAG,"visita No es del día de hoy , persistir...");
            }
        }
        List<AlarmJvs> list3 = AlarmJvs.getVisitasSupervisorAll(dataBase);
        for (AlarmJvs jvs:list3){
            DateTime dateTimeNow1 = new DateTime(jvs.getFecha().getTime());
            int day = dateTimeNow1.getDayOfMonth();
            int mes = dateTimeNow1.getMonthOfYear();
            System.out.println(jvs);
            Log.d(TAG,"día actual de la alerta visita supervisor:"+day+" mesactual:"+mes);
            if(sameDay==day && mes == mesHoy){
                Log.d(TAG,"visita supervisor es del día de hoy, eliminar...");
                jvs.cancelAlarm(context);
                AlarmJvs.delete(dataBase,jvs);
            }else{
                Log.d(TAG,"visita supervisor No es del día de hoy , persistir...");
            }
        }
    }
}
