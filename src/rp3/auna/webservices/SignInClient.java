package rp3.auna.webservices;

import android.content.Context;
import android.os.Bundle;
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
 * Created by Jesus Villa on 21/10/2017.
 */

public class SignInClient {
    private static final String TAG = SignInClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_ENCODED= MediaType.parse("application/x-www-form-urlencoded");
    private String resource = "account/signin";
    private Context context;
    private Callback callBack;

    public SignInClient(Context context,Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void signin(String logonName, String pass){
        final String url = context.getResources().getString(R.string.url)+resource;
        Log.d(TAG,url);
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_ENCODED,"logonname="+logonName+"&password="+pass);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("clientid", "android-app-marketforce")
                .addHeader("clientsecret", "rp3-marketforce2014")
                .addHeader("authtypetoken", "rp3.marketforce")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .post(requestBody).build();
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS);

        b.build().newCall(request).enqueue(callBack);
    }
}
