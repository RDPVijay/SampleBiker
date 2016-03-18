package com.example.lenovo.samplebiker;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewHolder{

    TextView sou,des;
    CheckBox check;
    Button butt;
    Boolean value = false;
    enum ststus {

        COMPLETE,
        INCOMPLETE,
        INPROGRESS

    }

}
