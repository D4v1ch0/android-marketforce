package rp3.auna.util.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.List;

import rp3.accounts.User;
import rp3.auna.Contants;
import rp3.auna.bean.LlamadaData;
import rp3.auna.bean.RegistroPago;
import rp3.auna.bean.SolicitudMovil;
import rp3.auna.bean.VentaFisicaData;
import rp3.auna.bean.VentaRegularData;
import rp3.auna.bean.VisitaVtaDetalle;
import rp3.auna.models.ventanueva.LlamadaVta;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.util.constants.Constants;


/**
 * Created by Jesus Villa on 04/08/2015.
 */
public class SessionManager {

    private static final String TAG = "SessionManager";
    private Context _applicationContext;
    private SharedPreferences _pref;
    private SharedPreferences _prefData;
    private SharedPreferences _prefDataVisita;
    private SharedPreferences _prefProspectoEdit;
    private SharedPreferences _prefProspectoLlamada;
    private SharedPreferences _prefLlamada;
    private SharedPreferences _prefSolicitud;
    private SharedPreferences _prefListVisitas;
    private static SharedPreferences _sessionIp;
    private static SessionManager _sessionManager;

    //Visitas antes de gestionar reprogramar
    private SharedPreferences _prefDataVisitaReprogramada;
    //Llamada en gestion
    private SharedPreferences _prefDataLlamagaGestion;
    //Visita detalle
    private SharedPreferences _prefDataVisitaDetalle;

    //RegistroPago
    private SharedPreferences _prefRegistroPago;
    //VentaFisica
    private SharedPreferences _prefVentaFisica;
    //VentaRegular
    private SharedPreferences _prefVentaRegular;

    //Guardar Session
    private SharedPreferences _prefSessionInit;

