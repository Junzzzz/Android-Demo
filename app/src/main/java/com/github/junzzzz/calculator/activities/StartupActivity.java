package com.github.junzzzz.calculator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.github.junzzzz.calculator.R;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        findViewById(R.id.test).setOnClickListener(view -> {
            startActivity(new Intent(this, CalculatorActivity.class));
        });
    }
}