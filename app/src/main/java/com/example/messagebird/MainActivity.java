package com.example.messagebird;

import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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





                if(app.connectedtotheserver){
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }else{
                    new ConnectToTheServer().execute();
                }

            }
        });
        if(!app.connectedtotheserver){
            new ConnectToTheServer().execute();
        }

        if(app.logined) {
            tabhost = getTabHost();
            tabhost.setup();
            TabHost.TabSpec tabSpec1 = tabhost.newTabSpec("tab1").setIndicator("Contact").setContent(new Intent(MainActivity.this, Contact.class));
            TabHost.TabSpec tabSpec2 = tabhost.newTabSpec("tab2").setIndicator("Message").setContent(new Intent(MainActivity.this, Message.class));
            TabHost.TabSpec tabSpec3 = tabhost.newTabSpec("tab3").setIndicator("Setting").setContent(new Intent(MainActivity.this, Settting.class));

            tabhost.addTab(tabSpec1);
            tabhost.addTab(tabSpec2);
            tabhost.addTab(tabSpec3);


            Intent intent = new Intent(MainActivity.this, MsgService.class);
            startService(intent);


            login.setVisibility(View.INVISIBLE);
            infoText.setVisibility(View.INVISIBLE);
        }

        if(app.connectedtotheserver&&app.logined){
            login.setVisibility(View.INVISIBLE);
            infoText.setVisibility(View.INVISIBLE);
        }
    }

    protected void onDestroy() {

        stopService(new Intent(MainActivity.this, MsgService.class));

        super.onDestroy();

    }

    private class  ConnectToTheServer extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {

            try{
                app.socket = new Socket(app.host, app.port);
                infoText.setText(app.socket.toString());
                app.oos = new ObjectOutputStream(app.socket.getOutputStream());
                app.ois= new ObjectInputStream(app.socket.getInputStream());
                app.bufferedreader = new BufferedReader(new InputStreamReader(app.socket.getInputStream()));

                return true;



            }catch(Exception e){
                e.printStackTrace();
                return false;
            }


        }
        @Override
        protected void onPostExecute(Boolean b) {
            if(b.booleanValue()){
                app.connectedtotheserver = true;

                infoText.setText("Welcome.");
                login.setText("Login");

            }else{
                app.connectedtotheserver = false;
                login.setText("Retry");
                infoText.setText("unable to access the server, try again in the later.");
            }
        }

    }





}
