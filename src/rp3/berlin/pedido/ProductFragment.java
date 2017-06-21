package rp3.berlin.pedido;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.accounts.User;
import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.cliente.SignInFragment;
import rp3.berlin.models.pedido.LibroPrecio;
import rp3.berlin.models.pedido.PedidoDetalle;
import rp3.berlin.models.pedido.Producto;
import rp3.berlin.models.pedido.Vendedor;
import rp3.berlin.sync.Agente;
import rp3.berlin.utils.DrawableManager;

/**
 * Created by magno_000 on 13/10/2015.
 */

public class ProductFragment extends BaseFragment implements SignInFragment.SignConfirmListener{

    public static String ARG_CODE = "Code";

    public final static int DIALOG_DESC = 8;
    private ProductAcceptListener createFragmentListener;
    private JSONObject jsonObject;
    private DrawableManager DManager;
    private double porcentajeDescManual = 0, valorDescManual = 0, valorDescManualTotal = 0, porcentajeDescAuto = 0, valorDescAuto = 0, valorDescAutoTotal = 0;
    private DecimalFormat df;
    private NumberFormat numberFormat, numberFormatInteger, numberFormatDiscount;
    private SignInFragment signInFragment;
    private String usrDescManual;
    private List<LibroPrecio> libroPrecios;

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
        numberFormatInteger.setMaximumFractionDigits(2);
        numberFormatInteger.setMinimumFractionDigits(2);

        numberFormatDiscount = NumberFormat.getInstance();
        numberFormatDiscount.setMaximumFractionDigits(2);
        numberFormatDiscount.setMinimumFractionDigits(2);

