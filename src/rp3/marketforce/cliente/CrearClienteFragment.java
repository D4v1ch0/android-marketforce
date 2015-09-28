package rp3.marketforce.cliente;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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

import com.google.android.gms.maps.model.LatLng;

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
import rp3.maps.utils.SphericalUtil;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Campo;
import rp3.marketforce.models.Canal;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.ruta.CrearVisitaActivity;
import rp3.marketforce.ruta.CrearVisitaFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DetailsPageAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.marketforce.utils.NothingSelectedSpinnerAdapter;
import rp3.marketforce.utils.Utils;
import rp3.util.ConnectionUtils;
import rp3.util.GooglePlayServicesUtils;
import rp3.util.IdentificationValidator;
import rp3.util.LocationUtils;
import rp3.widget.ViewPager;

public class CrearClienteFragment extends BaseFragment {

    boolean rotated = false;

	public static CrearClienteFragment newInstance(long id_cliente, int tipo)
	{
		CrearClienteFragment fragment = new CrearClienteFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_CLIENTE, id_cliente);
        args.putInt(ARG_TIPO, tipo);
		fragment.setArguments(args);
		return fragment;
	}

	public static String ARG_CLIENTE = "cliente";
    public static String ARG_TIPO = "tipo";

    public final static int DIALOG_VISITA = 1;
    public final static int DIALOG_GPS = 2;

	private LayoutInflater inflater;
	private LinearLayout ContactosContainer, DireccionContainer;
	private List<LinearLayout> listViewDirecciones, listViewContactos;
    private List<GeopoliticalStructure> listCiudades;
	private int curentPage, posDir;
    private Location currentLoc;
	private long idCliente = 0;
    private int tipo;
	Uri photo = Utils.getOutputMediaFileUri(Utils.MEDIA_TYPE_IMAGE);
	boolean isClient;
	int posContact = -1;
	public Cliente cliente;
	public List<String> contactPhotos;
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
			if(Validaciones() && CamposObligatorios())
			{
				Grabar();
                if(tipo == Contants.IS_MODIFICACION)
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

    private boolean CamposObligatorios() {
        List<Campo> campos = Campo.getCamposObligatorios(getDataBase(), tipo);
        for(Campo campo : campos) {
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_IDENTIFICACION) && ((TextView)getRootView().findViewById(R.id.cliente_identificacion)).length() <= 0) {
                Toast.makeText(this.getContext(), "Falta Identificación.", Toast.LENGTH_LONG).show();
                return false;
            }
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FOTO) && cliente.getURLFoto().length() <= 0){
                Toast.makeText(this.getContext(), "Falta Foto.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItemPosition() == 1) {
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FECHA_NACIMIENTO) && ((TextView) getRootView().findViewById(R.id.cliente_fecha_nacimiento)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Fecha de Nacimiento.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_NOMBRE_NATURAL) && ((TextView) getRootView().findViewById(R.id.cliente_primer_nombre)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Primer Nombre.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_NOMBRE_NATURAL) && ((TextView) getRootView().findViewById(R.id.cliente_segundo_nombre)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Segundo Nombre.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_APELLIDO_NATURAL) && ((TextView) getRootView().findViewById(R.id.cliente_primer_apellido)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Primer Apellido.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_APELLIDO_NATURAL) && ((TextView) getRootView().findViewById(R.id.cliente_segundo_apellido)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Segundo Apellido.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO) && ((TextView) getRootView().findViewById(R.id.cliente_correo)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Correo.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            else {

                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_NOMBRE_JURIDICO) && ((TextView) getRootView().findViewById(R.id.cliente_nombre)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Nombre.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_RAZON_SOCIAL) && ((TextView) getRootView().findViewById(R.id.cliente_razon_social)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Razón Social.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PAGINA_WEB) && ((TextView) getRootView().findViewById(R.id.cliente_pagina_web)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Página Web.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ACTIVIDAD_ECONOMICA) && ((TextView) getRootView().findViewById(R.id.cliente_actividad_economica)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Actividad Económica.", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO) && ((TextView) getRootView().findViewById(R.id.cliente_correo_juridico)).length() <= 0) {
                    Toast.makeText(this.getContext(), "Falta Correo.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }

    private void SetCamposDireccion(){
        List<Campo> campos = Campo.getCampos(getDataBase(), tipo);
        for(Campo campo : campos) {
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_DIRECCION_DESCRIPCION))
                listViewDirecciones.get(listViewDirecciones.size() -1).findViewById(R.id.cliente_direccion).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_REFERENCIA))
                listViewDirecciones.get(listViewDirecciones.size() -1).findViewById(R.id.cliente_referencia).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CIUDAD))
                listViewDirecciones.get(listViewDirecciones.size() -1).findViewById(R.id.cliente_ciudad).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_DIRECCION_MOVIL))
                listViewDirecciones.get(listViewDirecciones.size() -1).findViewById(R.id.cliente_telefono1).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_DIRECCION_FIJO))
                listViewDirecciones.get(listViewDirecciones.size() -1).findViewById(R.id.cliente_telefono2).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_POSICION_GEO))
                listViewDirecciones.get(listViewDirecciones.size() -1).findViewById(R.id.cliente_ubicacion).setEnabled(false);
        }
    }
    private void SetCamposContactos() {
        List<Campo> campos = Campo.getCampos(getDataBase(), tipo);
        for(Campo campo : campos) {
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_NOMBRE_CONTACTO))
                listViewContactos.get(listViewContactos.size()-1).findViewById(R.id.cliente_nombres).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_APELLIDO_CONTACTO))
                listViewContactos.get(listViewContactos.size()-1).findViewById(R.id.cliente_apellidos).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_DIRECCION_CONTACTO))
                listViewContactos.get(listViewContactos.size()-1).findViewById(R.id.cliente_direccion_contacto).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_MOVIL_CONTACTO))
                listViewContactos.get(listViewContactos.size()-1).findViewById(R.id.cliente_telefono1_contacto).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FIJO_CONTACTO))
                listViewContactos.get(listViewContactos.size()-1).findViewById(R.id.cliente_telefono2_contacto).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CARGO_CONTACTO))
                listViewContactos.get(listViewContactos.size()-1).findViewById(R.id.cliente_cargo).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO_CONTACTO))
                listViewContactos.get(listViewContactos.size()-1).findViewById(R.id.cliente_correo_contacto).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FOTO_CONTACTO))
                listViewContactos.get(listViewContactos.size()-1).findViewById(R.id.cliente_contacto_foto).setEnabled(false);
        }
    }

    private void SetCampos() {
        List<Campo> campos = Campo.getCampos(getDataBase(), tipo);
        //contacto.findViewById(R.id.agregar_contacto).setVisibility(View.GONE);
        Spannable wordtoSpan = new SpannableString(" *");
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_unvisited)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if(tipo == Contants.IS_MODIFICACION)
            getRootView().findViewById(R.id.crear_cliente_tipo_persona).setEnabled(false);
        else {
            getRootView().findViewById(R.id.agregar_direccion).setVisibility(View.GONE);
            addDireccion();
        }
        for(Campo campo : campos) {
            /*if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_TIPO_IDENTIFICACION)) {
                getRootView().findViewById(R.id.cliente_tipo_identificacion).setEnabled(false);
                if(campo.isObligatorio()) ((TextView)getRootView().findViewById(R.id.tipo_identificacion_label)).append(wordtoSpan);
            }*/
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_IDENTIFICACION)) {
                getRootView().findViewById(R.id.cliente_identificacion).setEnabled(false);
                if(campo.isObligatorio()) setSpannable((EditText)getRootView().findViewById(R.id.cliente_identificacion));
            }
            /*if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_TIPO_CLI)) {
                getRootView().findViewById(R.id.cliente_tipo_cliente).setEnabled(false);
                if(campo.isObligatorio()) ((TextView)getRootView().findViewById(R.id.tipo_cliente_label)).append(wordtoSpan);
            }*/
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CANAL))
                getRootView().findViewById(R.id.cliente_canal).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FOTO))
                getRootView().findViewById(R.id.cliente_foto).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FECHA_NACIMIENTO))
                getRootView().findViewById(R.id.cliente_fecha_nacimiento).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_GENERO))
                getRootView().findViewById(R.id.cliente_genero).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ESTADO_CIVIL))
                getRootView().findViewById(R.id.cliente_estado_civil).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_NOMBRE_NATURAL))
                getRootView().findViewById(R.id.cliente_primer_nombre).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_NOMBRE_NATURAL))
                getRootView().findViewById(R.id.cliente_segundo_nombre).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_APELLIDO_NATURAL))
                getRootView().findViewById(R.id.cliente_primer_apellido).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_APELLIDO_NATURAL))
                getRootView().findViewById(R.id.cliente_segundo_apellido).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO))
                getRootView().findViewById(R.id.cliente_correo).setEnabled(false);

            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_NOMBRE_JURIDICO))
                getRootView().findViewById(R.id.cliente_nombre).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_RAZON_SOCIAL))
                getRootView().findViewById(R.id.cliente_razon_social).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PAGINA_WEB))
                getRootView().findViewById(R.id.cliente_pagina_web).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ACTIVIDAD_ECONOMICA))
                getRootView().findViewById(R.id.cliente_actividad_economica).setEnabled(false);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO))
                getRootView().findViewById(R.id.cliente_correo_juridico).setEnabled(false);
        }
        campos = Campo.getCamposObligatorios(getDataBase(), tipo);
        for(Campo campo : campos) {
            //if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_TIPO_IDENTIFICACION))
            //    ((TextView)getRootView().findViewById(R.id.tipo_identificacion_label)).append(wordtoSpan);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_IDENTIFICACION))
                setSpannable(((TextView) getRootView().findViewById(R.id.cliente_identificacion)));
            //if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_TIPO_CLI))
            //    ((TextView)getRootView().findViewById(R.id.tipo_cliente_label)).append(wordtoSpan);
            //if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CANAL))
            //    ((TextView)getRootView().findViewById(R.id.canal_label)).append(wordtoSpan);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FOTO))
                ((TextView)getRootView().findViewById(R.id.foto_label)).append(wordtoSpan);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_FECHA_NACIMIENTO))
                setSpannable((EditText)getRootView().findViewById(R.id.cliente_fecha_nacimiento));
            //if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_GENERO))
            //    ((TextView)getRootView().findViewById(R.id.genero_label)).append(wordtoSpan);
            //if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ESTADO_CIVIL))
            //    ((TextView)getRootView().findViewById(R.id.estado_civil_label)).append(wordtoSpan);
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_NOMBRE_NATURAL))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_primer_nombre));
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_NOMBRE_NATURAL))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_segundo_nombre));
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PRIMER_APELLIDO_NATURAL))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_primer_apellido));
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_SEGUNDO_APELLIDO_NATURAL))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_segundo_nombre));
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_correo));

            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_NOMBRE_JURIDICO))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_nombre));
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_RAZON_SOCIAL))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_razon_social));
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_PAGINA_WEB))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_pagina_web));
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_ACTIVIDAD_ECONOMICA))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_actividad_economica));
            if (campo.getIdCampo().equalsIgnoreCase(Contants.CAMPO_CORREO))
                setSpannable((EditText) getRootView().findViewById(R.id.cliente_correo_juridico));
        }

    }
	
	private void Grabar() {
		Cliente cli = new Cliente();
			if(idCliente != 0)
				cli = Cliente.getClienteID(getDataBase(), idCliente, true);
			cli.setIdCanal((int) ((Spinner)getRootView().findViewById(R.id.cliente_canal)).getAdapter().getItemId(((Spinner)getRootView().findViewById(R.id.cliente_canal)).getSelectedItemPosition()));
			cli.setIdTipoIdentificacion((int) ((Spinner)getRootView().findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner)getRootView().findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition()));
			cli.setIdentificacion(((EditText)getRootView().findViewById(R.id.cliente_identificacion)).getText().toString());
			cli.setTipoPersona(((GeneralValue)((Spinner)getRootView().findViewById(R.id.crear_cliente_tipo_persona)).getSelectedItem()).getCode());
			cli.setIdTipoCliente((int) ((Spinner)getRootView().findViewById(R.id.cliente_tipo_cliente)).getAdapter().getItemId(((Spinner)getRootView().findViewById(R.id.cliente_tipo_cliente)).getSelectedItemPosition()));
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
			if(cli.getID() == 0 )
				Cliente.insert(getDataBase(), cli);
			else
				Cliente.update(getDataBase(), cli);

			for(int i = 0; i < listViewDirecciones.size(); i ++)
			{
				ClienteDireccion cliDir = new ClienteDireccion();
				if(cliDirecciones != null && cliDirecciones.size() > i)
				{
					cliDir = cliDirecciones.get(i);
				}
				else
				{
					//cliDir.setIdClienteDireccion(i+1);
					if(idCliente != 0)
						cliDir.setIdCliente(cli.getIdCliente());
				}
				cliDir.set_idCliente(cli.getID());
				cliDir.setDireccion(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).getText().toString());
				cliDir.setTipoDireccion(((GeneralValue)((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).getSelectedItem()).getCode());
				cliDir.setEsPrincipal(((CheckBox)listViewDirecciones.get(i).findViewById(R.id.cliente_es_principal)).isChecked());
				cliDir.setTelefono1(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono1)).getText().toString());
				cliDir.setTelefono2(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono2)).getText().toString());
				cliDir.setReferencia(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_referencia)).getText().toString());
				cliDir.setCiudadDescripcion(((AutoCompleteTextView)listViewDirecciones.get(i).findViewById(R.id.cliente_ciudad)).getText().toString());
                if(listCiudades.size() > i && listCiudades.get(i) != null)
				    cliDir.setIdCiudad((int) listCiudades.get(i).getID());

				if(!((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).getText().toString().equals(""))
				{
					cliDir.setLongitud(Double.parseDouble(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).getText().toString()));
					cliDir.setLatitud(Double.parseDouble(((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_latitud)).getText().toString()));
				}
				if(cliDir.getID() == 0)
					ClienteDireccion.insert(getDataBase(), cliDir);
				else
					ClienteDireccion.update(getDataBase(), cliDir);
				
				if(i == 0)
				{
					cli.setDireccion(cliDir.getDireccion());
					cli.setTelefono(cliDir.getTelefono1());
					Cliente.update(getDataBase(), cli);
				}
			}

			for(int i = 0; i < listViewContactos.size(); i ++)
			{
				Contacto cliCont = new Contacto();
				if(cliContactos != null && cliContactos.size() > i)
				{
					cliCont = cliContactos.get(i);
				}
				else
				{
					//cliCont.setIdContacto(i+1);
					if(idCliente != 0)
						cliCont.setIdCliente(cli.getIdCliente());
				}
				cliCont.set_idCliente(cli.getID());
				cliCont.setNombre(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_nombres)).getText().toString());
				cliCont.setApellido(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_apellidos)).getText().toString());
				cliCont.setCargo(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_cargo)).getText().toString());
				cliCont.setTelefono1(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono1_contacto)).getText().toString());
				cliCont.setTelefono2(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono2_contacto)).getText().toString());
				cliCont.setCorreo(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).getText().toString());
				cliCont.setIdClienteDireccion(((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).getSelectedItemPosition()+1);
				cliCont.setURLFoto(contactPhotos.get(i));
				
				if(cliCont.getID() == 0)
					Contacto.insert(getDataBase(), cliCont);
				else
					Contacto.update(getDataBase(), cliCont);
			}
			
			if(ConnectionUtils.isNetAvailable(getActivity()))
			{
				Bundle bundle = new Bundle();
				if(idCliente != 0)
					bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE_FULL);
				else {
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_CREATE);
                    showDialogConfirmation(DIALOG_VISITA, R.string.message_crear_visita, R.string.label_crear_visita);
                    cliente = cli;
                }
				bundle.putLong(ARG_CLIENTE, cli.getID());
				requestSync(bundle);
			}
	}

    @Override
    public void onPositiveConfirmation(int id){
        super.onPositiveConfirmation(id);
        switch (id)
        {
            case DIALOG_VISITA:
                Intent intent2 = new Intent(getActivity(), CrearVisitaActivity.class);
                intent2.putExtra(CrearVisitaFragment.ARG_IDAGENDA,(int) cliente.getID());
                intent2.putExtra(CrearVisitaFragment.ARG_FROM, "Cliente");
                startActivity(intent2);
                finish();
                break;
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
            ((EditText)listViewDirecciones.get(posDir).findViewById(R.id.cliente_direccion)).setText(addr.get(0).getFeatureName());
            ((EditText)listViewDirecciones.get(posDir).findViewById(R.id.cliente_referencia)).setText(addr.get(1).getFeatureName());
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
		
		if(listViewDirecciones == null) {
            listViewDirecciones = new ArrayList<LinearLayout>();
            listViewContactos = new ArrayList<LinearLayout>();
            listCiudades = new ArrayList<GeopoliticalStructure>();
            //ciudades = GeopoliticalStructure.getGeopoliticalStructureCities(getDataBase());

            adapter = new GeopoliticalStructureAdapter(getContext(), getDataBase());
            inflater = (LayoutInflater) this.getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            ((TextView) getRootView().findViewById(R.id.agregar_direccion)).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    addDireccion();

                }
            });
            DireccionContainer = (LinearLayout) getRootView().findViewById(R.id.crear_cliente_container_direccion);

            ((TextView) getRootView().findViewById(R.id.agregar_contacto)).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    addContacto();

                }
            });
            ContactosContainer = (LinearLayout) getRootView().findViewById(R.id.crear_cliente_container_contacto);

            SimpleGeneralValueAdapter tipoPersonaAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_TIPO_PERSONA);
            SimpleGeneralValueAdapter tipoEstadoCivilAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_ESTADO_CIVIL);
            SimpleGeneralValueAdapter tipoGeneroAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), rp3.marketforce.Contants.GENERAL_TABLE_GENERO);
            SimpleIdentifiableAdapter tipoCliente = new SimpleIdentifiableAdapter(getContext(), TipoCliente.getTipoCliente(getDataBase(), ""));
            SimpleIdentifiableAdapter tipoCanal = new SimpleIdentifiableAdapter(getContext(), Canal.getCanal(getDataBase(), ""));
            SimpleDictionaryAdapter tipoIdentificacion = new SimpleDictionaryAdapter(getContext(), IdentificationType.getAll(getDataBase()));

            ((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).setAdapter(tipoPersonaAdapter);
            ((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).setPrompt("Seleccione un tipo de persona");
            ((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).setOnItemSelectedListener(new OnItemSelectedListener() {

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
            ((ImageButton) getRootView().findViewById(R.id.cliente_foto)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClient = true;
                    takePicture(1);
                }
            });
            ((EditText) getRootView().findViewById(R.id.cliente_fecha_nacimiento)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogDatePicker(0);
                }
            });
        }
		else
		{
            rotated = true;
		}

        if(getArguments().containsKey(ARG_TIPO) && getArguments().getInt(ARG_TIPO) != 0 && !rotated)
        {
            tipo = getArguments().getInt(ARG_TIPO);
            SetCampos();
        }
		if(getArguments().containsKey(ARG_CLIENTE) && getArguments().getLong(ARG_CLIENTE) != 0 && !rotated)
		{
			idCliente = getArguments().getLong(ARG_CLIENTE);
			setDatosClientes();
		}
	}


    private void setDatosClientes() {
		DrawableManager DManager = new DrawableManager();
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM yyyy");
		Cliente cli = Cliente.getClienteID(getDataBase(), idCliente, true);
		((Spinner)getRootView().findViewById(R.id.cliente_canal)).setSelection(getPosition(((Spinner)getRootView().findViewById(R.id.cliente_canal)).getAdapter(), cli.getIdCanal()));
		((Spinner)getRootView().findViewById(R.id.cliente_tipo_identificacion)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getAdapter(), cli.getTipoIdentificacionId()));
		((EditText)getRootView().findViewById(R.id.cliente_identificacion)).setText(cli.getIdentificacion());
		((Spinner)getRootView().findViewById(R.id.crear_cliente_tipo_persona)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.crear_cliente_tipo_persona)).getAdapter(), cli.getTipoPersona()));
		((Spinner)getRootView().findViewById(R.id.cliente_tipo_cliente)).setSelection(getPosition(((Spinner) getRootView().findViewById(R.id.cliente_tipo_cliente)).getAdapter(), cli.getIdTipoCliente()));
        if(cli.getFechaNacimiento() != null && cli.getFechaNacimiento().getTime() != 0) {
            ((EditText) getRootView().findViewById(R.id.cliente_fecha_nacimiento)).setText(format1.format(cli.getFechaNacimiento()));
            cliente.setFechaNacimiento(cli.getFechaNacimiento());
        }
		DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getActivity(), cli.getURLFoto()),
                (ImageButton) getRootView().findViewById(R.id.cliente_foto));
		if(cli.getTipoPersona().equalsIgnoreCase("N"))
		{
			((EditText)getRootView().findViewById(R.id.cliente_primer_nombre)).setText(cli.getNombre1());
			((EditText)getRootView().findViewById(R.id.cliente_segundo_nombre)).setText(cli.getNombre2());
			((EditText)getRootView().findViewById(R.id.cliente_primer_apellido)).setText(cli.getApellido1());
			((EditText)getRootView().findViewById(R.id.cliente_segundo_apellido)).setText(cli.getApellido2());
			((EditText)getRootView().findViewById(R.id.cliente_correo)).setText(cli.getCorreoElectronico());
			((Spinner)getRootView().findViewById(R.id.cliente_genero)).setSelection(getPosition(((Spinner)getRootView().findViewById(R.id.cliente_genero)).getAdapter(), cli.getGenero()));
			((Spinner)getRootView().findViewById(R.id.cliente_estado_civil)).setSelection(getPosition(((Spinner)getRootView().findViewById(R.id.cliente_estado_civil)).getAdapter(), cli.getEstadoCivil()));
		}
		else
		{
			((EditText)getRootView().findViewById(R.id.cliente_nombre)).setText(cli.getNombre1());
			((EditText)getRootView().findViewById(R.id.cliente_actividad_economica)).setText(cli.getActividadEconomica());
			((EditText)getRootView().findViewById(R.id.cliente_correo_juridico)).setText(cli.getCorreoElectronico());
			((EditText)getRootView().findViewById(R.id.cliente_razon_social)).setText(cli.getRazonSocial());
			((EditText)getRootView().findViewById(R.id.cliente_pagina_web)).setText(cli.getPaginaWeb());
		}
		
		for(int i = 0; i < cli.getClienteDirecciones().size(); i ++)
		{
			addDireccion();
			((Button)listViewDirecciones.get(i).findViewById(R.id.eliminar_direccion)).setVisibility(View.GONE);
			((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).setText(cli.getClienteDirecciones().get(i).getDireccion());
			((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).setSelection(
					getPosition(((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).getAdapter(), cli.getClienteDirecciones().get(i).getTipoDireccion()));
			((CheckBox)listViewDirecciones.get(i).findViewById(R.id.cliente_es_principal)).setChecked(cli.getClienteDirecciones().get(i).getEsPrincipal());
			((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono1)).setText(cli.getClienteDirecciones().get(i).getTelefono1());
			((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_telefono2)).setText(cli.getClienteDirecciones().get(i).getTelefono2());
			((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_referencia)).setText(cli.getClienteDirecciones().get(i).getReferencia());
			((AutoCompleteTextView)listViewDirecciones.get(i).findViewById(R.id.cliente_ciudad)).setText(cli.getClienteDirecciones().get(i).getCiudadDescripcion());
            listCiudades.add(null);
			if(cli.getClienteDirecciones().get(i).getLongitud() != 0)
			{
				((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).setText("" + cli.getClienteDirecciones().get(i).getLongitud());
				((EditText)listViewDirecciones.get(i).findViewById(R.id.cliente_latitud)).setText("" + cli.getClienteDirecciones().get(i).getLatitud());
                ((ImageView)listViewDirecciones.get(i).findViewById(R.id.cliente_ubicacion_check)).setImageResource(R.drawable.checkbox_on);
			}
			if(!cli.isNuevo())
				((Spinner)listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).setEnabled(false);
		}
		
		for(int i = 0; i < cli.getContactos().size(); i ++)
		{
			addContacto();
			((Button)listViewContactos.get(i).findViewById(R.id.eliminar_contacto)).setVisibility(View.GONE);
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_nombres)).setText(cli.getContactos().get(i).getNombre());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_apellidos)).setText(cli.getContactos().get(i).getApellido());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_cargo)).setText(cli.getContactos().get(i).getCargo());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono1_contacto)).setText(cli.getContactos().get(i).getTelefono1());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_telefono2_contacto)).setText(cli.getContactos().get(i).getTelefono2());
			((EditText)listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).setText(cli.getContactos().get(i).getCorreo());
			((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).setSelection(
					getPosition(((Spinner)listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).getAdapter(), (int) cli.getContactos().get(i).getIdClienteDireccion()));
			DManager.fetchDrawableOnThread(PreferenceManager.getString("server") + 
					rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER) + Utils.getImageDPISufix(getActivity(), cli.getContactos().get(i).getURLFoto()), 
					(ImageButton)listViewContactos.get(i).findViewById(R.id.cliente_contacto_foto));
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
        //direccion.findViewById(R.id.cliente_direccion).requestFocus();
        direccion.findViewById(R.id.cliente_direccion).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    for(LinearLayout contacto : listViewContactos)
                    {
                        ((Spinner)contacto.findViewById(R.id.cliente_direccion_contacto)).setAdapter(getDirecciones());
                    }
                }
            }
        });
        if(tipo == Contants.IS_CREACION)
            direccion.findViewById(R.id.eliminar_direccion).setVisibility(View.GONE);
		((Button) direccion.findViewById(R.id.eliminar_direccion)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				listViewDirecciones.remove(direccion);
				DireccionContainer.removeView(direccion);
                listCiudades.remove(pos);
			}});

        if(pos == 0)
            ((CheckBox) direccion.findViewById(R.id.cliente_es_principal)).setChecked(true);
		((ImageButton) direccion.findViewById(R.id.cliente_ubicacion)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (GooglePlayServicesUtils.servicesConnected((BaseActivity)getActivity())) {

                    try
                    {
                        ((BaseActivity)getActivity()).showDialogProgress("GPS","Obteniendo Posición");
                        LocationUtils.getLocation(getContext(), new LocationUtils.OnLocationResultListener() {

                            @Override
                            public void getLocationResult(Location location) {
                                if (location != null) {
                                    ((EditText) listViewDirecciones.get(pos).findViewById(R.id.cliente_longitud)).setText("" + location.getLongitude());
                                    ((EditText) listViewDirecciones.get(pos).findViewById(R.id.cliente_latitud)).setText("" + location.getLatitude());
                                    ((ImageView) listViewDirecciones.get(pos).findViewById(R.id.cliente_ubicacion_check)).setImageResource(R.drawable.checkbox_on);
                                    currentLoc = location;
                                    posDir = pos;
                                    if(((EditText) listViewDirecciones.get(pos).findViewById(R.id.cliente_longitud)).getText().length() > 0)
                                    {
                                        showDialogConfirmation(DIALOG_GPS, R.string.message_direccion_google, R.string.title_direccion);
                                    }
                                    else
                                    {
                                        SaveAddress();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Debe de activar su GPS.", Toast.LENGTH_SHORT).show();
                                }
                                ((BaseActivity)getActivity()).closeDialogProgress();
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

        SetCamposDireccion();
	}
	
	private void addContacto()
	{
		final LinearLayout contacto = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_cliente_contacto_detail, null);
		final int pos = listViewContactos.size();
        contacto.findViewById(R.id.cliente_nombres).requestFocus();
		((Button) contacto.findViewById(R.id.eliminar_contacto)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                listViewContactos.remove(contacto);
                ContactosContainer.removeView(contacto);
                contactPhotos.remove(pos);
            }
        });
		contactPhotos.add("");
		((Spinner) contacto.findViewById(R.id.cliente_direccion_contacto)).setAdapter(getDirecciones());
		((ImageButton) contacto.findViewById(R.id.cliente_contacto_foto)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                posContact = pos;
                isClient = false;
                takePicture(1);
            }
        });
		ContactosContainer.addView(contacto);
		listViewContactos.add(contacto);
        SetCamposContactos();
	}
	


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
            Bitmap pree = null;
            if(data != null) {
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
            }
            else {
                try {
                    pree = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
			if(isClient)
			{
                ((ImageButton) getRootView().findViewById(R.id.cliente_foto)).setImageBitmap(pree);
                cliente.setURLFoto(Utils.SaveBitmap(pree, String.format("%s.%s", java.util.UUID.randomUUID(), ".jpg")));
			}
			else
			{
                ((ImageButton) listViewContactos.get(posContact).findViewById(R.id.cliente_contacto_foto)).setImageBitmap(pree);
                contactPhotos.set(posContact, Utils.SaveBitmap(pree, String.format("%s.%s", java.util.UUID.randomUUID(), ".jpg")));
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
            if((cliente.getFechaNacimiento() == null || cliente.getFechaNacimiento().getTime() >= Calendar.getInstance().getTime().getTime()) && getRootView().findViewById(R.id.cliente_fecha_nacimiento).isEnabled() && ((TextView)getRootView().findViewById(R.id.fecha_nacimiento_label)).getText().toString().contains("*"))
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
			if(((EditText)getRootView().findViewById(R.id.cliente_nombre)).getText().toString().trim().length() <= 0)
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
            if(!IdentificationValidator.ValidateIdentification(((EditText)getRootView().findViewById(R.id.cliente_identificacion)).getText().toString(),
                    (int) ((Spinner)getRootView().findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner)getRootView().findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition())))
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

            if(((CheckBox)listViewDirecciones.get(i).findViewById(R.id.cliente_es_principal)).isChecked())
                existsPrincipal = true;
		}

        for(int i = 0; i < listViewContactos.size(); i++)
        {
            if(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_nombres)).getText().length() <= 0 && listViewContactos.get(i).findViewById(R.id.cliente_nombres).isEnabled())
            {
                Toast.makeText(getContext(), "Debe de ingresar los nombres del contacto.", Toast.LENGTH_LONG).show();
                return false;
            }
            if(((EditText)listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).getText().length() > 0 && listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto).isEnabled())
            {
                if(!((EditText)listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).getText().toString().trim().contains("@") ||
                        !((EditText)listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).getText().toString().trim().contains(".")) {
                    Toast.makeText(getContext(), "Mail incorrecto.", Toast.LENGTH_LONG).show();
                    return false;
                }
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

    private void setSpannable(TextView view)
    {
        Spannable wordtoSpan = new SpannableString(view.getHint() + " *");
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_unvisited)), view.getHint().length(), wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setHint(wordtoSpan);
    }

}
