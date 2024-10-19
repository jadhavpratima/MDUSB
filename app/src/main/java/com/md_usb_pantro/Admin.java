package com.md_usb_pantro;
import static com.md_usb_pantro.Menu.hasordecode;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;

public class Admin extends AppCompatActivity {
    EditText owner_name,city,mobile_no,modelno,modelname,merchant_data;
    Button home,free,paid;
    TextView dish1status,dish2status,dish3status,dish4status,dish5status,dish6status;
    TextView dish1,dish2,dish3,dish4,dish5,dish6;
    public static int Retrytime = 200000;
    EditText dish1_rate,dish2_rate,dish3_rate,dish4_rate,dish5_rate,dish6_rate;
    TextView ID1,ID2,ID3,ID4,ID5,ID6,value;
    Button save, dish1_enable,dish1_disable,dish2_enable,dish2_disable,dish3_enable,dish3_disable,dish4_enable,dish4_disable,dish5_enable,dish5_disable,dish6_enable,dish6_disable;
    String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
    Matcher m;
    ImageView stock,clean;
    Context context;
    String B="B";
    static int i = 0;
    private boolean crlf = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin);
        context=this;
        overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
        init();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stockdialog();
            }
        });





        SharedPreferences sharedPreferences = getSharedPreferences("jc001", MODE_PRIVATE);

        //owner info
        String name1 = sharedPreferences.getString("name","");
        owner_name.setText(name1);
        String phone1 = sharedPreferences.getString("phone","");
        mobile_no.setText(phone1);
        String mdname = sharedPreferences.getString("Modelname","");
        modelname.setText(mdname);
        String mdno = sharedPreferences.getString("Modelno", "");
        modelno.setText(mdno);
        String City = sharedPreferences.getString("City","");
        city.setText(City);
        String mid = sharedPreferences.getString("merchant", "");
        merchant_data.setText(mid);



        //dish rate
        String dish1rate = sharedPreferences.getString("dish1rate","");
        dish1_rate.setText(dish1rate);
        String dish2rate = sharedPreferences.getString("dish2rate", "");
        dish2_rate.setText(dish2rate);
        String dish3rate = sharedPreferences.getString("dish3rate", "");
        dish3_rate.setText(dish3rate);
        String dish4rate = sharedPreferences.getString("dish4rate", "");
        dish4_rate.setText(dish4rate);
        String dish5rate = sharedPreferences.getString("dish5rate", "");
        dish5_rate.setText(dish5rate);
        String dish6rate = sharedPreferences.getString("dish6rate", "");
        dish6_rate.setText(dish6rate);

        String dish1name = sharedPreferences.getString("dish1","");
        dish1.setText(dish1name);
        String dish2name = sharedPreferences.getString("dish2", "");
        dish2.setText(dish2name);
        String dish3name = sharedPreferences.getString("dish3", "");
        dish3.setText(dish3name);
        String dish4name = sharedPreferences.getString("dish4", "");
        dish4.setText(dish4name);
        String dish5name = sharedPreferences.getString("dish5", "");
        dish5.setText(dish5name);
        String dish6name = sharedPreferences.getString("dish6", "");
        dish6.setText(dish6name);

        String val = sharedPreferences.getString("valuee","");
        value.setText(val);

        if (val.equals("free")){
            free.setVisibility(View.GONE);
            paid.setVisibility(View.VISIBLE);
        }

        if (val.equals("paid")){
            paid.setVisibility(View.GONE);
            free.setVisibility(View.VISIBLE);
        }


        String dish1stts = sharedPreferences.getString("dishstatus1","");
        dish1status.setText(dish1stts);

        String dish2stts = sharedPreferences.getString("dishstatus2","");
        dish2status.setText(dish2stts);

        String dish3stts = sharedPreferences.getString("dishstatus3","");
        dish3status.setText(dish3stts);

        String dish4stts = sharedPreferences.getString("dishstatus4","");
        dish4status.setText(dish4stts);

        String dish5stts = sharedPreferences.getString("dishstatus5","");
        dish5status.setText(dish5stts);

        String dish6stts = sharedPreferences.getString("dishstatus6","");
        dish6status.setText(dish6stts);

        if (dish1stts.equals("enable"))
        {
            dish1_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish1_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish1stts.equals("disable"))
        {
            dish1_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish1_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }

        if (dish2stts.equals("enable"))
        {
            dish2_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish2_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish2stts.equals("disable"))
        {
            dish2_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish2_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }
        if (dish3stts.equals("enable"))
        {
            dish3_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish3_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish3stts.equals("disable"))
        {
            dish3_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish3_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }
        if (dish4stts.equals("enable"))
        {
            dish4_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish4_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish4stts.equals("disable"))
        {
            dish4_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish4_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }
        if (dish5stts.equals("enable"))
        {
            dish5_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish5_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish5stts.equals("disable"))
        {
            dish5_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish5_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }
        if (dish6stts.equals("enable"))
        {
            dish6_enable.setBackgroundColor(getResources().getColor(R.color.GreenYellow));
            dish6_disable.setBackgroundColor(getResources().getColor(R.color.SlateGray));

        }if (dish6stts.equals("disable"))
        {
            dish6_disable.setBackgroundColor(getResources().getColor(R.color.Red));
            dish6_enable.setBackgroundColor(getResources().getColor(R.color.SlateGray));
        }


        dish1_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish1status.setText("enable");
                dish1_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish1_disable.setBackgroundColor(Color.parseColor("#708090"));

            }
        });

        dish1_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish1status.setText("disable");
                dish1_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish1_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish2_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish2status.setText("enable");
                dish2_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish2_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish2_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish2status.setText("disable");
                dish2_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish2_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish3_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish3status.setText("enable");
                dish3_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish3_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish3_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish3status.setText("disable");
                dish3_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish3_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish4_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish4status.setText("enable");
                dish4_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish4_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish4_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish4status.setText("disable");
                dish4_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish4_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish5_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish5status.setText("enable");
                dish5_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish5_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish5_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish5status.setText("disable");
                dish5_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish5_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish6_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish6status.setText("enable");
                dish6_enable.setBackgroundColor(Color.parseColor("#ADFF2F"));
                dish6_disable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });

        dish6_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dish6status.setText("disable");
                dish6_disable.setBackgroundColor(Color.parseColor("#FF0000"));
                dish6_enable.setBackgroundColor(Color.parseColor("#708090"));
            }
        });


        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value.setText("free");
                free.setVisibility(View.GONE);
                paid.setVisibility(View.VISIBLE);

            }
        });

        paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value.setText("paid");
                paid.setVisibility(View.GONE);
                free.setVisibility(View.VISIBLE);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("jc001", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();


                //owner info.
                editor.putString("name", owner_name.getText().toString().trim());
                editor.putString("phone", mobile_no.getText().toString().trim());
                editor.putString("merchant",merchant_data.getText().toString().trim());
                editor.putString("Modelno",modelno.getText().toString().trim());
                editor.putString("Modelname",modelname.getText().toString().trim());
                editor.putString("City",city.getText().toString().trim());


                //Dish rate
                editor.putString("dish1rate",dish1_rate.getText().toString().trim());
                editor.putString("dish2rate",dish2_rate.getText().toString().trim());
                editor.putString("dish3rate",dish3_rate.getText().toString().trim());
                editor.putString("dish4rate",dish4_rate.getText().toString().trim());
                editor.putString("dish5rate",dish5_rate.getText().toString().trim());
                editor.putString("dish6rate",dish6_rate.getText().toString().trim());

                //Dish name
                editor.putString("dish1",dish1.getText().toString().trim());
                editor.putString("dish2",dish2.getText().toString().trim());
                editor.putString("dish3",dish3.getText().toString().trim());
                editor.putString("dish4",dish4.getText().toString().trim());
                editor.putString("dish5",dish5.getText().toString().trim());
                editor.putString("dish6",dish6.getText().toString().trim());

                //Dish description
              /*  editor.putString("dish1desc",dish1_desc.getText().toString().trim());
                editor.putString("dish2desc",dish2_desc.getText().toString().trim());
                editor.putString("dish3desc",dish3_desc.getText().toString().trim());*/

                //Dish status
                editor.putString("dishstatus1",dish1status.getText().toString().trim());
                editor.putString("dishstatus2",dish2status.getText().toString().trim());
                editor.putString("dishstatus3",dish3status.getText().toString().trim());
                editor.putString("dishstatus4",dish4status.getText().toString().trim());
                editor.putString("dishstatus5",dish5status.getText().toString().trim());
                editor.putString("dishstatus6",dish6status.getText().toString().trim());
                editor.putString("valuee",value.getText().toString().trim());


               /* String ownername = owner_name.getText().toString();
                String id1 =ID1 .getText().toString();
                String id2 = ID2.getText().toString();
                String id3 = ID3.getText().toString();
                String id4 = ID4.getText().toString();
                String dish1name =dish1 .getText().toString();
                String dish2name = dish2.getText().toString();
                String dish3name = dish3.getText().toString();
                String dish4name = dish4.getText().toString();
                String dish1price =dish1_rate .getText().toString();
                String dish2price = dish2_rate.getText().toString();
                String dish3price = dish3_rate.getText().toString();
                String dish4price = dish4_rate.getText().toString();*/


                    editor.apply();
                Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void init(){
        owner_name = findViewById(R.id.editname);
        city = findViewById(R.id.editcity);
        home = findViewById(R.id.home);
        modelname = findViewById(R.id.editmdname);
        mobile_no = findViewById(R.id.editno);
        modelno = findViewById(R.id.modelnumber);
        stock=findViewById(R.id.stock);
        merchant_data = findViewById(R.id.editmid);
        dish1 = findViewById(R.id.admin_optn1_name);
        dish2 = findViewById(R.id.admin_optn2_name);
        dish3 = findViewById(R.id.admin_optn3_name);
        dish4 = findViewById(R.id.admin_optn4_name);
        dish5 = findViewById(R.id.admin_optn5_name);
        dish6 = findViewById(R.id.admin_optn6_name);
        dish1_rate = findViewById(R.id.admin_optn1_rate);
        dish2_rate = findViewById(R.id.admin_optn2_rate);
        dish3_rate = findViewById(R.id.admin_optn3_rate);
        dish4_rate = findViewById(R.id.admin_optn4_rate);
        dish5_rate = findViewById(R.id.admin_optn5_rate);
        dish6_rate = findViewById(R.id.admin_optn6_rate);
        dish1status = findViewById(R.id.optn1_status);
        dish2status = findViewById(R.id.optn2_status);
        dish3status = findViewById(R.id.optn3_status);
        dish4status = findViewById(R.id.optn4_status);
        dish5status = findViewById(R.id.optn5_status);
        dish6status = findViewById(R.id.optn6_status);
        dish1_enable = findViewById(R.id.dish1_enable);
        dish1_disable = findViewById(R.id.dish1_disable);
        dish2_enable = findViewById(R.id.dish2_enable);
        dish2_disable = findViewById(R.id.dish2_disable);
        dish3_enable = findViewById(R.id.dish3_enable);
        dish3_disable = findViewById(R.id.dish3_disable);
        dish4_enable = findViewById(R.id.dish4_enable);
        dish4_disable = findViewById(R.id.dish4_disable);
        dish5_enable = findViewById(R.id.dish5_enable);
        dish5_disable = findViewById(R.id.dish5_disable);
        dish6_enable = findViewById(R.id.dish6_enable);
        dish6_disable = findViewById(R.id.dish6_disable);
        save = findViewById(R.id.save);
        ID1=findViewById(R.id.sr1);
        ID2=findViewById(R.id.sr2);
        ID3=findViewById(R.id.sr3);
        ID4=findViewById(R.id.sr4);
        ID5=findViewById(R.id.sr5);
        ID6=findViewById(R.id.sr6);
        free = findViewById(R.id.free);
        paid = findViewById(R.id.paid);
        value = findViewById(R.id.value);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            v.getLocationOnScreen(sourceCoordinates);
            float x = ev.getRawX() + v.getLeft() - sourceCoordinates[0];
            float y = ev.getRawY() + v.getTop() - sourceCoordinates[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                hideKeyboard(Admin.this);
            }

        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(Activity activity) {
        findViewById(android.R.id.content).clearFocus();
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void stockdialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.stockr);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        Button button = dialog.findViewById(R.id.savestock);
        EditText stock1 = dialog.findViewById(R.id.dish1stock);
        EditText stock2 = dialog.findViewById(R.id.dish2stock);
        EditText stock3 = dialog.findViewById(R.id.dish3stock);
        EditText stock4 = dialog.findViewById(R.id.dish4stock);
        EditText stock5 = dialog.findViewById(R.id.dish5stock);
        EditText stock6 = dialog.findViewById(R.id.dish6stock);



        SharedPreferences sharedPreferences = getSharedPreferences("jc", MODE_PRIVATE);
        //owner info
        String dishstock1 = sharedPreferences.getString("stock1", "");
        stock1.setText(dishstock1);
        String dishstock2 = sharedPreferences.getString("stock2", "");
        stock2.setText(dishstock2);
        String dishstock3 = sharedPreferences.getString("stock3", "");
        stock3.setText(dishstock3);
        String dishstock4 = sharedPreferences.getString("stock4", "");
        stock4.setText(dishstock4);
        String dishstock5 = sharedPreferences.getString("stock5", "");
        stock5.setText(dishstock5);
        String dishstock6 = sharedPreferences.getString("stock6", "");
        stock6.setText(dishstock6);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("jc", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //owner info.
                editor.putString("stock1", stock1.getText().toString().trim());
                editor.putString("stock2", stock2.getText().toString().trim());
                editor.putString("stock3",stock3.getText().toString().trim());
                editor.putString("stock4",stock4.getText().toString().trim());
                editor.putString("stock5",stock5.getText().toString().trim());
                editor.putString("stock6",stock6.getText().toString().trim());

                Toast.makeText(getApplicationContext(),"Stock Saved Successfully",Toast.LENGTH_SHORT).show();
                editor.apply();
                dialog.dismiss();
            }
        });
        if(!((Activity) context).isFinishing())
        {
            //show dialog
            dialog.show();
        }

    }
}