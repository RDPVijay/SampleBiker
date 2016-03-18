package com.example.lenovo.samplebiker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lenovo on 3/4/2016.
 */
public class ListAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<String> data1 = new ArrayList<String>();
    boolean val = false;
    int p = -2;

    ViewHolder viewHolder = new ViewHolder();



    customButtonListener customListener;
    public interface customButtonListener{

        void onButtonClickListener(int position, String value, String value2);

    }

    public void setCustomButtonListener(customButtonListener listener) {

        this.customListener = listener;

    }

    /*public void setChecked(boolean val, int p){

        this.val = val;
        this.p = p;
/*
        LayoutInflater in = LayoutInflater.from(context);
        View c = in.inflate(R.layout.order_list_item, null);
        CheckBox ch = (CheckBox)c.findViewById(R.id.checkBox);
        ch.setChecked(val);
    }
*/
    public ListAdapter(Context context1, ArrayList<String> dataitem1, ArrayList<String> dataitem2){

        super(context1, R.layout.order_list_item, dataitem1);

        this.data = dataitem1;
        this.data1 = dataitem2;
        this.context = context1;


    }

    public View getView(final int position, View convertView, ViewGroup parent){

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.order_list_item, null);



            viewHolder.sou = (TextView)convertView.findViewById(R.id.Source);
            viewHolder.des = (TextView)convertView.findViewById(R.id.Dest);
            viewHolder.check = (CheckBox)convertView.findViewById(R.id.checkBox);
            viewHolder.butt = (Button)convertView.findViewById(R.id.nextButton);

            convertView.setTag(viewHolder);


        }

        else {

            viewHolder = (ViewHolder)convertView.getTag();

        }

        final String temp = data.get(position);

        viewHolder.sou.setText(temp);

        final String temp1 = data1.get(position);
        viewHolder.des.setText(temp1);

        viewHolder.check.setText("1000" + position);

        /*if (p == position ){

            viewHolder.value =val;
            viewHolder.check.setChecked(viewHolder.value);

        }*/
        viewHolder.butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListener != null)
                    customListener.onButtonClickListener(position,temp,temp1);
            }
        });

        return convertView;

    }

}

