package rp3.auna;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rp3.auna.adapter.ImageAdapter;
import rp3.auna.customviews.ItemOffsetDecoration;

public class MultiPhotoSelectedActivity extends AppCompatActivity {
    private static final String TAG=MultiPhotoSelectedActivity.class.getSimpleName();
    @BindView(R.id.statusBarMultiphoto)FrameLayout statusBar;
    @BindView(R.id.fabSendMulti) FloatingActionButton fab;
    private static final int RESULT_GALLERY_CODE=3;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_phot_selected);
        ButterKnife.bind(this);
        navigationBarStatusBar();
        populateImagesFromGallery();
    }

    public void btnChoosePhotosClick(View v){
        ArrayList<String> selectedItems = imageAdapter.getCheckedItems();

        if (selectedItems!= null && selectedItems.size() > 0) {
            //Toast.makeText(this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
            if(selectedItems.size()<=3){
                Log.d(TAG,"Total photos selected: " + selectedItems.size());
                Log.d(TAG, "Selected Items: " + selectedItems.toString());
                Intent intent=new Intent();
                intent.putStringArrayListExtra("fotos",selectedItems);
                setResult(RESULT_GALLERY_CODE,intent);
                finish();
            }else {
                Log.d(TAG,"Disculpe solo puede enviar 3 documentos...");
                Toast.makeText(this, "Solo 3 fotos maximo...", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Seleccione alguna imagen porfavor..", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Seleccione alguna imagen porfavor..");
        }
    }

    private void populateImagesFromGallery() {
        ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
        initializeRecyclerView(imageUrls);
    }

    private ArrayList<String> loadPhotosFromNativeGallery() {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");
        ArrayList<String> imageUrls = new ArrayList<String>();
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            imageUrls.add(imagecursor.getString(dataColumnIndex));
            System.out.println("=====> Array path => "+imageUrls.get(i));
        }
        return imageUrls;
    }

    private void initializeRecyclerView(ArrayList<String> imageUrls) {
        imageAdapter = new ImageAdapter(this, imageUrls, new ImageAdapter.OnItem() {
            @Override
            public void onSelect(int position) {
                Log.d(TAG,"onSelect:estoy en:"+position);
            }

            @Override
            public void onSelectLong(int position) {
                Log.d(TAG,"onLongSelect:estoy en:"+position);
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3);
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
                MultiPhotoSelectedActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                statusBar.setBackgroundColor(color);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                MultiPhotoSelectedActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                MultiPhotoSelectedActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                MultiPhotoSelectedActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
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
