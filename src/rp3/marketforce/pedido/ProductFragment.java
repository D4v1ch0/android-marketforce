package rp3.marketforce.pedido;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import rp3.accounts.User;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.data.Identifiable;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.SignInFragment;
import rp3.marketforce.models.TipoCliente;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.models.pedido.Vendedor;
import rp3.marketforce.sync.Agente;
import rp3.marketforce.utils.DrawableManager;

/**
 * Created by magno_000 on 13/10/2015.
 */

public class ProductFragment extends BaseFragment implements SignInFragment.SignConfirmListener{

    public static String ARG_CODE = "Code";
    private ProductAcceptListener createFragmentListener;
    private JSONObject jsonObject;
    private DrawableManager DManager;
    private double porcentajeDescManual = 0, valorDescManual = 0, valorDescManualTotal = 0, porcentajeDescAuto = 0, valorDescAuto = 0, valorDescAutoTotal = 0;
    private DecimalFormat df;
    private NumberFormat numberFormat, numberFormatInteger;
    private SignInFragment signInFragment;
    private String usrDescManual;

    public static ProductFragment newInstance(String jcode)
    {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CODE, jcode);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setContentView(R.layout.fragment_producto);
        if(getParentFragment()!=null){
            createFragmentListener = (ProductAcceptListener)getParentFragment();
        }else{
            createFragmentListener = (ProductAcceptListener) activity;
        }

    }

    @Override
    public void onSignSuccess(Bundle bundle) {
        if(bundle.getInt(Agente.KEY_DESCUENTO) >= Integer.parseInt(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString())) {
            usrDescManual = bundle.getString(SignInFragment.ARG_USER);
            Toast.makeText(getContext(), "Usuario Autorizado.", Toast.LENGTH_LONG).show();
            saveDetail();
        }
        else
        {
            Toast.makeText(getContext(), "Usuario No Autorizado.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSignError(Bundle bundle) {

    }

    public interface ProductAcceptListener{
        public void onDeleteSuccess(PedidoDetalle transaction);
        public void onAcceptSuccess(PedidoDetalle transaction);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        numberFormatInteger = NumberFormat.getInstance();
        numberFormatInteger.setMaximumFractionDigits(0);
        numberFormatInteger.setMinimumFractionDigits(0);

        DManager = new DrawableManager();
        try {
        String code = getArguments().getString("Code");
            jsonObject = new JSONObject(code);
            if(!jsonObject.isNull("c"))
                ((EditText)rootView.findViewById(R.id.producto_cantidad)).setText(jsonObject.getString("c"));
            if(!jsonObject.isNull("udm"))
                usrDescManual = jsonObject.getString("udm");
            ((TextView)rootView.findViewById(R.id.producto_descripcion)).setText("Descripción: " + jsonObject.getString("d"));
            ((TextView)rootView.findViewById(R.id.producto_precio)).setText("Precio: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(jsonObject.getDouble("vi")));
            ((TextView)rootView.findViewById(R.id.producto_precio_final)).setText("Precio Total: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " 0");
            List<Vendedor> list_vendedores = Vendedor.getVendedores(getDataBase());
            SimpleIdentifiableAdapter vendedores = new SimpleIdentifiableAdapter(getContext(), list_vendedores);
            ((Spinner) rootView.findViewById(R.id.producto_vendedor)).setAdapter(vendedores);
            if(!jsonObject.isNull("ven")) {
                //selecciono el vendedor
                for(int i = 0; i < list_vendedores.size(); i++)
                {
                    if(jsonObject.getString("ven").equalsIgnoreCase(list_vendedores.get(i).getIdVendedor()))
                        ((Spinner) rootView.findViewById(R.id.producto_vendedor)).setSelection(i);
                }
            }
            else
            {
                //selecciono al usuario del dispositivo
                for(int i = 0; i < list_vendedores.size(); i++)
                {
                    if(User.getCurrentUser(this.getContext()).getLogonName().equalsIgnoreCase(list_vendedores.get(i).getIdVendedor()))
                        ((Spinner) rootView.findViewById(R.id.producto_vendedor)).setSelection(i);
                }
            }
            DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_PRODUCTOS) + jsonObject.getString("f"),
                    (ImageView) rootView.findViewById(R.id.producto_imagen));
            if(!jsonObject.isNull("pdm"))
                ((EditText)rootView.findViewById(R.id.producto_descuento_manual)).setText(numberFormatInteger.format(jsonObject.getDouble("pdm") * 100));
            ((EditText)rootView.findViewById(R.id.producto_descuento_manual)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0)
                        porcentajeDescManual = 0;
                    else {
                        porcentajeDescManual = Double.parseDouble(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;
                    }
                    int cantidad = 0;
                    if (((EditText) rootView.findViewById(R.id.producto_cantidad)).length() > 0)
                        cantidad = Integer.parseInt(((EditText) rootView.findViewById(R.id.producto_cantidad)).getText().toString());

                    try {
                        porcentajeDescAuto = jsonObject.getDouble("pd");
                        valorDescAuto = jsonObject.getDouble("p") * porcentajeDescAuto;
                        valorDescAutoTotal = valorDescAuto * cantidad;
                        valorDescManual = (jsonObject.getDouble("p") - valorDescAuto) * porcentajeDescManual;
                        double precio_total = cantidad * jsonObject.getDouble("p");
                        valorDescManualTotal = valorDescManual * cantidad;
                        precio_total = precio_total - valorDescManualTotal - valorDescAutoTotal;
                        precio_total = precio_total * (1 + Float.parseFloat(jsonObject.getString("pi")));
                        ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("Precio Total: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precio_total));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            ((EditText)rootView.findViewById(R.id.producto_cantidad)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0)
                        ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("Precio Total: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " 0");
                    else {
                        int cantidad = Integer.parseInt(((EditText) rootView.findViewById(R.id.producto_cantidad)).getText().toString());
                        if(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).length() > 0)
                            porcentajeDescManual = Double.parseDouble(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;
                        try {
                            porcentajeDescAuto = jsonObject.getDouble("pd");
                            valorDescAuto = jsonObject.getDouble("p") * porcentajeDescAuto;
                            valorDescAutoTotal = valorDescAuto * cantidad;
                            valorDescManual = (jsonObject.getDouble("p") - valorDescAuto) * porcentajeDescManual;
                            double precio_total = cantidad * jsonObject.getDouble("p");
                            valorDescManualTotal = valorDescManual * cantidad;
                            precio_total = precio_total - valorDescManualTotal - valorDescAutoTotal;
                            precio_total = precio_total * (1 + Float.parseFloat(jsonObject.getString("pi")));
                            ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("Precio Total: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precio_total));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            //jsonObject.put("c", prod.getCodigoExterno());
            //jsonObject.put("b", prod.getIdBeneficio());
            //jsonObject.put("pd", prod.getPorcentajeDescuento());
            //jsonObject.put("pi", prod.getPorcentajeImpuesto());
            //jsonObject.put("vd", prod.getPrecioDescuento());
            //jsonObject.put("vi", prod.getPrecioImpuesto());

            if(jsonObject.isNull("c"))
                ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("Precio Total: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " 0");
            else {
                int cantidad = Integer.parseInt(((EditText) rootView.findViewById(R.id.producto_cantidad)).getText().toString());
                if(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).length() > 0)
                    porcentajeDescManual = Double.parseDouble(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;
                try {
                    porcentajeDescAuto = jsonObject.getDouble("pd");
                    valorDescAuto = jsonObject.getDouble("p") * porcentajeDescAuto;
                    valorDescAutoTotal = valorDescAuto * cantidad;
                    valorDescManual = (jsonObject.getDouble("p") - valorDescAuto) * porcentajeDescManual;
                    double precio_total = cantidad * jsonObject.getDouble("p");
                    valorDescManualTotal = valorDescManual * cantidad;
                    precio_total = precio_total - valorDescManualTotal - valorDescAutoTotal;
                    precio_total = precio_total * (1 + Float.parseFloat(jsonObject.getString("pi")));
                    ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("Precio Total: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precio_total));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Validaciones si es que tipo de documento es nota de credito
            if(!jsonObject.isNull("tipo") && jsonObject.getString("tipo").equalsIgnoreCase("NC"))
            {
                ((EditText)rootView.findViewById(R.id.producto_descuento_manual)).setEnabled(false);
                rootView.findViewById(R.id.producto_vendedor).setEnabled(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(), "Código Inválido.", Toast.LENGTH_LONG).show();
            dismiss();
        }

        rootView.findViewById(R.id.producto_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        rootView.findViewById(R.id.producto_eliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PedidoDetalle detalle = new PedidoDetalle();
                try {
                    detalle.setDescripcion(jsonObject.getString("d"));
                    detalle.setValorUnitario(jsonObject.getDouble("p"));
                    detalle.setIdProducto(jsonObject.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                createFragmentListener.onDeleteSuccess(detalle);
                dismiss();

            }
        });
        rootView.findViewById(R.id.producto_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (((EditText) getRootView().findViewById(R.id.producto_cantidad)).length() > 0 && !((EditText) getRootView().findViewById(R.id.producto_cantidad)).getText().toString().equalsIgnoreCase("0")) {
                        if (((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).length() > 0 && (jsonObject.isNull("tipo") || !jsonObject.getString("tipo").equalsIgnoreCase("NC")) &&
                                Integer.parseInt(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString()) > PreferenceManager.getInt(Contants.KEY_DESCUENTO_MAXIMO) && ((jsonObject.isNull("c")) ||
                                        (!jsonObject.isNull("c") && (Integer.parseInt(((EditText) getRootView().findViewById(R.id.producto_cantidad)).getText().toString()) > jsonObject.getInt("c") &&
                                                Integer.parseInt(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString()) >= (jsonObject.getDouble("pdm") * 100)) ||
                                                Integer.parseInt(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString()) > (jsonObject.getDouble("pdm") * 100)))) {
                            //Toast.makeText(getContext(), "Su máximo descuento permitido es del " + PreferenceManager.getInt(Contants.KEY_DESCUENTO_MAXIMO) + "%.", Toast.LENGTH_LONG).show();
                            signInFragment = new SignInFragment();
                            signInFragment.type = "AuthDesc";
                            showDialogFragment(signInFragment, "Autorizar Descuento", "Autorizar Descuento");
                            return;
                        }
                        saveDetail();
                    } else {
                        Toast.makeText(getContext(), "Ingrese una cantidad", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getContext(), "Hubo un error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveDetail()
    {
        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setCantidad(Integer.parseInt(((EditText) getRootView().findViewById(R.id.producto_cantidad)).getText().toString()));
        try {
            detalle.setDescripcion(jsonObject.getString("d"));
            detalle.setValorUnitario(jsonObject.getDouble("p"));
            detalle.setIdProducto(jsonObject.getInt("id"));
            detalle.setUrlFoto(jsonObject.getString("f"));
            detalle.setSubtotal(detalle.getValorUnitario() * detalle.getCantidad());
            detalle.setPorcentajeDescuentoOro(Double.parseDouble(jsonObject.getString("pdo")));
            detalle.setPorcentajeDescuentoManual(porcentajeDescManual);
            detalle.setValorDescuentoManual(valorDescManual);
            detalle.setValorDescuentoAutomatico(valorDescAuto);
            detalle.setValorDescuentoAutomaticoTotal(valorDescAutoTotal);
            detalle.setValorDescuentoManualTotal(valorDescManualTotal);
            detalle.setPorcentajeDescuentoAutomatico(porcentajeDescAuto);
            detalle.setPorcentajeImpuesto(Float.parseFloat(jsonObject.getString("pi")));
            detalle.setValorImpuesto(Float.parseFloat(jsonObject.getString("pi")) * (Float.parseFloat(jsonObject.getString("p")) - valorDescManual - valorDescAuto));
            detalle.setValorImpuestoTotal(detalle.getValorImpuesto() * detalle.getCantidad());
            detalle.setProducto(Producto.getProductoIdServer(getDataBase(), detalle.getIdProducto()));
            detalle.setValorTotal(detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal() + detalle.getValorImpuestoTotal());
            detalle.setBaseImponible(jsonObject.getDouble("pi") == 0 ? 0 : detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal());
            detalle.setBaseImponibleCero(jsonObject.getDouble("pi") == 0 ? detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal() : 0);
            detalle.setSubtotalSinDescuento(detalle.getSubtotal());
            detalle.setSubtotalSinImpuesto(detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal());
            detalle.setIdBeneficio(jsonObject.getInt("ib"));
            detalle.setIdVendedor(((String) ((Identifiable) ((Spinner) getRootView().findViewById(R.id.producto_vendedor)).getAdapter().getItem(((Spinner) getRootView().findViewById(R.id.producto_vendedor)).getSelectedItemPosition())).getValue("")));
            if(usrDescManual != null && !usrDescManual.equalsIgnoreCase(""))
                detalle.setUsrDescManual(usrDescManual);

            //Validaciones si es que tipo de documento es nota de credito
            if(!jsonObject.isNull("tipo") && jsonObject.getString("tipo").equalsIgnoreCase("NC"))
            {
                detalle.setCantidadOriginal(jsonObject.getInt("co"));
                if(detalle.getCantidad() > jsonObject.getInt("co"))
                {
                    Toast.makeText(getContext(), "No puede devolver mas unidades de lo que se facturó.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        createFragmentListener.onAcceptSuccess(detalle);
        dismiss();
    }
}
