package rp3.berlin.models.pedido;

import rp3.data.entity.EntityBase;
import rp3.berlin.db.Contract;

/**
 * Created by magno_000 on 10/04/2017.
 */

public class Serie extends EntityBase<Serie> {

    private long id;
    private String serie;
    private String grupoEstadistico;

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
        return Contract.Serie.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.Serie.COLUMN_ID_SERIE, this.serie);
        setValue(Contract.Serie.COLUMN_GRUPO_ESTADISTICO, this.grupoEstadistico);
    }

    @Override
    public Object getValue(String key) {
        return id;
    }

    @Override
    public String getDescription() {
        return grupoEstadistico;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getGrupoEstadistico() {
        return grupoEstadistico;
    }

    public void setGrupoEstadistico(String grupoEstadistico) {
        this.grupoEstadistico = grupoEstadistico;
    }
}