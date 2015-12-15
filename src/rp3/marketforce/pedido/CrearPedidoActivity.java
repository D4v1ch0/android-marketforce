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
 * Created by magno_000 on 13/10/2015.
 */
public class CrearPedidoActivity extends BaseActivity {

    public static String ARG_TIPO_DOCUMENTO = "tipo_documento";
    public static String ARG_IDPEDIDO = "idcliente";
    public static String ARG_IDAGENDA = "idagenda";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id_pedido = 0;
        long id_agenda = 0;
        String tipo = "FA";
        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(ARG_IDPEDIDO))
        {
            id_pedido = getIntent().getExtras().getLong(ARG_IDPEDIDO);
            setTitle("Editar Pedido");
        }
        else
            setTitle("Crear Pedido");

        if(getIntent().getExtras() != null) {
            id_agenda = getIntent().getExtras().getLong(ARG_IDAGENDA, 0);
            tipo = getIntent().getExtras().getString(ARG_TIPO_DOCUMENTO, "FA");
        }

        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            CrearPedidoFragment newFragment = CrearPedidoFragment.newInstance(id_pedido, id_agenda, tipo);
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
