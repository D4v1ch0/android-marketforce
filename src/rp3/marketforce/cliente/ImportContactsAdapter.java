package rp3.marketforce.cliente;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.R;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImportContactsAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Cliente> list_contactos;
	private List<Boolean> checked;
	private DrawableManager DManager;
	private int tipo;

	public ImportContactsAdapter(Context context, List<Cliente> list_contactos, int tipo)
	{
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list_contactos = list_contactos;
		DManager = new DrawableManager();
		this.tipo = tipo;
		checked = new ArrayList<Boolean>();
		for(Cliente cli : list_contactos)
			checked.add(false);
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
		
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = (View) inflater.inflate(this.context.getApplicationContext().getResources().getLayout(R.layout.rowlist_import_contact), null);
		
		Cliente contact = list_contactos.get(position);
		switch(tipo)
		{
			case R.id.import_android:
				((TextView) convertView.findViewById(R.id.import_item_name)).setText(contact.getNombreCompleto());
				((TextView) convertView.findViewById(R.id.import_item_info1)).setText(contact.getTelefono());
				((TextView) convertView.findViewById(R.id.import_item_info2)).setText(contact.getCorreoElectronico());
				fetchContactPhoto(contact.getIdCliente(), ((ImageView) convertView.findViewById(R.id.import_item_image)));
				
				 try {
			            InputStream inputStream = new ByteArrayInputStream(openPhoto(contact.getIdCliente()));
			 
			            if (inputStream != null)
			                ((ImageView) convertView.findViewById(R.id.import_item_image)).setImageBitmap(BitmapFactory.decodeStream(inputStream));
			 
			            assert inputStream != null;
			            inputStream.close();
			 
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				break;	
			case R.id.import_linkedin:
				break;
			case R.id.import_skype:
				break;
			case R.id.import_facebook:
				break;
			case R.id.import_gmail:
				break;
			default:
				break;
		}
		
		((CheckBox) convertView.findViewById(R.id.import_item_check)).setChecked(checked.get(position));
		convertView.setClickable(true);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setChecked(position);
				
			}
		});
		return convertView;
	}
	
	public List<Cliente> getSelectedClients() {
		List<Cliente> result = new ArrayList<Cliente>();
		
		for(int i = 0; i < list_contactos.size(); i++ )
		{
			if(checked.get(i))
			{
				byte[] data = null;
				List<ClienteDireccion> cds = new ArrayList<ClienteDireccion>();
				Cliente getter = list_contactos.get(i);
				data = openPhoto(getter.getIdCliente());
				if(data != null)
				{
					try
					{
						File file = Utils.getOutputMediaFile(Utils.MEDIA_TYPE_IMAGE, getter.getIdCliente() + "");
						FileOutputStream stream = new FileOutputStream(file.getPath()); 
				        stream.write(data);
				        stream.close();
						getter.setURLFoto(file.getAbsolutePath());
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				getter.setNombre1(getter.getNombreCompleto());
				getter.setNombre2("");
				getter.setApellido1("");
				getter.setApellido2("");
				getter.setIdTipoIdentificacion(1);
				getter.setGenero("M");
				getter.setEstadoCivil("S");
				getter.setTipoPersona("N");
				getter.setIdTipoCliente(1);
				getter.setIdCanal(1);
				getter.setIdCliente(0);
				getter.setPendiente(true);
				getter.setNuevo(true);
				getter.setDireccion("(Sin especificar)");
				ClienteDireccion cd = new ClienteDireccion();
				cd.setDireccion("(Sin especificar)");
				cd.setTelefono1(getter.getTelefono());
				cd.setEsPrincipal(true);
				cd.setTipoDireccion("D");
				cd.setIdCiudad(1);
				cds.add(cd);
				getter.setClienteDirecciones(cds);
				result.add(getter);
			}
		}
		return result;
	}
	
	public void checkAll(boolean isChecked) {
		checked.clear();
		for(Cliente cli : list_contactos)
			checked.add(isChecked);
		notifyDataSetChanged();
	}
	
	public void setChecked(int position)
	{
		if(checked.get(position))
			checked.set(position, false);
		else
			checked.set(position, true);
		notifyDataSetChanged();
	}
	
	public byte[] openPhoto(long contactId) {
		 byte[] data = null;
	     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	     Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
	     Cursor cursor = context.getContentResolver().query(photoUri,
	          new String[] {Contacts.Photo.PHOTO}, null, null, null);
	     if (cursor == null) {
	         return null;
	     }
	     try {
	         if (cursor.moveToFirst()) {
	             data = cursor.getBlob(0);
	         }
	     } finally {
	         cursor.close();
	     }
	     return data;
	 }
	
	public void fetchContactPhoto(final long l, final ImageView imageView) {

        	final Handler handler = new Handler() {
        		@Override
        		public void handleMessage(Message message) {
        			if(message.obj != null)
        				imageView.setImageBitmap((Bitmap) message.obj);
        		}
        	};

        	Thread thread = new Thread() {
        		@Override
        		public void run() {
        			Bitmap bitmap = null;
        			try {
			            InputStream inputStream = new ByteArrayInputStream(openPhoto(l));
			            
			            if (inputStream != null)
			            	bitmap = BitmapFactory.decodeStream(inputStream);
			 
			            assert inputStream != null;
			            inputStream.close();
			 
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
        			Message message = handler.obtainMessage(1, bitmap);
        			handler.sendMessage(message);
        		}
        	};
        	thread.start();
    }

}
