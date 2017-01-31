package rp3.auna.oportunidad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import rp3.app.BaseActivity;
import rp3.auna.R;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class FiltroOportunidadActivity extends BaseActivity implements FiltroOportunidadFragment.OportunidadFiltroListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        setTitle("Filtro");
        if (!hasFragment(rp3.core.R.id.content)) {
            FiltroOportunidadFragment newFragment = FiltroOportunidadFragment.newInstance();
            newFragment.filtroData = getIntent();
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    public void onFiltroSend(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onFiltroClean() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
