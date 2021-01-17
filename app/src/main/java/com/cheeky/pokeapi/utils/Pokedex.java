package com.cheeky.pokeapi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.cheeky.pokeapi.models.Pokemon;
import com.google.gson.Gson;

//I made the methods and members in this static because they only ever needs to be once instance.
//I could have also used the singleton pattern but I thought that would be overkill

/**
 * A cheeky utility class for saving and retrieving persistent pokemon data
 */
public class Pokedex {
    private static final String POKEDEX_KEY = "pokedex";

    public static Pokemon[] getPokemons(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String jsonValue = sharedPref.getString(POKEDEX_KEY, "[]");
        return new Gson().fromJson(jsonValue, Pokemon[].class);
    }

    public static boolean isInitialized(Activity activity) {
        return getPokemons(activity).length > 0;
    }

    public static void initializePokemons(Activity activity, Pokemon[] pokemons) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(POKEDEX_KEY, new Gson().toJson(pokemons));
        editor.apply();
    }
}
