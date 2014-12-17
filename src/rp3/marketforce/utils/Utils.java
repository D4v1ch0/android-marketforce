package rp3.marketforce.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static String getImageDPISufix(Context ctx, String foto)
	{
		if(foto!=null)
		{
			try
			{
				String sufix = "";
				if(ctx.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_MEDIUM)
					sufix = "_min";
				if(ctx.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_HIGH)
					sufix = "_sma";
				if(ctx.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_XHIGH)
					sufix = "_sma";
				if(ctx.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_XXHIGH)
					sufix = "_med";
				
				String nom = foto.substring(0, foto.indexOf("."));
				String ext = foto.substring(foto.indexOf("."), foto.length());
				
				foto = nom + sufix + ext;
				
				return foto;
			}
			catch(Exception ex)
			{
				return "";
			}
		}
		return "";
	}
	
	public static Bitmap resizeBitMapImage(String filePath, int targetWidth,
            int targetHeight) {
        Bitmap bitMapImage = null;
        // First, get the dimensions of the image
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        double sampleSize = 0;
        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math
                .abs(options.outWidth - targetWidth);

        if (options.outHeight * options.outWidth * 2 >= 1638) {
            // Load, scaling to smallest power of 2 that'll get it <= desired
            // dimensions
            sampleSize = scaleByHeight ? options.outHeight / targetHeight
                    : options.outWidth / targetWidth;
            sampleSize = (int) Math.pow(2d,
                    Math.floor(Math.log(sampleSize) / Math.log(2d)));
        }
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[128];
        while (true) {
            try {
                options.inSampleSize = (int) sampleSize;
                bitMapImage = BitmapFactory.decodeFile(filePath, options);

                break;
            } catch (Exception ex) {
                try {
                    sampleSize = sampleSize * 2;
                } catch (Exception ex1) {

                }
            }
        }

        return bitMapImage;
    }
	
	public static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MarketForce");

	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	public static String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	public static String BitmapToBase64(String filename)
	{
		Bitmap bm = BitmapFactory.decodeFile(filename);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object   
		byte[] b = baos.toByteArray(); 
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encodedImage;
	}
	
	public static String CroppedBitmapToBase64(String filename)
	{
		Bitmap bm = BitmapFactory.decodeFile(filename);
		int menor = 0;
		menor = Math.min(bm.getWidth(), bm.getHeight());
		Bitmap croppedBmp = Bitmap.createBitmap(bm, 0, 0, menor, menor);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		croppedBmp.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object   
		byte[] b = baos.toByteArray(); 
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encodedImage;
	}
}
