package com.example.messagebird;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message extends Activity {
    private ListView listview;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        listview=(ListView)findViewById(R.id.listview_messagelist_message);

        putData();
        //这里使用当前的布局资源作为ListView的模板。
        //使用这种方式，SimpleAdapter会忽略ListView控件，仅以ListView之外的控件作为模板。
        simpleAdapter = new SimpleAdapter(Message.this, data,
                R.layout.layout_messagelist, new String[] { "icon",
                "name", "ss" }, new int[] { R.id.ivIcon, R.id.tvName,
                R.id.tvSS });
        listview.setAdapter(simpleAdapter);


    }




    private void putData()
    {
        data=new ArrayList<Map<String,Object>>();
        Map<String, Object> map1=new HashMap<String, Object>();
       // map1.put("icon", R.drawable.item1);
        map1.put("name", "简爱");
        map1.put("ss", "风将绿了夜的途");
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
    }


    public class Rcv extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(getApplicationContext(), intent.getStringExtra("message"), Toast.LENGTH_LONG).show();
        }
    }
}
