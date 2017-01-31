package rp3.auna.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.resumen.AgenteDetalleFragment;
import rp3.auna.sync.SyncAdapter;
import rp3.runtime.Session;

/**
 * Created by magno_000 on 13/01/2016.
 */
public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Session.Start(context);
        rp3.configuration.Configuration.TryInitializeConfiguration(context);
        //int perc = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0);
        //NotificationPusher.pushNotification(1, context, "El usuario " + Session.getUser().getFullName() + " ha cambiado la hora de su dispositivo. Auto-Time: " + perc, "Cambio de hora");
        if(android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 0 && PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR, 0) != 0) {
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION);
            bundle.putInt(AgenteDetalleFragment.ARG_AGENTE, PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR, 0));
            bundle.putString(AgenteDetalleFragment.ARG_TITLE, "Cambio de hora");
            bundle.putString(AgenteDetalleFragment.ARG_MESSAGE, "El usuario " + Session.getUser().getFullName() + " ha cambiado la hora de su dispositivo.");
            rp3.sync.SyncUtils.requestSync(bundle);
        }
    }

}