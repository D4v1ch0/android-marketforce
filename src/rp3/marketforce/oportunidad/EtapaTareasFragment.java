package rp3.marketforce.oportunidad;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.oportunidad.Etapa;
import rp3.marketforce.models.oportunidad.EtapaTarea;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadTarea;
import rp3.marketforce.oportunidad.actividades.ActividadActivity;
import rp3.marketforce.oportunidad.actividades.CheckboxActivity;
import rp3.marketforce.oportunidad.actividades.GrupoActivity;
import rp3.marketforce.oportunidad.actividades.MultipleActivity;
import rp3.marketforce.oportunidad.actividades.SeleccionActivity;
import rp3.marketforce.oportunidad.actividades.TextoActivity;
import rp3.marketforce.utils.Utils;

import static rp3.util.Screen.getOrientation;

/**
 * Created by magno_000 on 18/05/2015.
 */
public class EtapaTareasFragment extends BaseFragment {

    public final static String ARG_ETAPA = "etapa";
    public final static String ARG_OPORTUNIDAD = "oportunidad";
    public final static int REQ_CODE_SPEECH_INPUT = 1200;

    public static final String ARG_ITEM_ID = "idagenda";

    private int idEtapa;
    private long idOportunidad;
    private List<OportunidadTarea> tareas;
    private EtapaTareasAdapter adapter;
    private Oportunidad opt;

    public static EtapaTareasFragment newInstance(int idEtapa, long idOportunidad) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_ETAPA, idEtapa);
        arguments.putLong(ARG_OPORTUNIDAD, idOportunidad);
        EtapaTareasFragment fragment = new EtapaTareasFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getParentFragment() == null)
            setRetainInstance(true);

        if (getArguments().containsKey(ARG_ETAPA)) {
            idEtapa = getArguments().getInt(ARG_ETAPA);
        }
        if (getArguments().containsKey(ARG_OPORTUNIDAD)) {
            idOportunidad = getArguments().getLong(ARG_OPORTUNIDAD);
        }

        if (idEtapa != 0) {
            super.setContentView(R.layout.fragment_etapa_tareas);
        } else {
            super.setContentView(R.layout.base_content_no_selected_item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Etapa etapa = Etapa.getEtapaById(getDataBase(), idEtapa);

        ((TextView)getRootView().findViewById(R.id.etapa_descripcion)).setText(etapa.getDescripcion());

        opt = Oportunidad.getOportunidadId(getDataBase(), idOportunidad);
        opt.setPendiente(true);
        Oportunidad.update(getDataBase(), opt);

        tareas = new ArrayList<OportunidadTarea>();
        for(OportunidadTarea tarea : opt.getOportunidadTareas())
        {
            if(tarea.getIdEtapa() == idEtapa) {
                tareas.add(tarea);
            }
        }
        adapter = new EtapaTareasAdapter(getContext(), tareas);
        ((ListView) getRootView().findViewById(R.id.list_tareas)).setAdapter(adapter);
        ((ListView) getRootView().findViewById(R.id.list_tareas)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OportunidadTarea setter = adapter.getItem(i);
                if(setter.getTarea().getTipoTarea().equalsIgnoreCase("CO"))
                    showTareaGrupo(setter);
            }
        });

        ((ImageView) getRootView().findViewById(R.id.obs_voice_to_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        ((Button) getRootView().findViewById(R.id.finalizar_etapa)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opt.setIdEtapa(idEtapa+1);
                Oportunidad.update(getDataBase(), opt);
                finish();
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
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT:
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        setTextViewText(R.id.obs_etapa, result.get(0));
                    }
                    break;
            }
        }
    }

    public void showTareaTexto(Actividad ata, OportunidadTarea setter)
    {
        Intent intent = new Intent(getContext(), TextoActivity.class);
        intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
        intent.putExtra(ARG_ETAPA, setter.getIdEtapa());
        intent.putExtra(ARG_OPORTUNIDAD, setter.getIdOportunidad());
        //intent.putExtra(ActividadActivity.ARG_AGENDA_INT, setter.get_idAgenda());
        intent.putExtra(ActividadActivity.ARG_TAREA, setter.getIdTarea());
        intent.putExtra(ActividadActivity.ARG_VISTA, false);
        intent.putExtra(ActividadActivity.ARG_TITULO, setter.getTarea().getNombreTarea());
        startActivity(intent);
    }

    public void showTareaSeleccion(Actividad ata, OportunidadTarea setter)
    {
        Intent intent = new Intent(getContext(), SeleccionActivity.class);
        intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
        intent.putExtra(ARG_ETAPA, setter.getIdEtapa());
        intent.putExtra(ARG_OPORTUNIDAD, setter.getIdOportunidad());
        intent.putExtra(ActividadActivity.ARG_VISTA, false);
        intent.putExtra(ActividadActivity.ARG_TITULO, setter.getTarea().getNombreTarea());
        startActivity(intent);
    }
    public void showTareaMultiSeleccion(Actividad ata, OportunidadTarea setter)
    {
        Intent intent = new Intent(getContext(), MultipleActivity.class);
        intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
        intent.putExtra(ARG_ETAPA, setter.getIdEtapa());
        intent.putExtra(ARG_OPORTUNIDAD, setter.getIdOportunidad());
        intent.putExtra(ActividadActivity.ARG_VISTA, false);
        intent.putExtra(ActividadActivity.ARG_TITULO, setter.getTarea().getNombreTarea());
        startActivity(intent);
    }

    public void showTareaCheckbox(Actividad ata, OportunidadTarea setter)
    {
        Intent intent = new Intent(getContext(), CheckboxActivity.class);
        intent.putExtra(ARG_ITEM_ID, ata.getIdTarea());
        intent.putExtra(ARG_ETAPA, setter.getIdEtapa());
        intent.putExtra(ARG_OPORTUNIDAD, setter.getIdOportunidad());
        intent.putExtra(ActividadActivity.ARG_VISTA, false);
        intent.putExtra(ActividadActivity.ARG_TITULO, setter.getTarea().getNombreTarea());
        startActivity(intent);
    }

    public void showTareaGrupo(OportunidadTarea agt)
    {
        Intent intent = new Intent(getContext(), GrupoActivity.class);
        intent.putExtra(ARG_ITEM_ID, agt.getIdTarea());
        intent.putExtra(ARG_ETAPA, agt.getIdEtapa());
        intent.putExtra(ARG_OPORTUNIDAD, agt.getIdOportunidad());
        intent.putExtra(ActividadActivity.ARG_VISTA, false);
        intent.putExtra(ActividadActivity.ARG_TITULO, agt.getTarea().getNombreTarea());
        startActivity(intent);
    }
}
