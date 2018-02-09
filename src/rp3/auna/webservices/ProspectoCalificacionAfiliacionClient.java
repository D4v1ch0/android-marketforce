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
import rp3.auna.bean.Informacion;

/**
 * Created by Jesus Villa on 26/10/2017.
 */

public class ProspectoCalificacionAfiliacionClient {
    private static final String TAG = ProspectoCalificacionAfiliacionClient.class.getSimpleName();
    private String resource = "prospectovta/FiltraNumeroDocumento";
    private Context context;
    private Callback callBack;

    public ProspectoCalificacionAfiliacionClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void validar(int tipodocumento,String documento){
        final String url = context.getResources().getString(R.string.url)
                +resource+"?tipo_documento="+tipodocumento+"&documento="+documento;
        Log.d(TAG,url);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .addHeader("content-type", "application/json")
                .build();
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS);

        b.build().newCall(request).enqueue(callBack);
    }
}
