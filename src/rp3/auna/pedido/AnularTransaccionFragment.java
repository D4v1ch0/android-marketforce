package rp3.auna.pedido;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.models.pedido.Pedido;
import rp3.auna.models.pedido.PedidoDetalle;
import rp3.auna.sync.SyncAdapter;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 05/02/2016.
 */
public class AnularTransaccionFragment extends BaseFragment {

    public static final String ARG_TRANSACCION = "transaccion";

    public static final int REQ_CODE_SPEECH_INPUT_ANUL = 101;

    private long id;
    private PedidoDetailFragment.PedidoDetailFragmentListener createFragmentListener;

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

        if(getParentFragment()!=null){
            createFragmentListener = (PedidoDetailFragment.PedidoDetailFragmentListener)getParentFragment();
        }else{
            createFragmentListener = (PedidoDetailFragment.PedidoDetailFragmentListener) activity;
        }
        setContentView(R.layout.fragment_anular_transaccion);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && null != data) {

            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            setTextViewText(R.id.actividad_texto_respuesta, StringUtils.getStringCapSentence(result.get(0)));
        }

    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        id = getArguments().getLong(ARG_TRANSACCION);

        SimpleGeneralValueAdapter motivoAnulacionAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), Contants.GENERAL_TABLE_MOTIVOS_ANULACION);
        ((Spinner) rootView.findViewById(R.id.anulacion_motivo)).setAdapter(motivoAnulacionAdapter);

        ((ImageView) rootView.findViewById(R.id.actividad_voice_to_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        rootView.findViewById(R.id.actividad_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.actividad_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pedido pedido = Pedido.getPedido(getDataBase(), id, true);
                pedido.setMotivoAnulacion(((GeneralValue) ((Spinner) getRootView().findViewById(R.id.anulacion_motivo)).getSelectedItem()).getCode());
                pedido.setObservacionAnulacion(((EditText) getRootView().findViewById(R.id.actividad_texto_respuesta)).getText().toString());
                pedido.setEstado("A");
                pedido.setFechaAnulacion(Calendar.getInstance().getTime());
                Pedido.update(getDataBase(), pedido);
                if (pedido.getTipoDocumento().equalsIgnoreCase("NC")) {
                    Pedido toUpdate = Pedido.getPedido(getDataBase(), pedido.get_idDocumentoRef(), true);
                    for (PedidoDetalle detalle_nc : pedido.getPedidoDetalles()) {
                        for (PedidoDetalle detalleRef : toUpdate.getPedidoDetalles()) {
                            if (detalle_nc.getIdProducto() == detalleRef.getIdProducto()) {
                                detalleRef.setCantidadDevolucion(detalleRef.getCantidadDevolucion() - detalle_nc.getCantidad());
                                PedidoDetalle.update(getDataBase(), detalleRef);
                            }
                        }
                    }
                }
                Bundle bundle2 = new Bundle();
                bundle2.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_ANULAR_PEDIDO);
                bundle2.putLong(CrearPedidoFragment.ARG_PEDIDO, id);
                requestSync(bundle2);
                createFragmentListener.onPermisoChanged(pedido);
                dismiss();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable Ahora");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT_ANUL);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
