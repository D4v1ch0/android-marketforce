package rp3.auna.actividades;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import rp3.auna.Contants;
import rp3.auna.R;

/**
 * Created by magno_000 on 09/04/2015.
 */
public class ActualizacionActivity extends ActividadActivity {

    private static final String TAG = ActualizacionActivity.class.getSimpleName();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setTitle("Editar Cliente");
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            Log.d(TAG,"!hasFragment(rp3.core.R.id.content...");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            ActualizacionFragment newFragment = ActualizacionFragment.newInstance(id_agenda_int, Contants.IS_GESTION, id_actividad, id_ruta);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    public void aceptarCambios(View v) {

    }

    /**
     *
     * Ciclo de vida
     *
     */

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}