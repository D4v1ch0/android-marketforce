package rp3.auna.webservices.virtual;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.bean.virtual.Authentication;
import rp3.auna.util.helper.Util;
import rp3.configuration.PreferenceManager;

/**
 * Created by Jesus Villa on 03/04/2018.
 */

public class UploadFingerPrintRp3Client {
    private static final String TAG = AutenticarHuellaRp3Client.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_MULTIPART= MediaType.parse("multipart/form-data");
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final MediaType MEDIA_TYPE_PDF= MediaType.parse("application/pdf");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String resource = "ventavirtual/UploadFingerPrint";
    private Context context;
    private Callback callBack;

    public UploadFingerPrintRp3Client(Context context, Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void upload(byte[] data,Authentication persona){

        String json = new Gson().toJson(persona);
        final String url = context.getResources().getString(R.string.url)+resource;
        Log.d(TAG,"URL:"+url);
        Log.d(TAG,"JSON:"+json);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("Archivo", persona.getImagen(),
                RequestBody.create(MEDIA_TYPE_JPEG, data));
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"Persona\""),
                RequestBody.create(JSON,json));
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
