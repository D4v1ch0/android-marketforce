package rp3.auna.oportunidad;

import android.os.Bundle;
import android.util.Log;

import rp3.app.BaseActivity;

/**
 * Created by magno_000 on 25/05/2015.
 */
public class FotoOportunidadActivity extends BaseActivity {

    private static final String TAG = FotoOportunidadActivity.class.getSimpleName();
    private FotoOportunidadFragment newFragment;

    public final static String ARG_TIPO = "tipo";
    public final static String ARG_ID = "id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        getActionBar().hide();
        long id = 0;
        id = getIntent().getLongExtra(ARG_ID, 0);
        int tipo = getIntent().getIntExtra(ARG_TIPO, 1);
        setContentView(rp3.core.R.layout.layout_simple_content);

        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = FotoOportunidadFragment.newInstance(id, tipo);
            setFragment(rp3.core.R.id.content, newFragment);
        }
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
