package rp3.marketforce.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class Agente extends EntityBase<Agente> {

    private long id;
    private int idAgente;
    private String nombre;
    private String telefono;
    private String email;

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean isAutoGeneratedId() {
        return true;
    }

    @Override
    public String getTableName() {
        return Contract.Agente.TABLE_NAME;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setValues() {
        setValue(Contract.Agente.COLUMN_ID_AGENTE, this.idAgente);
        setValue(Contract.Agente.COLUMN_NOMBRE, this.nombre);
        setValue(Contract.Agente.COLUMN_TELEFONO, this.telefono);
        setValue(Contract.Agente.COLUMN_EMAIL, this.email);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static List<Agente> getAgentes(DataBase db) {
        Cursor c = db.query(Contract.Agente.TABLE_NAME, new String[] {Contract.Agente._ID, Contract.Agente.COLUMN_ID_AGENTE,
                Contract.Agente.COLUMN_NOMBRE},null, null, null,null, Contract.Agente.COLUMN_NOMBRE);

        List<Agente> list = new ArrayList<Agente>();
        while(c.moveToNext()){
            Agente agente = new Agente();
            agente.setID(CursorUtils.getInt(c, Contract.Agente._ID));
            agente.setIdAgente(CursorUtils.getInt(c, Contract.Agente.COLUMN_ID_AGENTE));
            agente.setNombre(CursorUtils.getString(c, Contract.Agente.COLUMN_NOMBRE));
            list.add(agente);
        }
        c.close();
        return list;
    }

    public static Agente getAgente(DataBase db, int idAgente) {
        Cursor c = db.query(Contract.Agente.TABLE_NAME, new String[] {Contract.Agente._ID, Contract.Agente.COLUMN_ID_AGENTE,
                Contract.Agente.COLUMN_NOMBRE, Contract.Agente.COLUMN_TELEFONO, Contract.Agente.COLUMN_EMAIL},
                Contract.Agente.COLUMN_ID_AGENTE + " = ?", new String[]{idAgente + ""});

        Agente agente = new Agente();
        while(c.moveToNext()){

            agente.setID(CursorUtils.getInt(c, Contract.Agente._ID));
            agente.setIdAgente(CursorUtils.getInt(c, Contract.Agente.COLUMN_ID_AGENTE));
            agente.setNombre(CursorUtils.getString(c, Contract.Agente.COLUMN_NOMBRE));
            agente.setTelefono(CursorUtils.getString(c, Contract.Agente.COLUMN_TELEFONO));
            agente.setEmail(CursorUtils.getString(c, Contract.Agente.COLUMN_EMAIL));
        }
        c.close();
        return agente;
    }
}