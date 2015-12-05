package rp3.marketforce.models.oportunidad;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.Contants;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.Agente;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.oportunidad.FiltroOportunidadFragment;
import rp3.util.Convert;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 14/05/2015.
 */
public class Oportunidad extends EntityBase<Oportunidad> {

    private long id;
    private int idOportunidad;
    private int probabilidad;
    private double importe;
    private int idAgente;
    private Date fechaUltimaGestion;
    private int calificacion;
    private String observacion;
    private double latitud;
    private double longitud;
    private int idEtapa;
    private String estado;
    private Date fechaCreacion;
    private boolean pendiente;
    private String direccion;
    private String referencia;
    private String descripcion;
    private String direccionReferencia;
    private String tipoEmpresa;
    private String telefono1;
    private String telefono2;
    private String correo;
    private String paginaWeb;
    private int idOportunidadTipo;

    private List<OportunidadContacto> oportunidadContactos;
    private List<OportunidadResponsable> oportunidadResponsables;
    private List<OportunidadTarea> oportunidadTareas;
    private List<OportunidadFoto> oportunidadFotos;
    private List<OportunidadEtapa> oportunidadEtapas;
    private List<OportunidadBitacora> oportunidadBitacoras;
    private Etapa etapa;
    private Agente agente;

    private int maxEtapas;


    @Override
    public long getID() {
        return id;
    }

    @Override
    public void setID(long id) {
        this.id = id;
    }

    public int getIdOportunidad() {
        return idOportunidad;
    }

    public void setIdOportunidad(int idOportunidad) {
        this.idOportunidad = idOportunidad;
    }

