package rp3.marketforce.ruta;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.ruta.ContactsAgendaFragment.SaveContactsListener;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.Utils;
import rp3.util.StringUtils;

public class MotivoNoVisitaFragment extends BaseFragment {
	public static MotivoNoVisitaFragment newInstance(long idAgenda)
	{
		Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, idAgenda);
        MotivoNoVisitaFragment fragment = new MotivoNoVisitaFragment();
		fragment.setArguments(arguments);
		return fragment;
	}

	public static String ARG_AGENDA = "idAgenda";
	public static String TAG = "Motivo de No Visita";
	private long idAgenda;
	private Agenda agenda;
	private SaveContactsListener saveListener;
    public static final int REQ_CODE_SPEECH_INPUT = 101;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(RutasDetailFragment.ARG_ITEM_ID)) {            
            idAgenda = getArguments().getLong(RutasDetailFragment.ARG_ITEM_ID);   
        }else if(savedInstanceState!=null){
        	idAgenda = savedInstanceState.getLong(RutasDetailFragment.STATE_IDAGENDA);
        }    
        
        if(idAgenda != 0){        	
        	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
        }
        super.setContentView(R.layout.fragment_dialog_no_visita);
	}
	
	@Override
	public void onAttach(Activity activity) {    	
	    super.onAttach(activity);
	}
	    
	@Override
	public void onResume() {
	    super.onResume();
	}
	
	@Override
	public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		getDialog().setTitle("Motivo de No Visita");
		saveListener = (SaveContactsListener) getParentFragment();
		SimpleGeneralValueAdapter motivosNoVisitaAdapter= new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_MOTIVOS_NO_VISITA);
		((Spinner) rootView.findViewById(R.id.no_visita_motivos)).setAdapter(motivosNoVisitaAdapter);
		
		((Button) rootView.findViewById(R.id.obs_aceptar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				agenda.setObservaciones(getTextViewString(R.id.obs_text));
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
        if (resultCode == RESULT_OK) {

            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                setTextViewText(R.id.obs_text, StringUtils.getStringCapSentence(result.get(0)));
            }

        }
    }

}
