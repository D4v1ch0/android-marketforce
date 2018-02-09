package rp3.auna.models.ventanueva;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.auna.R;
import rp3.auna.db.Contract;
import rp3.auna.util.broadcast.AlarmReceiver;
import rp3.auna.util.broadcast.BootReceiver;
import rp3.auna.util.helper.Parcelables;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.util.CursorUtils;

/**
 * Created by Jesus Villa on 26/12/2017.
 */

public class AlarmJvs extends rp3.data.entity.EntityBase<AlarmJvs> implements Parcelable {
    public static final String TAG = AlarmJvs.class.getSimpleName();
    private long id;
    private boolean active;
    public boolean shouldVibrate;
    private String name;
    private String tonePath;
    private DateTime dateTime;
    private String dateTimeString;
    private int type;
    private int identificador;
    private DateTimeFormatter dateTimeFormatter;
    private static DateTimeFormatter parcelableDateTimeFormatter = DateTimeFormat.forPattern("yyyy:MM:dd/kk:mm");
    private Date fecha;
    private String mensaje;
    private int idAgente;
    private Context context;

    public AlarmJvs() {
        active = true;
        shouldVibrate = true;
        name = "Alarm";
        dateTime = DateTime.now();
        id = -1;
        dateTimeFormatter = DateTimeFormat.forPattern("kk:mm");

        try {
            tonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
        } catch (Exception e) {
            tonePath = "";
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "AlarmJvs{" +
                "id=" + id +
                ", active=" + active +
                ", shouldVibrate=" + shouldVibrate +
                ", name='" + name + '\'' +
                ", tonePath='" + tonePath + '\'' +
                ", dateTime=" + dateTime +
                ", dateTimeString='" + dateTimeString + '\'' +
                ", type=" + type +
                ", identificador=" + identificador +
                ", dateTimeFormatter=" + dateTimeFormatter +
                ", fecha=" + fecha +
                ", mensaje='" + mensaje + '\'' +
                ", idAgente=" + idAgente +
                ", context=" + context +
                '}';
    }

    //region Encapsulamiento


    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAlarmTimeStringParcelable() {
        return parcelableDateTimeFormatter.print(dateTime);
    }

    public void setAlarmTimeStringParcelable(String parcelableAlarmTimeString) {
        try {
            dateTime = parcelableDateTimeFormatter.parseDateTime(parcelableAlarmTimeString);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void setDateTimeString(DateTime dateTimeString){
        this.dateTimeString = parcelableDateTimeFormatter.print(dateTime);
    }

    public String getDateTimeString(){
        return this.dateTimeString;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isShouldVibrate() {
        return shouldVibrate;
    }

    public void setShouldVibrate(boolean shouldVibrate) {
        this.shouldVibrate = shouldVibrate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTonePath() {
        return tonePath;
    }

    public void setTonePath(String tonePath) {
        this.tonePath = tonePath;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public static DateTimeFormatter getParcelableDateTimeFormatter() {
        return parcelableDateTimeFormatter;
    }

    public static void setParcelableDateTimeFormatter(DateTimeFormatter parcelableDateTimeFormatter) {
        AlarmJvs.parcelableDateTimeFormatter = parcelableDateTimeFormatter;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public void setIdd(long id){
        this.id=id;
    }

    public long getIdd(){
        return this.id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    //endregion

    //region Database

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    @Override
    public boolean isAutoGeneratedId() {
        return true;
    }

    @Override
    public String getTableName() {
        return Contract.AlarmManagerJVS.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_ACTIVE, this.active);
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_NAME, this.name);
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TIME, getAlarmTimeStringParcelable());
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TONE, this.tonePath);
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_VIBRATE,this.shouldVibrate);
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TYPE,this.type);
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_IDENTIFICADOR,this.identificador);
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_FECHA,this.fecha);
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_MENSAJE,this.fecha);
        setValue(Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_AGENTE,this.idAgente);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    //endregion

