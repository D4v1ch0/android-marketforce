package rp3.marketforce.loader;

import android.content.Context;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.pedido.LibroPrecio;

/**
 * Created by magno_000 on 21/04/2017.
 */

public class LibroPrecioLoader  extends
        rp3.content.SimpleObjectLoader<List<LibroPrecio>> {

    private DataBase db;
    private String item, cliente, listaPrecio;

    public LibroPrecioLoader(Context context, DataBase db, String item, String cliente, String listaPrecio) {
        super(context);
        this.db = db;
        this.item = item;
        this.cliente = cliente;
        this.listaPrecio = listaPrecio;
    }

    @Override
    public List<LibroPrecio> loadInBackground() {
        List<LibroPrecio> result = null;
        result = LibroPrecio.getPrecio(db, item, cliente, listaPrecio);
        return result;
    }
}