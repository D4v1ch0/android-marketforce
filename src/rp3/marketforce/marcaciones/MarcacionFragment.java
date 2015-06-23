package rp3.marketforce.marcaciones;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.maps.utils.SphericalUtil;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.DiaLaboral;
import rp3.marketforce.models.marcacion.Marcacion;
import rp3.marketforce.models.marcacion.Permiso;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;
import rp3.widget.DigitalClock;

/**
 * Created by magno_000 on 05/06/2015.
 */
public class MarcacionFragment extends BaseFragment {

    private static final int PRESS_TIME = 2000;
    private double DISTANCE = 0;

    CountDownTimer count;
    JustificacionFragment fragment;
    private SimpleDateFormat format1, format2, format3, format4, format5;
    DateFormat format;
    private ViewPager PagerDetalles;
    private DetailsPageAdapter pagerAdapter;
    private FrameLayout marcaciones, historico;

    public static MarcacionFragment newInstance() {
        return new MarcacionFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_marcacion_pager, R.menu.fragment_marcaciones_menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ImageView) getRootView().findViewById(R.id.point_hoy)).setImageResource(R.drawable.circle_reprogramed);
        ((ImageView) getRootView().findViewById(R.id.point_semana)).setImageResource(R.drawable.circle_shape);

        List<DiaMarcacion> marcacionList = getDiasMarcaciones();
        if(marcacionList.size() > 0) {
            MarcacionAdapter adapter = new MarcacionAdapter(this.getContext(), marcacionList);
            ((ListView) historico.findViewById(R.id.list_marcaciones)).setAdapter(adapter);
            historico.findViewById(R.id.historico_empty).setVisibility(View.GONE);
        }
        else
        {
            historico.findViewById(R.id.list_marcaciones).setVisibility(View.GONE);
        }

        PagerDetalles = (ViewPager) getRootView().findViewById(R.id.dashboard_graphics_pager);
        pagerAdapter = new DetailsPageAdapter();
        pagerAdapter.addView(marcaciones);
        pagerAdapter.addView(historico);
        PagerDetalles.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        ((ImageView) getRootView().findViewById(R.id.point_hoy)).setImageResource(R.drawable.circle_reprogramed);
                        ((ImageView) getRootView().findViewById(R.id.point_semana)).setImageResource(R.drawable.circle_shape);
                        break;
                    case 1:
                        ((ImageView) getRootView().findViewById(R.id.point_semana)).setImageResource(R.drawable.circle_reprogramed);
                        ((ImageView) getRootView().findViewById(R.id.point_hoy)).setImageResource(R.drawable.circle_shape);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        PagerDetalles.setAdapter(pagerAdapter);
        PagerDetalles.setCurrentItem(0);
        ((DigitalClock) marcaciones.findViewById(R.id.digitalClock)).setCalendar(Calendar.getInstance());
        DISTANCE = Double.parseDouble(PreferenceManager.getString(Contants.KEY_MARACIONES_DISTANCIA));
        Permiso ausencia = Permiso.getAusencia(getDataBase());
        if(ausencia == null) {
            ((DonutProgress) marcaciones.findViewById(R.id.donut_inicio_jornada)).setMax(PRESS_TIME);
            marcaciones.findViewById(R.id.button_inicio_jornada).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                        count = new CountDownTimer(PRESS_TIME, 100) {

                            @Override
                            public void onTick(long l) {
                                ((DonutProgress) marcaciones.findViewById(R.id.donut_inicio_jornada)).setProgress((int) (PRESS_TIME - l));
                            }

                            @Override
                            public void onFinish() {
                                ((DonutProgress) marcaciones.findViewById(R.id.donut_inicio_jornada)).setProgress(PRESS_TIME);
                                final Marcacion marc = new Marcacion();
                                marc.setTipo("J1"); //falta tipo
                                marc.setPendiente(true);
                                if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

                                    try {
                                        ((BaseActivity) getActivity()).showDialogProgress("GPS", "Obteniendo Posición");
                                        LocationUtils.getLocation(getContext(), new LocationUtils.OnLocationResultListener() {

                                            @Override
                                            public void getLocationResult(Location location) {
                                                if (location != null) {
                                                    marc.setLatitud(location.getLatitude());
                                                    marc.setLongitud(location.getLongitude());
                                                    LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                                                    LatLng partida = new LatLng(Double.parseDouble(PreferenceManager.getString(Contants.KEY_LATITUD_PARTIDA)),
                                                            Double.parseDouble(PreferenceManager.getString(Contants.KEY_LONGITUD_PARTIDA)));
                                                    double distance = SphericalUtil.computeDistanceBetween(pos, partida);
                                                    marc.setEnUbicacion(distance < DISTANCE);
                                                    marc.setFecha(Calendar.getInstance().getTime());
                                                    Marcacion.insert(getDataBase(), marc);
                                                    Toast.makeText(getContext(), "Se ha iniciado la Jornada.", Toast.LENGTH_LONG).show();
                                                    if (distance < DISTANCE) {
                                                        DiaLaboral dia = DiaLaboral.getDia(getDataBase(), Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
                                                        if (dia.getHoraInicio2() != null)
                                                            SetButtonBreak();
                                                        SetButtonFinJornada();
                                                        marcaciones.findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                                                        Calendar cal_hoy = Calendar.getInstance();
                                                        try {
                                                            cal_hoy.setTime(format.parse(dia.getHoraInicio1().replace("h", ":")));
                                                        } catch (Exception ex) {
                                                        }
                                                        int atraso = CheckMinutes(cal_hoy);
                                                        if (atraso > 0) {
                                                            marc.setMintutosAtraso(atraso);
                                                            Marcacion.update(getDataBase(), marc);
                                                            Permiso permiso = Permiso.getPermisoMarcacion(getDataBase(), 0);
                                                            if (permiso == null) {
                                                                fragment.idMarcacion = marc.getID();
                                                                showDialogFragment(fragment, "Justificacion");
                                                                Toast.makeText(getContext(), "Usted esta marcando atrasado. Indique su justificación", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                permiso.setIdMarcacion(marc.getID());
                                                                Permiso.update(getDataBase(), permiso);
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_MARCACION);
                                                                requestSync(bundle);
                                                            }
                                                        } else {
                                                            marcaciones.findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                                                            SetButtonFinJornada();
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_MARCACION);
                                                            requestSync(bundle);
                                                        }
                                                    } else {
                                                        DiaLaboral dia = DiaLaboral.getDia(getDataBase(), Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
                                                        Calendar cal_hoy = Calendar.getInstance();
                                                        try {
                                                            cal_hoy.setTime(format.parse(dia.getHoraInicio1().replace("h", ":")));
                                                        } catch (Exception ex) {
                                                        }
                                                        int atraso = CheckMinutes(cal_hoy);
                                                        if(atraso > 0) {
                                                            marc.setMintutosAtraso(atraso);
                                                            Marcacion.update(getDataBase(), marc);
                                                        }
                                                        fragment.idMarcacion = marc.getID();
                                                        showDialogFragment(fragment, "Justificacion");
                                                        Toast.makeText(getContext(), "Esta marcando fuera de su punto de partida. Ingrese una justificación.", Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(getContext(), "Debe de activar su GPS.", Toast.LENGTH_SHORT).show();
                                                }
                                                ((BaseActivity) getActivity()).closeDialogProgress();
                                            }
                                        });
                                    } catch (Exception ex) {
                                    }

                                }

                            }
                        }.start();
                    }
                    if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                        count.cancel();
                        ((DonutProgress) marcaciones.findViewById(R.id.donut_inicio_jornada)).setProgress(0);
                    }
                    return false;
                }
            });

            Marcacion ultimaMarcacion = Marcacion.getUltimaMarcacion(getDataBase());
            if (ultimaMarcacion != null) {
                Calendar dia_hoy = Calendar.getInstance();
                Calendar dia_marcacion = Calendar.getInstance();
                DiaLaboral dia_laboral = DiaLaboral.getDia(getDataBase(), Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
                dia_marcacion.setTime(ultimaMarcacion.getFecha());
                if (dia_hoy.get(Calendar.DAY_OF_YEAR) == dia_marcacion.get(Calendar.DAY_OF_YEAR)) {
                    if (ultimaMarcacion.getTipo().equalsIgnoreCase("J1")) {
                        marcaciones.findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                        if (dia_laboral.getHoraInicio2() != null)
                            SetButtonBreak();
                        SetButtonFinJornada();
                    } else if (ultimaMarcacion.getTipo().equalsIgnoreCase("J2")) {
                        marcaciones.findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                        marcaciones.findViewById(R.id.layout_break).setVisibility(View.GONE);
                        marcaciones.findViewById(R.id.layout_fin_jornada).setVisibility(View.VISIBLE);
                        SetButtonFinBreak();
                    } else if (ultimaMarcacion.getTipo().equalsIgnoreCase("J3")) {
                        marcaciones.findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                        marcaciones.findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
                        SetButtonFinJornada();
                    } else if (ultimaMarcacion.getTipo().equalsIgnoreCase("J4")) {
                        marcaciones.findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                        marcaciones.findViewById(R.id.layout_fin_jornada).setVisibility(View.GONE);
                        marcaciones.findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
                        marcaciones.findViewById(R.id.layout_break).setVisibility(View.GONE);
                        marcaciones.findViewById(R.id.marcacion_justificar).setVisibility(View.GONE);
                    }
                }
            }
        }
        else
        {
            marcaciones.findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
            marcaciones.findViewById(R.id.layout_fin_jornada).setVisibility(View.GONE);
            marcaciones.findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
            marcaciones.findViewById(R.id.layout_break).setVisibility(View.GONE);
            marcaciones.findViewById(R.id.marcacion_justificar).setVisibility(View.GONE);
        }
    }

    private List<DiaMarcacion> getDiasMarcaciones() {
        SimpleDateFormat formatFecha = new SimpleDateFormat("EEEE");
        format5 = new SimpleDateFormat("dd/MM/yy");
        List<DiaMarcacion> list = new ArrayList<DiaMarcacion>();
        List<Marcacion> marcaciones = Marcacion.getMarcaciones(getDataBase());
        DiaMarcacion dia = new DiaMarcacion();
        Calendar cal = null;
        for(int i = 0; i < marcaciones.size(); i ++ )
        {
            Marcacion setter = marcaciones.get(i);
            if(cal == null) {
                cal = Calendar.getInstance();
                cal.setTime(setter.getFecha());
            }

            Calendar hoy = Calendar.getInstance();
            hoy.setTime(setter.getFecha());
            if(cal.get(Calendar.DAY_OF_YEAR) != hoy.get(Calendar.DAY_OF_YEAR))
            {
                list.add(dia);
                dia = new DiaMarcacion();
            }
            if(dia.dia == null)
            {
                dia.dia = formatFecha.format(setter.getFecha()).substring(0, 2).toUpperCase();
                dia.fecha = format5.format(setter.getFecha());
            }

            if(setter.getTipo().equals("J1"))
                dia.inicio_jornada1 = format.format(setter.getFecha());
            if(setter.getTipo().equals("J2"))
                dia.fin_jornada1 = format.format(setter.getFecha());
            if(setter.getTipo().equals("J3"))
                dia.inicio_jornada2 = format.format(setter.getFecha());
            if(setter.getTipo().equals("J4"))
                dia.fin_jornada2 = format.format(setter.getFecha());

            if(setter.getMintutosAtraso() > 0)
                dia.atraso = true;
        }
        list.add(dia);
        return list;
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        format1 = new SimpleDateFormat("EEEE");
        format2 = new SimpleDateFormat("dd");
        format3 = new SimpleDateFormat("MM");
        format4 = new SimpleDateFormat("yy");
        format5 = new SimpleDateFormat("aa");
        format = new SimpleDateFormat("HH:mm");

        marcaciones = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_marcacion, null);
        historico = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_marcaciones_historico, null);

        String dia = "";
        Calendar hoy = Calendar.getInstance();
        dia = format1.format(hoy.getTime());
        dia = dia.substring(0, 1).toUpperCase() + dia.substring(1);
        String num = format2.format(hoy.getTime());
        String mes = format3.format(hoy.getTime());
        String anio = format4.format(hoy.getTime());
        DiaLaboral dialab = DiaLaboral.getDia(getDataBase(), Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
        ((TextView) historico.findViewById(R.id.historico_inicio_primera)).setText(dialab.getHoraInicio1().replace("h", ":"));
        ((TextView) historico.findViewById(R.id.historico_fin_primera)).setText(dialab.getHoraFin1().replace("h", ":"));
        if (dialab.getHoraFin2() == null) {
            historico.findViewById(R.id.historico_segunda_jornada).setVisibility(View.GONE);
            ((TextView) marcaciones.findViewById(R.id.marcacion_hora_fin)).setText(dialab.getHoraFin1().replace("h", ":"));
        }
        else {
            ((TextView) historico.findViewById(R.id.historico_inicio_segunda)).setText(dialab.getHoraInicio2().replace("h", ":"));
            ((TextView) historico.findViewById(R.id.historico_fin_segunda)).setText(dialab.getHoraFin2().replace("h", ":"));
            ((TextView) marcaciones.findViewById(R.id.marcacion_hora_fin)).setText(dialab.getHoraFin2().replace("h", ":"));
        }

        ((TextView) marcaciones.findViewById(R.id.marcacion_dia)).setText(dia);
        ((TextView) marcaciones.findViewById(R.id.marcacion_fecha)).setText(num + " / " + mes + " / " + anio);


        fragment = new JustificacionFragment();
        marcaciones.findViewById(R.id.marcacion_justificar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFragment(fragment, "Justificacion");
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    public void SetButtonBreak() {
        marcaciones.findViewById(R.id.layout_break).setVisibility(View.VISIBLE);
        ((DonutProgress) marcaciones.findViewById(R.id.donut_break)).setMax(PRESS_TIME);
        marcaciones.findViewById(R.id.button_break).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    count = new CountDownTimer(PRESS_TIME, 100) {

                        @Override
                        public void onTick(long l) {
                            ((DonutProgress) marcaciones.findViewById(R.id.donut_break)).setProgress((int) (PRESS_TIME - l));
                        }

                        @Override
                        public void onFinish() {
                            ((DonutProgress) marcaciones.findViewById(R.id.donut_break)).setProgress(PRESS_TIME);
                            final Marcacion marc = new Marcacion();
                            marc.setTipo("J2"); //falta tipo
                            marc.setPendiente(true);
                            if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

                                try {
                                    ((BaseActivity) getActivity()).showDialogProgress("GPS", "Obteniendo Posición");
                                    LocationUtils.getLocation(getContext(), new LocationUtils.OnLocationResultListener() {

                                        @Override
                                        public void getLocationResult(Location location) {
                                            if (location != null) {
                                                marc.setLatitud(location.getLatitude());
                                                marc.setLongitud(location.getLongitude());
                                                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                                                LatLng partida = new LatLng(Double.parseDouble(PreferenceManager.getString(Contants.KEY_LATITUD_PARTIDA)),
                                                        Double.parseDouble(PreferenceManager.getString(Contants.KEY_LONGITUD_PARTIDA)));
                                                double distance = SphericalUtil.computeDistanceBetween(pos, partida);
                                                marc.setEnUbicacion(distance < DISTANCE);
                                                marc.setFecha(Calendar.getInstance().getTime());
                                                Marcacion.insert(getDataBase(), marc);
                                                Toast.makeText(getContext(), "Se ha iniciado el break.", Toast.LENGTH_LONG).show();
                                                if (distance < DISTANCE) {
                                                    marcaciones.findViewById(R.id.layout_break).setVisibility(View.GONE);
                                                    SetButtonFinBreak();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_MARCACION);
                                                    requestSync(bundle);
                                                } else {
                                                    fragment.idMarcacion = marc.getID();
                                                    showDialogFragment(fragment, "Justificacion");
                                                    Toast.makeText(getContext(), R.string.message_fuera_posicion, Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Debe de activar su GPS.", Toast.LENGTH_SHORT).show();
                                            }
                                            ((BaseActivity) getActivity()).closeDialogProgress();
                                        }
                                    });
                                } catch (Exception ex) {
                                }

                            }

                        }
                    }.start();
                }
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    count.cancel();
                    ((DonutProgress) marcaciones.findViewById(R.id.donut_break)).setProgress(0);
                }
                return false;
            }
        });
    }

    public void SetButtonFinBreak() {
        marcaciones.findViewById(R.id.layout_fin_break).setVisibility(View.VISIBLE);
        ((DonutProgress) marcaciones.findViewById(R.id.donut_fin_break)).setMax(PRESS_TIME);
        marcaciones.findViewById(R.id.button_fin_break).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    count = new CountDownTimer(PRESS_TIME, 100) {

                        @Override
                        public void onTick(long l) {
                            ((DonutProgress) marcaciones.findViewById(R.id.donut_fin_break)).setProgress((int) (PRESS_TIME - l));
                        }

                        @Override
                        public void onFinish() {
                            ((DonutProgress) marcaciones.findViewById(R.id.donut_fin_break)).setProgress(PRESS_TIME);
                            final Marcacion marc = new Marcacion();
                            marc.setTipo("J3"); //falta tipo
                            marc.setPendiente(true);
                            if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

                                try {
                                    ((BaseActivity) getActivity()).showDialogProgress("GPS", "Obteniendo Posición");
                                    LocationUtils.getLocation(getContext(), new LocationUtils.OnLocationResultListener() {

                                        @Override
                                        public void getLocationResult(Location location) {
                                            if (location != null) {
                                                marc.setLatitud(location.getLatitude());
                                                marc.setLongitud(location.getLongitude());
                                                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                                                LatLng partida = new LatLng(Double.parseDouble(PreferenceManager.getString(Contants.KEY_LATITUD_PARTIDA)),
                                                        Double.parseDouble(PreferenceManager.getString(Contants.KEY_LONGITUD_PARTIDA)));
                                                double distance = SphericalUtil.computeDistanceBetween(pos, partida);
                                                marc.setEnUbicacion(distance < DISTANCE);
                                                marc.setFecha(Calendar.getInstance().getTime());
                                                Marcacion.insert(getDataBase(), marc);
                                                Toast.makeText(getContext(), "Se ha terminado el break.", Toast.LENGTH_LONG).show();
                                                if (distance < DISTANCE) {
                                                    marcaciones.findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_MARCACION);
                                                    requestSync(bundle);
                                                } else {
                                                    fragment.idMarcacion = marc.getID();
                                                    showDialogFragment(fragment, "Justificacion");
                                                    Toast.makeText(getContext(), R.string.message_fuera_posicion, Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Debe de activar su GPS.", Toast.LENGTH_SHORT).show();
                                            }
                                            ((BaseActivity) getActivity()).closeDialogProgress();
                                        }
                                    });
                                } catch (Exception ex) {
                                }

                            }

                        }
                    }.start();
                }
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    count.cancel();
                    ((DonutProgress) marcaciones.findViewById(R.id.donut_fin_break)).setProgress(0);
                }
                return false;
            }
        });
    }

    public void SetButtonFinJornada() {
        marcaciones.findViewById(R.id.layout_fin_jornada).setVisibility(View.VISIBLE);
        ((DonutProgress) marcaciones.findViewById(R.id.donut_fin_jornada)).setMax(PRESS_TIME);
        marcaciones.findViewById(R.id.button_fin_jornada).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    count = new CountDownTimer(PRESS_TIME, 100) {

                        @Override
                        public void onTick(long l) {
                            ((DonutProgress) marcaciones.findViewById(R.id.donut_fin_jornada)).setProgress((int) (PRESS_TIME - l));
                        }

                        @Override
                        public void onFinish() {
                            ((DonutProgress) marcaciones.findViewById(R.id.donut_fin_jornada)).setProgress(PRESS_TIME);
                            final Marcacion marc = new Marcacion();
                            marc.setTipo("J4"); //falta tipo
                            marc.setPendiente(true);
                            if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

                                try {
                                    ((BaseActivity) getActivity()).showDialogProgress("GPS", "Obteniendo Posición");
                                    LocationUtils.getLocation(getContext(), new LocationUtils.OnLocationResultListener() {

                                        @Override
                                        public void getLocationResult(Location location) {
                                            if (location != null) {
                                                marc.setLatitud(location.getLatitude());
                                                marc.setLongitud(location.getLongitude());
                                                LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                                                LatLng partida = new LatLng(Double.parseDouble(PreferenceManager.getString(Contants.KEY_LATITUD_PARTIDA)),
                                                        Double.parseDouble(PreferenceManager.getString(Contants.KEY_LONGITUD_PARTIDA)));
                                                double distance = SphericalUtil.computeDistanceBetween(pos, partida);
                                                marc.setEnUbicacion(distance < DISTANCE);
                                                marc.setFecha(Calendar.getInstance().getTime());
                                                Marcacion.insert(getDataBase(), marc);
                                                Toast.makeText(getContext(), "Se ha finalizado la Jornada.", Toast.LENGTH_LONG).show();
                                                if (distance < DISTANCE) {
                                                    DiaLaboral dia = DiaLaboral.getDia(getDataBase(), Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
                                                    Calendar cal_hoy = Calendar.getInstance();
                                                    try {
                                                        if(dia.getHoraFin2() == null)
                                                            cal_hoy.setTime(format.parse(dia.getHoraFin1().replace("h",":")));
                                                        else
                                                            cal_hoy.setTime(format.parse(dia.getHoraFin2().replace("h",":")));
                                                    }
                                                    catch (Exception ex)
                                                    {}
                                                    int atraso = CheckMinutes(cal_hoy);
                                                    if(atraso < 0)
                                                    {
                                                        marc.setMintutosAtraso(atraso*(-1));
                                                        Marcacion.update(getDataBase(), marc);
                                                        fragment.idMarcacion = marc.getID();
                                                        showDialogFragment(fragment, "Justificacion");
                                                        Toast.makeText(getContext(), "Usted esta finalizando su jornada por adelantado. Indique su justificación", Toast.LENGTH_LONG).show();
                                                    }
                                                    else {
                                                        marcaciones.findViewById(R.id.layout_fin_jornada).setVisibility(View.GONE);
                                                        marcaciones.findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
                                                        marcaciones.findViewById(R.id.layout_break).setVisibility(View.GONE);
                                                        marcaciones.findViewById(R.id.marcacion_justificar).setVisibility(View.GONE);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_MARCACION);
                                                        requestSync(bundle);
                                                    }
                                                } else {
                                                    fragment.idMarcacion = marc.getID();
                                                    showDialogFragment(fragment, "Justificacion");
                                                    Toast.makeText(getContext(), R.string.message_fuera_posicion, Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Debe de activar su GPS.", Toast.LENGTH_SHORT).show();
                                            }
                                            ((BaseActivity) getActivity()).closeDialogProgress();
                                        }
                                    });
                                } catch (Exception ex) {
                                }

                            }

                        }
                    }.start();
                }
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    count.cancel();
                    ((DonutProgress) marcaciones.findViewById(R.id.donut_fin_jornada)).setProgress(0);
                }
                return false;
            }
        });
    }

    public int CheckMinutes(Calendar cal1)
    {
        Calendar hoy = Calendar.getInstance();
        int horas = hoy.get(Calendar.HOUR_OF_DAY) - cal1.get(Calendar.HOUR_OF_DAY);
        int minutos = hoy.get(Calendar.MINUTE) - cal1.get(Calendar.MINUTE);
        return (horas * 60) + minutos;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_syncronize:
                showDialogProgress(R.string.message_title_synchronizing, R.string.message_please_wait);
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_PENDIENTES_PERMISO);
                requestSync(bundle);
                break;
            case R.id.action_justificacion_previa:
                Intent intent = new Intent(this.getContext(), JustificacionPreviaActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        super.onSyncComplete(data, messages);
        if(data.get(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_UPLOAD_PENDIENTES_PERMISO)) {
            closeDialogProgress();
            onResume();
        }
    }

    public class DiaMarcacion
    {
        public String dia;
        public String fecha;
        public String inicio_jornada1;
        public String fin_jornada1;
        public String inicio_jornada2;
        public String fin_jornada2;
        public boolean atraso;
    }
}
