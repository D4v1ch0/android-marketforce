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
	    setTitle("Crear Agenda");
	    setHomeAsUpEnabled(true, true);
	    if (!hasFragment(rp3.core.R.id.content)) {
	    	CrearVisitaFragment newFragment = CrearVisitaFragment.newInstance(id, text);
            setFragment(rp3.core.R.id.content, newFragment);    
        }
	}

}
