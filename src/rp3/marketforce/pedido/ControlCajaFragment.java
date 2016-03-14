package rp3.marketforce.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import rp3.marketforce.db.Contract;
import rp3.marketforce.models.pedido.ControlCaja;
import rp3.marketforce.models.pedido.FormaPago;
import rp3.marketforce.models.pedido.Pago;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 22/02/2016.
 */
public class ControlCajaFragment extends BaseFragment {

    public static final String ARG_CONTROL = "control";
    private SimpleDateFormat format1, format2, format3, format5;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setContentView(R.layout.fragment_control_caja);
        if(getParentFragment()!=null){
            ((PedidoFragment)getParentFragment()).RefreshMenu();
        }

    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        format1 = new SimpleDateFormat("EEEE");
        format2 = new SimpleDateFormat("dd");
        format3 = new SimpleDateFormat("MMMM");
        format5 = new SimpleDateFormat("yyyy");

        ((TextView) rootView.findViewById(R.id.control_fecha)).setText(format1.format(Calendar.getInstance().getTime()) + ", " + format2.format(Calendar.getInstance().getTime()) +  " de " +
                                        format3.format(Calendar.getInstance().getTime()) + " del " + format5.format(Calendar.getInstance().getTime()));

        rootView.findViewById(R.id.actividad_cancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.actividad_aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((EditText) rootView.findViewById(R.id.control_valor)).length() > 0)
                {
                    ControlCaja control = new ControlCaja();
                    control.setValorApertura(Float.parseFloat(((EditText) rootView.findViewById(R.id.control_valor)).getText().toString()));
                    if(control.getValorApertura() < 0)
                    {
                        Toast.makeText(getContext(), "El valor de apertura no puede ser menor a 0.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    control.setIdControlCaja(0);
                    control.setFechaApertura(Calendar.getInstance().getTime());
                    control.setIdAgente(PreferenceManager.getInt(Contants.KEY_IDAGENTE));
                    ControlCaja.insert(getDataBase(), control);
                    if (control.getID() == 0)
                        control.setID(getDataBase().queryMaxInt(Contract.ControlCaja.TABLE_NAME, Contract.ControlCaja._ID));

                    if (ConnectionUtils.isNetAvailable(getActivity())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_UPDATE_CAJA);
                        bundle.putLong(ARG_CONTROL, control.getID());
                        requestSync(bundle);
                    }

                    dismiss();
                }
                else
                {
                    Toast.makeText(getContext(), "Debe de ingresar un valor de apertura.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
