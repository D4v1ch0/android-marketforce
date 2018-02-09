package rp3.auna.actividades;

import rp3.auna.R;
import rp3.auna.models.Actividad;
import rp3.auna.models.AgendaTarea;
import rp3.auna.models.AgendaTareaActividades;
import rp3.util.StringUtils;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class TextoActivity extends ActividadActivity {

    private static final String TAG = TextoActivity.class.getSimpleName();
	Actividad ata;
	private AgendaTareaActividades act;
    boolean actSinGrupo;
    private final int REQ_CODE_SPEECH_INPUT = 100;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate...");
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
        if(id_agenda != 0)
	        act = AgendaTareaActividades.getActividadSimple(getDataBase(), id_ruta, id_agenda, id_tarea, ata.getIdTareaActividad());
        else
            act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), id_agenda_int, id_tarea, ata.getIdTareaActividad());
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
        Log.d(TAG,"promptSpeechInput...");
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
        Log.d(TAG,"onActivityResult...");
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
        Log.d(TAG,"aceptarCambios...");
        if(getTextViewString(R.id.actividad_texto_respuesta).equalsIgnoreCase(""))
            act.setResultado(" ");
        else
		    act.setResultado(getTextViewString(R.id.actividad_texto_respuesta).trim());
        //act.setIdsResultado(getTextViewString(R.id.actividad_texto_respuesta));

		if(id_padre == 0)
		{
            if(act.getID() == 0)
                AgendaTareaActividades.insert(getDataBase(), act);
            else
                AgendaTareaActividades.update(getDataBase(), act);
            if(!actSinGrupo) {
                AgendaTarea agt = null;
                if(act.getIdAgenda() != 0)
                    agt = AgendaTarea.getTarea(getDataBase(), act.getIdAgenda(), act.getIdRuta(), ata.getIdTarea());
                else
                    agt = AgendaTarea.getTareaIntern(getDataBase(), act.get_idAgenda(), act.getIdRuta(), ata.getIdTarea());
                agt.setEstadoTarea("R");
                AgendaTarea.update(getDataBase(), agt);
            }
		}
        else
            AgendaTareaActividades.update(getDataBase(), act);
		finish();
		
	}

    /**
     *
     * Ciclo de vida
     *
     */

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

}
