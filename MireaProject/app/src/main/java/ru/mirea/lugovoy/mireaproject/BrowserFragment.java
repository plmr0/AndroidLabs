package ru.mirea.lugovoy.mireaproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class BrowserFragment extends Fragment implements View.OnClickListener
{
    private WebView webView;
    private EditText editTextAddress;

    public BrowserFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void loadDefaultPage()
    {
        String defaultPage = "https://www.yandex.ru/";
        webView.loadUrl(defaultPage);
    }

    private void loadUrl(String url)
    {
        if (!url.startsWith("https://"))
        {
            url = "https://" + url;
            webView.loadUrl(url);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "CutPasteId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_browser, container, false);

        editTextAddress = (EditText) view.findViewById(R.id.editTextAddressBar);

        Button go = view.findViewById(R.id.buttonGo);
        go.setOnClickListener(this);

        Button back = view.findViewById(R.id.buttonBack);
        back.setOnClickListener(this);

        Button forward = view.findViewById(R.id.buttonForward);
        forward.setOnClickListener(this);

        webView = (WebView) view.findViewById(R.id.browser);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView v, String url)
            {
                super.onPageFinished(v, url);
                editTextAddress.setText(url);
            }
        });

        loadDefaultPage();

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.buttonGo:
                String url = this.editTextAddress.getText().toString();
                loadUrl(url);
                break;
            case R.id.buttonBack:
                webView.goBack();
                break;
            case R.id.buttonForward:
                webView.goForward();
                break;
            default:
                break;
        }
    }
}