package rp3.auna.oportunidad.actividades;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import rp3.auna.R;
import rp3.auna.models.Actividad;
import rp3.auna.models.oportunidad.OportunidadTarea;
import rp3.auna.models.oportunidad.OportunidadTareaActividad;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 12/11/2015.
 */
public class NumericoActivity extends ActividadActivity {

    Actividad ata;
    private OportunidadTareaActividad act;
    boolean actSinGrupo;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        int numero = getIntent().getExtras().getInt(ARG_NUMERO, 1);
        int tema = getIntent().getExtras().getInt(ARG_THEME, R.style.MyAppTheme);
        setTheme(tema);
        actSinGrupo = getIntent().getExtras().getBoolean(ARG_SIN_GRUPO, false);
        if(tema != R.style.MyAppTheme)
        {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        if(soloVista)
            setContentView(R.layout.layout_numerico_activity);
        else
            setContentView(R.layout.layout_numerico_activity, R.menu.fragment_crear_cliente);

        if(id_padre == 0)
        {
            if(actSinGrupo)
                ata = Actividad.getActividadSimple(getDataBase(), id_tarea, id_actividad);
            else {
                ata = Actividad.getActividadSimple(getDataBase(), id_actividad);
                findViewById(R.id.actividad_aceptar).setVisibility(View.GONE);
                findViewById(R.id.actividad_cancelar).setVisibility(View.GONE);
            }
        }
        else
        {
            ata = Actividad.getActividadSimple(getDataBase(), id_tarea, id_actividad);
        }

        setTextViewText(R.id.label_pregunta_actividad, ata.getDescripcion());
        setTextViewText(R.id.detail_activity_number, numero + "");
        if(id_oportunidad != 0)
            act = OportunidadTareaActividad.getActividadSimple(getDataBase(), id_etapa, id_oportunidad, id_tarea, ata.getIdTareaActividad());

        if(act != null)
        {
            if(act.getResultado() != null && !act.getResultado().equalsIgnoreCase("null"))
            {
                setTextViewText(R.id.actividad_texto_respuesta, act.getResultado());
            }
        }
        else
        {
            if(id_padre == 0)
                act = initActividad(ata.getIdTareaActividad());
            else
                act = initActividadInsert(ata.getIdTareaActividad());
        }

        if(ata.getLimite() != 0)
            ((EditText) findViewById(R.id.actividad_texto_respuesta)).setFilters(new InputFilter[] {new InputFilter.LengthFilter(ata.getLimite())});

        if(soloVista)
        {
            ((TextView) findViewById(R.id.actividad_texto_respuesta)).setEnabled(false);
            ((TextView) findViewById(R.id.actividad_texto_respuesta)).setFocusable(false);
            ((TextView) findViewById(R.id.actividad_texto_respuesta)).setFocusableInTouchMode(false);
            findViewById(R.id.actividad_aceptar).setVisibility(View.GONE);
            findViewById(R.id.actividad_cancelar).setVisibility(View.GONE);
            //findViewById(R.id.actividad_voice_to_text).setVisibility(View.GONE);
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable Ahora");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    setTextViewText(R.id.actividad_texto_respuesta, StringUtils.getStringCapSentence(result.get(0)));
                }
                break;
            }

        }
    }

    @Override
    public void aceptarCambios(View v) {
        if(ata.getLimite() == 0 || getTextViewString(R.id.actividad_texto_respuesta).trim().equalsIgnoreCase("") || getTextViewString(R.id.actividad_texto_respuesta).length() == ata.getLimite()) {
            if (getTextViewString(R.id.actividad_texto_respuesta).equalsIgnoreCase(""))
                act.setResultado(" ");
            else
                act.setResultado(getTextViewString(R.id.actividad_texto_respuesta).trim());
            //act.setIdsResultado(getTextViewString(R.id.actividad_texto_respuesta));

            if(id_padre == 0)
            {
                if(act.getID() == 0)
                    OportunidadTareaActividad.insert(getDataBase(), act);
                else
                    OportunidadTareaActividad.update(getDataBase(), act);
                if(!actSinGrupo) {
                    OportunidadTarea agt = null;
                    if(act.getIdOportunidad() != 0)
                        agt = OportunidadTarea.getTarea(getDataBase(), act.getIdOportunidad(), act.getIdEtapa(), ata.getIdTarea());

                    agt.setEstado("R");
                    OportunidadTarea.update(getDataBase(), agt);
                }
            }
            else
                OportunidadTareaActividad.update(getDataBase(), act);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Debe de ingresar " + ata.getLimite() + " número(s).",
                    Toast.LENGTH_SHORT).show();
        }

    }

}