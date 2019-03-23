package com.example.messagebird;

import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import networkpacket.NetworkPacket;

public class MainActivity extends TabActivity {
    private MsdBirdApplication app = null;
    TabHost tabhost = null;
    private Button login = null;
    private TextView infoText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoText = findViewById(R.id.tvInfo_main);
        app = (MsdBirdApplication)getApplication();
        login = findViewById(R.id.btnLogin_main);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabhost = getTabHost();
                tabhost.setup();

                TabHost.TabSpec tabSpec1 = tabhost.newTabSpec("tab1").setIndicator("Contact").setContent(new Intent(MainActivity.this, Contact.class));
                TabHost.TabSpec tabSpec2 = tabhost.newTabSpec("tab2").setIndicator("Message").setContent(new Intent(MainActivity.this, Message.class));
                TabHost.TabSpec tabSpec3 = tabhost.newTabSpec("tab3").setIndicator("Setting").setContent(new Intent(MainActivity.this, Settting.class));
                TabHost.TabSpec tabSpec4 = tabhost.newTabSpec("tab4").setIndicator("MsgDetail").setContent(new Intent(MainActivity.this, MsgDatail.class));



                tabhost.addTab(tabSpec1);
                tabhost.addTab(tabSpec2);
                tabhost.addTab(tabSpec3);
                tabhost.addTab(tabSpec4);


                /*
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);*/
            }
        });
        new ConnectToTheServer().execute();


    }

    private class  ConnectToTheServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            try{
                app.socket = new Socket(app.host, app.port);
                infoText.setText(app.socket.toString());
                app.oos = new ObjectOutputStream(app.socket.getOutputStream());
                app.ois= new ObjectInputStream(app.socket.getInputStream());

                return new String("Success.");



            }catch(Exception e){
                e.printStackTrace();
                return new String("Failed.");
            }


        }
        @Override
        protected void onPostExecute(String content) {
            if(true){
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(Login.this,MainActivity.class);
                //startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "id or password is incorrect.", Toast.LENGTH_LONG).show();
            }
        }

    }


}
