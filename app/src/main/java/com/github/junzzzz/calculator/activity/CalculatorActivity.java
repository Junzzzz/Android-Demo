package com.github.junzzzz.calculator.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.junzzzz.calculator.support.Calculator;
import com.github.junzzzz.calculator.R;

public class CalculatorActivity extends AppCompatActivity {
    private static final int[] BTN_ID = new int[]{
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5,
            R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_decimal, R.id.btn_p0, R.id.btn_p1
    };

    private static final int[] BTN_OPERATOR_ID = new int[]{
            R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide
    };

    private TextView formulaView;
    private TextView resultView;

    private Calculator calculator;
    private StringBuilder str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        this.formulaView = findViewById(R.id.formula);
        this.resultView = findViewById(R.id.result);

        this.calculator = new Calculator();
        this.str = new StringBuilder();

        for (int value : BTN_ID) {
            Button btn = findViewById(value);

            btn.setOnClickListener(v -> appendText(btn));
        }

        for (int value : BTN_OPERATOR_ID) {
            Button btn = findViewById(value);
            btn.setOnClickListener(v -> {
                if (this.str.length() == 0 && this.resultView.getText().length() > 0) {
                    preserveAppendText(btn);
                } else {
                    appendText(btn);
                }
            });
        }

        findViewById(R.id.btn_delete).setOnClickListener(view -> {
            if (this.str.length() == 0) return;
            this.str.deleteCharAt(this.str.length() - 1);
            this.resultView.setText(this.str.toString());
        });

        findViewById(R.id.btn_clear).setOnClickListener(view -> {
            this.formulaView.setText("");
            this.resultView.setText("0");
            this.str = new StringBuilder();
        });

        findViewById(R.id.btn_equal).setOnClickListener(view -> {
            String formula = this.str.toString();
            String result = Integer.toString(this.calculator.calculate(formula));
            this.formulaView.setText(formula);
            this.resultView.setText(result);
            this.str = new StringBuilder();
        });
    }

    private void appendText(Button button) {
        this.str.append(button.getText());
        this.resultView.setText(this.str.toString());
    }

    private void preserveAppendText(Button button) {
        this.str.append(this.resultView.getText())
                .append(button.getText());
        this.resultView.setText(this.str.toString());
    }
}