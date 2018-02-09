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

import rp3.auna.Contants;
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

    public static void setAlarmVisita(Date fecha,DataBase db, Context context, int hour, int minute,int identificador,String mensaje){
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
        alarmJvs.setDateTime(setTime);
        alarmJvs.setShouldVibrate(true);
        alarmJvs.setName("Alerta de Visita");
        alarmJvs.setType(1);
        alarmJvs.setIdentificador(identificador);
        alarmJvs.setFecha(fecha);
        alarmJvs.setMensaje(mensaje);
        alarmJvs.setIdAgente(idAgente);
        alarmJvs.schedule(context);
        try {
            boolean o = AlarmJvs.insert(db,alarmJvs);
            Log.d(TAG,(o)?"se Inserto Alerta de Visita":"No se inserto alerta de Visita");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void setAlarmLlamada(Date fecha,DataBase db,Context context,int hour,int minute,int identificador,String mensaje){
        AlarmJvs alarmJvs = new AlarmJvs();
        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        alarmJvs.setActive(false);
        DateTime setTime = DateTime.now();
        setTime = setTime.withHourOfDay(hour);
        setTime = setTime.withMinuteOfHour(minute);
        setTime = setTime.withSecondOfMinute(0);
        Log.d(TAG,"Hora del dia:"+hour+" ,Minuto del dia:"+minute);
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
        alarmJvs.schedule(context);
        try {
            boolean o = AlarmJvs.insert(db,alarmJvs);
            Log.d(TAG,(o)?"se Inserto Notificar de visita":"No se inserto notificar de visita");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
