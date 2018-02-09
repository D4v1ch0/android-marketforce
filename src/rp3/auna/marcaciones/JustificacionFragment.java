package rp3.auna.marcaciones;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.db.Contract;
import rp3.auna.models.marcacion.Marcacion;
import rp3.auna.models.marcacion.Permiso;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.utils.NothingSelectedSpinnerAdapter;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 09/06/2015.
 */
public class JustificacionFragment extends BaseFragment {

    private static final String TAG = JustificacionFragment.class.getSimpleName();
    private final int REQ_CODE_SPEECH_INPUT = 100;

    public static final String ARG_PERMISO = "id_permidso";

    private Permiso permiso;

    public boolean setCancelar = false;
    public long idMarcacion;

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG,"onAttach...");
        super.onAttach(activity);
        setContentView(R.layout.fragment_justificacion);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        setCancelable(false);

        getDialog().setTitle("Justificación");

        if(idMarcacion != 0 && !setCancelar)
            rootView.findViewById(R.id.cancelar_justificacion).setVisibility(View.GONE);

        rootView.findViewById(R.id.cancelar_justificacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rootView.findViewById(R.id.aceptar_justificacion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((Spinner) rootView.findViewById(R.id.justificacion_motivos)).getSelectedItemPosition() != 0) {
                    if(((TextView) rootView.findViewById(R.id.justificacion_text)).getText().toString().trim().length() > 0) {
                        permiso.setObservacion(((TextView) rootView.findViewById(R.id.justificacion_text)).getText().toString());
                        permiso.setFecha(Calendar.getInstance().getTime());
                        permiso.setTipo(((GeneralValue) ((Spinner) rootView.findViewById(R.id.justificacion_motivos)).getSelectedItem()).getCode());
                        if (permiso.getID() == 0)
                            Permiso.insert(getDataBase(), permiso);
                        else
                            Permiso.update(getDataBase(), permiso);

                        if (idMarcacion == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_PERMISO);
                            bundle.putLong(ARG_PERMISO, permiso.getID());
                            requestSync(bundle);
                        } else {
                            if(permiso.getID() == 0)
                                permiso.setID(getDataBase().queryMaxLong(Contract.Permiso.TABLE_NAME, Contract.Permiso._ID));
                            permiso.setIdMarcacion(idMarcacion);
                            Marcacion marc = Marcacion.getMarcacion(getDataBase(), idMarcacion);
                            marc.setPendiente(true);
                            Marcacion.update(getDataBase(), marc);
                            Permiso.update(getDataBase(), permiso);
                            Bundle bundle = new Bundle();
                            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_MARCACION);
                            //requestSync(bundle);
                        }

                        getParentFragment().onResume();

                        finish();
                    }
                    else
                    {
                        Toast.makeText(getContext(), R.string.message_sin_observacion, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), R.string.message_falta_motivo, Toast.LENGTH_LONG).show();
                }
            }
        });

        rootView.findViewById(R.id.voice_to_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        SimpleGeneralValueAdapter valueAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), Contants.GENERAL_TABLE_MOTIVO_PERMISO);
        ((Spinner)rootView.findViewById(R.id.justificacion_motivos)).setAdapter(new NothingSelectedSpinnerAdapter(
                valueAdapter,
                R.layout.spinner_empty_selected,
                this.getContext()));

        permiso = Permiso.getPermisoMarcacion(getDataBase(), 0);
        if(permiso == null) {
            permiso = new Permiso();
            if(idMarcacion != 0) {
                permiso.setObservacion("(Sin Justificación)");
                permiso.setFecha(Calendar.getInstance().getTime());
                permiso.setTipo("0");

                Permiso.insert(getDataBase(), permiso);
                if (permiso.getID() == 0)
                    permiso.setID(getDataBase().queryMaxLong(Contract.Permiso.TABLE_NAME, Contract.Permiso._ID));
                permiso.setIdMarcacion(idMarcacion);
                Permiso.update(getDataBase(), permiso);
            }
        }
        else
        {
            try {
                ((TextView) rootView.findViewById(R.id.justificacion_text)).setText(permiso.getObservacion());
                ((Spinner)rootView.findViewById(R.id.justificacion_motivos)).setSelection(getPosition(((Spinner) rootView.findViewById(R.id.justificacion_motivos)).getAdapter(), permiso.getTipo()));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
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
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
        Log.d(TAG,"onFragmentResult...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT:
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        ((TextView)getRootView().findViewById(R.id.justificacion_text)).setText(StringUtils.getStringCapSentence(result.get(0)));
                    }
                    break;
            }
        }
    }

    private int getPosition(SpinnerAdapter spinnerAdapter, String i)
    {
        int position = -1;
        for(int f = 0; f < spinnerAdapter.getCount(); f++)
        {
            if(((GeneralValue)spinnerAdapter.getItem(f)).getCode().equals(i))
                position = f;
        }
        return position;
    }

    /**
     *
     * Ciclo de vida
     *
     */

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
}
