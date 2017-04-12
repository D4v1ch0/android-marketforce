package rp3.auna.cliente;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pe.solera.api_payme_android.bean_response.AuthorizeResponse;
import pe.solera.api_payme_android.bean_response.PayMeResponse;
import pe.solera.api_payme_android.interfaces.HTTPRequestInterface;
import pe.solera.api_payme_android.util.PGPUtils;
import pe.solera.api_payme_android.web_services.ParseServices;
import rp3.app.BaseFragment;
import rp3.auna.Contants;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.auna.R;
import rp3.auna.actividades.ActualizacionFragment;
import rp3.auna.models.auna.ClienteTarjeta;
import rp3.data.models.GeneralValue;

/**
 * Created by magno_000 on 17/01/2017.
 */
public class AgregarTarjetaFragment extends BaseFragment {

    public static String ARG_POS = "posicion";

    private int pos;
    private View view;

    public interface AgregarTarjetaDialogListener {
        void onFinishAgregarTarjetaDialog(ClienteTarjeta clienteTarjeta);
    }

    public static AgregarTarjetaFragment newInstance(int pos) {
        AgregarTarjetaFragment fragment = new AgregarTarjetaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POS, pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pos = getArguments().getInt(ARG_POS);

        view = inflater.inflate(R.layout.layout_cliente_tarjeta_detail, container);

        getDialog().setTitle("Agregar Tarjeta");

        ((Button) view.findViewById(R.id.validar_tarjeta)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(Validaciones()) {
                    ClienteTarjeta clienteTarjeta = new ClienteTarjeta();
                    clienteTarjeta.setIdMarcaTarjeta(((GeneralValue)((Spinner) view.findViewById(R.id.cliente_tarjeta)).getSelectedItem()).getCode());
                    clienteTarjeta.setFechaCaducidad(((ArrayAdapter<String>) ((Spinner) view.findViewById(R.id.cliente_tarjeta_fecha)).getAdapter()).getItem(((Spinner) view.findViewById(R.id.cliente_tarjeta_fecha)).getSelectedItemPosition()));
                    clienteTarjeta.setNumero(((EditText) view.findViewById(R.id.cliente_numero_tarjeta)).getText().toString());
                    clienteTarjeta.setCodigoSeguridad(((EditText) view.findViewById(R.id.cliente_cod_seguridad)).getText().toString());
                    clienteTarjeta.setEsPrincipal(((CheckBox) view.findViewById(R.id.cliente_es_principal_tarjeta)).isChecked());
                    ((ActualizacionFragment) getParentFragment()).onFinishAgregarTarjetaDialog(clienteTarjeta);
                    //dismiss();
                }
            }});

        ((Button) view.findViewById(R.id.eliminar_tarjeta)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if(pos == 0)
            ((CheckBox) view.findViewById(R.id.cliente_es_principal_tarjeta)).setChecked(true);

        SimpleGeneralValueAdapter procesadorasAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), Contants.GENERAL_TABLE_PROCESADORA);
        ((Spinner) view.findViewById(R.id.cliente_tarjeta)).setAdapter(procesadorasAdapter);

        ((Spinner) view.findViewById(R.id.cliente_tarjeta_fecha)).setAdapter(getFechasCaducidad());

        return view;
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
        if(((EditText) view.findViewById(R.id.cliente_numero_tarjeta)).getText() == null || ((EditText) view.findViewById(R.id.cliente_numero_tarjeta)).getText().toString().length() <= 0)
        {
            Toast.makeText(getContext(), R.string.warning_falta_nombre, Toast.LENGTH_LONG);
            return false;
        }
        if(((EditText) view.findViewById(R.id.cliente_cod_seguridad)).getText() == null || ((EditText) view.findViewById(R.id.cliente_cod_seguridad)).getText().toString().length() <= 0)
        {
            Toast.makeText(getContext(), R.string.warning_falta_apellido, Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

}