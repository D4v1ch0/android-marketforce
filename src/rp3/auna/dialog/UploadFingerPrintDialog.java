package rp3.auna.dialog;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import asia.kanopi.fingerscan.Fingerprint;
import asia.kanopi.fingerscan.Status;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import rp3.auna.Contants;
import rp3.auna.CotizacionActivity;
import rp3.auna.R;
import rp3.auna.bean.virtual.Authentication;
import rp3.auna.util.helper.Util;
import rp3.auna.util.print.FingerPrint;
import rp3.auna.webservices.virtual.AutenticarHuellaRp3Client;
import rp3.configuration.PreferenceManager;

/**
 * Created by Jesus Villa on 03/04/2018.
 */

public class UploadFingerPrintDialog extends DialogFragment {
        private static final String TAG = UploadFingerPrintDialog.class.getSimpleName();
        @BindView(R.id.fingerprint_iconn)ImageView ivFinger;
        //@BindView(R.id.tvAuthenticatee)TextView tvAutenticate;
        @BindView(R.id.btnSubirUploadFinger) Button btnSubir;
        @BindView(R.id.btnCancelarUploadFinger) Button btnCancelar;
        @Nullable @BindView(R.id.etNumeroDocumentoo) EditText etDocumento;
        @Nullable @BindView(R.id.etNombree) EditText etNombre;
        @Nullable @BindView(R.id.etApellidoss) EditText etApellidos;
        private FingerPrint fingerprint;
        callBackListener call;
        String errorScanner;
        byte[] imageData = null;
        //region Handlers Scanner
        Handler updateHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG,"updateHandler Upload...");
                try{
                    if(msg!=null){
                        Log.d(TAG,"msg!=null...");
                        //System.out.println(msg);
                    }else{
                        Log.d(TAG,"msg==null...");
                    }
                    int status = msg.getData().getInt("status");
                    errorScanner=null;
                    switch (status) {
                        case Status.INITIALISED:
                            errorScanner=("Setting up reader");
                            //tvAutenticate.setText("Configurar el lector");
                            break;
                        case Status.SCANNER_POWERED_ON:
                            errorScanner=("Reader powered on");
                            //tvAutenticate.setText("Lector encendido, presione el sensor");
                            break;
                        case Status.READY_TO_SCAN:
                            errorScanner=("Ready to scan finger");
                            //tvAutenticate.setText("Listo para scanear huella");
                            break;
                        case Status.FINGER_DETECTED:
                            errorScanner=("Finger detected");
                            //tvAutenticate.setText("Huella no reconocida por el lector, presione nuevamente.");
                            break;
                        case Status.RECEIVING_IMAGE:
                            errorScanner=("Receiving image");
                            //tvAutenticate.setText("Recibiendo huella...");
                            break;
                        case Status.FINGER_LIFTED:
                            errorScanner=("El dedo ha sido levantado del lesto");
                            //tvAutenticate.setText("Configurar el lector");
                            break;
                        case Status.SCANNER_POWERED_OFF:
                            errorScanner=("Reader is off");
                            //tvAutenticate.setText("El lector esta apagado.");
                            break;
                        case Status.SUCCESS:
                            errorScanner=("Fingerprint successfully captured");
                            //tvAutenticate.setText("Huella reconocida, autenticar...");
                            break;
                        case Status.ERROR:
                            errorScanner=("Error");
                            errorScanner=(msg.getData().getString("errorMessage"));
                            //tvAutenticate.setText(errorScanner);
                            break;
                        default:
                            errorScanner=(String.valueOf(status));
                            errorScanner=(msg.getData().getString("errorMessage"));
                            //tvAutenticate.setText(errorScanner);
                            break;

                    }
                }catch (Exception e){
                    Log.d(TAG,"catch:"+e.getMessage());
                    e.printStackTrace();
                    Util.ErrorToFile(e);
                }
            }
        };

        Handler printHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.d(TAG,TAG+"printHandler Upload...");
                        //super.handleMessage(msg);
                        try{
                            if(msg!=null){
                                Log.d(TAG,"msg!=null...");
                                //System.out.println(msg);
                            }else{
                                Log.d(TAG,"msg==null...");
                            }
                            byte[] image;
                            String errorMessage = "empty";
                            int status = msg.getData().getInt("status");
                            //Intent intent = new Intent();
                            //intent.putExtra("status", status);
                            if (status == Status.SUCCESS) {
                                Log.d(TAG,"statuss== SUCCESS..");
                                try{
                                    image = msg.getData().getByteArray("img");
                                    imageData = image;
                                    if(imageData!=null){
                                        Log.d(TAG,"imageData!=null...");
                                        dismiss();
                                        //Toast.makeText(getActivity(), "IMAGEDATA", Toast.LENGTH_SHORT).show();
                                        //Drawable finger = getResources().getDrawable(R.drawable.ic_finger_black);
                                        //Bitmap bitmap = Util.getBitmapByte(imageData);
                                        //Glide.with(getActivity()).load(bitmap).centerCrop().placeholder(R.drawable.ic_finger_black).into(ivFinger);
                                    }else{
                                        Log.d(TAG,"imageData==null...");
                                    }
                                }catch (Exception e){
                                    Log.d(TAG,e.getMessage());
                                    e.printStackTrace();
                                    Util.ErrorToFile(e);
                                }
                                //ivFinger.setBackgroundColor(Color.WHITE);
                            } else {
                                Log.d(TAG,"Status != SUCCESS...");
                                errorMessage = msg.getData().getString("errorMessage");
                                //intent.putExtra("errorMessage", errorMessage);
                                //call.onError(errorMessage);
                                dismiss();
                            }
                        }catch (Exception e){
                            Log.d(TAG,"Catch:"+e.getMessage());
                            e.printStackTrace();
                            Util.ErrorToFile(e);
                        }
                    }
                };
        //endregion

    @Nullable
    @OnClick(R.id.btnSubirUploadFinger)
    void onSubirClicked() {
        // TODO ...
        try {
            Log.d(TAG,"onSubirClicked...");
            String apellidos = etApellidos.getText().toString();
            String nombres = etNombre.getText().toString();
            String documento = etDocumento.getText().toString();
            if(imageData==null){
                Log.d(TAG,"imageData==null...");
                try {
                    Toast.makeText(getActivity(), "Debe registrar una huella porfavor, intentelo nuevamente.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }catch (Exception e){
                    Log.d(TAG,e.getMessage());
                    e.printStackTrace();
                    Util.ErrorToFile(e);
                }

            }else if(TextUtils.isEmpty(apellidos) ||
                    TextUtils.isEmpty(nombres) ||
                    TextUtils.isEmpty(documento)){
                Log.d(TAG,"Faltan campos...");
                try {
                    Toast.makeText(getActivity(), "Debe llenar todos los campos, intentelo nuevamente.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }catch (Exception e){
                    Log.d(TAG,e.getMessage());
                    e.printStackTrace();
                    Util.ErrorToFile(e);
                }

            }else if (apellidos.trim().length() == 0 ||
                    nombres.trim().length() == 0 ||
                    documento.trim().length() == 0){
                Log.d(TAG,"Faltan valores...");
                try {
                    Toast.makeText(getActivity(), "Debe llenar todos los campos, intentelo nuevamente.", Toast.LENGTH_SHORT).show();
                    dismiss();
                }catch (Exception e){
                    Log.d(TAG,e.getMessage());
                    e.printStackTrace();
                    Util.ErrorToFile(e);
                }

            }else{
                Log.d(TAG,"Todo OKK...");
                try {
                    Authentication person = new Authentication();
                    person.setIdAuthentication(0);
                    person.setNombres(etNombre.getText().toString());
                    person.setApellidos(etApellidos.getText().toString());
                    person.setDocumento(etDocumento.getText().toString());
                    int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE,0);
                    if(idAgente==0){
                        Toast.makeText(getActivity(), "Debe cerrar sesión ó sincronizar porfavor.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String nombreFoto = idAgente+ Util.nombreFoto();
                    person.setImagen(nombreFoto);
                    Log.d(TAG,"personaaaaaaaaaaaaaaaaaaaaaa");
                    call.onSuccess(imageData,person);
                    dismiss();
                }catch (Exception e){
                    Log.d(TAG,e.getMessage());
                    e.printStackTrace();
                    Util.ErrorToFile(e);
                }
            }
        }catch (Exception e){
            Log.d(TAG,"Catch:"+e.getMessage());
            e.printStackTrace();
            Util.ErrorToFile(e);
        }
    }

    @Nullable
    @OnClick(R.id.btnCancelarUploadFinger)
    void onCanceledClicked() {
        // TODO ...
        dismiss();
    }

    public AlertDialog createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_layout_upload_fingerprint, null, false);
        builder.setView(v);
        ButterKnife.bind(this, v);
        configureScanner();
        //configure();
        return builder.create();
    }

    private void configureScanner(){
        setCancelable(true);
        fingerprint = new FingerPrint();
    }

    public static UploadFingerPrintDialog newInstance(UploadFingerPrintDialog.callBackListener call){
        UploadFingerPrintDialog dialog = new UploadFingerPrintDialog();
        dialog.setListener(call);
        return dialog;
    }

    public void setListener(UploadFingerPrintDialog.callBackListener call) {
        this.call = call;
        //this.updateHandler = updateHandler;
        //this.printHandler = printHandler;
    }

    private void configure(){
        btnCancelar.setOnClickListener(v -> {});
        btnSubir.setOnClickListener(v -> {

        });
    }

    public interface callBackListener{
        void onError(String mensaje);
        void onSuccess(byte[] data,Authentication person);
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
        try {
            fingerprint.scan(getActivity(), printHandler, updateHandler);
        }catch (Exception e){
            Log.d(TAG,"onStart...");
            e.printStackTrace();
            Util.ErrorToFile(e);
        }
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG,"onDestroyView...");
        try{
            fingerprint.turnOffReader();
        }catch (Exception e){
            e.printStackTrace();
            Util.ErrorToFile(e);
        }
        super.onDestroyView();

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

    //endregion
}
