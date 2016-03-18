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

/*        connectAsyncTask _connectAsyncTask = new connectAsyncTask();
        _connectAsyncTask.execute();
        mapView.setBuiltInZoomControls(true);
        mapView.displayZoomControls(true);
        mOverlays = mapView.getOverlays();
        mapView.getController().animateTo(srcGeo);
        mapView.getController().setZoom(5);

    }

    private class connectAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(OrderDetails.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            fetchData();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(doc!=null){
                Overlay ol = new MyOverLay(OrderDetails.this,srcGeo,srcGeo,1);
                mOverlays.add(ol);
                NodeList _nodelist = doc.getElementsByTagName("status");
                Node node1 = _nodelist.item(0);
                String _status1  = node1.getChildNodes().item(0).getNodeValue();
                if(_status1.equalsIgnoreCase("OK")){
                    NodeList _nodelist_path = doc.getElementsByTagName("overview_polyline");
                    Node node_path = _nodelist_path.item(0);
                    Element _status_path = (Element)node_path;
                    NodeList _nodelist_destination_path = _status_path.getElementsByTagName("points");
                    Node _nodelist_dest = _nodelist_destination_path.item(0);
                    String _path  = _nodelist_dest.getChildNodes().item(0).getNodeValue();
                    List<GeoPoint> _geopoints = decodePoly(_path);
                    GeoPoint gp1;
                    GeoPoint gp2;
                    gp2 = _geopoints.get(0);
                    Log.d("_geopoints","::"+_geopoints.size());
                    for(int i=1;i<_geopoints.size();i++) // the last one would be crash
                    {

                        gp1 = gp2;
                        gp2 = _geopoints.get(i);
                        Overlay ol1 = new MyOverLay(gp1,gp2,2, Color.GREEN);
                        mOverlays.add(ol1);
                    }
                    Overlay ol2 = new MyOverLay(OrderDetails.this,desGeo,desGeo,3);
                    mOverlays.add(ol2);

                    progressDialog.dismiss();
                }else{
                    showAlert("Unable to find the route");
                }

                Overlay ol2 = new MyOverLay(OrderDetails.this,desGeo,desGeo,3);
                mOverlays.add(ol2);
                progressDialog.dismiss();
                mapView.scrollBy(-1,-1);
                mapView.scrollBy(1,1);
            }else{
                showAlert("Unable to find the route");
            }

        }

    }
    Document doc = null;
    private void fetchData()
    {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps/api/directions/xml?origin=");
        urlString.append(Double.toString((double) srcGeo.getLatitudeE6() / 1.0E6));
        urlString.append(",");
        urlString.append( Double.toString((double)srcGeo.getLongitudeE6()/1.0E6 ));
        urlString.append("&destination=");//to
        urlString.append( Double.toString((double)desGeo.getLatitudeE6()/1.0E6 ));
        urlString.append(",");
        urlString.append( Double.toString((double)desGeo.getLongitudeE6()/1.0E6 ));
        urlString.append("&sensor=true&mode=driving");
        Log.d("url", "::" + urlString.toString());
        HttpURLConnection urlConnection= null;
        URL url = null;
        try
        {
            url = new URL(urlString.toString());
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = (Document) db.parse(urlConnection.getInputStream());//Util.XMLfromString(response);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private List<GeoPoint> decodePoly(String encoded) {

        List<GeoPoint> poly = new ArrayList<GeoPoint>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
                    (int) (((double) lng / 1E5) * 1E6));
            poly.add(p);
        }

        return poly;
    }
    private void showAlert(String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(OrderDetails.this);
        alert.setTitle("Error");
        alert.setCancelable(false);
        alert.setMessage(message);
        alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        alert.show();
    }

*/
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
