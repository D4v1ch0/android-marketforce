package rp3.berlin.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.marcaciones.MarcacionActivity;

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
		getActivity().setTitle(R.string.title_option_setinicio);
		
		//setRetainInstance(true);
        if(PreferenceManager.getBoolean(Contants.KEY_APLICA_MARCACION) && PreferenceManager.getBoolean(Contants.KEY_MODULO_MARCACIONES, true))
		    setContentView(R.layout.fragment_dashboard, R.menu.fragment_dashboard_menu);
        else
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_marcaciones:
                Intent intent = new Intent(getContext(), MarcacionActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}