package rp3.auna.webservices;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rp3.auna.R;

/**
 * Created by Jesus Villa on 25/01/2018.
 */

public class FinalizarVentaClient {
    private static final String TAG = FinalizarVentaClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "ventanueva/finalizarventa";
    private Context context;
    private Callback callBack;

    public FinalizarVentaClient(Context context, Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void finalizar(String json){
        final String url = context.getResources().getString(R.string.url)+resource;
        Log.d(TAG,url);
        Log.d(TAG,json);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JPEG,json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .post(requestBody).build();
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        b.build().newCall(request).enqueue(callBack);
    }
}
