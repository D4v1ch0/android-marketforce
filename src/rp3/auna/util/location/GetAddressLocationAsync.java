package rp3.auna.util.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by JesusVilla on 10/09/15.
 */
public class GetAddressLocationAsync extends AsyncTask<String, Void, String> {

    GetAddressLocationCallback _callback;
    private Context _context;
    private double x, y;
    private StringBuilder str;
    private Geocoder geocoder;
    private List<Address> addresses;

    public GetAddressLocationAsync(Context context, GetAddressLocationCallback callback, double latitude, double longitude) {
        x = latitude;
        y = longitude;
        _callback = callback;
        _context = context;
    }

    @Override
    protected void onPreExecute() {
        _callback.onAddressLocationPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            geocoder = new Geocoder(_context, Locale.ENGLISH);
            addresses = geocoder.getFromLocation(x, y, 1);
            str = new StringBuilder();
            if (geocoder.isPresent()) {
                Address returnAddress = addresses.get(0);

                String localityString = returnAddress.getLocality();
                String city = returnAddress.getCountryName();
                String region_code = returnAddress.getCountryCode();
                String zipcode = returnAddress.getPostalCode();

                str.append(localityString + "");
                str.append(city + "" + region_code + "");
                str.append(zipcode + "");


            } else {
            }
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        String address = null;

        if (addresses != null) {
            try {
                address = addresses.get(0).getAddressLine(0);
            } catch (Exception ex) {
                address = "";
            }
        }

        _callback.onAddressLocationPostExecute(address);
    }

    public abstract interface GetAddressLocationCallback {
        void onAddressLocationPreExecute();

        void onAddressLocationPostExecute(String address);
    }

}
