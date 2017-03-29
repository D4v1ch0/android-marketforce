package rp3.auna.actividades;

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
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarContext;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pe.solera.api_payme_android.bean.Address;
import pe.solera.api_payme_android.bean.Commerce;
import pe.solera.api_payme_android.bean.Person;
import pe.solera.api_payme_android.bean.Product;
import pe.solera.api_payme_android.bean.PurchaseInformation;
import pe.solera.api_payme_android.bean.Tax;
import pe.solera.api_payme_android.bean.TransactionInformation;
import pe.solera.api_payme_android.bean_response.PayMeResponse;
import pe.solera.api_payme_android.classes.activities.LoadingAct;
import pe.solera.api_payme_android.util.Constants;
import pe.solera.api_payme_android.util.Util;
import rp3.app.BaseActivity;
import rp3.app.BaseFragment;
import rp3.auna.models.AgendaTareaActividades;
import rp3.auna.models.auna.AgendaLlamada;
import rp3.auna.models.auna.Cotizacion;
import rp3.auna.ruta.RutasDetailFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.GeopoliticalStructureAdapter;
import rp3.content.SimpleDictionaryAdapter;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.data.MessageCollection;
import rp3.data.models.GeneralValue;
import rp3.data.models.GeopoliticalStructure;
import rp3.data.models.IdentificationType;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.cliente.AgregarTarjetaFragment;
import rp3.auna.cliente.CrearClienteFragment;
import rp3.auna.models.Agenda;
import rp3.auna.models.AgendaTarea;
import rp3.auna.models.Campo;
import rp3.auna.models.Canal;
import rp3.auna.models.Cliente;
import rp3.auna.models.ClienteDireccion;
import rp3.auna.models.auna.ClienteTarjeta;
import rp3.auna.models.Contacto;
import rp3.auna.models.TipoCliente;
import rp3.auna.sync.SyncAdapter;
import rp3.auna.utils.DrawableManager;
import rp3.auna.utils.NothingSelectedSpinnerAdapter;
import rp3.auna.utils.Utils;
import rp3.maps.utils.SphericalUtil;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.IdentificationValidator;
import rp3.util.LocationUtils;

/**
 * Created by magno_000 on 10/04/2015.
 */
public class ActualizacionFragment extends BaseFragment implements AgregarTarjetaFragment.AgregarTarjetaDialogListener{

    boolean rotated = false;
    private final static String TAG = "IntPayMe";

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

