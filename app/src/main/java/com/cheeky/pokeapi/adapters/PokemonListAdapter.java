package com.cheeky.pokeapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cheeky.pokeapi.R;
import com.cheeky.pokeapi.activities.PokemonInfoActivity;
import com.cheeky.pokeapi.models.Pokemon;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder> implements Filterable {

    private List<Pokemon> _pokemons;
    private List<Pokemon> _filteredPokemons;
    private Filter _searchFilter;

    public PokemonListAdapter(List<Pokemon> pokemons){
        _pokemons = pokemons;
        _filteredPokemons = pokemons;
    }

    private void initializeSearchFilter() {
        _searchFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchQuery = constraint.toString().isEmpty() ? "" : constraint.toString().toLowerCase();

                List<Pokemon> pokemons = _pokemons.stream()
                        .filter(pokemon -> pokemon.getName().toLowerCase()
                                .contains(searchQuery)).collect(Collectors.toList());


                _filteredPokemons.addAll(pokemons);

                FilterResults results = new FilterResults();
                results.values = _filteredPokemons;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                _filteredPokemons = (ArrayList<Pokemon>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poke_card, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        holder.updatePokemon(_pokemons.get(position));
    }

    @Override
    public int getItemCount() {
        return _pokemons.size();
    }

    @Override
    public Filter getFilter() {
        return _searchFilter;
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPokemonName;
        private ImageView ivPokemonPicture;
        private Pokemon currentPokemon;

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
            this.itemView.setOnClickListener(v -> {
                Context context = this.itemView.getContext();

                Intent intent = new Intent(context, PokemonInfoActivity.class);
                intent.putExtra("pokemon_name", currentPokemon.getName());
                intent.putExtra("pokemon_url", currentPokemon.getUrl());

                context.startActivity(intent);
            });
        }

        public void updatePokemon(Pokemon pokemon) {
            currentPokemon = pokemon;
            tvPokemonName.setText(pokemon.getName());
            Picasso.get().load(pokemon.getPictureUrl()).into(ivPokemonPicture);
        }
    }
}