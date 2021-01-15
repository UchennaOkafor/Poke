package com.cheeky.pokeapi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cheeky.pokeapi.R;
import com.cheeky.pokeapi.models.Pokemon;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder> {

    private List<Pokemon> _pokemons;

    public PokemonListAdapter(List<Pokemon> pokemons){
        _pokemons = pokemons;
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poke_card, parent, false);
        return new PokemonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        holder.updatePokemon(_pokemons.get(position));
    }

    @Override
    public int getItemCount() {
        return _pokemons.size();
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPokemonName;
        private ImageView ivPokemonPicture;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            initializeWidgets(itemView);
            initializeEvents();
        }

        private void initializeWidgets(View itemView) {
            tvPokemonName = itemView.findViewById(R.id.tvPokemonName);
            ivPokemonPicture = itemView.findViewById(R.id.ivPokemonPicture);
        }

        private void initializeEvents() {

        }

        public void updatePokemon(Pokemon pokemon) {
            tvPokemonName.setText(pokemon.getName());
            Picasso.get().load(pokemon.getPictureUrl()).into(ivPokemonPicture);
        }
    }
}