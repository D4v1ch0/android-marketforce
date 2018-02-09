package rp3.auna.ventanueva;

import android.os.Bundle;
import android.util.Log;

import rp3.app.BaseActivity;
import rp3.auna.R;

public class CrearLlamadaActivity extends BaseActivity {

	private static final String TAG = CrearLlamadaActivity.class.getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
	    long id = 0;
	    String text = "agenda";
		long idCliente = 0;
	    if(getIntent().getExtras() != null)
	    {
	    	if(getIntent().getExtras().containsKey(CrearLlamadaFragment.ARG_IDAGENDA))
	    		id = getIntent().getExtras().getInt(CrearLlamadaFragment.ARG_IDAGENDA);
	    	if(getIntent().getExtras().containsKey(CrearLlamadaFragment.ARG_FROM))
	    		text = getIntent().getExtras().getString(CrearLlamadaFragment.ARG_FROM);
			if(getIntent().getExtras().containsKey(CrearLlamadaFragment.ARG_CLIENTE))
				idCliente = getIntent().getExtras().getLong(CrearLlamadaFragment.ARG_CLIENTE);
				Log.d(TAG,"idCliente:"+idCliente);
	    }
	    	
	    setContentView(rp3.core.R.layout.layout_simple_content, R.menu.fragment_crear_cliente);
	    setTitle("Crear Llamada");
	    setHomeAsUpEnabled(true, true);
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	CrearLlamadaFragment newFragment = CrearLlamadaFragment.newInstance(id, text, idCliente);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		//super.onDestroy();
	}
}
