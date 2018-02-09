package rp3.auna.dialog;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;
import rp3.auna.adapter.LlamadaAdapter;
import rp3.auna.customviews.DividerItemDecoration;
import rp3.auna.models.ventanueva.AgendaVta;

/**
 * Created by Jesus Villa on 20/10/2017.
 */

public class ElegirLlamadaDialog extends DialogFragment {
    private static final String TAG = ElegirLlamadaDialog.class.getSimpleName();
    @BindView(R.id.tvLlamada)TextView tvTitulo;
    @BindView(R.id.rvListaTelefono)RecyclerView recyclerView;
    @BindView(R.id.btnFinalizarLlamada)Button btnFinalizar;
    @BindView(R.id.btnEeditarProspecto)Button btnEditarProspecto;
    private callbackLlamadaelegir llamada;
    private RecyclerView.LayoutManager llmanager;
    private LlamadaAdapter adapter;
    private int selected = -1;

    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_elegir_llamada, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        configureAcceptCancelButton();
        llmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llmanager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        List<String> list = new ArrayList<>();
        list.add("Gestionar llamada");
        list.add("Reprogramar llamada");
        list.add("Cancelar llamada");
        adapter=new LlamadaAdapter(getActivity(), new LlamadaAdapter.OnSelectedListener() {
            @Override
            public void onSelected(String s, int position) {
                Log.d(TAG,"Eligio:"+s);
                //llamada.onNumeroSelected(position);
                selected = position;
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
                Log.d(TAG,"btnCancelar...");
                Log.d(TAG,"Se eligio la opcion cancelar...");
                llamada.onSelectedFinalizar();
                dismiss();
            }
        });
        btnEditarProspecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnAceptar...");
                if(selected !=-1){
                    llamada.onNumeroSelected(selected);
                    dismiss();
                }else{
                    Toast.makeText(getActivity(), "Elija una opcion porfavor", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public static ElegirLlamadaDialog newInstance(ElegirLlamadaDialog.callbackLlamadaelegir callbackLlamada){
        ElegirLlamadaDialog dialog = new ElegirLlamadaDialog();
        dialog.setListener(callbackLlamada);
        return dialog;
    }

    public void setListener(callbackLlamadaelegir callbackLlamada) {
        this.llamada = callbackLlamada;
    }


    public interface callbackLlamadaelegir{
        void onSelectedFinalizar();
        void onNumeroSelected(int position);
    }

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

    }
}
