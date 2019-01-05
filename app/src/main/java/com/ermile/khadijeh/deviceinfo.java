package com.ermile.khadijeh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class deviceinfo extends AppCompatActivity {
    public static final String MyPref = "MyPrefers";
    public static final String Name = "nameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceinfo);


        final EditText edtName =  findViewById(R.id.edt_name);
        final Button btnSave = findViewById(R.id.btn_save);
        final SharedPreferences shPref = getSharedPreferences(MyPref, Context.MODE_PRIVATE);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String get_txt = edtName.getText().toString();
                SharedPreferences.Editor sEdit = shPref.edit();
                sEdit.putString(Name, get_txt);
                sEdit.apply();

                Toast.makeText(deviceinfo.this, "Saved", Toast.LENGTH_LONG).show();

            }
        });

        if (shPref.contains(Name)) {
            edtName.setText(shPref.getString(Name, null));
        }



    }
}