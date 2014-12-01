package rp3.marketforce.ruta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.LocationUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MapaActivity extends BaseActivity {
	
	public static String ACTION_TYPE = "type";
	
	public static String ACTION_POSICION = "posicion";
	public static String ACTION_LLEGAR = "llegar";
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
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    ctx = this;
	    DManager = new DrawableManager();
	    setContentView(R.layout.layout_map_rutas);
	    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	        .getMap();
	    
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
	    
	    String action = getIntent().getExtras().getString(ACTION_TYPE);
	    
	    if(action.equalsIgnoreCase(ACTION_POSICION))
	    	setPosicion(getIntent().getExtras().getLong(ARG_AGENDA));
	    if(action.equalsIgnoreCase(ACTION_LLEGAR))
	    	setRuta(getIntent().getExtras().getLong(ARG_AGENDA));
	    if(action.equalsIgnoreCase(ACTION_RUTAS))
	    	showRutaPorFecha();
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
		showRutaPorFecha();
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
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		map.clear();
		persona.setVisibility(View.GONE);
		
		List<Agenda> list_agendas = Agenda.getRutaDia(getDataBase(), c);
		RutasMapaAdapter adapter = new RutasMapaAdapter(getApplicationContext(), list_agendas);
		listview.setAdapter(adapter);
		
		for(int i = 0; i < list_agendas.size(); i ++)
		{
			LatLng pos = new LatLng(list_agendas.get(i).getClienteDireccion().getLatitud(), list_agendas.get(i).getClienteDireccion().getLongitud());
			
			Marker mark = map.addMarker(new MarkerOptions().position(pos)
			        .title(list_agendas.get(i).getCliente().getNombreCompleto().trim()));
			mark.showInfoWindow();
			
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12), 2000, null);
			
			if(i != 0)
			{
				LatLng org = new LatLng(list_agendas.get(i-1).getClienteDireccion().getLatitud(), list_agendas.get(i-1).getClienteDireccion().getLongitud());
				showRuta(org, pos);
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
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(uri));
		startActivity(intent);
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
					rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(this, agenda.getCliente().getURLFoto()),
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

	private void setPosicion(long id) {
		ComoLlegar.setVisibility(View.VISIBLE);
		Posicion.setVisibility(View.GONE);
		map.clear();
		agenda = Agenda.getAgenda(getDataBase(), id);
		ClienteDireccion cld = ClienteDireccion.getClienteDireccionIdDireccion(getDataBase(), agenda.getIdCliente(), agenda.getIdClienteDireccion());
		
		LatLng pos = new LatLng(cld.getLatitud(), cld.getLongitud());
		
		Marker mark = map.addMarker(new MarkerOptions().position(pos)
		        .title(agenda.getNombreCompleto().trim()));
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
	    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	    
	    setTextViewText(R.id.map_name, agenda.getNombreCompleto());
	    setTextViewText(R.id.map_phone, agenda.getClienteDireccion().getTelefono1());
	    setTextViewText(R.id.map_mail, agenda.getCliente().getCorreoElectronico());
	    
	    DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
				rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(this, agenda.getCliente().getURLFoto()),
				(ImageView) findViewById(R.id.map_image));
	    
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
                    .width(10).color(getResources().getColor(R.color.bg_button_bg_main))
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

}
