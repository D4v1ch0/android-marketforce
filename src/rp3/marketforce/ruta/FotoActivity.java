package rp3.marketforce.ruta;

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
import rp3.util.Screen;

/**
 * Created by magno_000 on 14/04/2015.
 */
public class FotoActivity extends BaseActivity {
    private FotoFragment newFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        params.height = size.y - 100;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        params.y = params.y + 50;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onCreate(savedInstanceState);
        if(Campo.existsCampo(getDataBase(), Contants.IS_MODIFICACION, Contants.CAMPO_FOTO))
            setContentView(rp3.core.R.layout.layout_simple_content, R.menu.fragment_foto_menu);
        else
            setContentView(rp3.core.R.layout.layout_simple_content);
        //setTitle("Crear Agenda");
        long id = 0;
        id = getIntent().getLongExtra(RutasDetailFragment.ARG_ITEM_ID, 0);
        setTitle("Foto");

        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = FotoFragment.newInstance(id);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
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

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
