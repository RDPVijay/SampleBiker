package com.example.lenovo.samplebiker;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Lenovo on 2/26/2016.
 */
public class OrderList extends ListActivity implements ListAdapter.customButtonListener {


    private ListView listView;
    ListAdapter adapter;
    ArrayList<String> dataItems = new ArrayList<String>();
    ArrayList<String> dataItems2 = new ArrayList<String>();

    int p=-1;


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

        Toast.makeText(OrderList.this, "Button click " + value + value2, Toast.LENGTH_SHORT).show();

        p = position;
        Intent i = new Intent(this, OrderDetails.class);
        i.putExtra("Sour", value);
        i.putExtra("Dest", value2);
        startActivityForResult(i,2);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent i = data;
        Boolean val = i.getBooleanExtra("VAL",false);
        if (requestCode == 2){

            Toast.makeText(this,"Task Has been completed",Toast.LENGTH_LONG);
            //adapter.setChecked(val, p);
            //listView.setAdapter(adapter);


        }
    }

}
