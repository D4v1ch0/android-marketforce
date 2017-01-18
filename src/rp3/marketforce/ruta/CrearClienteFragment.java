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
import android.widget.Toast;

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
    private View view;

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

        view = inflater.inflate(R.layout.fragment_crear_cliente_visita, container);

        ((EditText) view.findViewById(R.id.cliente_primer_nombre)).setText(nombres);

        getDialog().setTitle("Crear Cliente");
        SimpleDictionaryAdapter tipoIdentificacion = new SimpleDictionaryAdapter(getContext(), IdentificationType.getAll(getDataBase()));
        ((Spinner) view.findViewById(R.id.cliente_tipo_identificacion)).setAdapter(tipoIdentificacion);
        ((Spinner) view.findViewById(R.id.cliente_tipo_identificacion)).setPrompt("Seleccione un tipo de identificación");

        ((Button) view.findViewById(R.id.actividad_aceptar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(Validaciones()) {
                    Cliente cli = new Cliente();
                    cli.setIdCanal((int) Canal.getCanal(getDataBase(), "").get(0).getID());
                    cli.setIdTipoIdentificacion((int) ((Spinner) view.findViewById(R.id.cliente_tipo_identificacion)).getAdapter().getItemId(((Spinner) view.findViewById(R.id.cliente_tipo_identificacion)).getSelectedItemPosition()));
                    cli.setIdentificacion(((EditText) view.findViewById(R.id.cliente_identificacion)).getText().toString());
                    cli.setTipoPersona("N");
                    cli.setIdTipoCliente((int) TipoCliente.getTipoCliente(getDataBase(), "").get(0).getID());
                    cli.setExentoImpuesto(false);
                    cli.setCiudadanoOro(false);
                    cli.setNombre1(((EditText) view.findViewById(R.id.cliente_primer_nombre)).getText().toString());
                    cli.setNombre2("");
                    cli.setApellido1(((EditText) view.findViewById(R.id.cliente_primer_apellido)).getText().toString());
                    cli.setApellido2("");
                    cli.setCorreoElectronico("");
                    cli.setGenero("M");
                    cli.setEstadoCivil("S");
                    cli.setNombreCompleto(cli.getNombre1() + " " + cli.getApellido1());
                    cli.setPendiente(true);
                    List<Contacto> cliContactos = cli.getContactos();
                    cli.setContactos(null);
                    List<ClienteDireccion> cliDirecciones = cli.getClienteDirecciones();
                    cli.setClienteDirecciones(null);
                    Cliente.insert(getDataBase(), cli);


                    if (cli.getID() == 0)
                        cli.setID(getDataBase().queryMaxInt(Contract.Cliente.TABLE_NAME, Contract.Cliente._ID));


                    ClienteDireccion cliDir = new ClienteDireccion();
                    cliDir.set_idCliente(cli.getID());
                    cliDir.setDireccion("(Sin descripción)");
                    cliDir.setTipoDireccion("T");
                    cliDir.setEsPrincipal(true);
                    cliDir.setTelefono1(((EditText) view.findViewById(R.id.cliente_movil)).getText().toString());
                    cliDir.setTelefono2(((EditText) view.findViewById(R.id.cliente_fijo)).getText().toString());
                    cliDir.setReferencia("Ninguna");
                    cliDir.setCiudadDescripcion("");
                    cliDir.setIdClienteDireccion(1);

                    ClienteDireccion.insert(getDataBase(), cliDir);

                    cli.setDireccion(cliDir.getDireccion());
                    cli.setTelefono(cliDir.getTelefono1());
                    Cliente.update(getDataBase(), cli);


                    if (ConnectionUtils.isNetAvailable(getActivity())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_CLIENTE_CREATE);
                        bundle.putLong(rp3.marketforce.cliente.CrearClienteFragment.ARG_CLIENTE, cli.getID());
                        requestSync(bundle);
                    }
                    long idCliente = cli.getID();
                    ((CrearVisitaFragment) getParentFragment()).onFinishCrearClienteDialog(idCliente);
                    dismiss();
                }
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

    public boolean Validaciones()
    {
        if(((EditText) view.findViewById(R.id.cliente_primer_nombre)).getText() == null || ((EditText) view.findViewById(R.id.cliente_primer_nombre)).getText().toString().length() <= 0)
        {
            Toast.makeText(getContext(), R.string.warning_falta_nombre, Toast.LENGTH_LONG);
            return false;
        }
        if(((EditText) view.findViewById(R.id.cliente_primer_apellido)).getText() == null || ((EditText) view.findViewById(R.id.cliente_primer_apellido)).getText().toString().length() <= 0)
        {
            Toast.makeText(getContext(), R.string.warning_falta_apellido, Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

}
