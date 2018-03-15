package rp3.auna.customviews.Tabs.TabsViews;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import rp3.auna.Main2Activity;
import rp3.auna.ProspectoActivity;
import rp3.auna.R;
import rp3.auna.adapter.AgendaLlamadaAdapter;
import rp3.auna.bean.ProspectoVta;
import rp3.auna.bean.RepositorioRespuesta;
import rp3.auna.content.DuracionLlamadaReceiver;
import rp3.auna.dialog.DateSelectPickerDialog;
import rp3.auna.dialog.DireccionProspectoDialog;
import rp3.auna.dialog.ElegirLlamadaDialog;
import rp3.auna.dialog.GeneralValueDialog;
import rp3.auna.dialog.LlamadaDialog;
import rp3.auna.dialog.MotivoLlamadaDialog;
import rp3.auna.events.EventBus;
import rp3.auna.events.Events;
import rp3.auna.fragments.AgendaFragment;
import rp3.auna.loader.ventanueva.LlamadaVtaLoader;
import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.helper.CallLogHelper;
import rp3.auna.util.recyclerview.DividerItemDecoration;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.auna.webservices.NoContactarClient;
import rp3.configuration.PreferenceManager;
import rp3.data.models.GeneralValue;
import rp3.util.Convert;

import static rp3.auna.Contants.GENERAL_TABLE_MOTIVOS_CANCELAR_LLAMADA_TABLE_ID;
import static rp3.auna.Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_LLAMADA;
import static rp3.auna.Contants.GENERAL_VALUE_CODE_LLAMADA_CANCELADA;
import static rp3.auna.Contants.GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA;
import static rp3.auna.Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE;
import static rp3.auna.Contants.GENERAL_VALUE_CODE_LLAMADA_REPROGRAMADA;
import static rp3.auna.Contants.GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA;
import static rp3.auna.Contants.KEY_IDAGENTE;

/**
 * Created by Jesus Villa on 17/10/2017.
 */

public class TabLlamada extends Fragment {

