package rp3.auna;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Locale;

import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.AlarmJvs;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.helper.Parcelables;
import rp3.auna.utils.Utils;
import rp3.db.sqlite.DataBase;

public class AlarmAlertActivity extends AppCompatActivity {
    private static final String TAG = AlarmAlertActivity.class.getSimpleName();
    //private AlarmJvs alarm;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean alarmActive;
    private ClickListener clickListener;
    private Button button;
    private AlarmJvs alarmJvs = null;
    private TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm_alert);

        clickListener = new ClickListener();
        button = (Button) findViewById(R.id.button_deactivate);
        button.setOnClickListener(clickListener);

        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Log.d(TAG,"bundle != null...");
            alarmJvs = Parcelables.toParcelableAlarm(bundle.getByteArray(AlarmJvs.TAG));
            Log.d(TAG,alarmJvs.toString());
        }

        if (alarmJvs == null) {
            Log.d(TAG, "Alarm is null!");
            return;
        }

        this.setTitle(alarmJvs.getName());

        final TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);

        final CallStateListener callStateListener = new CallStateListener();

        telephonyManager.listen(callStateListener, CallStateListener.LISTEN_CALL_STATE);

        startAlarm();
    }

    private void startAlarm() {
        Log.d(TAG,"startAlarm...");
        if (alarmJvs != null && !alarmJvs.getTonePath().isEmpty()) {
            Log.d(TAG,"alarm != null && !alarm.getTonePath().isEmpty() is true...");
            Log.d(TAG, "startAlarm(): " + alarmJvs.getAlarmTimeStringParcelable());
            mediaPlayer = new MediaPlayer();

            if (alarmJvs.isShouldVibrate()) {
                Log.d(TAG,"alarm.isShouldVibrate()...");
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = {1000, 300, 300, 300};
                vibrator.vibrate(pattern, 0);
            }else{
                Log.d(TAG,"!alarm.isShouldVibrate()...");
            }

            try {
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(this, Uri.parse(alarmJvs.getTonePath()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mediaPlayer.release();
                alarmActive = false;
            }
        }else{
            Log.d(TAG,"alarm != null && !alarm.getTonePath().isEmpty() is false...");
        }
    }

    private void stopAlarm() {
        Log.d(TAG,"stopAlarm...");
        if (alarmJvs != null) {
            Log.d(TAG,"alarm != null..."+alarmJvs.toString());
            Log.d(TAG, "stop alarm");
            alarmJvs.setActive(false);


            try {
                AlarmJvs.delete(Utils.getDataBase(this),alarmJvs);
                vibrator.cancel();
            } catch (Exception e) {
                Log.d(TAG,"Exception cancel");
                e.printStackTrace();
            }
            try {
                mediaPlayer.stop();
            } catch (Exception e) {
                Log.d(TAG,"Exception stop");
                e.printStackTrace();
            }
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                Log.d(TAG,"Exception release");
                e.printStackTrace();
            }
        }else{
            Log.d(TAG,"alarm == null...");
        }
    }

    //region Clases

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!alarmActive || alarmJvs == null) {
                Log.d(TAG,"!alarmActive || alarm == null...");
                return;
            }
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            stopAlarm();
            finish();
        }
    }

    private class CallStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(getClass().getSimpleName(), "Incoming call: "
                            + incomingNumber);
                    try {
                        mediaPlayer.pause();
                    } catch (Exception e) {

                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(getClass().getSimpleName(), "Call State Idle");
                    try {
                        mediaPlayer.start();
                    } catch (Exception e) {

                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    //endregion

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG,"onNewIntent...");
        processIntent(intent);
    }

    private void processIntent(Intent intent){
        Log.d(TAG,"processIntent...");
        Log.d(TAG,intent.toString());
        setText(intent.getExtras());
    }

    private void setText(Bundle bundle){
        Log.d(TAG,"setText...");
        alarmJvs = Parcelables.toParcelableAlarm(bundle.getByteArray(AlarmJvs.TAG));
        processBundle();
    }

    //region Ciclo de vida

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
        processBundle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onBackPressed() {
        if (!alarmActive) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    //endregion

    private void processBundle(){
        Log.d(TAG,"processsBundle...");
        alarmActive = true;
        try{
            if(alarmJvs!=null){
                Log.d(TAG,"alarmJvs!=null...");
                Log.d(TAG,alarmJvs.toString());
                if(alarmJvs.getType()==1){
                    Log.d(TAG,"alarma es de tipo para visita...");
                    ApplicationParameter applicationParameter =
                            ApplicationParameter.getParameter(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class), Constants.PARAMETERID_ALERTAVISITA,
                                    Constants.LABEL_VISITAS);
                    final int value = Integer.parseInt(applicationParameter.getValue());
                    button.setText("Visita por iniciar en "+value+" minutos");

                    t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                Log.d(TAG,"status != TextToSpeech.ERROR...");
                                try {
                                    t1.setLanguage(new Locale("es", "ES"));
                                    t1.speak("Visita por iniciar en "+value+" minutos", TextToSpeech.QUEUE_FLUSH, null);
                                } catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }else{
                                Log.d(TAG,"status == TextToSpeech.ERROR...");
                            }
                        }
                    });

                }else if(alarmJvs.getType()==2){
                    Log.d(TAG,"alarma es de tipo para llamada...");
                    ApplicationParameter applicationParameter =
                            ApplicationParameter.getParameter(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class), Constants.PARAMETERID_ALERTALLAMADA,
                                    Constants.LABEL_LLAMADAS);
                    final int value = Integer.parseInt(applicationParameter.getValue());
                    button.setText("Llamada por iniciar en "+value+" minutos");

                    t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR) {
                                Log.d(TAG,"...");
                                try {
                                    t1.setLanguage(new Locale("es", "ES"));
                                    t1.speak("Llamada por iniciar en "+value+" minutos", TextToSpeech.QUEUE_FLUSH, null);
                                } catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }else{
                                Log.d(TAG,"status == TextToSpeech.ERROR...");
                            }
                        }
                    });
                }
            }else{
                Log.d(TAG,"AlarmJvs==null...");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
