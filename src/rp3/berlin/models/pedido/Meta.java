package rp3.berlin.models.pedido;

/**
 * Created by magno_000 on 27/06/2017.
 */

public class Meta {
    private int id;
    private int idAgente;
    private String descripcion;
    private String crep;
    private String grupoEstadistico;
    private String grupoEstadisticoDescripcion;
    private String grupoComision;
    private String grupoComisionDescripcion;
    private double cReal;
    private double cPresupuestado;
    private double real;
    private double presupuestado;
    private double porCumplir;
    private float porcDisVaria;
    private double varDistri;
    private double porAPagas;
    private double usdTotal;
    private String descGC;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCrep() {
        return crep;
    }

    public void setCrep(String crep) {
        this.crep = crep;
    }

    public String getGrupoEstadistico() {
        return grupoEstadistico;
    }

    public void setGrupoEstadistico(String grupoEstadistico) {
        this.grupoEstadistico = grupoEstadistico;
    }

    public String getGrupoEstadisticoDescripcion() {
        return grupoEstadisticoDescripcion;
    }

    public void setGrupoEstadisticoDescripcion(String grupoEstadisticoDescripcion) {
        this.grupoEstadisticoDescripcion = grupoEstadisticoDescripcion;
    }

    public String getGrupoComision() {
        return grupoComision;
    }

    public void setGrupoComision(String grupoComision) {
        this.grupoComision = grupoComision;
    }

    public String getGrupoComisionDescripcion() {
        return grupoComisionDescripcion;
    }

    public void setGrupoComisionDescripcion(String grupoComisionDescripcion) {
        this.grupoComisionDescripcion = grupoComisionDescripcion;
    }

    public double getcReal() {
        return cReal;
    }

    public void setcReal(double cReal) {
        this.cReal = cReal;
    }

    public double getcPresupuestado() {
        return cPresupuestado;
    }

    public void setcPresupuestado(double cPresupuestado) {
        this.cPresupuestado = cPresupuestado;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getPresupuestado() {
        return presupuestado;
    }

    public void setPresupuestado(double presupuestado) {
        this.presupuestado = presupuestado;
    }

    public double getPorCumplir() {
        return porCumplir;
    }

    public void setPorCumplir(double porCumplir) {
        this.porCumplir = porCumplir;
    }

    public float getPorcDisVaria() {
        return porcDisVaria;
    }

    public void setPorcDisVaria(float porcDisVaria) {
        this.porcDisVaria = porcDisVaria;
    }

    public double getVarDistri() {
        return varDistri;
    }

    public void setVarDistri(double varDistri) {
        this.varDistri = varDistri;
    }

    public double getPorAPagas() {
        return porAPagas;
    }

    public void setPorAPagas(double porAPagas) {
        this.porAPagas = porAPagas;
    }

    public double getUsdTotal() {
        return usdTotal;
    }

    public void setUsdTotal(double usdTotal) {
        this.usdTotal = usdTotal;
    }

    public String getDescGC() {
        return descGC;
    }

    public void setDescGC(String descGC) {
        this.descGC = descGC;
    }
}
