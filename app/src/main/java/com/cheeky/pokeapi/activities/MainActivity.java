package com.cheeky.pokeapi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cheeky.pokeapi.R;
import com.cheeky.pokeapi.adapters.PokemonListAdapter;
import com.cheeky.pokeapi.models.Pokemon;
import com.cheeky.pokeapi.utils.PokeEndpoints;
import com.cheeky.pokeapi.utils.SpacesItemDecoration;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rvPokemons);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacesItemDecoration(25));

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest (Request.Method.GET,
                PokeEndpoints.MAIN_ENDPOINT, null, response -> {
                try {
                    JSONArray results = response.getJSONArray("results");
                    Pokemon[] pokemons = new Gson().fromJson(results.toString(), Pokemon[].class);
                    adapter = new PokemonListAdapter(Arrays.asList(pokemons));
                    recyclerView.setAdapter(adapter);
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