package ru.mirea.lugovoy.practice7.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    private final String URL_WHOIS = "http://ip-api.com/json/";

    private Button button;
    private TextView ipTextView;
    private TextView providerTextView;
    private TextView countryTextView;
    private TextView regionTextView;
    private TextView cityTextView;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.ipButton);
        ipTextView = findViewById(R.id.ipTextView);
        providerTextView = findViewById(R.id.providerTextView);
        countryTextView = findViewById(R.id.countryTextView);
        regionTextView = findViewById(R.id.regionTextView);
        cityTextView = findViewById(R.id.cityTextView);

        context = this;
    }

    public void onClick(View view)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = null;
        if (connectivityManager != null)
        {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected())
        {
            new DownloadPageTask().execute(URL_WHOIS); // запускаем в новом потоке
        }
        else
        {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadPageTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            ipTextView.setText("Загружаем...");
            providerTextView.setText("Загружаем...");
            countryTextView.setText("Загружаем...");
            regionTextView.setText("Загружаем...");
            cityTextView.setText("Загружаем...");
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return downloadIp(urls[0]);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                JSONObject responseJson = new JSONObject(result);
                ipTextView.setText(responseJson.getString("query"));
                providerTextView.setText(responseJson.getString("org"));
                countryTextView.setText(responseJson.getString("country"));
                regionTextView.setText(responseJson.getString("regionName"));
                cityTextView.setText(responseJson.getString("city"));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }

    private String downloadIp(String address) throws IOException
    {
        InputStream inputStream = null;
        String data = "";
        try
        {
            URL url = new URL(address);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) // 200 OK
            {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1)
                {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();
                data = new String(result);
            }
            else
            {
                data = connection.getResponseMessage() + " . Error Code : " + responseCode;
            }
            connection.disconnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }

        return data;
    }
}