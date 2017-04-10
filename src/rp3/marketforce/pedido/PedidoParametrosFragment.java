package rp3.marketforce.pedido;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.ClienteDireccion;
import rp3.marketforce.models.Contacto;
import rp3.marketforce.models.Tarea;
import rp3.marketforce.ruta.CrearVisitaFragment;
import rp3.marketforce.ruta.TareasFragment;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;
import rp3.util.Convert;

/**
 * Created by magno_000 on 07/04/2017.
 */

public class PedidoParametrosFragment extends BaseFragment{
    public static String ARG_TIPO_TRANS = "trans";


    private AutoCompleteTextView cliente_auto;
    private SimpleGeneralValueAdapter adapterDuracion;
    private ArrayList<String> list_nombres;
    private List<Cliente> list_cliente;
    private List<Tarea> list_tareas;
    private String transaccion;
    private SimpleGeneralValueAdapter seriesAdapter;

    public static PedidoParametrosFragment newInstance(String text) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TIPO_TRANS, text);
        PedidoParametrosFragment fragment = new PedidoParametrosFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        String lastText = "";
        transaccion = getArguments().getString(ARG_TIPO_TRANS);
        list_nombres = new ArrayList<String>();
        List<String> list_ciudad = new ArrayList<String>();
        if (list_tareas == null)
            list_tareas = new ArrayList<Tarea>();

        if (cliente_auto != null)
            lastText = cliente_auto.getText().toString();

        cliente_auto = (AutoCompleteTextView) rootView.findViewById(R.id.crear_pedido_cliente);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);

        list_cliente = Cliente.getCliente(getDataBase());
        for (Cliente cli : list_cliente) {
            list_nombres.add(cli.getIdExterno() + " - " + cli.getNombreCompleto().trim());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_small_text, list_nombres);

        list_ciudad.add("Guayaquil");
        list_ciudad.add("Quito");
        final ArrayAdapter<String> ciudadAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, list_ciudad);
        ((Spinner) getRootView().findViewById(R.id.crear_pedido_ciudad)).setAdapter(ciudadAdapter);


        cliente_auto.setAdapter(adapter);
        cliente_auto.setThreshold(1);

        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cliente_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int pos, long id) {
                    ArrayList<String> direcciones = new ArrayList<String>();
                    int position = list_nombres.indexOf(adapter.getItem(pos));
                    if (position != -1) {
                        for (ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones()) {
                            direcciones.add(cliDir.getDireccion());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
                        ((Spinner) rootView.findViewById(R.id.crear_visita_direccion)).setAdapter(adapter);
                    }

                }
            });


        } else {
            cliente_auto.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ArrayList<String> direcciones = new ArrayList<String>();
                    int position = list_nombres.indexOf(cliente_auto.getText().toString());
                    if (position != -1) {
                        for (ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones()) {
                            direcciones.add(cliDir.getDireccion());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
                        ((Spinner) rootView.findViewById(R.id.crear_pedido_direccion)).setAdapter(adapter);
                    }

                }
            });
        }

        if (!lastText.equalsIgnoreCase("")) {
            ArrayList<String> direcciones = new ArrayList<String>();
            int position = list_nombres.indexOf(lastText);
            if (position != -1) {
                for (ClienteDireccion cliDir : list_cliente.get(position).getClienteDirecciones()) {
                    direcciones.add(cliDir.getDireccion());
                }
                ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
                ((Spinner) rootView.findViewById(R.id.crear_pedido_direccion)).setAdapter(adapterDir);
            }
        }

        ((Button) rootView.findViewById(R.id.crear_pedido)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                long idCliente = 0;
                int position = list_nombres.indexOf(cliente_auto.getText().toString());
                if (position != -1) {
                    idCliente = list_cliente.get(position).getID();
                }
                Intent intent = new Intent(getContext(), CrearPedidoActivity.class);
                intent.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, transaccion);
                intent.putExtra(CrearPedidoActivity.ARG_CLIENTE, idCliente);
                intent.putExtra(CrearPedidoActivity.ARG_SERIE, seriesAdapter.getCode(((Spinner) getRootView().findViewById(R.id.crear_pedido_serie)).getSelectedItemPosition()));
                dismiss();
                startActivity(intent);
            }
        });

        seriesAdapter = new SimpleGeneralValueAdapter(this.getContext(), GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_SERIES_BERLIN));
        ((Spinner) getRootView().findViewById(R.id.crear_pedido_serie)).setAdapter(seriesAdapter);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        super.setContentView(R.layout.fragment_pedido_parametros);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
