package ro.pub.cs.systems.eim.practicaltest01var05;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest01Var05MainActivity extends AppCompatActivity {

    private static final String TAG = "PracticalTestMain";
    
    private Button computeButton;
    private Button addNumberButton;
    private TextView resultTextView;
    private EditText addNumberEditText;

    private int sum = -1;
    private boolean modified;

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var05_main);

        computeButton = (Button) findViewById(R.id.computeButton);
        addNumberButton = (Button) findViewById(R.id.addNumberButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        addNumberEditText = (EditText) findViewById(R.id.addNumberEditText);

        if (savedInstanceState != null) {
            restore(savedInstanceState);
        }

        addNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String number = addNumberEditText.getText().toString();

                if (!number.equals("")) {

                    String result = resultTextView.getText().toString();

                    if (result.equals("")) {
                        resultTextView.setText(number);
                    } else {

                        resultTextView.setText(result + "+" + number);
                    }
                    addNumberEditText.setText("");

                    modified = true;
                }
            }
        });

        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (modified) {
                    Intent intent = new Intent(PracticalTest01Var05MainActivity.this, PracticalTest01Var05SecondaryActivity.class);

                    intent.putExtra(Constants.SUM_EXTRA, resultTextView.getText().toString());
                    startActivityForResult(intent, Constants.SECOND_ACTIVITY_REQUEST_CODE);
                    modified = false;
                } else {
                    String message = "Sum = ";
                    Toast.makeText(PracticalTest01Var05MainActivity.this, message + sum, Toast.LENGTH_SHORT).show();
                }

                checkExceeded();
            }
        });


        // setup broadcast receiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Received: " + intent.getStringExtra(Constants.BROADCAST_EXTRA));

                Toast.makeText(PracticalTest01Var05MainActivity.this,
                        intent.getStringExtra(Constants.BROADCAST_EXTRA), Toast.LENGTH_LONG).show();
            }
        };

        // setup intent filter
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION1);
    }

    private void checkExceeded() {
        Log.d(TAG, "onClick: Current sum = " + sum);
        if (sum >= Constants.MAX_RANGE && PracticalTest01Var05Service.status == Constants.INACTIVE) {
            Log.d(TAG, "onClick: Sum exceeded");

            Intent intent = new Intent(PracticalTest01Var05MainActivity.this, PracticalTest01Var05Service.class);

            intent.putExtra(Constants.SUM_EXTRA, String.valueOf(sum));
            startService(intent);

            Toast.makeText(PracticalTest01Var05MainActivity.this, "Service started", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.SECOND_ACTIVITY_REQUEST_CODE) {
            sum = resultCode;

            String message = "Sum = ";
            Toast.makeText(this, message + resultCode, Toast.LENGTH_SHORT).show();


            checkExceeded();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        outState.putString(Constants.EDIT_TEXT_VAL, addNumberEditText.getText().toString());
        outState.putString(Constants.TEXT_VIEW_VAL, resultTextView.getText().toString());
        outState.putString(Constants.SUM_VAL, String.valueOf(sum));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        restore(savedInstanceState);
    }

    private void restore(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(Constants.EDIT_TEXT_VAL)) {
            addNumberEditText.setText(savedInstanceState.getString(Constants.EDIT_TEXT_VAL));
        } else {
            addNumberEditText.setText("");
        }

        if (savedInstanceState.containsKey(Constants.TEXT_VIEW_VAL)) {
            resultTextView.setText(savedInstanceState.getString(Constants.TEXT_VIEW_VAL));
        } else {
            resultTextView.setText("");
        }

        if (savedInstanceState.containsKey(Constants.SUM_VAL)) {
            sum = Integer.parseInt(savedInstanceState.getString(Constants.SUM_VAL));
        } else {
            sum = -1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        super.onPause();
    }


    @Override
    protected void onDestroy() {

        Log.d(TAG, "onDestroy: Stop service");
        Intent intent = new Intent(PracticalTest01Var05MainActivity.this, PracticalTest01Var05Service.class);
        stopService(intent);

        super.onDestroy();
    }
}
