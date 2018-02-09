package rp3.auna.pedido;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.auna.Contants;
import rp3.auna.R;
import rp3.auna.db.Contract;
import rp3.auna.models.pedido.ControlCaja;
import rp3.auna.sync.SyncAdapter;
import rp3.util.ConnectionUtils;

/**
 * Created by magno_000 on 22/02/2016.
 */
public class ControlCajaFragment extends BaseFragment {

    private static final String TAG = ControlCajaFragment.class.getSimpleName();
    public static final String ARG_CONTROL = "control";
    private SimpleDateFormat format1, format2, format3, format5;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach...");
        setContentView(R.layout.fragment_control_caja);
        if(getParentFragment()!=null){
            ((PedidoFragment)getParentFragment()).RefreshMenu();
        }

    }

    @Override
    public void onFragmentResult(String tagName, int resultCode, Bundle data) {
        super.onFragmentResult(tagName, resultCode, data);
        Log.d(TAG,"onFragmentResult...");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState...");
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        Log.d(TAG,"onFragmentCreateView...");
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
                    control.setIdCaja(PreferenceManager.getInt(Contants.KEY_ID_CAJA));
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

    /**
     *
     * Ciclo de vida
     *
     */

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }
}
