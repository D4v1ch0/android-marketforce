package rp3.auna.cliente;

import java.util.List;

import rp3.auna.R;
import rp3.auna.loader.InternContactsLoader;
import rp3.auna.models.Cliente;
import rp3.auna.models.ClienteDireccion;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.CheckBox;

public class ImportContactsFragment extends rp3.app.BaseFragment  {

	private static final String TAG = ImportContactsFragment.class.getSimpleName();
	public static String ARG_ID_ORIGEN = "origen";
	private int tipo;
	private LoaderInternContacts loaderContacts;
	private ImportContactsAdapter adapter;
	private ListView listView;
	
	public static ImportContactsFragment newInstance(int id)
	{
		Bundle arguments = new Bundle();
        arguments.putInt(ARG_ID_ORIGEN, id);
        ImportContactsFragment fragment = new ImportContactsFragment();
		fragment.setArguments(arguments);
		return fragment;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
        tipo = getArguments().getInt(ARG_ID_ORIGEN);
        super.setContentView(R.layout.fragment_import_contacts);
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG,"onAttach...");
	    super.onAttach(activity);
	    setRetainInstance(true);
	}
	

	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
		Log.d(TAG,"onFragmentCreateView...");
		loaderContacts = new LoaderInternContacts();
		listView = ((ListView) rootView.findViewById(R.id.import_list_view));
		listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setChecked(position);			
			}
		});
		
		Bundle args = new Bundle();
	    getLoaderManager().initLoader(0, args, loaderContacts);
		
		((Button) rootView.findViewById(R.id.obs_aceptar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				List<Cliente> cli = adapter.getSelectedClients();
				for(Cliente cl : cli)
				{
					Cliente.insert(getDataBase(), cl);
					for(ClienteDireccion cd : cl.getClienteDirecciones())
					{
						cd.set_idCliente(cl.getID());
						ClienteDireccion.insert(getDataBase(), cd);
					}
				}
				finish();
				
			}});
		((Button) rootView.findViewById(R.id.obs_cancelar)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
				
			}});
		
		((CheckBox) rootView.findViewById(R.id.import_seleccionar_todos)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				adapter.checkAll(isChecked);
			}
		});
	}
	
	public class LoaderInternContacts implements LoaderCallbacks<List<Cliente>>{

    	@Override
		public Loader<List<Cliente>> onCreateLoader(int arg0,
				Bundle bundle) {				
			return new InternContactsLoader(getActivity());		
		}

		@Override
		public void onLoadFinished(Loader<List<Cliente>> arg0,
				List<Cliente> data)
		{								
			adapter = new ImportContactsAdapter(getContext(), data, tipo);
			((ListView) getRootView().findViewById(R.id.import_list_view)).setAdapter(adapter);
			getRootView().findViewById(R.id.import_progress).setVisibility(View.GONE);
			((ListView) getRootView().findViewById(R.id.import_list_view)).setVisibility(View.VISIBLE);
		}

		@Override
		public void onLoaderReset(Loader<List<Cliente>> arg0) {	
			
		}
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
