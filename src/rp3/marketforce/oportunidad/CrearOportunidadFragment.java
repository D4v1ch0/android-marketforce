package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.GeopoliticalStructureAdapter;
import rp3.content.SimpleDictionaryAdapter;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.data.models.GeneralValue;
import rp3.data.models.GeopoliticalStructure;
import rp3.data.models.IdentificationType;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Campo;
import rp3.marketforce.models.Canal;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.models.oportunidad.Agente;
import rp3.marketforce.models.oportunidad.Etapa;
import rp3.marketforce.models.oportunidad.EtapaTarea;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadContacto;
import rp3.marketforce.models.oportunidad.OportunidadEtapa;
import rp3.marketforce.models.oportunidad.OportunidadFoto;
import rp3.marketforce.models.oportunidad.OportunidadResponsable;
import rp3.marketforce.models.oportunidad.OportunidadTarea;
import rp3.marketforce.ruta.CrearVisitaActivity;
import rp3.marketforce.ruta.CrearVisitaFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.Utils;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.IdentificationValidator;
import rp3.util.LocationUtils;
import rp3.widget.ViewPager;

/**
 * Created by magno_000 on 19/05/2015.
 */
public class CrearOportunidadFragment extends BaseFragment {

    private View view;
    public final static int REQ_CODE_SPEECH_INPUT = 1200;
    public final static int PHOTO_1 = 4;
    public final static int PHOTO_2 = 5;
    public final static int PHOTO_3 = 6;
    public boolean setData = false;

    public static CrearOportunidadFragment newInstance(long id) {
        Bundle arguments = new Bundle();
        arguments.putLong(CrearOportunidadActivity.ARG_ID, id);
        CrearOportunidadFragment fragment = new CrearOportunidadFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public final static int DIALOG_VISITA = 1;
    public final static int DIALOG_GPS = 2;

    private LinearLayout ContactosContainer, ResponsableContainer;
    private List<LinearLayout> listViewResponsables, listViewContactos;
    private List<Integer> listAgentesIds;
    private Location currentLoc;
    private long id = 0;
    private int tipo;
    Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
    boolean isClient;
    int posContact = -1;
    public Oportunidad oportunidad;
    public List<String> contactPhotos, photos;
    private FrameLayout contacto;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private DrawableManager DManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oportunidad = new Oportunidad();
        contactPhotos = new ArrayList<String>();
        photos = new ArrayList<String>();
        photos.add("");
        photos.add("");
        photos.add("");
        if (getArguments().containsKey(CrearOportunidadActivity.ARG_ID)) {
            id = getArguments().getLong(CrearOportunidadActivity.ARG_ID);
        }else if(savedInstanceState!=null){
            id = savedInstanceState.getLong(CrearOportunidadActivity.ARG_ID);
        }

        DManager = new DrawableManager();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tryEnableGooglePlayServices(true);
        setContentView(R.layout.fragment_crear_oportunidad, R.menu.fragment_crear_cliente);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:
                if(Validaciones())
                {
                    Grabar();
                    finish();
                }
                break;
            case R.id.action_cancel:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Grabar() {
        Oportunidad opt = new Oportunidad();
        if(id != 0)
            opt = Oportunidad.getOportunidadId(getDataBase(), id);
        opt.setDescripcion(((EditText) view.findViewById(R.id.oportunidad_nombre)).getText().toString());

        if(((EditText)view.findViewById(R.id.oportunidad_monto)).length() > 0)
            opt.setImporte(Double.parseDouble(((EditText) view.findViewById(R.id.oportunidad_monto)).getText().toString()));
        else
            opt.setImporte(0);

        opt.setCalificacion((int) ((RatingBar)view.findViewById(R.id.oportunidad_importancia)).getRating());
        opt.setEstado("A");
        opt.setIdEtapa(1);
        opt.setFechaCreacion(Calendar.getInstance().getTime());
        opt.setFechaUltimaGestion(Calendar.getInstance().getTime());
        opt.setObservacion(((EditText) view.findViewById(R.id.oportunidad_comentario)).getText().toString());
        opt.setReferencia(((EditText) view.findViewById(R.id.oportunidad_referencia)).getText().toString());
        opt.setDireccion(((EditText) view.findViewById(R.id.oportunidad_direccion)).getText().toString());
        opt.setTipoEmpresa(((EditText) view.findViewById(R.id.oportunidad_tipo)).getText().toString());
        opt.setTelefono1(((EditText) view.findViewById(R.id.oportunidad_movil)).getText().toString());
        opt.setTelefono2(((EditText) view.findViewById(R.id.oportunidad_fijo)).getText().toString());
        opt.setDireccionReferencia(((EditText) view.findViewById(R.id.oportunidad_direccion_referencia)).getText().toString());
        opt.setPaginaWeb(((EditText) view.findViewById(R.id.oportunidad_pagina_web)).getText().toString());
        opt.setCorreo(((EditText) view.findViewById(R.id.oportunidad_email)).getText().toString());
        opt.setLongitud(oportunidad.getLongitud());
        opt.setLatitud(oportunidad.getLatitud());
        opt.setProbabilidad(((SeekBar) view.findViewById(R.id.oportunidad_probabilidad)).getProgress());
        opt.setPendiente(true);


        if(opt.getID() == 0 )
            Oportunidad.insert(getDataBase(), opt);
        else
            Oportunidad.update(getDataBase(), opt);

        for(int i = 0; i < listViewResponsables.size(); i ++)
        {
            OportunidadResponsable responsable = new OportunidadResponsable();
            if(opt.getOportunidadResponsables()!= null && opt.getOportunidadResponsables().size() > i)
            {
                responsable = opt.getOportunidadResponsables().get(i);
            }
            responsable.set_idOportunidad((int) opt.getID());
            responsable.setIdOportunidad(opt.getIdOportunidad());
            responsable.setIdAgente(listAgentesIds.get(i));

            if(responsable.getID() == 0)
                OportunidadResponsable.insert(getDataBase(), responsable);
            else
                OportunidadResponsable.update(getDataBase(), responsable);
        }

        for(int i = 0; i < listViewContactos.size(); i ++)
        {
            OportunidadContacto cont = new OportunidadContacto();
            if(opt.getOportunidadContactos() != null && opt.getOportunidadContactos().size() > i)
            {
                cont = opt.getOportunidadContactos().get(i);
            }
            cont.set_idOportunidad((int) opt.getID());
            cont.setIdOportunidad(opt.getIdOportunidad());
            cont.setNombre(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_nombre)).getText().toString());
            cont.setCargo(((EditText)listViewContactos.get(i).findViewById(R.id.contacto_cargo)).getText().toString());
            cont.setURLFoto(contactPhotos.get(i));

            if(cont.getID() == 0)
                OportunidadContacto.insert(getDataBase(), cont);
            else
                OportunidadContacto.update(getDataBase(), cont);
        }

