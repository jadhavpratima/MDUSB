package com.md_usb_pantro;
import static com.md_usb_pantro.Menu.hasordecode;
import static com.md_usb_pantro.Menu.hasordecodeliststock;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.md_usb_pantro.utils.EthernetService;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TerminalActivity extends AppCompatActivity {
    TextView display;
    private EditText editText;
    private MyHandler mHandler;
    static Context context;
    MediaPlayer mMediaPlayer;
    Button sendButton;
    Toast toast;
    Handler handler = new Handler();
    Runnable runnable;
    Dialog dialogsuccs, dialogsuccs1, dialogsuccs2, dialogsuccs3, dialogsuccs6, dialogsuccs33, dialog1;
    int cnt = 0;
    int cnt1 = 0;
    int cnt2 = 0;
    int cnt4 = 0;
    SharedPreferences sharedPreferencesnew;
    public static String var;
    public static Activity mactivity;
    private TextView tvTerminal, plswait;
    private EditText etSend;
    private boolean crlf = false;
    Handler handlerorder = new Handler();
    Handler handlerordercompleted = new Handler();
    Handler handlerorderprocess = new Handler();
    Runnable runnableorder, runnableordercompleted;
    int delay = 1000;
    LinearLayout lyt_progress;
    int cnt3 = 0;
    TextView textView;
    MediaPlayer pleasewaitaudio, dishreadyaudio, thankyouaudio, supportaudio;
    Animation blink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_terminal);
        context = this;
        //performClick();
        mactivity = TerminalActivity.this;
        mHandler = new MyHandler(this);
        display = (TextView) findViewById(R.id.textView1);
        editText = (EditText) findViewById(R.id.editText1);
        sendButton = (Button) findViewById(R.id.buttonSend);
        Button intents = findViewById(R.id.secondactivity);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tvTerminal = (TextView) findViewById(R.id.tv_terminal);
        etSend = (EditText) findViewById(R.id.et_send);
        plswait = findViewById(R.id.pleasewait);
        lyt_progress = findViewById(R.id.lyt_progress);
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_blink);
        textView = findViewById(R.id.textviewprogress);

        try {
            pleasewaitaudio = new MediaPlayer();
            pleasewaitaudio = MediaPlayer.create(this, R.raw.its_being_prepaired_pls_wait);
            pleasewaitaudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            pleasewaitaudio.setLooping(false);
            pleasewaitaudio.start();
        } catch (Exception e) {
        }


        updatestock();

            String bitdata = hasordecode.get("D");
            if (bitdata != null) {
                Log.d("Code", bitdata);
            } else {
                Log.d("Code", "bitdata is null");
            }

            if (bitdata != null) {
                sendCommandToServer(bitdata.getBytes());
            } else {
                Log.e("Error", "bitdata is null, cannot send command");
            }

        // Start checking
        checkReceivedData();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }


    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
//        startService(EthernetService.class, EthernetConnection, null);

