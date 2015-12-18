package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.marketforce.models.pedido.Producto;
import rp3.marketforce.utils.DrawableManager;

/**
 * Created by magno_000 on 16/12/2015.
 */
public class PagosListFragment extends BaseFragment implements AgregarPagoFragment.PagoAgregarListener{

    public static final String ARG_VALOR = "valor";

    private PagosAcceptListener createFragmentListener;
    private PagoAdapter adapter;
    public List<Pago> pagos;
    private NumberFormat numberFormat;
    double valorTotal = 0;
    double saldo = 0;

    public static PagosListFragment newInstance(double valorTotal)
    {
        PagosListFragment fragment = new PagosListFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_VALOR, valorTotal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setContentView(R.layout.fragment_forma_pago);
        if(getParentFragment()!=null){
            createFragmentListener = (PagosAcceptListener)getParentFragment();
        }else{
            createFragmentListener = (PagosAcceptListener) activity;
        }
        setCancelable(false);

    }

    @Override
    public void onAcceptSuccess(Pago pago) {
        pagos.add(pago);
        saldo = valorTotal;

        for(Pago pag : pagos)
        {
            saldo = saldo - pag.getValor();
        }

        ((TextView) getRootView().findViewById(R.id.forma_pago_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(saldo));
    }

    public interface PagosAcceptListener{
        public void onAcceptSuccess(List<Pago> pagos);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        if(pagos == null)
            pagos = new ArrayList<Pago>();

        adapter = new PagoAdapter(this.getContext(), pagos);
        ((ListView) rootView.findViewById(R.id.forma_pago_list)).setAdapter(adapter);

        saldo = getArguments().getDouble(ARG_VALOR);
        valorTotal = saldo;

        for(Pago pago : pagos)
        {
            saldo = saldo - pago.getValor();
        }

        ((TextView) rootView.findViewById(R.id.forma_pago_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(saldo));

        rootView.findViewById(R.id.forma_pago_agregar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarPagoFragment fragment = AgregarPagoFragment.newInstance(saldo);
                showDialogFragment(fragment, "Pago", "Pago # " + (pagos.size() + 1));
            }
        });

        rootView.findViewById(R.id.forma_pago_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragmentListener.onAcceptSuccess(pagos);
                dismiss();
            }
        });

        if(pagos.size() == 0)
        {
            AgregarPagoFragment fragment = AgregarPagoFragment.newInstance(saldo);
            showDialogFragment(fragment, "Pago", "Pago # " + (pagos.size() + 1));
        }

    }
}
