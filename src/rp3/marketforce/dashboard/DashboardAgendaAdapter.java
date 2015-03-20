package rp3.marketforce.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.ruta.RutasListFragment.TransactionListFragmentListener;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardAgendaAdapter extends BaseAdapter{
	
	List<Agenda> list_agenda;
	LayoutInflater inflater;
	DrawableManager DManager;
	Context ctx;
	private SimpleDateFormat format4;
	
	public DashboardAgendaAdapter(Context c, List<Agenda> list_agenda){
		this.inflater = LayoutInflater.from(c);
		this.ctx = c;
		this.list_agenda = list_agenda;
		DManager = new DrawableManager();
		format4= new SimpleDateFormat("HH:mm");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_agenda.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_agenda.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = (View) inflater.inflate(this.ctx.getApplicationContext().getResources().getLayout(R.layout.rowlist_dashboard_agenda), null);
		final Agenda agd = list_agenda.get(position);
        ((ImageView) convertView.findViewById(R.id.dashboard_agenda_imagen)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
                BitmapFactory.decodeResource(ctx.getResources(), R.drawable.user),
                ctx.getResources().getDimensionPixelOffset(R.dimen.image_size)));
        ((TextView) convertView.findViewById(R.id.dashboard_agenda_phone)).setText("");

		if(agd.getCliente() == null) {
            ((TextView) convertView.findViewById(R.id.dashboard_agenda_rowlist_nombre)).setText(agd.getNombreCompleto());
            ((TextView) convertView.findViewById(R.id.dashboard_agenda_phone)).setText("");
            ((TextView) convertView.findViewById(R.id.dashboard_agenda_hora)).setText(format4.format(agd.getFechaInicio()));
            ((TextView) convertView.findViewById(R.id.dashboard_agenda_mail)).setText("");
        }
        else {
            if (agd.getCliente().getNombreCompleto() != null)
                ((TextView) convertView.findViewById(R.id.dashboard_agenda_rowlist_nombre)).setText(agd.getCliente().getNombreCompleto().trim());
            else
                ((TextView) convertView.findViewById(R.id.dashboard_agenda_rowlist_nombre)).setText(agd.getCliente().getNombre1());
            if(agd.getClienteDireccion() != null)
                ((TextView) convertView.findViewById(R.id.dashboard_agenda_phone)).setText(agd.getClienteDireccion().getTelefono1());

            ((TextView) convertView.findViewById(R.id.dashboard_agenda_hora)).setText(format4.format(agd.getFechaInicio()));
            ((TextView) convertView.findViewById(R.id.dashboard_agenda_mail)).setText(agd.getCliente().getCorreoElectronico());
            DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agd.getCliente().getURLFoto(),
                    (ImageView) convertView.findViewById(R.id.dashboard_agenda_imagen));
        }
		
		((TextView) convertView.findViewById(R.id.dashboard_agenda_mail)).setClickable(true);
		((TextView) convertView.findViewById(R.id.dashboard_agenda_mail)).setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
					            "mailto",agd.getCliente().getCorreoElectronico(), null));
						ctx.startActivity(Intent.createChooser(intent, "Send Email"));
					}});
		((TextView) convertView.findViewById(R.id.dashboard_agenda_phone)).setClickable(true);
		((TextView) convertView.findViewById(R.id.dashboard_agenda_phone)).setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						String uri = "tel:" + agd.getClienteDireccion().getTelefono1();
						Intent intent = new Intent(Intent.ACTION_DIAL);
						intent.setData(Uri.parse(uri));
						Uri mUri = Uri.parse("smsto:" + Utils.convertToSMSNumber(agd.getClienteDireccion().getTelefono1()));
			            Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
			            mIntent.putExtra("chat",true);
			            Intent chooserIntent = Intent.createChooser(mIntent, "Seleccionar Acci�n");
			            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent });
			            ctx.startActivity(chooserIntent);
					}});
		
		Calendar cal = Calendar.getInstance();
		Calendar cal_init = Calendar.getInstance();
		cal_init.setTime(agd.getFechaInicio());
		long time_now = cal.getTimeInMillis();
		long diff = cal_init.getTimeInMillis() - cal.getTimeInMillis();
		
		int horas = (int) (diff / (1000*60*60));
		int restante = (int) (diff / (1000*60));
		int minutos =  restante - (horas * 60);
		if(diff < 0)
		{
			if(horas != 0)
				((TextView) convertView.findViewById(R.id.dashboard_agenda_tiempo)).setText("Hace " + Math.abs(horas) + " horas y " + Math.abs(minutos) + " minutos.");
			else
				((TextView) convertView.findViewById(R.id.dashboard_agenda_tiempo)).setText("Hace " + Math.abs(minutos) + " minutos.");
			((TextView) convertView.findViewById(R.id.dashboard_agenda_tiempo)).setTextColor(ctx.getResources().getColor(R.color.color_unvisited));
		}
		else
		{
			if(horas != 0)
				((TextView) convertView.findViewById(R.id.dashboard_agenda_tiempo)).setText("Faltan " + horas +  " horas con " + minutos +  " minutos.");
			else
			{
				((TextView) convertView.findViewById(R.id.dashboard_agenda_tiempo)).setText("Faltan " + minutos +  " minutos.");
				if(minutos < 30)
					((TextView) convertView.findViewById(R.id.dashboard_agenda_tiempo)).setTextColor(ctx.getResources().getColor(R.color.color_unvisited));
			}
			
			
		}

		return convertView;
	}
}
