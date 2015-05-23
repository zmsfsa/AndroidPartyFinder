package com.example.mzmey.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MZmey on 22.05.2015.
 */
public class EventCreate extends Activity{

    private static final char PLUS = '+';
    private static final String DATE_DELIMETR = "-";
    private final String QUESTION_MARK = "?";
    private EditText edSend;
    private final String DELIMETR = "=";
    private static String uri = "http://93.175.7.110:8080/event/create";
    private RequestQueue queue;
    private TextView tvOut;
    private EditText edEvent;
    private String login = "mzmey37";
    private EditText edDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        tvOut = (TextView)findViewById(R.id.tvOut);
        edEvent = (EditText)findViewById(R.id.edEvent);
        edDate = (EditText)findViewById(R.id.edDate);
        edSend = (EditText)findViewById(R.id.edSend);
        queue = MyQueue.getInstance(this.getApplicationContext()).getQueue();
    }

    public void onClick(View v){

        String[] dates = edDate.getText().toString().split(DATE_DELIMETR);

        if((dates.length != 3) || (Integer.parseInt(dates[0]) < 1) ||
                (Integer.parseInt(dates[0]) > 31) || (Integer.parseInt(dates[1]) < 0)
                || ((Integer.parseInt(dates[1]) > 31))){

            tvOut.setText("wrong format of date");

        } else {

            StringRequest sr = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("OK")) {
                        tvOut.setText("Event created. Click 'come to event' to visit event's page");
                    } else {
                        tvOut.setText(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    tvOut.setText("Connecction problem, check your network");
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("login", login);
                    params.put("eventName", edEvent.getText().toString());
                    params.put("eventDate", edDate.getText().toString());

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    return params;
                }
            };
            queue.add(sr);
        }
    }

    public void onGoOn(View v){

        Intent intent = new Intent(this, EventPage.class);
        intent.putExtra("login", login);
        intent.putExtra("name", edEvent.getText().toString());
        startActivity(intent);
    }

    private String noPros(String in){
        char[] c = in.toCharArray();
        for(int i = 0; i < c.length; i++)
            if (c[i] == PLUS)
                c[i] = ' ';
        return new String(c);
    }
}
