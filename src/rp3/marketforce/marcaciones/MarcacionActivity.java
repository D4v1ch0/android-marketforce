package rp3.marketforce.marcaciones;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode != 0 && resultCode != 0)
        {
            List<Fragment> frags = getSupportFragmentManager().getFragments();
            for(android.support.v4.app.Fragment fr: frags) {
                fr.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}
