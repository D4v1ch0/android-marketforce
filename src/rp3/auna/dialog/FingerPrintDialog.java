package rp3.auna.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import asia.kanopi.fingerscan.Fingerprint;
import asia.kanopi.fingerscan.Status;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rp3.auna.R;
import rp3.auna.util.helper.Util;
import rp3.auna.webservices.AutenticarHuellaRp3Client;

/**
 * Created by Jesus Villa on 09/03/2018.
 */

public class FingerPrintDialog extends DialogFragment {
    private static final String TAG = FingerPrintDialog.class.getSimpleName();
    @BindView(R.id.fingerprint_icon)ImageView ivFinger;
    @BindView(R.id.fingerprint_status)TextView tvAutenticate;
    private Fingerprint fingerprint;
    FingerPrintDialog.callBackListener call;
    String errorScanner;
    ProgressDialog progressDialog;
    //region Handlers Scanner
    Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"updateHandler...");
            if(msg!=null){
                Log.d(TAG,"msg!=null...");
                System.out.println(msg);
            }else{
                Log.d(TAG,"msg==null...");
            }
            int status = msg.getData().getInt("status");
            errorScanner=null;
            switch (status) {
                case Status.INITIALISED:
                    errorScanner=("Setting up reader");
                    tvAutenticate.setText("Configurar el lector");
                    break;
                case Status.SCANNER_POWERED_ON:
                    errorScanner=("Reader powered on");
                    tvAutenticate.setText("Lector encendido, presione el sensor");
                    break;
                case Status.READY_TO_SCAN:
                    errorScanner=("Ready to scan finger");
                    tvAutenticate.setText("Listo para scanear huella");
                    break;
                case Status.FINGER_DETECTED:
                    errorScanner=("Finger detected");
                    tvAutenticate.setText("Huella no reconocida por el lector, presione nuevamente.");
                    break;
                case Status.RECEIVING_IMAGE:
                    errorScanner=("Receiving image");
                    tvAutenticate.setText("Recibiendo huella...");
                    break;
                case Status.FINGER_LIFTED:
                    errorScanner=("El dedo ha sido levantado del lesto");
                    tvAutenticate.setText("Configurar el lector");
                    break;
                case Status.SCANNER_POWERED_OFF:
                    errorScanner=("Reader is off");
                    tvAutenticate.setText("El lector esta apagado.");
                    break;
                case Status.SUCCESS:
                    errorScanner=("Fingerprint successfully captured");
                    tvAutenticate.setText("Huella reconocida, autenticar...");
                    break;
                case Status.ERROR:
                    errorScanner=("Error");
                    errorScanner=(msg.getData().getString("errorMessage"));
                    tvAutenticate.setText(errorScanner);
                    break;
                default:
                    errorScanner=(String.valueOf(status));
                    errorScanner=(msg.getData().getString("errorMessage"));
                    tvAutenticate.setText(errorScanner);
                    break;

            }
        }
    };

    Handler printHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"printHandler...");

            if(msg!=null){
                Log.d(TAG,"msg!=null...");
                System.out.println(msg);
            }else{
                Log.d(TAG,"msg==null...");
            }
            byte[] image;
            String errorMessage = "empty";
            int status = msg.getData().getInt("status");
            Intent intent = new Intent();
            intent.putExtra("status", status);
            if (status == Status.SUCCESS) {
                Log.d(TAG,"statuss== SUCCESS..");
                image = msg.getData().getByteArray("img");
                progressDialog = Util.createProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Autenticando...");
                progressDialog.show();
                new AutenticarHuellaRp3Client(getActivity(), new okhttp3.Callback() {
                    Handler handler =new Handler(Looper.getMainLooper());
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG,"onFailure...");
                        Log.d(TAG,"IOException:"+e.getMessage());
                        e.printStackTrace();
                        progressDialog.dismiss();
                        handler.post(() -> {
                            Toast.makeText(getActivity(), R.string.message_error_sync_connection_http_error, Toast.LENGTH_SHORT).show();
                            onDestroy();
                        });
                    }

                    @Override
                    public void onResponse(Call calll, Response response) throws IOException {
                        Log.d(TAG,"onResponse...");
                        final String rpta = response.message();
                        progressDialog.dismiss();
                        Log.d(TAG,"rpta:"+rpta);
                        handler.post(() -> {
                            if(response.isSuccessful()){
                                Log.d(TAG,"response.isSuccessful...");
                                if(rpta.equalsIgnoreCase("1")){
                                    Log.d(TAG,"Autenticado....");
                                    call.onSuccess();
                                    Toast.makeText(getActivity(), "Autenticado satisfactoriamente!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(), "No se encuentra registrado.", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Log.d(TAG,"response no is Successful...");
                                Toast.makeText(getActivity(), rpta, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).autenticar(image);


            } else {
                Log.d(TAG,"Status != SUCCESS...");
                errorMessage = msg.getData().getString("errorMessage");
                intent.putExtra("errorMessage", errorMessage);
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
            //setResult(RESULT_OK, intent);
            //finish();
        }
    };
    //endregion


    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_autenticacion_virtual_mkf, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        configureScanner();
        return builder.create();
    }

    private void configureScanner(){
        setCancelable(true);
        fingerprint = new Fingerprint();
    }

    public static FingerPrintDialog newInstance(FingerPrintDialog.callBackListener call, Bundle todo){
        FingerPrintDialog dialog = new FingerPrintDialog();
        dialog.setArguments(todo);
        dialog.setListener(call);
        return dialog;
    }

    public void setListener(FingerPrintDialog.callBackListener call) {
        this.call = call;
    }


    public interface callBackListener{
        void onError(String mensaje);
        void onSuccess();
    }

    //region Ciclo de vida

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
    public void onStart() {
        Log.d(TAG,"onStart...");
        super.onStart();
    }

    @Override
    public void onDestroyView() {

        Log.d(TAG,"onDestroyView...");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fingerprint.turnOffReader();
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
        fingerprint.scan(getActivity(), printHandler, updateHandler);
        Log.d(TAG,"onResume...");

    }

    //endregion
}