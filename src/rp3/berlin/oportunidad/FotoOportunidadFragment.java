package rp3.berlin.oportunidad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import rp3.app.BaseFragment;
import rp3.configuration.PreferenceManager;
import rp3.berlin.Contants;
import rp3.berlin.R;
import rp3.berlin.models.oportunidad.OportunidadContacto;
import rp3.berlin.models.oportunidad.OportunidadFoto;
import rp3.berlin.utils.DrawableManager;

/**
 * Created by magno_000 on 25/05/2015.
 */
public class FotoOportunidadFragment extends BaseFragment {

    private long id;
    private int tipo;
    private DrawableManager DManager;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    public static FotoOportunidadFragment newInstance(long id, int tipo ) {
        Bundle arguments = new Bundle();
        arguments.putLong(FotoOportunidadActivity.ARG_ID, id);
        arguments.putInt(FotoOportunidadActivity.ARG_TIPO, tipo);
        FotoOportunidadFragment fragment = new FotoOportunidadFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(FotoOportunidadActivity.ARG_ID)) {
            id = getArguments().getLong(FotoOportunidadActivity.ARG_ID);
        }else if(savedInstanceState!=null){
            id = savedInstanceState.getLong(FotoOportunidadActivity.ARG_ID);
        }
        if (getArguments().containsKey(FotoOportunidadActivity.ARG_TIPO)) {
            tipo = getArguments().getInt(FotoOportunidadActivity.ARG_TIPO);
        }else if(savedInstanceState!=null){
            tipo = savedInstanceState.getInt(FotoOportunidadActivity.ARG_TIPO);
        }
        DManager = new DrawableManager();

        super.setContentView(R.layout.fragment_foto_oportunidad);
    }

    @Override
    public void onFragmentCreateView(View rootView, Bundle savedInstanceState) {
        super.onFragmentCreateView(rootView, savedInstanceState);
        if(id != 0)
        {
            switch (tipo)
            {
                case 1:
                    OportunidadContacto opCont = OportunidadContacto.getContactoInt(getDataBase(), id);
                    DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                                    rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opCont.getURLFoto().replace("\"",""),
                            (ImageView) this.getRootView().findViewById(R.id.image_set));
                    break;
                case 2:
                    OportunidadFoto opFoto = OportunidadFoto.getFotoInt(getDataBase(), id);
                    DManager.fetchDrawableOnThreadOnline(PreferenceManager.getString("server") +
                                    rp3.configuration.Configuration.getAppConfiguration().get(Contants.IMAGE_FOLDER_OPORTUNIDADES) + opFoto.getURLFoto().replace("\"",""),
                            (ImageView) this.getRootView().findViewById(R.id.image_set));
                    break;
            }

            ((ImageView) this.getRootView().findViewById(R.id.image_set)).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ImageView view = (ImageView) v;
                    System.out.println("matrix=" + savedMatrix.toString());
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            savedMatrix.set(matrix);
                            startPoint.set(event.getX(), event.getY());
                            mode = DRAG;
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            oldDist = spacing(event);
                            if (oldDist > 10f) {
                                savedMatrix.set(matrix);
                                midPoint(midPoint, event);
                                mode = ZOOM;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            mode = NONE;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (mode == DRAG) {
                                matrix.set(savedMatrix);
                                matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                            } else if (mode == ZOOM) {
                                float newDist = spacing(event);
                                if (newDist > 10f) {
                                    matrix.set(savedMatrix);
                                    float scale = newDist / oldDist;
                                    matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                                }
                            }
                            break;
                    }
                    view.setImageMatrix(matrix);
                    return true;
                }

                @SuppressLint("FloatMath")
                private float spacing(MotionEvent event) {
                    float x = event.getX(0) - event.getX(1);
                    float y = event.getY(0) - event.getY(1);
                    return (float)Math.sqrt(x * x + y * y);
                }

                private void midPoint(PointF point, MotionEvent event) {
                    float x = event.getX(0) + event.getX(1);
                    float y = event.getY(0) + event.getY(1);
                    point.set(x / 2, y / 2);
                }
            });
            float image_height = ((ImageView) this.getRootView().findViewById(R.id.image_set)).getDrawable().getIntrinsicHeight();
            float image_width = ((ImageView) this.getRootView().findViewById(R.id.image_set)).getDrawable().getIntrinsicWidth();
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            float screen_width = wm.getDefaultDisplay().getWidth();
            float screen_height = wm.getDefaultDisplay().getHeight();
            RectF drawableRect = new RectF(0, 0, image_width, image_height);
            RectF viewRect = new RectF(0, 0, screen_width, screen_height);
            matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
            ((ImageView) this.getRootView().findViewById(R.id.image_set)).setImageMatrix(matrix);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ImageView) this.getRootView().findViewById(R.id.image_set)).setImageDrawable(null);
    }
}