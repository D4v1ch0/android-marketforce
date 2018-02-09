package rp3.auna.ventanueva;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rp3.app.BaseActivity;
import rp3.auna.R;
import rp3.auna.cliente.ClientDetailFragment;
import rp3.auna.models.Cliente;

public class DetailClienteActivity extends BaseActivity {

    private static final String TAG = DetailClienteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = 0;
        String text = "agenda";
        long idCliente = 0;
        if(getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().containsKey("IdCliente"))
                idCliente = getIntent().getExtras().getLong("IdCliente");
            Log.d(TAG,"idCliente:"+idCliente);
        }

        setContentView(rp3.core.R.layout.layout_simple_content, R.menu.fragment_crear_cliente);
        setTitle("Crear Llamada");
        setHomeAsUpEnabled(true, true);
        if (!hasFragment(rp3.core.R.id.content)) {
            Log.d(TAG,"!hasFragment...");
            for(Cliente cliente : Cliente.getClientAndContacts(getDataBase())){
                Log.d(TAG,"for:"+cliente.toString());
                if(idCliente == cliente.getIdCliente()){
                    Log.d(TAG,"break:"+cliente.toString());
                    ClientDetailFragment clientDetailFragment  = ClientDetailFragment.newInstance(cliente);
                    ///((MainActivity)getActivity()).selectedItem = 20;
                    setFragment(R.id.content_transaction_detail, clientDetailFragment );
                    //setFragment(rp3.core.R.id.content, newFragment);
                    break;
                }
            }

        }else{
            Log.d(TAG,"hasFragment...");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //super.onDestroy();
    }
}
