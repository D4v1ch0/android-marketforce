package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.oportunidad.AgendaOportunidad;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadBitacora;
import rp3.marketforce.resumen.AgenteDetalleFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 07/09/2015.
 */
public class OportunidadBitacoraDetailFragment extends BaseFragment {

    public final static int REQ_CODE_SPEECH_INPUT = 100;

    public final static String ARG_OPORTUNIDAD = "id_oportunidad";
    public final static String ARG_BITACORA = "id_bitacora";

    private int idOportunidad;
    private int idBitacora = 0;
    private Oportunidad oportunidad;
    private OportunidadBitacora bitacora;
    private List<OportunidadBitacora> list;

    private SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yy HH:mm");
    private OportunidadBitacoraListListener OportunidadBitacoraListCallback;

    public interface OportunidadBitacoraListListener
    {
        public void Refresh();
    }

    public static OportunidadBitacoraDetailFragment newInstance(int idOportunidad, int idBitacora) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_OPORTUNIDAD, idOportunidad);
        bundle.putInt(ARG_BITACORA, idBitacora);
        OportunidadBitacoraDetailFragment fragment = new OportunidadBitacoraDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static OportunidadBitacoraDetailFragment newInstance(int idOportunidad) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_OPORTUNIDAD, idOportunidad);
        OportunidadBitacoraDetailFragment fragment = new OportunidadBitacoraDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_oportunidad_bitacora_detail);
        if(getParentFragment()!=null){
            OportunidadBitacoraListCallback = (OportunidadBitacoraListListener)getParentFragment();
        }else{
            OportunidadBitacoraListCallback = (OportunidadBitacoraListListener) activity;
            setRetainInstance(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        idOportunidad = getArguments().getInt(ARG_OPORTUNIDAD);
        oportunidad = Oportunidad.getOportunidadId(getDataBase(), idOportunidad);
        idBitacora = getArguments().getInt(ARG_BITACORA, 0);

        if(idBitacora != 0)
        {
            bitacora = OportunidadBitacora.getBitacoraOportunidadUnit(getDataBase(), oportunidad.getIdOportunidad(), idBitacora);
            if(bitacora.getID() == 0)
                bitacora = OportunidadBitacora.getBitacoraOportunidadUnitInt(getDataBase(), oportunidad.getID(), idBitacora);

            ((EditText) rootView.findViewById(R.id.actividad_texto_respuesta)).setText(bitacora.getDetalle());
            ((TextView) rootView.findViewById(R.id.bitacora_agente)).setText(bitacora.getAgente().getNombre());
            ((TextView) rootView.findViewById(R.id.bitacora_fecha)).setText(format1.format(bitacora.getFecha()));

            ((EditText) rootView.findViewById(R.id.actividad_texto_respuesta)).setKeyListener(null);
            ((EditText) rootView.findViewById(R.id.actividad_texto_respuesta)).setTextIsSelectable(true);
            rootView.findViewById(R.id.actividad_voice_to_text).setVisibility(View.GONE);
            rootView.findViewById(R.id.actividad_aceptar).setVisibility(View.GONE);
        }
        else
        {
            rootView.findViewById(R.id.bitacora_fecha).setVisibility(View.GONE);
            rootView.findViewById(R.id.bitacora_agente).setVisibility(View.GONE);
            rootView.findViewById(R.id.actividad_aceptar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((EditText) rootView.findViewById(R.id.actividad_texto_respuesta)).getText().toString().trim().length() > 0) {
                        AgendaOportunidad agd = AgendaOportunidad.getAgendaOportunidadGestionado(getDataBase());
                        bitacora = new OportunidadBitacora();
                        bitacora.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));
                        bitacora.setFecha(Calendar.getInstance().getTime());
                        bitacora.setIdOportunidad(oportunidad.getIdOportunidad());
                        bitacora.set_idOportunidad((int) oportunidad.getID());
                        bitacora.setIdAgenda((int) agd.getID());
                        bitacora.setDetalle(((EditText) rootView.findViewById(R.id.actividad_texto_respuesta)).getText().toString());
                        bitacora.setIdOportunidadBitacora(oportunidad.getOportunidadBitacoras().size() + 1);
                        OportunidadBitacora.insert(getDataBase(), bitacora);
                        oportunidad.setPendiente(true);
                        Oportunidad.update(getDataBase(), oportunidad);
                        //Se envia notificación a todos los agentes
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_NOTIFICATION_OPORTUNIDAD);
                        bundle.putInt(AgenteDetalleFragment.ARG_AGENTE, oportunidad.getIdOportunidad());
                        bundle.putString(AgenteDetalleFragment.ARG_TITLE, oportunidad.getDescripcion());
                        bundle.putString(AgenteDetalleFragment.ARG_MESSAGE, ((TextView) rootView.findViewById(R.id.actividad_texto_respuesta)).getText().toString());
                        requestSync(bundle);

                        Bundle bundle2 = new Bundle();
                        bundle2.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PENDIENTES_OPORTUNIDADES);
                        requestSync(bundle2);


                        Toast.makeText(getContext(), "Registro Ingresado.", Toast.LENGTH_LONG).show();
                        OportunidadBitacoraListCallback.Refresh();
                        finish();
                    } else {
                        Toast.makeText(getContext(), "Debe de escribir un detalle.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        rootView.findViewById(R.id.actividad_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ((ImageView) rootView.findViewById(R.id.actividad_voice_to_text)).setOnClickListener(new View.OnClickListener() {
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
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT:
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        ((TextView)getRootView().findViewById(R.id.actividad_texto_respuesta)).setText(StringUtils.getStringCapSentence(result.get(0)));
                    }
                    break;
            }
        }
    }
}
