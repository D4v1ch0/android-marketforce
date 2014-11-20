package rp3.marketforce.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {
	public static String getImageDPISufix(Context ctx, String foto)
	{
		if(foto!=null)
		{
			String sufix = "";
			if(ctx.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_MEDIUM)
				sufix = "_mdpi";
			if(ctx.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_HIGH)
				sufix = "_hdpi";
			if(ctx.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_XHIGH)
				sufix = "_xhdpi";
			if(ctx.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_XXHIGH)
				sufix = "_xxhdpi";
			
			String nom = foto.substring(0, foto.indexOf("."));
			String ext = foto.substring(foto.indexOf("."), foto.length());
			
			foto = nom + sufix + ext;
			
			return foto;
		}
		return "";
	}
}
