package com.example.lenovo.samplebiker;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


public class Tab extends TabActivity{

private static final String ORDER_SPEC = "order";
private static final String HISTORY_SPEC = "history";
private static final String PROFILE_SPEC = "profile";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            TabHost tabHost = getTabHost();

            TabHost.TabSpec orderSpec = tabHost.newTabSpec(ORDER_SPEC);

            orderSpec.setIndicator(ORDER_SPEC);
            Intent orderIntent = new Intent(this, OrderList.class);
            orderSpec.setContent(orderIntent);

            tabHost.addTab(orderSpec);

            TabHost.TabSpec historySpec = tabHost.newTabSpec(HISTORY_SPEC);

            historySpec.setIndicator(HISTORY_SPEC);
            Intent historyIntent = new Intent(this, History.class);
            historySpec.setContent(historyIntent);

            tabHost.addTab(historySpec);

            TabHost.TabSpec profileSpec = tabHost.newTabSpec(PROFILE_SPEC);

            profileSpec.setIndicator(PROFILE_SPEC);
            Intent profileIntent = new Intent(this, Profile_view.class);
            profileSpec.setContent(profileIntent);

            tabHost.addTab(profileSpec);

            tabHost.setCurrentTab(2);

        }
}