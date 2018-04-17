package rp3.auna.util.helper;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rp3.auna.Contants;
import rp3.auna.Main2Activity;
import rp3.auna.MainActivity;
import rp3.auna.R;
import rp3.auna.StartActivity;
import rp3.auna.bean.VisitaVta;
import rp3.auna.models.Agenda;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.NotificationPusher;

import static com.orm.util.ContextUtil.getPackageManager;


public class Util {

    public static final String TAG="Util";

    public static void ErrorToFile(Exception ex)
    {
        try{
            File file = new File(Environment.getExternalStorageDirectory()+ "/testAuna.log");
            PrintStream ps = null;
            try {
                ps = new PrintStream( new FileOutputStream(file, true));
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                ps.append("\r\n");
                ex.printStackTrace(ps);
                ps.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            Log.d(TAG,"ErrorTofile..."+e.getMessage());
            e.printStackTrace();
        }

    }

    public static Bitmap getBitmapByte(byte[] bytes){
        Bitmap bitmap = null;
        try{
            /*
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 8;
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);*/

            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            options.inPurgeable = true; // inPurgeable is used to free up memory while required
            Bitmap songImage1 = BitmapFactory.decodeByteArray(bytes,0, bytes.length,options);//Decode image, "thumbnail" is the object of image file
            Bitmap songImage = Bitmap.createScaledBitmap(songImage1, 50 , 50 , true);// convert decoded bitmap into well scalled Bitmap format.
            bitmap = songImage;
            return bitmap;
        }catch (Exception e){
            Log.d(TAG,"getBitmapByte:"+e.getMessage());
            e.printStackTrace();
            ErrorToFile(e);
            return bitmap;
        }
    }

    public static double calcularDistancia(double initialLat, double initialLong,
                                           double finalLat, double finalLong) {
        Location selected_location = new Location("locationA");
        selected_location.setLatitude(initialLat);
        selected_location.setLongitude(initialLong);
        Location near_locations = new Location("locationB");
        near_locations.setLatitude(finalLat);
        near_locations.setLongitude(finalLong);

        double distance = selected_location.distanceTo(near_locations);
        return distance;
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        return dialog;
    }

    public static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static boolean validarCorreo(String email) {

        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;

    }


    public static Double Redondear(double numero) {
        return Math.rint(numero * 100) / 100;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    public static ArrayList<Fragment> createImageViewFragments(List<String> imagePathDetail)
    {
        ArrayList<Fragment> list = new  ArrayList<Fragment>();

        for(int i=0;i<imagePathDetail.size();i++)
        {
           /* Fragment frag = new ImageSliderFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", imagePathDetail.get(i));
            frag.setArguments(bundle);
            list.add(frag);*/
        }

        return list;
    }


    /*public static ArrayList<Fragment> createImageSliderMainFragments(ArrayList<String> listBanner)
    {
        ArrayList<Fragment> list = new  ArrayList<Fragment>();

        for(int i=0;i<listBanner.size();i++)
        {
            Fragment frag = new SliderPrincipalFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", listBanner.get(i));
            frag.setArguments(bundle);
            list.add(frag);
        }

        return list;
    }*/
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {

                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                        return true;

                    }

                }

            }

        }
        return false;
    }

    public static String fechaActual(){
        String fecha="";
        SimpleDateFormat format=new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        Date date=new Date();
        fecha+=format.format(date);
        return fecha;
    }

    public static String fechaCalendar(){

            String fecha="";
            SimpleDateFormat format=new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
            Date date=new Date();
            Calendar c = Calendar.getInstance();
            Log.d("UTIL","Current time => " + c.getTime());
            fecha+=format.format(c.getTime());
            Log.d("UTIL","Fecha:"+fecha);
        return fecha;
    }

    public static String fechaCalendarSync(){

        String fecha="";
        SimpleDateFormat format=new SimpleDateFormat(Constants.DATE_TIME_FORMAT_SYNC);
        Date date=new Date();
        Calendar c = Calendar.getInstance();
        Log.d("UTIL","Current time => " + c.getTime());
        fecha+=format.format(c.getTime());
        Log.d("UTIL","Fecha:"+fecha);
        return fecha;
    }

    public static String fechaCalendarFoto(){

        String fecha="";
        SimpleDateFormat format=new SimpleDateFormat(Constants.DATE_TIME_FORMAT_FOTO);
        Date date=new Date();
        Calendar c = Calendar.getInstance();
        Log.d("UTIL","Current time => " + c.getTime());
        fecha+=format.format(c.getTime());
        Log.d("UTIL","Fecha:"+fecha);
        return fecha;
    }

    public static String nombreFoto(){
        String fecha="";
        SimpleDateFormat format=new SimpleDateFormat(Constants.DATE_TIME_FORMAT_FOTO);
        Date date=new Date();
        fecha+="_"+format.format(date)+Constants.FORMAT_JPEG;
        return fecha;
    }

    public static String nombrePdf(){
        String fecha="";
        SimpleDateFormat format=new SimpleDateFormat(Constants.DATE_TIME_FORMAT_FOTO);
        Date date=new Date();
        fecha+="_"+format.format(date)+Constants.FORMAT_PDF;
        return fecha;
    }

    public static String nombreUbicacion(){
        String nombre="";
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy");
        nombre+="/"+format.format(date);//Año
        date=new Date();
        format=new SimpleDateFormat("MM");
        nombre+="/"+format.format(date)+"/";//Mes
        return nombre;
    }

    public static String nombreUbicacionNoAtendido(){
        return Util.nombreUbicacion()+"/NoAtendido/";
    }

    public static String nombreUbicacionNoVisita(){
        return Util.nombreUbicacion()+"/NoVisita/";
    }

    public static String nombreUbicacionSiAtendido(){
        return Util.nombreUbicacion()+"/SiAtendido/";
    }

    public static String nombreUbicacionFoto(){
        return Util.nombreUbicacionSiAtendido()+"Fotografico/";
    }

    public static String nombreFotoRelevo(){
        String nombre="";
        nombre+="_"+Util.fechaCalendarFoto();
        return nombre;
    }


   /* public static String generarNombreAudio() {
        String ret = "AUDBM";
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat(Constants.DATE_FORMAT_AUDIO);
        Date date=new Date();
        ret += simpleDateFormat1.format(date)+Constants.EXTENSION_ARCHIVOS_AUDIO;

        return ret;
    }

    public static String serializeUsuario(Usuario usuario){
        Gson usuarioJson = new Gson();
        return usuarioJson.toJson(usuario);
    }

    public static Usuario createUsuarioFromJson(String usuarioJson){
        Gson objectJson = new Gson();
        return objectJson.fromJson(usuarioJson,Usuario.class);
    }*/

    public static boolean hasActiveInternetConnection() {
        try
        {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(4000);
            urlc.setReadTimeout(4000);
            urlc.connect();
            int networkcode2 = urlc.getResponseCode();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e)
        {
            Log.i("warning", "Error checking internet connection", e);
            return false;
        }

    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", h,m,s);
    }

    public static String convertSecondToHMmSsDuration(int seconds){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(seconds),
                TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds)),
                TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
        return hms;
    }

    public static String convertMillisToHMmSsDuration(long seconds){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(seconds),
                TimeUnit.MILLISECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(seconds)),
                TimeUnit.MILLISECONDS.toSeconds(seconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(seconds)));
        return hms;
    }

    public static String loadJSONFromAsset(Context context,String nombre) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("ubigeo/"+nombre+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d(TAG,ex.getMessage());
            return null;
        }
        return json;
    }

    public static List<VisitaVta> reLoadVisitas(Context context, rp3.auna.models.ventanueva.VisitaVta visitaVta){
        rp3.auna.bean.VisitaVta obj = new rp3.auna.bean.VisitaVta();
        obj.setEstado(visitaVta.getEstado());
        obj.setLlamadaId(0);
        if(String.valueOf(visitaVta.getVisitaId())!=null){
            obj.setVisitaId(visitaVta.getVisitaId());
        }else{
            obj.setVisitaId(0);
        }
        obj.setIdCliente(visitaVta.getIdCliente());
        obj.setDuracionCode(visitaVta.getDuracionCode());
        obj.setFechaFin(visitaVta.getFechaFin().getTime());
        obj.setFechaInicio(visitaVta.getFechaInicio().getTime());
        obj.setFechaVisita(visitaVta.getFechaVisita().getTime());
        obj.setDescripcion(visitaVta.getDescripcion());
        obj.setFotos(null);
        obj.setReferidoCount(0);
        obj.setReferidoTabla(visitaVta.getReferidoTabla());
        obj.setLatitud(Float.parseFloat(String.valueOf(visitaVta.getLatitud())));
        obj.setLongitud(Float.parseFloat(String.valueOf(visitaVta.getLongitud())));
        obj.setIdClienteDireccion(visitaVta.getIdClienteDireccion());
        obj.setIdVisita(visitaVta.getIdVisita());
        obj.setMotivoReprogramacionTabla(visitaVta.getMotivoReprogramacionTabla());
        obj.setMotivoReprogramacionValue(visitaVta.getMotivoReprogramacionValue());
        obj.setMotivoVisitaTabla(visitaVta.getMotivoVisitaTabla());
        obj.setMotivoVisitaValue(visitaVta.getMotivoVisitaValue());
        obj.setObservacion(visitaVta.getObservacion());
        obj.setTipoVenta(visitaVta.getTipoVenta());
        obj.setVisitaValue(visitaVta.getVisitaValue());
        obj.setTiempoCode(visitaVta.getTiempoCode());
        obj.setReferidoValue(visitaVta.getReferidoValue());
        List<rp3.auna.bean.VisitaVta> visitaVtaList = SessionManager.getInstance(context).getListVisitaSession();
        if(visitaVtaList==null){
            visitaVtaList = new ArrayList<>();
        }else{
            if(visitaVtaList.size()==0){

            }
        }
        return visitaVtaList;
    }

    //1 minute = 60 seconds
