package rp3.auna.actividades;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import rp3.auna.Contants;
import rp3.auna.R;

/**
 * Created by magno_000 on 09/04/2015.
 */
public class ActualizacionActivity extends ActividadActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Editar Cliente");
        setHomeAsUpEnabled(true, true);
        setContentView(R.layout.layout_simple_content);
        if (!hasFragment(rp3.core.R.id.content)) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            ActualizacionFragment newFragment = ActualizacionFragment.newInstance(id_agenda_int, Contants.IS_GESTION, id_actividad, id_ruta);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    public void aceptarCambios(View v) {

    }
}