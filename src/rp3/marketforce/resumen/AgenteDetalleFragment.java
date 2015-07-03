package rp3.marketforce.resumen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agente;
import rp3.marketforce.sync.SyncAdapter;
import rp3.marketforce.utils.Utils;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 03/07/2015.
 */
public class AgenteDetalleFragment extends BaseFragment {

    public int idAgente;

    public final static String ARG_AGENTE = "id_agente";
    public final static String ARG_TITLE = "titulo";
    public final static String ARG_MESSAGE = "mensaje";

    public static AgenteDetalleFragment newInstance(int idAgente)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_AGENTE, idAgente);
        AgenteDetalleFragment fragment = new AgenteDetalleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_agente_detalle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        idAgente = getArguments().getInt(ARG_AGENTE);

        final Agente agente = Agente.getAgente(getDataBase(), idAgente);

        ((TextView) rootView.findViewById(R.id.agente_nombre)).setText(agente.getNombre());
        if(!TextUtils.isEmpty(agente.getTelefono())) {
            ((TextView) rootView.findViewById(R.id.agente_movil)).setText(agente.getTelefono());
            ((TextView) rootView.findViewById(R.id.agente_movil)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String uri = "tel:" + agente.getTelefono();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    Uri mUri = Uri.parse("smsto:" + Utils.convertToSMSNumber(agente.getTelefono()));
                    Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
                    mIntent.putExtra("chat",true);
                    Intent chooserIntent = Intent.createChooser(mIntent, "Seleccionar Acción");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent });
                    startActivity(chooserIntent);
                }});
        }
        else
            ((TextView) rootView.findViewById(R.id.agente_movil)).setClickable(false);

        if(!TextUtils.isEmpty(agente.getEmail())) {
            ((TextView) rootView.findViewById(R.id.agente_correo)).setText(agente.getEmail());
            ((TextView) rootView.findViewById(R.id.agente_correo)).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", agente.getEmail(), null));
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }});
        }
        else
            ((TextView) rootView.findViewById(R.id.agente_correo)).setClickable(false);

        rootView.findViewById(R.id.agente_enviar_notificación).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((TextView) rootView.findViewById(R.id.agente_titulo)).length() == 0) {
                    Toast.makeText(getContext(), R.string.message_ingreso_titulo, Toast.LENGTH_LONG).show();
                    return;
                }
                if(((TextView) rootView.findViewById(R.id.obs_text)).length() == 0) {
                    Toast.makeText(getContext(), R.string.message_ingreso_mensaje, Toast.LENGTH_LONG).show();
                    return;
                }
                if(ConnectionUtils.isNetAvailable(getContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_SEND_NOTIFICATION);
                    bundle.putInt(ARG_AGENTE, idAgente);
                    bundle.putString(ARG_TITLE, ((TextView) rootView.findViewById(R.id.agente_titulo)).getText().toString());
                    bundle.putString(ARG_MESSAGE, ((TextView) rootView.findViewById(R.id.obs_text)).getText().toString());
                    requestSync(bundle);
                    Toast.makeText(getContext(), R.string.message_notificacion_enviada, Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getContext(), R.string.message_error_sync_no_net_available, Toast.LENGTH_LONG).show();
            }
        });
    }
}
