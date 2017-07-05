package rp3.berlin.dashboard;

import android.os.Bundle;

import rp3.app.BaseActivity;
import rp3.berlin.R;
import rp3.berlin.cliente.EstadoCuentaFragment;

/**
 * Created by magno_000 on 27/06/2017.
 */

public class MetasActivity extends BaseActivity {

    private MetasFragment newFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id_cliente = 0;
        setTitle("Metas");

        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = new MetasFragment();
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
