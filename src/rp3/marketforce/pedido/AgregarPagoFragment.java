package rp3.marketforce.pedido;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.utils.NothingSelectedSpinnerAdapter;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 16/12/2015.
 */
public class AgregarPagoFragment extends BaseFragment {

    public static final String ARG_SALDO = "saldo";
    public static final String ARG_IDPAGO = "idpago";
    public static final String ARG_EFECTIVO = "efectivo";
    public static final String ARG_PAGO = "pago";
    public static final String ARG_FORMA = "forma";
    public static final String ARG_OBS = "obs";

    public static final int REQ_CODE_SPEECH_INPUT_PAGO = 102;

    private PagoAgregarListener createFragmentListener;
    private PagoAdapter adapter;
    private NumberFormat numberFormat;
    private double saldo, pago;
    private int idpago, idFormaPago;
    private boolean efectivo;
    private String obs;

    public static AgregarPagoFragment newInstance(double saldo, boolean efectivo)
    {
        AgregarPagoFragment fragment = new AgregarPagoFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_SALDO, saldo);
        args.putBoolean(ARG_EFECTIVO, efectivo);
        fragment.setArguments(args);
        return fragment;
    }

    public static AgregarPagoFragment newInstance(int idpago, double saldo, boolean efectivo, Pago pago)
    {
        AgregarPagoFragment fragment = new AgregarPagoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IDPAGO, idpago);
        if(saldo > 0)
            args.putDouble(ARG_SALDO, saldo + pago.getValor());
        else
            args.putDouble(ARG_SALDO, pago.getValor());
        args.putBoolean(ARG_EFECTIVO, efectivo);
        args.putDouble(ARG_PAGO, pago.getValor());
        args.putInt(ARG_FORMA, pago.getIdFormaPago());
        args.putString(ARG_OBS, pago.getObservacion());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case REQ_CODE_SPEECH_INPUT_PAGO:
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    setTextViewText(R.id.actividad_texto_respuesta, StringUtils.getStringCapSentence(result.get(0)));
                }
                break;
        }
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        saldo = getArguments().getDouble(ARG_SALDO);
        idpago = getArguments().getInt(ARG_IDPAGO, -1);
        efectivo = getArguments().getBoolean(ARG_EFECTIVO);

        SimpleIdentifiableAdapter formasPago = new SimpleIdentifiableAdapter(getContext(), FormaPago.getFormasPago(getDataBase()));
        ((Spinner) rootView.findViewById(R.id.pago_tipo)).setAdapter(formasPago);
        ((EditText) getRootView().findViewById(R.id.pago_valor)).setText(saldo + "");

        ((ImageView) rootView.findViewById(R.id.actividad_voice_to_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        if(idpago != -1)
        {
            obs = getArguments().getString(ARG_OBS);
            pago = getArguments().getDouble(ARG_PAGO);
            idFormaPago = getArguments().getInt(ARG_FORMA);

            List<FormaPago> lista_formas = FormaPago.getFormasPago(getDataBase());
            for(int i = 0; i < lista_formas.size(); i++)
            {
                if(lista_formas.get(i).getIdFormaPago() == idFormaPago)
                    ((Spinner) rootView.findViewById(R.id.pago_tipo)).setSelection(i);
            }
            ((EditText) getRootView().findViewById(R.id.pago_valor)).setText(pago + "");
            ((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).setText(obs);

        }

        rootView.findViewById(R.id.actividad_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.actividad_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Spinner) getRootView().findViewById(R.id.pago_tipo)).getSelectedItemPosition() == -1) {
                    Toast.makeText(getContext(), "Falta especificar la forma de pago.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (((EditText) getRootView().findViewById(R.id.pago_valor)).length() == 0 || Float.parseFloat(((EditText) getRootView().findViewById(R.id.pago_valor)).getText().toString()) <= 0) {
                    Toast.makeText(getContext(), "Ingrese el valor del pago.", Toast.LENGTH_LONG).show();
                    return;
                }
                Pago pago = new Pago();
                pago.setIdPago(idpago);
                pago.setValor(Float.parseFloat(((EditText) getRootView().findViewById(R.id.pago_valor)).getText().toString()));
                int idFormaPago = ((int) ((Spinner) getRootView().findViewById(R.id.pago_tipo)).getAdapter().getItemId(((Spinner) getRootView().findViewById(R.id.pago_tipo)).getSelectedItemPosition()));
                pago.setFormaPago(FormaPago.getFormaPagoInt(getDataBase(), idFormaPago));
                pago.setIdFormaPago(pago.getFormaPago().getIdFormaPago());
                if (!pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo") && pago.getValor() > saldo) {
                    Toast.makeText(getContext(), "Solo debe de existir excedente con efectivo.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo") && efectivo) {
                    Toast.makeText(getContext(), "Ya existe un pago con efectivo. Solo se puede ingresar uno.", Toast.LENGTH_LONG).show();
                    return;
                }
                pago.setObservacion(((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).getText().toString());
                createFragmentListener.onAcceptSuccess(pago);
                dismiss();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable Ahora");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_PAGO);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
