package rp3.auna.util.print;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

import asia.kanopi.fingerscan.*;
import asia.kanopi.fingerscan.ScanFinger;
import asia.kanopi.fingerscan.Status;

/**
 * Created by Jesus Villa on 23/03/2018.
 */

public class FingerPrint {
    private static final String TAG = FingerPrint.class.getSimpleName();
    private UruConnection reader;
    private UsbManager usbManager;
    private Context context;
    private Handler imageHandler;
    private Handler updateHandler;
    private asia.kanopi.fingerscan.Status status;
    private static boolean deviceRegistered;

    private static final int U_ARE_U_4500B_PRODUCT_ID = 10;
    private static final int U_ARE_U_4500B_VENDOR_ID = 1466;


    private static final String ACTION_USB_PERMISSION = "asia.kanopi.USB_PERMISSION";
    private static final String LOG_TAG = "Fingerprint";

    public FingerPrint() {
        status = new asia.kanopi.fingerscan.Status();
        status.setStatus(asia.kanopi.fingerscan.Status.INITIALISED);
        deviceRegistered = false;
    }

    public void scan(Context context, Handler imageHandler) {
        this.context = context;
        this.imageHandler = imageHandler;
        this.updateHandler = null;
        connectToReader();
    }

    public void scan(Context context, Handler imageHandler, Handler updateHandler) {
        this.context = context;
        this.imageHandler = imageHandler;
        this.updateHandler = updateHandler;
        connectToReader();
    }

    public int getStatus() {
        return status.getStatus();
    }

    private void setStatus(int code, String message) {
        status.setStatus(code, message);
        sendUpdate();
    }

    private void sendUpdate() {
        try{
            if (updateHandler != null) {
                Message msg = updateHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("status", status.getStatus());
                if (status.getStatus() == asia.kanopi.fingerscan.Status.ERROR) {
                    bundle.putString("errorMessage", status.getErrorMessage());
                }
                msg.setData(bundle);
                updateHandler.sendMessage(msg);
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
            e.printStackTrace();
        }

    }

    private void connectToReader() {
        try{
            reader = new UruConnection();
            usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            boolean scannerFound = false;
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            if (deviceList.isEmpty()) {
                setStatus(asia.kanopi.fingerscan.Status.ERROR, "No USB devices found");

            } else {
                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
                        context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                if(!deviceRegistered){
                    IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
                    context.registerReceiver(mUsbReceiver, filter);
                    deviceRegistered = true;
                }

                while (deviceIterator.hasNext()) {
                    UsbDevice device = deviceIterator.next();
                    if (device.getProductId() == U_ARE_U_4500B_PRODUCT_ID
                            && device.getVendorId() == U_ARE_U_4500B_VENDOR_ID) {
                        scannerFound = true;
                        usbManager.requestPermission(device, mPermissionIntent);
                    }
                }
                if (!scannerFound) {
                    setStatus(asia.kanopi.fingerscan.Status.ERROR, "U.are.U 4500B scanner not connected via USB");
                }
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
            e.printStackTrace();
        }

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null){
                            initiateCommunication(device);
                        } else {
                            setStatus(asia.kanopi.fingerscan.Status.ERROR, "Null device");
                        }
                    } else {
                        setStatus(Status.ERROR, "Permission denied to access fingerprint scanner");
                    }
                }
            }
        }
    };

    private void initiateCommunication(UsbDevice usbDevice) {
        try {
            Thread t;
            UsbInterface uru_interface = usbDevice.getInterface(0);
            final UsbDeviceConnection uru_connection = usbManager.openDevice(usbDevice);
            uru_connection.claimInterface(uru_interface, true);

            reader.m_connection = uru_connection;
            try{
                // Set up listener in new thread
                asia.kanopi.fingerscan.ScanFinger r = new ScanFinger(reader, usbDevice, status, imageHandler,
                        updateHandler, context);
                t = new Thread(r);
                t.start();

                reader.init_reader(uru_connection);
            }catch (Exception e){
                Log.d(TAG,e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e (TAG, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void unregisterDevice(Context context) {
        if (deviceRegistered) {
            context.unregisterReceiver(mUsbReceiver);
            deviceRegistered = false;
        }
    }

    public void turnOffReader() {
        if (deviceRegistered) {
            reader.turnScannerOff();
            unregisterDevice(context);
        }
    }
}
