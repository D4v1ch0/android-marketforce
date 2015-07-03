package rp3.marketforce.content;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import rp3.marketforce.R;
import rp3.util.NotificationPusher;

/**
 * Created by magno_000 on 15/06/2015.
 */
public class GCMReceiver extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("Message");
        String title = data.getString("Title");
        if(TextUtils.isEmpty(title))
        {
            title = getApplicationContext().getString(R.string.app_name);
        }
        Log.d("Marketforce", "From: " + from);
        Log.d("Marketforce", "Message: " + message);

        if(!TextUtils.isEmpty(message))
            NotificationPusher.pushNotification(1, getApplicationContext(), message, title);
    }


}
