package rp3.auna.models.ventanueva;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.auna.Contants;
import rp3.auna.db.Contract;
import rp3.auna.util.constants.Constants;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.util.CursorUtils;

/**
 * Created by Jesus Villa on 12/09/2017.
 */

public class LlamadaVta extends rp3.data.entity.EntityBase<LlamadaVta> {

    private static final String TAG = LlamadaVta.class.getSimpleName();
    private long id;
    private int IdLlamada;
    private String Descripcion;
    private String Observacion;
    private Date FechaLlamada;
    private Date FechaInicioLlamada;
    private Date FechaFinLlamada;
    private int Duracion;
    private int IdCliente;
    private int IdAgente;
    private double Latitud;
    private double Longitud;
    private int LlamadaTabla;
    private String LlamadoValue;
    private int MotivoReprogramacionTabla;
    private String MotivoReprogramacionValue;
    private int MotivoVisitaTabla;
    private String MotivoVisitaValue;
    private int ReferidoTabla;
    private String ReferidoValue;
    private int LlamadaValor; //Concreto realizar una Visita o no [0 - 1]
    private int LlamadaId; //Id de la llamada que se reprogramo
    private int Estado;
    private int Insertado;
    /**
     * 0 = Sincronizado
     * 1 = Insertado
     * 2 = Actualizado
     *
     * **/

    //region Encapsulamiento
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getIdLlamada() {
        return IdLlamada;
    }

