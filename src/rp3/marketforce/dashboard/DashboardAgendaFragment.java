package rp3.marketforce.dashboard;

import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.ruta.RutasDetailActivity;
import rp3.marketforce.utils.DrawableManager;
import rp3.util.BitmapUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ListView;

public class DashboardAgendaFragment extends BaseFragment {
	
	DashboardAgendaAdapter adapter;
	private List<Agenda> list_agenda;

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
	public void onResume() {
		super.onResume();
		DrawableManager DManager = new DrawableManager();
    	
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	list_agenda = Agenda.getRutaDiaDashboard(getDataBase(), cal);
    	
    	if(((ListView) getRootView().findViewById(R.id.dashboard_agenda_list)) != null)
    	{
	    	LayoutInflater inflater = LayoutInflater.from(getActivity());
	    	
	    	View empty = inflater.inflate(R.layout.rowlist_empty_agenda, null);
	    	if(((LinearLayout)empty.getParent()) != null)
	    		((LinearLayout)empty.getParent()).removeView(empty);
	    	if(list_agenda.size() <= 0)
	    	{
	    		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    		empty.setLayoutParams(params);
	    		((LinearLayout) getRootView()).addView(empty);
	    	}
	    	
	    	adapter = new DashboardAgendaAdapter(getActivity(), list_agenda);
	    	
	    	((ListView) getRootView().findViewById(R.id.dashboard_agenda_list)).setAdapter(adapter);
	    	((ListView) getRootView().findViewById(R.id.dashboard_agenda_list)).setEmptyView(empty);
	    	
	    	((ListView) getRootView().findViewById(R.id.dashboard_agenda_list)).setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent rutasDetail = RutasDetailActivity.newIntent(getContext(), list_agenda.get(position).getID());
					startActivity(rutasDetail);
					
				}
			});
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
    			((LinearLayout) getRootView().findViewById(R.id.dashboard_agenda_container)).addView(empty);
    		}
    		for(final Agenda agd : list_agenda)
    		{
    			LinearLayout agenda_layout = new LinearLayout(getActivity());
    			agenda_layout = (LinearLayout) inflater.inflate(R.layout.rowlist_dashboard_agenda, null);
    			agenda_layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent rutasDetail = RutasDetailActivity.newIntent(getContext(), agd.getID());
						startActivity(rutasDetail);
					}

    			});
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
    			((ImageView) agenda_layout.findViewById(R.id.dashboard_agenda_imagen)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
    					BitmapFactory.decodeResource(getResources(), R.drawable.user), 
    					getResources().getDimensionPixelOffset(R.dimen.image_size)));
				DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") + 
    					rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agd.getCliente().getURLFoto(),
    					(ImageView) agenda_layout.findViewById(R.id.dashboard_agenda_imagen));
				
				((LinearLayout) getRootView().findViewById(R.id.dashboard_agenda_container)).addView(agenda_layout);
    		}
    	}
	}
	
	@Override
	public void onStart() {		
		super.onStart();
			
	}
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	
    	
 }

}
