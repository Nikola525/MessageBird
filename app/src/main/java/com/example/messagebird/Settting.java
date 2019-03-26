package com.example.messagebird;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settting extends Activity {
    Button logout = null;
    private MsdBirdApplication app = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        app = (MsdBirdApplication)getApplication();
        logout = findViewById(R.id.btnLogout_setting);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.logined = false;
                app.connectedtotheserver = false;

                Intent intent = new Intent(Settting.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
