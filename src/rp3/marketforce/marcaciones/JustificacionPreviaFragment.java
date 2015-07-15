package rp3.marketforce.marcaciones;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rp3.app.BaseFragment;
import rp3.content.SimpleGeneralValueAdapter;
import rp3.data.models.GeneralValue;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.marcacion.Justificacion;
import rp3.marketforce.sync.SyncAdapter;
import rp3.util.StringUtils;

/**
 * Created by magno_000 on 17/06/2015.
 */
public class JustificacionPreviaFragment extends BaseFragment {

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private CaldroidFragment caldroidFragment;
    private Calendar fecha;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fecha = Calendar.getInstance();
        setContentView(R.layout.fragment_justificaciones_previas, R.menu.fragment_crear_cliente);
    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        setCalendar();
        ((CheckBox)rootView.findViewById(R.id.justificacion_ausencia)).setChecked(true);
        SimpleGeneralValueAdapter valueAdapter = new SimpleGeneralValueAdapter(getContext(), getDataBase(), Contants.GENERAL_TABLE_MOTIVO_PERMISO);
        ((Spinner)rootView.findViewById(R.id.justificacion_spinner)).setAdapter(valueAdapter);
        rootView.findViewById(R.id.voice_to_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        ((CheckBox)rootView.findViewById(R.id.justificacion_atraso)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    ((CheckBox)rootView.findViewById(R.id.justificacion_ausencia)).setChecked(false);
                else
                    ((CheckBox)rootView.findViewById(R.id.justificacion_ausencia)).setChecked(true);
            }
        });
        ((CheckBox)rootView.findViewById(R.id.justificacion_ausencia)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    ((CheckBox)rootView.findViewById(R.id.justificacion_atraso)).setChecked(false);
                else
                    ((CheckBox)rootView.findViewById(R.id.justificacion_atraso)).setChecked(true);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_save:
                Justificacion justificacion = new Justificacion();
                justificacion.setFecha(fecha.getTime());
                justificacion.setPropia(true);
                if(((TextView)getRootView().findViewById(R.id.justificacion_text)).length() > 0)
                    justificacion.setObservacion(((TextView)getRootView().findViewById(R.id.justificacion_text)).getText().toString());
                else
                    justificacion.setObservacion("");
                justificacion.setTipo(((GeneralValue) ((Spinner) getRootView().findViewById(R.id.justificacion_spinner)).getSelectedItem()).getCode());
                justificacion.setAusencia( ((CheckBox)getRootView().findViewById(R.id.justificacion_ausencia)).isChecked());
                justificacion.setPendiente(true);
                Justificacion.insert(getDataBase(), justificacion);
                Bundle bundle = new Bundle();
                bundle.putString(SyncAdapter.ARG_SYNC_TYPE, SyncAdapter.SYNC_TYPE_PERMISO_PREVIO);
                requestSync(bundle);
                finish();
                break;
            case R.id.action_cancel:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hable Ahora");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Dispositivo no soporta voz a texto.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setCalendar()
    {
        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        caldroidFragment.setArguments(args);

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.crear_visita_calendar, caldroidFragment);
        t.commit();

        caldroidFragment.setMinDate(Calendar.getInstance().getTime());
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                caldroidFragment.setCalendarDate(date);
                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, fecha.getTime());
                caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, date);
                int horas = fecha.get(Calendar.HOUR_OF_DAY);
                int minutos = fecha.get(Calendar.MINUTE);
                fecha.setTime(date);
                fecha.set(Calendar.HOUR_OF_DAY, horas);
                fecha.set(Calendar.MINUTE, minutos);
            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
            }

        };
        caldroidFragment.setCaldroidListener(listener);
        caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, Calendar.getInstance().getTime());
        caldroidFragment.setBackgroundResourceForDate(R.drawable.blue_border_date, fecha.getTime());
    }

    public static JustificacionPreviaFragment newInstance() {
        return new JustificacionPreviaFragment();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT:
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        ((TextView)getRootView().findViewById(R.id.justificacion_text)).setText(StringUtils.getStringCapSentence(result.get(0)));
                    }
                    break;
            }
        }
    }

    private int getPosition(SpinnerAdapter spinnerAdapter, String i)
    {
        int position = -1;
        for(int f = 0; f < spinnerAdapter.getCount(); f++)
        {
            if(((GeneralValue)spinnerAdapter.getItem(f)).getCode().equals(i))
                position = f;
        }
        return position;
    }
}


