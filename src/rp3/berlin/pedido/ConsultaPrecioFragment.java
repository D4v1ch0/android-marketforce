package rp3.berlin.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
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
import rp3.berlin.loader.LibroPrecioLoader;
import rp3.berlin.models.Cliente;
import rp3.berlin.models.pedido.LibroPrecio;
import rp3.berlin.utils.NothingSelectedSpinnerAdapter;

/**
 * Created by magno_000 on 28/04/2017.
 */

public class ConsultaPrecioFragment extends BaseFragment {

    private ArrayList<String> list_nombres;
    private AutoCompleteTextView cliente_auto;
    private AutoCompleteTextView producto_auto;
    private List<Cliente> list_cliente;
    private SimpleGeneralValueAdapter lineaAdapter;
    private ArrayList<String> list_libro;
    private ProductoAutoCompleteAdapter productoAdapter;
    private LoaderPrecio loaderPrecios;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        String lastText = "";
        list_nombres = new ArrayList<String>();
        list_libro = new ArrayList<String>();

        if (cliente_auto != null)
            lastText = cliente_auto.getText().toString();

        cliente_auto = (AutoCompleteTextView) rootView.findViewById(R.id.precio_cliente);
        producto_auto = (AutoCompleteTextView) rootView.findViewById(R.id.precio_producto);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);

        list_cliente = Cliente.getCliente(getDataBase());
        for (Cliente cli : list_cliente) {
            list_nombres.add(cli.getIdExterno() + " - " + cli.getNombreCompleto().trim());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_small_text, list_nombres);

        lineaAdapter = new SimpleGeneralValueAdapter(getContext(), GeneralValue.getGeneralValues(getDataBase(), Contants.GENERAL_TABLE_LINEA_BERLIN));
        ((Spinner) getRootView().findViewById(R.id.precio_linea)).setAdapter(new NothingSelectedSpinnerAdapter(
                lineaAdapter,
                R.layout.spinner_empty_selected,
                this.getContext(), "Línea"));
        //((Spinner) getRootView().findViewById(R.id.precio_linea)).setAdapter(lineaAdapter);

        list_libro.add("Remate");
        list_libro.add("Standard");
        final ArrayAdapter<String> libroAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, list_libro);
        ((Spinner) getRootView().findViewById(R.id.precio_libro)).setAdapter(libroAdapter);

        productoAdapter = new ProductoAutoCompleteAdapter(this.getContext(), getDataBase());

        cliente_auto.setAdapter(adapter);
        cliente_auto.setThreshold(3);

        producto_auto.setAdapter(productoAdapter);
        producto_auto.setThreshold(3);

        ((Button) rootView.findViewById(R.id.precio_consultar)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogProgress("Consultando", "Cargando precios");
                if(((RadioButton) getRootView().findViewById(R.id.precio_general)).isChecked())
                {
                    String producto = "", libro = "", linea = "";
                    producto = ((EditText) getRootView().findViewById(R.id.precio_producto)).getText().toString();
                    if(((Spinner) getRootView().findViewById(R.id.precio_linea)).getSelectedItemPosition() > 0)
                        linea = lineaAdapter.getCode(((Spinner) getRootView().findViewById(R.id.precio_linea)).getSelectedItemPosition() - 1);
                    int position_libro = ((Spinner) getRootView().findViewById(R.id.precio_libro)).getSelectedItemPosition();
                    if(position_libro == 0)
                        libro = Contants.LIBRO_REMATE;
                    else if(position_libro == 1)
                        libro = Contants.LIBRO_ESTANDAR;
                    if(producto.length() > 0 || libro.length() > 0 || linea.length() > 0) {
                        Bundle args = new Bundle();
                        args.putString(LoaderPrecio.STRING_CLIENTE, "");
                        args.putString(LoaderPrecio.STRING_ITEM, producto);
                        args.putString(LoaderPrecio.STRING_LIBRO, libro);
                        args.putString(LoaderPrecio.STRING_LINEA, linea);
                        args.putString(LoaderPrecio.STRING_TIPO, "Consulta");
                        if (loaderPrecios == null)
                            loaderPrecios = new LoaderPrecio();
                        executeLoader(0, args, loaderPrecios);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Debe ingresar al menos un parámetro", Toast.LENGTH_LONG).show();
                        closeDialogProgress();
                    }
                }
                else if(((RadioButton) getRootView().findViewById(R.id.precio_por_cliente)).isChecked())
                {
                    String cliente = "", producto = "";
                    int position = list_nombres.indexOf(((EditText) getRootView().findViewById(R.id.precio_cliente)).getText().toString());
                    if(position > -1)
                        cliente = list_cliente.get(position).getIdExterno();
                    producto = ((EditText) getRootView().findViewById(R.id.precio_producto)).getText().toString();
                    if(producto.length() > 0 || cliente.length() > 0) {
                        Bundle args = new Bundle();
                        args.putString(LoaderPrecio.STRING_CLIENTE, cliente);
                        args.putString(LoaderPrecio.STRING_ITEM, producto);
                        args.putString(LoaderPrecio.STRING_LIBRO, Contants.LIBRO_CLIENTE);
                        args.putString(LoaderPrecio.STRING_LINEA, "");
                        args.putString(LoaderPrecio.STRING_TIPO, "Consulta");
                        if (loaderPrecios == null)
                            loaderPrecios = new LoaderPrecio();
                        executeLoader(0, args, loaderPrecios);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Debe ingresar al menos un parámetro", Toast.LENGTH_LONG).show();
                        closeDialogProgress();
                    }
                }
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((RadioButton) view).isChecked();
                switch (view.getId()) {
                    case R.id.precio_general:
                        if (checked) {
                            getRootView().findViewById(R.id.precio_por_cliente_layout).setVisibility(View.GONE);
                            getRootView().findViewById(R.id.precio_general_layout).setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.precio_por_cliente:
                        if (checked) {
                            getRootView().findViewById(R.id.precio_por_cliente_layout).setVisibility(View.VISIBLE);
                            getRootView().findViewById(R.id.precio_general_layout).setVisibility(View.GONE);
                        }
                        break;
                }
            }
        };

        ((RadioButton) getRootView().findViewById(R.id.precio_general)).setOnClickListener(listener);
        ((RadioButton) getRootView().findViewById(R.id.precio_por_cliente)).setOnClickListener(listener);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        super.setContentView(R.layout.fragment_consulta_precio);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public class LoaderPrecio implements LoaderManager.LoaderCallbacks<List<LibroPrecio>> {

        public static final String STRING_CLIENTE = "cliente";
        public static final String STRING_ITEM = "item";
        public static final String STRING_LIBRO = "libro";
        public static final String STRING_LINEA = "linea";
        public static final String STRING_TIPO = "tipo";
        private String item, cliente, libro, linea, tipo;

        @Override
        public Loader<List<LibroPrecio>> onCreateLoader(int arg0,
                                                        Bundle bundle) {

            item = bundle.getString(STRING_ITEM);
            cliente = bundle.getString(STRING_CLIENTE);
            linea = bundle.getString(STRING_LINEA);
            tipo = bundle.getString(STRING_TIPO);
            libro = bundle.getString(STRING_LIBRO);
            return new LibroPrecioLoader(getActivity(), getDataBase(), item, cliente, linea, tipo, libro);

        }

        @Override
        public void onLoadFinished(Loader<List<LibroPrecio>> arg0,
                                   List<LibroPrecio> data) {

            try {
                Collections.sort(data, new Comparator<LibroPrecio>() {
                    @Override
                    public int compare(final LibroPrecio object1, final LibroPrecio object2) {
                        return object1.getDescripcion().compareTo(object2.getDescripcion());
                    }
                });
                PrecioAdapter adapter = new PrecioAdapter(getContext(), data);
                ((ListView) getRootView().findViewById(R.id.list_precios)).setAdapter(adapter);
                getRootView().findViewById(R.id.precio_layout).setVisibility(View.VISIBLE);
                closeDialogProgress();
            } catch (Exception ex) {

            }
        }

        @Override
        public void onLoaderReset(Loader<List<LibroPrecio>> arg0) {

        }
    }
}
