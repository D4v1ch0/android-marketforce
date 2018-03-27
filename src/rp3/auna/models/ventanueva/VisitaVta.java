package rp3.auna.models.ventanueva;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.auna.Contants;
import rp3.auna.db.Contract;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.util.Convert;
import rp3.util.CursorUtils;

/**
 * Created by Jesus Villa on 22/09/2017.
 */

public class VisitaVta extends rp3.data.entity.EntityBase<VisitaVta>  {
    private long _id;
    private int IdVisita;
    private String Descripcion;
    private Date FechaVisita;
    private Date FechaInicio;
    private Date FechaFin;
    private int IdCliente;
    private String IdClienteDireccion;
    private int IdAgente;
    private String Observacion;
    private double Latitud;
    private double Longitud;
    private int MotivoReprogramacionTabla;
    private String MotivoReprogramacionValue;
    private int MotivoVisitaTabla;
    private String MotivoVisitaValue;
    private int VisitaTabla;
    private String VisitaValue;
    private int ReferidoTabla;
    private String ReferidoValue;
    private String TiempoCode;
    private String DuracionCode;
    private String TipoVenta;
    private int ReferidoCount;
    private int VisitaId;
    private int LlamadaId;
    private int Estado;
    private int Insertado;
    /**
     * 0 = Sincronizado
     * 1 = Insertado
     * 2 = Actualizado
     *
     * **/

    //region Comportamiento
    public int getVisitaId() {
        return VisitaId;
    }

    public void setVisitaId(int visitaId) {
        VisitaId = visitaId;
    }

    public int getLlamadaId() {
        return LlamadaId;
    }

    public void setLlamadaId(int llamadaId) {
        LlamadaId = llamadaId;
    }

    private ArrayList<FotoVisitaVta> fotos;

