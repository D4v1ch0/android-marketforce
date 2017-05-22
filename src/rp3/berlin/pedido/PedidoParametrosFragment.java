package rp3.berlin.pedido;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.Agenda;
import rp3.berlin.models.Cliente;
import rp3.berlin.models.ClienteDireccion;
import rp3.berlin.models.Tarea;

/**
 * Created by magno_000 on 07/04/2017.
 */

public class PedidoParametrosFragment extends BaseFragment{
    public static String ARG_TIPO_TRANS = "trans";
    public static String ARG_TIPO_FROM = "from";
    public static String ARG_ID_AGENDA = "id_agenda";

    private AutoCompleteTextView cliente_auto, serie_auto;
    private ArrayList<String> list_nombres, list_nombres_series;
    private List<Cliente> list_cliente;
    private List<GeneralValue> list_serie;
    private List<Tarea> list_tareas;
    private String transaccion;
    private SimpleGeneralValueAdapter tipoOrdenAdapter;
    private int from;
    private long id_agenda;

    public static PedidoParametrosFragment newInstance(String text, int from) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TIPO_TRANS, text);
        arguments.putInt(ARG_TIPO_FROM, from);
        PedidoParametrosFragment fragment = new PedidoParametrosFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static PedidoParametrosFragment newInstance(String text) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TIPO_TRANS, text);
        PedidoParametrosFragment fragment = new PedidoParametrosFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public static PedidoParametrosFragment newInstance(String text, long id_agenda) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TIPO_TRANS, text);
        arguments.putLong(ARG_ID_AGENDA, id_agenda);
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
        from = getArguments().getInt(ARG_TIPO_FROM , 0);
        id_agenda = getArguments().getLong(ARG_ID_AGENDA , 0);
        list_nombres = new ArrayList<String>();
        list_nombres_series = new ArrayList<String>();
        List<String> list_ciudad = new ArrayList<String>();
        if (list_tareas == null)
            list_tareas = new ArrayList<Tarea>();

        if (cliente_auto != null)
            lastText = cliente_auto.getText().toString();

        cliente_auto = (AutoCompleteTextView) rootView.findViewById(R.id.crear_pedido_cliente);
        serie_auto = (AutoCompleteTextView) rootView.findViewById(R.id.crear_pedido_serie);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);

        list_cliente = Cliente.getCliente(getDataBase());
        for (Cliente cli : list_cliente) {
            list_nombres.add(cli.getIdExterno() + " - " + cli.getNombreCompleto().trim());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_small_text, list_nombres);

        list_serie = GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_SERIES_BERLIN);
        for (GeneralValue gv : list_serie) {
            list_nombres_series.add(gv.getCode() + " - " + gv.getValue());
        }
        final ArrayAdapter<String> adapterSeries = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_small_text, list_nombres_series);

        tipoOrdenAdapter = new SimpleGeneralValueAdapter(getContext(), GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_TIPO_ORDEN_BERLIN));
        ((Spinner) getRootView().findViewById(R.id.crear_pedido_tipo_orden)).setAdapter(tipoOrdenAdapter);

        list_ciudad.add("Guayaquil");
        list_ciudad.add("Quito");
        final ArrayAdapter<String> ciudadAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, list_ciudad);
        ((Spinner) getRootView().findViewById(R.id.crear_pedido_ciudad)).setAdapter(ciudadAdapter);

        cliente_auto.setAdapter(adapter);
        cliente_auto.setThreshold(3);

        serie_auto.setAdapter(adapterSeries);
        serie_auto.setThreshold(3);

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
                        Collections.sort(direcciones, new Comparator<String>() {
                            @Override
                            public int compare(final String object1, final String object2) {
                                return object1.compareTo(object2);
                            }
                        });
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
                        Collections.sort(direcciones, new Comparator<String>() {
                            @Override
                            public int compare(final String object1, final String object2) {
                                return object1.compareTo(object2);
                            }
                        });
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
                String idSerie = "";
                int position = list_nombres.indexOf(cliente_auto.getText().toString());
                int position_series = list_nombres_series.indexOf(serie_auto.getText().toString());
                if (position != -1) {
                    if (position_series != -1) {
                        idCliente = list_cliente.get(position).getID();
                        idSerie = list_serie.get(position_series).getCode();
                        int idDireccion = list_cliente.get(position).getClienteDirecciones().get(((Spinner) rootView.findViewById(R.id.crear_pedido_direccion)).getSelectedItemPosition()).getIdClienteDireccion();
                        Intent intent = new Intent(getContext(), CrearPedidoActivity.class);
                        intent.putExtra(CrearPedidoActivity.ARG_TIPO_DOCUMENTO, transaccion);
                        intent.putExtra(CrearPedidoActivity.ARG_CLIENTE, idCliente);
                        intent.putExtra(CrearPedidoActivity.ARG_SERIE, idSerie);
                        intent.putExtra(CrearPedidoActivity.ARG_DIRECCION, idDireccion);
                        intent.putExtra(CrearPedidoActivity.ARG_TIPO_ORDEN, tipoOrdenAdapter.getCode(((Spinner) getRootView().findViewById(R.id.crear_pedido_tipo_orden)).getSelectedItemPosition()));
                        intent.putExtra(CrearPedidoActivity.ARG_IDAGENDA, id_agenda);
                        dismiss();
                        if(from == 1)
                            getActivity().finish();
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Debe seleccionar una serie", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Seteo cliente en el caso de que se venga de una agenda
        if(id_agenda != 0)
        {
            Agenda agd = Agenda.getAgenda(getDataBase(), id_agenda);
            for(int i = 0; i < list_cliente.size(); i++)
            {
                Cliente cli = list_cliente.get(i);
                if(cli.getIdCliente() == agd.getIdCliente())
                {
                    ArrayList<String> direcciones = new ArrayList<String>();
                    cliente_auto.setText(list_nombres.get(i));
                    for (ClienteDireccion cliDir : list_cliente.get(i).getClienteDirecciones()) {
                        direcciones.add(cliDir.getDireccion());
                    }
                    Collections.sort(direcciones, new Comparator<String>() {
                        @Override
                        public int compare(final String object1, final String object2) {
                            return object1.compareTo(object2);
                        }
                    });
                    ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(getContext(), R.layout.spinner_small_text, direcciones);
                    ((Spinner) rootView.findViewById(R.id.crear_pedido_direccion)).setAdapter(adapterDir);
                    cliente_auto.setEnabled(false);
                    cliente_auto.setFocusable(false);
                    cliente_auto.setFocusableInTouchMode(false);
                }
            }
        }

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
