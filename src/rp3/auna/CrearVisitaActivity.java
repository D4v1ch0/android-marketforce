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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationSettingsStates;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import com.google.android.gms.common.api.Status;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.app.DialogTimePickerChangeListener;
import rp3.app.DialogTimePickerFragment;
import rp3.auna.db.Contract;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.util.location.LocationProvider;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;

public class CrearVisitaActivity extends ActionBarActivity implements DialogTimePickerChangeListener,
        rp3.auna.util.location.LocationProvider.LocationCallback{

    private static final String TAG = CrearVisitaActivity.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS=1000;
    private static final int RESULT_VISITA_REPROGRAMADA = 12;
    private static final int RESULT_VISITA_NUEVO = 5;
    private static final int RESULT_VISITA_NUEVA_PAGO_FISICO = 11;
    private static final int RESULT_VISITA_NUEVA_LLAMADA = 12;
    private static final int RESULT_VISITA_REPROGRAMADA_FINALIZADA = 12;
    private static final int RESULT_LLAMADA_SI_DESEA_PROGRAMA = 33;
    private static final int ID_DURACION = 0;
    private static final int ID_TIEMPO = 1;
    @BindView(R.id.toolbarNuevaVisita)Toolbar toolbar;
    @BindView(R.id.statusBarVisita)FrameLayout statusBar;
    @BindView(R.id.scrollViewNuevaVisita)NestedScrollView scrollView;
    @BindView(R.id.fabNuevaVisita)FloatingActionButton fabNuevo;
    @BindView(R.id.crear_visita_prospecto_clientevisita)TextView tvProspecto;
    @BindView(R.id.crear_visita_prospecto_direccionvisita)TextView tvDireccion;
    @BindView(R.id.crear_visita_prospecto_desde_textvisita) TextView tvLlamadaHora;
    @BindView(R.id.etCrearVisitaDescripcionvisita)EditText etDescripcion;
    @BindView(R.id.tvDuracionEstimado) TextView tvDuracionEstimada;
    @BindView(R.id.tvTiempoEstimado) TextView tvTiempoEstimada;
    @BindView(R.id.crear_visita_prospecto_calendarvisita)FrameLayout frameLayoutCalendar;
    private Calendar fecha, fecha_hora,fechaLlamada;
    private int TIME_PICKER_INTERVAL = 5;
    private SimpleGeneralValueAdapter adapterDuracion;
    private List<GeneralValue> generalValueList;
    private String[] arrayDuracion;
    private ArrayList<String> list;
    private CaldroidFragment caldroidFragment;
    private SimpleDateFormat format1 = new SimpleDateFormat(Contants.DATE_TIME_FORMAT_HH_MM);
    private int idAgente;
    private long id;
    private int idProspecto;
    private String prospecto;
    private int estado;
    private String direccion;
    private AgendaVta agendaVta;
    private VisitaVta visita;
    private double latitud=0;
    private double longitud=0;
    private String duracionCode;
    private String tiempoCode;
    //Variable Service
    private int service = 1;
    private int llamadaId = 0;
    private int visitaId = 0;

    public Location location;
    private LocationProvider locationProvider;
    private Status status;
    private boolean GPS = false;
    private AlertDialog dialogGps;

    /**
     * Estados:
     * 1.-Se creara una visita comun y corriente sobre un prospecto seleccionado
     * 2.-Se creara una visita por una Llamada gestionada
     * 3.-Se creara una visita por una visita finalizada(Tipo pago fisico)
     * 4.-Se creara una visita por una visita finalizada(Tipo pago pasarela de pago)
     * 5.-Se creara una visita por una visita pendiente(Se inicio y se cotizo pero no se concreto la venta y se reprogramo)
     * 6.-Se creara una visita por una visita reprogramada(Se eligio la opcion reprogramar y debe pasarse su VisitaID)
     * 7.-Se creara una visita por una visita reprogramada(Eligiendo reprogramar desde tab)
     * 8.-Se creara una visita por una visita finalizada(Tipo pago regular)
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_visita2);
        ButterKnife.bind(this);
        initLocation();
        toolbarStatusBar();
        navigationBarStatusBar();
        getData();
        init();
        initTiempos();
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

    //Validar Mismo Tiempo

    private void initTiempos(){
        adapterDuracion = new SimpleGeneralValueAdapter(this, Utils.getDataBase(this), Contants.GENERAL_TABLE_DURACION_VISITA);
        generalValueList = GeneralValue.getGeneralValues(Utils.getDataBase(this),Contants.GENERAL_TABLE_DURACION_VISITA);


        for(int i = 0;i<generalValueList.size();i++){
            if(generalValueList.get(i).getReference3()!=null){
                if(generalValueList.get(i).getReference3().equalsIgnoreCase("0")){
                    generalValueList.remove(i);
                    break;
                }
            }

        }
        Collections.sort(generalValueList,
                new Comparator<GeneralValue>()
                {
                    public int compare(GeneralValue o1,
                                       GeneralValue o2)
                    {
                        Log.d(TAG,"o1:"+o1.getReference2());
                        Integer s1 = Integer.parseInt(o1.getReference2());
                        Integer s2 = Integer.parseInt(o2.getReference2());
                        if (s1 == s2)
                        {
                            return 0;
                        }
                        else if (s1 < s2)
                        {
                            return -1;
                        }
                        return 1;
                    }
                });
         list = new ArrayList<>();
        for(int j = 0;j<generalValueList.size();j++){
            list.add(generalValueList.get(j).getValue());
        }
        tvDuracionEstimada.setText(generalValueList.get(0).getValue());
        tvTiempoEstimada.setText(generalValueList.get(0).getValue());
        duracionCode = generalValueList.get(0).getCode();
        tiempoCode = generalValueList.get(0).getCode();

        tvDuracionEstimada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDuracion(ID_DURACION);
            }
        });
        tvTiempoEstimada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDuracion(ID_TIEMPO);
            }
        });
    }

    public void showDuracion(final int type) {
        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.row_single_choice, list);
        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == ID_DURACION) {
                            duracionCode = (generalValueList.get(which).getCode());
                            tvDuracionEstimada.setText(generalValueList.get(which).getValue());
                            Log.d(TAG,"DURACION= Code:"+duracionCode+" value:"+generalValueList.get(which).getValue());
                            dialog.dismiss();
                        } else if (type == ID_TIEMPO) {
                            tiempoCode = (generalValueList.get(which).getCode());
                            tvTiempoEstimada.setText(generalValueList.get(which).getValue());
                            Log.d(TAG,"TIEMPO= Code:"+duracionCode+" value:"+generalValueList.get(which).getValue());
                            dialog.dismiss();
                        }

                    }
                });
        builderSingle.show();
    }

    private void saveData(){
        visita =new VisitaVta();
        Log.d(TAG,"fecha Visita:"+fechaLlamada.getTime());
        visita.setFechaVisita(fechaLlamada.getTime());
        visita.setFotos(null);
        visita.setInsertado(1);
        if(TextUtils.isEmpty(etDescripcion.getText().toString().trim())){
            visita.setDescripcion(null);
        }else{
            visita.setDescripcion(etDescripcion.getText().toString().trim());
        }
        visita.setEstado(1);
        visita.setFechaFin(null);
        visita.setFechaInicio(null);
        visita.setIdAgente(idAgente);
        if(estado==1){
            Log.d(TAG,"el prospecto esta en BD...");
            visita.setIdCliente(Integer.parseInt(String.valueOf(id)));
        }else{
            Log.d(TAG,"el prospecto esta en servidor...");
            visita.setIdCliente(idProspecto);
        }
        visita.setIdClienteDireccion(direccion);
        if(location!=null){
            if(location.getLatitude()>0 && location.getLongitude()>0){
                visita.setLatitud(location.getLatitude());
                visita.setLongitud(location.getLongitude());
            }else{
                visita.setLatitud(latitud);
                visita.setLongitud(longitud);
            }
        }else{
            visita.setLatitud(latitud);
            visita.setLongitud(longitud);
        }
        visita.setMotivoReprogramacionTabla(0);
        visita.setMotivoReprogramacionValue(null);//Indisponibilidad del cliente
        visita.setObservacion(null);
        visita.setMotivoVisitaTabla(Contants.GENERAL_TABLE_ESTADOS_VISITA);
        visita.setMotivoVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE);
        visita.setReferidoTabla(Contants.GENERAL_TABLE_ESTADOS_CONSULTA_REFERIDO);
        visita.setReferidoValue(Contants.GENERAL_VALUE_CONSULTA_REFERIDO_NO);
        visita.setVisitaTabla(Contants.GENERAL_TABLE_ESTADOS_VISITA);
        visita.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE);
        visita.setTiempoCode(tiempoCode);
        visita.setDuracionCode(duracionCode);
        if(service==3){
            visita.setVisitaId(visitaId);
            visita.setEstado(3);
        }else if(service==5){
            visita.setVisitaId(visitaId);
            visita.setEstado(8);
        }
        else if(service==6){
            visita.setVisitaId(visitaId);
            visita.setEstado(6);
        }
        else if(service==2){
            visita.setLlamadaId(llamadaId);
            visita.setEstado(2);
        }
        visita.setInsertado(1);

        boolean vi = VisitaVta.insert(Utils.getDataBase(this),visita);
        Log.d(TAG,vi?"Visita insertada...":"Visita no se inserto...");
        Log.d(TAG,"Visita:"+visita.toString());
        Log.d(TAG,"Cantidad de visitas en Bd temp:"+ VisitaVta.getVisitasInsert(Utils.getDataBase(this)).size());
        if(service==1){
            Log.d(TAG,"Service==1:Enviar RESULT_VISITA_NUEVO...");
            setResult(RESULT_VISITA_NUEVO);
            finish();
        }else if(service==3){
            Log.d(TAG,"Service==3:Enviar RESULT_VISITA_NUEVA_PAGO_FISICO...");
            setResult(RESULT_VISITA_NUEVA_PAGO_FISICO);
            finish();
        }else if(service==5){
            Log.d(TAG,"Service==5:Enviar RESULT_VISITA_NUEVA_LLAMADA...");
            setResult(RESULT_VISITA_REPROGRAMADA);
            finish();
        }
        else if(service==6){
            Log.d(TAG,"Service==6:Enviar RESULT_VISITA_REPROGRAMADA_FINALIZADA...");
            Log.d(TAG,"finalizar volver a la cotizacion matarla e ir al main...");
            setResult(RESULT_VISITA_REPROGRAMADA_FINALIZADA);
            finish();
        }
        else if(service==2){
            Log.d(TAG,"Service==6:Enviar RESULT_VISITA_REPROGRAMADA_FINALIZADA...");
            setResult(RESULT_LLAMADA_SI_DESEA_PROGRAMA);
            finish();
        }

    }

    //region init

    private void init(){
        fecha = Calendar.getInstance();
        fechaLlamada = Calendar.getInstance();
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fabNuevo.hide();
                } else {
                    fabNuevo.show();
                }
            }
        });
        setCalendar();
        fecha_hora = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        tvLlamadaHora.setText(format1.format(fecha.getTime()));
        tvLlamadaHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimePicker(1, fecha_hora.get(Calendar.HOUR_OF_DAY), fecha_hora.get(Calendar.MINUTE), TIME_PICKER_INTERVAL);
            }
        });
        fabNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(validateCurrentTime()){
                        showConfirm();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
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
        t.replace(R.id.crear_visita_prospecto_calendarvisita, caldroidFragment);
        t.commit();

        caldroidFragment.setMinDate(Calendar.getInstance().getTime());

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Log.d(TAG,"onSelectDate...");
                caldroidFragment.setCalendarDate(date);
                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, fecha.getTime());
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

    public void toolbarStatusBar() {
        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agendar Visita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void navigationBarStatusBar() {
        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CrearVisitaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                CrearVisitaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CrearVisitaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                CrearVisitaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    private void getData(){
        id = getIntent().getExtras().getLong("Id",0);
        idProspecto = getIntent().getExtras().getInt("IdProspecto",0);
        prospecto = getIntent().getExtras().getString("Prospecto",null);
        estado = getIntent().getExtras().getInt("Estado",0);
        direccion = getIntent().getExtras().getString("Direccion",null);
        latitud = getIntent().getExtras().getDouble("Latitud",0);
        longitud = getIntent().getExtras().getDouble("Longitud",0);
        idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE,0);
        tvProspecto.setText(prospecto);
        tvDireccion.setText(direccion);
        service = getIntent().getExtras().getInt("Service",1);
        llamadaId = getIntent().getExtras().getInt("LlamadaId",0);
        visitaId = getIntent().getExtras().getInt("VisitaId",0);
        if(service==3){
            getSupportActionBar().setTitle("Proxima Visita");
            Log.d(TAG,"VisitaId:"+visitaId);
            Log.d(TAG,"LlamadaId:"+llamadaId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }else if(service == 5){
            getSupportActionBar().setTitle("Reprogramar Visita");
            Log.d(TAG,"visitaId:"+visitaId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        else if(service == 6){
            getSupportActionBar().setTitle("Reprogramar Visita");
            Log.d(TAG,"VisitaId:"+visitaId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        else if(service == 2){
            getSupportActionBar().setTitle("Proxima Visita");
            Log.d(TAG,"LlamadaId:"+llamadaId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }

    private void showConfirm(){
        AlertDialog dialog = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle).
                setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            if(validateCurrentTime()){
                                saveData();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).
                        setMessage("¿Desea agendar Visita?")
                .setTitle("RP3 Market Force")
                .setCancelable(false)
                .create();
        dialog.show();

    }

    public void showDialogTimePicker(int id, int hour, int minute, int interval){
        DialogTimePickerFragment f = new DialogTimePickerFragment(id, this, hour, minute, interval);
        f.show(getSupportFragmentManager(),"timepicker");
    }

    //endregion

    //region Ciclo de vida

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(i == android.R.id.home){
            onBackPressed();
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

        if(service==1){
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
        if(service==1){
            super.onBackPressed();
            finish();
        }
        else if(service==3 || service ==5 || service == 6 || service == 2){
            moveTaskToBack(true);
        }
    }

    @Override
    public void onDailogTimePickerChange(int id, int hours, int minutes) {
        fecha.set(Calendar.HOUR_OF_DAY, hours);
        fecha.set(Calendar.MINUTE, minutes);
        fechaLlamada.set(Calendar.HOUR_OF_DAY, hours);
        fechaLlamada.set(Calendar.MINUTE, minutes);
        tvLlamadaHora.setText(format1.format(fecha.getTime()));
        Log.d(TAG,"Horas:"+hours+" minutes:"+minutes);
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
                            status.startResolutionForResult(CrearVisitaActivity.this,REQUEST_CHECK_SETTINGS);
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


    //Holaaa
}
