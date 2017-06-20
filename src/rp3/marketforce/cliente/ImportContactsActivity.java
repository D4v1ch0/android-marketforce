package rp3.marketforce.cliente;

import android.os.Bundle;
import android.util.Log;

import rp3.app.BaseActivity;

public class ImportContactsActivity extends BaseActivity {
	private static final String TAG = ImportContactsActivity.class.getSimpleName();
	ImportContactsFragment newFragment;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
		setHomeAsUpEnabled(true, true);
	    setContentView(rp3.core.R.layout.layout_simple_content);
	    setTitle("Importar Contactos");
	    int id = 0;
	    id = getIntent().getIntExtra(ImportContactsFragment.ARG_ID_ORIGEN, 0);
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	newFragment = ImportContactsFragment.newInstance(id);
            setFragment(rp3.core.R.id.content, newFragment);    
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
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}
}
