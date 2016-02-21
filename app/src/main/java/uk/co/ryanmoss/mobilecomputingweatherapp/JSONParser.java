package uk.co.ryanmoss.mobilecomputingweatherapp;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryanmoss on 28/01/2016.
 */
public class JSONParser {

    JSONArray[] weatherArray;
    JSONObject weatherJson;
    JSONParser(JSONObject json) {
        this.weatherJson = json;
    }

    public Double[] getCoords() {
        try {

            JSONObject coorObj = weatherJson.getJSONObject("coord");

            Double[] coord = new Double[2];

           coord[0] = coorObj.getDouble("lon");
            coord[1] = coorObj.getDouble("lat");


            return coord;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getWeather() {
        try {
            JSONArray wJson = weatherJson.getJSONArray("weather");
            JSONObject jsonObj = wJson.getJSONObject(0);
            String[] weather = new String[4];

            weather[0] = jsonObj.getString("id");
            weather[1] = jsonObj.getString("main");
            weather[2] = jsonObj.getString("description");
            weather[3] = jsonObj.getString("icon");
            return weather;
        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getBase() {
        try {
            String[] base = new String[1];
            base[0] = weatherJson.getString("base");
            return base;
        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getMain() {
        try {
            JSONObject mainObj = weatherJson.getJSONObject("main");
            String[] main = new String[5];
            main[0] = mainObj.getString("temp");
            main[1] = mainObj.getString("pressure");
            main[2] = mainObj.getString("humidity");
            main[3] = mainObj.getString("temp_min");
            main[4] = mainObj.getString("temp_max");

            return main;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getWind() {
        try {
            JSONObject windObj = weatherJson.getJSONObject("wind");
            String[] wind = new String[2];

            wind[0] = windObj.getString("speed");
            wind[1] = windObj.getString("deg");

            return wind;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getClouds() {
        try {
            JSONObject cloudsObj = weatherJson.getJSONObject("clouds");
            String[] clouds= new String[1];

            clouds[0] = cloudsObj.getString("all");

            return clouds;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getDT() {
        try {
            String[] dt= new String[1];

            dt[0] = weatherJson.getString("dt");

            return dt;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getSys() {
        try {
            JSONObject sysObj = weatherJson.getJSONObject("sys");
            String[] sys= new String[6];


            sys[0] = sysObj.getString("message");
            sys[1] = sysObj.getString("country");
            sys[2] = sysObj.getString("sunrise");
            sys[3] = sysObj.getString("sunset");

            return sys;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getID() {
        try {
            String[]id= new String[1];

            id[0] = weatherJson.getString("id");

            return id;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public String[] getName() {
        try {
            String[] name= new String[1];

            name[0] = weatherJson.getString("name");

            return name;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }

    public int[] getCod() {
        try {
            int[] cod = new int[1];

            cod[0] = weatherJson.getInt("cod");

            return cod;

        } catch (JSONException e) {
            Log.e("JSONParser", e.getMessage());
        }
        return null;
    }
}
