package rp3.berlin.utils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;


import android.util.Log;

public class TileProviderFactory {

    public static WMSTileProvider getOsgeoWmsTileProvider(final String url) {


        //This is configured for:
        // http://beta.sedac.ciesin.columbia.edu/maps/services
        // (TODO check that this WMS service still exists at the time you try to run this demo,
        // if it doesn't, find another one that supports EPSG:900913
        //final String WMS_FORMAT_STRING =
        //        "http://www.geoportaligm.gob.ec/25k/wms?service=wms&version=1.3.0&request=GetMap&layers=cultivo_a&srs=EPSG:900913&bbox=%f,%f,%f,%f&width=256&height=256&format=image/png&transparent=true";


        WMSTileProvider tileProvider = new WMSTileProvider(256,256) {

            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                double[] bbox = getBoundingBox(x, y, zoom);
                String s = String.format(Locale.US, url, bbox[MINX],
                        bbox[MINY], bbox[MAXX], bbox[MAXY]);
                Log.d("WMSDEMO", s);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };
        return tileProvider;
    }
}