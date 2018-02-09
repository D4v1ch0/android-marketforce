package rp3.auna.pedido;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.auna.R;

/**
 * Created by magno_000 on 04/11/2015.
 */
public class CategoriaActivity extends BaseActivity {

    private static final String TAG = CategoriaActivity.class.getSimpleName();
    public static String ARG_IDCATEGORIA = "idcategoria";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        int idCategoria = 0;
        int tipo = 0;
        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(ARG_IDCATEGORIA))
        {
            idCategoria = getIntent().getExtras().getInt(ARG_IDCATEGORIA);
            setTitle("Sub Categorías");
        }
        else
            setTitle("Categorías");

        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            CategoriaFragment newFragment = CategoriaFragment.newInstance(idCategoria);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        for(android.support.v4.app.Fragment fr: frags){
            fr.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void finishOnResult(String code)
    {
        Log.d(TAG,"finishOnResult...");
        Intent intent = new Intent();
        intent.putExtra(ProductoListFragment.ARG_PRODUCTO, code);
        setResult(RESULT_OK, intent);
        finish();
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
