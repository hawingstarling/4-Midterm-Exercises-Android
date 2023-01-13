package com.example.seminar01;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar sb = null;
    TextView linear1T1;
    TextView linear1T2;
    TextView linear2T1;
    TextView linear2T2;
    TextView linear2T3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sb = (SeekBar) findViewById(R.id.slider);
        sb.setMax(100);

        linear1T1 = (TextView) findViewById(R.id.linear1T1);
        linear1T2 = (TextView) findViewById(R.id.linear1T2);
        linear2T1 = (TextView) findViewById(R.id.linear2T1);
        linear2T2 = (TextView) findViewById(R.id.linear2T2);
        linear2T3 = (TextView) findViewById(R.id.linear2T3);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progChange = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int[] redArray = {255, 0, 0};
                int[] blueArray = {0, 0, 255};
                int[] yellowArray = {255, 255, 0};

                progChange = i;

                redArray[0] = redArray[0] - (255/100)*progChange;
                redArray[1] = redArray[1] + (229/100)*progChange;
                redArray[2] = redArray[2] + (238/100)*progChange;
                blueArray[0] = blueArray[0] + (255/100)*progChange;
                blueArray[1] = blueArray[1] + (102/100)*progChange;
                blueArray[2] = blueArray[2] - (255/100)*progChange;
                yellowArray[0] = yellowArray[0] - (125/100)*progChange;
                yellowArray[1] = yellowArray[1] - (255/100)*progChange;
                yellowArray[2] = yellowArray[2] + (130/100)*progChange;

                linear1T1.setBackgroundColor(Color.rgb(redArray[0], redArray[1], redArray[2]));
                linear1T2.setBackgroundColor(Color.rgb(redArray[0], redArray[1], redArray[2]));
                linear2T1.setBackgroundColor(Color.rgb(yellowArray[0], yellowArray[1], yellowArray[2]));
                linear2T2.setBackgroundColor(Color.rgb(yellowArray[0], yellowArray[1], yellowArray[2]));
                linear2T3.setBackgroundColor(Color.rgb(yellowArray[0], yellowArray[1], yellowArray[2]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}