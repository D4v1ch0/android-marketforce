package rp3.marketforce.oportunidad;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.marketforce.R;

/**
 * Created by magno_000 on 19/05/2015.
 */
public class CrearOportunidadActivity extends BaseActivity  {
    public static final String ARG_ID = "id";

    CrearOportunidadFragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        long id = 0;
        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(ARG_ID))
            id = getIntent().getExtras().getLong(ARG_ID);
        if(id == 0)
            setTitle("Crear Prospecto");
        else
            setTitle("Editar Prospecto");
        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = CrearOportunidadFragment.newInstance(id);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        for(android.support.v4.app.Fragment fr: frags){
            fr.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        newFragment.SaveData();
        super.onConfigurationChanged(newConfig);
    }
}
