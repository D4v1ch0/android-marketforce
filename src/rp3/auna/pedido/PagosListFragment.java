package rp3.auna.pedido;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.pedido.Pago;

/**
 * Created by magno_000 on 16/12/2015.
 */
public class PagosListFragment extends BaseFragment implements AgregarPagoFragment.PagoAgregarListener{

    private static final String TAG = PagosListFragment.class.getSimpleName();
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
        Log.d(TAG,"onCreate...");
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach...");
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
        Log.d(TAG,"onAcceptSuccess...");
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

    @Override
    public double getNewSaldo(int idPago) {
        List<Integer> idsFormaPago = new ArrayList<Integer>();
        for(Pago pago : pagos)
            idsFormaPago.add(pago.getIdFormaPago());
        idsFormaPago.add(idPago);
        valorTotal = createFragmentListener.getNewSaldo(idsFormaPago);
        saldo = valorTotal;

        for(Pago pag : pagos)
        {
            saldo = saldo - pag.getValor();
        }

        ((TextView) getRootView().findViewById(R.id.forma_pago_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(saldo));
        return saldo;
    }

    @Override
    public double getNewSaldoUpdate(int idFormaPago, int idPago) {
        List<Integer> idsFormaPago = new ArrayList<Integer>();
        int pos = 0;
        for(Pago pago : pagos) {
            if(pos != idPago)
                idsFormaPago.add(pago.getIdFormaPago());
            pos++;
        }
        idsFormaPago.add(idFormaPago);
        valorTotal = createFragmentListener.getNewSaldo(idsFormaPago);
        saldo = valorTotal;

        pos = 0;
        for(Pago pag : pagos)
        {
            if(pos != idPago)
                saldo = saldo - pag.getValor();
            pos++;
        }

        ((TextView) getRootView().findViewById(R.id.forma_pago_saldo)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(saldo));
        return saldo;
    }

    @Override
    public Pago getPago(int idPago) {
        return pagos.get(idPago);
    }

    public interface PagosAcceptListener{
        public void onAcceptSuccess(List<Pago> pagos);
        public double getNewSaldo(List<Integer> idsFormaPago);
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
                                    getNewSaldo(0);
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
