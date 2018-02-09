package rp3.auna.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;
import rp3.auna.bean.CotizacionMovil;
import rp3.auna.utils.Utils;

/**
 * Created by Jesus Villa on 27/10/2017.
 */

public class EmailCotizacionDialog extends DialogFragment {
    private static final String TAG = CitaDialog.class.getSimpleName();
    @BindView(R.id.btnCancelarEnvioEmail)Button btnFinalizar;
    @BindView(R.id.btnEnviarEmail)Button btnEditarProspecto;
    @BindView(R.id.etEmailCotizacion) EditText etEmail;
    private callbackEmail callback;
    private String email;
    private int estado = 0;

    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_email_cotizacion, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        configureAcceptCancelButton();
        return builder.create();
    }

    private void configureAcceptCancelButton(){
        int pago = getArguments().getInt("Pago",0);
        if(pago==1){
            btnFinalizar.setVisibility(View.GONE);
        }
        if(email!=null)
            etEmail.setText(email);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnCancelar...");
                Log.d(TAG,"Se eligio la opcion cancelar...");
                callback.onSelectedCancelar();
                dismiss();
            }
        });
        btnEditarProspecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnAceptar...");
                if(validate()){
                    Log.d(TAG,"Email es valido...");
                    callback.onEmailEnviar(etEmail.getText().toString().trim());
                    dismiss();
                }
            }
        });
        if(estado==1){
            btnFinalizar.setVisibility(View.GONE);
        }
    }

    private boolean validate(){
        if(TextUtils.isEmpty(etEmail.getText())){
            etEmail.setError("Escriba un correo porfavor");
            return false;
        }
        if(!Utils.validarCorreo(etEmail.getText().toString().trim())){
            etEmail.setError("Escriba un formato correcto de Email");
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()){
            etEmail.setError("Escriba el Email correctamente");
            return false;
        }
        return true;
    }

    public static EmailCotizacionDialog newInstance(callbackEmail callback,String email,int estado){
        EmailCotizacionDialog dialog = new EmailCotizacionDialog();
        dialog.setListener(callback,email,estado);
        return dialog;
    }

    public void setListener(callbackEmail callback,String email,int estado) {
        this.callback = callback;
        this.email = email;
        this.estado = estado;
    }

    public interface callbackEmail{
        void onSelectedCancelar();
        void onEmailEnviar(String email);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createCustomDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
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
