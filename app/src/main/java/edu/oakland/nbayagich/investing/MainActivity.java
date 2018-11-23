package edu.oakland.nbayagich.investing;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button stopGame, startGame, showGrowth = null;
    TextView accountNumber = null;

    float newAccountNumValue = 0;
    int seconds = 0;
    HashMap<Integer, String> plotValues = new HashMap<Integer, String>();

    InvestingThread thread = null;
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopGame = findViewById(R.id.stopGameButton);
        startGame = findViewById(R.id.startGameButton);
        showGrowth = findViewById(R.id.showGrowthButton);
        accountNumber = findViewById(R.id.accountAmount);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                plotValues = (HashMap<Integer, String>) msg.getData().getSerializable("Data");
            }
        };


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread = new InvestingThread(accountNumber.getText().toString().substring(1, accountNumber.length()), handler);
                thread.start();
                startGame.setEnabled(false);
                showGrowth.setEnabled(true);
                stopGame.setEnabled(true);

                handler.postDelayed(new Runnable(){
                    public void run(){
                        if (thread != null)
                        {
                            seconds++;
                            //Calculate the GOOD and BAD strategies
                            float randomGoodStrategyNumber = new Random().nextFloat();
                            float randomBadStrategyNumber = new Random().nextFloat()* -1;
                            float goodStrategy = (100 * randomGoodStrategyNumber);
                            float badStrategy = (100 * randomBadStrategyNumber);

                            // New account number using the calculations above
                            newAccountNumValue = Float.valueOf(accountNumber.getText().toString().substring(1, accountNumber.length())) + goodStrategy + badStrategy;

                            // Updates the account value on screen
                            if(newAccountNumValue <= 0)
                                accountNumber.setText("$" + "0");
                            else
                                accountNumber.setText("$" + String.format("%.2f",newAccountNumValue));

                            // New account number reaches 0, stop game and plot it
                            if(newAccountNumValue <= 0)
                            {
                                plotValues.put(seconds, String.valueOf(0));
                                SendMessagetoQueue(plotValues);
                                return;
                            }

                            // For every 10 seconds, store value for plotting
                            if(seconds % 10 == 0)
                            {
                                plotValues.put(seconds, String.valueOf(newAccountNumValue));
                            }


                            // Runs code every second
                            handler.postDelayed(this, 1000);
                        }
                    }
                }, 1000);

            }
        });




        stopGame.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread = null;
                startGame.setEnabled(true);
                showGrowth.setEnabled(false);
                stopGame.setEnabled(false);
                ResetValuesToInitialState();
                return;
            }
        }));




        showGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Plot the value whenever user decides to hit Show Growth button
                if(newAccountNumValue <= 0)
                    plotValues.put(seconds, String.valueOf(0));
                else
                    plotValues.put(seconds, String.valueOf(newAccountNumValue));

                // Create Intent
                Intent intent = new Intent(v.getContext(), WebViewActivity.class);
                // Pass in the data values to plot
                intent.putExtra("PlotValues", plotValues);
                //Start activity with the WebView
                startActivityForResult(intent, 1);
            }
        });

    }


    private void SendMessagetoQueue(HashMap<Integer, String> plotValues)
    {
        Message msg = handler.obtainMessage();
        Bundle bundle = msg.getData();
        bundle.putSerializable("Data", plotValues);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    private void ResetValuesToInitialState()
    {
        accountNumber.setText("$100");
        newAccountNumValue = 0;
        seconds = 0;
    }
}
