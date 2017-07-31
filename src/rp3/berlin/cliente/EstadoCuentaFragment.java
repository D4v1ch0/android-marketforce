package rp3.berlin.cliente;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.Cliente;
import rp3.berlin.models.pedido.EstadoCuenta;
import rp3.berlin.sync.SyncAdapter;
import rp3.util.Convert;

/**
 * Created by magno_000 on 03/05/2017.
 */

public class EstadoCuentaFragment extends BaseFragment {
    public static String ARG_CLIENTE = "idcliente";

    private long idCliente;
    private Cliente cli;
    private NumberFormat numberFormat;
    private List<EstadoCuenta> estadoCuentas;

    public static EstadoCuentaFragment newInstance(long idCliente)
    {
        EstadoCuentaFragment fragment = new EstadoCuentaFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CLIENTE, idCliente);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
        //setContentView(R.layout.fragment_estado_cuenta);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_estado_cuenta, R.menu.fragment_estado_cuenta);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_enviar_estado:
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_ESTADO_CUENTA);
                bundle.putString(ARG_CLIENTE, cli.getIdExterno());
                requestSync(bundle);

                showDialogProgress("Enviando", "Enviando correo a cliente con estado de cuenta");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);

        idCliente = getArguments().getLong(ARG_CLIENTE, 0);
        cli = Cliente.getClienteID(getDataBase(), idCliente, false);

        ((TextView) rootView.findViewById(R.id.estado_cuenta_cliente)).setText(cli.getIdExterno() + " - " + cli.getNombreCompleto());

        if(estadoCuentas == null) {
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ESTADO_CUENTA);
            bundle.putString(ARG_CLIENTE, cli.getIdExterno());
            requestSync(bundle);

            showDialogProgress("Cargando", "Consultando Estado de Cuenta");
        }
        else
        {
            EstadoCuentaAdapter adapter = new EstadoCuentaAdapter(getContext(), estadoCuentas);
            ((ListView)getRootView().findViewById(R.id.estado_cuenta_lista)).setAdapter(adapter);
            GetResumen(estadoCuentas);
        }
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_ESTADO_CUENTA)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                String desc = data.getString(ARG_CLIENTE);
                ShowEstadoCuenta(desc);
            }
        }
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_SEND_ESTADO_CUENTA)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            }
            else
            {
                Toast.makeText(this.getContext(), "Estado de cuenta enviado", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void ShowEstadoCuenta(String json)
    {
        try {
            JSONArray jsonArray = new JSONArray(json);
            Calendar cal = Calendar.getInstance();
            estadoCuentas = new ArrayList<>();

            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                EstadoCuenta estadoCuenta = new EstadoCuenta();
                estadoCuenta.setAbono(jsonObject.getDouble("Abono"));
                if(jsonObject.isNull("DiasVencidos"))
                    estadoCuenta.setDiasVencidos(0);
                else
                    estadoCuenta.setDiasVencidos(jsonObject.getInt("DiasVencidos"));

                if(jsonObject.isNull("DiasXVencer"))
                    estadoCuenta.setDiasXVencer(0);
                else
                    estadoCuenta.setDiasXVencer(jsonObject.getInt("DiasXVencer"));

                estadoCuenta.setDocumento(jsonObject.getString("NumeroDocumento") + "");
                estadoCuenta.setFechaEmision(Convert.getDateFromDotNetTicks(jsonObject.getLong("FechaEmisionTicks")));
                if(!jsonObject.isNull("FechaVencimientoTicks"))
                    estadoCuenta.setFechaVencimiento(Convert.getDateFromDotNetTicks(jsonObject.getLong("FechaVencimientoTicks")));

                if(jsonObject.isNull("Saldo"))
                    estadoCuenta.setSaldo(0);
                else
                    estadoCuenta.setSaldo(jsonObject.getDouble("Saldo"));

                if(jsonObject.isNull("PagosAplic"))
                    estadoCuenta.setPagosAplic(0);
                else
                    estadoCuenta.setPagosAplic(jsonObject.getDouble("PagosAplic"));

                if(jsonObject.isNull("Interes"))
                    estadoCuenta.setInteres(0);
                else
                    estadoCuenta.setInteres(jsonObject.getDouble("Interes"));

                estadoCuenta.setValor(jsonObject.getDouble("Monto"));
                estadoCuentas.add(estadoCuenta);
            }

            EstadoCuentaAdapter adapter = new EstadoCuentaAdapter(getContext(), estadoCuentas);
            ((ListView)getRootView().findViewById(R.id.estado_cuenta_lista)).setAdapter(adapter);
            GetResumen(estadoCuentas);
        }
        catch (Exception ex)
        {

        }
    }

    private void GetResumen(List<EstadoCuenta> listado)
    {
        double vencida_0_8 = 0, vencida_9_15 = 0, vencida_16_30 = 0, vencida_31_60 = 0, vencida_61 = 0, total_vencida = 0;
        double vencer_1_30 = 0, vencer_31_60 = 0, vencer_61 = 0, total_vencer = 0, total_saldos = 0;
        Calendar cal = Calendar.getInstance();

        for (EstadoCuenta estado : listado) {
            //Primero valido si la fecha de vencimiento es mayor o menor al día de hoy
            if(!estado.getDocumento().contains("N") && !estado.getDocumento().contains("E")) {
                if (estado.getFechaVencimiento() != null) {
                    if (cal.getTime().getTime() > estado.getFechaVencimiento().getTime()) {
                        //Cartera vencida
                        if (estado.getDiasVencidos() > 8) {
                            if (estado.getDiasVencidos() > 15) {
                                if (estado.getDiasVencidos() > 30) {
                                    if (estado.getDiasVencidos() > 60) {
                                        vencida_61 = vencida_61 + estado.getSaldo();
                                    } else {
                                        vencida_31_60 = vencida_31_60 + estado.getSaldo();
                                    }
                                } else {
                                    vencida_16_30 = vencida_16_30 + estado.getSaldo();
                                }
                            } else {
                                vencida_9_15 = vencida_9_15 + estado.getSaldo();
                            }
                        } else {
                            vencida_0_8 = vencida_0_8 + estado.getSaldo();
                        }
                    } else {
                        //Cartera por vencer
                        int diasXVencer = (-1) * estado.getDiasXVencer();
                        if (diasXVencer > 30) {
                            if (diasXVencer > 60) {
                                vencer_61 = vencer_61 + estado.getSaldo();
                            } else {
                                vencer_31_60 = vencer_31_60 + estado.getSaldo();
                            }
                        } else {
                            vencer_1_30 = vencer_1_30 + estado.getSaldo();
                        }
                    }
                }
            }
            total_saldos = total_saldos + estado.getSaldo();
        }

        total_vencida = vencida_0_8 + vencida_9_15 + vencida_16_30 + vencida_31_60 + vencida_61;
        total_vencer = vencer_1_30 + vencer_31_60 + vencer_61;

        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencida_0_8)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(vencida_0_8));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencida_9_15)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(vencida_9_15));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencida_16_30)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(vencida_16_30));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencida_31_60)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(vencida_31_60));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencida_61_mas)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(vencida_61));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencida_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(total_vencida));

        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencer_1_30)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(vencer_1_30));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencer_31_60)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(vencer_31_60));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencer_61_mas)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(vencer_61));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_vencer_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(total_vencer));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(total_saldos));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_cupo_credito)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(cli.getLimiteCredito()));
        ((TextView) getRootView().findViewById(R.id.estado_cuenta_saldo_disponible)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(cli.getLimiteCredito() - total_saldos));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            Toast.makeText(getContext(), "Portrait", Toast.LENGTH_LONG).show();
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            Toast.makeText(getContext(), "Landscape", Toast.LENGTH_LONG).show();*/
    }
}
