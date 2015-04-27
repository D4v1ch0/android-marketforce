package rp3.marketforce.caja;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.ValueDependentColor;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.marketforce.Contants;
import rp3.marketforce.R;
import rp3.marketforce.models.AgenteResumen;
import rp3.marketforce.models.Caja;
import rp3.marketforce.resumen.ResumenAdapter;
import rp3.marketforce.utils.DetailsPageAdapter;

/**
 * Created by magno_000 on 24/04/2015.
 */
public class CajaFragment extends BaseFragment {
    Caja caja;
    public static Fragment newInstance() {
        return new CajaFragment();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setRetainInstance(true);
        setContentView(R.layout.fragment_caja);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Caja # " + PreferenceManager.getInt(Contants.KEY_ID_CAJA));
        caja = Caja.getCajaActiva(getDataBase());
        if(caja.getIdCajaControl() != 0)
        {
            getRootView().findViewById(R.id.caja_contenido).setVisibility(View.VISIBLE);
            ((TextView) getRootView().findViewById(R.id.caja_estado)).setText(getResources().getText(R.string.label_abierta));
            ((Button) getRootView().findViewById(R.id.caja_apertura)).setText(getResources().getText(R.string.action_cerrar_caja));
            ((TextView) getRootView().findViewById(R.id.caja_arqueo_total)).setText("$ " + caja.getMontoApertura());
            ((TextView) getRootView().findViewById(R.id.caja_arqueo_apertura)).setText("$ " + caja.getMontoApertura());

            ((Button) getRootView().findViewById(R.id.caja_apertura)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogConfirmation(1, R.string.message_confirmacion_cierre_caja, R.string.title_option_caja);
                }
            });
        }
        else
        {
            ((Button) getRootView().findViewById(R.id.caja_apertura)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), MontoActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onPositiveConfirmation(int id) {
        super.onPositiveConfirmation(id);
        caja.setActivo(false);
        caja.setFechaCierre(Calendar.getInstance().getTime());
        Caja.update(getDataBase(), caja);
        getRootView().findViewById(R.id.caja_contenido).setVisibility(View.GONE);
        ((TextView) getRootView().findViewById(R.id.caja_estado)).setText(getResources().getText(R.string.label_cerrada));
        ((Button) getRootView().findViewById(R.id.caja_apertura)).setText(getResources().getText(R.string.action_abrir_caja));
        ((Button) getRootView().findViewById(R.id.caja_apertura)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MontoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }

    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

    }
}
