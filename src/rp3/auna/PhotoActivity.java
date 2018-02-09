package rp3.auna;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.util.constants.Constants;
import rp3.auna.util.helper.Util;
import rp3.auna.util.session.SessionManager;
import rp3.configuration.PreferenceManager;

public class PhotoActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private static final String TAG = PhotoActivity.class.getSimpleName();
    private static final int RESULT_PHOTO= 5;
    @BindView(R.id.ivAceptar) ImageView ivAceptar;
    @BindView(R.id.ivCancelar) ImageView ivCancelar;
    @BindView(R.id.ivTomarFoto) ImageView ivTomarFoto;
    @BindView(R.id.ivBackPhotoToolbar) ImageView ivBack;
    @BindView(R.id.ivPreview) ImageView ivPreview;
    @BindView(R.id.surfaceviewPhoto) SurfaceView surfaceView;
    private Camera.PictureCallback jpegCallback;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private String filename;
    private String path = null;
    float mDist = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data =new Intent();
                data.putExtra("path",path);
                setResult(RESULT_PHOTO,data);
                finish();
            }
        });
        ivCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCamera();
            }
        });
        ivTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    captureImage();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,e.getMessage());
                }
            }
        });
        initCamera();
    }

    //region Camera
    private void initCamera(){
        filename= Constants.PATH_IMAGENES;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    if(verificarCarpeta()) {
                        int idAgente = PreferenceManager.getInt(Contants.KEY_IDAGENTE);
                        String name = String.valueOf(idAgente)+ Util.nombreFoto();
                        String nombre = filename + name;
                        path = nombre;
                        outStream = new FileOutputStream(nombre);
                        outStream.write(data);
                        outStream.close();
                        //refreshCamera();
                        Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                        Log.d(TAG,"onPictureTaken Path: "+nombre);
                        File archivo=new File(nombre);
                        Uri uri=getImageContentUri(archivo);
                        if(uri!=null){
                            Log.d(TAG,"Imagen Uri creada...");
                            ivPreview.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext()).load(archivo).into(ivPreview);
                        }else{
                            Log.d(TAG,"Imagen Uri no creada....");
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d(TAG,"FileNotFoundException:"+e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG,"IOException:"+e.getMessage());
                } finally {
                    Log.d(TAG,"finally...");
                }
            }
        };
    }

    public void setCameraDisplayOrientation(Activity activity,int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0;Log.d(TAG,"degress:"+degrees); break;
            case Surface.ROTATION_90: degrees = 90;Log.d(TAG,"degress:"+degrees); break;
            case Surface.ROTATION_180: degrees = 180;Log.d(TAG,"degress:"+degrees); break;
            case Surface.ROTATION_270: degrees = 270;Log.d(TAG,"degress:"+degrees); break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            Log.d(TAG,"info facing front...");
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
            Log.d(TAG,"result:"+result);
        } else {  // back-facing
            Log.d(TAG,"info back-facing...");
            result = (info.orientation - degrees + 360) % 360;
            Log.d(TAG,"result:"+result);
        }
        camera.setDisplayOrientation(result);
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            ivCancelar.setVisibility(View.GONE);
            ivAceptar.setVisibility(View.GONE);
            ivTomarFoto.setVisibility(View.VISIBLE);
            if(path!=null){
                File file = new File(path);
                if(file!=null){
                    file.delete();
                    path = null;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void captureImage() throws IOException {
        //take the picture

        camera.takePicture(null, null, jpegCallback);
        ivTomarFoto.setVisibility(View.GONE);
        ivAceptar.setVisibility(View.VISIBLE);
        ivCancelar.setVisibility(View.VISIBLE);
    }

    //endregion

    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return this.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public boolean verificarCarpeta() {
        boolean estado = true;
        File archivo=new File(filename);
        if (archivo.exists()) {
            estado = true;
            Log.d(TAG,"Carpeta existe existe");
        }

        if (!archivo.exists()) {
            estado = true;
            archivo.mkdir();
            Log.d(TAG,"Carpeta no existe existe");
            //Toast.makeText(getApplicationContext(), "Carpeta ya esta creada", Toast.LENGTH_LONG).show();
        }
        return estado;
    }

    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            // zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            // zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        camera.setParameters(params);
    }

    public void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null
                && supportedFocusModes
                .contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    // currently set to auto-focus on single touch
                }
            });
        }
    }

    /** Determine the space between the first two fingers */
    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Get the pointer ID
        Camera.Parameters params = camera.getParameters();
        int action = event.getAction();

        if (event.getPointerCount() > 1) {
            // handle multi-touch events
            if (action == MotionEvent.ACTION_POINTER_DOWN) {
                mDist = getFingerSpacing(event);
            } else if (action == MotionEvent.ACTION_MOVE
                    && params.isZoomSupported()) {
                camera.cancelAutoFocus();
                handleZoom(event, params);
            }
        } else {
            // handle single touch events
            if (action == MotionEvent.ACTION_UP) {
                handleFocus(event, params);
            }
        }
        return true;
    }

    //region CallBack Camera

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"surfaceCreated...");
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            camera.enableShutterSound(true);
            setCameraDisplayOrientation(this,Camera.CameraInfo.CAMERA_FACING_BACK,camera);
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            Log.d(TAG,"RuntimeException:"+e.getLocalizedMessage());
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG,"surfaceChanged...");
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG,"surfaceDestroyed...");
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    //endregion

    // region Ciclo de vida

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onBackPressed() {
        if(ivAceptar.getVisibility()== View.VISIBLE){
            refreshCamera();
        }else{
            super.onBackPressed();
            finish();
        }
        Log.d(TAG,"onBackPressed...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    //endregion
}
