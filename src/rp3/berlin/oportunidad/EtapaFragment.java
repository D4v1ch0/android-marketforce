package rp3.berlin.oportunidad;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.Actividad;
import rp3.berlin.models.oportunidad.Etapa;
import rp3.berlin.models.oportunidad.Oportunidad;
import rp3.berlin.models.oportunidad.OportunidadBitacora;
import rp3.berlin.models.oportunidad.OportunidadEtapa;
import rp3.berlin.models.oportunidad.OportunidadTarea;
import rp3.berlin.oportunidad.actividades.ActividadActivity;
import rp3.berlin.oportunidad.actividades.CheckboxActivity;
import rp3.berlin.oportunidad.actividades.GrupoActivity;
import rp3.berlin.oportunidad.actividades.MultipleActivity;
import rp3.berlin.oportunidad.actividades.SeleccionActivity;
import rp3.berlin.oportunidad.actividades.TextoActivity;
import rp3.berlin.sync.SyncAdapter;
import rp3.util.CalendarUtils;
import rp3.util.ConnectionUtils;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 01/06/2015.
 */
public class EtapaFragment extends BaseFragment {
    public final static String ARG_ETAPA = "etapa";
    public final static String ARG_OPORTUNIDAD = "oportunidad";
    public final static String ARG_ID_AGENDA = "id_agenda";
    public final static int REQ_CODE_SPEECH_INPUT = 1200;

    public static final String ARG_ITEM_ID = "idagenda";

    private int idEtapa,idAgenda;
    private long idOportunidad;
    private List<OportunidadTarea> tareas;
    private SubEtapaAdapter adapter;
    private Oportunidad opt;
    private boolean esActiva;
    SimpleDateFormat format1 = new SimpleDateFormat("dd");
    SimpleDateFormat format2 = new SimpleDateFormat("MM");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy");

    public static EtapaFragment newInstance(int idEtapa, long idOportunidad, int idAgenda) {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_ETAPA, idEtapa);
        arguments.putLong(ARG_OPORTUNIDAD, idOportunidad);
        arguments.putInt(ARG_ID_AGENDA, idAgenda);
        EtapaFragment fragment = new EtapaFragment();
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
        if (getArguments().containsKey(ARG_ID_AGENDA)) {
            idAgenda = getArguments().getInt(ARG_ID_AGENDA);
        }

