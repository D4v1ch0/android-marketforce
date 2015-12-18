package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.utils.NothingSelectedSpinnerAdapter;

/**
 * Created by magno_000 on 16/12/2015.
 */
public class AgregarPagoFragment extends BaseFragment {

    public static final String ARG_SALDO = "saldo";

    private PagoAgregarListener createFragmentListener;
    private PagoAdapter adapter;
    private NumberFormat numberFormat;
    private double saldo;

    public static AgregarPagoFragment newInstance(double saldo)
    {
        AgregarPagoFragment fragment = new AgregarPagoFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_SALDO, saldo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setContentView(R.layout.fragment_agregar_pago);
        if(getParentFragment()!=null){
            createFragmentListener = (PagoAgregarListener)getParentFragment();
        }else{
            createFragmentListener = (PagoAgregarListener) activity;
        }

    }

    public interface PagoAgregarListener{
        public void onAcceptSuccess(Pago pago);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        saldo = getArguments().getDouble(ARG_SALDO);

        SimpleIdentifiableAdapter formasPago = new SimpleIdentifiableAdapter(getContext(), FormaPago.getFormasPago(getDataBase()));
        ((Spinner) rootView.findViewById(R.id.pago_tipo)).setAdapter(formasPago);
        ((EditText) getRootView().findViewById(R.id.pago_valor)).setText(saldo + "");

        rootView.findViewById(R.id.actividad_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.actividad_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((Spinner) getRootView().findViewById(R.id.pago_tipo)).getSelectedItemPosition() == -1)
                {
                    Toast.makeText(getContext(), "Falta especificar la forma de pago.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(((EditText) getRootView().findViewById(R.id.pago_valor)).length() == 0 || Float.parseFloat(((EditText) getRootView().findViewById(R.id.pago_valor)).getText().toString()) == 0)
                {
                    Toast.makeText(getContext(), "Ingrese el valor del pago.", Toast.LENGTH_LONG).show();
                    return;
                }
                Pago pago = new Pago();
                pago.setValor(Float.parseFloat(((EditText) getRootView().findViewById(R.id.pago_valor)).getText().toString()));
                pago.setIdFormaPago((int) ((Spinner) getRootView().findViewById(R.id.pago_tipo)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.pago_tipo)).getSelectedItemPosition()));
                pago.setFormaPago(FormaPago.getFormaPago(getDataBase(), pago.getIdFormaPago()));
                if(!pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo") && pago.getValor() > saldo)
                {
                    Toast.makeText(getContext(), "Solo debe de existir excedente con efectivo.", Toast.LENGTH_LONG).show();
                    return;
                }
                pago.setObservacion(((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).getText().toString());
                createFragmentListener.onAcceptSuccess(pago);
                dismiss();
            }
        });
    }
}
