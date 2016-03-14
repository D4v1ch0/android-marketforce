package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.models.pedido.ProductoCodigo;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.DrawableManager;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 08/03/2016.
 */
public class CodeReaderFragment extends BaseFragment {

    private DecimalFormat df;
    private NumberFormat numberFormat, numberFormatInteger;
    private ProductCodeAcceptListener createFragmentListener;
    private DrawableManager DManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

        ((EditText)rootView.findViewById(R.id.code_to_read)).requestFocus();
        ((EditText)rootView.findViewById(R.id.code_to_read)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    ProductoCodigo productoCodigo = ProductoCodigo.getProductoCodigo(getDataBase(), v.getText().toString());
                    if (productoCodigo == null) {
                        rootView.findViewById(R.id.code_no_product).setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.code_product).setVisibility(View.GONE);
                    } else {
                        Producto prod = Producto.getProductoSingleByCodigoExterno(getDataBase(), productoCodigo.getCodigoExterno());
                        rootView.findViewById(R.id.code_no_product).setVisibility(View.GONE);
                        rootView.findViewById(R.id.code_product).setVisibility(View.VISIBLE);
                        ((TextView) rootView.findViewById(R.id.producto_descripcion)).setText("Descripción: " + prod.getDescripcion());
                        ((TextView) rootView.findViewById(R.id.producto_sku)).setText("SKU: " + prod.getCodigoExterno());
                        ((TextView) rootView.findViewById(R.id.producto_precio)).setText("Precio: " + PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(prod.getPrecioImpuesto()));

                        DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                                        rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_PRODUCTOS) + prod.getUrlFoto(),
                                (ImageView) rootView.findViewById(R.id.producto_imagen));

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


                        createFragmentListener.onAcceptCodeSuccess(detalle);
                    }
                    v.setText("");
                }
                return false;
            }
        });
    }
}
