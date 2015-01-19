package rp3.marketforce.ruta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.ruta.ContactsAgendaFragment.SaveContactsListener;
import rp3.marketforce.sync.SyncAdapter;

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
				agenda.setObservaciones(getTextViewString(R.id.no_visita_observaciones));
				agenda.setIdMotivoNoVisita(((GeneralValue)((Spinner) rootView.findViewById(R.id.no_visita_motivos)).getSelectedItem()).getCode());
				agenda.setEstadoAgenda(Contants.ESTADO_NO_VISITADO);
				agenda.setEstadoAgendaDescripcion(Contants.DESC_NO_VISITADO);
				agenda.setEnviado(false);
				Agenda.update(getDataBase(), agenda);
				Bundle bundle = new Bundle();
				bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AGENDA_NO_VISITA);
				bundle.putInt(ARG_AGENDA, (int) idAgenda);
				requestSync(bundle);
				saveListener.Refresh();
				getParentFragment().onResume();
				dismiss();
				
			}});
		((Button) rootView.findViewById(R.id.obs_cancelar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dismiss();
				
			}});
	}

}
