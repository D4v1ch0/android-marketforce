package rp3.berlin.oportunidad;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.data.models.GeneralValue;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.Agente;
import rp3.berlin.models.Canal;
import rp3.berlin.models.oportunidad.AgendaOportunidad;
import rp3.berlin.models.oportunidad.Etapa;
import rp3.berlin.models.oportunidad.EtapaTarea;
import rp3.berlin.models.oportunidad.Oportunidad;
import rp3.berlin.models.oportunidad.OportunidadBitacora;
import rp3.berlin.models.oportunidad.OportunidadContacto;
import rp3.berlin.models.oportunidad.OportunidadEtapa;
import rp3.berlin.models.oportunidad.OportunidadFoto;
import rp3.berlin.models.oportunidad.OportunidadResponsable;
import rp3.berlin.models.oportunidad.OportunidadTarea;
import rp3.berlin.models.oportunidad.OportunidadTipo;
import rp3.berlin.sync.SyncAdapter;
import rp3.berlin.utils.DrawableManager;
import rp3.berlin.utils.NothingSelectedSpinnerAdapter;
import rp3.berlin.utils.Utils;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.LocationUtils;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 19/05/2015.
 */
public class CrearOportunidadFragment extends BaseFragment implements AgenteFragment.EditAgentesListener, EtapasDefinicionFragment.EtapasDefinicionListener {

    private View view;
    public final static int REQ_CODE_SPEECH_INPUT = 1200;
    public final static int PHOTO_1 = 4;
    public final static int PHOTO_2 = 5;
    public final static int PHOTO_3 = 6;
    public boolean setData = false;
    public int idTipoOportunidad = -1;

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
    private List<Integer> listAgentesIds, listAgentesIdsDelete, listContactosIdsDelete;
    private List<OportunidadContacto> listContactos;
    private List<OportunidadEtapa> listEtapas;
    private List<OportunidadTipo> listTipos;
    private Location currentLoc;
    private long id = 0;
    private int tipo;
    Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
    boolean isClient, photoFlag = false;
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
                    if(id == 0)
                        EvaluarEtapas();
                    else
                    {
                        Grabar();
                        finish();
                    }
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

