package ru.mirea.lugovoy.practice4.loadermanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    public final String TAG = this.getClass().getSimpleName();
    private int LoaderID = 1234;
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader)
    {
        Log.d(TAG, "onLoaderReset");
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle)
    {
        if (i == LoaderID)
        {
            Toast.makeText(this, "onCreateLoader:" + i, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, bundle);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s)
    {
        if (loader.getId() == LoaderID)
        {
            Log.d(TAG, "onLoadFinished" + s);
            Toast.makeText(this, "onLoadFinished:" + s, Toast.LENGTH_SHORT).show();
            textView.setText(s);
        }
    }

    public void onClick(View view)
    {
        Bundle bundle = new Bundle();
        bundle.putString(MyLoader.ARG_WORD, editText.getText().toString());
        getSupportLoaderManager().initLoader(LoaderID, bundle, this);
    }
}