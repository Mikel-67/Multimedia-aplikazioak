package com.example.challenge1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button kalkulatubtn;
    EditText incheak;
    TextView emaitza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIds();


        kalkulatubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incheak.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                }else
                {
                    kalkulatuIncheMetro();
                }
            }
        });
    }

    private void getIds(){
        kalkulatubtn = findViewById(R.id.kalkulatubtn);
        incheak = findViewById(R.id.inches);
        emaitza = findViewById(R.id.emaitza);
    }

    private void kalkulatuIncheMetro(){
        int incheak = Integer.parseInt(this.incheak.getText().toString());
        double emaitza = incheak * 0.0254;
        this.emaitza.setText(emaitza + " meters");
    }
}