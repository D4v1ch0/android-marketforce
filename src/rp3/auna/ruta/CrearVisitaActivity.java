package rp3.auna.ruta;

import rp3.app.BaseActivity;
import rp3.auna.R;

import android.os.Bundle;
import android.util.Log;

public class CrearVisitaActivity extends BaseActivity {

	private static final String TAG = CrearVisitaActivity.class.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
	    long id = 0;
	    String text = "agenda";
	    if(getIntent().getExtras() != null)
	    {
	    	if(getIntent().getExtras().containsKey(CrearVisitaFragment.ARG_IDAGENDA))
	    		id = getIntent().getExtras().getInt(CrearVisitaFragment.ARG_IDAGENDA);
	    	if(getIntent().getExtras().containsKey(CrearVisitaFragment.ARG_FROM))
	    		text = getIntent().getExtras().getString(CrearVisitaFragment.ARG_FROM);
	    }
	    	
	    setContentView(rp3.core.R.layout.layout_simple_content, R.menu.fragment_crear_cliente);
	    setTitle("Crear VisitaVta");
	    setHomeAsUpEnabled(true, true);
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	CrearVisitaFragment newFragment = CrearVisitaFragment.newInstance(id, text);
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
