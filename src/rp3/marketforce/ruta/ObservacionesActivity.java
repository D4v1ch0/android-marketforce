package rp3.marketforce.ruta;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class ObservacionesActivity extends BaseActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
	            WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    LayoutParams params = getWindow().getAttributes(); 
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.MATCH_PARENT;
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		super.onCreate(savedInstanceState);
	    setContentView(rp3.core.R.layout.layout_simple_content, R.menu.fragment_crear_cliente);
	    setTitle("Crear Agenda");
	    long id = 0;
	    id = getIntent().getLongExtra(RutasDetailFragment.ARG_ITEM_ID, 0);
	    setTitle("Observaciones");
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	ObservacionesFragment newFragment = ObservacionesFragment.newInstance(id);
            setFragment(rp3.core.R.id.content, newFragment);    
        }
	}

}
