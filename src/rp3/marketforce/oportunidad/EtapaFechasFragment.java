package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.marketforce.models.oportunidad.Oportunidad;
import rp3.marketforce.models.oportunidad.OportunidadBitacora;
import rp3.marketforce.models.oportunidad.OportunidadEtapa;
import rp3.util.CalendarUtils;

/**
 * Created by magno_000 on 01/12/2015.
 */
public class EtapaFechasFragment extends BaseFragment {

    public static final String ID_ETAPA = "ID_ETAPA";

    public long idEtapa;
    public static EtapaFechasFragment newInstance(long id)
    {
        Bundle args = new Bundle();
        args.putLong(ID_ETAPA, id);
        EtapaFechasFragment fragment = new EtapaFechasFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_etapa_fechas);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        idEtapa = getArguments().getLong(ID_ETAPA);

        OportunidadEtapa etp = OportunidadEtapa.getEtapaOportunidad(getDataBase(), idEtapa);
        SimpleDateFormat format1 = new SimpleDateFormat("EE dd/MM/yy");

        long diasProgramado = -1, diasReal = -1, fechaInicioProg = -1;

        //Se obtiene fecha inicial planeada
        List<OportunidadEtapa> optEtapa = OportunidadEtapa.getEtapasOportunidad(getDataBase(), etp.getIdOportunidad());
        if(optEtapa.size() == 0)
            optEtapa = OportunidadEtapa.getEtapasOportunidadInt(getDataBase(), etp.get_idOportunidad());

        boolean etapaEncontrada = false;
        for(OportunidadEtapa etapa : optEtapa)
        {
            if(etapa.getEtapa().getIdEtapaPadre() == 0 && !etapaEncontrada)
            {
                if(fechaInicioProg == -1)
                {
                    fechaInicioProg = etapa.getFechaInicio().getTime();
                }
                if(etapa.getID() == etp.getID())
                {
                    etapaEncontrada = true;
                }
                else
                {
                    if(etapa.getFechaFinPlan() != null && etapa.getFechaFinPlan().getTime() > 0)
                    {
                        fechaInicioProg = etapa.getFechaFinPlan().getTime();
                    }
                    else
                    {
                        if(etapa.getEtapa().getDias() == 0)
                        {
                            etapaEncontrada = true;
                            fechaInicioProg = -1;
                        }
                        else
                        {
                            Calendar cl = Calendar.getInstance();
                            cl.setTimeInMillis(fechaInicioProg);
                            cl.add(Calendar.DATE, etapa.getEtapa().getDias());
                            fechaInicioProg = cl.getTime().getTime();
                        }
                    }
                }
            }
        }

        //Se obtiene dias Reales
        if(etp.getFechaInicio() != null && etp.getFechaInicio().getTime() > 0 && etp.getFechaFin() != null && etp.getFechaFin().getTime() > 0)
        {
            Calendar fecIni = Calendar.getInstance();
            Calendar fecFin = Calendar.getInstance();
            fecIni.setTime(etp.getFechaInicio());
            fecFin.setTime(etp.getFechaFin());

            diasReal = CalendarUtils.DayDiff(fecFin, fecIni);
        }

        //Se obtiene dias Programados
        if(fechaInicioProg != -1 && etp.getFechaFinPlan() != null && etp.getFechaFinPlan().getTime() > 0)
        {
            Calendar cl = Calendar.getInstance();
            Calendar finPlan = Calendar.getInstance();
            cl.setTimeInMillis(fechaInicioProg);
            finPlan.setTime(etp.getFechaFinPlan());

            diasProgramado = CalendarUtils.DayDiff(finPlan, cl);

        }


        if(etp.getFechaInicio() != null && etp.getFechaInicio().getTime() > 0)
            ((TextView) rootView.findViewById(R.id.etapa_fecha_inicio)).setText(format1.format(etp.getFechaInicio()));
        if(etp.getFechaFin() != null && etp.getFechaFin().getTime() > 0)
            ((TextView) rootView.findViewById(R.id.etapa_fecha_fin)).setText(format1.format(etp.getFechaFin()));
        if(etp.getFechaFinPlan() != null && etp.getFechaFinPlan().getTime() > 0)
            ((TextView) rootView.findViewById(R.id.etapa_fecha_plan)).setText(format1.format(etp.getFechaFinPlan()));
        if(diasReal >= 0)
            ((TextView) rootView.findViewById(R.id.etapa_dias_real)).setText(diasReal + " Día(s)");
        if(diasProgramado >= 0)
            ((TextView) rootView.findViewById(R.id.etapa_dias_programado)).setText(diasProgramado + " Día(s)");
        if(fechaInicioProg != -1)
            ((TextView) rootView.findViewById(R.id.etapa_fecha_ini_plan)).setText(format1.format(new Date(fechaInicioProg)));
    }
}