package rp3.auna.oportunidad;

import android.os.Bundle;

import rp3.app.BaseActivity;
import rp3.auna.R;

/**
 * Created by magno_000 on 01/06/2015.
 */
public class EtapaActivity extends BaseActivity {
    public final static String ARG_ETAPA = "etapa";
    public final static String ARG_OPORTUNIDAD = "oportunidad";

    private int idEtapa;
    private long idOportunidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        setTitle("Etapas");
        if(getIntent().getExtras().containsKey(ARG_ETAPA))
            idEtapa = getIntent().getExtras().getInt(ARG_ETAPA);
        if(getIntent().getExtras().containsKey(ARG_OPORTUNIDAD))
            idOportunidad = getIntent().getExtras().getLong(ARG_OPORTUNIDAD);
        if (!hasFragment(rp3.core.R.id.content)) {
            EtapaFragment newFragment = EtapaFragment.newInstance(idEtapa, idOportunidad);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
