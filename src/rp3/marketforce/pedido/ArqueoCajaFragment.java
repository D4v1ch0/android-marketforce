package rp3.marketforce.pedido;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.models.pedido.PedidoDetalle;
import rp3.runtime.Session;
import rp3.util.Convert;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 17/12/2015.
 */
public class ArqueoCajaFragment extends BaseFragment implements ArqueoControlFragment.ControlCajaListener {

    private static final int DIALOG_FECHA = 1;
    public final static int SPACES = 36;

    private NumberFormat numberFormat;
    private ControlCaja control;
    private SimpleDateFormat format1, format2, format3, format5, format6;
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

        format1 = new SimpleDateFormat("EEEE");
        format2 = new SimpleDateFormat("dd");
        format3 = new SimpleDateFormat("MMMM");
        format5 = new SimpleDateFormat("yyyy");
        format6 = new SimpleDateFormat("HH:mm");

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
            case R.id.action_imprimir:
                imprimirArqueo();
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
        control = transaction;

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

    public void imprimirArqueo()
    {
        String toPrint = "";
        List<Pago> pagos = Pago.getArqueoCaja(getDataBase(), control.getID());

        toPrint = toPrint + StringUtils.centerStringInLine("Fecha Apertura:", SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine(format1.format(control.getFechaApertura()) + ", " + format2.format(control.getFechaApertura()) + " de " +
                format3.format(control.getFechaApertura()) + " del " + format5.format(control.getFechaApertura()) + " " + format6.format(control.getFechaApertura()), SPACES);
        toPrint = toPrint + StringUtils.centerStringInLine("Aperturado por:" + Session.getUser().getLogonName(), SPACES);
        toPrint = toPrint + '\n';
        if(control.getFechaCierre() != null && control.getFechaCierre().getTime() > 0)
        {
            toPrint = toPrint + StringUtils.centerStringInLine("Fecha Cierre:", SPACES);
            toPrint = toPrint + StringUtils.centerStringInLine(format1.format(control.getFechaCierre()) + ", " + format2.format(control.getFechaCierre()) + " de " +
                    format3.format(control.getFechaCierre()) + " del " + format5.format(control.getFechaCierre()) + " " + format6.format(control.getFechaCierre()), SPACES);
            toPrint = toPrint + StringUtils.centerStringInLine("Cerrado por:" + Session.getUser().getLogonName(), SPACES);
        }

        toPrint = toPrint + '\n';


        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";

        toPrint = toPrint + '\n';
        toPrint = toPrint + "Tipo de Pago   Transacciones   Total" + '\n';
        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';

        int cantidad = 0;
        double valor = 0;
        for(Pago pago: pagos) {
            cantidad = cantidad + pago.getIdPago();
            valor = valor + pago.getValor();

            if(pago.getIdFormaPago() == 0)
                toPrint = toPrint + StringUtils.leftStringInSpace("Nota Crédito", 12) + " ";
            else if(pago.getIdFormaPago() == -1)
                toPrint = toPrint + StringUtils.leftStringInSpace("Apertura", 12) + " ";
            else if(pago.getFormaPago().getDescripcion().length() > 12)
                toPrint = toPrint + StringUtils.leftStringInSpace(pago.getFormaPago().getDescripcion().substring(0,12), 12) + " ";
            else
                toPrint = toPrint + StringUtils.leftStringInSpace(pago.getFormaPago().getDescripcion(), 12) + " ";

            toPrint = toPrint + StringUtils.rightStringInSpace(pago.getIdPago() + "" , 11) + " ";
            toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(pago.getValor()), 11);
            toPrint = toPrint + '\n';
        }

        for(int i = 1; i <= SPACES; i++)
            toPrint = toPrint + "=";
        toPrint = toPrint + '\n';
        toPrint = toPrint + StringUtils.leftStringInSpace("Total", 12) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(cantidad + "" , 11) + " ";
        toPrint = toPrint + StringUtils.rightStringInSpace(numberFormat.format(valor), 11);
        toPrint = toPrint + '\n';

        Intent print = new Intent(Intent.ACTION_SEND);
        print.addCategory(Intent.CATEGORY_DEFAULT);
        print.putExtra(Intent.EXTRA_TEXT, toPrint);
        print.setType("text/plain");
        startActivity(Intent.createChooser(print, "Imprimir"));
    }
}
