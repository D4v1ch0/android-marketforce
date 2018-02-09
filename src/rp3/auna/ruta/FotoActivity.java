package rp3.auna.ruta;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.Campo;

/**
 * Created by magno_000 on 14/04/2015.
 */
public class FotoActivity extends BaseActivity {
    private static final String TAG = FotoActivity.class.getSimpleName();
    private FotoFragment newFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate...");
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        params.y = params.y + 50;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onCreate(savedInstanceState);
        if(Campo.existsCampo(getDataBase(), Contants.IS_MODIFICACION, Contants.CAMPO_FOTO))
            setContentView(rp3.core.R.layout.layout_simple_content, R.menu.fragment_foto_menu);
        else
            setContentView(rp3.core.R.layout.layout_simple_content);
        //setTitle("Crear Agenda");
        long id = 0;
        id = getIntent().getLongExtra(RutasDetailFragment.ARG_ITEM_ID, 0);
        setTitle("Foto");

        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = FotoFragment.newInstance(id);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult...");
        if(requestCode != 0 && resultCode != 0)
        {
            List<Fragment> frags = getSupportFragmentManager().getFragments();
            for(android.support.v4.app.Fragment fr: frags) {
                fr.onActivityResult(requestCode, resultCode, data);
            }
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
