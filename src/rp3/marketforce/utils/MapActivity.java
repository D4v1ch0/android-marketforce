package rp3.marketforce.utils;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;

/**
 * Created by magno_000 on 08/07/2015.
 */
public class MapActivity extends BaseActivity {

    private GoogleMap map;
    private static View view;
    private LatLng sup;
    private SupportMapFragment mapFragment;

    public static final String ARG_LATITUD = "latitud";
    public static final String ARG_LONGITUD = "longitud";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_map);
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
                        setMapa();

                    }
                });

            }
        }, 1000);
    }

    private void setMapa() {
        // map = ((MapActivity) getActivity().getFragmentManager().findFragmentById(R.id.recorrido_map)).getMap();
        map.clear();
        double latitud = getIntent().getExtras().getDouble(ARG_LATITUD);
        double longitud = getIntent().getExtras().getDouble(ARG_LONGITUD);
        sup = new LatLng(latitud, longitud);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sup, Contants.ZOOM), 1, null);
        map.addMarker(new MarkerOptions().position(sup));

    }
}