    //region Parcel

    @Override
    public int describeContents() {
        return hashCode();
    }

    public String getTimeUntilNextAlarmMessage() {
        Duration duration = new Duration(DateTime.now(), dateTime);

        final long days = duration.getStandardDays();
        duration = duration.minus(days * 24 * 60 * 60 * 1000);

        final long hours = duration.getStandardHours();
        duration = duration.minus(hours * 60 * 60 * 1000);

        final long minutes = duration.getStandardMinutes();

        String message = "Alarma con sonido ";
        if (context != null) {
            message = context.getResources().getString(R.string.alarm_will_sound_in);
        }

        if (hours > 0) {
            if (context != null) {
                message += String.format(context.getResources().getString(R.string.alarm_hours_and_minutes), hours, minutes);
            } else {
                message += String.format("%d horas y %d minutos", hours, minutes);
            }
        } else {
            if (minutes > 0) {
                if (context != null) {
                    message += String.format(context.getResources().getString(R.string.alarm_minutes), minutes);
                } else {
                    message += String.format("%d minutos", minutes);
                }
            } else {
                if (context != null) {
                    message += context.getResources().getString(R.string.alarm_less_minute);
                } else {
                    message += "menos de un minuto";
                }
            }
        }


        return message;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)  {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(tonePath);
        dest.writeString(getAlarmTimeStringParcelable());
        dest.writeString(String.valueOf(shouldVibrate));
        dest.writeString(String.valueOf(active));
        dest.writeInt(type);
        dest.writeInt(identificador);
        dest.writeLong(fecha.getTime());
        dest.writeString(mensaje);
        dest.writeInt(idAgente);
    }

    public static final Parcelable.Creator<AlarmJvs> CREATOR = new Parcelable.Creator<AlarmJvs>() {

        public AlarmJvs createFromParcel(Parcel source) {
            final AlarmJvs alarm = new AlarmJvs();

            alarm.setIdd(source.readLong());
            alarm.setName(source.readString());
            alarm.setTonePath(source.readString());
            alarm.setDateTime(DateTime.parse(source.readString(), parcelableDateTimeFormatter));
            alarm.setShouldVibrate(Boolean.parseBoolean(source.readString()));
            alarm.setActive(Boolean.parseBoolean(source.readString()));
            alarm.setType(source.readInt());
            alarm.setIdentificador(source.readInt());
            alarm.setFecha(new Date(source.readLong()));
            alarm.setMensaje(source.readString());
            alarm.setIdAgente(source.readInt());
            return alarm;
        }

        public AlarmJvs[] newArray(int size) {
            return new AlarmJvs[size];
        }
    };

    public void reset() {
        final DateTime now = DateTime.now();
        final DateTime old = dateTime;

        // old date is in past
        if (dateTime.isBeforeNow()) {

            // same day?
            dateTime = dateTime
                    .withYear(now.getYear())
                    .withDayOfMonth(now.getDayOfMonth())
                    .withHourOfDay(old.getHourOfDay())
                    .withMinuteOfHour(old.getMinuteOfHour())
                    .withSecondOfMinute(0);

            // next day?
            if (dateTime.isBeforeNow()) {
                dateTime = dateTime.plusDays(1);
            }
        }
    }

    //endregion

    //region Querys

