package rp3.auna;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.bean.CotizacionVisita;
import rp3.auna.bean.RegistroPago;
import rp3.auna.bean.SolicitudMovil;
import rp3.auna.bean.VisitaVtaDetalle;
import rp3.auna.customviews.Tabs.TabsAdapter.TabsDesignViewPagerAdapter;
import rp3.auna.customviews.Tabs.TabsAdapter.TabsDetalleAdapter;
import rp3.auna.models.Agente;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.configuration.PreferenceManager;
import rp3.data.models.GeneralValue;
import rp3.util.Convert;

import static rp3.auna.Contants.GENERAL_TABLE_MOTIVOS_CANCELAR_CITA_TABLE_ID;
import static rp3.auna.Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA;

public class DetalleVisitaActivity extends AppCompatActivity {
    private static final String TAG = DetalleVisitaActivity.class.getSimpleName();
    @BindView(R.id.toolbarDetalleVisita) Toolbar toolbar;
    //@BindView(R.id.statusBarVisita) FrameLayout frameLayout;
    //Views Prospecto
    @BindView(R.id.tvAfiliado) TextView tvAfiliado;
    @BindView(R.id.tvDireccion) TextView tvDireccion;
    @BindView(R.id.tvTelefono) TextView tvTelefono;
    @BindView(R.id.tvCelular) TextView tvCelular;
    @BindView(R.id.tvCorreo) TextView tvCorreo;
    @BindView(R.id.tvAgente) TextView tvAgente;
    @BindView(R.id.tvFecha) TextView tvFecha;
    @BindView(R.id.tvGestionHora) TextView tvGestionHora;
    @BindView(R.id.tvEstadoVisita) TextView tvEstadoVisita;
    //Views General
    //@BindView(R.id.scrollDetalleVisita)NestedScrollView scrollView;
    @BindView(R.id.lyCodeMotivos)View lyMotivos;
    @BindView(R.id.ivEstado)ImageView ivEstado;
    @BindView(R.id.tvMotivo)TextView tvMotivo;
    @BindView(R.id.tvObservacion)TextView tvObservacion;
    @BindView(R.id.tabsLayoutDetalle)TabLayout tabLayout;
    @BindView(R.id.pager) ViewPager pager;

