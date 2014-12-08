package rp3.marketforce.dashboard;

import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.utils.DrawableManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ListView;

public class DashboardAgendaFragment extends BaseFragment {

	public static DashboardAgendaFragment newInstance() {
		DashboardAgendaFragment fragment = new DashboardAgendaFragment();
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
//		setContentView(R.layout.fragment_client,R.menu.fragment_client);
		setContentView(R.layout.fragment_dashboard_agenda);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
	}
	
	@Override
	public void onStart() {		
		super.onStart();
			
	}
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	
    	DrawableManager DManager = new DrawableManager();
    	
    	Calendar cal = Calendar.getInstance();
    	List<Agenda> list_agenda = Agenda.getRutaDia(getDataBase(), Calendar.getInstance());
    	
    	if(((ListView) rootView.findViewById(R.id.dashboard_agenda_list)) != null)
    	{
	    	LayoutInflater inflater = LayoutInflater.from(getActivity());
	    	
	    	View empty = inflater.inflate(R.layout.rowlist_empty_agenda, null);
	    	if(((LinearLayout)empty.getParent()) != null)
	    		((LinearLayout)empty.getParent()).removeView(empty);
	    	if(list_agenda.size() <= 0)
	    	{
	    		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    		empty.setLayoutParams(params);
	    		((LinearLayout) rootView).addView(empty);
	    	}
	    	
	    	DashboardAgendaAdapter adapter = new DashboardAgendaAdapter(getActivity(), list_agenda);
	    	
	    	((ListView) rootView.findViewById(R.id.dashboard_agenda_list)).setAdapter(adapter);
	    	((ListView) rootView.findViewById(R.id.dashboard_agenda_list)).setEmptyView(empty);
    	}
    	else
    	{
    		LayoutInflater inflater = LayoutInflater.from(getActivity());
    		
    		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    		if(list_agenda.size() <= 0)
    		{
    			View empty = inflater.inflate(R.layout.rowlist_empty_agenda, null);
    			if(((LinearLayout)empty.getParent()) != null)
    				((LinearLayout)empty.getParent()).removeView(empty);
    	    	//getActivity().addContentView(empty, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    			((LinearLayout) rootView.findViewById(R.id.dashboard_agenda_container)).addView(empty);
    		}
    		for(final Agenda agd : list_agenda)
    		{
    			LinearLayout agenda_layout = new LinearLayout(getActivity());
    			agenda_layout = (LinearLayout) inflater.inflate(R.layout.rowlist_dashboard_agenda, null);
    			agenda_layout.setLayoutParams(params);
    			((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_rowlist_nombre)).setText(agd.getCliente().getNombreCompleto().trim());
    			((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setText(agd.getClienteDireccion().getTelefono1());
    			((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setText(agd.getCliente().getCorreoElectronico());
    			
    			((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setClickable(true);
    			((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setOnClickListener(new OnClickListener(){

    						@Override
    						public void onClick(View v) {
    							Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
    						            "mailto",agd.getCliente().getCorreoElectronico(), null));
    							getActivity().startActivity(Intent.createChooser(intent, "Send Email"));
    						}});
    			((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setClickable(true);
    			((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setOnClickListener(new OnClickListener(){
    						@Override
    						public void onClick(View v) {
    							String uri = "tel:" + agd.getClienteDireccion().getTelefono1();
    							Intent intent = new Intent(Intent.ACTION_CALL);
    							intent.setData(Uri.parse(uri));
    							getActivity().startActivity(intent);
    						}});
    			
    			Calendar cal_hoy = Calendar.getInstance();
    			Calendar cal_init = Calendar.getInstance();
    			cal_init.setTime(agd.getFechaInicio());
    			long diff = cal_init.getTimeInMillis() - cal_hoy.getTimeInMillis();
    			if(diff < 0)
    			{
    				((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_tiempo)).setText("");
    			}
    			else
    			{
    				int horas = (int) (diff / (1000*60*60));
    				int restante = (int) (diff / (1000*60));
    				int minutos =  restante - (horas * 60);
    				((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_tiempo)).setText("Faltan " + horas +  " horas con " + minutos +  " minutos para esta reunion.");
    			}
				DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
    					rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agd.getCliente().getURLFoto(),
    					(ImageView) agenda_layout.findViewById(R.id.dashboard_agenda_imagen));
				
				((LinearLayout) rootView.findViewById(R.id.dashboard_agenda_container)).addView(agenda_layout);
    		}
    	}
 }

}
