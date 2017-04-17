package rp3.marketforce.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.marketforce.Contants;
import rp3.marketforce.MainActivity;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.ruta.RutasDetailActivity;
import rp3.marketforce.ruta.RutasListAdapter;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.BitmapUtils;
import rp3.util.Convert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ListView;

@SuppressLint("ResourceType")
public class DashboardAgendaFragment extends BaseFragment {
	
	DashboardAgendaAdapter adapter;
	private List<Agenda> list_agenda;
    private SwipeRefreshLayout pullRefresher;

    public static DashboardAgendaFragment newInstance() {
		DashboardAgendaFragment fragment = new DashboardAgendaFragment();
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
//		setContentView(R.layout.fragment_client,R.menu.fragment_client);
		setContentView(R.layout.fragment_dashboard_agenda);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
	}
	
	@Override
	public void onResume() {
		super.onResume();
		SimpleDateFormat format4 = new SimpleDateFormat("HH:mm");
		DrawableManager DManager = new DrawableManager();
    	
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	list_agenda = Agenda.getRutaDiaDashboard(getDataBase(), cal);
        list_agenda.add(new Agenda());
    	
    	if(((ListView) getRootView().findViewById(R.id.dashboard_agenda_list)) != null)
    	{
	    	LayoutInflater inflater = LayoutInflater.from(getActivity());
	    	
	    	View empty = inflater.inflate(R.layout.rowlist_empty_agenda, null);
	    	if(((LinearLayout)empty.getParent()) != null)
	    		((LinearLayout)empty.getParent()).removeView(empty);
	    	if(list_agenda.size() <= 0)
	    	{
	    		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    		empty.setLayoutParams(params);
	    		((LinearLayout) getRootView()).addView(empty);
	    	}
	    	
	    	adapter = new DashboardAgendaAdapter(getActivity(), list_agenda);
	    	
	    	((ListView) getRootView().findViewById(R.id.dashboard_agenda_list)).setAdapter(adapter);
	    	((ListView) getRootView().findViewById(R.id.dashboard_agenda_list)).setEmptyView(empty);
	    	
	    	((ListView) getRootView().findViewById(R.id.dashboard_agenda_list)).setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(list_agenda.size() <= position + 1)
                    {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.putExtra(MainActivity.TO_AGENDAS, true);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent rutasDetail = RutasDetailActivity.newIntent(getContext(), list_agenda.get(position).getID());
                        startActivity(rutasDetail);
                    }
					
				}
			});
    	}
    	else
    	{
    		LayoutInflater inflater = LayoutInflater.from(getActivity());
    		
    		((LinearLayout) getRootView().findViewById(R.id.dashboard_agenda_container)).removeAllViews();
    		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    		if(list_agenda.size() <= 0)
    		{
    			View empty = inflater.inflate(R.layout.rowlist_empty_agenda, null);
    			if(((LinearLayout)empty.getParent()) != null)
    				((LinearLayout)empty.getParent()).removeView(empty);
    	    	//getActivity().addContentView(empty, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    			((LinearLayout) getRootView().findViewById(R.id.dashboard_agenda_container)).addView(empty);
    		}
    		for(final Agenda agd : list_agenda)
    		{
    			LinearLayout agenda_layout = new LinearLayout(getActivity());

                if(agd.getNombreCompleto() != null) {
                    agenda_layout = (LinearLayout) inflater.inflate(R.layout.rowlist_dashboard_agenda, null);
                    agenda_layout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent rutasDetail = RutasDetailActivity.newIntent(getContext(), agd.getID());
                            startActivity(rutasDetail);
                        }

                    });
                    agenda_layout.setLayoutParams(params);
                    ((ImageView) agenda_layout.findViewById(R.id.dashboard_agenda_imagen)).setImageBitmap(BitmapUtils.getRoundedRectBitmap(
                            BitmapFactory.decodeResource(getResources(), R.drawable.user),
                            getResources().getDimensionPixelOffset(R.dimen.image_size)));
                    ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setText("");

                    if (agd.getCliente() == null) {
                        ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_rowlist_nombre)).setText(agd.getNombreCompleto());
                        ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setText("");
                        ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_hora)).setText(format4.format(agd.getFechaInicio()));
                        ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setText("");
                    } else {
                        if (agd.getCliente().getNombreCompleto() != null)
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_rowlist_nombre)).setText(agd.getCliente().getNombreCompleto().trim());
                        else
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_rowlist_nombre)).setText(agd.getCliente().getNombre1());
                        if (agd.getClienteDireccion() != null && agd.getClienteDireccion().getTelefono1().length() > 0) {
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setText(agd.getClienteDireccion().getTelefono1());
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setClickable(true);
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String uri = "tel:" + agd.getClienteDireccion().getTelefono1();
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse(uri));
                                    Uri mUri = Uri.parse("smsto:" + Utils.convertToSMSNumber(agd.getClienteDireccion().getTelefono1()));
                                    Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                                    mIntent.putExtra("chat", true);
                                    Intent chooserIntent = Intent.createChooser(mIntent, "Seleccionar Acción");
                                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
                                    startActivity(chooserIntent);
                                }
                            });
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setPaintFlags(((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                        } else
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_phone)).setText(R.string.label_sin_especificar);

                        ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_hora)).setText(format4.format(agd.getFechaInicio()));

                        if (agd.getCliente().getCorreoElectronico().length() > 0) {
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setText(agd.getCliente().getCorreoElectronico());
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setClickable(true);
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                            "mailto", agd.getCliente().getCorreoElectronico(), null));
                                    startActivity(Intent.createChooser(intent, "Send Email"));
                                }
                            });
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setPaintFlags(((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setTextColor(getResources().getColorStateList(R.drawable.text_link));
                        } else
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_mail)).setText(R.string.label_sin_especificar);

                        DManager.fetchDrawableOnThreadRounded(PreferenceManager.getString("server") +
                                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agd.getCliente().getURLFoto(),
                                (ImageView) agenda_layout.findViewById(R.id.dashboard_agenda_imagen));
                    }

                    ((ImageView) agenda_layout.findViewById(R.id.dashboard_agenda_gestionando)).setVisibility(View.GONE);

                    if(agd.getEstadoAgenda().equalsIgnoreCase(Contants.ESTADO_GESTIONANDO)) {
                        ((ImageView) agenda_layout.findViewById(R.id.dashboard_agenda_gestionando)).setVisibility(View.VISIBLE);
                        ((ImageView) agenda_layout.findViewById(R.id.dashboard_agenda_gestionando)).setImageResource(R.drawable.circle_in_process);
                    }

                    cal = Calendar.getInstance();
                    Calendar cal_init = Calendar.getInstance();
                    cal_init.setTime(agd.getFechaInicio());
                    long time_now = cal.getTimeInMillis();
                    long diff = cal_init.getTimeInMillis() - cal.getTimeInMillis();

                    int horas = (int) (diff / (1000 * 60 * 60));
                    int restante = (int) (diff / (1000 * 60));
                    int minutos = restante - (horas * 60);
                    if (diff < 0) {
                        if (horas != 0)
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_tiempo)).setText("Hace " + Math.abs(horas) + " horas y " + Math.abs(minutos) + " minutos.");
                        else
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_tiempo)).setText("Hace " + Math.abs(minutos) + " minutos.");
                        ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_tiempo)).setTextColor(getResources().getColor(R.color.color_unvisited));
                    } else {
                        if (horas != 0)
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_tiempo)).setText("Faltan " + horas + " horas con " + minutos + " minutos.");
                        else {
                            ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_tiempo)).setText("Faltan " + minutos + " minutos.");
                            if (minutos < 30)
                                ((TextView) agenda_layout.findViewById(R.id.dashboard_agenda_tiempo)).setTextColor(getResources().getColor(R.color.color_unvisited));
                        }
                    }
                }
                else
                {
                    agenda_layout = (LinearLayout) inflater.inflate(R.layout.rowlist_next_agendas, null);
                    agenda_layout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra(MainActivity.TO_AGENDAS, true);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
				
				((LinearLayout) getRootView().findViewById(R.id.dashboard_agenda_container)).addView(agenda_layout);
    		}
    	}
	}
	
	@Override
	public void onStart() {		
		super.onStart();
			
	}
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        pullRefresher = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        if (pullRefresher != null) {
            pullRefresher.setRefreshing(false);

            pullRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_AGENDAS);
                    requestSync(bundle);
                }
            });
        }
    }

    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);
        closeDialogProgress();
        if(pullRefresher != null) {
            pullRefresher.setRefreshing(false);
            onResume();
        }
    }
}
