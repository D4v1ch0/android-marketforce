package rp3.auna;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewDatabase;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rp3.auna.bean.SignIn;
import rp3.auna.bean.SolicitudMovil;
import rp3.auna.bean.VisitaVta;
import rp3.auna.dialog.MotivoCitaDialog;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.util.helper.Util;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.auna.webservices.SignInClient;
import rp3.auna.webservices.SolicitudClient;
import rp3.auna.webservices.UpdateVisitaClient;
import rp3.runtime.Session;
import rp3.util.Convert;

import static rp3.auna.Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA;

public class CaptacionSolicitudActivity extends AppCompatActivity {
    private static final String TAG = CaptacionSolicitudActivity.class.getSimpleName();
    private static final int REQUEST_VISITA_SOLICITUD_INICIO = 10;
    private static final int RESULT_VISITA_SOLICITUD_COTIZACION_INICIAL_OK = 10;
    private static final int RESULT_VISITA_SOLICITUD_COTIZACION_INICIO_CANCELADA = 11;
    private static final int REQUEST_VISITA_REPROGRAMADA_FINALIZADA = 12;
    private static final int RESULT_VISITA_REPROGRAMADA_FINALIZADA = 12;
    //Control de cambio, se solicito que se posicione en el Tab de Visita al reprogamar ó cancelar la visita
    private static final int RESULT_VISITA_REPROGRAMM_CANCEL = 34;
    private boolean estadoActivity = true;
    @BindView(R.id.toolbarCaptacionSolicitud) Toolbar toolbar;
    @BindView(R.id.statusBarCaptacion) FrameLayout frameLayout;
    @BindView(R.id.containerOff) RelativeLayout containerOff;
    @BindView(R.id.containerweb) WebView webView;
    private String oAuthClientId = "android-app-marketforce";
    private String oAuthClientSecret = "rp3-marketforce2014";
    private String mAuthTokenType = "rp3.marketforce";
    private String mAuthToken;
    private String ContentType = "application/json";
    private String logonName = Session.getUser().getLogonName();
    private String password = Session.getUser().getPassword();
    private SignIn obj;
    private int idVisita;
    private String tipoSolicitud;
    private int tipoVenta;
    private String programa;
    private String costo;
    private int esNuevo;
    private boolean initPage = false;
    private rp3.auna.models.ventanueva.VisitaVta visitaVta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captacion_solicitud);
        ButterKnife.bind(this);
        Log.d(TAG,"onCreate...");
        toolbarStatusBar();
        navigationBarStatusBar();
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_cancelar_visita){
            Log.d(TAG,"Cancelar visita...");
            showDialogVisitaReprogrammar(2);
        }
        if(id==R.id.action_agendar_visita){
            Log.d(TAG,"Reprograma visita...");
            showDialogVisitaReprogrammar(1);
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        visitaVta = SessionManager.getInstance(this).getVisitaSession();
        idVisita = getIntent().getIntExtra("IdVisita",0);
        tipoSolicitud = getIntent().getStringExtra("Tipo");
        tipoVenta = getIntent().getIntExtra("Venta",0);
        programa = getIntent().getStringExtra("Programa");
        costo = getIntent().getStringExtra("Costo");
        esNuevo = getIntent().getIntExtra("EsNuevo",1);
        if(tipoSolicitud.equalsIgnoreCase("F")){
            getSupportActionBar().setTitle("Registro de Solicitud Fisica");
        }else{
            getSupportActionBar().setTitle("Registro de Solicitud Virtual");
        }
        if(validate()){
            containerOff.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            final ProgressDialog progressdialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
            progressdialog.setCancelable(false);
            progressdialog.setTitle("RP3 Market Force");
            progressdialog.setMessage("Cargando...espere porfavor...");
            progressdialog.show();
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.d(TAG,"shouldOverrideUrlLoading...");
                    String nuevo= getApplicationContext().getResources().getString(R.string.urlapi)+"Captacion/Solicitud/Inicio";
                    Log.d(TAG,"UrlChange:"+url);
                    if(url.equalsIgnoreCase(nuevo)){
                        getAfiliado();
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    Log.d(TAG,"2show...");
                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    Log.d(TAG,"onPageStarted...");
                    if(initPage){
                        if(progressdialog!=null){
                            if(progressdialog.isShowing()){
                                progressdialog.dismiss();
                                progressdialog.setTitle("RP3 Market Force");
                                progressdialog.setMessage("Cargando...espere porfavor...");
                                //progressdialog.show();
                            }else{

                                //progressdialog = new ProgressDialog(getBaseContext(),R.style.AppCompatAlertDialogStyle);
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressdialog.setCancelable(false);
                                        progressdialog.dismiss();
                                        progressdialog.setTitle("RP3 Market Force");
                                        progressdialog.setMessage("Cargando...espere porfavor...");
                                        if(estadoActivity){
                                            progressdialog.show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    Log.d(TAG,"onReceivedSslError...");
                    if(progressdialog!=null){
                        if(progressdialog.isShowing()){
                            progressdialog.dismiss();
                        }
                    }
                    super.onReceivedSslError(view, handler, error);
                }

                public void onPageFinished(WebView view, String url) {
                    Log.d(TAG,"onPageFinished...");
                    if(!initPage){
                        if(progressdialog!=null){
                            if(progressdialog.isShowing()){
                                progressdialog.dismiss();
                            }
                        }
                    }
                    initPage = true;

                }
            });
            webView.getSettings().setJavaScriptEnabled(true);
            String urlLogin = (getResources().getString(R.string.urlapi)+"Captacion/Solicitud/"+
                    "LinkSolicitudMovil?logonName="+logonName+
                    "&password="+password+
                    "&idVisita="+idVisita+
                    "&tipoSolicitud="+tipoSolicitud+
                    "&tipoVenta="+(tipoVenta==1?"N":"J")+
                    "&CostoProgramaTotal="+costo+
                    "&IdPrograma="+programa+
                    "&esNuevo="+esNuevo);
            Log.d(TAG,"URL:"+urlLogin);
            webView.loadUrl(urlLogin);
        }else{
            containerOff.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            Toast.makeText(this, "Sin conexion...", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAfiliado(){
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.show();
        new SolicitudClient(this, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CaptacionSolicitudActivity.this, "Hubo un problema en el servidor intentelo nuevamente...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            Log.d(TAG,"response.isSuccessful...");
                            String json = null;
                            try {
                                json = response.body().string();
                                SolicitudMovil solicitudMovil = new Gson().fromJson(json,SolicitudMovil.class);
                                SessionManager.getInstance(getApplicationContext()).createSessionSolicitud(solicitudMovil);
                            } catch (IOException e) {
                                Log.d(TAG,"IOException:"+e.getMessage());
                                e.printStackTrace();
                            }
                            Log.d(TAG,json);
                            setResult(RESULT_VISITA_SOLICITUD_COTIZACION_INICIAL_OK);
                            finish();
                        }else{
                            Log.d(TAG,"!response.isSuccessful...");
                            Toast.makeText(CaptacionSolicitudActivity.this, "Hubo un problema intentelo nuevamente...", Toast.LENGTH_SHORT).show();
                            initPage = false;
                            String urlLogin = (getResources().getString(R.string.urlapi)+"Captacion/Solicitud/"+
                                    "LinkSolicitudMovil?logonName="+logonName+
                                    "&password="+password+
                                    "&idVisita="+idVisita+
                                    "&tipoSolicitud="+tipoSolicitud+
                                    "&tipoVenta="+(tipoVenta==1?"N":"J")+
                                    "&CostoProgramaTotal="+costo+
                                    "&IdPrograma="+programa+
                                    "&esNuevo="+esNuevo);
                            Log.d(TAG,"URL:"+urlLogin);
                            webView.getSettings().setLoadWithOverviewMode(true);
                            webView.getSettings().setUseWideViewPort(true);
                            webView.getSettings().setSupportZoom(true);
                            webView.getSettings().setBuiltInZoomControls(false);
                            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                            webView.getSettings().setDomStorageEnabled(true);
                            webView.setHorizontalScrollBarEnabled(true);
                            webView.setVerticalScrollBarEnabled(true);
                            webView.setScrollbarFadingEnabled(false);
                            webView.setVerticalFadingEdgeEnabled(false);
                            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                            } else {
                                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                            }
                            webView.loadUrl(urlLogin);
                        }
                    }
                });
            }
        }).obtener(idVisita);
    }

    //region Opciones en la visita

    //region Reprogramacion o Cancelacion de la Visita

    private void showDialogVisitaReprogrammar(final int tipo){
        Bundle arg = new Bundle();
        arg.putInt("Tipo",tipo);
        arg.putInt("Repro",2);
        final MotivoCitaDialog dialog = MotivoCitaDialog.newInstance(new MotivoCitaDialog.callbackElegir() {
            @Override
            public void onGeneralSelected(String code,String motivo) {
                if(tipo == 1){
                    reprogramarVisita(tipo,code,motivo);
                }else{
                    reprogramarVisita(tipo,code,motivo);
                }
            }
        });
        dialog.setArguments(arg);
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(),"MotivoDialog");
    }

    private void reprogramarVisita(int tipo,String code,String motivo){
        if(tipo==1){
            showUpdateVisitaClient(1,code,motivo,1);
        }else{
            showUpdateVisitaClient(1,code,motivo,2);
        }
    }

    private void showUpdateVisitaClient(final int estado, String code, String motivo, final int tipo){
        Log.d(TAG,"showUpdateVisitaClient estado:"+estado);
        visitaVta.setEstado(estado);
        visitaVta.setFechaFin(Calendar.getInstance().getTime());
        visitaVta.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA);
        //Iniciar las visitas para enviar
        rp3.auna.bean.VisitaVta obj = new rp3.auna.bean.VisitaVta();
        obj.setEstado(visitaVta.getEstado());
        obj.setLlamadaId(0);
        if(String.valueOf(visitaVta.getVisitaId())!=null){
            obj.setVisitaId(visitaVta.getVisitaId());
        }else{
            obj.setVisitaId(0);
        }
        obj.setIdAgente(visitaVta.getIdAgente());
        obj.setIdCliente(visitaVta.getIdCliente());
        obj.setDuracionCode(visitaVta.getDuracionCode());
        obj.setFechaFin(Convert.getDotNetTicksFromDate(visitaVta.getFechaFin()));
        obj.setFechaInicio(Convert.getDotNetTicksFromDate(visitaVta.getFechaInicio()));
        obj.setFechaVisita(Convert.getDotNetTicksFromDate(visitaVta.getFechaVisita()));
        obj.setDescripcion(visitaVta.getDescripcion());
        obj.setFotos(null);
        obj.setReferidoCount(0);
        obj.setReferidoTabla(visitaVta.getReferidoTabla());
        obj.setLatitud(Float.parseFloat(String.valueOf(visitaVta.getLatitud())));
        obj.setLongitud(Float.parseFloat(String.valueOf(visitaVta.getLongitud())));
        obj.setIdClienteDireccion(visitaVta.getIdClienteDireccion());
        obj.setIdVisita(visitaVta.getIdVisita());
        if(code!=null && tipo == 1){
            visitaVta.setObservacion(motivo);
            visitaVta.setMotivoReprogramacionValue(code);
            visitaVta.setMotivoReprogramacionTabla(GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA);
        }else if(code !=null && tipo ==2){
            visitaVta.setObservacion(motivo);
            visitaVta.setMotivoReprogramacionValue(code);
            visitaVta.setMotivoReprogramacionTabla(Contants.GENERAL_TABLE_MOTIVOS_CANCELAR_CITA_TABLE_ID);
        }
        obj.setMotivoReprogramacionTabla(visitaVta.getMotivoReprogramacionTabla());
        obj.setMotivoReprogramacionValue(visitaVta.getMotivoReprogramacionValue());
        obj.setMotivoVisitaTabla(visitaVta.getMotivoVisitaTabla());
        obj.setMotivoVisitaValue(visitaVta.getMotivoVisitaValue());
        obj.setObservacion(visitaVta.getObservacion());
        obj.setTipoVenta(visitaVta.getTipoVenta());
        obj.setVisitaTabla(visitaVta.getVisitaTabla());
        obj.setVisitaValue(visitaVta.getVisitaValue());
        obj.setTiempoCode(visitaVta.getTiempoCode());
        obj.setReferidoValue(visitaVta.getReferidoValue());
        List<VisitaVta> list = new ArrayList<>();
        list.add(obj);
        //
        String json = new Gson().toJson(list);
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("RP3 Market Force");
        progressDialog.setMessage("Finalizando Visita...");
        progressDialog.show();
        new UpdateVisitaClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CaptacionSolicitudActivity.this, getResources().getString(R.string.message_error_sync_general_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            Log.d(TAG,"isSucessful...");
                            boolean result = rp3.auna.models.ventanueva.VisitaVta.update(Utils.getDataBase(getApplicationContext()),visitaVta);
                            if(result){
                                Log.d(TAG,"Se actualizo visita...");
                            }else{
                                Log.d(TAG,"No se actualizo visita...");
                            }
                            //Reprogramar Visita
                            if(tipo==1){
                                Intent intent1 = new Intent(getApplicationContext(),CrearVisitaActivity.class);
                                Bundle todo = new Bundle();
                                ProspectoVtaDb prospecto = null;
                                Log.d(TAG,"Id Prospecto en visita:"+visitaVta.getIdCliente());
                                List<ProspectoVtaDb> list = ProspectoVtaDb.getProspectosActualizados(Utils.getDataBase(getApplicationContext()));
                                if(list!=null){
                                    Log.d(TAG,"list!=null: Prospectos Sincronizados:"+list.size());
                                    for(ProspectoVtaDb obj:list){
                                        if(visitaVta.getIdCliente()==obj.getIdProspecto()){
                                            prospecto = obj;
                                            Log.d(TAG,"break:"+prospecto.toString());
                                            break;
                                        }
                                    }
                                    todo.putInt("Service",6);
                                    todo.putLong("Id",prospecto.getID());
                                    todo.putInt("IdProspecto",prospecto.getIdProspecto());
                                    todo.putString("Prospecto",prospecto.getNombre());
                                    todo.putString("Direccion",visitaVta.getIdClienteDireccion());
                                    todo.putInt("Estado",0);
                                    todo.putDouble("Latitud",visitaVta.getLatitud());
                                    todo.putDouble("Longitud",visitaVta.getLongitud());
                                    todo.putInt("VisitaId",visitaVta.getIdVisita());
                                    intent1.putExtras(todo);
                                    startActivityForResult(intent1,REQUEST_VISITA_REPROGRAMADA_FINALIZADA);
                                }
                                else{
                                    Log.d(TAG,"list==null");
                                }
                                //Cancelar Visita
                            }else if(tipo==2){
                                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                                //startActivity(intent);
                                setResult(RESULT_VISITA_SOLICITUD_COTIZACION_INICIO_CANCELADA);
                                //SessionManager.getInstance(getApplicationContext()).removeVisitaSession();
                                finish();
                                Toast.makeText(CaptacionSolicitudActivity.this, "Visita finalizada!",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Log.d(TAG,"!!isSucessful...");
                            Toast.makeText(CaptacionSolicitudActivity.this, "Hubo un problema al finalizar la visita...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).actualizar(json);
    }

    //endregion

    //endregion

    //region General

    public void toolbarStatusBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registro de Solicitud");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void navigationBarStatusBar() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CaptacionSolicitudActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                frameLayout.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                CaptacionSolicitudActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                frameLayout.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CaptacionSolicitudActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                frameLayout.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                CaptacionSolicitudActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    private boolean validate(){
        if(Util.isNetworkAvailable(this)){
            return true;
        }
        return false;
    }

    private void showDialogBack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        builder.setTitle("RP3 Market Force")
                .setMessage("Al retroceder se perdera todos los datos ingresados.\n¿Desea retroceder?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"onClick Si retroceder...");
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"onClick no retroceder...");
                    }
                });
        builder.show();
    }

    //endregion

    //region Ciclo de vida

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
        estadoActivity = false;
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
    public void onBackPressed() {
       //super.onBackPressed();
        moveTaskToBack(true);
        Log.d(TAG,"onBackPressed...");
        //showDialogBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
        estadoActivity = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
        estadoActivity = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_visita_opciones,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_VISITA_REPROGRAMADA_FINALIZADA){
            Log.d(TAG,"request visita reprogramada ...");
            if(resultCode == RESULT_VISITA_REPROGRAMADA_FINALIZADA){
                Log.d(TAG,"result visita reprogramada finalizada...");
                setResult(RESULT_VISITA_REPROGRAMADA_FINALIZADA);
                finish();
            }
        }
    }

    //endregion
}
