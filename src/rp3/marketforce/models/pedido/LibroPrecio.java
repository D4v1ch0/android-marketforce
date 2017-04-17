package rp3.marketforce.models.pedido;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 06/04/2017.
 */

public class LibroPrecio extends EntityBase<LibroPrecio> {

    private long id;
    private String idLibro;
    private String item;
    private String divisa;
    private double precio;
    private String medida;
    private Date fechaEfectiva;
    private Date fechaVencimiento;
    private double valorEscalado;
    private int tipoEscalado;
    private String libroEstandar;


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
        return Contract.LibroPrecio.TABLE_NAME;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMedida() {
        return medida;
    }

    public void setMedida(String medida) {
        this.medida = medida;
    }

    public Date getFechaEfectiva() {
        return fechaEfectiva;
    }

    public void setFechaEfectiva(Date fechaEfectiva) {
        this.fechaEfectiva = fechaEfectiva;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getValorEscalado() {
        return valorEscalado;
    }

    public void setValorEscalado(double valorEscalado) {
        this.valorEscalado = valorEscalado;
    }

    public int getTipoEscalado() {
        return tipoEscalado;
    }

    public void setTipoEscalado(int tipoEscalado) {
        this.tipoEscalado = tipoEscalado;
    }

    public String getLibroEstandar() {
        return libroEstandar;
    }

    public void setLibroEstandar(String libroEstandar) {
        this.libroEstandar = libroEstandar;
    }

    @Override
    public void setValues() {
        setValue(Contract.LibroPrecio.COLUMN_FECHA_EFECTIVA, this.fechaEfectiva);
        setValue(Contract.LibroPrecio.COLUMN_FECHA_VENCIMIENTO, this.fechaVencimiento);
        setValue(Contract.LibroPrecio.COLUMN_DIVISA, this.divisa);
        setValue(Contract.LibroPrecio.COLUMN_ID_LIBRO, this.idLibro);
        setValue(Contract.LibroPrecio.COLUMN_ITEM, this.item);
        setValue(Contract.LibroPrecio.COLUMN_MEDIDA, this.medida);
        setValue(Contract.LibroPrecio.COLUMN_PRECIO, this.precio);
        setValue(Contract.LibroPrecio.COLUMN_VALOR_ESCALADO, this.valorEscalado);
        setValue(Contract.LibroPrecio.COLUMN_TIPO_ESCALADO, this.tipoEscalado);
        setValue(Contract.LibroPrecio.COLUMN_LIBRO_ESTANDAR, this.libroEstandar);
    }

    @Override
    public Object getValue(String key) {
        return id;
    }

    @Override
    public String getDescription() {
        return item;
    }

    public static LibroPrecio getPrecio(DataBase db, String item, String cliente, String listaPrecio) {
        String query = QueryDir.getQuery(Contract.LibroPrecio.QUERY_LIBRO_PRECIO);
        Calendar cal = Calendar.getInstance();

        Cursor c = db.rawQuery(query, new String[]{item, cliente, listaPrecio, cal.getTimeInMillis() + "", cal.getTimeInMillis() + ""} );

        LibroPrecio precio = new LibroPrecio();
        while(c.moveToNext()){
            precio.setItem(CursorUtils.getString(c, Contract.LibroPrecio.COLUMN_ITEM));
            precio.setPrecio(CursorUtils.getDouble(c, Contract.LibroPrecio.COLUMN_PRECIO));
        }
        c.close();

        if(precio.getItem() == null)
        {
            query = QueryDir.getQuery(Contract.LibroPrecio.QUERY_LIBRO_PRECIO_ESTANDAR);

            Cursor d = db.rawQuery(query, new String[]{item} );

            while(d.moveToNext()){
                precio.setItem(CursorUtils.getString(d, Contract.LibroPrecio.COLUMN_ITEM));
                precio.setPrecio(CursorUtils.getDouble(d, Contract.LibroPrecio.COLUMN_PRECIO));
            }
            d.close();
        }
        return precio;
    }
}