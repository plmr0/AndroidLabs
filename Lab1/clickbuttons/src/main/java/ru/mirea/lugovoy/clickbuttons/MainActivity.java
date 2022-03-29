package ru.mirea.lugovoy.clickbuttons;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private TextView tvOut;
    private Button buttonOK;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = (TextView) findViewById(R.id.tvOut);
        buttonOK = (Button) findViewById(R.id.btnCancel);
        buttonCancel = (Button) findViewById(R.id.btnCancel);

//        View.OnClickListener oclBtnOk = new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                tvOut.setText("Нажата кнопка ОК");
//            }
//        };
//
//        View.OnClickListener oclBtnCancel = new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                tvOut.setText("Нажата кнопка CANCEL");
//            }
//        };
//
//        buttonOK.setOnClickListener(oclBtnOk);
//        buttonCancel.setOnClickListener(oclBtnCancel);
    }

    public void btnOkClicked(View view)
    {
        Toast.makeText(this, "Ещё один способ!", Toast.LENGTH_SHORT).show();
        tvOut.setText("Нажата кнопка ОК");
    }

    public void  btnCancelClicked(View view)
    {
        Toast.makeText(this, "Ещё один способ!", Toast.LENGTH_SHORT).show();
        tvOut.setText("Нажата кнопка CANCEL");
    }
}