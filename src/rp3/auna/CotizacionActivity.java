package rp3.auna;
/***
 * Created by Jesús Villa Sánchez
 * */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import asia.kanopi.fingerscan.Status;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pe.solera.api_payme_android.bean.Address;
import pe.solera.api_payme_android.bean.Commerce;
import pe.solera.api_payme_android.bean.DataReserved;
import pe.solera.api_payme_android.bean.Person;
import pe.solera.api_payme_android.bean.Product;
import pe.solera.api_payme_android.bean.PurchaseInformation;
import pe.solera.api_payme_android.bean.Tax;
import pe.solera.api_payme_android.bean.TransactionInformation;
import pe.solera.api_payme_android.bean_response.PayMeResponse;
import pe.solera.api_payme_android.classes.activities.LoadingAct;
import rp3.auna.bean.AfiliadoMovil;
import rp3.auna.bean.Cotizacion;
import rp3.auna.bean.CotizacionMovil;
import rp3.auna.bean.CotizacionVisita;
import rp3.auna.bean.OperationNumberMovil;
import rp3.auna.bean.PaymeCodWallet;
import rp3.auna.bean.ProspectoVta;
import rp3.auna.bean.RegistroPago;
import rp3.auna.bean.ResponseSolicitudWallet;
import rp3.auna.bean.SolicitudAfiliadoMovil;
import rp3.auna.bean.SolicitudMovil;
import rp3.auna.bean.SolicitudOncosys;
import rp3.auna.bean.TarjetaValidar;
import rp3.auna.bean.VentaFisicaData;
import rp3.auna.bean.VentaRegularData;
import rp3.auna.bean.VisitaVtaDetalle;
import rp3.auna.bean.VisitaVtaFinal;
import rp3.auna.bean.Wallet;
import rp3.auna.bean.virtual.CotizacionVirtual;
import rp3.auna.dialog.EmailContratanteDialog;
import rp3.auna.dialog.EmailCotizacionDialog;
import rp3.auna.dialog.FingerPrintDialog;
import rp3.auna.dialog.MotivoCitaDialog;
import rp3.auna.models.ApplicationParameter;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.models.ventanueva.VisitaVta;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.helper.Util;
import rp3.auna.util.session.SessionManager;
import rp3.auna.utils.NothingSelectedSpinnerAdapter;
import rp3.auna.utils.Utils;
import rp3.auna.webservices.AutenticarHuellaRp3Client;
import rp3.auna.webservices.CotizacionVisitaClient;
import rp3.auna.webservices.CotizadorClient;
import rp3.auna.webservices.EmailAfiliacionProcesoClient;
import rp3.auna.webservices.EmailCotizacionClient;
import rp3.auna.webservices.EmailInstruccionesPagoClient;
import rp3.auna.webservices.FinalizarVentaClient;
import rp3.auna.webservices.OperationNumberClient;
import rp3.auna.bean.RespuestaValidar;
import rp3.auna.webservices.RegistrarPagoClient;
import rp3.auna.webservices.SaveCotizacionClient;
import rp3.auna.webservices.SolicitudClient;
import rp3.auna.webservices.UpdateVisitaClient;
import rp3.auna.webservices.ValidarSolicitudOncosysClient;
import rp3.auna.webservices.ValidarTarjetaClient;
import rp3.auna.webservices.WalletClient;
import rp3.auna.webservices.virtual.CotizadorVirtualClient;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.runtime.Session;
import rp3.util.Convert;

import static rp3.auna.Contants.GENERAL_TABLE_CONTROL_ERRORES_WS_VALIDAR_PAGO;
import static rp3.auna.Contants.GENERAL_TABLE_MODO_TARIFA_TIPO_PAGO;
import static rp3.auna.Contants.GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA;
import static rp3.auna.Contants.GENERAL_TABLE_TIPO_DOCUMENTO;
import static rp3.auna.Contants.KEY_IDAGENTE;
import static rp3.auna.Contants.PAYME_COD_CARD_HOLDER_COMMERCE;
import static rp3.auna.Contants.PAYME_IDACQUIRER;
import static rp3.auna.Contants.PAYME_ID_ENT_COMMERCE;

public class CotizacionActivity extends AppCompatActivity {

    private static final String TAG = CotizacionActivity.class.getSimpleName();
    //region Constants CARD
    private static final char CARD_DATE_DIVIDER = '/';
    private static final int CARD_DATE_TOTAL_SYMBOLS = 7; // size of pattern MM/YYYY
    private static final int CARD_DATE_TOTAL_DIGITS = 6; // max numbers of digits in pattern: MM + YYyy
    private static final int CARD_DATE_DIVIDER_MODULO = 3; // means divider position is every 3rd symbol beginning with 1
    private static final int CARD_DATE_DIVIDER_POSITION = CARD_DATE_DIVIDER_MODULO - 1; // means divider position is every 2nd symbol beginning with 0
    //endregion

    //region Request & Result
    private static final int RESULT_VISITA_COTIZACION_NUEVO = 6;
    private static final int REQUEST_REFERIR_PROSPECTOS = 20;
    private static final int REQUEST_VISITA_COTIZACION_NUEVO = 6;
    private static final int REQUEST_VISITA_PAGO_FISICO_DOCUMENTOS= 7;
    //Control de cambio, se solicito que se posicione en el Tab de Visita al reprogamar ó cancelar la visita
    private static final int RESULT_VISITA_REPROGRAMM_CANCEL = 34;
    //Result Referidos
    private static final int RESULT_VISITA_ONLINE_REFERIDO = 21;
    private static final int RESULT_VISITA_EFECTIVO_REFERIDO = 22;
    private static final int RESULT_VISITA_REGULAR_REFERIDO = 23;

    //Cotizacion Inicial
    private static final int REQUEST_VISITA_SOLICITUD_COTIZACION_INICIAL = 10;
    private static final int RESULT_VISITA_SOLICITUD_COTIZACION_INICIAL_OK = 10;
    private static final int RESULT_VISITA_SOLICITUD_COTIZACION_INICIO_CANCELADA = 11;
    //Cotizacion Final
    private static final int REQUEST_VISITA_SOLICITUD_COTIZACION_FINAL = 14;
    private static final int RESULT_VISITA_SOLICITUD_COTIZACION_FINAL_OK= 14;
    private static final int RESULT_VISITA_SOLICITUD_COTIZACION_FINAL_CANCELADA = 15;
    //Pago
    private static final int REQUEST_VISITA_NUEVA_PAGO_FISICO = 11;
    private static final int RESULT_VISITA_NUEVA_PAGO_FISICO = 11;
    private static final int REQUEST_VISITA_REPROGRAMADA_FINALIZADA = 12;
    private static final int RESULT_VISITA_REPROGRAMADA_FINALIZADA = 12;
    private static final int RESULT_VISITA_CANCELADA_FINALIZADA = 13;
    //endregion

    //region Views General

    @BindView(R.id.toolbarCotizacion) Toolbar toolbar;
    @BindView(R.id.tvCotizacion) TextView tvCotizacionTitle;
    @BindView(R.id.statusBarCotizacion) FrameLayout frameLayout;
    @BindView(R.id.cotizacion_param_content_movil) LinearLayout ContainerParams;
    @BindView(R.id.cotizacion_resp_anual_content_movil) LinearLayout ContainerAnual;
    @BindView(R.id.cotizacion_resp_credito_content_movil) LinearLayout ContainerTC;
    @BindView(R.id.cotizacion_resp_debito_content_movil) LinearLayout ContainerTD;
    @BindView(R.id.cotizacion_select_anual_movil) LinearLayout SelectAnual;
    @BindView(R.id.cotizacion_select_tc_movil) LinearLayout SelectTC;
    @BindView(R.id.cotizacion_select_td_movil) LinearLayout SelectTD;
    @BindView(R.id.cotizacion_resp_empty_movil) View EmptyView;
    @BindView(R.id.cotizacion_resp_scroll_movil) View ResponseView;
    @BindView(R.id.cotizacion_anual_total_Movil)TextView TotalAnual;
    @BindView(R.id.cotizacion_mensual_credito_Movil)TextView TotalTC;
    @BindView(R.id.cotizacion_mensual_debito_Movil)TextView TotalTD;
    @BindView(R.id.agregar_afiliadoMovil)TextView tvAgregarfiliado;
    @BindView(R.id.rbIndividual) RadioButton rbIndividual;
    @BindView(R.id.rbCorporativo) RadioButton rbCorporativo;
    @BindView(R.id.rbgroupTipoVenta) RadioGroup rbGroupTipoVenta;
    @BindView(R.id.lnRucEmpresa) LinearLayout lnRucEmpresa;
    @BindView(R.id.etRucEmpresa) EditText etRucEmpresa;
    //endregion

    @BindView(R.id.lnFraccionamientos) LinearLayout lnFraccionamientos;
    @BindView(R.id.spFraccionamiento) Spinner spFraccionamiento;
    @BindView(R.id.rbPaymeSi) RadioButton rbPaymeSi;//En linea
    @BindView(R.id.rbPaymeNo) RadioButton rbPaymeNo;//Efectivo
    @BindView(R.id.rbRegular) RadioButton rbRegular;//Regular
    @BindView(R.id.etNumeroTarjetaPayme) EditText etNumeroTarjeta;
    @BindView(R.id.lyMarcaTarjeta) LinearLayout lyMarcaTarjeta;
    @BindView(R.id.lyNumeroTarjeta) LinearLayout lyNumeroTarjeta;
    @BindView(R.id.btnValidarTarjeta) Button btnValidarTarjeta;
    @BindView(R.id.cvPayme) CardView cvPayme;
    //Nuevos Campos
    @BindView(R.id.lyTipoTarjeta) LinearLayout lyTipoTarjeta;
    @BindView(R.id.lyFechaTarjeta) LinearLayout lyFechaTarjeta;
    @BindView(R.id.lyFrecuenciaPago) LinearLayout lyFrecuenciaPago;
    @BindView(R.id.tvDatosTarjeta) TextView tvDatosTarjeta;
    @BindView(R.id.etFechaVencimiento) EditText etFechaVencimiento;
    @BindView(R.id.rbFrecuenciaPagoAnual) RadioButton rbFrecuenciaAnual;
    @BindView(R.id.rbFrecuenciaPagoMensual) RadioButton rbFrecuenciaMensual;
    private SolicitudMovil solicitudMovil;
    private SolicitudAfiliadoMovil afiliadoContratante;
    private List<LinearLayout> afiliadosLayouts;
    private VisitaVta visitaVta;
    private CotizacionMovil cotizacionMovil;/**Este es el Cotizador Normal*/
    private List<Cotizacion> cotizacionList;
    private List<Cotizacion> cotizacionAfiliados;
    private List<Cotizacion> cotizacionFraccionamientos;
    private double totalAnual = 0;
    private double totalTC = 0;
    private double totalTD = 0;
    private int select = -1;
    private int tipoVenta = -1;
    private boolean email = false;
    private int estado;
    private String agente;
    private List<GeneralValue> listFraccionamientos;
    private List<GeneralValue> listExcepcions;
    private String precioCuota = null;
    private String cantidadCuota = null;
    private boolean sendEmail = false;
    private Date selectedDateAfiliado = null;
    private int selectTipoPago = -1;
    private boolean validatePayme = false;
    private int operationNumber = -1;
    //Variables para validar el flujo
    private RegistroPago registroPago = null;
    private Double precio = null;
    private int selectPlanCotizacion = -1;
    private int selectTipoVenta = 1;
    private int requestVenta = -1;
    private int     selectTipoPagoFinal = -1;
    private int operationNumberFinal = -1;
    private boolean sendEmailFinal = false;
    private boolean validatePaymeFinal = false;
    private int REQUEST_STATE = -1;
    private MenuItem itemEmail;
    private String correoContratante = null;
    private String celularContratante = null;
    private String numeroOperacion = null;
    private String codAsoCardHolderWallet = null;
    private Calendar fechaMinimaAfiliado = null;
    private Calendar fechaMaximaAfiliado = null;
    //Valores de los Nuevos Campos
    private int frecuenciaPagoSelected = -1;
    //Valores para determinar lo seleccionado anteriormente
    private List<GeneralValue> listProgramas = null;
    private String cuotaSelectedDetail = null;
    private String planSelectedDetail = null;
    //Si es la primera vez en consultar = 1 , else other
    private int flagConsultoDetalle = 1;
    private int flagRealizoCotizacionInicial = 0;

