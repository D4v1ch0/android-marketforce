package rp3.auna.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.adapter.LlamadaAdapter;
import rp3.auna.customviews.DividerItemDecoration;
import rp3.auna.utils.Utils;
import rp3.data.models.GeneralValue;

/**
 * Created by Jesus Villa on 24/10/2017.
 */

public class MotivoCitaDialog extends DialogFragment {
    private static final String  TAG = MotivoCitaDialog.class.getSimpleName();
    @BindView(R.id.tvLlamada)TextView tvTitulo;
    @BindView(R.id.rvListaTelefono)RecyclerView recyclerView;
    @BindView(R.id.btnEeditarProspecto)Button btnEditarProspecto;
    @BindView(R.id.btnCancelarProspecto)Button btnCancelar;
    @BindView(R.id.etMotivo)
    EditText etMotivo;
    private callbackElegir llamada;
    private RecyclerView.LayoutManager llmanager;
    private LlamadaAdapter adapter;
    private int selected = -1;
    List<GeneralValue> generalValues;

    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_reprogramacion_cita, null, false);
        builder.setView(v);
        builder.setCancelable(false);
        ButterKnife.bind(this, v);
        configureAcceptCancelButton();
        llmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llmanager);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        List<String> list = new ArrayList<>();
        if(getArguments().getInt("Tipo")==1){
            generalValues = GeneralValue.getGeneralValues(Utils.getDataBase(getActivity()), Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA);
            int repro = getArguments().getInt("Repro",0);
            if(repro==1){
                for (GeneralValue generalValue:generalValues){
                    if(generalValue.getReference1()!=null){
                        if(generalValue.getReference1().trim().length()>0){
                            if(generalValue.getReference1().equalsIgnoreCase("1")){
                                list.add(generalValue.getValue());
                            }
                        }
                    }
                }
            }else{
                for (GeneralValue generalValue:generalValues){
                    if(generalValue.getReference1()!=null){
                        if(generalValue.getReference1().trim().length()>0){
                                list.add(generalValue.getValue());

                        }
                    }
                }
            }

        }else{
            generalValues = GeneralValue.getGeneralValues(Utils.getDataBase(getActivity()),Contants.GENERAL_TABLE_MOTIVOS_CANCELAR_CITA_TABLE_ID);
            for (GeneralValue generalValue:generalValues){
                list.add(generalValue.getValue());
            }
        }

        adapter=new LlamadaAdapter(getActivity(), new LlamadaAdapter.OnSelectedListener() {
            @Override
            public void onSelected(String s, int position) {
                Log.d(TAG,"Eligio:"+s);
                selected = position;
            }
        });
        adapter.addMoreItems(list);
        recyclerView.setAdapter(adapter);
        return builder.create();
    }

    private void configureAcceptCancelButton(){
        btnEditarProspecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnAceptar...");
                //if(selected !=-1 && !TextUtils.isEmpty(etMotivo.getText())){
                if(selected !=-1){
                    llamada.onGeneralSelected(generalValues.get(selected).getCode(),etMotivo.getText().toString().trim());
                    dismiss();
                }else{
                    Toast.makeText(getActivity(), "Elija una opcion porfavor", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnCancelar...");
                dismiss();
            }
        });
    }

    public static MotivoCitaDialog newInstance(callbackElegir callbackLlamada){
        MotivoCitaDialog dialog = new MotivoCitaDialog();
        dialog.setListener(callbackLlamada);
        return dialog;
    }

    public void setListener(callbackElegir callbackElegir) {
        this.llamada = callbackElegir;
    }

    public interface callbackElegir{
        void onGeneralSelected(String code,String motivo);
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
