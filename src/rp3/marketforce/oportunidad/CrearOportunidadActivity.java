package rp3.marketforce.oportunidad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.marketforce.R;

/**
 * Created by magno_000 on 19/05/2015.
 */
public class CrearOportunidadActivity extends BaseActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        setTitle("Crear Oportunidad");
        if (!hasFragment(rp3.core.R.id.content)) {
            CrearOportunidadFragment newFragment = CrearOportunidadFragment.newInstance();
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
}
