package com.cheeky.pokeapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cheeky.pokeapi.models.Pokemon;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();

        //findViewById(R.id.button).setOnClickListener(v -> {
            //Snackbar.make(v, String.format("%s is cool", myClass[0].getName()), Snackbar.LENGTH_LONG)
                    //.setAction("Action", null).show();
        //});

        String url = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=1118";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, url, null,
            response -> {
                try {
                    JSONObject results = response.getJSONObject("results");
                    System.out.println(results.toString());

                    Pokemon[] pokemons = gson.fromJson(results.toString(), Pokemon[].class);
                    Toast.makeText(this, String.valueOf(pokemons.length), Toast.LENGTH_SHORT).show();
                    //sout("Response is: "+ response.substring(0,500));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                //sout("That didn't work!")
            }
        );

        queue.add(jsonObjectRequest);
    }
}