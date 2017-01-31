package rp3.auna.pedido;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.auna.R;

/**
 * Created by magno_000 on 04/11/2015.
 */
public class CategoriaActivity extends BaseActivity {

    public static String ARG_IDCATEGORIA = "idcategoria";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        for(android.support.v4.app.Fragment fr: frags){
            fr.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void finishOnResult(String code)
    {
        Intent intent = new Intent();
        intent.putExtra(ProductoListFragment.ARG_PRODUCTO, code);
        setResult(RESULT_OK, intent);
        finish();
    }
}
