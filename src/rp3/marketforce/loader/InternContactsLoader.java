package rp3.marketforce.loader;

import java.util.ArrayList;
import java.util.List;

import rp3.db.sqlite.DataBase;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.utils.ImageFilePath;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class InternContactsLoader extends rp3.content.SimpleObjectLoader<List<Cliente>> {

	private Context ctx;

	public InternContactsLoader(Context context) {
		super(context);
		ctx = context;
	}

	@Override
	public List<Cliente> loadInBackground() {
		ContentResolver cr = ctx.getContentResolver();
		List<Cliente> contactos = new ArrayList<Cliente>();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
		if(cur.moveToFirst())
		{
			do
			{
				Cliente setter = new Cliente();
				long id = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));
				setter.setNombreCompleto(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
				setter.setNombreCompleto(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
				if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                           ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                           new String[]{id+""}, null);
                    while (pCur.moveToNext()) {
                    	setter.setTelefono(pCur.getString(
                                 pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
                    pCur.close();

                   Cursor emailCur = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id+""}, null);
                    while (emailCur.moveToNext()) {
                    	setter.setCorreoElectronico(emailCur.getString(
                                      emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                    }
                    emailCur.close();
                    setter.setIdCliente(id);
                    contactos.add(setter);
                }
			}while(cur.moveToNext());
		}
		return contactos;
	}

}
