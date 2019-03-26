package com.example.messagebird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import networkpacket.NetworkPacket;

public class Message extends Activity implements AdapterView.OnItemClickListener {
    private MsdBirdApplication app = null;
    Rcv rcv = null;
    private ListView listview;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        app = (MsdBirdApplication)getApplication();
        listview=findViewById(R.id.listview_messagelist_message);

        listview.setOnItemClickListener(this);

        data=new ArrayList<Map<String,Object>>();
        putData();
        simpleAdapter = new SimpleAdapter(Message.this, data,
                R.layout.layout_messagelist, new String[] { "icon", "usrid",
                "name", "msg" }, new int[] { R.id.ivIcon_messagelist, R.id.tvUsrID_messagelist, R.id.tvName_messagelist,
                R.id.tvMsg_messagelist });
        listview.setAdapter(simpleAdapter);

        rcv = new Rcv();
        IntentFilter rcvfilter = new IntentFilter("android.intent.action.Rcv");
        registerReceiver(rcv, rcvfilter);

    }




    private void putData()
    {
        /*
        data=new ArrayList<Map<String,Object>>();
        Map<String, Object> map1=new HashMap<String, Object>();
       // map1.put("icon", R.drawable.item1);
        map1.put("usrid", "3000");
        map1.put("name", "简爱");
        map1.put("msg", "风将绿了夜的途");
        Map<String, Object> map2=new HashMap<String, Object>();
       // map2.put("icon", R.drawable.item2);
        map2.put("name", " 陌 陌");
        map2.put("ss", "寻找你,你在我心中__。");
        Map<String, Object> map3=new HashMap<String, Object>();
        //map3.put("icon", R.drawable.item3);
        map3.put("name", "汐颜");
        map3.put("ss", "最新分享:中国合伙人正能量22句话...");
        Map<String, Object> map4=new HashMap<String, Object>();
       // map4.put("icon", R.drawable.item4);
        map4.put("name", "花仙子");
        map4.put("ss", " ");
        data.add(map1);
        data.add(map2);
        data.add(map3);
        data.add(map4);
        */
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
/*
        adapterView.getSelectedItemPosition();
        adapterView.getSelectedItemId();
        adapterView.getSelectedItem();




        Toast.makeText(this, "listview : onclick" + adapterView.getSelectedItemId(),
                Toast.LENGTH_SHORT).show();

        adapterView.getSelectedItem();



        Toast.makeText(this, "position->" + i + "\ntext->" ,Toast.LENGTH_LONG).show();

        TextView tv_name = (TextView) view.findViewById(R.id.tvName_messagelist);
        Toast.makeText(this, "position->" + i + "\ntext->" + tv_name.getText() ,Toast.LENGTH_LONG).show();
*/

        TextView tv_partnerusrid =  view.findViewById(R.id.tvUsrID_messagelist);
        Intent intent = new Intent(Message.this, MsgDatail.class);
        intent.putExtra("partnerusrid", tv_partnerusrid.getText());
        app.partnerusrid = Long.parseLong(tv_partnerusrid.getText().toString());
        startActivity(intent);


        /*
        Map<String,Object>map = new HashMap<String,Object>();
        //map.put("pic", R.drawable.ic_launcher);
        map.put("name", "汐颜");
        map.put("ss", "最新分享:中国合伙人正能量22句话...");
        data.add(map);
        simpleAdapter.notifyDataSetChanged();
*/
    }


    public class Rcv extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(Integer.parseInt(intent.getStringExtra("type")) == NetworkPacket.TYPE_NEWMSG){
                if(data.size() < 10){
                Map<String,Object>map = new HashMap<String,Object>();
                //map.put("pic", R.drawable.ic_launcher);
                map.put("usrid", Long.parseLong(intent.getStringExtra("parnerusrid")));
                //map.put("name", "简爱");
                map.put("msg", intent.getStringExtra("content"));
                data.add(map);
                simpleAdapter.notifyDataSetChanged();
            }else {
                    data.remove(0);
                    Map<String, Object> map = new HashMap<String, Object>();
                    //map.put("pic", R.drawable.ic_launcher);
                    map.put("usrid", Long.parseLong(intent.getStringExtra("parnerusrid")));
                    //map.put("name", "简爱");
                    map.put("msg", intent.getStringExtra("content"));
                    data.add(map);
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
