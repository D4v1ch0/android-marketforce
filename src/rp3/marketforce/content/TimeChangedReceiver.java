package rp3.marketforce.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.resumen.AgenteDetalleFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.runtime.Session;
import rp3.util.NotificationPusher;

/**
 * Created by magno_000 on 13/01/2016.
 */
public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Session.Start(context);
        rp3.configuration.Configuration.TryInitializeConfiguration(context);
        //NotificationPusher.pushNotification(1, context, "El usuario " + Session.getUser().getFullName() + " ha cambiado la hora de su dispositivo.", "Cambio de hora");
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION);
        bundle.putInt(AgenteDetalleFragment.ARG_AGENTE, PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR, 0));
        bundle.putString(AgenteDetalleFragment.ARG_TITLE, "Cambio de hora");
        bundle.putString(AgenteDetalleFragment.ARG_MESSAGE, "El usuario " + Session.getUser().getFullName() + " ha cambiado la hora de su dispositivo.");
        rp3.sync.SyncUtils.requestSync(bundle);
    }

}