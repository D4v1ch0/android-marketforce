package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;

import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.content.SimpleIdentifiableAdapter;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.models.pedido.Pedido;
import rp3.marketforce.sync.SyncAdapter;

/**
 * Created by magno_000 on 05/02/2016.
 */
public class AnularTransaccionFragment extends BaseFragment {

    public static final String ARG_TRANSACCION = "transaccion";

    private long id;

    public static AnularTransaccionFragment newInstance(long transaccion)
    {
        AnularTransaccionFragment fragment = new AnularTransaccionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TRANSACCION, transaccion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setContentView(R.layout.fragment_anular_transaccion);

    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        id = getArguments().getLong(ARG_TRANSACCION);

        SimpleGeneralValueAdapter motivoAnulacionAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), Contants.GENERAL_TABLE_MOTIVOS_ANULACION);
        ((Spinner) rootView.findViewById(R.id.anulacion_motivo)).setAdapter(motivoAnulacionAdapter);

        rootView.findViewById(R.id.actividad_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.actividad_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pedido pedido = Pedido.getPedido(getDataBase(), id);
                pedido.setMotivoAnulacion(((GeneralValue)((Spinner)getRootView().findViewById(R.id.anulacion_motivo)).getSelectedItem()).getCode());
                pedido.setObservacionAnulacion(((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).getText().toString());
                pedido.setEstado("A");
                Pedido.update(getDataBase(), pedido);
                Bundle bundle2 = new Bundle();
                bundle2.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ANULAR_PEDIDO);
                bundle2.putLong(CrearPedidoFragment.ARG_PEDIDO, id);
                requestSync(bundle2);
                dismiss();
            }
        });
    }
}
