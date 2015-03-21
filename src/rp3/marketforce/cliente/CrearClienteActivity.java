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

	public static String ARG_IDCLIENTE = "idcliente";
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    long id_cliente = 0;
	    if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(ARG_IDCLIENTE))
	    {
	    	id_cliente = getIntent().getExtras().getLong(ARG_IDCLIENTE);
            setTitle("Editar Cliente");
	    }
        else
        {
            setTitle("Crear Cliente");
        }

        setHomeAsUpEnabled(true, true);
	    setContentView(R.layout.layout_simple_content);
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	CrearClienteFragment newFragment = CrearClienteFragment.newInstance(id_cliente);
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
