package com.clima.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.clima.utils.ClimaUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btnObtenerClima;
    private TextView tvUrl;
    private TextView tvResponseJson;
    private TextView tvTemperaturaCelcius;
    private TextView tvEstadoCielo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnObtenerClima = (Button) findViewById(R.id.btn_obtener_clima);
        tvUrl = (TextView) findViewById(R.id.tv_url);
        tvResponseJson = (TextView) findViewById(R.id.tv_response_json);
        tvTemperaturaCelcius = (TextView) findViewById(R.id.tv_temperatura_celcius);
        tvEstadoCielo = (TextView) findViewById(R.id.tv_estado_cielo);

        btnObtenerClima.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                obtenerClima();
            }
        });

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
    }

    private void castJsonFormat(String json){
        int idError = 0;
        try{
            StringBuilder sb = new StringBuilder(); idError++;
            JSONObject jsonObject = new JSONObject(json);  idError++;

            JSONObject  jsonMain = jsonObject.getJSONObject("main");  idError++;
            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather"); idError++;
            JSONObject jsonWeather = jsonArrayWeather.getJSONObject(0); idError++;

            String celcius = jsonMain.getString("temp"); idError++;         // Obtener el tag temp del JSON
            String weather = jsonWeather.getString("main"); idError++;

            tvTemperaturaCelcius.setText("Temperatura C. = " + celcius);  idError++;
            tvEstadoCielo.setText("Estado = " + weather); idError++;
        }catch(Exception ex){
            ex.printStackTrace();
            Log.d("MainActivity > castJson", ex.getMessage() + " | iderror = " + idError);
        }
    }

    // Clase para manejar las peticiones al API de Openweathermaps
    public class ClimaAsyncTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            tvResponseJson.setText("Cargando informacion...");
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
}
