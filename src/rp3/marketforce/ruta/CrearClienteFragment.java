package rp3.marketforce.ruta;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleDictionaryAdapter;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.data.models.IdentificationType;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.Canal;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.Tarea;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 11/01/2017.
 */
public class CrearClienteFragment extends BaseFragment {

    public static String ARG_NOMBRES = "nombres";

    private String nombres;

    public interface CrearClienteDialogListener {
        int onFinishCrearClienteDialog(long idCliente);
    }

    public static CrearClienteFragment newInstance(String nombre) {
        CrearClienteFragment fragment = new CrearClienteFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_NOMBRES, nombre);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        nombres = getArguments().getString(ARG_NOMBRES);

        View view = inflater.inflate(R.layout.fragment_crear_cliente_visita, container);

        ((EditText) view.findViewById(R.id.cliente_primer_nombre)).setText(nombres);

        getDialog().setTitle("Crear Cliente");
        SimpleDictionaryAdapter tipoIdentificacion = new SimpleDictionaryAdapter(getContext(), IdentificationType.getAll(getDataBase()));
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).setAdapter(tipoIdentificacion);
        ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).setPrompt("Seleccione un tipo de identificación");

        ((Button) view.findViewById(R.id.actividad_aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Cliente cli = new Cliente();
                cli.setIdCanal((int) Canal.getCanal(getDataBase(), "").get(0).getID());
                cli.setIdTipoIdentificacion((int) ((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition()));
                cli.setIdentificacion(((EditText) getRootView().findViewById(R.id.cliente_identificacion)).getText().toString());
                cli.setTipoPersona("N");
                cli.setIdTipoCliente((int) TipoCliente.getTipoCliente(getDataBase(), "").get(0).getID());
                cli.setExentoImpuesto(false);
                cli.setCiudadanoOro(false);
                    cli.setNombre1(((EditText) getRootView().findViewById(R.id.cliente_primer_nombre)).getText().toString());
                    cli.setNombre2("");
                    cli.setApellido1(((EditText) getRootView().findViewById(R.id.cliente_primer_apellido)).getText().toString());
                    cli.setApellido2("");
                    cli.setCorreoElectronico(((EditText) getRootView().findViewById(R.id.cliente_correo)).getText().toString());
                    cli.setGenero(((GeneralValue) ((Spinner) getRootView().findViewById(R.id.cliente_genero)).getSelectedItem()).getCode());
                    cli.setEstadoCivil(((GeneralValue) ((Spinner) getRootView().findViewById(R.id.cliente_estado_civil)).getSelectedItem()).getCode());
                    cli.setNombreCompleto(cli.getNombre1() + " " + cli.getNombre2() + " " + cli.getApellido1() + " " + cli.getApellido2());
                    cli.setFechaNacimiento(cliente.getFechaNacimiento());
                cli.setPendiente(true);
                List<Contacto> cliContactos = cli.getContactos();
                cli.setContactos(null);
                List<ClienteDireccion> cliDirecciones = cli.getClienteDirecciones();
                cli.setClienteDirecciones(null);
                if (cli.getID() == 0)
                    Cliente.insert(getDataBase(), cli);
                else
                    Cliente.update(getDataBase(), cli);

                if (cli.getID() == 0)
                    cli.setID(getDataBase().queryMaxInt(Contract.Cliente.TABLE_NAME, Contract.Cliente._ID));

                for (int i = 0; i < listViewDirecciones.size(); i++) {
                    ClienteDireccion cliDir = new ClienteDireccion();
                    if (cliDirecciones != null && cliDirecciones.size() > i) {
                        cliDir = cliDirecciones.get(i);
                    } else {
                        //cliDir.setIdClienteDireccion(i+1);
                        if (idCliente != 0)
                            cliDir.setIdCliente(cli.getIdCliente());
                    }
                    cliDir.set_idCliente(cli.getID());
                    cliDir.setDireccion(((EditText) listViewDirecciones.get(i).findViewById(R.id.cliente_direccion)).getText().toString());
                    cliDir.setTipoDireccion(((GeneralValue) ((Spinner) listViewDirecciones.get(i).findViewById(R.id.cliente_tipo_direccion_spinner)).getSelectedItem()).getCode());
                    cliDir.setEsPrincipal(((CheckBox) listViewDirecciones.get(i).findViewById(R.id.cliente_es_principal)).isChecked());
                    cliDir.setTelefono1(((EditText) listViewDirecciones.get(i).findViewById(R.id.cliente_telefono1)).getText().toString());
                    cliDir.setTelefono2(((EditText) listViewDirecciones.get(i).findViewById(R.id.cliente_telefono2)).getText().toString());
                    cliDir.setReferencia(((EditText) listViewDirecciones.get(i).findViewById(R.id.cliente_referencia)).getText().toString());
                    cliDir.setCiudadDescripcion(((AutoCompleteTextView) listViewDirecciones.get(i).findViewById(R.id.cliente_ciudad)).getText().toString());
                    if (listCiudades.size() > i && listCiudades.get(i) != null)
                        cliDir.setIdCiudad((int) listCiudades.get(i).getID());

                    if (!((EditText) listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).getText().toString().equals("")) {
                        cliDir.setLongitud(Double.parseDouble(((EditText) listViewDirecciones.get(i).findViewById(R.id.cliente_longitud)).getText().toString()));
                        cliDir.setLatitud(Double.parseDouble(((EditText) listViewDirecciones.get(i).findViewById(R.id.cliente_latitud)).getText().toString()));
                    }
                    if (cliDir.getID() == 0)
                        ClienteDireccion.insert(getDataBase(), cliDir);
                    else
                        ClienteDireccion.update(getDataBase(), cliDir);

                    if (i == 0) {
                        cli.setDireccion(cliDir.getDireccion());
                        cli.setTelefono(cliDir.getTelefono1());
                        Cliente.update(getDataBase(), cli);
                    }
                }

                for (int i = 0; i < listViewContactos.size(); i++) {
                    Contacto cliCont = new Contacto();
                    if (cliContactos != null && cliContactos.size() > i) {
                        cliCont = cliContactos.get(i);
                    } else {
                        //cliCont.setIdContacto(i+1);
                        if (idCliente != 0)
                            cliCont.setIdCliente(cli.getIdCliente());
                    }
                    cliCont.set_idCliente(cli.getID());
                    cliCont.setNombre(((EditText) listViewContactos.get(i).findViewById(R.id.cliente_nombres)).getText().toString());
                    cliCont.setApellido(((EditText) listViewContactos.get(i).findViewById(R.id.cliente_apellidos)).getText().toString());
                    cliCont.setCargo(((EditText) listViewContactos.get(i).findViewById(R.id.cliente_cargo)).getText().toString());
                    cliCont.setTelefono1(((EditText) listViewContactos.get(i).findViewById(R.id.cliente_telefono1_contacto)).getText().toString());
                    cliCont.setTelefono2(((EditText) listViewContactos.get(i).findViewById(R.id.cliente_telefono2_contacto)).getText().toString());
                    cliCont.setCorreo(((EditText) listViewContactos.get(i).findViewById(R.id.cliente_correo_contacto)).getText().toString());
                    cliCont.setIdClienteDireccion(((Spinner) listViewContactos.get(i).findViewById(R.id.cliente_direccion_contacto)).getSelectedItemPosition() + 1);
                    cliCont.setURLFoto(contactPhotos.get(i));

                    if (cliCont.getID() == 0)
                        Contacto.insert(getDataBase(), cliCont);
                    else
                        Contacto.update(getDataBase(), cliCont);
                }

                if (ConnectionUtils.isNetAvailable(getActivity())) {
                    Bundle bundle = new Bundle();
                    if (idCliente != 0)
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_UPDATE_FULL);
                    else {
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_CREATE);
                        //showDialogConfirmation(DIALOG_VISITA, R.string.message_crear_visita, R.string.label_crear_visita);
                        cliente = cli;
                    }
                    bundle.putLong(ARG_CLIENTE, cli.getID());
                    requestSync(bundle);
                }
                long idCliente = cli.getID();
                ((CrearVisitaFragment) getParentFragment()).onFinishCrearClienteDialog(idCliente);
                dismiss();

            }
        });

        ((Button) view.findViewById(R.id.actividad_cancelar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
