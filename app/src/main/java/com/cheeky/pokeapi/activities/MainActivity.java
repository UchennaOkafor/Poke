package com.cheeky.pokeapi.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cheeky.pokeapi.R;
import com.cheeky.pokeapi.adapters.PokemonListAdapter;
import com.cheeky.pokeapi.models.Pokemon;
import com.cheeky.pokeapi.utils.PokeEndpoints;
import com.cheeky.pokeapi.utils.Pokedex;
import com.cheeky.pokeapi.utils.SpacesItemDecoration;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private PokemonListAdapter pokemonListAdapter;
    private RecyclerView rvPokemons;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {
        initializeWidgets();
        initializePokedex();
    }

    private void initializeWidgets() {
        rvPokemons = findViewById(R.id.rvPokemons);
        rvPokemons.setHasFixedSize(true);
        rvPokemons.setItemAnimator(new DefaultItemAnimator());
        rvPokemons.setLayoutManager(new GridLayoutManager(this, 2));
        rvPokemons.addItemDecoration(new SpacesItemDecoration(25));

        loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(R.layout.loading_card);

        ImageView imgView = loadingDialog.findViewById(R.id.ivLoadingImage);
        Glide.with(this).load(R.drawable.pokeball_loading).into(imgView);
    }

    private void initializePokedex() {
        if (Pokedex.isInitialized(this)) {
            Pokemon[] pokemons = Pokedex.getPokemons(this);
            pokemonListAdapter = new PokemonListAdapter(Arrays.asList(pokemons));
            rvPokemons.setAdapter(pokemonListAdapter);
        } else {
            //For some reason the gif in the dialog doesn't always play properly until the images have loaded
            //I thought this was an issue with the Volley request blocking the UI thread, but Volley runs in the background.
            //I assume the problem is because the event queue hasn't finished updating yet.
            //So my question is, what is best practice on how I can fix this issue?
            //Does Android have the equivalent to vue.js's vue.nextTick() that solves this problem ? Thanks :D

            loadingDialog.show();
            JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    PokeEndpoints.API_POKEDEX_ENDPOINT, null, response -> {
                try {
                    JSONArray results = response.getJSONArray("results");
                    Pokemon[] pokemons = new Gson().fromJson(results.toString(), Pokemon[].class);
                    pokemonListAdapter = new PokemonListAdapter(Arrays.asList(pokemons));
                    rvPokemons.setAdapter(pokemonListAdapter);
                    Pokedex.initializePokemons(this, pokemons);
                    loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(this, "Unable to load the list of pokemons", Toast.LENGTH_SHORT).show());

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pokemonListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}