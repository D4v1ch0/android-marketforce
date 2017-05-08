package rp3.marketforce.cliente;

import android.os.Bundle;

import rp3.app.BaseActivity;
import rp3.marketforce.R;

/**
 * Created by magno_000 on 08/05/2017.
 */

public class ComprasClienteActivity  extends BaseActivity {
    public static String ARG_ID_CLIENTE = "id_cliente";
    private ComprasClienteFragment newFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id_cliente = 0;
        setTitle("Compras de Cliente");

        if(getIntent().getExtras() != null) {
            id_cliente = getIntent().getExtras().getLong(ARG_ID_CLIENTE, 0);
        }

        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = ComprasClienteFragment.newInstance(id_cliente);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
