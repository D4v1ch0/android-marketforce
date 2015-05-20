package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import rp3.marketforce.models.Campo;
import rp3.marketforce.models.Canal;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.models.oportunidad.Agente;
import rp3.marketforce.models.oportunidad.EtapaTarea;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadContacto;
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

    public static CrearOportunidadFragment newInstance() {
        return new CrearOportunidadFragment();
    }

    public static String ARG_CLIENTE = "cliente";
    public static String ARG_TIPO = "tipo";

    public final static int DIALOG_VISITA = 1;
    public final static int DIALOG_GPS = 2;

    private ViewPager PagerDetalles;
    private DetailsPageAdapter pagerAdapter;
    private LayoutInflater inflater;
    private ImageButton TabInfo;
    private ImageButton TabDirecciones;
    private ImageButton TabContactos;
    private LinearLayout ContactosContainer, ResponsableContainer;
    private List<LinearLayout> listViewResponsables, listViewContactos;
    private List<Integer> listAgentesIds;
    private int curentPage, posDir;
    private Location currentLoc;
    private long idCliente = 0;
    private int tipo;
    Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
    boolean isClient, rotated;
    int posContact = -1;
    public Oportunidad oportunidad;
    public List<String> contactPhotos;
    private FrameLayout info;
    private LinearLayout direccion;
    private FrameLayout contacto;
    private ImageView ArrowInfo;
    private ImageView ArrowCont;
    private ImageView ArrowDir;
    private List<GeopoliticalStructure> ciudades;
    private GeopoliticalStructureAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oportunidad = new Oportunidad();
        contactPhotos = new ArrayList<String>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tryEnableGooglePlayServices(true);
        setContentView(R.layout.fragment_crear_cliente, R.menu.fragment_crear_cliente);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(PagerDetalles != null)
            setPageConfig(PagerDetalles.getCurrentItem());
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
        if(idCliente != 0)
            opt = Oportunidad.getOportunidadId(getDataBase(), idCliente);
        opt.setDescripcion(((EditText) info.findViewById(R.id.oportunidad_descripcion)).getText().toString());
        if(((EditText)info.findViewById(R.id.oportunidad_probabilidad)).length() > 0)
            opt.setProbabilidad(Integer.parseInt(((EditText) info.findViewById(R.id.oportunidad_probabilidad)).getText().toString()));
        else
            opt.setProbabilidad(0);

        if(((EditText)info.findViewById(R.id.oportunidad_importe)).length() > 0)
            opt.setImporte(Double.parseDouble(((EditText) info.findViewById(R.id.oportunidad_importe)).getText().toString()));
        else
            opt.setImporte(0);

        opt.setCalificacion(((RatingBar)info.findViewById(R.id.oportunidad_calificacion)).getNumStars());
        opt.setEstado("A");
        opt.setIdEtapa(1);
        opt.setFechaCreacion(Calendar.getInstance().getTime());
        opt.setFechaUltimaGestion(Calendar.getInstance().getTime());
        opt.setObservacion(((EditText) info.findViewById(R.id.oportunidades_comentarios)).getText().toString());
        opt.setReferencia(((EditText) info.findViewById(R.id.oportunidad_referencia)).getText().toString());
        opt.setDireccion(((EditText) direccion.findViewById(R.id.oportunidad_direccion)).getText().toString());
        opt.setLongitud(oportunidad.getLongitud());
        opt.setLatitud(oportunidad.getLatitud());


        if(opt.getID() == 0 )
            Oportunidad.insert(getDataBase(), opt);
        else
            Oportunidad.update(getDataBase(), opt);

        for(int i = 0; i < listViewResponsables.size(); i ++)
        {
            OportunidadResponsable responsable = new OportunidadResponsable();
            responsable.set_idOportunidad((int) opt.getID());
            responsable.setIdAgente(listAgentesIds.get(i));

            if(responsable.getID() == 0)
                OportunidadResponsable.insert(getDataBase(), responsable);
            else
                OportunidadResponsable.update(getDataBase(), responsable);
        }

        for(int i = 0; i < listViewContactos.size(); i ++)
        {
            OportunidadContacto cont = new OportunidadContacto();
            cont.set_idOportunidad((int) opt.getID());
            cont.setNombre(((EditText) listViewContactos.get(i).findViewById(R.id.contacto_nombre)).getText().toString());
            cont.setCargo(((EditText)listViewContactos.get(i).findViewById(R.id.contacto_cargo)).getText().toString());

            if(cont.getID() == 0)
                OportunidadContacto.insert(getDataBase(), cont);
            else
                OportunidadContacto.update(getDataBase(), cont);
        }

        List<EtapaTarea> etapaTareas = EtapaTarea.getEtapaTareas(getDataBase());
        for(EtapaTarea tarea : etapaTareas)
        {
            OportunidadTarea oportunidadTarea = new OportunidadTarea();
            oportunidadTarea.setEstado("P");
            oportunidadTarea.setIdTarea(tarea.getIdTarea());
            oportunidadTarea.setIdEtapa(tarea.getIdEtapa());
            oportunidadTarea.set_idOportunidad((int) opt.getID());
            oportunidadTarea.setOrden(tarea.getOrden());
            OportunidadTarea.insert(getDataBase(), oportunidadTarea);
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
            ((EditText)direccion.findViewById(R.id.oportunidad_direccion)).setText(addr.get(0).getFeatureName());
            ((EditText)direccion.findViewById(R.id.oportunidad_referencia_direccion)).setText(addr.get(1).getFeatureName());
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
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        if(info == null)
        {
            listViewResponsables = new ArrayList<LinearLayout>();
            listViewContactos = new ArrayList<LinearLayout>();
            listAgentesIds = new ArrayList<Integer>();
            //ciudades = GeopoliticalStructure.getGeopoliticalStructureCities(getDataBase());

            adapter = new GeopoliticalStructureAdapter(getContext(), getDataBase());

            TabInfo = (ImageButton) getRootView().findViewById((R.id.detail_tab_info));
            TabDirecciones = (ImageButton) getRootView().findViewById((R.id.detail_tab_direccion));
            TabContactos = (ImageButton) getRootView().findViewById((R.id.detail_tab_contactos));
            ArrowInfo = (ImageView) getRootView().findViewById((R.id.detail_tab_info_arrow));
            ArrowDir = (ImageView) getRootView().findViewById((R.id.detail_tab_direccion_arrow));
            ArrowCont = (ImageView) getRootView().findViewById((R.id.detail_tab_contactos_arrow));
            PagerDetalles = (ViewPager) rootView.findViewById(R.id.crear_cliente_pager);

            pagerAdapter = new DetailsPageAdapter();
            inflater = (LayoutInflater) this.getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            info = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_oportunidad_info, null);
            ((TextView) info.findViewById(R.id.oportunidad_agregar_responsable)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    addResponsable();

                }});
            ResponsableContainer = (LinearLayout) info.findViewById(R.id.oportunidad_responsables);
            direccion = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_oportunidad_direccion, null);
            ((ImageButton) direccion.findViewById(R.id.oportunidad_ubicacion)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (GooglePlayServicesUtils.servicesConnected((BaseActivity) getActivity())) {

                        try
                        {
                            ((BaseActivity)getActivity()).showDialogProgress("GPS","Obteniendo Posición");
                            LocationUtils.getLocation(getContext(), new LocationUtils.OnLocationResultListener() {

                                @Override
                                public void getLocationResult(Location location) {
                                    if (location != null) {
                                        ((ImageView) direccion.findViewById(R.id.oportunidad_ubicacion_check)).setImageResource(R.drawable.checkbox_on);
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
                        }
                        catch(Exception ex)
                        {	}

                    }

                }});
            contacto = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_oportunidad_contactos, null);
            ((TextView) contacto.findViewById(R.id.agregar_contacto)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    addContacto();

                }});
            ContactosContainer = (LinearLayout) contacto.findViewById(R.id.crear_cliente_container_contacto);
            pagerAdapter.addView(info);
            pagerAdapter.addView(direccion);
            pagerAdapter.addView(contacto);
            PagerDetalles.setAdapter(pagerAdapter);
            setPageConfig(PagerDetalles.getCurrentItem());
        }
        else
        {
            TabInfo = (ImageButton) getRootView().findViewById((R.id.detail_tab_info));
            TabDirecciones = (ImageButton) getRootView().findViewById((R.id.detail_tab_direccion));
            TabContactos = (ImageButton) getRootView().findViewById((R.id.detail_tab_contactos));
            ArrowInfo = (ImageView) getRootView().findViewById((R.id.detail_tab_info_arrow));
            ArrowDir = (ImageView) getRootView().findViewById((R.id.detail_tab_direccion_arrow));
            ArrowCont = (ImageView) getRootView().findViewById((R.id.detail_tab_contactos_arrow));
            PagerDetalles = (ViewPager) rootView.findViewById(R.id.crear_cliente_pager);
            pagerAdapter = new DetailsPageAdapter();
            pagerAdapter.addView(info);
            pagerAdapter.addView(direccion);
            pagerAdapter.addView(contacto);
            PagerDetalles.setAdapter(pagerAdapter);
            ArrowInfo.setVisibility(View.INVISIBLE);
            ArrowDir.setVisibility(View.INVISIBLE);
            ArrowCont.setVisibility(View.INVISIBLE);
            setPageConfig(PagerDetalles.getCurrentItem());
            rotated = true;
        }
        TabInfo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PagerDetalles.setCurrentItem(0);
            }});

        TabDirecciones.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PagerDetalles.setCurrentItem(1);
            }});

        TabContactos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PagerDetalles.setCurrentItem(2);
            }});

        PagerDetalles.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                setPageConfig(arg0);

            }});
    }


    private void setDatosOportunidad() {



    }

    protected void takePicture(final int idView) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this.getActivity());
        myAlertDialog.setTitle("Fotografía");
        myAlertDialog.setMessage("Obtener de");

        myAlertDialog.setPositiveButton("Galería",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryIntent.putExtra("return-data", true);
                        galleryIntent.putExtra("crop", "true");
                        galleryIntent.putExtra("aspectX", 1);
                        galleryIntent.putExtra("aspectY", 1);
                        getActivity().startActivityForResult(galleryIntent, idView);
                    }
                });

        myAlertDialog.setNegativeButton("Cámara",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
                        captureIntent.putExtra("crop", "true");
                        captureIntent.putExtra("aspectX", 1);
                        captureIntent.putExtra("aspectY", 1);
                        getActivity().startActivityForResult(captureIntent, idView);

                    }
                });
        myAlertDialog.show();
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
        ((ImageView) contacto.findViewById(R.id.contacto_foto)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                posContact = pos;
                isClient = false;
                takePicture(1);
            }});
        ContactosContainer.addView(contacto);
        listViewContactos.add(contacto);
    }

    private void setPageConfig(int page){
        curentPage = page;
        String title = pagerAdapter.getPageTitle(page).toString();
        if(title.equalsIgnoreCase("Info"))
        {
            TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_activated));
            TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            ArrowInfo.setVisibility(View.VISIBLE);
            ArrowDir.setVisibility(View.INVISIBLE);
            ArrowCont.setVisibility(View.INVISIBLE);
        }
        if(title.equalsIgnoreCase("Direcciones"))
        {
            TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_activated));
            TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            ArrowInfo.setVisibility(View.INVISIBLE);
            ArrowDir.setVisibility(View.VISIBLE);
            ArrowCont.setVisibility(View.INVISIBLE);
        }
        if(title.equalsIgnoreCase("Contactos"))
        {
            TabInfo.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            TabDirecciones.setBackgroundColor(getResources().getColor(R.color.tab_inactivated));
            TabContactos.setBackgroundColor(getResources().getColor(R.color.tab_activated));
            ArrowInfo.setVisibility(View.INVISIBLE);
            ArrowDir.setVisibility(View.INVISIBLE);
            ArrowCont.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setPageConfig(PagerDetalles.getCurrentItem());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap pree = null;
            if(data.getData() != null) {
                try {
                    pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            if(data.getExtras().containsKey("data"))
                pree = (Bitmap)data.getExtras().get("data");
            else
                try {
                    photo = Uri.parse(data.getAction());
                    pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(isClient)
            {
                ((ImageButton) info.findViewById(R.id.cliente_foto)).setImageBitmap(pree);
                //cliente.setURLFoto(Utils.SaveBitmap(pree, "edit_client"));
            }
            else
            {
                ((ImageView) listViewContactos.get(posContact).findViewById(R.id.contacto_foto)).setImageBitmap(pree);
                contactPhotos.set(posContact, Utils.SaveBitmap(pree, "edit_contact"));
            }
        }
    }

    public boolean Validaciones()
    {
        return true;
    }

}
