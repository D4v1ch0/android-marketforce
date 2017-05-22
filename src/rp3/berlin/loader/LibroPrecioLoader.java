package rp3.berlin.loader;

import android.content.Context;

import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.berlin.Contants;
import rp3.berlin.models.pedido.LibroPrecio;

/**
 * Created by magno_000 on 21/04/2017.
 */

public class LibroPrecioLoader  extends
        rp3.content.SimpleObjectLoader<List<LibroPrecio>> {

    private DataBase db;
    private String item, cliente, listaPrecio, linea, libro = "", tipo = "";

    public LibroPrecioLoader(Context context, DataBase db, String item, String cliente, String linea, String tipo, String libro) {
        super(context);
        this.db = db;
        this.item = item;
        this.cliente = cliente;
        this.linea = linea;
        this.tipo = tipo;
        this.libro = libro;
    }

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
        if(libro.equalsIgnoreCase(""))
            result = LibroPrecio.getPrecio(db, item, cliente, listaPrecio);
        else
        {
            if(libro.equalsIgnoreCase(Contants.LIBRO_CLIENTE))
                result = LibroPrecio.consultaPrecioCliente(db, item, cliente);
            if(libro.equalsIgnoreCase(Contants.LIBRO_REMATE))
                result = LibroPrecio.consultaPrecioGeneral(db, libro, item, linea);
            if(libro.equalsIgnoreCase(Contants.LIBRO_ESTANDAR))
                result = LibroPrecio.consultaPrecioGeneral(db, libro, item, linea);
        }
        return result;
    }
}