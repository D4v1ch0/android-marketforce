package rp3.marketforce.ruta;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import android.app.Activity;
import android.os.Bundle;

public class CrearVisitaActivity extends BaseActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(rp3.core.R.layout.layout_simple_content);
	    setTitle("Crear Agenda");
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	CrearVisitaFragment newFragment = CrearVisitaFragment.newInstance();
            setFragment(rp3.core.R.id.content, newFragment);    
        }
	}

}
