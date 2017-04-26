package rp3.marketforce.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
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
            int targetHeight, int orientation) {
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

        if(orientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitMapImage = Bitmap.createBitmap(bitMapImage, 0, 0, bitMapImage.getWidth(), bitMapImage.getHeight(), matrix, true);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filePath);
                bitMapImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(orientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(180);
            bitMapImage = Bitmap.createBitmap(bitMapImage, 0, 0, bitMapImage.getWidth(), bitMapImage.getHeight(), matrix, true);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filePath);
                bitMapImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitMapImage;
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
	public static File getOutputMediaFile(int type){

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
	
	public static File getOutputMediaFile(int type, String suffix){

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
	        "IMG_"+ timeStamp + suffix + ".jpg");
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
		try
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
		catch(Exception ex)
		{
			return null;
		}
	}
	
	public static String convertToSMSNumber(String number)
	{
		if(number.length() >= 0 && number.startsWith("0"))
		{
			number = number.substring(1);
			number = "+593" + number;
		}
		return number;
	}
	
	public static void ErrorToFile(Exception ex)
	{
		File file = new File(Environment.getExternalStorageDirectory()+ "/test.log");
		PrintStream ps = null;
		try {
			ps = new PrintStream( new FileOutputStream(file, true));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ps.append("\r\n");
		ex.printStackTrace(ps);
		ps.close();
	}
	
	public static void ErrorToFile(String ex)
	{
		File file = new File(Environment.getExternalStorageDirectory()+ "/test.log");
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(file, true));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ps.append("\r\n");
		ps.append(ex);
		ps.close();
	}

    public static String getImagesPath()
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MarketForce");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        return mediaStorageDir.getPath() + "/";
    }

    public static String SaveBitmap(Bitmap bitmap, String filename)
    {
        filename = Utils.getImagesPath() + filename;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filename;
    }

    public static int getDayOfWeek(Calendar cal)
    {
        switch (cal.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.MONDAY: return 1;
            case Calendar.TUESDAY: return 2;
            case Calendar.WEDNESDAY: return 3;
            case Calendar.THURSDAY: return 4;
            case Calendar.FRIDAY: return 5;
            case Calendar.SATURDAY: return 6;
            case Calendar.SUNDAY: return 7;
            default: return 1;
        }
    }

    public static double getDescuento(double auto, double manual)
	{
		double porc = 0;
		double valor = 100;
		valor = valor - (valor * auto);
		valor = valor - (valor * manual);
		valor = valor / 100;
		valor = 1 - valor;
		return valor;
	}
}
