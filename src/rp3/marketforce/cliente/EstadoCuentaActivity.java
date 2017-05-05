package rp3.marketforce.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.marketforce.pedido.CrearPedidoFragment;

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
