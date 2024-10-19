package com.md_usb_pantro;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.md_usb_pantro.R;
import com.md_usb_pantro.utils.CacheUtils;
import com.md_usb_pantro.utils.Constant;
import com.md_usb_pantro.utils.Dialogues;
import com.md_usb_pantro.utils.NetworkUtility;
import com.phonepe.intent.sdk.api.PhonePe;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import butterknife.ButterKnife;

public class Payment extends AppCompatActivity implements Animation.AnimationListener {
    private Handler inactivityHandler = new Handler();
    private Runnable inactivityRunnable = new Runnable() {
        @Override
        public void run() {
            // Navigate back to the main screen
            Intent intent = new Intent(Payment.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish the payment activity
        }
    };












    TextView amount,name,rs;
    TextView ml;
    Button cancel;
    ImageView pointing,qr;
    Animation animMove;
    MediaPlayer mMediaPlayer;
    MediaPlayer payment_audio;
    Context context;
    Button statusbtn;
    public static   String txnId;
    String am,ml1, namee;
    long longamt;
    Handler handler = new Handler();
    Handler handlerordercompleted = new Handler();
    Runnable runnableordercompleted;
    int delayordercompleted = 3000;
    Runnable runnable;
    String merchant_id;
    int delay = 8000;
    Dialog dialogsuccs;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment); // Your payment layout

