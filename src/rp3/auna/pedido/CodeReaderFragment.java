package rp3.auna.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.pedido.PedidoDetalle;
import rp3.auna.models.pedido.Producto;
import rp3.auna.models.pedido.ProductoCodigo;
import rp3.auna.utils.DrawableManager;

/**
 * Created by magno_000 on 08/03/2016.
 */
public class CodeReaderFragment extends BaseFragment {

    private static final String TAG = CodeReaderFragment.class.getSimpleName();
    private DecimalFormat df;
    private NumberFormat numberFormat, numberFormatInteger;
    private ProductCodeAcceptListener createFragmentListener;
    private DrawableManager DManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach...");
        setContentView(R.layout.fragment_code_reader);
        if(getParentFragment()!=null){
            createFragmentListener = (ProductCodeAcceptListener)getParentFragment();
        }else{
            createFragmentListener = (ProductCodeAcceptListener) activity;
        }

    }

    public interface ProductCodeAcceptListener{
        public void onAcceptCodeSuccess(PedidoDetalle transaction);
    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
        Log.d(TAG,"onFragmentResult...");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState...");
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
        df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        numberFormatInteger = NumberFormat.getInstance();
        numberFormatInteger.setMaximumFractionDigits(0);
        numberFormatInteger.setMinimumFractionDigits(0);
        DManager = new DrawableManager();

        //Para ingreso manual de codigo
        ((EditText)rootView.findViewById(R.id.code_to_read)).setFocusableInTouchMode(true);
        rootView.findViewById(R.id.code_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((EditText)rootView.findViewById(R.id.code_to_read)).length() > 0) {
                    findProduct(((EditText)rootView.findViewById(R.id.code_to_read)).getText().toString());
                    ((EditText)rootView.findViewById(R.id.code_to_read)).setText("");
                }
                else
                {
                    Toast.makeText(getContext(), "Ingrese un código.", Toast.LENGTH_LONG).show();
                }
            }
        });

        ((EditText)rootView.findViewById(R.id.code_to_read)).requestFocus();
        ((EditText)rootView.findViewById(R.id.code_to_read)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event != null)
                    if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        findProduct(v.getText().toString());
                        v.setText("");
                        v.requestFocus();
                    }
                return false;
            }
        });

    }

    public void findProduct(String codigo)
    {
        Log.d(TAG,"findProduct...");
        ProductoCodigo productoCodigo = ProductoCodigo.getProductoCodigo(getDataBase(), codigo);
        if (productoCodigo == null) {
            getRootView().findViewById(R.id.code_no_product).setVisibility(View.VISIBLE);
            getRootView().findViewById(R.id.code_product).setVisibility(View.GONE);
        } else {
            Producto prod = Producto.getProductoSingleByCodigoExterno(getDataBase(), productoCodigo.getCodigoExterno());
            getRootView().findViewById(R.id.code_no_product).setVisibility(View.GONE);
            getRootView().findViewById(R.id.code_product).setVisibility(View.VISIBLE);
            ((TextView) getRootView().findViewById(R.id.producto_descripcion)).setText("Descripción: " + prod.getDescripcion());
            ((TextView) getRootView().findViewById(R.id.producto_sku)).setText("SKU: " + prod.getCodigoExterno());
            ((TextView) getRootView().findViewById(R.id.producto_precio)).setText("Precio: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(prod.getPrecioImpuesto()));

            ((ImageView) getRootView().findViewById(R.id.producto_imagen)).setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_PRODUCTOS) + prod.getUrlFoto(),
                    (ImageView) getRootView().findViewById(R.id.producto_imagen));

            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setCantidad(1);
            detalle.setDescripcion(prod.getDescripcion());
            detalle.setValorUnitario(prod.getValorUnitario());
            detalle.setIdProducto(prod.getIdProducto());
            detalle.setUrlFoto(prod.getUrlFoto());
            detalle.setSubtotal(detalle.getValorUnitario() * detalle.getCantidad());
            detalle.setPorcentajeDescuentoManual(0);
            detalle.setValorDescuentoManual(0);
            detalle.setValorDescuentoAutomatico(0);
            detalle.setValorDescuentoAutomaticoTotal(0);
            detalle.setValorDescuentoManualTotal(0);
            detalle.setPorcentajeDescuentoAutomatico(prod.getPorcentajeDescuento());
            detalle.setPorcentajeImpuesto(prod.getPorcentajeImpuesto());
            detalle.setValorImpuesto(prod.getPorcentajeImpuesto() * prod.getValorUnitario());
            detalle.setValorImpuestoTotal(detalle.getValorImpuesto() * detalle.getCantidad());
            detalle.setProducto(Producto.getProductoIdServer(getDataBase(), detalle.getIdProducto()));
            detalle.setValorTotal(detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal() + detalle.getValorImpuestoTotal());
            detalle.setBaseImponible(prod.getPorcentajeImpuesto() == 0 ? 0 : detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal());
            detalle.setBaseImponibleCero(prod.getPorcentajeImpuesto() == 0 ? detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal() : 0);
            detalle.setSubtotalSinDescuento(detalle.getSubtotal());
            detalle.setSubtotalSinImpuesto(detalle.getSubtotal() - detalle.getValorDescuentoAutomaticoTotal() - detalle.getValorDescuentoManualTotal());


            createFragmentListener.onAcceptCodeSuccess(detalle);
        }
    }

    /**
     *
     * Ciclo de vida
     *
     */

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
