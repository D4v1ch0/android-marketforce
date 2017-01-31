package rp3.auna.loader;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.auna.models.pedido.Pedido;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoLoader extends
        rp3.content.SimpleObjectLoader<List<Pedido>> {

    private DataBase db;
    private String search;

    public PedidoLoader(Context context, DataBase db, String search) {
        super(context);
        this.db = db;
        this.search = search;
    }

    @Override
    public List<Pedido> loadInBackground() {
        List<Pedido> result = null;
        Log.e("CARGA PEDIDOS", "Antes de Consulta: " + Calendar.getInstance().getTime().toString());
        if(search == null || search.length() <= 0)
            result = Pedido.getPedidos(db);
        else
            result = Pedido.getPedidos(db, search);
        Log.e("CARGA PEDIDOS", "Despues de Consulta: " + Calendar.getInstance().getTime().toString());
        return result;
    }

}