    public static List<AlarmJvs> getAll(DataBase db) {
        String query = QueryDir.getQuery(Contract.AlarmManagerJVS.QUERY_ALL);

        Cursor c = db.rawQuery(query);
        List<AlarmJvs> list = new ArrayList<AlarmJvs>();

        if(c.moveToFirst()){
            do
            {
                AlarmJvs alarmJvs =  new AlarmJvs();
                alarmJvs.setID(CursorUtils.getInt(c,Contract.AlarmManagerJVS._ID));
                alarmJvs.setAlarmTimeStringParcelable(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TIME));
                alarmJvs.setTonePath(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TONE));
                alarmJvs.setShouldVibrate(CursorUtils.getInt(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_VIBRATE) == 1);
                alarmJvs.setName(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_NAME));
                alarmJvs.setActive(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_ACTIVE)==1);
                alarmJvs.setType(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TYPE));
                alarmJvs.setIdentificador(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_IDENTIFICADOR));
                alarmJvs.setFecha(CursorUtils.getDate(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_FECHA));
                alarmJvs.setMensaje(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_MENSAJE));
                alarmJvs.setIdAgente(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_AGENTE));
                Log.d(TAG,alarmJvs.toString());
                list.add(alarmJvs);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<AlarmJvs> getVisitasAll(DataBase db) {
        String query = (Contract.AlarmManagerJVS.QUERY_VISITAS_ALL);

        Cursor c = db.rawQuery(query);
        List<AlarmJvs> list = new ArrayList<AlarmJvs>();

        if(c.moveToFirst()){
            do
            {
                AlarmJvs alarmJvs =  new AlarmJvs();
                alarmJvs.setID(CursorUtils.getInt(c,Contract.AlarmManagerJVS._ID));
                alarmJvs.setAlarmTimeStringParcelable(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TIME));
                alarmJvs.setTonePath(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TONE));
                alarmJvs.setShouldVibrate(CursorUtils.getInt(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_VIBRATE) == 1);
                alarmJvs.setName(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_NAME));
                alarmJvs.setActive(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_ACTIVE)==1);
                alarmJvs.setType(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TYPE));
                alarmJvs.setIdentificador(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_IDENTIFICADOR));
                alarmJvs.setFecha(CursorUtils.getDate(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_FECHA));
                alarmJvs.setMensaje(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_MENSAJE));
                alarmJvs.setIdAgente(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_AGENTE));
                Log.d(TAG,alarmJvs.toString());
                list.add(alarmJvs);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<AlarmJvs> getVisitasSupervisorAll(DataBase db) {
        String query = (Contract.AlarmManagerJVS.QUERY_VISITAS_SUPERVISOR_ALL);

        Cursor c = db.rawQuery(query);
        List<AlarmJvs> list = new ArrayList<AlarmJvs>();

        if(c.moveToFirst()){
            do
            {
                AlarmJvs alarmJvs =  new AlarmJvs();
                alarmJvs.setID(CursorUtils.getInt(c,Contract.AlarmManagerJVS._ID));
                alarmJvs.setAlarmTimeStringParcelable(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TIME));
                alarmJvs.setTonePath(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TONE));
                alarmJvs.setShouldVibrate(CursorUtils.getInt(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_VIBRATE) == 1);
                alarmJvs.setName(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_NAME));
                alarmJvs.setActive(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_ACTIVE)==1);
                alarmJvs.setType(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TYPE));
                alarmJvs.setIdentificador(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_IDENTIFICADOR));
                alarmJvs.setFecha(CursorUtils.getDate(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_FECHA));
                alarmJvs.setMensaje(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_MENSAJE));
                alarmJvs.setIdAgente(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_AGENTE));
                Log.d(TAG,alarmJvs.toString());
                list.add(alarmJvs);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<AlarmJvs> getLlamadasAll(DataBase db) {
        String query = (Contract.AlarmManagerJVS.QUERY_LLAMADAS_ALL);

        Cursor c = db.rawQuery(query);
        List<AlarmJvs> list = new ArrayList<AlarmJvs>();

        if(c.moveToFirst()){
            do
            {
                AlarmJvs alarmJvs =  new AlarmJvs();
                alarmJvs.setID(CursorUtils.getInt(c,Contract.AlarmManagerJVS._ID));
                alarmJvs.setAlarmTimeStringParcelable(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TIME));
                alarmJvs.setTonePath(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TONE));
                alarmJvs.setShouldVibrate(CursorUtils.getInt(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_VIBRATE) == 1);
                alarmJvs.setName(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_NAME));
                alarmJvs.setActive(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_ACTIVE)==1);
                alarmJvs.setType(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TYPE));
                alarmJvs.setIdentificador(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_IDENTIFICADOR));
                alarmJvs.setFecha(CursorUtils.getDate(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_FECHA));
                alarmJvs.setMensaje(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_MENSAJE));
                alarmJvs.setIdAgente(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_AGENTE));
                Log.d(TAG,alarmJvs.toString());
                list.add(alarmJvs);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<AlarmJvs> getLlamadasSupervisorAll(DataBase db) {
        String query = (Contract.AlarmManagerJVS.QUERY_LLAMADAS_SUPERVISOR_ALL);

        Cursor c = db.rawQuery(query);
        List<AlarmJvs> list = new ArrayList<AlarmJvs>();

        if(c.moveToFirst()){
            do
            {
                AlarmJvs alarmJvs =  new AlarmJvs();
                alarmJvs.setID(CursorUtils.getInt(c,Contract.AlarmManagerJVS._ID));
                alarmJvs.setAlarmTimeStringParcelable(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TIME));
                alarmJvs.setTonePath(CursorUtils.getString(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TONE));
                alarmJvs.setShouldVibrate(CursorUtils.getInt(c, Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_VIBRATE) == 1);
                alarmJvs.setName(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_NAME));
                alarmJvs.setActive(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_ACTIVE)==1);
                alarmJvs.setType(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_TYPE));
                alarmJvs.setIdentificador(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_IDENTIFICADOR));
                alarmJvs.setFecha(CursorUtils.getDate(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_FECHA));
                alarmJvs.setMensaje(CursorUtils.getString(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_MENSAJE));
                alarmJvs.setIdAgente(CursorUtils.getInt(c,Contract.AlarmManagerJVS.COLUMN_ALARM_MANAGER_ALARM_AGENTE));
                Log.d(TAG,alarmJvs.toString());
                list.add(alarmJvs);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    //endregion

    //region Service

    public void schedule(Context context) {
        Log.d(TAG,"schedule...");
        this.context = context;
        active = true;
        Log.d(TAG,this.toString());
        Intent myIntent = new Intent(context, AlarmReceiver.class);

        final Bundle bundle = new Bundle();
        bundle.putByteArray(AlarmJvs.TAG, Parcelables.toByteArray(this));
        myIntent.putExtras(bundle);

        Log.d(TAG,"dateTime Type:"+getType());
        Log.d(TAG,"Mes:"+dateTime.getMonthOfYear()+" Dia del mes:"+dateTime.getDayOfMonth()+" Hora del dia:"+dateTime.getHourOfDay()+" Minuto de hora:"+dateTime.getMinuteOfHour());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.getIdentificador(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,dateTime.getHourOfDay());
        calendar.set(Calendar.MINUTE,dateTime.getMinuteOfHour());
        calendar.set(Calendar.SECOND,0);
        Calendar fechita = Calendar.getInstance();
        fechita.setTime(this.getFecha());
        try {
            tonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
        } catch (Exception e) {
            tonePath = "";
            e.printStackTrace();
        }
        Log.d(TAG,"FECHA FINAL DE LA ALERTA SCHEDULE CALENDAR: "+calendar.getTime().toLocaleString());
        Log.d(TAG,"FECHA FINAL DE LA ALERTA SCHEDULE DATE: "+fechita.getTime().toLocaleString());
        //alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);

        // Enable {@code BootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Log.d(TAG,"Tiempo que falta para alertar:"+getTimeUntilNextAlarmMessage());
        //Toast.makeText(context, getTimeUntilNextAlarmMessage(), Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context) {
        active = true;

        Intent myIntent = new Intent(context, AlarmReceiver.class);

        final Bundle bundle = new Bundle();
        bundle.putByteArray(AlarmJvs.TAG, Parcelables.toByteArray(this));
        myIntent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.getIdentificador(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    //endregion
}
