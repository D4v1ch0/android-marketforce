package rp3.auna.cliente;

import rp3.auna.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class ImportChooseFragment extends rp3.app.BaseFragment  {

	private static final String TAG = ImportChooseFragment.class.getSimpleName();

	public static ImportChooseFragment newInstance()
	{
		ImportChooseFragment fragment = new ImportChooseFragment();
		return fragment;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
        super.setContentView(R.layout.fragment_dialog_import_contactos);
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
		getDialog().setTitle(R.string.label_menu_importar_contactos);
		((Button) rootView.findViewById(R.id.obs_aceptar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				int id = ((RadioGroup) rootView.findViewById(R.id.import_radio_group)).getCheckedRadioButtonId();
				Intent intent = new Intent(getActivity(), ImportContactsActivity.class);
				intent.putExtra(ImportContactsFragment.ARG_ID_ORIGEN, id);
				startActivity(intent);
				dismiss();
				
			}});
		((Button) rootView.findViewById(R.id.obs_cancelar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dismiss();
				
			}});
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
