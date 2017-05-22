package rp3.berlin.cliente;

import android.os.Bundle;
import rp3.app.BaseActivity;

public class ImportContactsActivity extends BaseActivity {

	ImportContactsFragment newFragment;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
}
