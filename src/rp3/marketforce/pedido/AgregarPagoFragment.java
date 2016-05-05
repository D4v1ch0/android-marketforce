package rp3.marketforce.pedido;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.data.Identifiable;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.models.pedido.Banco;
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.MarcaTarjeta;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.TipoDiferido;
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
        public double getNewSaldo(int idPago);
        public double getNewSaldoUpdate(int idFormaPago, int idPago);
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
        DecimalFormat df = new DecimalFormat("#.##");

        SimpleIdentifiableAdapter formasPago = new SimpleIdentifiableAdapter(getContext(), FormaPago.getFormasPago(getDataBase()));
        ((Spinner) rootView.findViewById(R.id.pago_tipo)).setAdapter(formasPago);
        ((Spinner) rootView.findViewById(R.id.pago_tipo)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DecimalFormat df = new DecimalFormat("#.##");
                ValidarCampos(FormaPago.getFormasPago(getDataBase()).get(position));
                if(idpago != - 1)
                    saldo = createFragmentListener.getNewSaldoUpdate(FormaPago.getFormasPago(getDataBase()).get(position).getIdFormaPago(), idpago);
                else
                    saldo = createFragmentListener.getNewSaldo(FormaPago.getFormasPago(getDataBase()).get(position).getIdFormaPago());
                ((EditText) getRootView().findViewById(R.id.pago_valor)).setText(df.format(saldo) + ".00");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<Banco> listBancos = Banco.getBancos(getDataBase());
        SimpleIdentifiableAdapter bancos = new SimpleIdentifiableAdapter(getContext(), listBancos);
        ((Spinner) rootView.findViewById(R.id.pago_banco)).setAdapter(bancos);
        ((Spinner) rootView.findViewById(R.id.pago_banco)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int banco = ((int) ((Identifiable)((Spinner) getRootView().findViewById(R.id.pago_banco)).getAdapter().getItem(position)).getValue(""));
                List<MarcaTarjeta> listMarcaTarjetas = null;
                listMarcaTarjetas = MarcaTarjeta.getMarcasTarjetasPorBanco(getDataBase(), banco);
                SimpleIdentifiableAdapter tarjetas = new SimpleIdentifiableAdapter(getContext(), listMarcaTarjetas);
                ((Spinner) rootView.findViewById(R.id.pago_tarjeta)).setAdapter(tarjetas);
                if (listMarcaTarjetas.size() > 0) {
                    int marca = listMarcaTarjetas.get(((Spinner) getRootView().findViewById(R.id.pago_tarjeta)).getSelectedItemPosition()).getIdMarcaTarjeta();
                    SimpleIdentifiableAdapter tipoDiferidos = new SimpleIdentifiableAdapter(getContext(), TipoDiferido.getTipoDiferidosByBancoTarjeta(getDataBase(), banco, marca));
                    ((Spinner) rootView.findViewById(R.id.pago_diferido)).setAdapter(tipoDiferidos);
                }
                SimpleIdentifiableAdapter tipoDiferidos = new SimpleIdentifiableAdapter(getContext(), new ArrayList<Identifiable>());
                ((Spinner) rootView.findViewById(R.id.pago_diferido)).setAdapter(tipoDiferidos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<MarcaTarjeta> listMarcaTarjetas = null;
        if(listBancos.size() > 0) {
            listMarcaTarjetas = MarcaTarjeta.getMarcasTarjetasPorBanco(getDataBase(), listBancos.get(0).getIdBanco());
            SimpleIdentifiableAdapter marcaTarjetas = new SimpleIdentifiableAdapter(getContext(), listMarcaTarjetas);
            ((Spinner) rootView.findViewById(R.id.pago_tarjeta)).setAdapter(marcaTarjetas);
        }

        ((Spinner) rootView.findViewById(R.id.pago_tarjeta)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int marca = ((int) ((Identifiable)((Spinner) getRootView().findViewById(R.id.pago_tarjeta)).getAdapter().getItem(position)).getValue(""));
                int banco = ((int) ((Identifiable)((Spinner) getRootView().findViewById(R.id.pago_banco)).getAdapter().getItem(((Spinner) getRootView().findViewById(R.id.pago_banco)).getSelectedItemPosition())).getValue(""));
                SimpleIdentifiableAdapter tipoDiferidos = new SimpleIdentifiableAdapter(getContext(), TipoDiferido.getTipoDiferidosByBancoTarjeta(getDataBase(), banco, marca));
                ((Spinner) rootView.findViewById(R.id.pago_diferido)).setAdapter(tipoDiferidos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(listMarcaTarjetas != null && listMarcaTarjetas.size() != 0 && listBancos.size() > 0) {
            List<TipoDiferido> listTipoDiferido = TipoDiferido.getTipoDiferidosByBancoTarjeta(getDataBase(), listBancos.get(0).getIdBanco(), listMarcaTarjetas.get(0).getIdMarcaTarjeta());
            SimpleIdentifiableAdapter tipoDiferidos = new SimpleIdentifiableAdapter(getContext(), listTipoDiferido);
            ((Spinner) rootView.findViewById(R.id.pago_diferido)).setAdapter(tipoDiferidos);
        }

        ((EditText) getRootView().findViewById(R.id.pago_valor)).setText(df.format(saldo) + ".00");

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
                if(lista_formas.get(i).getIdFormaPago() == idFormaPago) {
                    ValidarCampos(lista_formas.get(i));
                    ((Spinner) rootView.findViewById(R.id.pago_tipo)).setSelection(i);
                }
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
                if (pago.getFormaPago().getDescripcion().equalsIgnoreCase("Cheque"))
                {
                    if (((Spinner) getRootView().findViewById(R.id.pago_banco)).getSelectedItemPosition() < -1 && GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEBANCHEQ).getValue().equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getContext(), "Debe escoger un banco.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USECUECHEQ).getValue().equalsIgnoreCase("1") && ((EditText) getRootView().findViewById(R.id.pago_numero_cuenta)).length() <= 0)
                    {
                        Toast.makeText(getContext(), "Debe ingresar el número de cuenta.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USENUMCHEQ).getValue().equalsIgnoreCase("1") && ((EditText) getRootView().findViewById(R.id.pago_numero_documento)).length() <= 0)
                    {
                        Toast.makeText(getContext(), "Debe ingresar el número de documento.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    pago.setIdBanco(((int) ((Identifiable)((Spinner) getRootView().findViewById(R.id.pago_banco)).getAdapter().getItem(((Spinner) getRootView().findViewById(R.id.pago_banco)).getSelectedItemPosition())).getValue("")));
                    pago.setNumeroCuenta(((EditText) getRootView().findViewById(R.id.pago_numero_cuenta)).getText().toString());
                    pago.setNumeroDocumento(((EditText) getRootView().findViewById(R.id.pago_numero_documento)).getText().toString());
                }
                if (pago.getFormaPago().getDescripcion().equalsIgnoreCase("Tarjeta Credito"))
                {
                    if (((Spinner) getRootView().findViewById(R.id.pago_banco)).getSelectedItemPosition() <= -1 && GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEBANCOTC).getValue().equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getContext(), "Debe escoger un banco.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (((Spinner) getRootView().findViewById(R.id.pago_tarjeta)).getSelectedItemPosition() <= -1 && GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEMARCATC).getValue().equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getContext(), "Debe escoger una tarjeta.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (((Spinner) getRootView().findViewById(R.id.pago_diferido)).getSelectedItemPosition() <= -1 && GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEDIFERTC).getValue().equalsIgnoreCase("1"))
                    {
                        Toast.makeText(getContext(), "Debe escoger el tipo de diferido.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USENUMTC).getValue().equalsIgnoreCase("1") && ((EditText) getRootView().findViewById(R.id.pago_numero_documento)).length() <= 0)
                    {
                        Toast.makeText(getContext(), "Debe ingresar el número de tarjeta.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEAUTORTC).getValue().equalsIgnoreCase("1") && ((EditText) getRootView().findViewById(R.id.pago_autorizacion)).length() <= 0)
                    {
                        Toast.makeText(getContext(), "Debe ingresar el código de autorización.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USELOTETC).getValue().equalsIgnoreCase("1") && ((EditText) getRootView().findViewById(R.id.pago_lote)).length() <= 0)
                    {
                        Toast.makeText(getContext(), "Debe ingresar el número de lote.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    pago.setIdBanco(((int) ((Identifiable)((Spinner) getRootView().findViewById(R.id.pago_banco)).getAdapter().getItem(((Spinner) getRootView().findViewById(R.id.pago_banco)).getSelectedItemPosition())).getValue("")));
                    pago.setIdMarcaTarjeta(((int) ((Identifiable)((Spinner) getRootView().findViewById(R.id.pago_tarjeta)).getAdapter().getItem(((Spinner) getRootView().findViewById(R.id.pago_tarjeta)).getSelectedItemPosition())).getValue("")));
                    pago.setIdTipoDiferido(((int) ((Identifiable)((Spinner) getRootView().findViewById(R.id.pago_diferido)).getAdapter().getItem(((Spinner) getRootView().findViewById(R.id.pago_diferido)).getSelectedItemPosition())).getValue("")));
                    pago.setNumeroDocumento(((EditText) getRootView().findViewById(R.id.pago_numero_documento)).getText().toString());
                    pago.setAutorizadorTarjeta(Integer.parseInt(((EditText) getRootView().findViewById(R.id.pago_autorizacion)).getText().toString()));
                    pago.setCodigoSeguridad(Integer.parseInt(((EditText) getRootView().findViewById(R.id.pago_lote)).getText().toString()));
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

    private void ValidarCampos(FormaPago formaPago)
    {
        getRootView().findViewById(R.id.pago_banco_layout).setVisibility(View.GONE);
        getRootView().findViewById(R.id.pago_tarjeta_layout).setVisibility(View.GONE);
        getRootView().findViewById(R.id.pago_numero_cuenta_layout).setVisibility(View.GONE);
        getRootView().findViewById(R.id.pago_numero_documento_layout).setVisibility(View.GONE);
        getRootView().findViewById(R.id.pago_autorizador_layout).setVisibility(View.GONE);
        getRootView().findViewById(R.id.pago_codigo_seguridad_layout).setVisibility(View.GONE);
        getRootView().findViewById(R.id.pago_tipo_diferido_layout).setVisibility(View.GONE);
        if(formaPago.getDescripcion().equalsIgnoreCase("Cheque"))
        {
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEBANCHEQ).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_banco_layout).setVisibility(View.VISIBLE);
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USECUECHEQ).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_numero_cuenta_layout).setVisibility(View.VISIBLE);
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USENUMCHEQ).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_numero_documento_layout).setVisibility(View.VISIBLE);
        } else if(formaPago.getDescripcion().equalsIgnoreCase("Tarjeta Credito"))
        {
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEBANCOTC).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_banco_layout).setVisibility(View.VISIBLE);
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEMARCATC).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_tarjeta_layout).setVisibility(View.VISIBLE);
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USENUMTC).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_numero_documento_layout).setVisibility(View.VISIBLE);
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEAUTORTC).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_autorizador_layout).setVisibility(View.VISIBLE);
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USELOTETC).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_codigo_seguridad_layout).setVisibility(View.VISIBLE);
            if(GeneralValue.getGeneralValue(getDataBase(), Contants.POS_USEDIFERTC).getValue().equalsIgnoreCase("1")) getRootView().findViewById(R.id.pago_tipo_diferido_layout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        createFragmentListener.getNewSaldo(0);
        super.onDismiss(dialog);
    }
}
