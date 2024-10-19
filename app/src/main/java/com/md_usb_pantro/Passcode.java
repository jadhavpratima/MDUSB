package com.md_usb_pantro;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanks.passcodeview.PasscodeView;
import com.md_usb_pantro.R;

public class Passcode extends AppCompatActivity {
PasscodeView passcodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_passcode);
        passcodeView = findViewById(R.id.passcodeView);
        overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);
        passcodeView.setPasscodeLength(4)
        .setLocalPasscode("1234")
        .setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
                Toast.makeText(getApplication(),"Wrong!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String number) {
            Intent i = new Intent(getApplicationContext(),Admin.class);
            startActivity(i);
            finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}