    public void setIdLlamada(int idLlamada) {
        IdLlamada = idLlamada;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public Date getFechaLlamada() {
        return FechaLlamada;
    }

    public void setFechaLlamada(Date fechaLlamada) {
        FechaLlamada = fechaLlamada;
    }

    public Date getFechaInicioLlamada() {
        return FechaInicioLlamada;
    }

    public void setFechaInicioLlamada(Date fechaInicioLlamada) {
        FechaInicioLlamada = fechaInicioLlamada;
    }

    public Date getFechaFinLlamada() {
        return FechaFinLlamada;
    }

    public void setFechaFinLlamada(Date fechaFinLlamada) {
        FechaFinLlamada = fechaFinLlamada;
    }

    public int getDuracion() {
        return Duracion;
    }

    public void setDuracion(int duracion) {
        Duracion = duracion;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
    }

    public int getIdAgente() {
        return IdAgente;
    }

    public void setIdAgente(int idAgente) {
        IdAgente = idAgente;
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

    public int getLlamadaTabla() {
        return LlamadaTabla;
    }

    public void setLlamadaTabla(int llamadaTabla) {
        LlamadaTabla = llamadaTabla;
    }

    public String getLlamadoValue() {
        return LlamadoValue;
    }

    public void setLlamadoValue(String llamadoValue) {
        LlamadoValue = llamadoValue;
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

    public int getLlamadaValor() {
        return LlamadaValor;
    }

    public void setLlamadaValor(int llamadaValor) {
        LlamadaValor = llamadaValor;
    }

    public int getLlamadaId() {
        return LlamadaId;
    }

    public void setLlamadaId(int llamadaId) {
        LlamadaId = llamadaId;
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
        return Contract.LlamadaVta.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA, this.IdLlamada);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION, this.Descripcion);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION, this.Observacion);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA, this.FechaLlamada);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA, this.FechaInicioLlamada);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA ,this.FechaFinLlamada);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION, this.Duracion);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE, this.IdCliente);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE,this.IdAgente);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD, this.Latitud);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD, this.Longitud);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA, this.LlamadaTabla);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE, this.LlamadoValue);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA, this.MotivoReprogramacionTabla);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE, this.MotivoReprogramacionValue);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA, this.MotivoVisitaTabla);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE, this.MotivoVisitaValue);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA ,this.ReferidoTabla);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE, this.ReferidoValue);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR, this.LlamadaValor);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID, this.LlamadaId);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO,this.Estado);
        setValue(Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO, this.Insertado);
    }

    //endregion


    @Override
    public String toString() {
        return "LlamadaVta{" +
                "id=" + id +
                ", IdLlamada=" + IdLlamada +
                ", Descripcion='" + Descripcion + '\'' +
                ", Observacion='" + Observacion + '\'' +
                ", FechaLlamada=" + FechaLlamada +
                ", FechaInicioLlamada=" + FechaInicioLlamada +
                ", FechaFinLlamada=" + FechaFinLlamada +
                ", Duracion=" + Duracion +
                ", IdCliente=" + IdCliente +
                ", IdAgente=" + IdAgente +
                ", Latitud=" + Latitud +
                ", Longitud=" + Longitud +
                ", LlamadaTabla=" + LlamadaTabla +
                ", LlamadoValue='" + LlamadoValue + '\'' +
                ", MotivoReprogramacionTabla=" + MotivoReprogramacionTabla +
                ", MotivoReprogramacionValue='" + MotivoReprogramacionValue + '\'' +
                ", MotivoVisitaTabla=" + MotivoVisitaTabla +
                ", MotivoVisitaValue='" + MotivoVisitaValue + '\'' +
                ", ReferidoTabla=" + ReferidoTabla +
                ", ReferidoValue='" + ReferidoValue + '\'' +
                ", LlamadaValor=" + LlamadaValor +
                ", LlamadaId=" + LlamadaId +
                ", Estado=" + Estado +
                ", Insertado=" + Insertado +
                '}';
    }



    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static List<LlamadaVta>  getLlamadasFromProspectoBD(DataBase db,int idProspecto) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_SINCRONIZADAIDBDPROSPECTOBD);
        Cursor c = null;
        c = db.rawQuery(query, new String[] {idProspecto + ""});
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();
        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                list.add(llamadaVta);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }


    public static LlamadaVta getLlamadasFromAgenda(DataBase db,int idAgenda) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_SINCRONIZADAS_AGENDA);
        Cursor c = null;
        c = db.rawQuery(query, new String[] {idAgenda + ""});
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();

        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                list.add(llamadaVta);
                break;
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

    public static LlamadaVta getLlamadasFromAgendaBD(DataBase db,int idAgenda) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_SINCRONIZADAS_AGENDABD);
        Cursor c = null;
        c = db.rawQuery(query, new String[] {idAgenda + ""});
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();
        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                list.add(llamadaVta);
                break;
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

    public static List<LlamadaVta> getLlamadasInsert(DataBase db) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_INSERTADAS);
        //Log.d(TAG,query);
        Cursor c = db.rawQuery(query);
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();
        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                //Log.d(TAG,llamadaVta.getFechaLlamada().toString());
                //Log.d(TAG,llamadaVta.getFechaInicioLlamada().toString());
                //Log.d(TAG,llamadaVta.getFechaFinLlamada().toString());
                list.add(llamadaVta);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<LlamadaVta> getLlamadasAll(DataBase db) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_ALL);

        Cursor c = db.rawQuery(query);
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();

        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                //Log.d(TAG,llamadaVta.getFechaLlamada().toString());
                //Log.d(TAG,llamadaVta.getFechaInicioLlamada().toString());
                //Log.d(TAG,llamadaVta.getFechaFinLlamada().toString());
                list.add(llamadaVta);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<LlamadaVta> getLlamadasPendienteAll(DataBase db,int tiempoMenor) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_PENDIENTE_ALL);
        Log.d(TAG,query);
        Cursor c = db.rawQuery(query);
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();

        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                final long ONE_MINUTE_IN_MILLIS=60000;
                final Calendar hoy = Calendar.getInstance();
                final long t= calendar.getTimeInMillis();
                Date fechaAlertarLlamada = new Date(t-(tiempoMenor*ONE_MINUTE_IN_MILLIS));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(fechaAlertarLlamada);
                if(hoy.getTimeInMillis()<calendar1.getTimeInMillis()){
                    llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                    llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                    llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                    llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                    llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                    llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                    llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                    llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                    list.add(llamadaVta);
                }

            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<LlamadaVta> getLlamadasPendienteSupervisorAll(DataBase db,int tiempoMenor) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_PENDIENTE_ALL);
        Log.d(TAG,query);
        Cursor c = db.rawQuery(query);
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();

        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                final long ONE_MINUTE_IN_MILLIS=60000;
                final Calendar hoy = Calendar.getInstance();
                final long t= calendar.getTimeInMillis();
                Date fechaAlertarLlamada = new Date(t+(tiempoMenor*ONE_MINUTE_IN_MILLIS));
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(fechaAlertarLlamada);
                if(hoy.getTimeInMillis()<calendar1.getTimeInMillis()){
                    llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                    llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                    llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                    llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                    llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                    llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                    llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                    llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                    list.add(llamadaVta);
                }

            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<LlamadaVta> getLlamadasSincronizadasAll(DataBase db) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_SINCRONIZADAS);

        Cursor c = db.rawQuery(query);
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();

        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                list.add(llamadaVta);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<LlamadaVta> getLlamadasActualizadas(DataBase db) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_ACTUALIZADAS);

        Cursor c = db.rawQuery(query);
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();

        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                list.add(llamadaVta);
            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static LlamadaVta getLlamadaId(DataBase db,int i) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_ALL);
        //Log.d(TAG,"query:"+query);
        Cursor c = db.rawQuery(query);
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();
        LlamadaVta llamadaVta = null;
        if(c.moveToFirst()){
            do
            {
                if(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA) == i){
                     llamadaVta =  new LlamadaVta();
                    llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                    llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                    llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                    llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                    llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                    llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                    llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                    llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
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

    public static LlamadaVta getLlamadaIdBD(DataBase db,int i) {
        String query = QueryDir.getQuery(Contract.LlamadaVta.QUERY_LLAMADAVTA_INSERTADAS);
        Log.d(TAG,"query:"+query);
        Cursor c = db.rawQuery(query);
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();
        LlamadaVta llamadaVta = null;
        if(c.moveToFirst()){
            do
            {
                if(CursorUtils.getInt(c,Contract.LlamadaVta._ID)==i){
                    llamadaVta =  new LlamadaVta();
                    llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                    llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                    llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                    llamadaVta.setObservacion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_OBSERVACION));
                    llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                    llamadaVta.setFechaInicioLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_INICIO_LLAMADA));
                    llamadaVta.setFechaFinLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_FIN_LLAMADA));
                    llamadaVta.setDuracion(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DURACION));
                    llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                    llamadaVta.setIdAgente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDAGENTE));
                    llamadaVta.setLatitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LATITUD));
                    llamadaVta.setLongitud(CursorUtils.getFloat(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_LONGITUD));
                    llamadaVta.setLlamadaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_TABLA));
                    llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                    llamadaVta.setMotivoReprogramacionTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_TABLA));
                    llamadaVta.setMotivoReprogramacionValue(CursorUtils.getString(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_REPROGRAMACION_VALUE));
                    llamadaVta.setMotivoVisitaTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_TABLA));
                    llamadaVta.setMotivoVisitaValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_MOTIVO_VISITA_VALUE));
                    llamadaVta.setReferidoTabla(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_TABLA));
                    llamadaVta.setReferidoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_REFERIDO_VALUE));
                    llamadaVta.setLlamadaValor(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALOR));
                    llamadaVta.setLlamadaId(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_LLAMADAID));
                    llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                    llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
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

    public static List<LlamadaVta> getLlamadasMinutes(DataBase db,long minutes_max, long minutes_min){
        long actual = Calendar.getInstance().getTimeInMillis();
        /*Cursor c = db.query(Contract.LlamadaVta.TABLE_NAME, new String[]{
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_ID,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO},
                "(" + Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " - " + actual + ") <= " + minutes_max + " AND " +
                        "(" + Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " - " + actual + ") >= " + minutes_min + " AND " +
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE +
                        " = '"+ Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE+"'",
                null, null, null, Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " ASC");*/
        Cursor c = db.query(Contract.LlamadaVta.TABLE_NAME, new String[]{
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_ID,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO},
                //"(" + Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " - " + actual + ") <= " + minutes_max + " AND " +
                        //"(" + Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " - " + actual + ") >= " + minutes_min + " AND " +
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE +
                        " = '"+ Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE+"'",
                null, null, null, Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " ASC");
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();

        Calendar now = Calendar.getInstance();

        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                Date date = llamadaVta.getFechaLlamada();
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(date);
                long ONE_MINUTE_IN_MILLIS=60000;
                long t= fecha.getTimeInMillis();
                Date beforeFechaLlamada = new Date(t-(5*ONE_MINUTE_IN_MILLIS));
                Calendar before = Calendar.getInstance();
                before.setTime(beforeFechaLlamada);

                Date b = llamadaVta.getFechaLlamada();//fechaLlamada
                Date a = beforeFechaLlamada;
                Date d= now.getTime();
                boolean sameTime = d.compareTo(a) >= 0 && d.compareTo(b) <= 0;
                if(sameTime){
                    list.add(llamadaVta);
                }

            }while(c.moveToNext());
        }
        c.close();
        return list;
    }

    public static List<LlamadaVta> getLlamadasAllCancel(DataBase db) {
        long actual = Calendar.getInstance().getTimeInMillis();
        /*Cursor c = db.query(Contract.LlamadaVta.TABLE_NAME, new String[]{
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_ID,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO},
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " > " + actual+ " AND " +
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE +
                        " = '"+ Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE+"'",
                null, null, null, Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " ASC");*/
        Cursor c = db.query(Contract.LlamadaVta.TABLE_NAME, new String[]{
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_ID,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO,
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO},
                //Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " > " + actual+ " AND " +
                        Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE +
                        " = '"+ Contants.GENERAL_VALUE_CODE_LLAMADA_PENDIENTE+"'",
                null, null, null, Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA + " ASC");
        List<LlamadaVta> list = new ArrayList<LlamadaVta>();

        if(c.moveToFirst()){
            do
            {
                LlamadaVta llamadaVta =  new LlamadaVta();
                llamadaVta.setId(CursorUtils.getInt(c,Contract.LlamadaVta._ID));
                llamadaVta.setIdLlamada(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDLLAMADA));
                llamadaVta.setDescripcion(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_DESCRIPCION));
                llamadaVta.setFechaLlamada(CursorUtils.getDate(c,Contract.LlamadaVta.COLUMN_LLAMADAVTA_FECHA_LLAMADA));
                llamadaVta.setIdCliente(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_IDCLIENTE));
                llamadaVta.setLlamadoValue(CursorUtils.getString(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_LLAMADA_VALUE));
                llamadaVta.setEstado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_ESTADO));
                llamadaVta.setInsertado(CursorUtils.getInt(c, Contract.LlamadaVta.COLUMN_LLAMADAVTA_INSERTADO));
                Date fechaLlamada = llamadaVta.getFechaLlamada();
                Calendar now1 = Calendar.getInstance();
                Calendar hoy = Calendar.getInstance();

                Date now = now1.getTime();
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

    public static void actualizarInsertados(DataBase db) {
        db.execSQL(Contract.LlamadaVta.QUERY_LLAMADAVTA_UPDATE_TO_INSERTADAS);
    }

    public static void deleteLlamadas(DataBase db) {
        Log.d("LlamadaEntity","deleteLlamadas...");
        db.execSQL(Contract.LlamadaVta.QUERY_DELETE);
    }

    public static int getMaxIdAgendaVta(DataBase db){
        String query = Contract.LlamadaVta.QUERY_GET_MAX_ID;
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
            Log.d("Llamada models","Exception:"+ e.getMessage());
            return -1;
        }
    }

    public static int getMaxIdLlamadaVta(DataBase db){
        String query = Contract.LlamadaVta.QUERY_GET_MAX_IDLLAMADA;
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
            Log.d("Llamada models","Exception:"+ e.getMessage());
            return -1;
        }
    }

}
