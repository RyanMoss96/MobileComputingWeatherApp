package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by ryanmoss on 28/01/2016.
 */
public class WeatherHTTP  {

    private String baseURL = "http://api.openweathermap.org/data/2.5/weather?APPID=";


    public JSONObject connectAPI(String city, String api) {
        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            baseURL += api+ "&units=metric&q=" + city;
            URL url = new URL((baseURL));
            connection = (HttpURLConnection)url.openConnection();
            Log.e("Wea2therHTTP", url.toString());
            //connection.addRequestProperty("x-api-key", params[1]);
            connection.setRequestMethod("GET");


            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();


            connection.disconnect();

            JSONObject data = new JSONObject(buffer.toString());



            return data;

        } catch(MalformedURLException e) {
            Log.e("1", e.getMessage());
        } catch(IOException e)  {
            Log.e("Wea2therHTTP", e.getMessage());
            Log.e("L", "Explanation of what was being attempted when the exception was thrown", e);
        } catch(JSONException e) {
            Log.e("Wea3therHTTP", e.getMessage());
        } finally {
            try {
                Log.i("MyActivity", "MyClass.getView() — get item number " + connection.getResponseCode());
                connection.disconnect();
            } catch(Throwable t) {

            }
        }

        return null;

    }








}
