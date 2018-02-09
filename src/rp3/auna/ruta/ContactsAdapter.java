package rp3.auna.ruta;

import java.util.List;

import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.Contacto;
import rp3.auna.utils.DrawableManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsAdapter extends BaseAdapter {

	private static final String TAG = ContactsAdapter.class.getSimpleName();
	private Context context;
	private LayoutInflater inflater;
	private List<Contacto> list_contactos;
	private DrawableManager DManager;
	
	public ContactsAdapter(Context context, List<Contacto> list_contactos)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list_contactos = list_contactos;
		DManager = new DrawableManager();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_contactos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_contactos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_contact_agenda), null);
		
		Contacto contact = list_contactos.get(position);
		String apellido = "";
		DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + contact.getURLFoto(),
				(ImageView) convertView.findViewById(R.id.contact_image));
		if(contact.getApellido() != null)
			apellido = contact.getApellido();
		((TextView) convertView.findViewById(R.id.contact_name)).setText(contact.getNombre() + " " + apellido);
		
		return convertView;
	}

}
