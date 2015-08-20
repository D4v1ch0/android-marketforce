package rp3.marketforce.radar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.maps.utils.SphericalUtil;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.AgenteUbicacion;
import rp3.marketforce.models.Ubicacion;
import rp3.marketforce.resumen.AgenteDetalleFragment;
import rp3.marketforce.ruta.MapaActivity;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;

/**
 * Created by magno_000 on 27/03/2015.
 */
public class RadarFragment extends BaseFragment {
    private GoogleMap map;
    private AgenteDetalleFragment agenteDetalleFragment;
    private ArrayList<Marker> markers;
    private static View view;
    private Calendar cal;
    private SimpleDateFormat format1;
    private LatLng sup;
    private SupportMapFragment mapFragment;

    public static RadarFragment newInstance() {
        RadarFragment fragment = new RadarFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
        setContentView(R.layout.fragment_radar, R.menu.fragment_radar_menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_radar, container, false);
            view.findViewById(R.id.radar_ubicaciones).setVisibility(View.VISIBLE);
            ((TextView)view.findViewById(R.id.radar_ubicacion1)).setText(PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_1, 1) + " hora(s)");
            ((TextView)view.findViewById(R.id.radar_ubicacion2)).setText(PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_2, 24) + " hora(s)");
            ((TextView)view.findViewById(R.id.radar_ubicacion3)).setText(PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_3, 48) + " hora(s)");
            ((TextView)view.findViewById(R.id.radar_ubicacion4)).setText("+ " + PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_3, 48) + " hora(s)");
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
                                if(sup == null)
                                    setMapa();
                                else
                                    SetOldPoints();

                            }
                        });
                    }
                }
            }, 1000);
        } catch (InflateException e) {
	        /* map is already there, just return view as it is */
        }
        format1 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        cal = Calendar.getInstance();

        return view;
    }

    private void SetOldPoints() {
        view.findViewById(R.id.radar_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AGENTES_UBICACION);
                requestSync(bundle);

            }
        });

        //map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.recorrido_map)).getMap();
        map.clear();
        final List<AgenteUbicacion> list_ubicaciones = AgenteUbicacion.getResumen(getDataBase());
        markers = new ArrayList<Marker>();
        map.addMarker(new MarkerOptions().position(sup));

        for (int i = 0; i < list_ubicaciones.size(); i++) {
            LatLng pos = new LatLng(list_ubicaciones.get(i).getLatitud(), list_ubicaciones.get(i).getLongitud());

            double distance = SphericalUtil.computeDistanceBetween(pos, sup);
            distance = distance / 1000;

            Marker mark = null;
            Date dt2 = Calendar.getInstance().getTime();

            long diff = dt2.getTime() - list_ubicaciones.get(i).getFecha().getTime();
            double diffHours = diff / (60 * 60 * 1000);
            if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_1, 1)) {
                mark = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_position, i + 1 + ""))));
                map.addPolyline(new PolylineOptions().add(pos,sup).color(getResources().getColor(R.color.color_pending)));
            }
            else if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_2, 24)) {
                mark = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_verde, i + 1 + ""))));
                map.addPolyline(new PolylineOptions().add(pos,sup).color(getResources().getColor(R.color.color_visited)));
            }
            else if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_3, 48)) {
                mark = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_naranja, i + 1 + ""))));
                map.addPolyline(new PolylineOptions().add(pos,sup).color(getResources().getColor(R.color.color_orange)));
            }
            else {
                mark = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_rojo, i + 1 + ""))));
                map.addPolyline(new PolylineOptions().add(pos,sup).color(getResources().getColor(R.color.color_unvisited)));
            }

            mark.showInfoWindow();
            mark.setTitle(list_ubicaciones.get(i).getNombres() + " " + list_ubicaciones.get(i).getApellidos() + " - " + format1.format(list_ubicaciones.get(i).getFecha()));
            mark.setSnippet(NumberFormat.getInstance().format(distance) + " kilometro(s)." + "\n" + "Touch para enviar notificación.");
            markers.add(mark);
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sup, 12), 2000, null);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int idAgente = list_ubicaciones.get(markers.indexOf(marker)).getIdAgente();
                agenteDetalleFragment = AgenteDetalleFragment.newInstance(idAgente);
                showDialogFragment(agenteDetalleFragment, "Agente", "Agente");
            }
        });
    }


    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_ver_agentes:
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(agenteDetalleFragment != null)
            agenteDetalleFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setMapa() {
       // map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.recorrido_map)).getMap();
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Contants.LATITUD, Contants.LONGITUD), Contants.ZOOM), 1, null);

        view.findViewById(R.id.radar_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);

                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AGENTES_UBICACION);
                requestSync(bundle);

            }
        });

        if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

            try
            {
                ((BaseActivity)getActivity()).showDialogProgress("GPS","Obteniendo Ubicación");
                LocationUtils.getLocation(getContext(), new LocationUtils.OnLocationResultListener() {

                    @Override
                    public void getLocationResult(Location location) {
                        if (location != null) {
                            sup = new LatLng(location.getLatitude(), location.getLongitude());
                            SetPoints();
                        } else {
                            Toast.makeText(getContext(), "Debe de activar su GPS.", Toast.LENGTH_SHORT).show();
                        }
                        ((BaseActivity) getActivity()).closeDialogProgress();
                    }
                });
            }
            catch(Exception ex)
            {	}

        }
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);
        if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equalsIgnoreCase(SyncAdapter.SYNC_TYPE_AGENTES_UBICACION)) {
            closeDialogProgress();
            setMapa();
        }

    }

    private void SetPoints()
    {
        final List<AgenteUbicacion> list_ubicaciones = AgenteUbicacion.getResumen(getDataBase());
        markers = new ArrayList<Marker>();
        map.addMarker(new MarkerOptions().position(sup));

        for (int i = 0; i < list_ubicaciones.size(); i++) {
            LatLng pos = new LatLng(list_ubicaciones.get(i).getLatitud(), list_ubicaciones.get(i).getLongitud());

            double distance = SphericalUtil.computeDistanceBetween(pos, sup);
            distance = distance / 1000;

            Marker mark = null;
            Date dt2 = Calendar.getInstance().getTime();

            long diff = dt2.getTime() - list_ubicaciones.get(i).getFecha().getTime();
            double diffHours = diff / (60 * 60 * 1000);
            if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_1, 1)) {
                mark = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_position, i + 1 + ""))));
                map.addPolyline(new PolylineOptions().add(pos,sup).color(getResources().getColor(R.color.color_pending)));
            }
            else if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_2, 24)) {
                mark = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_verde, i + 1 + ""))));
                map.addPolyline(new PolylineOptions().add(pos,sup).color(getResources().getColor(R.color.color_visited)));
            }
            else if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_3, 48)) {
                mark = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_naranja, i + 1 + ""))));
                map.addPolyline(new PolylineOptions().add(pos,sup).color(getResources().getColor(R.color.color_orange)));
            }
            else {
                mark = map.addMarker(new MarkerOptions().position(pos)
                        .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_rojo, i + 1 + ""))));
                map.addPolyline(new PolylineOptions().add(pos,sup).color(getResources().getColor(R.color.color_unvisited)));
            }

            mark.showInfoWindow();
            mark.setTitle(list_ubicaciones.get(i).getNombres() + " " + list_ubicaciones.get(i).getApellidos() + " - " + format1.format(list_ubicaciones.get(i).getFecha()));
            mark.setSnippet(NumberFormat.getInstance().format(distance) + " kilometro(s)." + "\n" + "Touch para enviar notificación.");
            markers.add(mark);
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sup, 12), 2000, null);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int idAgente = list_ubicaciones.get(markers.indexOf(marker)).getIdAgente();
                agenteDetalleFragment = AgenteDetalleFragment.newInstance(idAgente);
                showDialogFragment(agenteDetalleFragment, "Agente", "Agente");
            }
        });
    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        //Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        //paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
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
