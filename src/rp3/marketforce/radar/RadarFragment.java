package rp3.marketforce.radar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
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

import java.lang.reflect.Type;
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
public class RadarFragment extends BaseFragment implements AgenteRadarFragment.AgenteRadarDialogListener {
    private static final String TAG = RadarFragment.class.getSimpleName();
    private GoogleMap map;
    private boolean isRotated = false;
    private AgenteDetalleFragment agenteDetalleFragment;
    private ArrayList<Marker> markers;
    private ArrayList<Integer> notShow;
    private static View view;
    private Calendar cal;
    private SimpleDateFormat format1;
    private LatLng sup;
    private SupportMapFragment mapFragment;
    private boolean ub1 = true, ub2 = true, ub3 = true, ub4 = true;
    List<AgenteUbicacion> list_ubicaciones;

    public static RadarFragment newInstance() {
        RadarFragment fragment = new RadarFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
        if(notShow == null)
            notShow = new ArrayList<Integer>();
        setContentView(R.layout.fragment_radar, R.menu.fragment_radar_menu);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            isRotated = true;
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
            if(!ub1)((TextView) view.findViewById(R.id.radar_ubicacion1)).setTypeface(null, Typeface.NORMAL);
            if(!ub2)((TextView) view.findViewById(R.id.radar_ubicacion2)).setTypeface(null, Typeface.NORMAL);
            if(!ub3)((TextView) view.findViewById(R.id.radar_ubicacion3)).setTypeface(null, Typeface.NORMAL);
            if(!ub4)((TextView) view.findViewById(R.id.radar_ubicacion4)).setTypeface(null, Typeface.NORMAL);
            view.findViewById(R.id.radar_ubicacion1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((TextView) v).getTypeface() != null && ((TextView) v).getTypeface().isBold()) {
                        ((TextView) v).setTypeface(null, Typeface.NORMAL);
                        ub1 = false;
                    } else {
                        ((TextView) v).setTypeface(null, Typeface.BOLD);
                        ub1 = true;
                    }
                    SetOldPoints();
                }
            });
            view.findViewById(R.id.radar_ubicacion2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((TextView) v).getTypeface() != null && ((TextView) v).getTypeface().isBold()) {
                        ((TextView) v).setTypeface(null, Typeface.NORMAL);
                        ub2 = false;
                    } else {
                        ((TextView) v).setTypeface(null, Typeface.BOLD);
                        ub2 = true;
                    }
                    SetOldPoints();
                }
            });
            view.findViewById(R.id.radar_ubicacion3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((TextView) v).getTypeface() != null && ((TextView) v).getTypeface().isBold()) {
                        ((TextView) v).setTypeface(null, Typeface.NORMAL);
                        ub3 = false;
                    } else {
                        ((TextView) v).setTypeface(null, Typeface.BOLD);
                        ub3 = true;
                    }
                    SetOldPoints();
                }
            });
            view.findViewById(R.id.radar_ubicacion4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((TextView) v).getTypeface() != null && ((TextView) v).getTypeface().isBold()) {
                        ((TextView) v).setTypeface(null, Typeface.NORMAL);
                        ub4 = false;
                    } else {
                        ((TextView) v).setTypeface(null, Typeface.BOLD);
                        ub4 = true;
                    }
                    SetOldPoints();
                }
            });
            ((BaseActivity)getActivity()).showDialogProgress("GPS", "Cargando Ubicaciones");
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
                                view.findViewById(R.id.radar_refresh).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notShow = new ArrayList<Integer>();
                                        ub1 = true;
                                        ub2 = true;
                                        ub3 = true;
                                        ub4 = true;
                                        ((TextView) view.findViewById(R.id.radar_ubicacion1)).setTypeface(null, Typeface.BOLD);
                                        ((TextView) view.findViewById(R.id.radar_ubicacion2)).setTypeface(null, Typeface.BOLD);
                                        ((TextView) view.findViewById(R.id.radar_ubicacion3)).setTypeface(null, Typeface.BOLD);
                                        ((TextView) view.findViewById(R.id.radar_ubicacion4)).setTypeface(null, Typeface.BOLD);
                                        if(ConnectionUtils.isNetAvailable(getContext())) {
                                            ((BaseActivity)getActivity()).showDialogProgress("GPS", "Cargando Ubicaciones");
                                            Bundle bundle = new Bundle();
                                            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AGENTES_UBICACION);
                                            requestSync(bundle);
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), "Sin conexión. No se puede obtener últimas ubicaciones.", Toast.LENGTH_LONG).show();
                                            setMapa();
                                        }
                                    }
                                });
                                if(sup == null) {
                                    if(ConnectionUtils.isNetAvailable(getContext())) {

                                        Bundle bundle = new Bundle();
                                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AGENTES_UBICACION);
                                        requestSync(bundle);
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "Sin conexión. No se puede obtener últimas ubicaciones.", Toast.LENGTH_LONG).show();
                                        setMapa();
                                    }
                                }
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

        //map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.recorrido_map)).getMap();
        map.clear();
        if(list_ubicaciones == null)
            list_ubicaciones = AgenteUbicacion.getResumen(getDataBase());
        markers = new ArrayList<Marker>();
        map.addMarker(new MarkerOptions().position(sup));

        for (int i = 0; i < list_ubicaciones.size(); i++) {
            if(notShow.contains(list_ubicaciones.get(i).getIdAgente()))
                continue;
            LatLng pos = new LatLng(list_ubicaciones.get(i).getLatitud(), list_ubicaciones.get(i).getLongitud());

            double distance = SphericalUtil.computeDistanceBetween(pos, sup);
            distance = distance / 1000;

            Marker mark = null;
            Date dt2 = Calendar.getInstance().getTime();

            long diff = dt2.getTime() - list_ubicaciones.get(i).getFecha().getTime();
            double diffHours = diff / (60 * 60 * 1000);
            if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_1, 1)) {
                if(ub1) {
                    mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_position, i + 1 + ""))));
                    map.addPolyline(new PolylineOptions().add(pos, sup).color(getResources().getColor(R.color.color_pending)));
                }
            }
            else if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_2, 24)) {
                if(ub2) {
                    mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_verde, i + 1 + ""))));
                    map.addPolyline(new PolylineOptions().add(pos, sup).color(getResources().getColor(R.color.color_visited)));
                }
            }
            else if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_3, 48)) {
                if(ub3) {
                    mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_naranja, i + 1 + ""))));
                    map.addPolyline(new PolylineOptions().add(pos, sup).color(getResources().getColor(R.color.color_orange)));
                }
            }
            else {
                if(ub4) {
                    mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_rojo, i + 1 + ""))));
                    map.addPolyline(new PolylineOptions().add(pos, sup).color(getResources().getColor(R.color.color_unvisited)));
                }
            }

            if(mark != null) {
                mark.showInfoWindow();
                mark.setTitle(list_ubicaciones.get(i).getNombres() + " " + list_ubicaciones.get(i).getApellidos() + " - " + format1.format(list_ubicaciones.get(i).getFecha()));
                mark.setSnippet(NumberFormat.getInstance().format(distance) + " kilometro(s)." + "\n" + "Touch para enviar notificación.");
                markers.add(mark);
            }
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sup, 12), 2000, null);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int idAgente = 0;
                for (int i = 0; i < list_ubicaciones.size(); i++) {
                    String title = list_ubicaciones.get(i).getNombres() + " " + list_ubicaciones.get(i).getApellidos() + " - " + format1.format(list_ubicaciones.get(i).getFecha());
                    if(title.equalsIgnoreCase(marker.getTitle())) {
                        idAgente = list_ubicaciones.get(i).getIdAgente();
                    }
                }

                agenteDetalleFragment = AgenteDetalleFragment.newInstance(idAgente);
                showDialogFragment(agenteDetalleFragment, "Agente", "Agente");
            }
        });
        ((BaseActivity) getActivity()).closeDialogProgress();
    }


    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ver_agentes:
                AgenteRadarFragment fragment = AgenteRadarFragment.newInstance(notShow);
                showDialogFragment(fragment, "Agentes","Agentes");

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

        if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

            try
            {
                LocationUtils.getLocation(getContext(), 100, new LocationUtils.OnLocationResultListener() {

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
        Log.d(TAG,"onSyncComplete...");
        if(data.getString(SyncAdapter.ARG_SYNC_TYPE).equalsIgnoreCase(SyncAdapter.SYNC_TYPE_AGENTES_UBICACION)) {
            closeDialogProgress();
            setMapa();
        }

    }

    private void SetPoints()
    {
        if(list_ubicaciones == null)
            list_ubicaciones = AgenteUbicacion.getResumen(getDataBase());
        markers = new ArrayList<Marker>();
        map.addMarker(new MarkerOptions().position(sup));

        for (int i = 0; i < list_ubicaciones.size(); i++) {
            if(notShow.contains(list_ubicaciones.get(i).getIdAgente()))
                continue;
            LatLng pos = new LatLng(list_ubicaciones.get(i).getLatitud(), list_ubicaciones.get(i).getLongitud());

            double distance = SphericalUtil.computeDistanceBetween(pos, sup);
            distance = distance / 1000;

            Marker mark = null;
            Date dt2 = Calendar.getInstance().getTime();

            long diff = dt2.getTime() - list_ubicaciones.get(i).getFecha().getTime();
            double diffHours = diff / (60 * 60 * 1000);
            if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_1, 1)) {
                if(ub1) {
                    mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_position, i + 1 + ""))));
                    map.addPolyline(new PolylineOptions().add(pos, sup).color(getResources().getColor(R.color.color_pending)));
                }
            }
            else if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_2, 24)) {
                if(ub2) {
                    mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_verde, i + 1 + ""))));
                    map.addPolyline(new PolylineOptions().add(pos, sup).color(getResources().getColor(R.color.color_visited)));
                }
            }
            else if(diffHours < PreferenceManager.getInt(Contants.KEY_AGENTE_UBICACION_3, 48)) {
                if(ub3) {
                    mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_naranja, i + 1 + ""))));
                    map.addPolyline(new PolylineOptions().add(pos, sup).color(getResources().getColor(R.color.color_orange)));
                }
            }
            else {
                if(ub4) {
                    mark = map.addMarker(new MarkerOptions().position(pos)
                            .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.map_rojo, i + 1 + ""))));
                    map.addPolyline(new PolylineOptions().add(pos, sup).color(getResources().getColor(R.color.color_unvisited)));
                }
            }

            if(mark != null) {
                mark.showInfoWindow();
                mark.setTitle(list_ubicaciones.get(i).getNombres() + " " + list_ubicaciones.get(i).getApellidos() + " - " + format1.format(list_ubicaciones.get(i).getFecha()));
                mark.setSnippet(NumberFormat.getInstance().format(distance) + " kilometro(s)." + "\n" + "Touch para enviar notificación.");
                markers.add(mark);
            }
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sup, 12), 2000, null);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int idAgente = 0;
                for (int i = 0; i < list_ubicaciones.size(); i++) {
                    String title = list_ubicaciones.get(i).getNombres() + " " + list_ubicaciones.get(i).getApellidos() + " - " + format1.format(list_ubicaciones.get(i).getFecha());
                    if(title.equalsIgnoreCase(marker.getTitle())) {
                        idAgente = list_ubicaciones.get(i).getIdAgente();
                    }
                }

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

        return bm;
    }

    @Override
    public void onFinishAgenteRadarDialog(ArrayList<Integer> notShow) {
        this.notShow = notShow;
        SetOldPoints();
        Log.d(TAG,"onFinishAgenteRadarDialog...");
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
