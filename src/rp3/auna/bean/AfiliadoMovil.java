package rp3.auna.bean;

/**
 * Created by Jesus Villa on 24/10/2017.
 */

public class AfiliadoMovil {
    private String IdRegistro;
    private String OrigenSolicitud;
    private String Canal;
    private String CodigoPrograma;
    private String TipoOperacion;
    private String RucEmpresa;
    private String FechaNacimiento;
    private String Sexo;
    private String FlagFumador;
    private String Campana;
    private String UserMovil;

    @Override
    public String toString() {
        return "AfiliadoMovil{" +
                "IdRegistro='" + IdRegistro + '\'' +
                ", OrigenSolicitud='" + OrigenSolicitud + '\'' +
                ", Canal='" + Canal + '\'' +
                ", CodigoPrograma='" + CodigoPrograma + '\'' +
                ", TipoOperacion='" + TipoOperacion + '\'' +
                ", RucEmpresa='" + RucEmpresa + '\'' +
                ", FechaNacimiento='" + FechaNacimiento + '\'' +
                ", Sexo='" + Sexo + '\'' +
                ", FlagFumador='" + FlagFumador + '\'' +
                ", Campana='" + Campana + '\'' +
                ", UserMovil='" + UserMovil + '\'' +
                '}';
    }

    //region Encapsulamiento

    public String getIdRegistro() {
        return IdRegistro;
    }

    public void setIdRegistro(String idRegistro) {
        IdRegistro = idRegistro;
    }

    public String getOrigenSolicitud() {
        return OrigenSolicitud;
    }

    public void setOrigenSolicitud(String origenSolicitud) {
        OrigenSolicitud = origenSolicitud;
    }

    public String getCanal() {
        return Canal;
    }

    public void setCanal(String canal) {
        Canal = canal;
    }

    public String getCodigoPrograma() {
        return CodigoPrograma;
    }

    public void setCodigoPrograma(String codigoPrograma) {
        CodigoPrograma = codigoPrograma;
    }

    public String getTipoOperacion() {
        return TipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        TipoOperacion = tipoOperacion;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public String getFlagFumador() {
        return FlagFumador;
    }

    public void setFlagFumador(String flagFumador) {
        FlagFumador = flagFumador;
    }

    public String getCampana() {
        return Campana;
    }

    public void setCampana(String campana) {
        Campana = campana;
    }

    public String getUserMovil() {
        return UserMovil;
    }

    public void setUserMovil(String userMovil) {
        UserMovil = userMovil;
    }

    public String getRucEmpresa() {
        return RucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        RucEmpresa = rucEmpresa;
    }
    //endregion

}