    //region Solicitud Virtual
    @BindView(R.id.rbFisica) RadioButton rbFisica;
    @BindView(R.id.rbVirtual) RadioButton rbVirtual;
    private List<GeneralValue> listExcepcionsVirtual;
    private int consultarCotizacion = -1;
    private CotizacionVirtual cotizacionVirtual;/**Este es el Cotizador Virtual model*/
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizacion);
        Log.d(TAG,"onCreate...");
        ButterKnife.bind(this);
        try{
            toolbarStatusBar();
            setParametersDatesAfiliado();
            navigationBarStatusBar();
            getData();
            initViews();
            initSelectTipoVenta();
            initVirtual();
            configureInitCheck();
            detailCotizacion();
        }catch (Exception e){
            e.printStackTrace();
        }

        //validateVisita();
    }

    private void refreshFraccionamientos(){
        int countCuotaDetail = 0 ;
        listFraccionamientos = GeneralValue.getGeneralValues(Utils.getDataBase(this),Contants.GENERAL_TABLE_COTIZACION_FRACCIONAMIENTO);
        List<String> precios = new ArrayList<>();
        NumberFormat numberFormat;
        Locale locale = new Locale("es", "pe");
        numberFormat = NumberFormat.getInstance(locale);
        //numberFormat.setMaximumFractionDigits(4);
        numberFormat.setMinimumFractionDigits(2);
        if(cotizacionList!=null) {
            Log.v(TAG,"refreshFraccionamientos:cotizacionList!=null");
            if (cotizacionList.size() > 0) {
                Log.v(TAG,"refreshFraccionamientos:cotizacionList.size() > 0");
                cotizacionFraccionamientos = new ArrayList<>();
                for(int i = 0;i<cotizacionList.size();i++){
                    if(cotizacionList.get(i).getIdOperacion().equalsIgnoreCase("F")){
                        cotizacionFraccionamientos.add(cotizacionList.get(i));
                    }
                }
                final int countCuotas = cotizacionFraccionamientos.size();
                countCuotaDetail = countCuotas;
                Log.d(TAG,"countCuotas:"+countCuotas);
                //Validar countFracciones para determinar la cantidad
                precios.add((String.valueOf(removeSoloComas(numberFormat.format(totalAnual)))));
                if(consultarCotizacion==1){
                    Log.d(TAG,"Es consulta del cotizador normal...");
                    //2 cuotas
                    if(countCuotas>0){
                        Log.d(TAG,"2 cuotas de fraccion...");
                        precios.add(removeSoloComas(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(0).getTD()))));
                    }
                    //3 Cuotas
                    if(countCuotas>2){
                        Log.d(TAG,"3 cuotas de fraccion...");
                        precios.add(removeSoloComas(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(2).getTD()))));
                    }
                    //4 Cuotas
                    if(countCuotas>5){
                        Log.d(TAG,"4 cuotas de fraccion...");
                        precios.add(removeSoloComas(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(5).getTD()))));
                    }
                }else if(consultarCotizacion==2){
                    Log.d(TAG,"Es consulta del cotizador virtual...");
                    //2 cuotas
                    if(countCuotas>0){
                        Log.d(TAG,"2 cuotas de fraccion...");
                        precios.add(removeSoloComas(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(0).getTD()))));
                    }
                    //3 Cuotas
                    if(countCuotas>2){
                        Log.d(TAG,"3 cuotas de fraccion...");
                        precios.add(removeSoloComas(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(2).getTD()))));
                    }
                    //4 Cuotas
                    if(countCuotas>5){
                        Log.d(TAG,"4 cuotas de fraccion...");
                        precios.add(removeSoloComas(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(5).getTD()))));
                    }

                }else{
                    Log.d(TAG,"consultarCotizacion != 1 && != 2...");
                    return;
                }

                //region ocio
                 /*precios.add(replaceComa(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(0).getTD()))));
                precios.add(replaceComa(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(2).getTD()))));
                precios.add(replaceComa(numberFormat.format(Double.parseDouble(cotizacionFraccionamientos.get(5).getTD()))));*/
                //endregion
            }
        }
        List<GeneralValue> temp = new ArrayList<>();
        Log.d(TAG,"Cantidad de FraccionamientosList:"+listFraccionamientos.size());
        Log.d(TAG,"Cantidad de Precios Size:"+precios.size());
        for(int j = 0; j < precios.size();j++){
            int k = j+1;
            GeneralValue obj = listFraccionamientos.get(j);
            obj.setValue(k+" cuota: S/"+precios.get(j));
            obj.setReference1(precios.get(j));
            temp.add(obj);
        }
        listFraccionamientos.clear();
        listFraccionamientos.addAll(temp);
        Log.d(TAG,"Fracciones totals a mostrar:"+listFraccionamientos.size());
        SimpleGeneralValueAdapter programaAdapter = new SimpleGeneralValueAdapter(this, listFraccionamientos){

            public View getView(int position, View convertView,ViewGroup parent) {
                Log.d(TAG,"SimpleGeneralValueAdapter: getView...");
                View v = super.getView(position, convertView, parent);
                RelativeLayout layout = ((RelativeLayout)v);
                TextView tv = (TextView)layout.findViewById(R.id.tvGeneralSelectedd);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_normal));
                //tv.setTextColor(getResources().getColor(R.color.White));
                return v;
            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                Log.d(TAG,"SimpleGeneralValueAdapter getDropDownView...");
                //((TextView) v).setGravity(Gravity.CENTER);
                return v;

            }

        };
        spFraccionamiento.setAdapter(new NothingSelectedSpinnerAdapter(
                programaAdapter,
                R.layout.item_spinner_white,
                this, "Seleccionar"));
        spFraccionamiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean estado = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG," spFraccionamiento onItemSelected...");
                if(estado){
                    estado = false;
                    Log.d(TAG,"spFraccionamiento Estado true...");
                }else{
                    Log.d(TAG,"spFraccionamiento Estado false...");
                    Log.d(TAG,"spFraccionamiento position == 0...");
                    if(position==0){
                    }else{
                        RelativeLayout layout = ((RelativeLayout) view);
                        TextView tv = (TextView) layout.findViewById(R.id.tvGeneralSelectedd);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_normal));
                        tv.setTextColor(getResources().getColor(R.color.White));
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG,"onNothingSelected...");
            }
        });
        //spFraccionamiento.setPrompt("Seleccionar cuota");
        if(visitaVta.getEstado()==6 && estado ==1){
            if(flagConsultoDetalle==1){
                flagConsultoDetalle=2;
                selectedPlanDetail();
                if(planSelectedDetail!=null){
                    if(planSelectedDetail.length()>0){
                        if(planSelectedDetail.trim().equalsIgnoreCase("Anual")){
                            for (int i = 1;i<=countCuotaDetail;i++){
                                final int j = i;
                                if (j==Integer.parseInt(cuotaSelectedDetail)){
                                    spFraccionamiento.post(() ->
                                            spFraccionamiento.setSelection(j)
                                    );
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void selectedPlanDetail(){
        if(planSelectedDetail!=null){
            if(planSelectedDetail.length()>0){
                if(planSelectedDetail.trim().equalsIgnoreCase("Anual")){
                    select = 1;
                    selectPlanCotizacion = 1;
                    ContainerAnual.setBackgroundColor(getResources().getColor(R.color.alternative_background));
                    ContainerTD.setBackgroundColor(getResources().getColor( R.color.White));
                    ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
                    SelectAnual.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                    SelectTC.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                    SelectTD.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                    spFraccionamiento.setEnabled(true);
                }else if(planSelectedDetail.trim().equalsIgnoreCase("TC")){
                    select = 2;
                    selectPlanCotizacion = 2;
                    ContainerAnual.setBackgroundColor(getResources().getColor(R.color.White));
                    ContainerTD.setBackgroundColor(getResources().getColor(R.color.White));
                    ContainerTC.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                    SelectAnual.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                    SelectTC.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                    SelectTD.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                    fraccionamientoSelectedIndex(0);
                    spFraccionamiento.setEnabled(false);
                }else if(planSelectedDetail.trim().equalsIgnoreCase("TD")){
                    select = 3;
                    selectPlanCotizacion = 3;
                    ContainerAnual.setBackgroundColor(getResources().getColor( R.color.White));
                    ContainerTD.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                    ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
                    SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
                    SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
                    SelectTD.setBackgroundColor(getResources().getColor(R.color.alternative_background));
                    fraccionamientoSelectedIndex(0);
                    spFraccionamiento.setEnabled(false);
                }
            }
        }
    }

    private void initSelectTipoVenta(){
        listFraccionamientos = GeneralValue.getGeneralValues(Utils.getDataBase(this),Contants.GENERAL_TABLE_COTIZACION_FRACCIONAMIENTO);
        listExcepcions = GeneralValue.getGeneralValues(Utils.getDataBase(this),Contants.GENERAL_TABLE_COTIZACION_TIPOS_ERROR);
        rbIndividual.setOnClickListener(v -> {
            tipoVenta = 1;
            if(estado==2){
                rbIndividual.setChecked(true);
                rbCorporativo.setChecked(false);
                cvPayme.setVisibility(View.VISIBLE);
                ((CheckBox) findViewById(R.id.checkboxcamapana)).setVisibility(View.VISIBLE);
                findViewById(R.id.lyCampana).setVisibility(View.VISIBLE);
                //si hubo una consulta anterior
                if(requestVenta!=-1){
                    //el ultimo request fue Individual
                    if(requestVenta == 1){
                        if(cotizacionList!=null){
                            if(cotizacionList.size()>0){
                                ResponseView.setVisibility(View.VISIBLE);
                                EmptyView.setVisibility(View.GONE);
                                etRucEmpresa.setVisibility(View.GONE);
                                etRucEmpresa.setText(null);
                            }
                        }
                    }else{
                        //el ultimo request fue Corporativo
                        if(cotizacionList!=null){
                            if(cotizacionList.size()>0){
                                ResponseView.setVisibility(View.GONE);
                                EmptyView.setVisibility(View.VISIBLE);
                                lnRucEmpresa.setVisibility(View.GONE);
                                etRucEmpresa.setVisibility(View.GONE);
                            }
                        }
                    }
                }else{
                    //No Hubo consulta anterior solo ocultar el campo RUC y limpiar
                    lnRucEmpresa.setVisibility(View.GONE);
                    etRucEmpresa.setText(null);
                }
            }else{
                ((CheckBox) findViewById(R.id.checkboxcamapana)).setVisibility(View.VISIBLE);
                findViewById(R.id.lyCampana).setVisibility(View.VISIBLE);
                //si hubo una consulta anterior
                if(requestVenta!=-1){
                    //el ultimo request fue Individual
                    if(requestVenta == 1){
                        if(cotizacionList!=null){
                            if(cotizacionList.size()>0){
                                ResponseView.setVisibility(View.VISIBLE);
                                EmptyView.setVisibility(View.GONE);
                                etRucEmpresa.setVisibility(View.GONE);
                                etRucEmpresa.setText(null);
                            }
                        }
                    }else{
                        //el ultimo request fue Corporativo
                        if(cotizacionList!=null){
                            if(cotizacionList.size()>0){
                                ResponseView.setVisibility(View.GONE);
                                EmptyView.setVisibility(View.VISIBLE);
                                lnRucEmpresa.setVisibility(View.GONE);
                                etRucEmpresa.setVisibility(View.GONE);
                            }
                        }
                    }
                }else{
                    //No Hubo consulta anterior solo ocultar el campo RUC y limpiar
                    lnRucEmpresa.setVisibility(View.GONE);
                    etRucEmpresa.setText(null);
                }
            }
        });

        rbCorporativo.setOnClickListener(v -> {
            tipoVenta = 2;
            if(estado==2){
                rbCorporativo.setChecked(true);
                rbIndividual.setChecked(false);
                cvPayme.setVisibility(View.GONE);
                ((CheckBox) findViewById(R.id.checkboxcamapana)).setVisibility(View.GONE);
                findViewById(R.id.lyCampana).setVisibility(View.GONE);
                //si hubo una consulta anterior
                if(requestVenta!=-1){
                    //el ultimo request fue Individual
                    if(requestVenta == 1){
                        if(cotizacionList!=null){
                            if(cotizacionList.size()>0){
                                ResponseView.setVisibility(View.GONE);
                                EmptyView.setVisibility(View.VISIBLE);
                                lnRucEmpresa.setVisibility(View.VISIBLE);
                                etRucEmpresa.setVisibility(View.VISIBLE);
                                etRucEmpresa.setText(null);
                                findViewById(R.id.lyCampana).setVisibility(View.GONE);
                            }
                        }
                    }else{
                        //el ultimo request fue Corporativo
                        if(cotizacionList!=null){
                            if(cotizacionList.size()>0){
                                ResponseView.setVisibility(View.VISIBLE);
                                EmptyView.setVisibility(View.GONE);
                                lnRucEmpresa.setVisibility(View.VISIBLE);
                                etRucEmpresa.setVisibility(View.VISIBLE);
                                findViewById(R.id.lyCampana).setVisibility(View.GONE);
                            }
                        }
                    }
                }else{
                    //No hubo consulta anterior solo mostrar el campo RUC
                    lnRucEmpresa.setVisibility(View.VISIBLE);
                    findViewById(R.id.lyCampana).setVisibility(View.GONE);
                }
            }else{
                ((CheckBox) findViewById(R.id.checkboxcamapana)).setVisibility(View.GONE);
                findViewById(R.id.lyCampana).setVisibility(View.GONE);
                //si hubo una consulta anterior
                if(requestVenta!=-1){
                    //el ultimo request fue Individual
                    if(requestVenta == 1){
                        if(cotizacionList!=null){
                            if(cotizacionList.size()>0){
                                ResponseView.setVisibility(View.GONE);
                                EmptyView.setVisibility(View.VISIBLE);
                                lnRucEmpresa.setVisibility(View.VISIBLE);
                                etRucEmpresa.setVisibility(View.VISIBLE);
                                etRucEmpresa.setText(null);
                                findViewById(R.id.lyCampana).setVisibility(View.GONE);
                            }
                        }
                    }else{
                        //el ultimo request fue Corporativo
                        if(cotizacionList!=null){
                            if(cotizacionList.size()>0){
                                ResponseView.setVisibility(View.VISIBLE);
                                EmptyView.setVisibility(View.GONE);
                                lnRucEmpresa.setVisibility(View.VISIBLE);
                                etRucEmpresa.setVisibility(View.VISIBLE);
                                findViewById(R.id.lyCampana).setVisibility(View.GONE);
                            }
                        }
                    }
                }else{
                    //No hubo consulta anterior solo mostrar el campo RUC
                    lnRucEmpresa.setVisibility(View.VISIBLE);
                    findViewById(R.id.lyCampana).setVisibility(View.GONE);
                }
            }

        });
    }

    private void setParametersDatesAfiliado(){
        ApplicationParameter applicationParameter = ApplicationParameter.getParameter(Utils.getDataBase(this), Constants.PARAMETERID_ANO_MAX_FECHA,Constants.LABEL_COTIZACION_AFILIADO);
        ApplicationParameter applicationParameter1 = ApplicationParameter.getParameter(Utils.getDataBase(this), Constants.PAMARETERID_ANO_MINIMO_FECHA,Constants.LABEL_COTIZACION_AFILIADO);

        Calendar hoy = Calendar.getInstance();
        int añoActual = hoy.get(Calendar.YEAR);
        int añoAnterior = (añoActual-(Integer.parseInt(applicationParameter.getValue())));
        Log.d(TAG,"añoActual:"+añoActual);
        Log.d(TAG,"añoAnterior:"+añoAnterior);
        int añoMax = Integer.parseInt(applicationParameter.getValue());
        int añoMin = Integer.parseInt(applicationParameter1.getValue());
        Log.d(TAG,"añoMax:"+añoMax);
        Log.d(TAG,"añoMin:"+añoMin);
        Calendar calendarMaximo = Calendar.getInstance();
        calendarMaximo.set(Calendar.YEAR,añoActual);
        calendarMaximo.set(Calendar.DAY_OF_YEAR,1);
        //calendar.set(Calendar.MONTH,1);


        Calendar calendarMinimo = Calendar.getInstance();
        calendarMinimo.set(Calendar.YEAR,añoAnterior);
        calendarMinimo.set(Calendar.DAY_OF_YEAR,1);
        //calendar1.set(Calendar.MONTH,1);

        fechaMaximaAfiliado = calendarMinimo;
        Calendar getDate = Calendar.getInstance();
        int dias = getDate.get(Calendar.DAY_OF_YEAR);
        int diaInsertar = dias-1;
        getDate.set(Calendar.DAY_OF_YEAR,diaInsertar);
        fechaMinimaAfiliado = getDate;
        //fechaMinimaAfiliado = calendarMaximo;
        //SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        //fechaMinimaAfiliado = new Date(sf.format(fechaMinimaAfiliado));
        //fechaMaximaAfiliado = new Date(sf.format(fechaMaximaAfiliado));
        System.out.println(fechaMaximaAfiliado.getTime());
        System.out.println(fechaMinimaAfiliado.getTime());
    }

    //region General Views

    private void toolbarStatusBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void navigationBarStatusBar() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CotizacionActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                frameLayout.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                CotizacionActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                frameLayout.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                CotizacionActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                frameLayout.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                CotizacionActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    //endregion

    private void getData(){
        visitaVta  = SessionManager.getInstance(this).getVisitaSession();
        if((visitaVta==null)){
            Log.d(TAG,"No hay visita en session...");
        }else{
            Log.d(TAG,"Si hay visita en Session:"+visitaVta.toString());
        }
        estado = (getIntent().getIntExtra("Estado",1));
        tvCotizacionTitle.setText(estado==1?"Cotización Inicial":"Cotización Final");
        REQUEST_STATE = REQUEST_VISITA_COTIZACION_NUEVO;
        Log.d(TAG,"Estado:"+estado);
        cvPayme.setVisibility(estado==1?View.GONE:View.VISIBLE);
        agente = (Session.getUser().getFullName());
    }

    //region Detalle de una visita reprogramada

    private void detailCotizacion(){
        if(visitaVta.getEstado()==6){
            VisitaVtaDetalle visitaVtaDetalle = SessionManager.getInstance(this).getDetalleVisita();
            if(visitaVtaDetalle!=null){
                Log.d(TAG,"Si hay detalle de la visita...");
                List<CotizacionVisita> cotizacionVisitaList = visitaVtaDetalle.getCotizacion();
                CotizacionVisita cotizacionVisita1 = null;
                for (CotizacionVisita cotizacionVisita:cotizacionVisitaList){
                    if(cotizacionVisita.getFlag()==1){
                        cotizacionVisita1 = cotizacionVisita;
                        break;
                    }
                }
                if( cotizacionVisita1!=null){
                    Log.d(TAG,"Si hay cotizacion uno...");
                    setDataDetailCotizacion(cotizacionVisita1);
                }else{
                    Log.d(TAG,"No hay cotizacion uno...");
                }

            }else{
                Log.d(TAG,"no hay detalle de la visita...");
                flagConsultoDetalle = 2;
            }
        }
    }

    private void setDataDetailCotizacion(CotizacionVisita cotizacion){
        //Afiliados
        List<AfiliadoMovil> afiliadoMovils = cotizacion.getAfiliados();
        //Tipo de Venta
        if(cotizacion.getTipoVenta().equalsIgnoreCase("1")){
            rbIndividual.setChecked(true);
            rbCorporativo.setChecked(false);
        }else{
            rbCorporativo.setChecked(true);
            rbIndividual.setChecked(false);
        }
        //Programa
        String codeProgramaSelect = null;
        for (AfiliadoMovil afiliadoMovil:afiliadoMovils){
            codeProgramaSelect = afiliadoMovil.getCodigoPrograma();
            break;
        }

        //Set Programa in Spinner
        for (int i=0;i<listProgramas.size();i++){
            final int j = i;
            if(codeProgramaSelect.equalsIgnoreCase(listProgramas.get(i).getCode())){
                ((Spinner) findViewById(R.id.cotizacion_programa_movil)).post(() -> ((Spinner) findViewById(R.id.cotizacion_programa_movil)).setSelection(j+1));
                break;
            }
        }

        //Canal
        String campanaSelected = null;
        for (AfiliadoMovil afiliadoMovil:afiliadoMovils){
            campanaSelected = afiliadoMovil.getCampana();
            break;
        }
        if(campanaSelected.equalsIgnoreCase("False")){
            ((CheckBox) findViewById(R.id.checkboxcamapana)).setChecked(false);
        }else{
            ((CheckBox) findViewById(R.id.checkboxcamapana)).setChecked(true);
        }
        //Set Afiliados in fields
        setAddAfiliadoDetail(afiliadoMovils);

        //Cantidad de cuotas
        cuotaSelectedDetail = cotizacion.getCantidadCuota();
        //Plan seleccionado
        for (Cotizacion cotizacion1:cotizacion.getRespuesta()){
            planSelectedDetail = cotizacion1.getInfo();
            break;
        }

        //Ejecutar la consulta al ws de cotizacion
        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(() -> getDataSelected(),200);
    }

    private void setAddAfiliadoDetail(List<AfiliadoMovil> list){
        for (int i=0;i<list.size();i++){
            final int j = i;
            final AfiliadoMovil afiliadoMovil = list.get(j);
            String fecha = afiliadoMovil.getFechaNacimiento();
            String sexo = afiliadoMovil.getSexo();
            String fumador = afiliadoMovil.getFlagFumador();
            final LinearLayout afiliado = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.container_afiliado, null);
            //((TextView) afiliado.findViewById(R.id.afiliado_numero_movil)).setText("AFILIADO # " + pos);
            ((Button) afiliado.findViewById(R.id.eliminar_afiliado_movil)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG,"Cantidad de Layouts onclick eliminar:"+afiliadosLayouts.size());
                    afiliadosLayouts.remove(afiliado);
                    ContainerParams.removeView(afiliado);
                    refreshTvAfiliado();
                }
            });

            SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getBaseContext(), Utils.getDataBase(getApplicationContext()), rp3.auna.Contants.GENERAL_TABLE_GENERO);
            ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).setAdapter(tipoGeneroAdapter);
            //((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).setPrompt("Seleccione un género");

            /**
             * Agregar la Fecha del afiliado
             */
            ((EditText) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).setText(fecha);
            /**
             * Agregar el Sexo que corresponde a lo seleccionado
             */
            if(sexo.trim().equalsIgnoreCase("F")){
                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).post(
                        () -> ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).setSelection(0)
                );
            }else{
                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).post(
                        () -> ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).setSelection(1)
                );
            }
            /**
             * Agregar el fumador
             */
            if(fumador.trim().equalsIgnoreCase("True")){
                ((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).setChecked(true);
            }else{
                ((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).setChecked(false);
            }

            /***
             * Agrego evento al nuevo campo de Afiliado: Fecha de Nacimiento
             */
            final int pos = afiliadosLayouts.size();
            Log.d(TAG,"Cantidad de Layouts:"+afiliadosLayouts.size());
            (afiliado.findViewById(R.id.tvafiliado_fecha_movil)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"Cantidad de Layouts onclick fecha:"+afiliadosLayouts.size());
                    Log.d(TAG,"onclick tvFecha : "+pos);
                    setFechaAfiliado(pos);
                }
            });
            ContainerParams.addView(afiliado);
            afiliadosLayouts.add(afiliado);
            refreshTvAfiliado();
        }
    }

    //endregion

    //region Todo

    private void initViews(){
        //rbIndividual.setChecked(true);
        //tipoVenta = 1;
        SelectAnual.setClickable(true);
        afiliadosLayouts = new ArrayList<>();
        //Cargo cotización en el caso de que exista
        List<GeneralValue> list = GeneralValue.getGeneralValues(Utils.getDataBase(this),Contants.GENERAL_TABLE_PROGRAMAS);
        List<GeneralValue> generalValueList = new ArrayList<>();
        for(GeneralValue obj:list){
            if(obj.getReference1()!=null){
                if(obj.getReference1().equalsIgnoreCase("A")){
                    generalValueList.add(obj);
                }
            }
        }

        listProgramas = generalValueList;
        SimpleGeneralValueAdapter programaAdapter = new SimpleGeneralValueAdapter(this, generalValueList);

        NothingSelectedSpinnerAdapter nothingSelectedSpinnerAdapter = new NothingSelectedSpinnerAdapter(
                programaAdapter,
                R.layout.spinner_empty_selected,
                this, "Seleccionar");


        ((Spinner) findViewById(R.id.cotizacion_programa_movil)).setAdapter(nothingSelectedSpinnerAdapter);
        //((Spinner) findViewById(R.id.cotizacion_programa_movil))
        //((Spinner) findViewById(R.id.cotizacion_programa_movil)).setPrompt("Seleccione un programa");


        //Configuro boton para agregar afiliado
        tvAgregarfiliado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddAfiliado();
            }
        });
        //refreshSelectedPay();
    }

    private void setAddAfiliado(){
        final LinearLayout afiliado = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.container_afiliado, null);
        //((TextView) afiliado.findViewById(R.id.afiliado_numero_movil)).setText("AFILIADO # " + pos);
        ((Button) afiliado.findViewById(R.id.eliminar_afiliado_movil)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cantidad de Layouts onclick eliminar:"+afiliadosLayouts.size());
                afiliadosLayouts.remove(afiliado);
                ContainerParams.removeView(afiliado);
                refreshTvAfiliado();
            }
        });

        SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getBaseContext(), Utils.getDataBase(getApplicationContext()), rp3.auna.Contants.GENERAL_TABLE_GENERO);
        ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).setAdapter(tipoGeneroAdapter);
        //((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).setPrompt("Seleccione un género");


        /***
         * Agrego evento al nuevo campo de Afiliado: Fecha de Nacimiento
         */
        final int pos = afiliadosLayouts.size();
        Log.d(TAG,"Cantidad de Layouts:"+afiliadosLayouts.size());
        (afiliado.findViewById(R.id.tvafiliado_fecha_movil)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Cantidad de Layouts onclick fecha:"+afiliadosLayouts.size());
                Log.d(TAG,"onclick tvFecha : "+pos);
                setFechaAfiliado(pos);
            }
        });
        ContainerParams.addView(afiliado);
        afiliadosLayouts.add(afiliado);
        refreshTvAfiliado();
    }

    private void fraccionamientoSelectedIndex(final int i){
        spFraccionamiento.post(new Runnable() {
            @Override
            public void run() {
                spFraccionamiento.setSelection(i);
            }
        });
    }

    private void refreshSelectedPay(){
        if(tipoVenta!=-1 && tipoVenta==1){
        SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
        SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
        SelectTD.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
        ContainerAnual.setBackgroundColor(getResources().getColor(R.color.White));
        ContainerTD.setBackgroundColor(getResources().getColor(R.color.White));
        ContainerTC.setBackgroundColor(getResources().getColor(R.color.White));

        //seleccionar las cuotas sin haber seleccionado antes el pago anual
            spFraccionamiento.setEnabled(false);

        ContainerAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ContainerAnual onclick...");
                select = 1;
                selectPlanCotizacion = 1;
                ContainerAnual.setBackgroundColor(getResources().getColor(R.color.alternative_background));
                ContainerTD.setBackgroundColor(getResources().getColor( R.color.White));
                ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
                SelectAnual.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                SelectTC.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                SelectTD.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                spFraccionamiento.setEnabled(true);
                if(estado==2){
                    frecuenciaPagoSelected = 1;
                    rbFrecuenciaAnual.setEnabled(true);
                    rbFrecuenciaMensual.setChecked(false);
                    rbFrecuenciaAnual.setChecked(true);
                    rbFrecuenciaMensual.setChecked(false);

                }
            }
        });
        ContainerTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ContainerTD onclick...");
                select = 3;
                selectPlanCotizacion = 3;
                ContainerAnual.setBackgroundColor(getResources().getColor( R.color.White));
                ContainerTD.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
                SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
                SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
                SelectTD.setBackgroundColor(getResources().getColor(R.color.alternative_background));
                fraccionamientoSelectedIndex(0);
                spFraccionamiento.setEnabled(false);
                if(estado==2){
                    frecuenciaPagoSelected = 2;
                    rbFrecuenciaMensual.setEnabled(true);
                    rbFrecuenciaAnual.setChecked(false);
                    rbFrecuenciaAnual.setChecked(false);
                    rbFrecuenciaMensual.setChecked(true);
                }
            }
        });
        ContainerTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ContainerTC onclick...");
                select = 2;
                selectPlanCotizacion = 2;
                ContainerAnual.setBackgroundColor(getResources().getColor(R.color.White));
                ContainerTD.setBackgroundColor(getResources().getColor(R.color.White));
                ContainerTC.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                SelectAnual.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                SelectTC.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                SelectTD.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                fraccionamientoSelectedIndex(0);
                spFraccionamiento.setEnabled(false);
                if(estado==2){
                    frecuenciaPagoSelected = 2;
                    rbFrecuenciaMensual.setEnabled(true);
                    rbFrecuenciaAnual.setChecked(false);
                    rbFrecuenciaAnual.setChecked(false);
                    rbFrecuenciaMensual.setChecked(true);
                }
            }
        });

        SelectAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"SelectAnual onclick...");
                select = 1;
                selectPlanCotizacion = 1;
                ContainerAnual.setBackgroundColor(getResources().getColor(R.color.alternative_background));
                ContainerTD.setBackgroundColor(getResources().getColor( R.color.White));
                ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
                SelectAnual.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                SelectTC.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                SelectTD.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                spFraccionamiento.setEnabled(true);
                if(estado==2){
                    frecuenciaPagoSelected = 1;
                    rbFrecuenciaAnual.setEnabled(true);
                    rbFrecuenciaMensual.setChecked(false);
                    rbFrecuenciaAnual.setChecked(true);
                    rbFrecuenciaMensual.setChecked(false);
                }
            }
        });
        SelectTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"SelectTC onclick...");
                select = 2;
                selectPlanCotizacion = 2;
                ContainerAnual.setBackgroundColor(getResources().getColor( R.color.White));
                ContainerTD.setBackgroundColor(getResources().getColor( R.color.White));
                ContainerTC.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                SelectAnual.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                SelectTC.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                SelectTD.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                fraccionamientoSelectedIndex(0);
                spFraccionamiento.setEnabled(false);
                if(estado==2){
                    frecuenciaPagoSelected = 2;
                    rbFrecuenciaMensual.setEnabled(true);
                    rbFrecuenciaAnual.setChecked(false);
                    rbFrecuenciaAnual.setChecked(false);
                    rbFrecuenciaMensual.setChecked(true);
                }
            }
        });
        SelectTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"SelectTD onclick...");
                select = 3;
                selectPlanCotizacion = 3;
                ContainerAnual.setBackgroundColor(getResources().getColor( R.color.White));
                ContainerTD.setBackgroundColor(getResources().getColor(R.color.alternative_background));
                ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
                SelectAnual.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                SelectTC.setBackgroundColor(getResources().getColor( R.color.color_background_selector));
                SelectTD.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                fraccionamientoSelectedIndex(0);
                spFraccionamiento.setEnabled(false);
                if(estado==2){
                    frecuenciaPagoSelected = 2;
                    rbFrecuenciaMensual.setEnabled(true);
                    rbFrecuenciaAnual.setChecked(false);
                    rbFrecuenciaAnual.setChecked(false);
                    rbFrecuenciaMensual.setChecked(true);
                }
            }
        });
        }else{
            Log.v(TAG,"refreshSelectedPay TipoVenta != 1");
            ContainerAnual.setBackgroundColor(getResources().getColor( R.color.White));
            ContainerTD.setBackgroundColor(getResources().getColor( R.color.White));
            ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
            SelectAnual.setBackgroundColor(getResources().getColor( R.color.White));
            SelectTC.setBackgroundColor(getResources().getColor( R.color.White));
            SelectTD.setBackgroundColor(getResources().getColor( R.color.White));
            ContainerAnual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"ContainerAnual onClick venta corporativa...");
                }
            });
            ContainerTD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"ContainerTD onClick venta corporativa...");
                }
            });
            ContainerTC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"ContainerTC onClick venta corporativa...");
                }
            });
            SelectAnual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"SelectAnual onclick venta corporativa...");
                }
            });
            SelectTC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"SelectTC onclick venta corporativa...");
                }
            });
            SelectTD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"SelecteTD onclick venta corporativa...");
                }
            });
        }
    }

    private void refreshTvAfiliado(){
        for(int i = 0;i<afiliadosLayouts.size();i++){
            LinearLayout afiliado = afiliadosLayouts.get(i);
            int j = i +1;
            ((TextView) afiliado.findViewById(R.id.afiliado_numero_movil)).setText("AFILIADO # " + j);
        }
    }

    private void setFechaAfiliado(final int i){
        Log.d(TAG,"position de la fecha:"+i);
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar today = Calendar.getInstance();
        final LinearLayout afiliado = afiliadosLayouts.get(i);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.YEAR, year);
                SimpleDateFormat sdf = new SimpleDateFormat(Contants.DATE_FORMAT, Locale.US);
                //if(validateCurrentTimeAfiliado(myCalendar.getTime())){
                    selectedDateAfiliado = myCalendar.getTime();
                    ((EditText) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).setText(sdf.format(myCalendar.getTime()));
                //}
            }
        };

        Log.d(TAG,"Millis Min:"+fechaMinimaAfiliado.getTimeInMillis());
        Log.d(TAG,"Millis Max:"+fechaMaximaAfiliado.getTimeInMillis());
        Log.d(TAG,"Millis System:"+System.currentTimeMillis());
        if(selectedDateAfiliado==null){
            final DatePickerDialog dialog = new DatePickerDialog(this, R.style.TimePicker, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(fechaMaximaAfiliado.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(fechaMinimaAfiliado.getTimeInMillis());
            dialog.show();
        }else{
            myCalendar.setTime(selectedDateAfiliado);
            final DatePickerDialog dialog = new DatePickerDialog(this, R.style.TimePicker, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(fechaMaximaAfiliado.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(fechaMinimaAfiliado.getTimeInMillis());
            dialog.show();
        }

    }

    private void getDataSelected(){
        if(validate()){
            if(consultarCotizacion==1){
                int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
                rp3.auna.models.Agente a = rp3.auna.models.Agente.getAgente(Utils.getDataBase(this),idAgente);
                Log.d(TAG,"Agente:"+a.toString());
                cotizacionMovil = new CotizacionMovil();
                cotizacionMovil.setIdCotizacion(0);
                cotizacionMovil.setEmail("");
                cotizacionMovil.setEmailAgente(a.getEmail());
                cotizacionMovil.setIdAgente(idAgente);
                cotizacionMovil.setIdVisita(visitaVta.getIdVisita());
                cotizacionMovil.setEstado(estado);
                cotizacionMovil.setFlag(estado);
                cotizacionMovil.setAgente(agente);
                if(estado==1){
                    cotizacionMovil.setNombreProspecto(ProspectoVtaDb.getProspectoNombreByIdVisita(visitaVta.getIdVisita(),Utils.getDataBase(this)));
                }else if(estado==2){
                    cotizacionMovil.setNombreProspecto(afiliadoContratante.getNombre()+" "+afiliadoContratante.getApellidoPaterno());
                }
                List<AfiliadoMovil> afiliadoMovils = new ArrayList<>();
                JSONArray jsonArray = new JSONArray();
                JSONObject obj = new JSONObject();
                for(int i = 0; i < afiliadosLayouts.size(); i++)
                {
                    LinearLayout afiliado = afiliadosLayouts.get(i);
                    AfiliadoMovil afiliadoMovil = new AfiliadoMovil();

                    try
                    {
                        afiliadoMovil.setTipoOperacion(Contants.COTIZADOR_TIPO_OPERACION);
                        afiliadoMovil.setCodigoPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getCode());
                        afiliadoMovil.setFechaNacimiento(((EditText) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).getText().toString());
                        if(((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).isChecked()){
                            afiliadoMovil.setFlagFumador("True");
                        }else{
                            afiliadoMovil.setFlagFumador("False");
                        }
                        afiliadoMovil.setIdRegistro("00" + (i+1));
                        afiliadoMovil.setOrigenSolicitud(Contants.COTIZADOR_ORIGEN_SOLICITUD);
                        afiliadoMovil.setSexo(((GeneralValue)((Spinner)afiliado.findViewById(R.id.afiliado_sexo_movil)).getSelectedItem()).getCode());

                        afiliadoMovil.setUserMovil(Contants.COTIZADOR_USER);
                        if(tipoVenta==2){
                            afiliadoMovil.setRucEmpresa(etRucEmpresa.getText().toString().trim());
                            afiliadoMovil.setCanal(Contants.COTIZADOR_CANAL_CORPORATIVO_INDIVIDUAL);
                            afiliadoMovil.setCampana("False");
                        }else{
                            afiliadoMovil.setRucEmpresa(null);
                            afiliadoMovil.setCanal(Contants.COTIZADOR_CANAL_FUERZA_VENTA_INDIVIDUAL);
                            if(((CheckBox) findViewById(R.id.checkboxcamapana)).isChecked()){
                                afiliadoMovil.setCampana("True");
                            }else{
                                afiliadoMovil.setCampana("False");
                            }
                        }
                        Log.d(TAG,afiliadoMovil.toString());
                    }
                    catch (Exception ex)
                    {
                        Log.d(TAG,"bucle for exception:"+ex.getMessage());
                    }
                    afiliadoMovils.add(afiliadoMovil);
                }
                cotizacionMovil.setAfiliados(afiliadoMovils);
                try {
                    obj.put("IdCotizacion",0);
                    obj.put("IdAgente", PreferenceManager.getInt(Contants.KEY_IDAGENTE));
                    obj.put("IdVisita",visitaVta.getIdVisita());
                    obj.put("Email","nvillasanchez@gmail.com");
                    obj.put("Afiliados",jsonArray);
                } catch (JSONException e) {
                    Log.d(TAG,"JSONEXCEPTION:"+e.getMessage());
                    e.printStackTrace();
                }
                Log.d(TAG, obj.toString());
                Log.d(TAG,"json:"+new Gson().toJson(cotizacionMovil));
                showDialogCotizarClient(cotizacionMovil);
            }else if(consultarCotizacion==2){
                cotizacionVirtual = new CotizacionVirtual();
                SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                String fechaVenta = format.format(calendar.getTime());
                cotizacionVirtual.setFechaVenta(fechaVenta);
                int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
                rp3.auna.models.Agente a = rp3.auna.models.Agente.getAgente(Utils.getDataBase(this),idAgente);
                Log.d(TAG,"Agente:"+a.toString());
                cotizacionVirtual.setIdCotizacion(0);
                cotizacionVirtual.setEmail("");
                cotizacionVirtual.setEmailAgente(a.getEmail());
                cotizacionVirtual.setIdAgente(idAgente);
                cotizacionVirtual.setIdVisita(visitaVta.getIdVisita());
                cotizacionVirtual.setEstado(estado);
                cotizacionVirtual.setFlag(estado);
                cotizacionVirtual.setAgente(agente);
                if(estado==1){
                    cotizacionVirtual.setNombreProspecto(ProspectoVtaDb.getProspectoNombreByIdVisita(visitaVta.getIdVisita(),Utils.getDataBase(this)));
                }else if(estado==2){
                    cotizacionVirtual.setNombreProspecto(afiliadoContratante.getNombre()+" "+afiliadoContratante.getApellidoPaterno());
                }
                List<AfiliadoMovil> afiliadoMovils = new ArrayList<>();
                JSONArray jsonArray = new JSONArray();
                JSONObject obj = new JSONObject();
                for(int i = 0; i < afiliadosLayouts.size(); i++)
                {
                    LinearLayout afiliado = afiliadosLayouts.get(i);
                    AfiliadoMovil afiliadoMovil = new AfiliadoMovil();

                    try
                    {
                        afiliadoMovil.setTipoOperacion(Contants.COTIZADOR_TIPO_OPERACION);
                        afiliadoMovil.setCodigoPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getCode());
                        afiliadoMovil.setFechaNacimiento(((EditText) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).getText().toString());
                        if(((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).isChecked()){
                            afiliadoMovil.setFlagFumador("True");
                        }else{
                            afiliadoMovil.setFlagFumador("False");
                        }
                        afiliadoMovil.setIdRegistro("00" + (i+1));
                        afiliadoMovil.setOrigenSolicitud(Contants.COTIZADOR_ORIGEN_SOLICITUD);
                        afiliadoMovil.setSexo(((GeneralValue)((Spinner)afiliado.findViewById(R.id.afiliado_sexo_movil)).getSelectedItem()).getCode());

                        afiliadoMovil.setUserMovil(Contants.COTIZADOR_USER);
                        if(tipoVenta==2){
                            afiliadoMovil.setRucEmpresa(etRucEmpresa.getText().toString().trim());
                            afiliadoMovil.setCanal(Contants.COTIZADOR_CANAL_CORPORATIVO_INDIVIDUAL);
                            afiliadoMovil.setCampana("False");
                        }else{
                            afiliadoMovil.setRucEmpresa(null);
                            afiliadoMovil.setCanal(Contants.COTIZADOR_CANAL_FUERZA_VENTA_INDIVIDUAL);
                            if(((CheckBox) findViewById(R.id.checkboxcamapana)).isChecked()){
                                afiliadoMovil.setCampana("True");
                            }else{
                                afiliadoMovil.setCampana("False");
                            }
                        }
                        Log.d(TAG,afiliadoMovil.toString());
                    }
                    catch (Exception ex)
                    {
                        Log.d(TAG,"bucle for exception:"+ex.getMessage());
                    }
                    afiliadoMovils.add(afiliadoMovil);
                }
                cotizacionVirtual.setAfiliados(afiliadoMovils);
                try {
                    obj.put("IdCotizacion",0);
                    obj.put("IdAgente", PreferenceManager.getInt(Contants.KEY_IDAGENTE));
                    obj.put("IdVisita",visitaVta.getIdVisita());
                    obj.put("Email","nvillasanchez@gmail.com");
                    obj.put("Afiliados",jsonArray);
                } catch (JSONException e) {
                    Log.d(TAG,"JSONEXCEPTION:"+e.getMessage());
                    e.printStackTrace();
                }
                Log.d(TAG, obj.toString());
                Log.d(TAG,"json:"+new Gson().toJson(cotizacionMovil));
                showDialogCotizadorVirtualClient(cotizacionVirtual);
            }

        }
    }

    private void setResponseCotizacion(List<Cotizacion> cotizacion){
        Log.v(TAG,"setResponseCotizacion Cantidad respuestas::"+cotizacion.size());
        Log.v(TAG,"setResponseCotizacion Cantidad AfiliadosLayout:"+afiliadosLayouts.size());
        ContainerAnual.removeAllViews();
        ContainerTD.removeAllViews();
        ContainerTC.removeAllViews();
        try
        {
            //Control de errores
            NumberFormat numberFormat;
            Locale locale = new Locale("es", "pe");
            numberFormat = NumberFormat.getInstance(locale);
            //numberFormat.setMaximumFractionDigits();
            numberFormat.setMinimumFractionDigits(2);

            totalAnual = 0;
            totalTC = 0;
            totalTD = 0;
            boolean hayError = false;
            cotizacionAfiliados = new ArrayList<>();
            cotizacionFraccionamientos = new ArrayList<>();
            for(Cotizacion cotizacionAfiliado:cotizacion){
                if(cotizacionAfiliado.getIdOperacion().equalsIgnoreCase("C")){
                    cotizacionAfiliados.add(cotizacionAfiliado);
                }
            }
            if(consultarCotizacion==1){
                cotizacionMovil.setRespuesta(cotizacion);
            }else{
                cotizacionVirtual.setRespuesta(cotizacion);
            }

            //cotizacionMovil.setRespuesta(cotizacionAfiliados);
            Log.v(TAG,"setResponseCotizacion Cantidad de Cotizados:"+cotizacionAfiliados.size());
            for(Cotizacion cotizacionFraccion:cotizacion){
                if(cotizacionFraccion.getIdOperacion().equalsIgnoreCase("F")){
                    cotizacionFraccionamientos.add(cotizacionFraccion);
                }
            }
            Log.v(TAG,"setResponseCotizacion Cantidad de Fracciones:"+cotizacionFraccionamientos.size());
            for (int i = 0; i < afiliadosLayouts.size(); i++) {
                LinearLayout afiliado = afiliadosLayouts.get(i);
                Cotizacion jsonObject = cotizacionAfiliados.get(i);

                if (jsonObject.getResult().equalsIgnoreCase("1")) {
                    hayError = true;
                    break;
                }
                LinearLayout AfiliadoAnual = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.container_afiliado_cotizacion, null);
                LinearLayout AfiliadoTC = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.container_afiliado_cotizacion, null);
                LinearLayout AfiliadoTD = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.container_afiliado_cotizacion, null);

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_numero_movil)).setText("AFILIADO # " + (i + 1));
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_numero_movil)).setText("AFILIADO # " + (i + 1));
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_numero_movil)).setText("AFILIADO # " + (i + 1));

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_edad_movil)).setText(((TextView) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).getText().toString());
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_edad_movil)).setText(((TextView) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).getText().toString());
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_edad_movil)).setText(((TextView) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).getText().toString());

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_sexo_movil)).setText(((GeneralValue) ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).getSelectedItem()).getValue());
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_sexo_movil)).setText(((GeneralValue) ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).getSelectedItem()).getValue());
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_sexo_movil)).setText(((GeneralValue) ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).getSelectedItem()).getValue());

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_fumador_movil)).setText(((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).isChecked() ? "Es fumador" : "No es fumador");
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_fumador_movil)).setText(((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).isChecked() ? "Es fumador" : "No es fumador");
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_fumador_movil)).setText(((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).isChecked() ? "Es fumador" : "No es fumador");

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_precio_movil)).setText("S/ " + numberFormat.format(Double.parseDouble(jsonObject.getAnual())));
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_precio_movil)).setText("S/ " + numberFormat.format(Double.parseDouble(jsonObject.getTC())));
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_precio_movil)).setText("S/ " + numberFormat.format(Double.parseDouble(jsonObject.getTD())));
                totalAnual = totalAnual + Double.parseDouble(jsonObject.getAnual());
                totalTC = totalTC + Double.parseDouble(jsonObject.getTC());
                totalTD = totalTD + Double.parseDouble(jsonObject.getTD());

                ContainerAnual.addView(AfiliadoAnual);
                ContainerTC.addView(AfiliadoTC);
                ContainerTD.addView(AfiliadoTD);
            }

            if (!hayError) {
                Log.d(TAG," No !hayError...");
                TotalAnual.setText("S/ " + numberFormat.format(totalAnual)+" anual");
                TotalTC.setText("S/ " + numberFormat.format(totalTC)+" al mes");
                TotalTD.setText("S/ " + numberFormat.format(totalTD)+" al mes");

                refreshSelectedPay();
                //Mostrar resultado de cotizacionMovil
                if(tipoVenta==1){
                    Log.v(TAG,"setResponseCotizacion TipoVenta==1");
                    refreshFraccionamientos();
                    lnFraccionamientos.setVisibility(View.VISIBLE);
                }else{
                    flagConsultoDetalle = 2;
                    Log.v(TAG,"setResponseCotizacion TipoVenta==2");
                    lnFraccionamientos.setVisibility(View.GONE);
                }
                Log.d(TAG,"responseView.setVisibility VISIBLE...");
                ResponseView.setVisibility(View.VISIBLE);
                EmptyView.setVisibility(View.GONE);


            } else
            {
                Log.d(TAG,"hayError...");
                Toast.makeText(this, "Hubo un problema al mostrar el resultado de la cotizacionMovil, intentelo nuevamente porfavor..", Toast.LENGTH_SHORT).show();
                //SE DEBE MOSTRAR EL ERROR QUE RETORNA EL SERVICIO
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d(TAG,"exception:"+ex.getMessage());
        }
    }

    private boolean validate(){
        if(tipoVenta==-1){
            Toast.makeText(this, "Seleccione un tipo de venta porfavor", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(afiliadosLayouts.size()==0){
            Log.d(TAG,"No agrego a ningun afiliado...");
            Toast.makeText(this, "Porfavor agregue a un afiliado", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem())==null){
            Toast.makeText(this, "Seleccione un programa porfavor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getCode()==null){
            Toast.makeText(this, "Seleccione un programa porfavor", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(tipoVenta==2){
            if(TextUtils.isEmpty(etRucEmpresa.getText())){
                Toast.makeText(this, "Ingrese un numero de RUC de la empresa", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(etRucEmpresa.getText().toString().trim().length()!=11){
                Toast.makeText(this, "Ingrese un RUC de 11 digitos", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(consultarCotizacion==-1){
            Toast.makeText(this, "Seleccione un tipo de cotización por favor.", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean estado = true;
        for(int j = 0 ; j<afiliadosLayouts.size(); j++){
            LinearLayout afiliado = afiliadosLayouts.get(j);
            if(TextUtils.isEmpty(((EditText)afiliado.findViewById(R.id.tvafiliado_fecha_movil)).getText())){
                estado = false;
                //((EditText)afiliado.findViewById(R.id.tvafiliado_fecha_movil)).setError("Debe asignar una fecha");
                Toast.makeText(this, "Coloque una fecha de nacimiento al Afiliado #"+(j+1), Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return estado;
    }

    private void showDialogEnvioEmail(){
        if(consultarCotizacion==-1){
            Toast.makeText(this, "Debe realizar una cotización...", Toast.LENGTH_SHORT).show();
        }
        if(consultarCotizacion == 1){
            if(cotizacionMovil == null){
                Toast.makeText(this, "Debe realizar una cotizacion para poder enviar un email...", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(consultarCotizacion == 2){
            if(cotizacionVirtual == null){
                Toast.makeText(this, "Debe realizar una cotizacion para poder enviar un email...", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        /*if(cotizacionMovil.getRespuesta()==null || cotizacionVirtual.getRespuesta() == null){
            Toast.makeText(this, "Debe realizar una consulta de cotizacion para poder enviar un email...", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(tipoVenta==-1){
            Toast.makeText(this, "Elija un plan a cotizar...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(tipoVenta==1){
            if(select==-1){
                Toast.makeText(this, "Elija un plan a cotizar...", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        validate();

        if(selectPlanCotizacion==1 && tipoVenta==1){
            Log.d(TAG,"Selecciono Anual a pagar...");
            if(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem())==null){
                Toast.makeText(this, "Seleccione una fraccion porfavor", Toast.LENGTH_SHORT).show();
                return;
            }
            if(consultarCotizacion==1){
                cotizacionMovil.setCantidadCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getCode());
                cotizacionMovil.setPrecioCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1());
            }else{
                cotizacionVirtual.setCantidadCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getCode());
                cotizacionVirtual.setPrecioCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1());
            }

        }else{
            if(consultarCotizacion==1){
                cotizacionMovil.setCantidadCuota("0");
                cotizacionMovil.setPrecioCuota("0");
                cotizacionMovil.setTipoVenta("2");
            }else{
                cotizacionVirtual.setCantidadCuota("0");
                cotizacionVirtual.setPrecioCuota("0");
                cotizacionVirtual.setTipoVenta("2");
            }

        }
        setTipoCotizacion();
        if(consultarCotizacion==1){
            cotizacionMovil.setIdVisita(visitaVta.getIdVisita());
            cotizacionMovil.setPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getValue());
            cotizacionMovil.setTipoVenta(tipoVenta==1?"1":"2");
        }else{
            cotizacionVirtual.setIdVisita(visitaVta.getIdVisita());
            cotizacionVirtual.setPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getValue());
            cotizacionVirtual.setTipoVenta(tipoVenta==1?"1":"2");
        }

        String email = null;// = ProspectoVtaDb.getProspectoEmailByIdVisita(visitaVta.getIdVisita(),Utils.getDataBase(this));
        int idProspecto = SessionManager.getInstance(this).getVisitaSession().getIdCliente();
        Log.d(TAG,"idProspecto:"+idProspecto);
        final ProspectoVtaDb prospecto = ProspectoVtaDb.getProspectoIdProspecto(Utils.getDataBase(this),idProspecto);
        if(prospecto!=null){
            Log.d(TAG,"Si hay prospecto:"+prospecto.toString());
            email=prospecto.getEmail();
        }else{
            Log.d(TAG,"No hay prospecto...");
        }
        //ProspectoVtaDb.getAll(Utils.getDataBase(this));
        Log.d(TAG,"Email prospecto:"+email);
        Log.d(TAG,"Cotizacion a enviar:"+new Gson().toJson(cotizacionMovil));
        EmailCotizacionDialog dialogEmail = EmailCotizacionDialog.newInstance(new EmailCotizacionDialog.callbackEmail() {
            @Override
            public void onSelectedCancelar() {
                Log.d(TAG,"onSelectedCancelar...");
            }

            @Override
            public void onEmailEnviar(String email) {
                Log.d(TAG,"onEmailEnviar:"+email+"...");
                if(consultarCotizacion==1){
                    cotizacionMovil.setEmail(email);
                }else{
                    cotizacionVirtual.setEmail(email);
                }

                prospecto.setEmail(email);
                ProspectoVtaDb.update(Utils.getDataBase(getApplicationContext()),prospecto);
                String movil = null ;
                if(consultarCotizacion==1){
                    movil = new Gson().toJson(cotizacionMovil);
                    Log.d(TAG,"json Email:"+new Gson().toJson(cotizacionMovil));
                }else{
                    movil = new Gson().toJson(cotizacionVirtual);
                    Log.d(TAG,"json Email:"+new Gson().toJson(cotizacionVirtual));
                }
                final ProgressDialog progressDialog = new ProgressDialog(CotizacionActivity.this,R.style.AppCompatAlertDialogStyle);
                progressDialog.setTitle(CotizacionActivity.this.getResources().getString(R.string.appname_marketforce));
                progressDialog.setMessage("Enviando Email...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new EmailCotizacionClient(getApplicationContext(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG,"onFailure...");
                        Log.d(TAG,"IOException:"+e.getMessage());
                        progressDialog.dismiss();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CotizacionActivity.this, R.string.message_error_sync_connection_server_fail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        progressDialog.dismiss();
                        Log.d(TAG,"onResponse...");
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(response.isSuccessful()){
                                    sendEmail = true;
                                    Log.d(TAG,"response.isSuccessful...");

                                    Toast.makeText(CotizacionActivity.this, "Email enviado Satisfactoriamente.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Log.d(TAG,"response.!isSucessful...");
                                    Toast.makeText(CotizacionActivity.this, R.string.message_error_general, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }).cotizar(movil);
            }
        },email,0);
        Bundle todo = new Bundle();
        todo.putInt("Pago",0);
        dialogEmail.setArguments(todo);
        dialogEmail.show(getSupportFragmentManager(),"");
    }

    private void showDialogGrabarCotizacion(int updateVisita){
        if(consultarCotizacion==-1){
            Toast.makeText(this, "Debe realizar una cotización...", Toast.LENGTH_SHORT).show();
        }
        if(consultarCotizacion == 1){
            if(cotizacionMovil == null){
                Toast.makeText(this, "Debe realizar una cotizacion para poder enviar un email...", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(consultarCotizacion == 2){
            if(cotizacionVirtual == null){
                Toast.makeText(this, "Debe realizar una cotizacion para poder enviar un email...", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        /*if(cotizacionMovil.getRespuesta()==null || cotizacionVirtual.getRespuesta()==null){
            Toast.makeText(this, "Debe realizar una cotizacion para poder enviar un email...", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(tipoVenta==-1){
            Toast.makeText(this, "Elija un plan a cotizar...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(tipoVenta==1){
            if(select==-1){
                Toast.makeText(this, "Elija un plan a cotizar...", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        validate();
        if(select==1 && tipoVenta == 1){
            if(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem())==null){
                Toast.makeText(this, "Seleccione una fraccion porfavor", Toast.LENGTH_SHORT).show();
                return;
            }
            if(consultarCotizacion==1){
                cotizacionMovil.setCantidadCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getCode());
                cotizacionMovil.setPrecioCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1());
            }else{
                cotizacionVirtual.setCantidadCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getCode());
                cotizacionVirtual.setPrecioCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1());
            }

        }else{
            if(consultarCotizacion==1){
                cotizacionMovil.setCantidadCuota("0");
                cotizacionMovil.setPrecioCuota("0");
            }else{
                cotizacionVirtual.setCantidadCuota("0");
                cotizacionVirtual.setPrecioCuota("0");
            }

        }
        setTipoCotizacion();
        if(consultarCotizacion==1){
            cotizacionMovil.setPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getValue());
            cotizacionMovil.setTipoVenta(tipoVenta==1?"1":"2");
        }else{
            cotizacionVirtual.setPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getValue());
            cotizacionVirtual.setTipoVenta(tipoVenta==1?"1":"2");
        }

        if(estado==1){
            if(!validateSendEmail()){
                AlertDialog dialog = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle)
                        .setTitle(this.getResources().getString(R.string.appname_marketforce))
                        .setMessage("No ha enviado la cotización por correo.\n" +
                                "¿Desea guardar la cotización y continuar la venta?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"onClick si guardar...");
                                //showDialogSolicitud();
                                saveResponseCotizacion(visitaVta.getIdVisita(),"F",0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG,"onClick No guardar...");
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }else{
                saveResponseCotizacion(visitaVta.getIdVisita(),"F",0);
                //showDialogSolicitud();
            }
        }else if(estado==2){
            saveResponseCotizacion(visitaVta.getIdVisita(),"F",updateVisita);
        }

    }

    private void setTipoCotizacion(){
        List<Cotizacion> cotizaciones = null;
        if(consultarCotizacion==1){
            cotizaciones= cotizacionMovil.getRespuesta();
        }else{
            cotizaciones= cotizacionVirtual.getRespuesta();
        }

        List<Cotizacion> seleccionados = new ArrayList<>();
        switch (select){
            case 1:
                for(Cotizacion coti:cotizaciones){
                    coti.setInfo("Anual");
                    seleccionados.add(coti);
                }
                break;
            case 2:
                for(Cotizacion coti:cotizaciones){
                    coti.setInfo("TC");
                    seleccionados.add(coti);
                }
                break;
            case 3:
                for(Cotizacion coti:cotizaciones){
                    coti.setInfo("TD");
                    seleccionados.add(coti);
                }
                break;
        }
        if(consultarCotizacion==1){
            cotizacionMovil.setRespuesta(seleccionados);
        }else{
            cotizacionVirtual.setRespuesta(seleccionados);
        }

    }

    private boolean validateSendEmail(){
        if(sendEmail)
            return true;
        return false;
    }

    private void saveResponseCotizacion(final int idVisita, final String tipo, final int updateVisita){
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle("Guardando Cotizacion");
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String movil = null;
        if(consultarCotizacion==1){
            movil = new Gson().toJson(cotizacionMovil);
        }else{
            movil = new Gson().toJson(cotizacionVirtual);
        }

        new SaveCotizacionClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure...");
                Log.d(TAG,"IOException:"+e.getMessage());
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CotizacionActivity.this, getApplicationContext().getResources().getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Log.d(TAG,"isSuccessful...");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Es Cotización Inicial
                            if(estado==1){
                                //Aqui preguntar la autenticación por huella
                                //setDataSolicitud(visitaVta.getIdVisita(),"V");
                                showAutenticationFingerPrint();
                                //setDataSolicitud(idVisita,tipo);
                            }
                            //Es Cotización Final
                            else if(estado==2){
                                //Es Tipo Efectivo
                                if(updateVisita==2){
                                    showEmailInstrucciones(2);
                                }
                                //Es  Regular ó En Línea
                                else if (updateVisita ==1){
                                    showEmailAfiliacionProceso(1);
                                }
                                else if(updateVisita ==3){
                                    VentaRegularData ventaRegularData = SessionManager.getInstance(CotizacionActivity.this).getVentaRegular();
                                    showEmailAfiliacionProcesoClient(new Gson().toJson(ventaRegularData),updateVisita);
                                }
                                //showUpdateVisitaClient(updateVisita,null,null,0);
                            }

                        }
                    });
                }else{
                    Log.d(TAG,"NoisSuccessful...");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CotizacionActivity.this, getApplicationContext().getResources().getString(R.string.message_error_sync_general_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).cotizar(movil);
    }

    private void setDataSolicitud(int idVisita,String tipo){
        try{
            Intent intent = new Intent(this,CaptacionSolicitudActivity.class);
            intent.putExtra("IdVisita",idVisita);
            intent.putExtra("Tipo",tipo);
            intent.putExtra("Venta",tipoVenta);
            intent.putExtra("Programa",((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getCode());
            intent.putExtra("EsNuevo",1);
            String costo;
            if(selectPlanCotizacion==1){
                costo = String.valueOf(totalAnual);
            }else if(selectPlanCotizacion==2){
                costo = String.valueOf(totalTC);
            }else {
                costo = String.valueOf(totalTD);
            }
            intent.putExtra("Costo",costo);
            visitaVta.setEstado(2);
            SessionManager.getInstance(CotizacionActivity.this).createVisitaSession(visitaVta);
            startActivityForResult(intent,REQUEST_VISITA_SOLICITUD_COTIZACION_INICIAL);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setDataSolicitudBack(int idVisita,String tipo){
        String programa = ((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getCode();
        int tipoVenta = this.tipoVenta;
        String costo;
        if(selectPlanCotizacion==1){
            costo = String.valueOf(totalAnual);
        }else if(selectPlanCotizacion==2){
            costo = String.valueOf(totalTC);
        }else {
            costo = String.valueOf(totalTD);
        }
        //validar datos
        if(programa==null){
            programa = "60";
        }
        else if(programa.length()==0){
            programa="60";
        }
        //
        Intent intent = new Intent(this,CaptacionSolicitudActivity.class);
        intent.putExtra("IdVisita",idVisita);
        intent.putExtra("Tipo",tipo);
        intent.putExtra("Venta",tipoVenta);
        intent.putExtra("Programa",programa);
        intent.putExtra("EsNuevo",0);
        intent.putExtra("Costo",costo);
        visitaVta.setEstado(2);
        SessionManager.getInstance(this).createVisitaSession(visitaVta);
        startActivityForResult(intent,REQUEST_VISITA_SOLICITUD_COTIZACION_INICIAL);
    }

    private void showDialogCotizarClient(CotizacionMovil cotizacionzacionMovil){
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.appname_marketforce);
        progressDialog.setMessage("Consultando Cotización...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new CotizadorClient(this, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(CotizacionActivity.this, "Hubo un problema en el servidor...intentelo mas tarde...", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                Log.d(TAG,"onResponse...");
                String json = response.body().string();
                if(response.isSuccessful()){
                    Log.d(TAG,"isSuccessful...");
                    TypeToken<List<Cotizacion>> type = new TypeToken<List<Cotizacion>>(){};
                    cotizacionList = new Gson().fromJson(json,type.getType());
                    Log.d(TAG,"Hay "+cotizacionList.size()+" respuestas de cotizacionMovil...");
                    Log.v(TAG,json);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        boolean procede = true;
                        for(Cotizacion cotizacion:cotizacionList){
                            if(cotizacion.getResult().equalsIgnoreCase("1")){
                                for(GeneralValue generalValue:listExcepcions){
                                    if(cotizacion.getInfo().contains(generalValue.getCode())){
                                        Toast.makeText(CotizacionActivity.this, generalValue.getValue(), Toast.LENGTH_SHORT).show();
                                        Log.v(TAG,"Excepcion General:"+generalValue.toString());
                                        procede = false;
                                        break;
                                    }
                                }
                                procede = false;
                                Log.d(TAG,cotizacion.toString());
                                //Toast.makeText(CotizacionActivity.this, cotizacion.getInfo(), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if(procede){
                            Log.v(TAG,"Si procede...");
                            consultarCotizacion = 1;
                            flagRealizoCotizacionInicial = 1;
                            cotizacionMovil.setRespuesta(cotizacionList);
                            selectTipoVenta = tipoVenta;
                            selectPlanCotizacion = select;
                            if(tipoVenta == 1){
                                requestVenta = 1;
                            }else{
                                requestVenta = 2;
                            }
                            setResponseCotizacion(cotizacionList);
                        }
                        else{
                            Log.v(TAG,"No procede...");
                        }
                    });
                }else{
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> Toast.makeText(CotizacionActivity.this, "Hubo un problema al realizar la cotizacion. Intentelo nuevamente.", Toast.LENGTH_SHORT).show());
                    Log.d(TAG,"Not isSucessful...");
                }
            }
        }).cotizar(cotizacionzacionMovil);
    }

    //region Validate Visita no Used.
    private boolean validateCurrentTimeAfiliado(Date date){
        if(date.compareTo(fechaMaximaAfiliado.getTime())>0){
            Toast.makeText(this, "La fecha seleccionada es mayor a lo permitido.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(date.compareTo(fechaMinimaAfiliado.getTime())>0){
            Toast.makeText(this, "La fecha seleccionada es menor a lo permitido.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void setReprogramarVisitaFisica(){
        Log.d(TAG,"setReprogramarVisitaFisica...");
        Intent intent1 = new Intent(getApplicationContext(),CrearVisitaActivity.class);
        Bundle todo = new Bundle();
        ProspectoVtaDb prospecto = null;
        Log.d(TAG,"Id Prospecto en visita:"+visitaVta.getIdCliente());
        List<ProspectoVtaDb> list = ProspectoVtaDb.getProspectosActualizados(Utils.getDataBase(getApplicationContext()));
        if(list!=null){
            Log.d(TAG,"list!=null: Prospectos Sincronizados:"+list.size());
            for(ProspectoVtaDb obj:list){
                if(visitaVta.getIdCliente()==obj.getIdProspecto()){
                    prospecto = obj;
                    Log.d(TAG,"break:"+prospecto.toString());
                    break;
                }
            }
            todo.putInt("Service",3);
            todo.putLong("Id",prospecto.getID());
            todo.putInt("IdProspecto",prospecto.getIdProspecto());
            todo.putString("Prospecto",prospecto.getNombre());
            todo.putString("Direccion",visitaVta.getIdClienteDireccion());
            todo.putInt("Estado",0);
            todo.putDouble("Latitud",visitaVta.getLatitud());
            todo.putDouble("Longitud",visitaVta.getLongitud());
            todo.putInt("VisitaId",visitaVta.getIdVisita());
            intent1.putExtras(todo);
            startActivityForResult(intent1,REQUEST_VISITA_NUEVA_PAGO_FISICO);
        }
        else{
            Log.d(TAG,"list==null");
        }
        //ntent intent = new Intent(getApplicationContext(),StartActivity.class);
        //SessionManager.getInstance(getApplicationContext()).removeVisitaSession();
        //startActivity(intent);
        //finish();
    }
    /*private void showDialogSolicitud(){
        AlertDialog dialog = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle)
                .setTitle("Rp3MarketForce")
                .setMessage("¿Utilizar Solicitud Fisica de datos?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"onClick si solicitud fisica...");
                        saveResponseCotizacion(visitaVta.getIdVisita(),"F",0);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"onClick No solicitud virtual...");
                        saveResponseCotizacion(visitaVta.getIdVisita(),"V",0);
                    }
                })
                .create();
        dialog.show();
    }*/

    private void validateVisita(){
        //Si tiene dato en VisitaId es decir que proviene de una visita realizada
        String visitaId = String.valueOf(visitaVta.getVisitaId());
        if(visitaId.isEmpty()){

        }
        //Verificar si hay una visita en gestión
        if(visitaVta.getEstado()==2) {
            final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Obteniendo informacion...");
            progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
            progressDialog.setCancelable(false);
            progressDialog.show();
            //Traer la informacion de cotizacion de esa Visita
            new CotizacionVisitaClient(this, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    progressDialog.dismiss();
                    Log.d(TAG,"onFailure...");
                    Log.d(TAG,"IOException:"+e.getMessage());
                    Toast.makeText(CotizacionActivity.this, getResources().getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG,"onResponse...");
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        Log.d(TAG,"isSuccessful...");
                        String json = response.body().string();
                        TypeToken<List<CotizacionMovil>> typeToken = new TypeToken<List<CotizacionMovil>>(){};
                        List<CotizacionMovil> cotizacionMovils = new Gson().fromJson(json,typeToken.getType());
                        if(cotizacionMovils!=null){
                            if(cotizacionMovils.size()>0){
                                List<AfiliadoMovil> respuestaAfiliados = cotizacionMovils.get(0).getAfiliados();
                                List<Cotizacion> respuestaCotizacion = cotizacionMovils.get(0).getRespuesta();
                                cotizacionMovil = new CotizacionMovil();
                                cotizacionMovil.setIdCotizacion(0);
                                cotizacionMovil.setEmail(cotizacionMovils.get(0).getEmail());
                                cotizacionMovil.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));
                                cotizacionMovil.setIdVisita(visitaVta.getIdVisita());
                                cotizacionMovil.setEstado(estado);
                                cotizacionMovil.setAgente(agente);
                                if(cotizacionMovil.getTipoVenta().equalsIgnoreCase("1")){
                                    ((RadioButton)findViewById(R.id.rbIndividual)).setChecked(true);
                                }else{
                                    ((RadioButton)findViewById(R.id.rbIndividual)).setChecked(true);
                                }
                                getDataVisitaFinal();
                            }
                        }
                    }else{
                        Toast.makeText(CotizacionActivity.this, getResources().getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"!isSuccessful...");
                    }
                }
            }).obtener(visitaVta.getIdVisita());
        }
    }

    private void getDataVisitaFinal(){
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Obteniendo información...");
        progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
        progressDialog.setCancelable(false);
        progressDialog.show();
        new SolicitudClient(this, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressDialog.dismiss();
                Log.d(TAG,"onFailure:"+e.getMessage());
                Toast.makeText(CotizacionActivity.this, getResources().getString(R.string.message_error_sync_general_error), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                Log.d(TAG,"onResponse...");
                if(response.isSuccessful()){
                    Log.d(TAG,"isSucessful...");
                    String json = response.body().string();
                    SolicitudClient solicitud = new Gson().fromJson(json,SolicitudClient.class);
                }else{
                    Log.d(TAG,"!isSucessful...");
                }
            }
        }).obtener(visitaVta.getIdVisita());
    }

    private void showBackDialog(){
        AlertDialog builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Rp3Auna")
                .setMessage("¿Desea cancelar la cotizacionMovil y finalizar la visita?")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.d(TAG,"onCancel..");
                        dialog.dismiss();
                    }
                }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"onClick aceptar...");
                        dialog.dismiss();
                        onBackPressed();
                    }
                }).create();
        builder.show();
    }
    //endregion

    //endregion

    //region Pago

    private Commerce commerce = new Commerce();
    private NumberFormat numberFormat = NumberFormat.getInstance();

    private TransactionInformation transactionInformation = new TransactionInformation();
    private ArrayList<Person> personsBilling = new  ArrayList<Person>();
    private ArrayList<Person> personsShipping = new  ArrayList<Person>();
    private ArrayList<Tax> taxes = new  ArrayList<Tax>();
    private ArrayList<Product> products = new  ArrayList<Product>();
    private ArrayList<PurchaseInformation> purchasesInformation = new  ArrayList<PurchaseInformation>();
    private ArrayList<DataReserved> reservedFields = new ArrayList<>();
    private void listener(int operationNumber,String email,String celular,String codWallet) {
        Log.d(TAG,"listener operationNumber:"+operationNumber);
        setValuesToSend(operationNumber,email,celular,codWallet);
        Intent intent = new Intent(getApplicationContext(), LoadingAct.class);
        intent.putExtra(LoadingAct.COMMERCE_PARAM, commerce);
        intent.putExtra(LoadingAct.TRANSACTION_INFORMATION_PARAM, transactionInformation);
        intent.putExtra(LoadingAct.PERSON_BILLING_PARAM, personsBilling);
        intent.putExtra(LoadingAct.PERSON_SHIPPING_PARAM, personsShipping);
        intent.putExtra(LoadingAct.TAXES_PARAM, taxes);
        intent.putExtra(LoadingAct.PRODUCTS_PARAM, products);
        intent.putExtra(LoadingAct.PURCHASES_INFORMATION_PARAM, purchasesInformation);
        intent.putExtra(LoadingAct.RESERV_ID_FIELDS_PARAM, reservedFields);
        Log.d(TAG,"Llamando ALIGNET...");
        startActivityForResult(intent, pe.solera.api_payme_android.util.Constants.RESULT_API);
    }

    private String replaceComa(String cadena){
        return cadena.replace(",",".");
    }

    private void setValuesToSend(int operationNumber,String email,String celular,String codWallet) {
        this.operationNumber = operationNumber;
        NumberFormat numberFormat;
        Locale locale = new Locale("es", "pe");
        numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setMinimumFractionDigits(0);
        String numberOperation = Utils.getNumberOperation(operationNumber);
        this.numeroOperacion = null;
        this.numeroOperacion = numberOperation;
        Log.d(TAG,"Numero de operacion:"+numberOperation);
        Log.d(TAG,"Transaccion:Numero Operacion:"+this.numeroOperacion);
        Log.d(TAG,"Transaccion:Email:"+email);
        Log.d(TAG,"Transaccion:Celular"+celular);
        //List<ApplicationParameter> applicationParameter = ApplicationParameter
          //      .getParameter(Utils.getDataBase(this), Constants.LABEL_COMMERCE);
        commerce.setCommerceName(this.getResources().getString(R.string.appname_marketforce));
        commerce.setCommerceLogo("logo_commerce");
        commerce.setPayButtonIcon("commerce_pay_button");
        commerce.setCommerceColor("1B83B7");

        ApplicationParameter parameterIdAcquirer = ApplicationParameter.getParameter(Utils.getDataBase(this),Constants.PARAMETERID_IDACQUIRER,Constants.LABEL_TRANSACTIONINFORMATION);
        transactionInformation.setIdAcquirer(parameterIdAcquirer.getValue());
        Log.d(TAG,"setIdAcquirer:"+parameterIdAcquirer.getValue());

        ApplicationParameter parameterEntCommerce = ApplicationParameter.getParameter(Utils.getDataBase(this),Constants.PARAMETERID_IDENTCOMMERCE,Constants.LABEL_TRANSACTIONINFORMATION);
        transactionInformation.setIdEntCommerce(parameterEntCommerce.getValue());
        Log.d(TAG,"setIdEntCommerce:"+parameterEntCommerce.getValue());

        Log.d(TAG,"setCodAsoCardHolderWallet:"+codWallet);
        //codAsoCardHolderWallet : No se envia cuando el usuario es nuevo,
        //una vez que el usuario ha realizado su primer pago en la libreria, esta devuelve el codAsoCardHolderWallet
        //transactionInformation.setCodAsoCardHolderWallet(codWallet);
        //transactionInformation.setCodAsoCardHolderWallet("");
        //transactionInformation.setCodAsoCardHolderWallet("4--1286--11505");
        //transactionInformation.setCodAsoCardHolderWallet("");
        ApplicationParameter parameterCodCardHolderCommerce = ApplicationParameter.getParameter(Utils.getDataBase(this),Constants.PARAMETERID_CODCARDHOLDERCOMMERCE,Constants.LABEL_TRANSACTIONINFORMATION);
        Log.d(TAG,"CodCardHolderCommerce:"+parameterCodCardHolderCommerce.getValue());
        transactionInformation.setCodCardHolderCommerce(parameterCodCardHolderCommerce.getValue());

        transactionInformation.setCodAsoCardHolderWallet(codWallet);
        //transactionInformation.setCodCardHolderCommerce("oncosalud001");
        transactionInformation.setMail(email);

        //transactionInformation.setMail(email);
        transactionInformation.setNameCardholder(afiliadoContratante.getNombre());
        Log.d(TAG,"NameCardHolder:"+afiliadoContratante.getNombre());
        Log.d(TAG,"LastNameCardHolder:"+afiliadoContratante.getApellidoPaterno());
        transactionInformation.setLastNameCardholder(afiliadoContratante.getApellidoPaterno());
        personsBilling.clear();

        Person personBilling = new Person();
        personBilling.setFirstName(afiliadoContratante.getNombre());
        personBilling.setLastName(afiliadoContratante.getApellidoPaterno());

        Address addressBilling = new Address();
        addressBilling.setAddress("Los Olivos");
        addressBilling.setCity("Lima");
        addressBilling.setState("Lima");
        addressBilling.setCountryCode("PE");
        addressBilling.setZipCode("Lima 18");
        Log.d(TAG,"PhoneNumber:"+celular);
        addressBilling.setPhoneNumber(celular);
        addressBilling.setEmail(email);

        ArrayList<Address> addressesBilling = new ArrayList<Address>();
        addressesBilling.add(addressBilling);
        personBilling.setAddresses(addressesBilling);

        personsBilling.add(personBilling);
        //

        //
        personsShipping.clear();

        Person personShipping = new Person();
        personShipping.setFirstName(afiliadoContratante.getNombre());
        personShipping.setLastName(afiliadoContratante.getApellidoPaterno());

        Address addressShipping = new Address();
        addressShipping.setAddress("las flores");
        addressShipping.setCity("Lima");
        addressShipping.setState("Lima");
        addressShipping.setCountryCode("PE");
        addressShipping.setZipCode("Lima 18");
        addressShipping.setPhoneNumber(celular);
        addressShipping.setEmail(email);
        //addressShipping.setPhoneNumber(afiliadoContratante.getCelular()!=null?afiliadoContratante.getCelular():afiliadoContratante.getTelefono());
        //addressShipping.setEmail(afiliadoContratante.getCorreo());

        ArrayList<Address> addressesShipping = new ArrayList<Address>();
        addressesShipping.add(addressShipping);
        personShipping.setAddresses(addressesShipping);

        personsShipping.add(personShipping);
        //

        //
        taxes.clear();

        /*Tax tax1 = new Tax();
        tax1.setIdTax("100");
        tax1.setNameTax("Tax1");
        tax1.setAmountTax("10");

        Tax tax2 = new Tax();
        tax2.setIdTax("200");
        tax2.setNameTax("Tax2");
        tax2.setAmountTax("20");

        taxes.add(tax1);
        taxes.add(tax2);*/

        //
        products.clear();

        Product product1 = new Product();
        product1.setItem("001");
        String codeProducto = ((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getCode();
        String nameProducto = ((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getValue();
        Log.d(TAG,"codeProducto:"+codeProducto+" nameProducto:"+nameProducto);
        product1.setCode(codeProducto);
        product1.setName(nameProducto);

        switch (select){
            case 1:
                if(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem())==null){
                    Toast.makeText(this, "Seleccione una fraccion porfavor", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fraccion = ((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1();
                Log.d(TAG,"fraccion:"+fraccion);
                Log.d(TAG,"Valor normal:"+fraccion);
                //String number2 = numberFormat.format(Double.valueOf(fraccion));
                Double number2 = ((Double.parseDouble(((fraccion)))));
                Log.d(TAG,"Select 1 number:"+number2);
                //String numberCommas2 = removeCommas(number2);
                //Log.d(TAG,"Select 1 numberCommas:"+numberCommas2);
                //precio = Double.valueOf((numberFormat.format(number2)));
                //precio = number2Double.valueOf(removeCommas(numberFormat.format(number2)));
                precio = number2;
                break;
            case 2:
                Log.d(TAG,"Valor normal:"+totalTC);
                //String number = numberFormat.format(Double.valueOf(cotizacionMovil.getRespuesta().get(0).getTC()));
                Double number = (totalTC);
                Log.d(TAG,"Select 2 number:"+number);
                //String numberCommas = removeCommas(number);
                //Log.d(TAG,"Select 2 numberCommas:"+numberCommas);
                precio = (number);
                break;
            case 3:
                Log.d(TAG,"Valor normal:"+totalTD);
                //String number1 = numberFormat.format(Double.valueOf(cotizacionMovil.getRespuesta().get(0).getTD()));
                Double number1 =totalTD;
                //Double number1 = (Double.parseDouble(removeCommaXPunto(removePunto(numberFormat.format(totalTD)))));
                Log.d(TAG,"Select 3 number:"+number1);
                //String numberCommas1 = removeCommas(number1);
                //Log.d(TAG,"Select 3 numberCommas:"+numberCommas1);
                precio = (number1);
                break;
        }
        Log.d(TAG,"ProductUnitPrice:"+precio);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        String precioFormat = removeCommas(numberFormat.format(precio));
        Double precioDouble = Double.parseDouble(precioFormat);
        //Log.d(TAG,"precioFormat:"+precioFormat);
        //Log.d(TAG,"precioDobuel:"+precioDouble);
        product1.setUnitPrice(precio);
        product1.setQuantity("1");

        products.add(product1);
        //

        //
        purchasesInformation.clear();

        PurchaseInformation purchaseInformation = new PurchaseInformation();
        purchaseInformation.setCurrencyCode("604");
        String monto = removeCommas(String.valueOf(precioFormat));
        //Log.d(TAG,"monto con comas:"+String.valueOf(precio));
        //Log.d(TAG,"Monto sin comas:"+monto);
        Log.d(TAG,"PurchaseAmount:"+precioFormat);
        //purchaseInformation.setPurchaseAmount(monto+"00");
        purchaseInformation.setPurchaseAmount(precioFormat);
        purchaseInformation.setOperationNumber(this.numeroOperacion);
        purchaseInformation.setCallerPhoneNumber(celular);
        purchaseInformation.setTerminalCode("1113");
        //purchaseInformation.setTerminalCode("414");
        purchaseInformation.setIpAddress("255.255.255.255");

        purchasesInformation.add(purchaseInformation);
        //

        /*DataReserved dr1 = new DataReserved();
        dr1.setIdReserved("01");
        dr1.setValueReserved("DataReserved 01");

        DataReserved dr2 = new DataReserved();
        dr2.setIdReserved("02");
        dr2.setValueReserved("DataReserved 02");
*/
        reservedFields = new ArrayList<>();
        //reservedFields.add(dr1);
        //reservedFields.add(dr2);
    }

    private static String removeCommas(String value) {
        return value.replace(",","").replace(".","");
    }

    private static String removePunto(String value) {
        return value.replace(".","");
    }

    private static String removePuntoComa(String value) {
        return value.replace(".",",");
    }

    private static String removeCommaXPunto(String value){
        return value.replace(",",".");
    }

    private static String removeSoloComas(String value){
        return value.replace(",","");
    }


    //endregion

    //region Reprogramacion o Cancelacion de la Visita

    private void showDialogVisitaReprogrammar(final int tipo){
        Bundle arg = new Bundle();
        arg.putInt("Tipo",tipo);
        arg.putInt("Repro",2);
        final MotivoCitaDialog dialog = MotivoCitaDialog.newInstance(new MotivoCitaDialog.callbackElegir() {
            @Override
            public void onGeneralSelected(String code,String motivo) {
                if(tipo == 1){
                    reprogramarVisita(tipo,code,motivo);
                }else{
                    reprogramarVisita(tipo,code,motivo);
                }
            }
        });
        dialog.setArguments(arg);
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(),"MotivoDialog");
    }

    private void reprogramarVisita(int tipo,String code,String motivo){
        if(tipo==1){
            //Aqui debemos chequear si es Cotizacion Inicial y si ya realizó alguna cotización
            if(estado==1 && flagRealizoCotizacionInicial==1){
                showDialogGrabarCotizaciónInicial(1,code,motivo,1);
            }else{
                showUpdateVisitaClient(1,code,motivo,1);
            }
        }else{
            showUpdateVisitaClient(1,code,motivo,2);
        }
    }

    private void showDialogGrabarCotizaciónInicial(int estado,String code,String motivo,int tipo){
        if(consultarCotizacion==-1){
            Toast.makeText(this, "Debe realizar una cotización...", Toast.LENGTH_SHORT).show();
        }
        if(consultarCotizacion == 1){
            if(cotizacionMovil == null){
                Toast.makeText(this, "Debe realizar una cotizacion para poder enviar un email...", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(consultarCotizacion == 2){
            if(cotizacionVirtual == null){
                Toast.makeText(this, "Debe realizar una cotizacion para poder enviar un email...", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(tipoVenta==-1){
            Toast.makeText(this, "Elija un plan a cotizar...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(tipoVenta==1){
            if(select==-1){
                Toast.makeText(this, "Elija un plan a cotizar...", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        validate();
        if(select==1 && tipoVenta == 1){
            if(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem())==null){
                Toast.makeText(this, "Seleccione una fraccion porfavor", Toast.LENGTH_SHORT).show();
                return;
            }
            if(consultarCotizacion==1){
                cotizacionMovil.setCantidadCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getCode());
                cotizacionMovil.setPrecioCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1());
            }else{
                cotizacionVirtual.setCantidadCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getCode());
                cotizacionVirtual.setPrecioCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1());
            }

        }else{
            if(consultarCotizacion==1){
                cotizacionMovil.setCantidadCuota("0");
                cotizacionMovil.setPrecioCuota("0");
            }else{
                cotizacionVirtual.setCantidadCuota("0");
                cotizacionVirtual.setPrecioCuota("0");
            }

        }
        setTipoCotizacion();
        if(consultarCotizacion==1){
            cotizacionMovil.setPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getValue());
            cotizacionMovil.setTipoVenta(tipoVenta==1?"1":"2");
        }else{
            cotizacionVirtual.setPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getValue());
            cotizacionVirtual.setTipoVenta(tipoVenta==1?"1":"2");
        }

        String movil = null;
        if(consultarCotizacion==1){
            movil = new Gson().toJson(cotizacionMovil);
        }else {
            movil = new Gson().toJson(cotizacionVirtual);
        }
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.appname_marketforce);
        progressDialog.setMessage("Guardando Cotizacion...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new SaveCotizacionClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure...");
                Log.d(TAG,"IOException:"+e.getMessage());
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CotizacionActivity.this, getApplicationContext().getResources().getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Log.d(TAG,"isSuccessful...");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showUpdateVisitaClient(1,code,motivo,1);
                        }
                    });
                }else{
                    Log.d(TAG,"NoisSuccessful...");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CotizacionActivity.this, getApplicationContext().getResources().getString(R.string.message_error_sync_general_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).cotizar(movil);
    }

    //endregion

    private void initSolicitudFisica(){
        //Cargo las marcas de tarjeta segun EQ "Procesadora"
        List<GeneralValue> list = GeneralValue.getGeneralValues(Utils.getDataBase(this),Contants.GENERAL_TABLE_PROCESADORA);
        SimpleGeneralValueAdapter programaAdapter = new SimpleGeneralValueAdapter(this, list);
        ((Spinner) findViewById(R.id.spMarcaTarjeta_payme)).setAdapter(new NothingSelectedSpinnerAdapter(
                programaAdapter,
                R.layout.spinner_empty_selected,
                this, "Seleccionar"));
        //Cargo los tipos de tarjeta segun EQ "Tipos"
        List<GeneralValue> listTipos = GeneralValue.getGeneralValues(Utils.getDataBase(this),Contants.GENERAL_TABLE_TIPOS_TARJETA);
        SimpleGeneralValueAdapter tipoAdapter = new SimpleGeneralValueAdapter(this, listTipos);
        ((Spinner) findViewById(R.id.spTipoTarjeta_payme)).setAdapter(new NothingSelectedSpinnerAdapter(
                tipoAdapter,
                R.layout.spinner_empty_selected,
                this, "Seleccionar"));
        ((Spinner) findViewById(R.id.cotizacion_programa_movil)).setPrompt("Seleccionar");
        rbPaymeSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick rbPaymeSi en linea...");
                selectTipoPago = 1;
                showLayoutTarjeta(true);
                selectTipoPagoFinal = 1;
            }
        });
        rbPaymeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick rbPaymeNo Efectivo...");
                selectTipoPago = 2;
                showLayoutTarjeta(false);
                selectTipoPagoFinal = 2;
            }
        });
        rbRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick rbRegular Regular..");
                selectTipoPago = 3 ;
                showLayoutTarjeta(false);
                selectTipoPagoFinal = 3;
            }
        });
        //region Data no usada
        /*btnValidarTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick btnValidarTarjeta...");
                if(validateFieldTarjeta()){
                    showValidateTarjetaClient();
                }
            }
        });*/
        //endregion
    }

    //region Cotizacion Final: Forma de pago en linea

    private void showDialogContratanteData(){
        Bundle todo = new Bundle();
        if(afiliadoContratante.getCorreo()!=null){
            if(afiliadoContratante.getCorreo().trim().length()>0){
                todo.putString("Email",afiliadoContratante.getCorreo());
            }
        }
        if(afiliadoContratante.getCelular()!=null){
            if(afiliadoContratante.getCelular().trim().length()>0){
                todo.putString("Celular",afiliadoContratante.getCelular());
            }
        }
        if(afiliadoContratante.getCodAsoCardHolderWallet()!=null){
            if(afiliadoContratante.getCodAsoCardHolderWallet().trim().length()>0){
                todo.putString("CodWallet",afiliadoContratante.getCodAsoCardHolderWallet());
            }
        }else{
            if(codAsoCardHolderWallet!=null){
                //todo.putString("CodWallet",codAsoCardHolderWallet);
            }
        }
        EmailContratanteDialog dialog = EmailContratanteDialog.newInstance(new EmailContratanteDialog.callBack() {
            @Override
            public void onSelectedCancelar() {
                Log.d(TAG,"onSelectedCancelar...");

            }

            @Override
            public void onSelected(String email, String celular,String codWallet) {
                Log.d(TAG,"onSelected...");
                correoContratante = email;
                celularContratante = celular;
                codAsoCardHolderWallet = codWallet;
                if(codAsoCardHolderWallet!=null){
                    Log.d(TAG,"codAsoCardHolderWallet!=null...");
                    if(codAsoCardHolderWallet.trim().length()>0){
                        Log.d(TAG,"codAsoCardHolderWallet.trim().length()>0...");
                        showClientWallet(correoContratante,celularContratante,codAsoCardHolderWallet);
                    }else{
                        Log.d(TAG,"codAsoCardHolderWallet.trim().length()==0...");
                        codAsoCardHolderWallet = "";
                        showClientWallet(correoContratante,celularContratante,codAsoCardHolderWallet);
                    }
                }else{
                    Log.d(TAG,"codAsoCardHolderWallet==null......");
                    codAsoCardHolderWallet = "";
                    showClientWallet(correoContratante,celularContratante,codAsoCardHolderWallet);
                }
            }
        },todo);
        dialog.show(getSupportFragmentManager(),"");
    }

    private SolicitudOncosys getSolicitudOncosysSend(final String correo,final String celular){
        SolicitudOncosys solicitudOncosys = new SolicitudOncosys();
        //Seteo los valores para el PaymeCodWallet
        ApplicationParameter parameterCodCardHolderCommerce = ApplicationParameter
                .getParameter(Utils.getDataBase(this),Constants.PARAMETERID_CODCARDHOLDERCOMMERCE,Constants.LABEL_TRANSACTIONINFORMATION);
        ApplicationParameter parameterEntCommerce = ApplicationParameter
                .getParameter(Utils.getDataBase(this),Constants.PARAMETERID_IDENTCOMMERCE,Constants.LABEL_TRANSACTIONINFORMATION);
        transactionInformation.setIdEntCommerce(parameterEntCommerce.getValue());
        PaymeCodWallet paymeCodWallet = new PaymeCodWallet();
        paymeCodWallet.setCodCardHolderCommerce(parameterCodCardHolderCommerce.getValue());
        paymeCodWallet.setIdEntCommerce(parameterEntCommerce.getValue());
        String lastNames = afiliadoContratante.getApellidoPaterno();
        if(afiliadoContratante.getApellidoMaterno()!=null){
            lastNames+=" "+afiliadoContratante.getApellidoMaterno();
        }
        paymeCodWallet.setLastNames(lastNames);
        paymeCodWallet.setMail(correo);
        paymeCodWallet.setNames(afiliadoContratante.getNombre());
        paymeCodWallet.setPrefix("");
        //Ahora seteo los valores para la Solicitud hacia el Oncosys
        solicitudOncosys.setAgente(visitaVta.getIdAgente());
        solicitudOncosys.setApellidoPaterno(afiliadoContratante.getApellidoPaterno());
        solicitudOncosys.setCodigoAsesor(null);
        solicitudOncosys.setCodigoCanal("01");

        if(((CheckBox) findViewById(R.id.checkboxcamapana)).isChecked()){
            solicitudOncosys.setCodigoPrcCampaña("02");
        }else{
            solicitudOncosys.setCodigoPrcCampaña("01");
        }

        solicitudOncosys.setCorreo(correo);
        solicitudOncosys.setCut(0);
        solicitudOncosys.setTitular(afiliadoContratante.getNombre()+" "+lastNames);
        solicitudOncosys.setTipoTarjeta(((GeneralValue)((Spinner) findViewById(R.id.spTipoTarjeta_payme)).getSelectedItem()).getCode());
        solicitudOncosys.setTipoRegistro("2");
        solicitudOncosys.setTipoTarifa("01");
        solicitudOncosys.setTipoDocumentoVenta("01");
        solicitudOncosys.setPaymeCodWallet(paymeCodWallet);
        solicitudOncosys.setNumeroTarjeta(etNumeroTarjeta.getText().toString().trim());
        solicitudOncosys.setNumeroSolicitud(solicitudMovil.getNumeroSolicitud());
        solicitudOncosys.setNombre1(afiliadoContratante.getNombre());
        solicitudOncosys.setMontoIngresado(String.valueOf(precio));
        //
        if(solicitudMovil.getModoTarifa()!=null){
            if(solicitudMovil.getModoTarifa().length()>0){
                GeneralValue generalValue = null;
                List<GeneralValue> list = GeneralValue.getGeneralValues(Utils.getDataBase(this),GENERAL_TABLE_MODO_TARIFA_TIPO_PAGO);
                for (GeneralValue obj:list){
                    if(obj.getCode().equalsIgnoreCase(solicitudMovil.getModoTarifa())){
                        generalValue = obj;
                        break;
                    }
                }
                solicitudOncosys.setModoTarifa(generalValue.getReference2());
            }else{
                solicitudOncosys.setModoTarifa("01");
            }
        }else{
            solicitudOncosys.setModoTarifa("01");
        }

        List<GeneralValue> generalValuesTipo = GeneralValue.getGeneralValues(Utils.getDataBase(this),GENERAL_TABLE_TIPO_DOCUMENTO);
        GeneralValue generalValue = null;
        for (GeneralValue obj:generalValuesTipo){
            if(obj.getCode().equalsIgnoreCase(afiliadoContratante.getTipoDocumentoIdentificacion())){
                generalValue = obj;
                break;
            }
        }
        solicitudOncosys.setTipoDocumentoIdentificacion(generalValue.getReference1());
        solicitudOncosys.setNumeroDocumentoIdentificacion(afiliadoContratante.getNumeroDocumentoIdentificacion());
        solicitudOncosys.setIdVisita(visitaVta.getIdVisita());
        solicitudOncosys.setIdAfiliadoContratante(afiliadoContratante.getIdSolicitudAfiliado());
        if(frecuenciaPagoSelected==1){
            solicitudOncosys.setFrecuenciaPago("01");
        }else{
            solicitudOncosys.setFrecuenciaPago("02");
        }

        solicitudOncosys.setFechaVencimiento(etFechaVencimiento.getText().toString().trim());
        solicitudOncosys.setMoneda("PEN");
        solicitudOncosys.setEntidadProcesadora(((GeneralValue)((Spinner) findViewById(R.id.spMarcaTarjeta_payme)).getSelectedItem()).getCode());
        solicitudOncosys.setCodigoPrograma(((GeneralValue)((Spinner) findViewById(R.id.cotizacion_programa_movil)).getSelectedItem()).getCode());
        return solicitudOncosys;
    }


    private void setMontoIngresado(){
        //montoIngresado
        switch (select){
            case 1:
                if(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem())==null){
                    Toast.makeText(this, "Seleccione una fraccion porfavor", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fraccion = ((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1();
                Log.d(TAG,"fraccion:"+fraccion);
                Log.d(TAG,"Valor normal:"+fraccion);
                //String number2 = numberFormat.format(Double.valueOf(fraccion));
                Double number2 = (Double.parseDouble((fraccion)));
                Log.d(TAG,"Select 1 number:"+number2);
                //String numberCommas2 = removeCommas(number2);
                //Log.d(TAG,"Select 1 numberCommas:"+numberCommas2);
                //precio = Double.valueOf((numberFormat.format(number2)));
                //precio = number2Double.valueOf(removeCommas(numberFormat.format(number2)));
                precio = number2;
                Log.d(TAG,"precio desde fraccion:"+precio);
                break;
            case 2:
                Log.d(TAG,"Valor normal:"+totalTC);
                //String number = numberFormat.format(Double.valueOf(cotizacionMovil.getRespuesta().get(0).getTC()));
                Double number = (totalTC);
                Log.d(TAG,"Select 2 number:"+number);
                //String numberCommas = removeCommas(number);
                //Log.d(TAG,"Select 2 numberCommas:"+numberCommas);
                precio = (number);
                break;
            case 3:
                Log.d(TAG,"Valor normal:"+totalTD);
                //String number1 = numberFormat.format(Double.valueOf(cotizacionMovil.getRespuesta().get(0).getTD()));
                Double number1 = (totalTD);
                Log.d(TAG,"Select 3 number:"+number1);
                //String numberCommas1 = removeCommas(number1);
                //Log.d(TAG,"Select 3 numberCommas:"+numberCommas1);
                precio = (number1);
                break;
        }
    }

    private void showClientWallet(final String correo,final String celular,String codigo){
        try{
            setMontoIngresado();
            final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Espere...");
            progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
            progressDialog.show();
            String json = new Gson().toJson(getSolicitudOncosysSend(correo,celular));
            Log.d(TAG,"Json Client:"+json);
            new ValidarSolicitudOncosysClient(this, new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());
                @Override
                public void onFailure(Call call, final IOException e) {
                    progressDialog.dismiss();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"onFailure...");
                            e.printStackTrace();
                            Toast.makeText(CotizacionActivity.this, R.string.message_error_sync_connection_http_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    progressDialog.dismiss();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG,"onResponse...");
                            try{
                                if(response.isSuccessful()){
                                    Log.d(TAG,"isSucessful...");
                                    try {
                                        String res = response.body().string();
                                        Log.d(TAG,"RES:"+res);

                                        ResponseSolicitudWallet wallet = new Gson().fromJson(res,ResponseSolicitudWallet.class);
                                        if(wallet!=null){
                                            Log.d(TAG,"wallet!=null...");
                                            if(wallet.getFlag()==0){
                                                Log.d(TAG,"Satisfacotorio...");
                                                setResponseListenerPaymeWallet(wallet.getCut(),correo,celular,wallet.getCodAsoCardHolderWallet());
                                            }else if(wallet.getFlag()==1){
                                                List<GeneralValue> list = GeneralValue.getGeneralValues(Utils.getDataBase(CotizacionActivity.this),GENERAL_TABLE_CONTROL_ERRORES_WS_VALIDAR_PAGO);
                                                GeneralValue general=null;
                                                for (GeneralValue obj:list){
                                                    if(wallet.getCodeResponse().contains( obj.getCode()) || wallet.getCodeResponse().equalsIgnoreCase(obj.getCode())){
                                                        general = obj;
                                                        break;
                                                    }
                                                }
                                                Toast.makeText(CotizacionActivity.this, general.getValue(), Toast.LENGTH_SHORT).show();
                                            }else if(wallet.getFlag()==2){
                                                Toast.makeText(CotizacionActivity.this, "Ocurrio un problema al consultar el servicio de Pago. Intentelo nuevamente porfavor.", Toast.LENGTH_SHORT).show();
                                            }else if(wallet.getFlag()==3){
                                                Toast.makeText(CotizacionActivity.this, "Vuelva a consultar el servicio porfavor.", Toast.LENGTH_SHORT).show();
                                            }else if(wallet.getFlag()==4){
                                                Toast.makeText(CotizacionActivity.this, wallet.getResponse(), Toast.LENGTH_SHORT).show();
                                            }else if(wallet.getFlag()==5){
                                                Toast.makeText(CotizacionActivity.this, "Hubo un problema al consultar al servidor. Intentelo nuevamente porfavor.", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(CotizacionActivity.this, R.string.message_error_general, Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Log.d(TAG,"wallet==null...");
                                            Toast.makeText(CotizacionActivity.this, R.string.message_error_general, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Log.d(TAG,"No isSucessful...");
                                    Toast.makeText(CotizacionActivity.this, R.string.message_error_general, Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(CotizacionActivity.this, R.string.message_error_general, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }).validar(json);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, R.string.message_error_general, Toast.LENGTH_SHORT).show();
        }

        //region Anterior WalletClient
        /*new WalletClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CotizacionActivity.this, R.string.message_error_sync_connection_http_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                final String json = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            Log.d(TAG,"response.isSucessfull Wallet...");
                            if(json!=null){
                                Log.d(TAG,"json!=null...");
                                Log.d(TAG,"json:"+json);
                                Wallet wallet = new Gson().fromJson(json,Wallet.class);
                                if(wallet!=null){
                                    Log.d(TAG,"wallet!=null..");
                                    Log.d(TAG,wallet.toString());
                                    if(wallet.getCodAsoCardHolderWallet()!=null){
                                        Log.d(TAG,"wallet.getCodAsoCardHolderWallet()!=null...");
                                        Log.d(TAG,"CodAsoCardHolderWallet:"+wallet.getCodAsoCardHolderWallet());
                                        if(wallet.getCodAsoCardHolderWallet().trim().length()>0){
                                            Log.d(TAG,"wallet.getCodAsoCardHolderWallet().trim().length()>0...");
                                            codAsoCardHolderWallet = wallet.getCodAsoCardHolderWallet();
                                            showValidateTarjetaClient(correoContratante,celularContratante,codAsoCardHolderWallet);
                                        }else{
                                            Log.d(TAG,"wallet.getCodAsoCardHolderWallet().trim().length()==0...");
                                            codAsoCardHolderWallet = "";
                                            showValidateTarjetaClient(correoContratante,celularContratante,codAsoCardHolderWallet);
                                        }
                                    }else{
                                        Log.d(TAG,"wallet.getCodAsoCardHolderWallet()==null...");
                                        codAsoCardHolderWallet = "";
                                        showValidateTarjetaClient(correoContratante,celularContratante,codAsoCardHolderWallet);
                                    }
                                }else{
                                    Log.d(TAG,"wallet==null...");
                                    codAsoCardHolderWallet = "";
                                    showValidateTarjetaClient(correoContratante,celularContratante,codAsoCardHolderWallet);
                                }
                            }else{
                                Log.d(TAG,"json==null...");
                                codAsoCardHolderWallet = "";
                                showValidateTarjetaClient(correoContratante,celularContratante,codAsoCardHolderWallet);
                            }


                        }else{
                            Log.d(TAG,"No is successful...");
                            Toast.makeText(CotizacionActivity.this, R.string.message_error_sync_connection_http_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).obtener(correo);*/
        //endregion
    }

    private void setResponseListenerPaymeWallet(int operationNumberMovil,String email,String celular,String codAsoCardHolderWallet){
        registroPago = new RegistroPago();
        registroPago.setNumeroSolicitud(solicitudMovil.getNumeroSolicitud());
        registroPago.setIdAfiliadoContratante(afiliadoContratante.getIdSolicitudAfiliado());
        registroPago.setEntidadProcesadora(((GeneralValue)((Spinner) findViewById(R.id.spMarcaTarjeta_payme)).getSelectedItem()).getCode());
        registroPago.setNumeroTarjeta(etNumeroTarjeta.getText().toString().trim());
        registroPago.setMoneda("PEN");
        registroPago.setIdVisita(visitaVta.getIdVisita());
        registroPago.setOperationNumber(operationNumberMovil);
        registroPago.setEmailProspecto(email);
        registroPago.setCodAsoCardHolderWallet(codAsoCardHolderWallet);
        listener(operationNumberMovil,email,celular,codAsoCardHolderWallet);
    }

    private void showEmailRegistroPago(final boolean estado){
        Log.d(TAG,"showRegistroPagoClient:Estado Payme:"+estado);
        if(estado){
            registroPago.setEstado(1);
        }else{
            registroPago.setEstado(2);
        }
        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        rp3.auna.models.Agente a = rp3.auna.models.Agente.getAgente(Utils.getDataBase(getApplicationContext()),idAgente);
        Log.d(TAG,"Agente:"+a.toString());
        registroPago.setEmailAgente(a.getEmail());
        registroPago.setIdAgente(idAgente);
        registroPago.setNombreProspecto(afiliadoContratante.getNombre()+" "+afiliadoContratante.getApellidoPaterno());
        registroPago.setMarcaTarjeta(((GeneralValue)((Spinner) findViewById(R.id.spMarcaTarjeta_payme)).getSelectedItem()).getValue());
        showRegistroPagoClient(estado);
    }

    private void showRegistroPagoClient(final boolean estado){
        Log.d(TAG,"RegistroPago:"+registroPago.toString());
        if(estado){
            registroPago.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE,0));
            SessionManager.getInstance(this).createRegistroPago(registroPago);
            showDialogGrabarCotizacion(1);
        }else{
            Toast.makeText(CotizacionActivity.this,"Intente nuevamente la operacion de pago..." , Toast.LENGTH_SHORT).show();
        }
        //region Anterior Forma para el Pago
        /*
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new RegistrarPagoClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"IOException:"+e.getMessage());
                        Toast.makeText(CotizacionActivity.this, getResources().getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                final String json = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"json:"+json);
                        if(response.isSuccessful()){
                            Log.d(TAG,"isSucessful...");
                            if(estado){
                                Toast.makeText(CotizacionActivity.this, getResources().getString(R.string.buying_processed), Toast.LENGTH_SHORT).show();
                                showDialogGrabarCotizacion(1);
                                //saveResponseCotizacion(visitaVta.getIdVisita(),"F",4);
                                //showUpdateVisitaClient(4);
                            }else{
                                Toast.makeText(CotizacionActivity.this,"Intente nuevamente la operacion de pago..." , Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Log.d(TAG,"no isSuccessful...");
                        }
                    }
                });
            }
        }).registrar(registroPago);
        */
        //endregion
    }

    //region Servicios Anteriores de Oncosys
    /*
    private void showValidateTarjetaClient(final String email,final String celular,final String codWallet){
        correoContratante = email;
        TarjetaValidar tarjeta = new TarjetaValidar();
        tarjeta.setCut(Contants.COTIZADOR_TIPO_OPERACION);
        tarjeta.setTipoVenta(tipoVenta==1?Contants.COTIZADOR_CANAL_FUERZA_VENTA_INDIVIDUAL:Contants.COTIZADOR_CANAL_CORPORATIVO_INDIVIDUAL);
        tarjeta.setTarjeta(etNumeroTarjeta.getText().toString().trim());
        tarjeta.setTipoTarjeta(((GeneralValue)((Spinner) findViewById(R.id.spMarcaTarjeta_payme)).getSelectedItem()).getCode());
        tarjeta.setUsuario(Contants.COTIZADOR_USER);
        String json = new Gson().toJson(tarjeta);
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Validando tarjeta...");
        progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
        progressDialog.show();
        new ValidarTarjetaClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"IOException:"+e.getMessage());
                        Toast.makeText(CotizacionActivity.this, getApplicationContext().getResources().getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            try {
                                String json = response.body().string();
                                Log.d(TAG,"response.isSuccessful()...");
                                RespuestaValidar respuestaValidar = new Gson().fromJson(json,RespuestaValidar.class);
                                if(respuestaValidar.getInfo().trim().isEmpty()){
                                    Log.d(TAG,"Info == null...");
                                    validatePayme = true;
                                    Toast.makeText(CotizacionActivity.this, "La Tarjeta es valida", Toast.LENGTH_SHORT).show();
                                    obtenerOperationNumberClient(email,celular);
                                }else{
                                    Log.d(TAG,"Info != null...");
                                    Toast.makeText(CotizacionActivity.this, "Tarjeta no valida", Toast.LENGTH_SHORT).show();
                                    etNumeroTarjeta.setError("Bin de tarjeta no valida...");
                                    validatePayme = false;
                                }
                            } catch (IOException e) {
                                Log.d(TAG,"IOException:"+e.getMessage());
                                e.printStackTrace();
                            }

                        }else{
                            Log.d(TAG,"!response.isSuccessful()...");
                            Toast.makeText(CotizacionActivity.this, getApplicationContext().getResources().getString(R.string.message_error_sync_connection_http_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).validar(json);
    }

    private void obtenerOperationNumberClient(final String email,final String celular){
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
        progressDialog.setMessage("Validando Operación...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new OperationNumberClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"IOException:"+e.getMessage());
                        Toast.makeText(CotizacionActivity.this, getApplicationContext().getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            Log.d(TAG,"isSuccessful...");
                            try {
                                String json = response.body().string();
                                OperationNumberMovil operationNumberMovil = new Gson().fromJson(json,OperationNumberMovil.class);
                                Log.d(TAG,"OperationNumberMovil:"+json);
                                registroPago = new RegistroPago();
                                registroPago.setIdAfiliadoContratante(afiliadoContratante.getIdSolicitudAfiliado());
                                registroPago.setEntidadProcesadora(((GeneralValue)((Spinner) findViewById(R.id.spMarcaTarjeta_payme)).getSelectedItem()).getCode());
                                registroPago.setNumeroTarjeta(etNumeroTarjeta.getText().toString().trim());
                                registroPago.setMoneda("PEN");
                                registroPago.setIdVisita(visitaVta.getIdVisita());
                                registroPago.setOperationNumber(operationNumberMovil.getOperationNumber());
                                registroPago.setEmailProspecto(email);
                                registroPago.setCodAsoCardHolderWallet(codAsoCardHolderWallet);
                                listener(operationNumberMovil.getOperationNumber(),email,celular,codAsoCardHolderWallet);
                            } catch (IOException e) {
                                Log.d(TAG,"IOException:"+e.getMessage());
                                e.printStackTrace();
                            }
                        }else{
                            Log.d(TAG,"No is Sucessful...");
                            Toast.makeText(CotizacionActivity.this, getApplicationContext().getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).obtener(visitaVta.getIdVisita());
    }
    */
    //endregion

    private boolean validateFieldTarjeta(){
        if(((GeneralValue)((Spinner) findViewById(R.id.spTipoTarjeta_payme)).getSelectedItem())==null){
            Toast.makeText(this, "Seleccione un Tipo de Tarjeta porfavor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(((GeneralValue)((Spinner) findViewById(R.id.spMarcaTarjeta_payme)).getSelectedItem())==null){
            Toast.makeText(this, "Seleccione una Marca de Tarjeta porfavor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(etNumeroTarjeta.getText())){
            Toast.makeText(this, "Digite el Numero bin de la tarjeta", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(etFechaVencimiento.getText())){
            Toast.makeText(this, "Digite la fecha de vencimiento de la tarjeta", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etNumeroTarjeta.getText().toString().trim().length()<6 || etNumeroTarjeta.getText().toString().trim().length()>6){
            Toast.makeText(this, "Digite una longitud valida del numero bin de tarjeta porfavor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(etFechaVencimiento.getText().toString().trim().length()<7 || etNumeroTarjeta.getText().toString().trim().length()>7){
            Toast.makeText(this, "Digite la fecha de vencimiento de la tarjeta correctamente porfavor", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(frecuenciaPagoSelected == -1){
            Toast.makeText(this, "Seleccione una frecuencia de pago porfavor", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showLayoutTarjeta(boolean state){
        if(state){
            lyMarcaTarjeta.setVisibility(View.VISIBLE);
            lyNumeroTarjeta.setVisibility(View.VISIBLE);
            lyTipoTarjeta.setVisibility(View.VISIBLE);
            lyFrecuenciaPago.setVisibility(View.VISIBLE);
            lyFechaTarjeta.setVisibility(View.VISIBLE);
            tvDatosTarjeta.setVisibility(View.VISIBLE);
            /*if(frecuenciaPagoSelected!=-1){
                rbFrecuenciaAnual.setEnabled(false);
                rbFrecuenciaMensual.setEnabled(false);
                rbFrecuenciaAnual.setChecked(false);
                rbFrecuenciaMensual.setChecked(false);
            }*/
            //rbFrecuenciaAnual.setEnabled(false);
            //rbFrecuenciaMensual.setEnabled(false);
            //rbFrecuenciaAnual.setChecked(false);
            //rbFrecuenciaMensual.setChecked(false);
            //frecuenciaPagoSelected = -1;
            rbFrecuenciaAnual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"rbFrecuenciaAnual...");
                    frecuenciaPagoSelected = 1;
                }
            });
            rbFrecuenciaMensual.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"rbFrecuenciaMensual...");
                    frecuenciaPagoSelected = 2;
                }
            });
            //btnValidarTarjeta.setVisibility(View.VISIBLE);
        }else{
            //rbFrecuenciaAnual.setEnabled(false);
            //rbFrecuenciaMensual.setEnabled(false);
            //rbFrecuenciaAnual.setChecked(false);
            //rbFrecuenciaMensual.setChecked(false);
            //frecuenciaPagoSelected = -1;
            /*if(frecuenciaPagoSelected!=-1){
                rbFrecuenciaAnual.setEnabled(false);
                rbFrecuenciaMensual.setEnabled(false);
                rbFrecuenciaAnual.setChecked(false);
                rbFrecuenciaMensual.setChecked(false);
            }*/
            lyMarcaTarjeta.setVisibility(View.GONE);
            lyNumeroTarjeta.setVisibility(View.GONE);
            lyTipoTarjeta.setVisibility(View.GONE);
            lyFrecuenciaPago.setVisibility(View.GONE);
            lyFechaTarjeta.setVisibility(View.GONE);
            tvDatosTarjeta.setVisibility(View.GONE);
            //btnValidarTarjeta.setVisibility(View.GONE);
        }
    }

    //endregion

    //region Cotizacion Final: Forma de pago Regular

    private void showEmailAfiliacionProceso(final int formaPago){
        Log.d(TAG,"showEmailAfiliacionProceso:"+formaPago);
        final int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        rp3.auna.models.Agente a = rp3.auna.models.Agente.getAgente(Utils.getDataBase(getApplicationContext()),idAgente);
        Log.d(TAG,"Agente:"+a.toString());
        final VentaRegularData model = new VentaRegularData();
        model.setIdAgente(idAgente);
        model.setEmailAgente(a.getEmail());
        model.setNombreProspecto(afiliadoContratante.getNombre()+" "+afiliadoContratante.getApellidoPaterno());
        switch (selectPlanCotizacion){
            case 1:
                model.setMontoTotal(TotalAnual.getText().toString());
                break;
            case 2:model.setMontoTotal(TotalTC.getText().toString());
                break;
            case 3:model.setMontoTotal(TotalTD.getText().toString());
                break;
            default:model.setMontoTotal("0.00");
        }
        model.setNumeroSolicitud(solicitudMovil.getNumeroSolicitud());
        //Verificar que forma de pago es para ver si se pide otra vez el email
        if(formaPago == 1){
            Log.d(TAG,"En Linea...");
            model.setEmailProspecto(correoContratante);
            //showEmailAfiliacionProcesoClient(new Gson().toJson(model),formaPago);
            SessionManager.getInstance(CotizacionActivity.this).createVentaRegular(model);
            Log.d(TAG,"Venta En Línea Data:"+model.toString());
            showEmailAfiliacionProcesoClient(new Gson().toJson(model),formaPago);
        }else if(formaPago == 2){
            Log.d(TAG,"En Efectivo...");
            model.setEmailProspecto(correoContratante);
            SessionManager.getInstance(CotizacionActivity.this).createVentaRegular(model);
            Log.d(TAG,"Venta Efectivo Data:"+model.toString());
            showEmailAfiliacionProcesoClient(new Gson().toJson(model),formaPago);
        }else if(formaPago == 3){
            Log.d(TAG,"Regular...");
            EmailCotizacionDialog dialog = EmailCotizacionDialog.newInstance(new EmailCotizacionDialog.callbackEmail() {
                @Override
                public void onSelectedCancelar() {
                    Log.d(TAG,"onSelectedCancelar...");
                }

                @Override
                public void onEmailEnviar(String email) {
                    Log.d(TAG,"onEmailEnviar...:"+email);
                    model.setEmailProspecto(email);
                    SessionManager.getInstance(CotizacionActivity.this).createVentaRegular(model);
                    Log.d(TAG,"Venta Regular Data:"+model.toString());
                    showDialogGrabarCotizacion(3);
                    //showEmailAfiliacionProcesoClient(new Gson().toJson(model),formaPago);
                }
            },afiliadoContratante.getCorreo(),1);
            Bundle todo = new Bundle();
            todo.putInt("Pago",0);
            dialog.setArguments(todo);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(),"");
        }

    }

    private void showEmailAfiliacionProcesoClient(String json, final int formaPago){
        Log.d(TAG,"showEmailAfiliacionProcesoClient:"+json);
        switch (formaPago){
            case 1:
                Log.d(TAG,"forma de pago en linea..,");
                //Estado de Visita = Pago en linea
                setDataVisitaFinalizada(4);
                showUpdateVisitaClient(4,null,null,0);
                break;
            case 2:
                Log.d(TAG,"forma de pago efectivo...");
                //Estado de Visita = Pago en efectivo
                setDataVisitaFinalizada(3);
                showUpdateVisitaClient(3,null,null,0);
                break;
            case 3:
                Log.d(TAG,"forma de pago regular...");
                //Estado de Visita = Pago Regular
                setDataVisitaFinalizada(7);
                showUpdateVisitaClient(7,null,null,0);
                break;
        }
        //region Anterior formar de enviar el Email
        /*final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new EmailAfiliacionProcesoClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, final IOException e) {
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"IOExcetion:"+e.getMessage());
                        Toast.makeText(CotizacionActivity.this, getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                String res = response.body().string();
                Log.d(TAG,"res:"+res);
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            Log.d(TAG,"isSuccessful...");
                            if(REQUEST_STATE == REQUEST_VISITA_COTIZACION_NUEVO){
                                switch (formaPago){
                                    case 1:
                                        Log.d(TAG,"forma de pago en linea..,");
                                        //Estado de Visita = Pago en linea
                                        setDataVisitaFinalizada(4);
                                        showUpdateVisitaClient(4,null,null,0);
                                        break;
                                    case 2:
                                        Log.d(TAG,"forma de pago efectivo...");
                                        //Estado de Visita = Pago en efectivo
                                        setDataVisitaFinalizada(3);
                                        showUpdateVisitaClient(3,null,null,0);
                                        break;
                                    case 3:
                                        Log.d(TAG,"forma de pago regular...");
                                        //Estado de Visita = Pago Regular
                                        setDataVisitaFinalizada(7);
                                        showUpdateVisitaClient(7,null,null,0);
                                        break;
                                }


                            }else{
                                Log.d(TAG,"Determinar visita...");
                            }
                        }else{
                            Log.d(TAG,"!isSuccessful..");
                            Toast.makeText(CotizacionActivity.this, getString(R.string.message_error_sync_connection_http_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).enviar(json);
        */
        //endregion
    }

    //endregion

    //region Cotizacion Final: Forma de pago Efectivo

    private void showEmailInstrucciones(final int estado){
        EmailCotizacionDialog dialog = EmailCotizacionDialog.newInstance(new EmailCotizacionDialog.callbackEmail() {
            @Override
            public void onSelectedCancelar() {
                Log.d(TAG,"onSelectedCancelar...");
            }

            @Override
            public void onEmailEnviar(String email) {
                Log.d(TAG,"onEmailEnviar...");
                int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
                rp3.auna.models.Agente a = rp3.auna.models.Agente.getAgente(Utils.getDataBase(getApplicationContext()),idAgente);
                Log.d(TAG,"Agente:"+a.toString());
                VentaFisicaData ventaFisicaData = new VentaFisicaData();
                ventaFisicaData.setIdAgente(idAgente);
                ventaFisicaData.setEmailAgente(a.getEmail());
                ventaFisicaData.setEmailProspecto(email);
                correoContratante = email;
                ventaFisicaData.setNombreProspecto(afiliadoContratante.getNombre()+" "+afiliadoContratante.getApellidoPaterno());
                switch (selectPlanCotizacion){
                    case 1:
                        ventaFisicaData.setMontoTotal(TotalAnual.getText().toString());
                        break;
                    case 2:ventaFisicaData.setMontoTotal(TotalTC.getText().toString());
                        break;
                    case 3:ventaFisicaData.setMontoTotal(TotalTD.getText().toString());
                        break;
                    default:ventaFisicaData.setMontoTotal("0.00");
                }
                ventaFisicaData.setNumeroSolicitud(solicitudMovil.getNumeroSolicitud());
                SessionManager.getInstance(CotizacionActivity.this).createVentaFisica(ventaFisicaData);
                showEmailInstruccionesPagoClient(new Gson().toJson(ventaFisicaData),estado);
            }
        },afiliadoContratante.getCorreo(),1);
        Bundle todo = new Bundle();
        todo.putInt("Pago",0);
        dialog.setArguments(todo);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),"");
    }

    private void showEmailInstruccionesPagoClient(String json,final int formaPago){
        Log.d(TAG,"showEmailInstruccionesPagoClient:"+json);
        showEmailAfiliacionProceso(formaPago);
        //region Anterior forma de Venta Física
        /*final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new EmailInstruccionesPagoClient(this, new Callback() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"IOExcetion:"+e.getMessage());
                        Toast.makeText(CotizacionActivity.this, getString(R.string.message_error_sync_connection_server_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                String res = response.body().string();
                Log.d(TAG,"res:"+res);
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()){
                            Log.d(TAG,"isSuccessful...");
                            showEmailAfiliacionProceso(formaPago);
                        }else{
                            Log.d(TAG,"!isSuccessful..");
                            Toast.makeText(CotizacionActivity.this, getString(R.string.message_error_sync_connection_http_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).enviar(json);*/
        //endregion
    }

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(estado==1){
            optionsItemMenuInicial(id);
        }else if(estado==2){
            optionsItemMenuFinal(id);
        }
        return super.onOptionsItemSelected(item);
    }

    private void optionsItemMenuInicial(int id){
        if(id==R.id.action_save_cotizacion){
            Log.d(TAG,"Grabar cotizacionMovil...");
            showDialogGrabarCotizacion(0);
        }
        else if(id==R.id.action_email_cotizacion){
            Log.d(TAG,"Enviar email...");
            showDialogEnvioEmail();
            //listener();
        }
        else if(id==R.id.action_consultarid_cotizacion){
            Log.d(TAG,"Consultar cotizacionMovil Inicial...");
            getDataSelected();
        }
        else if(id==R.id.action_cancelar_visita){
            Log.d(TAG,"Cancelar Visita cotizacionMovil...");
            showDialogVisitaReprogrammar(2);
        }

        else if(id==R.id.action_agendar_visita){
            Log.d(TAG,"Agenda visita cotizacionMovil...");
            showDialogVisitaReprogrammar(1);
        }
    }

    private void optionsItemMenuFinal(int id){
        if(id==R.id.action_consultarid_cotizacion){
            Log.d(TAG,"Consultar cotizacionMovil Final...");
            getDataSelected();
        }
        else if(id==R.id.action_save_cotizacion){
            Log.d(TAG,"GuardarPayme ...");
            //region Validaciones del grabado
            if(ResponseView.getVisibility() == View.GONE){
                Toast.makeText(this, "Debe generar una cotizacion porfavor.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(selectTipoPagoFinal==-1){
                Toast.makeText(this, "Seleccione una forma de pago porfavor.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(selectPlanCotizacion==-1){
                Toast.makeText(this, "Seleccione un plan de cotización.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(tipoVenta == 1){
                if(selectPlanCotizacion == 1){
                    if(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem())==null){
                        Toast.makeText(this, "Seleccione una fraccion porfavor", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cotizacionMovil.setCantidadCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getCode());
                    cotizacionMovil.setPrecioCuota(((GeneralValue)((Spinner) findViewById(R.id.spFraccionamiento)).getSelectedItem()).getReference1());
                }

            }else{
                cotizacionMovil.setCantidadCuota("0");
                cotizacionMovil.setPrecioCuota("0");
            }
            //endregion

            if(selectTipoPagoFinal==1){
                Log.d(TAG,"Tipo de Pago es por pasarela de pago...");
                Log.d(TAG,"Ahora se solicito que el bin de la tarjeta a validar sea en el mismo servicio..");
                //Tipo Pago: 1=Alignet 2=Efectivo 3=Regular
                // *Anotación: A Criterio primero se grabará la visita y luego la data
                visitaVta.setTipoVenta("1");
                if(validateFieldTarjeta()){
                    //Aquí solo hago la llamada a Alignet
                    showDialogContratanteData();
                }
            }else if(selectTipoPagoFinal == 2){
                Log.d(TAG,"El tipo de pago es en Efectivo, enviarle Instrucciones de correo...");
                visitaVta.setTipoVenta("2");
                showDialogGrabarCotizacion(2);
            }
            else{
                Log.d(TAG,"Tipo de Pago es regular...");
                visitaVta.setTipoVenta("3");
                showEmailAfiliacionProceso(3);
            }
        }

        //region Opciones de Gestion
        else if(id==R.id.action_cancelar_visita){
            Log.d(TAG,"Cancelar Visita cotizacionMovil...");
            showDialogVisitaReprogrammar(2);
        }

        else if(id==R.id.action_agendar_visita){
            Log.d(TAG,"Agenda visita cotizacionMovil...");
            showDialogVisitaReprogrammar(1);
        }
        else if(id==android.R.id.home){
            Log.d(TAG,"Click en boton retroceder...ir a la solicitud...");
            setDataSolicitudBack(visitaVta.getIdVisita(),"F");
        }
        //endregion
    }

    private void setDataVisitaFinalizada(final int estado){
        visitaVta.setEstado(estado);
        visitaVta.setFechaFin(Calendar.getInstance().getTime());
        visitaVta.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA);
    }

    private void showUpdateVisitaClient(final int estado, String code, String motivo, final int tipo){
        Log.d(TAG,"showUpdateVisitaClient estado:"+estado);
        if(!Util.isNetworkAvailable(this)){
            Toast.makeText(this, R.string.message_error_sync_no_net_available, Toast.LENGTH_SHORT).show();
            return;
        }else{
            visitaVta.setEstado(estado);
            visitaVta.setFechaFin(Calendar.getInstance().getTime());
            visitaVta.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA);

            //Iniciar las visitas para enviar
            rp3.auna.bean.VisitaVta obj = new rp3.auna.bean.VisitaVta();
            obj.setEstado(visitaVta.getEstado());
            obj.setLlamadaId(0);
            if(String.valueOf(visitaVta.getVisitaId())!=null){
                obj.setVisitaId(visitaVta.getVisitaId());
            }else{
                obj.setVisitaId(0);
            }
            obj.setIdAgente(visitaVta.getIdAgente());
            obj.setIdCliente(visitaVta.getIdCliente());
            obj.setDuracionCode(visitaVta.getDuracionCode());
            obj.setFechaFin(Convert.getDotNetTicksFromDate(visitaVta.getFechaFin()));
            obj.setFechaInicio(Convert.getDotNetTicksFromDate(visitaVta.getFechaInicio()));
            obj.setFechaVisita(Convert.getDotNetTicksFromDate(visitaVta.getFechaVisita()));
            obj.setDescripcion(visitaVta.getDescripcion());
            obj.setFotos(null);
            obj.setReferidoCount(0);
            obj.setReferidoTabla(visitaVta.getReferidoTabla());
            obj.setLatitud(Float.parseFloat(String.valueOf(visitaVta.getLatitud())));
            obj.setLongitud(Float.parseFloat(String.valueOf(visitaVta.getLongitud())));
            obj.setIdClienteDireccion(visitaVta.getIdClienteDireccion());
            obj.setIdVisita(visitaVta.getIdVisita());
            if(code!=null && tipo == 1){
                visitaVta.setObservacion(motivo);
                visitaVta.setMotivoReprogramacionValue(code);
                visitaVta.setMotivoReprogramacionTabla(GENERAL_TABLE_MOTIVOS_REPROGRAMACION_TABLE_ID_CITA);
            }else if(code !=null && tipo ==2){
                visitaVta.setObservacion(motivo);
                visitaVta.setMotivoReprogramacionValue(code);
                visitaVta.setMotivoReprogramacionTabla(Contants.GENERAL_TABLE_MOTIVOS_CANCELAR_CITA_TABLE_ID);
            }
            obj.setMotivoReprogramacionTabla(visitaVta.getMotivoReprogramacionTabla());
            obj.setMotivoReprogramacionValue(visitaVta.getMotivoReprogramacionValue());
            obj.setMotivoVisitaTabla(visitaVta.getMotivoVisitaTabla());
            obj.setMotivoVisitaValue(visitaVta.getMotivoVisitaValue());
            obj.setObservacion(visitaVta.getObservacion());
            obj.setTipoVenta(visitaVta.getTipoVenta());
            obj.setVisitaTabla(visitaVta.getVisitaTabla());
            obj.setVisitaValue(visitaVta.getVisitaValue());
            obj.setTiempoCode(visitaVta.getTiempoCode());
            obj.setReferidoValue(visitaVta.getReferidoValue());
            visitaVta.setInsertado(2);
            List<rp3.auna.bean.VisitaVta> list = new ArrayList<>();
            list.add(obj);
            //
            String json = new Gson().toJson(list);
            final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(this.getResources().getString(R.string.appname_marketforce));
            progressDialog.setMessage("Finalizando Visita...");
            progressDialog.show();
            if(tipo>0){
                //region Actualizar Visita
                new UpdateVisitaClient(this, new Callback() {
                    Handler handler = new Handler(Looper.getMainLooper());
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG,"onFailure...");
                        progressDialog.dismiss();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CotizacionActivity.this, getResources().getString(R.string.message_error_sync_general_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        Log.d(TAG,"onResponse...");
                        progressDialog.dismiss();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(response.isSuccessful()){
                                    Log.d(TAG,"isSucessful...");
                                    boolean result = VisitaVta.update(Utils.getDataBase(getApplicationContext()),visitaVta);
                                    if(result){
                                        Log.d(TAG,"Se actualizo visita...");
                                    }else{
                                        Log.d(TAG,"No se actualizo visita...");
                                    }
                                    //region Visita Gestión
                                    //Reprogramar Visita
                                    if(tipo==1){
                                        Log.d(TAG,"reprograma visita...");
                                        Intent intent1 = new Intent(getApplicationContext(),CrearVisitaActivity.class);
                                        Bundle todo = new Bundle();
                                        ProspectoVtaDb prospecto = null;
                                        Log.d(TAG,"Id Prospecto en visita:"+visitaVta.getIdCliente());
                                        List<ProspectoVtaDb> list = ProspectoVtaDb.getProspectosActualizados(Utils.getDataBase(getApplicationContext()));
                                        if(list!=null){
                                            Log.d(TAG,"list!=null: Prospectos Sincronizados:"+list.size());
                                            for(ProspectoVtaDb obj:list){
                                                if(visitaVta.getIdCliente()==obj.getIdProspecto()){
                                                    prospecto = obj;
                                                    Log.d(TAG,"break:"+prospecto.toString());
                                                    break;
                                                }
                                            }
                                            todo.putInt("Service",6);
                                            todo.putLong("Id",prospecto.getID());
                                            todo.putInt("IdProspecto",prospecto.getIdProspecto());
                                            todo.putString("Prospecto",prospecto.getNombre());
                                            todo.putString("Direccion",visitaVta.getIdClienteDireccion());
                                            todo.putInt("Estado",0);
                                            todo.putDouble("Latitud",visitaVta.getLatitud());
                                            todo.putDouble("Longitud",visitaVta.getLongitud());
                                            todo.putInt("VisitaId",visitaVta.getIdVisita());
                                            intent1.putExtras(todo);
                                            startActivityForResult(intent1,REQUEST_VISITA_REPROGRAMADA_FINALIZADA);
                                            //CotizacionActivity.this.startActivityForResult(intent1,REQUEST_VISITA_REPROGRAMADA_FINALIZADA);
                                        }
                                        else{
                                            Log.d(TAG,"list==null");
                                        }
                                        //Cancelar Visita
                                    }else if(tipo==2){
                                        Log.d(TAG,"cancelar visita...");
                                        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                                        //startActivity(intent);
                                        SessionManager.getInstance(getApplicationContext()).removeVisitaSession();
                                        setResult(RESULT_VISITA_REPROGRAMM_CANCEL);
                                        finish();
                                        //finish();
                                        Toast.makeText(CotizacionActivity.this, "Visita cancelada satisfactoriamente",Toast.LENGTH_SHORT).show();
                                    }

                                    //endregion

                                    /**
                                     * Pedir referidos
                                     */

                                    //region Finalizar En Línea = 1
                                    //Finalizo se realizo por forma de pago En linea pasarela de pago
                                    else if(estado==4){
                                        Log.d(TAG,"estado==4...");
                                        if(REQUEST_STATE == REQUEST_VISITA_COTIZACION_NUEVO){
                                            ProspectoVtaDb prospectoVtaDb = ProspectoVtaDb.getProspectoId(Utils.getDataBase(getApplicationContext()),visitaVta.getIdCliente());
                                            prospectoVtaDb.setEstadoCode(Contants.GENERAL_VALUE_CODE_VENTA_PROSPECTO);
                                            prospectoVtaDb.setEstado(2);
                                            boolean estado = ProspectoVtaDb.update(Utils.getDataBase(getApplicationContext()),prospectoVtaDb);
                                            Log.d(TAG,(estado)?"Se actualizo prospecto":"No se actualizo prospecto");
                                            setResult(RESULT_VISITA_ONLINE_REFERIDO);
                                            finish();
                                        }
                                        Toast.makeText(CotizacionActivity.this, "Visita finalizada!",Toast.LENGTH_SHORT).show();
                                    }
                                    //endregion

                                    //region Finalizar Efectivo = 2
                                    //Finalizo se realizo por forma de pago: Efectivo
                                    else if(estado==3){
                                        Log.d(TAG,"estado==3...");
                                        if(REQUEST_STATE == REQUEST_VISITA_COTIZACION_NUEVO){
                                            Log.d(TAG,"REQUEST_STATE == REQUEST_VISITA_COTIZACION_NUEVO...");
                                            //Ocultar este reprogramar Visita queda pendiente Okas
                                            // /setReprogramarVisitaFisica();
                                            ProspectoVtaDb prospectoVtaDb = ProspectoVtaDb.getProspectoId(Utils.getDataBase(getApplicationContext()),visitaVta.getIdCliente());
                                            prospectoVtaDb.setEstado(2);
                                            prospectoVtaDb.setEstadoCode(Contants.GENERAL_VALUE_CODE_VENTA_PROSPECTO);
                                            boolean estado = ProspectoVtaDb.update(Utils.getDataBase(getApplicationContext()),prospectoVtaDb);
                                            Log.d(TAG,(estado)?"Se actualizo prospecto":"No se actualizo prospecto");
                                            setResult(RESULT_VISITA_REGULAR_REFERIDO);
                                            finish();
                                            //setResult(RESULT_VISITA_EFECTIVO_REFERIDO);
                                            //finish();
                                        }
                                        Toast.makeText(CotizacionActivity.this, "Visita finalizada!",Toast.LENGTH_SHORT).show();
                                    }
                                    //endregion

                                    //region Finalizar Regular = 3
                                    //Finalizo se realizo visita por forma de pago Regular: solo
                                    else if(estado==7){
                                        Log.d(TAG,"estado==7...");
                                        if(REQUEST_STATE == REQUEST_VISITA_COTIZACION_NUEVO){
                                            ProspectoVtaDb prospectoVtaDb = ProspectoVtaDb.getProspectoId(Utils.getDataBase(getApplicationContext()),visitaVta.getIdCliente());
                                            prospectoVtaDb.setEstado(2);
                                            prospectoVtaDb.setEstadoCode(Contants.GENERAL_VALUE_CODE_VENTA_PROSPECTO);
                                            boolean estado = ProspectoVtaDb.update(Utils.getDataBase(getApplicationContext()),prospectoVtaDb);
                                            Log.d(TAG,(estado)?"Se actualizo prospecto":"No se actualizo prospecto");
                                            setResult(RESULT_VISITA_REGULAR_REFERIDO);
                                            finish();
                                        }
                                        Toast.makeText(CotizacionActivity.this, "Visita finalizada!",Toast.LENGTH_SHORT).show();
                                    }
                                    //endregion

                                    Log.d(TAG,"estado = "+estado);
                                }else{
                                    Log.d(TAG,"!!isSucessful...");
                                    Toast.makeText(CotizacionActivity.this, "Hubo un problema al finalizar la visita...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).actualizar(json);
                //endregion
            }else{
                VisitaVtaFinal venta = new VisitaVtaFinal();
                obj.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA);
                visitaVta.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA);
                visitaVta.setInsertado(2);
                venta.setIdSolicitud(solicitudMovil.getIdSolicitud());
                venta.setVisita(obj);
                if(estado==4){
                    venta.setRegistroPago(SessionManager.getInstance(this).getRegistroPago());
                    venta.setVentaFisicaData(null);
                    venta.setVentaRegularData(SessionManager.getInstance(this).getVentaRegular());
                }else if(estado==3){
                    venta.setRegistroPago(null);
                    venta.setVentaFisicaData(SessionManager.getInstance(this).getVentaFisica());
                    venta.setVentaRegularData(SessionManager.getInstance(this).getVentaRegular());
                }else if(estado==7){
                    venta.setRegistroPago(null);
                    venta.setVentaFisicaData(null);
                    venta.setVentaRegularData(SessionManager.getInstance(this).getVentaRegular());
                }
                String jsonFinal = new Gson().toJson(venta);
                Log.d(TAG,"VENTAFINAL:"+jsonFinal);
                new FinalizarVentaClient(this, new Callback() {
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        progressDialog.dismiss();
                        Log.d(TAG,"onFailure...");
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CotizacionActivity.this, R.string.message_error_sync_connection_server_fail, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        Log.d(TAG,"onResponse...");
                        progressDialog.dismiss();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String json = response.body().string();
                                    Log.d(TAG,"jsonResponse: "+json);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(response.isSuccessful()){
                                    Log.d(TAG,"isSuccessful...");
                                    ProspectoVtaDb prospectoVtaDb = ProspectoVtaDb.getProspectoId(Utils.getDataBase(getApplicationContext()),visitaVta.getIdCliente());
                                    prospectoVtaDb.setEstadoCode(Contants.GENERAL_VALUE_CODE_VENTA_PROSPECTO);
                                    prospectoVtaDb.setEstado(2);
                                    boolean est = ProspectoVtaDb.update(Utils.getDataBase(getApplicationContext()),prospectoVtaDb);
                                    Log.d(TAG,(est)?"Se actualizo prospecto":"No se actualizo prospecto");
                                    SessionManager.getInstance(CotizacionActivity.this).removeRegistroPago();
                                    SessionManager.getInstance(CotizacionActivity.this).removeVentaFisica();
                                    SessionManager.getInstance(CotizacionActivity.this).removeVentaRegular();
                                    Toast.makeText(CotizacionActivity.this, "Venta finalizada!",Toast.LENGTH_SHORT).show();
                                    VisitaVta.update(Utils.getDataBase(CotizacionActivity.this),visitaVta);

                                    if(estado==4){
                                        Log.d(TAG,"setResult Online...");
                                        setResult(RESULT_VISITA_ONLINE_REFERIDO);
                                        finish();
                                    }else if(estado == 3){
                                        setResult(RESULT_VISITA_REGULAR_REFERIDO);
                                        finish();
                                    }else if(estado ==7){
                                        setResult(RESULT_VISITA_REGULAR_REFERIDO);
                                        finish();
                                    }
                                }else{
                                    Log.d(TAG,"No isSucessful...");
                                    Toast.makeText(CotizacionActivity.this, R.string.message_error_sync_connection_server_fail, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).finalizar(jsonFinal);
            }

        }
    }

    private void setBlockRecurrencia(boolean flag){
        //RECURRRENTE: ANUAL O MENSUAL
        // AHORA SOLO APLICAR ESTE: NO RECURRENTE: SELECCIONE ANUAL
        if(flag){
            ContainerTD.setEnabled(true);
            ContainerTC.setEnabled(true);
            SelectTC.setEnabled(true);
            SelectTD.setEnabled(true);
        }else{
            ContainerTD.setEnabled(false);
            ContainerTC.setEnabled(false);
            SelectTC.setEnabled(false);
            SelectTD.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...requestcode:"+requestCode+" resultcode:"+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        //region Request & Result de la Cotizacion
        if(requestCode==REQUEST_VISITA_SOLICITUD_COTIZACION_INICIAL){
            Log.d(TAG,"Request solicitud cotizacion inicial...");
            if(resultCode == RESULT_VISITA_SOLICITUD_COTIZACION_INICIAL_OK){
                Log.d(TAG,"Resultado desde la solicitud iniciada por cotizacion inicial OK...");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                //Iniciar la Cotizacion Final
                visitaVta.setEstado(2);
                solicitudMovil = SessionManager.getInstance(getApplicationContext()).getSolicitud();
                Log.d(TAG,solicitudMovil.toString());
                SessionManager.getInstance(getApplicationContext()).removeSolicitud();
                SessionManager.getInstance(this).createVisitaSession(visitaVta);
                estado = 2;
                select = -1;
                Log.d(TAG,"tipoVenta:"+tipoVenta);
                tipoVenta = 1;
                selectedDateAfiliado = null;
                //rbGroupTipoVenta.clearCheck();
                //rbIndividual.clearFocus();
                //rbCorporativo.setChecked(false);
                SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
                SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
                SelectTD.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
                ContainerAnual.setBackgroundColor(getResources().getColor(R.color.White));
                ContainerTD.setBackgroundColor(getResources().getColor(R.color.White));
                ContainerTC.setBackgroundColor(getResources().getColor(R.color.White));
                requestVenta = -1;
                selectPlanCotizacion = -1;
                selectTipoPagoFinal = -1;
                selectTipoPago = -1;
                selectTipoVenta = -1;

               //
                consultarCotizacion=-1;
                //
                ResponseView.setVisibility(View.GONE);
                rbFrecuenciaAnual.setChecked(false);
                rbFrecuenciaMensual.setChecked(false);
                rbFrecuenciaAnual.setEnabled(false);
                rbFrecuenciaMensual.setEnabled(false);
                rbPaymeSi.setChecked(false);
                rbRegular.setChecked(false);
                rbPaymeNo.setChecked(false);

                frecuenciaPagoSelected = -1;
                tvCotizacionTitle.setText(estado==1?"Cotización Inicial":"Cotización Final");
                itemEmail.setVisible(false);
                cvPayme.setVisibility(View.VISIBLE);
                etFechaVencimiento.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.d(TAG,"etFechaVencimiento:beforeTextChanged");
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.d(TAG,"etFechaVencimiento:onTextChanged= s:"+s+" start:"+start+" count:"+count);
                        /*if(!TextUtils.isEmpty(etFechaVencimiento.getText())){
                            String txt = etFechaVencimiento.getText().toString().trim();
                                txt = etFechaVencimiento.getText().toString().trim();
                                if(txt!=""){
                                    if(txt.length()==1){
                                        if(!txt.equalsIgnoreCase("0") && !txt.equalsIgnoreCase("1")){
                                            etFechaVencimiento.setText(null);
                                        }
                                    }else if(txt.length()==2){
                                        String first = txt.substring(0,0);
                                        String second = txt.substring(1);
                                        if(!first.equalsIgnoreCase("0") && !first.equalsIgnoreCase("1")){

                                        }
                                    }
                                    if(txt.length()==2){
                                        etFechaVencimiento.setText(txt+"/");
                                    }
                                }
                        }*/

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d(TAG,"etFechaVencimiento:afterTextChanged");
                        Log.d(TAG,"s.lenght:"+s.length());
                        if (!Util.isInputCorrect(s, CARD_DATE_TOTAL_SYMBOLS, CARD_DATE_DIVIDER_MODULO, CARD_DATE_DIVIDER)) {
                            Log.d(TAG,"!Util.isInputCorrect...");
                            if(s.length()==3){
                                Log.d(TAG,"s.lenght<3...");
                                String concatString = Util.concatString(Util.getDigitArray(s, CARD_DATE_TOTAL_DIGITS), CARD_DATE_DIVIDER_POSITION, CARD_DATE_DIVIDER);
                                Log.d(TAG,"concatString:"+concatString);
                                s.replace(0, s.length(),concatString );
                            }



                        }
                    }
                });
                if(solicitudMovil.getIdGeneraSolicitud().equalsIgnoreCase("F")){
                    for(SolicitudAfiliadoMovil solicitud:solicitudMovil.getAfiliados()){
                        if(solicitud.getIdTitular()!=null){
                            if(solicitud.getIdTitular().length()>0){
                                if(solicitud.getIdTitular().equalsIgnoreCase(Contants.GENERAL_VALUE_SOLICITUD_VALIDAR_TITULAR_SI)){
                                    afiliadoContratante = solicitud;
                                    break;
                                }
                            }
                        }
                    }
                    initSolicitudFisica();
                }
                //Recurrencia
                if(solicitudMovil.getModoTarifa()!=null){
                    if(solicitudMovil.getModoTarifa().length()>0){
                        GeneralValue generalValue = null;
                        List<GeneralValue> list = GeneralValue.getGeneralValues(Utils.getDataBase(this),GENERAL_TABLE_MODO_TARIFA_TIPO_PAGO);
                        for (GeneralValue obj:list){
                            if(obj.getCode().equalsIgnoreCase(solicitudMovil.getModoTarifa())){
                                generalValue = obj;
                                break;
                            }
                        }
                        //Dato
                        if(generalValue.getCode().equalsIgnoreCase("RE")){
                            setBlockRecurrencia(true);
                        }else{
                            setBlockRecurrencia(false);
                        }
                    }
                }
                showLayoutTarjeta(false);
                //
            }
            else if(resultCode == RESULT_VISITA_SOLICITUD_COTIZACION_INICIO_CANCELADA){
                Log.d(TAG,"Resultado desde la solicitud iniciada por cotizacion inicial cancelada...");
                setResult(RESULT_VISITA_REPROGRAMM_CANCEL);
                this.finish();
                Log.d(TAG,"meterle RETURN...");
                return;
            }
            else if(resultCode == RESULT_VISITA_REPROGRAMADA_FINALIZADA){
                Log.d(TAG,"Resultado desde la solicitud iniciada por cotizacion inicial reprogramada la visita...");
                Log.d(TAG,"Enviar el result para enviar las visitas en BD...");
                //setResult(RESULT_VISITA_REPROGRAMADA_FINALIZADA);
                setResult(RESULT_VISITA_REPROGRAMM_CANCEL);
                this.finish();
                Log.d(TAG,"meterle RETURN...");
                return;
            }
        }
        //endregion

        //region Request % Result Solicitud Test
        if(requestCode == REQUEST_VISITA_SOLICITUD_COTIZACION_FINAL){
            Log.d(TAG,"Request solicitud cotizacion final..");
            if(resultCode == RESULT_VISITA_SOLICITUD_COTIZACION_FINAL_OK){
                Log.d(TAG,"Resultado desde la solicitud iniciada por cotizacion final OK...");
            }else if(resultCode == RESULT_VISITA_SOLICITUD_COTIZACION_INICIO_CANCELADA){
                Log.d(TAG,"Resultado desde la solicitud iniciada por cotizacion inicial cancelada...");
                setResult(RESULT_VISITA_REPROGRAMM_CANCEL);
                this.finish();
                Log.d(TAG,"meterle RETURN...");
                return;
            }
            else if(resultCode == RESULT_VISITA_REPROGRAMADA_FINALIZADA){
                Log.d(TAG,"Resultado desde la solicitud iniciada por cotizacion inicial reprogramada la visita...");
                Log.d(TAG,"Enviar el result para enviar las visitas en BD...");
                //setResult(RESULT_VISITA_REPROGRAMADA_FINALIZADA);
                setResult(RESULT_VISITA_REPROGRAMM_CANCEL);
                this.finish();
                Log.d(TAG,"meterle RETURN...");
                return;
            }
        }
        //endregion

        //region Code Alignet
        if (requestCode == pe.solera.api_payme_android.util.Constants.RESULT_API) {
            Log.d(TAG,"request activity alignet...");
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG,"resultCode==Activity.RESULT_OK...");
                boolean successful = data.getBooleanExtra("successful", false);
                PayMeResponse payMeResponse = (PayMeResponse) data.getSerializableExtra("payMeResponse");
                if (successful) {
                    String fecha = payMeResponse.getDate() +" "+ payMeResponse.getHour();
                    Log.d(TAG,"fecha payme date + hour:"+fecha);
                    String splisFechaDateAno = payMeResponse.getDate();
                    SimpleDateFormat sf = new SimpleDateFormat(Contants.DATE_TIME_FORMAT_ALIGNET);
                    if(payMeResponse.getDate()!=null){
                        try {
                            Date fechaPayme = sf.parse(fecha);
                            Calendar calendar = Calendar.getInstance();
                            registroPago.setFechaPago(Convert.getDotNetTicksFromDate(fechaPayme));

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG,"ParseException en fecha:"+e.getMessage());
                            registroPago.setFechaPago(Convert.getDotNetTicksFromDate(Calendar.getInstance().getTime()));
                        }
                    }else{
                        registroPago.setFechaPago(Convert.getDotNetTicksFromDate(Calendar.getInstance().getTime()));
                    }

                    registroPago.setAnswerCode(payMeResponse.getAnswerCode());
                    registroPago.setNumeroOperacion(payMeResponse.getOperationNumber());
                    registroPago.setNumeroTransaccion(payMeResponse.getIdTransaction());
                    registroPago.setAnswerMessage(payMeResponse.getAnswerMessage());
                    registroPago.setOperationNumber(operationNumber);
                    registroPago.setErrorCode(payMeResponse.getErrorCode());
                    registroPago.setErrorMessage(payMeResponse.getErrorMessage());
                    registroPago.setCodAsoCardHolderWallet(payMeResponse.getCodAsoCardHolderWallet());
                    registroPago.setCardNumber(payMeResponse.getCardNumber());
                    registroPago.setIdAfiliadoContratante(afiliadoContratante.getIdSolicitudAfiliado());
                    registroPago.setMonto(precio);
                    showEmailRegistroPago(successful);
                    Log.d(TAG,"succesfull..");
                    Log.d(TAG, "pay - ok:" + payMeResponse.toString());
                    Log.d(TAG,"objeto payme.tostring:"+payMeResponse.toString());

                } else {
                    Toast.makeText(this, getResources().getString(R.string.buying_unprocessed), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"!succesfull...");
                    if (payMeResponse != null) {
                        Log.d(TAG,"payme!=null");
                        Log.d(TAG, "pay - error:" + payMeResponse.getErrorCode());
                        Log.d(TAG,"objeto payme.tostring"+payMeResponse.toString());
                        registroPago.setOperationNumber(operationNumber);
                        registroPago.setErrorCode(payMeResponse.getErrorCode());
                        registroPago.setErrorMessage(payMeResponse.getErrorMessage());
                        registroPago.setCodAsoCardHolderWallet(payMeResponse.getCodAsoCardHolderWallet());
                        registroPago.setIdAfiliadoContratante(afiliadoContratante.getIdSolicitudAfiliado());
                        registroPago.setMonto(precio);
                        registroPago.setFechaPago(Convert.getDotNetTicksFromDate(Calendar.getInstance().getTime()));
                        showEmailRegistroPago(successful);
                    } else {
                        Log.d(TAG,"payme==null");
                        Log.d(TAG, "pay - error");
                        Log.d(TAG,""+payMeResponse);
                    }
                }
            }else{
                Log.d(TAG,"API_PAYME_REQUEST: NO ES OK SINO RESULT_CANCELED...");
                PayMeResponse payMeResponse = (PayMeResponse) data.getSerializableExtra("payMeResponse");
                if(payMeResponse!=null){
                    Log.d(TAG,"paymeResponse!=null...");
                    Log.d(TAG,payMeResponse.toString());
                    registroPago.setAnswerCode(payMeResponse.getAnswerCode());
                    registroPago.setAnswerMessage(payMeResponse.getAnswerMessage());
                    registroPago.setOperationNumber(operationNumber);
                    registroPago.setErrorCode(payMeResponse.getErrorCode());
                    registroPago.setErrorMessage(payMeResponse.getErrorMessage());
                    registroPago.setCodAsoCardHolderWallet(payMeResponse.getCodAsoCardHolderWallet());
                    registroPago.setIdAfiliadoContratante(afiliadoContratante.getIdSolicitudAfiliado());
                    registroPago.setMonto(precio);
                    showEmailRegistroPago(false);
                }else{
                    Log.d(TAG,"paymeResponse==null...");
                }
            }
        }
        //endregion

        //region Fisica
        if(requestCode == REQUEST_VISITA_NUEVA_PAGO_FISICO && resultCode == RESULT_VISITA_NUEVA_PAGO_FISICO){
            Log.d(TAG,"requestCode == REQUEST_VISITA_NUEVA_PAGO_FISICO && resultCode == RESULTVISITA_NUEVA_PAGO_FISICO");
            Log.d(TAG,"Enviar el result para enviar las visitas en BD...");
            setResult(RESULT_VISITA_EFECTIVO_REFERIDO);
            finish();
        }
        //endregion

        //region Reprogramada
        //if(REQUEST_STATE == REQUEST_VISITA_COTIZACION_NUEVO){
            if(requestCode == REQUEST_VISITA_REPROGRAMADA_FINALIZADA && resultCode == RESULT_VISITA_REPROGRAMADA_FINALIZADA){
                Log.d(TAG,"requestCode == REQUEST_VISITA_REPROGRAMADA_FINALIZADA && resultCode == RESULT_VISITA_REPROGRAMADA_FINALIZADA");
                Log.d(TAG,"Enviar el result al main2 para enviar las visitas en BD y matar esta Activity...");
                //setResult(RESULT_VISITA_REPROGRAMADA_FINALIZADA);
                setResult(RESULT_VISITA_REPROGRAMM_CANCEL);
                //CotizacionActivity.this.finish();
                finish();
                Log.d(TAG,"meterle RETURN...");
                SessionManager.getInstance(this).removeVisitaSession();
                return;
            }
        //}
        //endregion
    }

    //region Ciclo de Vida

    @Override
    protected void onStart() {
        Log.d(TAG,"onStart...");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause...");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop...");
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed...");
        //super.onBackPressed();
        if(estado==2){
            setDataSolicitudBack(visitaVta.getIdVisita(),"F");
        }else{
            moveTaskToBack(true);
        }
        /*if(estado==1){
            moveTaskToBack(true);
        }else if(estado==2){

        }*/
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy...");
        //SessionManager.getInstance(this).removeVisitaSession();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cotizacion,menu);
        itemEmail = menu.findItem(R.id.action_email_cotizacion);
        return super.onCreateOptionsMenu(menu);
    }

    //endregion


    //region Solicitud Virtual Methods

    private void showAutenticationFingerPrint(){
        Log.d(TAG,"showAutenticationFingerPrint...");
        FingerPrintDialog fingerPrintDialog = FingerPrintDialog.newInstance(new FingerPrintDialog.callBackListener() {
            Handler handler = new Handler(Looper.getMainLooper());
            @Override
            public void onError(String mensaje) {
                Log.d(TAG,"onError:"+mensaje);

                try{
                    //setDataSolicitud(visitaVta.getIdVisita(),"V");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess() {
                Log.d(TAG,"onSuccess...");
                try{
                    //setDataSolicitud(visitaVta.getIdVisita(),"V");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },
                new Bundle(),
                new Handler(Looper.getMainLooper()){
                    public void handleMessage(Message msg) {

                    }

        } ,
                new Handler(Looper.getMainLooper()){
                    public void handleMessage(Message msg) {
                        Log.d(TAG,"printHandler...");

                        if(msg!=null){
                            Log.d(TAG,"msg!=null...");
                            System.out.println(msg);
                        }else{
                            Log.d(TAG,"msg==null...");
                        }
                        byte[] image;
                        String errorMessage = "empty";
                        int status = msg.getData().getInt("status");
                        Intent intent = new Intent();
                        intent.putExtra("status", status);
                        if (status == Status.SUCCESS) {
                            Log.d(TAG,"statuss== SUCCESS..");
                            image = msg.getData().getByteArray("img");
                            showDialogPrintAuthenticate(image);
                        } else {
                            Log.d(TAG,"Status != SUCCESS...");
                            errorMessage = msg.getData().getString("errorMessage");
                            intent.putExtra("errorMessage", errorMessage);
                            Toast.makeText(CotizacionActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }

        });
        fingerPrintDialog.setCancelable(true);
        fingerPrintDialog.show(getSupportFragmentManager(),null);
    }

    private void showDialogPrintAuthenticate(byte[] image){
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.appname_marketforce);
        progressDialog.setMessage("Autenticando huella...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new AutenticarHuellaRp3Client(CotizacionActivity.this, new Callback() {
            @Override
            public void onFailure(Call call1, IOException e) {
                Log.d(TAG,"onFailure...");
                Log.d(TAG,"IOException:"+e.getMessage());
                e.printStackTrace();
                try{
                    progressDialog.dismiss();
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
            @Override
            public void onResponse(Call calll, Response response) throws IOException {
                Log.d(TAG,"onResponse...");
                progressDialog.dismiss();
                try{
                    final String rpta = response.body().string();

                    Log.d(TAG,"rpta autentication huella:"+rpta);
                    if(response.isSuccessful()){
                        Log.d(TAG,"response.isSuccessful...");
                        if(rpta.trim().equalsIgnoreCase("1") || rpta.trim().equalsIgnoreCase("\"1\"")){
                            Log.d(TAG,"Autenticado....");
                            setDataSolicitud(visitaVta.getIdVisita(),"V");
                        }else{
                            Toast.makeText(CotizacionActivity.this, "No se encuentra registrado.", Toast.LENGTH_SHORT).show();
                            setDataSolicitud(visitaVta.getIdVisita(),"V");
                            //call.onError("No se encuentra registrado.");
                        }
                    }else{
                        Log.d(TAG,"response no is Successful...");
                        Toast.makeText(CotizacionActivity.this, "No se encuentra registrado.", Toast.LENGTH_SHORT).show();
                        //call.onError("No se encuentra registrado.");

                        //Toast.makeText(getActivity(), rpta, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).autenticar(image);
    }

    private void showDialogCotizadorVirtualClient(CotizacionVirtual cotizacionzacionMovil){
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle(R.string.appname_marketforce);
        progressDialog.setMessage("Consultando Cotización...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new CotizadorVirtualClient(this, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,"onFailure...");
                progressDialog.dismiss();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> Toast.makeText(CotizacionActivity.this, "Hubo un problema en el servidor...intentelo mas tarde...", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                Log.d(TAG,"onResponse...");
                String json = response.body().string();
                if(response.isSuccessful()){
                    Log.d(TAG,"isSuccessful...");
                    TypeToken<List<Cotizacion>> type = new TypeToken<List<Cotizacion>>(){};
                    cotizacionList = new Gson().fromJson(json,type.getType());
                    Log.d(TAG,"Hay "+cotizacionList.size()+" respuestas de cotizacionMovil...");
                    Log.v(TAG,json);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        boolean procede = true;
                        for(Cotizacion cotizacion:cotizacionList){
                            if(cotizacion.getResult().equalsIgnoreCase("1")){
                                for(GeneralValue generalValue:listExcepcions){
                                    if(cotizacion.getInfo().contains(generalValue.getCode())){
                                        Toast.makeText(CotizacionActivity.this, generalValue.getValue(), Toast.LENGTH_SHORT).show();
                                        Log.v(TAG,"Excepcion General:"+generalValue.toString());
                                        procede = false;
                                        break;
                                    }
                                }
                                procede = false;
                                Log.d(TAG,cotizacion.toString());
                                //Toast.makeText(CotizacionActivity.this, cotizacion.getInfo(), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if(procede){
                            Log.v(TAG,"Si procede...");
                            consultarCotizacion = 2;
                            flagRealizoCotizacionInicial = 1;
                            cotizacionVirtual.setRespuesta(cotizacionList);
                            selectTipoVenta = tipoVenta;
                            selectPlanCotizacion = select;
                            if(tipoVenta == 1){
                                requestVenta = 1;
                            }else{
                                requestVenta = 2;
                            }
                            setResponseCotizacion(cotizacionList);
                        }
                        else{
                            Log.v(TAG,"No procede...");
                        }
                    });
                }else{
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> Toast.makeText(CotizacionActivity.this, "Hubo un problema al realizar la cotizacion. Intentelo nuevamente.", Toast.LENGTH_SHORT).show());
                    Log.d(TAG,"Not isSucessful...");
                }
            }
        }).cotizar(cotizacionzacionMovil);
    }

    private void configureViewsVirtual(){
        rbVirtual.setOnClickListener(v -> {
            Log.d(TAG,"rbVirtual...");
            consultarCotizacion=2;
            rbVirtual.setChecked(true);
            rbFisica.setChecked(false);
            Log.d(TAG,"consultarCotizacion es:"+consultarCotizacion);
        });
        rbFisica.setOnClickListener(v -> {
            Log.d(TAG,"rbFisica...");
            consultarCotizacion=1;
            rbFisica.setChecked(true);
            rbVirtual.setChecked(false);
            Log.d(TAG,"consultarCotizacion es:"+consultarCotizacion);
        });
    }

    private void initVirtual(){
        configureViewsVirtual();
    }

    private void configureInitCheck(){
        rbFisica.setChecked(true);
        consultarCotizacion=2;
        rbIndividual.setChecked(true);
        tipoVenta = 1;
    }
}
