package rp3.marketforce.cliente;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.R.layout;
import rp3.marketforce.ruta.CrearVisitaFragment;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    	for(android.support.v4.app.Fragment fr: frags){
            fr.onActivityResult(requestCode, resultCode, data);
        }
    	super.onActivityResult(requestCode, resultCode, data);
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//newFragment.rotated = true;
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
