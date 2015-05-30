package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MZmey on 30.05.2015.
 */
public class EventOld extends FragmentActivity {
    private static final String URI = "uri";
    private static final String MAKE_JOIN = "makeJoin";
    private static final String CHECK_IN = "checkIn";
    private static final String DEL = "/";
    private static final String PHOTO = "photo";
    private static final String ADDR = "addr";
    private static final String EVENT_NAME = "eventName";
    private static final String FNAME = "fName";
    private static final char PLUS = '+';
    private static final String LNAME = "lName";
    private static final String DATE = "date";
    private static final String IN = "in";
    private static String URI_ADD = "/event";
    private final String LOGIN = "login";
    private int param = LinearLayout.LayoutParams.MATCH_PARENT;
    private RequestQueue queue;
    private TextView tvName;
    private Button checkOrJoin;
    private String uri;
    private String LOG = "my con";
    private TextView tvOut;
    private TextView tvAddr;
    private TextView tvDate;
    private String stPath;
    private boolean left = true;
    private String login;
    private LinearLayout leftL;
    private LinearLayout rightL;
    private String name;
    private ScrollView scUsers;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private LatLng latLng;
    private Marker marker;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_checked_in);
        name = getIntent().getStringExtra(EVENT_NAME);
        login = getIntent().getStringExtra(LOGIN);
        stPath = getIntent().getStringExtra(URI);
        tvOut = (TextView) findViewById(R.id.tvOut);
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddr = (TextView) findViewById(R.id.tvAddr);
        tvDate = (TextView) findViewById(R.id.tvDate);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = mapFragment.getMap();
        if (map == null){
            finish();
            return;
        }

        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();
        uri = stPath + URI_ADD + login;
        StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> params = Mapper.queryToMap(response);
                tvDate.setText(params.get(DATE));
                tvName.setText(params.get(noPros(EVENT_NAME)));
                tvAddr.setText(params.get(ADDR));

                String[] coordinates = params.get("coordinates").split(" ");
                for (String coord : coordinates) {
                    String[] c = coord.split("-");
                    if (!c.equals("")) {
                        double latitude = Double.parseDouble(c[0]);
                        double longitude = Double.parseDouble(c[1]);
                        makeOtherPoint(latitude, longitude);
                    }
                }
                double longitude = Double.parseDouble(params.get("myHeight"));
                double latitude = Double.parseDouble(params.get("myWidth"));
                makeMyPoint(latitude, longitude);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvName.setText("Connecction problem, check your network");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put(EVENT_NAME, name);
                params.put(LOGIN, login);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(sr);

    }

    public void makeOtherPoint(double latitude, double longitude) {
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    public void makeMyPoint(double latitude, double longitude) {
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == PLUS)
                c[i] = ' ';
        return new String(c);
    }
}
