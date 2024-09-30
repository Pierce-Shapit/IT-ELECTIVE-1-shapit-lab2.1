package com.example.labactivity21_shapit_calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    private TextView txtResult;
    private TextView txtHistory;


    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0.0;
    private boolean isNewCalculation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.result_tv);
        txtHistory = findViewById(R.id.history_tv);


        setNumberButtonListeners();
        setOperatorButtonListeners();
        setControlButtonListeners();
    }

    private void setNumberButtonListeners() {
        for (int i = 0; i <= 9; i++) {
            String buttonID = "button_" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            findViewById(resID).setOnClickListener(new NumberClickListener());
        }
    }

    private void setOperatorButtonListeners() {
        findViewById(R.id.button_plus).setOnClickListener(new OperatorClickListener());
        findViewById(R.id.button_minus).setOnClickListener(new OperatorClickListener());
        findViewById(R.id.button_multiply).setOnClickListener(new OperatorClickListener());
        findViewById(R.id.button_divide).setOnClickListener(new OperatorClickListener());
    }

    private void setControlButtonListeners() {
        findViewById(R.id.button_c).setOnClickListener(v -> clear());
        findViewById(R.id.button_equals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.button_dot).setOnClickListener(v -> appendDot());
        findViewById(R.id.button_del).setOnClickListener(v -> deleteLastChar());
    }

    private class NumberClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;


            if (isNewCalculation) {
                currentInput = "";
                isNewCalculation = false;
            }

            currentInput += button.getText().toString();
            txtResult.setText(currentInput);
        }
    }

    private class OperatorClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;


            if (!currentInput.isEmpty()) {
                if (firstOperand != 0 || !operator.isEmpty()) {
                    calculateResult();
                } else {
                    firstOperand = Double.parseDouble(currentInput);
                }
            }

            operator = button.getText().toString();
            currentInput = "";
        }
    }

    private void calculateResult() {
        if (!currentInput.isEmpty() && !operator.isEmpty()) {
            double secondOperand = Double.parseDouble(currentInput);
            double result = 0.0;

            switch (operator) {
                case "+":
                    result = firstOperand + secondOperand;
                    break;
                case "-":
                    result = firstOperand - secondOperand;
                    break;
                case "*":
                    result = firstOperand * secondOperand;
                    break;
                case "/":
                    if (secondOperand != 0) {
                        result = firstOperand / secondOperand;
                    } else {
                        txtResult.setText("Error");
                        currentInput = "";
                        return;
                    }
                    break;
            }


            txtResult.setText(removeTrailingZeros(String.valueOf(result)));
            txtHistory.append(firstOperand + " " + operator + " " + secondOperand + " = " + removeTrailingZeros(String.valueOf(result)) + "\n");


            firstOperand = result;
            currentInput = "";
            operator = "";
            isNewCalculation = true;
        }
    }

    private void clear() {
        currentInput = "";
        operator = "";
        firstOperand = 0.0;
        txtResult.setText("0");
        txtHistory.setText("");
        isNewCalculation = false;
    }

    private void appendDot() {
        if (isNewCalculation) {
            currentInput = "0.";
            isNewCalculation = false;
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        txtResult.setText(currentInput);
    }

    private void deleteLastChar() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            txtResult.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }

    private String removeTrailingZeros(String number) {
        if (number.contains(".")) {
            number = number.replaceAll("\\.0+$", "");
            number = number.replaceAll("(\\.\\d*?)0+$", "$1");
        }
        return number;
    }
}
