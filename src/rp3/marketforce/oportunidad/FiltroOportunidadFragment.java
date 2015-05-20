package rp3.marketforce.oportunidad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import rp3.app.BaseFragment;
import rp3.marketforce.R;
import rp3.widget.RangeSeekBar;

/**
 * Created by magno_000 on 15/05/2015.
 */
public class FiltroOportunidadFragment extends BaseFragment {

    private RangeSeekBar<Integer> seekBar;

    public static FiltroOportunidadFragment newInstance() {
        return new FiltroOportunidadFragment();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

        seekBar = new RangeSeekBar<Integer>(0, 100, this.getContext());
        //seekBar.setImageResource(R.drawable.pestana_prob);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                ((TextView)rootView.findViewById(R.id.filtro_min)).setText(minValue + " %");
                ((TextView)rootView.findViewById(R.id.filtro_max)).setText(maxValue + " %");
            }
        });

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.filtro_probablidad);
        layout.addView(seekBar);

    }
}
