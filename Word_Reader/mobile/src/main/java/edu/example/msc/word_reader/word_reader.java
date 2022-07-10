package edu.example.msc.word_reader;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class word_reader extends AppCompatActivity {




    public Button bt;
    public Button bt2;
    public Button bt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_word_reader);

        bt= (Button) findViewById(R.id.button3);
        bt2=(Button) findViewById(R.id.button4);
        bt3=(Button) findViewById(R.id.button2);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActvity();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActvity2();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActvity3();
            }
        });


    }
    public void openActvity()
    {
    Intent intent=new Intent(this, HelpActivity.class);
    startActivity(intent);
    }

    public void openActvity2()
    {
        Intent intent=new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void openActvity3()
    {
        Intent intent=new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);


    }


}
