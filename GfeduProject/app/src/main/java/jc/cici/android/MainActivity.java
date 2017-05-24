package jc.cici.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.login.NormalActivity;
import jc.cici.android.atom.ui.study.CheckLoginActivity;
import jc.cici.android.atom.ui.study.StudyHomeActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private int s_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        s_ID = sharedPreferences.getInt("S_ID",0);
        findViewById(R.id.ensureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s_ID == 0){
                    Intent it = new Intent(MainActivity.this, CheckLoginActivity.class);
                    startActivity(it);
                }else{
                    Intent it = new Intent(MainActivity.this, StudyHomeActivity.class);
                    startActivity(it);
                }

            }
        });
    }
}
