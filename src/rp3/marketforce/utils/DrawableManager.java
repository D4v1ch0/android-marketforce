package rp3.marketforce.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import rp3.marketforce.R;
import rp3.util.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class DrawableManager {
	private static LruCache<String, Bitmap> mMemoryCache;
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 10; // 10MB - Tamaño de cache
	private Context ctx;
	//private final Map<String, Drawable> drawableMap;

    public DrawableManager() {
        //drawableMap = new HashMap<String, Drawable>();
    	final int cacheSize = DEFAULT_MEM_CACHE_SIZE;

	    // Use 1/8th of the available memory for this memory cache.
	    //final int cacheSize = 1024 * 1024 * memClass / 8;
	    
	    if(mMemoryCache == null)
	    {
	    	mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
	        	@Override
	        	protected int sizeOf(String key, Bitmap bitmap) {
	            	// The cache size will be measured in bytes rather than number of items.
	            	return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
	        	}
	    	};
	    }
    }

    public Drawable fetchDrawable(String urlString) {
        if (mMemoryCache.get(urlString) != null) {
            return new BitmapDrawable(mMemoryCache.get(urlString));
        }

        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Drawable resp;

            if (bitmap != null) {
            	mMemoryCache.put(urlString, bitmap);
            	resp = new BitmapDrawable(bitmap);
            } else {
            	resp = ctx.getResources().getDrawable(R.drawable.user);
              Log.w(this.getClass().getSimpleName(), "could not get thumbnail");
            }

			return resp;
        } catch (MalformedURLException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return ctx.getResources().getDrawable(R.drawable.user);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return ctx.getResources().getDrawable(R.drawable.user);
        }
    }
    
    public Bitmap fetchBitmap(String urlString) {
        if (mMemoryCache.get(urlString) != null) {
            return mMemoryCache.get(urlString);
        }

        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            if (bitmap != null) {
            	mMemoryCache.put(urlString, bitmap);
            } else {
              Log.w(this.getClass().getSimpleName(), "could not get thumbnail");
            }

			return bitmap;
        } catch (MalformedURLException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return null;
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return null;
        }
    }

    public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
    	ctx = imageView.getContext();
        if (mMemoryCache.get(urlString) != null) {
            imageView.setImageDrawable(new BitmapDrawable(mMemoryCache.get(urlString)));
        }
        else
        {

        	final Handler handler = new Handler() {
            	@Override
            	public void handleMessage(Message message) {
                	imageView.setImageDrawable((Drawable) message.obj);
            	}
        	};

        	Thread thread = new Thread() {
            	@Override
            	public void run() {
            		//	TODO : set imageView to a "pending" image
                	Drawable drawable = fetchDrawable(urlString);
                	Message message = handler.obtainMessage(1, drawable);
                	handler.sendMessage(message);
            	}
        	};
        	thread.start();
        }
    }
    
    public void fetchDrawableOnThreadRounded(final String urlString, final ImageView imageView) {
    	ctx = imageView.getContext();
        if (mMemoryCache.get(urlString) != null) {
        	Bitmap bitmap = mMemoryCache.get(urlString);
            bitmap = BitmapUtils.getRoundedRectBitmap(mMemoryCache.get(urlString), bitmap.getHeight() );
            imageView.setImageBitmap(bitmap);
        }
        else
        {

        	final Handler handler = new Handler() {
            	@Override
            	public void handleMessage(Message message) {
                	Bitmap bitmap = (Bitmap) message.obj;
                	if(bitmap != null)
                	{
                		bitmap = BitmapUtils.getRoundedRectBitmap(mMemoryCache.get(urlString), bitmap.getHeight());
                		imageView.setImageBitmap(bitmap);
                	}
            	}
        	};

        	Thread thread = new Thread() {
            	@Override
            	public void run() {
            		//	TODO : set imageView to a "pending" image
                	Bitmap drawable = fetchBitmap(urlString);
                	Message message = handler.obtainMessage(1, drawable);
                	handler.sendMessage(message);
            	}
        	};
        	thread.start();
        }
    }
    
    public void fetchDrawableOnThread(final String urlString, final LinearLayout imageView) {
    	ctx = imageView.getContext();
        if (mMemoryCache.get(urlString) != null) {
            imageView.setBackgroundDrawable(new BitmapDrawable(mMemoryCache.get(urlString)));
        }
        else
        {

        	final Handler handler = new Handler() {
        		@Override
        		public void handleMessage(Message message) {
        			imageView.setBackgroundDrawable((Drawable) message.obj);
        		}
        	};

        	Thread thread = new Thread() {
        		@Override
        		public void run() {
        			//TODO : set imageView to a "pending" image
        			Drawable drawable = fetchDrawable(urlString);
        			Message message = handler.obtainMessage(1, drawable);
        			handler.sendMessage(message);
        		}
        	};
        	thread.start();
        }
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
    	try
    	{
	        DefaultHttpClient httpClient = new DefaultHttpClient();
	        HttpGet request = new HttpGet(urlString);
	        HttpResponse response = httpClient.execute(request);
	        return response.getEntity().getContent();
    	}
    	catch(Exception ex)
    	{
    		return null;
    	}
    }
}
