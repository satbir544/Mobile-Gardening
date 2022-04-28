package com.example.plantreapp.progressBar;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plantreapp.R;

public class MyProgressBar extends AppCompatActivity {
    private ProgressBar circular_pro;
    private Button click_me_btn;
    private TextView status;

    private int progressStatus;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        circular_pro = (ProgressBar)findViewById(R.id.progessbar_circular);
        click_me_btn = (Button)findViewById(R.id.progress_btn);
        status= (TextView)findViewById(R.id.text_status);

        click_me_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(progressStatus<100){
                            progressStatus+=1;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    circular_pro.setProgress(progressStatus);
                                    status.setText(progressStatus+"%");
                                }
                            });
                            try {
                                Thread.sleep(200);

                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            }
        });
    }
}