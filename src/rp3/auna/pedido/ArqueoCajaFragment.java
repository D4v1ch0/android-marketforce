package rp3.auna.pedido;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.starmicronics.stario.PortInfo;
import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;

import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.pedido.ControlCaja;
import rp3.auna.models.pedido.Pago;
import rp3.auna.utils.PrintHelper;

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
        List<Pago> pagos = Pago.getArqueoCaja(getDataBase(), transaction.getID(), true);
        control = transaction;

        int cantidad = 0;
        double valor = 0;
        for(Pago pago: pagos)
        {
            if(pago.getIdFormaPago() > -2) {
                cantidad = cantidad + pago.getIdPago();
                valor = valor + pago.getValor();
            }
        }

        ((TextView) getRootView().findViewById(R.id.arqueo_total_transacciones)).setText(cantidad + "");
        ((TextView) getRootView().findViewById(R.id.arqueo_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + numberFormat.format(valor));

        ArqueoCajaAdapter adapter = new ArqueoCajaAdapter(this.getContext(), pagos);
        ((ListView) getRootView().findViewById(R.id.arqueo_list)).setAdapter(adapter);
    }

    public void imprimirArqueo()
    {
        if(control != null) {
            List<Pago> pagos = Pago.getArqueoCaja(getDataBase(), control.getID(), true);
            String toPrint = PrintHelper.generarArqueo(pagos, control);

            try {
                PortInfo portInfo = null;
                List<PortInfo> portList = StarIOPort.searchPrinter("BT:");
                for (PortInfo port : portList) {
                    if (port.getPortName().contains("BT:STAR"))
                        portInfo = port;
                }

                if (portInfo == null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.title_error_impresión)
                            .setMessage(R.string.warning_impresora_no_vinculada)
                            .setPositiveButton(R.string.action_reintentar, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    imprimirArqueo();
                                }
                            })
                            .setCancelable(true);
                    dialog.show();
                    return;
                } else {
                    StarIOPort port = StarIOPort.getPort(portInfo.getPortName(), "portable;", 10000);
                    if (PrintHelper.isPrinterReady(port.retreiveStatus()) != -1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                                .setTitle(R.string.title_error_impresión)
                                .setMessage(PrintHelper.isPrinterReady(port.retreiveStatus()))
                                .setPositiveButton(R.string.action_reintentar, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        imprimirArqueo();
                                    }
                                })
                                .setCancelable(true);
                        dialog.show();
                        return;
                    } else {
                        byte[] command = toPrint.getBytes(Charset.forName("UTF-8"));
                        port.writePort(command, 0, command.length);
                        byte[] cut = {27, 100, 3};
                        port.writePort(cut, 0, cut.length);
                    }

                }

            } catch (StarIOPortException e) {
                e.printStackTrace();
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.title_error_impresión)
                        .setMessage(R.string.warning_error_desconocido)
                        .setPositiveButton(R.string.action_reintentar, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                imprimirArqueo();
                            }
                        })
                        .setCancelable(true);
                dialog.show();
                return;
            }
        }
        else
        {
            Toast.makeText(getContext(), "Debe de Seleccionar una Apertura de Caja", Toast.LENGTH_SHORT).show();
        }
    }
}