//1 hour = 60 x 60 = 3600
//1 day = 3600 x 24 = 86400
    public static String printDifference(Date startDate, Date endDate) {
        String diferencia = null;
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return diferencia= "Horas: "+elapsedHours+" Minutos:"+elapsedMinutes;
    }

    public static void setAlarmAgenda(final Main2Activity context){
        Log.d(TAG,"setAlarmAgenda...");
        final Context ctx = context;
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },1000);*/
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                    //List<LlamadaVta> llamadasCanceled = LlamadaVta.getLlamadasAllCancel(Utils.getDataBase(ctx));
                    //Log.d(TAG,"Cantidad de llamadas a cancelar:"+llamadasCanceled.size());
                    /*for(LlamadaVta obj:llamadasCanceled){
                        //obj.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
                        //if(obj.getIdLlamada()>0){
                          //  obj.setInsertado(0);
                        //}else{
                          //  obj.setInsertado(1);
                        //}
                        //boolean res = LlamadaVta.update(Utils.getDataBase(ctx),obj);
                        ((Main2Activity)context).sendNotification("No realizo una llamada programada");
                        //Log.d(TAG,res?"Llamada Actualizado a cancelar:"+obj.getID()+" "+obj.getIdLlamada()+"":"Llamada No Actuliazado a cancelar:"+obj.getID()+" "+obj.getIdLlamada()+"");
                        break;
                    }*/

                    //List<rp3.auna.models.ventanueva.VisitaVta> visitasCanceled = rp3.auna.models.ventanueva.VisitaVta.getVisitasCanceled(Utils.getDataBase(ctx));
                    //Log.d(TAG,"Cantidad de visitas a cancelar:"+visitasCanceled.size());
                    //for (rp3.auna.models.ventanueva.VisitaVta obj:visitasCanceled){
                        /*obj.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_NO_REALIZADA);
                        if(obj.getIdVisita()>0){
                            obj.setInsertado(0);
                        }else{
                            obj.setInsertado(1);
                        }
                        boolean res = rp3.auna.models.ventanueva.VisitaVta.update(Utils.getDataBase(ctx),obj);*/
                        //Log.d(TAG,res?"Visita Actualizado a cancelar:"+obj.getID()+" "+obj.getIdVisita()+"":"Visita No Actuliazado a cancelar:"+obj.getID()+" "+obj.getIdVisita()+"");
                      //  ((Main2Activity)context).sendNotification("No realizo una visita programada");
                        //break;
                    //}

                    if(SessionManager.getInstance(ctx).getVisitaSession()==null){
                        if(SessionManager.getInstance(ctx).getLlamadaGestion()==null) {
                            List<LlamadaVta> llamadas = LlamadaVta.getLlamadasMinutes(Utils.getDataBase(ctx), 300000, 240000);
                            Log.d(TAG, "Llamadas pendientes a alertar:" + llamadas.size());
                            for (LlamadaVta llamadaVta : llamadas) {
                                //rospectoVtaDb prospectoVtaDb = ProspectoVtaDb.getProspectoIdProspecto(Utils.getDataBase(ctx),llamadaVta.getIdCliente());
                                pushNotification(ctx, llamadaVta, null, "Faltan 5 Minutos para realizar la llamada\n");
                            }

                            List<rp3.auna.models.ventanueva.VisitaVta> visitas = rp3.auna.models.ventanueva.VisitaVta.getVisitasMinutes(Utils.getDataBase(ctx), 1800000, 1740000);
                            Log.d(TAG, "Visitas pendientes a alertar:" + visitas.size());
                            for (rp3.auna.models.ventanueva.VisitaVta visita : visitas) {
                                //ProspectoVtaDb prospectoVtaDb = ProspectoVtaDb.getProspectoIdProspecto(Utils.getDataBase(ctx),visita.getIdCliente());
                                NotificationPusher.pushNotification((int) visita.getID(), context,
                                        "Tiene una visita por comenzar", "Agenda");
                                //pushNotification(ctx, null,visita ,"Faltan 30 Minutos para realizar la visita");
                            }


                        }
                    }
                        Thread.sleep(60000);
                        setAlarmAgenda(context);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Log.d(TAG,"Thread Alarm:"+e.getMessage()+"...");
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void pushNotification(Context ctx, LlamadaVta llamadaVta, rp3.auna.models.ventanueva.VisitaVta visitaVta, String message) {
        Log.d(TAG,"pushNotification...");
        if(llamadaVta!=null){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("RP3 Market Force")
                            .setContentText(message);
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(ctx, StartActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(Main2Activity.class);
            stackBuilder.addNextIntent(resultIntent);
            int id = (int) llamadaVta.getID();
            Log.d(TAG,"id sqlite de llamada:"+(id+1000));
            boolean alarmRunning = (PendingIntent.getBroadcast(ctx, id,resultIntent,
                    PendingIntent.FLAG_NO_CREATE) != null);
            if(alarmRunning){
                Log.d(TAG,"alarmRunning llamada...");
                return;
            }
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            id,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mBuilder.setAutoCancel(true);

            mNotificationManager.notify((id+1000), mBuilder.build());
        }else{
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(ctx)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("RP3 Market Force")
                            .setContentText(message);
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(ctx, StartActivity.class);
            int id = (int) visitaVta.getID();
            Log.d(TAG,"id sqlite de visita:"+(id+10300));
            boolean alarmRunning = (PendingIntent.getBroadcast(ctx, id,resultIntent,
                    PendingIntent.FLAG_NO_CREATE) != null);
            if(alarmRunning){
                Log.d(TAG,"alarmRunning visita...");
                return;
            }
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
            stackBuilder.addParentStack(Main2Activity.class);

            stackBuilder.addNextIntent(resultIntent);


            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            id,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mBuilder.setAutoCancel(true);

            mNotificationManager.notify((id+10000), mBuilder.build());
        }
    }

    public static void onClickWhatsApp(Context context,String number,int type) {
        PackageManager pm= context.getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            if(type==1){
                waIntent.setPackage("com.whatsapp");
            }else if(type == 2){
                waIntent.setPackage("com.facebook.orca");
            }


            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(waIntent, "Contactar"));

        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG,"NnameNortFoundException:"+e.getMessage());
            Toast.makeText(context, "WhatsApp no instalado", Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    public static void onSharedEmail(Context context,String text,String subject){
        Log.d(TAG,"onSharedEmail..");
        try{
            /*Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/*");
            intent.putExtra(Intent.EXTRA_TEXT, "Desarrollador movil [Jesus Villa 960108775 nvillasanchez@gmail.com]"+"\n "+longitud+"\n"+latitud+"\n"+direccion);
            context.startActivity(intent);*/
            List<Intent> emailAppLauncherIntents = new ArrayList<>();
            //Intent that only email apps can handle:
            Intent emailAppIntent = new Intent(Intent.ACTION_SENDTO);
            emailAppIntent.setData(Uri.parse("mailto:"));
            emailAppIntent.putExtra(Intent.EXTRA_EMAIL, text);
            emailAppIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

            PackageManager packageManager = context.getPackageManager();

            //All installed apps that can handle email intent:
            List<ResolveInfo> emailApps = packageManager.queryIntentActivities(emailAppIntent, PackageManager.MATCH_ALL);
            for (ResolveInfo resolveInfo : emailApps) {
                String packageName = resolveInfo.activityInfo.packageName;
                Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
                emailAppLauncherIntents.add(launchIntent);
            }
            //Create chooser
            Intent chooserIntent = Intent.createChooser(new Intent(), "Seleccionar App Email:");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, emailAppLauncherIntents.toArray(new Parcelable[emailAppLauncherIntents.size()]));
            context.startActivity(chooserIntent);
        }catch (Exception e){
            Log.d(TAG,"Exception:"+e.getMessage());
            e.printStackTrace();
        }
    }

    public static void onSharedNumber(Context context,String text){
        Log.d(TAG,"onSharedNumber..");
        try{
            String uri = "tel:" + text;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            Uri mUri = Uri.parse("smsto:" + Utils.convertToSMSNumberPeru(text));
            Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
            mIntent.putExtra("chat",true);
            Intent chooserIntent = Intent.createChooser(mIntent, "Seleccionar Acción");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent });
            context.startActivity(chooserIntent);
        }catch (Exception e){
            Log.d(TAG,"Exception:"+e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isNumber(String cadena){
        String regexStr = "^[0-9]*$";
        if(cadena.trim().matches(regexStr))
        {
            return true;
        }
        return false;
    }

    public static void dismissKeyboard(AppCompatActivity context){
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void isGooglePlayServicesConnected(AppCompatActivity activity){
        if(GooglePlayServicesUtils.googleServicesConnected(activity)){}
    }

    //region Temp

    /*new SpiClient(getApplicationContext(), new Callback() {
                                Handler mHandler = new Handler(Looper.getMainLooper());
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d(TAG,"onFailure..."+e.getMessage());
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ProspectoActivity.this, "Hubo un problema al consultar este documento", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, final Response response) throws IOException {
                                    Log.d(TAG,"onResponse...");
                                    Log.d(TAG,"statusCode="+response.code());
                                    final String json = response.body().string();
                                    Log.d(TAG,json);
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(response.isSuccessful()){
                                                Log.d(TAG,"Yes is Successful...");
                                                rp3.auna.bean.Response response = new Gson().fromJson(json, rp3.auna.bean.Response.class);
                                                if(response.getFlag()==2){
                                                    Toast.makeText(ProspectoActivity.this, "El Documento ingresado esta No Autorizado", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    prospectoVta.setEstado(1);
                                                    prospectoVta.insert(Utils.getDataBase(getApplicationContext()),prospectoVta);
                                                    setResult(RESULT_PROSPECTO_NUEVO);
                                                    finish();
                                                }
                                            }else{
                                                Log.d(TAG,"No is Successful...");
                                                Toast.makeText(ProspectoActivity.this, "Hubo un problema al verificar el documento...vuelva a intentarlo...", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }).validar(model);*/

    //endregion

    public static boolean isNumeric(String str) {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            nfe.printStackTrace();
            return false;
        }
        return true;
    }

    //region Formato de Fecha Credit Card

    public static boolean isInputCorrect(Editable s, int size, int dividerPosition, char divider) {
        boolean isCorrect = s.length() <= size;
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (i + 1) % dividerPosition == 0) {
                isCorrect &= divider == s.charAt(i);
            } else {
                isCorrect &= Character.isDigit(s.charAt(i));
            }
        }
        return isCorrect;
    }

    public static String concatString(char[] digits, int dividerPosition, char divider) {
        final StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < digits.length; i++) {
            if (digits[i] != 0) {
                formatted.append(digits[i]);
                if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                    formatted.append(divider);
                }
            }
        }

        return formatted.toString();
    }

    public static char[] getDigitArray(final Editable s, final int size) {
        char[] digits = new char[size];
        int index = 0;
        for (int i = 0; i < s.length() && index < size; i++) {
            char current = s.charAt(i);
            if (Character.isDigit(current)) {
                digits[index] = current;
                index++;
            }
        }
        return digits;
    }

    //endregion

    public static String replaceNumeric(String str){
        return str.replaceAll("[0-9]","");
    }

    public static Rect locateView(View v)
    {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try
        {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe)
        {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }
}
