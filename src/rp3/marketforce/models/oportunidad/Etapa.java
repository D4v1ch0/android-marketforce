package rp3.marketforce.models.oportunidad;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 14/05/2015.
 */
public class Etapa extends EntityBase<Etapa> {

    private long id;
    private int idEtapa;
    private int idEtapaPadre;
    private String descripcion;
    private int orden;
    private String estado;
    private List<Etapa> subEtapas;
    private int idOportunidadTipo;

    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    public int getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(int idEtapa) {
        this.idEtapa = idEtapa;
    }

    public int getIdEtapaPadre() {
        return idEtapaPadre;
    }

    public void setIdEtapaPadre(int idEtapaPadre) {
        this.idEtapaPadre = idEtapaPadre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Etapa> getSubEtapas() {
        return subEtapas;
    }

    public void setSubEtapas(List<Etapa> subEtapas) {
        this.subEtapas = subEtapas;
    }

    @Override
    public boolean isAutoGeneratedId() {
        return true;
    }

    @Override
    public String getTableName() {
        return Contract.Etapa.TABLE_NAME;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdOportunidadTipo() {
        return idOportunidadTipo;
    }

    public void setIdOportunidadTipo(int idOportunidadTipo) {
        this.idOportunidadTipo = idOportunidadTipo;
    }

    @Override
    public void setValues() {
        setValue(Contract.Etapa.COLUMN_ID_ETAPA, this.idEtapa);
        setValue(Contract.Etapa.COLUMN_ID_ETAPA_PADRE, this.idEtapaPadre);
        setValue(Contract.Etapa.COLUMN_DESCRIPCION, this.descripcion);
        setValue(Contract.Etapa.COLUMN_ESTADO, this.estado);
        setValue(Contract.Etapa.COLUMN_ORDEN, this.orden);
        setValue(Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO, this.idOportunidadTipo);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static List<Etapa> getEtapasAll(DataBase db, int tipo){

        Cursor c = db.query(Contract.Etapa.TABLE_NAME, new String[] {Contract.Etapa._ID, Contract.Etapa.COLUMN_ID_ETAPA, Contract.Etapa.COLUMN_ID_ETAPA_PADRE,
                Contract.Etapa.COLUMN_ESTADO, Contract.Etapa.COLUMN_ORDEN, Contract.Etapa.COLUMN_DESCRIPCION, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO},
                Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO + " = ?", new String[] {tipo + ""});

        List<Etapa> list = new ArrayList<Etapa>();
        while(c.moveToNext()){
            Etapa etp = new Etapa();
            etp.setID(CursorUtils.getInt(c, Contract.Etapa._ID));
            etp.setIdEtapa(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA));
            etp.setIdEtapaPadre(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA_PADRE));
            etp.setDescripcion(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setEstado(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setOrden(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ORDEN));
            etp.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO));
            list.add(etp);
        }
        c.close();
        return list;
    }

    public static List<Etapa> getEtapas(DataBase db){

        Cursor c = db.query(Contract.Etapa.TABLE_NAME, new String[] {Contract.Etapa._ID, Contract.Etapa.COLUMN_ID_ETAPA, Contract.Etapa.COLUMN_ID_ETAPA_PADRE,
                Contract.Etapa.COLUMN_ESTADO, Contract.Etapa.COLUMN_ORDEN, Contract.Etapa.COLUMN_DESCRIPCION, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO}, Contract.Etapa.COLUMN_ID_ETAPA_PADRE + " = ?", new String[]{"0"});

        List<Etapa> list = new ArrayList<Etapa>();
        while(c.moveToNext()){
            Etapa etp = new Etapa();
            etp.setID(CursorUtils.getInt(c, Contract.Etapa._ID));
            etp.setIdEtapa(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA));
            etp.setIdEtapaPadre(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA_PADRE));
            etp.setDescripcion(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setEstado(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setOrden(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ORDEN));
            etp.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO));
            list.add(etp);
        }
        c.close();
        return list;
    }

    public static List<Etapa> getSubEtapasQuery(DataBase db, int idEtapa){

        Cursor c = db.query(Contract.Etapa.TABLE_NAME, new String[] {Contract.Etapa._ID, Contract.Etapa.COLUMN_ID_ETAPA, Contract.Etapa.COLUMN_ID_ETAPA_PADRE,
                Contract.Etapa.COLUMN_ESTADO, Contract.Etapa.COLUMN_ORDEN, Contract.Etapa.COLUMN_DESCRIPCION, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO}, Contract.Etapa.COLUMN_ID_ETAPA_PADRE + " = ?", new String[]{idEtapa + ""});

        List<Etapa> list = new ArrayList<Etapa>();
        while(c.moveToNext()){
            Etapa etp = new Etapa();
            etp.setID(CursorUtils.getInt(c, Contract.Etapa._ID));
            etp.setIdEtapa(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA));
            etp.setIdEtapaPadre(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA_PADRE));
            etp.setDescripcion(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setEstado(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setOrden(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ORDEN));
            etp.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO));
            list.add(etp);
        }
        c.close();
        return list;
    }

    public static Etapa getEtapaById(DataBase db, int idEtapa) {
        Cursor c = db.query(Contract.Etapa.TABLE_NAME, new String[] {Contract.Etapa._ID, Contract.Etapa.COLUMN_ID_ETAPA, Contract.Etapa.COLUMN_ID_ETAPA_PADRE,
                Contract.Etapa.COLUMN_ESTADO, Contract.Etapa.COLUMN_ORDEN, Contract.Etapa.COLUMN_DESCRIPCION, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO}, Contract.Etapa.COLUMN_ID_ETAPA + " = ? ", new String[] {idEtapa + ""} );

        Etapa etp = new Etapa();
        while(c.moveToNext()){
            etp.setID(CursorUtils.getInt(c, Contract.Etapa._ID));
            etp.setIdEtapa(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA));
            etp.setIdEtapaPadre(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA_PADRE));
            etp.setDescripcion(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setEstado(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setOrden(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ORDEN));
            etp.setSubEtapas(getSubEtapasQuery(db, idEtapa));
            etp.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO));
        }
        c.close();
        return etp;
    }

    public static Etapa getEtapaNext(DataBase db, int orden, int tipo) {
        Cursor c = db.query(Contract.Etapa.TABLE_NAME, new String[] {Contract.Etapa._ID, Contract.Etapa.COLUMN_ID_ETAPA, Contract.Etapa.COLUMN_ID_ETAPA_PADRE,
                Contract.Etapa.COLUMN_ESTADO, Contract.Etapa.COLUMN_ORDEN, Contract.Etapa.COLUMN_DESCRIPCION, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO},  Contract.Etapa.COLUMN_ID_ETAPA_PADRE + " = 0 AND " +
                Contract.Etapa.COLUMN_ORDEN + " = ? AND " + Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO + " = ?", new String[] {orden + "", tipo + ""} );

        Etapa etp = new Etapa();
        while(c.moveToNext()){
            etp.setID(CursorUtils.getInt(c, Contract.Etapa._ID));
            etp.setIdEtapa(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA));
            etp.setIdEtapaPadre(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_ETAPA_PADRE));
            etp.setDescripcion(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setEstado(CursorUtils.getString(c, Contract.Etapa.COLUMN_DESCRIPCION));
            etp.setOrden(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ORDEN));
            etp.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Etapa.COLUMN_ID_OPORTUNIDAD_TIPO));
        }
        c.close();
        return etp;
    }


}