        // Start the inactivity timer
        resetInactivityTimer();



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment);
        overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
        context = this;
        ButterKnife.bind(this);
        PhonePe.init(this);
        statusbtn = findViewById(R.id.status);
        amount = findViewById(R.id.amount);
        ml = findViewById(R.id.ml);
        cancel = findViewById(R.id.cancel);
        name = findViewById(R.id.name);
        qr = findViewById(R.id.qr);
        pointing = findViewById(R.id.pointing);
        rs = findViewById(R.id.rs);
        SharedPreferences sharedPreferences = getSharedPreferences("jc001", MODE_PRIVATE);
        merchant_id = sharedPreferences.getString("merchant","");

        Intent intent = getIntent();
        ml1 = intent.getStringExtra("ml");
        am = intent.getStringExtra("total");
        namee = intent.getStringExtra("name");
        amount.setText(am);
        name.setText(namee);
        ml.setText(ml1);
        initpayment();
        animMove = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        animMove.setAnimationListener(this);
        pointing.startAnimation(animMove);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer = MediaPlayer.create(this, R.raw.qrcode);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.start();
        }catch (Exception e){}

        CountDownTimer timer = new CountDownTimer(2500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mMediaPlayer.start();
            }
        };
        timer.start();

        statusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Statuspayment();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Payment.this);
                alertDialogBuilder.setTitle("Confirm Exit...!!");
                alertDialogBuilder.setIcon(R.drawable.warning);
                alertDialogBuilder.setMessage("Are you sure, You want to exit");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You canceled Transaction", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You clicked on No", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You Clicked on Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        /*home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    private void resetInactivityTimer() {

        inactivityHandler.removeCallbacks(inactivityRunnable);
        inactivityHandler.postDelayed(inactivityRunnable, 90000); // 90 seconds
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Reset the timer on any touch event
        resetInactivityTimer();
        return super.onTouchEvent(event);
    }





    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                Statuspaymentpopup();
            }
        }, delay);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
        inactivityHandler.removeCallbacks(inactivityRunnable);

    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    public static String SHA256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initpayment() {
        txnId = UUID.randomUUID().toString().substring(0, 35);
        String userId = CacheUtils.getInstance(this).getUserId();
        String apiEndPoint = "/v3/qr/init";
        HashMap<String, Object> data = new HashMap<>();
        data.put("merchantId", merchant_id);
        data.put("terminalId", merchant_id);
        data.put("transactionId", txnId);
        data.put("amount", Double.parseDouble(amount.getText().toString()) * 100);
        data.put("merchantOrderId", "OD139924923");
        data.put("storeId", "OD139924923");
        data.put("expiresIn", 180);
        data.put("message", "Payment towards order No. OD139924923.");
        String dataString = new Gson().toJson(data);
        String dataString64 = null;

        try {
            dataString64 = Base64.encodeToString(dataString.getBytes("UTF-8"), Base64.DEFAULT);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //String checksumV2 = CheckSumUtils.getCheckSum(dataString64, apiEndPoint, Constant.SALT, Constant.SALT_KEY_INDEX);
        //  String checksum = SHA256(dataString64 + apiEndPoint + Constant.SALT) + "" + Constant.SALT_KEY_INDEX;
        String checksum = SHA256(dataString64 + apiEndPoint + Constant.SALT) + "###" + Constant.SALT_KEY_INDEX;
        try {
            if (NetworkUtility.isOnline(getApplicationContext())) {
                final ProgressDialog progressDialog = Dialogues.showProgressDialog(this, this.getResources().getString(R.string.loading));
                String strGetPay = "https://mercury-t2.phonepe.com/v3/qr/init";
                final RequestQueue requestQueue = Volley.newRequestQueue(context);
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("request", dataString64);
                final String mRequestBody = jsonBody.toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, strGetPay, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("reposnse", response);

                        try {
                            Dialogues.dismissProgressDialogue(progressDialog);
                            JSONObject jsonObject = new JSONObject(response);
                            String jsonObject1 = jsonObject.getString("data");
                            String jsonObject2 = jsonObject.getString("code");
                            JSONObject jsonobj = new JSONObject(jsonObject1);
                            String qrString = jsonobj.getString("qrString");
                            if (jsonObject2.equalsIgnoreCase("SUCCESS")) {
                                Toast.makeText(getApplicationContext(), "Scan and Pay", Toast.LENGTH_SHORT).show();
                                try {
                                    initQRCode(qrString);
                                }catch (Exception e){}

                            } else {
                                Toast.makeText(getApplicationContext(), "Reinitiate the transaction", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Log.d("reposnse", response);
                            Dialogues.dismissProgressDialogue(progressDialog);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Dialogues.dismissProgressDialogue(progressDialog);
                        if (String.valueOf(error).contains("com.android.volley.ServerError")) {
                            Toast.makeText(getApplicationContext(), "Reinitiate the transaction", Toast.LENGTH_SHORT).show();
                            Dialogues.dismissProgressDialogue(progressDialog);

                        }


                        if (String.valueOf(error).contains("com.android.volley.AuthFailureError")) {
                            Dialogues.dismissProgressDialogue(progressDialog);
                            Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();

                        }
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json;charset=UTF-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {

                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            Dialogues.dismissProgressDialogue(progressDialog);
                            responseString = String.valueOf(response.statusCode);
                        }
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("X-VERIFY", checksum);
                        headers.put("X-PROVIDER-ID", "RAPIDOSMARTPOS");
                        /*    headers.put("X-CALL-MODE", "");*/
                        return headers;
                    }
                };

                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                } };
                SSLContext sc = null;
                try {
                    sc = SSLContext.getInstance("SSL");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

                requestQueue.add(stringRequest);
            } else {

                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
    }


    private void initQRCode(String qrsting) {
        StringBuilder textToSend = new StringBuilder();
        textToSend.append(qrsting);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(textToSend.toString(), BarcodeFormat.QR_CODE, 600, 600);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr.setImageBitmap(bitmap);
            qr.setVisibility(View.VISIBLE);


        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private void Statuspayment() {
        String checksum = SHA256("/v3/transaction/" + merchant_id + "/" + txnId + "/status" + Constant.SALT) + "###" + Constant.SALT_KEY_INDEX;
        String strGetPay = "https://mercury-t2.phonepe.com/v3/transaction/" + merchant_id + "/" + txnId + "/status";
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strGetPay, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*Log.d("reposnse" , response);*/
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String code = jsonObject.getString("code");
                    String responess = jsonObject.getString("success");
                    String jsonObject1 = jsonObject.getString("data");
                    JSONObject jsonobj = new JSONObject(jsonObject1);
                    String statuscode = jsonobj.getString("payResponseCode");


                    if (code.equalsIgnoreCase("PAYMENT_SUCCESS")) {
                        showDialogProductYellow();
                        try {
                            payment_audio = new MediaPlayer();
                            payment_audio = MediaPlayer.create(context, R.raw.paymentsuccessfully);
                            payment_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            payment_audio.setLooping(false);
                            payment_audio.start();
                        }catch (Exception e){}


                    } else if (code.equalsIgnoreCase("PAYMENT_PENDING")) {
                        Inprocesspopup();

                    } else if (code.equalsIgnoreCase("INTERNAL_SERVER_ERROR") && (responess.equalsIgnoreCase("false"))) {
                        Inprocesspopup();

                    } else if (code.equalsIgnoreCase("INTERNAL_SERVER_ERROR")) {
                        Inprocesspopup();

                    } else if (code.equalsIgnoreCase("TRANSACTION_NOT_FOUND")) {
                        Transactionnotfound();

                    } else if (code.equalsIgnoreCase("PAYMENT_FAILED") && (responess.equalsIgnoreCase("false"))) {

                        Transactionfailed();
                    } else if (code.equalsIgnoreCase("PAYMENT_CANCELLED")) {
                        Transactioncancel();

                    } else if (code.equalsIgnoreCase("PAYMENT_ERROR")) {
                        Transactionerror();
                    } else if (code.equalsIgnoreCase("AUTHORIZATION_FAILED")) {
                        Transactionauthorization();

                    } else if (code.equalsIgnoreCase("PAYMENT_DECLINED")) {
                        Transactiondeclined();
                    }

                } catch (


                        JSONException e) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }

                    String code = null;
                    try {
                        code = jsonObject.getString("code");
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    String responess = null;
                    try {
                        responess = jsonObject.getString("success");
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    if (code.equalsIgnoreCase("PAYMENT_FAILED") && (responess.equalsIgnoreCase("false"))) {
                        Transactionfailed();
                    }
                    if (code.equalsIgnoreCase("PAYMENT_ERROR") && (responess.equalsIgnoreCase("false"))) {
                        Transactionerror();
                    }
                    if (code.equalsIgnoreCase("INTERNAL_SERVER_ERROR") && (responess.equalsIgnoreCase("false"))) {
                        Inprocesspopup();
                    }

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (String.valueOf(error).contains("com.android.volley.AuthFailureError")) {

                    Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
                }
                if (String.valueOf(error).contains("com.android.volley.ServerError")) {
                    Inprocesspopup();

                }

                if (String.valueOf(error).contains("com.android.volley.ClientError")) {
                    Transactionfailed();
                }
                Log.d("reposnse", String.valueOf(error));
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json;charset=UTF-8";
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-VERIFY", checksum);
                headers.put("X-PROVIDER-ID", "RAPIDOSMARTPOS");
                return headers;
            }
        };


        requestQueue.add(stringRequest);
    }


    private void Statuspaymentpopup() {

        String checksum = SHA256("/v3/transaction/" + merchant_id + "/" + txnId + "/status" + Constant.SALT) + "###" + Constant.SALT_KEY_INDEX;
        String strGetPay = "https://mercury-t2.phonepe.com/v3/transaction/" + merchant_id + "/" + txnId + "/status";
        if (requestQueue == null) {
            requestQueue  = Volley.newRequestQueue(context);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strGetPay, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String jsonObject1 = jsonObject.getString("data");
                    JSONObject jsonobj = new JSONObject(jsonObject1);
                    String statuscode = jsonobj.getString("payResponseCode");
                    if (code.equalsIgnoreCase("PAYMENT_SUCCESS")) {

                        try {
                            showDialogProductYellow();
                            payment_audio = new MediaPlayer();
                            payment_audio = MediaPlayer.create(context, R.raw.paymentsuccessfully);
                            payment_audio.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            payment_audio.setLooping(false);
                            payment_audio.start();
                        }catch (Exception e){}

                    } else {

                    }
                    // textView.setText(code);


                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (String.valueOf(error).contains("com.android.volley.AuthFailureError")) {
                    Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public String getBodyContentType()
            {
                return "application/json;charset=UTF-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-VERIFY", checksum);
                headers.put("X-PROVIDER-ID", "RAPIDOSMARTPOS");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }





    private void showDialogProductYellow() {
        dialogsuccs = new Dialog(this);
        dialogsuccs.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogsuccs.setContentView(R.layout.dialog_header_success_yellowthankyou);
        dialogsuccs.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogsuccs.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogsuccs.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


     /*   Button button = dialogsuccs.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialogsuccs.dismiss();
                Intent intent = new Intent(getApplicationContext(), TerminalActivity.class);
                startActivity(intent);
            }
        });*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(runnable);
                Intent intent = new Intent(Payment.this, TerminalActivity.class);
                startActivity(intent);
                dialogsuccs.dismiss();

            }
        }, 3000);

        dialogsuccs.show();

    }

    private void Inprocesspopup() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.inprocesspopscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.okbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
/*                Intent i= new Intent(getApplicationContext(), TerminalActivity.class);
                startActivity(i);*/
            }
        });

        dialog.show();
    }


    private void Transactionnotfound() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.transactionnotpopscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void Transactionfailed() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.transactionfailedpopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        handlerordercompleted.postDelayed(runnableordercompleted = new Runnable() {
            public void run() {
                handlerordercompleted.postDelayed(runnableordercompleted, delayordercompleted);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                handlerordercompleted.removeCallbacks(runnableordercompleted);

            }
        }, delayordercompleted);
        super.onResume();


        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void Transactioncancel() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.transactioncancelpopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.okbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void Transactionerror() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.transactionerror);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }


    private void Transactionauthorization() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.authorizationpopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialog.findViewById(R.id.okbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }






    private void Transactiondeclined() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.declinedpopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.okbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}