    public static ActualizacionFragment newInstance(long id_agenda, int tipo, int idTarea, int id_ruta)
    {
        ActualizacionFragment fragment = new ActualizacionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_AGENDA, id_agenda);
        args.putInt(ARG_TIPO, tipo);
        args.putInt(ARG_TAREA, idTarea);
        args.putInt(ARG_RUTA, id_ruta);
        fragment.setArguments(args);
        return fragment;
    }

    private NumberFormat numberFormat;
    private boolean puedeFinalizar = false;
    public static String ARG_AGENDA = "cliente";
    public static String ARG_TIPO = "tipo";
    public static String ARG_TAREA = "tarea";
    public static String ARG_RUTA = "ruta";
    public static String ARG_PARAMS = "parametros";
    public static String ARG_RESPONSE = "response";
    private LayoutInflater inflater;
    private LinearLayout ContactosContainer, DireccionContainer, TarjetasContainer;
    private List<LinearLayout> listViewDirecciones, listViewContactos, listViewTarjetas;
    private List<GeopoliticalStructure> listCiudades;
    private int curentPage;
    private Agenda agd;
    private long idAgenda = 0;
    private int tipo, idTarea, idRuta;
    Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
    boolean isClient;
    int posContact = -1;
    public Cliente cliente;
    public List<String> contactPhotos;
    private List<GeopoliticalStructure> ciudades;
    private GeopoliticalStructureAdapter adapter;
    private Cotizacion cotizacion;
    private AgregarTarjetaFragment tarjetaFragment;
    private ClienteTarjeta agregarTarjeta;
    private String response, idOperacion = "";
    private Agenda agenda;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cliente = new Cliente();
        contactPhotos = new ArrayList<String>();
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        numberFormat.setMinimumFractionDigits(0);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:
                if(Validaciones())
                {
                    if(puedeFinalizar) {
                        Grabar();
                        finish();
                    }
                    else
                    {
                        if(cotizacion.getOpcion() != 2)
                        {
                            validarSolicitud();
                        }
                        else
                        {
                            Toast.makeText(this.getContext(), "Debe realizar la venta para poder finalizar esta tarea.", Toast.LENGTH_LONG).show();
                        }
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

    private void Grabar() {
        Cliente cli = agd.getCliente();
        ClienteDireccion cliDir = agd.getClienteDireccion();
        Contacto contacto = agd.getContacto();
        cli.setIdCanal((int) ((Spinner) getRootView().findViewById(R.id.cliente_canal)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.cliente_canal)).getSelectedItemPosition()));
        cli.setIdTipoIdentificacion((int) ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition()));
        cli.setIdentificacion(((EditText) getRootView().findViewById(R.id.cliente_identificacion)).getText().toString());
        cli.setTarjeta(((EditText) getRootView().findViewById(R.id.cliente_tarjeta)).getText().toString());
        cli.setTipoPersona(((GeneralValue)((Spinner)getRootView().findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItem()).getCode());
        cli.setIdTipoCliente((int) ((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).getSelectedItemPosition()));
        if(cliente.getURLFoto() != null && !cliente.getURLFoto().trim().equals(""))
            cli.setURLFoto(cliente.getURLFoto());
        if(((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItemPosition() == 1)
        {
            cli.setNombre1(((EditText)getRootView().findViewById(R.id.cliente_primer_nombre)).getText().toString());
            cli.setNombre2(((EditText)getRootView().findViewById(R.id.cliente_segundo_nombre)).getText().toString());
            cli.setApellido1(((EditText)getRootView().findViewById(R.id.cliente_primer_apellido)).getText().toString());
            cli.setApellido2(((EditText)getRootView().findViewById(R.id.cliente_segundo_apellido)).getText().toString());
            cli.setCorreoElectronico(((EditText)getRootView().findViewById(R.id.cliente_correo)).getText().toString());
            cli.setGenero(((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
            cli.setEstadoCivil(((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
            cli.setNombreCompleto(cli.getNombre1() + " " + cli.getNombre2() + " " + cli.getApellido1() + " " + cli.getApellido2());
            cli.setFechaNacimiento(cliente.getFechaNacimiento());
        }
        else
        {
            cli.setApellido1("");
            cli.setApellido2("");
            cli.setNombre2("");
            cli.setNombre1(((EditText)getRootView().findViewById(R.id.cliente_nombre)).getText().toString());
            cli.setActividadEconomica(((EditText)getRootView().findViewById(R.id.cliente_actividad_economica)).getText().toString());
            cli.setCorreoElectronico(((EditText)getRootView().findViewById(R.id.cliente_correo_juridico)).getText().toString());
            cli.setRazonSocial(((EditText)getRootView().findViewById(R.id.cliente_razon_social)).getText().toString());
            cli.setPaginaWeb(((EditText)getRootView().findViewById(R.id.cliente_pagina_web)).getText().toString());
            cli.setGenero(((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
            cli.setEstadoCivil(((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
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
            cliDir.setDireccion(((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_direccion)).getText().toString());
            cliDir.setTipoDireccion(((GeneralValue) ((Spinner) listViewDirecciones.get(0).findViewById(R.id.cliente_tipo_direccion_spinner)).getSelectedItem()).getCode());
            cliDir.setEsPrincipal(((CheckBox) listViewDirecciones.get(0).findViewById(R.id.cliente_es_principal)).isChecked());
            cliDir.setTelefono1(((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_telefono1)).getText().toString());
            cliDir.setTelefono2(((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_telefono2)).getText().toString());
            cliDir.setReferencia(((EditText) listViewDirecciones.get(0).findViewById(R.id.cliente_referencia)).getText().toString());
            cliDir.setCiudadDescripcion(((AutoCompleteTextView) listViewDirecciones.get(0).findViewById(R.id.cliente_ciudad)).getText().toString());
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

        //Grabo detalle de tarea
        AgendaTareaActividades act = null;
        if(agd.getIdAgenda() != 0)
            act = AgendaTareaActividades.getActividadSimple(getDataBase(), agd.getIdRuta(), agd.getIdAgenda(), idTarea, 1);
        else
            act = AgendaTareaActividades.getActividadSimpleIdIntern(getDataBase(), agd.getID(), idTarea, 1);
        if(act == null)
        {
            act = new AgendaTareaActividades();
            act.setIdAgenda((int) agd.getIdAgenda());
            act.setIdTarea(idTarea);
            act.setIdRuta(agd.getIdRuta());
            act.setIdTareaActividad(1);
            act.set_idAgenda(agd.getID());
            AgendaTareaActividades.insert(getDataBase(), act);
        }

        act.setResultado("Venta Realizada\n" + " - Forma de Pago: " + ((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_forma_pago)).getSelectedItem()).getValue() + "\n"
                    + " - Solicitud: " + ((EditText) getRootView().findViewById(R.id.cliente_solicitud)).getText().toString() + "\n"
                    + " - Valor: S/. " + numberFormat.format(cotizacion.getValor()));
        AgendaTareaActividades.update(getDataBase(), act);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);


        listViewDirecciones = new ArrayList<LinearLayout>();
        listViewContactos = new ArrayList<LinearLayout>();
        listViewTarjetas = new ArrayList<LinearLayout>();
        listCiudades = new ArrayList<GeopoliticalStructure>();
        //ciudades = GeopoliticalStructure.getGeopoliticalStructureCities(getDataBase());

        adapter = new GeopoliticalStructureAdapter(getContext(), getDataBase());

        inflater = (LayoutInflater) this.getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        ((TextView) getRootView().findViewById(R.id.agregar_direccion)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addDireccion();

            }
        });
        DireccionContainer = (LinearLayout) getRootView().findViewById(R.id.crear_cliente_container_direccion);

        ((TextView) getRootView().findViewById(R.id.agregar_contacto)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addContacto();

            }
        });
        ContactosContainer = (LinearLayout) getRootView().findViewById(R.id.crear_cliente_container_contacto);

        ((TextView) getRootView().findViewById(R.id.agregar_tarjeta)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addTarjeta();

            }
        });
        TarjetasContainer = (LinearLayout) getRootView().findViewById(R.id.crear_cliente_container_forma_pago);

        SimpleGeneralValueAdapter programaAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), Contants.GENERAL_TABLE_PROGRAMAS);
        SimpleGeneralValueAdapter tipoPersonaAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.auna.Contants.GENERAL_TABLE_TIPO_PERSONA);
        SimpleGeneralValueAdapter tipoEstadoCivilAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.auna.Contants.GENERAL_TABLE_ESTADO_CIVIL);
        SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.auna.Contants.GENERAL_TABLE_GENERO);
        SimpleGeneralValueAdapter tipoFormaPago = new SimpleGeneralValueAdapter(getContext(), getDataBase(), Contants.GENERAL_TABLE_FORMA_PAGO);
        SimpleIdentifiableAdapter tipoCliente = new SimpleIdentifiableAdapter(getContext(), TipoCliente.getTipoCliente(getDataBase(), ""));
        SimpleIdentifiableAdapter tipoCanal = new SimpleIdentifiableAdapter(getContext(), Canal.getCanal(getDataBase(), ""));
        SimpleDictionaryAdapter tipoIdentificacion = new SimpleDictionaryAdapter(getContext(), IdentificationType.getAll(getDataBase()));

        ((Spinner) getRootView().findViewById(R.id.cliente_programa)).setAdapter(new NothingSelectedSpinnerAdapter(
                programaAdapter,
                R.layout.spinner_empty_selected,
                this.getContext(), "Seleccione un programa"));
        ((Spinner) getRootView().findViewById(R.id.cliente_programa)).setPrompt("Seleccione un programa");

        ((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).setAdapter(tipoPersonaAdapter);
        ((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).setPrompt("Seleccione un tipo de persona");
        ((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 1) {
                    ((LinearLayout) getRootView().findViewById(R.id.crear_cliente_content_natural)).setVisibility(View.VISIBLE);
                    ((LinearLayout) getRootView().findViewById(R.id.crear_cliente_content_juridico)).setVisibility(View.GONE);
                } else {
                    ((LinearLayout) getRootView().findViewById(R.id.crear_cliente_content_natural)).setVisibility(View.GONE);
                    ((LinearLayout) getRootView().findViewById(R.id.crear_cliente_content_juridico)).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        ((Spinner) getRootView().findViewById(R.id.cliente_forma_pago)).setAdapter(tipoFormaPago);
        ((Spinner) getRootView().findViewById(R.id.cliente_forma_pago)).setPrompt("Seleccione una forma de pago");
        ((Spinner) getRootView().findViewById(R.id.cliente_forma_pago)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 1 && cotizacion.getOpcion() == 2) {
                    getRootView().findViewById(R.id.agregar_tarjeta).setVisibility(View.VISIBLE);
                    TarjetasContainer.setVisibility(View.VISIBLE);
                } else {
                    getRootView().findViewById(R.id.agregar_tarjeta).setVisibility(View.GONE);
                    TarjetasContainer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        ((Spinner) getRootView().findViewById(R.id.cliente_estado_civil)).setAdapter(tipoEstadoCivilAdapter);
        ((Spinner) getRootView().findViewById(R.id.cliente_estado_civil)).setPrompt("Seleccione un estado civil");
        ((Spinner) getRootView().findViewById(R.id.cliente_genero)).setAdapter(tipoGeneroAdapter);
        ((Spinner) getRootView().findViewById(R.id.cliente_genero)).setPrompt("Seleccione un género");
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).setAdapter(tipoCliente);
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).setAdapter(new NothingSelectedSpinnerAdapter(
                tipoCliente,
                R.layout.spinner_empty_selected,
                this.getContext(), "Tipo de Cliente"));
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).setPrompt("Seleccione una tipo de cliente");
        ((Spinner) getRootView().findViewById(R.id.cliente_canal)).setAdapter(tipoCanal);
        ((Spinner) getRootView().findViewById(R.id.cliente_canal)).setAdapter(new NothingSelectedSpinnerAdapter(
                tipoCanal,
                R.layout.spinner_empty_selected,
                this.getContext(), "Canal"));
        ((Spinner) getRootView().findViewById(R.id.cliente_canal)).setPrompt("Seleccione un canal");
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).setAdapter(tipoIdentificacion);
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).setPrompt("Seleccione un tipo de identificación");
        ((ImageButton) getRootView().findViewById(R.id.cliente_foto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClient = true;
                takePicture(1);
            }
        });
        ((EditText) getRootView().findViewById(R.id.cliente_fecha_nacimiento)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDatePicker(0);
            }
        });


        if (getArguments().containsKey(ARG_TAREA) && getArguments().getInt(ARG_TAREA) != 0 && !rotated)
            idTarea = getArguments().getInt(ARG_TAREA);
        if (getArguments().containsKey(ARG_AGENDA) && getArguments().getLong(ARG_AGENDA) != 0 && !rotated) {
            idAgenda = getArguments().getLong(ARG_AGENDA);
            agd = Agenda.getAgenda(getDataBase(), idAgenda);
            setDatosClientes();
        }
        if (getArguments().containsKey(ARG_TIPO) && getArguments().getInt(ARG_TIPO) != 0 && !rotated) {
            tipo = getArguments().getInt(ARG_TIPO);
            SetCampos();
        }
        if (getArguments().containsKey(ARG_RUTA) && getArguments().getInt(ARG_RUTA) != 0 && !rotated)
            idRuta = getArguments().getInt(ARG_RUTA);

        cotizacion = Cotizacion.getCotizacionInt(getDataBase(), idAgenda, idRuta, idTarea);
        if(cotizacion != null && cotizacion.getID() != 0)
        {
            try {
                NumberFormat numberFormat;
                numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(2);
                numberFormat.setMinimumFractionDigits(2);
                //Se carga programa
                JSONArray jsonArray = new JSONArray(cotizacion.getParametros());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ((Spinner) getRootView().findViewById(R.id.cliente_programa)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.cliente_programa)).getAdapter(), jsonObject.getString("CodigoPrograma")));
                }

                //Se carga valor de cotizacion
                ((EditText) getRootView().findViewById(R.id.cliente_valor)).setText(numberFormat.format(cotizacion.getValor()));

            } catch (Exception ex) {

            }

            //Valido que forma de pago aparece a partir de la cotizacion
            if(cotizacion.getOpcion() == 2)
                ((Spinner) getRootView().findViewById(R.id.cliente_forma_pago)).setSelection(1);
            if(cotizacion.getOpcion() == 3)
                ((Spinner) getRootView().findViewById(R.id.cliente_forma_pago)).setSelection(0);
            if(cotizacion.getOpcion() != 1)
                ((Spinner) getRootView().findViewById(R.id.cliente_forma_pago)).setEnabled(false);

        }
        ((Spinner) getRootView().findViewById(R.id.cliente_programa)).setEnabled(false);

    }

    private void SetCampos() {
        /*List<Campo> campos = Campo.getCampos(getDataBase(), tipo);
        Cliente cli = agd.getCliente();
        Contacto cont = agd.getContacto();
        getRootView().findViewById(R.id.agregar_direccion).setVisibility(View.GONE);
        getRootView().findViewById(R.id.agregar_contacto).setVisibility(View.GONE);
        getRootView().findViewById(R.id.crear_cliente_tipo_persona).setEnabled(false);
        for(Campo campo : campos)
        {
            //if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_TIPO_IDENTIFICACION))
            //    getRootView().findViewById(R.id.cliente_tipo_identificacion).setEnabled(false);
            //if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_IDENTIFICACION))
            //    getRootView().findViewById(R.id.cliente_identificacion).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_TIPO_CLI))
                getRootView().findViewById(R.id.cliente_tipo_cliente).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CANAL))
                getRootView().findViewById(R.id.cliente_canal).setEnabled(false);
            if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FOTO))
                getRootView().findViewById(R.id.cliente_foto).setEnabled(false);

            if (cli.getTipoPersona().equalsIgnoreCase("N")) {
                //if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FECHA_NACIMIENTO))
                //    getRootView().findViewById(R.id.cliente_fecha_nacimiento).setEnabled(false);
                //if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_GENERO))
                //    getRootView().findViewById(R.id.cliente_genero).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ESTADO_CIVIL))
                    getRootView().findViewById(R.id.cliente_estado_civil).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_NOMBRE_NATURAL))
                    getRootView().findViewById(R.id.cliente_primer_nombre).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_NOMBRE_NATURAL))
                    getRootView().findViewById(R.id.cliente_segundo_nombre).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_APELLIDO_NATURAL))
                    getRootView().findViewById(R.id.cliente_primer_apellido).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_APELLIDO_NATURAL))
                    getRootView().findViewById(R.id.cliente_segundo_apellido).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO))
                    getRootView().findViewById(R.id.cliente_correo).setEnabled(false);
            }
            else
            {
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_NOMBRE_JURIDICO))
                    getRootView().findViewById(R.id.cliente_nombre).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_RAZON_SOCIAL))
                    getRootView().findViewById(R.id.cliente_razon_social).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PAGINA_WEB))
                    getRootView().findViewById(R.id.cliente_pagina_web).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ACTIVIDAD_ECONOMICA))
                    getRootView().findViewById(R.id.cliente_actividad_economica).setEnabled(false);
                if(campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO))
                    getRootView().findViewById(R.id.cliente_correo_juridico).setEnabled(false);
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

        }*/
    }

    private void setDatosClientes() {
        DrawableManager DManager = new DrawableManager();
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM yyyy");
        Cliente cli = agd.getCliente();
        ClienteDireccion cliDir = agd.getClienteDireccion();
        Contacto contacto = agd.getContacto();
        ((Spinner) getRootView().findViewById(R.id.cliente_canal)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.cliente_canal)).getAdapter(), cli.getIdCanal()));
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getAdapter(), cli.getTipoIdentificacionId()));
        ((EditText) getRootView().findViewById(R.id.cliente_identificacion)).setText(cli.getIdentificacion());
        ((EditText) getRootView().findViewById(R.id.cliente_tarjeta)).setText(cli.getTarjeta());
        ((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).getAdapter(), cli.getTipoPersona()));
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).getAdapter(), cli.getIdTipoCliente()));
        if (cli.getFechaNacimiento() != null && cli.getFechaNacimiento().getTime() != 0) {
            ((EditText) getRootView().findViewById(R.id.cliente_fecha_nacimiento)).setText(format1.format(cli.getFechaNacimiento()));
            cliente.setFechaNacimiento(cli.getFechaNacimiento());
        }
        DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getActivity(), cli.getURLFoto()),
                (ImageButton) getRootView().findViewById(R.id.cliente_foto));
        if (cli.getTipoPersona().equalsIgnoreCase("N")) {
            ((EditText) getRootView().findViewById(R.id.cliente_primer_nombre)).setText(cli.getNombre1());
            ((EditText) getRootView().findViewById(R.id.cliente_segundo_nombre)).setText(cli.getNombre2());
            ((EditText) getRootView().findViewById(R.id.cliente_primer_apellido)).setText(cli.getApellido1());
            ((EditText) getRootView().findViewById(R.id.cliente_segundo_apellido)).setText(cli.getApellido2());
            ((EditText) getRootView().findViewById(R.id.cliente_correo)).setText(cli.getCorreoElectronico());
            ((Spinner) getRootView().findViewById(R.id.cliente_genero)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.cliente_genero)).getAdapter(), cli.getGenero()));
            ((Spinner) getRootView().findViewById(R.id.cliente_estado_civil)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.cliente_estado_civil)).getAdapter(), cli.getEstadoCivil()));
            ((LinearLayout) getRootView().findViewById(R.id.crear_cliente_content_natural)).setVisibility(View.VISIBLE);
            ((LinearLayout) getRootView().findViewById(R.id.crear_cliente_content_juridico)).setVisibility(View.GONE);
        } else {
            ((EditText) getRootView().findViewById(R.id.cliente_nombre)).setText(cli.getNombre1());
            ((EditText) getRootView().findViewById(R.id.cliente_actividad_economica)).setText(cli.getActividadEconomica());
            ((EditText) getRootView().findViewById(R.id.cliente_correo_juridico)).setText(cli.getCorreoElectronico());
            ((EditText) getRootView().findViewById(R.id.cliente_razon_social)).setText(cli.getRazonSocial());
            ((EditText) getRootView().findViewById(R.id.cliente_pagina_web)).setText(cli.getPaginaWeb());
            ((LinearLayout) getRootView().findViewById(R.id.crear_cliente_content_natural)).setVisibility(View.GONE);
            ((LinearLayout) getRootView().findViewById(R.id.crear_cliente_content_juridico)).setVisibility(View.VISIBLE);
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
            getRootView().findViewById(R.id.cliente_contactos_header).setVisibility(View.GONE);
        }


    }

    @Override
    public void onDailogDatePickerChange(int id, Calendar c) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM yyyy");
        ((EditText) getRootView().findViewById(R.id.cliente_fecha_nacimiento)).setText(format1.format(c.getTime()));
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
                        //captureIntent.putExtra("crop", "true");
                        captureIntent.putExtra("aspectX", 1);
                        captureIntent.putExtra("aspectY", 1);
                        getActivity().startActivityForResult(captureIntent, idView);

                    }
                });
        myAlertDialog.show();
    }

    private void addTarjeta()
    {
        if(validacionesAlignet()) {
            tarjetaFragment = AgregarTarjetaFragment.newInstance(listViewTarjetas.size());
            showDialogFragment(tarjetaFragment, "Agregar Tarjeta");
        }
    }

    private void addDireccion()
    {
        final LinearLayout direccion = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_cliente_direccion_detail, null);
        final int pos = listViewDirecciones.size();
        direccion.findViewById(R.id.cliente_direccion).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    for (LinearLayout contacto : listViewContactos) {
                        ((Spinner) contacto.findViewById(R.id.cliente_direccion_contacto)).setAdapter(getDirecciones());
                    }
                }
            }
        });
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
        SimpleGeneralValueAdapter tipoDireccionAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.auna.Contants.GENERAL_TABLE_TIPO_DIRECCION);
        ((Spinner) direccion.findViewById(R.id.cliente_tipo_direccion_spinner)).setAdapter(tipoDireccionAdapter);
        ((Spinner) direccion.findViewById(R.id.cliente_tipo_direccion_spinner)).setPrompt("Seleccione tipo de dirección");

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
        ((Button) contacto.findViewById(R.id.eliminar_contacto)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listViewContactos.remove(contacto);
                ContactosContainer.removeView(contacto);
                contactPhotos.remove(pos);
            }
        });
        contactPhotos.add("");
        ((Spinner) contacto.findViewById(R.id.cliente_direccion_contacto)).setAdapter(getDirecciones());
        ((ImageButton) contacto.findViewById(R.id.cliente_contacto_foto)).setOnClickListener(new View.OnClickListener() {

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RESULT_API) {
            if (resultCode == RESULT_OK) {
                boolean successful = data.getBooleanExtra("successful", false);
                PayMeResponse payMeResponse = (PayMeResponse) data.getSerializableExtra("payMeResponse");
                if (successful) {
                    registrarPagoOncosys(payMeResponse);
                    if(cotizacion.getOpcion() == 2)
                        agregaTarjeta();
                    else
                    {
                        puedeFinalizar = true;
                        Grabar();
                        finishAgenda();
                        finish();
                    }
                } else {
                    if (payMeResponse != null) {
                        Toast.makeText(this.getActivity(), "Transacción rechazada - Response: " + payMeResponse.toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this.getActivity(), "Transacción rechazada - Response: No se obtuvo respuesta", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Log.d(TAG, "pay - error, no existe data para generar el objeto PaymeResponse");
            }
        }
        else if (resultCode == RESULT_OK) {
            Bitmap pree = null;
            if (data.getData() != null) {
                try {
                    pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (data.getExtras().containsKey("data"))
                pree = (Bitmap) data.getExtras().get("data");
            else
                try {
                    photo = Uri.parse(data.getAction());
                    pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (isClient) {
                ((ImageButton) getRootView().findViewById(R.id.cliente_foto)).setImageBitmap(pree);
                cliente.setURLFoto(Utils.SaveBitmap(pree, "new_client"));
            } else {
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
        return new ArrayAdapter<String>(getActivity(), rp3.core.R.layout.base_rowlist_simple_spinner_small, list);
    }

    public ArrayAdapter<String> getFechasCaducidad()
    {
        SimpleDateFormat format1 = new SimpleDateFormat("MM/yyyy");
        List<String> list = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        for(int i = 0; i < 50; i ++)
        {
            list.add(format1.format(cal.getTime()));
            cal.add(Calendar.MONTH, 1);
        }
        return new ArrayAdapter<String>(getActivity(), rp3.core.R.layout.base_rowlist_simple_spinner_small, list);
    }

    public boolean Validaciones()
    {
        if(((EditText)getRootView().findViewById(R.id.cliente_identificacion)).getText().toString().trim().length() >= 0 && getRootView().findViewById(R.id.cliente_identificacion).isEnabled())
        {
            Cliente proof = Cliente.getClienteByIdentificacion(getDataBase(), ((EditText)getRootView().findViewById(R.id.cliente_identificacion)).getText().toString().trim());
            if(proof != null && proof.getID() != agd.getCliente().getID()) {
                Toast.makeText(getContext(), "Ya existe cliente con esta identificación.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(listViewDirecciones.size() <= 0)
        {
            Toast.makeText(getContext(), "No se puede agregar clientes sin dirección.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).getSelectedItemPosition() == 0)
        {
            Toast.makeText(getContext(), "Falta especificar el tipo de cliente.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(((Spinner) getRootView().findViewById(R.id.cliente_canal)).getSelectedItemPosition() == 0)
        {
            Toast.makeText(getContext(), "Falta especificar el canal del cliente.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItemPosition() == 1)
        {
            if(((EditText)getRootView().findViewById(R.id.cliente_primer_nombre)).getText().toString().trim().length() <= 0 && getRootView().findViewById(R.id.cliente_primer_nombre).isEnabled())
            {
                Toast.makeText(getContext(), "Falta primer nombre de cliente.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((EditText)getRootView().findViewById(R.id.cliente_primer_apellido)).getText().toString().trim().length() <= 0 && getRootView().findViewById(R.id.cliente_primer_apellido).isEnabled())
            {
                Toast.makeText(getContext(), "Falta primer apellido de cliente.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(getRootView().findViewById(R.id.cliente_fecha_nacimiento).isEnabled() && cliente.getFechaNacimiento().getTime() >= Calendar.getInstance().getTime().getTime())
            {
                Toast.makeText(getContext(), "Fecha de nacimiento no puede ser mayor a hoy.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((EditText)getRootView().findViewById(R.id.cliente_correo)).getText().toString().trim().length() > 0 && getRootView().findViewById(R.id.cliente_correo).isEnabled())
            {
                if(!((EditText)getRootView().findViewById(R.id.cliente_correo)).getText().toString().trim().contains("@") ||
                        !((EditText)getRootView().findViewById(R.id.cliente_correo)).getText().toString().trim().contains(".")) {
                    Toast.makeText(getContext(), "Mail incorrecto.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        else
        {
            if(((EditText)getRootView().findViewById(R.id.cliente_nombre)).getText().toString().trim().length() <= 0 && getRootView().findViewById(R.id.cliente_nombre).isEnabled())
            {
                Toast.makeText(getContext(), "Falta nombre de cliente.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((EditText)getRootView().findViewById(R.id.cliente_correo_juridico)).getText().toString().trim().length() > 0 && getRootView().findViewById(R.id.cliente_correo_juridico).isEnabled())
            {
                if(!((EditText)getRootView().findViewById(R.id.cliente_correo_juridico)).getText().toString().trim().contains("@") ||
                        !((EditText)getRootView().findViewById(R.id.cliente_correo_juridico)).getText().toString().trim().contains(".")) {
                    Toast.makeText(getContext(), "Mail incorrecto.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        if(((EditText)getRootView().findViewById(R.id.cliente_identificacion)).getText().toString().trim().length() > 0 && getRootView().findViewById(R.id.cliente_identificacion).isEnabled())
        {
            if(!IdentificationValidator.ValidateIdentification(getDataBase(), ((EditText) getRootView().findViewById(R.id.cliente_identificacion)).getText().toString(),
                    (int) ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition())))
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
            try {
                if (((GeneralValue) spinnerAdapter.getItem(f)).getCode().equals(i))
                    position = f;
            }
            catch (Exception ex)
            {}
        }
        return position;
    }

    @Override
    public void onFinishAgregarTarjetaDialog(ClienteTarjeta clienteTarjeta) {
        agregarTarjeta = clienteTarjeta;
        validarSolicitud();

    }

    public void agregaTarjeta()
    {
        final LinearLayout tarjeta = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_cliente_tarjeta_detail, null);
        final int pos = listViewTarjetas.size();
        ((Button) tarjeta.findViewById(R.id.eliminar_tarjeta)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listViewTarjetas.remove(tarjeta);
                TarjetasContainer.removeView(tarjeta);
            }
        });

        ((Button) tarjeta.findViewById(R.id.eliminar_tarjeta)).setVisibility(View.GONE);
        ((TextView) getRootView().findViewById(R.id.agregar_tarjeta)).setVisibility(View.GONE);

        List<GeneralValue> marcas = GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_PROCESADORA);
        SimpleGeneralValueAdapter procesadorasAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), Contants.GENERAL_TABLE_PROCESADORA);
        ((Spinner) tarjeta.findViewById(R.id.cliente_tarjeta)).setAdapter(procesadorasAdapter);
        for(int i = 0; i < marcas.size(); i++)
        {
            if(marcas.get(i).getCode().equalsIgnoreCase(agregarTarjeta.getIdMarcaTarjeta()))
                ((Spinner) tarjeta.findViewById(R.id.cliente_tarjeta)).setSelection(i);
        }

        ((Spinner) tarjeta.findViewById(R.id.cliente_tarjeta_fecha)).setAdapter(getFechasCaducidad());
        ((Spinner) tarjeta.findViewById(R.id.cliente_tarjeta_fecha)).setSelection(((ArrayAdapter<String>) ((Spinner) tarjeta.findViewById(R.id.cliente_tarjeta_fecha)).getAdapter()).getPosition(agregarTarjeta.getFechaCaducidad()));
        ((EditText) tarjeta.findViewById(R.id.cliente_numero_tarjeta)).setText(agregarTarjeta.getNumero());
        ((EditText) tarjeta.findViewById(R.id.cliente_cod_seguridad)).setText(agregarTarjeta.getCodigoSeguridad());
        ((CheckBox) tarjeta.findViewById(R.id.cliente_es_principal_tarjeta)).setChecked(agregarTarjeta.getEsPrincipal());

        tarjeta.findViewById(R.id.validar_tarjeta).setVisibility(View.GONE);
        tarjeta.findViewById(R.id.cliente_numero_tarjeta).setEnabled(false);
        tarjeta.findViewById(R.id.cliente_cod_seguridad).setEnabled(false);
        tarjeta.findViewById(R.id.cliente_tarjeta_fecha).setEnabled(false);
        tarjeta.findViewById(R.id.cliente_tarjeta).setEnabled(false);
        tarjeta.findViewById(R.id.cliente_es_principal_tarjeta).setEnabled(false);
        ((CheckBox) tarjeta.findViewById(R.id.cliente_tarjeta_valida)).setChecked(true);

        TarjetasContainer.addView(tarjeta);
        listViewTarjetas.add(tarjeta);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                tarjetaFragment.dismissAllowingStateLoss();
            }
        }, 1000);
        Grabar();
        finishAgenda();

    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_VALIDA_SOLICITUD)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                response = data.getString(ARG_RESPONSE);
                validarResponse();
            }
        }
    }

    private void finishAgenda()
    {
        agenda = Agenda.getAgenda(getDataBase(), idAgenda);
        if (agenda == null)
            agenda = Agenda.getAgendaClienteNull(getDataBase(), idAgenda);
        if (agenda.getClienteDireccion() != null && agenda.getClienteDireccion().getTelefono1() != null && agenda.getClienteDireccion().getTelefono1().length() > 0) {
            List<AgendaLlamada> llamadas = RutasDetailFragment.getCallDetails(getContext(), agenda.getClienteDireccion().getTelefono1(), agenda);
            for (AgendaLlamada llamada : llamadas)
                AgendaLlamada.insert(getDataBase(), llamada);
        }

        agenda.setEstadoAgenda(Contants.ESTADO_VISITADO);
        agenda.setEstadoAgendaDescripcion(Contants.DESC_VISITADO);
        agenda.setFechaFinReal(Calendar.getInstance().getTime());
        final Context ctx = getContext();
        if (agenda.getLatitud() == 0 && agenda.getLongitud() == 0) {
            Location location = LocationUtils.getLastLocation(ctx);
            if (location != null) {
                agenda.setLatitud(location.getLatitude());
                agenda.setLongitud(location.getLongitude());
                LatLng pos = new LatLng(agenda.getLatitud(), agenda.getLongitud());
                LatLng cli = new LatLng(agenda.getClienteDireccion().getLatitud(), agenda.getClienteDireccion().getLongitud());
                agenda.setDistancia((long) SphericalUtil.computeDistanceBetween(pos, cli));
            }
        }
        agenda.setEnviado(false);
        if(idOperacion.trim().length()>0)
            agenda.setObservaciones("# de Operación: " + idOperacion);

        Agenda.update(getDataBase(), agenda);
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ENVIAR_AGENDA);
        bundle.putInt(RutasDetailFragment.ARG_AGENDA_ID, (int) idAgenda);
        requestSync(bundle);
        //new AsyncUpdater.UpdateAgenda().execute((int) idAgenda);
        if (agenda.getLatitud() == 0 && agenda.getLongitud() == 0) {
            try {
                LocationUtils.getLocation(ctx, new LocationUtils.OnLocationResultListener() {

                    @Override
                    public void getLocationResult(Location location) {
                        if (location != null) {
                            agenda.setLatitud(location.getLatitude());
                            agenda.setLongitud(location.getLongitude());
                        }
                        agenda.setEnviado(false);
                        LatLng pos = new LatLng(agenda.getLatitud(), agenda.getLongitud());
                        LatLng cli = new LatLng(agenda.getClienteDireccion().getLatitud(), agenda.getClienteDireccion().getLongitud());
                        agenda.setDistancia((long) SphericalUtil.computeDistanceBetween(pos, cli));
                        BaseActivity act = (BaseActivity) ctx;
                        Agenda.update(act.getDataBase(), agenda);
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_AGENDA_GEOLOCATION);
                        bundle.putInt(RutasDetailFragment.ARG_AGENDA_ID, (int) idAgenda);
                        bundle.putDouble(RutasDetailFragment.ARG_LONGITUD, location.getLongitude());
                        bundle.putDouble(RutasDetailFragment.ARG_LATITUD, location.getLatitude());
                        act.requestSync(bundle);

                    }
                });
            } catch (Exception ex) {
            }
        }
    }

    //region Registro Pago
    public void registrarPagoOncosys(PayMeResponse response)
    {
        Bundle bundle = new Bundle();
        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_REGISTRAR_PAGO);
        bundle.putString(ARG_PARAMS, generaJSONPago(response));
        requestSync(bundle);
        puedeFinalizar = true;
    }

    public String generaJSONPago(PayMeResponse response) {

        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("IdOperacion", response.getOperationNumber());
            jsonObject.put("Tarjeta", response.getCardNumber());
            jsonObject.put("Monto", numberFormat.format(cotizacion.getValor()));
            jsonObject.put("Moneda", "PEN");
            jsonObject.put("EntidadProcesadora", cotizacion.getOpcion() == 2 ? "" + agregarTarjeta.getIdMarcaTarjeta() : "02");
            jsonObject.put("CodAut", response.getIdTransaction());

        } catch (Exception ex) {
        }

        return jsonObject.toString();

    }
    //endregion

    //region Validar Solicitud

    private boolean validarResponse()
    {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString("Result").equalsIgnoreCase("0"))
            {
                if(((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_forma_pago)).getSelectedItem()).getCode().equalsIgnoreCase("T"))
                {
                    if(cotizacion.getOpcion() != 3) {
                        idOperacion = jsonObject.getLong("IdTransaccion") + "";
                        listener();
                    }
                    else
                    {
                        agregaTarjeta();
                    }
                }
                else
                {
                    puedeFinalizar = true;
                    Grabar();
                    finishAgenda();
                    finish();
                }
            }
            else
            {
                Toast.makeText(this.getActivity(), "Ocurrio un error al intentar validar su solicitud. Por favor vuelva a intentarlo o comuníquese con un administrador", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this.getActivity(), "Ocurrio un error al intentar validar su solicitud. Por favor vuelva a intentarlo o comuníquese con un administrador", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void validarSolicitud()
    {
        if(ValidacionesSolicitud())
        {
            if(idOperacion.equalsIgnoreCase("")) {
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_VALIDA_SOLICITUD);
                bundle.putString(ARG_PARAMS, generaJSON());
                requestSync(bundle);
                showDialogProgress(R.string.message_title_connecting, R.string.message_validando_solicitud);
            }
            else
            {
                listener();
            }
        }
    }

    private boolean ValidacionesSolicitud()
    {
        if(cotizacion == null || cotizacion.getID() == 0)
        {
            Toast.makeText(this.getActivity(), "Debe ingresar previamente una cotización.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((EditText) getRootView().findViewById(R.id.cliente_solicitud)).getText().length() <= 0)
        {
            Toast.makeText(this.getActivity(), "Falta ingresar número de solicitud.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((Spinner) getRootView().findViewById(R.id.cliente_programa)).getSelectedItemPosition() == 0)
        {
            Toast.makeText(this.getActivity(), "Debe escoger un programa.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((EditText) getRootView().findViewById(R.id.cliente_identificacion)).getText().length() <= 0)
        {
            Toast.makeText(this.getActivity(), "Falta ingresar número de identificación de cliente.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((EditText) getRootView().findViewById(R.id.cliente_primer_apellido)).getText().length() <= 0)
        {
            Toast.makeText(this.getActivity(), "Falta ingresar primer apellido de cliente", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((EditText) getRootView().findViewById(R.id.cliente_primer_nombre)).getText().length() <= 0)
        {
            Toast.makeText(this.getActivity(), "Falta ingresar primer nombre de cliente.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(((EditText) getRootView().findViewById(R.id.cliente_correo)).getText().length() <= 0)
        {
            Toast.makeText(this.getActivity(), "Falta ingresar correo electrónico de cliente.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(cliente.getFechaNacimiento() == null || cliente.getFechaNacimiento().getTime() <= 0)
        {
            Toast.makeText(this.getActivity(), "Falta ingresar fecha de nacimiento de cliente.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private String generaJSON()
    {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("NumSolicitud", ((EditText) getRootView().findViewById(R.id.cliente_solicitud)).getText().toString());
            jsonObject.put("CodPrograma", ((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_programa)).getSelectedItem()).getCode());
            jsonObject.put("Asesor", PreferenceManager.getString(Contants.KEY_AGENTE_IDENTIFICACION, "19329618"));
            jsonObject.put("ModoTarifa", cotizacion.getOpcion() == 2 ? "01" : "02");
            jsonObject.put("FrecuenciaPago", cotizacion.getOpcion() == 1 ? "01" : "02");
            jsonObject.put("TipoTarjeta", cotizacion.getOpcion() == 3 ? "02" : "01");
            jsonObject.put("Monto", numberFormat.format(cotizacion.getValor()));
            if(cotizacion.getOpcion() == 2) {
                jsonObject.put("Procesadora", agregarTarjeta.getIdMarcaTarjeta());
                jsonObject.put("Tarjeta", agregarTarjeta.getNumero());
                jsonObject.put("Vencimiento", agregarTarjeta.getFechaCaducidad());
                jsonObject.put("Titular", ((EditText)getRootView().findViewById(R.id.cliente_primer_nombre)).getText().toString() + " " + ((EditText)getRootView().findViewById(R.id.cliente_primer_apellido)).getText().toString());
            }
            jsonObject.put("TipoIdentificacion", "0" + ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition()));
            jsonObject.put("Identificacion", ((EditText) getRootView().findViewById(R.id.cliente_identificacion)).getText().toString());
            jsonObject.put("Apellido", ((EditText)getRootView().findViewById(R.id.cliente_primer_apellido)).getText().toString());
            jsonObject.put("Nombre", ((EditText)getRootView().findViewById(R.id.cliente_primer_nombre)).getText().toString());
            jsonObject.put("FechaNacimiento", format1.format(cliente.getFechaNacimiento()));
            jsonObject.put("Sexo", ((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
            jsonObject.put("Correo", ((EditText) getRootView().findViewById(R.id.cliente_correo)).getText().toString());
        }
        catch (Exception ex)
        {}

        return jsonObject.toString();
    }
    //endregion

    //region PAY ME

    private Commerce commerce = new Commerce();
    private TransactionInformation transactionInformation = new TransactionInformation();
    private ArrayList<Person> personsBilling = new  ArrayList<Person>();
    private ArrayList<Person> personsShipping = new  ArrayList<Person>();
    private ArrayList<Tax> taxes = new  ArrayList<Tax>();
    private ArrayList<Product> products = new  ArrayList<Product>();
    private ArrayList<PurchaseInformation> purchasesInformation = new  ArrayList<PurchaseInformation>();

    private void listener() {
        SugarContext.init(this.getActivity());
        setValuesToSend();

        Intent intent = new Intent(this.getActivity(), LoadingAct.class);
        intent.putExtra("commerce", commerce);
        intent.putExtra("transactionInformation", transactionInformation);
        intent.putExtra("personsBilling", personsBilling);
        intent.putExtra("personsShipping", personsShipping);
        intent.putExtra("taxes", taxes);
        intent.putExtra("products", products);
        intent.putExtra("purchasesInformation", purchasesInformation);

        startActivityForResult(intent, Constants.RESULT_API);
    }

    private void setValuesToSend() {

        commerce.setCommerceName("Auna");
        commerce.setCommerceLogo("logo_commerce");
        commerce.setCommerceColor("1B83B7");

        transactionInformation.setIdAcquirer("4");
        transactionInformation.setIdEntCommerce("606");
        //transactionInformation.setCodAsoCardHolderWallet("36--220--2909");
        transactionInformation.setCodAsoCardHolderWallet("");
        transactionInformation.setCodCardHolderCommerce("ABC120");
        transactionInformation.setMail(((EditText) getRootView().findViewById(R.id.cliente_correo)).getText().toString());
        transactionInformation.setNameCardholder(((EditText)getRootView().findViewById(R.id.cliente_primer_nombre)).getText().toString());
        transactionInformation.setLastNameCardholder(((EditText)getRootView().findViewById(R.id.cliente_primer_apellido)).getText().toString());

        //
        personsBilling.clear();

        Person personBilling = new Person();
        personBilling.setFirstName(((EditText)getRootView().findViewById(R.id.cliente_primer_nombre)).getText().toString());
        personBilling.setLastName(((EditText)getRootView().findViewById(R.id.cliente_primer_apellido)).getText().toString());

        Address addressBilling = new Address();
        addressBilling.setAddress("las flores");
        addressBilling.setCity("Lima");
        addressBilling.setState("Lima");
        addressBilling.setCountryCode("PE");
        addressBilling.setZipCode("Lima 18");
        addressBilling.setPhoneNumber("999111999");
        addressBilling.setEmail(((EditText) getRootView().findViewById(R.id.cliente_correo)).getText().toString());

        ArrayList<Address> addressesBilling = new ArrayList<Address>();
        addressesBilling.add(addressBilling);
        personBilling.setAddresses(addressesBilling);

        personsBilling.add(personBilling);
        //

        //
        personsShipping.clear();

        Person personShipping = new Person();
        personShipping.setFirstName(((EditText)getRootView().findViewById(R.id.cliente_primer_nombre)).getText().toString());
        personShipping.setLastName(((EditText)getRootView().findViewById(R.id.cliente_primer_apellido)).getText().toString());

        Address addressShipping = new Address();
        addressShipping.setAddress("las flores");
        addressShipping.setCity("Lima");
        addressShipping.setState("Lima");
        addressShipping.setCountryCode("PE");
        addressShipping.setZipCode("Lima 18");
        addressShipping.setPhoneNumber("999111999");
        addressShipping.setEmail(((EditText) getRootView().findViewById(R.id.cliente_correo)).getText().toString());

        ArrayList<Address> addressesShipping = new ArrayList<Address>();
        addressesShipping.add(addressShipping);
        personShipping.setAddresses(addressesShipping);

        personsShipping.add(personShipping);
        //

        //
        taxes.clear();

        /*Tax tax1 = new Tax();
        tax1.setIdTax("100");
        tax1.setNameTax("Tax1");
        tax1.setAmountTax("10");

        Tax tax2 = new Tax();
        tax2.setIdTax("200");
        tax2.setNameTax("Tax2");
        tax2.setAmountTax("20");

        taxes.add(tax1);
        taxes.add(tax2);*/

        //
        products.clear();

        Product product1 = new Product();
        product1.setItem("001");
        product1.setCode(((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_programa)).getSelectedItem()).getCode());
        product1.setName(((GeneralValue)((Spinner)getRootView().findViewById(R.id.cliente_programa)).getSelectedItem()).getValue());
        product1.setUnitPrice(numberFormat.format(cotizacion.getValor()));
        product1.setQuantity("1");

        products.add(product1);
        //

        //
        purchasesInformation.clear();

        PurchaseInformation purchaseInformation = new PurchaseInformation();
        purchaseInformation.setCurrencyCode("604");
        purchaseInformation.setPurchaseAmount(numberFormat.format(cotizacion.getValor())+"00");
        purchaseInformation.setOperationNumber(idOperacion);
        purchaseInformation.setCallerPhoneNumber("999111999");
        purchaseInformation.setTerminalCode("414");
        purchaseInformation.setIpAddress("255.255.255.255");

        purchasesInformation.add(purchaseInformation);
    }

    public boolean validacionesAlignet()
    {
        if(((Spinner) getRootView().findViewById(R.id.cliente_programa)).getSelectedItemPosition() == 0)
        {
            Toast.makeText(this.getActivity(), "Debe escoger un programa.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    //endregion

}

