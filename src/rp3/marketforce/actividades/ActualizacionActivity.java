package rp3.marketforce.actividades;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.cliente.CrearClienteFragment;
import rp3.marketforce.models.Actividad;
import rp3.marketforce.models.AgendaTarea;
import rp3.marketforce.models.AgendaTareaActividades;
import rp3.marketforce.models.AgendaTareaOpciones;

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
            ActualizacionFragment newFragment = ActualizacionFragment.newInstance(id_agenda_int, Contants.IS_GESTION, id_actividad);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }

    @Override
    public void aceptarCambios(View v) {

    }
}