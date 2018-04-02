package ro.pub.cs.systems.eim.practicaltest01var05;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PracticalTest01Var05SecondaryActivity extends AppCompatActivity {

    private static final String TAG = "PracticalTestSecond";
    
    private Button returnButton;
    private TextView calculateResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var05_secondary);

        returnButton = (Button) findViewById(R.id.returnButton);
        calculateResultTextView = (TextView) findViewById(R.id.calculateResultTextView);


        Intent intent = getIntent();
        String sumStr = intent.getStringExtra(Constants.SUM_EXTRA);
        String []items = sumStr.split("\\+");

        int sum = 0;
        for (String item : items) {
            sum += Integer.parseInt(item);
        }
        calculateResultTextView.setText(sumStr + " = " + sum);


        final int finalSum = sum;
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(finalSum);
                finish();
            }
        });


    }
}
