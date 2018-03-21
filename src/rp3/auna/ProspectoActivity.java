package rp3.auna;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.OnEditorAction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rp3.auna.bean.Documento;
import rp3.auna.bean.InVentasProspecto;
import rp3.auna.bean.Informacion;
import rp3.auna.bean.ProspectoVta;
import rp3.auna.bean.ValidateDocument;
import rp3.auna.dialog.CalificativoDialog;
import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.helper.MinMaxFilter;
import rp3.auna.util.helper.Util;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.Utils;
import rp3.auna.webservices.ProspectoCalificacionAfiliacionClient;
import rp3.auna.webservices.SpiClient;
import rp3.auna.webservices.ValidateDocumentClient;
import rp3.data.models.IdentificationType;
import rp3.db.sqlite.DataBase;

import static rp3.data.entity.EntityBase.ACTION_UPDATE;

public class ProspectoActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = ProspectoActivity.class.getSimpleName();
    private static final int RESULT_PROSPECTO_NUEVO = 3;
    private static final int REQUEST_PROSPECTO_EDIT = 17;
    private static final int RESULT_PROSPECTO_EDIT = 17;
    //Variables para los referidos
    private static final int RESULT_REFERIDOS_FROM_VISITA = 30;
    private  Button notifCount;
    private int mNotifCount = 0;
    private int visitaReferidoId = 0;

    //region Primary Views
    @BindView(R.id.toolbarNuevoProspecto) Toolbar toolbar;
    @BindView(R.id.statusBar) FrameLayout statusBar;
    @BindView(R.id.scrollViewNuevoProspecto) NestedScrollView scrollView;
    @BindView(R.id.fabNuevoProspecto) FloatingActionButton fabNuevo;
    @BindView(R.id.spTipoPersona) Spinner spTipoPersona;
    //endregion

    //region General Views
    /*General View*/
    @BindView(R.id.containerTvInformacion) LinearLayout containerTvInformacion;
    @BindView(R.id.containerTvTelefono) LinearLayout containerTvTelefono;
    @BindView(R.id.containerTvDireccion) LinearLayout containerTvDireccion;
    @BindView(R.id.tvInformacion) TextView tvInformacion;
    //endregion

    //region Container Views
    /*Containers*/
    @BindView(R.id.relativeDocumentoN) RelativeLayout containerDocumento;
    @BindView(R.id.relativeInformacionN) RelativeLayout containerInformacion;
    @BindView(R.id.relativeTelefonosN) RelativeLayout containerTelefono;
    @BindView(R.id.relativeDirecciones) RelativeLayout containerDireccion;
    //endregion

    //region Natural Views
    /*Views TipoPersona Natural*/
    @BindView(R.id.spTipoDocumento) Spinner spTipoDocumento;
    @BindView(R.id.etDocumento) EditText etDocumento;
    @BindView(R.id.etNombre) EditText etNombre;
    @BindView(R.id.etApellidoPaterno) EditText etApellidoPaterno;
    @BindView(R.id.etApellidoMaterno) EditText etApellidoMaterno;
    @BindView(R.id.etEmailNatural) EditText etEmailNatural;
    @BindView(R.id.etCelular) EditText etCelular;
    @BindView(R.id.etTelefono) EditText etTelefono;
    @BindView(R.id.etDireccion1) EditText etDireccion1;
    @BindView(R.id.etDireccion2) EditText etDireccion2;
    @BindView(R.id.etCentroTrabajo) EditText etCentroTrabajo;
    //endregion

    /*Container J*/
    @BindView(R.id.relativeDocumentoJ) RelativeLayout containerDocumentoJ;
    @BindView(R.id.relativeInformacionJ) RelativeLayout containerInformacionJ;
    @BindView(R.id.relativeTelefonosJ) RelativeLayout containerTelefonoJ;
    @BindView(R.id.relativeDireccionesJ) RelativeLayout containerDireccionJ;

    /*Views TipoPersona juridica*/
    @BindView(R.id.etRuc) EditText etRuc;
    @BindView(R.id.etEmpresa) EditText etEmpresa;
    @BindView(R.id.etNombreContacto) EditText etNombreContacto;
    @BindView(R.id.etApellidoPaternoContacto) EditText etApellidoPaternoContacto;
    @BindView(R.id.etApellidoMaternoContacto) EditText etApellidoMaternoContacto;
    @BindView(R.id.etEmailJuridico) EditText etEmailJuridico;
    @BindView(R.id.etCelularJ) EditText etNumeroEmpresa;
    @BindView(R.id.etTelefonoJ) EditText etNumeroContacto;
    @BindView(R.id.etDireccionJ1) EditText etDireccion1J;
    @BindView(R.id.etDireccionJ2 ) EditText etDireccion2J;

    /*Atributos*/
    private ArrayList<String> tipoPersonas;
    private String tipoPersonaCode = null;
    private ProspectoVtaDb prospectoVta;
    private boolean validateCalificacion = false;

    /*Atributos persona Natural*/
    private ArrayList<String> listTipoDocumento;
    private String tipoDocumentoSelected = null;
    private int idTipoDocumentoSelected = 0;

    private List<IdentificationType> documentos;

    //Validar si es un prospecto: CREAR, EDITAR Ó REFERIDO
    //1.-Nuevo, 2.-Editar 3.-Referido
    private int opcionProspectar = 0;
    private int opcionLlamada = 0;
    private ProspectoVtaDb prospectoEditar;
    private TextWatcher mListenerText = null;
    private boolean documentEditarFirstTime = true;
    private String documentoProspectoEditar = null;
    private boolean validateDocument = true;
    private int flagDocumento = 0;
    private boolean changeNombre = true;
    private IdentificationType identificationType = null;
    private int flagShowCalificacion = 0;
    private int flagDocumentValidate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospecto);
        Log.d(TAG,"onCreate...");
        ButterKnife.bind(this);
        toolbarStatusBar();
        navigationBarStatusBar();
        try{
            validateEditNew();
            initData();
            setTextCapFields();
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG,"onCreate:"+e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //region Todo

    private void validateEditNew(){
        if(getIntent().getExtras()!=null){
            opcionProspectar = getIntent().getExtras().getInt("Opcion",0);
            opcionLlamada = getIntent().getExtras().getInt("Llamada",0);
            if(opcionProspectar==2){
                prospectoEditar = SessionManager.getInstance(this).getProspectoEdit();
                SessionManager.getInstance(this).removeProspectoEdit();
                Log.d(TAG,"Prospecto a editar:"+prospectoEditar.toString());
                getSupportActionBar().setTitle("Editar Prospecto");
                if(opcionLlamada==3){
                    Log.d(TAG,"opcion de llamada...");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }else if(opcionProspectar == 3){
                Log.d(TAG,"Opcion para referidos...");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                visitaReferidoId = SessionManager.getInstance(this).getVisitaSession().getIdVisita();
                SessionManager.getInstance(ProspectoActivity.this).removeVisitaSession();
                getSupportActionBar().setTitle("Agregar Referido");
            }
        }
    }

    private void validateProspecto(){
        Log.d(TAG,"validateProspecto...");
        //Validar si la opcion es para editar un prospecto
        if(opcionProspectar == 2){
            Log.d(TAG,prospectoEditar.toString());
            //Prospecto a editar es del tipo Natural
            if(prospectoEditar.getTipoPersonaCode().equalsIgnoreCase("N")){
                Log.d(TAG,prospectoEditar.toString());
                tipoPersonaCode = "N";
                spTipoPersona.post(new Runnable() {
                    @Override
                    public void run() {
                        spTipoPersona.setSelection(1);
                        setDataEditarN();
                    }
                });
            }else{
                tipoPersonaCode = "J";
                spTipoPersona.post(new Runnable() {
                    @Override
                    public void run() {
                        spTipoPersona.setSelection(2);
                        setDataEditarJ();
                    }
                });
            }
        }
    }

    private void setDataEditarN(){
        Log.d(TAG,"setDataEditarN...");
        Log.d(TAG,prospectoEditar.toString());
        //Validar si la persona natural tiene documento
        if((prospectoEditar.getDocumento())!=null){
            Log.d(TAG,"documento !=null...");
            etDocumento.setText(prospectoEditar.getDocumento());
            if(prospectoEditar.getDocumento().length()>0){
                for(int i = 0 ; i<listTipoDocumento.size()-1;i++){
                    if(documentos.get(i).getID()==prospectoEditar.getTipoDocumento()){
                        final int j = i;
                        spTipoDocumento.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"Numero documento setText...");
                                documentEditarFirstTime = true;
                                spTipoDocumento.setSelection(j+1);
                                idTipoDocumentoSelected = j+1;
                                etDocumento.setText(prospectoEditar.getDocumento());
                            }
                        });
                        break;
                    }
                }
            }
        }else{
            Log.d(TAG,"documento == null...");
        }
        //Setear a los views la data correspondiente a N que se tenga
        if(!TextUtils.isEmpty(prospectoEditar.getNombres()) && prospectoEditar.getNombres()!=null){etNombre.setText(prospectoEditar.getNombres());}
        if(!TextUtils.isEmpty(prospectoEditar.getApellidoPaterno()) && prospectoEditar.getApellidoPaterno()!=null){etApellidoPaterno.setText(prospectoEditar.getApellidoPaterno());}
        if(!TextUtils.isEmpty(prospectoEditar.getApellidoMaterno()) && prospectoEditar.getApellidoMaterno()!=null){etApellidoMaterno.setText(prospectoEditar.getApellidoMaterno());}
        //if(!TextUtils.isEmpty(prospectoEditar.getContactoNombre()) && prospectoEditar.getContactoNombre()!=null){etNombre.setText(prospectoEditar.getContactoNombre());}
        //if(!TextUtils.isEmpty(prospectoEditar.getContactoApellidoPaterno()) && prospectoEditar.getContactoApellidoPaterno()!=null){etApellidoPaterno.setText(prospectoEditar.getContactoApellidoPaterno());}
        //if(!TextUtils.isEmpty(prospectoEditar.getContactoApellidoMaterno())){etApellidoMaterno.setText(prospectoEditar.getContactoApellidoMaterno());}
        if(!TextUtils.isEmpty(prospectoEditar.getEmail())){etEmailNatural.setText(prospectoEditar.getEmail());}
        if(!TextUtils.isEmpty(prospectoEditar.getCelular())){etCelular.setText(prospectoEditar.getCelular());}
        if(!TextUtils.isEmpty(prospectoEditar.getTelefono())){etTelefono.setText(prospectoEditar.getTelefono());}
        if(!TextUtils.isEmpty(prospectoEditar.getDireccion1())){etDireccion1.setText(prospectoEditar.getDireccion1());}
        if(!TextUtils.isEmpty(prospectoEditar.getDireccion2())){etDireccion2.setText(prospectoEditar.getDireccion2());}
        if(!TextUtils.isEmpty(prospectoEditar.getRazonSocial())){etCentroTrabajo.setText(prospectoEditar.getRazonSocial());}
    }

    private void setDataEditarJ(){
        Log.d(TAG,"setDataEditarJ...");
        if(!TextUtils.isEmpty(prospectoEditar.getRuc())){etRuc.setText(prospectoEditar.getRuc());}
        if(!TextUtils.isEmpty(prospectoEditar.getRazonSocial())){etEmpresa.setText(prospectoEditar.getRazonSocial());}
        if(!TextUtils.isEmpty(prospectoEditar.getContactoNombre())){etNombreContacto.setText(prospectoEditar.getContactoNombre());}
        if(!TextUtils.isEmpty(prospectoEditar.getContactoApellidoPaterno())){etApellidoPaternoContacto.setText(prospectoEditar.getContactoApellidoPaterno());}
        if(!TextUtils.isEmpty(prospectoEditar.getContactoApellidoMaterno())){etApellidoMaternoContacto.setText(prospectoEditar.getContactoApellidoMaterno());}
        if(!TextUtils.isEmpty(prospectoEditar.getEmail())){etEmailJuridico.setText(prospectoEditar.getEmail());}
        if(!TextUtils.isEmpty(prospectoEditar.getCelular())){etNumeroEmpresa.setText(prospectoEditar.getCelular());}
        if(!TextUtils.isEmpty(prospectoEditar.getTelefono())){etNumeroContacto.setText(prospectoEditar.getTelefono());}
        if(!TextUtils.isEmpty(prospectoEditar.getDireccion1())){etDireccion1J.setText(prospectoEditar.getDireccion1());}
        if(!TextUtils.isEmpty(prospectoEditar.getDireccion2())){etDireccion2J.setText(prospectoEditar.getDireccion2());}
    }

    private void initData(){
        Log.d(TAG,"initData...");
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fabNuevo.hide();
                } else {
                    fabNuevo.show();
                }
            }
        });
        documentos = IdentificationType.getAll(Utils.getDataBase(this));
        tipoPersonas = new ArrayList<>();
        tipoPersonas.add("Seleccione");
        tipoPersonas.add("Natural");
        tipoPersonas.add("Jurídico");
        ArrayAdapter<String> adapterTipoPersona = new ArrayAdapter<String>(this,R.layout.tv_spinner,tipoPersonas);
        spTipoPersona.setAdapter(adapterTipoPersona);
        spTipoPersona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private boolean estado=true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"onItemSelected...");
                Log.d(TAG,"ItemSelected position:"+position);
                //Seleccionar el tipo de prospecto a Editar
                if(opcionProspectar==2){
                    if(estado){
                        estado=false;
                        if(prospectoEditar.getTipoPersonaCode().equalsIgnoreCase("N")){
                            tipoPersonaCode="N";
                            dismissJuridico();
                            initCleanViewsJuridico();
                            showViewsNatural();
                        }else{
                            tipoPersonaCode="J";
                            dismissNatural();
                            initCleanViewsNatural();
                            showViewsJuridico();
                        }
                    }else{
                        if(position == 0){
                            dismissNatural();
                            dismissJuridico();
                            tipoPersonaCode=null;
                        }
                        else if(position==1){
                            tipoPersonaCode="N";
                            dismissJuridico();
                            initCleanViewsJuridico();
                            showViewsNatural();
                        }
                        else{
                            tipoPersonaCode="J";
                            dismissNatural();
                            initCleanViewsNatural();
                            showViewsJuridico();
                        }
                    }
                }
                //Seguir el flujo normal al Crear un prospecto
                else{
                    if(position == 0){
                        dismissNatural();
                        dismissJuridico();
                        tipoPersonaCode=null;
                    }
                    else if(position==1){
                        tipoPersonaCode="N";
                        dismissJuridico();
                        initCleanViewsJuridico();
                        showViewsNatural();
                    }
                    else{tipoPersonaCode="J";
                        dismissNatural();
                        initCleanViewsNatural();
                        showViewsJuridico();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG,"onNothingSelected...");
            }
        });
        fabNuevo.setOnClickListener(this);
    }

    private void validate(){
        if(tipoPersonaCode==null){Toast.makeText(this, "Debe seleccionar un tipo de persona y llenar sus datos respectivamente...", Toast.LENGTH_SHORT).show();return;}
        if(tipoPersonaCode=="N"){if(validateNatural()){initDataPersonaN();}return;}
        if(tipoPersonaCode=="J"){if(validateJuridico()){initDataPersonaJ();}return;}
    }

    //Confirmar cambios
    private void showConfirmDialog(final String tipo){
        Log.d(TAG,"showConfirmDialog..."+tipo);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                prospectoVta.setIdAgente(rp3.configuration.PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
                prospectoVta.setEstado(1);
                prospectoVta.setEstadoCode("A");
                Log.d(TAG,"flagdocumento:"+flagDocumento+" opcionProspectar:"+opcionProspectar);
                if(tipo.equalsIgnoreCase("N")){
                    if(flagDocumento == 0){
                        setConfirmFromDialog(tipo);
                    }else if (flagDocumento == 1){
                        if(opcionProspectar==2){
                            if(!TextUtils.isEmpty(etDocumento.getText())){
                                if(prospectoEditar.getDocumento()!=null){
                                    if(prospectoEditar.getDocumento().length()>0){
                                        if(etDocumento.getText().toString().equalsIgnoreCase(prospectoEditar.getDocumento())){
                                            setConfirmFromDialog(tipo);
                                        }else{
                                            Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_exist, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }else{
                                    setConfirmFromDialog(tipo);
                                }
                            }else{
                                Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_exist, Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_exist, Toast.LENGTH_SHORT).show();
                        }

                    }else if (flagDocumento == 2){
                        Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_robinson, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_validate, Toast.LENGTH_SHORT).show();
                    }

            /*if(validateProspecto(tipo)){
                Log.d(TAG,"Normal N validar...");
                setConfirmFromDialog(tipo);
            }else{
                Log.d(TAG,"tipo N is false...equivalente en su base...");
                Toast.makeText(this, "Disculpe este documento ya se encuentra registrado en su base de prospectos.", Toast.LENGTH_SHORT).show();
            }*/
                }else{
                    if(flagDocumento == 0 ){
                        setConfirmFromDialog(tipo);
                    }else if (flagDocumento == 1){
                        Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_exist, Toast.LENGTH_SHORT).show();
                    }else if (flagDocumento == 2){
                        Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_robinson, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_validate, Toast.LENGTH_SHORT).show();
                    }
            /*if(validateProspecto(tipo)){
                Log.d(TAG,"Normal J validar...");
                setConfirmFromDialog(tipo);
            }else{
                Log.d(TAG,"tipo J is false...equivalente en su base...");
                Toast.makeText(this, "Disculpe este Ruc ya se encuentra registrado en su base de prospectos.", Toast.LENGTH_SHORT).show();
            }*/
                }
            }
        });

    }

    private void setConfirmFromDialog(final String tipo){
        DataBase dataBase = Utils.getDataBase(ProspectoActivity.this);
        /*if(opcionProspectar==2){
            ProspectoVtaDb model = ProspectoVtaDb.getProspectoIdProspectoBD(dataBase,(int)prospectoEditar.getID());
            if(model==null){
                Log.d(TAG,"el prospecto a editar no se hallo su ID de sqlite...");
            }else{
                Log.d(TAG,"El prospecto a editar si esta en sqlite...");
                Log.d(TAG,model.toString());
                model.setApellidoMaterno("MelgaarQWQWQ");
                setDataToEdit();
                boolean duro = ProspectoVtaDb.update(dataBase,model);
                if(duro){
                    Log.d(TAG,"Si actualizo en duro...");
                    ProspectoVtaDb model2 = ProspectoVtaDb.getProspectoIdProspectoBD(dataBase,(int)prospectoEditar.getID());
                    Log.d(TAG,model2.toString());
                }else{
                    Log.d(TAG,"No se actualizo en duro...");
                }
            }
        }
        if(dataBase.isOpen()){
            Log.d(TAG,"database is open...");
           //dataBase.close();
        }else{
            Log.d(TAG,"database is close...");
        }*/
        if(opcionProspectar == 3){
            Log.d(TAG,"opcionProspectar == 3...");
            showAlertDialogReferidoConfirm();
        }else{
            Log.d(TAG,"opcion a prospectar:"+opcionProspectar);
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle)
                    .setTitle(this.getResources().getString(R.string.appname_marketforce));
            if(opcionProspectar==2){
                builder.setMessage("¿Desea editar este prospecto?");
                if(prospectoEditar.getIdProspecto()>0){
                    prospectoVta.setIdProspecto(prospectoEditar.getIdProspecto());
                }else{
                    prospectoVta.setIdProspecto((int)prospectoEditar.getID());
                }
                if(prospectoEditar.getOrigenCode().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF)){
                    prospectoVta.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF);
                }else{
                    prospectoVta.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_MKF_MOVIL);
                }
            }
            else if(opcionProspectar==0){
                builder.setMessage("¿Desea grabar este prospecto?");
            }
            /*else if(opcionProspectar == 3){
                builder.setMessage("¿Desea grabar este referido?");
            }*/
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG,"onClickDialog Si...");
                    prospectoVta.setEstado(1);
                    if(opcionProspectar==2){
                        if(prospectoEditar.getOrigenCode().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF)){
                            prospectoVta.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF);
                        }else{
                            prospectoVta.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_MKF_MOVIL);
                        }
                    }else{
                        prospectoVta.setEstadoCode(Contants.GENERAL_VALUE_CODE_APTO_PROSPECCION);
                        prospectoVta.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_MKF_MOVIL);
                    }

                    String json = new Gson().toJson(prospectoVta);
                    if(tipo.equalsIgnoreCase("N") && prospectoVta.getTipoDocumento()>0 && prospectoVta.getDocumento()!=null){
                        Log.d(TAG," es de tipo N y tiene documento...");
                        if(flagDocumento==1 && opcionProspectar == 2){
                            if(etDocumento.getText().toString().equalsIgnoreCase(prospectoEditar.getDocumento())){
                                Log.d(TAG,"tiene documento y es natural es prospecto aeditar...");
                                if(opcionProspectar==2){
                                    Log.d(TAG,"Prospecto a editar...");
                                    Log.d(TAG,"ProspectoEditar before:"+prospectoEditar.toString());
                                    if(prospectoEditar.getIdProspecto()>0){
                                        prospectoEditar.setEstado(2);
                                    }else{
                                        prospectoEditar.setEstado(1);
                                    }
                                    prospectoEditar.setIdAgente(rp3.configuration.PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
                                    prospectoEditar.setEstadoCode(Contants.GENERAL_VALUE_CODE_APTO_PROSPECCION);
                                    setDataToEdit();
                                    Log.d(TAG,"ProspectoEditar after:"+prospectoEditar.toString());
                                    //dataBase.beginTransactionNonExclusive();
                                    try {
                                        //do some insertions or whatever you need
                                        //dataBase.setTransactionSuccessful();
                                        //boolean resukt = ProspectoVtaDb.update(Utils.getDataBase(ProspectoActivity.this),prospectoEditar,ACTION_UPDATE);
                                        boolean resukt = ProspectoVtaDb.update(dataBase,prospectoEditar);
                                        System.out.print(prospectoEditar.getMessages());
                                        if(resukt){
                                            Log.d(TAG,"Se actualizo el prospecto a editar...");
                                            Log.d(TAG,prospectoEditar.toString());
                                        }else{
                                            Log.d(TAG,"No se actulizo prospecto a editar...");
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        //dataBase.endTransaction();
                                        dataBase.close();
                                    }

                                    setResult(RESULT_PROSPECTO_EDIT);
                                    finish();
                                }
                                else if(opcionProspectar==0){
                                    Log.d(TAG,"Prospecto a crear...");
                                    prospectoVta.setEstado(1);
                                    boolean result = ProspectoVtaDb.insert(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),prospectoVta);
                                    if(result){
                                        Log.d(TAG,"Se inserto el prospecto a crear...");
                                        Log.d(TAG,prospectoVta.toString());
                                    }else{
                                        Log.d(TAG,"No se inserto el prospecto a crear...");
                                    }
                                    setResult(RESULT_PROSPECTO_NUEVO);
                                    finish();
                                }
                            }else{
                                if(validateDocument){
                                    Log.d(TAG,"tiene documento y es natural...");
                                    if(opcionProspectar==2){
                                        Log.d(TAG,"Prospecto a editar...");
                                        Log.d(TAG,"ProspectoEditar before:"+prospectoEditar.toString());
                                        if(prospectoEditar.getIdProspecto()>0){
                                            prospectoEditar.setEstado(2);
                                        }else{
                                            prospectoEditar.setEstado(1);
                                        }
                                        prospectoEditar.setIdAgente(rp3.configuration.PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
                                        prospectoEditar.setEstadoCode("A");
                                        //prospectoEditar.setEstado(1);
                                        prospectoEditar.setEstadoCode(Contants.GENERAL_VALUE_CODE_APTO_PROSPECCION);
                                        prospectoEditar.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF);
                                        setDataToEdit();
                                        Log.d(TAG,"ProspectoEditar after:"+prospectoEditar.toString());
                                        //dataBase.beginTransactionNonExclusive();
                                        try {
                                            //do some insertions or whatever you need
                                            //dataBase.setTransactionSuccessful();
                                            //boolean resukt = prospectoEditar.update(Utils.getDataBase(ProspectoActivity.this),prospectoEditar,ACTION_UPDATE);
                                            boolean resukt = ProspectoVtaDb.update(dataBase,prospectoEditar);
                                            System.out.print(prospectoEditar.getMessages());
                                            if(resukt){
                                                Log.d(TAG,"Se actualizo el prospecto a editar...");
                                                Log.d(TAG,prospectoEditar.toString());
                                            }else{
                                                Log.d(TAG,"No se actulizo prospecto a editar...");
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        } finally {
                                            //dataBase.endTransaction();
                                            dataBase.close();
                                        }

                                        setResult(RESULT_PROSPECTO_EDIT);
                                        finish();
                                    }
                                    else if(opcionProspectar==0){
                                        Log.d(TAG,"Prospecto a crear...");
                                        prospectoVta.setEstado(1);
                                        boolean result = ProspectoVtaDb.insert(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),prospectoVta);
                                        if(result){
                                            Log.d(TAG,"Se inserto el prospecto a crear...");
                                            Log.d(TAG,prospectoVta.toString());
                                        }else{
                                            Log.d(TAG,"No se inserto el prospecto a crear...");
                                        }
                                        setResult(RESULT_PROSPECTO_NUEVO);
                                        finish();
                                    }
                                }else{
                                    Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_validate, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            if(validateDocument){
                                Log.d(TAG,"tiene documento y es natural...");
                                if(opcionProspectar==2){
                                    Log.d(TAG,"Prospecto a editar...");
                                    Log.d(TAG,"ProspectoEditar before:"+prospectoEditar.toString());
                                    if(prospectoEditar.getIdProspecto()>0){
                                        prospectoEditar.setEstado(2);
                                    }else{
                                        prospectoEditar.setEstado(1);
                                    }

                                    prospectoEditar.setIdAgente(rp3.configuration.PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
                                    prospectoEditar.setEstadoCode("A");
                                    //prospectoEditar.setEstado(1);
                                    prospectoEditar.setEstadoCode(Contants.GENERAL_VALUE_CODE_APTO_PROSPECCION);
                                    if(prospectoEditar.getOrigenCode().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF)){
                                        prospectoEditar.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF);
                                    }else{
                                        prospectoEditar.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_MKF_MOVIL);
                                    }
                                    setDataToEdit();
                                    Log.d(TAG,"ProspectoEditar after:"+prospectoEditar.toString());
                                    //dataBase.beginTransactionNonExclusive();
                                    try {
                                        //do some insertions or whatever you need
                                        //dataBase.setTransactionSuccessful();
                                        //boolean resukt = prospectoEditar.update(Utils.getDataBase(ProspectoActivity.this),prospectoEditar,ACTION_UPDATE);
                                        boolean resukt = ProspectoVtaDb.update(dataBase,prospectoEditar);;
                                        System.out.print(prospectoEditar.getMessages());
                                        if(resukt){
                                            Log.d(TAG,"Se actualizo el prospecto a editar...");
                                            Log.d(TAG,prospectoEditar.toString());
                                        }else{
                                            Log.d(TAG,"No se actulizo prospecto a editar...");
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    } finally {
                                        //dataBase.endTransaction();
                                        dataBase.close();
                                    }
                                    setResult(RESULT_PROSPECTO_EDIT);
                                    finish();
                                }
                                else if(opcionProspectar==0){
                                    Log.d(TAG,"Prospecto a crear...");
                                    prospectoVta.setEstado(1);
                                    boolean result = ProspectoVtaDb.insert(Utils.getDataBase(getApplicationContext()),prospectoVta);
                                    if(result){
                                        Log.d(TAG,"Se inserto el prospecto a crear...");
                                        Log.d(TAG,prospectoVta.toString());
                                    }else{
                                        Log.d(TAG,"No se inserto el prospecto a crear...");
                                    }
                                    setResult(RESULT_PROSPECTO_NUEVO);
                                    finish();
                                }
                            }else{
                                Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_validate, Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                    else if (tipo.equalsIgnoreCase("N")){
                        Log.d(TAG,"Solo es Es natural...");
                        Log.d(TAG,"SET RESULT Natural...");
                        if(opcionProspectar==2){
                            Log.d(TAG,"Prospecto a editar...");
                            prospectoEditar.setEstadoCode("A");
                            Log.d(TAG,"ProspectoEditar before:"+prospectoEditar.toString());
                            if(prospectoEditar.getIdProspecto()>0){
                                prospectoEditar.setEstado(2);
                            }else{
                                prospectoEditar.setEstado(1);
                            }
                            setDataToEdit();
                            Log.d(TAG,"ProspectoEditar after:"+prospectoEditar.toString());
                            //dataBase.beginTransactionNonExclusive();
                            try {
                                //do some insertions or whatever you need
                                //dataBase.setTransactionSuccessful();
                                //boolean resukt = prospectoEditar.update(Utils.getDataBase(ProspectoActivity.this),prospectoEditar,ACTION_UPDATE);
                                boolean resukt = ProspectoVtaDb.update(dataBase,prospectoEditar);
                                System.out.print(prospectoEditar.getMessages());
                                if(resukt){
                                    Log.d(TAG,"Se actualizo el prospecto a editar...");
                                    Log.d(TAG,prospectoEditar.toString());
                                }else{
                                    Log.d(TAG,"No se actulizo prospecto a editar...");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            } finally {
                                //dataBase.endTransaction();
                                dataBase.close();
                            }
                            //boolean resukt = ProspectoVtaDb.update(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),prospectoEditar);
                            setResult(RESULT_PROSPECTO_EDIT);
                            finish();
                        }
                        else if(opcionProspectar==0){
                            Log.d(TAG,"Prospecto a crear...");
                            prospectoVta.setEstado(1);
                            boolean result = ProspectoVtaDb.insert(Utils.getDataBase(getApplicationContext()),prospectoVta);
                            if(result){
                                Log.d(TAG,"Se inserto el prospecto a crear...");
                                Log.d(TAG,prospectoVta.toString());
                            }else{
                                Log.d(TAG,"No se inserto el prospecto a crear...");
                            }
                            setResult(RESULT_PROSPECTO_NUEVO);
                            finish();
                        }/*else if(opcionProspectar==3){
                                showAlertDialogReferidoConfirm();
                            }*/
                    }else {
                        Log.d(TAG, "Es juridico...");
                        Log.d(TAG,"SET RESULT Juridico...");
                        if(opcionProspectar==2){
                            Log.d(TAG,"Prospecto a editar...");
                            Log.d(TAG,"ProspectoEditar before:"+prospectoEditar.toString());
                            if(prospectoEditar.getIdProspecto()>0){
                                prospectoEditar.setEstado(2);
                            }else{
                                prospectoEditar.setEstado(1);
                            }

                            prospectoEditar.setEstadoCode("A");
                            setDataToEdit();
                            Log.d(TAG,"ProspectoEditar after:"+prospectoEditar.toString());
                            //dataBase.beginTransactionNonExclusive();
                            try {
                                //do some insertions or whatever you need
                                //dataBase.setTransactionSuccessful();
                                //boolean resukt = prospectoEditar.update(Utils.getDataBase(ProspectoActivity.this),prospectoEditar,ACTION_UPDATE);
                                boolean resukt = ProspectoVtaDb.update(dataBase,prospectoEditar);
                                System.out.print(prospectoEditar.getMessages());
                                if(resukt){
                                    Log.d(TAG,"Se actualizo el prospecto a editar...");
                                    Log.d(TAG,prospectoEditar.toString());
                                }else{
                                    Log.d(TAG,"No se actulizo prospecto a editar...");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            } finally {
                                //dataBase.endTransaction();
                                dataBase.close();
                            }

                            setResult(RESULT_PROSPECTO_EDIT);
                            finish();
                        }
                        else if(opcionProspectar==0){
                            Log.d(TAG,"Prospecto a crear...");
                            prospectoVta.setEstado(1);
                            ProspectoVtaDb.insert(DataBase.newDataBase(rp3.auna.db.DbOpenHelper.class),prospectoVta);
                            setResult(RESULT_PROSPECTO_NUEVO);
                            finish();
                        }
                            /*else if(opcionProspectar==3){
                                showAlertDialogReferidoConfirm();
                            }*/
                    }
                    Log.d(TAG,"json prospecto:"+json);
                }
            })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG,"onClickDialog No...");
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void setDataToEdit(){
        if(tipoPersonaCode.equalsIgnoreCase("N")){
            prospectoEditar.setTipoPersonaCode(tipoPersonaCode);
            String nombre = etNombre.getText().toString().trim()+" "+etApellidoPaterno.getText().toString().trim();
            String celular = etCelular.getText().toString().trim();
            if(!TextUtils.isEmpty(etApellidoMaterno.getText())){
                if(etApellidoMaterno.getText().toString().trim().length()>0){
                    nombre+= " "+etApellidoMaterno.getText().toString().trim();
                    prospectoEditar.setApellidoMaterno(etApellidoMaterno.getText().toString().trim());
                    prospectoEditar.setContactoApellidoMaterno(etApellidoMaterno.getText().toString().trim());
                }else{
                    //Si el campo de texto lo borro asi haya tenido dato eliminarlo
                    prospectoEditar.setApellidoMaterno("");
                    prospectoEditar.setContactoApellidoMaterno("");
                }
            }
            else{
                //Si el campo de texto lo borro asi haya tenido dato eliminarlo
                prospectoEditar.setApellidoMaterno("");
                prospectoEditar.setContactoApellidoMaterno("");
            }
            prospectoEditar.setNombre(nombre);
            prospectoEditar.setApellidoPaterno(etApellidoPaterno.getText().toString());
            prospectoEditar.setNombres(etNombre.getText().toString());
            prospectoEditar.setContactoNombre(etNombre.getText().toString().trim());
            prospectoEditar.setContactoApellidoPaterno(etApellidoPaterno.getText().toString().trim());
            prospectoEditar.setCelular(celular);
            if(idTipoDocumentoSelected!=0 && !TextUtils.isEmpty(etDocumento.getText()) && identificationType!=null){
                int minLength = identificationType.getMaskType();
                int maxLength = identificationType.getLenght();
                if(idTipoDocumentoSelected == 1){
                    if(etDocumento.getText().toString().trim().length()==minLength){
                        prospectoEditar.setDocumento(etDocumento.getText().toString());
                        prospectoEditar.setTipoDocumento(idTipoDocumentoSelected);
                    }
                    else{
                        etDocumento.setError("Requiere 8 digitos");
                        Log.d(TAG,"Requiere 8 digitos...");
                        return;
                    }
                }else{
                    if(etDocumento.getText().toString().trim().length()<minLength){
                        etDocumento.setError("Requiere digitos "+minLength+" minimos");
                        return;
                    }else{
                        prospectoEditar.setTipoDocumento(idTipoDocumentoSelected);
                        prospectoEditar.setDocumento(etDocumento.getText().toString().trim());
                    }
                }
            }
            if(!TextUtils.isEmpty(etTelefono.getText())){prospectoEditar.setTelefono(etTelefono.getText().toString());}
            else{prospectoEditar.setTelefono("");}
            if(!TextUtils.isEmpty(etDireccion1.getText())){prospectoEditar.setDireccion1(etDireccion1.getText().toString().trim());}
            else{prospectoEditar.setDireccion1("");}
            if(!TextUtils.isEmpty(etDireccion2.getText())){prospectoEditar.setDireccion2(etDireccion2.getText().toString().trim());}
            else{prospectoEditar.setDireccion2("");}
            if(!TextUtils.isEmpty(etCentroTrabajo.getText())){prospectoEditar.setRazonSocial(etCentroTrabajo.getText().toString().trim());}
            if(!TextUtils.isEmpty(etEmailNatural.getText())) {
                if(Utils.validarCorreo(etEmailNatural.getText().toString().trim()) &&
                        android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailNatural.getText().toString().trim()).matches()){
                    prospectoEditar.setEmail(etEmailNatural.getText().toString().trim());
                }else{
                    etEmailNatural.setError("Ingrese un correo valido");
                    return;
                }
            }else{
                prospectoEditar.setEmail("");
            }
        }
        else{
            prospectoEditar.setTipoPersonaCode(tipoPersonaCode);
            String nombreEmpresa = etEmpresa.getText().toString().trim();
            String nombreContacto = etNombreContacto.getText().toString().trim();
            String apellidoPaterno = etApellidoPaternoContacto.getText().toString().trim();
            String numeroEmpresa = etNumeroEmpresa.getText().toString().trim();
            String numeroContacto = etNumeroContacto.getText().toString().trim();
            prospectoEditar.setRazonSocial(nombreEmpresa);
            prospectoEditar.setContactoApellidoPaterno(apellidoPaterno);
            prospectoEditar.setContactoNombre(nombreContacto);
            prospectoEditar.setCelular(numeroContacto);
            if(!TextUtils.isEmpty(etApellidoMaternoContacto.getText())){prospectoEditar.setContactoApellidoMaterno(etApellidoMaternoContacto.getText().toString().trim());}
            else{prospectoEditar.setContactoApellidoMaterno("");}
            if(!TextUtils.isEmpty(etRuc.getText())){
                if(etRuc.getText().length()==11){
                    prospectoEditar.setRuc(etRuc.getText().toString().trim());
                }else{
                    Toast.makeText(this, "Porfavor Ingrese el numero de RUC correctamente", Toast.LENGTH_SHORT).show();
                }

            }else{prospectoEditar.setRuc("");}
            if(!TextUtils.isEmpty(etDireccion1J.getText())){prospectoEditar.setDireccion1(etDireccion1.getText().toString().trim());}
            else{prospectoEditar.setDireccion1("");}
            if(!TextUtils.isEmpty(etDireccion2J.getText())){prospectoEditar.setDireccion2(etDireccion2.getText().toString().trim());}
            else{prospectoEditar.setDireccion2("");}
            if(!TextUtils.isEmpty(etEmailJuridico.getText())) {
                if(Utils.validarCorreo(etEmailJuridico.getText().toString().trim())&&
                        android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailJuridico.getText().toString().trim()).matches() ){
                    prospectoEditar.setEmail(etEmailJuridico.getText().toString().trim());
                }else{
                    etEmailJuridico.setError("Ingrese un correo valido");
                    return;
                }
            }else{
                prospectoEditar.setEmail("");
            }
        }
        prospectoEditar.setEstadoCode("A");
        if(prospectoEditar.getOrigenCode().equalsIgnoreCase(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF)){
            prospectoEditar.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF);
        }else{
            prospectoEditar.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_MKF_MOVIL);
        }

        Log.d(TAG,"DataEditar:"+prospectoEditar.toString());
    }

    //endregion

    //region Events Text

    /*private void setInputEditText(){
        etDocumento.setImeActionLabel("Consultar", EditorInfo.IME_ACTION_DONE);
        etDocumento.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etDocumento.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d(TAG,"onDocumentDone...");
                Log.d(TAG,"actionId:"+actionId);
                Log.d(TAG,"Event:"+event.toString());
                Log.d(TAG,"View editText:"+v.getId());
                onNextClick(message);
                handled = true;
            }
            return handled;
        });
    }*/

    private void filterDocumentMinMax(){

    }

    @OnTextChanged(R.id.etDocumento)
    void onDniTextChanged(CharSequence documentoCode, int start, int count, int after) {
        Log.d(TAG,"onDniTextChanged...");
        Log.d(TAG,"Chansequence:"+documentoCode+" Start:"+start+" Count:"+count+" After:"+after);
        Log.d(TAG,"lenth:"+(documentoCode.toString().trim().length()));
        Log.d(TAG,"idTipoDocumento:"+idTipoDocumentoSelected);


        if(identificationType!=null){
            int minLegnth = identificationType.getMaskType();
            int maxLenght = identificationType.getLenght();
            Log.d(TAG,"identificationType!=null...");
            //region Validar si es menor a 8 cuando sea documento
            if(idTipoDocumentoSelected>0){
                if(idTipoDocumentoSelected==1 && count<minLegnth){
                    validateDocument = true;
                    flagDocumento = 0;
                }else {
                    if(count<=minLegnth){
                        validateDocument = true;
                        flagDocumento = 0;
                    }
                }
            }else{
                validateDocument = true;
                flagDocumento = 0;
            }
            //endregion

            //
            if(opcionProspectar==2){
                Log.d(TAG,"es Editar prospecto...");
                //region Validacion del documento cuando se edita un prospecto
                if(prospectoEditar!=null){
                    if(prospectoEditar.getDocumento()!=null){
                        Log.d(TAG,"prospectoEditar.getDocumento!n=null...");
                        if(documentEditarFirstTime == false){
                            Log.d(TAG,"documentEditarFirstTime==false...");
                            int length = (documentoCode.toString().trim().length());
                            if(length == minLegnth && idTipoDocumentoSelected==1){
                                if(documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar)){
                                    Log.d(TAG,"documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar),CHanged == 8...");
                                    if(flagShowCalificacion==0){
                                        Util.dismissKeyboard(ProspectoActivity.this);
                                        flagDocumento = 0;
                                        validateDocument = true;
                                        showCalificacionClient();
                                    }
                                }else{
                                    Log.d(TAG,"!!documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar)CHanged == 8...");
                                    if(flagDocumentValidate==0){
                                        flagDocumento = -1;
                                        validateDocument = false;
                                        showValidateDocumentClient(documentoCode.toString());
                                        Util.dismissKeyboard(ProspectoActivity.this);
                                    }

                                }
                            }
                            /*else if (length>maxLenght && idTipoDocumentoSelected>1){
                                Log.d(TAG,"length>5 && idTipoDocumentoSelected>1...");
                                if(flagDocumentValidate==0){
                                    flagDocumento = -1;
                                    validateDocument = false;
                                    showValidateDocumentClient(documentoCode.toString());
                                    Util.dismissKeyboard(ProspectoActivity.this);
                                }
                            }*/
                        }else{
                            documentEditarFirstTime = false;
                            Log.d(TAG,"documentEditarFirstTime==true...");
                            int length = (documentoCode.toString().trim().length());
                            if(length == minLegnth && idTipoDocumentoSelected==1){
                                Log.d(TAG,"length == 8 && idTipoDocumentoSelected==1...");
                                if(documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar)){
                                    Log.d(TAG,"documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar) CHanged == 8...");
                                    Util.dismissKeyboard(ProspectoActivity.this);
                                    if(flagShowCalificacion==0){
                                        showCalificacionClient();
                                    }
                                }
                                else{
                                    Log.d(TAG,"!!documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar)CHanged == 8...");
                                    if(flagDocumentValidate==0){
                                        flagDocumento = -1;
                                        validateDocument = false;
                                        showValidateDocumentClient(documentoCode.toString());
                                        Util.dismissKeyboard(ProspectoActivity.this);
                                    }
                                }
                            }/*else if (length>minLegnth && idTipoDocumentoSelected>1){
                                Log.d(TAG,"length>5 && idTipoDocumentoSelected>1...");
                                if(flagDocumentValidate==0){
                                    flagDocumento = -1;
                                    validateDocument = false;
                                    showValidateDocumentClient(documentoCode.toString());
                                    Util.dismissKeyboard(ProspectoActivity.this);
                                }
                            }*/
                        }
                    }
                }
                //endregion
            }else{
                Log.d(TAG,"es crear prospecto...");
                //region Validacion del documento cuando se crea un prospecto
                int length = (documentoCode.toString().trim().length());
                if(length == minLegnth && idTipoDocumentoSelected==1){
                    Log.d(TAG,"length == 8 && idTipoDocumentoSelected==1...");
                    Log.d(TAG,"CHanged == 8...");
                    if(flagDocumentValidate==0){
                        flagDocumento = -1;
                        validateDocument = false;
                        showValidateDocumentClient(documentoCode.toString());
                        Util.dismissKeyboard(ProspectoActivity.this);
                    }
                }
                /*else if (length>minLegnth && idTipoDocumentoSelected>1){
                    Log.d(TAG,"length>5 && idTipoDocumentoSelected>1...");
                    if(flagDocumentValidate==0){
                        flagDocumento = -1;
                        validateDocument = false;
                        showValidateDocumentClient(documentoCode.toString());
                        Util.dismissKeyboard(ProspectoActivity.this);
                    }
                }*/
                //endregion
            }
        }



    }

    @OnEditorAction(value = R.id.etDocumento)
    boolean onDocumentDone(EditText v, int actionId, android.view.KeyEvent event) {
        Log.d(TAG,"onDocumentDone...");
        Log.d(TAG,"actionId:"+actionId);
        if(event!=null){
            Log.d(TAG,"event!=null...");
            Log.d(TAG,"Event:"+event.toString());
        }else{
            Log.d(TAG,"event==null");
        }
        //Log.d(TAG,"Event:"+event.toString());
        Log.d(TAG,"View editText:"+v.getId());
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            //do here your stuff f
            Log.d(TAG,"actionId == EditorInfo.IME_ACTION_DONE...");
            //Establecer variables de EditText.getCount, minLenght and maxLegnth
            if(identificationType!=null){
                Log.d(TAG,"identificationType!=null...");
                int length = etDocumento.getText().toString().trim().length();
                int minLength = identificationType.getMaskType();
                int maxLength = identificationType.getLenght();
                Log.d(TAG,"Lenght:"+length);
                Log.d(TAG,"minLength:"+minLength);
                Log.d(TAG,"maxLength:"+maxLength);
                Log.d(TAG,"idTipoDocumentoSelected:"+idTipoDocumentoSelected);
                //region Validar si es menor a 8 cuando sea documento
                if(idTipoDocumentoSelected>0){
                    if(idTipoDocumentoSelected==1 && length<minLength){
                        validateDocument = true;
                        flagDocumento = 0;
                    }else {
                        if(length<=minLength){
                            validateDocument = true;
                            flagDocumento = 0;
                        }
                    }
                }else{
                    validateDocument = true;
                    flagDocumento = 0;
                }
                //endregion
                //
                if(opcionProspectar==2){
                    Log.d(TAG,"es Editar prospecto...");
                    //region Validacion del documento cuando se edita un prospecto
                    if(prospectoEditar!=null){
                        if(prospectoEditar.getDocumento()!=null){
                            Log.d(TAG,"prospectoEditar.getDocumento!n=null...");
                            documentEditarFirstTime = true;
                            if(documentEditarFirstTime == false){

                                Log.d(TAG,"documentEditarFirstTime==false...");
                                //int length = (documentoCode.toString().trim().length());
                                /*if(length == minLength && idTipoDocumentoSelected==1){
                                    if(etDocumento.getText().toString().trim().equalsIgnoreCase(documentoProspectoEditar)){
                                        Log.d(TAG,"documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar),CHanged == 8...");
                                        if(flagShowCalificacion==0){
                                            Util.dismissKeyboard(ProspectoActivity.this);
                                            flagDocumento = 0;
                                            validateDocument = true;
                                            showCalificacionClient();
                                        }
                                    }else{
                                        Log.d(TAG,"!!documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar)CHanged == 8...");
                                        if(flagDocumentValidate==0){
                                            flagDocumento = -1;
                                            validateDocument = false;
                                            showValidateDocumentClient(etDocumento.getText().toString().trim());
                                            Util.dismissKeyboard(ProspectoActivity.this);
                                        }

                                    }
                                }
                                else */if (length>=minLength && idTipoDocumentoSelected>1){
                                    Log.d(TAG,"length>5 && idTipoDocumentoSelected>1...");
                                    if(flagDocumentValidate==0){
                                        flagDocumento = -1;
                                        validateDocument = false;
                                        showValidateDocumentClient(etDocumento.getText().toString().trim());
                                        Util.dismissKeyboard(ProspectoActivity.this);
                                    }
                                }
                            }else{
                                //documentEditarFirstTime = false;
                                Log.d(TAG,"documentEditarFirstTime==true...");
                                //int length = (documentoCode.toString().trim().length());
                                /*if(length == minLength && idTipoDocumentoSelected==1){
                                    Log.d(TAG,"length == 8 && idTipoDocumentoSelected==1...");
                                    if(etDocumento.getText().toString().trim().equalsIgnoreCase(documentoProspectoEditar)){
                                        Log.d(TAG,"documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar) CHanged == 8...");
                                        Util.dismissKeyboard(ProspectoActivity.this);
                                        if(flagShowCalificacion==0){
                                            showCalificacionClient();
                                        }
                                    }
                                    else{
                                        Log.d(TAG,"!!documentoCode.toString().equalsIgnoreCase(documentoProspectoEditar)CHanged == 8...");
                                        if(flagDocumentValidate==0){
                                            flagDocumento = -1;
                                            validateDocument = false;
                                            showValidateDocumentClient(etDocumento.getText().toString().trim());
                                            Util.dismissKeyboard(ProspectoActivity.this);
                                        }
                                    }
                                }else*/ if (length>=minLength && idTipoDocumentoSelected>1){
                                    Log.d(TAG,"length>5 && idTipoDocumentoSelected>1...");
                                    if(flagDocumentValidate==0){
                                        flagDocumento = -1;
                                        validateDocument = false;
                                        showValidateDocumentClient(etDocumento.getText().toString().trim());
                                        Util.dismissKeyboard(ProspectoActivity.this);
                                    }
                                }
                            }
                        }
                    }
                    //endregion
                }else{
                    Log.d(TAG,"es crear prospecto...");
                    //region Validacion del documento cuando se crea un prospecto
                    //int length = (documentoCode.toString().trim().length());
                    if(length == minLength && idTipoDocumentoSelected==1){
                        Log.d(TAG,"length == 8 && idTipoDocumentoSelected==1...");
                        Log.d(TAG,"CHanged == 8...");
                        if(flagDocumentValidate==0){
                            flagDocumento = -1;
                            validateDocument = false;
                            showValidateDocumentClient(etDocumento.getText().toString().trim());
                            Util.dismissKeyboard(ProspectoActivity.this);
                        }
                    }
                    else if (length>=minLength && idTipoDocumentoSelected>1){
                        Log.d(TAG,"length>5 && idTipoDocumentoSelected>1...");
                        if(flagDocumentValidate==0){
                            flagDocumento = -1;
                            validateDocument = false;
                            showValidateDocumentClient(etDocumento.getText().toString().trim());
                            Util.dismissKeyboard(ProspectoActivity.this);
                        }
                    }
                    //endregion
                }
            }

            return true;
        }
        return false;
    }


    //region Events Not Used.
    /*@OnTextChanged(R.id.etNombre)
    void onNombreNaturalChanged(CharSequence documentoCode, int start, int count, int after){
        Log.d(TAG,"onNombreNaturalChanged...");
        Log.d(TAG,"Chansequence:"+documentoCode+" Start:"+start+" Count:"+count+" After:"+after);
        Log.d(TAG,"lenth:"+(documentoCode.toString().trim().length()));
        if(documentoCode!=null){
            String str = Util.replaceNumeric(String.valueOf(documentoCode));
            etNombre.setText(str);
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }*/
    //endregion

    private void setTextCapFields(){
        Log.d(TAG,"setTextCapFields...");
        /*etNombre.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable et) {
                String s=et.toString();
                Log.d(TAG,"onNombreNaturalChanged...");
                if(s!=null){
                    Log.d(TAG,"EditableString:"+s);
                    Log.d(TAG,"lenth:"+(s.toString().trim().length()));
                    String str = Util.replaceNumeric(String.valueOf(s));
                    etNombre.setText(str);
                }
            }
        });*/
    }

    //endregion

    //region Natural

    private boolean validateNatural(){
        if(TextUtils.isEmpty(etNombre.getText())){etNombre.setError("Campo obligatorio");return false;}
        if(TextUtils.isEmpty(etApellidoPaterno.getText())){etApellidoPaterno.setError("Campo obligatorio");return false;}
        if(TextUtils.isEmpty(etCelular.getText())){etCelular.setError("Campo obligatorio");return false;}
        if(etCelular.getText().toString().trim().length()<6){etCelular.setError("Se requieren 6 digitos minimo");}
        return true;
    }

    private void dismissNatural(){
        Log.d(TAG,"dismissNatural...");
        containerDocumento.setVisibility(View.GONE);
        containerInformacion.setVisibility(View.GONE);
        containerTelefono.setVisibility(View.GONE);
        containerDireccion.setVisibility(View.GONE);
        containerTvInformacion.setVisibility(View.GONE);
        containerTvTelefono.setVisibility(View.GONE);
        containerTvDireccion.setVisibility(View.GONE);
        tvInformacion.setVisibility(View.GONE);
    }

    private void initCleanViewsNatural(){
        Log.d(TAG,"initViewsNatural...");
        tvInformacion.setText("Informacion");
        etDocumento.setText(null);
        etNombre.setText(null);
        etApellidoPaterno.setText(null);
        etApellidoMaterno.setText(null);
        etCelular.setText(null);
        etTelefono.setText(null);
        etDireccion1.setText(null);
        etDireccion2.setText(null);
        etCentroTrabajo.setText(null);
        etEmailNatural.setText(null);
    }

    private void showViewsNatural(){
        Log.d(TAG,"showViewsNatural...");
        containerDocumento.setVisibility(View.VISIBLE);
        containerInformacion.setVisibility(View.VISIBLE);
        containerTelefono.setVisibility(View.VISIBLE);
        containerDireccion.setVisibility(View.VISIBLE);
        containerTvInformacion.setVisibility(View.VISIBLE);
        containerTvTelefono.setVisibility(View.VISIBLE);
        containerTvDireccion.setVisibility(View.VISIBLE);
        tvInformacion.setVisibility(View.VISIBLE);
        tvInformacion.setText("Información");
        listTipoDocumento = new ArrayList<>();
        listTipoDocumento.add("Seleccione");
        for(IdentificationType doc:documentos){
            listTipoDocumento.add(doc.getName());
        }
        /*listTipoDocumento.add("Seleccione");
        listTipoDocumento.add("DNI");
        listTipoDocumento.add("OTROS");*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.tv_spinner,listTipoDocumento);
        spTipoDocumento.setAdapter(adapter);
        spTipoDocumento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean estado = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"onItemSelected first time in spDocument...");
                /*if(estado){
                    Log.d(TAG,"OnItemSelected primera vez...");
                    estado = false;
                }else{*/
                try{
                    if(position==0){
                        Log.d(TAG,"position == 0...");
                        tipoDocumentoSelected = null;
                        idTipoDocumentoSelected = 0;
                        etDocumento.setText(null);
                        identificationType = null;
                    }else {
                        Log.d(TAG,"position == "+position);
                        int i = position - 1;
                        identificationType = documentos.get(i);
                        Log.d(TAG,"identificationType Select:"+identificationType.toString());

                        //region Editar prospecto Identificación
                        if(opcionProspectar==2){
                            Log.d(TAG,"opcionProspectar==2...");
                            if(prospectoEditar!=null){
                                Log.d(TAG,"prospectoEditar!=null...");
                                if(prospectoEditar.getDocumento()!=null){
                                    Log.d(TAG,"prospectoEditar.getDocumento()!=null...");
                                    //Cuenta con documento
                                    if(prospectoEditar.getDocumento().length()>0){
                                        Log.d(TAG,"prospectoEditar.getDocumento().length()>0...");
                                        if(documentEditarFirstTime){
                                            Log.d(TAG,"documentEditarFirstTime true...");
                                            documentEditarFirstTime = false;
                                            etDocumento.setText(prospectoEditar.getDocumento());
                                            documentoProspectoEditar = prospectoEditar.getDocumento();
                                            validateDocument = true;
                                            Log.d(TAG, "onItemSelected..." + listTipoDocumento.get(position));
                                            tipoDocumentoSelected = listTipoDocumento.get(position);
                                            idTipoDocumentoSelected = (int) documentos.get(i).getID();
                                            Log.d(TAG,"idTIpoDocumentoSelected:"+idTipoDocumentoSelected);
                                            int maxLength = identificationType.getLenght();
                                            if(idTipoDocumentoSelected==1){
                                                //maxLength = 8;
                                                MinMaxFilter filter = new MinMaxFilter(8,8);
                                                InputFilter[] fArray = new InputFilter[1];
                                                //fArray[0] = filter;
                                                fArray[0] = new InputFilter.LengthFilter(maxLength);
                                                etDocumento.setFilters(fArray);
                                                etDocumento.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            }else{
                                                //maxLength = identificationType.getLenght();
                                                InputFilter[] fArray = new InputFilter[1];
                                                fArray[0] = new InputFilter.LengthFilter(maxLength);
                                                etDocumento.setFilters(fArray);
                                                etDocumento.setInputType(InputType.TYPE_CLASS_TEXT);
                                            }
                                        }else{
                                            Log.d(TAG,"documentEditarFirstTime false...");
                                            //etDocumento.setText(null);
                                            Log.d(TAG, "onItemSelected..." + listTipoDocumento.get(position));
                                            tipoDocumentoSelected = listTipoDocumento.get(position);
                                            //int i = position - 1;
                                            idTipoDocumentoSelected = (int) documentos.get(i).getID();
                                            Log.d(TAG,"idTIpoDocumentoSelected:"+idTipoDocumentoSelected);
                                            int maxLength = identificationType.getLenght();
                                            if(idTipoDocumentoSelected==1){
                                                //maxLength = 8;
                                                MinMaxFilter filter = new MinMaxFilter(8,8);
                                                InputFilter[] fArray = new InputFilter[1];
                                                fArray[0] = new InputFilter.LengthFilter(maxLength);
                                                //fArray[0] = filter;
                                                etDocumento.setFilters(fArray);
                                                etDocumento.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            }else{
                                                //maxLength = 15;
                                                maxLength = identificationType.getLenght();
                                                InputFilter[] fArray = new InputFilter[1];
                                                fArray[0] = new InputFilter.LengthFilter(maxLength);
                                                etDocumento.setFilters(fArray);
                                                etDocumento.setInputType(InputType.TYPE_CLASS_TEXT);
                                            }
                                        }
                                    }
                                    //Este prospecto a editar no cuenta con Documento
                                    else{
                                        etDocumento.setText(null);
                                        Log.d(TAG, "onItemSelected..." + listTipoDocumento.get(position));
                                        tipoDocumentoSelected = listTipoDocumento.get(position);
                                        //int i = position - 1;
                                        idTipoDocumentoSelected = (int) documentos.get(i).getID();
                                        Log.d(TAG,"idTIpoDocumentoSelected:"+idTipoDocumentoSelected);
                                        int maxLength = identificationType.getLenght();
                                        if(idTipoDocumentoSelected==1){
                                            //maxLength = 8;
                                            MinMaxFilter filter = new MinMaxFilter(8,8);
                                            InputFilter[] fArray = new InputFilter[1];
                                            fArray[0] = new InputFilter.LengthFilter(maxLength);
                                            //fArray[0] = filter;
                                            etDocumento.setFilters(fArray);
                                            etDocumento.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        }else{
                                            //maxLength = 15;
                                            maxLength = identificationType.getLenght();
                                            InputFilter[] fArray = new InputFilter[1];
                                            fArray[0] = new InputFilter.LengthFilter(maxLength);
                                            etDocumento.setFilters(fArray);
                                            etDocumento.setInputType(InputType.TYPE_CLASS_TEXT);
                                        }
                                    }
                                }
                                //Este prospecto a editar no cuenta con Documento
                                else{
                                    etDocumento.setText(null);
                                    Log.d(TAG, "onItemSelected..." + listTipoDocumento.get(position));
                                    tipoDocumentoSelected = listTipoDocumento.get(position);
                                    //int i = position - 1;
                                    idTipoDocumentoSelected = (int) documentos.get(i).getID();
                                    Log.d(TAG,"idTIpoDocumentoSelected:"+idTipoDocumentoSelected);
                                    int maxLength = identificationType.getLenght();
                                    if(idTipoDocumentoSelected==1){
                                        //maxLength = 8;
                                        MinMaxFilter filter = new MinMaxFilter(8,8);
                                        InputFilter[] fArray = new InputFilter[1];
                                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                                        //fArray[0] = filter;
                                        etDocumento.setFilters(fArray);
                                        etDocumento.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    }else{
                                        //maxLength = 15;
                                        maxLength = identificationType.getLenght();
                                        InputFilter[] fArray = new InputFilter[1];
                                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                                        etDocumento.setFilters(fArray);
                                        etDocumento.setInputType(InputType.TYPE_CLASS_TEXT);
                                    }
                                }
                            }
                        }
                        //endregion

                        //region Agregar prospecto Identificación
                        else{
                            etDocumento.setText(null);
                            Log.d(TAG, "onItemSelected..." + listTipoDocumento.get(position));
                            tipoDocumentoSelected = listTipoDocumento.get(position);
                            //int i = position - 1;
                            idTipoDocumentoSelected = (int) documentos.get(i).getID();
                            //

                            identificationType = documentos.get(i);
                            //
                            Log.d(TAG,"idTIpoDocumentoSelected:"+idTipoDocumentoSelected);

                            int maxLength = identificationType.getLenght();
                            if(idTipoDocumentoSelected==1){
                                //maxLength = 8;
                                MinMaxFilter filter = new MinMaxFilter(8,8);
                                InputFilter[] fArray = new InputFilter[1];
                                fArray[0] = new InputFilter.LengthFilter(maxLength);
                                //fArray[0] = filter;
                                etDocumento.setFilters(fArray);
                                etDocumento.setInputType(InputType.TYPE_CLASS_NUMBER);
                            }else{
                                //maxLength = 15;
                                maxLength = identificationType.getLenght();
                                InputFilter[] fArray = new InputFilter[1];
                                fArray[0] = new InputFilter.LengthFilter(maxLength);
                                etDocumento.setFilters(fArray);
                                etDocumento.setInputType(InputType.TYPE_CLASS_TEXT);
                            }
                        }
                        //endregion
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                //}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG,"onNothingSelected...");
            }
        });
        validateProspecto();
    }

    private void showCalificacionClient(){
        Log.d(TAG,"SETRESULT Natural show ClienteCafiliacion.....");
        flagShowCalificacion = 1;
        final ProgressDialog progressDialogg =new ProgressDialog(ProspectoActivity.this,R.style.AppCompatAlertDialogStyle);
        progressDialogg.setTitle(this.getResources().getString(R.string.appname_marketforce));
        progressDialogg.setMessage("Consultando documento...");
        progressDialogg.setCancelable(false);
        progressDialogg.show();
        new ProspectoCalificacionAfiliacionClient(getApplicationContext(), new Callback() {
            Handler mHandler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialogg.dismiss();
                Log.d(TAG,"onFailure..."+e.getMessage());
                mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"SET RESULT Natural...");
                            //prospectoVta.setEstado(1);
                            Toast.makeText(ProspectoActivity.this, "Hubo un problema en el servidor al consultar este documento. Intentelo mas tarde", Toast.LENGTH_SHORT).show();
                            //prospectoVta.insert(Utils.getDataBase(getApplicationContext()),prospectoVta);
                            //setResult(RESULT_PROSPECTO_NUEVO);
                            //finish();*/

                        }
                    });
                flagShowCalificacion = 0;
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                flagShowCalificacion = 0;
                progressDialogg.dismiss();
                Log.d(TAG,"onResponse...");
                Log.d(TAG,"statusCode="+response.code());
                final String json = response.body().string();
                Log.d(TAG,json);
                mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(response.isSuccessful()){
                                Log.d(TAG,"Yes is Successful...");
                                InVentasProspecto response = new Gson().fromJson(json, InVentasProspecto.class);
                                showDialogBuildCalificacionDialog(response);
                                //region Antiguo response
                                /*
                                if(!TextUtils.isEmpty(response.getNRO_DOCIDENTIDAD())){
                                    Log.d(TAG,"vacio no hay respuesta...");
                                    String msj = "CALIFICACION \n"
                                            +"SEGMENTO:"+response.getCOD_SEGMENTO()+"\n"
                                            +"SBS"+response.getIND_CALIF_SBS()+"\n"
                                            +"TDC:"+response.getIND_CALIF_TDC()+"\n"
                                            +"DATOS DE ORIGEN"+"\n";
                                    if(response.getAfiliado()!=null){
                                        msj += "Origen:"+response.getAfiliado().getOrigenCliente()+"\n"+
                                                "Categoria:"+response.getAfiliado().getCategoria()+"\n"+
                                                "Grupo Familiar:"+response.getAfiliado().getCodigoGrupoFamiliar();
                                    }
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(ProspectoActivity.this,R.style.AppCompatAlertDialogStyle)
                                            .setTitle("RP3 Market Force")
                                            .setMessage(msj)
                                            .setCancelable(true);
                                    if(response.getAfiliado()==null){
                                        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.d(TAG,"click aceptar...");
                                                Log.d(TAG,"SET RESULT Natural...");
                                                dialog.dismiss();
                                            }
                                        });
                                    }else{
                                        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Log.d(TAG,"click aceptar...");
                                                Log.d(TAG,"SET RESULT Natural...");
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                    AlertDialog alertDialog = dialog.create();
                                    alertDialog.setCancelable(true);
                                    alertDialog.show();
                                }else{
                                    Log.d(TAG,"Esta vacio el documento consultado a afiliacion");
                                                    /*String msj = "AFILIADO \n"
                                                            +"SEGMENTO:"+response.getCOD_SEGMENTO()+"\n"
                                                            +"SBS"+response.getIND_CALIF_SBS()+"\n"
                                                            +"TDC:"+response.getIND_CALIF_TDC()+"\n"
                                                            +"DATOS DE ORIGEN:"+"\n"
                                                            +"Origen:"+response.getAfiliado().getOrigenCliente()+"\n"
                                                            +"Categoria:"+response.getAfiliado().getCategoria()+"\n"
                                                            +"Grupo Familiar:"+response.getAfiliado().getCodigoGrupoFamiliar();
                                                            */
                                    /*AlertDialog dialog = new AlertDialog.Builder(ProspectoActivity.this,R.style.AppCompatAlertDialogStyle)
                                            .setTitle("RP3 Market Force")
                                            .setMessage("Sin informacion sobre el documento consultado.")
                                            .setCancelable(true)
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Log.d(TAG,"click aceptar...");
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create();
                                    dialog.show();
                                }*/
                                //endregion
                            }else{
                                Log.d(TAG,"No is Successful...");
                                Log.d(TAG,"Exception:"+response.message());
                                Toast.makeText(ProspectoActivity.this, "Hubo un problema al verificar el documento. Vuelva a intentarlo porfavor.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).validar(idTipoDocumentoSelected,etDocumento.getText().toString().trim());

        }

    private void initDataPersonaN(){
        Log.d(TAG,"initDataPersonaN...");
        prospectoVta = new ProspectoVtaDb();
        prospectoVta.setTipoPersonaCode(tipoPersonaCode);
        String nombre = etNombre.getText().toString().trim()+" "+etApellidoPaterno.getText().toString().trim();
        String celular = etCelular.getText().toString().trim();
        if(!TextUtils.isEmpty(etApellidoMaterno.getText())){
            prospectoVta.setApellidoMaterno(etApellidoMaterno.getText().toString());
            nombre+= " "+etApellidoMaterno.getText().toString().trim();
            prospectoVta.setContactoApellidoMaterno(etApellidoMaterno.getText().toString().trim());
        }
        prospectoVta.setNombre(nombre);
        prospectoVta.setApellidoPaterno(etApellidoPaterno.getText().toString());
        prospectoVta.setNombres(etNombre.getText().toString());
        prospectoVta.setApellidoPaterno(etApellidoPaterno.getText().toString());
        prospectoVta.setContactoNombre(etNombre.getText().toString().trim());
        prospectoVta.setContactoApellidoPaterno(etApellidoPaterno.getText().toString().trim());
        prospectoVta.setCelular(celular);
        prospectoVta.setTipoDocumento(idTipoDocumentoSelected);
        if(idTipoDocumentoSelected!=0 && !TextUtils.isEmpty(etDocumento.getText()) && identificationType!=null){
            //Asignar minimos y maximos
            int minLegnthDocument = identificationType.getMaskType();
            int maxLenghtDocument = identificationType.getLenght();
            if(idTipoDocumentoSelected == 1){
                if(etDocumento.getText().toString().trim().length()==minLegnthDocument){
                    prospectoVta.setDocumento(etDocumento.getText().toString());
                    prospectoVta.setTipoDocumento(idTipoDocumentoSelected);
                }
                else{
                    etDocumento.setError("Requiere "+minLegnthDocument+" caracteres.");
                    Log.d(TAG,"Requiere 8 digitos...");
                    Toast.makeText(this, "Requiere 8 digitos el documento", Toast.LENGTH_SHORT).show();
                    return;
                }
            }else{
                if(etDocumento.getText().toString().trim().length()<minLegnthDocument){
                    etDocumento.setError("Requiere digitos menor a "+minLegnthDocument+" caracteres.");
                    Toast.makeText(this, "El documento Requiere digitos mayor a "+minLegnthDocument+" caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    prospectoVta.setTipoDocumento(idTipoDocumentoSelected);
                    int length = identificationType.getLenght();
                    prospectoVta.setDocumento(Utils.getNumberDocumento(etDocumento.getText().toString().trim(),length));
                }
            }
        }
        if(!TextUtils.isEmpty(etTelefono.getText())){
            if(etTelefono.getText().toString().trim().length()<6){
                etTelefono.setError("Se requieren 6 digitos minimo");
                Toast.makeText(this, "Se requieren 6 digitos minimo en el telefono", Toast.LENGTH_SHORT).show();
                return;
            }
            prospectoVta.setTelefono(etTelefono.getText().toString());
        }
        if(!TextUtils.isEmpty(etDireccion1.getText())){prospectoVta.setDireccion1(etDireccion1.getText().toString().trim());}
        if(!TextUtils.isEmpty(etDireccion2.getText())){prospectoVta.setDireccion2(etDireccion2.getText().toString().trim());}
        if(!TextUtils.isEmpty(etCentroTrabajo.getText())){prospectoVta.setRazonSocial(etCentroTrabajo.getText().toString().trim());}
        if(!TextUtils.isEmpty(etEmailNatural.getText())) {
            if(Utils.validarCorreo(etEmailNatural.getText().toString().trim()) &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailNatural.getText().toString().trim()).matches()){
                prospectoVta.setEmail(etEmailNatural.getText().toString().trim());
            }else{
                etEmailNatural.setError("Ingrese un correo valido");
                return;
            }
        }
        showConfirmDialog("N");
    }

    //endregion

    //region Juridico

    private boolean validateJuridico(){
        if(TextUtils.isEmpty(etEmpresa.getText())){etEmpresa.setError("Campo obligatorio");return false;}
        if(TextUtils.isEmpty(etNombreContacto.getText())){etNombreContacto.setError("Campo obligatorio");return false;}
        if(TextUtils.isEmpty(etApellidoPaternoContacto.getText())){etApellidoPaternoContacto.setError("Campo obligatorio");return false;}
        if(TextUtils.isEmpty(etNumeroContacto.getText())){etNumeroContacto.setError("Campo obligatorio");return false;}
        if(TextUtils.isEmpty(etNumeroEmpresa.getText())){etNumeroEmpresa.setError("Campo obligatorio");return false;}
        //Validar tamaños
        if(etNumeroContacto.getText().toString().trim().length()<6){etNumeroContacto.setError("Se requieren minimo 6 digitos");return false;}
        if(etNumeroEmpresa.getText().toString().trim().length()<6){etNumeroEmpresa.setError("Se requieren minimo 6 digitos");return false;}
        return true;
    }

    private void dismissJuridico(){
        Log.d(TAG,"dismissJuridico...");
        containerDocumentoJ.setVisibility(View.GONE);
        containerInformacionJ.setVisibility(View.GONE);
        containerTelefonoJ.setVisibility(View.GONE);
        containerDireccionJ.setVisibility(View.GONE);
        containerTvInformacion.setVisibility(View.GONE);
        containerTvTelefono.setVisibility(View.GONE);
        containerTvDireccion.setVisibility(View.GONE);
        tvInformacion.setVisibility(View.GONE);
    }

    private void initCleanViewsJuridico(){
        Log.d(TAG,"initViewsJuridico...");
        etRuc.setText(null);
        etEmpresa.setText(null);
        etNombreContacto.setText(null);
        etApellidoPaternoContacto.setText(null);
        etApellidoMaternoContacto.setText(null);
        etNumeroEmpresa.setText(null);
        etNumeroContacto.setText(null);
        etDireccion1J.setText(null);
        etDireccion2J.setText(null);
        tvInformacion.setText("Informacion de contacto");
        etEmailJuridico.setText(null);
    }

    private void showViewsJuridico(){
        Log.d(TAG,"showViewsJuridico...");
        containerDocumentoJ.setVisibility(View.VISIBLE);
        containerInformacionJ.setVisibility(View.VISIBLE);
        containerTelefonoJ.setVisibility(View.VISIBLE);
        containerDireccionJ.setVisibility(View.VISIBLE);
        containerTvInformacion.setVisibility(View.VISIBLE);
        containerTvTelefono.setVisibility(View.VISIBLE);
        containerTvDireccion.setVisibility(View.VISIBLE);
        tvInformacion.setVisibility(View.VISIBLE);
        tvInformacion.setText("Información Contacto");
        InputFilter[] fArray = new InputFilter[1];
        MinMaxFilter filter = new MinMaxFilter(11,11);
        fArray[0] = filter;
        etDocumento.setFilters(fArray);
        etRuc.setFilters(fArray);
        validateProspecto();
    }

    private void initDataPersonaJ(){
        Log.d(TAG,"initDataPersonaJ...");
        prospectoVta = new ProspectoVtaDb();
        prospectoVta.setTipoPersonaCode(tipoPersonaCode);
        String nombreEmpresa = etEmpresa.getText().toString().trim();
        String nombreContacto = etNombreContacto.getText().toString().trim();
        String apellidoPaterno = etApellidoPaternoContacto.getText().toString().trim();
        String numeroEmpresa = etNumeroEmpresa.getText().toString().trim();
        String numeroContacto = etNumeroContacto.getText().toString().trim();
        prospectoVta.setRazonSocial(nombreEmpresa);
        prospectoVta.setContactoApellidoPaterno(apellidoPaterno);
        prospectoVta.setContactoNombre(nombreContacto);
        prospectoVta.setCelular(numeroContacto);
        prospectoVta.setEmpresaTelefono(numeroEmpresa);
        if(!TextUtils.isEmpty(etApellidoMaternoContacto.getText())){prospectoVta.setContactoApellidoMaterno(etApellidoMaternoContacto.getText().toString().trim());}
        if(!TextUtils.isEmpty(etRuc.getText())){
            if(etRuc.getText().length()==11){
                prospectoVta.setRuc(etRuc.getText().toString().trim());
            }else{
                Toast.makeText(this, "Porfavor Ingrese el numero de RUC correctamente", Toast.LENGTH_SHORT).show();
            }

        }
        if(!TextUtils.isEmpty(etDireccion1J.getText())){prospectoVta.setDireccion1(etDireccion1.getText().toString().trim());}
        if(!TextUtils.isEmpty(etDireccion2J.getText())){prospectoVta.setDireccion2(etDireccion2.getText().toString().trim());}
        if(!TextUtils.isEmpty(etEmailJuridico.getText())) {
            if(Utils.validarCorreo(etEmailJuridico.getText().toString().trim()) &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailJuridico.getText().toString().trim()).matches()){
                prospectoVta.setEmail(etEmailJuridico.getText().toString().trim());
            }else{
                etEmailJuridico.setError("Ingrese un correo valido");
                return;
            }
        }

        showConfirmDialog("J");
    }

    //endregion

    //region Views General

    public void toolbarStatusBar() {
        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nuevo Prospecto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void navigationBarStatusBar() {
        // Fix portrait issues
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Fix issues for KitKat setting Status Bar color primary
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                ProspectoActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            // Fix issues for Lollipop, setting Status Bar color primary dark
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                ProspectoActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Fix landscape issues (only Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                ProspectoActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                ProspectoActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    //endregion

    //region Referidos Action

    private void setConfirmDialogMoreReferido(){
        ApplicationParameter parameterReferido = ApplicationParameter
                .getParameter(Utils.getDataBase(this), Constants.PARAMETERID_REFERIDOCOUNT,Constants.LABEL_PROSPECTO);
        int countMinimo = Integer.parseInt(parameterReferido.getValue());
        if(mNotifCount==0){
            //el minimo es 0 y no agrego a nadie consultar si desea finalizar sin referir a nadie
            if(countMinimo==0){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
                dialog.setTitle(this.getResources().getString(R.string.appname_marketforce))
                        .setMessage("No refirio a nadie.\n¿Desea finalizar?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SessionManager.getInstance(ProspectoActivity.this).removeVisitaSession();
                                setResult(RESULT_REFERIDOS_FROM_VISITA);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setCancelable(false);
                AlertDialog a = dialog.create();
                a.show();
            }else{
                //el minimo es distinto de 0
                Toast.makeText(this, "Debe guardar "+countMinimo+" referido como minimo.", Toast.LENGTH_SHORT).show();
            }
        }
        //Los referidos grabados es menor al minimo requerido
        else if(mNotifCount<countMinimo){
            Toast.makeText(this, "Debe guardar "+countMinimo+" referido como minimo.", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
            dialog.setTitle(this.getResources().getString(R.string.appname_marketforce))
                    .setMessage("¿Desea guardar referidos y finalizar?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            setResult(RESULT_REFERIDOS_FROM_VISITA);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false);
            AlertDialog a = dialog.create();
            a.show();
        }
    }

    private void showAlertDialogReferidoConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        builder.setTitle(this.getResources().getString(R.string.appname_marketforce))
                .setMessage("¿Confirmar referido?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prospectoVta.setEstado(1);
                        prospectoVta.setVisitaReferido(visitaReferidoId);
                        prospectoVta.setEstadoCode(Contants.GENERAL_VALUE_CODE_APTO_PROSPECCION);
                        prospectoVta.setOrigenCode(Contants.GENERAL_VALUE_CODE_ORIGEN_VENTA_MKF);
                        boolean result = prospectoVta.insert(Utils.getDataBase(getApplicationContext()),prospectoVta);
                        if(result){
                            Log.d(TAG,"Se grabo prospecto en sqlite...");
                            Log.d(TAG,prospectoVta.toString());
                        }else{
                            Log.d(TAG,"No se grabo prospecto en sqlite...");
                        }
                        initCleanViewsJuridico();
                        initCleanViewsNatural();
                        dismissNatural();
                        dismissJuridico();
                        tipoPersonaCode=null;
                        idTipoDocumentoSelected = 0;
                        spTipoPersona.post(new Runnable() {
                            @Override
                            public void run() {
                                spTipoPersona.setSelection(0);
                            }
                        });
                        setNotifCount(1);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"onCreateOptionsMenu...");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_prospecto_referido, menu);
        View count = menu.findItem(R.id.badge).getActionView();
        MenuItem countMenu = menu.findItem(R.id.badge);
        MenuItem view = menu.findItem(R.id.action_guardar_referido);
        notifCount = (Button) count.findViewById(R.id.notif_count);
        notifCount.setText(String.valueOf(mNotifCount));
        if(opcionProspectar!=3){
            count.setVisibility(View.GONE);
            view.setVisible(false);
            countMenu.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void setNotifCount(int count){
        mNotifCount = mNotifCount+count;
        invalidateOptionsMenu();
    }

    //endregion

    //region OnClick

    @Override
    public void onClick(View v) {
        if(v.getId()==fabNuevo.getId()){Log.d(TAG,"fabNuevo Onclick...");validate();}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_guardar_referido){
            setConfirmDialogMoreReferido();
        }
        else if(id == android.R.id.home){
            validateBack();
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    //region Ciclo de vida
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
        if(opcionProspectar==0){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
        SessionManager.getInstance(this).removeLlamada();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d(TAG,"onBackPressed...");
        if(opcionProspectar==2 || opcionProspectar==0){
            Log.d(TAG,"validateBack...");
            validateBack();
        }else if(opcionProspectar==3){
            validateBack();
        }else{
            Log.d(TAG,"moveTakToBack...");
            moveTaskToBack(true);
        }

        //finish();
    }

    //endregion

    private boolean validateProspecto(String tipoPersona){
        if(tipoPersona.equalsIgnoreCase("N")){
            //Validar natural su documento
            if(idTipoDocumentoSelected!=-1 && prospectoVta.getDocumento()!=null){
                if(prospectoVta.getDocumento().trim().length()>0){
                    List<ProspectoVtaDb> list = ProspectoVtaDb.getAll(Utils.getDataBase(this));
                    for (ProspectoVtaDb prospectoVtaDb:list){
                        if(prospectoVtaDb.getDocumento()!=null){
                            if(prospectoVtaDb.getDocumento().trim().length()>0){
                                if(prospectoVtaDb.getTipoPersonaCode().equalsIgnoreCase("N")&&
                                        prospectoVtaDb.getDocumento().trim().equalsIgnoreCase(prospectoVta.getDocumento()) &&
                                        prospectoVtaDb.getTipoDocumento() == prospectoVta.getTipoDocumento()){
                                    Log.d(TAG,"Natural....Documento y tipo de documento iguales...prohibir...");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }else{
            //Validar Juridico su numero
            if(prospectoVta.getRuc()!=null){
                if(prospectoVta.getRuc().trim().length()>0){
                    List<ProspectoVtaDb> list = ProspectoVtaDb.getAll(Utils.getDataBase(this));
                    for (ProspectoVtaDb prospectoVtaDb:list){
                        if(prospectoVtaDb.getRuc()!=null){
                            if(prospectoVtaDb.getRuc().trim().length()>0){
                                if(prospectoVtaDb.getTipoPersonaCode().equalsIgnoreCase("J") &&
                                        prospectoVtaDb.getRuc().equalsIgnoreCase(prospectoVta.getRuc())){
                                    Log.d(TAG,"Juridico....Ruc son iguales....prohibir....");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;

    }

    private void validateBack(){
        if(opcionProspectar==3){
            Log.d(TAG,"prospectar...");
            moveTaskToBack(true);
        }
        else if(opcionProspectar==2){
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
            builder.setTitle(this.getResources().getString(R.string.appname_marketforce));
            builder.setMessage("¿Desea cancelar la edición del prospecto?");
            builder.setCancelable(false);
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(opcionProspectar==2){
                        Log.d(TAG,"Opcion de edicion..finalizar activity y reiniciar el main...");
                        SessionManager.getInstance(getApplicationContext()).removeProspectoEdit();
                        //Intent intent = new Intent(getApplicationContext(),StartActivity.class);
                        //startActivity(intent);
                        ProspectoActivity.super.onBackPressed();
                        finish();
                    }else{
                        Log.d(TAG,"Opcion de creacion regresar nada mas...");
                        ProspectoActivity.super.onBackPressed();
                        finish();
                    }
                }
            }).create().show();
        }
        else if(opcionProspectar==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
            builder.setTitle(this.getResources().getString(R.string.appname_marketforce));
            builder.setMessage("¿Desea cancelar la creación del prospecto?");
            builder.setCancelable(false);
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(opcionProspectar==2){
                        Log.d(TAG,"Opcion de edicion..finalizar activity y reiniciar el main...");
                        SessionManager.getInstance(getApplicationContext()).removeProspectoEdit();
                        //Intent intent = new Intent(getApplicationContext(),StartActivity.class);
                        //startActivity(intent);
                        ProspectoActivity.super.onBackPressed();
                        finish();
                    }else{
                        Log.d(TAG,"Opcion de creacion regresar nada mas...");
                        ProspectoActivity.super.onBackPressed();
                        finish();
                    }
                }
            }).create().show();
        }

    }

    private void showValidateDocumentClient(String documento){
        flagDocumentValidate = 1;
        new ValidateDocumentClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d(TAG,"onFailure...");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        Toast.makeText(ProspectoActivity.this, R.string.message_error_sync_connection_http_error, Toast.LENGTH_SHORT).show();
                    }
                });
                flagDocumentValidate = 0;
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                flagDocumentValidate = 0;
                final String json = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            Log.d(TAG,"Successful...");
                            ValidateDocument obj = new Gson().fromJson(json,ValidateDocument.class);
                            if(obj.getFlag()==0){
                                Log.d(TAG,"flag == 0");
                                flagDocumento = 0;
                                validateDocument = true;
                                showCalificacionClient();
                            }else if(obj.getFlag()==1){
                                Log.d(TAG,"flag == 1");
                                flagDocumento = 1;
                                validateDocument = false;
                                showCalificacionClient();
                                //Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_exist, Toast.LENGTH_SHORT).show();
                            }else if(obj.getFlag()==2){
                                Log.d(TAG,"flag == 2");
                                flagDocumento = 2;
                                validateDocument = false;
                                showCalificacionClient();
                                //Toast.makeText(ProspectoActivity.this, R.string.prospecto_document_robinson, Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Log.d(TAG,"No is successful...");
                            System.out.println(response);
                        }
                    }
                });
            }
        }).validar(documento);
    }

    private void showDialogBuildCalificacionDialog(InVentasProspecto inVentasProspecto){
        final CalificativoDialog dialog = CalificativoDialog.newInstance(inVentasProspecto, new CalificativoDialog.callbackOpcionSelected() {
            @Override
            public void onSelectedCancelar() {
                Log.d(TAG,"onSelectedCancelar...");
            }

            @Override
            public void onSelectedAceptar() {
                Log.d(TAG,"onSelectedAceptar...");
            }
        });
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),"");
    }
}
