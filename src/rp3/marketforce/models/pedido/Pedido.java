package rp3.marketforce.models.pedido;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Cliente;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 12/10/2015.
 */
public class Pedido extends EntityBase<Pedido> {

    private long id;
    private int idPedido;
    private int idCliente;
    private int idAgenda;
    private int _idAgenda;
    private int _idCliente;
    private double valorTotal;
    private String email;
    private String estado;
    private Date fechaCreacion;
    private String numeroDocumento;
    private String observaciones;
    private String tipoDocumento;
    private double totalDescuentos;
    private double totalImpuestos;
    private double redondeo;
    private double subtotal;
    private double subtotalSinDescuento;
    private double excedente;
    private double baseImponible;
    private double baseImponibleCero;
    private String motivoAnulacion;
    private String observacionAnulacion;

    private List<PedidoDetalle> pedidoDetalles;
    private List<Pago> pagos;
    private Cliente cliente;

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
        return Contract.Pedido.TABLE_NAME;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(int idAgenda) {
        this.idAgenda = idAgenda;
    }

    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }

    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }

    public String getObservacionAnulacion() {
        return observacionAnulacion;
    }

    public void setObservacionAnulacion(String observacionAnulacion) {
        this.observacionAnulacion = observacionAnulacion;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<PedidoDetalle> getPedidoDetalles() {
        return pedidoDetalles;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setPedidoDetalles(List<PedidoDetalle> pedidoDetalles) {
        this.pedidoDetalles = pedidoDetalles;
    }

    public int get_idAgenda() {
        return _idAgenda;
    }

    public void set_idAgenda(int _idAgenda) {
        this._idAgenda = _idAgenda;
    }

    public int get_idCliente() {
        return _idCliente;
    }

    public void set_idCliente(int _idCliente) {
        this._idCliente = _idCliente;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public double getTotalDescuentos() {
        return totalDescuentos;
    }

    public void setTotalDescuentos(double totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public double getTotalImpuestos() {
        return totalImpuestos;
    }

    public void setTotalImpuestos(double totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }

    public double getRedondeo() {
        return redondeo;
    }

    public void setRedondeo(double redondeo) {
        this.redondeo = redondeo;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getSubtotalSinDescuento() {
        return subtotalSinDescuento;
    }

    public void setSubtotalSinDescuento(double subtotalSinDescuento) {
        this.subtotalSinDescuento = subtotalSinDescuento;
    }

    public double getExcedente() {
        return excedente;
    }

    public void setExcedente(double excedente) {
        this.excedente = excedente;
    }

    public double getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(double baseImponible) {
        this.baseImponible = baseImponible;
    }

    public double getBaseImponibleCero() {
        return baseImponibleCero;
    }

    public void setBaseImponibleCero(double baseImponibleCero) {
        this.baseImponibleCero = baseImponibleCero;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    @Override
    public void setValues() {
        setValue(Contract.Pedido.COLUMN_ID_PEDIDO, this.idPedido);
        setValue(Contract.Pedido.COLUMN_ID_AGENDA, this.idAgenda);
        setValue(Contract.Pedido.COLUMN_ID_AGENDA_INT, this._idAgenda);
        setValue(Contract.Pedido.COLUMN_ID_CLIENTE, this.idCliente);
        setValue(Contract.Pedido.COLUMN_VALOR_TOTAL, this.valorTotal);
        setValue(Contract.Pedido.COLUMN_EMAIL, this.email);
        setValue(Contract.Pedido.COLUMN_ESTADO, this.estado);
        setValue(Contract.Pedido.COLUMN_FECHA_CREACION, this.fechaCreacion);
        setValue(Contract.Pedido.COLUMN_NUMERO_DOCUMENTO, this.numeroDocumento);
        setValue(Contract.Pedido.COLUMN_TIPO_DOCUMENTO, this.tipoDocumento);
        setValue(Contract.Pedido.COLUMN_OBSERVACIONES, this.observaciones);
        setValue(Contract.Pedido.COLUMN_TOTAL_DESCUENTOS, this.totalDescuentos);
        setValue(Contract.Pedido.COLUMN_TOTAL_IMPUESTOS, this.totalImpuestos);
        setValue(Contract.Pedido.COLUMN_SUBTOTAL, this.subtotal);
        setValue(Contract.Pedido.COLUMN_SUBTOTAL_SIN_DESCUENTO, this.subtotalSinDescuento);
        setValue(Contract.Pedido.COLUMN_REDONDEO, this.redondeo);
        setValue(Contract.Pedido.COLUMN_EXCEDENTE, this.excedente);
        setValue(Contract.Pedido.COLUMN_BASE_IMPONIBLE, this.baseImponible);
        setValue(Contract.Pedido.COLUMN_BASE_IMPONIBLE_CERO, this.baseImponibleCero);
        setValue(Contract.Pedido.COLUMN_ID_CLIENTE_INT, this._idCliente);
        setValue(Contract.Pedido.COLUMN_MOTIVO_ANULACION, this.motivoAnulacion);
        setValue(Contract.Pedido.COLUMN_OBSERVACION_ANULACION, this.observacionAnulacion);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static List<Pedido> getPedidos(DataBase db) {
        Cursor c = db.query(Contract.Pedido.TABLE_NAME, new String[] {Contract.Pedido._ID, Contract.Pedido.COLUMN_ID_PEDIDO, Contract.Pedido.COLUMN_ID_AGENDA, Contract.Pedido.COLUMN_ID_AGENDA_INT,
                Contract.Pedido.COLUMN_ID_CLIENTE, Contract.Pedido.COLUMN_VALOR_TOTAL, Contract.Pedido.COLUMN_EMAIL, Contract.Pedido.COLUMN_ESTADO, Contract.Pedido.COLUMN_FECHA_CREACION, Contract.Pedido.COLUMN_ID_CLIENTE_INT,
                Contract.Pedido.COLUMN_NUMERO_DOCUMENTO, Contract.Pedido.COLUMN_TIPO_DOCUMENTO, Contract.Pedido.COLUMN_OBSERVACIONES, Contract.Pedido.COLUMN_TOTAL_DESCUENTOS, Contract.Pedido.COLUMN_TOTAL_IMPUESTOS, Contract.Pedido.COLUMN_SUBTOTAL,
                Contract.Pedido.COLUMN_SUBTOTAL_SIN_DESCUENTO, Contract.Pedido.COLUMN_REDONDEO, Contract.Pedido.COLUMN_EXCEDENTE, Contract.Pedido.COLUMN_BASE_IMPONIBLE, Contract.Pedido.COLUMN_BASE_IMPONIBLE_CERO}
                ,null, null, null,null, Contract.Pedido.COLUMN_FECHA_CREACION);

        List<Pedido> list = new ArrayList<Pedido>();
        while(c.moveToNext()){
            Pedido pedido = new Pedido();
            pedido.setID(CursorUtils.getInt(c, Contract.Pedido._ID));
            pedido.setIdAgenda(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_AGENDA));
            pedido.set_idAgenda(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_AGENDA_INT));
            pedido.setIdCliente(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_CLIENTE));
            pedido.setIdPedido(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_PEDIDO));
            pedido.setValorTotal(CursorUtils.getDouble(c, Contract.Pedido.COLUMN_VALOR_TOTAL));
            pedido.setEmail(CursorUtils.getString(c, Contract.Pedido.COLUMN_EMAIL));
            pedido.setEstado(CursorUtils.getString(c, Contract.Pedido.COLUMN_ESTADO));
            pedido.setFechaCreacion(CursorUtils.getDate(c, Contract.Pedido.COLUMN_FECHA_CREACION));
            if(pedido.getIdPedido() != 0) {
                pedido.setPedidoDetalles(PedidoDetalle.getPedidoDetalles(db, pedido.getIdPedido()));
                pedido.setPagos(Pago.getPagos(db, pedido.getIdPedido()));
            }
            else {
                pedido.setPedidoDetalles(PedidoDetalle.getPedidoDetallesInt(db, pedido.getID()));
                pedido.setPagos(Pago.getPagosInt(db, pedido.getID()));
            }
            pedido.setTipoDocumento(CursorUtils.getString(c, Contract.Pedido.COLUMN_TIPO_DOCUMENTO));
            pedido.setNumeroDocumento(CursorUtils.getString(c, Contract.Pedido.COLUMN_NUMERO_DOCUMENTO));
            pedido.set_idCliente(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_CLIENTE_INT));
            pedido.setObservaciones(CursorUtils.getString(c, Contract.Pedido.COLUMN_OBSERVACIONES));
            pedido.setTotalDescuentos(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_TOTAL_DESCUENTOS));
            pedido.setTotalImpuestos(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_TOTAL_IMPUESTOS));
            pedido.setSubtotal(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_SUBTOTAL));
            pedido.setSubtotalSinDescuento(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_SUBTOTAL_SIN_DESCUENTO));
            pedido.setExcedente(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_EXCEDENTE));
            pedido.setRedondeo(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_REDONDEO));
            pedido.setBaseImponible(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_BASE_IMPONIBLE));
            pedido.setBaseImponibleCero(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_BASE_IMPONIBLE_CERO));
            if(pedido.getIdCliente() != 0)
                pedido.setCliente(Cliente.getClienteIDServer(db, pedido.getIdCliente(), false));
            else
                pedido.setCliente(Cliente.getClienteID(db, pedido.get_idCliente(), false));
            list.add(pedido);
        }
        c.close();
        return list;
    }

    public static Pedido getPedido(DataBase db, long id) {
        Cursor c = db.query(Contract.Pedido.TABLE_NAME, new String[] {Contract.Pedido._ID, Contract.Pedido.COLUMN_ID_PEDIDO, Contract.Pedido.COLUMN_ID_AGENDA, Contract.Pedido.COLUMN_ID_AGENDA_INT,
                Contract.Pedido.COLUMN_ID_CLIENTE, Contract.Pedido.COLUMN_VALOR_TOTAL, Contract.Pedido.COLUMN_EMAIL, Contract.Pedido.COLUMN_ESTADO, Contract.Pedido.COLUMN_FECHA_CREACION, Contract.Pedido.COLUMN_ID_CLIENTE_INT,
                Contract.Pedido.COLUMN_NUMERO_DOCUMENTO, Contract.Pedido.COLUMN_TIPO_DOCUMENTO, Contract.Pedido.COLUMN_OBSERVACIONES, Contract.Pedido.COLUMN_TOTAL_DESCUENTOS, Contract.Pedido.COLUMN_TOTAL_IMPUESTOS, Contract.Pedido.COLUMN_SUBTOTAL,
                Contract.Pedido.COLUMN_SUBTOTAL_SIN_DESCUENTO, Contract.Pedido.COLUMN_REDONDEO, Contract.Pedido.COLUMN_EXCEDENTE, Contract.Pedido.COLUMN_BASE_IMPONIBLE, Contract.Pedido.COLUMN_BASE_IMPONIBLE_CERO,
                Contract.Pedido.COLUMN_MOTIVO_ANULACION, Contract.Pedido.COLUMN_OBSERVACION_ANULACION}
                ,Contract.Pedido._ID + " = ? ", new String[]{id + ""}, null,null, Contract.Pedido.COLUMN_FECHA_CREACION);

        Pedido pedido = new Pedido();
        while(c.moveToNext()){

            pedido.setID(CursorUtils.getInt(c, Contract.Pedido._ID));
            pedido.setIdAgenda(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_AGENDA));
            pedido.set_idAgenda(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_AGENDA_INT));
            pedido.setIdCliente(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_CLIENTE));
            pedido.setIdPedido(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_PEDIDO));
            pedido.setValorTotal(CursorUtils.getDouble(c, Contract.Pedido.COLUMN_VALOR_TOTAL));
            pedido.setEmail(CursorUtils.getString(c, Contract.Pedido.COLUMN_EMAIL));
            pedido.setEstado(CursorUtils.getString(c, Contract.Pedido.COLUMN_ESTADO));
            pedido.setFechaCreacion(CursorUtils.getDate(c, Contract.Pedido.COLUMN_FECHA_CREACION));
            pedido.setCliente(Cliente.getClienteIDServer(db, pedido.getIdCliente(), false));
            pedido.setPedidoDetalles(PedidoDetalle.getPedidoDetalles(db, pedido.getIdPedido()));
            if(pedido.getIdPedido() != 0) {
                pedido.setPedidoDetalles(PedidoDetalle.getPedidoDetalles(db, pedido.getIdPedido()));
                pedido.setPagos(Pago.getPagos(db, pedido.getIdPedido()));
            }
            else {
                pedido.setPedidoDetalles(PedidoDetalle.getPedidoDetallesInt(db, pedido.getID()));
                pedido.setPagos(Pago.getPagosInt(db, pedido.getID()));
            }
            pedido.setTipoDocumento(CursorUtils.getString(c, Contract.Pedido.COLUMN_TIPO_DOCUMENTO));
            pedido.setNumeroDocumento(CursorUtils.getString(c, Contract.Pedido.COLUMN_NUMERO_DOCUMENTO));
            pedido.set_idCliente(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_CLIENTE_INT));
            pedido.setObservaciones(CursorUtils.getString(c, Contract.Pedido.COLUMN_OBSERVACIONES));
            pedido.setTotalDescuentos(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_TOTAL_DESCUENTOS));
            pedido.setTotalImpuestos(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_TOTAL_IMPUESTOS));
            pedido.setSubtotal(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_SUBTOTAL));
            pedido.setSubtotalSinDescuento(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_SUBTOTAL_SIN_DESCUENTO));
            pedido.setExcedente(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_EXCEDENTE));
            pedido.setRedondeo(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_REDONDEO));
            pedido.setBaseImponible(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_BASE_IMPONIBLE));
            pedido.setBaseImponibleCero(CursorUtils.getFloat(c, Contract.Pedido.COLUMN_BASE_IMPONIBLE_CERO));
            pedido.setMotivoAnulacion(CursorUtils.getString(c, Contract.Pedido.COLUMN_MOTIVO_ANULACION));
            pedido.setObservacionAnulacion(CursorUtils.getString(c, Contract.Pedido.COLUMN_OBSERVACION_ANULACION));
            if(pedido.getIdCliente() != 0)
                pedido.setCliente(Cliente.getClienteIDServer(db, pedido.getIdCliente(), false));
            else
                pedido.setCliente(Cliente.getClienteID(db, pedido.get_idCliente(), false));

        }
        c.close();
        return pedido;
    }

    public static Pedido getPedidoByAgenda(DataBase db, long idAgenda) {
        Cursor c = db.query(Contract.Pedido.TABLE_NAME, new String[] {Contract.Pedido._ID, Contract.Pedido.COLUMN_ID_PEDIDO, Contract.Pedido.COLUMN_ID_AGENDA, Contract.Pedido.COLUMN_ID_AGENDA_INT,
                Contract.Pedido.COLUMN_ID_CLIENTE, Contract.Pedido.COLUMN_VALOR_TOTAL, Contract.Pedido.COLUMN_EMAIL, Contract.Pedido.COLUMN_ESTADO, Contract.Pedido.COLUMN_FECHA_CREACION, Contract.Pedido.COLUMN_ID_CLIENTE_INT,
                Contract.Pedido.COLUMN_NUMERO_DOCUMENTO, Contract.Pedido.COLUMN_TIPO_DOCUMENTO, Contract.Pedido.COLUMN_OBSERVACIONES, Contract.Pedido.COLUMN_TOTAL_DESCUENTOS, Contract.Pedido.COLUMN_TOTAL_IMPUESTOS, Contract.Pedido.COLUMN_SUBTOTAL,
                Contract.Pedido.COLUMN_SUBTOTAL_SIN_DESCUENTO, Contract.Pedido.COLUMN_REDONDEO, Contract.Pedido.COLUMN_EXCEDENTE, Contract.Pedido.COLUMN_BASE_IMPONIBLE, Contract.Pedido.COLUMN_BASE_IMPONIBLE_CERO}
                ,Contract.Pedido.COLUMN_ID_AGENDA_INT + " = ? ", new String[]{idAgenda + ""}, null,null, Contract.Pedido.COLUMN_FECHA_CREACION);

        Pedido pedido = new Pedido();
        while(c.moveToNext()){

            pedido.setID(CursorUtils.getInt(c, Contract.Pedido._ID));
            pedido.setIdAgenda(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_AGENDA));
            pedido.set_idAgenda(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_AGENDA_INT));
            pedido.setIdCliente(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_CLIENTE));
            pedido.setIdPedido(CursorUtils.getInt(c, Contract.Pedido.COLUMN_ID_PEDIDO));
            pedido.setValorTotal(CursorUtils.getDouble(c, Contract.Pedido.COLUMN_VALOR_TOTAL));
            pedido.setEmail(CursorUtils.getString(c, Contract.Pedido.COLUMN_EMAIL));
            pedido.setEstado(CursorUtils.getString(c, Contract.Pedido.COLUMN_ESTADO));
            pedido.setFechaCreacion(CursorUtils.getDate(c, Contract.Pedido.COLUMN_FECHA_CREACION));
            pedido.setCliente(Cliente.getClienteIDServer(db, pedido.getIdCliente(), false));
            pedido.setPedidoDetalles(PedidoDetalle.getPedidoDetalles(db, pedido.getIdPedido()));
            if(pedido.getIdPedido() != 0)
                pedido.setPedidoDetalles(PedidoDetalle.getPedidoDetalles(db, pedido.getIdPedido()));
            else
                pedido.setPedidoDetalles(PedidoDetalle.getPedidoDetallesInt(db, pedido.getID()));
        }
        c.close();
        return pedido;
    }
}