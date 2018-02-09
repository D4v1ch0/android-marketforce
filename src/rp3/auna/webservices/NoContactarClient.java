package rp3.auna.webservices;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import rp3.auna.R;
import rp3.auna.bean.ProspectoVta;
import rp3.auna.bean.RegistroPago;
import rp3.runtime.Session;

/**
 * Created by Jesus Villa on 04/01/2018.
 */

public class NoContactarClient {
    private static final String TAG = NoContactarClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("application/json");
    private String resource = "prospectovta/robinson/";
    private Context context;
    private Callback callBack;

    public NoContactarClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void registrar(List<ProspectoVta> movil){
        final String url = context.getResources().getString(R.string.url)+resource;
        final String json = new Gson().toJson(movil);
        Log.d(TAG,url);
        Log.d(TAG,json);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JPEG,json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader(context.getResources().getString(R.string.cliend_id),context.getResources().getString(R.string.cliend_id_response))
                .addHeader(context.getResources().getString(R.string.client_secret),context.getResources().getString(R.string.client_secret_response))
                .addHeader(context.getResources().getString(R.string.auth_type_token),Session.getUser().getAuthTokenType())
                .addHeader(context.getResources().getString(R.string.auth_token), Session.getUser().getAuthToken())
                .addHeader("content-type", "application/json")
                .post(requestBody).build();
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS);

        b.build().newCall(request).enqueue(callBack);
    }
}
