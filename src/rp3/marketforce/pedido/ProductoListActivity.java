package rp3.marketforce.pedido;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.CrearClienteFragment;

/**
 * Created by magno_000 on 20/10/2015.
 */
public class ProductoListActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Búsqueda de Productos");
        setHomeAsUpEnabled(true, true);

        int idCategoria = -1;
        if(getIntent().getExtras() != null)
            idCategoria = getIntent().getExtras().getInt(CategoriaFragment.ARG_IDCATEGORIA, -1);


        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            ProductoListFragment newFragment = ProductoListFragment.newInstance(idCategoria);
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