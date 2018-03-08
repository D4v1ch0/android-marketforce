package rp3.auna.ruta;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.Agenda;
import rp3.auna.ruta.ContactsAgendaFragment.SaveContactsListener;
import rp3.auna.sync.SyncAdapter;
import rp3.util.StringUtils;

public class MotivoNoVisitaFragment extends BaseFragment {
	public static final String TAG = MotivoNoVisitaFragment.class.getSimpleName();
	public static MotivoNoVisitaFragment newInstance(long idAgenda)
	{
		Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, idAgenda);
        MotivoNoVisitaFragment fragment = new MotivoNoVisitaFragment();
		fragment.setArguments(arguments);
		return fragment;
	}

	public static String ARG_AGENDA = "idAgenda";
	private long idAgenda;
	private Agenda agenda;
	private SaveContactsListener saveListener;
    public static final int REQ_CODE_SPEECH_INPUT = 101;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
		if (getArguments().containsKey(RutasDetailFragment.ARG_ITEM_ID)) {
            idAgenda = getArguments().getLong(RutasDetailFragment.ARG_ITEM_ID);   
        }else if(savedInstanceState!=null){
        	idAgenda = savedInstanceState.getLong(RutasDetailFragment.STATE_IDAGENDA);
        }    
        
        if(idAgenda != 0){        	
        	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
			if(agenda == null)
				agenda = Agenda.getAgendaClienteNull(getDataBase(), idAgenda);
        }
        super.setContentView(R.layout.fragment_dialog_no_visita);
	}
	
	@Override
	public void onAttach(Activity activity) {    	
	    super.onAttach(activity);
		Log.d(TAG,"onAttach...");
	}

	
	@Override
	public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		Log.d(TAG,"onFragmentCreateView...");
		getDialog().setTitle("Motivo de No Venta");
		saveListener = (SaveContactsListener) getParentFragment();
		SimpleGeneralValueAdapter motivosNoVisitaAdapter= new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.auna.Contants.GENERAL_TABLE_MOTIVOS_NO_VISITA);
		((Spinner) rootView.findViewById(R.id.no_visita_motivos)).setAdapter(motivosNoVisitaAdapter);
		
		((Button) rootView.findViewById(R.id.obs_aceptar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(((EditText)rootView.findViewById(R.id.obs_text)).getText().toString().trim().length() <= 0)
				{
					Toast.makeText(getContext(),
							"Debe escribir una observación.",
							Toast.LENGTH_SHORT).show();
					return;
				}
				agenda.setObservaciones(((EditText)rootView.findViewById(R.id.obs_text)).getText().toString());
				agenda.setIdMotivoNoVisita(((GeneralValue)((Spinner) rootView.findViewById(R.id.no_visita_motivos)).getSelectedItem()).getCode());
				agenda.setEstadoAgenda(Contants.ESTADO_NO_VISITADO);
				agenda.setEstadoAgendaDescripcion(Contants.DESC_NO_VISITADO);
				agenda.setEnviado(false);
				Agenda.update(getDataBase(), agenda);
				Bundle bundle = new Bundle();
				bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AGENDA_NO_VISITA);
				bundle.putInt(ARG_AGENDA, (int) idAgenda);
				requestSync(bundle);
				if(saveListener != null) 
				{
					saveListener.Refresh();
					getParentFragment().onResume();
				}
				else
				{
					((RutasDetailActivity)getActivity()).onResume();
				}
				
				
				dismiss();
				
			}});
		((Button) rootView.findViewById(R.id.obs_cancelar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dismiss();
				
			}});
        ((ImageView) rootView.findViewById(R.id.observaciones_voice_to_text)).setOnClickListener(new View.OnClickListener() {
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
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG,"onActivityResult...");
        if (resultCode == RESULT_OK) {
			Log.d(TAG,"resultCode == RESULT_OK...");
            if (resultCode == RESULT_OK && null != data) {
				Log.d(TAG,"resultCode == RESULT_OK && null != data...");
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                setTextViewText(R.id.obs_text, StringUtils.getStringCapSentence(result.get(0)));
            }

        }
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