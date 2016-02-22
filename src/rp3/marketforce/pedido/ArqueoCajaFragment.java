package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.Pago;
import rp3.util.Convert;

/**
 * Created by magno_000 on 17/12/2015.
 */
public class ArqueoCajaFragment extends BaseFragment implements ArqueoControlFragment.ControlCajaListener {

    private static final int DIALOG_FECHA = 1;

    private NumberFormat numberFormat;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setContentView(R.layout.fragment_arqueo_caja, R.menu.fragment_arqueo_menu);

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        //showDialogDatePicker(DIALOG_FECHA);
        showDialogFragment(new ArqueoControlFragment(), "Cajas Aperturadas", "Cajas Aperturadas");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_nuevo_arqueo:
                showDialogFragment(new ArqueoControlFragment(), "Cajas Aperturadas", "Cajas Aperturadas");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDailogDatePickerChange(int id, Calendar c) {
        super.onDailogDatePickerChange(id, c);
    }

    @Override
    public void onControlCajaSelected(ControlCaja transaction) {
        List<Pago> pagos = Pago.getArqueoCaja(getDataBase(), transaction.getID());

        int cantidad = 0;
        double valor = 0;
        for(Pago pago: pagos)
        {
            cantidad = cantidad + pago.getIdPago();
            valor = valor + pago.getValor();
        }

        ((TextView) getRootView().findViewById(R.id.arqueo_total_transacciones)).setText(cantidad + "");
        ((TextView) getRootView().findViewById(R.id.arqueo_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + numberFormat.format(valor));

        ArqueoCajaAdapter adapter = new ArqueoCajaAdapter(this.getContext(), pagos);
        ((ListView) getRootView().findViewById(R.id.arqueo_list)).setAdapter(adapter);
    }
}
