package rp3.auna.cliente;

import rp3.auna.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

public class ImportChooseFragment extends rp3.app.BaseFragment  {

	public static ImportChooseFragment newInstance()
	{
		ImportChooseFragment fragment = new ImportChooseFragment();
		return fragment;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.fragment_dialog_import_contactos);
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
}
