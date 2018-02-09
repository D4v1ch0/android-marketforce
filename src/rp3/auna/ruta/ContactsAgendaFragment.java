package rp3.auna.ruta;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import rp3.app.BaseFragment;
import rp3.auna.R;
import rp3.auna.models.Agenda;
import rp3.auna.models.Cliente;
import rp3.auna.models.Contacto;

public class ContactsAgendaFragment extends BaseFragment {
	
	public static String TAG = ContactsAgendaFragment.class.getSimpleName();
	private long idAgenda;
	private Agenda agenda;
	private SaveContactsListener saveListener;
	
	public interface SaveContactsListener {
        public void Refresh();
	}
	
	public static ContactsAgendaFragment newInstance(long idAgenda)
	{
		Bundle arguments = new Bundle();
        arguments.putLong(RutasDetailFragment.ARG_ITEM_ID, idAgenda);
        ContactsAgendaFragment fragment = new ContactsAgendaFragment();
        fragment.setArguments(arguments);
        return fragment;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG,".onCreate..");
        if (getArguments().containsKey(RutasDetailFragment.ARG_ITEM_ID)) {
			Log.d(TAG,"getArguments().containsKey(RutasDetailFragment.ARG_ITEM_ID)...");
            idAgenda = getArguments().getLong(RutasDetailFragment.ARG_ITEM_ID);   
        }else if(savedInstanceState!=null){
			Log.d(TAG,"savedInstanceState!=null...");
        	idAgenda = savedInstanceState.getLong(RutasDetailFragment.STATE_IDAGENDA);
        }    
        
        if(idAgenda != 0){
			Log.d(TAG,"idAgenda != 0...");
        	agenda = Agenda.getAgenda(getDataBase(), idAgenda);
        }
        super.setContentView(R.layout.fragment_list_contactos);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG,"onAattach...");
		if(getParentFragment()!=null)
		{
			Log.d(TAG,"getParentFragment()!=null...");
			saveListener = (SaveContactsListener) getParentFragment();
		}
		else
		{
			Log.d(TAG,"getParentFragment()==null...");
			saveListener = (SaveContactsListener) activity;
		}
	}
	      
	    
	@Override
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
		getDialog().setTitle("Escoger Contacto");
		Log.d(TAG,"onFragmentCreateView...");
		boolean id_interno = true;
		if(agenda.getIdCliente() != 0)
			id_interno = false;
		final List<Contacto> contacts = Contacto.getContactoIdCliente(getDataBase(), agenda.getIdCliente(), id_interno);
		Contacto new_cont = new Contacto();
		new_cont.setNombre("Nuevo Contacto");
		contacts.add(new_cont);
		ContactsAdapter adapter = new ContactsAdapter(getContext(), contacts);
		((ListView) rootView.findViewById(R.id.list_contactos_agenda)).setAdapter(adapter);
		((ListView) rootView.findViewById(R.id.list_contactos_agenda)).setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(contacts.get(position).getIdContacto() != 0)
				{
					Log.d(TAG,"contacts.get(position).getIdContacto() != 0...");
					agenda.setIdContacto((int) contacts.get(position).getIdContacto());
					Agenda.update(getDataBase(), agenda);
					saveListener.Refresh();
					dismiss();
				}
				else
				{
					Log.d(TAG,"contacts.get(position).getIdContacto() == 0...");
					CreateContact();
				}
			}
		});
	}
	
	private void CreateContact()
	{
		Log.d(TAG,"createContact...");
		AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

		alert.setTitle("Nuevo Contacto");
		alert.setMessage("Ingrese el nombre del nuevo contacto");

		final EditText input = new EditText(getContext());
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  Contacto contact = new Contacto();
		  contact.setIdCliente(agenda.getIdCliente());
		  contact.setNombre(value);
		  contact.setIdContacto(Contacto.getLastId(getDataBase(), agenda.getIdCliente()) + 1);
		  contact.setEmpresa(agenda.getNombreCompleto());
		  Contacto.insert(getDataBase(), contact);
		  agenda.setIdContacto((int) contact.getIdContacto());
			agenda.getCliente().setPendiente(true);
			Cliente.update(getDataBase(), agenda.getCliente());
		  Agenda.update(getDataBase(), agenda);
		  saveListener.Refresh();
		  dismiss();
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		  }
		});

		alert.show();
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
