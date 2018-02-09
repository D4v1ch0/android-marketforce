package rp3.auna.webservices;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rp3.auna.R;

/**
 * Created by Jesus Villa on 21/11/2017.
 */

public class OperationNumberClient {
    private static final String TAG = OperationNumberClient.class.getSimpleName();
    private String resource = "ventanueva/GetOperationNumber";
    private Context context;
    private Callback callBack;

    public OperationNumberClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void obtener(int idVisita){
        final String url = context.getResources().getString(R.string.url)
                +resource+"?idvisita="+idVisita;
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
