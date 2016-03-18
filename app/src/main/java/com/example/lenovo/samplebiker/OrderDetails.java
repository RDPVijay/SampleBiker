package com.example.lenovo.samplebiker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapView;
//import com.google.android.maps.Overlay;


public class OrderDetails extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Resource resources;
//    MapView mapView;

    private double currlat, currlong;
    private double deslat, deslong;

    private GoogleApiClient client;
  //  GeoPoint srcGeo, desGeo;
    //List<Overlay> mOverlays;

    LatLng source,desti;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);

        resources = new Resource(OrderDetails.this);
        final Intent intent = getIntent();

        String sou = intent.getStringExtra("Sour");
        TextView sou1 = (TextView) findViewById(R.id.sou);
        sou1.setText(sou);

        String des = intent.getStringExtra("Dest");
        TextView des1 = (TextView) findViewById(R.id.des);
        des1.setText(des);

        try {
            getLocationFromAddress(des);
        } catch (IOException e) {
            e.printStackTrace();
        }

      //  mapView = (MapView)findViewById(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        final Button comp = (Button) findViewById(R.id.comp);
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(OrderDetails.this).create();
                alertDialog.setMessage("Delivered Successfully???");
                alertDialog.setButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent.putExtra("VAL", true);
                        setResult(2, intent);
                        comp.setEnabled(false);
                        finish();
                    }
                });
                alertDialog.show();

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        currlat = Resource.getLatitude();
        currlong = Resource.getLongitude();
        source = new LatLng(currlat, currlong);
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(source, 5);
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.addMarker(new MarkerOptions().position(source).title("Your Location"));
        map.animateCamera(yourLocation);

        desti = new LatLng(deslat, deslong);
        map.addMarker(new MarkerOptions().position(desti).title("Destination"));

    }

    @Override
    public void onStart() {

        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.lenovo.samplebiker/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.lenovo.samplebiker/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void getLocationFromAddress(String desAdd) throws IOException {

        Geocoder geocoder = new Geocoder(this);
        List<Address> address;

        address = geocoder.getFromLocationName(desAdd, 5);
        if (address == null)
            return;
        deslat = Double.parseDouble(String.valueOf(address.get(0).getLatitude()));
        deslong = Double.parseDouble(String.valueOf(address.get(0).getLongitude()));


        /*Geocoder geocoder1 = new Geocoder(this, Locale.getDefault());
        address = geocoder1.getFromLocation(currlat,currlong,1);

        String addr = address.get(0).getAddressLine(0);
        String city = address.get(0).getLocality();
        TextView sou1 =(TextView)findViewById(R.id.sou);
        sou1.setText(addr + "," + city);*/

    }

}
