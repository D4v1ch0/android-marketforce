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
import rp3.auna.R;
import rp3.auna.adapter.GeneralValueAdapter;
import rp3.auna.customviews.DividerItemDecoration;

/**
 * Created by Jesus Villa on 25/01/2018.
 */

public class DireccionProspectoDialog extends DialogFragment {
    private static final String TAG = CitaDialog.class.getSimpleName();
    @BindView(R.id.btnEnviarDireccion)Button btnFinalizar;
    @BindView(R.id.etEmailDireccion)EditText etDireccion;
    private callbackDireccion llamada;

    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_direccion_prospecto, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        configureAcceptCancelButton();

        return builder.create();
    }

    private void configureAcceptCancelButton(){
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnCancelar...");
                Log.d(TAG,"Se eligio la opcion cancelar...");
                if(validate()){
                    llamada.onSelectedFinalizar(etDireccion.getText().toString());
                    dismiss();
                }
            }
        });
    }

    private boolean validate(){
        if(TextUtils.isEmpty(etDireccion.getText())){
            Toast.makeText(getActivity(), "Agregar ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static DireccionProspectoDialog newInstance(callbackDireccion callbackLlamada){
        DireccionProspectoDialog dialog = new DireccionProspectoDialog();
        dialog.setListener(callbackLlamada);
        return dialog;
    }

    public void setListener(callbackDireccion callbackLlamada) {
        this.llamada = callbackLlamada;
    }


    public interface callbackDireccion{
        void onSelectedFinalizar(String direccion);
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
