package rp3.marketforce.cliente;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.marketforce.R.layout;
import rp3.marketforce.ruta.CrearVisitaFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CrearClienteActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setTitle("Crear Cliente");
	    setContentView(R.layout.layout_simple_content);
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	CrearClienteFragment newFragment = CrearClienteFragment.newInstance();
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

}