    private static final String TAG = TabLlamada.class.getSimpleName();
    private static final int REQUEST_PROSPECTO_EDIT = 17;
    private static final int REQUEST_LLAMADA_REPROGRAMADA = 18;
    private static final int RESULT_LLAMADA_REPROGRAMADA = 18;
    private static final int REQUEST_LLAMADA_NUEVO = 4;
    private static final int REQUEST_CALL = 101;
    private static final int RESULT_CALL = 101;
    @BindView(R.id.recyclerViewAgenda) RecyclerView recyclerView;
    @BindView(R.id.containerAgenda) RelativeLayout container;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    private AgendaLlamadaAdapter adapter;
    private List<LlamadaVta> list;
    private List<LlamadaVta> vtaList;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private TypedValue typedValueToolbarHeight = new TypedValue();
    int recyclerViewPaddingTop;
    private LlamadaVta llamadaSelected = null;
    private LlamadaVta llamadaVta = null;
    private LlamadaVta llamadaRepro = null;
    private int position = 0;
    public Date dateSelected = null;
    private String numeroSelected = null;
    public boolean estado = false;
    private int durationLlamada = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_agenda, container, false);
        ButterKnife.bind(this,view);
        Log.d(TAG,"onCreateView...");
        recyclerViewDesign();
        adapterView();
        return view;
    }

    public void setDate(Date date){
        dateSelected = date;
        if(estado){
            if(isResumed()){
                if(isVisible()){
                    if(getUserVisibleHint()){
                        obtenerDate(Convert.getDotNetTicksFromDate(date));
                    }
                }
            }
        }
    }

    private void adapterView(){
        adapter = new AgendaLlamadaAdapter(getActivity(), new AgendaLlamadaAdapter.CallbackVer() {
            @Override
            public void agendaSelected(final LlamadaVta agendaVta, int positionn) {
                Log.d(TAG,"agendaSelected...");
                Log.d(TAG,"agenda:"+agendaVta.toString());
                int idAgente = PreferenceManager.getInt(KEY_IDAGENTE,0);
                if(idAgente==0){
                    Toast.makeText(getActivity(), R.string.generic_show_message_service, Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(agendaVta.getFechaLlamada());
                llamadaRepro = agendaVta;
                boolean sameDay = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                        calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
                if(sameDay){
                    Log.d(TAG,"Hoy Si es la fecha en que se programo esa llamada...");
                    if(agendaVta.getLlamadoValue().equalsIgnoreCase(GENERAL_VALUE_CODE_LLAMADA_PENDIENTE)){
                        Log.d(TAG,"Llamada estado pendiente...");
                        Log.d(TAG,"Estado llamada:"+agendaVta.getLlamadoValue());
                        position = positionn;
                        ElegirLlamadaDialog dialog = ElegirLlamadaDialog.newInstance(new ElegirLlamadaDialog.callbackLlamadaelegir() {
                            @Override
                            public void onSelectedFinalizar() {
                                Log.d(TAG,"Cancelo la eleccion...");
                            }

                            @Override
                            public void onNumeroSelected(int position) {
                                Log.d(TAG,"Selecciono la opcion...");
                                switch (position){
                                    case 0:
                                        Log.d(TAG,"Gestionar...");
                                        showLlamada(agendaVta);
                                        break;
                                    case 1:
                                        Log.d(TAG,"Reprogramar...");
                                        showDialogMotivo(agendaVta,1);
                                        break;
                                    case 2:
                                        Log.d(TAG,"Cancelar...");
                                        showDialogMotivo(agendaVta,2);
                                        break;
                                }
                            }
                        });
                        dialog.show(getFragmentManager(),"ElegirLlamadaDialog");
                    }else if(agendaVta.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_REPROGRAMADA)){
                        Toast.makeText(getActivity(), "Esta llamada fue reprogramada.", Toast.LENGTH_SHORT).show();
                    }else if(agendaVta.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_CANCELADA)){
                        Toast.makeText(getActivity(), "Esta llamada fue cancelada.", Toast.LENGTH_SHORT).show();
                    }
                    else if(agendaVta.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA)){
                        Toast.makeText(getActivity(), "Esta llamada no fue realizada.", Toast.LENGTH_SHORT).show();
                    }
                    else if(agendaVta.getLlamadoValue().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA)){
                        Toast.makeText(getActivity(), "Esta llamada fue realizada.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Log.d(TAG,"Valor de la llamada:"+agendaVta.getLlamadoValue());
                        Toast.makeText(getActivity(), "Esta llamada no esta pendiente.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d(TAG,"Hoy no es la fecha en que se programo esa llamada.");
                    Toast.makeText(getActivity(), "La fecha de hoy no coincide con la fecha programada...", Toast.LENGTH_SHORT).show();
                }
            }
        }, new AgendaLlamadaAdapter.CallbackAction() {
            @Override
            public void agendaActionSelected(LlamadaVta agendaVta, int position,View v) {
                Log.d(TAG,"agendaActionSelected...");

            }
        });
        recyclerView.setAdapter(adapter);
        LoaderAgendaVta loader = new LoaderAgendaVta();
        Bundle args = new Bundle();
        getLoaderManager().initLoader(0,args,loader);
    }

    private void showDialogMotivo(final LlamadaVta agendaVta,final int tipo){
        SessionManager.getInstance(getActivity()).removeLlamadaGestion();
        Bundle todo = new Bundle();
        todo.putInt("Tipo",tipo);
        final MotivoLlamadaDialog dialog = MotivoLlamadaDialog.newInstance(new MotivoLlamadaDialog.callbackElegir() {
            @Override
            public void onGeneralSelected(String code,String motivo) {
                if(tipo == 1){
                    reprogramarCall(agendaVta,code,motivo);
                }else{
                    cancelarCall(agendaVta,code,motivo);
                }
            }
        });
        dialog.setArguments(todo);
        dialog.setCancelable(true);
        dialog.show(getFragmentManager(),"MotivoDialog");
    }

    //region General

    private void recyclerViewDesign() {
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
        appBarLayout = (AppBarLayout)getActivity().findViewById(R.id.appBarMain);
        tabLayout = (TabLayout)getActivity().findViewById(R.id.tabsLayout);
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
                Log.d(TAG,">=21 : "+recyclerViewPaddingTop);
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
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    //endregion

    private void showLlamada(final LlamadaVta model){
        AlertDialog alert = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle)
                .setTitle(this.getResources().getString(R.string.appname_marketforce))
                .setMessage("¿Desea iniciar la llamada?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG,"si hay...");
                            Calendar calendar = Calendar.getInstance();
                        llamadaVta = model;
                        llamadaVta.setFechaInicioLlamada(calendar.getTime());
                        llamadaVta.setLlamadaValor(0);
                        llamadaVta.setReferidoValue("");
                        Location location = ((Main2Activity)getActivity()).location;

                        if(location!=null){
                            llamadaVta.setLatitud(location.getLatitude());
                            llamadaVta.setLongitud(location.getLongitude());
                        }else {
                            llamadaVta.setLatitud(0.00);
                            llamadaVta.setLongitud(0.00);
                        }
                        SessionManager.getInstance(getActivity()).createLlamadaGestion(llamadaVta);
                        manageCall(llamadaVta);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alert.show();
    }

    private void manageCall(final LlamadaVta model){
        llamadaVta = model;
        //model.setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
        //Calendar c = Calendar.getInstance();
        //model.setFechaFinLlamada(c.getTime());
        final ArrayList<String> list = new ArrayList<>();
        int idProspecto = model.getIdCliente();
        Log.d(TAG,"idProspecto:"+idProspecto);
        List<ProspectoVtaDb> prospectoVtaDb = ProspectoVtaDb.getAll(Utils.getDataBase(getActivity()));
        ProspectoVtaDb objeto=null;
        if(model.getInsertado()==1){
            Log.d(TAG,"la agenda esta en bd sqlite...");
            for (ProspectoVtaDb obj:prospectoVtaDb) {
                Log.d(TAG,String.valueOf(obj.getIdProspecto())+" es IdProspecto...");
                if(obj.getEstado()==3){
                    if(obj.getIdProspecto() == idProspecto){
                        objeto = obj;
                        Log.d(TAG,"obj.getIdProspecto() == idProspecto...");
                        Log.d(TAG,obj.toString());
                        if(obj.getCelular()!=null)
                        {
                            if(!obj.getCelular().trim().equalsIgnoreCase("")){
                                Log.d(TAG,"si tiene celular...");
                                list.add(obj.getCelular());
                            }
                        }
                        if(obj.getTelefono()!=null){
                            if(!obj.getTelefono().trim().equalsIgnoreCase("")){
                                Log.d(TAG,"si tiene telefono...");
                                list.add(obj.getTelefono());
                            }
                        }
                        break;
                    }
                }else{
                    if(obj.getID() == idProspecto){
                        objeto = obj;
                        Log.d(TAG,"obj.getID == idProspecto...");
                        Log.d(TAG,obj.toString());
                        if(obj.getCelular()!=null){
                            if(!obj.getCelular().trim().equalsIgnoreCase("")){
                                Log.d(TAG,"si tiene celular...");
                                list.add(obj.getCelular());
                            }
                        }
                        if(obj.getTelefono()!=null){
                            if(!obj.getTelefono().trim().equalsIgnoreCase("")){
                                Log.d(TAG,"si tiene telefono...");
                                list.add(obj.getTelefono());
                            }
                        }
                        break;
                    }
                }
            }
        }else{
            Log.d(TAG,"la agenda esta en servidor...");
            for (ProspectoVtaDb obj:prospectoVtaDb) {
                Log.d(TAG,String.valueOf(obj.getIdProspecto())+" es IdProspecto...");
                if(obj.getEstado()==3){
                    if(obj.getIdProspecto() == idProspecto){
                        objeto = obj;
                        Log.d(TAG,"obj.getIdProspecto() == idProspecto...");
                        Log.d(TAG,obj.toString());
                        if(obj.getCelular()!=null){
                            if(!obj.getCelular().trim().equalsIgnoreCase("")){
                                Log.d(TAG,"si tiene celular...");
                                list.add(obj.getCelular());
                            }
                        }
                        if(obj.getTelefono()!=null){
                            if(!obj.getTelefono().trim().equalsIgnoreCase("")){
                                Log.d(TAG,"si tiene telefono...");
                                list.add(obj.getTelefono());
                            }
                        }
                        break;
                    }
                }else{
                    if(obj.getID() == idProspecto){
                        objeto = obj;
                        Log.d(TAG,"obj.getID == idProspecto...");
                        Log.d(TAG,obj.toString());
                        if(obj.getCelular()!=null){
                            if(!obj.getCelular().trim().equalsIgnoreCase("")){
                                Log.d(TAG,"si tiene celular...");
                                list.add(obj.getCelular());
                            }
                        }
                        if(obj.getTelefono()!=null){
                            if(!obj.getTelefono().trim().equalsIgnoreCase("")){
                                Log.d(TAG,"si tiene telefono...");
                                list.add(obj.getTelefono());
                            }
                        }
                        break;
                    }
                }
            }
        }
        Log.d(TAG,"Cantidad de telefonos:"+list.size());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
        final String[] numeros = new String[list.size()];
        for (int j = 0;j<list.size();j++){
            numeros[j] = list.get(j);
            arrayAdapter.add(numeros[j]);
            Log.d(TAG,numeros[j]);
        }
        final ArrayList<Integer> intento = new ArrayList<>();
        final ProspectoVtaDb finalObjeto = objeto;
        final LlamadaDialog dialog = LlamadaDialog.newInstance(new LlamadaDialog.callbackLlamada() {
            @Override
            public void onSelectedFinalizar(int duration) {
                if(duration == -1){
                    Log.d(TAG,"duration == -1");
                    showCallsLog(list,finalObjeto);
                }
            }

            @Override
            public void onSelectedEditar() {
                Log.d(TAG,"SelectedEditar...");
                if(durationLlamada==-1){
                    Toast.makeText(getActivity(), "No ha realizado una llamada mayor a los segundos establecidos para poder finalizar y editar el prospecto.", Toast.LENGTH_SHORT).show();
                }else{
                    if(validateEditProspecto(list)){
                        finalizar("Editar",finalObjeto,null);
                    }else{
                        Toast.makeText(getActivity(), "No ha realizado una llamada mayor a los segundos establecidos para poder finalizar y editar el prospecto.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onNumeroSelected(final String numero) {
                Log.d(TAG,"Selecciono el numero:"+numero);
                numeroSelected = numero;
                intento.add(1);
                if(intento.size()>1){
                    Log.d(TAG,"Intentos:"+intento.size());
                    AlertDialog builder = new AlertDialog.Builder(getActivity())
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    durationLlamada=2;
                                    String uri = "tel:" + numero;
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse(uri));
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setMessage("¿Desea llamar nuevamente a este numero:"+numero+"?")
                            .create();
                    builder.show();
                }else{
                    Log.d(TAG,"Primer intento..");
                    durationLlamada=1;
                    String uri = "tel:" + numero;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            }

            @Override
            public void onReprogramarSelected(){
                Log.d(TAG,"Reprogramar...");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
                builder.setTitle(getActivity().getResources().getString(R.string.appname_marketforce))
                        .setMessage("¿Esta seguro de reprogramar esta llamada?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"Si reprogramar...");
                                showDialogMotivo(llamadaRepro,1);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"No reprogramar...");
                            }
                        });
                builder.setCancelable(true);
                AlertDialog dialog = builder.show();
                dialog.show();
            }

            @Override
            public void onDismissed(int dismissed){
                if(dismissed==-1){
                    SessionManager.getInstance(getActivity()).removeLlamadaGestion();
                }
            }
        });
        Bundle todo = new Bundle();
        todo.putStringArrayList("lista",list);
        todo.putInt("prospecto",idProspecto);
        if(objeto!=null){todo.putString("titulo",objeto.getNombre());}
        dialog.setCancelable(true);
        dialog.setArguments(todo);
        dialog.show(getFragmentManager(),"DialogLlamada");

    }

    private void showCallsLog(ArrayList<String> list,final ProspectoVtaDb objeto){
        Log.d(TAG,"showCallsLogs...");
        final Cursor curLog = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
        final ArrayList<String> res = CallLogHelper.getCallLogs(curLog);
        Log.d(TAG,res.toString());
        if(res==null){
            if(res.get(1).equalsIgnoreCase("0")){
                Log.d(TAG,"res==null || res.get(1).equalsIgnoreCase(\"0\")...");
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setMessage("¿Se detecto una duracion de 0 segundos en la llamada, desea finalizar?")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Cursor c = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
                                String durationFin = CallLogHelper.getCallLogs(c).get(1);
                                Log.d(TAG,"onClick Aceptar duration:"+durationFin);
                                finalizar(durationFin,null,null);
                            }
                        })
                        .create();
                dialog.show();
            }
        }
        else{
            Log.d(TAG,"Finalizar...");
            if(numeroSelected==null){
                Log.d(TAG,"No ha realizado alguna llamada a un numero.");
                Toast.makeText(getActivity(), "No ha realizado alguna llamada a un numero", Toast.LENGTH_SHORT).show();
            }else{
                if(validateLlamada()){
                    finalizar(res.get(1),objeto,"Forzar");
                    //finalizar(res.get(1),null,null);
                }else{
                    Log.d(TAG,"No cumple con la validacion para finalizar llamadas...");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("RP3 Market Force");
                    builder.setMessage("¿Desea finalizar la gestion de la llamada?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG,"Si acepto fializar la gestion d la llamada");
                            finalizar(res.get(1),objeto,"Forzar");
                        }
                    });
                    AlertDialog d = builder.show();
                    d.show();
                    //Toast.makeText(getActivity(), "No ha realizado una llamada satisfactoria mayor a 30 segundos.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean validateLlamada(){
        boolean estado = false;
        final Cursor cursor= CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
        ArrayList<String> res = CallLogHelper.getCallLogs(cursor);
        if(res.size()>0){
            if(res.size()==3){
                    if(numeroSelected.equalsIgnoreCase(res.get(2)) && res.get(0).equalsIgnoreCase("Outgoing")){
                        //Calcular ahora la duracion de esa llamada
                        int duracion = Integer.parseInt(res.get(1));
                        ApplicationParameter parameter = ApplicationParameter.getParameter(Utils.getDataBase(getActivity()),
                                Constants.PARAMETERID_DURACIONLLAMADA,Constants.LABEL_LLAMADAS);
                        int duraa = Integer.parseInt(parameter.getValue());
                        if(duracion>duraa){
                            estado = true;
                        }
                    }
            }else {
                estado = false;
            }

        }else{
            estado = false;
        }
        return estado;
    }

    private boolean validateEditProspecto(List<String> numeros){
        boolean estado = false;
        final Cursor cursor= CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
        ArrayList<String> res = CallLogHelper.getCallLogs(cursor);
        if(res.size()>0){
            if(res.size()==3){
                for (String numero:numeros){
                    if(numero.equalsIgnoreCase(res.get(2)) && res.get(0).equalsIgnoreCase("Outgoing")){
                        //Calcular ahora la duracion de esa llamada
                        int duracion = Integer.parseInt(res.get(1));
                        ApplicationParameter parameter = ApplicationParameter.getParameter(Utils.getDataBase(getActivity()),
                                Constants.PARAMETERID_DURACIONLLAMADA,Constants.LABEL_LLAMADAS);
                        int duraa = Integer.parseInt(parameter.getValue());
                        if(duracion>duraa){
                            estado = true;
                        }
                    }
                }
            }else {
                estado = false;
            }

        }else{
            estado = false;
        }
        return estado;
    }

    private void finalizar(String duration,ProspectoVtaDb finalObjeto,String forzar){
        Log.d(TAG,"Method finalizar duration:"+duration);
        if(forzar!=null && finalObjeto!=null){
            Log.d(TAG,"forzar finalizar...");
            Log.d(TAG,"SelectedFinalizar...");
            finalizarLlamadaMotivos(finalObjeto);
        }
        else if(finalObjeto!=null){
            Log.d(TAG,"finalizar editar...");
            llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
            Log.d(TAG,"position selected..."+position);
            vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
            adapter.addMoreItems(vtaList);
            Log.d(TAG,"SessionManagerDuration:"+SessionManager.getInstance(getActivity()).getDataSession().getDuration());
            Cursor c = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
            String durationFin = CallLogHelper.getCallLogs(c).get(1);
            Log.d(TAG,"onClick Aceptar finalizar duration:"+durationFin);
            llamadaVta.setDuracion(Integer.parseInt(durationFin));
            Calendar ce = Calendar.getInstance();
            llamadaVta.setFechaFinLlamada(ce.getTime());
            Log.d(TAG,"LLamada:"+llamadaVta.toString());
            if(llamadaVta.getIdLlamada()>0){
                llamadaVta.setInsertado(0);
            }else{
                llamadaVta.setInsertado(1);
            }
            boolean result = LlamadaVta.update(Utils.getDataBase(getActivity()),llamadaVta);
            Log.d(TAG, result?"Se actualizo...":"No se actualizo...");
            Intent intent = new Intent(getActivity(),ProspectoActivity.class);
            Bundle todo = new Bundle();
            todo.putInt("Opcion",2);
            todo.putInt("Llamada",3);
            intent.putExtras(todo);
            Log.d(TAG, finalObjeto.toString());
            SessionManager.getInstance(getActivity()).createProspectoEdit(finalObjeto);
            getActivity().startActivityForResult(intent,REQUEST_PROSPECTO_EDIT);
        }
        else{
            Log.d(TAG,"SelectedFinalizar...");
            llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
            Log.d(TAG,"position selected..."+position);
            vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
            adapter.addMoreItems(vtaList);
            Log.d(TAG,"SessionManagerDuration:"+SessionManager.getInstance(getActivity()).getDataSession().getDuration());
            Cursor c = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
            String durationFin = CallLogHelper.getCallLogs(c).get(1);
            Log.d(TAG,"onClick Aceptar finalizar duration:"+durationFin);
            llamadaVta.setDuracion(Integer.parseInt(durationFin));
            Calendar ce = Calendar.getInstance();
            llamadaVta.setFechaFinLlamada(ce.getTime());
            llamadaVta.setLlamadaValor(1);
            //llamadaVta.setInsertado(0);
            Log.d(TAG,"LLamada:"+llamadaVta.toString());
            if(llamadaVta.getIdLlamada()>0){
                llamadaVta.setInsertado(0);
            }else{
                llamadaVta.setInsertado(1);
            }
            boolean result = LlamadaVta.update(Utils.getDataBase(getActivity()),llamadaVta);
            Log.d(TAG, result?"Se actualizo...":"No se actualizo...");
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
            ((Main2Activity)getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
            refresh();
        }
    }

    private void finalizarLlamadaMotivos(final ProspectoVtaDb objeto){
        final List<GeneralValue> motivos = GeneralValue.getGeneralValues(Utils.getDataBase(getActivity()),Contants.GENERAL_TABLE_MOTIVOS_RESPUESTAS_TABLE_ID_LLAMADA);
        ArrayList<String> list = new ArrayList<>();
        Log.d(TAG,"cantidad de motivos general");
        for (GeneralValue obj:motivos){
            list.add(obj.getValue());
        }

        GeneralValueDialog dialog = GeneralValueDialog.newInstance(new GeneralValueDialog.callbackGeneralSelected() {
            @Override
            public void onSelected(String selected, int position) {
                Log.d(TAG,"selected:"+selected+" position:"+position);
                GeneralValue obj = motivos.get(position);
                Log.d(TAG,"general selected:"+obj.toString());
                if(obj.getReference1()!=null){
                    if(obj.getReference1().trim().equalsIgnoreCase("1")){
                        Log.d(TAG,"Es llamame luego ah entonces reprogramar llamada...");
                        finalizarNoGestionMotivo(obj,1,objeto);
                    }else if(obj.getReference1().trim().equalsIgnoreCase("2")){
                        Log.d(TAG,"Es No contactar entonces no contactemoslo...");
                        finalizarNoGestionMotivo(obj,2,objeto);
                    }else if(obj.getReference1().trim().equalsIgnoreCase("3")){
                        Log.d(TAG,"Es Si contactar entonces Si desea el programa, contactemoslo con una visita...");
                        finalizarNoGestionMotivo(obj,4,objeto);
                    }else{
                        finalizarNoGestionMotivo(obj,3,objeto);
                    }
                }else{
                    finalizarNoGestionMotivo(obj,3,objeto);
                }
            }
        });
        Bundle todo = new Bundle();
        todo.putStringArrayList("list",list);
        todo.putString("Aceptar","Aceptar");
        dialog.setArguments(todo);
        dialog.setCancelable(true);
        dialog.show(getFragmentManager(),"");
    }

    private void finalizarNoGestionMotivo(GeneralValue general,int estado,ProspectoVtaDb objeto){
        if(estado==1){
            Log.d(TAG,"Si se reprogramara...");
            llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_REPROGRAMADA);
            llamadaVta.setMotivoVisitaTabla(Contants.GENERAL_TABLE_MOTIVOS_RESPUESTAS_TABLE_ID_LLAMADA);
            llamadaVta.setMotivoVisitaValue(general.getCode());
            Log.d(TAG,"position selected..."+position);
            vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_REPROGRAMADA);
            adapter.addMoreItems(vtaList);
            Log.d(TAG,"SessionManagerDuration:"+SessionManager.getInstance(getActivity()).getDataSession().getDuration());
            Cursor c = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
            String durationFin = CallLogHelper.getCallLogs(c).get(1);
            Log.d(TAG,"onClick Aceptar finalizar duration:"+durationFin);
            llamadaVta.setDuracion(Integer.parseInt(durationFin));
            Calendar ce = Calendar.getInstance();
            llamadaVta.setFechaFinLlamada(ce.getTime());
            //llamadaVta.setInsertado(0);
            if(llamadaVta.getIdLlamada()>0){
                llamadaVta.setInsertado(0);
            }else{
                llamadaVta.setInsertado(1);
            }
            Log.d(TAG,"LLamada:"+llamadaVta.toString());
            boolean result = LlamadaVta.update(Utils.getDataBase(getActivity()),llamadaVta);
            Log.d(TAG, result?"Se actualizo...":"No se actualizo...");
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
            SessionManager.getInstance(getActivity()).createSessionLlamada("Llamada");
            Log.d(TAG,"Objeto a pasar:"+objeto.toString());
            SessionManager.getInstance(getActivity()).createProspectoLlamada(objeto);
            ((Main2Activity) getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
        }else if(estado==2){
            Log.d(TAG,"No contactar...Al prospecto...");
            if(objeto!=null){
                if(objeto.getDocumento()!=null && objeto.getTipoDocumento()>0){
                    if(objeto.getDocumento().length()>0){
                        noContactar(general,objeto);
                    }
                }else{
                    noContactar(general,objeto);
                }
            }
        }else if(estado == 4){
            //Ecole aqui se debe validar si tiene direccion el prospectus
            Log.d(TAG,"Si desea el programa...crear visita...");
            Cursor ce = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
            String durationFin1 = CallLogHelper.getCallLogs(ce).get(1);
            int dur = Integer.parseInt(durationFin1);
            Log.d(TAG,"duracion...");
            ApplicationParameter parameter = ApplicationParameter.getParameter(Utils.getDataBase(getActivity()),
                    Constants.PARAMETERID_DURACIONLLAMADA,Constants.LABEL_LLAMADAS);
            int duraa = Integer.parseInt(parameter.getValue());
            if(dur>duraa){
                llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
                vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
            }else{
                llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
                vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
            }
            llamadaVta.setDuracion(Integer.parseInt(durationFin1));
            llamadaVta.setMotivoVisitaTabla(Contants.GENERAL_TABLE_MOTIVOS_RESPUESTAS_TABLE_ID_LLAMADA);
            llamadaVta.setMotivoVisitaValue(general.getCode());
            Log.d(TAG,"position selected..."+position);

            adapter.addMoreItems(vtaList);
            //Log.d(TAG,"SessionManagerDuration:"+SessionManager.getInstance(getActivity()).getDataSession().getDuration());
            //Cursor c = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
            //String durationFin = CallLogHelper.getCallLogs(c).get(1);
            //Log.d(TAG,"onClick Aceptar finalizar duration:"+durationFin);
            Calendar cee = Calendar.getInstance();
            llamadaVta.setFechaFinLlamada(cee.getTime());
            //llamadaVta.setInsertado(0);
            if(llamadaVta.getIdLlamada()>0){
                llamadaVta.setInsertado(0);
            }else{
                llamadaVta.setInsertado(1);
            }
            Log.d(TAG,"LLamada:"+llamadaVta.toString());
            objeto.setEstadoCode(Contants.GENERAL_VALUE_CODE_LLAMADA_RESPUESTA_SI);
            if(objeto.getIdProspecto()>0){
                objeto.setEstado(2);
            }else{
                objeto.setEstado(1);
            }
            boolean result = LlamadaVta.update(Utils.getDataBase(getActivity()),llamadaVta);
            Log.d(TAG, result?"Se actualizo llamada...":"No se actualizo llamada...");


            //
            if(objeto.getDireccion1()==null && objeto.getDireccion2()==null){
                showDialogConfirmDirectionProspecto(objeto);
            }else{
                if(objeto.getDireccion1().length()==0 && objeto.getDireccion2().length()==0){
                    showDialogConfirmDirectionProspecto(objeto);
                }else{
                    if(objeto.getDireccion1().length()>0){
                        String direccion1 = objeto.getDireccion1();
                        boolean resObjeto = ProspectoVtaDb.update(Utils.getDataBase(getActivity()),objeto);
                        Log.d(TAG, resObjeto?"Se actualizo prospecto...":"No se actualizo prospecto...");
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
                        SessionManager.getInstance(getActivity()).createSessionLlamada("LlamadaPrograma");
                        Log.d(TAG,"Objeto a pasar:"+objeto.toString());
                        SessionManager.getInstance(getActivity()).createProspectoLlamada(objeto);
                        ((Main2Activity) getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
                    }else{
                        String direccion2 = objeto.getDireccion2();
                        boolean resObjeto = ProspectoVtaDb.update(Utils.getDataBase(getActivity()),objeto);
                        Log.d(TAG, resObjeto?"Se actualizo prospecto...":"No se actualizo prospecto...");
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
                        SessionManager.getInstance(getActivity()).createSessionLlamada("LlamadaPrograma");
                        Log.d(TAG,"Objeto a pasar:"+objeto.toString());
                        SessionManager.getInstance(getActivity()).createProspectoLlamada(objeto);
                        ((Main2Activity) getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
                    }
                }
            }

        }else{
            Log.d(TAG,"No se reprogramara...");
            Cursor ce = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
            String durationFin1 = CallLogHelper.getCallLogs(ce).get(1);
            int dur = Integer.parseInt(durationFin1);
            ApplicationParameter parameter = ApplicationParameter.getParameter(Utils.getDataBase(getActivity()),
                    Constants.PARAMETERID_DURACIONLLAMADA,Constants.LABEL_LLAMADAS);
            int duraa = Integer.parseInt(parameter.getValue());
            Log.d(TAG,"duracion...");
            if(dur>duraa){
                llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
                vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
            }else{
                llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
                vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
            }
            llamadaVta.setDuracion(Integer.parseInt(durationFin1));
            //llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
            llamadaVta.setMotivoVisitaTabla(Contants.GENERAL_TABLE_MOTIVOS_RESPUESTAS_TABLE_ID_LLAMADA);
            llamadaVta.setMotivoVisitaValue(general.getCode());
            Log.d(TAG,"position selected..."+position);
            //vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
            adapter.addMoreItems(vtaList);
            Log.d(TAG,"SessionManagerDuration:"+SessionManager.getInstance(getActivity()).getDataSession().getDuration());
            //Cursor c = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
            //String durationFin = CallLogHelper.getCallLogs(c).get(1);
            //Log.d(TAG,"onClick Aceptar finalizar duration:"+durationFin);

            Calendar cee = Calendar.getInstance();
            llamadaVta.setFechaFinLlamada(cee.getTime());
            //llamadaVta.setInsertado(0);
            if(llamadaVta.getIdLlamada()>0){
                llamadaVta.setInsertado(0);
            }else{
                llamadaVta.setInsertado(1);
            }
            Log.d(TAG,"LLamada:"+llamadaVta.toString());
            boolean result = LlamadaVta.update(Utils.getDataBase(getActivity()),llamadaVta);
            Log.d(TAG, result?"Se actualizo...":"No se actualizo...");
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
            ((Main2Activity)getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
            refresh();
        }
        SessionManager.getInstance(getActivity()).removeLlamadaGestion();
    }

    private void noContactar(final GeneralValue general,final ProspectoVtaDb objeto){
        Log.d(TAG,"noContactar...");
        Cursor ce = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
        String durationFin1 = CallLogHelper.getCallLogs(ce).get(1);
        int dur = Integer.parseInt(durationFin1);
        Log.d(TAG,"duracion...");
        ApplicationParameter parameter = ApplicationParameter.getParameter(Utils.getDataBase(getActivity()),
                Constants.PARAMETERID_DURACIONLLAMADA,Constants.LABEL_LLAMADAS);
        int duraa = Integer.parseInt(parameter.getValue());
        if(dur>duraa){
            llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
            vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_SI_REALIZO_LLAMADA);
        }else{
            llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
            vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_NO_REALIZO_LLAMADA);
        }
        llamadaVta.setDuracion(Integer.parseInt(durationFin1));
        llamadaVta.setMotivoVisitaTabla(Contants.GENERAL_TABLE_MOTIVOS_RESPUESTAS_TABLE_ID_LLAMADA);
        llamadaVta.setMotivoVisitaValue(general.getCode());
        Log.d(TAG,"position selected..."+position);

        adapter.addMoreItems(vtaList);
        Log.d(TAG,"SessionManagerDuration:"+SessionManager.getInstance(getActivity()).getDataSession().getDuration());
        //Cursor c = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
        //String durationFin = CallLogHelper.getCallLogs(c).get(1);
        //Log.d(TAG,"onClick Aceptar finalizar duration:"+durationFin);
        Calendar cee = Calendar.getInstance();
        llamadaVta.setFechaFinLlamada(cee.getTime());
        //llamadaVta.setInsertado(0);
        if(llamadaVta.getIdLlamada()>0){
            llamadaVta.setInsertado(0);
        }else{
            llamadaVta.setInsertado(1);
        }
        Log.d(TAG,"LLamada:"+llamadaVta.toString());
        objeto.setEstadoCode(Contants.GENERAL_VALUE_CODE_LLAMADA_RESPUESTA_NO_ME_CONTACTES);
        if(objeto.getIdProspecto()>0){
            objeto.setEstado(2);
        }else{
            objeto.setEstado(1);
        }
        Log.d(TAG,"Prospecto a actualizar"+objeto.toString());
        boolean result = LlamadaVta.update(Utils.getDataBase(getActivity()),llamadaVta);
        Log.d(TAG, result?"Se actualizo llamada...":"No se actualizo llamada...");
        boolean resObjeto = ProspectoVtaDb.update(Utils.getDataBase(getActivity()),objeto);
        Log.d(TAG, resObjeto?"Se actualizo prospecto...":"No se actualizo prospecto...");
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
        ((Main2Activity)getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
        refresh();
    }

    private void robinsonServiceClient(final GeneralValue general,final ProspectoVtaDb obj){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.setTitle(getActivity().getResources().getString(R.string.appname_marketforce));
        progressDialog.show();
        final List<ProspectoVta> list = new ArrayList<>();
        ProspectoVta object = new ProspectoVta();
        if(obj.getIdProspecto()>0){
            object.setIdProspecto(obj.getIdProspecto());
        }else{
            object.setIdProspecto(0);
        }
        object.setNombre(obj.getNombre());
        object.setCelular(obj.getCelular());
        object.setTelefono(obj.getTelefono());
        object.setTipoDocumento(obj.getTipoDocumento());
        object.setDocumento(obj.getDocumento());
        object.setDireccion1(obj.getDireccion1());
        object.setDireccion2(obj.getDireccion2());
        object.setTipoPersonaCode(obj.getTipoPersonaCode());
        object.setRuc(obj.getRuc());
        object.setRazonSocial(obj.getRazonSocial());
        object.setNombres(obj.getNombres());
        object.setApellidoPaterno(obj.getApellidoPaterno());
        object.setApellidoMaterno(obj.getApellidoMaterno());
        object.setContactoNombre(obj.getContactoNombre());
        object.setContactoApellidoPaterno(obj.getContactoApellidoPaterno());
        object.setContactoApellidoMaterno(obj.getContactoApellidoMaterno());
        object.setContactoTelefono(obj.getContactoTelefono());
        object.setEmpresaTelefono(obj.getEmpresaTelefono());
        object.setEmail(obj.getEmail());
        object.setLlamadaReferido(obj.getLlamadaReferido());
        object.setVisitaReferido(obj.getVisitaReferido());
        object.setIdAgente(obj.getIdAgente());
        object.setOrigenCode(obj.getOrigenCode());
        object.setEstadoCode(obj.getEstadoCode());
        list.add(object);
        Thread hilo = new Thread(){
            @Override
            public void run() {
                new NoContactarClient(getContext(), new Callback() {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        Log.d(TAG,"onFailure...");
                        progressDialog.dismiss();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        Log.d(TAG,"onResponse...");
                        progressDialog.dismiss();
                        final String json = response.body().string();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(response.isSuccessful()){
                                    Log.d(TAG,"isSuccessful...");
                                    TypeToken<List<RepositorioRespuesta>> typeToken = new TypeToken<List<RepositorioRespuesta>>(){};
                                    List<RepositorioRespuesta> resp = new Gson().fromJson(json,typeToken.getType());
                                    if(resp.size()>0){
                                        Log.d(TAG,resp.get(0).toString());
                                        noContactar(general,obj);
                                    }else{
                                        Log.d(TAG,"resp.size == 0 ");
                                        noContactar(general,obj);
                                    }
                                }else{
                                    Log.d(TAG,"noIsSuccessful...");
                                }
                            }
                        });
                    }
                }).registrar(list);
            }
        };

        hilo.start();
        hilo.interrupt();

    }

    private void reprogramarCall(final LlamadaVta model,String code,String motivo) {
        int idProspecto = model.getIdCliente();
        Log.d(TAG, "idProspecto:" + idProspecto);
        List<ProspectoVtaDb> prospectoVtaDb = ProspectoVtaDb.getAll(Utils.getDataBase(getActivity()));
        ProspectoVtaDb objeto = null;
        if(model.getInsertado()==1){
            Log.d(TAG,"la agenda esta en bd sqlite...");
            for (ProspectoVtaDb obj:prospectoVtaDb) {
                Log.d(TAG,String.valueOf(obj.getIdProspecto())+" es IdProspecto...");
                if(obj.getEstado()==3){
                    if(obj.getIdProspecto() == idProspecto){
                        objeto = obj;
                        Log.d(TAG,"obj.getIdProspecto() == idProspecto...");
                        Log.d(TAG,obj.toString());
                        break;
                    }
                }else{
                    if(obj.getID() == idProspecto){
                        objeto = obj;
                        Log.d(TAG,"obj.getID() == idProspecto...");
                        Log.d(TAG,obj.toString());
                        break;
                    }
                }
            }
        }else{
            Log.d(TAG,"la agenda esta en servidor...");
            for (ProspectoVtaDb obj:prospectoVtaDb) {
                Log.d(TAG,String.valueOf(obj.getIdProspecto())+" es IdProspecto...");
                if(obj.getEstado()==1){
                    if(obj.getID() == idProspecto){
                        objeto = obj;
                        Log.d(TAG,"obj.getID() == idProspecto...");
                        Log.d(TAG,obj.toString());
                        break;
                    }
                }else{
                    if(obj.getIdProspecto() == idProspecto){
                        objeto = obj;
                        Log.d(TAG,"obj.getIdProspecto() == idProspecto...");
                        Log.d(TAG,obj.toString());
                        break;
                    }
                }
            }
        }
        llamadaSelected = model;
        llamadaVta = llamadaSelected;
        Calendar calendar = Calendar.getInstance();
        llamadaVta.setFechaInicioLlamada(null);
        llamadaVta.setLlamadaValor(0);
        llamadaVta.setReferidoValue("");
        llamadaVta.setObservacion(motivo);
        llamadaVta.setMotivoReprogramacionValue(code);
        llamadaVta.setMotivoReprogramacionTabla(GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_LLAMADA);
        Location location = ((Main2Activity) getActivity()).location;
        if (location != null) {
            llamadaVta.setLatitud(location.getLatitude());
            llamadaVta.setLongitud(location.getLongitude());
        } else {
            llamadaVta.setLatitud(0.00);
            llamadaVta.setLongitud(0.00);
        }
        llamadaVta.setLlamadaTabla(1831);
        llamadaVta.setMotivoReprogramacionTabla(1841);
        llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_REPROGRAMADA);
        vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_REPROGRAMADA);
        adapter.addMoreItems(vtaList);
        llamadaVta.setDuracion(0);
        Calendar c = Calendar.getInstance();
        llamadaVta.setFechaFinLlamada(null);
        //llamadaVta.setInsertado(0);
        if(llamadaVta.getIdLlamada()>0){
            llamadaVta.setInsertado(0);
        }else{
            llamadaVta.setInsertado(1);
        }
        Log.d(TAG, "LLamada:" + llamadaVta.toString());
        boolean result = LlamadaVta.update(Utils.getDataBase(getActivity()), llamadaVta);
        Log.d(TAG, result?"Se actualizo...":"No se actualizo...");
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
        SessionManager.getInstance(getActivity()).createSessionLlamada("Llamada");
        Log.d(TAG,"Objeto a pasar:"+objeto.toString());
        SessionManager.getInstance(getActivity()).createProspectoLlamada(objeto);
        ((Main2Activity) getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
    }

    private void cancelarCall(final LlamadaVta model,String code,String motivo){
        llamadaSelected = model;
        llamadaVta = llamadaSelected;
        Calendar calendar = Calendar.getInstance();
        llamadaVta.setFechaInicioLlamada(null);
        llamadaVta.setLlamadaValor(0);
        llamadaVta.setReferidoValue("");
        Location location = ((Main2Activity) getActivity()).location;
        if (location != null) {
            llamadaVta.setLatitud(location.getLatitude());
            llamadaVta.setLongitud(location.getLongitude());
        } else {
            llamadaVta.setLatitud(0.00);
            llamadaVta.setLongitud(0.00);
        }
        llamadaVta.setLlamadoValue(Contants.GENERAL_VALUE_CODE_LLAMADA_CANCELADA);
        llamadaVta.setObservacion(motivo);
        llamadaVta.setMotivoReprogramacionValue(code);
        llamadaVta.setMotivoReprogramacionTabla(GENERAL_TABLE_MOTIVOS_CANCELAR_LLAMADA_TABLE_ID);
        //llamadaVta.setLlamadaTabla(GENERAL_TABLE_MOTIVOS_CANCELAR_LLAMADA_TABLE_ID);
        vtaList.get(position).setLlamadoValue(GENERAL_VALUE_CODE_LLAMADA_CANCELADA);
        adapter.addMoreItems(vtaList);
        llamadaVta.setDuracion(0);
        Calendar c = Calendar.getInstance();
        llamadaVta.setFechaFinLlamada(null);
        //llamadaVta.setInsertado(1);
        if(llamadaVta.getIdLlamada()>0){
            llamadaVta.setInsertado(0);
        }else{
            llamadaVta.setInsertado(1);
        }
        Log.d(TAG, "LLamada:" + llamadaVta.toString());
        boolean result = LlamadaVta.update(Utils.getDataBase(getActivity()), llamadaVta);
        Log.d(TAG, result?"Se actualizo...":"No se actualizo...");
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
        ((Main2Activity) getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
        refresh();
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
        else if( id == R.id.agenda_action_filtrarllamada){
            Log.d(TAG,"item filtrar por fecha...");
            if(list!=null){
                Log.d(TAG,"No hay visitas en list...");
                if(list.size()>0){
                    Log.d(TAG,"Si hay visitas en list...");
                    showDatePickerDialog();
                }else{
                    Log.d(TAG,"No hay visitas count en list...");
                    Toast.makeText(getActivity(), "No tiene agenda de llamadas...", Toast.LENGTH_SHORT).show();
                }
            }else{
                Log.d(TAG,"No hay visitas en list...");
                Toast.makeText(getActivity(), "No tiene agenda de llamadas...", Toast.LENGTH_SHORT).show();
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
        newFragment.show(getFragmentManager(), "datePicker");
        */
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
                calendar2.setTime(list.get(i).getFechaLlamada());
                Log.d(TAG,"Date comparar:"+calendar2.getTime().toString());
                Log.d(TAG,"Año1:"+calendar1.get(Calendar.YEAR)+"DayOfYear1:"+calendar1.get(Calendar.DAY_OF_YEAR));
                Log.d(TAG,"Año2:"+calendar2.get(Calendar.YEAR)+"DayOfYear1:"+calendar2.get(Calendar.DAY_OF_YEAR));
                boolean sameDay = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                        calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
                if(sameDay){
                    Log.d(TAG,"sameDay == true...");
                    vtaList.add(list.get(i));
                }else{
                    Log.d(TAG,"sameDay == false..");
                }
            }
        }
        /*
        if(list!=null){
            for(int i = 0;i<list.size();i++){
                vtaList.add(list.get(i));
            }
        }*/
        adapter.addMoreItems(vtaList);
        Log.d(TAG,"vtaList a veR:"+vtaList.size());
        if(vtaList!=null){
            Log.d(TAG,"vtaList!=null..");
            if(vtaList.size()>0){
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                Log.d(TAG,"count de items..."+recyclerView.getAdapter().getItemCount());

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

    //region Loader and refresh

    public class LoaderAgendaVta implements LoaderManager.LoaderCallbacks<List<LlamadaVta>> {

        @Override
        public Loader<List<LlamadaVta>> onCreateLoader(int arg0, Bundle bundle) {
            Log.d(TAG,"onCreateLoader...");
            progressBar.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return new LlamadaVtaLoader(getActivity(), Utils.getDataBase(getActivity()),true,"");
        }

        @Override
        public void onLoadFinished(Loader<List<LlamadaVta>> arg0, List<LlamadaVta> data) {
            Log.d(TAG,"onLoadFinished...");
            if(getActivity() != null) {
                Log.d(TAG,"getActivity() != null...");
                if (data != null){
                    Log.d(TAG,"data != null...");
                    if (data.size() > 0) {
                        Log.d(TAG,"cantidad de llamadas agenda:"+data.size());
                        toolbar.setTranslationY(0);
                        appBarLayout.setTranslationY(0);
                        list = new ArrayList<>();
                        for(LlamadaVta obj:data){
                                list.add(obj);
                        }
                        Log.d(TAG,"cantidad de llamadas:"+list.size());
                        if(list.size()==0){
                            Log.d(TAG,"list.size()==0...");
                            recyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);
                        }else{
                            Log.d(TAG,"list.size()="+list.size());
                            Log.d(TAG,"list.size()>0...");
                            //Calendar calendar = Calendar.getInstance();
                            filtrarFecha(dateSelected);
                            recyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            container.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        Log.d(TAG,"cantidad de prospectos visita==0...");
                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        container.setVisibility(View.VISIBLE);
                    }
                }else{
                    Log.d(TAG,"data==null...");
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                }
            }else{
                Log.d(TAG,"getActivity == null...");
            }
        }

        @Override
        public void onLoaderReset(Loader<List<LlamadaVta>> arg0) {
            Log.d(TAG,"onLoaderReset...");
        }
    }

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

    //endregion


    private void validarEditarProspectoDurationLlamada(){

    }

    //region Ciclo de Vida

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG,"onCreateOptionsMenu...");
        inflater.inflate(R.menu.menu_agenda_fecha_llamada,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
        EventBus.getBus().register(this);
        estado = true;
        refresh();
        int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE,0);
    }


    @Override
    public void onStop() {
        super.onStop();

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
        EventBus.getBus().unregister(this);
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroyView();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.d(TAG,"getUserVisibleHint...");
        Log.d(TAG,"Value:"+super.getUserVisibleHint());
        return super.getUserVisibleHint();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        Log.d(TAG,"requestCode:"+requestCode+" resultCode:"+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //endregion

    //region Events

    public void obtenerDate(long date){
        dateSelected = Convert.getDateFromDotNetTicks(date);
        Log.d(TAG,"obtenerDate:"+dateSelected.toString());
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_REFRESH_LLAMADA);
        bundle.putLong(AgendaFragment.FECHA,date);
        ((Main2Activity)getActivity()).invokeSyncLlamada("Agenda","Obteniendo...",bundle);
    }

    @Subscribe
    public void recievedMessage(Events.Message msj){
        Log.d(TAG,"recievedMessage llamada...");
        Bundle todo = msj.getMessage();
        if(todo!=null){
            if (todo.getString("Llamada")!=null){
                if(todo.getString("Llamada").equalsIgnoreCase("Refresh")){
                    refresh();
                }
            }
        }

    }

    private void showDialogConfirmDirectionProspecto(final ProspectoVtaDb prospectoVtaDb){
        DireccionProspectoDialog direccionProspectoDialog = DireccionProspectoDialog.
                newInstance(new DireccionProspectoDialog.callbackDireccion() {
                    @Override
                    public void onSelectedFinalizar(String direccion) {
                        if(prospectoVtaDb.getIdProspecto()>0){
                            prospectoVtaDb.setEstado(2);
                        }else{
                            prospectoVtaDb.setEstado(1);
                        }
                        prospectoVtaDb.setDireccion1(direccion);
                        boolean resObjeto = ProspectoVtaDb.update(Utils.getDataBase(getActivity()),prospectoVtaDb);
                        Log.d(TAG, resObjeto?"Se actualizo prospecto...":"No se actualizo prospecto...");
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_LLAMADAVTA);
                        SessionManager.getInstance(getActivity()).createSessionLlamada("LlamadaPrograma");
                        Log.d(TAG,"Objeto a pasar:"+prospectoVtaDb.toString());
                        SessionManager.getInstance(getActivity()).createProspectoLlamada(prospectoVtaDb);
                        ((Main2Activity) getActivity()).invokeSyncLlamada("Agenda","Actualizando llamadas...",bundle);
                    }
                });
        direccionProspectoDialog.setCancelable(false);
        direccionProspectoDialog.show(getFragmentManager(),"");
    }

    //endregion
}
