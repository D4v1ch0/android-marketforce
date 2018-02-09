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
 * Created by Jesus Villa on 13/12/2017.
 */

public class WalletClient {
    private static final String TAG = WalletClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "VentaNueva/GetCodAsWallet";
    private Context context;
    private Callback callBack;

    public WalletClient(Context context, Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void obtener(String correo){
        final String url = context.getResources().getString(R.string.url)+resource+"?correo="+correo;
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
