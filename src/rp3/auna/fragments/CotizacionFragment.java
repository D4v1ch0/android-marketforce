package rp3.auna.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.bean.AfiliadoMovil;
import rp3.auna.bean.Cotizacion;
import rp3.auna.bean.CotizacionMovil;
import rp3.auna.bean.CotizacionVisita;
import rp3.auna.dialog.CitaDialog;
import rp3.auna.models.ventanueva.ProspectoVtaDb;
import rp3.auna.utils.NothingSelectedSpinnerAdapter;
import rp3.auna.utils.Utils;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;

/**
 * Created by Jesus Villa on 24/10/2017.
 */

public class CotizacionFragment extends Fragment {
    private static final String TAG = CotizacionFragment.class.getSimpleName();
    @BindView(R.id.lyContainerTemp)View temp;
    @BindView(R.id.lyContainerData)View data;
    //region Views
    @BindView(R.id.rbIndividual)RadioButton rbIndividual;
    @BindView(R.id.rbCorporativo)RadioButton rbCorporativo;
    @BindView(R.id.lnRucEmpresa) LinearLayout lnRucEmpresa;
    @BindView(R.id.lnFraccionamientos) LinearLayout lnFraccionamientos;
    @BindView(R.id.spFraccionamiento) Spinner spFraccionamiento;
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
    //endregion
    //region Variables
    private CotizacionVisita cotizacionVisita;
    private List<LinearLayout> afiliadosLayouts;
    private List<Cotizacion> cotizacionFraccionamientos;
    private List<GeneralValue> list;
    private List<Cotizacion> cotizacionAfiliados;
    private List<GeneralValue> listFraccionamientos;
    private List<GeneralValue> listExcepcions;
    private String ruc = null;
    private double totalAnual = 0;
    private double totalTC = 0;
    private double totalTD = 0;
    //endregion

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView...");
        View view = inflater.inflate(R.layout.fragment_response_cotizacion,container,false);
        ButterKnife.bind(this,view);
        try{
            if(visualize()){
                Log.d(TAG,"visualize...");
                temp.setVisibility(View.GONE);
                data.setVisibility(View.VISIBLE);
                initViews(view);
            }else{
                Log.d(TAG,"No visualize...");
                temp.setVisibility(View.VISIBLE);
                data.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private boolean visualize(){
        if(cotizacionVisita!=null){
            return true;
        }
        return false;
    }

    private void setEnabled(View view){
        ((CheckBox) view.findViewById(R.id.checkboxcamapana)).setEnabled(false);
        ((Spinner) view.findViewById(R.id.cotizacion_programa_movil)).setEnabled(false);
        rbIndividual.setEnabled(false);
        temp.setEnabled(false);
        data.setEnabled(false);
        rbIndividual.setEnabled(false);
        rbCorporativo.setEnabled(false);
        lnRucEmpresa.setEnabled(false);
        lnFraccionamientos.setEnabled(false);
        spFraccionamiento.setEnabled(false);
        ContainerParams.setEnabled(false);
        ContainerAnual.setEnabled(false);
        ContainerTC.setEnabled(false);
        ContainerTD.setEnabled(false);
        SelectAnual.setEnabled(false);
        SelectTC.setEnabled(false);
        SelectTD.setEnabled(false);
        EmptyView.setEnabled(false);
        ResponseView.setEnabled(false);
        TotalAnual.setEnabled(false);
        TotalTC.setEnabled(false);
        TotalTD.setEnabled(false);
    }

    //region Informacion

    private void setAfiliados(View view){
        List<AfiliadoMovil> afiliadoMovils = cotizacionVisita.getAfiliados();
        //Programa
        List<GeneralValue> values = new ArrayList<>();
        for (GeneralValue generalValue:list){
            if(generalValue.getReference1().equalsIgnoreCase("A")){
                values.add(generalValue);
            }
        }
        for (int i = 0 ;i<values.size();i++){
            final int j = i;
            GeneralValue generalValue = values.get(i);
            if(generalValue.getCode().equalsIgnoreCase(afiliadoMovils.get(0).getCodigoPrograma())){
                ((Spinner) view.findViewById(R.id.cotizacion_programa_movil)).post(new Runnable() {
                    @Override
                    public void run() {
                        ((Spinner) view.findViewById(R.id.cotizacion_programa_movil)).setSelection(j+1);
                    }
                });
                break;
            }
        }
        //Campaña
        if(afiliadoMovils.get(0).getCampana().equalsIgnoreCase("False")){
            ((CheckBox) view.findViewById(R.id.checkboxcamapana)).setChecked(false);
        }else{
            ((CheckBox) view.findViewById(R.id.checkboxcamapana)).setChecked(true);
        }

        ((CheckBox) view.findViewById(R.id.checkboxcamapana)).setEnabled(false);

        for (int i = 0;i<afiliadoMovils.size();i++){
            AfiliadoMovil afiliadoMovil = afiliadoMovils.get(i);
            final LinearLayout afiliado = (LinearLayout) LayoutInflater.from(view.getContext()).inflate(R.layout.container_afiliado, null);
            SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(view.getContext(), Utils.getDataBase(view.getContext()), rp3.auna.Contants.GENERAL_TABLE_GENERO);
            ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).setAdapter(tipoGeneroAdapter);
            ((Spinner) afiliado.findViewById(R.id.afiliado_sexo_movil)).setEnabled(false);
            //Esconde button eliminar
            afiliado.findViewById(R.id.eliminar_afiliado_movil).setVisibility(View.GONE);
            //Fecha de Nacimiento
            ((EditText) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).setText(afiliadoMovil.getFechaNacimiento());
            ((EditText) afiliado.findViewById(R.id.tvafiliado_fecha_movil)).setEnabled(false);
            //Flag Fumador
            if(afiliadoMovil.getFlagFumador().equalsIgnoreCase("False")){
                ((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).setChecked(false);
            }else{
                ((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).setChecked(true);
            }
            ((CheckBox) afiliado.findViewById(R.id.checkBox_movil)).setEnabled(false);
            //Sexo
            List<GeneralValue> generos = GeneralValue.getGeneralValues(Utils.getDataBase(view.getContext()), rp3.auna.Contants.GENERAL_TABLE_GENERO);
            for (int k = 0;k<generos.size();k++){
                GeneralValue generalValue = generos.get(k);
                if(generalValue.getCode().equalsIgnoreCase(afiliadoMovil.getSexo())){
                    ((Spinner)afiliado.findViewById(R.id.afiliado_sexo_movil)).setSelection(k,true);
                    ((Spinner)afiliado.findViewById(R.id.afiliado_sexo_movil)).setEnabled(false);
                    break;
                }
            }
            afiliado.setEnabled(false);
            ContainerParams.addView(afiliado);
            afiliadosLayouts.add(afiliado);
        }
    }

    private void setCotizacion(View view){
        List<Cotizacion> cotizacions = cotizacionVisita.getRespuesta();
        ContainerAnual.removeAllViews();
        ContainerTD.removeAllViews();
        ContainerTC.removeAllViews();
        //Control de errores
        NumberFormat numberFormat;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        totalAnual = 0;
        totalTC = 0;
        totalTD = 0;
        boolean hayError = false;
        cotizacionAfiliados = new ArrayList<>();
        cotizacionFraccionamientos = new ArrayList<>();
        //Obtener las Cotizaciones
        for(Cotizacion cotizacionAfiliado:cotizacions){
            if(cotizacionAfiliado.getIdOperacion().equalsIgnoreCase("C")){
                cotizacionAfiliados.add(cotizacionAfiliado);
            }
        }
        //Obtener las fracciones
        for(Cotizacion cotizacionFraccion:cotizacions){
            if(cotizacionFraccion.getIdOperacion().equalsIgnoreCase("F")){
                cotizacionFraccionamientos.add(cotizacionFraccion);
            }
        }
        //Informacion de cotizacion
        Log.v(TAG,"setResponseCotizacion Cantidad de Fracciones:"+cotizacionFraccionamientos.size());
        for (int i = 0; i < afiliadosLayouts.size(); i++) {
            LinearLayout afiliado = afiliadosLayouts.get(i);
            Cotizacion jsonObject = cotizacionAfiliados.get(i);

            if (jsonObject.getResult().equalsIgnoreCase("1")) {
                hayError = true;
                break;
            }
            LinearLayout AfiliadoAnual = (LinearLayout) LayoutInflater.from(view.getContext()).inflate(R.layout.container_afiliado_cotizacion, null);
            LinearLayout AfiliadoTC = (LinearLayout) LayoutInflater.from(view.getContext()).inflate(R.layout.container_afiliado_cotizacion, null);
            LinearLayout AfiliadoTD = (LinearLayout) LayoutInflater.from(view.getContext()).inflate(R.layout.container_afiliado_cotizacion, null);

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

            ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_precio_movil)).setText("S/. " + numberFormat.format(Double.parseDouble(jsonObject.getAnual())));
            ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_precio_movil)).setText("S/. " + numberFormat.format(Double.parseDouble(jsonObject.getTC())));
            ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_precio_movil)).setText("S/. " + numberFormat.format(Double.parseDouble(jsonObject.getTD())));
            totalAnual = totalAnual + Double.parseDouble(jsonObject.getAnual());
            totalTC = totalTC + Double.parseDouble(jsonObject.getTC());
            totalTD = totalTD + Double.parseDouble(jsonObject.getTD());
            afiliado.setEnabled(false);
            ContainerAnual.addView(AfiliadoAnual);
            ContainerTC.addView(AfiliadoTC);
            ContainerTD.addView(AfiliadoTD);
        }

        if (!hayError) {
            Log.d(TAG,"!hayError...");
            TotalAnual.setText("S/. " + numberFormat.format(totalAnual)+" anual");
            TotalTC.setText("S/. " + numberFormat.format(totalTC)+" al mes");
            TotalTD.setText("S/. " + numberFormat.format(totalTD)+" al mes");
            //Mostrar resultado de cotizacionMovil
            if(rbIndividual.isChecked()){
                Log.v(TAG,"setResponseCotizacion TipoVenta==1");
                refreshFraccionamientos(cotizacions,view);
                lnFraccionamientos.setVisibility(View.VISIBLE);
            }else{
                Log.v(TAG,"setResponseCotizacion TipoVenta==2");
                lnFraccionamientos.setVisibility(View.GONE);
            }
            ResponseView.setVisibility(View.VISIBLE);
            EmptyView.setVisibility(View.GONE);
            refreshSelectedPay();
        } else
        {
            Log.d(TAG,"hayError...");
            Toast.makeText(view.getContext(), "Hubo un problema al mostrar el resultado de la cotizacionMovil, intentelo nuevamente porfavor..", Toast.LENGTH_SHORT).show();
            //SE DEBE MOSTRAR EL ERROR QUE RETORNA EL SERVICIO
        }

    }

    private void initViews(final View view){
        if(cotizacionVisita.getTipoVenta().equalsIgnoreCase("1")){
            rbIndividual.setChecked(true);
            rbCorporativo.setChecked(false);
        }else{
            rbCorporativo.setChecked(true);
            rbCorporativo.setVisibility(View.VISIBLE);
        }
        rbCorporativo.setEnabled(false);
        afiliadosLayouts = new ArrayList<>();
        //Cargo cotización en el caso de que exista
        list = GeneralValue.getGeneralValues(Utils.getDataBase(getActivity()),Contants.GENERAL_TABLE_PROGRAMAS);
        List<GeneralValue> generalValueList = new ArrayList<>();
        for(GeneralValue obj:list){
            if(obj.getReference1().equalsIgnoreCase("A")){
                generalValueList.add(obj);
            }
        }

        SimpleGeneralValueAdapter programaAdapter = new SimpleGeneralValueAdapter(view.getContext(), generalValueList);
        ((Spinner) view.findViewById(R.id.cotizacion_programa_movil)).setAdapter(new NothingSelectedSpinnerAdapter(
                programaAdapter,
                R.layout.spinner_empty_selected,
                view.getContext(), "Seleccionar"));
        ((Spinner) view.findViewById(R.id.cotizacion_programa_movil)).setEnabled(false);

        //Insertar Afiliados
        setAfiliados(view);
        refreshTvAfiliado();
        //Insertar Cotizacion
        setCotizacion(view);
        setEnabled(view);
    }

    private void refreshFraccionamientos(List<Cotizacion>cotizacionList,View view){
        listFraccionamientos = GeneralValue.getGeneralValues(Utils.getDataBase(view.getContext()), Contants.GENERAL_TABLE_COTIZACION_FRACCIONAMIENTO);
        listExcepcions = GeneralValue.getGeneralValues(Utils.getDataBase(view.getContext()),Contants.GENERAL_TABLE_COTIZACION_TIPOS_ERROR);
        String valueSeleccionado = null;
        List<String> precios = new ArrayList<>();
        if(cotizacionList!=null) {
            Log.v(TAG,"refreshFraccionamientos:cotizacionList!=null");
            if (cotizacionList.size() > 0) {
                Log.v(TAG,"refreshFraccionamientos:cotizacionList.size() > 0");
                cotizacionFraccionamientos = new ArrayList<>();
                for(int i = 0;i<cotizacionList.size();i++){
                    if(cotizacionList.get(i).getIdOperacion().equalsIgnoreCase("F")){
                        cotizacionFraccionamientos.add(cotizacionList.get(i));
                        valueSeleccionado = cotizacionList.get(i).getInfo();
                    }
                }

                precios.add(String.valueOf(totalAnual));
                final int countCuotas = cotizacionFraccionamientos.size();
                if(countCuotas>0){
                    //2 cuotas
                    precios.add((cotizacionFraccionamientos.get(0).getTD()));
                }
                //3 Cuotas
                if(countCuotas>2){
                    precios.add((cotizacionFraccionamientos.get(2).getTD()));
                }
                //4 Cuotas
                if(countCuotas>5){
                    precios.add((cotizacionFraccionamientos.get(5).getTD()));
                }
               // precios.add(cotizacionFraccionamientos.get(0).getTD());
                //precios.add(cotizacionFraccionamientos.get(2).getTD());
                //precios.add(cotizacionFraccionamientos.get(5).getTD());
            }
        }

        List<GeneralValue> temp = new ArrayList<>();
        for(int j = 0; j < precios.size();j++){
            int k = j+1;
            GeneralValue obj = listFraccionamientos.get(j);
            obj.setValue(k+" cuota: S/."+precios.get(j));
            obj.setReference1(precios.get(j));
            temp.add(obj);
        }
        GeneralValue obj = new GeneralValue();
        obj.setValue(cotizacionVisita.getCantidadCuota()+" cuota: S/."+cotizacionVisita.getPrecioCuota());
        Log.d(TAG,obj.toString());
        Log.d(TAG,"cantidad de cuotas a mostrar:"+temp.size());
        listFraccionamientos.clear();
        listFraccionamientos.addAll(temp);
        SimpleGeneralValueAdapter programaAdapter = new SimpleGeneralValueAdapter(view.getContext(), listFraccionamientos){

            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                RelativeLayout layout = ((RelativeLayout)v);
                TextView tv = (TextView)layout.findViewById(R.id.tvGeneralSelectedd);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_normal));
                //tv.setTextColor(getResources().getColor(R.color.White));
                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                //((TextView) v).setGravity(Gravity.CENTER);
                return v;

            }

        };
        spFraccionamiento.setAdapter(new NothingSelectedSpinnerAdapter(
                programaAdapter,
                R.layout.item_spinner_white,
                view.getContext(), "Seleccionar"));
        spFraccionamiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean estado = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"onItemSelected...");
                if(estado){
                    estado = false;
                    Log.d(TAG,"Estado true...");
                }else{
                    Log.d(TAG,"Estado false...");
                    RelativeLayout layout = ((RelativeLayout) view);
                    TextView tv = (TextView) layout.findViewById(R.id.tvGeneralSelectedd);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_normal));
                    tv.setTextColor(getResources().getColor(R.color.White));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG,"onNothingSelected...");
            }
        });

        spFraccionamiento.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG,"spFraccionamiento sub hilo...");
                    String cantidadCuota = cotizacionVisita.getCantidadCuota();
                    if(cantidadCuota!=null) {
                        Log.d(TAG, "cantidadCuota !=null : cantidadCuota: " + cantidadCuota);
                        /*if(cantidadCuota.trim().equalsIgnoreCase("1")){
                            spFraccionamiento.setSelection(1);
                        }else if(cantidadCuota.trim().equalsIgnoreCase("2")){
                            spFraccionamiento.setSelection(2);
                        }else if(cantidadCuota.trim().equalsIgnoreCase("3")){
                            spFraccionamiento.setSelection(3);
                        }else if(cantidadCuota.trim().equalsIgnoreCase("4")){
                            spFraccionamiento.setSelection(4);
                        }
                        */
                        spFraccionamiento.setSelection(Integer.parseInt(cantidadCuota));
                    }else{
                            spFraccionamiento.setSelection(0);
                        }
                    }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        spFraccionamiento.setEnabled(false);
        lnFraccionamientos.setEnabled(false);
        if(valueSeleccionado!=null){
            if(!valueSeleccionado.equalsIgnoreCase("Anual")){
                lnFraccionamientos.setVisibility(View.GONE);
            }
        }
    }

    private void refreshSelectedPay(){
        List<Cotizacion> cotizacions = cotizacionVisita.getRespuesta();
        String tipoVenta = cotizacions.get(0).getInfo();
        SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
        SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
        SelectTD.setBackgroundColor(getResources().getColor(R.color.color_background_selector));
        if(rbIndividual.isChecked()){
            if(tipoVenta.equalsIgnoreCase("TC")){
                ContainerAnual.setBackgroundColor(getResources().getColor(R.color.White));
                ContainerTD.setBackgroundColor(getResources().getColor(R.color.White));
                ContainerTC.setBackgroundColor(getResources().getColor( R.color.alternative_background));
            }else if(tipoVenta.equalsIgnoreCase("TD")){
                ContainerAnual.setBackgroundColor(getResources().getColor( R.color.White));
                ContainerTD.setBackgroundColor(getResources().getColor( R.color.alternative_background));
                ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
            }else{
                if(rbIndividual.isChecked()){
                    ContainerAnual.setBackgroundColor(getResources().getColor(R.color.alternative_background));
                    ContainerTD.setBackgroundColor(getResources().getColor( R.color.White));
                    ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
                }
            }
        }else{
            ContainerAnual.setBackgroundColor(getResources().getColor( R.color.White));
            ContainerTD.setBackgroundColor(getResources().getColor( R.color.White));
            ContainerTC.setBackgroundColor(getResources().getColor( R.color.White));
        }

    }

    private void refreshTvAfiliado(){
        for(int i = 0;i<afiliadosLayouts.size();i++){
            LinearLayout afiliado = afiliadosLayouts.get(i);
            int j = i +1;
            ((TextView) afiliado.findViewById(R.id.afiliado_numero_movil)).setText("AFILIADO # " + j);
        }
    }

    //endregion

    //region Instance

    public static CotizacionFragment newInstance(CotizacionVisita list){
        CotizacionFragment dialog = new CotizacionFragment();
        dialog.setCotizacionList(list);
        return dialog;
    }

    private void setCotizacionList(CotizacionVisita list){
        this.cotizacionVisita = list;
    }

    //endregion

    //region Ciclo de vida

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    //endregion
}
