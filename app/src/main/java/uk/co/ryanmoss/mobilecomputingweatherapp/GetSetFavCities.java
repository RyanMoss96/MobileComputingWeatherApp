package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ryanmoss on 30/01/2016.
 */
public class GetSetFavCities {

    private String fileName = "FavouriteCities.txt";
    private ArrayList<String> FavCityList = new ArrayList<>();

    public String getFileName() {
        return fileName;
    }

    public void addFavouriteCity(String city) {
        FavCityList.add(city);
        Log.e("Fav", FavCityList.toString());
    }

    public void removeFavouriteCity(String city)
    {
        FavCityList.remove(city);
        Log.e("Fav", FavCityList.toString());
    }

    public ArrayList<String> getFavouriteCity()
    {
        return FavCityList;
    }

    public void writeCities(Context ctx){
        try{
            FileOutputStream fos = ctx.openFileOutput(fileName, ctx.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(FavCityList);
            os.close();
            fos.close();
        } catch(FileNotFoundException e)
        {
            Log.e("Write File", e.getMessage());
        }
        catch(IOException e)
        {
            Log.e("Write File", e.getMessage());
        }
    }

    public void readCities(Context ctx){
        try {

            FileInputStream in = ctx.openFileInput(fileName);


            ObjectInputStream ois = new ObjectInputStream(in);

            FavCityList = (ArrayList <String>)ois.readObject();

            ois.close();
            Log.e("Read", FavCityList.toString());

        } catch (IOException e) {
            Log.e("Read", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Read", e.getMessage());
        }
    }

    public void deleteFile(Context ctx){
        ctx.deleteFile(fileName);

    }

    public void sortCities()
    {
        Collections.sort(FavCityList, String.CASE_INSENSITIVE_ORDER);
    }



}
