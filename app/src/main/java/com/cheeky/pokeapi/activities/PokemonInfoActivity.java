package com.cheeky.pokeapi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cheeky.pokeapi.R;
import com.cheeky.pokeapi.models.Pokemon;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class PokemonInfoActivity extends AppCompatActivity {

    private Pokemon currentPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_info);

        Intent intent = getIntent();
        currentPokemon = new Pokemon(intent.getStringExtra("pokemon_name"), intent.getStringExtra("pokemon_url"));

        ImageView ivPokemonPicture = findViewById(R.id.ivPokemonDetailsPicture);
        TextView tvPokemonName = findViewById(R.id.tvPokemonDetailsName);

        Picasso.get().load(currentPokemon.getPictureUrl()).error(R.drawable.placeholder_image).into(ivPokemonPicture);
        tvPokemonName.setText(currentPokemon.getName());

        loadFullPokemonDetails();
    }

    private void loadFullPokemonDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                currentPokemon.getUrl(), null, response -> {
            try {
                int baseExperience = response.getInt("base_experience");
                int height = response.getInt("height");
                int weight = response.getInt("weight");
                JSONArray types = response.getJSONArray("types");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this, String.format("Unable to load %s's details", currentPokemon.getName()), Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonObjectRequest);
    }
}