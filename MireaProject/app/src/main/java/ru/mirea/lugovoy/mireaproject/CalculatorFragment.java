package ru.mirea.lugovoy.mireaproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CalculatorFragment extends Fragment implements View.OnClickListener
{
    private TextView textViewResult;
    private TextView textViewPrevious;

    private String input = "";
    private String previous = "";
    private String action = "";

    private Double numberA = null;
    private Double numberB = null;

    public CalculatorFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        this.textViewResult = (TextView) view.findViewById(R.id.textViewResult);
        this.textViewPrevious = (TextView) view.findViewById(R.id.textViewPrevious);

        Button zero = view.findViewById(R.id.buttonZero);
        zero.setOnClickListener(this);

        Button one = view.findViewById(R.id.buttonOne);
        one.setOnClickListener(this);

        Button two = view.findViewById(R.id.buttonTwo);
        two.setOnClickListener(this);

        Button three = view.findViewById(R.id.buttonThree);
        three.setOnClickListener(this);

        Button four = view.findViewById(R.id.buttonFour);
        four.setOnClickListener(this);

        Button five = view.findViewById(R.id.buttonFive);
        five.setOnClickListener(this);

        Button six = view.findViewById(R.id.buttonSix);
        six.setOnClickListener(this);

        Button seven = view.findViewById(R.id.buttonSeven);
        seven.setOnClickListener(this);

        Button eight = view.findViewById(R.id.buttonEight);
        eight.setOnClickListener(this);

        Button nine = view.findViewById(R.id.buttonNine);
        nine.setOnClickListener(this);

        Button plus = view.findViewById(R.id.buttonPlus);
        plus.setOnClickListener(this);

        Button minus = view.findViewById(R.id.buttonMinus);
        minus.setOnClickListener(this);

        Button multiply = view.findViewById(R.id.buttonMultiply);
        multiply.setOnClickListener(this);

        Button division = view.findViewById(R.id.buttonDivide);
        division.setOnClickListener(this);

        Button equality = view.findViewById(R.id.buttonEquals);
        equality.setOnClickListener(this);

        Button dot = view.findViewById(R.id.buttonDot);
        dot.setOnClickListener(this);

        Button clear = view.findViewById(R.id.buttonClean);
        clear.setOnClickListener(this);

        return view;
    }

    private void checkForZero(int selected)
    {
        if (this.input.equals("0"))
        {
            this.input = String.valueOf(selected);
        }
        else
        {
            this.input += String.valueOf(selected);
        }

        this.textViewResult.setText(this.input);
    }

    private void cleanInput()
    {
        this.input = "";
        this.textViewResult.setText("");
    }

    private void cleanPrevious()
    {
        this.numberA = null;
        this.numberB = null;
        this.action = null;
        this.previous = "";
        this.textViewPrevious.setText("");
    }

    private void cleanAll()
    {
        cleanInput();
        cleanPrevious();
    }

    private void beginAction(String action)
    {
        if (!this.input.isEmpty() && (!this.previous.isEmpty()))
        {
            calculate();
        }

        if (!input.isEmpty())
        {
            this.numberA = Double.valueOf(this.input);
            this.action = action;
            this.previous = String.format("%s %s", this.input, this.action);
            this.textViewPrevious.setText(this.previous);
            cleanInput();
        }
    }

    private void calculate()
    {
        if (!this.input.isEmpty() && (!this.previous.isEmpty()))
        {
            Double result = 0d;

            this.numberB = Double.valueOf(this.input);
            switch (action)
            {
                case "+":
                    result = this.numberA + this.numberB;
                    break;
                case "-":
                    result = this.numberA - this.numberB;
                    break;
                case "*":
                    result = this.numberA * this.numberB;
                    break;
                case "/":
                    result = this.numberA / this.numberB;
                    break;
                default:
                    break;
            }

            cleanPrevious();

            if (result % 1 == 0) // Целое
            {
                int res_int = result.intValue();
                this.input = String.valueOf(res_int);
            }
            else // Дробное
            {
                this.input = String.valueOf(result);
            }

            this.textViewResult.setText(this.input);
        }
    }

    private void checkForDot()
    {
        if (!this.input.isEmpty())
        {
            if (!this.input.contains("."))
            {
                this.input = this.input + ".";
                this.textViewResult.setText(this.input);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonZero:
                checkForZero(0);
                break;
            case R.id.buttonOne:
                checkForZero(1);
                break;
            case R.id.buttonTwo:
                checkForZero(2);
                break;
            case R.id.buttonThree:
                checkForZero(3);
                break;
            case R.id.buttonFour:
                checkForZero(4);
                break;
            case R.id.buttonFive:
                checkForZero(5);
                break;
            case R.id.buttonSix:
                checkForZero(6);
                break;
            case R.id.buttonSeven:
                checkForZero(7);
                break;
            case R.id.buttonEight:
                checkForZero(8);
                break;
            case R.id.buttonNine:
                checkForZero(9);
                break;
            case R.id.buttonPlus:
                beginAction("+");
                break;
            case R.id.buttonMinus:
                beginAction("-");
                break;
            case R.id.buttonMultiply:
                beginAction("*");
                break;
            case R.id.buttonDivide:
                beginAction("/");
                break;
            case R.id.buttonEquals:
                calculate();
                break;
            case R.id.buttonDot:
                checkForDot();
                break;
            case R.id.buttonClean:
                cleanAll();
                break;
            default:
                break;
        }
    }
}