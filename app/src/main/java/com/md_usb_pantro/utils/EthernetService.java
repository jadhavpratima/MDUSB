package com.md_usb_pantro.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.io.OutputStream;
import java.io.InputStream;

public class EthernetService extends Service {

    public static final String TAG = "EthernetService";
    public static final String ACTION_ETHERNET_CONNECTED = "com.md_usb_pantro.ACTION_ETHERNET_CONNECTED";
    public static final String ACTION_NO_ETHERNET = "com.md_usb_pantro.ACTION_NO_ETHERNET";
    public static final String ACTION_ETHERNET_DISCONNECTED = "com.md_usb_pantro.ACTION_ETHERNET_DISCONNECTED";
    public static final String ACTION_ETHERNET_NOT_SUPPORTED = "com.md_usb_pantro.ACTION_ETHERNET_NOT_SUPPORTED";
    public static final int MESSAGE_FROM_SERIAL_PORT = 1;
    public static final int CTS_CHANGE = 2;
    public static final int DSR_CHANGE =3 ;
    public static boolean SERVICE_CONNECTED;
    public static String ACTION_Ethernet_PERMISSION_GRANTED;

    private final IBinder binder = new EthernetBinder();
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Handler mHandler;

    public static void write(byte[] bytes) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Start Ethernet connection logic
        new Thread(new ConnectionThread()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public void sendMessage(byte[] data) {
        if (outputStream != null) {
            try {
                outputStream.write(data);
            } catch (IOException e) {
                Log.e(TAG, "Error sending data", e);
            }
        }
    }

    private class ConnectionThread implements Runnable {
        @Override
        public void run() {
            try {
                // Replace with your server IP and port
                socket = new Socket("127.0.0.1", 12345);
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();

                // Notify that Ethernet is connected
                sendBroadcast(new Intent(ACTION_ETHERNET_CONNECTED));

                // Listen for incoming messages
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    // Process incoming data (e.g., send to handler)
                    if (mHandler != null) {
                        mHandler.obtainMessage(0, new String(buffer, 0, bytesRead)).sendToTarget();
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Error in Ethernet connection", e);
                sendBroadcast(new Intent(ACTION_ETHERNET_DISCONNECTED));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error closing socket", e);
        }
    }

    public class EthernetBinder extends Binder {
        public EthernetService getService() {
            return EthernetService.this;
        }
    }
}
