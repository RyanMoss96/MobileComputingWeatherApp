package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


public class DisplayFavouriteCity extends AppCompatActivity {

    public ProgressDialog progress;
    public String cityName;
    public Context ctx = this;

    public String temperature;
    public String humidity;
    public String mainWeather;
    public String descWeather;
    public String sunrise;
    public String sunset;
    public Double latitude;
    public Double longitude;
    public int cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_favourite_city);



        Button setHomeButton = (Button) findViewById(R.id.action_set_home);
        Intent intent = getIntent();
        String apiKey = ctx.getString(R.string.open_weather_maps_app_id);

        cityName = intent.getExtras().getString("CityName");
        setTitle(cityName);
        new weatherAPI().execute(cityName, apiKey);
        progress = ProgressDialog.show(this, "Connecting",
                "Getting the weather data for " + cityName, true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, MapsActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                startActivity(intent);
            }
        });

        FloatingActionButton fabWiki = (FloatingActionButton) findViewById(R.id.fabWiki);
        fabWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WikipediaClass wiki = new WikipediaClass();
                wiki.openWiki(cityName, ctx);
            }
        });

            TextView lblTemperature = (TextView) findViewById(R.id.lblTemp);

            lblTemperature.setText(temperature + " \u2103");





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_set_home:
                SharedPreferences settings = getSharedPreferences("HomeCityPref", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("HomeCity", cityName);

                Toast toast = Toast.makeText(ctx, cityName + " has been set as your Home City!", Toast.LENGTH_SHORT);
                toast.show();

                // Commit the edits!
                editor.commit();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.set_home, menu);
        return true;
    }

    public class weatherAPI extends AsyncTask<String, Void, JSONObject> {


        public void onPreExecute() {

        }

        protected JSONObject doInBackground(String... params) {

            WeatherHTTP weather = new WeatherHTTP();
            JSONObject json = weather.connectAPI(params[0], params[1]);


            return json;

        }

        protected void onPostExecute(JSONObject data) {

            JSONParser jsonParse = new JSONParser(data);
            Log.e("hh", data.toString());
            String[] weatherInfo = jsonParse.getWeather();
            String[] main = jsonParse.getMain();
            String[] system = jsonParse.getSys();
            Double[] coord = jsonParse.getCoords();
            String[] name = jsonParse.getName();
            int[] codA = jsonParse.getCod();
            cod = codA[0];

                TextView lblTemperature = (TextView) findViewById(R.id.lblTemp);
                TextView lblWeather = (TextView) findViewById(R.id.lblWeather);
                lblTemperature.setText(main[0] + " \u2103");
                lblWeather.setText(weatherInfo[1]);
            setTitle(name[0]);
                latitude = coord[1];
                longitude = coord[0];

                new weatherIcon().execute(weatherInfo[3]);
        }
    }

    public class weatherIcon extends AsyncTask<String, Void, Bitmap> {

        public void onPreExecute() {

        }

        protected Bitmap doInBackground(String... params) {

            WeatherIcon icon = new WeatherIcon();
            Bitmap bmp = icon.getIcon(params[0]);

            return bmp;
        }

        protected void onPostExecute(Bitmap bmp) {
                progress.dismiss();
                ImageView img = (ImageView) findViewById(R.id.weatherImage);
                final int maxSize = 500;
                int outWidth;
                int outHeight;

                int inWidth = bmp.getWidth();
                int inHeight = bmp.getHeight();

                if (inWidth > inHeight) {
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, outWidth, outHeight, false);
                img.setImageBitmap(resizedBitmap);
        }
    }
}