    public int getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(int probabilidad) {
        this.probabilidad = probabilidad;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public Date getFechaUltimaGestion() {
        return fechaUltimaGestion;
    }

    public void setFechaUltimaGestion(Date fechaUltimaGestion) {
        this.fechaUltimaGestion = fechaUltimaGestion;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(int idEtapa) {
        this.idEtapa = idEtapa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getMaxEtapas() {
        return maxEtapas;
    }

    public void setMaxEtapas(int maxEtapas) {
        this.maxEtapas = maxEtapas;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isPendiente() {
        return pendiente;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccionReferencia() {
        return direccionReferencia;
    }

    public void setDireccionReferencia(String direccionReferencia) {
        this.direccionReferencia = direccionReferencia;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public int getIdOportunidadTipo() {
        return idOportunidadTipo;
    }

    public void setIdOportunidadTipo(int idOportunidadTipo) {
        this.idOportunidadTipo = idOportunidadTipo;
    }

    public List<OportunidadContacto> getOportunidadContactos() {
        return oportunidadContactos;
    }

    public void setOportunidadContactos(List<OportunidadContacto> oportunidadContactos) {
        this.oportunidadContactos = oportunidadContactos;
    }

    public List<OportunidadResponsable> getOportunidadResponsables() {
        return oportunidadResponsables;
    }

    public void setOportunidadResponsables(List<OportunidadResponsable> oportunidadResponsables) {
        this.oportunidadResponsables = oportunidadResponsables;
    }

    public List<OportunidadTarea> getOportunidadTareas() {
        return oportunidadTareas;
    }

    public void setOportunidadTareas(List<OportunidadTarea> oportunidadTareas) {
        this.oportunidadTareas = oportunidadTareas;
    }

    public List<OportunidadFoto> getOportunidadFotos() {
        return oportunidadFotos;
    }

    public void setOportunidadFotos(List<OportunidadFoto> oportunidadFotos) {
        this.oportunidadFotos = oportunidadFotos;
    }

    public List<OportunidadEtapa> getOportunidadEtapas() {
        return oportunidadEtapas;
    }

    public void setOportunidadEtapas(List<OportunidadEtapa> oportunidadEtapas) {
        this.oportunidadEtapas = oportunidadEtapas;
    }

    public List<OportunidadBitacora> getOportunidadBitacoras() {
        return oportunidadBitacoras;
    }

    public void setOportunidadBitacoras(List<OportunidadBitacora> oportunidadBitacoras) {
        this.oportunidadBitacoras = oportunidadBitacoras;
    }

    public Agente getAgente() {
        return agente;
    }

    public void setAgente(Agente agente) {
        this.agente = agente;
    }

    @Override
    public boolean isAutoGeneratedId() {
        return true;
    }

    @Override
    public String getTableName() {
        return Contract.Oportunidad.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD, this.idOportunidad);
        setValue(Contract.Oportunidad.COLUMN_CALIFICACION, this.calificacion);
        setValue(Contract.Oportunidad.COLUMN_ESTADO, this.estado);
        setValue(Contract.Oportunidad.COLUMN_FECHA_CREACION, this.fechaCreacion);
        setValue(Contract.Oportunidad.COLUMN_FECHA_ULTIMA_GESTION, this.fechaUltimaGestion);
        setValue(Contract.Oportunidad.COLUMN_ID_AGENTE, this.idAgente);
        setValue(Contract.Oportunidad.COLUMN_ID_ETAPA, this.idEtapa);
        setValue(Contract.Oportunidad.COLUMN_IMPORTE, this.importe);
        setValue(Contract.Oportunidad.COLUMN_LATITUD, this.latitud);
        setValue(Contract.Oportunidad.COLUMN_LONGITUD, this.longitud);
        setValue(Contract.Oportunidad.COLUMN_PROBABILIDAD, this.probabilidad);
        setValue(Contract.Oportunidad.COLUMN_OBSERVACION, this.observacion);
        setValue(Contract.Oportunidad.COLUMN_PENDIENTE, this.pendiente);
        setValue(Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD_TIPO, this.idOportunidadTipo);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public static List<Oportunidad> getOportunidades(DataBase db){
        String query = QueryDir.getQuery(Contract.Oportunidad.QUERY_LIST_NO_FILTER);

        Cursor c = db.rawQuery(query);

        List<Oportunidad> list = new ArrayList<Oportunidad>();
        while(c.moveToNext()){

            Oportunidad opt = new Oportunidad();
            opt.setID(CursorUtils.getLong(c, Contract.Oportunidad._ID));
            opt.setIdOportunidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD));
            opt.setIdEtapa(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_ETAPA));
            opt.setCalificacion(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_CALIFICACION));
            opt.setEstado(CursorUtils.getString(c, Contract.Oportunidad.FIELD_ESTADO));
            opt.setDescripcion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DESCRIPCION));
            opt.setDireccion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION));
            opt.setFechaCreacion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_CREACION));
            opt.setFechaUltimaGestion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_ULTIMA_GESTION));
            opt.setIdAgente(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_AGENTE));
            opt.setImporte(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_IMPORTE));
            opt.setLatitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LATITUD));
            opt.setLongitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LONGITUD));
            opt.setObservacion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_OBSERVACION));
            opt.setPendiente(CursorUtils.getBoolean(c, Contract.Oportunidad.FIELD_PENDIENTE));
            opt.setProbabilidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_PROBABILIDAD));
            opt.setReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_REFERENCIA));
            opt.setDireccionReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION_REFERENCIA));
            opt.setCorreo(CursorUtils.getString(c, Contract.Oportunidad.FIELD_CORREO));
            opt.setTipoEmpresa(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TIPO_EMPRESA));
            opt.setPaginaWeb(CursorUtils.getString(c, Contract.Oportunidad.FIELD_PAGINA_WEB));
            opt.setTelefono1(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO1));
            opt.setTelefono2(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO2));
            opt.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD_TIPO));
            opt.setEtapa(Etapa.getEtapaById(db, opt.getIdEtapa()));
            opt.setAgente(Agente.getAgente(db, opt.getIdAgente()));
            opt.setMaxEtapas(Etapa.getEtapasPadres(db, opt.getIdOportunidadTipo()));
            list.add(opt);
        }
        c.close();
        return list;
    }

    public static List<Oportunidad> getOportunidadesSearch(DataBase db, String text){
        String query = QueryDir.getQuery(Contract.Oportunidad.QUERY_SEARCH);
        String version = db.getSQLiteVersion();
        int compare = Convert.versionCompare(version, Contants.SQLITE_VERSION_SEARCH);
        Cursor c = null;

        if(compare > 0)
            c = db.rawQuery(query, text + "*");
        else
            c = db.rawQuery(query, "'*" + text + "*'");

        List<Oportunidad> list = new ArrayList<Oportunidad>();
        while(c.moveToNext()){

            Oportunidad opt = new Oportunidad();
            opt.setID(CursorUtils.getLong(c, Contract.Oportunidad._ID));
            opt.setIdOportunidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD));
            opt.setIdEtapa(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_ETAPA));
            opt.setCalificacion(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_CALIFICACION));
            opt.setEstado(CursorUtils.getString(c, Contract.Oportunidad.FIELD_ESTADO));
            opt.setDescripcion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DESCRIPCION));
            opt.setDireccion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION));
            opt.setFechaCreacion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_CREACION));
            opt.setFechaUltimaGestion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_ULTIMA_GESTION));
            opt.setIdAgente(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_AGENTE));
            opt.setImporte(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_IMPORTE));
            opt.setLatitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LATITUD));
            opt.setLongitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LONGITUD));
            opt.setObservacion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_OBSERVACION));
            opt.setPendiente(CursorUtils.getBoolean(c, Contract.Oportunidad.FIELD_PENDIENTE));
            opt.setProbabilidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_PROBABILIDAD));
            opt.setReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_REFERENCIA));
            opt.setDireccionReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION_REFERENCIA));
            opt.setCorreo(CursorUtils.getString(c, Contract.Oportunidad.FIELD_CORREO));
            opt.setTipoEmpresa(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TIPO_EMPRESA));
            opt.setPaginaWeb(CursorUtils.getString(c, Contract.Oportunidad.FIELD_PAGINA_WEB));
            opt.setTelefono1(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO1));
            opt.setTelefono2(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO2));
            opt.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD_TIPO));
            opt.setEtapa(Etapa.getEtapaById(db, opt.getIdEtapa()));
            opt.setAgente(Agente.getAgente(db, opt.getIdAgente()));
            opt.setMaxEtapas(Etapa.getEtapasPadres(db, opt.getIdOportunidadTipo()));
            list.add(opt);
        }
        c.close();
        return list;
    }

    public static List<Oportunidad> getOportunidadesFiltro(DataBase db, Intent intent){
        String condition = "", etapas_cond = "", tipos_cond = "", estados_cond = "";

        Bundle bundle = intent.getBundleExtra(FiltroOportunidadFragment.FILTRO);

        ArrayList<Integer> etapas_raw = bundle.getIntegerArrayList(FiltroOportunidadFragment.ETAPAS);
        ArrayList<Integer> tipos_raw = bundle.getIntegerArrayList(FiltroOportunidadFragment.TIPOS);
        ArrayList<String> estados_raw = bundle.getStringArrayList(FiltroOportunidadFragment.ESTADOS);

        for(int i = 0; i < tipos_raw.size(); i ++) {
            if (tipos_cond.length() == 0)
                tipos_cond = " AND (";
            else
                tipos_cond = tipos_cond + " OR ";
            tipos_cond = tipos_cond + Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD_TIPO + " = " + tipos_raw.get(i);
        }
        if(tipos_cond.length() > 0)
            tipos_cond = tipos_cond + ")";

        for(int i = 0; i < etapas_raw.size(); i ++) {
            if (etapas_cond.length() == 0)
                etapas_cond = " AND (";
            else
                etapas_cond = etapas_cond + " OR ";
            etapas_cond = etapas_cond + Contract.Oportunidad.COLUMN_ID_ETAPA + " = " + etapas_raw.get(i);
        }
        if(etapas_cond.length() > 0)
            etapas_cond = etapas_cond + ")";

        for(int i = 0; i < estados_raw.size(); i ++) {

            if (estados_cond.length() == 0)
                estados_cond = " (";
            else
                estados_cond = estados_cond + " OR ";
            estados_cond = estados_cond + Contract.Oportunidad.COLUMN_ESTADO + " = '" + estados_raw.get(i) + "'";

        }
        if(estados_cond.length() > 0)
            estados_cond = estados_cond + ")";

        condition = estados_cond + etapas_cond + tipos_cond;

        if(bundle.containsKey(FiltroOportunidadFragment.DESDE_CANTIDAD))
            condition = condition + " AND " + Contract.Oportunidad.COLUMN_IMPORTE + " >= " + bundle.getDouble(FiltroOportunidadFragment.DESDE_CANTIDAD);
        if(bundle.containsKey(FiltroOportunidadFragment.HASTA_CANTIDAD))
            condition = condition + " AND " + Contract.Oportunidad.COLUMN_IMPORTE + " <= " + bundle.getDouble(FiltroOportunidadFragment.HASTA_CANTIDAD);
        if(bundle.containsKey(FiltroOportunidadFragment.DESDE_CREACION))
            condition = condition + " AND " + Contract.Oportunidad.COLUMN_FECHA_CREACION + " >= " + bundle.getLong(FiltroOportunidadFragment.DESDE_CREACION);
        if(bundle.containsKey(FiltroOportunidadFragment.HASTA_CREACION))
            condition = condition + " AND " + Contract.Oportunidad.COLUMN_FECHA_CREACION + " <= " + bundle.getLong(FiltroOportunidadFragment.HASTA_CREACION);
        /*if(bundle.containsKey(FiltroOportunidadFragment.DESDE_GESTION))
            condition = condition + " AND " + Contract.Oportunidad.COLUMN_FECHA_ULTIMA_GESTION + " >= " + bundle.getDouble(FiltroOportunidadFragment.DESDE_GESTION);
        if(bundle.containsKey(FiltroOportunidadFragment.HASTA_GESTION))
            condition = condition + " AND " + Contract.Oportunidad.COLUMN_FECHA_ULTIMA_GESTION + " <= " + bundle.getDouble(FiltroOportunidadFragment.HASTA_GESTION);*/

        condition = condition + " AND " + Contract.Oportunidad.COLUMN_CALIFICACION + " <= " + bundle.getInt(FiltroOportunidadFragment.IMPORTANCIA_MAX);
        condition = condition + " AND " + Contract.Oportunidad.COLUMN_CALIFICACION + " >= " + bundle.getInt(FiltroOportunidadFragment.IMPORTANCIA_MIN);
        condition = condition + " AND " + Contract.Oportunidad.COLUMN_PROBABILIDAD + " <= " + bundle.getInt(FiltroOportunidadFragment.PROBABILIDAD_MAX);
        condition = condition + " AND " + Contract.Oportunidad.COLUMN_PROBABILIDAD + " >= " + bundle.getInt(FiltroOportunidadFragment.PROBABILIDAD_MIN);


        Cursor c = db.query(Contract.Oportunidad.TABLE_NAME, new String[] {Contract.Oportunidad._ID}, condition, new String[]{});

        List<Long> list = new ArrayList<Long>();
        while(c.moveToNext()){
            list.add(CursorUtils.getLong(c, Contract.Oportunidad._ID));
        }
        c.close();

        return getOportunidadesByIds(db, list);
    }

    public static List<Oportunidad> getOportunidadesByIds(DataBase db, List<Long> ids){
        String query = QueryDir.getQuery(Contract.Oportunidad.QUERY_LIST_BY_IDS);
        String ids_cond = "";
        for(int i = 0; i < ids.size(); i++)
        {
            if(i != ids.size() -1)
                ids_cond = ids_cond + ids.get(i) + ", ";
            else
                ids_cond = ids_cond + ids.get(i);
        }
        query = query.replace("?", ids_cond);
        Cursor c = db.rawQuery(query);

        List<Oportunidad> list = new ArrayList<Oportunidad>();
        while(c.moveToNext()){

            Oportunidad opt = new Oportunidad();
            opt.setID(CursorUtils.getLong(c, Contract.Oportunidad._ID));
            opt.setIdOportunidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD));
            opt.setIdEtapa(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_ETAPA));
            opt.setCalificacion(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_CALIFICACION));
            opt.setEstado(CursorUtils.getString(c, Contract.Oportunidad.FIELD_ESTADO));
            opt.setDescripcion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DESCRIPCION));
            opt.setDireccion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION));
            opt.setFechaCreacion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_CREACION));
            opt.setFechaUltimaGestion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_ULTIMA_GESTION));
            opt.setIdAgente(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_AGENTE));
            opt.setImporte(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_IMPORTE));
            opt.setLatitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LATITUD));
            opt.setLongitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LONGITUD));
            opt.setObservacion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_OBSERVACION));
            opt.setPendiente(CursorUtils.getBoolean(c, Contract.Oportunidad.FIELD_PENDIENTE));
            opt.setProbabilidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_PROBABILIDAD));
            opt.setReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_REFERENCIA));
            opt.setDireccionReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION_REFERENCIA));
            opt.setCorreo(CursorUtils.getString(c, Contract.Oportunidad.FIELD_CORREO));
            opt.setTipoEmpresa(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TIPO_EMPRESA));
            opt.setPaginaWeb(CursorUtils.getString(c, Contract.Oportunidad.FIELD_PAGINA_WEB));
            opt.setTelefono1(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO1));
            opt.setTelefono2(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO2));
            opt.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD_TIPO));
            opt.setEtapa(Etapa.getEtapaById(db, opt.getIdEtapa()));
            opt.setAgente(Agente.getAgente(db, opt.getIdAgente()));
            list.add(opt);
        }
        c.close();
        return list;
    }

    protected boolean insertDb(DataBase db) {
        boolean result = false;

        try
        {
            result = super.insertDb(db);
            if(this.id == 0)
            {
                this.id = db.queryMaxLong(Contract.Oportunidad.TABLE_NAME, Contract.Oportunidad._ID);
            }

            if(result){
                if(result)
                {
                    OportunidadExt cl_ex = new OportunidadExt();
                    result = OportunidadExt.insert(db, cl_ex);
                }
            }

        }catch(Exception ex){
            result = false;
            Log.e("Oportunidad Insert", ex.getMessage());
        }finally{
        }
        return result;
    }

    protected boolean updateDb(DataBase db) {
        boolean result = super.updateDb(db);

        if(result)
        {
            OportunidadExt cl_ex = new OportunidadExt();
            result = OportunidadExt.update(db, cl_ex);
        }
        return result;
    }

    public static Oportunidad getOportunidadId(DataBase db, long opId) {
        String query = QueryDir.getQuery(Contract.Oportunidad.QUERY_OPORTUNIDAD_BY_ID);
        Cursor c = db.rawQuery(query,""+opId);

        Oportunidad opt = new Oportunidad();
        while(c.moveToNext()){

            opt.setID(CursorUtils.getLong(c, Contract.Oportunidad._ID));
            opt.setIdOportunidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD));
            opt.setIdEtapa(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_ETAPA));
            opt.setCalificacion(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_CALIFICACION));
            opt.setEstado(CursorUtils.getString(c, Contract.Oportunidad.FIELD_ESTADO));
            opt.setDescripcion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DESCRIPCION));
            opt.setDireccion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION));
            opt.setFechaCreacion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_CREACION));
            opt.setFechaUltimaGestion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_ULTIMA_GESTION));
            opt.setIdAgente(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_AGENTE));
            opt.setImporte(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_IMPORTE));
            opt.setLatitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LATITUD));
            opt.setLongitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LONGITUD));
            opt.setObservacion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_OBSERVACION));
            opt.setPendiente(CursorUtils.getBoolean(c, Contract.Oportunidad.FIELD_PENDIENTE));
            opt.setProbabilidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_PROBABILIDAD));
            opt.setReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_REFERENCIA));
            opt.setDireccionReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION_REFERENCIA));
            opt.setCorreo(CursorUtils.getString(c, Contract.Oportunidad.FIELD_CORREO));
            opt.setTipoEmpresa(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TIPO_EMPRESA));
            opt.setPaginaWeb(CursorUtils.getString(c, Contract.Oportunidad.FIELD_PAGINA_WEB));
            opt.setTelefono1(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO1));
            opt.setTelefono2(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO2));
            opt.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD_TIPO));

            if(opt.getIdOportunidad() != 0) {
                opt.setOportunidadContactos(OportunidadContacto.getContactosOportunidad(db, opt.getIdOportunidad()));
                opt.setOportunidadResponsables(OportunidadResponsable.getResponsablesOportunidad(db, opt.getIdOportunidad()));
                opt.setOportunidadTareas(OportunidadTarea.getTareasOportunidad(db, opt.getIdOportunidad()));
                opt.setOportunidadFotos(OportunidadFoto.getFotos(db, opt.getIdOportunidad()));
                opt.setOportunidadEtapas(OportunidadEtapa.getEtapasOportunidad(db, opt.getIdOportunidad()));
                opt.setOportunidadBitacoras(OportunidadBitacora.getBitacoraOportunidad(db, opt.getIdOportunidad()));
            }
            else
            {
                opt.setOportunidadContactos(OportunidadContacto.getContactosOportunidadInt(db, opt.getID()));
                opt.setOportunidadResponsables(OportunidadResponsable.getResponsablesOportunidadInt(db, opt.getID()));
                opt.setOportunidadTareas(OportunidadTarea.getTareasOportunidadInt(db, opt.getID()));
                opt.setOportunidadFotos(OportunidadFoto.getFotosInt(db, opt.getID()));
                opt.setOportunidadEtapas(OportunidadEtapa.getEtapasOportunidadInt(db, opt.getID()));
                opt.setOportunidadBitacoras(OportunidadBitacora.getBitacoraOportunidadInt(db, opt.getID()));
            }

        }
        c.close();
        return opt;
    }

    public static List<Oportunidad> getOportunidadesPendientes(DataBase db) {
        String query = QueryDir.getQuery( Contract.Oportunidad.QUERY_OPORTUNIDADES_PENDIENTES );
        Cursor c = db.rawQuery(query);

        List<Oportunidad> oportunidades = new ArrayList<Oportunidad>();
        while(c.moveToNext()){
            Oportunidad opt = new Oportunidad();
            opt.setID(CursorUtils.getLong(c, Contract.Oportunidad._ID));
            opt.setIdOportunidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD));
            opt.setIdEtapa(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_ETAPA));
            opt.setCalificacion(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_CALIFICACION));
            opt.setEstado(CursorUtils.getString(c, Contract.Oportunidad.FIELD_ESTADO));
            opt.setDescripcion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DESCRIPCION));
            opt.setDireccion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION));
            opt.setFechaCreacion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_CREACION));
            opt.setFechaUltimaGestion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_ULTIMA_GESTION));
            opt.setIdAgente(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_AGENTE));
            opt.setImporte(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_IMPORTE));
            opt.setLatitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LATITUD));
            opt.setLongitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LONGITUD));
            opt.setObservacion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_OBSERVACION));
            opt.setPendiente(CursorUtils.getBoolean(c, Contract.Oportunidad.FIELD_PENDIENTE));
            opt.setProbabilidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_PROBABILIDAD));
            opt.setReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_REFERENCIA));
            opt.setDireccionReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION_REFERENCIA));
            opt.setCorreo(CursorUtils.getString(c, Contract.Oportunidad.FIELD_CORREO));
            opt.setTipoEmpresa(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TIPO_EMPRESA));
            opt.setPaginaWeb(CursorUtils.getString(c, Contract.Oportunidad.FIELD_PAGINA_WEB));
            opt.setTelefono1(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO1));
            opt.setTelefono2(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO2));
            opt.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD_TIPO));

            opt.setOportunidadContactos(OportunidadContacto.getContactosOportunidad(db, opt.getIdOportunidad()));
            opt.setOportunidadResponsables(OportunidadResponsable.getResponsablesOportunidad(db, opt.getIdOportunidad()));
            opt.setOportunidadTareas(OportunidadTarea.getTareasOportunidad(db, opt.getIdOportunidad()));
            opt.setOportunidadFotos(OportunidadFoto.getFotos(db, opt.getIdOportunidad()));
            opt.setOportunidadEtapas(OportunidadEtapa.getEtapasOportunidad(db, opt.getIdOportunidad()));
            opt.setOportunidadBitacoras(OportunidadBitacora.getBitacoraOportunidad(db, opt.getIdOportunidad()));
            oportunidades.add(opt);
        }
        c.close();
        return oportunidades;
    }

    public static List<Oportunidad> getOportunidadesInserts(DataBase db) {
        String query = QueryDir.getQuery(Contract.Oportunidad.QUERY_OPORTUNIDADES_INSERTS);
        Cursor c = db.rawQuery(query);

        List<Oportunidad> oportunidades = new ArrayList<Oportunidad>();
        while(c.moveToNext()){
            Oportunidad opt = new Oportunidad();

            opt.setID(CursorUtils.getLong(c, Contract.Oportunidad._ID));
            opt.setIdOportunidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD));
            opt.setIdEtapa(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_ETAPA));
            opt.setCalificacion(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_CALIFICACION));
            opt.setEstado(CursorUtils.getString(c, Contract.Oportunidad.FIELD_ESTADO));
            opt.setDescripcion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DESCRIPCION));
            opt.setDireccion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION));
            opt.setFechaCreacion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_CREACION));
            opt.setFechaUltimaGestion(CursorUtils.getDate(c, Contract.Oportunidad.FIELD_FECHA_ULTIMA_GESTION));
            opt.setIdAgente(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_AGENTE));
            opt.setImporte(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_IMPORTE));
            opt.setLatitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LATITUD));
            opt.setLongitud(CursorUtils.getDouble(c, Contract.Oportunidad.FIELD_LONGITUD));
            opt.setObservacion(CursorUtils.getString(c, Contract.Oportunidad.FIELD_OBSERVACION));
            opt.setPendiente(CursorUtils.getBoolean(c, Contract.Oportunidad.FIELD_PENDIENTE));
            opt.setProbabilidad(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_PROBABILIDAD));
            opt.setReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_REFERENCIA));
            opt.setDireccionReferencia(CursorUtils.getString(c, Contract.Oportunidad.FIELD_DIRECCION_REFERENCIA));
            opt.setCorreo(CursorUtils.getString(c, Contract.Oportunidad.FIELD_CORREO));
            opt.setTipoEmpresa(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TIPO_EMPRESA));
            opt.setPaginaWeb(CursorUtils.getString(c, Contract.Oportunidad.FIELD_PAGINA_WEB));
            opt.setTelefono1(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO1));
            opt.setTelefono2(CursorUtils.getString(c, Contract.Oportunidad.FIELD_TELEFONO2));
            opt.setIdOportunidadTipo(CursorUtils.getInt(c, Contract.Oportunidad.FIELD_ID_OPORTUNIDAD_TIPO));

            opt.setOportunidadContactos(OportunidadContacto.getContactosOportunidadInt(db, opt.getID()));
            opt.setOportunidadResponsables(OportunidadResponsable.getResponsablesOportunidadInt(db, opt.getID()));
            opt.setOportunidadTareas(OportunidadTarea.getTareasOportunidadInt(db, opt.getID()));
            opt.setOportunidadFotos(OportunidadFoto.getFotosInt(db, opt.getID()));
            opt.setOportunidadEtapas(OportunidadEtapa.getEtapasOportunidadInt(db, opt.getID()));
            opt.setOportunidadBitacoras(OportunidadBitacora.getBitacoraOportunidadInt(db, opt.getID()));

            oportunidades.add(opt);
        }
        c.close();
        return oportunidades;
    }

    public static boolean existOportunidadServer(DataBase db, int idServer)
    {
        Cursor c = db.query(Contract.Oportunidad.TABLE_NAME, Contract.Oportunidad._ID, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer + "");
        if(c.moveToFirst())
            return true;
        else
            return false;
    }

    public static Oportunidad getOportunidadIdServer(DataBase db, int idServer)
    {
        Cursor c = db.query(Contract.Oportunidad.TABLE_NAME, Contract.Oportunidad._ID, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer + "");
        Oportunidad opt = new Oportunidad();
        if(c.moveToFirst())
        {
            opt.setID(CursorUtils.getInt(c, Contract.Oportunidad._ID));
        }
        return opt;

    }

    public static void deleteOportunidadIdServer(DataBase db, int idServer)
    {
        //Oportunidad opt = Oportunidad.getOportunidadIdServer(db, idServer);
        //db.delete(Contract.Oportunidad.TABLE_NAME, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer);
        db.delete(Contract.OportunidadResponsable.TABLE_NAME, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer);
        db.delete(Contract.OportunidadTarea.TABLE_NAME, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer);
        db.delete(Contract.OportunidadTareaActividad.TABLE_NAME, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer);
        db.delete(Contract.OportunidadEtapa.TABLE_NAME, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer);
        db.delete(Contract.OportunidadContacto.TABLE_NAME, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer);
        db.delete(Contract.OportunidadBitacora.TABLE_NAME, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer);
        db.delete(Contract.OportunidadFoto.TABLE_NAME, Contract.Oportunidad.COLUMN_ID_OPORTUNIDAD + " = ?", idServer);
        /*if(opt.getID() != 0)
        {
            db.delete(Contract.OportunidadExt.TABLE_NAME, Contract.OportunidadExt.COLUMN_ID + " = ?", opt.getID());
        }*/
    }

    public class OportunidadExt extends EntityBase<OportunidadExt>{

        @Override
        public long getID() {
            return id;
        }

        @Override
        public void setID(long idext) {
            id = idext;
        }

        @Override
        public boolean isAutoGeneratedId() {
            return false;
        }

        @Override
        public String getTableName() {
            return Contract.OportunidadExt.TABLE_NAME;
        }

        @Override
        public void setValues() {
            if(getAction() == ACTION_INSERT){
                setValue(Contract.OportunidadExt.COLUMN_ID , id);
            }
            setValue(Contract.OportunidadExt.COLUMN_DESCRIPCION, descripcion  );
            setValue(Contract.OportunidadExt.COLUMN_DIRECCION, direccion);
            setValue(Contract.OportunidadExt.COLUMN_REFERENCIA, referencia);
            setValue(Contract.OportunidadExt.COLUMN_CORREO, correo  );
            setValue(Contract.OportunidadExt.COLUMN_DIRECCION_REFERENCIA, direccionReferencia);
            setValue(Contract.OportunidadExt.COLUMN_PAGINA_WEB, paginaWeb);
            setValue(Contract.OportunidadExt.COLUMN_TELEFONO1, telefono1  );
            setValue(Contract.OportunidadExt.COLUMN_TELEFONO2, telefono2);
            setValue(Contract.OportunidadExt.COLUMN_TIPO_EMPRESA, tipoEmpresa);
        }

        @Override
        public Object getValue(String key) {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String getWhere() {
            return Contract.OportunidadExt.COLUMN_ID + " = ?";
        }
    }
}
