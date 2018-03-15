package rp3.auna.util.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.content.SyncAdapter;
import rp3.data.MessageCollection;
import rp3.sync.SyncUtils;
import rp3.util.ConnectionUtils;

/**
 * Created by Jesus Villa on 10/10/2017.
 */

public class UtilSync {
    private static final String TAG = UtilSync.class.getSimpleName();
    private Context context;
    public CallbackSync callbackSync;
    public UtilSync(Context context,CallbackSync callbackSync){
        this.context = context;
        this.callbackSync = callbackSync;
    }

    private BroadcastReceiver syncFinishedReceiverr = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //resetRotation();
            Log.d(TAG,"syncFinishedReceiver...");
            MessageCollection messages = (MessageCollection) intent.getExtras()
                    .getParcelable(SyncAdapter.NOTIFY_EXTRA_MESSAGES);
            Bundle bundle = intent.getExtras().getBundle(
                    SyncAdapter.NOTIFY_EXTRA_DATA);
            callbackSync.onSyncComplete(bundle, messages);

        }
    };

    public interface CallbackSync{
        void onSyncComplete(Bundle bundle,MessageCollection messageCollection);
    }
    public  void requestSyncMain(Bundle settingsBundle) {
        if (ConnectionUtils.isNetAvailable(context)) {
            //PreferenceManager.close();
            SyncUtils.requestSync(settingsBundle);
            //lockRotation();
        } else {
            MessageCollection mc = new MessageCollection();
            mc.addErrorMessage(context.getResources().getString(
                    rp3.core.R.string.message_error_sync_no_net_available).toString());
            callbackSync.onSyncComplete(new Bundle(), mc);
        }
    }
}
