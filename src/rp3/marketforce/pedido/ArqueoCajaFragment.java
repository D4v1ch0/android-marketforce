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
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.Pago;
import rp3.util.Convert;

/**
 * Created by magno_000 on 17/12/2015.
 */
public class ArqueoCajaFragment extends BaseFragment {

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

        showDialogDatePicker(DIALOG_FECHA);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_nuevo_arqueo:
                showDialogDatePicker(DIALOG_FECHA);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDailogDatePickerChange(int id, Calendar c) {
        super.onDailogDatePickerChange(id, c);

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        long inicio = Convert.getTicksFromDate(c.getTime());
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        long fin = Convert.getTicksFromDate(c.getTime());

        List<Pago> pagos = Pago.getArqueoCaja(getDataBase(), inicio, fin);

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
