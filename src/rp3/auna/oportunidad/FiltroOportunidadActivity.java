package rp3.auna.oportunidad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import rp3.app.BaseActivity;
import rp3.auna.R;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class FiltroOportunidadActivity extends BaseActivity implements FiltroOportunidadFragment.OportunidadFiltroListener {

    private static final String TAG = FiltroOportunidadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        setTitle("Filtro");
        if (!hasFragment(rp3.core.R.id.content)) {
            FiltroOportunidadFragment newFragment = FiltroOportunidadFragment.newInstance();
            newFragment.filtroData = getIntent();
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    public void onFiltroSend(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFiltroClean() {
        setResult(Activity.RESULT_CANCELED);
        finish();
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
