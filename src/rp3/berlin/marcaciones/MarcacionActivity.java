package rp3.berlin.marcaciones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.berlin.R;
import rp3.berlin.db.DbOpenHelper;
import rp3.runtime.Session;

/**
 * Created by magno_000 on 05/06/2015.
 */
public class MarcacionActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Session.Start(this);
        rp3.configuration.Configuration.TryInitializeConfiguration(this, DbOpenHelper.class);

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