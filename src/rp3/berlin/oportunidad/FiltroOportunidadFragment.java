package rp3.berlin.oportunidad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.berlin.R;
import rp3.berlin.models.oportunidad.Etapa;
import rp3.berlin.models.oportunidad.OportunidadTipo;
import rp3.widget.RangeSeekBar;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class FiltroOportunidadFragment extends BaseFragment implements TipoOportunidadFragment.EditTiposDialogListener {

    private RangeSeekBar<Integer> seekBar, seekBarProb;
    private final static int DESDE_FECHA_CREACION = 1;
    private final static int HASTA_FECHA_CREACION = 2;
    private final static int DESDE_FECHA_GESTION = 3;
    private final static int HASTA_FECHA_GESTION = 4;

    public final static String FILTRO = "filtro";
    public final static String ETAPAS = "etapas";
    public final static String ESTADOS = "estados";
    public final static String TIPOS = "tipos";
    public final static String IMPORTANCIA_MIN = "importancia_min";
    public final static String IMPORTANCIA_MAX = "importancia_max";
    public final static String PROBABILIDAD_MIN = "probabilidad_min";
    public final static String PROBABILIDAD_MAX = "probabilidad_max";
    public final static String DESDE_CREACION = "desde_creacion";
    public final static String HASTA_CREACION = "hasta_creacion";
    public final static String DESDE_GESTION = "desde_gestion";
    public final static String HASTA_GESTION = "hasta_gestion";
    public final static String DESDE_CANTIDAD = "desde_cantidad";
    public final static String HASTA_CANTIDAD = "hasta_cantidad";

    List<OportunidadTipo> tipos;
    SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
    NumberFormat numberFormat;
    OportunidadFiltroListener filtroListener;
    public Intent filtroData;

    private Calendar desde_creacion, hasta_creacion, desde_gestion, hasta_gestion;

    public static FiltroOportunidadFragment newInstance() {
        return new FiltroOportunidadFragment();
    }

    @Override
    public void onFinishTiposDialog(List<OportunidadTipo> tipos) {
        this.tipos = tipos;
        String text_tipos = "";
        if(tipos.size() != 0)
        {
            for (OportunidadTipo tipo : tipos)
                text_tipos = text_tipos + tipo.getDescripcion() + ", ";

            text_tipos = text_tipos.substring(0, text_tipos.length() - 2);
        }else
            text_tipos = getResources().getString(R.string.label_todos);

        ((Button) getRootView().findViewById(R.id.filtro_tipos)).setText(text_tipos);
    }

    public interface OportunidadFiltroListener {
        public void onFiltroSend(Intent intent);
        public void onFiltroClean();
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
        if(tipos == null)
            tipos = new ArrayList<>();
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(0);

        seekBar = new RangeSeekBar<Integer>(0, 5, this.getContext());
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                ((TextView)rootView.findViewById(R.id.filtro_min)).setText(minValue + "");
                ((TextView)rootView.findViewById(R.id.filtro_max)).setText(maxValue + "");
            }
        });

        rootView.findViewById(R.id.filtro_tipos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFragment(TipoOportunidadFragment.newInstance(tipos), "Tipos de Oportunidad");
            }
        });

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.filtro_importancia);
        layout.addView(seekBar);

        seekBarProb = new RangeSeekBar<Integer>(0, 100, this.getContext());
        seekBarProb.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                ((TextView)rootView.findViewById(R.id.filtro_min_prob)).setText(minValue + "%");
                ((TextView)rootView.findViewById(R.id.filtro_max_prob)).setText(maxValue + "%");
            }
        });
        ((LinearLayout) rootView.findViewById(R.id.filtro_probabilidad)).addView(seekBarProb);

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

        if(filtroData != null && filtroData.getBundleExtra(FiltroOportunidadFragment.FILTRO) != null)
        {
            ((CheckBox) getRootView().findViewById(R.id.filtro_etapa1)).setChecked(false);
            ((CheckBox) getRootView().findViewById(R.id.filtro_etapa2)).setChecked(false);
            ((CheckBox) getRootView().findViewById(R.id.filtro_etapa3)).setChecked(false);
            ((CheckBox) getRootView().findViewById(R.id.filtro_etapa4)).setChecked(false);
            ((CheckBox) getRootView().findViewById(R.id.filtro_etapa5)).setChecked(false);

            ((CheckBox) getRootView().findViewById(R.id.filtro_activos)).setChecked(false);
            ((CheckBox) getRootView().findViewById(R.id.filtro_suspendidos)).setChecked(false);
            ((CheckBox) getRootView().findViewById(R.id.filtro_no_concretados)).setChecked(false);
            ((CheckBox) getRootView().findViewById(R.id.filtro_concretados)).setChecked(false);

            Bundle bundle = filtroData.getBundleExtra(FiltroOportunidadFragment.FILTRO);

            ArrayList<Integer> etapas_raw = bundle.getIntegerArrayList(FiltroOportunidadFragment.ETAPAS);
            ArrayList<String> estados_raw = bundle.getStringArrayList(FiltroOportunidadFragment.ESTADOS);

            for(int i = 0; i < etapas_raw.size(); i ++) {
                switch (Etapa.getEtapaById(getDataBase(), etapas_raw.get(i)).getOrden())
                {
                    case 1: ((CheckBox) getRootView().findViewById(R.id.filtro_etapa1)).setChecked(true); break;
                    case 2: ((CheckBox) getRootView().findViewById(R.id.filtro_etapa2)).setChecked(true); break;
                    case 3: ((CheckBox) getRootView().findViewById(R.id.filtro_etapa3)).setChecked(true); break;
                    case 4: ((CheckBox) getRootView().findViewById(R.id.filtro_etapa4)).setChecked(true); break;
                    case 5: ((CheckBox) getRootView().findViewById(R.id.filtro_etapa5)).setChecked(true); break;
                }
            }

            for(int i = 0; i < estados_raw.size(); i ++) {
                if(estados_raw.get(i).equalsIgnoreCase("A")) ((CheckBox) getRootView().findViewById(R.id.filtro_activos)).setChecked(true);
                if(estados_raw.get(i).equalsIgnoreCase("S")) ((CheckBox) getRootView().findViewById(R.id.filtro_suspendidos)).setChecked(true);
                if(estados_raw.get(i).equalsIgnoreCase("NC")) ((CheckBox) getRootView().findViewById(R.id.filtro_no_concretados)).setChecked(true);
                if(estados_raw.get(i).equalsIgnoreCase("C")) ((CheckBox) getRootView().findViewById(R.id.filtro_concretados)).setChecked(true);
            }

            if(bundle.containsKey(FiltroOportunidadFragment.DESDE_CANTIDAD))
                ((TextView) getRootView().findViewById(R.id.filtro_desde)).setText(numberFormat.format(bundle.getDouble(FiltroOportunidadFragment.DESDE_CANTIDAD)));
            if(bundle.containsKey(FiltroOportunidadFragment.HASTA_CANTIDAD))
                ((TextView) getRootView().findViewById(R.id.filtro_hasta)).setText(numberFormat.format(bundle.getDouble(FiltroOportunidadFragment.HASTA_CANTIDAD)));
            if(bundle.containsKey(FiltroOportunidadFragment.DESDE_CREACION))
            {
                desde_creacion = Calendar.getInstance();
                desde_creacion.setTimeInMillis(bundle.getLong(FiltroOportunidadFragment.DESDE_CREACION));
                ((TextView) getRootView().findViewById(R.id.fecha_creacion_desde)).setText(format1.format(desde_creacion.getTime()));
            }
            if(bundle.containsKey(FiltroOportunidadFragment.HASTA_CREACION))
            {
                hasta_creacion = Calendar.getInstance();
                hasta_creacion.setTimeInMillis(bundle.getLong(FiltroOportunidadFragment.HASTA_CREACION));
                ((TextView) getRootView().findViewById(R.id.fecha_creacion_hasta)).setText(format1.format(hasta_creacion.getTime()));
            }

            seekBar.setSelectedMinValue(bundle.getInt(FiltroOportunidadFragment.IMPORTANCIA_MIN));
            seekBar.setSelectedMaxValue(bundle.getInt(FiltroOportunidadFragment.IMPORTANCIA_MAX));
            seekBarProb.setSelectedMinValue(bundle.getInt(FiltroOportunidadFragment.PROBABILIDAD_MIN));
            seekBarProb.setSelectedMaxValue(bundle.getInt(FiltroOportunidadFragment.PROBABILIDAD_MAX));

            ((TextView) getRootView().findViewById(R.id.filtro_min)).setText(bundle.getInt(FiltroOportunidadFragment.IMPORTANCIA_MIN) + "");
            ((TextView) getRootView().findViewById(R.id.filtro_max)).setText(bundle.getInt(FiltroOportunidadFragment.IMPORTANCIA_MAX) + "");
            ((TextView) getRootView().findViewById(R.id.filtro_min_prob)).setText(bundle.getInt(FiltroOportunidadFragment.PROBABILIDAD_MIN) + "%");
            ((TextView) getRootView().findViewById(R.id.filtro_max_prob)).setText(bundle.getInt(FiltroOportunidadFragment.PROBABILIDAD_MAX) + "%");
        }

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
                    ArrayList<Integer> tipos = new ArrayList<Integer>();
                    ArrayList<String> estados = new ArrayList<String>();

                    if (desde_creacion != null) {
                        desde_creacion.set(Calendar.HOUR_OF_DAY, 0);
                        desde_creacion.set(Calendar.MINUTE, 0);
                        desde_creacion.set(Calendar.SECOND, 0);
                        bundle.putLong(DESDE_CREACION, desde_creacion.getTimeInMillis());
                    }
                    if (hasta_creacion != null) {
                        hasta_creacion.set(Calendar.HOUR_OF_DAY, 23);
                        hasta_creacion.set(Calendar.MINUTE, 59);
                        hasta_creacion.set(Calendar.SECOND, 59);
                        bundle.putLong(HASTA_CREACION, hasta_creacion.getTimeInMillis());
                    }
                    /*
                    if (desde_gestion != null) {
                        desde_gestion.set(Calendar.HOUR_OF_DAY, 0);
                        desde_gestion.set(Calendar.MINUTE, 0);
                        desde_gestion.set(Calendar.SECOND, 0);
                        bundle.putLong(DESDE_GESTION, desde_gestion.getTimeInMillis());
                    }
                    if (hasta_gestion != null) {
                        hasta_gestion.set(Calendar.HOUR_OF_DAY, 23);
                        hasta_gestion.set(Calendar.MINUTE, 59);
                        hasta_gestion.set(Calendar.SECOND, 59);
                        bundle.putLong(HASTA_GESTION, hasta_gestion.getTimeInMillis());
                    }*/

                    bundle.putInt(IMPORTANCIA_MIN, seekBar.getSelectedMinValue());
                    bundle.putInt(IMPORTANCIA_MAX, seekBar.getSelectedMaxValue());
                    bundle.putInt(PROBABILIDAD_MIN, seekBarProb.getSelectedMinValue());
                    bundle.putInt(PROBABILIDAD_MAX, seekBarProb.getSelectedMaxValue());

                    if (((TextView) getRootView().findViewById(R.id.filtro_desde)).length() > 0)
                        bundle.putDouble(DESDE_CANTIDAD, Double.parseDouble(((TextView) getRootView().findViewById(R.id.filtro_desde)).getText().toString()));
                    if (((TextView) getRootView().findViewById(R.id.filtro_hasta)).length() > 0)
                        bundle.putDouble(HASTA_CANTIDAD, Double.parseDouble(((TextView) getRootView().findViewById(R.id.filtro_hasta)).getText().toString()));

                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa1)).isChecked())
                        etapas.addAll(Etapa.getEtapasIdsByOrden(getDataBase(), 1));
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa2)).isChecked())
                        etapas.addAll(Etapa.getEtapasIdsByOrden(getDataBase(), 2));
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa3)).isChecked())
                        etapas.addAll(Etapa.getEtapasIdsByOrden(getDataBase(), 3));
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa4)).isChecked())
                        etapas.addAll(Etapa.getEtapasIdsByOrden(getDataBase(), 4));
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_etapa5)).isChecked())
                        etapas.addAll(Etapa.getEtapasIdsByOrden(getDataBase(), 5));

                    if(((CheckBox) getRootView().findViewById(R.id.filtro_activos)).isChecked())
                        estados.add("A");
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_suspendidos)).isChecked())
                        estados.add("S");
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_concretados)).isChecked())
                        estados.add("C");
                    if(((CheckBox) getRootView().findViewById(R.id.filtro_no_concretados)).isChecked())
                        estados.add("NC");

                    for(OportunidadTipo tipo: this.tipos)
                    {
                        tipos.add(tipo.getIdOportunidadTipo());
                    }

                    bundle.putIntegerArrayList(ETAPAS, etapas);
                    bundle.putIntegerArrayList(TIPOS, tipos);
                    bundle.putStringArrayList(ESTADOS, estados);

                    result.putExtra(FILTRO, bundle);

                    filtroListener.onFiltroSend(result);

                }
                break;
            case R.id.action_clean:
                filtroListener.onFiltroClean();
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
            if(desde_creacion.get(Calendar.DAY_OF_YEAR) > hasta_creacion.get(Calendar.DAY_OF_YEAR))
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
                Toast.makeText(getContext(), R.string.message_desde_hasta_cantidad, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}
