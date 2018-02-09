package rp3.auna.util.helper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jesus Villa on 19/10/2017.
 */

public class CallLogHelper {
    private static final String TAG = CallLogHelper.class.getSimpleName();
    public static Cursor getAllCallLogs(ContentResolver cr) {
        // reading all data in descending order according to DATE
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor curCallLogs = cr.query(callUri, null, null, null, strOrder);
        return curCallLogs;
    }

    public static void insertPlaceholderCall(ContentResolver contentResolver,
                                             String name, String number) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, number);
        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
        values.put(CallLog.Calls.DURATION, 0);
        values.put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE);
        values.put(CallLog.Calls.NEW, 1);
        values.put(CallLog.Calls.CACHED_NAME, name);
        values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
        Log.d("Call Log", "Inserting call log placeholder for " + number);
        contentResolver.insert(CallLog.Calls.CONTENT_URI, values);
    }

    public static ArrayList<String> getCallLogs(Cursor curLog) {
        ArrayList<String> call = new ArrayList<>();
        while (curLog.moveToNext()) {
            String callNumber = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            String callName = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            if (callName == null) {
                Log.d(TAG,"Unknown");
            } else
                Log.d(TAG,(callName));
            String callDate = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long.parseLong(callDate)));
            String callType = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            if (callType.equals("1")) {
                Log.d(TAG,"Incoming");
                call.add("Incoming");
            } else {
                Log.d(TAG, "Outgoing");
                call.add("Outgoing");
            }
            String duration = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.DURATION));
            Log.d(TAG,"Numero+"+callNumber);
            Log.d(TAG,"Fecha+"+dateString);
            Log.d(TAG,"Tipo+"+callType);
            Log.d(TAG,"Duration+"+duration);
            Log.d(TAG,"BREAKPOINT;");
            call.add(duration);
            call.add(callNumber);
            break;
        }
        return call;
    }
}
