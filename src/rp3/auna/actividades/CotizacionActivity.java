package rp3.auna.actividades;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseActivity;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.cliente.CrearClienteFragment;
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

    private LinearLayout ContainerParams, ContainerAnual, ContainerTC, ContainerTD;
    private List<LinearLayout> afiliadosLayouts;
    private View EmptyView, ResponseView;
    private String ultimaConsultaParams;

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
        EmptyView = findViewById(R.id.cotizacion_resp_empty);
        ResponseView = findViewById(R.id.cotizacion_resp_scroll);
        afiliadosLayouts = new ArrayList<>();

        //Configuro boton para agregar afiliado
        findViewById(R.id.agregar_afiliado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout afiliado = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_afiliado, null);
                final int pos = afiliadosLayouts.size() + 1;
                ((TextView) afiliado.findViewById(R.id.afiliado_numero)).setText("AFILIADO # " + pos);
                ((Button) afiliado.findViewById(R.id.eliminar_afiliado)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        afiliadosLayouts.remove(afiliado);
                        ContainerParams.removeView(afiliado);
                    }
                });

                SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getApplicationContext(), getDataBase(), rp3.auna.Contants.GENERAL_TABLE_GENERO);
                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).setAdapter(tipoGeneroAdapter);
                ((Spinner) afiliado.findViewById(R.id.afiliado_sexo)).setPrompt("Seleccione un género");

                ContainerParams.addView(afiliado);
                afiliadosLayouts.add(afiliado);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:
                if(ValidarConsulta())
                {
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
                Toast.makeText(CotizacionActivity.this, "Debe ingresar al menos un afiliado", Toast.LENGTH_SHORT).show();
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
                ultimaConsultaParams = generaJson();
                for(int i = 0; i < afiliadosLayouts.size(); i++)
                {
                    LinearLayout afiliado = afiliadosLayouts.get(i);
                    LinearLayout AfiliadoAnual = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_afiliado_cotizacion, null);
                    LinearLayout AfiliadoTC = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_afiliado_cotizacion, null);
                    LinearLayout AfiliadoTD = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_afiliado_cotizacion, null);

                    ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_numero)).setText("AFILIADO # " + (i+1));
                    ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_numero)).setText("AFILIADO # " + (i+1));
                    ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_numero)).setText("AFILIADO # " + (i+1));

                    ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_edad)).setText(((TextView) afiliado.findViewById(R.id.afiliado_edad)).getText().toString() + " años");
                    ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_edad)).setText(((TextView) afiliado.findViewById(R.id.afiliado_edad)).getText().toString() + " años");
                    ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_edad)).setText(((TextView) afiliado.findViewById(R.id.afiliado_edad)).getText().toString() + " años");

                    ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_sexo)).setText(((GeneralValue)((Spinner)afiliado.findViewById(R.id.afiliado_sexo)).getSelectedItem()).getValue());
                    ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_sexo)).setText(((GeneralValue)((Spinner)afiliado.findViewById(R.id.afiliado_sexo)).getSelectedItem()).getValue());
                    ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_sexo)).setText(((GeneralValue)((Spinner)afiliado.findViewById(R.id.afiliado_sexo)).getSelectedItem()).getValue());

                    ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_fumador)).setText(((CheckBox) afiliado.findViewById(R.id.afiliado_es_fumador)).isChecked() ? "Es fumador" : "No es fumador");
                    ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_fumador)).setText(((CheckBox) afiliado.findViewById(R.id.afiliado_es_fumador)).isChecked() ? "Es fumador" : "No es fumador");
                    ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_fumador)).setText(((CheckBox) afiliado.findViewById(R.id.afiliado_es_fumador)).isChecked() ? "Es fumador" : "No es fumador");

                    ((TextView) AfiliadoAnual.findViewById(R.id.afiliado_cotizacion_precio)).setText("S/. " + (i+1));
                    ((TextView) AfiliadoTC.findViewById(R.id.afiliado_cotizacion_precio)).setText("S/. " + (i+1));
                    ((TextView) AfiliadoTD.findViewById(R.id.afiliado_cotizacion_precio)).setText("S/. " + (i+1));
                }
            }
        }
    }

    @Override
    public void aceptarCambios(View v) {

    }
}
