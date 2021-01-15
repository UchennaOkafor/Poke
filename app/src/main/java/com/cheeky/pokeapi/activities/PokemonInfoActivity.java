package com.cheeky.pokeapi.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.cheeky.pokeapi.R;

public class PokemonInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_info);

        System.out.println(getIntent().getStringExtra("pokemon_name"));
        System.out.println(getIntent().getStringExtra("pokemon_url"));
    }
}