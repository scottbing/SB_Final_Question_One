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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText word1;
    private EditText word2;
    private EditText word3;
    private Button submit;
    private String reqUrl = "http://rhymebrain.com/talk?";    // call rhymebrain
    private static final String REQUEST_METHOD_GET = "GET";
    private static final String TAG_HTTP_URL_CONNECTION = "HTTP_URL_CONNECTION";

    // Syllable Arrays
    private ArrayList<String> one_syllable;
    private ArrayList<String> two_syllable;
    private ArrayList<String> three_syllable;
    private ArrayList<String> four_syllable;
    private ArrayList<String> five_syllable;
    private ArrayList<String> six_syllable;
    private ArrayList<String> seven_syllable;

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
                int maxResults = 3;            // limit the number of results
                //make empty URL and connection
                URL url;
                HttpURLConnection ur1Connection = null; //HttpsURLConnection aiso avaitab1e
                try {

                    String service = "https://rhymebrain.com/talk?";    // call rhymebrain
                    //String parm = "getRhymes&word=" + word;
                    //String queryString = URLEncoder.encode(parm, "UTF-8");
                    String queryString = "getRhymes&word=" + word + "&maxResults=" + String.valueOf(maxResults);
                    //try to process url and connect to it
                    url = new  URL( service +  "function=" + queryString);
                    ur1Connection = (HttpURLConnection)url.openConnection();
                    ur1Connection.setRequestMethod("GET");

                    // Set connection timeout and read timeout value.
                    ur1Connection.setConnectTimeout(70000);
                    ur1Connection.setReadTimeout(70000);

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
            String word;
            String numOfSyllables;

            // establish syllable arrays
            ArrayList<String> one_syllable = new ArrayList<String>();
            ArrayList<String> two_syllable = new ArrayList<String>();
            ArrayList<String> three_syllable = new ArrayList<String>();
            ArrayList<String> four_syllable = new ArrayList<String>();
            ArrayList<String> five_syllable = new ArrayList<String>();
            ArrayList<String> six_syllable = new ArrayList<String>();
            ArrayList<String> seven_syllable = new ArrayList<String>();

            // process JSON rhyming word list
            JSONArray jsonArray = new JSONArray(rhymeJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject wordListObject = jsonArray.getJSONObject(i);
                word = wordListObject.getString("word");
                numOfSyllables = wordListObject.getString("syllables");

                switch (numOfSyllables) {
                    case "1":
                        one_syllable.add(word);
                        break;
                    case "2":
                        two_syllable.add(word);
                        break;
                    case "3":
                        three_syllable.add(word);
                        break;
                    case "4":
                        four_syllable.add(word);
                        break;
                    case "5":
                        five_syllable.add(word);
                        break;
                    case "6":
                        six_syllable.add(word);
                        break;
                    case "7":
                        seven_syllable.add(word);
                        break;
                    default:
                        // throw it away
                        break;
                }
            }
        } catch (JSONException e) {
            Log.d("MainActivity", e.toString());
            int i =0;
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
            String queryString = URLEncoder.encode(parm, "UTF-8");

            //try to process url and connect to it
            url = new  URL( service +  "&queryString=" + queryString);
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