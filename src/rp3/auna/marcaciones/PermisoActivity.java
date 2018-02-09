package rp3.auna.marcaciones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.auna.R;
import rp3.auna.db.DbOpenHelper;
import rp3.runtime.Session;

/**
 * Created by magno_000 on 10/07/2015.
 */
public class PermisoActivity extends BaseActivity{

    private static final String TAG = PermisoActivity.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        Session.Start(this);
        rp3.configuration.Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);
        setHomeAsUpEnabled(true, true);
        setTitle("Aprobar Permisos");

        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            PermisoFragment newFragment = PermisoFragment.newInstance();
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.d(TAG,".onActivityResult..");
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
