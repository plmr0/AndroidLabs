package ru.mirea.lugovoy.mireaproject.ui.bitcoin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.lugovoy.mireaproject.R;

public class BitcoinFragment extends Fragment
{
    private final String URL = "https://api.coindesk.com/v1/bpi/currentprice.json";

    private TextView date;
    private TextView dollar;
    private TextView euro;
    private TextView pound;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bitcoin, container, false);

        this.date = view.findViewById(R.id.dateBitcoinTextView);
        this.dollar = view.findViewById(R.id.dollarBitcoinTextView);
        this.euro = view.findViewById(R.id.euroBitcoinTextView);
        this.pound = view.findViewById(R.id.poundBitcoinTextView);

        loadInfo();

        return view;
    }

    public void loadInfo()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = null;
        if (connectivityManager != null)
        {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected())
        {
            new DownloadPageTask().execute(URL);
        }
        else
        {
            Toast.makeText(requireContext(), getString(R.string.web_info_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadPageTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            date.setText(getString(R.string.web_info_downloading));
            dollar.setText(getString(R.string.web_info_downloading));
            euro.setText(getString(R.string.web_info_downloading));
            pound.setText(getString(R.string.web_info_downloading));
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                return downloadBitcoin(urls[0]);
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
                JSONObject timeJson = responseJson.getJSONObject("time");
                JSONObject currenciesJson = responseJson.getJSONObject("bpi");

                String time = timeJson.getString("updated");
                String usd = currenciesJson.getJSONObject("USD").getString("rate");
                String eur = currenciesJson.getJSONObject("EUR").getString("rate");
                String gbp = currenciesJson.getJSONObject("GBP").getString("rate");

                date.setText(time);
                dollar.setText(String.format("%s $", usd));
                euro.setText(String.format("%s €", eur));
                pound.setText(String.format("%s £", gbp));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }

    private String downloadBitcoin(String address) throws IOException
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