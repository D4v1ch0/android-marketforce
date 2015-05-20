package rp3.marketforce.oportunidad;

import android.os.Bundle;
import android.widget.LinearLayout;

import rp3.app.BaseActivity;
import rp3.marketforce.R;
import rp3.widget.RangeSeekBar;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class FiltroOportunidadActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        setTitle("Filtro");
        if (!hasFragment(rp3.core.R.id.content)) {
            FiltroOportunidadFragment newFragment = FiltroOportunidadFragment.newInstance();
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
