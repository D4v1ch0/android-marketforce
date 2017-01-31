package rp3.auna.oportunidad.actividades;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import rp3.auna.R;
import rp3.auna.models.Actividad;
import rp3.auna.models.oportunidad.OportunidadTarea;
import rp3.auna.models.oportunidad.OportunidadTareaActividad;

public class TextoActivity extends ActividadActivity {

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
            setContentView(R.layout.layout_texto_activitidad);
        else
	        setContentView(R.layout.layout_texto_activitidad, R.menu.fragment_crear_cliente);
	    
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
		
		if(soloVista)
		{
			((TextView) findViewById(R.id.actividad_texto_respuesta)).setEnabled(false);
            ((TextView) findViewById(R.id.actividad_texto_respuesta)).setFocusable(false);
            ((TextView) findViewById(R.id.actividad_texto_respuesta)).setFocusableInTouchMode(false);
            findViewById(R.id.actividad_aceptar).setVisibility(View.GONE);
            findViewById(R.id.actividad_cancelar).setVisibility(View.GONE);
            findViewById(R.id.actividad_voice_to_text).setVisibility(View.GONE);
		}

        ((ImageView) findViewById(R.id.actividad_voice_to_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });


	    
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
                    setTextViewText(R.id.actividad_texto_respuesta, result.get(0));
                }
                break;
            }

        }
    }

	@Override
	public void aceptarCambios(View v) {
        if(getTextViewString(R.id.actividad_texto_respuesta).equalsIgnoreCase(""))
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

}
