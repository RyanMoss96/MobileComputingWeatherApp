package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.json.JSONObject;

import java.util.Calendar;


public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog progress;
    Context context = this;
    public static JSONObject jsonObj;
    String cityName;
    String temperature;
    String apiKey;
    NotificationManager mNotificationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        apiKey = context.getString(R.string.open_weather_maps_app_id);

        SharedPreferences homeCityPref = getSharedPreferences("HomeCityPref", 0);
        cityName = homeCityPref.getString("HomeCity", null);

        if (cityName != null) {
            progress = ProgressDialog.show(this, "Connecting",
                    "Getting the weather data for " + cityName, true);
            new weatherAPI().execute(cityName, apiKey);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.home) {
            intent = new Intent(this, HomeScreen.class);
        } else if (id == R.id.favourites) {
            intent = new Intent(this, FavouritesActivity.class);

        }
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setNotification(Bitmap bmp) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setLargeIcon(bmp);
        mBuilder.setSmallIcon(R.drawable.ic_menu_camera);
        mBuilder.setContentTitle("The Weather in " + cityName);
        mBuilder.setContentText(temperature + " \u2103");
        mBuilder.setOngoing(true);
        Intent resultIntent = new Intent(context, HomeScreen.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomeScreen.class);


        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);


        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        mNotificationManager.notify(1, mBuilder.build());

        Intent intent = new Intent(this, HomeScreen.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        long recurring = (1 * 60000);  // in milliseconds
        am.setRepeating(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis(), recurring, sender);


    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (cityName != null) {
                new weatherAPI().execute(cityName, apiKey);
            }

        }
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
            String[] weatherInfo = jsonParse.getWeather();
            String[] coordInfo = jsonParse.getCoords();
            String[] baseInfo = jsonParse.getBase();
            String[] main = jsonParse.getMain();

            temperature = main[0];

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

            if (progress.isShowing()) {
                progress.dismiss();
            }

            //Todo: Decide on the layout for the weather data.
            ImageView slideImage = (ImageView) findViewById(R.id.slideImage);

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

            setNotification(resizedBitmap);


        }
    }

}



