package rp3.auna.oportunidad;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.auna.R;

/**
 * Created by magno_000 on 19/05/2015.
 */
public class CrearOportunidadActivity extends BaseActivity  {

    private static final String TAG = CrearOportunidadActivity.class.getSimpleName();
    public static final String ARG_ID = "id";

    CrearOportunidadFragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        long id = 0;
        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(ARG_ID))
            id = getIntent().getExtras().getLong(ARG_ID);
        if(id == 0)
            setTitle("Crear Oportunidad");
        else
            setTitle("Editar Oportunidad");
        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = CrearOportunidadFragment.newInstance(id);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        for(android.support.v4.app.Fragment fr: frags){
            fr.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        newFragment.SaveData();
        Log.d(TAG,"onConfigurationChanged...");
        super.onConfigurationChanged(newConfig);
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
