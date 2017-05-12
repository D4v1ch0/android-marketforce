package rp3.marketforce.models.pedido;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.QueryDir;
import rp3.db.sqlite.DataBase;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.util.CursorUtils;

/**
 * Created by magno_000 on 19/10/2015.
 */
public class Producto extends rp3.data.entity.EntityBase<Producto>{

    private long id;
    private int idProducto;
    private int idSubCategoria;
    private double valorUnitario;
    private String descripcion;
    private String urlFoto;
    private String codigoExterno;
    private float precioImpuesto;
    private float precioDescuento;
    private float porcentajeImpuesto;
    private float porcentajeDescuento;
    private float porcentajeDescuentoOro;
    private int idBeneficio;
    private String aplicacion;
    private String grupoComision;
    private String linea;
    private String familia;
    private String aviso;

    private String idExterno2;
    private String grupoEstadistico;


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
        return Contract.Producto.TABLE_NAME;
    }

    @Override
    public void setValues() {
        setValue(Contract.Producto.COLUMN_ID_PRODUCTO, this.idProducto);
        setValue(Contract.Producto.COLUMN_ID_SUBCATEGORIA, this.idSubCategoria);
        setValue(Contract.Producto.COLUMN_VALOR_UNITARIO, this.valorUnitario);
        setValue(Contract.Producto.COLUMN_URL_FOTO, this.urlFoto);
        setValue(Contract.Producto.COLUMN_CODIGO_EXTERNO, this.codigoExterno);
        setValue(Contract.Producto.COLUMN_PRECIO_IMPUESTO, this.precioImpuesto);
        setValue(Contract.Producto.COLUMN_PRECIO_DESCUENTO, this.precioDescuento);
        setValue(Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO, this.porcentajeImpuesto);
        setValue(Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO, this.porcentajeDescuento);
        setValue(Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO, this.porcentajeDescuentoOro);
        setValue(Contract.Producto.COLUMN_ID_BENEFICIO, this.idBeneficio);
    }

    @Override
    public Object getValue(String key) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getCodigoExterno() {
        return codigoExterno;
    }

    public void setCodigoExterno(String codigoExterno) {
        this.codigoExterno = codigoExterno;
    }

    public float getPrecioImpuesto() {
        return precioImpuesto;
    }

    public void setPrecioImpuesto(float precioImpuesto) {
        this.precioImpuesto = precioImpuesto;
    }

    public float getPrecioDescuento() {
        return precioDescuento;
    }

    public void setPrecioDescuento(float precioDescuento) {
        this.precioDescuento = precioDescuento;
    }

    public float getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public void setPorcentajeImpuesto(float porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    public float getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(float porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public float getPorcentajeDescuentoOro() {
        return porcentajeDescuentoOro;
    }

    public void setPorcentajeDescuentoOro(float porcentajeDescuentoOro) {
        this.porcentajeDescuentoOro = porcentajeDescuentoOro;
    }

    public int getIdBeneficio() {
        return idBeneficio;
    }

    public void setIdBeneficio(int idBeneficio) {
        this.idBeneficio = idBeneficio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getIdExterno2() {
        return idExterno2;
    }

    public void setIdExterno2(String idExterno2) {
        this.idExterno2 = idExterno2;
    }

    public String getGrupoEstadistico() {
        return grupoEstadistico;
    }

    public void setGrupoEstadistico(String grupoEstadistico) {
        this.grupoEstadistico = grupoEstadistico;
    }

    public int getIdSubCategoria() {
        return idSubCategoria;
    }

    public void setIdSubCategoria(int idSubCategoria) {
        this.idSubCategoria = idSubCategoria;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getGrupoComision() {
        return grupoComision;
    }

    public void setGrupoComision(String grupoComision) {
        this.grupoComision = grupoComision;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public static List<Producto> getProductos(DataBase db, String serie){

        String query = QueryDir.getQuery(Contract.Producto.QUERY_PRODUCTOS);

        Cursor c = db.rawQuery(query, new String[]{serie});

        List<Producto> list = new ArrayList<Producto>();
        while(c.moveToNext()){
            Producto prod = new Producto();
            prod.setID(CursorUtils.getInt(c, Contract.Producto._ID));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            prod.setGrupoEstadistico(CursorUtils.getString(c, Contract.Producto.FIELD_EXTERNO_2));
            list.add(prod);
        }
        c.close();
        return list;
    }

    public static List<Producto> getProductos(DataBase db, int idSubCategoria, String serie){

        String query = QueryDir.getQuery(Contract.Producto.QUERY_PRODUCTOS_BY_CATEGORIA);

        Cursor c = db.rawQuery(query, new String[]{ idSubCategoria + "", serie});

        List<Producto> list = new ArrayList<Producto>();
        while(c.moveToNext()){
            Producto prod = new Producto();
            prod.setID(CursorUtils.getInt(c, Contract.Producto._ID));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            list.add(prod);
        }
        c.close();
        return list;
    }

    public static Producto getProductoID(DataBase db, long prodID)
    {
        String query = QueryDir.getQuery(Contract.Producto.QUERY_PRODUCTO_BY_ID);

        Cursor c = db.rawQuery(query, prodID );

        Producto prod = new Producto();
        while(c.moveToNext()){

            prod.setID(CursorUtils.getInt(c, Contract.Producto._ID));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            prod.setAplicacion(CursorUtils.getString(c, Contract.Producto.COLUMN_APLICACION));
            prod.setGrupoComision(CursorUtils.getString(c, Contract.Producto.COLUMN_GRUPO_COMISION));
            prod.setLinea(CursorUtils.getString(c, Contract.Producto.COLUMN_LINEA));
            prod.setFamilia(CursorUtils.getString(c, Contract.Producto.COLUMN_FAMILIA));
            prod.setAviso(CursorUtils.getString(c, Contract.Producto.COLUMN_AVISO));
        }
        c.close();
        return prod;
    }

    public static Producto getProductoIdServer(DataBase db, int prodID)
    {
        String query = QueryDir.getQuery(Contract.Producto.QUERY_PRODUCTO_BY_ID_SERVER);

        Cursor c = db.rawQuery(query, prodID );

        Producto prod = new Producto();
        while(c.moveToNext()){

            prod.setID(CursorUtils.getInt(c, Contract.Producto._ID));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            prod.setAplicacion(CursorUtils.getString(c, Contract.Producto.COLUMN_APLICACION));
            prod.setLinea(CursorUtils.getString(c, Contract.Producto.COLUMN_LINEA));
        }
        c.close();
        return prod;
    }

    @Override
    protected boolean insertDb(DataBase db) {
        boolean result = false;
        try
        {
            result = super.insertDb(db);
                if(result)
                {
                    ProductoExt cl_ex = new ProductoExt();
                    result = ProductoExt.insert(db, cl_ex);
                }
        }catch(Exception ex){
            result = false;
        }finally{
        }
        return result;
    }


    @Override
    protected boolean updateDb(DataBase db) {
        boolean result = false;
        try
        {
            result = super.updateDb(db);
            if(result){
                ProductoExt cl_ex = new ProductoExt();
                result = ProductoExt.update(db, cl_ex);
            }
        }catch(Exception ex){
            result = false;
        }finally{
        }
        return result;
    }

    public class ProductoExt extends EntityBase<ProductoExt>{

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
            return Contract.ProductoExt.TABLE_NAME;
        }

        @Override
        public void setValues() {
            setValue(Contract.ProductoExt.COLUMN_DESCRIPCION, descripcion);
        }

        @Override
        public String getWhere() {
            return Contract.ProductoExt.COLUMN_ID + " = ?";
        }

        @Override
        public Object getValue(String key) {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }
    }

    public static List<Producto> getProductoSearch(DataBase db, String termSearch, String serie)
    {
        String query = QueryDir.getQuery( Contract.Producto.QUERY_SEARCH );
        //String version = db.getSQLiteVersion();
        //int compare = Convert.versionCompare(version, Contants.SQLITE_VERSION_SEARCH);
        Cursor c = null;
        String termSearchSpace = getSearchString(termSearch);
        String parameter = termSearch.replace(" ", "").trim() + "*";

        c = db.rawQuery(query, new String[]{parameter, serie});


        List<Producto> list = new ArrayList<Producto>();
        while(c.moveToNext()){
            Producto prod = new Producto();
            prod.setID(c.getInt(0));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            list.add(prod);
        }
        c.close();
        return list;
    }

    public static List<Producto> getProductoSearchNoSerie(DataBase db, String termSearch)
    {
        String query = QueryDir.getQuery( Contract.Producto.QUERY_SEARCH_NO_SERIE );
        //String version = db.getSQLiteVersion();
        //int compare = Convert.versionCompare(version, Contants.SQLITE_VERSION_SEARCH);
        Cursor c = null;
        String termSearchSpace = getSearchString(termSearch);
        String parameter = termSearch.replace(" ", "").trim() + "*";

        c = db.rawQuery(query, new String[]{parameter});


        List<Producto> list = new ArrayList<Producto>();
        while(c.moveToNext()){
            Producto prod = new Producto();
            prod.setID(c.getInt(0));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            list.add(prod);
        }
        c.close();
        return list;
    }

    public static List<Producto> getProductoSearch(DataBase db, String termSearch, int idSubCategoria, String serie)
    {
        String query = QueryDir.getQuery( Contract.Producto.QUERY_SEARCH_BY_CATEGORIA );
        //String version = db.getSQLiteVersion();
        //int compare = Convert.versionCompare(version, Contants.SQLITE_VERSION_SEARCH);
        Cursor c = null;
        String termSearchSpace = getSearchString(termSearch);
        String parameter = termSearch.replace(" ", "").trim() + "*";

        c = db.rawQuery(query, new String[]{parameter, idSubCategoria + "", serie});


        List<Producto> list = new ArrayList<Producto>();
        while(c.moveToNext()){
            Producto prod = new Producto();
            prod.setID(c.getInt(0));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            list.add(prod);
        }
        c.close();
        return list;
    }

    public static List<Producto> getProductoByCodigoExterno(DataBase db, String codigoExterno, String serie)
    {
        String query = QueryDir.getQuery( Contract.Producto.QUERY_SEARCH_BY_CODIGO_EXTERNO );
        //String version = db.getSQLiteVersion();
        //int compare = Convert.versionCompare(version, Contants.SQLITE_VERSION_SEARCH);
        Cursor c = null;

        c = db.rawQuery(query, new String[]{codigoExterno, serie});


        List<Producto> list = new ArrayList<Producto>();
        while(c.moveToNext()){
            Producto prod = new Producto();
            prod.setID(c.getInt(0));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            list.add(prod);
        }
        c.close();
        return list;
    }

    public static Producto getProductoSingleByCodigoExterno(DataBase db, String codigoExterno)
    {
        String query = QueryDir.getQuery( Contract.Producto.QUERY_SEARCH_BY_CODIGO_EXTERNO_NO_SERIE );
        //String version = db.getSQLiteVersion();
        //int compare = Convert.versionCompare(version, Contants.SQLITE_VERSION_SEARCH);
        Cursor c = null;

        c = db.rawQuery(query, new String[]{codigoExterno});


        Producto prod = null;
        while(c.moveToNext()){
            prod = new Producto();
            prod.setID(c.getInt(0));
            prod.setDescripcion(CursorUtils.getString(c, Contract.Producto.FIELD_DESCRIPCION));
            prod.setIdSubCategoria(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_SUBCATEGORIA));
            prod.setIdProducto(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_PRODUCTO));
            prod.setValorUnitario(CursorUtils.getDouble(c, Contract.Producto.COLUMN_VALOR_UNITARIO));
            prod.setUrlFoto(CursorUtils.getString(c, Contract.Producto.COLUMN_URL_FOTO));
            prod.setIdBeneficio(CursorUtils.getInt(c, Contract.Producto.COLUMN_ID_BENEFICIO));
            prod.setCodigoExterno(CursorUtils.getString(c, Contract.Producto.COLUMN_CODIGO_EXTERNO));
            prod.setPorcentajeImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_IMPUESTO));
            prod.setPorcentajeDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO));
            prod.setPorcentajeDescuentoOro(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PORCENTAJE_DESCUENTO_ORO));
            prod.setPrecioDescuento(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_DESCUENTO));
            prod.setPrecioImpuesto(CursorUtils.getFloat(c, Contract.Producto.COLUMN_PRECIO_IMPUESTO));
            prod.setAplicacion(CursorUtils.getString(c, Contract.Producto.COLUMN_APLICACION));
            prod.setGrupoComision(CursorUtils.getString(c, Contract.Producto.COLUMN_GRUPO_COMISION));
            prod.setLinea(CursorUtils.getString(c, Contract.Producto.COLUMN_LINEA));
            prod.setFamilia(CursorUtils.getString(c, Contract.Producto.COLUMN_FAMILIA));
        }
        c.close();
        return prod;
    }

    public static void deleteProductos(DataBase db, List<Integer> originalIds)
    {
        String idsNotDelete = "";
        for(int i : originalIds)
            idsNotDelete = idsNotDelete + i + ",";

        if(idsNotDelete.length() > 0)
        {
            db.delete(Contract.Producto.TABLE_NAME, Contract.Producto.COLUMN_ID_PRODUCTO + " NOT IN (" + idsNotDelete + "?)", 0);
        }
    }

    public static void deleteProducto(DataBase db, int id) {
        Producto prod = getProductoIdServer(db, id);
        prod.setIdBeneficio(-1);
        Producto.update(db, prod);
        //db.delete(Contract.Producto.TABLE_NAME, Contract.Producto.COLUMN_ID_PRODUCTO + " = ?" , id);
    }

    public static int conteoProductos(DataBase db)
    {
        Cursor c = null;
        c = db.rawQuery("SELECT COUNT(*) as _count FROM " + Contract.Producto.TABLE_NAME);
        int cont = 0;
        while(c.moveToNext()){
            cont = c.getInt(0);
        }
        c.close();
        return cont;
    }

    public static String getSearchString(String code)
    {
        if(!code.contains(" ")) {
            if (code.length() > 1)
            {
                code = code.substring(0,1) + "-" + code.substring(1, code.length());
            }
            if (code.length() > 5)
            {
                code = code.substring(0,5) + "-" + code.substring(5, code.length());
            }
            if (code.length() > 9)
            {
                code = code.substring(0,9) + "-" + code.substring(9, code.length());
            }
        }
        else
        {
            code.replace(" ", "-");
        }
        return code;
    }
}
