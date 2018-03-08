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
 * Created by Jesus Villa on 15/01/2018.
 */

public class ValidarSolicitudOncosysClient {
    private static final String TAG = ValidarSolicitudOncosysClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "payme/solicitudoncosys";
    private Context context;
    private Callback callBack;

    public ValidarSolicitudOncosysClient(Context context, Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void validar(String json){
        final String url = context.getResources().getString(R.string.url)+resource;
        Log.d(TAG,"Url:"+url);
        Log.d(TAG,"Json:"+json);
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
