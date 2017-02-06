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
public class AgregarTarjetaFragment extends BaseFragment implements HTTPRequestInterface {

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
                    dismiss();
                }
            }});

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

    private boolean ValidarAlignet(ClienteTarjeta tarjeta)
    {
        /*try {
        JSONObject jsonObjectAuthorize = new JSONObject();
        jsonObjectAuthorize.put("idAcquirer", Contants.AUNA_ID_ADQUIRER);
        jsonObjectAuthorize.put("idEntCommerce", Contants.AUNA_ID_WALLET);
        jsonObjectAuthorize.put("tokken", this.consultResponse.getTokken());
        jsonObjectAuthorize.put("typeOperation", PGPUtils.encrypt("1", this.getContext()));
        jsonObjectAuthorize.put("codAsoCardHolderWallet", this.consultResponse.getCodAsoCardHolderWallet());
        jsonObjectAuthorize.put("codCardHolderCommerce", PGPUtils.encrypt(this.transactionInformation.getCodCardHolderCommerce(), this));
        jsonObjectAuthorize.put("registerCard", registerCard);
        //jsonObjectAuthorize.put("additionalObservations", PGPUtils.encrypt("-", this.getContext()));
        //jsonObjectAuthorize.put("billingData", arrDicBilling);
        jsonObjectAuthorize.put("shippingData", arrDicShipping1);
        jsonObjectAuthorize.put("purchaseData", arrDicPurchase2);
        jsonObjectAuthorize.put("taxes", arrDicTaxes3);
        jsonObjectAuthorize.put("products", arrDicProducts4);
        jsonObjectAuthorize.put("reservedFields", new JSONArray());
        jsonObjectAuthorize.put("typeCVV2", typeCVV2);
        jsonObjectAuthorize.put("CVV2", CVV2);
        jsonObjectAuthorize.put("expiryDate", expiryDate);
        jsonObjectAuthorize.put("typeOperationCard", typeOperationCard);
        jsonObjectAuthorize.put("idCard", idCard);
        jsonObjectAuthorize.put("cardNumberMask", cardNumberMask);
        jsonObjectAuthorize.put("cardNumber", cardNumber);
        jsonObjectAuthorize.put("brand", brand);
        jsonObjectAuthorize.put("macAddress", PGPUtils.encrypt(macAddress2, this));
        jsonObjectAuthorize.put("latitude", PGPUtils.encrypt(latitude1, this));
        jsonObjectAuthorize.put("longitude", PGPUtils.encrypt(longitude1, this));
        jsonObjectAuthorize.put("nameDevice", PGPUtils.encrypt(nameDevice1, this));
        jsonObjectAuthorize.put("modelDevice", PGPUtils.encrypt(modelDevice, this));
        Services.getInstance(this.getContext()).ws_authorize(this, jsonObjectAuthorize);
        }catch (Exception ex)
        {

        }*/
        return false;
    }

    @Override
    public void onResponse(JSONObject response, int identifierWS) {
        String ansDescription;
        if(identifierWS == 2) {
            try {
                //final Card e = (Card)this.arrCards.get(this.indexCard);
                AuthorizeResponse ansCode = ParseServices.getInstance().parseAuthorize(response, this.getContext());
                ansDescription = ansCode.getErrorCode();
                String errorMessage = ansCode.getErrorMessage();
                if(!ansDescription.equalsIgnoreCase("999") && !ansDescription.equalsIgnoreCase("001") && !ansDescription.equalsIgnoreCase("022")) {
                    ansDescription = PGPUtils.decrypt(ansCode.getErrorCode(), "", this.getContext());
                    errorMessage = PGPUtils.decrypt(ansCode.getErrorMessage(), "", this.getContext());
                } else {
                    ansDescription = ansCode.getErrorCode();
                    errorMessage = ansCode.getErrorMessage();
                }

                String date = PGPUtils.decrypt(ansCode.getDate(), "", this.getContext());
                String hour = PGPUtils.decrypt(ansCode.getHour(), "", this.getContext());
                PayMeResponse payMeResponse = new PayMeResponse();
                payMeResponse.setErrorCode(ansDescription);
                payMeResponse.setErrorMessage(errorMessage);
                payMeResponse.setDate(date);
                payMeResponse.setHour(hour);
                //payMeResponse.setCodAsoCardHolderWallet(PGPUtils.decrypt(this.consultResponse.getCodAsoCardHolderWallet(), "", this));
                String idTransaction;
                if(ansDescription.equalsIgnoreCase("000")) {
                    idTransaction = ansCode.getCardNumber();
                    String i = ansCode.getAuthorizedAmount();
                    final String CVV2mask = ansCode.getCVV2mask();
                    final String idCard = ansCode.getIdCard();
                    String operationNumber = ansCode.getOperationNumber();
                    String idTransaction1 = ansCode.getIdTransaction();
                    payMeResponse.setOperationNumber(operationNumber);
                    payMeResponse.setIdTransaction(idTransaction1);
                    payMeResponse.setCardNumber(idTransaction);
                    Handler handler = new Handler();
                    /*Runnable runnable = new Runnable() {
                        public void run() {
                            if(e.isNew()) {
                                e.setIdCard(idCard);
                                CardDAO.getInstance().insertCard(e, e.getExpiryDate(), CVV2mask, false);
                            } else if(e.isEdited()) {
                                CardDAO.getInstance().updateCard(e, CVV2mask);
                            }

                            CardDAO.getInstance().updateLastUserCard(e, true);
                        }
                    };
                    handler.post(runnable);*/
                    //PAGO EXITOSO
                    //this.animatePayMeEndAuthorize(true, false, payMeResponse, this.getString(pe.solera.api_payme_android.R.string.buying_processed));
                } else {
                    //PAGO FALLIDO
                    //this.animatePayMeEndAuthorize(false, false, payMeResponse, this.getString(pe.solera.api_payme_android.R.string.buying_unprocessed));
                }
            } catch (Exception var19) {
                var19.printStackTrace();
                //PAGO FALLIDO
                //this.animatePayMeEndAuthorize(false, false, (PayMeResponse)null, this.getString(pe.solera.api_payme_android.R.string.buying_unprocessed));
            }
        } /*else if(identifierWS == 1) {
            try {
                ConsultResponse var20 = ParseServices.getInstance().parseConsult(response, this);
                String var21 = var20.getAnsCode();
                ansDescription = var20.getAnsDescription();
                if(var21.equalsIgnoreCase("999")) {
                    var21 = var20.getAnsCode();
                    ansDescription = var20.getAnsDescription();
                } else {
                    var21 = PGPUtils.decrypt(var20.getAnsCode(), "", this);
                    ansDescription = PGPUtils.decrypt(var20.getAnsDescription(), "", this);
                }

                Log.d(this.TAG, "ansCode: " + var21);
                Log.d(this.TAG, "ansDescription: " + ansDescription);
                if(var21.equalsIgnoreCase("000")) {
                    this.consultResponse.setTokken(var20.getTokken());
                    this.consultResponse.setCodAsoCardHolderWallet(var20.getCodAsoCardHolderWallet());
                    this.lblMessage.setTextColor(this.getResources().getColor(pe.solera.api_payme_android.R.color.redValidation));
                    this.lblMessage.setText(pe.solera.api_payme_android.R.string.try_again_tokken);
                } else {
                    this.lblMessage.setTextColor(this.getResources().getColor(pe.solera.api_payme_android.R.color.redValidation));
                    this.lblMessage.setText(this.getResources().getString(pe.solera.api_payme_android.R.string.generic_problem));
                    this.runnableGoCommerce(false, (PayMeResponse)null);
                }
            } catch (Exception var18) {
                var18.printStackTrace();
            }
        }*/
    }

    @Override
    public void onErrorResponse(VolleyError volleyError, int identifierWS) {
        if(identifierWS == 2) {
            //CONEXIÓN FALLIDA
            //this.animatePayMeEndAuthorize(false, false, (PayMeResponse)null, this.getString(pe.solera.api_payme_android.R.string.buying_unprocessed));
        } /*else if(identifierWS == 1) {
            this.lblMessage.setTextColor(this.getResources().getColor(pe.solera.api_payme_android.R.color.redValidation));
            this.lblMessage.setText(this.getResources().getString(pe.solera.api_payme_android.R.string.generic_problem));
            this.runnableGoCommerce(false, (PayMeResponse)null);
        }*/
    }

}