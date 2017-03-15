package rp3.marketforce.oportunidad;

import android.os.Bundle;

import rp3.app.BaseActivity;
import rp3.marketforce.R;

/**
 * Created by magno_000 on 01/06/2015.
 */
public class EtapaActivity extends BaseActivity {
    public final static String ARG_ETAPA = "etapa";
    public final static String ARG_OPORTUNIDAD = "oportunidad";
    public final static String ARG_ID_AGENDA = "id_agenda";

    private int idEtapa;
    private long idOportunidad;
    private int idAgenda;

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
        if(getIntent().getExtras().containsKey(ARG_ID_AGENDA))
            idAgenda = getIntent().getExtras().getInt(ARG_ID_AGENDA);
        if (!hasFragment(rp3.core.R.id.content)) {
            EtapaFragment newFragment = EtapaFragment.newInstance(idEtapa, idOportunidad, idAgenda);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
