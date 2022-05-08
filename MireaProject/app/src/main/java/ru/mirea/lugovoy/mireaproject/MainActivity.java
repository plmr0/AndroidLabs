package ru.mirea.lugovoy.mireaproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.lugovoy.mireaproject.databinding.ActivityMainBinding;
import ru.mirea.lugovoy.mireaproject.db.App;
import ru.mirea.lugovoy.mireaproject.ui.stories.db.AppDatabase;

public class MainActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private final String SAVED_NAME = "saved_name";
    private final String SAVED_GROUP = "saved_group";

    private TextView navName;
    private TextView navGroup;

    private NavigationView navigationView;
    private View headerView;

    private SharedPreferences preferences;

    public static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_calculator, R.id.nav_browser, R.id.nav_music,
                R.id.nav_sensors, R.id.nav_camera, R.id.nav_recorder,
                R.id.nav_stories, R.id.nav_web_info)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        db = App.getInstance().getDatabase();

        this.headerView = navigationView.getHeaderView(0);

        this.navName = (TextView) this.headerView.findViewById(R.id.headerAuthorName);
        this.navGroup = (TextView) this.headerView.findViewById(R.id.headerAuthorGroup);

        load();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6969 && resultCode == RESULT_OK)
        {
            String name = data.getExtras().getString("name", getStringRes(R.string.nav_header_title));
            String group = data.getExtras().getString("group", getStringRes(R.string.nav_header_subtitle));

            this.navName.setText(name);
            this.navGroup.setText(group);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int selectedItem = item.getItemId();

        switch (selectedItem)
        {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_logout:
                setResult(RESULT_OK);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        setResult(RESULT_CANCELED);
        finish();
    }

    private String getStringRes(int id)
    {
        return getResources().getString(id);
    }

    private void load()
    {
        this.preferences = getSharedPreferences("user", MODE_PRIVATE);

        String name = this.preferences.getString(SAVED_NAME, getStringRes(R.string.nav_header_title));
        String group = this.preferences.getString(SAVED_GROUP, getStringRes(R.string.nav_header_subtitle));

        this.navName.setText(name);
        this.navGroup.setText(group);
    }

    private void openSettings()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 6969);
    }
}