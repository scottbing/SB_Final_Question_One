package com.android.sb_final_question_one;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText word1;
    private EditText word2;
    private EditText word3;
    private Button submit;
    private String reqUrl = "http://rhymebrain.com/talk?";    // call rhymebrain
    private static final String REQUEST_METHOD_GET = "GET";
    private static final String TAG_HTTP_URL_CONNECTION = "HTTP_URL_CONNECTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // connect XML to code
        word1 = (EditText) findViewById(R.id.word1);
        word2 = (EditText) findViewById(R.id.word2);
        word3 = (EditText) findViewById(R.id.word3);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                processWords();

            }
        });

    }

    private void processWords() {
        String word01, word02, word03;

        // get user input
        word01 = word1.getText().toString();
        startSendHttpRequestThread(word01);
        /*makeRequest(word01);
        word02 = word2.getText().toString();
        makeRequest(word02);
        word03 = word3.getText().toString();
        makeRequest(word03);*/
        // words successfully acquired - verified in debugger - sbing


    }

    /* Start a thread to send http request to web server use HttpURLConnection object. */
    private void startSendHttpRequestThread(final String word)
    {
        Thread sendHttpRequestThread = new Thread()
        {
            @Override
            public void run() {
                // using Panel 7.2.21 as a model- The Movie API

                //make empty URL and connection
                URL url;
                HttpURLConnection ur1Connection = null; //HttpsURLConnection aiso avaitab1e
                try {

                    String service = "https://rhymebrain.com/talk?";    // call rhymebrain
                    //String parm = "getRhymes&word=" + word;
                    //String function = URLEncoder.encode(parm, "UTF-8");
                    String function = "getRhymes&word=" + word;
                    //try to process url and connect to it
                    url = new  URL( service +  "function=" + function);
                    ur1Connection = (HttpURLConnection)url.openConnection();
                    ur1Connection.setRequestMethod("GET");
                    //create an input stream and stream reader from the connection
                    InputStream inputStream = ur1Connection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    //get some data from the stream
                    int data = inputStreamReader.read();
                    //string for collecting all output
                    String output = "";
                    //if the stream is not empty
                    while(data != -1) {
                        //turn what we read into a char and print it
                        char current = (char) data;
                        output += current;
                        data = inputStreamReader.read();

                        //Log.d("Network", output);
                    }
                    Log.d("Network", output);
                    parseJSON(output);
                }catch (Exception e) {
                    Log.d( "Network", e.toString());
                }finally {
                    if (ur1Connection != null) {
                        ur1Connection.disconnect();
                        ur1Connection = null;
                    }
                }
            }

        };
        // Start the child thread to request web page.
        sendHttpRequestThread.start();
    }

    private void parseJSON(String rhymeJSON) {
        try {

            JSONObject jsonObject = new JSONObject(rhymeJSON);
            JSONArray jsonArray = (JSONArray)jsonObject.get("results");
            JSONObject word = jsonArray.getJSONObject(0);
            JSONObject syllables = jsonArray.getJSONObject(4);

        } catch (JSONException e) {
            Log.d("MainActivity", e.toString());
        }
    }

    private void makeRequest(String word) {

        // using Panel 7.2.21 as a model- The Movie API

        //make empty URL and connection
        URL url;
        HttpURLConnection ur1Connection = null; //HttpsURLConnection aiso avaitab1e
        try {
            //String API_KEY =  YOUR API KEY GOES HERE"'????
            String service = "http://rhymebrain.com/talk?";    // call rhymebrain
            //String apiKey = URLEncoder.encode(API_KEY, anc:
            String parm = "getRhymes&word="+word;
            String function = URLEncoder.encode(parm, "UTF-8");

            //try to process url and connect to it
            url = new  URL( service +  "&function=" + function);
            ur1Connection = (HttpURLConnection)url.openConnection();
            ur1Connection.setRequestMethod("GET");
            //create an input stream and stream reader from the connection
            InputStream inputStream = ur1Connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            //get some data from the stream
            int data = inputStreamReader.read();
            //string for collecting all output
            String output = "";
            //if the stream is not empty
            while(data != -1) {
                //turn what we read into a char and print it
                char current = (char) data;
                output += current;
                data = inputStreamReader.read();

                //Log.d("Network", output);
            }

        }catch (Exception e) {
            Log.d( "Network", e.toString());
        }finally {
            if (ur1Connection != null) {
                ur1Connection.disconnect();
                ur1Connection = null;
            }
        }
    }

}