package com.example.mvp_aidl.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mvp_aidl.R;
import com.example.mvp_aidl.model.SingleClass;
import com.example.mvp_aidl.presenter.MainPresenter;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements IMainView{

    private TextView tvSum;
    private TextView tvMessage;
    private MainPresenter mMainPresenter = new MainPresenter(this);
    SingleClass singleClass;

    boolean threadRun = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singleClass = SingleClass.getInstance();

        Toast.makeText(getApplicationContext(), "INSTANCE MainActivity: " + singleClass.hashCode(), Toast.LENGTH_SHORT).show();

        EditText etFirst = findViewById(R.id.et_first);
        EditText etSecond = findViewById(R.id.et_second);
        tvSum = findViewById(R.id.tv_sum);
        tvMessage = findViewById(R.id.tv_message);
        Button btSum = findViewById(R.id.bt_sum);
        Button btDiff = findViewById(R.id.bt_diff);
        Button btBindService = findViewById(R.id.bt_bindService);

        btSum.setOnClickListener(v -> {
            showSum(10);
          /*mMainPresenter.calculateSum(Integer.parseInt(etFirst.getText().toString()) ,
                  Integer.parseInt(etSecond.getText().toString()));*/
        });

        btDiff.setOnClickListener(v -> {
            mMainPresenter.calculateDiff(Integer.parseInt(etFirst.getText().toString()) ,
                    Integer.parseInt(etSecond.getText().toString()));
        });

        btBindService.setOnClickListener(v -> {
            mMainPresenter.bindToService(getApplicationContext());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        singleClass = null;
        Toast.makeText(getApplicationContext(), "INSTANCE singleClass: null", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSum(int result) {
      /*  HashMap<Integer, String> map = new HashMap<>();
        map.put(1,"10");
        map.put(2,"11");
        getVal(map);
        map.put(5,"14");
        map.put(6,"15");
get process id from thread id android
        StringBuilder str = new StringBuilder();
        for(Map.Entry<Integer, String> entry: map.entrySet()) {
            str.append(entry.getValue());
        }

      tvSum.setText(str.toString());*/
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();

                if (!threadRun) {
                    threadRun = true;
                    System.out.println("SAN Inside thread" + Thread.currentThread().getId() + ",alive: " + isAlive() + ",state: " + getState() + ",threadRun: " + threadRun);
                    loadItems();
                } else {
                    System.out.println("SAN Interrpt thread" + Thread.currentThread().getId());
                    Thread.currentThread().interrupt();
                    System.out.println("SAN Interrpt thread" + Thread.currentThread().getId() + "active thread: "+ Thread.activeCount() + ",threadRun: " + threadRun);
                }


            }
        };
        t.start();
    //    System.out.println("SAN Outside thread" + t.currentThread().getId() + ",alive: " + t.isAlive() + ",state: " + t.getState() + ",threadRun: " + threadRun);
        //loadItems();
    }

    private HashMap<Integer, String> getVal(HashMap<Integer, String> map) {
        map.put(3,"12");
        map.put(4,"13");
        return map;
    }

    @Override
    public void notifyToHmi(int data) {
        String subtract = "Difference is: " + data;
        tvMessage.setText(subtract);
    }


    private void loadItems() {
        Handler handler;
       if(isOnUiThread()) {
           handler = new Handler();
       } else {
           handler = new Handler(Looper.getMainLooper());
       }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Inside handler" + Thread.currentThread().getId());
                    threadRun = false;
                    updateView();
                }
            }, 1000);

    }

    private void updateView() {
        tvSum.setText("View UPDATED!!!!!!");
    }

    private boolean isOnUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
