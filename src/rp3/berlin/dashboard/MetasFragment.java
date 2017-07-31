package rp3.berlin.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.cliente.EstadoCuentaAdapter;
import rp3.berlin.cliente.EstadoCuentaFragment;
import rp3.berlin.models.Cliente;
import rp3.berlin.models.pedido.EstadoCuenta;
import rp3.berlin.models.pedido.Meta;
import rp3.berlin.sync.SyncAdapter;
import rp3.configuration.PreferenceManager;
import rp3.data.MessageCollection;
import rp3.runtime.Session;
import rp3.util.Convert;

/**
 * Created by magno_000 on 27/06/2017.
 */

public class MetasFragment extends BaseFragment {
    public static String ARG_RESP = "idcliente";

    private NumberFormat numberFormat;
    private List<Meta> listMeta;
    private double total_a_pagar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_metas);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);


        ((TextView) rootView.findViewById(R.id.metas_agente)).setText(Session.getUser().getFullName());

        if(listMeta == null) {
            Bundle bundle = new Bundle();
            bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_METAS);
            requestSync(bundle);

            showDialogProgress("Cargando", "Consultando Metas");
        }
        else
        {
            MetasAdapter adapter = new MetasAdapter(getContext(), listMeta);
            ((ListView)getRootView().findViewById(R.id.metas_lista)).setAdapter(adapter);
            ((TextView) getRootView().findViewById(R.id.metas_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(total_a_pagar));
        }
    }

    @Override
    public void onSyncComplete(Bundle data, MessageCollection messages) {
        if (data.containsKey(SyncAdapter.ARG_SYNC_TYPE) && data.getString(SyncAdapter.ARG_SYNC_TYPE).equals(SyncAdapter.SYNC_TYPE_METAS)) {
            closeDialogProgress();
            if (messages.hasErrorMessage()) {
                showDialogMessage(messages);
            } else {
                String desc = data.getString(ARG_RESP);
                ShowMetas(desc);
            }
        }
    }

    public void ShowMetas(String json)
    {
        try {
            JSONArray jsonArray = new JSONArray(json);
            Calendar cal = Calendar.getInstance();
            listMeta = new ArrayList<>();
            total_a_pagar = 0;

            for(int i = 0; i < jsonArray.length(); i ++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Meta meta = new Meta();
                meta.setGrupoComisionDescripcion(jsonObject.getString("GrupoComisionDescripcion"));
                meta.setcReal(jsonObject.getDouble("CReal"));
                meta.setcPresupuestado(jsonObject.getDouble("CPresupuestado"));
                meta.setReal(jsonObject.getDouble("Real"));
                meta.setPresupuestado(jsonObject.getDouble("Presupuestado"));
                meta.setPorCumplir(jsonObject.getDouble("PorCumplir"));
                if(!jsonObject.isNull("UsdTotal"))
                    meta.setUsdTotal(jsonObject.getDouble("UsdTotal"));
                else
                    meta.setUsdTotal(0);
                if(!jsonObject.isNull("PorCumplir"))
                    meta.setPorCumplir(jsonObject.getDouble("PorCumplir"));
                if(!jsonObject.isNull("PorcDisVaria"))
                    meta.setPorcDisVaria(Float.parseFloat(jsonObject.getString("PorcDisVaria")));
                if(!jsonObject.isNull("VarDistri"))
                    meta.setVarDistri(jsonObject.getDouble("VarDistri"));
                listMeta.add(meta);
                total_a_pagar = total_a_pagar + meta.getUsdTotal();
            }

            MetasAdapter adapter = new MetasAdapter(getContext(), listMeta);
            ((ListView)getRootView().findViewById(R.id.metas_lista)).setAdapter(adapter);
            ((TextView) getRootView().findViewById(R.id.metas_total)).setText(PreferenceManager.getString(Contants.KEY_MONEDA_SIMBOLO) + " " + numberFormat.format(total_a_pagar));
        }
        catch (Exception ex)
        {

        }
    }

}
