package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.widget.RangeSeekBar;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class FiltroOportunidadFragment extends BaseFragment {

    private RangeSeekBar<Integer> seekBar;
    private final static int DESDE_FECHA_CREACION = 1;
    private final static int HASTA_FECHA_CREACION = 2;
    private final static int DESDE_FECHA_GESTION = 3;
    private final static int HASTA_FECHA_GESTION = 4;

    public final static String FILTRO = "filtro";
    public final static String ETAPAS = "etapas";
    public final static String ESTADOS = "estados";
    public final static String DESDE_CREACION = "desde_creacion";
    public final static String HASTA_CREACION = "hasta_creacion";
    public final static String DESDE_GESTION = "desde_gestion";
    public final static String HASTA_GESTION = "hasta_gestion";
    public final static String DESDE_CANTIDAD = "desde_cantidad";
    public final static String HASTA_CANTIDAD = "hasta_cantidad";

    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
    OportunidadFiltroListener filtroListener;

    private Calendar desde_creacion, hasta_creacion, desde_gestion, hasta_gestion;

    public static FiltroOportunidadFragment newInstance() {
        return new FiltroOportunidadFragment();
    }

    public interface OportunidadFiltroListener {
        public void onFiltroSend(Intent intent);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        filtroListener = (OportunidadFiltroListener) activity;
        tryEnableGooglePlayServices(true);
        setContentView(R.layout.fragment_filtro_oportunidad, R.menu.fragment_oportunidad_filtro);
        setRetainInstance(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        seekBar = new RangeSeekBar<Integer>(0, 5, this.getContext());
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                ((TextView)rootView.findViewById(R.id.filtro_min)).setText(minValue + "");
                ((TextView)rootView.findViewById(R.id.filtro_max)).setText(maxValue + "");
            }
        });

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.filtro_importancia);
        layout.addView(seekBar);

        rootView.findViewById(R.id.fecha_creacion_desde).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDatePicker(DESDE_FECHA_CREACION);
            }
        });
        rootView.findViewById(R.id.fecha_creacion_hasta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDatePicker(HASTA_FECHA_CREACION);
            }
        });
        rootView.findViewById(R.id.fecha_ultima_desde).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDatePicker(DESDE_FECHA_GESTION);
            }
        });
        rootView.findViewById(R.id.fecha_ultima_hasta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDatePicker(HASTA_FECHA_GESTION);
            }
        });

    }

    @Override
    public void onDailogDatePickerChange(int id, Calendar c) {
        super.onDailogDatePickerChange(id, c);
        switch (id)
        {
            case DESDE_FECHA_CREACION:
                desde_creacion = c;
                ((TextView) getRootView().findViewById(R.id.fecha_creacion_desde)).setText(format1.format(c.getTime()));
                break;
            case HASTA_FECHA_CREACION:
                hasta_creacion = c;
                ((TextView) getRootView().findViewById(R.id.fecha_creacion_hasta)).setText(format1.format(c.getTime()));
                break;
            case DESDE_FECHA_GESTION:
                desde_gestion = c;
                ((TextView) getRootView().findViewById(R.id.fecha_ultima_desde)).setText(format1.format(c.getTime()));
                break;
            case HASTA_FECHA_GESTION:
                hasta_creacion = c;
                ((TextView) getRootView().findViewById(R.id.fecha_ultima_hasta)).setText(format1.format(c.getTime()));
                break;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filtro:
                if(Validaciones()) {
                    Intent result = new Intent();
                    Bundle bundle = new Bundle();
                    ArrayList<Integer> etapas = new ArrayList<Integer>();
                    ArrayList<String> estados = new ArrayList<String>();

                    if (desde_creacion != null)
                        bundle.putLong(DESDE_CREACION, desde_creacion.getTimeInMillis());
                    if (hasta_creacion != null)
                        bundle.putLong(HASTA_CREACION, hasta_creacion.getTimeInMillis());
                    if (desde_gestion != null)
                        bundle.putLong(DESDE_GESTION, desde_gestion.getTimeInMillis());
                    if (hasta_gestion != null)
                        bundle.putLong(HASTA_GESTION, hasta_gestion.getTimeInMillis());

                    if (((TextView) getRootView().findViewById(R.id.filtro_desde)).length() > 0)
                        bundle.putDouble(DESDE_CANTIDAD, Double.parseDouble(((TextView) getRootView().findViewById(R.id.filtro_desde)).getText().toString()));
                    if (((TextView) getRootView().findViewById(R.id.filtro_hasta)).length() > 0)
                        bundle.putDouble(HASTA_CANTIDAD, Double.parseDouble(((TextView) getRootView().findViewById(R.id.filtro_hasta)).getText().toString()));

                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa1)).isChecked())
                        etapas.add(1);
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa2)).isChecked())
                        etapas.add(2);
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa3)).isChecked())
                        etapas.add(3);
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa4)).isChecked())
                        etapas.add(4);
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa5)).isChecked())
                        etapas.add(5);

                    if(((CheckBox) getRootView().findViewById(R.id.filtro_activos)).isChecked())
                        estados.add("A");
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_suspendidos)).isChecked())
                        estados.add("S");
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_concretados)).isChecked())
                        estados.add("C");
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_no_concretados)).isChecked())
                        estados.add("NC");

                    bundle.putIntegerArrayList(ETAPAS, etapas);
                    bundle.putStringArrayList(ESTADOS, estados);

                    result.putExtra(FILTRO, bundle);

                    filtroListener.onFiltroSend(result);

                }
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean Validaciones()
    {
        if(desde_gestion != null && hasta_gestion != null)
        {
            if(desde_gestion.getTimeInMillis() > hasta_gestion.getTimeInMillis())
            {
                Toast.makeText(getContext(), R.string.message_desde_hasta, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(desde_creacion != null && hasta_creacion != null)
        {
            if(desde_creacion.getTimeInMillis() > hasta_creacion.getTimeInMillis())
            {
                Toast.makeText(getContext(), R.string.message_desde_hasta, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(!((CheckBox) getRootView().findViewById(R.id.filtro_concretados)).isChecked() && !((CheckBox) getRootView().findViewById(R.id.filtro_activos)).isChecked() &&
                !((CheckBox) getRootView().findViewById(R.id.filtro_suspendidos)).isChecked() && !((CheckBox) getRootView().findViewById(R.id.filtro_no_concretados)).isChecked())
        {
            Toast.makeText(getContext(), R.string.message_un_estado, Toast.LENGTH_LONG).show();
            return false;
        }

        if(((TextView) getRootView().findViewById(R.id.filtro_desde)).length() > 0 && ((TextView) getRootView().findViewById(R.id.filtro_hasta)).length() > 0)
        {
            if(Double.parseDouble(((TextView) getRootView().findViewById(R.id.filtro_desde)).getText().toString()) > Double.parseDouble(((TextView) getRootView().findViewById(R.id.filtro_hasta)).getText().toString()))
            {
                Toast.makeText(getContext(), R.string.message_desde_hasta, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}
