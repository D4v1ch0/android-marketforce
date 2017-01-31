package rp3.auna.loader;

import android.content.Context;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.auna.models.pedido.Producto;

/**
 * Created by magno_000 on 20/10/2015.
 */
public class ProductoLoader  extends
        rp3.content.SimpleObjectLoader<List<Producto>> {

    private DataBase db;
    private String search;
    private int idSubCategoria;
    private String tipo;

    public ProductoLoader(Context context, DataBase db, String search, int idSubCategoria, String tipo) {
        super(context);
        this.db = db;
        this.search = search;
        this.idSubCategoria = idSubCategoria;
        this.tipo = tipo;
    }

    @Override
    public List<Producto> loadInBackground() {
        List<Producto> result = null;

        if(tipo.equalsIgnoreCase("sku") && search != null)
        {
            result = Producto.getProductoByCodigoExterno(db, search);
        }
        else {
            if (search == null || search.length() <= 0)
                if (idSubCategoria == -1)
                    result = Producto.getProductos(db);
                else
                    result = Producto.getProductos(db, idSubCategoria);
            else if (idSubCategoria == -1)
                result = Producto.getProductoSearch(db, search);
            else
                result = Producto.getProductoSearch(db, search, idSubCategoria);
        }

        return result;
    }
}