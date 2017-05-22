package rp3.berlin.cliente;

import android.os.Bundle;

import rp3.app.BaseActivity;
import rp3.berlin.R;

/**
 * Created by magno_000 on 03/05/2017.
 */

public class EstadoCuentaActivity extends BaseActivity {
    public static String ARG_ID_CLIENTE = "id_cliente";
    private EstadoCuentaFragment newFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id_cliente = 0;
        setTitle("Estado de Cuenta");

        if(getIntent().getExtras() != null) {
            id_cliente = getIntent().getExtras().getLong(ARG_ID_CLIENTE, 0);
        }

        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = EstadoCuentaFragment.newInstance(id_cliente);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