        DManager = new DrawableManager();
        try {
        String code = getArguments().getString("Code");
            jsonObject = new JSONObject(code);
            if(!jsonObject.isNull("c"))
                ((EditText)rootView.findViewById(R.id.producto_cantidad)).setText(jsonObject.getString("c"));
            if(!jsonObject.isNull("udm"))
                usrDescManual = jsonObject.getString("udm");
            porcentajeDescAuto = jsonObject.getDouble("pd");
            porcentajeDescAuto = porcentajeDescAuto * 100;
            ((TextView)rootView.findViewById(R.id.producto_aplicacion)).setText(jsonObject.getString("a"));
            ((TextView)rootView.findViewById(R.id.producto_descripcion)).setText(jsonObject.getString("d"));
            ((TextView)rootView.findViewById(R.id.producto_descuento_auto)).setText(numberFormatDiscount.format(porcentajeDescAuto) + "%");
            ((TextView)rootView.findViewById(R.id.producto_sin_desc)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(jsonObject.getDouble("p")));
            ((TextView)rootView.findViewById(R.id.producto_precio)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(jsonObject.getDouble("vd")));
            ((TextView)rootView.findViewById(R.id.producto_precio_final)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " 0");
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
                    try {
                    if (s.length() == 0)
                        porcentajeDescManual = 0;
                    else {
                        porcentajeDescManual = Double.parseDouble(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;
                    }
                    int cantidad = 0;
                    if (((EditText) rootView.findViewById(R.id.producto_cantidad)).length() > 0)
                        cantidad = Integer.parseInt(((EditText) rootView.findViewById(R.id.producto_cantidad)).getText().toString());


                        double precioLibro = evaluatePrecio(cantidad).getPrecio();
                        porcentajeDescAuto = jsonObject.getDouble("pd");
                        double precioDescuento = precioLibro - (precioLibro * porcentajeDescAuto);
                        valorDescAuto = precioLibro * porcentajeDescAuto;
                        valorDescAutoTotal = valorDescAuto * cantidad;
                        valorDescManual = (precioLibro - valorDescAuto) * porcentajeDescManual;
                        double precio_total = cantidad * precioLibro;
                        valorDescManualTotal = valorDescManual * cantidad;
                        precio_total = precio_total - valorDescManualTotal - valorDescAutoTotal;
                        ((TextView)rootView.findViewById(R.id.producto_precio)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precioDescuento));
                        //precio_total = precio_total * (1 + Float.parseFloat(jsonObject.getString("pi")));
                        ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precio_total));
                    } catch (Exception e) {
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
                        ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " 0");
                    else {
                        int cantidad = Integer.parseInt(((EditText) rootView.findViewById(R.id.producto_cantidad)).getText().toString());
                        try {
                        if(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).length() > 0)
                            porcentajeDescManual = Double.parseDouble(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;

                            double precioLibro = evaluatePrecio(cantidad).getPrecio();
                            porcentajeDescAuto = jsonObject.getDouble("pd");
                            double precioDescuento = precioLibro - (precioLibro * porcentajeDescAuto);
                            valorDescAuto = precioLibro * porcentajeDescAuto;
                            valorDescAutoTotal = valorDescAuto * cantidad;
                            valorDescManual = (precioLibro - valorDescAuto) * porcentajeDescManual;
                            double precio_total = cantidad * precioLibro;
                            valorDescManualTotal = valorDescManual * cantidad;
                            precio_total = precio_total - valorDescManualTotal - valorDescAutoTotal;
                            //precio_total = precio_total * (1 + Float.parseFloat(jsonObject.getString("pi")));
                            ((TextView)rootView.findViewById(R.id.producto_precio)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precioDescuento));
                            ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precio_total));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //Instancio precios
            libroPrecios = new ArrayList<>();
            JSONArray jLibros = jsonObject.getJSONArray("lp");
            for(int i = 0; i < jLibros.length(); i ++)
            {
                JSONObject jPrecio = jLibros.getJSONObject(i);
                LibroPrecio libroPrecio = new LibroPrecio();
                libroPrecio.setItem(jPrecio.getString("i"));
                libroPrecio.setPrecio(jPrecio.getDouble("p"));
                libroPrecio.setValorEscalado(jPrecio.getDouble("e"));
                libroPrecio.setFechaEfectiva(new Date(jPrecio.getLong("f")));
                libroPrecios.add(libroPrecio);
            }

            if(jsonObject.isNull("c"))
                ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " 0");
            else {
                int cantidad = Integer.parseInt(((EditText) rootView.findViewById(R.id.producto_cantidad)).getText().toString());
                if(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).length() > 0)
                    porcentajeDescManual = Double.parseDouble(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;
                try {
                    double precioLibro = evaluatePrecio(cantidad).getPrecio();
                    porcentajeDescAuto = jsonObject.getDouble("pd");
                    double precioDescuento = precioLibro - (precioLibro * porcentajeDescAuto);
                    valorDescAuto = precioLibro * porcentajeDescAuto;
                    valorDescAutoTotal = valorDescAuto * cantidad;
                    valorDescManual = (precioLibro - valorDescAuto) * porcentajeDescManual;
                    double precio_total = cantidad * precioLibro;
                    valorDescManualTotal = valorDescManual * cantidad;
                    precio_total = precio_total - valorDescManualTotal - valorDescAutoTotal;
                    //precio_total = precio_total * (1 + Float.parseFloat(jsonObject.getString("pi")));
                    ((TextView)rootView.findViewById(R.id.producto_precio)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precioDescuento));
                    ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("" + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precio_total));
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

        rootView.findViewById(R.id.producto_stock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BodegaFragment bodegaFragment = BodegaFragment.newInstance(jsonObject.getString("ce"));
                    showDialogFragment(bodegaFragment, "Saldos de Bodega", "Saldos de Bodega");
                }
                catch (Exception ex)
                {}
            }
        });

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
                                Double.parseDouble(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString()) > PreferenceManager.getInt(Contants.KEY_DESCUENTO_MAXIMO) && ((jsonObject.isNull("c")) ||
                                        (!jsonObject.isNull("c") && (Integer.parseInt(((EditText) getRootView().findViewById(R.id.producto_cantidad)).getText().toString()) > jsonObject.getInt("c") &&
                                                Double.parseDouble(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString()) >= (jsonObject.getDouble("pdm") * 100)) ||
                                                Double.parseDouble(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString()) > (jsonObject.getDouble("pdm") * 100)))) {
                            if(((Double.parseDouble(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString())) + (porcentajeDescAuto*100)) >= 100)
                            {
                                Toast.makeText(getContext(), "La suma de porcentajes de descuento no puede ser mayor o igual al 100%", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if(jsonObject.getDouble("t") <= ((Double.parseDouble(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString())) + (porcentajeDescAuto*100)))
                            {
                                showDialogConfirmation(DIALOG_DESC, "El descuento ingresado para este producto (" + numberFormatDiscount.format(((Double.parseDouble(((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).getText().toString())) + (porcentajeDescAuto*100))) +
                                        "%), es mayor al que se le puede dar al canal del cliente y su línea de producto (" + numberFormatDiscount.format(jsonObject.getDouble("t"))+ "%);" +
                                        " por ende este pedido será bloqueado hasta que reciba la aprobación de un supervisor. Desea continuar?", "Bloqueo de Pedido");
                                return;
                            }
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

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ((EditText) view).setSelection(((EditText) view).getText().length());
                }
            }
        };

        ((EditText) getRootView().findViewById(R.id.producto_cantidad)).setOnFocusChangeListener(onFocusChangeListener);
        ((EditText) getRootView().findViewById(R.id.producto_descuento_manual)).setOnFocusChangeListener(onFocusChangeListener);
    }

    @Override
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        switch (id)
        {
            case DIALOG_DESC:
                saveDetail();
                break;
            default:
                break;
        }
    }

    private void saveDetail()
    {
        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setCantidad(Integer.parseInt(((EditText) getRootView().findViewById(R.id.producto_cantidad)).getText().toString()));
        try {
            double precioFinal = evaluatePrecio(detalle.getCantidad()).getPrecio();
            detalle.setDescripcion(jsonObject.getString("d"));
            detalle.setValorUnitario(precioFinal);
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
            detalle.setValorImpuesto(Float.parseFloat(jsonObject.getString("pi")) * (precioFinal - valorDescManual - valorDescAuto));
            detalle.setValorImpuestoTotal(detalle.getValorImpuesto() * detalle.getCantidad());
            detalle.setProducto(Producto.getProductoIdServer(getDataBase(), detalle.getIdProducto()));
            detalle.setValorTotal(detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal() + detalle.getValorImpuestoTotal());
            detalle.setBaseImponible(jsonObject.getDouble("pi") == 0 ? 0 : detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal());
            detalle.setBaseImponibleCero(jsonObject.getDouble("pi") == 0 ? detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal() : 0);
            detalle.setSubtotalSinDescuento(detalle.getSubtotal());
            detalle.setSubtotalSinImpuesto(detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal());
            detalle.setIdBeneficio(jsonObject.getInt("ib"));
            detalle.setIdVendedor(detalle.getProducto().getLinea());
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

    public LibroPrecio evaluatePrecio(int cantidad)
    {
        LibroPrecio resp = null;
        for(LibroPrecio libroPrecio : libroPrecios)
        {
            if(resp == null && libroPrecio.getValorEscalado() <= cantidad)
                resp = libroPrecio;
            else
            {
                if(libroPrecio.getValorEscalado() <= cantidad && libroPrecio.getFechaEfectiva().getTime() > resp.getFechaEfectiva().getTime())
                    if(resp.getValorEscalado() <= libroPrecio.getValorEscalado())
                        resp = libroPrecio;
            }
        }
        return resp;
    }
}