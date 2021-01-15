package com.cheeky.pokeapi.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.cheeky.pokeapi.R;
import com.cheeky.pokeapi.adapters.PokemonListAdapter;
import com.cheeky.pokeapi.models.Pokemon;
import com.cheeky.pokeapi.utils.PokeEndpoints;
import com.cheeky.pokeapi.utils.SpacesItemDecoration;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private PokemonListAdapter pokemonListAdapter;
    private RecyclerView rvPokemons;

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
        rvPokemons.setHasFixedSize(true);
        rvPokemons.setItemAnimator(new DefaultItemAnimator());
        rvPokemons.setLayoutManager(new GridLayoutManager(this, 2));
        rvPokemons.addItemDecoration(new SpacesItemDecoration(25));
    }

    private void initializePokedex() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                PokeEndpoints.API_POKEDEX_ENDPOINT, null, response -> {
            try {
                //Deserializing the returned JSON array and using it to initialize the recycler view adapter
                JSONArray results = response.getJSONArray("results");
                Pokemon[] pokemons = new Gson().fromJson(results.toString(), Pokemon[].class);
                pokemonListAdapter = new PokemonListAdapter(Arrays.asList(pokemons));
                rvPokemons.setAdapter(pokemonListAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this, "Unable to load the list of pokemons", Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
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