package rp3.auna.utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import rp3.app.BaseActivity;
import rp3.auna.Contants;
import rp3.auna.R;

/**
 * Created by magno_000 on 08/07/2015.
 */
public class MapActivity extends BaseActivity {

    private static final String TAG = MapActivity.class.getSimpleName();
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
        Log.d(TAG,"onCreate...");
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
        Log.d(TAG,"setMapa...");
        // map = ((MapActivity) getActivity().getFragmentManager().findFragmentById(R.id.recorrido_map)).getMap();
        map.clear();
        double latitud = getIntent().getExtras().getDouble(ARG_LATITUD);
        double longitud = getIntent().getExtras().getDouble(ARG_LONGITUD);
        sup = new LatLng(latitud, longitud);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sup, Contants.ZOOM), 1, null);
        map.addMarker(new MarkerOptions().position(sup));

    }

    /**
     *
     * Ciclo de vida
     *
     */

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
