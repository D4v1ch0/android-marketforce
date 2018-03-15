package rp3.auna.webservices;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rp3.auna.R;

/**
 * Created by Jesus Villa on 13/03/2018.
 */

public class ObtenerLlamadaClient {
    private static final String TAG = ObtenerLlamadaClient.class.getSimpleName();
    private String resource = "agendavta/GetLlamadas";
    private Context context;
    private Callback callBack;

    public ObtenerLlamadaClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void obtener(int idAgente,long fecha){
        final String url = context.getResources().getString(R.string.url)
                +resource+"?idagente="+idAgente+"&fecha="+fecha;
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
