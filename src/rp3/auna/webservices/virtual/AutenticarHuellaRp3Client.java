package rp3.auna.webservices.virtual;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.util.helper.Util;
import rp3.configuration.PreferenceManager;

/**
 * Created by Jesus Villa on 09/03/2018.
 */

public class AutenticarHuellaRp3Client {
    private static final String TAG = AutenticarHuellaRp3Client.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_MULTIPART= MediaType.parse("multipart/form-data");
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final MediaType MEDIA_TYPE_PDF= MediaType.parse("application/pdf");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String resource = "ventavirtual/AutenticateFingerPrint";
    private Context context;
    private Callback callBack;

    public AutenticarHuellaRp3Client(Context context, Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void autenticar(byte[] data){
        int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE,0);
        if(idAgente==0){
            Toast.makeText(context, "Debe cerrar sesión ó sincronizar porfavor.", Toast.LENGTH_SHORT).show();
            return;
        }
        String nombreFoto = idAgente+Util.nombreFoto();
        final String url = context.getResources().getString(R.string.url)+resource;
        Log.d(TAG,"URL:"+url);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("Archivo", nombreFoto,
                RequestBody.create(MEDIA_TYPE_JPEG, data));

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody).build();
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(280, TimeUnit.SECONDS)
                .writeTimeout(280, TimeUnit.SECONDS)
                .readTimeout(280, TimeUnit.SECONDS);
// set other properties

        OkHttpClient client = b.build();
        client.newCall(request).enqueue(callBack);
    }
}