    public ArrayList<FotoVisitaVta> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<FotoVisitaVta> fotos) {
        this.fotos = fotos;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getIdVisita() {
        return IdVisita;
    }

    public void setIdVisita(int idVisita) {
        IdVisita = idVisita;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Date getFechaVisita() {
        return FechaVisita;
    }

    public void setFechaVisita(Date fechaVisita) {
        FechaVisita = fechaVisita;
    }

    public Date getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        FechaFin = fechaFin;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public String getIdClienteDireccion() {
        return IdClienteDireccion;
    }

    public void setIdClienteDireccion(String idClienteDireccion) {
        IdClienteDireccion = idClienteDireccion;
    }

    public int getIdAgente() {
        return IdAgente;
    }

    public void setIdAgente(int idAgente) {
        IdAgente = idAgente;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public int getMotivoReprogramacionTabla() {
        return MotivoReprogramacionTabla;
    }

    public void setMotivoReprogramacionTabla(int motivoReprogramacionTabla) {
        MotivoReprogramacionTabla = motivoReprogramacionTabla;
    }

    public String getMotivoReprogramacionValue() {
        return MotivoReprogramacionValue;
    }

    public void setMotivoReprogramacionValue(String motivoReprogramacionValue) {
        MotivoReprogramacionValue = motivoReprogramacionValue;
    }

    public int getMotivoVisitaTabla() {
        return MotivoVisitaTabla;
    }

    public void setMotivoVisitaTabla(int motivoVisitaTabla) {
        MotivoVisitaTabla = motivoVisitaTabla;
    }

    public String getMotivoVisitaValue() {
        return MotivoVisitaValue;
    }

    public void setMotivoVisitaValue(String motivoVisitaValue) {
        MotivoVisitaValue = motivoVisitaValue;
    }

    public int getVisitaTabla() {
        return VisitaTabla;
    }

    public void setVisitaTabla(int visitaTabla) {
        VisitaTabla = visitaTabla;
    }

    public String getVisitaValue() {
        return VisitaValue;
    }

    public void setVisitaValue(String visitaValue) {
        VisitaValue = visitaValue;
    }

    public int getReferidoTabla() {
        return ReferidoTabla;
    }

    public void setReferidoTabla(int referidoTabla) {
        ReferidoTabla = referidoTabla;
    }

    public String getReferidoValue() {
        return ReferidoValue;
    }

    public void setReferidoValue(String referidoValue) {
        ReferidoValue = referidoValue;
    }

    public String getTiempoCode() {
        return TiempoCode;
    }

    public void setTiempoCode(String tiempoCode) {
        TiempoCode = tiempoCode;
    }

    public String getDuracionCode() {
        return DuracionCode;
    }

    public void setDuracionCode(String duracionCode) {
        DuracionCode = duracionCode;
    }

    public int getReferidoCount() {
        return ReferidoCount;
    }

    public void setReferidoCount(int referidoCount) {
        ReferidoCount = referidoCount;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public int getInsertado() {
        return Insertado;
    }

    public void setInsertado(int insertado) {
        Insertado = insertado;
    }

    public String getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        TipoVenta = tipoVenta;
    }

    //endregion

    @Override
    public String toString() {
        return "VisitaVta{" +
                "_id=" + _id +
                ", IdVisita=" + IdVisita +
                ", Descripcion='" + Descripcion + '\'' +
                ", FechaVisita=" + FechaVisita +
                ", FechaInicio=" + FechaInicio +
                ", FechaFin=" + FechaFin +
                ", IdCliente=" + IdCliente +
                ", IdClienteDireccion='" + IdClienteDireccion + '\'' +
                ", IdAgente=" + IdAgente +
                ", Observacion='" + Observacion + '\'' +
                ", Latitud=" + Latitud +
                ", Longitud=" + Longitud +
                ", MotivoReprogramacionTabla=" + MotivoReprogramacionTabla +
                ", MotivoReprogramacionValue='" + MotivoReprogramacionValue + '\'' +
                ", MotivoVisitaTabla=" + MotivoVisitaTabla +
                ", MotivoVisitaValue='" + MotivoVisitaValue + '\'' +
                ", VisitaTabla=" + VisitaTabla +
                ", VisitaValue='" + VisitaValue + '\'' +
                ", ReferidoTabla=" + ReferidoTabla +
                ", ReferidoValue='" + ReferidoValue + '\'' +
                ", TiempoCode='" + TiempoCode + '\'' +
                ", DuracionCode='" + DuracionCode + '\'' +
                ", TipoVenta='" + TipoVenta + '\'' +
                ", ReferidoCount=" + ReferidoCount +
                ", VisitaId=" + VisitaId +
                ", LlamadaId=" + LlamadaId +
                ", Estado=" + Estado +
                ", Insertado=" + Insertado +
                ", fotos=" + fotos +
                '}';
    }

    //region Bd
    @Override
    public long getID() {
        return this._id;
    }

    @Override
    public void setID(long id) {

    }
    @Override
    public boolean isAutoGeneratedId() {
        return true;
    }

    @Override
    public String getTableName() {
        return Contract.VisitaVta.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA, this.IdVisita);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION, this.Descripcion);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA, this.FechaVisita);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO, this.FechaInicio);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN, this.FechaFin);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE ,this.IdCliente);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION, this.IdClienteDireccion);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE, this.IdAgente);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION,this.Observacion);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD, this.Latitud);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD, this.Longitud);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA, this.MotivoReprogramacionTabla);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE, this.MotivoReprogramacionValue);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA, this.MotivoVisitaTabla);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE, this.MotivoVisitaValue);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE, this.VisitaTabla);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE, this.VisitaValue);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA ,this.ReferidoTabla);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_VALUE, this.ReferidoValue);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE, this.TiempoCode);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE, this.DuracionCode);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA, this.TipoVenta);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT, this.ReferidoCount);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID, this.VisitaId);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID, this.LlamadaId);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO,this.Estado);
        setValue(Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO, this.Insertado);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
    //endregion

    public static List<VisitaVta> getVisitasInsert(DataBase db) {
            String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_INSERTADAS);
            Cursor c = db.rawQuery(query);
            Log.d("VisitaVta","QUERY VISITAS INSERTADAS:"+query);
            List<VisitaVta> list = new ArrayList<>();
            if(c.moveToFirst()){
                do
                {
                    VisitaVta llamadaVta =  new VisitaVta();
                    llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                    llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                    llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                    llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                    llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                    llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                    llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                    llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                    llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                    llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                    llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                    list.add(llamadaVta);
                }while(c.moveToNext());
            }
            c.close();
            return list;
    }

    public static VisitaVta getVisitasfromAgenda(DataBase db,int idAgenda) {
        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_SINCRONIZADAS_AGENDA);
        Cursor c = null;
        c = db.rawQuery(query, new String[] {idAgenda + ""});
        List<VisitaVta> list = new ArrayList<>();
        if(c.moveToFirst()){
            do
            {
                VisitaVta llamadaVta =  new VisitaVta();
                llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                list.add(llamadaVta);
            }while(c.moveToNext());
        }
        c.close();
        if(list!=null){
            if(list.size()==1){
                return list.get(0);
            }
        }
        return null;
    }

    public static VisitaVta getVisitasfromAgendaBD(DataBase db,int idAgenda) {
        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_SINCRONIZADAS_AGENDABD);
        Cursor c = null;
        c = db.rawQuery(query, new String[] {idAgenda + ""});
        List<VisitaVta> list = new ArrayList<>();
        if(c.moveToFirst()){
            do
            {
                VisitaVta llamadaVta =  new VisitaVta();
                llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                list.add(llamadaVta);
            }while(c.moveToNext());
        }
        c.close();
        if(list!=null){
            if(list.size()==1){
                return list.get(0);
            }
        }
        return null;
    }

    public static List<VisitaVta> getAll(DataBase db) {
        List<VisitaVta> list = new ArrayList<>();
        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_ALL);
        try {
            Cursor c = db.rawQuery(query);


            if(c.moveToFirst()){
                do
                {
                    VisitaVta llamadaVta =  new VisitaVta();
                    llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                    llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                    llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                    llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                    llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                    llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                    llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                    llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                    llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                    llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                    llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                    list.add(llamadaVta);
                }while(c.moveToNext());
            }
            c.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static List<VisitaVta> getPendienteAll(DataBase db,int tiempoMenor) {

        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_PENDIENTE_ALL);

        Cursor c = db.rawQuery(query);
        List<VisitaVta> list = new ArrayList<>();

        if(c.moveToFirst()){
            do
            {
                VisitaVta llamadaVta =  new VisitaVta();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(CursorUtils.getDate(c,Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                final long ONE_MINUTE_IN_MILLIS=60000;
                final Calendar hoy = Calendar.getInstance();
                final long t= calendar.getTimeInMillis();
                Date fechaAlertarLlamada = new Date(t-(tiempoMenor*ONE_MINUTE_IN_MILLIS));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(fechaAlertarLlamada);
                if(hoy.getTimeInMillis()<calendar1.getTimeInMillis()){
                    llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                    llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                    llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                    llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                    llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                    llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                    llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                    llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                    llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                    llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                    llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                    list.add(llamadaVta);
                }
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<VisitaVta> getPendienteSupervisorAll(DataBase db,int tiempoMenor) {

        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_PENDIENTE_ALL);

        Cursor c = db.rawQuery(query);
        List<VisitaVta> list = new ArrayList<>();

        if(c.moveToFirst()){
            do
            {
                VisitaVta llamadaVta =  new VisitaVta();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(CursorUtils.getDate(c,Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                final long ONE_MINUTE_IN_MILLIS=60000;
                final Calendar hoy = Calendar.getInstance();
                final long t= calendar.getTimeInMillis();
                Date fechaAlertarLlamada = new Date(t+(tiempoMenor*ONE_MINUTE_IN_MILLIS));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(fechaAlertarLlamada);
                if(hoy.getTimeInMillis()<calendar1.getTimeInMillis()){
                    llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                    llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                    llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                    llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                    llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                    llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                    llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                    llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                    llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                    llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                    llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                    list.add(llamadaVta);
                }
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static VisitaVta getVisitaId(DataBase db,int i) {
        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_ALL);
        Log.d("Visitata","query:"+query);
        Cursor c = db.rawQuery(query);
        VisitaVta llamadaVta = null;
        if(c.moveToFirst()){
            do
            {
                if(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA) == i){
                    llamadaVta = new VisitaVta();
                    llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                    llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                    llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                    llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                    llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                    llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                    llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                    llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                    llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                    llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                    llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                    break;
                }
                break;
            }while(c.moveToNext());
        }
        c.close();
        if(llamadaVta!=null){
            return llamadaVta;
        }
        return null;
    }

    public static VisitaVta getVisitaIdBD(DataBase db,int i) {
        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_INSERTADAS);
        Log.d("VisitaVta","query:"+query);
        Cursor c = db.rawQuery(query);
        VisitaVta llamadaVta = null;
        if(c.moveToFirst()){
            do
            {
                if(CursorUtils.getInt(c,Contract.VisitaVta._ID)==i){
                    llamadaVta = new VisitaVta();
                    llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                    llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                    llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                    llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                    llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                    llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                    llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                    llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                    llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                    llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                    llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                    break;
                }

            }while(c.moveToNext());
        }
        c.close();
        if(llamadaVta!=null){
            return llamadaVta;
        }
        return null;
    }

    public static List<VisitaVta> getVisitasfromProspectoBD(DataBase db,int idProspecto) {
        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_SINCRONIZADAIDBDPROSPECTOBD);
        String newQuery = "SELECT * FROM tbVisitaVta WHERE Insertado = 1 and IdCliente = "+idProspecto;
        Cursor c = null;
        Log.d("VisitaVta:","QUERY VISITAS POR UN PROSPECTO SQLITE:"+newQuery);
        //c = db.rawQuery(query, new String[] {idProspecto + ""});
        c = db.rawQuery(newQuery);
        List<VisitaVta> list = new ArrayList<>();
        if(c.moveToFirst()){
            do
            {
                VisitaVta llamadaVta =  new VisitaVta();
                llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                list.add(llamadaVta);
            }while(c.moveToNext());
        }
        c.close();

        return list;
    }

    public static List<VisitaVta> getVisitasMinutes(DataBase db,long minutes_max, long minutes_min){
        long actual = Calendar.getInstance().getTimeInMillis();
        /*Cursor c = db.query(Contract.VisitaVta.TABLE_NAME, new String[]{
                        Contract.VisitaVta.COLUMN_VISITAVTA_ID,
                        Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA,
                        Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE,
                        Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA,
                        Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION,
                        Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE,
                        Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO,
                        Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO},
                "(" + Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA + " - " + actual + ") <= " + minutes_max + " AND " +
                        "(" + Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA + " - " + actual + ") >= " + minutes_min + " AND " +
                        Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE +
                        " = '"+ Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE+"'",
                null, null, null, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA + " ASC");*/
        Cursor c = db.query(Contract.VisitaVta.TABLE_NAME, new String[]{
                        Contract.VisitaVta.COLUMN_VISITAVTA_ID,
                        Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA,
                        Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE,
                        Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA,
                        Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION,
                        Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE,
                        Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO,
                        Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO},
                //"(" + Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA + " - " + actual + ") <= " + minutes_max + " AND " +
                        //"(" + Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA + " - " + actual + ") >= " + minutes_min + " AND " +
                        Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE +
                        " = '"+ Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE+"'",
                null, null, null, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA + " ASC");
        List<VisitaVta> list = new ArrayList<VisitaVta>();

        Calendar now = Calendar.getInstance();
        if(c.moveToFirst()){
            do
            {
                VisitaVta llamadaVta =  new VisitaVta();
                llamadaVta.setID(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                llamadaVta.setFechaVisita(CursorUtils.getDate(c,Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                Date date = llamadaVta.getFechaVisita();
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(date);
                long ONE_MINUTE_IN_MILLIS=60000;
                long t= fecha.getTimeInMillis();
                Date beforeFechaLlamada = new Date(t-(30*ONE_MINUTE_IN_MILLIS));
                Calendar before = Calendar.getInstance();
                before.setTime(beforeFechaLlamada);

                /*Date b = llamadaVta.getFechaVisita();//FechaVisita
                Date a = beforeFechaLlamada;
                Date d= now.getTime();*/
                if (now.getTime().compareTo(date) > 0 && now.getTime().compareTo(beforeFechaLlamada)<0) {
                    list.add(llamadaVta);
                } /*else if (date1.compareTo(date2) < 0) {
                    System.out.println("Date1 is before Date2");
                } else if (date1.compareTo(date2) == 0) {
                    System.out.println("Date1 is equal to Date2");
                } else {
                    System.out.println("How to get here?");
                }
                boolean sameTime = now.getTimeInMillis()>=before.getTimeInMillis()&&
                        now.getTimeInMillis()<= t; //d.compareTo(a) >= 0 && d.compareTo(b) <= 0;
                if(sameTime){
                    list.add(llamadaVta);
                }*/
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<VisitaVta> getVisitasCanceled(DataBase db){
        long actual = Calendar.getInstance().getTimeInMillis();
        /*Cursor c = db.query(Contract.VisitaVta.TABLE_NAME, new String[]{
                        Contract.VisitaVta.COLUMN_VISITAVTA_ID,
                        Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA,
                        Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE,
                        Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA,
                        Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION,
                        Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE,
                        Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO,
                        Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO},
                        Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA +" > " + actual + " AND " +
                                Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE +
                        " = '"+ Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE+"'",
                null, null, null, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA + " ASC");*/
        Cursor c = db.query(Contract.VisitaVta.TABLE_NAME, new String[]{
                        Contract.VisitaVta.COLUMN_VISITAVTA_ID,
                        Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA,
                        Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE,
                        Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA,
                        Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION,
                        Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE,
                        Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO,
                        Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO},
                //Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA +" > " + actual + " AND " +
                        Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE +
                        " = '"+ Contants.GENERAL_VALUE_CODE_VISITA_PENDIENTE+"'",
                null, null, null, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA + " ASC");
        List<VisitaVta> list = new ArrayList<VisitaVta>();

        if(c.moveToFirst()){
            do
            {
                VisitaVta llamadaVta =  new VisitaVta();
                llamadaVta.setID(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                llamadaVta.setFechaVisita(CursorUtils.getDate(c,Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                Date fechaLlamada = llamadaVta.getFechaVisita();
                Calendar now1 = Calendar.getInstance();
                Date now = now1.getTime();
                Calendar hoy = Calendar.getInstance();
                Calendar llamadaDia = Calendar.getInstance();
                llamadaDia.setTime(fechaLlamada);
                int dia = hoy.get(Calendar.DAY_OF_YEAR);
                int diaLlamada = llamadaDia.get(Calendar.DAY_OF_YEAR);
                if(dia == diaLlamada){
                    boolean someTime  = now.compareTo(fechaLlamada) >= 0;
                    if(someTime){
                        list.add(llamadaVta);
                    }
                }

            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<VisitaVta> getVisitaSincronizadas(DataBase db) {
        String query = QueryDir.getQuery(Contract.VisitaVta.QUERY_VISITAVTA_SINCRONIZADAS);
        Log.d("Visitata","QUEYR VISITAS ACTUALIZADAS:"+query);
        Cursor c = db.rawQuery(query);
        List<VisitaVta>  visitaVtas = new ArrayList<>();
        if(c.moveToFirst()){
            do
            {
                    VisitaVta  llamadaVta = new VisitaVta();
                    llamadaVta.set_id(CursorUtils.getInt(c,Contract.VisitaVta._ID));
                    llamadaVta.setIdVisita(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDVISITA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_OBSERVACION));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.VisitaVta.COLUMN_VISITAVTA_LONGITUD));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_TABLA));
                    llamadaVta.setFechaFin(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_FIN));
                    llamadaVta.setFechaInicio(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_INICIO));
                    llamadaVta.setFechaVisita(CursorUtils.getDate(c, Contract.VisitaVta.COLUMN_VISITAVTA_FECHA_VISITA));
                    llamadaVta.setID(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ID));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDAGENTE));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setIdClienteDireccion(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_IDCLIENTEDIRECCION));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setVisitaTabla(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_TABLE));
                    llamadaVta.setVisitaValue(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_VISITA_VALUE));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.VisitaVta.COLUMN_VISITAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setTiempoCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIEMPO_CODE));
                    llamadaVta.setDuracionCode(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_DURACION_CODE));
                    llamadaVta.setTipoVenta(CursorUtils.getString(c, Contract.VisitaVta.COLUMN_VISITAVTA_TIPO_VENTA));
                    llamadaVta.setReferidoCount(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_REFERIDO_COUNT));
                    llamadaVta.setVisitaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_VISITAID));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c,Contract.VisitaVta.COLUMN_VISITAVTA_LLAMADAID));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_INSERTADO));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.VisitaVta.COLUMN_VISITAVTA_ESTADO));
                   visitaVtas.add(llamadaVta);
            }
            while(c.moveToNext());
        }

        return visitaVtas;
    }

    public static void actualizarInsertados(DataBase db) {
        db.execSQL(Contract.VisitaVta.QUERY_VISITAVTA_UPDATE_TO_INSERTADAS);
    }

    public static void deleteVisitas(DataBase db) {
        Log.d("VisitaEntity","deleteVisitas...");
        db.execSQL(Contract.VisitaVta.QUERY_DELETE);
    }
}