    private void EvaluarEtapas()
    {
        boolean hayVariable = false;
        List<Etapa> etapas = Etapa.getEtapasAll(getDataBase(), listTipos.get(((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).getSelectedItemPosition() - 1).getIdOportunidadTipo());
        for(Etapa etapa : etapas)
        {
            if(etapa.isEsVariable())
                hayVariable = true;
        }

        if(hayVariable)
        {
            EtapasDefinicionFragment fragment = EtapasDefinicionFragment.newInstance(listTipos.get(((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).getSelectedItemPosition() - 1).getIdOportunidadTipo());
            showDialogFragment(fragment, "Etapas a Definir","Etapas a Definir");
        }
        else
        {
            listEtapas = null;
            Grabar();
            finish();
        }

    }

    private void Grabar() {
        Oportunidad opt = new Oportunidad();
        opt.setIdOportunidadTipo(listTipos.get(((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).getSelectedItemPosition() - 1).getIdOportunidadTipo());
        if(id != 0)
            opt = Oportunidad.getOportunidadId(getDataBase(), id);
        else {
            opt.setEstado("A");
            Etapa etp = Etapa.getEtapaNext(getDataBase(), 1, opt.getIdOportunidadTipo());
            opt.setIdEtapa(etp.getIdEtapa());
            opt.setFechaCreacion(Calendar.getInstance().getTime());
        }
        opt.setDescripcion(((EditText) view.findViewById(R.id.oportunidad_nombre)).getText().toString());

        if(((EditText)view.findViewById(R.id.oportunidad_monto)).length() > 0)
            opt.setImporte(Double.parseDouble(((EditText) view.findViewById(R.id.oportunidad_monto)).getText().toString()));
        else
            opt.setImporte(0);

        opt.setCalificacion((int) ((RatingBar)view.findViewById(R.id.oportunidad_importancia)).getRating());
        opt.setFechaUltimaGestion(Calendar.getInstance().getTime());
        if(((EditText) view.findViewById(R.id.oportunidad_comentario)).length() == 0)
            opt.setObservacion("");
        else
            opt.setObservacion(((EditText) view.findViewById(R.id.oportunidad_comentario)).getText().toString());
        opt.setObservacion(((EditText) view.findViewById(R.id.oportunidad_comentario)).getText().toString());
        opt.setReferencia(((EditText) view.findViewById(R.id.oportunidad_referencia)).getText().toString());
        opt.setDireccion(((EditText) view.findViewById(R.id.oportunidad_direccion)).getText().toString());
        opt.setTipoEmpresa(((EditText) view.findViewById(R.id.oportunidad_tipo)).getText().toString());
        opt.setTelefono1(((EditText) view.findViewById(R.id.oportunidad_movil)).getText().toString());
        opt.setTelefono2(((EditText) view.findViewById(R.id.oportunidad_fijo)).getText().toString());
        opt.setDireccionReferencia(((EditText) view.findViewById(R.id.oportunidad_direccion_referencia)).getText().toString());
        opt.setPaginaWeb(((EditText) view.findViewById(R.id.oportunidad_pagina_web)).getText().toString());
        opt.setCorreo(((EditText) view.findViewById(R.id.oportunidad_email)).getText().toString());
        opt.setIdCanal((int) ((Spinner) getRootView().findViewById(R.id.oportunidad_canal)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.oportunidad_canal)).getSelectedItemPosition()));
        opt.setTipoPersona(((GeneralValue) ((Spinner) getRootView().findViewById(R.id.oportunidad_tipo_persona)).getSelectedItem()).getCode());
        opt.setLongitud(oportunidad.getLongitud());
        opt.setLatitud(oportunidad.getLatitud());
        opt.setProbabilidad(((SeekBar) view.findViewById(R.id.oportunidad_probabilidad)).getProgress());
        opt.setCanal(Canal.getCanal(getDataBase(), opt.getIdCanal()).getDescripcion());
        opt.setPendiente(true);


        if(opt.getID() == 0 ) {
            Oportunidad.insert(getDataBase(), opt);
            OportunidadBitacora bitacora = new OportunidadBitacora();
            bitacora.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            bitacora.setFecha(Calendar.getInstance().getTime());
            bitacora.setIdOportunidad(opt.getIdOportunidad());
            bitacora.set_idOportunidad((int) opt.getID());
            bitacora.setDetalle("Se creó oportunidad");
            bitacora.setIdOportunidadBitacora(1);
            OportunidadBitacora.insert(getDataBase(), bitacora);

            AgendaOportunidad agd = new AgendaOportunidad();
            agd.set_idOportunidad((int)opt.getID());
            agd.setIdOportunidad(opt.getIdOportunidad());
            agd.setPendiente(true);
            agd.setEstado(Contants.ESTADO_GESTIONANDO);
            agd.setFechaInicio(Calendar.getInstance().getTime());
            agd.setDescripcion(opt.getDescripcion());
            agd.setDireccion(opt.getDireccion());
            agd.setEmail(opt.getCorreo());
            AgendaOportunidad.insert(getDataBase(), agd);
        }
        else {
            Oportunidad.update(getDataBase(), opt);
            OportunidadBitacora bitacora = new OportunidadBitacora();
            bitacora.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));
            bitacora.setFecha(Calendar.getInstance().getTime());
            bitacora.setIdOportunidad(opt.getIdOportunidad());
            bitacora.set_idOportunidad((int) opt.getID());
            bitacora.setDetalle("Se actualizó oportunidad");
            bitacora.setIdOportunidadBitacora(opt.getOportunidadBitacoras().size() + 1);
            OportunidadBitacora.insert(getDataBase(), bitacora);
        }

        for(int i = 0; i < listAgentesIds.size(); i ++)
        {
            OportunidadResponsable responsable = OportunidadResponsable.getResponsable(getDataBase(), opt.getIdOportunidad(), listAgentesIds.get(i));
            if(responsable == null)
            {
                responsable = new OportunidadResponsable();
                responsable.set_idOportunidad((int) opt.getID());
                responsable.setIdOportunidad(opt.getIdOportunidad());
                responsable.setIdAgente(listAgentesIds.get(i));
            }

            //PATCH
                try {
                    if(((TextView) listViewResponsables.get(i).findViewById(R.id.responsable_tipo)).getText().toString().equalsIgnoreCase("Gestor"))
                        responsable.setTipo("G");
                    if(((TextView) listViewResponsables.get(i).findViewById(R.id.responsable_tipo)).getText().toString().equalsIgnoreCase("Interesado"))
                        responsable.setTipo("I");
                    if(((TextView) listViewResponsables.get(i).findViewById(R.id.responsable_tipo)).getText().toString().equalsIgnoreCase("Creador"))
                        responsable.setTipo("C");
                    if(((TextView) listViewResponsables.get(i).findViewById(R.id.responsable_tipo)).getText().toString().equalsIgnoreCase("Responsable"))
                        responsable.setTipo("R");
                }
                catch (Exception ex)
                {
                    if(!responsable.getTipo().equalsIgnoreCase("C"))
                        responsable.setTipo("G");
                }
            //END PATCH



            if(responsable.getID() == 0)
                OportunidadResponsable.insert(getDataBase(), responsable);
            else
                OportunidadResponsable.update(getDataBase(), responsable);
        }

