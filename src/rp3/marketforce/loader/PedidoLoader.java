package rp3.marketforce.loader;

import android.content.Context;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.pedido.Pedido;

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

        if(search == null || search.length() <= 0)
            result = Pedido.getPedidos(db, search);
        else
            result = Pedido.getPedidos(db, search);


        return result;
    }

}