package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView listView = (ListView) findViewById(R.id.favCities);
        popListView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = ((TextView) view).getText().toString();
                Intent intent = new Intent(ctx, DisplayFavouriteCity.class);
                intent.putExtra("CityName", selectedCity);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                final String selectedCity = ((TextView) view).getText().toString();
                final int pos = position;

                builder.setMessage(selectedCity)
                        .setTitle(R.string.delete_text);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GetSetFavCities favourites = new GetSetFavCities();
                        favourites.readCities(ctx);
                        favourites.removeFavouriteCity(selectedCity);
                        favourites.writeCities(ctx);

                        Toast toast = Toast.makeText(ctx, selectedCity + " has been removed from your favourites.", Toast.LENGTH_SHORT);
                        toast.show();

                        popListView(listView);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


                return true;
            }
        });
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        final ListView listView = (ListView) findViewById(R.id.favCities);
        popListView(listView);
        //Refresh your stuff here
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_open_add_favourite:
                Intent intent = new Intent(this, AddFavActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_cities, menu);
        return true;
    }

    public void popListView(ListView listView)
    {
        GetSetFavCities favourites = new GetSetFavCities();

        favourites.readCities(this);
        ArrayList<String> favCities = favourites.getFavouriteCity();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, favCities);
        listView.setAdapter(adapter);
    }
}
