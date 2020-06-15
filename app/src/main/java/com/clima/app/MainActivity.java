package com.clima.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.clima.model.OpenWeatherMaps;
import com.clima.utils.ClimaUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements ClimaAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<String> {

    private Button btnObtenerClima;
    private Button btnPruebaIntent;
    private TextView tvUrl;
    private TextView tvResponseJson;
    private TextView tvTemperaturaCelcius;
    private TextView tvEstadoCielo;
    private EditText txtPruebas;
    private Toast mToast;

    private TextView txtCity, txtLastUpdate, txtDescripcion, txtCelcius, txtTime, txtHumedad;
    private ImageView imageView;

    LocationManager locationManager;
    String provider;
    static double lat, lng;
    OpenWeatherMaps openWeatherMaps = new OpenWeatherMaps();

    private static final String LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks";
    private static final int CLIMA_SEARCH_LOADER = 22;

    private static final int NUM_LIST_ITEMS = 100;
    private ClimaAdapter mAdapter;
    private RecyclerView mNumbersList;
    private int MY_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnObtenerClima = (Button) findViewById(R.id.btn_obtener_clima);
        // btnPruebaIntent =
        tvUrl = (TextView) findViewById(R.id.tv_url);
        tvResponseJson = (TextView) findViewById(R.id.tv_response_json);
        tvTemperaturaCelcius = (TextView) findViewById(R.id.tv_temperatura_celcius);
        tvEstadoCielo = (TextView) findViewById(R.id.tv_estado_cielo);
        txtPruebas = (EditText) findViewById(R.id.txt_pruebas);

        // Textos
        txtCity = (TextView) findViewById(R.id.tv_city);
        txtLastUpdate = (TextView) findViewById(R.id.tv_last_update);
        txtDescripcion = (TextView) findViewById(R.id.tv_descripcion);
        txtCelcius = (TextView) findViewById(R.id.tv_celcius);
        txtHumedad = (TextView) findViewById(R.id.tv_humedad);
        txtTime = (TextView) findViewById(R.id.tv_hora);
        imageView = (ImageView) findViewById(R.id.img_clima);

        // Obtener coordenadas
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        // Revisar si la app tiene los permisos necesarios
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },MY_PERMISSION );
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if(location == null){
            Log.e("TAG", "Sin localizacion");
        }


        //Si tiene informacion

        if (savedInstanceState != null){
            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)){

                //String allPreviousLifeCycle = savedInstanceState.getString(LIFECYCLE_CALLBACKS_TEXT_KEY);
                //tvResponseJson.setText(allPreviousLifeCycle);
            }
        }

        btnObtenerClima.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                obtenerClima();
            }
        });
