package rp3.marketforce.marcaciones;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import rp3.app.BaseActivity;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Campo;
import rp3.marketforce.ruta.FotoFragment;
import rp3.marketforce.ruta.RutasDetailFragment;

/**
 * Created by magno_000 on 05/06/2015.
 */
public class MarcacionActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHomeAsUpEnabled(true, true);
        setTitle("Marcaciones");

        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            MarcacionFragment newFragment = MarcacionFragment.newInstance();
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
