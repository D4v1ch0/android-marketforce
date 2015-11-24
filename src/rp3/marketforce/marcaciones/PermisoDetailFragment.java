package rp3.marketforce.marcaciones;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.marcacion.Justificacion;
import rp3.marketforce.models.marcacion.Permiso;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.util.StringUtils;
import rp3.widget.ViewPager;

/**
 * Created by magno_000 on 19/06/2015.
 */
public class PermisoDetailFragment extends BaseFragment {


    public static final String STATE_CLIENT_ID = "clientId";
    private static final String ARG_ITEM_ID = "id";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private long clientId;
    private Justificacion justificacion;
    private SimpleDateFormat format5;
    private PermisoDetailFragmentListener detailFragmentListener;

    public static PermisoDetailFragment newInstance(Justificacion permiso) {
        Bundle arguments = new Bundle();
        arguments.putLong(PermisoDetailFragment.ARG_ITEM_ID, permiso.getID());
        PermisoDetailFragment fragment = new PermisoDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public interface PermisoDetailFragmentListener{
        void onPermisoChanged(Permiso permiso);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        format5 = new SimpleDateFormat("EEEE dd/MM/yy HH:mm");
        if (getParentFragment() == null)
            setRetainInstance(true);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            clientId = getArguments().getLong(ARG_ITEM_ID);
        } else if (savedInstanceState != null) {
            clientId = savedInstanceState.getLong(STATE_CLIENT_ID);
        }

        if (clientId != 0) {
            super.setContentView(R.layout.fragment_permiso_detail);
        } else {
            super.setContentView(R.layout.base_content_no_selected_item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (clientId != 0) {
                justificacion = Justificacion.getPermisoById(getDataBase(), clientId);
        }

        if(justificacion==null) return;
        ((TextView) getRootView().findViewById(R.id.justificacion_agente)).setText(justificacion.getAgente());
        ((TextView) getRootView().findViewById(R.id.justificacion_motivo)).setText(justificacion.getTipoDescripcion());
        if(justificacion.isAusencia()) {
            ((TextView) getRootView().findViewById(R.id.justificacion_tipo)).setText(R.string.label_ausencia_just);
            getRootView().findViewById(R.id.justificacion_layout_jornada).setVisibility(View.GONE);
        }
        else {
            ((TextView) getRootView().findViewById(R.id.justificacion_tipo)).setText(R.string.label_atraso);
            if(justificacion.getJornada().equalsIgnoreCase("J1"))
                ((TextView) getRootView().findViewById(R.id.justificacion_jornada)).setText("Inicio de Jornada");
            else if(justificacion.getJornada().equalsIgnoreCase("J2"))
                ((TextView) getRootView().findViewById(R.id.justificacion_jornada)).setText("Break");
            else if(justificacion.getJornada().equalsIgnoreCase("J3"))
                ((TextView) getRootView().findViewById(R.id.justificacion_jornada)).setText("Inicio 2da Jornada");
            else if(justificacion.getJornada().equalsIgnoreCase("J4"))
                ((TextView) getRootView().findViewById(R.id.justificacion_jornada)).setText("Fin de Jornada");
            else
                ((TextView) getRootView().findViewById(R.id.justificacion_jornada)).setText(justificacion.getJornada());
        }
        if(justificacion.getEstado().equalsIgnoreCase("P"))
            ((TextView) getRootView().findViewById(R.id.justificacion_estado)).setText("Pendiente");
        else {
            getRootView().findViewById(R.id.justificacion_aprobar).setVisibility(View.GONE);
            getRootView().findViewById(R.id.justificacion_rechazar).setVisibility(View.GONE);
            ((TextView) getRootView().findViewById(R.id.justificacion_estado)).setText("Aprobado");
        }
        ((TextView) getRootView().findViewById(R.id.justificacion_obs)).setText(justificacion.getObservacion());
        ((TextView) getRootView().findViewById(R.id.justificacion_fecha)).setText(StringUtils.getStringCapSentence(format5.format(justificacion.getFecha())));
        if(justificacion.getObservacionSupervisor() != null)
            ((TextView) getRootView().findViewById(R.id.justificacion_text)).setText(justificacion.getObservacionSupervisor());
        getRootView().findViewById(R.id.justificacion_aprobar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((TextView) getRootView().findViewById(R.id.justificacion_text)).length() <= 0)
                    Toast.makeText(getContext(), R.string.message_sin_observacion , Toast.LENGTH_LONG).show();
                else {
                    justificacion.setObservacionSupervisor(((TextView) getRootView().findViewById(R.id.justificacion_text)).getText().toString());
                    justificacion.setEstado("A");
                    justificacion.setPendiente(true);
                    Justificacion.update(getDataBase(), justificacion);
                    getRootView().findViewById(R.id.justificacion_aprobar).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.justificacion_rechazar).setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_JUSTIFICACIONES_UPLOAD);
                    requestSync(bundle);
                    ((TextView) getRootView().findViewById(R.id.justificacion_estado)).setText("Aprobado");
                    detailFragmentListener.onPermisoChanged(null);
                    Toast.makeText(getContext(), R.string.message_permiso_aprobado, Toast.LENGTH_LONG).show();
                }
            }
        });
        getRootView().findViewById(R.id.justificacion_rechazar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((TextView) getRootView().findViewById(R.id.justificacion_text)).length() <= 0)
                    Toast.makeText(getContext(), R.string.message_sin_observacion , Toast.LENGTH_LONG).show();
                else {
                    justificacion.setObservacionSupervisor(((TextView) getRootView().findViewById(R.id.justificacion_text)).getText().toString());
                    justificacion.setEstado("R");
                    justificacion.setPendiente(true);
                    Justificacion.update(getDataBase(), justificacion);
                    getRootView().findViewById(R.id.justificacion_aprobar).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.justificacion_rechazar).setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_JUSTIFICACIONES_UPLOAD);
                    requestSync(bundle);
                    ((TextView) getRootView().findViewById(R.id.justificacion_estado)).setText("Rechazado");
                    detailFragmentListener.onPermisoChanged(null);
                    Toast.makeText(getContext(), R.string.message_permiso_rechazado, Toast.LENGTH_LONG).show();
                }
            }
        });

        getRootView().findViewById(R.id.voice_to_text).setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                ((TextView) getRootView().findViewById(R.id.justificacion_text)).setText(StringUtils.getStringCapSentence(result.get(0)));
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getParentFragment()!=null){
            detailFragmentListener = (PermisoDetailFragmentListener)getParentFragment();
        }else{
            detailFragmentListener = (PermisoDetailFragmentListener) activity;
            setRetainInstance(true);
        }
    }
}