        if(id != 0)
        {
            OportunidadResponsable.deleteResponsable(getDataBase(), opt.getIdOportunidad(), 0);
            for(int ir = 0; ir < listAgentesIdsDelete.size(); ir++)
                OportunidadResponsable.deleteResponsable(getDataBase(), opt.getIdOportunidad(), listAgentesIdsDelete.get(ir));
        }

        int posPhoto = 0;
        for(int i = 0; i < listViewContactos.size(); i ++)
        {
            OportunidadContacto cont = new OportunidadContacto();
            if(opt.getOportunidadContactos() != null && opt.getOportunidadContactos().size() > i)
            {
                cont = opt.getOportunidadContactos().get(i);
            }
            cont.setIdOportunidadContacto(i + 1);
            cont.set_idOportunidad((int) opt.getID());
            cont.setIdOportunidad(opt.getIdOportunidad());
            cont.setNombre(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_nombre)).getText().toString());
            cont.setCargo(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_cargo)).getText().toString());
            cont.setEmail(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_email)).getText().toString());
            cont.setMovil(((EditText)listViewContactos.get(i).findViewById(R.id.contacto_movil)).getText().toString());

            while(contactPhotos.size() > posPhoto && contactPhotos.get(posPhoto) == null)
                posPhoto++;

            if(contactPhotos != null && contactPhotos.size() > posPhoto && contactPhotos.get(posPhoto).length() > 0)
                cont.setURLFoto(contactPhotos.get(i));

            if(cont.getID() == 0)
                OportunidadContacto.insert(getDataBase(), cont);
            else
                OportunidadContacto.update(getDataBase(), cont);

            posPhoto++;
        }

        if(id != 0)
        {
            for(int ir = 0; ir < listContactosIdsDelete.size(); ir++)
                OportunidadContacto.deleteContacto(getDataBase(), listContactosIdsDelete.get(ir));
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
            List<Etapa> etapas = Etapa.getEtapasAll(getDataBase(), opt.getIdOportunidadTipo());
            List<EtapaTarea> etapaTareas = EtapaTarea.getEtapaTareas(getDataBase(), opt.getIdOportunidadTipo());
            for (EtapaTarea tarea : etapaTareas) {
                OportunidadTarea oportunidadTarea = new OportunidadTarea();
                oportunidadTarea.setEstado("P");
                oportunidadTarea.setIdTarea(tarea.getIdTarea());
                oportunidadTarea.setIdEtapa(tarea.getIdEtapa());
                oportunidadTarea.setObservacion("");
                oportunidadTarea.set_idOportunidad((int) opt.getID());
                oportunidadTarea.setOrden(tarea.getOrden());
                OportunidadTarea.insert(getDataBase(), oportunidadTarea);
            }

            int etapaPadre = -1;
            if(listEtapas == null) {
                for (Etapa etapa : etapas) {
                    OportunidadEtapa oportunidadEtapa = new OportunidadEtapa();
                    oportunidadEtapa.setEstado("P");
                    oportunidadEtapa.setIdEtapa(etapa.getIdEtapa());
                    if (etapa.getOrden() == 1 && (etapa.getIdEtapaPadre() == 0 || etapa.getIdEtapaPadre() == etapaPadre)) {
                        oportunidadEtapa.setFechaInicio(Calendar.getInstance().getTime());
                        if (etapa.getIdEtapaPadre() == 0)
                            etapaPadre = etapa.getIdEtapa();
                    }
                    oportunidadEtapa.setIdEtapaPadre(etapa.getIdEtapaPadre());
                    oportunidadEtapa.set_idOportunidad((int) opt.getID());
                    oportunidadEtapa.setObservacion("");
                    OportunidadEtapa.insert(getDataBase(), oportunidadEtapa);
                }
            }
            else
            {
                for (OportunidadEtapa oportunidadEtapa : listEtapas)
                {
                    oportunidadEtapa.set_idOportunidad((int) opt.getID());
                    oportunidadEtapa.setObservacion("");
                    OportunidadEtapa.insert(getDataBase(), oportunidadEtapa);
                }
            }
        }

        if(ConnectionUtils.isNetAvailable(getActivity())) {
            Bundle bundle = new Bundle();
            if(id == 0)
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPLOAD_OPORTUNIDAD);
            else
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PENDIENTES_OPORTUNIDADES);
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
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        if(listAgentesIds == null) {
            listViewResponsables = new ArrayList<LinearLayout>();
            listViewContactos = new ArrayList<LinearLayout>();
            listAgentesIds = new ArrayList<Integer>();
            listAgentesIdsDelete = new ArrayList<Integer>();
            listContactosIdsDelete = new ArrayList<>();
        }

        ResponsableContainer = (LinearLayout) view.findViewById(R.id.oportunidad_responsables);
        view.findViewById(R.id.oportunidad_agregar_responsable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFragment(AgenteFragment.newInstance(listAgentesIds), "Agentes", "Agentes");
            }
        });
        view.findViewById(R.id.oportunidad_ubicacion).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

                    try {
                        ((BaseActivity) getActivity()).showDialogProgress("GPS", "Obteniendo Posición");
                        LocationUtils locationUtils = new LocationUtils();
                        locationUtils.getLocationReference(getContext(), new LocationUtils.OnLocationResultListener() {

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
            addThisAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE), "C");

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
                ((TextView) view.findViewById(R.id.oportunidad_prob_label)).setText(i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        listTipos = OportunidadTipo.getOportunidadTipoAll(getDataBase());
        List<String> tipos = new ArrayList<String>();

        for(OportunidadTipo oportunidadTipo: listTipos)
        {
            tipos.add(oportunidadTipo.getDescripcion());
        }

        SimpleIdentifiableAdapter tipoCanal = new SimpleIdentifiableAdapter(getContext(), Canal.getCanal(getDataBase(), ""));
        SimpleGeneralValueAdapter tipoPersonaAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.berlin.Contants.GENERAL_TABLE_TIPO_PERSONA);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, tipos.toArray(new String[tipos.size()]));
        ((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).setAdapter(adapter);
        ((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).setAdapter(new NothingSelectedSpinnerAdapter(
                adapter,
                R.layout.spinner_empty_selected,
                this.getContext()));
        ((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).setPrompt(" Seleccione un tipo");
        ((Spinner) getRootView().findViewById(R.id.oportunidad_canal)).setAdapter(tipoCanal);
        ((Spinner) getRootView().findViewById(R.id.oportunidad_canal)).setAdapter(new NothingSelectedSpinnerAdapter(
                tipoCanal,
                R.layout.spinner_empty_selected,
                this.getContext(), "Canal"));
        ((Spinner) getRootView().findViewById(R.id.oportunidad_canal)).setPrompt("Seleccione un canal");
        ((Spinner) getRootView().findViewById(R.id.oportunidad_tipo_persona)).setAdapter(tipoPersonaAdapter);
        ((Spinner) getRootView().findViewById(R.id.oportunidad_tipo_persona)).setPrompt("Seleccione un tipo de persona");

        if(idTipoOportunidad >= 0)
            ((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).setSelection(idTipoOportunidad);

        ((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idTipoOportunidad = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(id != 0)
            setDatosOportunidad();

        if(listContactos != null) {
            for (int i = 0; i < listViewContactos.size(); i++) {
                if(listContactos.size() > i) {
                    ((EditText) listViewContactos.get(i).findViewById(R.id.contacto_nombre)).setText(listContactos.get(i).getNombre());
                    ((EditText) listViewContactos.get(i).findViewById(R.id.contacto_cargo)).setText(listContactos.get(i).getCargo());
                    ((EditText) listViewContactos.get(i).findViewById(R.id.contacto_email)).setText(listContactos.get(i).getEmail() == null ? "" : listContactos.get(i).getEmail());
                    ((EditText) listViewContactos.get(i).findViewById(R.id.contacto_movil)).setText(listContactos.get(i).getMovil() == null ? "" : listContactos.get(i).getMovil());
                }
            }
        }
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
                addThisAgente(resp.getIdAgente(), resp.getTipo());
            for (OportunidadContacto opCont : opt.getOportunidadContactos())
                addContacto(opCont.getID());

            for(int i = 0; i < listTipos.size(); i++)
            {
                if(listTipos.get(i).getIdOportunidadTipo() == opt.getIdOportunidadTipo())
                {
                    ((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).setSelection(i+1);
                    idTipoOportunidad = i + 1;
                }
            }
            view.findViewById(R.id.oportunidad_tipo_etapas).setEnabled(false);

            ((Spinner)getRootView().findViewById(R.id.oportunidad_canal)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.oportunidad_canal)).getAdapter(), opt.getIdCanal()));
            ((Spinner)getRootView().findViewById(R.id.oportunidad_tipo_persona)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.oportunidad_tipo_persona)).getAdapter(), opt.getTipoPersona()));

            for (int i = 0; i < opt.getOportunidadFotos().size(); i++) {
                photos.set(i, opt.getOportunidadFotos().get(i).getURLFoto());
                if(opt.getOportunidadFotos().get(i).getURLFoto().length() != 0) {
                    switch (i) {
                        case 0:
                            DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opt.getOportunidadFotos().get(i).getURLFoto().replace("\"", ""),
                                    ((ImageButton) view.findViewById(R.id.oportunidad_foto1)));
                            ((ImageButton) view.findViewById(R.id.oportunidad_foto1)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;
                        case 1:
                            DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opt.getOportunidadFotos().get(i).getURLFoto().replace("\"", ""),
                                    ((ImageButton) view.findViewById(R.id.oportunidad_foto2)));
                            ((ImageButton) view.findViewById(R.id.oportunidad_foto2)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;
                        case 2:
                            DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opt.getOportunidadFotos().get(i).getURLFoto().replace("\"", ""),
                                    ((ImageButton) view.findViewById(R.id.oportunidad_foto3)));
                            ((ImageButton) view.findViewById(R.id.oportunidad_foto3)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                            break;
                    }
                }
            }

            setData = true;
        }

    }

    protected void takePicture(final int idView) {
        photoFlag = true;
        listContactos = new ArrayList<OportunidadContacto>();
        for(int i = 0; i < listViewContactos.size(); i ++)
        {
            OportunidadContacto cont = new OportunidadContacto();
            cont.setNombre(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_nombre)).getText().toString());
            cont.setCargo(((EditText)listViewContactos.get(i).findViewById(R.id.contacto_cargo)).getText().toString());
            listContactos.add(cont);
        }
        photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
        //captureIntent.putExtra("crop", "true");
        //captureIntent.putExtra("aspectX", 2);
        //captureIntent.putExtra("aspectY", 1);
        captureIntent.putExtra("return-data", true);
        getActivity().startActivityForResult(captureIntent, idView);
    }

    private void addThisAgente(final int id, String tipo) {
        if(tipo.length() <= 0)
            tipo = "G";
        final LinearLayout responsable = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.rowlist_responsable, null);
        final int pos = listViewResponsables.size();
        if(id != PreferenceManager.getInt(Contants.KEY_IDAGENTE)) {
            ((ImageView) responsable.findViewById(R.id.eliminar_responsable)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listViewResponsables.remove(responsable);
                    ResponsableContainer.removeView(responsable);
                    if(listAgentesIds.indexOf(id) != -1)
                        listAgentesIds.remove(listAgentesIds.indexOf(id));
                    listAgentesIdsDelete.add(id);
                }
            });
        }
        else
            responsable.findViewById(R.id.eliminar_responsable).setVisibility(View.INVISIBLE);
        Agente agt = Agente.getAgente(getDataBase(), id);
        ((TextView) responsable.findViewById(R.id.responsable_nombre)).setText(agt.getNombre());
        ((TextView) responsable.findViewById(R.id.responsable_tipo)).setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_TIPO_RESPONSABLES, tipo).getValue());
        ((TextView) responsable.findViewById(R.id.responsable_tipo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id != PreferenceManager.getInt(Contants.KEY_IDAGENTE))
                {
                    if(((TextView) responsable.findViewById(R.id.responsable_tipo)).getText().toString().equalsIgnoreCase("Gestor"))
                        ((TextView) responsable.findViewById(R.id.responsable_tipo)).setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_TIPO_RESPONSABLES, "I").getValue());
                    else if(((TextView) responsable.findViewById(R.id.responsable_tipo)).getText().toString().equalsIgnoreCase("Interesado"))
                        ((TextView) responsable.findViewById(R.id.responsable_tipo)).setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_TIPO_RESPONSABLES, "R").getValue());
                    else
                        ((TextView) responsable.findViewById(R.id.responsable_tipo)).setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_TIPO_RESPONSABLES, "G").getValue());                }
            }
        });
        listAgentesIds.add(agt.getIdAgente());
        ResponsableContainer.addView(responsable);
        listViewResponsables.add(responsable);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listContactos = new ArrayList<OportunidadContacto>();
        for(int i = 0; i < listViewContactos.size(); i ++)
        {
            OportunidadContacto cont = new OportunidadContacto();
            cont.setNombre(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_nombre)).getText().toString());
            cont.setCargo(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_cargo)).getText().toString());
            cont.setEmail(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_email)).getText().toString());
            cont.setMovil(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_movil)).getText().toString());
            listContactos.add(cont);
        }
    }

    private void addContacto()
    {
        final LinearLayout contacto = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_crear_oportunidad_contacto, null);
        final int pos = listViewContactos.size();
        if(pos != 0)
            contacto.findViewById(R.id.contacto_principal).setVisibility(View.GONE);
        ((ImageView) contacto.findViewById(R.id.contacto_eliminar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listViewContactos.remove(contacto);
                ContactosContainer.removeView(contacto);
                contactPhotos.remove(pos);
            }
        });
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

    private void addContacto(final long id)
    {
        OportunidadContacto opCont = OportunidadContacto.getContactoInt(getDataBase(), id);
        final LinearLayout contacto = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_crear_oportunidad_contacto, null);
        final int pos = listViewContactos.size();
        if(pos != 0)
            contacto.findViewById(R.id.contacto_principal).setVisibility(View.GONE);
        ((ImageView) contacto.findViewById(R.id.contacto_eliminar)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listViewContactos.remove(contacto);
                ContactosContainer.removeView(contacto);
                contactPhotos.remove(pos);
                listContactosIdsDelete.add((int)id);
            }
        });
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
        ((EditText) contacto.findViewById(R.id.contacto_email)).setText(opCont.getEmail() == null ? "" : opCont.getEmail());
        ((EditText) contacto.findViewById(R.id.contacto_movil)).setText(opCont.getMovil() == null ? "" : opCont.getMovil());
        if(opCont.getURLFoto() != null && opCont.getURLFoto().length() != 0) {
            DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opCont.getURLFoto().replace("\"", ""),
                    (ImageView) contacto.findViewById(R.id.contacto_foto));
            ((ImageButton) contacto.findViewById(R.id.contacto_foto)).setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
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
                    ((TextView) view.findViewById(R.id.oportunidad_comentario)).setText(StringUtils.getStringCapSentence(result.get(0)));
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
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photo);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                if (requestCode == PHOTO_1 || requestCode == PHOTO_2 || requestCode == PHOTO_3) {

                    if (requestCode == PHOTO_1) {
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto1)).setImageBitmap(pree);
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto1)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photos.set(0, Utils.SaveBitmap(pree, String.format("%s.%s", java.util.UUID.randomUUID(), ".jpg")));
                    }
                    if (requestCode == PHOTO_2) {
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto2)).setImageBitmap(pree);
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto2)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                         photos.set(1, Utils.SaveBitmap(pree, String.format("%s.%s", java.util.UUID.randomUUID(), ".jpg")));

                    }
                    if (requestCode == PHOTO_3) {
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto3)).setImageBitmap(pree);
                        ((ImageButton) getRootView().findViewById(R.id.oportunidad_foto3)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        photos.set(2, Utils.SaveBitmap(pree, String.format("%s.%s", java.util.UUID.randomUUID(), ".jpg")));
                    }
                } else {
                    ((ImageButton) listViewContactos.get(posContact).findViewById(R.id.contacto_foto)).setImageBitmap(pree);
                    ((ImageButton) listViewContactos.get(posContact).findViewById(R.id.contacto_foto)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    contactPhotos.set(posContact, Utils.SaveBitmap(pree, String.format("%s.%s", java.util.UUID.randomUUID(), ".jpg")));
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
        if(((TextView) view.findViewById(R.id.oportunidad_direccion)).length() <= 0)
        {
            Toast.makeText(getContext(), R.string.message_sin_direccion, Toast.LENGTH_LONG).show();
            return false;
        }
        if(((Spinner) view.findViewById(R.id.oportunidad_tipo_etapas)).getSelectedItemPosition() == 0)
        {
            Toast.makeText(getContext(), R.string.message_sin_tipo_oportunidad, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onFinishAgentesDialog(ArrayList<Integer> agentes) {
        for(int idAgente : agentes) {
            if(!listAgentesIds.contains(idAgente)) {
                final LinearLayout responsable = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.rowlist_responsable, null);
                final Agente agente = Agente.getAgente(getDataBase(), idAgente);
                final int pos = listViewResponsables.size();
                ((ImageView) responsable.findViewById(R.id.eliminar_responsable)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        listViewResponsables.remove(responsable);
                        ResponsableContainer.removeView(responsable);
                        if (listAgentesIds.indexOf(agente.getIdAgente()) != -1)
                            listAgentesIds.remove(listAgentesIds.indexOf(agente.getIdAgente()));
                        listAgentesIdsDelete.add(agente.getIdAgente());
                    }
                });
                ((TextView) responsable.findViewById(R.id.responsable_nombre)).setText(agente.getNombre());
                ((TextView) responsable.findViewById(R.id.responsable_tipo)).setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_TIPO_RESPONSABLES, "G").getValue());
                ((TextView) responsable.findViewById(R.id.responsable_tipo)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (id != PreferenceManager.getInt(Contants.KEY_IDAGENTE)) {
                            if (((TextView) responsable.findViewById(R.id.responsable_tipo)).getText().toString().equalsIgnoreCase("Gestor"))
                                ((TextView) responsable.findViewById(R.id.responsable_tipo)).setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_TIPO_RESPONSABLES, "I").getValue());
                            else if (((TextView) responsable.findViewById(R.id.responsable_tipo)).getText().toString().equalsIgnoreCase("Interesado"))
                                ((TextView) responsable.findViewById(R.id.responsable_tipo)).setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_TIPO_RESPONSABLES, "R").getValue());
                            else
                                ((TextView) responsable.findViewById(R.id.responsable_tipo)).setText(GeneralValue.getGeneralValue(getDataBase(), Contants.GENERAL_TABLE_TIPO_RESPONSABLES, "G").getValue());
                        }
                    }
                });

                listAgentesIds.add(agente.getIdAgente());
                ResponsableContainer.addView(responsable);
                listViewResponsables.add(responsable);
                if (listAgentesIdsDelete.indexOf(agente.getIdAgente()) != -1)
                    listAgentesIdsDelete.remove(listAgentesIdsDelete.indexOf(agente.getIdAgente()));
            }
        }
        for(int idAgenteDelete : listAgentesIds)
        {
            if(!agentes.contains(idAgenteDelete))
            {
                View viewToDelete = listViewResponsables.get(listAgentesIds.indexOf(idAgenteDelete));
                listViewResponsables.remove(viewToDelete);
                ResponsableContainer.removeView(viewToDelete);
                listAgentesIdsDelete.add(idAgenteDelete);
            }
        }
        for(int listDelete : listAgentesIdsDelete)
        {
            if (listAgentesIds.indexOf(listDelete) != -1)
                listAgentesIds.remove(listAgentesIds.indexOf(listDelete));
        }
    }

    public void SaveData()
    {

    }

    @Override
    public void onEtapasFinish(List<OportunidadEtapa> etapas) {
        listEtapas = etapas;
        Grabar();
        finish();
    }

    private int getPosition(SpinnerAdapter spinnerAdapter, int i)
    {
        int position = -1;
        for(int f = 0; f < spinnerAdapter.getCount(); f++)
        {
            if(spinnerAdapter.getItemId(f) == i)
                position = f;
        }
        return position;
    }
    private int getPosition(SpinnerAdapter spinnerAdapter, String i)
    {
        int position = -1;
        for(int f = 0; f < spinnerAdapter.getCount(); f++)
        {
            if(((GeneralValue)spinnerAdapter.getItem(f)).getCode().equals(i))
                position = f;
        }
        return position;
    }
}
