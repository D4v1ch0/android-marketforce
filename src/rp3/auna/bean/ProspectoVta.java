package rp3.auna.bean;

/**
 * Created by Jesus Villa on 06/10/2017.
 */

public class ProspectoVta {

    private int IdProspecto;
    private String Nombre;
    private String Celular;
    private String Telefono;
    private int TipoDocumento;
    private String Documento;
    private String Direccion1;
    private String Direccion2;
    private String TipoPersonaCode;
    private String Ruc;
    private String RazonSocial;
    private String Nombres;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String ContactoNombre;
    private String ContactoApellidoPaterno;
    private String ContactoApellidoMaterno;
    private String ContactoTelefono;
    private String EmpresaTelefono;
    private String Email;
    private int LlamadaReferido;
    private int VisitaReferido;
    private int IdAgente;
    private String OrigenCode;
    private String EstadoCode;
    private String Referente;

    //region Encapsulamiento


    public String getReferente() {
        return Referente;
    }

    public void setReferente(String referente) {
        Referente = referente;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    public int getIdProspecto() {
        return IdProspecto;
    }

    public void setIdProspecto(int idProspecto) {
        IdProspecto = idProspecto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
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

    public int getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return Documento;
    }

    public void setDocumento(String documento) {
        Documento = documento;
    }

    public String getDireccion1() {
        return Direccion1;
    }

    public void setDireccion1(String direccion1) {
        Direccion1 = direccion1;
    }

    public String getDireccion2() {
        return Direccion2;
    }

    public void setDireccion2(String direccion2) {
        Direccion2 = direccion2;
    }

    public String getTipoPersonaCode() {
        return TipoPersonaCode;
    }

    public void setTipoPersonaCode(String tipoPersonaCode) {
        TipoPersonaCode = tipoPersonaCode;
    }

    public String getRuc() {
        return Ruc;
    }

    public void setRuc(String ruc) {
        Ruc = ruc;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getContactoNombre() {
        return ContactoNombre;
    }

    public void setContactoNombre(String contactoNombre) {
        ContactoNombre = contactoNombre;
    }

    public String getContactoApellidoPaterno() {
        return ContactoApellidoPaterno;
    }

    public void setContactoApellidoPaterno(String contactoApellidoPaterno) {
        ContactoApellidoPaterno = contactoApellidoPaterno;
    }

    public String getContactoApellidoMaterno() {
        return ContactoApellidoMaterno;
    }

    public void setContactoApellidoMaterno(String contactoApellidoMaterno) {
        ContactoApellidoMaterno = contactoApellidoMaterno;
    }

    public String getContactoTelefono() {
        return ContactoTelefono;
    }

    public void setContactoTelefono(String contactoTelefono) {
        ContactoTelefono = contactoTelefono;
    }

    public String getEmpresaTelefono() {
        return EmpresaTelefono;
    }

    public void setEmpresaTelefono(String empresaTelefono) {
        EmpresaTelefono = empresaTelefono;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getLlamadaReferido() {
        return LlamadaReferido;
    }

    public void setLlamadaReferido(int llamadaReferido) {
        LlamadaReferido = llamadaReferido;
    }

    public int getVisitaReferido() {
        return VisitaReferido;
    }

    public void setVisitaReferido(int visitaReferido) {
        VisitaReferido = visitaReferido;
    }

    public int getIdAgente() {
        return IdAgente;
    }

    public void setIdAgente(int idAgente) {
        IdAgente = idAgente;
    }

    public String getOrigenCode() {
        return OrigenCode;
    }

    public void setOrigenCode(String origenCode) {
        OrigenCode = origenCode;
    }

    public String getEstadoCode() {
        return EstadoCode;
    }

    public void setEstadoCode(String estadoCode) {
        EstadoCode = estadoCode;
    }

    //endregion

    @Override
    public String toString() {
        return "ProspectoVta{" +
                "IdProspecto=" + IdProspecto +
                ", Nombre='" + Nombre + '\'' +
                ", Celular='" + Celular + '\'' +
                ", Telefono='" + Telefono + '\'' +
                ", TipoDocumento=" + TipoDocumento +
                ", Documento='" + Documento + '\'' +
                ", Direccion1='" + Direccion1 + '\'' +
                ", Direccion2='" + Direccion2 + '\'' +
                ", TipoPersonaCode='" + TipoPersonaCode + '\'' +
                ", Ruc='" + Ruc + '\'' +
                ", RazonSocial='" + RazonSocial + '\'' +
                ", Nombres='" + Nombres + '\'' +
                ", ApellidoPaterno='" + ApellidoPaterno + '\'' +
                ", ApellidoMaterno='" + ApellidoMaterno + '\'' +
                ", ContactoNombre='" + ContactoNombre + '\'' +
                ", ContactoApellidoPaterno='" + ContactoApellidoPaterno + '\'' +
                ", ContactoApellidoMaterno='" + ContactoApellidoMaterno + '\'' +
                ", ContactoTelefono='" + ContactoTelefono + '\'' +
                ", EmpresaTelefono='" + EmpresaTelefono + '\'' +
                ", Email='" + Email + '\'' +
                ", LlamadaReferido=" + LlamadaReferido +
                ", VisitaReferido=" + VisitaReferido +
                ", IdAgente=" + IdAgente +
                ", OrigenCode='" + OrigenCode + '\'' +
                ", EstadoCode='" + EstadoCode + '\'' +
                ", Referente='" + Referente + '\'' +
                '}';
    }
}
