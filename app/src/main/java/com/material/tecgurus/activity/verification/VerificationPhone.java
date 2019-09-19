package com.material.tecgurus.activity.verification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.material.tecgurus.R;
import com.material.tecgurus.utils.Tools;

public class VerificationPhone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_phone);
        Tools.setSystemBarColor(this, R.color.grey_20);
    }
}
