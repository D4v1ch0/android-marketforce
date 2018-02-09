package rp3.auna.cliente;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.auna.Contants;
import rp3.auna.R;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class CrearClienteActivity extends BaseActivity {

	private static final String TAG = CrearClienteActivity.class.getSimpleName();
	public static String ARG_IDCLIENTE = "idcliente";
	CrearClienteFragment newFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
	    long id_cliente = 0;
        int tipo = 0;
	    if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(ARG_IDCLIENTE))
	    {
	    	id_cliente = getIntent().getExtras().getLong(ARG_IDCLIENTE);
            setTitle("Editar Cliente");
            tipo = Contants.IS_MODIFICACION;
	    }
        else
        {
            setTitle("Crear Cliente");
            tipo = Contants.IS_CREACION;
        }

        setHomeAsUpEnabled(true, true);
	    setContentView(R.layout.layout_simple_content);
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	newFragment = CrearClienteFragment.newInstance(id_cliente, tipo);
            setFragment(rp3.core.R.id.content, newFragment);    
        } 	
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	List<android.support.v4.app.Fragment> frags = getSupportFragmentManager().getFragments();
		Log.d(TAG,"onActivityResult...");
    	for(android.support.v4.app.Fragment fr: frags){
            fr.onActivityResult(requestCode, resultCode, data);
        }
    	super.onActivityResult(requestCode, resultCode, data);
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG,"onConfigurationChanged...");
		//newFragment.rotated = true;
	}

	public void finishOnResult(long code)
	{
		Log.d(TAG,"finishOnResult...");
		Intent intent = new Intent();
		intent.putExtra(ARG_IDCLIENTE, code);
		setResult(RESULT_OK, intent);
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
