package rp3.marketforce.oportunidad;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import rp3.app.BaseActivity;

/**
 * Created by magno_000 on 28/05/2015.
 */
public class CambiarEstadoActivity extends BaseActivity {

    private CambiarEstadoFragment newFragment;

    public final static String ARG_ID = "id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        params.y = params.y + 50;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onCreate(savedInstanceState);
        long id = 0;
        id = getIntent().getLongExtra(ARG_ID, 0);
        setContentView(rp3.core.R.layout.layout_simple_content);

        if (!hasFragment(rp3.core.R.id.content)) {
            newFragment = CambiarEstadoFragment.newInstance(id);
            setFragment(rp3.core.R.id.content, newFragment);
        }
    }
}
