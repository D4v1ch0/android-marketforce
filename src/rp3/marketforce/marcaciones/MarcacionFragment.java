package rp3.marketforce.marcaciones;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import com.github.lzyzsd.circleprogress.DonutProgress;

import rp3.app.BaseFragment;
import rp3.marketforce.R;

/**
 * Created by magno_000 on 05/06/2015.
 */
public class MarcacionFragment extends BaseFragment {
    public static MarcacionFragment newInstance() {
        return new MarcacionFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setContentView(R.layout.fragment_marcacion);

    }

    @Override
    public void onFragmentCreateView(final View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);

        ((DonutProgress)rootView.findViewById(R.id.donut_inicio_jornada)).setMax(4000);
        rootView.findViewById(R.id.button_inicio_jornada).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new CountDownTimer(4000,100){

                    @Override
                    public void onTick(long l) {
                        ((DonutProgress)rootView.findViewById(R.id.donut_inicio_jornada)).setProgress((int)(l));
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
                return false;
            }
        });
    }
}
