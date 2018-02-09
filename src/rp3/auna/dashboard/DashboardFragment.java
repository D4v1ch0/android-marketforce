package rp3.auna.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.marcaciones.MarcacionActivity;

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
		Log.d(TAG,"onAttach...");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
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
		Log.d(TAG,"onFragmentCreateView...");
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
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy...");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPpause...");
	}
}
