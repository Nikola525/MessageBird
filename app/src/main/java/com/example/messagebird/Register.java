package com.example.messagebird;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import networkpacket.NetworkPacket;

public class Register extends Activity {
    private MsdBirdApplication app = null;
    private EditText name = null;
    private EditText email = null;
    private EditText password = null;
    private Button regiter = null;
    private Button tologin = null;
    boolean success = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        app = (MsdBirdApplication)getApplication();
        name = findViewById(R.id.edtname_register);
        email = findViewById(R.id.edtEamil_register);
        password = findViewById(R.id.edtPassword_register);
        regiter = findViewById(R.id.btnRegister_register);
        regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                Intent intent = new Intent(Login.this,
                        Register.class);
                startActivity(intent);
                */
                NetworkPacket sndpkt = new NetworkPacket();
                sndpkt.type = NetworkPacket.TYPE_REGISTER;
                sndpkt.email = email.getText().toString();
                sndpkt.password = password.getText().toString();
                sndpkt.name = name.getText().toString();
                //new LoginInBackGround().execute(sndpkt);
                new RegisterInBackground().execute(sndpkt);

            }
        });

        tologin = findViewById(R.id.btnLinkToLoginScreen_regiter);
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Register.this,
                        Login.class);
                startActivity(intent);


            }
        });
    }

    private class  RegisterInBackground extends AsyncTask<NetworkPacket, Void, Long> {
        @Override
        protected Long doInBackground(NetworkPacket... packets) {

            try{


                app.oos.writeObject(packets[0]);
                NetworkPacket rcvpkt = (NetworkPacket) app.ois.readObject();

                success = true;
                return new Long(rcvpkt.to);



            }catch(Exception e){
                e.printStackTrace();
                success = false;
                return new Long(0);
            }


        }
        @Override
        protected void onPostExecute(Long l) {
            if(success){
                app.registeredusrid = l.longValue();
                Toast.makeText(getApplicationContext(),"Your usrid is "+ l.toString() + "\nDon't forger it.", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(Login.this,MainActivity.class);
                //startActivity(intent);


            }else{
                Toast.makeText(getApplicationContext(), "Sorry, there are some problems. \n Please try again.", Toast.LENGTH_LONG).show();
            }
        }

    }
}
