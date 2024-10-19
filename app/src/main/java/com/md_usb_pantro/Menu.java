package com.md_usb_pantro;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Network;
import android.content.Intent;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.net.NetworkRequest;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;


public class Menu extends AppCompatActivity {


        private Handler inactivityHandler = new Handler();
        private Runnable inactivityRunnable = new Runnable() {
            @Override
            public void run() {
                // Go back to the MainActivity
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
    TextView menu_dish1, menu_dish2, menu_dish3, menu_dish4, menu_dish5, menu_dish6,
            menu_dish1_rate, menu_dish2_rate, menu_dish3_rate, menu_dish4_rate, menu_dish5_rate, menu_dish6_rate, dish1_quant, dish2_quant, dish3_quant, dish4_quant, dish5_quant, dish6_quant,
            menu_total, dish_name;
    Button confirm;
    TextView ml1, ml2, ml3, ml4, ml5, ml6;
    public static ArrayList<Orderitem> arrayList = new ArrayList();
    private Runnable returnToMainScreen;

    ImageView home;
    String rs, dishname, ml;
    Animation blink;
    int dishstock1new, dishstock2new, dishstock3new, dishstock4new, dishstock5new, dishstock6new;
    LinearLayout menu_linear1, menu_linear2, menu_linear3, menu_linear4, menu_linear5, menu_linear6;
    int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0;
    double cart;
    String dishstock1;
    String dishstock2;
    String dishstock3;
    String dishstock4;
    String dishstock5;
    String dishstock6;
    String dish1stts, dish2stts, dish3stts, dish4stts, dish5stts, dish6stts;
    MediaPlayer mMediaPlayer;
    Dialog dialog1;
    SharedPreferences sharedPreferencesnew;
    String dish1rate, dish2rate, dish3rate, dish4rate, dish5rate, dish6rate;
    String dish_1, dish_2, dish_3, dish_4, dish_5, dish_6;
    public static HashMap<String, String> hasordecode = new HashMap<>();
    public static HashMap<String, Integer> hasordecodelist = new HashMap<>();
    public static HashMap<String, Integer> hasordecodeliststock = new HashMap<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // Your menu layout

        // Start the inactivity timer
        resetInactivityTimer();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        context = this;

        //  offline();
        overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
        hasordecode.clear();
        hasordecodelist.clear();
        hasordecodeliststock.clear();
        blink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_blink);
        init();
        //    this.registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer = MediaPlayer.create(this, R.raw.selectmenu);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.start();
        } catch (Exception e) {
        }
    }

    private void resetInactivityTimer() {

        inactivityHandler.removeCallbacks(inactivityRunnable);
        inactivityHandler.postDelayed(inactivityRunnable, 60000); // 1 minute
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Reset the timer on any touch event
        resetInactivityTimer();
        return super.onTouchEvent(event);
    }


    public void init() {
        menu_dish1 = findViewById(R.id.menu1_name);
        menu_dish2 = findViewById(R.id.menu2_name);
        menu_dish3 = findViewById(R.id.menu3_name);
        menu_dish4 = findViewById(R.id.menu4_name);
        menu_dish5 = findViewById(R.id.menu5_name);
        menu_dish6 = findViewById(R.id.menu6_name);
        menu_dish1_rate = findViewById(R.id.menu1_rate);
        menu_dish2_rate = findViewById(R.id.menu2_rate);
        menu_dish3_rate = findViewById(R.id.menu3_rate);
        menu_dish4_rate = findViewById(R.id.menu4_rate);
        menu_dish5_rate = findViewById(R.id.menu5_rate);
        menu_dish6_rate = findViewById(R.id.menu6_rate);
        menu_total = findViewById(R.id.menutotal);
        dish_name = findViewById(R.id.dish_name);
        confirm = findViewById(R.id.confirm);
        home = findViewById(R.id.home);
        menu_linear1 = findViewById(R.id.cowlinear1);
        menu_linear2 = findViewById(R.id.cowlinear2);
        menu_linear3 = findViewById(R.id.cowlinear3);
        menu_linear4 = findViewById(R.id.buffalolinear1);
        menu_linear5 = findViewById(R.id.buffalolinear2);
        menu_linear6 = findViewById(R.id.buffalolinear3);
        ml1 = findViewById(R.id.cow1_ml);
        ml2 = findViewById(R.id.cow2_ml);
        ml3 = findViewById(R.id.cow3_ml);
        ml4 = findViewById(R.id.buffalo1_ml);
        ml5 = findViewById(R.id.buffalo2_ml);
        ml6 = findViewById(R.id.buffalo3_ml);
        sharedPreferencesnew = getSharedPreferences("jc", MODE_PRIVATE);
        //owner info
        try {
            dishstock1 = sharedPreferencesnew.getString("stock1", "");

        } catch (Exception e) {

        }
        try {
            dishstock2 = sharedPreferencesnew.getString("stock2", "");

        } catch (Exception e) {

        }
        try {
            dishstock3 = sharedPreferencesnew.getString("stock3", "");

        } catch (Exception e) {


        }
        try {
            dishstock4 = sharedPreferencesnew.getString("stock4", "");

        } catch (Exception e) {
        }


        try {
            dishstock5 = sharedPreferencesnew.getString("stock5", "");

        } catch (Exception e) {
        }


        try {
            dishstock6 = sharedPreferencesnew.getString("stock6", "");

        } catch (Exception e) {
        }


        try {
            dishstock1new = Integer.parseInt(dishstock1);
            dishstock2new = Integer.parseInt(dishstock2);
            dishstock3new = Integer.parseInt(dishstock3);
            dishstock4new = Integer.parseInt(dishstock4);
            dishstock5new = Integer.parseInt(dishstock5);
            dishstock6new = Integer.parseInt(dishstock6);
        } catch (Exception e) {
        }

        if (dishstock1new < 10) {
            menu_linear1.setVisibility(View.GONE);
        } else if (dishstock1new >= 11) {
            menu_linear1.setVisibility(View.VISIBLE);
        }

        if (dishstock2new < 10) {
            menu_linear2.setVisibility(View.GONE);
        } else if (dishstock2new >= 11) {
            menu_linear2.setVisibility(View.VISIBLE);
        }

        if (dishstock3new < 10) {
            menu_linear3.setVisibility(View.GONE);
        } else if (dishstock3new >= 11) {
            menu_linear3.setVisibility(View.VISIBLE);
        }

        if (dishstock4new < 10) {
            menu_linear4.setVisibility(View.GONE);
        } else if (dishstock4new >= 11) {
            menu_linear4.setVisibility(View.VISIBLE);
        }
        if (dishstock5new < 10) {
            menu_linear5.setVisibility(View.GONE);
        } else if (dishstock5new >= 11) {
            menu_linear5.setVisibility(View.VISIBLE);
        }

        if (dishstock6new < 10) {
            menu_linear6.setVisibility(View.GONE);
        } else if (dishstock6new >= 11) {
            menu_linear6.setVisibility(View.VISIBLE);
        }

        SharedPreferences sharedPreferences = getSharedPreferences("jc001", MODE_PRIVATE);

        //dish rate

        dish1rate = sharedPreferences.getString("dish1rate", "");
        menu_dish1_rate.setText("Rs " + dish1rate);
        dish2rate = sharedPreferences.getString("dish2rate", "");
        menu_dish2_rate.setText("Rs " + dish2rate);
        dish3rate = sharedPreferences.getString("dish3rate", "");
        menu_dish3_rate.setText("Rs " + dish3rate);
        dish4rate = sharedPreferences.getString("dish4rate", "");
        menu_dish4_rate.setText("Rs " + dish4rate);
        dish5rate = sharedPreferences.getString("dish5rate", "");
        menu_dish5_rate.setText("Rs " + dish5rate);
        dish6rate = sharedPreferences.getString("dish6rate", "");
        menu_dish6_rate.setText("Rs " + dish6rate);

        //Dish Name
        dish_1 = sharedPreferences.getString("dish1", "");
        menu_dish1.setText(dish_1);
        dish_2 = sharedPreferences.getString("dish2", "");
        menu_dish2.setText(dish_2);
        dish_3 = sharedPreferences.getString("dish3", "");
        menu_dish3.setText(dish_3);
        dish_4 = sharedPreferences.getString("dish4", "");
        menu_dish4.setText(dish_4);
        dish_5 = sharedPreferences.getString("dish5", "");
        menu_dish5.setText(dish_5);
        dish_6 = sharedPreferences.getString("dish6", "");
        menu_dish6.setText(dish_6);

        //dish status
        dish1stts = sharedPreferences.getString("dishstatus1", "");
        dish2stts = sharedPreferences.getString("dishstatus2", "");
        dish3stts = sharedPreferences.getString("dishstatus3", "");
        dish4stts = sharedPreferences.getString("dishstatus4", "");
        dish5stts = sharedPreferences.getString("dishstatus5", "");
        dish6stts = sharedPreferences.getString("dishstatus6", "");
        if (dish1stts.contains("disable")) {
            menu_linear1.setVisibility(View.GONE);
        }
        if (dish2stts.contains("disable")) {
            menu_linear2.setVisibility(View.GONE);
        }
        if (dish3stts.contains("disable")) {
            menu_linear3.setVisibility(View.GONE);
        }
        if (dish4stts.contains("disable")) {
            menu_linear4.setVisibility(View.GONE);
        }
        if (dish5stts.contains("disable")) {
            menu_linear5.setVisibility(View.GONE);
        }
        if (dish6stts.contains("disable")) {
            menu_linear6.setVisibility(View.GONE);
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        menu_linear1.setOnClickListener(this::onClick);
        menu_linear2.setOnClickListener(this::onClick);
        menu_linear3.setOnClickListener(this::onClick);
        menu_linear4.setOnClickListener(this::onClick);
        menu_linear5.setOnClickListener(this::onClick);
        menu_linear6.setOnClickListener(this::onClick);
        SharedPreferences sharedPreferencess = getSharedPreferences("jc001", MODE_PRIVATE);
        String val = sharedPreferencess.getString("valuee", "");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (val.equals("free")) {
                    if (menu_total.getText().equals("0")) {
                        showCustomDialog();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), TerminalActivity.class);
                        rs = menu_total.getText().toString().trim();
                        intent.putExtra("total", rs);
                        dishname = dish_name.getText().toString();
                        intent.putExtra("name", dishname);
                        intent.putExtra("ml", ml);
                        String bit = hasordecode.get("D");

                        if (bit != null) {
                            Log.d("code", bit.toString());
                        } else {
                            Log.d("code", "bit is null");
                        }

                        startActivity(intent);
                        finish();
                    }
                } else if (val.equals("paid")) {
                    if (menu_total.getText().equals("0")) {
                        showCustomDialog();
                    } else {
                        // Handle Wi-Fi and Ethernet connections
                        manageNetworkConnections();

                        Intent intent = new Intent(getApplicationContext(), Payment.class);
                        rs = menu_total.getText().toString().trim();
                        intent.putExtra("total", rs);
                        dishname = dish_name.getText().toString();
                        intent.putExtra("name", dishname);
                        intent.putExtra("ml", ml);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            // Method to manage network connections
            private void manageNetworkConnections() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


                // Bind to Ethernet if available
                NetworkRequest ethernetRequest = new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                        .build();

                connectivityManager.requestNetwork(ethernetRequest, new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        Log.d("Network", "Ethernet is available");
                        connectivityManager.
                                bindProcessToNetwork(network); // Bind Ethernet
                    }
                });
                // Bind to Wi-Fi if available
                NetworkRequest wifiRequest = new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .build();

                connectivityManager.requestNetwork(wifiRequest, new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        Log.d("Network", "Wi-Fi is available");
                        connectivityManager.bindProcessToNetwork(network); // Bind Wi-Fi
                    }
                });
            }

        });

    }@Override
    public void onBackPressed() {
        super.getClass();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks when activity is destroyed
        inactivityHandler.removeCallbacks(inactivityRunnable);
    }
    private void dish1display(int number) {
        dish1_quant.setText("" + number);

    }

    private void dish2display(int number) {
        dish2_quant.setText("" + number);
    }

    private void dish3display(int number) {
        dish3_quant.setText("" + number);
    }

    private void dish4display(int number) {
        dish4_quant.setText("" + number);
    }

    private void dish5display(int number) {
        dish5_quant.setText("" + number);
    }

    private void dish6display(int number) {
        dish6_quant.setText("" + number);
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    // ------------ change thise code usb to ethernet--------
    public void onClick(View v) {
        String commandString = "hi";  // To hold the command to send
        String menu="0";
        switch (v.getId()) {
            case R.id.cowlinear1:
                menu_total.setText(dish1rate);
                dish_name.setText(dish_1);
                ml = ml1.getText().toString();
                commandString = "(1,0,0,0,0,0)";
                menu="1";
                break;
            case R.id.cowlinear2:
                menu_total.setText(dish2rate);
                dish_name.setText(dish_2);
                ml = ml2.getText().toString();
                commandString = "(0,1,0,0,0,0)";
                menu="2";
                break;
            case R.id.cowlinear3:
                menu_total.setText(dish3rate);
                dish_name.setText(dish_3);
                ml = ml3.getText().toString();
                commandString = "(0,0,1,0,0,0)";
                menu="3";
                break;
            case R.id.buffalolinear1:
                menu_total.setText(dish4rate);
                dish_name.setText(dish_4);
                ml = ml4.getText().toString();
                commandString = "(0,0,0,1,0,0)";
                menu="4";
                break;
            case R.id.buffalolinear2:
                menu_total.setText(dish5rate);
                dish_name.setText(dish_5);
                ml = ml5.getText().toString();
                commandString = "(0,0,0,0,1,0)";
                menu="5";
                break;
            case R.id.buffalolinear3:
                menu_total.setText(dish6rate);
                dish_name.setText(dish_6);
                ml = ml6.getText().toString();
                commandString = "(0,0,0,0,0,1)";
                menu="6";
                break;
            default:
                break;

        }
        try {
            hasordecode.size();
            hasordecode.put("D",commandString);
            hasordecodeliststock.put(menu, 1);
        }catch (Exception e){}

        // Convert the command string into a byte array
        if (!commandString.isEmpty()) {
            byte[] commandBytes = commandString.getBytes();
            sendCommandToServer(commandBytes);
        }
    }
    private void sendCommandToServer(byte[] commandBytes) {
    }
};