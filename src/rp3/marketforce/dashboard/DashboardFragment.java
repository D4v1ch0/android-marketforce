package rp3.marketforce.dashboard;

import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import rp3.app.BaseFragment;
import rp3.marketforce.R;

public class DashboardFragment extends BaseFragment {
	private static final String TAG = DashboardFragment.class.getSimpleName();
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
		getActivity().setTitle(R.string.title_option_setinicio);
		
		//setRetainInstance(true);
		setContentView(R.layout.fragment_dashboard);
		graphicFragment = DashboardGraphicFragment.newInstance();	
		agendaFragment = DashboardAgendaFragment.newInstance();	
		mapFragment = DashboardMapFragment.newInstance();	
	}
	
	@Override
	public void onStart() {		
		super.onStart();
		Log.d(TAG,"onStart...");
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
		Log.d(TAG,"onDestroyView...");
	}
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	Log.d(TAG,"onFragemtnCreateView...");
	}

	/**
	 *
	 * Ciclo de vida
	 *
	 */

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
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}

}
