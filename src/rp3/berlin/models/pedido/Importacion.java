package rp3.berlin.models.pedido;

import java.util.Date;

/**
 * Created by magno_000 on 27/04/2017.
 */

public class Importacion {
    private int id;
    private String orden;
    private String item;
    private String tipoOC;
    private String proveedor;
    private String idPais;
    private Date fechaPedido;
    private Date fechaPlanRecepcion;
    private double cantidad;

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTipoOC() {
        return tipoOC;
    }

    public void setTipoOC(String tipoOC) {
        this.tipoOC = tipoOC;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Date getFechaPlanRecepcion() {
        return fechaPlanRecepcion;
    }

    public void setFechaPlanRecepcion(Date fechaPlanRecepcion) {
        this.fechaPlanRecepcion = fechaPlanRecepcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}
