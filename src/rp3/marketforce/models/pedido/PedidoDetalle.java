package rp3.marketforce.models.pedido;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class PedidoDetalle extends EntityBase<PedidoDetalle> {

    private long id;
    private int idPedidoDetalle;
    private int idPedido;
    private int _idPedido;
    private int idProducto;
    private String descripcion;
    private double valorUnitario;
    private int cantidad;
    private double valorTotal;

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
        return Contract.PedidoDetalle.TABLE_NAME;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdPedidoDetalle() {
        return idPedidoDetalle;
    }

    public void setIdPedidoDetalle(int idPedidoDetalle) {
        this.idPedidoDetalle = idPedidoDetalle;
    }

    public int get_idPedido() {
        return _idPedido;
    }

    public void set_idPedido(int _idPedido) {
        this._idPedido = _idPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public void setValues() {
        setValue(Contract.PedidoDetalle.COLUMN_ID_PEDIDO, this.idPedido);
        setValue(Contract.PedidoDetalle.COLUMN_ID_PEDIDO_DETALLE, this.idPedidoDetalle);
        setValue(Contract.PedidoDetalle.COLUMN_ID_PEDIDO_INT, this._idPedido);
        setValue(Contract.PedidoDetalle.COLUMN_ID_PRODUCTO, this.idProducto);
        setValue(Contract.PedidoDetalle.COLUMN_DESCRIPCION, this.descripcion);
        setValue(Contract.PedidoDetalle.COLUMN_VALOR_UNITARIO, this.valorUnitario);
        setValue(Contract.PedidoDetalle.COLUMN_CANTIDAD, this.cantidad);
        setValue(Contract.PedidoDetalle.COLUMN_VALOR_TOTAL, this.valorTotal);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static List<PedidoDetalle> getPedidoDetalles(DataBase db, int idPedido) {
        Cursor c = db.query(Contract.PedidoDetalle.TABLE_NAME, new String[] {Contract.PedidoDetalle._ID, Contract.PedidoDetalle.COLUMN_ID_PEDIDO, Contract.PedidoDetalle.COLUMN_ID_PEDIDO_DETALLE,
                Contract.PedidoDetalle.COLUMN_ID_PEDIDO_INT, Contract.PedidoDetalle.COLUMN_ID_PRODUCTO, Contract.PedidoDetalle.COLUMN_VALOR_UNITARIO, Contract.PedidoDetalle.COLUMN_CANTIDAD,
                Contract.PedidoDetalle.COLUMN_VALOR_TOTAL, Contract.PedidoDetalle.COLUMN_DESCRIPCION}
                ,Contract.PedidoDetalle.COLUMN_ID_PEDIDO + " = ? ", new String[]{idPedido + ""});

        List<PedidoDetalle> list = new ArrayList<PedidoDetalle>();
        while(c.moveToNext()){
            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setID(CursorUtils.getInt(c, Contract.PedidoDetalle._ID));
            detalle.set_idPedido(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_ID_PEDIDO_INT));
            detalle.setIdProducto(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_ID_PRODUCTO));
            detalle.setIdPedido(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_ID_PEDIDO));
            detalle.setIdPedidoDetalle(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_ID_PEDIDO_DETALLE));
            detalle.setValorUnitario(CursorUtils.getDouble(c, Contract.PedidoDetalle.COLUMN_VALOR_UNITARIO));
            detalle.setCantidad(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_CANTIDAD));
            detalle.setValorTotal(CursorUtils.getDouble(c, Contract.PedidoDetalle.COLUMN_VALOR_TOTAL));
            detalle.setDescripcion(CursorUtils.getString(c, Contract.PedidoDetalle.COLUMN_DESCRIPCION));
            list.add(detalle);
        }
        c.close();
        return list;
    }

    public static List<PedidoDetalle> getPedidoDetallesInt(DataBase db, long idPedido) {
        Cursor c = db.query(Contract.PedidoDetalle.TABLE_NAME, new String[] {Contract.PedidoDetalle._ID, Contract.PedidoDetalle.COLUMN_ID_PEDIDO, Contract.PedidoDetalle.COLUMN_ID_PEDIDO_DETALLE,
                Contract.PedidoDetalle.COLUMN_ID_PEDIDO_INT, Contract.PedidoDetalle.COLUMN_ID_PRODUCTO, Contract.PedidoDetalle.COLUMN_VALOR_UNITARIO, Contract.PedidoDetalle.COLUMN_CANTIDAD,
                Contract.PedidoDetalle.COLUMN_VALOR_TOTAL, Contract.PedidoDetalle.COLUMN_DESCRIPCION}
                ,Contract.PedidoDetalle.COLUMN_ID_PEDIDO_INT + " = ? ", new String[]{idPedido + ""});

        List<PedidoDetalle> list = new ArrayList<PedidoDetalle>();
        while(c.moveToNext()){
            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setID(CursorUtils.getInt(c, Contract.PedidoDetalle._ID));
            detalle.set_idPedido(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_ID_PEDIDO_INT));
            detalle.setIdProducto(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_ID_PRODUCTO));
            detalle.setIdPedido(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_ID_PEDIDO));
            detalle.setIdPedidoDetalle(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_ID_PEDIDO_DETALLE));
            detalle.setValorUnitario(CursorUtils.getDouble(c, Contract.PedidoDetalle.COLUMN_VALOR_UNITARIO));
            detalle.setCantidad(CursorUtils.getInt(c, Contract.PedidoDetalle.COLUMN_CANTIDAD));
            detalle.setValorTotal(CursorUtils.getDouble(c, Contract.PedidoDetalle.COLUMN_VALOR_TOTAL));
            detalle.setDescripcion(CursorUtils.getString(c, Contract.PedidoDetalle.COLUMN_DESCRIPCION));
            list.add(detalle);
        }
        c.close();
        return list;
    }

    public static void deleteDetallesByIdPedido(DataBase db, int idPedido)
    {
        db.delete(Contract.PedidoDetalle.TABLE_NAME, Contract.PedidoDetalle.COLUMN_ID_PEDIDO + " = ?", new String[]{idPedido + ""});
    }

    public static void deleteDetallesByIdPedidoInt(DataBase db, int idPedido)
    {
        db.delete(Contract.PedidoDetalle.TABLE_NAME, Contract.PedidoDetalle.COLUMN_ID_PEDIDO_INT + " = ?", new String[]{idPedido + ""});
    }
}