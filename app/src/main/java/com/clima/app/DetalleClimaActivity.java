package com.clima.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetalleClimaActivity extends AppCompatActivity {

    private TextView tvInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detalle_clima_activity);

        Intent i = getIntent();

        tvInfo = findViewById(R.id.tv_detalle_clima);

        if(i.hasExtra(Intent.EXTRA_TEXT)){

            String texto = i.getStringExtra(Intent.EXTRA_TEXT);
            tvInfo.setText(texto);

        }



    }
}
