package com.android.sb_final_question_one;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText word1;
    private EditText word2;
    private EditText word3;
    private Button submit;

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
        makeRequest(word01);
        word02 = word2.getText().toString();
        makeRequest(word02);
        word03 = word3.getText().toString();
        makeRequest(word03);
        // words successfully acquired - verified in debugger - sbing


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
            String query = URLEncoder.encode(word, "UTF-8");

            //try to process url and connect to it
            url = new  URL( service +  "&query=" + query);
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
                //
                //ur1Connection.close();  // does not recognize close()
                //
            }
        }
    }

}