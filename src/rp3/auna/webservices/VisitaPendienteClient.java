package rp3.auna.webservices;

import android.content.Context;
import android.util.Log;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rp3.auna.R;

/**
 * Created by Jesus Villa on 14/11/2017.
 */

public class VisitaPendienteClient {
    private static final String TAG = VisitaPendienteClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "llamada/GetVisitaPendiente";
    private Context context;
    private Callback callBack;

    public VisitaPendienteClient(Context context, Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void obtener(int idAgente){
        final String url = context.getResources().getString(R.string.url)+resource+"?idagente="+idAgente;
        Log.d(TAG,url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .get()
                .build();
        new OkHttpClient().newCall(request).enqueue(callBack);
    }
}
