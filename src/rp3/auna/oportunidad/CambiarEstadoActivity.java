package rp3.auna.oportunidad;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import rp3.app.BaseActivity;

/**
 * Created by magno_000 on 28/05/2015.
 */
public class CambiarEstadoActivity extends BaseActivity {

    private static final String TAG = CambiarEstadoActivity.class.getSimpleName();
    private CambiarEstadoFragment newFragment;

    public final static String ARG_ID = "id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate...");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        params.y = params.y + 50;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onCreate(savedInstanceState);
        long id = 0;
        id = getIntent().getLongExtra(ARG_ID, 0);
        setContentView(rp3.core.R.layout.layout_simple_content);

        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = CambiarEstadoFragment.newInstance(id);
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
