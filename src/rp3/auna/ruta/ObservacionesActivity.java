package rp3.auna.ruta;

import rp3.app.BaseActivity;
import rp3.auna.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.util.List;

public class ObservacionesActivity extends BaseActivity {
    private static final String TAG = ObservacionesActivity.class.getSimpleName();
	ObservacionesFragment newFragment;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
        Log.d(TAG,"onCreate...");
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
	            WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    LayoutParams params = getWindow().getAttributes(); 
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.MATCH_PARENT;
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		super.onCreate(savedInstanceState);
        boolean soloVista = !getIntent().getBooleanExtra(RutasDetailFragment.ARG_SOLO_VISTA, false);
        if(!getIntent().getBooleanExtra(RutasDetailFragment.ARG_SOLO_VISTA, false))
	        setContentView(rp3.core.R.layout.layout_simple_content, R.menu.fragment_crear_cliente);
        else
            setContentView(rp3.core.R.layout.layout_simple_content);

	    long id = 0;
	    id = getIntent().getLongExtra(RutasDetailFragment.ARG_ITEM_ID, 0);
	    setTitle("Observaciones");

	    if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = ObservacionesFragment.newInstance(id, soloVista);
            setFragment(rp3.core.R.id.content, newFragment);    
        }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
        Log.d(TAG,"onActivityResult...");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != 0 && resultCode != 0)
        {
            Log.d(TAG,"requestCode != 0 && resultCode != 0...");
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
