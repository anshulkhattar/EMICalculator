package com.example.emicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Locale;

public class EMIDetailsInputActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText amountEditText,roiEditText;
    private Button calculateButton,resetButton;
    private Spinner tenureSpinner;
    private TextView emiTextView;
    ImageView sound;
    private Integer pos;
    TextToSpeech textToSpeech;
    int sound_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emi_details_input);

        amountEditText=(EditText)findViewById(R.id.amountEditText);
        roiEditText=(EditText)findViewById(R.id.ROIEdittext);
        tenureSpinner=(Spinner)findViewById(R.id.spinner);
        emiTextView=(TextView)findViewById(R.id.emiTextView);
        calculateButton=(Button)findViewById(R.id.calculateButton);
        resetButton=(Button)findViewById(R.id.resetButton);
        sound = findViewById(R.id.sound);
        sound_flag=1;

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sound_flag==2){
                    sound.setImageResource(R.drawable.sound_on);
                    sound_flag=1;
                }
                else {
                    sound_flag=2;
                    sound.setImageResource(R.drawable.mute);
                }

            }
        });

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.getDefault());
                }
                else{
                    Toast.makeText(EMIDetailsInputActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Integer tenures[]={3,6,9,12,24,48,36};

        tenureSpinner.setOnItemSelectedListener(this);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,tenures);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tenureSpinner.setAdapter(spinnerAdapter);


        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double amount,roi,tenure;
                amount=Integer.parseInt(amountEditText.getText().toString());
                roi=Integer.parseInt(roiEditText.getText().toString());

                calculateEMI(amount,roi);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amountEditText.setText("");
                roiEditText.setText("");
                tenureSpinner.setSelection(0);
                emiTextView.setText("");
                emiTextView.setVisibility(View.GONE);
            }
        });
    }

    void calculateEMI(double amount,double roi){

        double emi=0;

        roi/=1200;

        emi=(amount*roi*Math.pow(1+roi,pos)/(Math.pow(1 + roi, pos) - 1));

        DecimalFormat df = new DecimalFormat(".###");

        emiTextView.setText("Rs. "+df.format(emi)+"\nper month");
        if (sound_flag!=2){
            textToSpeech.speak(("rupees"+df.format(emi)+"per month"),TextToSpeech.QUEUE_FLUSH,null);

        }
        emiTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Integer item = (Integer) adapterView.getItemAtPosition(i);

        pos=item;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
