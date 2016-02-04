package rp3.marketforce.pedido;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.utils.DrawableManager;

/**
 * Created by magno_000 on 13/10/2015.
 */

public class ProductFragment extends BaseFragment {

    public static String ARG_CODE = "Code";
    private ProductAcceptListener createFragmentListener;
    private JSONObject jsonObject;
    private DrawableManager DManager;
    private double porcentajeDescManual = 0, valorDescManual = 0, valorDescManualTotal = 0;
    private DecimalFormat df;
    private NumberFormat numberFormat, numberFormatInteger;

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
            ((TextView)rootView.findViewById(R.id.producto_descripcion)).setText("Descripción: " + jsonObject.getString("d"));
            ((TextView)rootView.findViewById(R.id.producto_precio)).setText("Precio: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(jsonObject.getDouble("vi")));
            ((TextView)rootView.findViewById(R.id.producto_precio_final)).setText("Precio Total: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " 0");
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
                        porcentajeDescManual = Float.parseFloat(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;
                    }
                    int cantidad = 0;
                    if (((EditText) rootView.findViewById(R.id.producto_cantidad)).length() > 0)
                        cantidad = Integer.parseInt(((EditText) rootView.findViewById(R.id.producto_cantidad)).getText().toString());

                    try {
                        valorDescManual = jsonObject.getDouble("vi") * porcentajeDescManual;
                        double precio_total = cantidad * jsonObject.getDouble("vi");
                        valorDescManualTotal = valorDescManual * cantidad;
                        precio_total = precio_total - valorDescManualTotal;
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
                            porcentajeDescManual = Float.parseFloat(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;
                        try {
                            valorDescManual = jsonObject.getDouble("vi") * porcentajeDescManual;
                            double precio_total = cantidad * jsonObject.getDouble("vi");
                            valorDescManualTotal = valorDescManual * cantidad;
                            precio_total = precio_total - valorDescManualTotal;
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
                    porcentajeDescManual = Float.parseFloat(((EditText) rootView.findViewById(R.id.producto_descuento_manual)).getText().toString()) / 100;
                try {
                    valorDescManual = jsonObject.getDouble("vi") * porcentajeDescManual;
                    double precio_total = cantidad * jsonObject.getDouble("vi");
                    valorDescManualTotal = valorDescManual * cantidad;
                    precio_total = precio_total - valorDescManualTotal;
                    ((TextView) rootView.findViewById(R.id.producto_precio_final)).setText("Precio Total: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(precio_total));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                if(((EditText) rootView.findViewById(R.id.producto_cantidad)).length() > 0) {
                    PedidoDetalle detalle = new PedidoDetalle();
                    detalle.setCantidad(Integer.parseInt(((EditText) rootView.findViewById(R.id.producto_cantidad)).getText().toString()));
                    try {
                        detalle.setDescripcion(jsonObject.getString("d"));
                        detalle.setValorUnitario(jsonObject.getDouble("p"));
                        detalle.setIdProducto(jsonObject.getInt("id"));
                        detalle.setUrlFoto(jsonObject.getString("f"));
                        detalle.setValorTotal((detalle.getCantidad() * Float.parseFloat(jsonObject.getString("vi"))) - valorDescManualTotal);
                        detalle.setSubtotal(detalle.getValorUnitario() * detalle.getCantidad());
                        detalle.setPorcentajeDescuentoManual(porcentajeDescManual);
                        detalle.setValorDescuentoManual(valorDescManual);
                        detalle.setValorDescuentoAutomatico(Float.parseFloat(jsonObject.getString("vd")) - Float.parseFloat(jsonObject.getString("vi")));
                        detalle.setValorDescuentoAutomaticoTotal(detalle.getValorDescuentoAutomatico() * detalle.getCantidad());
                        detalle.setValorDescuentoManualTotal(valorDescManualTotal);
                        detalle.setPorcentajeDescuentoAutomatico(Float.parseFloat(jsonObject.getString("pd")));
                        detalle.setPorcentajeImpuesto(Float.parseFloat(jsonObject.getString("pi")));
                        detalle.setValorImpuesto(Float.parseFloat(jsonObject.getString("vi")) - Float.parseFloat(jsonObject.getString("p")));
                        detalle.setValorImpuestoTotal(detalle.getValorImpuesto() * detalle.getCantidad());
                        detalle.setBaseImponible(jsonObject.getDouble("pi") == 0 ? 0 : detalle.getValorTotal());
                        detalle.setBaseImponibleCero(jsonObject.getDouble("pi") == 0 ? detalle.getValorTotal() : 0);
                        detalle.setProducto(Producto.getProductoIdServer(getDataBase(), detalle.getIdProducto()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    createFragmentListener.onAcceptSuccess(detalle);
                    dismiss();
                }
                else
                {
                    Toast.makeText(getContext(), "Ingrese una cantidad", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
