package rp3.marketforce.cliente;

import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.cliente.ClientListFragment.LoaderCliente;
import rp3.marketforce.loader.ClientLoader;
import rp3.marketforce.loader.InternContactsLoader;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.ruta.CrearVisitaActivity;
import rp3.marketforce.ruta.CrearVisitaFragment;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;

public class ImportContactsFragment extends rp3.app.BaseFragment  {
	
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
        tipo = getArguments().getInt(ARG_ID_ORIGEN);
        super.setContentView(R.layout.fragment_import_contacts);
	}
	
	@Override
	public void onAttach(Activity activity) {    	
	    super.onAttach(activity);
	    setRetainInstance(true);
	}
	
	    
	@Override
	public void onResume() {
	    super.onResume();
	}
	
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		super.onFragmentCreateView(rootView, savedInstanceState);
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
}
