package com.codestracture.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HtmlTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "HtmlTask";

    private HtmlTaskCallback callback;
    private String href;

    public HtmlTask(HtmlTaskCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... urls) {
        String strUrl = urls[0];
        href = urls[1];
//        try {
//            URL url = new URL(strUrl);
//            URLConnection urlConnection = url.openConnection();
//            InputStream inputStream = urlConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Utils.charsetNameForURLConnection(urlConnection)));
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line).append('\n');
//            }
//            if (stringBuilder.length() > 0)
//                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//            return stringBuilder.toString();
//        } catch (IOException e) {
//            Log.e(TAG, "HtmlTask failed", e);
//        }
        return null;
    }

    @Override
    protected void onPostExecute(String htmlString) {
        if (htmlString != null) {
            callback.onReceiveHtml(href, htmlString);
        } else {
            callback.onError(href);
        }
        cancel(true);
    }
}
