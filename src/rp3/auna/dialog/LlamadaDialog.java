package rp3.auna.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;
import rp3.auna.adapter.LlamadaAdapter;
import rp3.auna.bean.ProspectoVta;
import rp3.auna.models.ventanueva.AgendaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.customviews.DividerItemDecoration;
import rp3.auna.util.helper.CallLogHelper;

/**
 * Created by Jesus Villa on 18/10/2017.
 */

public class LlamadaDialog extends DialogFragment {
    private static final String TAG = LlamadaDialog.class.getSimpleName();
    @BindView(R.id.tvLlamada)TextView tvTitulo;
    @BindView(R.id.rvListaTelefono)RecyclerView recyclerView;
    @BindView(R.id.btnFinalizarLlamada)Button btnFinalizar;
    @BindView(R.id.btnEeditarProspecto)Button btnEditarProspecto;
    @BindView(R.id.btnReprogramarLlamada)Button btnReprogramar;
    private callbackLlamada llamada;
    private Bundle todo;
    private RecyclerView.LayoutManager llmanager;
    private LlamadaAdapter adapter;
    private ArrayList<String> list;
    private int prospectoVtaDb;
    private String titulo;
    private AgendaVta agendaVta;
    private boolean callState = false;
    private int duration = -1;
    private int dismissed = -1;

    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_llamada, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        todo=getArguments();
        prospectoVtaDb=todo.getInt("prospecto",0);
        titulo=todo.getString("titulo","");
        list = todo.getStringArrayList("lista");
        configureAcceptCancelButton();
        llmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llmanager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        adapter=new LlamadaAdapter(getActivity(), new LlamadaAdapter.OnSelectedListener() {
            @Override
            public void onSelected(String selected, int position) {
                Log.d(TAG,"Numero elegido:"+selected);
                llamada.onNumeroSelected(selected);
                //recyclerView.setEnabled(false);
            }
        });
        adapter.addMoreItems(list);
        recyclerView.setAdapter(adapter);
        return builder.create();
    }

    private void configureAcceptCancelButton(){
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnAceptar...");
                Log.d(TAG,"Se eligio la opcion finalizar la llamada...");
                dismissed = 1;
                llamada.onSelectedFinalizar(duration);
                dismiss();


            }
        });
        btnEditarProspecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnEditarProspecto...");
                llamada.onSelectedEditar();
                dismissed = -1;
                dismiss();
            }
        });

        btnReprogramar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnReprogramar...");
                dismissed = 1;
                llamada.onReprogramarSelected();
            }
        });
    }

    //region recycle
    /*private void finalizar(String cantidad){
        Log.d(TAG,"finalizar...");
        int duration = Integer.parseInt(cantidad);
        Log.d(TAG,"duration..."+duration);
        this.duration = duration;
        llamada.onSelectedFinalizar(duration);
        dismiss();
    }*/

    private void editarProspecto(){
        Log.d(TAG,"editarProspecto...");
    }

    private ArrayList<String> getCallLogs(Cursor curLog) {
        ArrayList<String> call = new ArrayList<>();
        while (curLog.moveToNext()) {
            String callNumber = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            String callName = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            if (callName == null) {
               Log.d(TAG,"Unknown");
            } else
                Log.d(TAG,(callName));
            String callDate = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long.parseLong(callDate)));
            String callType = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            if (callType.equals("1")) {
                Log.d(TAG,"Incoming");
                call.add("Incoming");
            } else {
                Log.d(TAG, "Outgoing");
                call.add("Outgoing");
            }
            String duration = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            Log.d(TAG,"Numero+"+callNumber);
            Log.d(TAG,"Fecha+"+dateString);
            Log.d(TAG,"Tipo+"+callType);
            Log.d(TAG,"Duration+"+duration);
            Log.d(TAG,"BREAKPOINT;");
            call.add(duration);
            break;
        }
        return call;
    }
    //endregion

    public static LlamadaDialog newInstance(callbackLlamada callbackLlamada){
        LlamadaDialog dialog = new LlamadaDialog();
        dialog.setCancelable(false);
        dialog.setListener(callbackLlamada);
        return dialog;
    }

    public void setListener(callbackLlamada callbackLlamada) {
        this.llamada = callbackLlamada;
    }


    public interface callbackLlamada{
        void onSelectedFinalizar(int duration);
        void onSelectedEditar();
        void onNumeroSelected(String numero);
        void onReprogramarSelected();
        void onDismissed(int dismissed);
    }

    /*private void showCallsLog(){
        final Cursor curLog = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
        ArrayList<String> res = CallLogHelper.getCallLogs(curLog);
        if(res==null || res.get(1).equalsIgnoreCase("0")){
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
                            Log.d(TAG,"onClick Aceptar duration:"+getCallLogs(curLog).get(1));
                            finalizar(getCallLogs(curLog).get(1));
                        }
                    })
                    .create();
            dialog.show();
        }else{
            Log.d(TAG,"Finalizar...");
            finalizar(getCallLogs(curLog).get(1));
        }
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createCustomDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
        if(callState){
            //showCallsLog();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        llamada.onDismissed(dismissed);
        super.onDismiss(dialog);

    }
}
