package rp3.auna.customviews.Tabs.TabsViews;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rp3.auna.Contants;
import rp3.auna.CotizacionActivity;
import rp3.auna.DetalleVisitaActivity;
import rp3.auna.Main2Activity;
import rp3.auna.R;
import rp3.auna.VisitaMediaActivity;
import rp3.auna.adapter.AgendaLlamadaAdapter;
import rp3.auna.adapter.AgendaVisitaAdapter;
import rp3.auna.bean.VisitaVtaDetalle;
import rp3.auna.dialog.CitaDialog;
import rp3.auna.dialog.DateSelectPickerDialog;
import rp3.auna.dialog.MotivoCitaDialog;
import rp3.auna.events.EventBus;
import rp3.auna.events.Events;
import rp3.auna.fragments.AgendaFragment;
import rp3.auna.loader.ventanueva.LlamadaVtaLoader;
import rp3.auna.loader.ventanueva.VisitaVtaLoader;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.util.recyclerview.DividerItemDecoration;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.auna.webservices.VisitaDetalleClient;
import rp3.util.Convert;

import static rp3.auna.Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA;

/**
 * Created by Jesus Villa on 17/10/2017.
 */

public class TabCita extends Fragment{

    private static final String TAG = TabCita.class.getSimpleName();
    private static final int REQUEST_VISITA_COTIZACION_NUEVO = 6;
    private static final int REQUEST_VISITA_PAGO_FISICO_DOCUMENTOS= 7;
    private static final int REQUEST_VISITA_REPROGRAMADA = 19;
    @BindView(R.id.recyclerViewAgenda) RecyclerView recyclerView;
    @BindView(R.id.containerAgenda) RelativeLayout container;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private AppBarLayout appBarLayout;
    private AgendaVisitaAdapter adapter;
    private List<VisitaVta> list;
    private List<VisitaVta> vtaList;
    private Toolbar toolbar;
    private TypedValue typedValueToolbarHeight = new TypedValue();
    int recyclerViewPaddingTop;
    private VisitaVta agendaSelected = null;
    private VisitaVta visitaVta = null;
    private int positionnn = 0;
    public Date dateSelected = null;
    public boolean estado = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_agenda, container, false);
        ButterKnife.bind(this,view);
        Log.d(TAG,"onCreateView...");

        //dateSelected = Calendar.getInstance().getTime();
        recyclerViewDesign(view);
        initAdapter();
        return view;
    }

    public void setDate(Date date){
        dateSelected = date;
        if(estado){
            if(isResumed()){
                if (isVisible()){
                    if(getUserVisibleHint()){
                        obtenerDate(Convert.getDotNetTicksFromDate(date));
                    }

                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected...");
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.d(TAG,"home...");
            ((Main2Activity)getActivity()).openDrawer();
            return true;
        }
        if( id == R.id.agenda_action_filtrar){
            Log.d(TAG,"item filtrar por fecha...");
            if(list!=null){
                Log.d(TAG,"No hay visitas en list...");
                if(list.size()>0){
                    Log.d(TAG,"Si hay visitas en list...");
                    showDatePickerDialog();
                }else{
                    Log.d(TAG,"No hay visitas count en list...");
                    Toast.makeText(getActivity(), "No tiene agenda de visitas...", Toast.LENGTH_SHORT).show();
                }
            }else{
                Log.d(TAG,"No hay visitas en list...");
                Toast.makeText(getActivity(), "No tiene agenda de visitas...", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void showDatePickerDialog() {
        /*DateSelectPickerDialog newFragment = DateSelectPickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String selectedDate = dayOfMonth + " / " + (month+1) + " / " + year;
                SimpleDateFormat format=new SimpleDateFormat(Contants.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,(month+1),dayOfMonth);
                final String date = format.format(calendar.getTime());
                Log.d(TAG,"dateselected:"+date);
                filtrarFecha(calendar.getTime());
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");*/
    }

    public void filtrarFecha(Date date){
        Log.d(TAG,"filtrarFecha...");
        vtaList = new ArrayList<>();
        dateSelected = date;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date);
        Log.d(TAG,"Date filtrada:"+calendar1.getTime().toString());
        if(list!=null){
            for(int i = 0;i<list.size();i++){
                calendar2.setTime(list.get(i).getFechaVisita());
                Log.d(TAG,"Date comparar:"+calendar2.getTime().toString());
                boolean sameDay = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                        calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
                if(sameDay){
                    Log.d(TAG,"sameDay...");
                    vtaList.add(list.get(i));
                }else{
                    Log.d(TAG,"!sameDay...");
                }
            }
        }/*
        if(list!=null){
            for(int i = 0;i<list.size();i++){
                vtaList.add(list.get(i));
            }
        }*/
        adapter.addMoreItems(vtaList);
        Log.d(TAG,"vtaList a veR:"+vtaList.size());
        if(vtaList!=null){
            Log.d(TAG,"list!=null..");
            if(vtaList.size()>0){
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                Log.d(TAG,"vtaList.size>0");
                showInformacion(false);
            }else{
                Log.d(TAG,"vtaList.SIZE>0");
                showInformacion(true);
            }
        }else{
            Log.d(TAG,"vtaList==null...");
            showInformacion(true);
        }
    }

    private void showInformacion(boolean bool){
        if(bool){
            container.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }else{
            container.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    //region General
    private void recyclerViewDesign(View view) {
        appBarLayout = (AppBarLayout)getActivity().findViewById(R.id.appBarMain);
        // Divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));
        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValueToolbarHeight, true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }else{
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 21) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21){
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }
            if (Build.VERSION.SDK_INT < 19) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        Log.d(TAG,"recyclerPaddingTop:"+recyclerViewPaddingTop);
        recyclerView.setPadding(0, (recyclerViewPaddingTop-15)*2, 0, 0 );
        //recyclerView.setPadding(0, recyclerViewPaddingTop, 0, 0);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG,"onCreateOptionsMenu...");
        inflater.inflate(R.menu.menu_agenda_fecha,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    //endregion

    private void initAdapter(){
        adapter = new AgendaVisitaAdapter(getActivity(), new AgendaVisitaAdapter.CallbackVer() {
            @Override
            public void agendaSelected(VisitaVta agendaVta, int positionn) {
                Log.d(TAG,"agendaSelected...");
                Log.d(TAG,"agendaSelected...");
                agendaSelected = agendaVta;
                visitaVta = agendaVta;
                Log.d(TAG,"agenda:"+agendaVta.toString());
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(agendaVta.getFechaVisita());
                boolean sameDay = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                        calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
                if(agendaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA)){
                    Log.d(TAG,"Visita detalle obtener...");
                    if(visitaVta.getIdVisita()>0){
                        showDetalleVisitaClient(visitaVta.getIdVisita(),1);
                    }else {
                        Toast.makeText(getActivity(), "Necesita sincronizar esta información con el servidor.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(sameDay){
                    Log.d(TAG,"Hoy Si es la fecha en que se programo esa visita...");
                    if(agendaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE)){
                        Log.d(TAG,"Visita estado pendiente...");
                        Log.d(TAG,"Estado Visita:"+agendaVta.getVisitaValue());
                        positionnn = positionn;
                        showOptionsVisita();
                    }else if(agendaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_CANCELADA)){
                        Toast.makeText(getActivity(), "Esta visita fue cancelada.", Toast.LENGTH_SHORT).show();
                    }
                    else if(agendaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_REPROGRAMADA)){
                        Toast.makeText(getActivity(), "Esta visita fue reprogramada.", Toast.LENGTH_SHORT).show();
                    }
                    else if(agendaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_NO_REALIZADA)){
                        Toast.makeText(getActivity(), "Esta visita no fue realizada.", Toast.LENGTH_SHORT).show();
                    }
                    else if(agendaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA)){
                        Log.d(TAG,"visita realizada...");
                    }
                    else{
                        Log.d(TAG,"Valor de la visita:"+agendaVta.getVisitaValue());
                        Toast.makeText(getActivity(), "Esta visita no esta pendiente.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d(TAG,"Hoy no es la fecha en que se programo esa llamada...");
                    Toast.makeText(getActivity(), "La fecha de hoy no coincide con la fecha programada.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new AgendaVisitaAdapter.CallbackAction() {
            @Override
            public void agendaActionSelected(VisitaVta agendaVta, int position,View v) {
                Log.d(TAG,"agendaActionSelected...Mostrar detalle consultando ws que obtenga la data..");
                visitaVta = agendaVta;
                Log.d(TAG,"agenda:"+agendaVta.toString());
                /*Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(agendaVta.getFechaVisita());
                boolean sameDay = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                        calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
                if(sameDay){
                    Log.d(TAG,"Hoy Si es la fecha en que se programo esa visita...");
                    if(agendaVta.getVisitaValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA)){
                        Log.d(TAG,"Visita estado Realizada...");
                        Log.d(TAG,"Estado Visita:"+agendaVta.getVisitaValue());
                        positionnn = position;
                        showDetailVisitaClient();
                    }
                }*/

            }
        });

        recyclerView.setAdapter(adapter);
        LoaderAgendaVta loader = new LoaderAgendaVta();
        Bundle args = new Bundle();
        getLoaderManager().initLoader(0,args,loader);
    }

    private void showDetalleVisitaClient(int idVisita,int type){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(getActivity().getResources().getString(R.string.appname_marketforce));
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new VisitaDetalleClient(getActivity(), new Callback() {
            Handler mHandler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                Log.d(TAG,"onFailure...");
                Log.d(TAG,"IOException");
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), R.string.message_error_sync_connection_http_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                progressDialog.dismiss();
                Log.d(TAG,"onResponse...");
                final String json = response.body().string();
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(response.isSuccessful()){
                                VisitaVtaDetalle obj = new Gson().fromJson(json,VisitaVtaDetalle.class);
                                SessionManager.getInstance(getActivity()).createDetalleVisita(obj);
                                if(type==1){
                                    //es detalle visita
                                    Intent intent = new Intent(getActivity(), DetalleVisitaActivity.class);
                                    getActivity().startActivity(intent);
                                }else if(type==2){
                                    //es iniciar cotizacion
                                    Intent intent1 = new Intent(getActivity(), CotizacionActivity.class);
                                    intent1.putExtra("Estado",1);
                                    getActivity().startActivityForResult(intent1,REQUEST_VISITA_COTIZACION_NUEVO);
                                }

                            }else{
                                Toast.makeText(getActivity(), R.string.message_error_sync_connection_server_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).obtener(idVisita);
    }

    private void showOptionsVisita(){
        CitaDialog citaDialog = CitaDialog.newInstance(new CitaDialog.callbackOpcionCitaelegir() {
            @Override
            public void onSelectedFinalizar() {
                Log.d(TAG,"onSelectedCancelar...");
            }

            @Override
            public void onOpcionElegida(int position) {
                switch (position){
                    case 0:
                        Log.d(TAG,"Eligio gestionar cita...");
                        showInicioGestion();
                        break;
                    case 1:
                        Log.d(TAG,"Eligio reprogramar cita...");
                        showOpcionElegida(agendaSelected,1);
                        break;
                    case 2:
                        Log.d(TAG,"Eligio cancelar cita...");
                        showOpcionElegida(agendaSelected,2);
                        break;
                }
            }
        });
        citaDialog.setCancelable(true);
        citaDialog.show(getFragmentManager(),"");
    }

    private void showInicioGestion(){
        Log.d(TAG,"showInicioGestion");
        Log.d(TAG,visitaVta.toString());
        if(visitaVta.getIdVisita()>0){

            //visitaVta = vtaList.get(position);
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            date.setTime(calendar.getTimeInMillis());
            visitaVta.setFechaInicio(date);
            SessionManager.getInstance(getActivity()).createVisitaSession(visitaVta);
            Intent intent;
            if(visitaVta.getEstado()==6){
                //Consultar data e iniciar
                showDetalleVisitaClient(visitaVta.getVisitaId(),2);
            }else{
                //Iniciar la Cotizacion Inicial
                intent = new Intent(getActivity(), CotizacionActivity.class);
                intent.putExtra("Estado",1);
                getActivity().startActivityForResult(intent,REQUEST_VISITA_COTIZACION_NUEVO);
            }

            /**
             * Queda pendientes estas notas
             */
            //region Notas
            /*if(visitaVta.getEstado()==1){
                intent = new Intent(getActivity(), CotizacionActivity.class);
                intent.putExtra("Estado",1);
                getActivity().startActivityForResult(intent,REQUEST_VISITA_COTIZACION_NUEVO);
            }else if(visitaVta.getEstado()==3){
                //Iniciar la visita fisica (Subir Documentos)
                intent = new Intent(getActivity(), VisitaMediaActivity.class);
                intent.putExtra("Estado",1);
                intent.putExtra("VisitaId",visitaVta.getVisitaId());
                getActivity().startActivityForResult(intent,REQUEST_VISITA_PAGO_FISICO_DOCUMENTOS);
            }else if(visitaVta.getEstado()==8){
                intent = new Intent(getActivity(), VisitaMediaActivity.class);
                intent.putExtra("Estado",1);
                intent.putExtra("VisitaId",visitaVta.getVisitaId());
                getActivity().startActivityForResult(intent,REQUEST_VISITA_COTIZACION_NUEVO);
            }
            else if(visitaVta.getEstado()==5){
                intent = new Intent(getActivity(), VisitaMediaActivity.class);
                intent.putExtra("Estado",1);
                intent.putExtra("VisitaId",visitaVta.getVisitaId());
                //Consultar WS para obtener la data que se tuvo.
                getActivity().startActivityForResult(intent,REQUEST_VISITA_COTIZACION_NUEVO);
            }*/
//endregion
        }else{
            Log.d(TAG,"Visita == 0");
            Toast.makeText(getActivity(), "Se necesita que sincronize la informacion con el servidor porfavor...", Toast.LENGTH_SHORT).show();
        }
    }

    private void showOpcionElegida(final VisitaVta agenda,final int tipo){
        Bundle arg = new Bundle();
        arg.putInt("Tipo",tipo);
        arg.putInt("Repro",1);
        final MotivoCitaDialog dialog = MotivoCitaDialog.newInstance(new MotivoCitaDialog.callbackElegir() {
            @Override
            public void onGeneralSelected(String code,String motivo) {
                if(tipo == 1){
                    reprogramarCita(agenda,code,motivo);
                }else{
                    cancelarCita(agenda,code,motivo);
                }
            }
        });
        dialog.setArguments(arg);
        dialog.setCancelable(true);
        dialog.show(getFragmentManager(),"MotivoDialog");
    }

    //region Reprogramar y cancelar

    private void reprogramarCita(VisitaVta agenda,String code,String motivo){
        int idProspecto = agenda.getIdCliente();
        Log.d(TAG, "idProspecto:" + idProspecto);
        Log.d(TAG, "si hay...");
        visitaVta = agenda;
        Calendar calendar = Calendar.getInstance();
        visitaVta.setFechaInicio(null);
        visitaVta.setReferidoValue("");
        visitaVta.setObservacion(motivo);
        visitaVta.setMotivoReprogramacionValue(code);
        visitaVta.setMotivoReprogramacionTabla(GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA);
        Location location = ((Main2Activity) getActivity()).location;
        if (location != null) {
            visitaVta.setLatitud(location.getLatitude());
            visitaVta.setLongitud(location.getLongitude());
        } else {
            visitaVta.setLatitud(0.00);
            visitaVta.setLongitud(0.00);
        }
        visitaVta.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_REPROGRAMADA);
        vtaList.get(positionnn).setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_REPROGRAMADA);
        adapter.addMoreItems(vtaList);
        Calendar c = Calendar.getInstance();
        visitaVta.setFechaFin(null);
        if(visitaVta.getIdVisita()>0){
            visitaVta.setInsertado(0);
        }else{
            visitaVta.setInsertado(1);
        }
        Log.d(TAG,"Agenda:"+agendaSelected.toString());
        Log.d(TAG, "Visita:" + visitaVta.toString());
        boolean result = VisitaVta.update(Utils.getDataBase(getActivity()), visitaVta);
        Log.d(TAG,result?"visita actualizada":"visita no actualizada");
        Log.d(TAG,visitaVta.toString());
        /*Intent intent = new Intent(getActivity(), rp3.auna.CrearVisitaActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putDouble("Latitud",location.getLatitude());
        bundle1.putDouble("Longitud",location.getLongitude());
        ProspectoVtaDb prospecto = null;
        List<ProspectoVtaDb> list = ProspectoVtaDb.getAll(Utils.getDataBase(getActivity()));
        if(list!=null) {
            Log.d(TAG, "list!=null: Prospectos Sincronizados:" + list.size());
            if(visitaVta.getInsertado()==1){
                for (ProspectoVtaDb obj : list) {
                    if (visitaVta.getIdCliente() == obj.getID()) {
                        prospecto = obj;
                        Log.d(TAG, "break:" + prospecto.toString());
                        break;
                    }else if(visitaVta.getIdCliente() == obj.getIdProspecto()){
                        prospecto = obj;
                        Log.d(TAG, "break:" + prospecto.toString());
                        break;
                    }
                }
            }else{
                for (ProspectoVtaDb obj : list) {
                    if (visitaVta.getIdCliente() == obj.getIdProspecto()) {
                        prospecto = obj;
                        Log.d(TAG, "break:" + prospecto.toString());
                        break;
                    }
                }
            }

            bundle1.putInt("Service", 5);
            bundle1.putLong("Id", prospecto.getID());
            bundle1.putInt("IdProspecto", prospecto.getIdProspecto());
            bundle1.putString("Prospecto", prospecto.getNombre());
            bundle1.putString("Direccion", visitaVta.getIdClienteDireccion());
            bundle1.putInt("Estado", 0);
            bundle1.putInt("VisitaId", visitaVta.getIdVisita());
            SessionManager.getInstance(getActivity()).removeVisitaReprogramada();
            intent.putExtras(bundle1);
            getActivity().startActivityForResult(intent, REQUEST_VISITA_REPROGRAMADA);
        }*/
        Log.d(TAG,result?"Se actualizo a reprogramar visita...":"No se a reprogramar actualizo...");
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_VISITA);
        SessionManager.getInstance(getActivity()).createVisitaRerogramar(visitaVta);
        ((Main2Activity) getActivity()).invokeSyncLlamada("Agenda","Actualizando visitas...",bundle);
    }

    private void cancelarCita(VisitaVta agenda,String code,String motivo){int id = 0;
        agendaSelected = agenda;
        Calendar calendar = Calendar.getInstance();
        visitaVta = agenda;
        visitaVta.setFechaInicio(null);
        visitaVta.setReferidoValue("");
        Location location = ((Main2Activity) getActivity()).location;
        if (location != null) {
            visitaVta.setLatitud(location.getLatitude());
            visitaVta.setLongitud(location.getLongitude());
        } else {
            visitaVta.setLatitud(0.00);
            visitaVta.setLongitud(0.00);
        }
        visitaVta.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_CANCELADA);
        visitaVta.setObservacion(motivo);
        visitaVta.setMotivoReprogramacionValue(code);
        visitaVta.setMotivoReprogramacionTabla(Contants.GENERAL_TABLE_MOTIVOS_CANCELAR_CITA_TABLE_ID);
        vtaList.get(positionnn).setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_CANCELADA);
        adapter.addMoreItems(vtaList);
        Calendar c = Calendar.getInstance();
        visitaVta.setFechaFin(null);
        if(visitaVta.getIdVisita()>0){
            visitaVta.setInsertado(0);
        }else{
            visitaVta.setInsertado(1);
        }
        boolean result = VisitaVta.update(Utils.getDataBase(getActivity()), visitaVta);
        Log.d(TAG,result?"Se actualizo a cancelar vISITA...":"No Se actualizo a cancelar vISITA...");
        Log.d(TAG, "vISITA:" + visitaVta.toString());
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_VISITA);
        ((Main2Activity) getActivity()).invokeSyncLlamada("Agenda","Actualizando visitas...",bundle);
        refresh();
    }

    //endregion

    //region Refresh Data
    public void refresh(){
        Log.d(TAG,"refresh...");
        if(getLoaderManager().getLoader(0)!=null){
            Log.d(TAG,"getLoaderManager().getLoader(0)!=null...");
            getLoaderManager().destroyLoader(0);
            LoaderAgendaVta loaderAgendaVta = new LoaderAgendaVta();
            getLoaderManager().initLoader(0,new Bundle(),loaderAgendaVta);
        }else{
            Log.d(TAG,"getLoaderManager().getLoader(0)==null...");
            LoaderAgendaVta loaderAgendaVta = new LoaderAgendaVta();
            getLoaderManager().initLoader(0,new Bundle(),loaderAgendaVta);
        }
    }

    public class LoaderAgendaVta implements LoaderManager.LoaderCallbacks<List<VisitaVta>> {

        @Override
        public Loader<List<VisitaVta>> onCreateLoader(int arg0,Bundle bundle) {
            Log.d(TAG,"onCreateLoader...");
            return new VisitaVtaLoader(getActivity(), Utils.getDataBase(getActivity()),true,"");
        }

        @Override
        public void onLoadFinished(Loader<List<VisitaVta>> arg0,List<VisitaVta> data) {
            Log.d(TAG,"onLoadFinished...");
            if(getActivity() != null) {
                //list = data;
                Log.d(TAG,"getActivity() != null...");
                if (data != null){
                    Log.d(TAG,"data != null...");
                    if (data.size() > 0) {
                        Log.d(TAG,"cantidad de citas agenda:"+data.size());
                        toolbar.setTranslationY(0);
                        appBarLayout.setTranslationY(0);
                        list = new ArrayList<>();
                        for(VisitaVta obj:data){
                            list.add(obj);
                        }
                        Log.d(TAG,"cantidad de citas:"+list.size());
                        if(list.size()==0){
                            Log.d(TAG,"list.size()==0...");
                            recyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);
                            //Toast.makeText(getActivity(), "No hay Citas sincronizados ni registradas...", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d(TAG,"list.size()>0...");
                            Calendar calendar = Calendar.getInstance();
                            filtrarFecha(dateSelected);
                            //adapter.addMoreItems(list);
                            recyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            container.setVisibility(View.GONE);
                        }

                    }
                    else {
                        Log.d(TAG,"...");
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        container.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(), "No hay Citas sincronizados ni registradas...", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(TAG,"DATA==...");
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), "No hay Citas sincronizados ni registradas...", Toast.LENGTH_SHORT).show();
                }
            }else {
                Log.d(TAG,"getActivity() == null...");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<VisitaVta>> arg0) {
            Log.d(TAG,"onLoaderReset...");
        }
    }

    //endregion

    //region Ciclo de vida

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
        estado = true;
        EventBus.getBus().register(this);
        refresh();
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.d(TAG,"getUserVisibleHint...");
        Log.d(TAG,"Value:"+super.getUserVisibleHint());
        return super.getUserVisibleHint();
    }

    @Override
    public void onStop() {
        super.onStop();
        estado = false;
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    public void onPause() {
        super.onPause();
        estado = false;
        Log.d(TAG,"onPause...");
        EventBus.getBus().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView...");

    }

    //endregion

    //region Events

    public void obtenerDate(long date){
        dateSelected = Convert.getDateFromDotNetTicks(date);
        Log.d(TAG,"obtenerDate:"+dateSelected.toString());
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_REFRESH_VISITA);
        bundle.putLong(AgendaFragment.FECHA,date);
        ((Main2Activity)getActivity()).invokeSyncLlamada("Agenda","Obteniendo...",bundle);
    }

    @Subscribe
    public void recievedMessage(Events.Message msj){
        Log.d(TAG,"recievedMessage visita...");
        Bundle todo = msj.getMessage();
        if(todo!=null){
            if (todo.getString("Llamada")!=null){
                if(todo.getString("Llamada").equalsIgnoreCase("Refresh")){
                    refresh();
                }
            }
        }

    }

    //endregion
}
