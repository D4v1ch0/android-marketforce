package rp3.berlin.oportunidad;

import android.os.Bundle;

import rp3.app.BaseActivity;

/**
 * Created by magno_000 on 25/05/2015.
 */
public class FotoOportunidadActivity extends BaseActivity {
    private FotoOportunidadFragment newFragment;

    public final static String ARG_TIPO = "tipo";
    public final static String ARG_ID = "id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        long id = 0;
        id = getIntent().getLongExtra(ARG_ID, 0);
        int tipo = getIntent().getIntExtra(ARG_TIPO, 1);
        setContentView(rp3.core.R.layout.layout_simple_content);

        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = FotoOportunidadFragment.newInstance(id, tipo);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
