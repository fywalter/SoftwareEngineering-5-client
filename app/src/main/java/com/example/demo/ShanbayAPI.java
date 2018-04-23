package com.example.demo;

/**
 * Created by xiaowei on 2018/4/22.
 */


import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.StringBuffer;
import android.util.Log;
import android.os.AsyncTask;
import android.content.Context;
import android.widget.Toast;

public class ShanbayAPI extends AsyncTask<String, String, Long>{
    static final String shanbayLink = "https://api.shanbay.com/bdc/search/?word=";
    private Context cxt;
    public ShanbayAPI(Context context){
        cxt = context;
    }

    static private String request(String urlString) {
        // TODO Auto-generated method stub

        StringBuffer sb = new StringBuffer("");
        HttpURLConnection connection = null;
        try{
            URL url = new URL(urlString);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (java.io.IOException e) {
            // Writing exception to log
            e.printStackTrace();
        }
        finally{
            if (connection != null){
                connection.disconnect();
            }
        }
        Log.i("Shanbay",sb.toString());
        return sb.toString();
    }
    @Override
    protected void onPreExecute() {
        Toast.makeText(cxt, "正在查询...",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Long doInBackground(String... params) {
        String result = request(shanbayLink+params[0]);
        publishProgress(result);
        return 0l;
    }


    @Override
    protected void onProgressUpdate(String... result) {
        Toast.makeText(cxt, (String)result[0],
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Long result) {

    }


}
