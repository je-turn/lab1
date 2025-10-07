package com.example.mycalculator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btn_dot, btn_clear, btn_plus, btn_minus, btn_mult, btn_div, btn_equal;
    TextView text_display;
    boolean toClear = false;


    // This is to evaluate the math expression

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.button5);
        btn5 = (Button) findViewById(R.id.button6);
        btn6 = (Button) findViewById(R.id.button7);
        btn7 = (Button) findViewById(R.id.button9);
        btn8 = (Button) findViewById(R.id.button10);
        btn9 = (Button) findViewById(R.id.button11);
        btn0 = (Button) findViewById(R.id.btn0);
        btn_dot = (Button) findViewById(R.id.btn_dot);
        btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_minus = (Button) findViewById(R.id.button8);
        btn_mult = (Button) findViewById(R.id.button12);
        btn_div = (Button) findViewById(R.id.button16);
        btn_equal = (Button) findViewById(R.id.btn_equal);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        text_display = (TextView) findViewById(R.id.textview_input_display);

        setClickListeners();
    }

    private void setClickListeners() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        btn_dot.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_mult.setOnClickListener(this);
        btn_div.setOnClickListener(this);
        btn_equal.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (toClear) {
            clear_display();
            toClear = false;
        }
        switch (v.getId()) {
            case R.id.btn1:
                addNumber("1");
                break;
            case R.id.btn2:
                addNumber("2");
                break;
            case R.id.btn3:
                addNumber("3");
                break;
            case R.id.button5:
                addNumber("4");
                break;
            case R.id.button6:
                addNumber("5");
                break;
            case R.id.button7:
                addNumber("6");
                break;
            case R.id.button9:
                addNumber("7");
                break;
            case R.id.button10:
                addNumber("8");
                break;
            case R.id.button11:
                addNumber("9");
                break;
            case R.id.btn0:
                addNumber("0");
                break;
            case R.id.btn_dot:
                addNumber(".");
                break;
            case R.id.btn_plus:
                addNumber("+");
                break;
            case R.id.button8:
                addNumber("-");
                break;
            case R.id.button12:
                addNumber("*");
                break;
            case R.id.button16:
                addNumber("/");
                break;
            case R.id.btn_equal:
                String result = null;
                try {
                    result = evaluate(text_display.getText().toString());
                    text_display.setText(result);
                } catch (Exception e) {
                    text_display.setText("Error");
                    toClear = true;
                }
                break;
            case R.id.btn_clear:
                clear_display();
                break;
        }
    }

    private String evaluate(String expression) throws Exception {
        String result;
        if (expression.length() < 1) {
            return "";
        } else if (!validFunction(expression)){
            throw new IllegalArgumentException("invalid expression");
        } else {
            result = compute(expression);
        }
        toClear = true;
        return result;
    }

    private void addNumber(String number) {
        text_display.setText(text_display.getText() + number);
    }

    private void clear_display() {
        text_display.setText("");
    }






    private String compute(String expression) {
        /* assume correct format (number or valid operation of two numbers)*/

        int plus = expression.indexOf("+");
        int minus = -1;
        int minus1 = expression.indexOf("-");
        int minus2 = expression.substring(minus+1).indexOf("-");
        int minus3 = -1;
        if (minus2 != -1) { minus3 = expression.substring(minus2+1).lastIndexOf("-"); }
        int times = expression.indexOf("*");
        int divby = expression.indexOf("/");

        //no operation conducted
        if (plus == -1 && (minus1 == -1 || (minus1 == 0 && minus2 == -1)) && times == -1 && divby == -1) {
            return expression;
        }

        //find index of operand
        int opdex = Math.max(plus, Math.max(times, divby));
        if (opdex == -1) {
            if (minus3 != -1 || minus1 == 0) { opdex = minus2; }
            else { opdex = minus1; }
        }

        //split terms from operand
        double term1 = Double.parseDouble(expression.substring(0, opdex));
        double term2 = Double.parseDouble(expression.substring(opdex+1));
        char oper = expression.charAt(opdex);

        //execute operation
        double answer;
        if (oper == '+') {
            answer = term1 + term2;
        } else if (oper == '-') {
            answer = term1 - term2;
        } else if (oper == '*') {
            answer = term1 * term2;
        } else if (term2 != 0) {
            answer = term1 / term2;
        } else {
            throw new IllegalArgumentException("cannot divide by 0");
        }

        //formatting answer
        if (Math.floor(answer) == Math.ceil(answer)) {
            return Integer.toString(Double.valueOf(answer).intValue());
        } else {
            BigDecimal decimal = new BigDecimal(answer);
            return decimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        }

    }






    private boolean validFunction(String expression) {
        int len = expression.length();
        boolean valid = true;
        int opdex = -1;
        boolean neg1 = false;
        boolean neg2 = false;
        boolean hasdec = false;
        int i = 0;
        if (!Character.isDigit(expression.charAt(len-1))) {
            valid = false;
        }
        while (i < len && valid) {
            char symb = expression.charAt(i);

            //operators
            if (symb == '+' || symb == '-' || symb == '*' || symb == '/') {
                if (symb == '-' && i == opdex+1) {
                    //negative sign
                    if (i == 0) {
                        neg1 = true;
                    } else {
                        neg2 = true;
                    }
                } else if (i == 0 || opdex > -1) {
                    valid = false;
                } else {
                    opdex = i;
                    hasdec = false;
                }
            }

            //decimal
            if (symb == '.') {
                if (hasdec || i == 0 || (neg1 && i == 1) || (i == opdex+1) || (neg2 && i == opdex+2)) {
                    valid = false;
                } else {
                    hasdec = true;
                }
            }

            i++;
        }
        return valid;
    }

}