        for(int i = 0; i < photos.size(); i ++)
        {
            OportunidadFoto foto = new OportunidadFoto();
            if(opt.getOportunidadFotos() != null && opt.getOportunidadFotos().size() > i)
            {
                foto = opt.getOportunidadFotos().get(i);
            }
            foto.set_idOportunidad((int) opt.getID());
            foto.setIdOportunidad(opt.getIdOportunidad());
            foto.setURLFoto(photos.get(i));

            if(foto.getID() == 0)
                OportunidadFoto.insert(getDataBase(), foto);
            else
                OportunidadFoto.update(getDataBase(), foto);
        }

        if(id == 0) {
            List<EtapaTarea> etapaTareas = EtapaTarea.getEtapaTareas(getDataBase());
            for (EtapaTarea tarea : etapaTareas) {
                OportunidadTarea oportunidadTarea = new OportunidadTarea();
                oportunidadTarea.setEstado("P");
                oportunidadTarea.setIdTarea(tarea.getIdTarea());
                oportunidadTarea.setIdEtapa(tarea.getIdEtapa());
                oportunidadTarea.set_idOportunidad((int) opt.getID());
                oportunidadTarea.setOrden(tarea.getOrden());
                OportunidadTarea.insert(getDataBase(), oportunidadTarea);
            }

            List<Etapa> etapas = Etapa.getEtapas(getDataBase());
            for(Etapa etapa : etapas)
            {
                OportunidadEtapa oportunidadEtapa = new OportunidadEtapa();
                oportunidadEtapa.setEstado("P");
                oportunidadEtapa.setIdEtapa(etapa.getIdEtapa());
                oportunidadEtapa.set_idOportunidad((int) opt.getID());
                oportunidadEtapa.setObservacion("");
                OportunidadEtapa.insert(getDataBase(), oportunidadEtapa);
            }
        }

