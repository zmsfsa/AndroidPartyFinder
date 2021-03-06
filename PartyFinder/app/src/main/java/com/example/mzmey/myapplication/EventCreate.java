package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MZmey on 22.05.2015.
 */
public class EventCreate extends FragmentActivity {

    private static final String URI = "uri";
    private String uri;
    private static final String LOGIN = "login";
    private static final String EVENT_DATE = "eventDate";
    private static final String URI_ADD = "/event/create";
    private static final String ADDRES = "addres";
    private static final String EVENT_NAME = "eventName";
    private static final String DATE_DELIMETR = "-";
    private RequestQueue queue;
    private StringRequest sr;
    private TextView tvOut;
    private EditText edEvent;
    private String login;
    private String stPath;
    private EditText edDate;
    private EditText edAddress;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private String width = null;
    private String height = null;
    private String LOG = "my con";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);

        Intent intent = getIntent();
        stPath = intent.getStringExtra(URI);
        login = intent.getStringExtra(LOGIN);
        uri = stPath + URI_ADD + login;
        edAddress = (EditText) findViewById(R.id.edAddress);
        tvOut = (TextView) findViewById(R.id.tvOut);
        edEvent = (EditText) findViewById(R.id.edEvent);
        edDate = (EditText) findViewById(R.id.edDate);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.eventMap);
        map = mapFragment.getMap();
        if (map == null) {
            finish();
            return;
        }

        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(
                        latLng.latitude, latLng.longitude)));
            }
        });
    }

    public void onClick(View v) {

        String[] dates = edDate.getText().toString().split(DATE_DELIMETR);

        if ((dates.length != 3) || (Integer.parseInt(dates[0]) < 1) ||
                (Integer.parseInt(dates[0]) > 31) || (Integer.parseInt(dates[1]) < 0)
                || ((Integer.parseInt(dates[1]) > 12))) {
            Toast.makeText(this, getString(R.string.date_format), Toast.LENGTH_SHORT).show();
        } else {
            if ((hasE(edAddress.getText().toString())) || (hasE(edEvent.getText().toString())))
                Toast.makeText(this, getString(R.string.enter_no_rus), Toast.LENGTH_SHORT).show();
            else {

                sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("OK")) {
                            onGoOn();
                        } else {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.connection), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put(LOGIN, login);
                        params.put(EVENT_NAME, edEvent.getText().toString());
                        params.put(EVENT_DATE, edDate.getText().toString());
                        params.put(ADDRES, edAddress.getText().toString());
                        if ((height != null) && (width != null)) {
                            params.put("height", height);
                            params.put("width", width);
                        } else {
                            params.put("height", "0");
                            params.put("width", "0");
                        }

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
        }
    }

    public void onGoOn() {

        Intent intent = new Intent(this, EventPage.class);
        intent.putExtra(LOGIN, login);
        intent.putExtra(EVENT_NAME, noPros(edEvent.getText().toString()));
        intent.putExtra(URI, stPath);
        startActivity(intent);
    }

    private String noPros(String in) {
        char[] c = in.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == '+')
                c[i] = ' ';
        return new String(c);
    }

    private static Pattern pattern0_9__a_z__A_Z = Pattern.compile(
            "[\\w\\u005F\\u002E]+", Pattern.UNICODE_CASE);

    public static boolean hasE(String str) {
        String[] a = str.split(" ");
        boolean res = false;
        for (String b : a) {
            Matcher m = pattern0_9__a_z__A_Z.matcher(b);
            if (m.matches()) {
                ;
            } else {
                return true;
            }
        }
        return res;
    }


}