// Start UsbService(if it was not started before) and Bind it
    }
    // Initialize received data
    String receivedBitData = "B11";
    int count=0;
    Handler handlerforcheck = new Handler(); // Create a handler to post delays

    // Method to check received data and handle the loop
    private void checkReceivedData() {

        getCommandfromServer(new CommandCallback() {
            @Override
            public void onResult(String result) {
                if (result == null) {
                    // Stop the loop if there's an error (null result)
                    Log.d("Error", "Error occurred while getting data from server");
                    return; // Exit the loop
                }
                // Update receivedBitData with the server result
                receivedBitData = result.substring(1, result.length()-1);;

                count++;
                if(count==50){
                    Log.d("Stopping", "So many time API hit stopping!");
                    return;
                }

                if (receivedBitData.equals("B11")) {
                    try {  // showDialogProductYellow();
                        new Handler().postDelayed(new Runnable() {
                            private EthernetService sendCommandToServer;

                            @Override
                            public void run() {
                                handler.removeCallbacks(runnable);
                                cnt4++;
                                if (cnt4 == 1) {
                                    String bitdata = hasordecode.get("D");
                                    Log.e("Code", bitdata);
                                    // if UsbService was correctly binded, Send data
//                                        sendCommandToServer.write(bitdata.getBytes());
                                    display.setText("");
                                }
                            }

                        }, 5000);
                    } catch (Exception e) {
                    }

                }
                if (receivedBitData.equals("B6")) {

                    try {

                        dialogsuccs.dismiss();
                        dialogsuccs6.dismiss();
                        dialogsuccs3.dismiss();
                        dialogsuccs33.dismiss();

                    } catch (Exception e) {
                    }

                    //showDialogProductYellow2();
                    cnt++;
                    if (cnt == 1) {
                        display.setText("");

                        try {
                            dialogsuccs.dismiss();
                            dialogsuccs6.dismiss();
                            dialogsuccs3.dismiss();
                            dialogsuccs33.dismiss();

                        } catch (Exception e) {
                        }

                    }
                }


                if (receivedBitData.equals("B3")) {
                    showDialogProductYellow6();
                    try {
                        dialogsuccs3.dismiss();
                    } catch (Exception e) {
                    }

                }
                if (receivedBitData.equals("B5")) {
                    try {
                        dialogsuccs1.dismiss();

                    } catch (Exception e) {
                    }
                    // Toast.makeText(mActivity.get(), "processing",Toast.LENGTH_LONG).show();
                    cnt1++;
                    if (cnt1 == 1) {

                        display.setText("");
                        try {
                            dialogsuccs6.dismiss();
                            dialogsuccs3.dismiss();
                            dialogsuccs33.dismiss();
                        } catch (Exception e) {
                        }
                    }
                }
                else if (receivedBitData.equals("B4"))
                {

                    cnt++;
                    try {
                        if(cnt==1)
                        {
                            Insertbottle();
                        }
                    } catch (Exception e) {
                    }


                }  else if (receivedBitData.equals("1,1,1B4,B,B"))
                {

                    cnt++;
                    try {
                        if(cnt==1)
                        {
                            Insertbottle();
                        }
                    } catch (Exception e) {
                    }

                } else if (receivedBitData.equals("B11,B11,B1,1,1B4,B,B"))
                {
                    cnt++;
                    try {
                        if(cnt==1)
                        {
                            Insertbottle();
                        }
                    } catch (Exception e) {
                    }
                }
                else if(receivedBitData.equals("B10"))
                {
                    try {

                        cnt2++;
                        if(cnt2==1) {
                            try {
                                thankyouaudio();
                            }catch (Exception e){}

                        }

                    }catch (Exception e){}
                    return;
                }
                if (receivedBitData.equals("B12")) {
                    try {
                        firstordercomplted();
                        //dialogsuccs33.dismiss();
                        dialogsuccs1.dismiss();
                        dialogsuccs.dismiss();
                    } catch (Exception e) {
                    }
                }



                handlerforcheck.postDelayed(() -> {
                    Log.d("Continuing", "Continuing to check data from server...");
                    checkReceivedData(); // Recursive call after the delay
                }, 1000);

            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
    }



    private void setFilters() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(EthernetService.ACTION_Ethernet_PERMISSION_GRANTED);
            filter.addAction(EthernetService.ACTION_NO_ETHERNET);
            filter.addAction(EthernetService.ACTION_ETHERNET_DISCONNECTED);
            filter.addAction(EthernetService.ACTION_ETHERNET_NOT_SUPPORTED);
            filter.addAction(EthernetService.ACTION_Ethernet_PERMISSION_GRANTED);

        }catch (Exception e){};

    }
    public class MyHandler extends Handler {
        private final WeakReference<TerminalActivity> mActivity;

        public MyHandler(TerminalActivity activity) {
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {

            Log.d("msg","hello how are you");

            switch (msg.what) {
                case EthernetService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    mActivity.get().display.append(data);
                    //   mActivity.get().display.setText(data);
                    var = mActivity.get().display.getText().toString();
                    //  Toast.makeText(context, var,Toast.LENGTH_LONG).show();


                    if (var.contains("B11")) {
                        try {  // showDialogProductYellow();
                            new Handler().postDelayed(new Runnable() {
                                private EthernetService sendCommandToServer;

                                @Override
                                public void run() {
                                    handler.removeCallbacks(runnable);
                                    cnt4++;
                                    if (cnt4 == 1) {
                                        String bitdata = hasordecode.get("D");
                                        Log.e("Code", bitdata);
                                        // if UsbService was correctly binded, Send data
//                                        sendCommandToServer.write(bitdata.getBytes());
                                        display.setText("");
                                    }
                                }

                            }, 5000);

                        } catch (Exception e) {
                        }

                    }
                    if (var.contains("B6")) {

                        try {

                            dialogsuccs.dismiss();
                            dialogsuccs6.dismiss();
                            dialogsuccs3.dismiss();
                            dialogsuccs33.dismiss();

                        } catch (Exception e) {
                        }

                        cnt++;
                        if (cnt == 1) {
                            display.setText("");

                            try {
                                dialogsuccs.dismiss();
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();
                                dialogsuccs33.dismiss();

                            } catch (Exception e) {
                            }

                        }
                    }


                    if (var.contains("B3")) {
                        showDialogProductYellow6();
                        try {
                            dialogsuccs3.dismiss();
                        } catch (Exception e) {
                        }

                    }
                    if (var.contains("B5")) {
                        try {
                            dialogsuccs1.dismiss();

                        } catch (Exception e) {
                        }

                        cnt1++;
                        if (cnt1 == 1) {

                            display.setText("");
                            try {
                                dialogsuccs6.dismiss();
                                dialogsuccs3.dismiss();
                                dialogsuccs33.dismiss();
                            } catch (Exception e) {
                            }
                        }
                    }
                    else if (var.contains("B4,B,B"))
                    {

                        cnt++;
                            try {
                                if(cnt==1)
                                {
                                    Insertbottle();
                                }
                    } catch (Exception e) {
                    }


                    }  else if (var.contains("1,1,1B4,B,B"))
                    {

                        cnt++;
                        try {
                            if(cnt==1)
                            {
                                Insertbottle();
                            }
                        } catch (Exception e) {
                        }

                    } else if (var.contains("B11,B11,B1,1,1B4,B,B"))
                    {

                        cnt++;
                        try {
                            if(cnt==1)
                            {
                                Insertbottle();
                            }
                        } catch (Exception e) {
                        }
                    }



                    else if(var.contains("B10,B,B"))
                    {
                        try {

                            cnt2++;
                            if(cnt2==1) {
                                try {
                                    thankyouaudio();
                                }catch (Exception e){}

                            }

                        }catch (Exception e){}

                    }

                    if (var.contains("B12,B,B")) {
                        //  Toast.makeText(mActivity.get(), "Completed",Toast.LENGTH_LONG).show();
                        try {
                            firstordercomplted();
                            //dialogsuccs33.dismiss();
                            dialogsuccs1.dismiss();
                            dialogsuccs.dismiss();
                        } catch (Exception e) {
                        }
                    }



                    break;

                case EthernetService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case EthernetService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    }

    private void showDialogProductYellow() {
        dialogsuccs = new Dialog(this);
        dialogsuccs.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogsuccs.setContentView(R.layout.dialog_header_success_yellow);
        dialogsuccs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogsuccs.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                display.setText("");

                // dialogsuccs.dismiss();

            }
        }, 1000);

        dialogsuccs.show();

    }


    private void updatestock() {
        try {

            int total1 = 0, total2 = 0, total3 = 0, total4 = 0, total5 = 0, total6 = 0;
            SharedPreferences sharedPreferences = getSharedPreferences("jc", MODE_PRIVATE);

            int dishstock1 = Integer.parseInt(sharedPreferences.getString("stock1", ""));
            int dishstock2 = Integer.parseInt(sharedPreferences.getString("stock2", ""));
            int dishstock3 = Integer.parseInt(sharedPreferences.getString("stock3", ""));
            int dishstock4 = Integer.parseInt(sharedPreferences.getString("stock4", ""));
            int dishstock5 = Integer.parseInt(sharedPreferences.getString("stock5", ""));
            int dishstock6 = Integer.parseInt(sharedPreferences.getString("stock6", ""));

            SharedPreferences.Editor editor = sharedPreferences.edit();

            Integer val1 = 0;
            Integer val2 = 0;
            Integer val3 = 0;
            Integer val4 = 0;
            Integer val5 = 0;
            Integer val6 = 0;


            val1 = hasordecodeliststock.get("1");
            val2 = hasordecodeliststock.get("2");
            val3 = hasordecodeliststock.get("3");
            val4 = hasordecodeliststock.get("4");
            val5 = hasordecodeliststock.get("5");
            val6 = hasordecodeliststock.get("6");

            if (val1 != null) {
                if (val1 > 0) {
                    total1 = dishstock1 - val1;
                    String st1 = String.valueOf(total1);
                    editor.putString("stock1", st1);
                }
            }
            if (val2 != null) {
                if (val2 > 0) {
                    total2 = dishstock2 - val2;
                    String st2 = String.valueOf(total2);
                    editor.putString("stock2", st2);
                }
            }
            if (val3 != null) {
                if (val3 > 0) {
                    total3 = dishstock3 - val3;
                    String st3 = String.valueOf(total3);
                    editor.putString("stock3", st3);
                }
            }
            if (val4 != null) {
                if (val4 > 0) {
                    total4 = dishstock4 - val4;
                    String st4 = String.valueOf(total4);
                    editor.putString("stock4", st4);
                }
            }
            if (val5 != null) {
                if (val5 > 0) {
                    total5 = dishstock5 - val5;
                    String st5 = String.valueOf(total5);
                    editor.putString("stock5", st5);
                }
            }

            if (val6 != null) {
                if (val6 > 0) {
                    total6 = dishstock6 - val6;
                    String st6 = String.valueOf(total6);
                    editor.putString("stock6", st6);
                }
            }

            editor.apply();

        } catch (Exception e) {

        }

    }


    private void showDialogProductYellow2() {
        dialogsuccs1 = new Dialog(this);
        dialogsuccs1.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogsuccs1.setContentView(R.layout.pickdish);
        dialogsuccs1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogsuccs1.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                display.setText("");
                //   dialogsuccs1.dismiss();

            }
        }, 1000);

        dialogsuccs1.show();

    }

    private void showDialogProductYellow3() {
        dialogsuccs2 = new Dialog(this);
        dialogsuccs2.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogsuccs2.setContentView(R.layout.dialog_header_success_yellow3);
        dialogsuccs2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogsuccs2.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs2.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button donebtn = dialogsuccs2.findViewById(R.id.donebtn);
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                dialogsuccs2.dismiss();
                dialogsuccs1.dismiss();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, 1000);

        dialogsuccs2.show();
    }

    private void showDialogProductYellow5() {
        dialogsuccs3 = new Dialog(this);
        dialogsuccs3.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogsuccs3.setContentView(R.layout.dialog_header_success_yellow5);
        dialogsuccs3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogsuccs3.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs3.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                display.setText("");
                //  dialogsuccs3.dismiss();

            }
        }, 1000);

        dialogsuccs3.show();

    }


    private void firstordercomplted() {
        dialogsuccs33 = new Dialog(this);
        dialogsuccs33.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogsuccs33.setContentView(R.layout.ordercompleted);
        dialogsuccs33.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogsuccs33.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs33.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialogsuccs.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialogsuccs33.dismiss();

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                display.setText("");
                dialogsuccs33.dismiss();

            }
        }, 1000);

        dialogsuccs33.show();

    }


    private void Insertbottle() {
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog1.setContentView(R.layout.insertbottle);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs33.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Button button = dialogsuccs.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialogsuccs33.dismiss();

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                display.setText("");
                dialog1.dismiss();

            }
        }, 1000);

        dialog1.show();

    }
    private void showDialogProductYellow6() {

        dialogsuccs6 = new Dialog(this);
        dialogsuccs6.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogsuccs6.setContentView(R.layout.dialog_header_success_yellow6);
        dialogsuccs6.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogsuccs6.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs6.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                dialogsuccs6.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


            }
        }, 1000);

        dialogsuccs6.show();


    }


    public void performClick() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                sendButton.performClick();
            }
        }, 1000);
    }

    public static void restartActivity(Activity activity) {
        activity.finish();
        activity.startActivity(activity.getIntent());
    }



    public void thankyouaudio() {
        try {
            thankyouaudio = new MediaPlayer();
            thankyouaudio = MediaPlayer.create(getApplicationContext(), R.raw.thankyouvisitagain);
            thankyouaudio.setAudioStreamType(AudioManager.STREAM_MUSIC);
            thankyouaudio.setLooping(false);
            thankyouaudio.start();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){}

    }

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
    // Create an ExecutorService to handle background tasks
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Function to send the command to the Raspberry Pi server via HTTP GET request
    private void sendCommandToServer(final byte[] command) {
        executorService.execute(() -> {
            String result;
            try {
                // Construct the URL for sending the command
                String urlString = "http://" + "192.168.0.100" + ":" + 5000 + "/sendbits?bits=" + new String(command);
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Read the response from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Assign the response to result
                result = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error sending command";
            }

            // Handle server response on the UI thread
            final String finalResult = result;
            runOnUiThread(() -> {
                // Update UI or display a toast here
                Log.d("Server response: " , finalResult);
            });
        });
    }

    public interface CommandCallback {
        void onResult(String result);
    }
    private void getCommandfromServer(CommandCallback callback) {
        executorService.execute(() -> {
            String result;
            try {
                // Construct the URL for sending the command
                String urlString = "http://" + "192.168.0.100" + ":" + 5000 + "/getbits";
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Read the response from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Assign the response to result
                result = response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error sending command";
            }

            // Handle server response on the UI thread
            final String finalResult = result;
            runOnUiThread(() -> {
                // Update UI or display a toast here
                Log.d("Server response: ", finalResult );
            });

            // Call the callback with the result
            if (callback != null) {
                callback.onResult(finalResult);
            }
        });
    }
}

