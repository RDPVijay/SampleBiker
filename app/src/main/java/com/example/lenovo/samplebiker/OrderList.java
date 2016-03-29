package com.example.lenovo.samplebiker;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class OrderList extends ListActivity {

    private ProgressDialog progress;
    ArrayList<HashMap<String, String>> orderlist;
    JSONArray order = null;


    private static final String TAG_MESSAGES = "messages";
    private static final String TAG_ID = "id";
    private static final String TAG_FROM = "from";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_SUBJECT = "subject";
    private static final String TAG_DATE = "date";


    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.oder_list);

        orderlist = new ArrayList<HashMap<String, String>>();

        new LoadInbox().execute();

    }


    private class LoadInbox extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(OrderList.this);
            progress.setMessage("Loading Orders ...");
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {


            JSONObject json = null;
            try {
                json = new JSONObject(loadJSONFromAsset());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("Inbox JSON: ", json.toString());

            try {
                order = json.getJSONArray(TAG_MESSAGES);
                // looping through All messages
                for (int i = 0; i < order.length(); i++) {
                    JSONObject c = order.getJSONObject(i);

                    String id = c.getString(TAG_ID);
                    String from = c.getString(TAG_FROM);
                    String subject = c.getString(TAG_SUBJECT);
                    String date = c.getString(TAG_DATE);

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_ID, id);
                    map.put(TAG_FROM, from);
                    map.put(TAG_SUBJECT, subject);
                    map.put(TAG_DATE, date);

                    orderlist.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String loadJSONFromAsset() {
            String json = null;
            try {
                InputStream is = getAssets().open("Order.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }

        protected void onPostExecute(String file_url) {
            progress.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            OrderList.this, orderlist,
                            R.layout.order_list_item, new String[] { TAG_FROM, TAG_SUBJECT, TAG_ID },
                            new int[] { R.id.Dest, R.id.Source, R.id.checkBox });
                    setListAdapter(adapter);
                }
            });

        }

    }
}
