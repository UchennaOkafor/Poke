package com.cheeky.pokeapi.activities;

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
    private TextView tvBaseXpValue, tvHeightValue, tvWeightValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_info);

        tvHeightValue = findViewById(R.id.tvHeightValue);
        tvWeightValue = findViewById(R.id.tvWeightValue);
        tvBaseXpValue = findViewById(R.id.tvBaseXpValue);

        loadFullPokemonDetails();
    }

    private void loadFullPokemonDetails() {
        String name = getIntent().getStringExtra("pokemon_name");
        String url = getIntent().getStringExtra("pokemon_url");
        currentPokemon = new Pokemon(name, url);

        ImageView ivPokemonPicture = findViewById(R.id.ivPokemonDetailsPicture);
        TextView tvPokemonName = findViewById(R.id.tvPokemonDetailsName);

        Picasso.get().load(currentPokemon.getPictureUrl())
                .error(R.drawable.placeholder_image).into(ivPokemonPicture);
        tvPokemonName.setText(currentPokemon.getName());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                currentPokemon.getUrl(), null, response -> {
            try {
                int baseExperience = response.getInt("base_experience");
                int height = response.getInt("height") * 10;
                int weight = response.getInt("weight") / 10;
                JSONArray types = response.getJSONArray("types");

                tvBaseXpValue.setText(String.valueOf(baseExperience));
                tvHeightValue.setText(String.format("%s cm", height));
                tvWeightValue.setText(String.format("%s kg", weight));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(this, String.format("Unable to load %s's details", currentPokemon.getName()), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}