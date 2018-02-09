package rp3.auna.webservices;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rp3.auna.R;

/**
 * Created by Jesus Villa on 01/01/2018.
 */

public class ValidateDocumentClient {
    private static final String TAG = ValidateDocumentClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "VentaNueva/ValidateDocumentProspecto";
    private Context context;
    private Callback callBack;

    public ValidateDocumentClient(Context context, Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void validar(String documento){
        final String url = context.getResources().getString(R.string.url)+resource+"?documento="+documento;
        Log.d(TAG,url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .get()
                .build();
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS);

        b.build().newCall(request).enqueue(callBack);
    }
}