        if(ConnectionUtils.isNetAvailable(getActivity())) {
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_OPORTUNIDADES);
            requestSync(bundle);
        }
    }

    @Override
    public void onPositiveConfirmation(int id){
        super.onPositiveConfirmation(id);
        switch (id)
        {
            case DIALOG_GPS:
                SaveAddress();
                break;
        }


    }

    public void SaveAddress()
    {
        Geocoder geo = new Geocoder(this.getContext());
        try {
            List<Address> addr = geo.getFromLocation(currentLoc.getLatitude(), currentLoc.getLongitude(), 2);
            ((EditText)view.findViewById(R.id.oportunidad_direccion)).setText(addr.get(0).getFeatureName());
            ((EditText)view.findViewById(R.id.oportunidad_direccion_referencia)).setText(addr.get(1).getFeatureName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNegativeConfirmation(int id) {
        super.onNegativeConfirmation(id);
        switch (id)
        {
            case DIALOG_VISITA:
                finish();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            showDialogProgress("Cargando", "Mostrando Mapa");
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
                    SetData();
                }
            });
        }
        else {
            try {
                view = inflater.inflate(R.layout.fragment_crear_oportunidad, container, false);
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
                                    SetData();
                                }
                            });
                        }
                    }
                }, 1000);

            } catch (InflateException e) {
	        /* map is already there, just return view as it is */
            }
        }

        setRootView(view);
        return view;
    }

    public void SetData()
    {
        if(listAgentesIds == null) {
            listViewResponsables = new ArrayList<LinearLayout>();
            listViewContactos = new ArrayList<LinearLayout>();
            listAgentesIds = new ArrayList<Integer>();
        }

        ResponsableContainer = (LinearLayout) view.findViewById(R.id.oportunidad_responsables);
        view.findViewById(R.id.oportunidad_agregar_responsable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addResponsable();
            }
        });
        view.findViewById(R.id.oportunidad_ubicacion).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

                    try {
                        ((BaseActivity) getActivity()).showDialogProgress("GPS", "Obteniendo Posición");
                        LocationUtils.getLocation(getContext(), new LocationUtils.OnLocationResultListener() {

                            @Override
                            public void getLocationResult(Location location) {
                                if (location != null) {
                                    map.clear();
                                    LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
                                    Marker mark = map.addMarker(new MarkerOptions().position(pos)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_position)));
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12), 1, null);
                                    oportunidad.setLatitud(location.getLatitude());
                                    oportunidad.setLongitud(location.getLongitude());
                                    currentLoc = location;
                                    showDialogConfirmation(DIALOG_GPS, R.string.message_direccion_google, R.string.title_direccion);

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
        });

        ((TextView) view.findViewById(R.id.agregar_contacto)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addContacto();

            }
        });
        ContactosContainer = (LinearLayout) view.findViewById(R.id.oportunidad_contacto);
        if(listAgentesIds.size() <= 0 && id == 0)
            addThisAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));

        view.findViewById(R.id.oportunidad_foto1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(PHOTO_1);
            }
        });
        view.findViewById(R.id.oportunidad_foto2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(PHOTO_2);
            }
        });
        view.findViewById(R.id.oportunidad_foto3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(PHOTO_3);
            }
        });
        ((ImageView) getRootView().findViewById(R.id.voice_to_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
        ((SeekBar) view.findViewById(R.id.oportunidad_probabilidad)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView) view.findViewById(R.id.oportunidad_prob_label)).setText(i+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(id != 0)
            setDatosOportunidad();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable Ahora");
        try {
            getActivity().startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

    }


    private void setDatosOportunidad() {
        Oportunidad opt = new Oportunidad();
        if(!setData) {
            if (id != 0)
                opt = Oportunidad.getOportunidadId(getDataBase(), id);

            NumberFormat format = NumberFormat.getInstance();
            format.setMinimumFractionDigits(0);
            format.setMaximumFractionDigits(0);
            format.setGroupingUsed(false);

            ((EditText) view.findViewById(R.id.oportunidad_nombre)).setText(opt.getDescripcion());
            ((EditText) view.findViewById(R.id.oportunidad_monto)).setText(format.format(opt.getImporte()));

            ((RatingBar) view.findViewById(R.id.oportunidad_importancia)).setRating(opt.getCalificacion());
            ((EditText) view.findViewById(R.id.oportunidad_comentario)).setText(opt.getObservacion());
            ((EditText) view.findViewById(R.id.oportunidad_referencia)).setText(opt.getReferencia());
            ((EditText) view.findViewById(R.id.oportunidad_direccion)).setText(opt.getDireccion());
            ((EditText) view.findViewById(R.id.oportunidad_tipo)).setText(opt.getTipoEmpresa());
            ((EditText) view.findViewById(R.id.oportunidad_movil)).setText(opt.getTelefono1());
            ((EditText) view.findViewById(R.id.oportunidad_fijo)).setText(opt.getTelefono2());
            ((EditText) view.findViewById(R.id.oportunidad_direccion_referencia)).setText(opt.getDireccionReferencia());
            ((EditText) view.findViewById(R.id.oportunidad_pagina_web)).setText(opt.getPaginaWeb());
            ((EditText) view.findViewById(R.id.oportunidad_email)).setText(opt.getCorreo());
            oportunidad.setLongitud(opt.getLongitud());
            oportunidad.setLatitud(opt.getLatitud());
            ((SeekBar) view.findViewById(R.id.oportunidad_probabilidad)).setProgress(opt.getProbabilidad());
            LatLng pos = new LatLng(opt.getLatitud(), opt.getLongitud());
            Marker mark = map.addMarker(new MarkerOptions().position(pos)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_position)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12), 1, null);

            for (OportunidadResponsable resp : opt.getOportunidadResponsables())
                addThisAgente(resp.getIdAgente());
            for (OportunidadContacto opCont : opt.getOportunidadContactos())
                addContacto(opCont.getID());

            for (int i = 0; i < opt.getOportunidadFotos().size(); i++) {
                photos.set(i, opt.getOportunidadFotos().get(i).getURLFoto());
                switch (i) {
                    case 0:
                        DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + opt.getOportunidadFotos().get(i).getURLFoto(),
                                ((ImageButton) view.findViewById(R.id.oportunidad_foto1)));
                        ((ImageButton) view.findViewById(R.id.oportunidad_foto1)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        break;
                    case 1:
                        DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + opt.getOportunidadFotos().get(i).getURLFoto(),
                                ((ImageButton) view.findViewById(R.id.oportunidad_foto2)));
                        ((ImageButton) view.findViewById(R.id.oportunidad_foto2)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        break;
                    case 2:
                        DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + opt.getOportunidadFotos().get(i).getURLFoto(),
                                ((ImageButton) view.findViewById(R.id.oportunidad_foto3)));
                        ((ImageButton) view.findViewById(R.id.oportunidad_foto3)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        break;
                }
            }

            setData = true;
        }

    }

    protected void takePicture(final int idView) {
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
        captureIntent.putExtra("crop", "true");
        captureIntent.putExtra("aspectX", 2);
        captureIntent.putExtra("aspectY", 1);
        //captureIntent.putExtra("return-data", true);
        getActivity().startActivityForResult(captureIntent, idView);
    }

    private void addThisAgente(int id)
    {
        final LinearLayout responsable = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.rowlist_responsable, null);
        final int pos = listViewResponsables.size();
        ((ImageView) responsable.findViewById(R.id.eliminar_responsable)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listViewResponsables.remove(responsable);
                ResponsableContainer.removeView(responsable);
            }
        });
        Agente agt = Agente.getAgente(getDataBase(), id);
        ((TextView) responsable.findViewById(R.id.responsable_nombre)).setText(agt.getNombre());
        listAgentesIds.add(agt.getIdAgente());
        ResponsableContainer.addView(responsable);
        listViewResponsables.add(responsable);
    }

    private void addResponsable()
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Seleccione un agente");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.select_dialog_singlechoice);
        final List<Agente> agentes = Agente.getAgentes(getDataBase());
        for(Agente agt : agentes)
            arrayAdapter.add(agt.getNombre());

        builderSingle.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Agente choose = agentes.get(which);
                        final LinearLayout responsable = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.rowlist_responsable, null);
                        final int pos = listViewResponsables.size();
                        ((ImageView) responsable.findViewById(R.id.eliminar_responsable)).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                listViewResponsables.remove(responsable);
                                ResponsableContainer.removeView(responsable);
                            }
                        });
                        ((TextView) responsable.findViewById(R.id.responsable_nombre)).setText(choose.getNombre());
                        listAgentesIds.add(choose.getIdAgente());
                        ResponsableContainer.addView(responsable);
                        listViewResponsables.add(responsable);
                    }
                });
        builderSingle.show();
    }

    private void addContacto()
    {
        final LinearLayout contacto = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_crear_oportunidad_contacto, null);
        final int pos = listViewContactos.size();
        ((ImageView) contacto.findViewById(R.id.contacto_eliminar)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listViewContactos.remove(contacto);
                ContactosContainer.removeView(contacto);
                contactPhotos.remove(pos);
            }});
        contactPhotos.add("");
        contacto.findViewById(R.id.contacto_foto).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                posContact = pos;
                isClient = false;
                takePicture(1);
            }
        });
        ContactosContainer.addView(contacto);
        listViewContactos.add(contacto);
    }

    private void addContacto(long id)
    {
        OportunidadContacto opCont = OportunidadContacto.getContactoInt(getDataBase(), id);
        final LinearLayout contacto = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_crear_oportunidad_contacto, null);
        final int pos = listViewContactos.size();
        ((ImageView) contacto.findViewById(R.id.contacto_eliminar)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listViewContactos.remove(contacto);
                ContactosContainer.removeView(contacto);
                contactPhotos.remove(pos);
            }});
        contactPhotos.add("");
        contacto.findViewById(R.id.contacto_foto).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                posContact = pos;
                isClient = false;
                takePicture(1);
            }
        });
        ((EditText) contacto.findViewById(R.id.contacto_nombre)).setText(opCont.getNombre());
        ((EditText) contacto.findViewById(R.id.contacto_cargo)).setText(opCont.getCargo());
        DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + opCont.getURLFoto(),
                (ImageView) contacto.findViewById(R.id.contacto_foto));
        ((ImageButton) contacto.findViewById(R.id.contacto_foto)).setScaleType(ImageView.ScaleType.CENTER_CROP);
        contactPhotos.add(opCont.getURLFoto());
        ContactosContainer.addView(contacto);
        listViewContactos.add(contacto);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_SPEECH_INPUT) {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ((TextView) view.findViewById(R.id.oportunidad_comentario)).setText(result.get(0));
                }
            } else {
                Bitmap pree = null;
                if (data != null) {
                    if (data.getData() != null) {
                        try {
                            pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (data.getExtras().containsKey("data"))
                        pree = (Bitmap) data.getExtras().get("data");
                    else
                    {
                        try {
                            photo = Uri.parse(data.getAction());
                            pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else
                    try {
                        photo = Uri.parse(data.getAction());
                        pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (requestCode == PHOTO_1 || requestCode == PHOTO_2 || requestCode == PHOTO_3) {

                    if (requestCode == PHOTO_1) {
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto1)).setImageBitmap(pree);
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto1)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photos.set(0, Utils.SaveBitmap(pree, "edit_client1"));
                    }
                    if (requestCode == PHOTO_2) {
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto2)).setImageBitmap(pree);
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto2)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photos.set(1, Utils.SaveBitmap(pree, "edit_client2"));
                    }
                    if (requestCode == PHOTO_3) {
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto3)).setImageBitmap(pree);
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto3)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photos.set(2, Utils.SaveBitmap(pree, "edit_client3"));
                    }
                } else {
                    ((ImageButton) listViewContactos.get(posContact).findViewById(R.id.contacto_foto)).setImageBitmap(pree);
                    ((ImageButton) listViewContactos.get(posContact).findViewById(R.id.contacto_foto)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    contactPhotos.set(posContact, Utils.SaveBitmap(pree, "edit_contact" + posContact));
                }
            }
        }
    }

    public boolean Validaciones()
    {
        if(((TextView) view.findViewById(R.id.oportunidad_nombre)).length() <= 0)
        {
            Toast.makeText(getContext(), R.string.message_sin_descripcion, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
