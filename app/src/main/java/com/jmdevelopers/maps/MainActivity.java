package com.jmdevelopers.maps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
   private TextView latitude;
   private  TextView longitude;
   private TextView endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        endereco=findViewById(R.id.endereco);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Double relatitude= extras.getDouble("latitude");
            Double relongitude = extras.getDouble("longitude");
            String reendereco=extras.getString("addres");
            latitude.setText(relatitude.toString());
            longitude.setText(relongitude.toString());
            endereco.setText(reendereco);

        }


    }
}
