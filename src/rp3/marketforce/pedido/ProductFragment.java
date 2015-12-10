package rp3.marketforce.pedido;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.utils.DrawableManager;

/**
 * Created by magno_000 on 13/10/2015.
 */

public class ProductFragment extends BaseFragment {

    public static String ARG_CODE = "Code";
    private ProductAcceptListener createFragmentListener;
    private JSONObject jsonObject;
    private DrawableManager DManager;

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

        DManager = new DrawableManager();
        try {
        String code = getArguments().getString("Code");
            jsonObject = new JSONObject(code);
            if(!jsonObject.isNull("c"))
                ((EditText)rootView.findViewById(R.id.producto_cantidad)).setText(jsonObject.getString("c"));
            ((TextView)rootView.findViewById(R.id.producto_descripcion)).setText("Descripción: " + jsonObject.getString("d"));
            ((TextView)rootView.findViewById(R.id.producto_precio)).setText("Precio: $ " + jsonObject.getString("p"));
            DManager.fetchDrawableOnThread(PreferenceManager.getString("server") +
                            rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_PRODUCTOS) + jsonObject.getString("f"),
                    (ImageView) rootView.findViewById(R.id.producto_imagen));
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
                        detalle.setValorTotal(detalle.getValorUnitario() * detalle.getCantidad());
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
