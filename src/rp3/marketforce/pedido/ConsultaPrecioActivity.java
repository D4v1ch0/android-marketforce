package rp3.marketforce.pedido;

import android.os.Bundle;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.marketforce.cliente.EstadoCuentaFragment;

/**
 * Created by magno_000 on 08/05/2017.
 */

public class ConsultaPrecioActivity   extends BaseActivity {
    private ConsultaPrecioFragment newFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Consulta de Precios");

        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = new ConsultaPrecioFragment();
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
