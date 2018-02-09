package rp3.auna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rp3.auna.adapter.FilesAdapter;
import rp3.auna.bean.FotoVisita;
import rp3.auna.bean.ItemFile;
import rp3.auna.bean.VisitaVta;
import rp3.auna.customviews.ItemOffsetDecoration;
import rp3.auna.util.helper.Util;
import rp3.auna.util.session.SessionManager;
import rp3.auna.webservices.VisitaFisicaClient;
import rp3.util.Convert;

public class VisitaMediaActivity extends AppCompatActivity {
    private static final String TAG = VisitaMediaActivity.class.getSimpleName();
    private static final int REQUEST_GALLERY = 3;
    private static final int RESULT_GALLERY = 3;
    private static final int REQUEST_MEDIA = 4;
    private static final int RESULT_MEDIA = 4;
    private static final int REQUEST_PHOTO = 5;
    private static final int RESULT_PHOTO = 5;
    @BindView(R.id.toolbarVisitaMedia) Toolbar toolbar;
    @BindView(R.id.statusBarVisitaMedia)FrameLayout statusBar;
    @BindView(R.id.tabsMedia) TabLayout tabLayout;
    @BindView(R.id.rvImage) RecyclerView rvImage;
    @BindView(R.id.rvPdf) RecyclerView rvPdf;
    private TextView tvDocumentos;
    private TextView tvImagenes;
    private ArrayList<String> images;
    private ArrayList<String> documents;
    private ArrayList<String> photo;
    private rp3.auna.models.ventanueva.VisitaVta visitaVta;
    private VisitaVta visita;
    private List<Bitmap> fotos;
    private List<File> fotosFile;
    private List<File> pdfs;
    //
    private FilesAdapter adapterPdf;
    private FilesAdapter adapterImg;
    private List<ItemFile> itemPdfs;
    private List<ItemFile> itemImgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate...");
        setContentView(R.layout.activity_visita_media);
        ButterKnife.bind(this);
        tvDocumentos = (TextView) findViewById(R.id.tvDocumentos);
        tvImagenes = (TextView)findViewById(R.id.tvImagenes);
        tvDocumentos.setText("Documentos");
        tvImagenes.setText("Imagenes");
        toolbarStatusBar();
        navigationBarStatusBar();
        getData();
        initAdapters();
    }

    private void initAdapters(){
        itemPdfs = new ArrayList<>();
        itemImgs = new ArrayList<>();

        adapterPdf = new FilesAdapter(this,  new FilesAdapter.OnItem() {
            @Override
            public void onSelect(int position) {

            }

            @Override
            public void onSelectLong(int position) {

            }
        });
        adapterImg = new FilesAdapter(this, new FilesAdapter.OnItem() {
            @Override
            public void onSelect(int position) {
                Log.d(TAG,"position...");
            }

            @Override
            public void onSelectLong(int position) {

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvPdf.setHasFixedSize(true);
        rvImage.setHasFixedSize(true);
        rvPdf.setItemAnimator(new DefaultItemAnimator());
        rvPdf.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));
        rvPdf.setLayoutManager(layoutManager);
        rvPdf.setAdapter(adapterPdf);

        rvImage.setItemAnimator(new DefaultItemAnimator());
        rvImage.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));
        rvImage.setLayoutManager(layoutManager1);
        rvImage.setAdapter(adapterImg);
    }

    private void getData(){
        visitaVta = SessionManager.getInstance(this).getVisitaSession();
        Calendar calendar = Calendar.getInstance();
        visitaVta.setFechaInicio(calendar.getTime());
        images = new ArrayList<>();
        documents = new ArrayList<>();
        photo = new ArrayList<>();
    }

    private void setDataVisita(){
        visitaVta.setEstado(2);
        visitaVta.setFechaFin(Calendar.getInstance().getTime());
        visitaVta.setVisitaValue(Contants.GENERAL_VALUE_CODE_VISITA_REALIZADA);
        //Iniciar las visitas para enviar
        rp3.auna.bean.VisitaVta obj = new rp3.auna.bean.VisitaVta();
        obj.setEstado(visitaVta.getEstado());
        obj.setLlamadaId(0);
        if(String.valueOf(visitaVta.getVisitaId())!=null){
            obj.setVisitaId(visitaVta.getVisitaId());
        }else{
            obj.setVisitaId(0);
        }
        obj.setIdAgente(visitaVta.getIdAgente());
        obj.setIdCliente(visitaVta.getIdCliente());
        obj.setDuracionCode(visitaVta.getDuracionCode());
        obj.setFechaFin(Convert.getDotNetTicksFromDate(visitaVta.getFechaFin()));
        obj.setFechaInicio(Convert.getDotNetTicksFromDate(visitaVta.getFechaInicio()));
        obj.setFechaVisita(Convert.getDotNetTicksFromDate(visitaVta.getFechaVisita()));
        obj.setDescripcion(visitaVta.getDescripcion());
        obj.setFotos(null);
        obj.setReferidoCount(0);
        obj.setReferidoTabla(visitaVta.getReferidoTabla());
        obj.setLatitud(Float.parseFloat(String.valueOf(visitaVta.getLatitud())));
        obj.setLongitud(Float.parseFloat(String.valueOf(visitaVta.getLongitud())));
        obj.setIdClienteDireccion(visitaVta.getIdClienteDireccion());
        obj.setIdVisita(visitaVta.getIdVisita());
        obj.setMotivoReprogramacionTabla(visitaVta.getMotivoReprogramacionTabla());
        obj.setMotivoReprogramacionValue(visitaVta.getMotivoReprogramacionValue());
        obj.setMotivoVisitaTabla(visitaVta.getMotivoVisitaTabla());
        obj.setMotivoVisitaValue(visitaVta.getMotivoVisitaValue());
        obj.setObservacion(visitaVta.getObservacion());
        obj.setTipoVenta(visitaVta.getTipoVenta());
        obj.setVisitaTabla(visitaVta.getVisitaTabla());
        obj.setVisitaValue(visitaVta.getVisitaValue());
        obj.setTiempoCode(visitaVta.getTiempoCode());
        obj.setReferidoValue(visitaVta.getReferidoValue());
        visita = obj;
        Log.d(TAG,visita.toString());
        setFotosInVisita();
    }

    private void setFotosInVisita(){
        List<FotoVisita> fotoVisitas = new ArrayList<>();
        int idAgente = rp3.configuration.PreferenceManager.getInt(Contants.KEY_IDAGENTE);
        fotos = new ArrayList<>();
        pdfs = new ArrayList<>();
        fotosFile = new ArrayList<>();
        for(String img:images){
            Log.d(TAG,img);
            fotos.add(getBitmap(img));
            fotosFile.add(new File(img));
            FotoVisita fotoVisita =new FotoVisita();
            String nombre = idAgente+ Util.nombreFoto();
            fotoVisita.setEstado(1);
            fotoVisita.setFoto(nombre);
            fotoVisita.setIdFoto(0);
            fotoVisita.setIdVisita(visita.getIdVisita());
            fotoVisitas.add(fotoVisita);
        }
        for (String img:documents){
            Log.d(TAG,img);
            pdfs.add(new File(img));
            FotoVisita fotoVisita =new FotoVisita();
            String nombre = idAgente+ Util.nombrePdf();
            fotoVisita.setEstado(2);
            fotoVisita.setFoto(nombre);
            fotoVisita.setIdFoto(0);
            fotoVisita.setIdVisita(visita.getIdVisita());
            fotoVisitas.add(fotoVisita);
        }

        Log.d(TAG,"fotos:"+fotos.size()+" pdfs:"+pdfs.size());
        visita.setFotos(fotoVisitas);
        showUploadArchivosClient();
    }

    private Bitmap getBitmap(String foto){
        String ruta=foto;
        File file=new File(ruta);
        Bitmap bitmap=null;

        bitmap = BitmapFactory.decodeFile(ruta);
        Log.d(TAG,"Si existe la foto...");
        return bitmap;
    }

    //region Todo

    public void toolbarStatusBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Visita");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void navigationBarStatusBar() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                VisitaMediaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                statusBar.setBackgroundColor(color);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                VisitaMediaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                VisitaMediaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                VisitaMediaActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        if(id==R.id.action_take_photo){
            initGallery(3);
        }
        else if(id==R.id.action_subir_documentos){
            Log.d(TAG,"subir documentos...");
            initGallery(2);
        }
        else if(id==R.id.action_subir_imagenes){
            Log.d(TAG,"subir imagenes...");
            initGallery(1);
        }
        else if(id == R.id.subir_archivos){
            setDataVisita();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initGallery(int estado){
        if(estado==1){
            Intent intent = new Intent(this,MultiPhotoSelectedActivity.class);
            startActivityForResult(intent,REQUEST_GALLERY);
        }else if(estado==2){
            Intent intent = new Intent(this,MultiMediaSelectedActivity.class);
            startActivityForResult(intent,REQUEST_MEDIA);
        }else if(estado==3){
            Intent cameraIntent = new Intent(this,PhotoActivity.class);
            startActivityForResult(cameraIntent,REQUEST_PHOTO);
        }

    }

    private void showUploadArchivosClient(){
        final ProgressDialog progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Espere porfavor...");
        progressDialog.setTitle("Rp3MarketForce");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            new VisitaFisicaClient(this, new Callback() {
                Handler handler = new Handler(Looper.getMainLooper());
                @Override
                public void onFailure(Call call, IOException e) {
                    progressDialog.dismiss();
                    Log.d(TAG,"failure...");
                    Log.d(TAG,"IOEXCEPTION:"+e.getMessage());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VisitaMediaActivity.this,getString(R.string.message_error_sync_connection_http_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    progressDialog.dismiss();
                    Log.d(TAG,"response...");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String json = response.body().string();
                                Log.d(TAG,"bodyjson"+json);
                                if(response.isSuccessful()){
                                    Log.d(TAG,"sucesss...");
                                    Toast.makeText(VisitaMediaActivity.this, "Visita finalizada satisfactoriamente!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),StartActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Log.d(TAG,"No success...");
                                    Toast.makeText(VisitaMediaActivity.this, R.string.message_error_sync_connection_server_fail, Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d(TAG,e.getMessage());
                            }

                        }
                    });
                }
            }).grabar(visita,fotos,pdfs,fotosFile);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }
    }

    //region Ciclo de vida
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_visita_fisica,menu);
        return super.onCreateOptionsMenu(menu);
    }

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
        //super.onBackPressed();
        Log.d(TAG,"onBackPressed...");
        moveTaskToBack(true);
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"onActivityResult...");
        if(requestCode==REQUEST_GALLERY && resultCode == RESULT_GALLERY && null != data){
            Log.d(TAG,"requestCode==REQUEST_GALLERY && resultCode == RESULT_GALLERY...");
            images = new ArrayList<String>();
            if(data.getStringArrayListExtra("fotos")!=null) {
                images.addAll(data.getStringArrayListExtra("fotos"));
                if(images.size()>0){
                    Log.d(TAG,"images.size()>0:"+images.size());
                    //tvImagenes.setText(images.size());
                }
                Log.d(TAG,"Cantidad de fotos seleccionadas:"+images.size());
            }else {
                Log.d(TAG,"no llego ningun clipdata...");
            }
        }
        else if(requestCode==REQUEST_MEDIA && resultCode == RESULT_MEDIA && null != data){
            Log.d(TAG,"requestCode==REQUEST_MEDIA && resultCode == RESULT_MEDIA...");
            documents = new ArrayList<String>();
            if(data.getStringArrayListExtra("fotos")!=null) {
                documents.addAll(data.getStringArrayListExtra("fotos"));
                if(documents.size()>0){
                    Log.d(TAG,"documentos.size()>0:"+documents.size());
                    //tvDocumentos.setText(documents.size());
                }
                Log.d(TAG,"Cantidad de documentos seleccionadas:"+documents.size());
            }else {
                Log.d(TAG,"no llego ningun documentodata...");
            }
        }
        else if(requestCode==REQUEST_PHOTO && resultCode == RESULT_PHOTO && null != data){
            Log.d(TAG,"requestCode==REQUEST_PHOTO && resultCode == RESULT_PHOTO...");
            String path = data.getStringExtra("path");
            photo.add(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
