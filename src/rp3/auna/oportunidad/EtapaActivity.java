package rp3.auna.oportunidad;

import android.os.Bundle;
import android.util.Log;

import rp3.app.BaseActivity;
import rp3.auna.R;

/**
 * Created by magno_000 on 01/06/2015.
 */
public class EtapaActivity extends BaseActivity {

    private static final String TAG = EtapaActivity.class.getSimpleName();
    public final static String ARG_ETAPA = "etapa";
    public final static String ARG_OPORTUNIDAD = "oportunidad";

    private int idEtapa;
    private long idOportunidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
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

    /**
     *
     * Ciclo de vida
     *
     */

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
