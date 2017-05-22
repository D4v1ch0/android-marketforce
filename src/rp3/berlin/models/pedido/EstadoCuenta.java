package rp3.berlin.models.pedido;

import java.util.Date;

/**
 * Created by magno_000 on 04/05/2017.
 */

public class EstadoCuenta {
    private int id;
    private String documento;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private int diasVencidos;
    private int diasXVencer;
    private double valor;
    private double abono;
    private double saldo;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getDiasVencidos() {
        return diasVencidos;
    }

    public void setDiasVencidos(int diasVencidos) {
        this.diasVencidos = diasVencidos;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getAbono() {
        return abono;
    }

    public void setAbono(double abono) {
        this.abono = abono;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getDiasXVencer() {
        return diasXVencer;
    }

    public void setDiasXVencer(int diasXVencer) {
        this.diasXVencer = diasXVencer;
    }
}
