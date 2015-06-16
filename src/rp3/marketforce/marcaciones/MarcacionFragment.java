package rp3.marketforce.marcaciones;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.maps.utils.SphericalUtil;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.DiaLaboral;
import rp3.marketforce.models.marcacion.Marcacion;
import rp3.marketforce.models.marcacion.Permiso;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;

/**
 * Created by magno_000 on 05/06/2015.
 */
public class MarcacionFragment extends BaseFragment {

    private static final int PRESS_TIME = 2000;

    CountDownTimer count;
    JustificacionFragment fragment;
    private SimpleDateFormat format1, format2, format3, format4, format5;
    DateFormat format;

    public static MarcacionFragment newInstance() {
        return new MarcacionFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_marcacion);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DonutProgress) getRootView().findViewById(R.id.donut_inicio_jornada)).setMax(PRESS_TIME);
        getRootView().findViewById(R.id.button_inicio_jornada).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    count = new CountDownTimer(PRESS_TIME, 100) {

                        @Override
                        public void onTick(long l) {
                            ((DonutProgress) getRootView().findViewById(R.id.donut_inicio_jornada)).setProgress((int) (PRESS_TIME - l));
                        }

                        @Override
                        public void onFinish() {
                            ((DonutProgress) getRootView().findViewById(R.id.donut_inicio_jornada)).setProgress(PRESS_TIME);
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
                                                marc.setEnUbicacion(distance < 30);
                                                marc.setFecha(Calendar.getInstance().getTime());
                                                Marcacion.insert(getDataBase(), marc);
                                                if (distance < 30) {
                                                    DiaLaboral dia = DiaLaboral.getDia(getDataBase(), Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
                                                    if (dia.getHoraInicio2() != null)
                                                        SetButtonBreak();
                                                    SetButtonFinJornada();
                                                    getRootView().findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
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
                                                        getRootView().findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                                                        SetButtonFinJornada();
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_MARCACION);
                                                        requestSync(bundle);
                                                    }
                                                } else {
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
                    ((DonutProgress) getRootView().findViewById(R.id.donut_inicio_jornada)).setProgress(0);
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
                    getRootView().findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                    if (dia_laboral.getHoraInicio2() != null)
                        SetButtonBreak();
                    SetButtonFinJornada();
                } else if (ultimaMarcacion.getTipo().equalsIgnoreCase("J2")) {
                    getRootView().findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.layout_break).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.layout_fin_jornada).setVisibility(View.VISIBLE);
                    SetButtonFinBreak();
                } else if (ultimaMarcacion.getTipo().equalsIgnoreCase("J3")) {
                    getRootView().findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
                    SetButtonFinJornada();
                } else if (ultimaMarcacion.getTipo().equalsIgnoreCase("J4")) {
                    getRootView().findViewById(R.id.layout_inicio_jornada).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.layout_fin_jornada).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.layout_break).setVisibility(View.GONE);
                    getRootView().findViewById(R.id.marcacion_justificar).setVisibility(View.GONE);
                }
            }
        }
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

        String dia = "";
        Calendar hoy = Calendar.getInstance();
        dia = format1.format(hoy.getTime());
        dia = dia.substring(0, 1).toUpperCase() + dia.substring(1);
        String num = format2.format(hoy.getTime());
        String mes = format3.format(hoy.getTime());
        String anio = format4.format(hoy.getTime());
        DiaLaboral dialab = DiaLaboral.getDia(getDataBase(), Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
        if (dialab.getHoraFin2() == null)
            ((TextView) rootView.findViewById(R.id.marcacion_hora_fin)).setText(dialab.getHoraFin1().replace("h", ":"));
        else
            ((TextView) rootView.findViewById(R.id.marcacion_hora_fin)).setText(dialab.getHoraFin2().replace("h", ":"));

        ((TextView) rootView.findViewById(R.id.marcacion_dia)).setText(dia);
        ((TextView) rootView.findViewById(R.id.marcacion_fecha)).setText(num + " / " + mes + " / " + anio);


        fragment = new JustificacionFragment();
        rootView.findViewById(R.id.marcacion_justificar).setOnClickListener(new View.OnClickListener() {
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
        getRootView().findViewById(R.id.layout_break).setVisibility(View.VISIBLE);
        ((DonutProgress) getRootView().findViewById(R.id.donut_break)).setMax(PRESS_TIME);
        getRootView().findViewById(R.id.button_break).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    count = new CountDownTimer(PRESS_TIME, 100) {

                        @Override
                        public void onTick(long l) {
                            ((DonutProgress) getRootView().findViewById(R.id.donut_break)).setProgress((int) (PRESS_TIME - l));
                        }

                        @Override
                        public void onFinish() {
                            ((DonutProgress) getRootView().findViewById(R.id.donut_break)).setProgress(PRESS_TIME);
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
                                                marc.setEnUbicacion(distance < 30);
                                                marc.setFecha(Calendar.getInstance().getTime());
                                                Marcacion.insert(getDataBase(), marc);
                                                if (distance < 30) {
                                                    getRootView().findViewById(R.id.layout_break).setVisibility(View.GONE);
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
                    ((DonutProgress) getRootView().findViewById(R.id.donut_break)).setProgress(0);
                }
                return false;
            }
        });
    }

    public void SetButtonFinBreak() {
        getRootView().findViewById(R.id.layout_fin_break).setVisibility(View.VISIBLE);
        ((DonutProgress) getRootView().findViewById(R.id.donut_fin_break)).setMax(PRESS_TIME);
        getRootView().findViewById(R.id.button_fin_break).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    count = new CountDownTimer(PRESS_TIME, 100) {

                        @Override
                        public void onTick(long l) {
                            ((DonutProgress) getRootView().findViewById(R.id.donut_fin_break)).setProgress((int) (PRESS_TIME - l));
                        }

                        @Override
                        public void onFinish() {
                            ((DonutProgress) getRootView().findViewById(R.id.donut_fin_break)).setProgress(PRESS_TIME);
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
                                                marc.setEnUbicacion(distance < 30);
                                                marc.setFecha(Calendar.getInstance().getTime());
                                                Marcacion.insert(getDataBase(), marc);
                                                if (distance < 30) {
                                                    getRootView().findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
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
                    ((DonutProgress) getRootView().findViewById(R.id.donut_fin_break)).setProgress(0);
                }
                return false;
            }
        });
    }

    public void SetButtonFinJornada() {
        getRootView().findViewById(R.id.layout_fin_jornada).setVisibility(View.VISIBLE);
        ((DonutProgress) getRootView().findViewById(R.id.donut_fin_jornada)).setMax(PRESS_TIME);
        getRootView().findViewById(R.id.button_fin_jornada).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    count = new CountDownTimer(PRESS_TIME, 100) {

                        @Override
                        public void onTick(long l) {
                            ((DonutProgress) getRootView().findViewById(R.id.donut_fin_jornada)).setProgress((int) (PRESS_TIME - l));
                        }

                        @Override
                        public void onFinish() {
                            ((DonutProgress) getRootView().findViewById(R.id.donut_fin_jornada)).setProgress(PRESS_TIME);
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
                                                marc.setEnUbicacion(distance < 30);
                                                marc.setFecha(Calendar.getInstance().getTime());
                                                Marcacion.insert(getDataBase(), marc);
                                                if (distance < 30) {
                                                    DiaLaboral dia = DiaLaboral.getDia(getDataBase(), Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
                                                    Calendar cal_hoy = Calendar.getInstance();
                                                    try {
                                                        cal_hoy.setTime(format.parse(dia.getHoraInicio1().replace("h",":")));
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
                                                        getRootView().findViewById(R.id.layout_fin_jornada).setVisibility(View.GONE);
                                                        getRootView().findViewById(R.id.layout_fin_break).setVisibility(View.GONE);
                                                        getRootView().findViewById(R.id.layout_break).setVisibility(View.GONE);
                                                        getRootView().findViewById(R.id.marcacion_justificar).setVisibility(View.GONE);
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
                    ((DonutProgress) getRootView().findViewById(R.id.donut_fin_jornada)).setProgress(0);
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
}
