package rp3.auna;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.gson.Gson;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import android.app.TimePickerDialog;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.app.DialogTimePickerChangeListener;
import rp3.app.DialogTimePickerFragment;
import rp3.auna.events.EventBus;
import rp3.auna.events.Events;
import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.location.LocationProvider;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.configuration.PreferenceManager;
import rp3.runtime.Session;

public class CrearLlamadaActivity extends ActionBarActivity implements DialogTimePickerChangeListener,
 rp3.auna.util.location.LocationProvider.LocationCallback{

    private static final String TAG = CrearLlamadaActivity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS=1000;
    private static final int RESULT_LLAMADA_NUEVO = 4;
    private static final int REQUEST_LLAMADA_REPROGRAMADA = 18;
    private static final int RESULT_LLAMADA_REPROGRAMADA = 18;
    @BindView(R.id.toolbarNuevaLlamada) Toolbar toolbar;
    @BindView(R.id.statusBarLlamada)FrameLayout statusBar;
    @BindView(R.id.scrollViewNuevaLlamada)NestedScrollView scrollView;
    @BindView(R.id.fabNuevaLlamada)FloatingActionButton fabNuevo;
    @BindView(R.id.crear_llamada_clientenew) TextView tvProspecto;
    @BindView(R.id.crear_llamada_desde_textnew) TextView tvLlamadaHora;
    @BindView(R.id.crear_llamada_descripcionnew) EditText etDescripcion;
    @BindView(R.id.crear_llamada_calendarnew) FrameLayout frameLayoutCalendar;
    private Calendar fecha, fecha_hora,fechaLlamada;
    private int TIME_PICKER_INTERVAL = 5;
    private CaldroidFragment caldroidFragment;
    private SimpleDateFormat format1 = new SimpleDateFormat(Contants.DATE_TIME_FORMAT_HH_MM);
    private int idAgente;
    private long id;
    private int idProspecto;
    private String prospecto;
    private int estado;
    private double latitud,longitud;
    private int idLlamadaRefencia = 0;
    LlamadaVta llamadaVta = null;
    private int tiempo = 0;
    public Location location;
    private LocationProvider locationProvider;
    private Status status;
    private boolean GPS = false;
    private AlertDialog dialogGps;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        setContentView(R.layout.activity_crear_llamada);
        ButterKnife.bind(this);
        initLocation();
        toolbarStatusBar();
        navigationBarStatusBar();
        getData();
        init();
        setViewObserver();
    }

    private ViewTreeObserver.OnGlobalLayoutListener CalendarViewTreeObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            frameLayoutCalendar.getViewTreeObserver().removeOnGlobalLayoutListener(CalendarViewTreeObserver);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) frameLayoutCalendar.getLayoutParams();
            lp.height = ((ViewGroup)frameLayoutCalendar).getChildAt(0).getHeight();
            frameLayoutCalendar.setLayoutParams(lp);
            frameLayoutCalendar.requestLayout();
        }
    };

    private void setViewObserver(){
        frameLayoutCalendar.getViewTreeObserver().addOnGlobalLayoutListener(CalendarViewTreeObserver);
    }

    private boolean validateCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        Date ahora = calendar.getTime();
        Date fechaProgramada = fechaLlamada.getTime();
        if(ahora.compareTo(fechaProgramada)>0){
            Toast.makeText(this, "La hora programada es menor a la actual", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateTime(){
        ApplicationParameter applicationParameter = ApplicationParameter.getParameter(Utils.getDataBase(this),
                Constants.PARAMETERID_TIEMPO_CREAR_LLAMADA,Constants.LABEL_LLAMADAS);
        tiempo = Integer.parseInt(applicationParameter.getValue());
        fechaLlamada.getTime();
        return true;
    }

    private void saveData(){
        llamadaVta =new LlamadaVta();
        if(location!=null){
            if(location.getLatitude()>0 && location.getLongitude()>0){
                llamadaVta.setLatitud(location.getLatitude());
                llamadaVta.setLongitud(location.getLongitude());
            }else{
                llamadaVta.setLatitud(latitud);
                llamadaVta.setLongitud(longitud);
            }
        }else{
            llamadaVta.setLatitud(latitud);
            llamadaVta.setLongitud(longitud);
        }

        llamadaVta.setInsertado(1);
        llamadaVta.setEstado(1);
        llamadaVta.setFechaFinLlamada(null);
        llamadaVta.setFechaInicioLlamada(null);
        llamadaVta.setObservacion(null);
        Log.d(TAG,"idagente:"+PreferenceManager.getInt(Contants.KEY_IDAGENTE));
        llamadaVta.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));
        if(estado==1){
            Log.d(TAG,"prospecto esta en bd sqlite...");
            llamadaVta.setIdCliente(Integer.parseInt(String.valueOf(id)));
        }else{
            Log.d(TAG,"prospecto en bd servidor...");
            llamadaVta.setIdCliente(idProspecto);
        }

        llamadaVta.setMotivoReprogramacionValue(null);
        llamadaVta.setMotivoReprogramacionTabla(0);//Motivo del porque reprogramo
        llamadaVta.setMotivoVisitaTabla(0);
        llamadaVta.setMotivoVisitaValue(null);//Motivo del porque no llamo
        llamadaVta.setReferidoTabla(0);
        llamadaVta.setReferidoValue(null);//Dio un referido o no
        llamadaVta.setLlamadaTabla(Contants.GENERAL_TABLE_ESTADOS_LLAMADA);
        llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE);//Realizo o no la llamada
        llamadaVta.setLlamadaValor(0);//Concreto una Llamada
        llamadaVta.setDuracion(0);
        if(TextUtils.isEmpty(etDescripcion.getText())){
            llamadaVta.setDescripcion(null);
        }else{
            llamadaVta.setDescripcion(etDescripcion.getText().toString().trim());
        }
        Log.d(TAG,"fechallamada:"+fechaLlamada.getTime().toString());
        llamadaVta.setFechaLlamada(fechaLlamada.getTime());
        Log.d(TAG, "idCliente"+idProspecto);
        if (idLlamadaRefencia==0){
            llamadaVta.setLlamadaId(0);
        }else {
            llamadaVta.setLlamadaId(idLlamadaRefencia);
        }
        llamadaVta.setInsertado(1);
        llamadaVta.setEstado(1);
        Log.d(TAG,llamadaVta.toString());
        String json = new Gson().toJson(llamadaVta);
        Log.d(TAG,"json:"+json);
        boolean res = LlamadaVta.insert(Utils.getDataBase(this),llamadaVta);
        Log.d(TAG,res?"inserto llamada...":"No se inserto llamada...");
        Log.d(TAG,"Cantidad de llamadas bd temp sqlite:"+ LlamadaVta.getLlamadasInsert(Utils.getDataBase(this)).size());
        Log.d(TAG,"Cantidad de llamadas en servidor:"+LlamadaVta.getLlamadasSincronizadasAll(Utils.getDataBase(this)).size());
        if(idLlamadaRefencia==0){
            setResult(RESULT_LLAMADA_NUEVO);
        }else{
            setResult(RESULT_LLAMADA_REPROGRAMADA);
        }

        finish();
    }

    private void showConfirm(){
        AlertDialog dialog = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle).
                setNegativeButton("Cancelar", (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton("Aceptar", (dialog12, which) -> {
                    try{
                        if(validateCurrentTime()){
                            saveData();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).
                setMessage("¿Desea agendar llamada?")
                .setTitle("RP3 Market Force")
                .setCancelable(false)
                .create();
        dialog.show();

    }

    private void init(){
        fecha = Calendar.getInstance();
        fechaLlamada = Calendar.getInstance();
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                fabNuevo.hide();
            } else {
                fabNuevo.show();
            }
        });
        fecha_hora = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        setCalendar();
        tvLlamadaHora.setText(format1.format(fecha.getTime()));
        tvLlamadaHora.setOnClickListener(v -> showDialogTimePicker(1, fecha_hora.get(Calendar.HOUR_OF_DAY), fecha_hora.get(Calendar.MINUTE), TIME_PICKER_INTERVAL));
        fabNuevo.setOnClickListener(v -> {
            try {
                if(validateCurrentTime()){
                    showConfirm();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    public void showDialogTimePicker(int id, int hour, int minute, int interval){
        DialogTimePickerFragment f = new DialogTimePickerFragment(id, this, hour, minute, interval);
        f.setStyle(R.style.AppCompatAlertDialogStyle,R.style.AppThemeMD);
        f.show(getSupportFragmentManager(),"timepicker");
    }

    private void setCalendar() {
        Log.d(TAG,"setCalendar...");
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.crear_llamada_calendarnew, caldroidFragment);
        t.commit();

        caldroidFragment.setMinDate(Calendar.getInstance().getTime());
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Log.d(TAG,"onSelectDate...");
                caldroidFragment.setCalendarDate(date);
                caldroidFragment.setBackgroundResourceForDate(R.color.whiteA80, fecha.getTime());
                caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, date);
                int horas = fecha.get(Calendar.HOUR_OF_DAY);
                int minutos = fecha.get(Calendar.MINUTE);
                fecha.setTime(date);
                fecha.set(Calendar.HOUR_OF_DAY, horas);
                fecha.set(Calendar.MINUTE, minutos);
                tvLlamadaHora.setText(format1.format(fecha.getTime()));
                fechaLlamada.setTime(date);
                fechaLlamada.setTime(fecha.getTime());
            }

            @Override
            public void onChangeMonth(int month, int year) {
                Log.d(TAG,"onChangeMonth...");
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Log.d(TAG,"onLongClickDate...");
            }

            @Override
            public void onCaldroidViewCreated() {
                Log.d(TAG,"onCaldroidViewCreated...");
            }

        };
        caldroidFragment.setCaldroidListener(listener);
        caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, Calendar.getInstance().getTime());
        caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, fecha.getTime());
    }

    private void getData(){
        if(SessionManager.getInstance(this).getLlamada()!=null){
            Log.d(TAG,"getLlamada!=null...");
            SessionManager.getInstance(this).removeLlamada();
            ProspectoVtaDb obj = SessionManager.getInstance(this).getProspectoLlamada();
            Log.d(TAG,obj.toString());
            id = obj.getID();
            idProspecto = obj.getIdProspecto();
            prospecto = obj.getNombre();
            estado = obj.getEstado();
            idLlamadaRefencia = LlamadaVta.getMaxIdLlamadaVta(Utils.getDataBase(this));
            getSupportActionBar().setTitle("Reprogramar Llamada");
            SessionManager.getInstance(this).removeProspectoLlamada();
        }else{
            Log.d(TAG,"getLlamada==null...");
            id = getIntent().getExtras().getLong("Id",0);
            idProspecto = getIntent().getExtras().getInt("IdProspecto",0);
            prospecto = getIntent().getExtras().getString("Prospecto",null);
            estado = getIntent().getExtras().getInt("Estado",0);
            idLlamadaRefencia = 0;
        }
        latitud = getIntent().getExtras().getDouble("Latitud",0);
        longitud = getIntent().getExtras().getDouble("Longitud",0);
        if(latitud==0 && longitud==0){
            Log.d(TAG,"No llego coordenadas...");
        }
        else{
            Log.d(TAG,"Si llego...");
        }
        idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        tvProspecto.setText(prospecto);
        Log.d(TAG,"idLlamadareferencia:"+idLlamadaRefencia);
    }

    //region General

    public void toolbarStatusBar() {
        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agendar Llamada");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void navigationBarStatusBar() {
        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CrearLlamadaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBarLlamada);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                CrearLlamadaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBarLlamada);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CrearLlamadaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBarLlamada);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                CrearLlamadaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    //endregion

    //region Ciclo de vida

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(i == android.R.id.home){
            if(idLlamadaRefencia==0){
                finish();
            }
            else{
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
        locationProvider.disconnect();
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
        if(idLlamadaRefencia==0){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        try{
            registerReceiver(gpsReceiver,new IntentFilter("android.location.PROVIDERS_CHANGED"));
        }catch (IllegalArgumentException e) {
            Log.d(TAG,"receiver:"+e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
        SessionManager.getInstance(this).removeLlamada();
        unregisterReceiver(gpsReceiver);
        locationProvider.disconnect();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d(TAG,"onBackPressed...");
        if(idLlamadaRefencia==0){
            finish();
        }
        else{
            moveTaskToBack(true);
        }
    }

    @Override
    public void onDailogTimePickerChange(int id, int hours, int minutes) {
        Log.d(TAG,"onDailogTimePickerChange..."+id+hours+minutes);
        fecha.set(Calendar.HOUR_OF_DAY, hours);
        fecha.set(Calendar.MINUTE, minutes);
        fechaLlamada.set(Calendar.HOUR_OF_DAY, hours);
        fechaLlamada.set(Calendar.MINUTE, minutes);
        tvLlamadaHora.setText(format1.format(fecha.getTime()));
        Log.d(TAG,"Horas:"+hours+" minutes:"+minutes);
        //super.onDailogTimePickerChange(id, hours, minutes);
    }

    //endregion

    //region Location
    public void initLocation(){
        locationProvider=new LocationProvider(this,this);
        locationProvider.connect();
    }

    @Override
    public void handleNewLocation(Location location) {
        //Log.d(TAG,"handleNewLocation...");
        if(this.location==null){
            //Log.d(TAG,"location==null...");
            if(location!=null){
                //Log.d(TAG,"lastLocation!=null...");
                this.location = location;
            }else{
                // Log.d(TAG,"lastLocation==null...");
            }
        }else{
            //Log.d(TAG,"location!=null...");
            if(location!=null){
                // Log.d(TAG,"lastLocation!=null...");
                this.location = location;
            }else{
                // Log.d(TAG,"lastLocation==null...");
            }
        }
    }

    @Override
    public void handleConnectionFailed(String failed) {
        Log.d(TAG,"handleConnectionFailed...");
        Log.d(TAG,"handleConnectionFailed:"+failed);
    }

    @Override
    public void handleConnectionSuccess(String success) {
        Log.d(TAG,"handleConnectionSuccess...");
        Log.d(TAG,"handleConnectionSuccess:"+success);
    }

    @Override
    public void handleSettingsSuccess(String success) {
        Log.d(TAG,"handleSettingsSuccess...");
        Log.d(TAG,"handleSettingsSuccess:"+success);
    }

    @Override
    public void handleSettingsResolutionRequired(String resolutionRequired, Status status, LocationSettingsStates state) {
        Log.d(TAG,"handleSettingsResolutionRequired...");
        Log.d(TAG,"handleSettingsResolutionRequired:"+resolutionRequired);
        try {
            this.status=status;
            GPS = false;
            status.startResolutionForResult(this,REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            Log.d(TAG,"IntentSender.SendIntentException:"+e.getLocalizedMessage());
        }
    }

    @Override
    public void handleSettingsChangeUnavailable(String changeUnavailable) {
        Log.d(TAG,"handleSettingsChangeUnavailable...");
        Log.d(TAG,"changeUnavailable:"+changeUnavailable);
    }

    @Override
    public void handleLastLocation(Location lastLocation) {
        Log.d(TAG,"handleLastLocation...");
        if(location==null){
            // Log.d(TAG,"location==null...");
            if(lastLocation!=null){
                // Log.d(TAG,"lastLocation!=null...");
                location = lastLocation;
            }else{
                // Log.d(TAG,"lastLocation==null...");
            }
        }else{
            //  Log.d(TAG,"location!=null...");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CHECK_SETTINGS){
            if(resultCode == RESULT_OK){
                Log.d(TAG,"RESULT_OK FOR GOOGLEAPICLIENTE LOCATION");
                GPS = true;
            }else if(resultCode == RESULT_CANCELED){
                Log.d(TAG,"RESULT_CANCELED FOR GOOGLEAPICLIENT LOCATION");
                GPS =false;
                showDialogGps();
                if(status!=null){
                    Log.d(TAG,"status!=null...");
                    try {
                        status.startResolutionForResult(this,REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        Log.d(TAG,"IntentSender.SendIntentException e...");
                        Log.d(TAG,e.getLocalizedMessage());
                    }
                }
            }
        }
        else if(requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_CANCELED){
            Log.d(TAG,"REQUEST_CHECK_SETTINGS...RESULT_CANCELED...");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                Log.d(TAG,"providers changed...");
                final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
                Bundle msj = new Bundle();
                if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    //do something
                    Log.d(TAG,"Prendido");
                    GPS = true;
                    if(dialogGps!=null){
                        if(dialogGps.isShowing()){
                            dialogGps.dismiss();
                        }
                    }
                    Log.d(TAG,"GPS encendido...");
                }
                else
                {
                    Log.d(TAG,"Apagado");
                    if(dialogGps!=null){
                        if(dialogGps.isShowing()){
                            dialogGps.dismiss();
                        }
                    }
                    showDialogGps();
                    GPS = false;
                    if(status!=null){
                        Log.d(TAG,"status!=null...");
                        Log.d(TAG,"status!=null...");
                        try {
                            status.startResolutionForResult(CrearLlamadaActivity.this,REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            Log.d(TAG,"IntentSender.SendIntentException e...");
                            Log.d(TAG,e.getLocalizedMessage());
                        }
                    }else{
                        Log.d(TAG,"status==null...");
                        locationProvider.request();
                    }
                }
            }
        }
    };

    private void showDialogGps(){
        if(dialogGps!=null){dialogGps.dismiss();}
        dialogGps = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle)
                .setTitle(getResources().getString(R.string.appname_marketforce))
                .setMessage("Debe Mantener el GPS encendido porfavor.")
                .setCancelable(false)
                .setNeutralButton(R.string.gpsdialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"Activar GPS onclickdialog...");
                        locationProvider.request();
                    }
                }).create();
        dialogGps.show();
    }


    //endregion
}
