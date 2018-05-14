package rp3.auna.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import rp3.auna.util.constants.Constants;
import rp3.util.Convert;

/**
 * Created by Jesus Villa on 08/11/2017.
 */

public class SolicitudAfiliadoMovil {
    private Integer IdSolicitudAfiliado;
    private Integer IdSolicitud;
    private String Nombre;
    private String ApellidoPaterno;
    private String Correo;
    private String CodAsoCardHolderWallet;
    private String Celular;
    private String Telefono;
    private String TipoContratante;
    private Integer NumeroAfiliado;
    private Integer Sexo;
    private String IdCondicion;
    private long FechaNacimiento;
    private String IdTitular;
    //Nuevos Campos
    //public String ModoTarifa;
    public String TipoDocumentoIdentificacion;
    public String NumeroDocumentoIdentificacion;
    public String ApellidoMaterno;

    //region Encapsulamiento
    //public String getModoTarifa() {
    //    return ModoTarifa;
    //}

    //public void setModoTarifa(String modoTarifa) {
    //    ModoTarifa = modoTarifa;
    //}

    public String getTipoDocumentoIdentificacion() {
        return TipoDocumentoIdentificacion;
    }

    public void setTipoDocumentoIdentificacion(String tipoDocumentoIdentificacion) {
        TipoDocumentoIdentificacion = tipoDocumentoIdentificacion;
    }

    public String getNumeroDocumentoIdentificacion() {
        return NumeroDocumentoIdentificacion;
    }

    public void setNumeroDocumentoIdentificacion(String numeroDocumentoIdentificacion) {
        NumeroDocumentoIdentificacion = numeroDocumentoIdentificacion;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    public String getIdTitular() {
        return IdTitular;
    }

    public void setIdTitular(String idTitular) {
        IdTitular = idTitular;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getCodAsoCardHolderWallet() {
        return CodAsoCardHolderWallet;
    }

    public void setCodAsoCardHolderWallet(String codAsoCardHolderWallet) {
        CodAsoCardHolderWallet = codAsoCardHolderWallet;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public Integer getIdSolicitudAfiliado() {
        return IdSolicitudAfiliado;
    }

    public void setIdSolicitudAfiliado(Integer idSolicitudAfiliado) {
        IdSolicitudAfiliado = idSolicitudAfiliado;
    }

    public Integer getIdSolicitud() {
        return IdSolicitud;
    }

    public void setIdSolicitud(Integer idSolicitud) {
        IdSolicitud = idSolicitud;
    }

    public String getTipoContratante() {
        return TipoContratante;
    }

    public void setTipoContratante(String tipoContratante) {
        TipoContratante = tipoContratante;
    }

    public Integer getNumeroAfiliado() {
        return NumeroAfiliado;
    }

    public void setNumeroAfiliado(Integer numeroAfiliado) {
        NumeroAfiliado = numeroAfiliado;
    }

    public Integer getSexo() {
        return Sexo;
    }

    public void setSexo(Integer sexo) {
        Sexo = sexo;
    }

    public String getIdCondicion() {
        return IdCondicion;
    }

    public void setIdCondicion(String idCondicion) {
        IdCondicion = idCondicion;
    }

    public String getFechaNacimiento() {
        SimpleDateFormat sf = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date date = Convert.getDateFromDotNetTicks(FechaNacimiento);
        return sf.format(date);
    }

    public void setFechaNacimiento(long fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }
    //endregion

    @Override
    public String toString() {
        return "SolicitudAfiliadoMovil{" +
                "IdSolicitudAfiliado=" + IdSolicitudAfiliado +
                ", IdSolicitud=" + IdSolicitud +
                ", Nombre='" + Nombre + '\'' +
                ", ApellidoPaterno='" + ApellidoPaterno + '\'' +
                ", Correo='" + Correo + '\'' +
                ", CodAsoCardHolderWallet='" + CodAsoCardHolderWallet + '\'' +
                ", Celular='" + Celular + '\'' +
                ", Telefono='" + Telefono + '\'' +
                ", TipoContratante='" + TipoContratante + '\'' +
                ", NumeroAfiliado=" + NumeroAfiliado +
                ", Sexo=" + Sexo +
                ", IdCondicion='" + IdCondicion + '\'' +
                ", FechaNacimiento=" + FechaNacimiento +
                ", IdTitular='" + IdTitular + '\'' +
                //", ModoTarifa='" + ModoTarifa + '\'' +
                ", TipoDocumentoIdentificacion='" + TipoDocumentoIdentificacion + '\'' +
                ", NumeroDocumentoIdentificacion='" + NumeroDocumentoIdentificacion + '\'' +
                ", ApellidoMaterno='" + ApellidoMaterno + '\'' +
                '}';
    }
}
