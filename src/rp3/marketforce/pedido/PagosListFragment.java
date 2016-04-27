package rp3.marketforce.pedido;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
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
    private AgregarPagoFragment fragment;
    public boolean isDetail = false;

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
        if(!isDetail) {
            if (getParentFragment() != null) {
                createFragmentListener = (PagosAcceptListener) getParentFragment();
            } else {
                createFragmentListener = (PagosAcceptListener) activity;
            }
        }
        setCancelable(isDetail);

    }

    @Override
    public void onAcceptSuccess(Pago pago) {
        if(pago.getIdPago() == -1)
            pagos.add(pago);
        else
            pagos.set(pago.getIdPago(), pago);

        saldo = valorTotal;

        for(Pago pag : pagos)
        {
            saldo = saldo - pag.getValor();
        }

        adapter.notifyDataSetChanged();
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
        if(!isDetail) {
            ((ListView) rootView.findViewById(R.id.forma_pago_list)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    PopupMenu popup = new PopupMenu(getContext(), view);

                    popup.getMenuInflater()
                            .inflate(R.menu.list_item_transaccion_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.item_menu_modificar_transaccion:
                                    boolean efectivo = false;
                                    if (!pagos.get(position).getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo")) {
                                        for (Pago pago : pagos) {
                                            if (pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo"))
                                                efectivo = true;
                                        }
                                    }
                                    AgregarPagoFragment fragment = AgregarPagoFragment.newInstance(position, saldo, efectivo, pagos.get(position));
                                    showDialogFragment(fragment, "Pago", "Pago # " + (position + 1));
                                    break;
                                case R.id.item_menu_eliminar_transaccion:
                                    pagos.remove(position);
                                    adapter = new PagoAdapter(getContext(), pagos);
                                    ((ListView) rootView.findViewById(R.id.forma_pago_list)).setAdapter(adapter);
                                    saldo = valorTotal;

                                    for (Pago pag : pagos) {
                                        saldo = saldo - pag.getValor();
                                    }

                                    ((TextView) getRootView().findViewById(R.id.forma_pago_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(saldo));
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                    return false;
                }
            });
        }

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
                boolean efectivo = false;
                for(Pago pago : pagos)
                {
                    if(pago.getFormaPago().getDescripcion().equalsIgnoreCase("Efectivo"))
                        efectivo = true;
                }
                fragment = AgregarPagoFragment.newInstance(saldo, efectivo);
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

        if(isDetail)
        {
            rootView.findViewById(R.id.forma_pago_aceptar).setVisibility(View.GONE);
            rootView.findViewById(R.id.forma_pago_agregar).setVisibility(View.GONE);
        }

        if(pagos.size() == 0 && !isDetail)
        {
            fragment = AgregarPagoFragment.newInstance(saldo, false);
            showDialogFragment(fragment, "Pago", "Pago # " + (pagos.size() + 1));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AgregarPagoFragment.REQ_CODE_SPEECH_INPUT_PAGO:
                if (resultCode == RESULT_OK && null != data) {
                    if (fragment != null)
                        fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }

    }
}
