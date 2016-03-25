package com.example.lenovo.samplebiker;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class OrderList extends ListActivity implements ListAdapter.customButtonListener {


    private ListView listView;
    ListAdapter adapter;
    ArrayList<String> dataItems = new ArrayList<String>();
    ArrayList<String> dataItems2 = new ArrayList<String>();



    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.oder_list);


        String[] Source = getResources().getStringArray(R.array.Source);
        List<String> dataTemp = Arrays.asList(Source);
        dataItems.addAll(dataTemp);

        String[] Dest = getResources().getStringArray(R.array.Dest);
        List<String> dataTemp2 = Arrays.asList(Dest);
        dataItems2.addAll(dataTemp2);

        listView = (ListView)findViewById(android.R.id.list);
        adapter = new ListAdapter(OrderList.this, dataItems,dataItems2);
        adapter.setCustomButtonListener(OrderList.this);
        listView.setAdapter(adapter);

    }

    @Override
    public void onButtonClickListener(int position, String value, String value2) {

        Toast.makeText(OrderList.this, "Map to " + value2, Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, OrderDetails.class);
        i.putExtra("Sour", value);
        i.putExtra("Dest", value2);
        startActivity(i);

    }

}
