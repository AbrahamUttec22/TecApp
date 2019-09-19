package com.material.tecgurus.activity.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.material.tecgurus.R;
import com.material.tecgurus.utils.Tools;

public class SettingProfileLight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile_light);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
    }
}
