package rp3.marketforce;

import rp3.app.BaseFragment;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class DefaultFragment extends BaseFragment{

	private static final String TAG = DefaultFragment.class.getSimpleName();

	public static DefaultFragment newInstance(int transactionTypeId) {
		DefaultFragment fragment = new DefaultFragment();
		return fragment;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		
//		setContentView(R.layout.fragment_client,R.menu.fragment_client);
		setContentView(R.layout.fragment_rutas);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setRetainInstance(true);		
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {		
		super.onFragmentCreateView(rootView, savedInstanceState);
						
		
	}	
	
	@Override
	public void onAfterCreateOptionsMenu(Menu menu) {		
	}

	/**
	 *
	 *Ciclo de vida
	 *
	 */


	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"onResume...");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG,"onStop...");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause...");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG,"onStart...");
	}

}
