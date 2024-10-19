package com.md_usb_pantro;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity {
/*    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case USBServiceSecond.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();

                    break;
                case USBServiceSecond.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                   // Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();


                    break;
                case USBServiceSecond.ACTION_NO_USB: // NO USB CONNECTED
                   // Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();


                    break;
                case USBServiceSecond.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    try {
                        Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                        logo.setVisibility(View.INVISIBLE);
                    }catch (Exception e){}


                    break;
                case USBServiceSecond.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    //Toast.makeText(context, "USB device not supported", T oast.LENGTH_SHORT).show();

                    break;
            }
        }
    };*/
    private EthernetService usbService;
    private TextView display1, display2;
    private EditText editText1, editText2;
    private Button button1, button2;
    ImageView logo;
   // private MyHandler mHandler;
    String var;
    Handler handler = new Handler();
    Runnable runnable;
    Context context;
    Dialog dialogsuccs,dialogsuccs1,dialogsuccs2,dialogsuccs3,dialogsuccs6;
    int cnt=0;
    int cnt1=0;
    int cnt2=0;
    int cnt4=0;
    Animation blink;
    MediaPlayer mMediaPlayer;
    String progress;
    static int i = 0;
    TextView adminentry;
    Button freemode,ordernow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        context = this;
        overridePendingTransition(0, 0);

        display1 = findViewById(R.id.textView1);
        display2 = findViewById(R.id.textView2);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);

        button1 = findViewById(R.id.buttonSend1);
        button2 = findViewById(R.id.buttonSend2);
        ordernow = findViewById(R.id.order_now);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_blink);

        freemode = findViewById(R.id.freemode);
        adminentry = findViewById(R.id.adminentry);

        SharedPreferences sharedPreferences = getSharedPreferences("jc001", MODE_PRIVATE);
        String val = sharedPreferences.getString("valuee", "");

        if (val.equals("free")) {
            freemode.setVisibility(View.VISIBLE);
            ordernow.setVisibility(View.GONE);
        } else if (val.equals("paid")) {
            freemode.setVisibility(View.GONE);
            // Check if Ethernet is connected and show or hide the ordernow button accordingly
            if (isEthernetConnected()) {
                ordernow.setVisibility(View.VISIBLE); // Show ordernow if Ethernet is connected
            } else {
                ordernow.setVisibility(View.GONE); // Hide ordernow if Ethernet is not connected
            }
        }

        freemode.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Menu.class);
            startActivity(intent);
        });

        ordernow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Menu.class);
            startActivity(intent);
        });

        adminentry.setOnClickListener(v -> {
            i++;
            if (i == 5) {
                i = 0;
                Intent i = new Intent(getApplicationContext(), Passcode.class);
                startActivity(i);
                finish();
            }

        });
    }

    //--------------thise method is use to check ethernet is connected or not
    private boolean isEthernetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true; // Ethernet is connected
                }
            }
        }
        return false; // Ethernet is not connected
    }
//-------------------------------------------


    @Override
    public void onResume() {
        super.onResume();
        //logo.setVisibility(View.VISIBLE);
  /*      setFilters();  // Start listening notifications from UsbService
         startService(USBServiceSecond.class, usbConnection, null);*/

// Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
/*        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);*/

    }

  /*  private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!USBServiceSecond.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(USBServiceSecond.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(USBServiceSecond.ACTION_NO_USB);
        filter.addAction(USBServiceSecond.ACTION_USB_DISCONNECTED);
        filter.addAction(USBServiceSecond.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(USBServiceSecond.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);

    }

    *//*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     *//*
    public  class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);


        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case USBServiceSecond.SYNC_READ:
                    String buffer = (String) msg.obj;
                    if(msg.arg1 == 0){
                        mActivity.get().display1.append(buffer);

                        //    Toast.makeText(context, (Integer) msg.obj,Toast.LENGTH_LONG).show();

                    }else if(msg.arg1 == 1){
                        mActivity.get().display2.append(buffer);





                        //   mActivity.get().display.setText(data);
                        var=mActivity.get().display2.getText().toString();
                        // Toast.makeText(context, var,Toast.LENGTH_LONG).show();
                        //   Toast.makeText(mActivity.get(), var,Toast.LENGTH_LONG).show();

                        if(var.contains("B11,B,B"))
                        {
                            logo.setVisibility(View.VISIBLE);
                            // showDialogProductYellow();
                            cnt4++;
                            if(cnt4==1) {
                                display2.setText("");

                            }

                        }

                        if(var.contains("1BB11,BBB,B"))
                        {
                            logo.setVisibility(View.VISIBLE);
                            // showDialogProductYellow();
                            cnt4++;
                            if(cnt4==1) {
                                display2.setText("");

                            }

                        }



                        if(var.contains("B11,BB,B11,B,1BB"))
                        {
                            logo.setVisibility(View.VISIBLE);
                            // showDialogProductYellow();
                            cnt4++;
                            if(cnt4==1) {
                                //   showDialogProductYellow5();

                            }

                        }
                        if(var.contains("B6,B,B"))
                        {
                            try {
                                dialogsuccs.dismiss();
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();

                            }catch (Exception e){}

                            //showDialogProductYellow2();
                            cnt++;
                            if(cnt==1) {
                                //   showDialogProductYellow2();
                            }
                        }
                        if(var.contains("B4,BB,BB5,B,B"))
                        {
                            try {
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();
                            }catch (Exception e){}
                            // Toast.makeText(mActivity.get(), "processing",Toast.LENGTH_LONG).show();
                            cnt1++;
                            if(cnt1==1) {
                                showDialogProductYellow();

                            }
                        }

                        if(var.contains("B4,B,B"))
                        {
                            try {

                                dialogsuccs3.dismiss();
                            }catch (Exception e){}
                            // Toast.makeText(mActivity.get(), "processing",Toast.LENGTH_LONG).show();

                            // showDialogProductYellow6();

                        }
                        if(var.contains("B5,B,B"))
                        {
                            try {
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();
                            }catch (Exception e){}
                            // Toast.makeText(mActivity.get(), "processing",Toast.LENGTH_LONG).show();
                            cnt1++;
                            if(cnt1==1) {
                                showDialogProductYellow();
                            }


                        }
                        if(var.contains("B10,B,B"))
                        {
                            //  Toast.makeText(mActivity.get(), "Completed",Toast.LENGTH_LONG).show();
                            try {
                                dialogsuccs1.dismiss();

                            }catch (Exception e){}

                            cnt2++;
                            if(cnt2==1) {
                                //    showDialogProductYellow3();
                                dialogsuccs1.dismiss();

                            }
                        }




                        break;


                    }

            }
        }
    }


    private void showDialogProductYellow() {
        dialogsuccs = new Dialog(this);
        dialogsuccs.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogsuccs.setContentView(R.layout.dialog_header_success_yellow);
        dialogsuccs.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogsuccs.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

     *//*   Button button=dialogsuccs.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialogsuccs.dismiss();
                Intent intent=new Intent(getApplicationContext(),TerminalActivity.class);
                startActivity(intent);
            }
        });*//*

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                display2.setText("");
                display1.setText("");
                dialogsuccs.dismiss();
                // dialogsuccs.dismiss();

            }
        }, 1000);

        dialogsuccs.show();

    }*/

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir.list() != null) {
                deleteDir2(dir);
            }
        } catch (Exception e) { e.printStackTrace();}
    }

    public boolean deleteDir2(File dir) {
        if (dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                boolean success = deleteDir2(child);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
