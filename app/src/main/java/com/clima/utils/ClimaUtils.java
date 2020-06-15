package com.clima.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import com.clima.app.MainActivity;
import com.clima.app.R;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Clase UTILS para comunicarse con la API de OpenWeatherMaps
 * URL: https://openweathermap.org/api
 */
public class ClimaUtils {

    private static final String URL_API = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String APP_ID = "appid=24985931b186aae046292154f6109b65" ;
    private static final String TEMPERATURE_METRIC_CELCIUS = "units=metric";
    private static final String LANG = "lang=es";
    private static final String CITY_ID_URL = "id=4726206";

    public static URL buildUrl(){
        Uri uri = Uri.parse(URL_API + CITY_ID_URL+ "&" + APP_ID + "&" + TEMPERATURE_METRIC_CELCIUS + "&" + LANG);
        Log.d("ClimaUtils > buildUrl", uri.toString());
        URL url = null;
        try{
            url = new URL(uri.toString());
        }catch (MalformedURLException ex){
            ex.printStackTrace();
            Log.d("ClimaUtils > error", ex.getMessage());
        }
        return url;
    }

    public static String getResponseFromHttp(URL url) throws IOException{
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        //Log.i("ClimaUtils > getResponseFromHttp ");
        try{
            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if(hasInput){
                return scanner.next();
            } else {
                return null;
            }

        }catch(Exception ex){
            ex.printStackTrace();
            Log.d("ClimaUtils > Error > ", ex.getMessage());
            return null;
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public String getImage(String icon){
        return String.format("https://openweathermap.org/img/wn/%s.png", icon);
    }

    public static String getDateNow(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
