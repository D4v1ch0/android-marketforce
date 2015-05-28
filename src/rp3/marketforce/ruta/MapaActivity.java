package rp3.marketforce.ruta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import rp3.app.BaseActivity;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.LocationUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MapaActivity extends BaseActivity {
	
	public static String ACTION_TYPE = "type";
	
	public static String ACTION_POSICION = "posicion";
    public static String ACTION_POSICION_CLIENTE = "posicion_cliente";
	public static String ACTION_LLEGAR = "llegar";
    public static String ACTION_LLEGAR_CLIENTE = "llegar_cliente";
	public static String ACTION_RUTAS = "rutas";
	
	public static String ARG_AGENDA = "agenda";
	
	Context ctx;
	private GoogleMap map;
	ImageButton expand, collapse;
	Button ComoLlegar, Posicion, RutasFechas;
	ListView listview;
	LinearLayout persona;
	Agenda agenda;
	DrawableManager DManager;
	List<Marker> markers;
	View lastItem;

	private SimpleDateFormat format1;
    private SupportMapFragment mapFragment;

    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setTitle("Ruta");
	    format1 = new SimpleDateFormat("EEEE dd MMMM yyyy");
	
	    ctx = this;
	    DManager = new DrawableManager();
	    setContentView(R.layout.layout_map_rutas);
	    setHomeAsUpEnabled(true, true);
        showDialogProgress("Cargando", "Mostrando Mapa");
        final String action = getIntent().getExtras().getString(ACTION_TYPE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                    FragmentManager fm = getCurrentFragmentManager();
                    mapFragment = SupportMapFragment
                            .newInstance();
                    fm.beginTransaction()
                            .replace(R.id.recorrido_dummy, mapFragment).commit();
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            map = googleMap;
                            closeDialogProgress();
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Contants.LATITUD, Contants.LONGITUD), Contants.ZOOM), 1, null);
                            map.getUiSettings().setZoomControlsEnabled(false);
                            if(action.equalsIgnoreCase(ACTION_POSICION))
                                setPosicion(getIntent().getExtras().getLong(ARG_AGENDA));
                            if(action.equalsIgnoreCase(ACTION_LLEGAR))
                                setRuta(getIntent().getExtras().getLong(ARG_AGENDA));
                            if(action.equalsIgnoreCase(ACTION_POSICION_CLIENTE))
                                setPosicionCliente(getIntent().getExtras().getLong(ARG_AGENDA));
                            if(action.equalsIgnoreCase(ACTION_LLEGAR_CLIENTE))
                                setRutaCliente(getIntent().getExtras().getLong(ARG_AGENDA));
                            if(action.equalsIgnoreCase(ACTION_RUTAS))
                            {
                                ComoLlegar.setVisibility(View.GONE);
                                Posicion.setVisibility(View.GONE);
                                if(getIntent().getExtras().containsKey(ARG_AGENDA))
                                {
                                    agenda = Agenda.getAgenda(getDataBase(), getIntent().getExtras().getLong(ARG_AGENDA));
                                    Calendar c =  Calendar.getInstance();
                                    c.setTime(agenda.getFechaInicio());
                                    onDailogDatePickerChange(0, c);
                                }
                                else
                                    onDailogDatePickerChange(0, Calendar.getInstance());
                            }

                        }
                    });

            }
        }, 1000);
        expand = (ImageButton) findViewById(R.id.map_expand);
        collapse = (ImageButton) findViewById(R.id.map_collapse);
        listview = (ListView) findViewById(R.id.map_list);
        persona = (LinearLayout) findViewById(R.id.map_contact);
        ComoLlegar = (Button) findViewById(R.id.map_como_llegar);
        Posicion = (Button) findViewById(R.id.map_posicion);
        RutasFechas = (Button) findViewById(R.id.map_ruta_por_fecha);

        persona.setVisibility(View.GONE);
        expand.setVisibility(View.GONE);
        listview.setVisibility(View.INVISIBLE);
        ComoLlegar.setVisibility(View.GONE);
        Posicion.setVisibility(View.GONE);
	}
	
	public void ClickPosicion(View v)
	{
		setPosicion(agenda.getID());
	}
	
	public void ClickComoLlegar(View v)
	{
		setRuta(agenda.getID());
	}
	
	public void ClickRutas(View v)
	{
		ComoLlegar.setVisibility(View.GONE);
		Posicion.setVisibility(View.GONE);
		if(agenda == null)
			showRutaPorFecha();
		else
		{
			Calendar c =  Calendar.getInstance();
			c.setTime(agenda.getFechaInicio());
			onDailogDatePickerChange(0, c);
		}
			
	}
	
	public void slideToBottom(final View view, boolean enable){
		TranslateAnimation animate = new TranslateAnimation(0,0,0,getResources().getDimension(R.dimen.list_map));
		animate.setDuration(500);
		animate.setFillAfter(true);
		if(enable)
		{
		animate.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				collapse.setVisibility(View.VISIBLE);
				expand.setVisibility(View.GONE);
				expand.setClickable(false);
				((TextView) findViewById(R.id.map_mail)).setVisibility(View.GONE);
				((TextView) findViewById(R.id.map_name)).setVisibility(View.GONE);
				((TextView) findViewById(R.id.map_phone)).setVisibility(View.GONE);
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}});
		}
		view.startAnimation(animate);
		view.setClickable(enable);
		view.setFocusable(enable);
		view.setEnabled(enable);
		}
	
	public void slideToTop(View view, boolean enable){
		TranslateAnimation animate = new TranslateAnimation(0,0,getResources().getDimension(R.dimen.list_map),0);
		animate.setDuration(500);
		animate.setFillAfter(true);
		if(enable)
		{
		animate.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
				collapse.setVisibility(View.GONE);
				((TextView) findViewById(R.id.map_mail)).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.map_name)).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.map_phone)).setVisibility(View.VISIBLE);
				expand.setClickable(true);
				listview.setEnabled(true);
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				expand.setVisibility(View.VISIBLE);
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}});
		}
		view.startAnimation(animate);
		view.setClickable(enable);
		view.setFocusable(enable);
		view.setEnabled(enable);
		}
	
	@Override
	public void onDailogDatePickerChange(int id, Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		agenda = null;
        format1 = new SimpleDateFormat("EEEE");
        SimpleDateFormat format2 = new SimpleDateFormat("dd");
        SimpleDateFormat format3 = new SimpleDateFormat("MMMM");
        SimpleDateFormat format5 = new SimpleDateFormat("yyyy");
        String dia = "";
        Calendar hoy = Calendar.getInstance();
        if(hoy.get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR))
            dia = "Hoy";
        else
            dia = format1.format(c.getTime());
        dia = dia.substring(0,1).toUpperCase() + dia.substring(1);
        String num = format2.format(c.getTime());
        String mes = format3.format(c.getTime());
        mes = mes.substring(0,1).toUpperCase() + mes.substring(1);
        String anio = format5.format(c.getTime());
		RutasFechas.setText(dia + ", " + num + " de " +  mes + " del " + anio);
		map.clear();
		persona.setVisibility(View.GONE);
		
		List<Agenda> list_agendas = Agenda.getRutaDia(getDataBase(), c);
		RutasMapaAdapter adapter = new RutasMapaAdapter(getApplicationContext(), list_agendas);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				markers.get(position).showInfoWindow();
				view.setSelected(true);
				view.setBackgroundResource(R.drawable.list_bckgrnd_selected);
				if(lastItem != null)
					lastItem.setBackgroundResource(R.drawable.list_bckgrnd);
				lastItem = view;
                if(markers.get(position).getPosition().latitude == 0 && markers.get(position).getPosition().longitude == 0)
                    Toast.makeText(getApplicationContext(), "Este cliente no tiene ingresada una geolocalización.", Toast.LENGTH_LONG).show();
                else
				    map.animateCamera(CameraUpdateFactory.newLatLngZoom(markers.get(position).getPosition(), 12), 1000, null);
			}
		});
		markers = new ArrayList<Marker>();
		
		for(int i = 0; i < list_agendas.size(); i ++)
		{
            if(list_agendas.get(i).getClienteDireccion() != null) {
                LatLng pos = new LatLng(list_agendas.get(i).getClienteDireccion().getLatitud(), list_agendas.get(i).getClienteDireccion().getLongitud());

                if(pos.longitude == 0 && pos.latitude == 0)
                {
                    Marker mark = map.addMarker(new MarkerOptions().position(pos)
                            .title(list_agendas.get(i).getCliente().getNombreCompleto().trim())
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_position, i + 1 + ""))));
                    mark.setAlpha(100);
                    //mark.showInfoWindow();
                    markers.add(mark);
                }
                else {
                    Marker mark = map.addMarker(new MarkerOptions().position(pos)
                            .title(list_agendas.get(i).getCliente().getNombreCompleto().trim())
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_position, i + 1 + ""))));
                    mark.showInfoWindow();
                    markers.add(mark);

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12), 2000, null);
                }

                if (markers.size() > 1) {
                    LatLng org = markers.get(markers.size() - 2).getPosition();
                    showRuta(org, pos);
                }
            }
		}
		super.onDailogDatePickerChange(id, c);
	}
	
	private void showRutaPorFecha() {
		ComoLlegar.setVisibility(View.GONE);
		Posicion.setVisibility(View.GONE);
		showDialogDatePicker(0);
		
	}
	
	private void showRuta(LatLng source, LatLng dest)
	{
		final String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude);
		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Fijando Ruta");
		dialog.show();
		
		Runnable runnable = new Runnable() {
		      @Override
		      public void run() {
		    	  final String resp = getJSONFromUrl(url);
		    	  Activity actv = (Activity)ctx;
		    	  actv.runOnUiThread(new Runnable()
			    	{

						@Override
						public void run() {
							dialog.dismiss();
							drawPath(resp);
							expand.setVisibility(View.VISIBLE);
							listview.setVisibility(View.VISIBLE);
							expand.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
										slideToBottom(listview, false);
										slideToBottom(expand, true);		
								}
							});
							
							collapse.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
										slideToTop(listview, false);
										slideToTop(expand, true);									
								}
							});
						}
			    		
			    	});
		        }
		      };
		new Thread(runnable).start();
	}
	
	public void SendEmail(View v)
	{
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
	            "mailto",agenda.getCliente().getCorreoElectronico(), null));
		startActivity(Intent.createChooser(intent, "Send Email"));
	}
	
	public void CallPhone(View v)
	{
		String uri = "tel:" + agenda.getClienteDireccion().getTelefono1();
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse(uri));
		Uri mUri = Uri.parse("smsto:" + Utils.convertToSMSNumber(agenda.getClienteDireccion().getTelefono1()));
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
        mIntent.putExtra("chat",true);
        Intent chooserIntent = Intent.createChooser(mIntent, "Seleccionar Acci�n");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent });
        startActivity(chooserIntent);
	}

	private void setRuta(long id) {
		ComoLlegar.setVisibility(View.GONE);
		Posicion.setVisibility(View.VISIBLE);
		map.clear();
		Location loc = LocationUtils.getLocation(this);
		agenda = Agenda.getAgenda(getDataBase(), id);
		ClienteDireccion cld = ClienteDireccion.getClienteDireccionIdDireccion(getDataBase(), agenda.getIdCliente(), agenda.getIdClienteDireccion());
		if(cld.getLongitud() != 0 && cld.getLatitud() != 0 && loc != null)
		{
			final String url = makeURL(loc.getLatitude(), loc.getLongitude(), cld.getLatitud(), cld.getLongitud());
			
			final LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());
			
			Marker mark = map.addMarker(new MarkerOptions().position(pos)
			        .title("Origen"));
			
			final LatLng pos2 = new LatLng(cld.getLatitud(), cld.getLongitud());
			
			Marker mark2 = map.addMarker(new MarkerOptions().position(pos2)
			        .title(agenda.getNombreCompleto().trim()));
			mark2.showInfoWindow();
			
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Fijando Ruta");
			dialog.show();
			
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos2, 12), 2000, null);
			
			setTextViewText(R.id.map_name, agenda.getNombreCompleto());
		    setTextViewText(R.id.map_phone, agenda.getClienteDireccion().getTelefono1());
		    setTextViewText(R.id.map_mail, agenda.getCliente().getCorreoElectronico());
		    
		    DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
					rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agenda.getCliente().getURLFoto(),
					(ImageView) findViewById(R.id.map_image));
			
			Runnable runnable = new Runnable() {
			      @Override
			      public void run() {
			    	  final String resp = getJSONFromUrl(url);
			    	  Activity actv = (Activity)ctx;
			    	  actv.runOnUiThread(new Runnable()
				    	{
	
							@Override
							public void run() {
								dialog.dismiss();
								drawPath(resp);
								expand.setVisibility(View.VISIBLE);
								persona.setVisibility(View.VISIBLE);
								expand.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
											slideToBottom(persona, false);
											slideToBottom(expand, true);								
									}
								});
								
								collapse.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View v) {
											slideToTop(persona, false);
											slideToTop(expand, true);									
									}
								});
							}
				    		
				    	});
			        }
			      };
			new Thread(runnable).start();
		}
		else
		{
			if(loc == null)
				showDialogMessage("Por favor, active su GPS de su dispositivo movil, y haga touch sobre el botón Como Llegar nuevamente.");
            else
                Toast.makeText(this,"Este cliente no tiene ingresada una geolocalización.", Toast.LENGTH_LONG).show();
		}
	}

    private void setRutaCliente(final long id) {
        ComoLlegar.setVisibility(View.GONE);
        Posicion.setVisibility(View.VISIBLE);
        RutasFechas.setVisibility(View.GONE);
        Posicion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setPosicionCliente(id);
            }
        });
        map.clear();
        Location loc = LocationUtils.getLocation(this);
        Cliente cli = Cliente.getClienteID(getDataBase(), id, true);
        ClienteDireccion cld = cli.getClienteDireccionPrincipal();
        if(cld.getLongitud() != 0 && cld.getLatitud() != 0 && loc != null)
        {
            final String url = makeURL(loc.getLatitude(), loc.getLongitude(), cld.getLatitud(), cld.getLongitud());

            final LatLng pos = new LatLng(loc.getLatitude(), loc.getLongitude());

            Marker mark = map.addMarker(new MarkerOptions().position(pos)
                    .title("Origen"));

            final LatLng pos2 = new LatLng(cld.getLatitud(), cld.getLongitud());

            Marker mark2 = map.addMarker(new MarkerOptions().position(pos2)
                    .title(cli.getNombreCompleto().trim()));
            mark2.showInfoWindow();

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Fijando Ruta");
            dialog.show();

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos2, 12), 2000, null);

            setTextViewText(R.id.map_name, cli.getNombreCompleto());
            setTextViewText(R.id.map_phone, cld.getTelefono1());
            setTextViewText(R.id.map_mail, cli.getCorreoElectronico());

            DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + cli.getURLFoto(),
                    (ImageView) findViewById(R.id.map_image));

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    final String resp = getJSONFromUrl(url);
                    Activity actv = (Activity)ctx;
                    actv.runOnUiThread(new Runnable()
                    {

                        @Override
                        public void run() {
                            dialog.dismiss();
                            drawPath(resp);
                            expand.setVisibility(View.VISIBLE);
                            persona.setVisibility(View.VISIBLE);
                            expand.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    slideToBottom(persona, false);
                                    slideToBottom(expand, true);
                                }
                            });

                            collapse.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    slideToTop(persona, false);
                                    slideToTop(expand, true);
                                }
                            });
                        }

                    });
                }
            };
            new Thread(runnable).start();
        }
        else
        {
            if(loc == null)
                showDialogMessage("Por favor, active su GPS de su dispositivo movil, y haga touch sobre el botón Como Llegar nuevamente.");
            else
                Toast.makeText(this,"Este cliente no tiene ingresada una geolocalización.", Toast.LENGTH_LONG).show();
        }
    }
	
	public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
 }

    private void setPosicionCliente(final long id) {
        ComoLlegar.setVisibility(View.VISIBLE);
        Posicion.setVisibility(View.GONE);
        RutasFechas.setVisibility(View.GONE);
        ComoLlegar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setRutaCliente(id);
            }
        });
        map.clear();
        Cliente cli = Cliente.getClienteID(getDataBase(), id, true);
        ClienteDireccion cld = cli.getClienteDireccionPrincipal();

        if (cld.getLatitud() == 0 && cld.getLongitud() == 0) {
            Toast.makeText(this, "Este cliente no tiene ingresada una geolocalización.", Toast.LENGTH_LONG).show();
        } else {

            LatLng pos = new LatLng(cld.getLatitud(), cld.getLongitud());

            Marker mark = map.addMarker(new MarkerOptions().position(pos)
                    .title(cli.getNombreCompleto().trim()));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        }

        DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + cli.getURLFoto(),
                (ImageView) findViewById(R.id.map_image));


        setTextViewText(R.id.map_name, cli.getNombreCompleto());
        setTextViewText(R.id.map_phone, cld.getTelefono1());
        setTextViewText(R.id.map_mail, cli.getCorreoElectronico());


        expand.setVisibility(View.VISIBLE);
        persona.setVisibility(View.VISIBLE);
        expand.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                slideToBottom(persona, false);
                slideToBottom(expand, true);
            }
        });

        collapse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                slideToTop(persona, false);
                slideToTop(expand, true);
            }
        });
    }

	private void setPosicion(long id) {
		ComoLlegar.setVisibility(View.VISIBLE);
		Posicion.setVisibility(View.GONE);
		map.clear();
		agenda = Agenda.getAgenda(getDataBase(), id);
        if(agenda == null) {
            agenda = Agenda.getAgendaClienteNull(getDataBase(), id);
            Toast.makeText(this,"Este cliente ya no existe en su ruta, por ende no se puede obtener su posicion.", Toast.LENGTH_LONG).show();
        }
        else {
            ClienteDireccion cld = ClienteDireccion.getClienteDireccionIdDireccion(getDataBase(), agenda.getIdCliente(), agenda.getIdClienteDireccion());

            if(cld.getLatitud() == 0 && cld.getLongitud() == 0)
            {
                Toast.makeText(this,"Este cliente no tiene ingresada una geolocalización.", Toast.LENGTH_LONG).show();
            }
            else {

                LatLng pos = new LatLng(cld.getLatitud(), cld.getLongitud());

                Marker mark = map.addMarker(new MarkerOptions().position(pos)
                        .title(agenda.getNombreCompleto().trim()));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
                map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }

            DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + agenda.getCliente().getURLFoto(),
                    (ImageView) findViewById(R.id.map_image));
        }
	    
	    setTextViewText(R.id.map_name, agenda.getNombreCompleto());
	    setTextViewText(R.id.map_phone, agenda.getClienteDireccion().getTelefono1());
	    setTextViewText(R.id.map_mail, agenda.getCliente().getCorreoElectronico());
	    

	    
	    expand.setVisibility(View.VISIBLE);
		persona.setVisibility(View.VISIBLE);
		expand.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					slideToBottom(persona, false);
					slideToBottom(expand, true);								
			}
		});
		
		collapse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					slideToTop(persona, false);
					slideToTop(expand, true);									
			}
		});
	}
	
	public void drawPath(String  result) {

        try {

               final JSONObject json = new JSONObject(result);
               JSONArray routeArray = json.getJSONArray("routes");
               JSONObject routes = routeArray.getJSONObject(0);
               JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
               String encodedString = overviewPolylines.getString("points");
               List<LatLng> list = decodePoly(encodedString);

               for(int z = 0; z<list.size()-1;z++){
                    LatLng src= list.get(z);
                    LatLng dest= list.get(z+1);
                    Polyline line = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                    .width(7).color(getResources().getColor(R.color.color_unvisited))
                    .geodesic(true));
                }

        } 
        catch (JSONException e) {

        }
    } 
	
	public String getJSONFromUrl(String url) {
		InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        try {
        	
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();           

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            json = sb.toString();
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return json;

    }
	
	private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                     (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
	
	public BitmapDescriptor getNumber(String number)
	{
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; 
		Bitmap bmp = Bitmap.createBitmap(200, 50, conf); 
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		canvas.drawText(number, 0, 50, paint); // paint defines the text color, stroke width, size
		return BitmapDescriptorFactory.fromBitmap(bmp);
	}
	
	private Bitmap writeTextOnDrawable(int drawableId, String text) {

	    Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
	            .copy(Bitmap.Config.ARGB_8888, true);

	    //Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

	    Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    paint.setColor(Color.WHITE);
	    //paint.setTypeface(tf);
	    paint.setTextAlign(Align.CENTER);
	    paint.setTextSize(getResources().getDimension(R.dimen.text_small_size));

	    Rect textRect = new Rect();
	    paint.getTextBounds(text, 0, text.length(), textRect);

	    Canvas canvas = new Canvas(bm);

	    //If the text is bigger than the canvas , reduce the font size
	    if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
	        paint.setTextSize(getResources().getDimension(R.dimen.text_small_size));        //Scaling needs to be used for different dpi's

	    //Calculate the positions
	    int xPos = (canvas.getWidth() / 2);     //-2 is for regulating the x position offset

	    //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
	    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;  

	    canvas.drawText(text, xPos, yPos, paint);

	    return  bm;
	}

}
