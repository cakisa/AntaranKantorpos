package com.horirevens.antarankantorpos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

public class DeliveryOrderActivity extends AppCompatActivity {
    private static final String MY_LOG = "log_DeliveryOrder";

    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(MY_LOG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order);

        listview = (ListView) findViewById(R.id.listView);
    }
}
