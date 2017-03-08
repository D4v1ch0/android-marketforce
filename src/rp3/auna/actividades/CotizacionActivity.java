package rp3.auna.actividades;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseActivity;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.cliente.CrearClienteFragment;
import rp3.auna.models.Agenda;
import rp3.auna.models.AgendaTarea;
import rp3.auna.models.auna.Cotizacion;
import rp3.auna.sync.SyncAdapter;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;

/**
 * Created by magno_000 on 27/01/2017.
 */
public class CotizacionActivity extends ActividadActivity {
    public static final String ARG_PARAMS = "params";
    public static final String ARG_RESPONSE = "response";

    private LinearLayout ContainerParams, ContainerAnual, ContainerTC, ContainerTD, SelectAnual, SelectTC, SelectTD;
    private List<LinearLayout> afiliadosLayouts;
    private View EmptyView, ResponseView;
    private String ultimaConsultaParams, ultimoResponse;
    private TextView TotalAnual, TotalTC, TotalTD;
    private Cotizacion cotizacion;
    private int select = -1;
    private double totalAnual = 0;
    private double totalTC = 0;
    private double totalTD = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(soloVista)
            setContentView(R.layout.fragment_cotizacion);
        else
            setContentView(R.layout.fragment_cotizacion, R.menu.activity_cotizacion);

        //Se instancian los contenedores y vistas
        ContainerParams = (LinearLayout) findViewById(R.id.cotizacion_param_content);
        ContainerAnual = (LinearLayout) findViewById(R.id.cotizacion_resp_anual_content);
        ContainerTC = (LinearLayout) findViewById(R.id.cotizacion_resp_credito_content);
        ContainerTD = (LinearLayout) findViewById(R.id.cotizacion_resp_debito_content);
        SelectAnual = (LinearLayout) findViewById(R.id.cotizacion_select_anual);
        SelectTC = (LinearLayout) findViewById(R.id.cotizacion_select_tc);
        SelectTD = (LinearLayout) findViewById(R.id.cotizacion_select_td);
        EmptyView = findViewById(R.id.cotizacion_resp_empty);
        ResponseView = findViewById(R.id.cotizacion_resp_scroll);
        afiliadosLayouts = new ArrayList<>();
        TotalAnual=(TextView)ResponseView.findViewById(R.id.cotizacion_anual_total);
        TotalTC=(TextView)ResponseView.findViewById(R.id.cotizacion_mensual_credito);
        TotalTD=(TextView)ResponseView.findViewById(R.id.cotizacion_mensual_debito);

        //Cargo cotización en el caso de que exista
        cotizacion = Cotizacion.getCotizacion(getDataBase(), id_agenda, id_ruta, id_tarea);
        if(cotizacion.getID() != 0)
            cargaCotizacion();
        else
        {
            cotizacion = Cotizacion.getCotizacionInt(getDataBase(), id_agenda_int, id_ruta, id_tarea);
            if(cotizacion.getID() != 0)
                cargaCotizacion();
        }

