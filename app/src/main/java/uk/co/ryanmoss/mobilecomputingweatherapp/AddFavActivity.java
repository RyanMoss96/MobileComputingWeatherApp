package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddFavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fav);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final  Context ctx = this;

        Button button = (Button) findViewById(R.id.btnAddCity);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                EditText textCity = (EditText) findViewById(R.id.eTxtCityName);
                String cityName = textCity.getText().toString();

                if(cityName.isEmpty()) {
                    Toast toast = Toast.makeText(ctx, "Please enter a City Name.", Toast.LENGTH_SHORT);
                    toast.show();

                } else{
                    Log.e("city", cityName);
                    cityName = cityName.replaceAll("\\s+","");

                    GetSetFavCities favourites = new GetSetFavCities();

                    favourites.readCities(ctx);
                    favourites.addFavouriteCity(cityName);
                    favourites.sortCities();
                    favourites.writeCities(ctx);

                    Toast toast = Toast.makeText(ctx, cityName + " has been saved as a favourite.", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
