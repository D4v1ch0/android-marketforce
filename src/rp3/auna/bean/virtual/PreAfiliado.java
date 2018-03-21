package rp3.auna.bean.virtual;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 20/03/2018.
 */

public class PreAfiliado {
    @SerializedName("IdReg")
    private String idReg;
    @SerializedName("Titular")
    public int titular;
    @SerializedName("TipoDocumento")
    private String tipoDocumento;
    @SerializedName("NumeroDocumento")
    private String numeroDocumento;
    @SerializedName("ApePaterno")
    private String apePaterno;
    @SerializedName("ApeMaterno")
    private String apeMaterno;
    @SerializedName("Nombre1")
    private String nombre1;
    @SerializedName("Nombre2")
    private String nombre2;
    @SerializedName("FechaNacimiento")
    private String fechaNacimiento;
    @SerializedName("Sexo")
    private String sexo;
    @SerializedName("Direccion")
    private String direccion;
    @SerializedName("ReferenciaDireccion")
    private String referenciaDireccion;
    @SerializedName("CodigoPostal")
    private String codigoPostal;
    @SerializedName("Ubigeo")
    private String ubigeo;
    @SerializedName("Telefono1")
    private String telefono1;
    @SerializedName("Telefono2")
    private String telefono2;
    @SerializedName("Celular")
    private String celular;
    @SerializedName("Correo")
    private String correo;
    @SerializedName("Nacionalidad")
    private String nacionalidad;
    @SerializedName("EstadoCivil")
    private String estadoCivil;
    @SerializedName("FlagFumador")
    private String flagFumador;
    @SerializedName("Categoria")
    private String categoria;
    @SerializedName("Monto")
    private String monto;
    @SerializedName("TipoRegistro")
    private String tipoRegistro;
    @SerializedName("RespuestaPeas")
    private String respuestaPeas;
    @SerializedName("PrimeraCuota")
    private String primeraCuota;
    @SerializedName("SegundaCuota")
    private String segundaCuota;
    @SerializedName("TerceraCuota")
    private String terceraCuota;
    @SerializedName("CuartaCuota")
    private String cuartaCuota;

    //region Encapsulamiento

    public String getIdReg() {
        return idReg;
    }

    public void setIdReg(String idReg) {
        this.idReg = idReg;
    }

    public int getTitular() {
        return titular;
    }

    public void setTitular(int titular) {
        this.titular = titular;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getApePaterno() {
        return apePaterno;
    }

    public void setApePaterno(String apePaterno) {
        this.apePaterno = apePaterno;
    }

    public String getApeMaterno() {
        return apeMaterno;
    }

    public void setApeMaterno(String apeMaterno) {
        this.apeMaterno = apeMaterno;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferenciaDireccion() {
        return referenciaDireccion;
    }

    public void setReferenciaDireccion(String referenciaDireccion) {
        this.referenciaDireccion = referenciaDireccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFlagFumador() {
        return flagFumador;
    }

    public void setFlagFumador(String flagFumador) {
        this.flagFumador = flagFumador;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getRespuestaPeas() {
        return respuestaPeas;
    }

    public void setRespuestaPeas(String respuestaPeas) {
        this.respuestaPeas = respuestaPeas;
    }

    public String getPrimeraCuota() {
        return primeraCuota;
    }

    public void setPrimeraCuota(String primeraCuota) {
        this.primeraCuota = primeraCuota;
    }

    public String getSegundaCuota() {
        return segundaCuota;
    }

    public void setSegundaCuota(String segundaCuota) {
        this.segundaCuota = segundaCuota;
    }

    public String getTerceraCuota() {
        return terceraCuota;
    }

    public void setTerceraCuota(String terceraCuota) {
        this.terceraCuota = terceraCuota;
    }

    public String getCuartaCuota() {
        return cuartaCuota;
    }

    public void setCuartaCuota(String cuartaCuota) {
        this.cuartaCuota = cuartaCuota;
    }

    //endregion


    @Override
    public String toString() {
        return "PreAfiliado{" +
                "idReg='" + idReg + '\'' +
                ", titular=" + titular +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", apePaterno='" + apePaterno + '\'' +
                ", apeMaterno='" + apeMaterno + '\'' +
                ", nombre1='" + nombre1 + '\'' +
                ", nombre2='" + nombre2 + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", sexo='" + sexo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", referenciaDireccion='" + referenciaDireccion + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", ubigeo='" + ubigeo + '\'' +
                ", telefono1='" + telefono1 + '\'' +
                ", telefono2='" + telefono2 + '\'' +
                ", celular='" + celular + '\'' +
                ", correo='" + correo + '\'' +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", flagFumador='" + flagFumador + '\'' +
                ", categoria='" + categoria + '\'' +
                ", monto='" + monto + '\'' +
                ", tipoRegistro='" + tipoRegistro + '\'' +
                ", respuestaPeas='" + respuestaPeas + '\'' +
                ", primeraCuota='" + primeraCuota + '\'' +
                ", segundaCuota='" + segundaCuota + '\'' +
                ", terceraCuota='" + terceraCuota + '\'' +
                ", cuartaCuota='" + cuartaCuota + '\'' +
                '}';
    }
}
