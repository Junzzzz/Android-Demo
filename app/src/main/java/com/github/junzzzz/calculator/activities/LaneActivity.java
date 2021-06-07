package com.github.junzzzz.calculator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.github.junzzzz.calculator.R;
import com.github.junzzzz.calculator.support.LaneOpenCV;

public class LaneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lane);
        findViewById(R.id.testView).setOnClickListener(view -> {
            ((TextView) view).setText(LaneOpenCV.stringFromJNI());
        });

    }
}