        if (idEtapa != 0) {
            super.setContentView(R.layout.fragment_etapa_subetapas);
        } else {
            super.setContentView(R.layout.base_content_no_selected_item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final Etapa etapa = Etapa.getEtapaById(getDataBase(), idEtapa);

        ((TextView)getRootView().findViewById(R.id.etapa_descripcion)).setText(etapa.getDescripcion());

        opt = Oportunidad.getOportunidadId(getDataBase(), idOportunidad);
        getActivity().setTitle(opt.getDescripcion());
        OportunidadEtapa oportunidadEtapa = OportunidadEtapa.getEtapaOportunidad(getDataBase(), opt.getIdOportunidad(), idEtapa);
        if(oportunidadEtapa.getObservacion() != null && !oportunidadEtapa.getObservacion().equalsIgnoreCase("null"))
            ((EditText) getRootView().findViewById(R.id.obs_etapa)).setText(oportunidadEtapa.getObservacion());
        opt.setPendiente(true);
        Oportunidad.update(getDataBase(), opt);

        if(etapa.getIdEtapa() != opt.getIdEtapa() || idAgenda == 0) {
            getRootView().findViewById(R.id.finalizar_etapa).setVisibility(View.GONE);
            esActiva = false;
        }
        else
            esActiva = true;

        tareas = new ArrayList<OportunidadTarea>();
        List<OportunidadTarea> subTareas = OportunidadTarea.getTareasOportunidadByEtapa(getDataBase(), opt.getIdOportunidad(), idEtapa);
        if(subTareas.size() > 0) {
            OportunidadTarea titleTarea = new OportunidadTarea();
            titleTarea.setObservacion("Tareas");
            tareas.add(titleTarea);
            tareas.addAll(subTareas);
        }
        List<OportunidadEtapa> oportunidadEtapas = OportunidadEtapa.getEtapaOportunidadHijas(getDataBase(), idOportunidad, idEtapa);
        if(oportunidadEtapas.size() == 0)
            oportunidadEtapas = OportunidadEtapa.getEtapaOportunidadHijasExt(getDataBase(), opt.getIdOportunidad(), idEtapa);

        if(oportunidadEtapas.size() > 0)
        {
            OportunidadTarea titleSubEtapa = new OportunidadTarea();
            titleSubEtapa.setObservacion("Sub Etapas");
            tareas.add(titleSubEtapa);
        }
        else
        {
            getRootView().findViewById(R.id.scrollView2).setVisibility(View.GONE);
        }
        int position = 0;
        long totalDias = 0;
        Calendar ant = Calendar.getInstance();
        ant.setTime(opt.getFechaCreacion());
        for(OportunidadEtapa etp : oportunidadEtapas) {
            OportunidadTarea subEtapa = new OportunidadTarea();
            subEtapa.setObservacion(etp.getEtapa().getDescripcion());
            subEtapa.setIdEtapa(etp.getIdEtapa());
            subEtapa.setEstado(etp.getEstado());
            subEtapa.setIdTarea(0);
            tareas.add(subEtapa);
            switch (position) {
                case 0:
                    getRootView().findViewById(R.id.etapa1_layout).setVisibility(View.VISIBLE);
                    if (etp.getEstado().equalsIgnoreCase("R")) {
                        ((TextView) getRootView().findViewById(R.id.etapa1_fecha)).setText(format1.format(etp.getFechaFin()) + "/" + format2.format(etp.getFechaFin()) + "/" + format3.format(etp.getFechaFin()));
                        Calendar thisDay = Calendar.getInstance();
                        thisDay.setTime(etp.getFechaFin());
                        ((TextView) getRootView().findViewById(R.id.etapa1_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                        ((ImageView) getRootView().findViewById(R.id.etapa1_indicator)).setImageResource(R.drawable.timeline1);
                        totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                    }
                    break;
                case 1:
                    getRootView().findViewById(R.id.etapa2_layout).setVisibility(View.VISIBLE);
                    if (etp.getEstado().equalsIgnoreCase("R")) {
                        ((TextView) getRootView().findViewById(R.id.etapa2_fecha)).setText(format1.format(etp.getFechaFin()) + "/" + format2.format(etp.getFechaFin()) + "/" + format3.format(etp.getFechaFin()));
                        Calendar thisDay = Calendar.getInstance();
                        thisDay.setTime(etp.getFechaFin());
                        ((TextView) getRootView().findViewById(R.id.etapa2_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                        ((ImageView) getRootView().findViewById(R.id.etapa2_indicator)).setImageResource(R.drawable.timeline2);
                        totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                    }
                    break;
                case 2:
                    getRootView().findViewById(R.id.etapa3_layout).setVisibility(View.VISIBLE);
                    if (etp.getEstado().equalsIgnoreCase("R")) {
                        ((TextView) getRootView().findViewById(R.id.etapa3_fecha)).setText(format1.format(etp.getFechaFin()) + "/" + format2.format(etp.getFechaFin()) + "/" + format3.format(etp.getFechaFin()));
                        Calendar thisDay = Calendar.getInstance();
                        thisDay.setTime(etp.getFechaFin());
                        ((TextView) getRootView().findViewById(R.id.etapa3_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                        ((ImageView) getRootView().findViewById(R.id.etapa3_indicator)).setImageResource(R.drawable.timeline3);
                        totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                    }
                    break;
                case 3:
                    getRootView().findViewById(R.id.etapa4_layout).setVisibility(View.VISIBLE);
                    if (etp.getEstado().equalsIgnoreCase("R")) {
                        ((TextView) getRootView().findViewById(R.id.etapa4_fecha)).setText(format1.format(etp.getFechaFin()) + "/" + format2.format(etp.getFechaFin()) + "/" + format3.format(etp.getFechaFin()));
                        Calendar thisDay = Calendar.getInstance();
                        thisDay.setTime(etp.getFechaFin());
                        ((TextView) getRootView().findViewById(R.id.etapa4_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                        ((ImageView) getRootView().findViewById(R.id.etapa4_indicator)).setImageResource(R.drawable.timeline4);
                        totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                    }
                    break;
                case 4:
                    getRootView().findViewById(R.id.etapa5_layout).setVisibility(View.VISIBLE);
                    if (etp.getEstado().equalsIgnoreCase("R")) {
                        ((TextView) getRootView().findViewById(R.id.etapa5_fecha)).setText(format1.format(etp.getFechaFin()) + "/" + format2.format(etp.getFechaFin()) + "/" + format3.format(etp.getFechaFin()));
                        Calendar thisDay = Calendar.getInstance();
                        thisDay.setTime(etp.getFechaFin());
                        ((TextView) getRootView().findViewById(R.id.etapa5_dias)).setText(CalendarUtils.DayDiff(thisDay, ant) + " Días");
                        ((ImageView) getRootView().findViewById(R.id.etapa5_indicator)).setImageResource(R.drawable.timeline5);
                        totalDias = totalDias + CalendarUtils.DayDiff(thisDay, ant);
                    }
                    break;
            }
            position++;
        }
        switch (position)
        {
            case 1: getRootView().findViewById(R.id.etapa1_conector).setVisibility(View.INVISIBLE); break;
            case 2: getRootView().findViewById(R.id.etapa2_conector).setVisibility(View.INVISIBLE); break;
            case 3: getRootView().findViewById(R.id.etapa3_conector).setVisibility(View.INVISIBLE); break;
            case 4: getRootView().findViewById(R.id.etapa4_conector).setVisibility(View.INVISIBLE); break;
        }
        adapter = new SubEtapaAdapter(getContext(), tareas);
        ((ListView) getRootView().findViewById(R.id.list_tareas)).setAdapter(adapter);
        ((ListView) getRootView().findViewById(R.id.list_tareas)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OportunidadTarea setter = adapter.getItem(i);
                if(setter.getIdTarea() != 0) {
                    if (setter.getTarea().getTipoTarea().equalsIgnoreCase("CO"))
                        showTareaGrupo(setter);
                }
                else
                {
                    Intent intent = new Intent(getContext(), EtapaTareasActivity.class);
                    intent.putExtra(EtapaTareasActivity.ARG_ETAPA, setter.getIdEtapa());
                    intent.putExtra(EtapaTareasActivity.ARG_OPORTUNIDAD, idOportunidad);
                    intent.putExtra(EtapaTareasActivity.ARG_TEXT, etapa.getDescripcion());
                    startActivity(intent);
                }
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

                Oportunidad.update(getDataBase(), opt);
                OportunidadEtapa oportunidadEtapa = OportunidadEtapa.getEtapaOportunidad(getDataBase(), opt.getIdOportunidad(), idEtapa);
                if(((EditText)getRootView().findViewById(R.id.obs_etapa)).length() == 0)
                    oportunidadEtapa.setObservacion("");
                else
                    oportunidadEtapa.setObservacion(((EditText) getRootView().findViewById(R.id.obs_etapa)).getText().toString());

                oportunidadEtapa.setFechaFin(Calendar.getInstance().getTime());
                oportunidadEtapa.setEstado("R");
                oportunidadEtapa.setIdAgenda(idAgenda);
                OportunidadEtapa.update(getDataBase(), oportunidadEtapa);

                Etapa next = Etapa.getEtapaNext(getDataBase(), etapa.getOrden() + 1, opt.getIdOportunidadTipo());

                //Se ingresa a log de oportunidad
                OportunidadBitacora bitacora = new OportunidadBitacora();
                bitacora.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));
                bitacora.setFecha(Calendar.getInstance().getTime());
                bitacora.setIdOportunidad(opt.getIdOportunidad());
                bitacora.set_idOportunidad((int) opt.getID());
                bitacora.setIdAgenda(idAgenda);
                bitacora.setDetalle("Se finalizó etapa " + etapa.getOrden() + ": " + etapa.getDescripcion());
                bitacora.setIdOportunidadBitacora(opt.getOportunidadBitacoras().size() + 1);
                OportunidadBitacora.insert(getDataBase(), bitacora);

                if(next.getID() != 0 && etapa.getOrden() < 5)
                {
                    opt.setIdEtapa(next.getIdEtapa());
                    OportunidadEtapa oportunidadEtapaNext = OportunidadEtapa.getEtapaOportunidad(getDataBase(), opt.getIdOportunidad(), next.getIdEtapa());
                    oportunidadEtapaNext.setFechaInicio(Calendar.getInstance().getTime());
                    OportunidadEtapa.update(getDataBase(), oportunidadEtapaNext);
                    for(Etapa etp : oportunidadEtapaNext.getEtapa().getSubEtapas())
                    {
                        OportunidadEtapa subEtapa = OportunidadEtapa.getEtapaOportunidad(getDataBase(), opt.getIdOportunidad(), etp.getIdEtapa());
                        subEtapa.setFechaInicio(Calendar.getInstance().getTime());
                        OportunidadEtapa.update(getDataBase(), subEtapa);
                    }
                }
                else
                {
                    opt.setEstado("C");
                }
                opt.setFechaUltimaGestion(Calendar.getInstance().getTime());
                Oportunidad.update(getDataBase(), opt);
                if(ConnectionUtils.isNetAvailable(getActivity())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PENDIENTES_OPORTUNIDADES);
                    requestSync(bundle);
                }
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
                        OportunidadEtapa oportunidadEtapa = OportunidadEtapa.getEtapaOportunidad(getDataBase(), opt.getIdOportunidad(), idEtapa);
                        oportunidadEtapa.setObservacion(StringUtils.getStringCapSentence(result.get(0)));
                        ((EditText)getRootView().findViewById(R.id.obs_etapa)).setText(StringUtils.getStringCapSentence(result.get(0)));
                        OportunidadEtapa.update(getDataBase(), oportunidadEtapa);
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
        intent.putExtra(ActividadActivity.ARG_VISTA, !opt.getEstado().equalsIgnoreCase("A") || !esActiva);
        intent.putExtra(ActividadActivity.ARG_TITULO, agt.getTarea().getNombreTarea());
        startActivity(intent);
    }
}
