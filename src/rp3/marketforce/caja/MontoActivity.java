package rp3.marketforce.caja;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.marketforce.ruta.ObservacionesFragment;
import rp3.marketforce.ruta.RutasDetailFragment;

/**
 * Created by magno_000 on 24/04/2015.
 */
public class MontoActivity extends BaseActivity {
    MontoFragment newFragment;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onCreate(savedInstanceState);
        setContentView(rp3.core.R.layout.layout_simple_content, R.menu.fragment_crear_cliente);
        setTitle("Monto");

        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = MontoFragment.newInstance();
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
