package rp3.auna.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rp3.auna.R;
import rp3.auna.bean.CotizacionMovil;

/**
 * Created by Jesus Villa on 27/11/2017.
 */

public class EmailInstruccionesPagoClient {
    private static final String TAG = EmailInstruccionesPagoClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "ventanueva/SendEmailInstruccionesPago/";
    private Context context;
    private Callback callBack;

    public EmailInstruccionesPagoClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void enviar(String json){
        final String url = context.getResources().getString(R.string.url)+resource;
        Log.d(TAG,url);
        Log.d(TAG,json);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JPEG,json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .post(requestBody).build();
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS);

        b.build().newCall(request).enqueue(callBack);
    }
}