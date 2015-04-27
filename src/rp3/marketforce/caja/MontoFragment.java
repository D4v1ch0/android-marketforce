package rp3.marketforce.caja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.Agenda;
import rp3.marketforce.models.Caja;
import rp3.marketforce.ruta.RutasDetailFragment;
import rp3.marketforce.utils.Utils;

import static rp3.util.Screen.getOrientation;

/**
 * Created by magno_000 on 24/04/2015.
 */
public class MontoFragment extends BaseFragment {
    public static MontoFragment newInstance() {
        return new MontoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.fragment_dialog_monto);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setRetainInstance(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:

                if(((EditText) getRootView().findViewById(R.id.monto_valor)).length() <= 0)
                {
                    Toast.makeText(this.getContext(), "Monto Incorrecto", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String valor = ((EditText) getRootView().findViewById(R.id.monto_valor)).getText().toString();
                    Caja caja = new Caja();
                    caja.setMontoApertura(Double.parseDouble(valor));
                    caja.setFechaApertura(Calendar.getInstance().getTime());
                    caja.setIdCajaControl(1);
                    caja.setActivo(true);
                    caja.setNombre("Prueba");
                    Caja.insert(getDataBase(),caja);
                    finish();
                }

                break;
            case R.id.action_cancel:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

    }
}
