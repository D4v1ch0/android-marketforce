package rp3.marketforce.actividades;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
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
import rp3.marketforce.cliente.CrearClienteFragment;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.Campo;
import rp3.marketforce.models.Canal;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.TipoCliente;
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
 * Created by magno_000 on 10/04/2015.
 */
public class ActualizacionFragment extends BaseFragment {

    boolean rotated = false;

    public static ActualizacionFragment newInstance(long id_agenda, int tipo, int idTarea)
    {
        ActualizacionFragment fragment = new ActualizacionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_AGENDA, id_agenda);
        args.putInt(ARG_TIPO, tipo);
        args.putInt(ARG_TAREA, idTarea);
        fragment.setArguments(args);
        return fragment;
    }

    public static String ARG_AGENDA = "cliente";
    public static String ARG_TIPO = "tipo";
    public static String ARG_TAREA = "tarea";
    private ViewPager PagerDetalles;
    private DetailsPageAdapter pagerAdapter;
    private LayoutInflater inflater;
    private ImageButton TabInfo;
    private ImageButton TabDirecciones;
    private ImageButton TabContactos;
    private LinearLayout ContactosContainer, DireccionContainer;
    private List<LinearLayout> listViewDirecciones, listViewContactos;
    private List<GeopoliticalStructure> listCiudades;
    private int curentPage;
    private Agenda agd;
    private long idAgenda = 0;
    private int tipo, idTarea;
    Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
    boolean isClient;
    int posContact = -1;
    public Cliente cliente;
    public List<String> contactPhotos;
    private FrameLayout info;
    private FrameLayout direccion;
    private FrameLayout contacto;
    private ImageView ArrowInfo;
    private ImageView ArrowCont;
    private ImageView ArrowDir;
    private List<GeopoliticalStructure> ciudades;
    private GeopoliticalStructureAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cliente = new Cliente();
        contactPhotos = new ArrayList<String>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //tryEnableGooglePlayServices(true);
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
        Cliente cli = agd.getCliente();
        ClienteDireccion cliDir = agd.getClienteDireccion();
        Contacto contacto = agd.getContacto();
        cli.setIdCanal((int) ((Spinner)info.findViewById(R.id.cliente_canal)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_canal)).getSelectedItemPosition()));
        cli.setIdTipoIdentificacion((int) ((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition()));
        cli.setIdentificacion(((EditText)info.findViewById(R.id.cliente_identificacion)).getText().toString());
        cli.setTipoPersona(((GeneralValue)((Spinner)info.findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItem()).getCode());
        cli.setIdTipoCliente((int) ((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).getAdapter().getItemId(((Spinner)info.findViewById(R.id.cliente_tipo_cliente)).getSelectedItemPosition()));
        if(cliente.getURLFoto() != null && !cliente.getURLFoto().trim().equals(""))
            cli.setURLFoto(cliente.getURLFoto());
        if(((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItemPosition() == 1)
        {
            cli.setNombre1(((EditText)info.findViewById(R.id.cliente_primer_nombre)).getText().toString());
            cli.setNombre2(((EditText)info.findViewById(R.id.cliente_segundo_nombre)).getText().toString());
            cli.setApellido1(((EditText)info.findViewById(R.id.cliente_primer_apellido)).getText().toString());
            cli.setApellido2(((EditText)info.findViewById(R.id.cliente_segundo_apellido)).getText().toString());
            cli.setCorreoElectronico(((EditText)info.findViewById(R.id.cliente_correo)).getText().toString());
            cli.setGenero(((GeneralValue)((Spinner)info.findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
            cli.setEstadoCivil(((GeneralValue)((Spinner)info.findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
            cli.setNombreCompleto(cli.getNombre1() + " " + cli.getNombre2() + " " + cli.getApellido1() + " " + cli.getApellido2());
            cli.setFechaNacimiento(cliente.getFechaNacimiento());
        }
        else
        {
            cli.setApellido1("");
            cli.setApellido2("");
            cli.setNombre2("");
            cli.setNombre1(((EditText)info.findViewById(R.id.cliente_nombre)).getText().toString());
            cli.setActividadEconomica(((EditText)info.findViewById(R.id.cliente_actividad_economica)).getText().toString());
            cli.setCorreoElectronico(((EditText)info.findViewById(R.id.cliente_correo_juridico)).getText().toString());
            cli.setRazonSocial(((EditText)info.findViewById(R.id.cliente_razon_social)).getText().toString());
            cli.setPaginaWeb(((EditText)info.findViewById(R.id.cliente_pagina_web)).getText().toString());
            cli.setGenero(((GeneralValue)((Spinner)info.findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
            cli.setEstadoCivil(((GeneralValue)((Spinner)info.findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
            cli.setNombreCompleto(cli.getNombre1());
        }
        cli.setPendiente(true);
        List<Contacto> cliContactos = cli.getContactos();
        cli.setContactos(null);
        List<ClienteDireccion> cliDirecciones = cli.getClienteDirecciones();
        cli.setClienteDirecciones(null);

        Cliente.update(getDataBase(), cli);


            cliDir.set_idCliente(cli.getID());
            cliDir.setIdClienteDireccion(agd.getIdClienteDireccion());
            cliDir.setDireccion(((EditText)listViewDirecciones.get(0).findViewById(R.id.cliente_direccion)).getText().toString());
            cliDir.setTipoDireccion(((GeneralValue)((Spinner)listViewDirecciones.get(0).findViewById(R.id.cliente_tipo_direccion_spinner)).getSelectedItem()).getCode());
            cliDir.setEsPrincipal(((CheckBox)listViewDirecciones.get(0).findViewById(R.id.cliente_es_principal)).isChecked());
            cliDir.setTelefono1(((EditText)listViewDirecciones.get(0).findViewById(R.id.cliente_telefono1)).getText().toString());
            cliDir.setTelefono2(((EditText)listViewDirecciones.get(0).findViewById(R.id.cliente_telefono2)).getText().toString());
            cliDir.setReferencia(((EditText)listViewDirecciones.get(0).findViewById(R.id.cliente_referencia)).getText().toString());
            cliDir.setCiudadDescripcion(((AutoCompleteTextView)listViewDirecciones.get(0).findViewById(R.id.cliente_ciudad)).getText().toString());
            if(listCiudades.size() > 0 && listCiudades.get(0) != null)
                cliDir.setIdCiudad((int) listCiudades.get(0).getID());

            if(!((EditText)listViewDirecciones.get(0).findViewById(R.id.cliente_longitud)).getText().toString().equals(""))
            {
                cliDir.setLongitud(Double.parseDouble(((EditText)listViewDirecciones.get(0).findViewById(R.id.cliente_longitud)).getText().toString()));
                cliDir.setLatitud(Double.parseDouble(((EditText)listViewDirecciones.get(0).findViewById(R.id.cliente_latitud)).getText().toString()));
            }

            ClienteDireccion.update(getDataBase(), cliDir);

            cli.setDireccion(cliDir.getDireccion());
            cli.setTelefono(cliDir.getTelefono1());
            Cliente.update(getDataBase(), cli);



        if(agd.getIdContacto() != 0)
        {
            contacto.set_idCliente(cli.getID());
            contacto.setNombre(((EditText)listViewContactos.get(0).findViewById(R.id.cliente_nombres)).getText().toString());
            contacto.setApellido(((EditText)listViewContactos.get(0).findViewById(R.id.cliente_apellidos)).getText().toString());
            contacto.setCargo(((EditText)listViewContactos.get(0).findViewById(R.id.cliente_cargo)).getText().toString());
            contacto.setTelefono1(((EditText)listViewContactos.get(0).findViewById(R.id.cliente_telefono1_contacto)).getText().toString());
            contacto.setTelefono2(((EditText)listViewContactos.get(0).findViewById(R.id.cliente_telefono2_contacto)).getText().toString());
            contacto.setCorreo(((EditText)listViewContactos.get(0).findViewById(R.id.cliente_correo_contacto)).getText().toString());
            contacto.setIdClienteDireccion(((Spinner)listViewContactos.get(0).findViewById(R.id.cliente_direccion_contacto)).getSelectedItemPosition()+1);
            contacto.setURLFoto(contactPhotos.get(0));

            Contacto.update(getDataBase(), contacto);
        }

        if(ConnectionUtils.isNetAvailable(getActivity()))
        {
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE_FULL);
            bundle.putLong(CrearClienteFragment.ARG_CLIENTE, cli.getID());
            requestSync(bundle);
        }

        AgendaTarea agt = AgendaTarea.getTarea(getDataBase(), agd.getIdAgenda(), agd.getIdRuta(), idTarea);
        agt.setEstadoTarea("R");
        AgendaTarea.update(getDataBase(), agt);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        if(info == null)
        {
            listViewDirecciones = new ArrayList<LinearLayout>();
            listViewContactos = new ArrayList<LinearLayout>();
            listCiudades = new ArrayList<GeopoliticalStructure>();
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
            info = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_info, null);
            direccion = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_direccion, null);
            ((TextView) direccion.findViewById(R.id.agregar_direccion)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    addDireccion();

                }});
            DireccionContainer = (LinearLayout) direccion.findViewById(R.id.crear_cliente_container_direccion);

            contacto = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_cliente_contacto, null);
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

            SimpleGeneralValueAdapter tipoPersonaAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_TIPO_PERSONA);
            SimpleGeneralValueAdapter tipoEstadoCivilAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_ESTADO_CIVIL);
            SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_GENERO);
            SimpleIdentifiableAdapter tipoCliente = new SimpleIdentifiableAdapter(getContext(), TipoCliente.getTipoCliente(getDataBase(), ""));
            SimpleIdentifiableAdapter tipoCanal = new SimpleIdentifiableAdapter(getContext(), Canal.getCanal(getDataBase(), ""));
            SimpleDictionaryAdapter tipoIdentificacion = new SimpleDictionaryAdapter(getContext(), IdentificationType.getAll(getDataBase()));

            ((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).setAdapter(tipoPersonaAdapter);
            ((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    if(position == 1)
                    {
                        ((LinearLayout) info.findViewById(R.id.crear_cliente_content_natural)).setVisibility(View.VISIBLE);
                        ((LinearLayout) info.findViewById(R.id.crear_cliente_content_juridico)).setVisibility(View.GONE);
                    }
                    else
                    {
                        ((LinearLayout) info.findViewById(R.id.crear_cliente_content_natural)).setVisibility(View.GONE);
                        ((LinearLayout) info.findViewById(R.id.crear_cliente_content_juridico)).setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub

                }});
            ((Spinner) info.findViewById(R.id.cliente_estado_civil)).setAdapter(tipoEstadoCivilAdapter);
            ((Spinner) info.findViewById(R.id.cliente_genero)).setAdapter(tipoGeneroAdapter);
            ((Spinner) info.findViewById(R.id.cliente_tipo_cliente)).setAdapter(tipoCliente);
            ((Spinner) info.findViewById(R.id.cliente_canal)).setAdapter(tipoCanal);
            ((Spinner) info.findViewById(R.id.cliente_tipo_identificacion)).setAdapter(tipoIdentificacion);
            ((ImageButton) info.findViewById(R.id.cliente_foto)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    isClient = true;
                    takePicture(1);
                }});
            ((EditText) info.findViewById(R.id.cliente_fecha_nacimiento)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    showDialogDatePicker(0);
                }});
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
            if(agd.getIdContacto() == 0)
                getRootView().findViewById(R.id.detail_tab_contactos_layout).setVisibility(View.GONE);

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

        if(getArguments().containsKey(ARG_TAREA) && getArguments().getInt(ARG_TAREA) != 0 && !rotated)
            idTarea = getArguments().getInt(ARG_TAREA);
        if(getArguments().containsKey(ARG_AGENDA) && getArguments().getLong(ARG_AGENDA) != 0 && !rotated)
        {
            idAgenda = getArguments().getLong(ARG_AGENDA);
            agd = Agenda.getAgenda(getDataBase(), idAgenda);
            setDatosClientes();
        }
        if(getArguments().containsKey(ARG_TIPO) && getArguments().getInt(ARG_TIPO) != 0 && !rotated)
        {
            tipo = getArguments().getInt(ARG_TIPO);
            SetCampos();
        }
    }

    private void SetCampos() {
        List<Campo> campos = Campo.getCampos(getDataBase(), tipo);
        Cliente cli = agd.getCliente();
        Contacto cont = agd.getContacto();
        direccion.findViewById(R.id.agregar_direccion).setVisibility(View.GONE);
        contacto.findViewById(R.id.agregar_contacto).setVisibility(View.GONE);
        info.findViewById(R.id.crear_cliente_tipo_persona).setEnabled(false);
        for(Campo campo : campos)
        {
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_TIPO_IDENTIFICACION))
                info.findViewById(R.id.cliente_tipo_identificacion).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_IDENTIFICACION))
                info.findViewById(R.id.cliente_identificacion).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_TIPO_CLI))
                info.findViewById(R.id.cliente_tipo_cliente).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CANAL))
                info.findViewById(R.id.cliente_canal).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FOTO))
                info.findViewById(R.id.cliente_foto).setEnabled(false);

            if (cli.getTipoPersona().equalsIgnoreCase("N")) {
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FECHA_NACIMIENTO))
                    info.findViewById(R.id.cliente_fecha_nacimiento).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_GENERO))
                    info.findViewById(R.id.cliente_genero).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ESTADO_CIVIL))
                    info.findViewById(R.id.cliente_estado_civil).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_NOMBRE_NATURAL))
                    info.findViewById(R.id.cliente_primer_nombre).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_NOMBRE_NATURAL))
                    info.findViewById(R.id.cliente_segundo_nombre).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_APELLIDO_NATURAL))
                    info.findViewById(R.id.cliente_primer_apellido).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_APELLIDO_NATURAL))
                    info.findViewById(R.id.cliente_segundo_apellido).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO))
                    info.findViewById(R.id.cliente_correo).setEnabled(false);
            }
            else
            {
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_NOMBRE_JURIDICO))
                    info.findViewById(R.id.cliente_nombre).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_RAZON_SOCIAL))
                    info.findViewById(R.id.cliente_razon_social).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PAGINA_WEB))
                    info.findViewById(R.id.cliente_pagina_web).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ACTIVIDAD_ECONOMICA))
                    info.findViewById(R.id.cliente_actividad_economica).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO))
                    info.findViewById(R.id.cliente_correo_juridico).setEnabled(false);
            }

            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_DIRECCION_DESCRIPCION))
                listViewDirecciones.get(0).findViewById(R.id.cliente_direccion).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_REFERENCIA))
                listViewDirecciones.get(0).findViewById(R.id.cliente_referencia).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CIUDAD))
                listViewDirecciones.get(0).findViewById(R.id.cliente_ciudad).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_DIRECCION_MOVIL))
                listViewDirecciones.get(0).findViewById(R.id.cliente_telefono1).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_DIRECCION_FIJO))
                listViewDirecciones.get(0).findViewById(R.id.cliente_telefono2).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_POSICION_GEO))
                listViewDirecciones.get(0).findViewById(R.id.cliente_ubicacion).setEnabled(false);

            if(agd.getIdContacto() != 0)
            {
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_NOMBRE_CONTACTO))
                    listViewContactos.get(0).findViewById(R.id.cliente_nombres).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_APELLIDO_CONTACTO))
                    listViewContactos.get(0).findViewById(R.id.cliente_apellidos).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_DIRECCION_CONTACTO))
                    listViewContactos.get(0).findViewById(R.id.cliente_direccion_contacto).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_MOVIL_CONTACTO))
                    listViewContactos.get(0).findViewById(R.id.cliente_telefono1_contacto).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FIJO_CONTACTO))
                    listViewContactos.get(0).findViewById(R.id.cliente_telefono2_contacto).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CARGO_CONTACTO))
                    listViewContactos.get(0).findViewById(R.id.cliente_cargo).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO_CONTACTO))
                    listViewContactos.get(0).findViewById(R.id.cliente_correo_contacto).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FOTO_CONTACTO))
                    listViewContactos.get(0).findViewById(R.id.cliente_contacto_foto).setEnabled(false);
            }

        }
    }

    private void setDatosClientes() {
        DrawableManager DManager = new DrawableManager();
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM yyyy");
        Cliente cli = agd.getCliente();
        ClienteDireccion cliDir = agd.getClienteDireccion();
        Contacto contacto = agd.getContacto();
        ((Spinner) info.findViewById(R.id.cliente_canal)).setSelection(getPosition(((Spinner) info.findViewById(R.id.cliente_canal)).getAdapter(), cli.getIdCanal()));
        ((Spinner) info.findViewById(R.id.cliente_tipo_identificacion)).setSelection(getPosition(((Spinner) info.findViewById(R.id.cliente_tipo_identificacion)).getAdapter(), cli.getTipoIdentificacionId()));
        ((EditText) info.findViewById(R.id.cliente_identificacion)).setText(cli.getIdentificacion());
        ((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).setSelection(getPosition(((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).getAdapter(), cli.getTipoPersona()));
        ((Spinner) info.findViewById(R.id.cliente_tipo_cliente)).setSelection(getPosition(((Spinner) info.findViewById(R.id.cliente_tipo_cliente)).getAdapter(), cli.getIdTipoCliente()));
        if (cli.getFechaNacimiento() != null && cli.getFechaNacimiento().getTime() != 0) {
            ((EditText) info.findViewById(R.id.cliente_fecha_nacimiento)).setText(format1.format(cli.getFechaNacimiento()));
            cliente.setFechaNacimiento(cli.getFechaNacimiento());
        }
        DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getActivity(), cli.getURLFoto()),
                (ImageButton) info.findViewById(R.id.cliente_foto));
        if (cli.getTipoPersona().equalsIgnoreCase("N")) {
            ((EditText) info.findViewById(R.id.cliente_primer_nombre)).setText(cli.getNombre1());
            ((EditText) info.findViewById(R.id.cliente_segundo_nombre)).setText(cli.getNombre2());
            ((EditText) info.findViewById(R.id.cliente_primer_apellido)).setText(cli.getApellido1());
            ((EditText) info.findViewById(R.id.cliente_segundo_apellido)).setText(cli.getApellido2());
            ((EditText) info.findViewById(R.id.cliente_correo)).setText(cli.getCorreoElectronico());
            ((Spinner) info.findViewById(R.id.cliente_genero)).setSelection(getPosition(((Spinner) info.findViewById(R.id.cliente_genero)).getAdapter(), cli.getGenero()));
            ((Spinner) info.findViewById(R.id.cliente_estado_civil)).setSelection(getPosition(((Spinner) info.findViewById(R.id.cliente_estado_civil)).getAdapter(), cli.getEstadoCivil()));
        } else {
            ((EditText) info.findViewById(R.id.cliente_nombre)).setText(cli.getNombre1());
            ((EditText) info.findViewById(R.id.cliente_actividad_economica)).setText(cli.getActividadEconomica());
            ((EditText) info.findViewById(R.id.cliente_correo_juridico)).setText(cli.getCorreoElectronico());
            ((EditText) info.findViewById(R.id.cliente_razon_social)).setText(cli.getRazonSocial());
            ((EditText) info.findViewById(R.id.cliente_pagina_web)).setText(cli.getPaginaWeb());
        }

        addDireccion();
        ((Button) listViewDirecciones.get(0).findViewById(R.id.eliminar_direccion)).setVisibility(View.GONE);
        ((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_direccion)).setText(cliDir.getDireccion());
        ((Spinner) listViewDirecciones.get(0).findViewById(R.id.cliente_tipo_direccion_spinner)).setSelection(
                getPosition(((Spinner) listViewDirecciones.get(0).findViewById(R.id.cliente_tipo_direccion_spinner)).getAdapter(), cliDir.getTipoDireccion()));
        ((CheckBox) listViewDirecciones.get(0).findViewById(R.id.cliente_es_principal)).setChecked(cliDir.getEsPrincipal());
        ((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_telefono1)).setText(cliDir.getTelefono1());
        ((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_telefono2)).setText(cliDir.getTelefono2());
        ((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_referencia)).setText(cliDir.getReferencia());
        ((AutoCompleteTextView) listViewDirecciones.get(0).findViewById(R.id.cliente_ciudad)).setText(cliDir.getCiudadDescripcion());
        listCiudades.add(null);
        if (cliDir.getLongitud() != 0) {
            ((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_longitud)).setText("" + cliDir.getLongitud());
            ((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_latitud)).setText("" + cliDir.getLatitud());
        }
        ((Spinner) listViewDirecciones.get(0).findViewById(R.id.cliente_tipo_direccion_spinner)).setEnabled(false);

        if (agd.getIdContacto() != 0)
        {
            addContacto();
            ((Button)listViewContactos.get(0).findViewById(R.id.eliminar_contacto)).setVisibility(View.GONE);
            ((EditText)listViewContactos.get(0).findViewById(R.id.cliente_nombres)).setText(contacto.getNombre());
            ((EditText)listViewContactos.get(0).findViewById(R.id.cliente_apellidos)).setText(contacto.getApellido());
            ((EditText)listViewContactos.get(0).findViewById(R.id.cliente_cargo)).setText(contacto.getCargo());
            ((EditText)listViewContactos.get(0).findViewById(R.id.cliente_telefono1_contacto)).setText(contacto.getTelefono1());
            ((EditText)listViewContactos.get(0).findViewById(R.id.cliente_telefono2_contacto)).setText(contacto.getTelefono2());
            ((EditText)listViewContactos.get(0).findViewById(R.id.cliente_correo_contacto)).setText(contacto.getCorreo());
            ((Spinner)listViewContactos.get(0).findViewById(R.id.cliente_direccion_contacto)).setSelection(
                    getPosition(((Spinner)listViewContactos.get(0).findViewById(R.id.cliente_direccion_contacto)).getAdapter(), (int) contacto.getIdClienteDireccion()));
            DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getActivity(), contacto.getURLFoto()),
                    (ImageButton)listViewContactos.get(0).findViewById(R.id.cliente_contacto_foto));
        }
        else
        {
            getRootView().findViewById(R.id.detail_tab_contactos_layout).setVisibility(View.GONE);
        }


    }

    @Override
    public void onDailogDatePickerChange(int id, Calendar c) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM yyyy");
        ((EditText) info.findViewById(R.id.cliente_fecha_nacimiento)).setText(format1.format(c.getTime()));
        cliente.setFechaNacimiento(c.getTime());
        super.onDailogDatePickerChange(id, c);
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

    private void addDireccion()
    {
        final LinearLayout direccion = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_cliente_direccion_detail, null);
        final int pos = listViewDirecciones.size();
        ((Button) direccion.findViewById(R.id.eliminar_direccion)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listViewDirecciones.remove(direccion);
                DireccionContainer.removeView(direccion);
                listCiudades.remove(pos);
            }});

        if(pos == 0)
            ((CheckBox) direccion.findViewById(R.id.cliente_es_principal)).setChecked(true);
        ((ImageButton) direccion.findViewById(R.id.cliente_ubicacion)).setOnClickListener(new View.OnClickListener(){

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
                                    ((EditText) listViewDirecciones.get(pos).findViewById(R.id.cliente_longitud)).setText("" + location.getLongitude());
                                    ((EditText) listViewDirecciones.get(pos).findViewById(R.id.cliente_latitud)).setText("" + location.getLatitude());
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
        SimpleGeneralValueAdapter tipoDireccionAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_TIPO_DIRECCION);
        ((Spinner) direccion.findViewById(R.id.cliente_tipo_direccion_spinner)).setAdapter(tipoDireccionAdapter);

        ((AutoCompleteTextView)direccion.findViewById(R.id.cliente_ciudad)).setAdapter(adapter);
        ((AutoCompleteTextView)direccion.findViewById(R.id.cliente_ciudad)).setThreshold(3);
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ((AutoCompleteTextView)direccion.findViewById(R.id.cliente_ciudad)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    listCiudades.set(pos, adapter.getItem(i));
                }
            });


        } else {
            ((AutoCompleteTextView)direccion.findViewById(R.id.cliente_ciudad)).setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {

                @Override
                public void onDismiss() {
                    GeopoliticalStructure setter = adapter.getSelected(((AutoCompleteTextView)direccion.findViewById(R.id.cliente_ciudad)).getText().toString());
                    if(setter != null)
                        listCiudades.set(pos, setter);
                }
            });
        }
        ((AutoCompleteTextView)direccion.findViewById(R.id.cliente_ciudad)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listCiudades.set(pos, adapter.getItem(i));
            }
        });
        listCiudades.add(null);
        DireccionContainer.addView(direccion);
        listViewDirecciones.add(direccion);
    }

    private void addContacto()
    {
        final LinearLayout contacto = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_cliente_contacto_detail, null);
        final int pos = listViewContactos.size();
        ((Button) contacto.findViewById(R.id.eliminar_contacto)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listViewContactos.remove(contacto);
                ContactosContainer.removeView(contacto);
                contactPhotos.remove(pos);
            }});
        contactPhotos.add("");
        ((Spinner) contacto.findViewById(R.id.cliente_direccion_contacto)).setAdapter(getDirecciones());
        ((ImageButton) contacto.findViewById(R.id.cliente_contacto_foto)).setOnClickListener(new View.OnClickListener(){

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
            for(int i = 0; i < listViewContactos.size(); i ++)
            {
                ((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).setAdapter(getDirecciones());
            }
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
                cliente.setURLFoto(Utils.SaveBitmap(pree, "new_client"));
            }
            else
            {
                ((ImageButton) listViewContactos.get(posContact).findViewById(R.id.cliente_contacto_foto)).setImageBitmap(pree);
                contactPhotos.set(posContact, Utils.SaveBitmap(pree, "new_contact"));
            }
        }
    }

    public ArrayAdapter<String> getDirecciones()
    {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < listViewDirecciones.size(); i ++)
        {
            list.add(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).getText().toString());
        }
        return new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, list);
    }

    public boolean Validaciones()
    {
        if(listViewDirecciones.size() <= 0)
        {
            Toast.makeText(getContext(), "No se puede agregar clientes sin dirección.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((Spinner) info.findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItemPosition() == 1)
        {
            if(((EditText)info.findViewById(R.id.cliente_primer_nombre)).getText().toString().trim().length() <= 0 && info.findViewById(R.id.cliente_primer_nombre).isEnabled())
            {
                Toast.makeText(getContext(), "Falta primer nombre de cliente.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((EditText)info.findViewById(R.id.cliente_primer_apellido)).getText().toString().trim().length() <= 0 && info.findViewById(R.id.cliente_primer_apellido).isEnabled())
            {
                Toast.makeText(getContext(), "Falta primer apellido de cliente.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(cliente.getFechaNacimiento().getTime() >= Calendar.getInstance().getTime().getTime())
            {
                Toast.makeText(getContext(), "Fecha de nacimiento no puede ser mayor a hoy.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((EditText)info.findViewById(R.id.cliente_correo)).getText().toString().trim().length() > 0 && info.findViewById(R.id.cliente_correo).isEnabled())
            {
                if(!((EditText)info.findViewById(R.id.cliente_correo)).getText().toString().trim().contains("@") ||
                        !((EditText)info.findViewById(R.id.cliente_correo)).getText().toString().trim().contains(".")) {
                    Toast.makeText(getContext(), "Mail incorrecto.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        else
        {
            if(((EditText)info.findViewById(R.id.cliente_nombre)).getText().toString().trim().length() <= 0 && info.findViewById(R.id.cliente_nombre).isEnabled())
            {
                Toast.makeText(getContext(), "Falta nombre de cliente.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((EditText)info.findViewById(R.id.cliente_correo_juridico)).getText().toString().trim().length() > 0 && info.findViewById(R.id.cliente_correo_juridico).isEnabled())
            {
                if(!((EditText)info.findViewById(R.id.cliente_correo_juridico)).getText().toString().trim().contains("@") ||
                        !((EditText)info.findViewById(R.id.cliente_correo_juridico)).getText().toString().trim().contains(".")) {
                    Toast.makeText(getContext(), "Mail incorrecto.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        if(((EditText)info.findViewById(R.id.cliente_identificacion)).getText().toString().trim().length() > 0 && info.findViewById(R.id.cliente_identificacion).isEnabled())
        {
            if(!IdentificationValidator.ValidateIdentification(((EditText) info.findViewById(R.id.cliente_identificacion)).getText().toString(),
                    (int) ((Spinner) info.findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner) info.findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition())))
            {
                Toast.makeText(getContext(), "Número de identificación incorrecto.", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        boolean existsPrincipal = false;
        for(int i = 0; i < listViewDirecciones.size(); i++)
        {
            if(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).getText().toString().length() <= 0 && listViewDirecciones.get(i).findViewById(R.id.cliente_direccion).isEnabled())
            {
                Toast.makeText(getContext(), "Debe de ingresar una dirección del cliente.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((AutoCompleteTextView)listViewDirecciones.get(i).findViewById(R.id.cliente_ciudad)).getText().toString().length() <= 0 && listViewDirecciones.get(i).findViewById(R.id.cliente_ciudad).isEnabled())
            {
                Toast.makeText(getContext(), "Ingrese una ciudad en la dirección del cliente.", Toast.LENGTH_LONG).show();
                return false;
            }

            if(((CheckBox)listViewDirecciones.get(i).findViewById(R.id.cliente_es_principal)).isChecked())
                existsPrincipal = true;
        }

        for(int i = 0; i < listViewContactos.size(); i++)
        {
            if(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_nombres)).getText().length() <= 0)
            {
                Toast.makeText(getContext(), "Debe de ingresar los nombres del contacto.", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        if(!existsPrincipal)
        {
            Toast.makeText(getContext(), "Debe de ingresar una dirección como principal.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
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

