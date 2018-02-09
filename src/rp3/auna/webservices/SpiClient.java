package rp3.auna.webservices;

/**
 * Created by Jesus Villa on 16/10/2017.
 */

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rp3.auna.R;
import rp3.auna.bean.Informacion;

/***
 * Client WS SPI
 */
public class SpiClient {
    private static final String TAG = SpiClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "spi/service";
    private Context context;
    private Callback callBack;

    public SpiClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void validar(Informacion informacion){
        final String url = context.getResources().getString(R.string.url)+resource;
        final String json = new Gson().toJson(informacion);
        Log.d(TAG,url);
        Log.d(TAG,json);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JPEG,json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("content-type", "application/json")
                .post(requestBody).build();
        new OkHttpClient().newCall(request).enqueue(callBack);
    }
}
