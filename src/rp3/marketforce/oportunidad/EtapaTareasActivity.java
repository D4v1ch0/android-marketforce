package rp3.marketforce.oportunidad;

import android.os.Bundle;

import rp3.app.BaseActivity;
import rp3.marketforce.R;

/**
 * Created by magno_000 on 18/05/2015.
 */
public class EtapaTareasActivity extends BaseActivity {

    public final static String ARG_ETAPA = "etapa";
    public final static String ARG_OPORTUNIDAD = "oportunidad";

    private int idEtapa;
    private long idOportunidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        setTitle("Tareas");
        if(getIntent().getExtras().containsKey(ARG_ETAPA))
            idEtapa = getIntent().getExtras().getInt(ARG_ETAPA);
        if(getIntent().getExtras().containsKey(ARG_OPORTUNIDAD))
            idOportunidad = getIntent().getExtras().getLong(ARG_OPORTUNIDAD);
        if (!hasFragment(rp3.core.R.id.content)) {
            EtapaTareasFragment newFragment = EtapaTareasFragment.newInstance(idEtapa, idOportunidad);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
