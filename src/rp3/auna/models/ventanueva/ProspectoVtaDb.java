package rp3.auna.models.ventanueva;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rp3.auna.models.Cliente;
import rp3.auna.models.Contacto;
import rp3.data.models.Contract;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.util.CursorUtils;

/**
 * Created by Jesus Villa on 06/10/2017.
 */

public class ProspectoVtaDb extends rp3.data.entity.EntityBase<ProspectoVtaDb>{
    private long _id;
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
    private int Estado;

    //region Encapsulamiento


    public String getReferente() {
        return Referente;
    }

    public void setReferente(String referente) {
        Referente = referente;
    }

    public String getEstadoCode() {
        return EstadoCode;
    }

    public void setEstadoCode(String estadoCode) {
        EstadoCode = estadoCode;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    //endregion

    /**
     * VTA DB
     * @return
     */

    @Override
    public long getID() {
        return _id;
    }


    @Override
    public String toString() {
        return "ProspectoVtaDb{" +
                "_id=" + _id +
                ", IdProspecto=" + IdProspecto +
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
                ", Estado=" + Estado +
                '}';
    }

    @Override
    public void setID(long id) {
        this._id = id;
    }

    @Override
    public boolean isAutoGeneratedId() {
        return true;
    }

    @Override
    public String getTableName() {
        return rp3.auna.db.Contract.ProspectoVta.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO,this.IdProspecto);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE, this.Nombre);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR, this.Celular);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO, this.Telefono);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO, this.TipoDocumento);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO,this.Documento);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1,this.Direccion1);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2,this.Direccion2);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA, this.TipoPersonaCode);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC,this.Ruc);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL, this.RazonSocial);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES, this.Nombres);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO, this.ApellidoPaterno);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO, this.ApellidoMaterno);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE, this.ContactoNombre);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO, this.ContactoApellidoPaterno);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO, this.ContactoApellidoMaterno);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO,this.ContactoTelefono);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO,this.EmpresaTelefono);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL,this.Email);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA,this.LlamadaReferido);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA,this.VisitaReferido);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE,this.IdAgente);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN,this.OrigenCode);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE,this.EstadoCode);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE,this.Referente);
        setValue(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO, this.Estado);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static int getMaxIdProspectoVta(DataBase db){
        String query = rp3.auna.db.Contract.ProspectoVta.QUERY_GET_MAX_ID;
        int id = -1;
        try{

            Cursor cursor=db.rawQuery(query,new String [] {});
            if (cursor != null)
                if(cursor.moveToFirst())
                {
                    id = cursor.getInt(0);

                }
            //  cursor.close();

            return id ;
        }
        catch(Exception e){
            Log.d("Prospecto models","Exception:"+ e.getMessage());
            return -1;
        }
    }

    public boolean updateValues(DataBase dataBase){
        ContentValues contentValues = new ContentValues();
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO,this.IdProspecto);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE, this.Nombre);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR, this.Celular);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO, this.Telefono);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO, this.TipoDocumento);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO,this.Documento);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1,this.Direccion1);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2,this.Direccion2);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA, this.TipoPersonaCode);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC,this.Ruc);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL, this.RazonSocial);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES, this.Nombres);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO, this.ApellidoPaterno);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO, this.ApellidoMaterno);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE, this.ContactoNombre);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO, this.ContactoApellidoPaterno);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO, this.ContactoApellidoMaterno);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO,this.ContactoTelefono);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO,this.EmpresaTelefono);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL,this.Email);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA,this.LlamadaReferido);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA,this.VisitaReferido);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE,this.IdAgente);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN,this.OrigenCode);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE,this.EstadoCode);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE,this.Referente);
        contentValues.put(rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO, this.Estado);
        long i = dataBase.update(rp3.auna.db.Contract.ProspectoVta.TABLE_NAME,contentValues,"_id="+getID());
        Log.d("ProspectoVTADB","i="+i);
        return i>0;
    }

    public void updateHard(DataBase dataBase){
        String query = "UPDATE "+rp3.auna.db.Contract.ProspectoVta.TABLE_NAME+" SET "+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1 + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2 + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE + " = "+ this.IdProspecto + ","+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO + " = "+ this.IdProspecto + " WHERE "+
                rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ID +" = "+ this._id;
        dataBase.execSQL(query);
    }

    public static List<ProspectoVtaDb> getProspectosActualizados(DataBase db) {
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_ACTUALIZADoS);
        Cursor c = db.rawQuery(query);
        if(c.moveToFirst()){
            do
            {
                ProspectoVtaDb prospectoVtaDb =  new ProspectoVtaDb();
                prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                prospectoVtaDb.setLlamadaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA));
                prospectoVtaDb.setVisitaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA));
                prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                prospectoVtaDb.setReferente(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE));
                prospectos.add(prospectoVtaDb);
            }while(c.moveToNext());
        }
        c.close();

        return prospectos;
    }

    public static List<ProspectoVtaDb> getProspectoListaNegra(DataBase db) {
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_LISTA_NEGRA);
        Cursor c = db.rawQuery(query);
        if(c.moveToFirst()){
            do
            {
                String documento = CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO);
                if(documento!=null){
                    if(documento.trim().length()>0){
                        ProspectoVtaDb prospectoVtaDb =  new ProspectoVtaDb();
                        prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                        prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                        prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                        prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                        prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                        prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                        prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                        prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                        prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                        prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                        prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                        prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                        prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                        prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                        prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                        prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                        prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                        prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                        prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                        prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                        prospectoVtaDb.setLlamadaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA));
                        prospectoVtaDb.setVisitaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA));
                        prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                        prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                        prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                        prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                        prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                        prospectoVtaDb.setReferente(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE));
                        prospectos.add(prospectoVtaDb);
                    }
                }

            }while(c.moveToNext());
        }
        c.close();

        return prospectos;
    }

    public static List<ProspectoVtaDb> getProspectoSincronizadas(DataBase db) {
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_SINCRONIZADAS);
        Cursor c = db.rawQuery(query);
        if(c.moveToFirst()){
            do
            {
                ProspectoVtaDb prospectoVtaDb =  new ProspectoVtaDb();
                prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                prospectoVtaDb.setLlamadaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA));
                prospectoVtaDb.setVisitaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA));
                prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                prospectoVtaDb.setReferente(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE));
                prospectos.add(prospectoVtaDb);
            }while(c.moveToNext());
        }
        c.close();

        return prospectos;
    }

    public static List<ProspectoVtaDb> getProspectoInsert(DataBase db) {
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_INSERTADAS);
        Cursor c = db.rawQuery(query);
        if(c.moveToFirst()){
            do
            {
                ProspectoVtaDb prospectoVtaDb =  new ProspectoVtaDb();
                prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                prospectoVtaDb.setLlamadaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA));
                prospectoVtaDb.setVisitaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA));
                prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                prospectoVtaDb.setReferente(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE));
                prospectos.add(prospectoVtaDb);
            }while(c.moveToNext());
        }
        c.close();

        return prospectos;
    }

    public static ProspectoVtaDb getProspectoIdProspecto(DataBase db,int i) {
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_SINCRONIZADAS_IDPROSPECTO);
        Cursor c = null;
        c = db.rawQuery(query, new String[] {i + ""});
        if(c.moveToFirst()){
            do
            {
                ProspectoVtaDb prospectoVtaDb =  new ProspectoVtaDb();
                prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                prospectoVtaDb.setReferente(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE));
                prospectos.add(prospectoVtaDb);
            }while(c.moveToNext());
        }
        c.close();
        if(prospectos!=null){
            if(prospectos.size()>0){
                return prospectos.get(0);
            }
        }
        return null;
    }

    public static ProspectoVtaDb getProspectoIdProspectoBD(DataBase db,int i) {
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_SINCRONIZADAS_IDBD);
        Cursor c = null;
        c = db.rawQuery(query, new String[] {i + ""});
        if(c.moveToFirst()){
            do
            {
                ProspectoVtaDb prospectoVtaDb =  new ProspectoVtaDb();
                prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                prospectos.add(prospectoVtaDb);
            }while(c.moveToNext());
        }
        c.close();
        if(prospectos!=null){
            if(prospectos.size()==1){
                return prospectos.get(0);
            }
        }
        return null;
    }

    public static List<ProspectoVtaDb> getAll(DataBase db) {
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_ALL);
        Cursor c = db.rawQuery(query);
        if(c.moveToFirst()){
            do
            {
                ProspectoVtaDb prospectoVtaDb =  new ProspectoVtaDb();
                prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                prospectoVtaDb.setLlamadaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA));
                prospectoVtaDb.setVisitaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA));
                prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                prospectoVtaDb.setReferente(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE));
                prospectos.add(prospectoVtaDb);
                Log.d("ProspectoEntity",prospectoVtaDb.toString());
            }while(c.moveToNext());
        }
        c.close();

        return prospectos;
    }

    public static ProspectoVtaDb getProspectoId(DataBase db,int i){
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_ALL);
        Cursor c = db.rawQuery(query);
        ProspectoVtaDb prospectoVtaDb = null;
        if(c.moveToFirst()){
            do
            {
                if(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO)==i){
                    prospectoVtaDb =  new ProspectoVtaDb();
                    prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                    prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                    prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                    prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                    prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                    prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                    prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                    prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                    prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                    prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                    prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                    prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                    prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                    prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                    prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                    prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                    prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                    prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                    prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                    prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                    prospectoVtaDb.setLlamadaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA));
                    prospectoVtaDb.setVisitaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA));
                    prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                    prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                    prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                    prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                    prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                    prospectoVtaDb.setReferente(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE));
                    prospectos.add(prospectoVtaDb);
                    Log.d("ProspectoEntity",prospectoVtaDb.toString());
                    break;
                }

            }while(c.moveToNext());
        }
        c.close();
        if(prospectoVtaDb==null){
            return null;
        }
        return prospectoVtaDb;
    }

    public static List<ProspectoVtaDb> getAllEstado(DataBase db) {
        List<ProspectoVtaDb> prospectos = new ArrayList<>();
        String query = QueryDir.getQuery(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_ALL_ESTADO);
        Cursor c = db.rawQuery(query);
        if(c.moveToFirst()){
            do
            {
                ProspectoVtaDb prospectoVtaDb =  new ProspectoVtaDb();
                prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                prospectoVtaDb.setCelular(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CELULAR));
                prospectoVtaDb.setTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TELEFONO));
                prospectoVtaDb.setTipoDocumento(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPODOCUMENTO));
                prospectoVtaDb.setDocumento(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DOCUMENTO));
                prospectoVtaDb.setDireccion1(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION1));
                prospectoVtaDb.setDireccion2(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_DIRECCION2));
                prospectoVtaDb.setContactoNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTONOMBRE));
                prospectoVtaDb.setContactoApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOPATERNO));
                prospectoVtaDb.setContactoApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOAPELLIDOMATERNO));
                prospectoVtaDb.setEmpresaTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMPRESATELEFONO));
                prospectoVtaDb.setRuc(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RUC));
                prospectoVtaDb.setRazonSocial(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_RAZONSOCIAL));
                prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                prospectoVtaDb.setContactoTelefono(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_CONTACTOTELEFONO));
                prospectoVtaDb.setEmail(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
                prospectoVtaDb.setLlamadaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_LLAMADA));
                prospectoVtaDb.setVisitaReferido(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERIDO_VISITA));
                prospectoVtaDb.setIdAgente(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDAGENTE));
                prospectoVtaDb.setEstado(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO));
                prospectoVtaDb.setTipoPersonaCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_TIPOPERSONA));
                prospectoVtaDb.setOrigenCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ORIGEN));
                prospectoVtaDb.setEstadoCode(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_ESTADO_CODE));
                prospectoVtaDb.setReferente(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_REFERENTE));
                prospectos.add(prospectoVtaDb);
                Log.d("ProspectoEntity",prospectoVtaDb.toString());
            }while(c.moveToNext());
        }
        c.close();

        return prospectos;
    }

    public static ProspectoVtaDb getProspectoReferente(DataBase db,int i){
        ProspectoVtaDb prospectoVtaDb = null;
        String query = rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTO_VTA_REFERIDO+i;
        Cursor c = db.rawQuery(query);
        if(c.moveToFirst()){
            do
            {
                prospectoVtaDb =  new ProspectoVtaDb();
                prospectoVtaDb.set_id(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta._ID));
                prospectoVtaDb.setIdProspecto(CursorUtils.getInt(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_IDPROSPECTO));
                prospectoVtaDb.setNombre(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
                prospectoVtaDb.setNombres(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRES));
                prospectoVtaDb.setApellidoPaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOPATERNO));
                prospectoVtaDb.setApellidoMaterno(CursorUtils.getString(c,rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_APELLIDOMATERNO));
                Log.d("ProspectoEntity",prospectoVtaDb.toString());
            }while(c.moveToNext());
        }
        c.close();

        return prospectoVtaDb;
    }

    public static void ActualizarInsertados(DataBase db) {
        db.execSQL(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_UPDATE_TO_INSERTADAS);
    }

    public static void actualizarProspectoBd(DataBase db,List<ProspectoVtaDb> list,List<ProspectoVtaDb> nuevos){
        for (ProspectoVtaDb obj:list){

            ProspectoVtaDb.update(db,obj,ACTION_UPDATE);
        }
    }

    public static void ActualizarInsertadosBd(DataBase db,int i) {

        db.execSQL(rp3.auna.db.Contract.ProspectoVta.QUERY_PROSPECTOVTA_UPDATE_TO_INSERTADAS);
    }

    public static void deleteProspectos(DataBase db) {
        Log.d("ProspectoEntity","deleteProspectos con el estado 0 porque ya subi todos los que estaban pendiente y ahora traje a todos desde el servidor mismo...");
        db.execSQL(rp3.auna.db.Contract.ProspectoVta.QUERY_DELETE);
    }

    public static void deleteProspectosSincronizados(DataBase db) {
        Log.d("ProspectoEntity","deleteProspectos con el estado 2 porque ya subi todos los que estaban pendiente y ahora traje a todos desde el servidor mismo...");
        db.execSQL(rp3.auna.db.Contract.ProspectoVta.QUERY_DELETE_SINCRONIZADA);
    }

    public static String getProspectoNombreByIdVisita(int idVisita,DataBase db){
        String query = "SELECT T2.Nombre FROM tbVisitaVta T1 INNER JOIN  tbProspectoVta T2\n" +
                       "ON T1.IdCliente = T2.IdProspecto WHERE T1.IdVisita = "+idVisita;
        Log.d("ProspectoEntity","Query:"+query);
        Cursor c = db.rawQuery(query);
        String nombre = null;
        if(c.moveToFirst()){

            nombre = (CursorUtils.getString(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_NOMBRE));
        }
        c.close();
        return nombre;
    }

    public static String getProspectoEmailByIdVisita(int idVisita,DataBase db){
        String query = "SELECT T2.Email FROM tbVisitaVta AS T1 INNER JOIN  tbProspectoVta AS T2\n" +
                "ON T1.IdCliente = T2.IdProspecto WHERE T1.IdVisita = "+idVisita;
        Log.d("ProspectoEntity","Query:"+query);
        Cursor c = db.rawQuery(query);
        String nombre = null;
        if(c.moveToFirst()){
            nombre = (CursorUtils.getString(c, rp3.auna.db.Contract.ProspectoVta.COLUMN_PROSPECTOVTA_EMAIL));
        }
        c.close();
        return nombre;
    }


}
