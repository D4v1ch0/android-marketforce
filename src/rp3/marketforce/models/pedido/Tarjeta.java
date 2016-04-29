package rp3.marketforce.models.pedido;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 22/04/2016.
 */
public class Tarjeta extends EntityBase<Tarjeta> {

    private long id;
    private int idMarcaTarjeta;
    private int idBanco;
    private int interna;

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    @Override
    public boolean isAutoGeneratedId() {
        return true;
    }

    @Override
    public String getTableName() {
        return Contract.Tarjeta.TABLE_NAME;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public int getInterna() {
        return interna;
    }

    public void setInterna(int interna) {
        this.interna = interna;
    }

    public int getIdMarcaTarjeta() {
        return idMarcaTarjeta;
    }

    public void setIdMarcaTarjeta(int idMarcaTarjeta) {
        this.idMarcaTarjeta = idMarcaTarjeta;
    }

    @Override
    public void setValues() {
        setValue(Contract.Tarjeta.COLUMN_ID_BANCO, this.idBanco);
        setValue(Contract.Tarjeta.COLUMN_INTERNA, this.interna);
        setValue(Contract.Tarjeta.COLUMN_ID_MARCA_TARJETA, this.idMarcaTarjeta);
    }

    @Override
    public Object getValue(String key) {
        return idBanco;
    }

    @Override
    public String getDescription() {
        return null;
    }
}