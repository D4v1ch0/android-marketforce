package rp3.auna.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.R;
import rp3.auna.adapter.LlamadaAdapter;
import rp3.auna.customviews.DividerItemDecoration;
import rp3.auna.utils.Utils;

/**
 * Created by Jesus Villa on 11/12/2017.
 */

public class EmailContratanteDialog extends DialogFragment {
    private static final String TAG = EmailContratanteDialog.class.getSimpleName();
    @BindView(R.id.btnCancelar)Button btnCancelar;
    @BindView(R.id.btnAceptar)Button btnAceptar;
    @BindView(R.id.etEmailContratante)EditText etEmailContratante;
    @BindView(R.id.etCelularContratante)EditText etCelularContratante;
    @BindView(R.id.tilCodWallet) View view;
    @BindView(R.id.etCodWallet) EditText etCodWallet;
    String codWallet = "";
    callBack call;


    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_data_alignet, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        configureAcceptCancelButton();
        return builder.create();
    }

    private void configureAcceptCancelButton(){
        setCancelable(true);
        if(getArguments()!=null){
            if(getArguments().getString("Email")!=null){
                etEmailContratante.setText(getArguments().getString("Email"));
            }
            if(getArguments().getString("Celular")!=null){
                etCelularContratante.setText(getArguments().getString("Celular"));
            }
            if(getArguments().getString("CodWallet")!=null){
                view.setVisibility(View.GONE);
                etCodWallet.setText(getArguments().getString("CodWallet"));
                codWallet = getArguments().getString("CodWallet");
            }
        }
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnCancelar...");
                call.onSelectedCancelar();
                dismiss();
            }
        });
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"btnAceptar...");
                if(validate()){
                    call.onSelected(etEmailContratante.getText().toString(),etCelularContratante.getText().toString(),codWallet);
                    dismiss();
                }
            }
        });
    }

    private boolean validate(){
        if(TextUtils.isEmpty(etEmailContratante.getText())){
            etEmailContratante.setError("Escriba un correo porfavor");
            return false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailContratante.getText().toString().trim()).matches()){
            etEmailContratante.setError("Escriba el Email correctamente");
            return false;
        }
        if(TextUtils.isEmpty(etCelularContratante.getText())){
            etCelularContratante.setError("Escriba un celular porfavor");
            return false;
        }
        if(etCelularContratante.getText().toString().trim().length()==0){
            etCelularContratante.setError("Escriba un celular porfavor");
            return false;
        }
        return true;
    }

    public static EmailContratanteDialog newInstance( callBack call,Bundle todo){
        EmailContratanteDialog dialog = new EmailContratanteDialog();
        dialog.setArguments(todo);
        dialog.setListener(call);
        return dialog;
    }

    public void setListener(callBack call) {
        this.call = call;
    }


    public interface callBack{
        void onSelectedCancelar();
        void onSelected(String email,String celular,String codWallet);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
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