        //Configuro boton para agregar afiliado
        findViewById(R.id.agregar_afiliado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout afiliado = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_afiliado, null);
                final int pos = afiliadosLayouts.size() + 1;
                ((TextView) afiliado.findViewById(R.id.afiliado_numero)).setText("AFILIADO # " + pos);
                ((Button) afiliado.findViewById(R.id.eliminar_afiliado)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        afiliadosLayouts.remove(afiliado);
                        ContainerParams.removeView(afiliado);
                    }
                });

                SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getBaseContext(), getDataBase(), rp3.auna.Contants.GENERAL_TABLE_GENERO);
                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).setAdapter(tipoGeneroAdapter);
                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).setPrompt("Seleccione un género");

                ContainerParams.addView(afiliado);
                afiliadosLayouts.add(afiliado);
            }
        });

        SelectAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 1;
                SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_grey_lines));
                SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                SelectTD.setBackgroundColor(getResources().getColor(R.color.color_background_white));
            }
        });
        SelectTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 2;
                SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                SelectTC.setBackgroundColor(getResources().getColor(R.color.color_grey_lines));
                SelectTD.setBackgroundColor(getResources().getColor(R.color.color_background_white));
            }
        });
        SelectTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 3;
                SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                SelectTD.setBackgroundColor(getResources().getColor(R.color.color_grey_lines));
            }
        });
    }

    private void cargaCotizacion() {
        ultimaConsultaParams = cotizacion.getParametros();
        ultimoResponse = cotizacion.getResponse();
        select = cotizacion.getOpcion();

        //Cargo afiliados
        try {
            JSONArray jsonArray = new JSONArray(ultimaConsultaParams);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final LinearLayout afiliado = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_afiliado, null);
                final int pos = afiliadosLayouts.size() + 1;
                ((TextView) afiliado.findViewById(R.id.afiliado_numero)).setText("AFILIADO # " + pos);
                ((Button) afiliado.findViewById(R.id.eliminar_afiliado)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        afiliadosLayouts.remove(afiliado);
                        ContainerParams.removeView(afiliado);
                    }
                });

                SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getBaseContext(), getDataBase(), rp3.auna.Contants.GENERAL_TABLE_GENERO);
                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).setAdapter(tipoGeneroAdapter);
                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).setPrompt("Seleccione un género");

                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).setSelection(jsonObject.getString("Sexo").equalsIgnoreCase("M") ? 1 : 0);
                ((CheckBox) afiliado.findViewById(R.id.afiliado_es_fumador)).setChecked(jsonObject.getBoolean("FlagFumador"));
                ((EditText) afiliado.findViewById(R.id.afiliado_edad)).setText(jsonObject.getString("Edad"));

                ContainerParams.addView(afiliado);
                afiliadosLayouts.add(afiliado);
            }
        } catch (Exception ex) {

        }

        CargaResponse();

        switch (cotizacion.getOpcion()) {
            case 1:
                SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_grey_lines));
                SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                SelectTD.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                break;
            case 2:
                SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                SelectTC.setBackgroundColor(getResources().getColor(R.color.color_grey_lines));
                SelectTD.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                break;
            case 3:
                SelectAnual.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                SelectTC.setBackgroundColor(getResources().getColor(R.color.color_background_white));
                SelectTD.setBackgroundColor(getResources().getColor(R.color.color_grey_lines));
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:
                if(ValidarConsulta())
                {
                    Grabar();
                    aceptarCambios(new View(this));
                    finish();
                }
                break;
            case R.id.action_cancel:
                finish();
                break;
            case R.id.action_consultar:
                if(ValidarParams())
                {
                    consultaContizacion();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean ValidarConsulta() {
        if(select == -1) {
            Toast.makeText(CotizacionActivity.this, "Debe seleccionar un programa.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ultimaConsultaParams.equalsIgnoreCase(generaJson()))
            return true;
        else
        {
            Toast.makeText(CotizacionActivity.this, "Afiliados ingresados no concuerdan con ultima cotización ingresado. Por favor vuelva a consultar.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void consultaContizacion() {
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CONSULTA_COTIZACION);
        bundle.putString(ARG_PARAMS, generaJson());
        requestSync(bundle);
        showDialogProgress(R.string.message_title_connecting, R.string.message_generando_cotizacion);
    }

    private String generaJson()
    {
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < afiliadosLayouts.size(); i++)
        {
            LinearLayout afiliado = afiliadosLayouts.get(i);
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("CodigoProcedenciaBase", Contants.COTIZADOR_PROCEDENCIA_BASE);
                jsonObject.put("CodigoPrograma", Contants.COTIZADOR_CODIGO_PROGRAMA);
                jsonObject.put("Edad", ((TextView) afiliado.findViewById(R.id.afiliado_edad)).getText().toString());
                jsonObject.put("FlagFumador", ((CheckBox) afiliado.findViewById(R.id.afiliado_es_fumador)).isChecked());
                jsonObject.put("IdRegistro", "00" + (i+1));
                jsonObject.put("OrigenSolicitud", Contants.COTIZADOR_ORIGEN_SOLICITUD);
                jsonObject.put("Sexo", ((GeneralValue)((Spinner)afiliado.findViewById(R.id.afiliado_sexo)).getSelectedItem()).getCode());
            }
            catch (Exception ex)
            {}
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    public boolean ValidarParams()
    {
        if(afiliadosLayouts.size() == 0)
        {
            Toast.makeText(CotizacionActivity.this, "Debe ingresar al menos un afiliado", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i = 0; i < afiliadosLayouts.size(); i++)
        {
            LinearLayout afiliado = afiliadosLayouts.get(i);
            if(((TextView) afiliado.findViewById(R.id.afiliado_edad)).length() <= 0)
            {
                Toast.makeText(CotizacionActivity.this, "El afiliado # "+(i+1)+" no tiene ingresado una edad.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if(data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_CONSULTA_COTIZACION)){
            closeDialogProgress();
            if(messages.hasErrorMessage()){
                showDialogMessage(messages);
            }else{
                ContainerAnual.removeAllViews();
                ContainerTD.removeAllViews();
                ContainerTC.removeAllViews();
                ultimaConsultaParams = generaJson();
                try {
                    ultimoResponse = data.getString(ARG_RESPONSE);
                    CargaResponse();
                }
                catch (Exception ex)
                {

                }

            }
        }
    }

    public void CargaResponse()
    {
        try
        {
            JSONArray jsonArray = new JSONArray(ultimoResponse);
            //Control de errores
            NumberFormat numberFormat;
            numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            numberFormat.setMinimumFractionDigits(2);

            totalAnual = 0;
            totalTC = 0;
            totalTD = 0;
            boolean hayError = false;

            for (int i = 0; i < afiliadosLayouts.size(); i++) {
                LinearLayout afiliado = afiliadosLayouts.get(i);
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.getString("Result").equalsIgnoreCase("1")) {
                    hayError = true;
                    break;
                }
                LinearLayout AfiliadoAnual = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_afiliado_cotizacion, null);
                LinearLayout AfiliadoTC = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_afiliado_cotizacion, null);
                LinearLayout AfiliadoTD = (LinearLayout) LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_afiliado_cotizacion, null);

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_numero)).setText("AFILIADO # " + (i + 1));
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_numero)).setText("AFILIADO # " + (i + 1));
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_numero)).setText("AFILIADO # " + (i + 1));

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_edad)).setText(((TextView) afiliado.findViewById(R.id.afiliado_edad)).getText().toString() + " años");
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_edad)).setText(((TextView) afiliado.findViewById(R.id.afiliado_edad)).getText().toString() + " años");
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_edad)).setText(((TextView) afiliado.findViewById(R.id.afiliado_edad)).getText().toString() + " años");

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_sexo)).setText(((GeneralValue) ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).getSelectedItem()).getValue());
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_sexo)).setText(((GeneralValue) ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).getSelectedItem()).getValue());
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_sexo)).setText(((GeneralValue) ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).getSelectedItem()).getValue());

                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_fumador)).setText(((CheckBox) afiliado.findViewById(R.id.afiliado_es_fumador)).isChecked() ? "Es fumador" : "No es fumador");
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_fumador)).setText(((CheckBox) afiliado.findViewById(R.id.afiliado_es_fumador)).isChecked() ? "Es fumador" : "No es fumador");
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_fumador)).setText(((CheckBox) afiliado.findViewById(R.id.afiliado_es_fumador)).isChecked() ? "Es fumador" : "No es fumador");




                ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_precio)).setText("S/. " + numberFormat.format(Double.parseDouble(jsonObject.getString("Anual"))));
                ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_precio)).setText("S/. " + numberFormat.format(Double.parseDouble(jsonObject.getString("TC"))));
                ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_precio)).setText("S/. " + numberFormat.format(Double.parseDouble(jsonObject.getString("TD"))));
                totalAnual = totalAnual + Double.parseDouble(jsonObject.getString("Anual"));
                totalTC = totalTC + Double.parseDouble(jsonObject.getString("TC"));
                totalTD = totalTD + Double.parseDouble(jsonObject.getString("TD"));

                ContainerAnual.addView(AfiliadoAnual);
                ContainerTC.addView(AfiliadoTC);
                ContainerTD.addView(AfiliadoTD);
            }

            if (!hayError) {
                TotalAnual.setText("S/. " + numberFormat.format(totalAnual)+" anual");
                TotalTC.setText("S/. " + numberFormat.format(totalTC)+" al mes");
                TotalTD.setText("S/. " + numberFormat.format(totalTD)+" al mes");
                //Mostrar resultado de cotizacion
                ResponseView.setVisibility(View.VISIBLE);
                EmptyView.setVisibility(View.GONE);
            } else
            {
                //SE DEBE MOSTRAR EL ERROR QUE RETORNA EL SERVICIO
            }
        }
        catch (Exception ex)
        {

        }
    }

    @Override
    public void aceptarCambios(View v) {
        AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), id_agenda, id_ruta, id_actividad);
        agt.setEstadoTarea("R");
        AgendaTarea.update(getDataBase(), agt);
        finish();
    }

    public void Grabar()
    {
        if(cotizacion.getID() == 0)
        {
            Cotizacion cot = new Cotizacion();
            cot.setIdAgenda(id_agenda);
            cot.set_idAgenda(id_agenda_int);
            cot.setIdRuta(id_ruta);
            cot.setIdTarea(id_tarea);
            cot.setParametros(ultimaConsultaParams);
            cot.setResponse(ultimoResponse);
            cot.setOpcion(select);
            switch (select)
            {
                case 1:
                    cot.setValor(totalAnual);
                    break;
                case 2:
                    cot.setValor(totalTC);
                    break;
                case 3:
                    cot.setValor(totalTD);
                    break;
            }
            Cotizacion.insert(getDataBase(), cot);
        }
        else
        {
            cotizacion.setParametros(ultimaConsultaParams);
            cotizacion.setResponse(ultimoResponse);
            cotizacion.setOpcion(select);
            cotizacion.set_idAgenda(id_agenda_int);
            switch (select)
            {
                case 1:
                    cotizacion.setValor(totalAnual);
                    break;
                case 2:
                    cotizacion.setValor(totalTC);
                    break;
                case 3:
                    cotizacion.setValor(totalTD);
                    break;
            }
            Cotizacion.update(getDataBase(),cotizacion);
        }

    }
}
