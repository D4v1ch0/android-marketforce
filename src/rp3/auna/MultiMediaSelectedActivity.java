package rp3.auna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.adapter.DocumentAdapter;
import rp3.auna.adapter.ImageAdapter;
import rp3.auna.customviews.ItemOffsetDecoration;

public class MultiMediaSelectedActivity extends AppCompatActivity {
    private static final String TAG=MultiMediaSelectedActivity.class.getSimpleName();
    @BindView(R.id.statusBarMultiphoto)FrameLayout statusBar;
    @BindView(R.id.fabSendMulti)
    FloatingActionButton fab;
    private static final int RESULT_GALLERY_CODE=4;
    private DocumentAdapter imageAdapter;
    private File dir;
    private ArrayList<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_media_selected);
        ButterKnife.bind(this);
        Log.d(TAG,"onCreate...");
        fileList = new ArrayList<>();
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        navigationBarStatusBar();
        populateImagesFromGallery();
    }

    private ArrayList<String> getPaths(ArrayList<File> fileList){
        ArrayList<String> list = new ArrayList<>();
        if(fileList.size()>0){
            for (File file:fileList){
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    public ArrayList<File> loadPdfFromNativeDocuments(File dir) {
        String pdfPattern = ".pdf";
        //File listFile[] = FileList.listFiles();
        ArrayList<String> list = new ArrayList<>();
        //
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    loadPdfFromNativeDocuments(listFile[i]);
                } else {

                    boolean booleanpdf = false;
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        for (int j = 0; j < fileList.size(); j++) {
                            if (fileList.get(j).getName().equals(listFile[i].getName())) {
                                booleanpdf = true;
                            } else {

                            }
                        }

                        if (booleanpdf) {
                            booleanpdf = false;
                        } else {
                            fileList.add(listFile[i]);

                        }
                    }
                }
            }
        }
        //
        /*if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        list.add(listFile[i].getAbsolutePath());

                    }
            }
        }*/
        Log.d(TAG,"Cantidad de pdf:"+list.size());

        return fileList;
    }

    public void btnChooseDocumentsClick(View v){
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

        if (selectedItems!= null && selectedItems.size() > 0) {
            //Toast.makeText(this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
            if(selectedItems.size()<=2){
                Log.d(TAG,"Total documentos selected: " + selectedItems.size());
                Log.d(TAG, "Selected Items: " + selectedItems.toString());
                Intent intent=new Intent();
                intent.putStringArrayListExtra("fotos",selectedItems);
                setResult(RESULT_GALLERY_CODE,intent);
                finish();
            }else {
                Log.d(TAG,"Disculpe solo puede enviar 2 documentos...");
                Toast.makeText(this, "Solo 2 documentos maximo...", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Seleccione algun documento porfavor..", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Seleccione alguna documento porfavor..");
        }
    }

    private void populateImagesFromGallery() {
        final ProgressDialog progressDialog =new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setTitle("RP3 Market Force");
        progressDialog.setMessage("Espere...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> imageUrls = getPaths(loadPdfFromNativeDocuments(dir));
                initializeRecyclerView(imageUrls);
                progressDialog.dismiss();
            }
        });

    }

    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        if(imageUrls.size()>0){
            Log.d(TAG,"si hay...");
        }else{
            Log.d(TAG,"No hay...");
            Toast.makeText(this, "No hay archivos pdf en el dispositivo...", Toast.LENGTH_SHORT).show();
        }
        imageAdapter = new DocumentAdapter(this, imageUrls, new ImageAdapter.OnItem() {
            @Override
            public void onSelect(int position) {
                Log.d(TAG,"onSelect:estoy en:"+position);
            }

            @Override
            public void onSelectLong(int position) {
                Log.d(TAG,"onLongSelect:estoy en:"+position);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMultiPhotoSelected);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));
        recyclerView.setAdapter(imageAdapter);
        //fab hide - show
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public void navigationBarStatusBar() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                MultiMediaSelectedActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                statusBar.setBackgroundColor(color);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                MultiMediaSelectedActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                MultiMediaSelectedActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                MultiMediaSelectedActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    //region Ciclo de vida


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
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume...");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG,"onBackPressed...");
        finish();
    }
    //endregion
}
