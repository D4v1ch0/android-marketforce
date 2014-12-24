package rp3.marketforce.dashboard;

import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebView.FindListener;
import rp3.app.BaseFragment;
import rp3.marketforce.R;

public class DashboardFragment extends BaseFragment {

	DashboardGraphicFragment graphicFragment;
	DashboardAgendaFragment agendaFragment;
	DashboardMapFragment mapFragment;
	boolean mapInstantiate = false;
	
	public static DashboardFragment newInstance(int i) {
		DashboardFragment fragment = new DashboardFragment();
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		getActivity().setTitle("Inicio");
		
		//setRetainInstance(true);				
		setContentView(R.layout.fragment_dashboard);
		graphicFragment = DashboardGraphicFragment.newInstance();	
		agendaFragment = DashboardAgendaFragment.newInstance();	
		mapFragment = DashboardMapFragment.newInstance();	
	}
	
	@Override
	public void onStart() {		
		super.onStart();
		
		if(!hasFragment(R.id.dashboard_frame_graphics))		
			setFragment(R.id.dashboard_frame_graphics, graphicFragment );	
		
		if(!hasFragment(R.id.dashboard_frame_agenda))		
			setFragment(R.id.dashboard_frame_agenda, agendaFragment );
			
		if(getRootView().findViewById(R.id.dashboard_frame_mapa) != null)
		{
			if(!hasFragment(R.id.dashboard_frame_mapa))
			{
				setFragment(R.id.dashboard_frame_mapa, mapFragment );
			}
		}
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	
	}

}