/*
        btnPruebaIntent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Prueba de boton", Toast.LENGTH_LONG).show();

                Context context = MainActivity.this;

                Class destination = DetalleClimaActivity.class;

                Intent i = new Intent(context, destination);

                i.putExtra(Intent.EXTRA_TEXT, txtPruebas.getText().toString());

                startActivity(i);
            }
        });
*/

        /*
        setContentView(R.layout.activity_recycler);
        mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(linearLayoutManager);

        mNumbersList.setHasFixedSize(true);

        mAdapter = new ClimaAdapter(NUM_LIST_ITEMS, this);

        mNumbersList.setAdapter(mAdapter); */


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId){

            case R.id.refresh_button:
                mAdapter = new ClimaAdapter(NUM_LIST_ITEMS, this);
                mNumbersList.setAdapter(mAdapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Revisar si la app tiene los permisos necesarios
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },MY_PERMISSION );
        }
        locationManager.removeUpdates((LocationListener) MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if(mToast != null){
            mToast.cancel();
        }

        String toastMessage = "Item #" + clickedItemIndex + " clicked";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        mToast.show();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String lifecycleDisplay = tvResponseJson.getText().toString();
        outState.putString(LIFECYCLE_CALLBACKS_TEXT_KEY, lifecycleDisplay);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void obtenerClima(){
        String urlQuery = "";
        URL url = ClimaUtils.buildUrl();
        tvUrl.setText(url.toString());

        // Revisar si la App se ha otorgado Permisos de Internet para realizar la petición
        // caso contrario solicitar la petición de Internet
        /* if(ContextCompat.checkSelfPermission(MainActivity.this ,Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.INTERNET)){

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.INTERNET},

                        );
            }
        } */

        new ClimaAsyncTask().execute(url);

        // Loader
        Bundle queryBundle = new Bundle();
        queryBundle.putString(LIFECYCLE_CALLBACKS_TEXT_KEY, url.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> climaLoader = loaderManager.getLoader(CLIMA_SEARCH_LOADER);

        if(climaLoader == null){
            loaderManager.initLoader(CLIMA_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(CLIMA_SEARCH_LOADER, queryBundle, this);
        }

    }

    private void castJsonFormat(String json){
        int idError = 0;
        try{
            StringBuilder sb = new StringBuilder(); idError++;
            JSONObject jsonObject = new JSONObject(json);  idError++;

            JSONObject  jsonMain = jsonObject.getJSONObject("main");  idError++;
            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather"); idError++;
            String city = jsonObject.getString("name").toString(); idError++;
            JSONObject jsonWeather = jsonArrayWeather.getJSONObject(0); idError++;

            String celcius = jsonMain.getString("temp"); idError++;         // Obtener el tag temp del JSON
            String min = jsonMain.getString("temp_min"); idError++;
            String max = jsonMain.getString("temp_max"); idError++;
            String weather = jsonWeather.getString("main"); idError++;
            String descripcion = jsonWeather.getString("description"); idError++;
            String icon = jsonWeather.getString("icon"); idError++;


            tvTemperaturaCelcius.setText("Temperatura C. = " + celcius);  idError++;
            tvEstadoCielo.setText("Estado = " + weather); idError++;
            txtDescripcion.setText("Estado: " + descripcion); idError++;
            txtCity.setText("" + city.toString()); idError++;
            txtLastUpdate.setText("");
            txtCelcius.setText("Temperatura C. = " + celcius);
            txtTime.setText("Min: " + min + " Max: " + max);


            //Establecer la imagen adecuada
            ClimaUtils clima = new ClimaUtils();

            new ImageAsyncTask(imageView).execute(clima.getImage(icon));


        }catch(Exception ex){
            ex.printStackTrace();
            Log.d("MainActivity > castJson", ex.getMessage() + " | iderror = " + idError);
        }
    }

    /* Implementacion de LoaderManager*/
    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            public String loadInBackground() {
                String searchClima = bundle.getString(LIFECYCLE_CALLBACKS_TEXT_KEY);
                if(searchClima == null || TextUtils.isEmpty(searchClima)){
                    return null;
                }

                try{
                    URL climaUrl =  new URL(searchClima);
                    return ClimaUtils.getResponseFromHttp(climaUrl);
                }catch (Exception ex){
                    Log.d("MainActivity > Error > ", ex.getMessage());
                    ex.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(bundle == null){
                    return ;
                }

                tvResponseJson.setText("Cargando informacion...");
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        Log.d("onLoadFinished", s.toString());

        if (s != null && !s.equals("")){
            tvResponseJson.setText(s);
            castJsonFormat(s);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    // Clase para manejar las peticiones al API de Openweathermaps

    public class ClimaAsyncTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            // tvResponseJson.setText("Cargando informacion...");
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String resultado = "";

            try{
                resultado = ClimaUtils.getResponseFromHttp(url);
            }catch (Exception ex){
                ex.printStackTrace();
            }


            return resultado;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")){
                tvResponseJson.setText(s);
                castJsonFormat(s);
            }
        }

    }

    public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap>{

        ImageView imageView;

        public ImageAsyncTask(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            int idError = 0;
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                Log.d("ImageAsyncTask url > ", url);
                InputStream inputStream = new java.net.URL(url).openStream(); idError++;
                bitmap = BitmapFactory.decodeStream(inputStream);   idError++;
            }catch (Exception ex){
                ex.printStackTrace();
                Log.d("ImageAsyncTask ", ex.getMessage() + " | iderror = " + idError);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