    private SessionManager(Context applicationContext)
    {
        _applicationContext = applicationContext;
        _pref = applicationContext.getSharedPreferences(Constants.USER_SESSION_KEY, Context.MODE_PRIVATE);
        _prefData=applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES, Context.MODE_PRIVATE);
        _prefDataVisita=applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES, Context.MODE_PRIVATE);
        _prefProspectoEdit = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_PROSPECTO_EDIT,Context.MODE_PRIVATE);
        _prefProspectoLlamada = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_PROSPECTO_LLAMADA,Context.MODE_PRIVATE);
        _prefLlamada = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_LLAMADA,Context.MODE_PRIVATE);
        _prefSolicitud = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_SOLICITUD,Context.MODE_PRIVATE);
        _prefListVisitas = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_VISITAS,Context.MODE_PRIVATE);
        _prefDataVisitaReprogramada = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_VISITA_REPROGRAMADA,Context.MODE_PRIVATE);
        _prefDataLlamagaGestion = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_LLAMADAGESTION,Context.MODE_PRIVATE);
        _prefDataVisitaDetalle =  applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_VISITA_DETALLE,Context.MODE_PRIVATE);
        _prefRegistroPago = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_REGISTROPAGO,Context.MODE_PRIVATE);
        _prefVentaFisica = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_VISITAFISICA,Context.MODE_PRIVATE);
        _prefVentaRegular = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_VISITAREGULAR,Context.MODE_PRIVATE);
        _prefSessionInit = applicationContext.getSharedPreferences(Constants.DATA_PRIVATE_PREFERENCES_SESION_INICIADA,Context.MODE_PRIVATE| Context.MODE_MULTI_PROCESS);
    }

    public static SessionManager getInstance(Context applicationContext) {
         if(_sessionManager==null)
         {
             _sessionManager = new SessionManager(applicationContext);
         }
         return (_sessionManager) ;
    }

    //region Session Init
    public void createSessionInit(String sesion) {
        removeDetalleVisita();
        SharedPreferences.Editor prefsEditor = _prefSessionInit.edit();
        Log.d(TAG, "grabando valor _prefSessionInit :"+sesion);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_SESION_INICIADA, sesion);
        prefsEditor.commit();
    }

    public void removeSessionInit(){
        //eliminar session de _prefDataVisitaReprogramada creada.
        SharedPreferences.Editor prefsEditor = _prefSessionInit.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_SESION_INICIADA);
        prefsEditor.commit();
    }

    public String getSessionInit() {
        String s_user = _prefSessionInit.getString(Constants.DATA_PRIVATE_PREFERENCES_SESION_INICIADA, null);
        if(s_user==null){
            Log.d(TAG,"null _prefSessionInit...");
            return null;}
        return s_user;
    }

    //endregion

    //region Detalle de la visita
    public void createDetalleVisita(VisitaVtaDetalle visitaVta) {
        removeDetalleVisita();
        SharedPreferences.Editor prefsEditor = _prefDataVisitaDetalle.edit();
        String json = new Gson().toJson(visitaVta);
        Log.d(TAG, "grabando valor _prefDataVisitaDetalle :"+visitaVta);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_VISITA_DETALLE, json);
        prefsEditor.commit();
    }

    public void removeDetalleVisita(){
        //eliminar session de _prefDataVisitaReprogramada creada.
        SharedPreferences.Editor prefsEditor = _prefDataVisitaDetalle.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_VISITA_DETALLE);
        prefsEditor.commit();
    }

    public VisitaVtaDetalle getDetalleVisita() {
        String s_user = _prefDataVisitaDetalle.getString(Constants.DATA_PRIVATE_PREFERENCES_VISITA_DETALLE, null);
        if(s_user==null){
            Log.d(TAG,"null _prefDataVisitaDetalle...");
            return null;}
        return new Gson().fromJson(s_user,VisitaVtaDetalle.class);
    }

    //endregion

    //region Llamada en Gestion
    public void createLlamadaGestion(LlamadaVta visitaVta) {
        removeLlamadaGestion();
        SharedPreferences.Editor prefsEditor = _prefDataLlamagaGestion.edit();
        String json = new Gson().toJson(visitaVta);
        Log.d(TAG, "grabando valor _prefDataLlamagaGestion :"+visitaVta);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_LLAMADAGESTION, json);
        prefsEditor.commit();
    }

    public void removeLlamadaGestion(){
        //eliminar session de _prefDataVisitaReprogramada creada.
        SharedPreferences.Editor prefsEditor = _prefDataLlamagaGestion.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_LLAMADAGESTION);
        prefsEditor.commit();
    }

    public LlamadaVta getLlamadaGestion() {
        String s_user = _prefDataLlamagaGestion.getString(Constants.DATA_PRIVATE_PREFERENCES_LLAMADAGESTION, null);
        if(s_user==null){
            Log.d(TAG,"null _prefDataLlamagaGestion...");
            return null;}
        return new Gson().fromJson(s_user,LlamadaVta.class);
    }

    //endregion

    //region Visita reprogramada
    public void createVisitaRerogramar(VisitaVta visitaVta) {
        removeVisitaReprogramada();
        SharedPreferences.Editor prefsEditor = _prefDataVisitaReprogramada.edit();
        String json = new Gson().toJson(visitaVta);
        Log.d(TAG, "grabando valor _prefDataVisitaReprogramada :"+visitaVta);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_VISITA_REPROGRAMADA, json);
        prefsEditor.commit();
    }

    public void removeVisitaReprogramada(){
        //eliminar session de _prefDataVisitaReprogramada creada.
        SharedPreferences.Editor prefsEditor = _prefDataVisitaReprogramada.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_VISITA_REPROGRAMADA);
        prefsEditor.commit();
    }

    public VisitaVta getVisitaReprogramada() {
        String s_user = _prefDataVisitaReprogramada.getString(Constants.DATA_PRIVATE_PREFERENCES_VISITA_REPROGRAMADA, null);
        if(s_user==null){
            Log.d(TAG,"null _prefDataVisitaReprogramada...");
            return null;}
        return new Gson().fromJson(s_user,VisitaVta.class);
    }

    //endregion

    //region Session Solicitud
    public void createSessionSolicitud(SolicitudMovil prospectoVtaDb)
    {
        removeSolicitud();
        SharedPreferences.Editor prefsEditor = _prefSolicitud.edit();
        String json = new Gson().toJson(prospectoVtaDb);
        Log.d(TAG, "grabando valor _prefSolicitud :"+prospectoVtaDb);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_SOLICITUD, json);
        prefsEditor.commit();
    }

    public void removeSolicitud(){
        //eliminar session de _prefSolicitud creada.
        SharedPreferences.Editor prefsEditor = _prefSolicitud.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_SOLICITUD);
        prefsEditor.commit();
    }

    public SolicitudMovil getSolicitud()
    {
        String s_user = _prefSolicitud.getString(Constants.DATA_PRIVATE_PREFERENCES_SOLICITUD, null);
        if(s_user==null){
            Log.d(TAG,"null _prefSolicitud...");
            return null;}
        return new Gson().fromJson(s_user,SolicitudMovil.class);
    }
    //endregion

    //region Session Llamada

    public void createSessionLlamada(String prospectoVtaDb)
    {
        removeLlamada();
        SharedPreferences.Editor prefsEditor = _prefLlamada.edit();
        Log.d(TAG, "grabando valor para reprogramar llamada :"+prospectoVtaDb);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_LLAMADA, prospectoVtaDb);
        prefsEditor.commit();
    }

    public void removeLlamada(){
        //eliminar session de llamada creada.
        SharedPreferences.Editor prefsEditor = _prefLlamada.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_LLAMADA);
        prefsEditor.commit();
    }

    public String getLlamada()
    {
        String s_user = _prefLlamada.getString(Constants.DATA_PRIVATE_PREFERENCES_LLAMADA, null);
        if(s_user==null){
            Log.d(TAG,"null..");
            return null;}
        return s_user;
    }

    public void createProspectoLlamada(ProspectoVtaDb prospectoVtaDb)
    {
        removeProspectoLlamada();
        SharedPreferences.Editor prefsEditor = _prefProspectoLlamada.edit();
        Gson gson = new Gson();
        String json = gson.toJson(prospectoVtaDb);
        Log.d(TAG, "grabando llamada prospectoVtaDb:"+prospectoVtaDb);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_PROSPECTO_LLAMADA, json);
        prefsEditor.commit();
    }

    public void removeProspectoLlamada(){
        //eliminar session de prospectoEdit creada.
        SharedPreferences.Editor prefsEditor = _prefProspectoLlamada.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_PROSPECTO_LLAMADA);
        prefsEditor.commit();
    }

    public ProspectoVtaDb getProspectoLlamada()
    {
        String s_user = _prefProspectoLlamada.getString(Constants.DATA_PRIVATE_PREFERENCES_PROSPECTO_LLAMADA, null);
        if(s_user==null){Log.d(TAG,"null..");return null;}
        return new Gson().fromJson(s_user,ProspectoVtaDb.class);
    }

    //endregion

    //region Prospecto Editar
    public void createProspectoEdit(ProspectoVtaDb prospectoVtaDb)
    {
        removeProspectoEdit();
        SharedPreferences.Editor prefsEditor = _prefProspectoEdit.edit();
        Gson gson = new Gson();
        String json = gson.toJson(prospectoVtaDb);
        Log.d(TAG, "grabando prospectoVtaDb:"+prospectoVtaDb);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_PROSPECTO_EDIT, json);
        prefsEditor.commit();
    }

    public void removeProspectoEdit(){
        //eliminar session de prospectoEdit creada.
        SharedPreferences.Editor prefsEditor = _prefProspectoEdit.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_PROSPECTO_EDIT);
        prefsEditor.commit();
    }

    public ProspectoVtaDb getProspectoEdit()
    {
        String s_user = _prefProspectoEdit.getString(Constants.DATA_PRIVATE_PREFERENCES_PROSPECTO_EDIT, null);
        if(s_user==null){Log.d(TAG,"null..");return null;}
        return new Gson().fromJson(s_user,ProspectoVtaDb.class);
    }

    //endregion

    //region Lista de visitas pendientes
    public void createListVisitaSession(List<rp3.auna.bean.VisitaVta> visitaVta)
    {
        removeVisitaSession();
        SharedPreferences.Editor prefsEditor = _prefListVisitas.edit();
        Gson gson = new Gson();
        String s_userr = gson.toJson(visitaVta);
        //Log.d(TAG, "subiendo usuario"+s_userr);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_VISITAS, s_userr);
        prefsEditor.commit();
    }

    public List<rp3.auna.bean.VisitaVta> getListVisitaSession()
    {
        String s_user = _prefListVisitas.getString(Constants.DATA_PRIVATE_PREFERENCES_VISITAS, null);
        TypeToken<List<rp3.auna.bean.VisitaVta>> token = new TypeToken<List<rp3.auna.bean.VisitaVta>>(){};
        List<rp3.auna.bean.VisitaVta> user = null;
        if(s_user==null){
            user = null;
        }else{
            user = new Gson().fromJson(s_user,token.getType());
        }
        return user;
    }

    private void removeListVisitaSession()
    {
        //eliminar session de VISITAS creada.
        SharedPreferences.Editor prefsEditor = _prefListVisitas.edit();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_VISITAS);
        prefsEditor.commit();
    }
    //endregion

    public void removeData(){
        //eliminar session de usuario creada.
        SharedPreferences.Editor prefsEditor = _prefData.edit();
        prefsEditor.clear();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES);
        prefsEditor.commit();
    }

    public void createUserSession(User user) {
        removeUserSession();
        SharedPreferences.Editor prefsEditor = _pref.edit();
        Gson gson = new Gson();
        String s_userr = gson.toJson(user);
        //Log.d(TAG, "subiendo usuario"+s_userr);
        prefsEditor.putString(Constants.USER_SESSION_KEY, s_userr);
        prefsEditor.commit();
    }

    public void createDuracionSession(LlamadaData duracion) {
        removeUserSession();
        SharedPreferences.Editor prefsEditor = _prefData.edit();
        Gson gson = new Gson();
        String json = gson.toJson(duracion);
        Log.d(TAG, "grabando llamadata:"+duracion);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES, json);
        prefsEditor.commit();
    }

    private void removeDataSession() {
        //eliminar session de usuario creada.
        SharedPreferences.Editor prefsEditor = _prefData.edit();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES);
        prefsEditor.commit();
        //logout en facebook
        //LoginManager.getInstance().logOut();
    }

    public LlamadaData getDataSession() {
        String s_user = _prefData.getString(Constants.DATA_PRIVATE_PREFERENCES, null);
        if(s_user==null){Log.d(TAG,"null..");return null;}
        return new Gson().fromJson(s_user,LlamadaData.class);
    }

    public User getUserLogged() {
        String s_user = _pref.getString(Constants.USER_SESSION_KEY, null);
        //Usuario user = (s_user == null || s_user.length()<5)?null: new Gson().fromJson(s_user, Usuario.class);
        User user = (s_user == null )?null: new Gson().fromJson(s_user, User.class);

        return user;
    }

    private void removeUserSession() {
        //eliminar session de usuario creada.
        SharedPreferences.Editor prefsEditor = _pref.edit();
        prefsEditor.remove(Constants.USER_SESSION_KEY);
        prefsEditor.commit();
        //logout en facebook
        //LoginManager.getInstance().logOut();
    }

    //region Crear Visita en gestion
    public void createVisitaSession(VisitaVta visitaVta) {
        removeVisitaSession();
        SharedPreferences.Editor prefsEditor = _prefDataVisita.edit();
        Gson gson = new Gson();
        String s_userr = gson.toJson(visitaVta);
        //Log.d(TAG, "subiendo usuario"+s_userr);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_VISITA, s_userr);
        prefsEditor.commit();
    }

    public VisitaVta getVisitaSession() {
        String s_user = _prefDataVisita.getString(Constants.DATA_PRIVATE_PREFERENCES_VISITA, null);
        VisitaVta user = (s_user == null )?null: new Gson().fromJson(s_user, VisitaVta.class);
        return user;
    }

    public void removeVisitaSession() {
        //eliminar session de VISITA creada.
        SharedPreferences.Editor prefsEditor = _prefDataVisita.edit();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_VISITA);
        prefsEditor.commit();
    }

    //endregion

    public boolean isLogged() {
        if (getUserLogged()==null)return false;
        else return true;
    }

    public void logoutApp() {
        removeUserSession();
        //removeData();
        //((Activity)_applicationContext).recreate();
        /*Intent i = ((Activity)_applicationContext).getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( ((Activity)_applicationContext).getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Activity)_applicationContext).startActivity(i);*/
    }

    public String getAndroidId() {
        String android_id = Settings.Secure.getString(_applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "Android ID : " + android_id);
        return  android_id;
    }


    //Regiones para guardar los metodos en linea

    //region RegistroPago
    public void createRegistroPago(RegistroPago registroPago) {
        removeRegistroPago();
        SharedPreferences.Editor prefsEditor = _prefRegistroPago.edit();
        Gson gson = new Gson();
        String s_userr = gson.toJson(registroPago);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_REGISTROPAGO, s_userr);
        prefsEditor.commit();
    }

    public RegistroPago getRegistroPago() {
        String s_user = _prefRegistroPago.getString(Constants.DATA_PRIVATE_PREFERENCES_REGISTROPAGO, null);
        RegistroPago user = (s_user == null )?null: new Gson().fromJson(s_user, RegistroPago.class);
        return user;
    }

    public void removeRegistroPago() {
        //eliminar session de VISITA creada.
        SharedPreferences.Editor prefsEditor = _prefRegistroPago.edit();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_REGISTROPAGO);
        prefsEditor.commit();
    }
    //endregion

    //region VentaFisica
    public void createVentaFisica(VentaFisicaData ventaFisicaData) {
        removeVentaFisica();
        SharedPreferences.Editor prefsEditor = _prefVentaFisica.edit();
        Gson gson = new Gson();
        String s_userr = gson.toJson(ventaFisicaData);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_VISITAFISICA, s_userr);
        prefsEditor.commit();
    }

    public VentaFisicaData getVentaFisica() {
        String s_user = _prefVentaFisica.getString(Constants.DATA_PRIVATE_PREFERENCES_VISITAFISICA, null);
        VentaFisicaData user = (s_user == null )?null: new Gson().fromJson(s_user, VentaFisicaData.class);
        return user;
    }

    public void removeVentaFisica() {
        //eliminar session de VISITA creada.
        SharedPreferences.Editor prefsEditor = _prefVentaFisica.edit();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_VISITAFISICA);
        prefsEditor.commit();
    }
    //endregion

    //region VentaRegular
    public void createVentaRegular(VentaRegularData ventaRegularData) {
        removeVentaRegular();
        SharedPreferences.Editor prefsEditor = _prefVentaRegular.edit();
        Gson gson = new Gson();
        String s_userr = gson.toJson(ventaRegularData);
        prefsEditor.putString(Constants.DATA_PRIVATE_PREFERENCES_VISITAREGULAR, s_userr);
        prefsEditor.commit();
    }

    public VentaRegularData getVentaRegular() {
        String s_user = _prefVentaRegular.getString(Constants.DATA_PRIVATE_PREFERENCES_VISITAREGULAR, null);
        VentaRegularData user = (s_user == null )?null: new Gson().fromJson(s_user, VentaRegularData.class);
        return user;
    }

    public void removeVentaRegular() {
        //eliminar session de VISITA creada.
        SharedPreferences.Editor prefsEditor = _prefVentaRegular.edit();
        prefsEditor.remove(Constants.DATA_PRIVATE_PREFERENCES_VISITAREGULAR);
        prefsEditor.commit();
    }
    //endregion



}
