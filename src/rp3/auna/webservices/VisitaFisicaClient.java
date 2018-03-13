package rp3.auna.webservices;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import rp3.auna.R;
import rp3.auna.bean.FotoVisita;
import rp3.auna.bean.VisitaVta;

/**
 * Created by Jesus Villa on 29/11/2017.
 */

public class VisitaFisicaClient {
    private static final String TAG = VisitaFisicaClient.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_MULTIPART= MediaType.parse("multipart/form-data");
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final MediaType MEDIA_TYPE_PDF= MediaType.parse("application/pdf");
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String resource = "ventanueva/VisitaMedia";
    private Context context;
    private Callback callBack;

    public VisitaFisicaClient(Context context, Callback callback){
        this.callBack = callback;
        this.context = context;
    }

    public void grabar(VisitaVta visitaVta, List<Bitmap> fotos, List<File> pdf,List<File> fotoFiles) throws IOException {
        final String json = new Gson().toJson(visitaVta);
        Log.d(TAG,"json:"+json);
        final String url = context.getResources().getString(R.string.url)+resource;
        final List<byte[]> fotoData = new ArrayList<>();
        final List<byte[]> pdfData = new ArrayList<>();
        final List<String> fotoNombres = new ArrayList<>();
        final List<String> pdfNombres = new ArrayList<>();
        for(FotoVisita fotoVisita:visitaVta.getFotos()){
            String nombre = "";
            if(fotoVisita.getEstado()==1){
                nombre = fotoVisita.getFoto();
                fotoNombres.add(nombre);
            }else if(fotoVisita.getEstado()==2){
                nombre = fotoVisita.getFoto();
                pdfNombres.add(nombre);
            }
        }
        for(Bitmap a:fotos){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            a.compress(Bitmap.CompressFormat.JPEG,100, stream);
            final byte[] bitmapdata = stream.toByteArray();
            fotoData.add(bitmapdata);


        }
        for(File b:pdf){
            /*byte[] data = null;
            RandomAccessFile f = new RandomAccessFile(b, "r");
            try {
                // Get and check length
                long longlength = 0;
                try {
                    longlength = f.length();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int length = (int) longlength;
                if (length != longlength)
                    throw new IOException("File size >= 2 GB");
                // Read file and return data
                data = new byte[length];
                f.readFully(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                f.close();
            }*/
            pdfData.add(FileUtils.readFileToByteArray(b));
        }

        Log.d(TAG,"PDF_DATA:"+pdfData.size());
        Log.d(TAG,"FOTO_DATA:"+fotoData.size());
        MediaType mediaType = MediaType.parse("");
        /*MultipartBody.Part[] fileParts = new MultipartBody.Part[(pdfData.size())];
        for (int i = 0; i < pdf.size(); i++) {
            File file = new File(pdf.get(i).getPath());
            RequestBody fileBody = RequestBody.create(mediaType, file);
            //Setting the file name as an empty string here causes the same issue, which is sending the request successfully without saving the files in the backend, so don't neglect the file name parameter.
            fileParts[i] = MultipartBody.Part.createFormData(String.format(Locale.ENGLISH, "files[%d]", i), file.getName(), fileBody);
        }*/


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for(int i = 0 ; i<fotoData.size();i++){
            /*byte[]a = fotoData.get(i);
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"Fotos\"; filename=\""+fotoNombres.get(i)+""),
                    RequestBody.create(MEDIA_TYPE_JPEG,fotoFiles.get(i)));*/
            builder.addFormDataPart("Fotos", fotoNombres.get(i),
                    RequestBody.create(MEDIA_TYPE_JPEG, fotoFiles.get(i)));
        }
        for(int i = 0 ; i<pdfData.size();i++){
            /*byte[]b = pdfData.get(i);
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"Pdfs\"; filename=\""+pdfNombres.get(i)),
                    RequestBody.create(MEDIA_TYPE_PDF,pdf.get(i)));*/
            builder.addFormDataPart("Pdfs", pdfNombres.get(i),
                    RequestBody.create(MEDIA_TYPE_PDF, pdf.get(i)));
        }
        /*builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"Visita\""),
                RequestBody.create(null,json));*/

        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"Visita\""),
                RequestBody.create(JSON,json));
       // builder.addFormDataPart("Visita",null,);

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                //.addHeader("content-type", "application/json")
                .post(requestBody).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Builder b = new Builder();
        b.connectTimeout(280, TimeUnit.SECONDS)
                .writeTimeout(280, TimeUnit.SECONDS)
                .readTimeout(280, TimeUnit.SECONDS);
// set other properties

        OkHttpClient client = b.build();
        client.newCall(request).enqueue(callBack);
    }
}
