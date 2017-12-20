package rp3.marketforce.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.resumen.AgenteDetalleFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.Utils;
import rp3.runtime.Session;

/**
 * Created by Gustavo Meregildo on 20/12/2017.
 */

public class UninstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Session.Start(context);
        rp3.configuration.Configuration.TryInitializeConfiguration(context);
        //int perc = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0);
        //NotificationPusher.pushNotification(1, context, "El usuario " + Session.getUser().getFullName() + " ha cambiado la hora de su dispositivo. Auto-Time: " + perc, "Cambio de hora");
        if(intent.getData().getSchemeSpecificPart() == context.getPackageName())
        {
            Utils.ErrorToFile("Context is ok - Se esta desinstalando Marketforce del dispositivo - " + Calendar.getInstance().getTime().toString());
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION);
            bundle.putInt(AgenteDetalleFragment.ARG_AGENTE, PreferenceManager.getInt(Contants.KEY_ID_SUPERVISOR, 0));
            bundle.putString(AgenteDetalleFragment.ARG_TITLE, "Desinstalación de App");
            bundle.putString(AgenteDetalleFragment.ARG_MESSAGE, "El usuario " + Session.getUser().getFullName() + " esta desinstalando su app Marketforce.");
            rp3.sync.SyncUtils.requestSync(bundle);
        }
    }
}