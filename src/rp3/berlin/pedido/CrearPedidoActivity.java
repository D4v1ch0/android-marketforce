package rp3.berlin.pedido;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import java.util.List;

import rp3.app.BaseActivity;
import rp3.berlin.R;

/**
 * Created by magno_000 on 13/10/2015.
 */
public class CrearPedidoActivity extends BaseActivity {

    public static String ARG_TIPO_DOCUMENTO = "tipo_documento";
    public static String ARG_IDPEDIDO = "idcliente";
    public static String ARG_CLIENTE = "cliente";
    public static String ARG_DIRECCION = "direccion";
    public static String ARG_TIPO_ORDEN = "tipo_orden";
    public static String ARG_SERIE = "serie";
    public static String ARG_IDAGENDA = "idagenda";
    public static String ARG_CIUDAD = "ciudad";
    private CrearPedidoFragment newFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id_pedido = 0;
        long id_agenda = 0;
        long id_cliente = 0;
        int id_direccion = 0;
        String tipo = "FA";
        String serie = "";
        String tipoorden = "";
        String ciudad = "";
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
            id_cliente = getIntent().getExtras().getLong(ARG_CLIENTE, 0);
            serie = getIntent().getExtras().getString(ARG_SERIE, "");
            tipoorden = getIntent().getExtras().getString(ARG_TIPO_ORDEN, "");
            id_direccion = getIntent().getExtras().getInt(ARG_DIRECCION, 0);
            ciudad = getIntent().getExtras().getString(ARG_CIUDAD, "");
        }

        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = CrearPedidoFragment.newInstance(id_pedido, id_agenda, tipo, id_cliente, serie, tipoorden, id_direccion, ciudad);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                showDialogConfirmation(CrearPedidoFragment.DIALOG_SAVE_CANCEL, R.string.message_guardar_pedido, R.string.title_abandonar_transaccion, true);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        showDialogConfirmation(CrearPedidoFragment.DIALOG_SAVE_CANCEL, R.string.message_guardar_pedido, R.string.title_abandonar_transaccion, true);
        //super.onBackPressed();
    }

    @Override
    public void onPositiveConfirmation(int id) {
        switch (id)
        {
            case CrearPedidoFragment.DIALOG_SAVE_CANCEL:
                newFragment.onPositiveConfirmation(id);
                break;
            default:
                break;
        }
        super.onPositiveConfirmation(id);
    }

    @Override
    public void onNegativeConfirmation(int id) {
        switch (id)
        {
            default:
                break;
        }
        super.onPositiveConfirmation(id);

    }
}
