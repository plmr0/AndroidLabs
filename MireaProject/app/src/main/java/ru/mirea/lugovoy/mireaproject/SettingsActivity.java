package ru.mirea.lugovoy.mireaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity
{
    private EditText nameEditText;
    private EditText groupEditText;

    private Button saveButton;

    private final String SAVED_NAME = "saved_name";
    private final String SAVED_GROUP = "saved_group";

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.action_settings);

        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.groupEditText = (EditText) findViewById(R.id.groupEditText);

        this.saveButton = (Button) findViewById(R.id.saveSettingsButton);

        this.saveButton.setOnClickListener(this::onClickSave);

        this.preferences = getSharedPreferences("user", MODE_PRIVATE);

        load();
    }

    private String getStringRes(int id)
    {
        return getResources().getString(id);
    }

    private void load()
    {
        String name = this.preferences.getString(SAVED_NAME, "");
        String group = this.preferences.getString(SAVED_GROUP, "");

        if (name.equals(getStringRes(R.string.nav_header_subtitle)))
        {
            name = "";
        }

        if (group.equals(getStringRes(R.string.nav_header_title)))
        {
            group = "";
        }

        this.nameEditText.setText(name);
        this.groupEditText.setText(group);
    }

    private void onClickSave(View view)
    {
        SharedPreferences.Editor editor = preferences.edit();

        String name = this.nameEditText.getText().toString();
        String group = this.groupEditText.getText().toString();

        if (name.length() == 0)
        {
            name = getStringRes(R.string.nav_header_subtitle);
        }

        if (group.length() == 0)
        {
            group = getStringRes(R.string.nav_header_title);
        }

        editor.putString(SAVED_NAME, name);
        editor.putString(SAVED_GROUP, group);

        editor.apply();

        Intent resultIntent = new Intent();

        resultIntent.putExtra("name", name);
        resultIntent.putExtra("group", group);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}