package rp3.marketforce.recorrido;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import rp3.app.BaseFragment;
import rp3.configuration.Configuration;
import rp3.configuration.PreferenceManager;
import rp3.maps.utils.SphericalUtil;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Ubicacion;
import rp3.marketforce.ruta.ContactsAgendaFragment;
import rp3.marketforce.ruta.CrearVisitaActivity;
import rp3.marketforce.ruta.MapaActivity;
import rp3.marketforce.ruta.MotivoNoVisitaFragment;
import rp3.marketforce.ruta.ReprogramarActivity;
import rp3.util.ConnectionUtils;

public class RecorridoFragment  extends BaseFragment {
	private static final String TAG = RecorridoFragment.class.getSimpleName();
	private GoogleMap map;
	private ArrayList<Marker> markers;
	private static View view;
	private Calendar cal;
	private SimpleDateFormat format1;
	private LatLng ult;
    private SupportMapFragment mapFragment;

    public static RecorridoFragment newInstance() {
		RecorridoFragment fragment = new RecorridoFragment();
		return fragment;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setContentView(R.layout.fragment_recorrido, R.menu.fragment_recorrido_menu);
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate...");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    if (view != null) {
            Log.d(TAG,"onCreteView...!=null");
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
            FragmentManager fm = getFragmentManager();
            mapFragment = SupportMapFragment
                    .newInstance();
            fm.beginTransaction()
                    .replace(R.id.recorrido_dummy, mapFragment).commit();
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    view.findViewById(R.id.progress_map).setVisibility(View.GONE);
                    closeDialogProgress();
                    setMapa();

                }
            });
	    }
        else {
            Log.d(TAG,"onCreateView...==null");
            try {
                view = inflater.inflate(R.layout.fragment_recorrido, container, false);
                showDialogProgress("Cargando", "Mostrando Mapa");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (isAdded()) {
                            FragmentManager fm = getFragmentManager();
                            mapFragment = SupportMapFragment
                                    .newInstance();
                            fm.beginTransaction()
                                    .replace(R.id.recorrido_dummy, mapFragment).commit();
                            mapFragment.getMapAsync(new OnMapReadyCallback() {
                                @Override
                                public void onMapReady(GoogleMap googleMap) {
                                    map = googleMap;
                                    view.findViewById(R.id.progress_map).setVisibility(View.GONE);
                                    closeDialogProgress();
                                    setMapa();

                                }
                            });
                        }
                    }
                }, 1000);

            } catch (InflateException e) {
	        /* map is already there, just return view as it is */
            }
        }
	    format1 = new SimpleDateFormat("EEEE dd");
	    cal = Calendar.getInstance();
	    return view;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_ver_ruta:
                if(!ConnectionUtils.isNetAvailable(getContext()))
                {
                    Toast.makeText(getContext(), "Sin Conexión. Active el acceso a internet para entrar a esta opción.", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent3 = new Intent(getActivity(), MapaActivity.class);
                    intent3.putExtra(MapaActivity.ACTION_TYPE, MapaActivity.ACTION_RUTAS);
                    startActivity(intent3);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);

	}
	
	private void setMapa()
	{
		ult = null;
		//map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.recorrido_map)).getMap();
		map.clear();
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Contants.LATITUD, Contants.LONGITUD), Contants.ZOOM), 1, null);
		
		if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - (cal.get(Calendar.DAY_OF_YEAR)) > 6)
			view.findViewById(R.id.recorrido_anterior).setVisibility(View.INVISIBLE);
		else
			view.findViewById(R.id.recorrido_anterior).setVisibility(View.VISIBLE);
		
		if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - (cal.get(Calendar.DAY_OF_YEAR)) <= 0)
			view.findViewById(R.id.recorrido_siguiente).setVisibility(View.INVISIBLE);
		else
			view.findViewById(R.id.recorrido_siguiente).setVisibility(View.VISIBLE);
		
		((TextView)view.findViewById(R.id.recorrido_dia)).setText(format1.format(cal.getTime()).substring(0, 1).toUpperCase() + format1.format(cal.getTime()).substring(1));
		view.findViewById(R.id.recorrido_siguiente).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				irSiguiente();
			}});
		
		view.findViewById(R.id.recorrido_anterior).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				irAnterior();
			}});

    	List<Ubicacion> list_ubicaciones = Ubicacion.getRecorrido(getDataBase(), cal);
    	markers = new ArrayList<Marker>();
    	int f = -1;
    	for(int i = 0; i < list_ubicaciones.size(); i ++)
		{
    		double distance = 31;
			LatLng pos = new LatLng(list_ubicaciones.get(i).getLatitud(), list_ubicaciones.get(i).getLongitud());
			
			if(ult != null)
				distance = SphericalUtil.computeDistanceBetween(pos, ult);
			else
				ult = pos;
			
			if(distance > 30 || i == list_ubicaciones.size() -1)
			{
                f++;
                if(i == 0)
                {
                    Marker mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerstart)));
                    mark.showInfoWindow();
                    markers.add(mark);
                }
                else if(i == list_ubicaciones.size() -1)
                {
                    Marker mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerend)));
                    mark.showInfoWindow();
                    markers.add(mark);
                }
                else
                {
                    Marker mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.markerpoint, f + ""))));
                    mark.showInfoWindow();
                    markers.add(mark);
                }

				
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12), 1, null);
				
				ult = pos;
			}
		}

        if(markers.size() > 1) {
            for (int i = 1; i < markers.size(); i++) {
                LatLng org = new LatLng(markers.get(i-1).getPosition().latitude, markers.get(i-1).getPosition().longitude);
                LatLng dest = new LatLng(markers.get(i).getPosition().latitude, markers.get(i).getPosition().longitude);
                showRuta(org, dest);
            }
        }
	}
	
	protected void irSiguiente() {
		cal.add(Calendar.DATE, 1);
		setMapa();
	}

	protected void irAnterior() {
		cal.add(Calendar.DATE, -1);
		setMapa();
	}

	private void showRuta(LatLng source, LatLng dest)
	{
		final String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude);
		
		Runnable runnable = new Runnable() {
		      @Override
		      public void run() {
		    	  final String resp = getJSONFromUrl(url);
		    	  Activity actv = (Activity)getActivity();
		    	  actv.runOnUiThread(new Runnable()
			    	{

						@Override
						public void run() {
                            try {
                                drawPath(resp);
                            }
                            catch (Exception ex)
                            {

                            }

						}
			    		
			    	});
		        }
		      };
		new Thread(runnable).start();
	}
	
	public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?key=");
        urlString.append(Configuration.getAppConfiguration().getWebKey());
        urlString.append("&origin=");// from
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
	
	private Bitmap writeTextOnDrawable(int drawableId, String text) {

	    Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
	            .copy(Bitmap.Config.ARGB_8888, true);

	    //Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

	    Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    paint.setColor(Color.BLUE);
	    //paint.setTypeface(tf);
	    paint.setTextAlign(Align.CENTER);
	    paint.setTextSize(getResources().getDimension(R.dimen.text_xxsmall_size));

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
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
