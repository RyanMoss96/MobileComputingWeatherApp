package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

/**
 * Created by ryanmoss on 30/01/2016.
 */
public class WeatherIcon {

    private String iconURL = "http://openweathermap.org/img/w/";

    public Bitmap getIcon(String iconCode)
    {
        try {
            URL url = new URL(iconURL + iconCode + ".png");
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bmp;

        } catch (Exception ex) {

        }
        return null;
    }
}
