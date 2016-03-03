package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ryanmoss on 25/02/2016.
 */
public class WikipediaClass {

    public void openWiki(String cityName, Context context)
    {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/" + cityName));
        context.startActivity(browserIntent);
    }
}