    private VisitaVtaDetalle visitaVtaDetalle;
    private TabsDetalleAdapter tabsDetalleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_visita);
        ButterKnife.bind(this);
        try{
            Log.d(TAG,"try...");
            toolbarStatusBar();
            navigationBarStatusBar();
            //scrollView.setFillViewport (true);
            getData();
        }catch (Exception e){
            Log.d(TAG,"Exception:"+e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupViewPager(CotizacionVisita inicial,CotizacionVisita _final,SolicitudMovil solicitudMovil,RegistroPago registroPago,Bundle todoPago) {
        tabsDetalleAdapter = new TabsDetalleAdapter(getSupportFragmentManager(), inicial, _final,solicitudMovil,registroPago,todoPago);
        pager.setAdapter(tabsDetalleAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    //region General Views

    private void toolbarStatusBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Detalle Visita");
        getSupportActionBar().setTitle("Detalle Visita");
        //setTitle("Detalle Visita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void navigationBarStatusBar() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                DetalleVisitaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                //frameLayout.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                DetalleVisitaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                //frameLayout.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                DetalleVisitaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                getWindow().setStatusBarColor(color);
                //frameLayout.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                DetalleVisitaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    private void getData(){
        visitaVtaDetalle = SessionManager.getInstance(this).getDetalleVisita();
        SessionManager.getInstance(this).removeDetalleVisita();
        if(visitaVtaDetalle!=null){
            Log.d(TAG,"visitaVtaDetalle!=null...");
            if(visitaVtaDetalle.getProspectoVta()!=null){
                tvAfiliado.setText(visitaVtaDetalle.getProspectoVta().getNombre());
                if(visitaVtaDetalle.getProspectoVta().getCelular()!=null){
                    tvCelular.setText("Celular: "+visitaVtaDetalle.getProspectoVta().getCelular());
                }else{
                    tvCelular.setText("");
                }
                if(visitaVtaDetalle.getProspectoVta().getTelefono()!=null){
                    tvTelefono.setText("Telefono: "+visitaVtaDetalle.getProspectoVta().getTelefono());
                }else {
                    tvTelefono.setText("");
                }
                if(visitaVtaDetalle.getProspectoVta().getEmail()!=null){
                    tvCorreo.setText("Correo: "+visitaVtaDetalle.getProspectoVta().getEmail());
                }else {
                    tvCorreo.setText("");
                }
            }
            Agente agente = Agente.getAgente(Utils.getDataBase(this), visitaVtaDetalle.getIdAgente());
            tvAgente.setText(agente.getNombre());
            tvDireccion.setText("Dirección: "+visitaVtaDetalle.getIdClienteDireccion());
            SimpleDateFormat formatFecha = new SimpleDateFormat(Constants.DATE_FORMAT);
            if(visitaVtaDetalle.getFechaVisita()>0){
                Date fecha = Convert.getDateFromDotNetTicks(visitaVtaDetalle.getFechaVisita());
                tvFecha.setText("Fecha: "+formatFecha.format(fecha));
            }
            if(visitaVtaDetalle.getFechaInicio()>0 && visitaVtaDetalle.getFechaFin()>0){
                SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_TIME_FORMAT_HH_MM);
                Date fechaInicio = Convert.getDateFromDotNetTicks(visitaVtaDetalle.getFechaInicio());
                Date fechaFin = Convert.getDateFromDotNetTicks(visitaVtaDetalle.getFechaFin());
                tvGestionHora.setText(format.format(fechaInicio)+"-"+format.format(fechaFin));
            }
            if(visitaVtaDetalle.getMotivoReprogramacionTabla()>0 && visitaVtaDetalle.getMotivoReprogramacionValue()!=null){
                if(visitaVtaDetalle.getMotivoReprogramacionTabla()==GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA){
                    List<GeneralValue> generalValues = GeneralValue.getGeneralValues(Utils.getDataBase(this), Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA);
                    Drawable drawable = this.getResources().getDrawable(R.drawable.ic_action_agenda);
                    drawable.mutate();
                    ivEstado.setImageDrawable(this.getResources().getDrawable(R.drawable.timeline2));
                    String motivo ="Motivo: ";
                    for (GeneralValue generalValue:generalValues){
                        if(generalValue.getCode().equalsIgnoreCase(visitaVtaDetalle.getMotivoReprogramacionValue())){
                            motivo+=generalValue.getValue();
                            break;
                        }
                    }
                    tvMotivo.setText(motivo);
                    if(visitaVtaDetalle.getObservacion()!=null){
                        if(visitaVtaDetalle.getObservacion().length()>0){
                            tvObservacion.setText("Observación: "+visitaVtaDetalle.getObservacion());
                        }
                    }
                }else if(visitaVtaDetalle.getMotivoReprogramacionTabla()==GENERAL_TABLE_MOTIVOS_CANCELAR_CITA_TABLE_ID){
                    List<GeneralValue> generalValues = GeneralValue.getGeneralValues(Utils.getDataBase(this), Contants.GENERAL_TABLE_MOTIVOS_CANCELAR_CITA_TABLE_ID);
                    Drawable drawable = this.getResources().getDrawable(R.drawable.ic_action_agenda);
                    drawable.mutate();
                    ivEstado.setImageDrawable(this.getResources().getDrawable(R.drawable.timeline3));
                    String motivo ="Motivo:";
                    for (GeneralValue generalValue:generalValues){
                        if(generalValue.getCode().equalsIgnoreCase(visitaVtaDetalle.getMotivoReprogramacionValue())){
                            motivo+=generalValue.getValue();
                            break;
                        }
                    }
                    tvMotivo.setText(motivo);
                    if(visitaVtaDetalle.getObservacion()!=null){
                        if(visitaVtaDetalle.getObservacion().length()>0){
                            tvObservacion.setText("Observación: "+visitaVtaDetalle.getObservacion());
                        }
                    }
                }else{
                    lyMotivos.setVisibility(View.GONE);
                }
            }else{
                lyMotivos.setVisibility(View.GONE);
            }
            CotizacionVisita _inicial  = null;
            CotizacionVisita _final  = null;
            SolicitudMovil solicitudMovil = null;
            RegistroPago registroPago = null;
            if(visitaVtaDetalle.getCotizacion()!=null){
                if(visitaVtaDetalle.getCotizacion().size()>0){
                    for (CotizacionVisita cotizacionVisita:visitaVtaDetalle.getCotizacion()){
                        if(cotizacionVisita.getFlag()==1){
                            _inicial = cotizacionVisita;
                        }else{
                            _final = cotizacionVisita;
                        }
                    }
                }
            }
            if(visitaVtaDetalle.getSolicitud()!=null){
                solicitudMovil = visitaVtaDetalle.getSolicitud();
            }
            if(visitaVtaDetalle.getPago()!=null){
                registroPago = visitaVtaDetalle.getPago();
            }
            //setear data inicial y final
            Bundle todoPago = null;
            if(visitaVtaDetalle.getTipoVenta()!=null){
                if(visitaVtaDetalle.getTipoVenta().trim().length()>0){
                    todoPago = new Bundle();
                    todoPago.putString("FormaPago",visitaVtaDetalle.getTipoVenta());
                }
            }
            setupViewPager(_inicial,_final,solicitudMovil,registroPago,todoPago);

        }else{
            Log.d(TAG,"visitaVtaDetalle==null...");
        }
    }

    //region Ciclo de vida

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
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
        super.onBackPressed();
        finish();
        Log.d(TAG,"onBackPressed...");
    }

    @Override
    protected void onDestroy() {
        SessionManager.getInstance(this).removeDetalleVisita();
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        super.onActivityResult(requestCode, resultCode, data);
    }

    //endregion
}
