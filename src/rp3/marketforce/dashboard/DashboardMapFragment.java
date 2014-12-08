package rp3.marketforce.dashboard;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;

public class DashboardMapFragment extends BaseFragment{

	
	private GoogleMap map;
	List<Marker> markers;
	
	public static DashboardMapFragment newInstance() {
		DashboardMapFragment fragment = new DashboardMapFragment();
		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
//		setContentView(R.layout.fragment_client,R.menu.fragment_client);
		setContentView(R.layout.fragment_dashboard_map);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);				
	}
	
	@Override
	public void onStart() {		
		super.onStart();
			
	}
	
	public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
    	super.onFragmentCreateView(rootView, savedInstanceState);
    	map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
    	
    	Calendar cal = Calendar.getInstance();
    	List<Agenda> list_agendas = Agenda.getRutaDia(getDataBase(), Calendar.getInstance());
    	markers = new ArrayList<Marker>();
    	
    	for(int i = 0; i < list_agendas.size(); i ++)
		{
			LatLng pos = new LatLng(list_agendas.get(i).getClienteDireccion().getLatitud(), list_agendas.get(i).getClienteDireccion().getLongitud());
			
			Marker mark = map.addMarker(new MarkerOptions().position(pos)
			        .title(list_agendas.get(i).getCliente().getNombreCompleto().trim()));
			mark.showInfoWindow();
			markers.add(mark);
			
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12), 2000, null);
			
			if(i != 0)
			{
				LatLng org = new LatLng(list_agendas.get(i-1).getClienteDireccion().getLatitud(), list_agendas.get(i-1).getClienteDireccion().getLongitud());
				showRuta(org, pos);
			}
		}
	}
	
	private void showRuta(LatLng source, LatLng dest)
	{
		final String url = makeURL(source.latitude, source.longitude, dest.latitude, dest.longitude);
		
		final ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Fijando Ruta");
		dialog.show();
		
		Runnable runnable = new Runnable() {
		      @Override
		      public void run() {
		    	  final String resp = getJSONFromUrl(url);
		    	  Activity actv = (Activity)getActivity();
		    	  actv.runOnUiThread(new Runnable()
			    	{

						@Override
						public void run() {
							dialog.dismiss();
							drawPath(resp);
						}
			    		
			    	});
		        }
		      };
		new Thread(runnable).start();
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
}
