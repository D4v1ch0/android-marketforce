package rp3.marketforce.loader;

import android.content.Context;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.Producto;

/**
 * Created by magno_000 on 20/10/2015.
 */
public class ProductoLoader  extends
        rp3.content.SimpleObjectLoader<List<Producto>> {

    private DataBase db;
    private String search, serie;
    private int idSubCategoria;
    private String tipo;

    public ProductoLoader(Context context, DataBase db, String search, int idSubCategoria, String tipo, String serie) {
        super(context);
        this.db = db;
        this.search = search;
        this.idSubCategoria = idSubCategoria;
        this.tipo = tipo;
        this.serie = serie;
    }

    @Override
    public List<Producto> loadInBackground() {
        List<Producto> result = null;

        if(tipo.equalsIgnoreCase("sku") && search != null)
        {
            result = Producto.getProductoByCodigoExterno(db, search, serie);
        }
        else {
            if (search == null || search.length() <= 0)
                if (idSubCategoria == -1)
                    result = Producto.getProductos(db, serie);
                else
                    result = Producto.getProductos(db, idSubCategoria, serie);
            else if (idSubCategoria == -1)
                result = Producto.getProductoSearch(db, search, serie);
            else
                result = Producto.getProductoSearch(db, search, idSubCategoria, serie);
        }

        return result;
    }
}