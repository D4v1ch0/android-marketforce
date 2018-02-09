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
 * Created by Jesus Villa on 14/11/2017.
 */

public class CotizacionVisitaClient {
    private static final String TAG = CotizacionVisitaClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "ventanueva/GetCotizacionVisita";
    private Context context;
    private Callback callBack;

    public CotizacionVisitaClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void obtener(int idvisita){
        final String url = context.getResources().getString(R.string.url)+resource+"?idvisita="+idvisita;
        Log.d(TAG,url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .get().build();
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS);

        b.build().newCall(request).enqueue(callBack);
    }
}
