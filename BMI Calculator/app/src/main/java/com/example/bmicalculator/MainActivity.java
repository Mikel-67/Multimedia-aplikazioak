package com.example.bmicalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private RadioButton male;
    private RadioButton female;
    private EditText age;
    private EditText feet;
    private EditText inches;
    private EditText weight;
    private Button calcualte;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIds();

        calcualte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double BMI = kalkulatiBMI();
                ateraemaitza(BMI);
            }
        });
    }

    private double kalkulatiBMI(){
        int feetZenbakia= Integer.parseInt(feet.getText().toString());
        int inchesZenbakia= Integer.parseInt(inches.getText().toString());
        int weightZenbakia= Integer.parseInt(weight.getText().toString());

        int inchTotalak = (feetZenbakia * 12) + inchesZenbakia;
        double metroak = inchTotalak * 0.0254;

        double bmi = weightZenbakia / (metroak * metroak);

        return bmi;
    }

    private void ateraemaitza(double bmi){
        DecimalFormat myDecimalFormat = new DecimalFormat("0.00");
        String bmiText = myDecimalFormat.format(bmi);

        String bmiCategory;
        if (bmi < 18.5) {
            bmiCategory = bmiText + "Pisu baxua";
        } else if (bmi > 25) {
            bmiCategory = bmiText + "Pisu gehiegi";
        } else {
            bmiCategory = bmiText + "Neurrian";
        }

        result.setText(bmiCategory);
    }
    private void getIds(){
        male=findViewById(R.id.radioMale);
        female=findViewById(R.id.radioFemale);
        age=findViewById(R.id.age);
        feet=findViewById(R.id.feet);
        inches=findViewById(R.id.inches);
        weight=findViewById(R.id.weight);
        calcualte=findViewById(R.id.calculate);
        result=findViewById(R.id.result);
    }
}