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
import rp3.auna.bean.RegistroPago;

/**
 * Created by Jesus Villa on 23/11/2017.
 */

public class RegistrarPagoClient {
    private static final String TAG = RegistrarPagoClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "ventanueva/RegistrarPago/";
    private Context context;
    private Callback callBack;

    public RegistrarPagoClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void registrar(RegistroPago movil){
        final String url = context.getResources().getString(R.string.url)+resource;
        final String json = new Gson().toJson(movil);
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
