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

import com.bumptech.glide.Glide;
import com.cheeky.pokeapi.R;
import com.cheeky.pokeapi.activities.PokemonDetailsActivity;
import com.cheeky.pokeapi.models.Pokemon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder> implements Filterable {

    private final List<Pokemon> _allPokemons;
    private List<Pokemon> _filteredPokemons;
    private Filter _searchFilter;

    public PokemonListAdapter(List<Pokemon> pokemons){
        _allPokemons = pokemons;
        _filteredPokemons = pokemons;
        initializeSearchFilter();
    }

    private void initializeSearchFilter() {
        _searchFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchQuery = constraint.toString().isEmpty() ? "" : constraint.toString().toLowerCase();

                _filteredPokemons = _allPokemons.stream()
                        .filter(pokemon -> pokemon.getName().toLowerCase()
                                .contains(searchQuery)).collect(Collectors.toList());

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
        holder.updatePokemon(_filteredPokemons.get(position));
    }

    @Override
    public int getItemCount() {
        return _filteredPokemons.size();
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

                Intent intent = new Intent(context, PokemonDetailsActivity.class);
                intent.putExtra("pokemon_name", currentPokemon.getName());
                intent.putExtra("pokemon_url", currentPokemon.getUrl());

                context.startActivity(intent);
            });
        }

        public void updatePokemon(Pokemon pokemon) {
            currentPokemon = pokemon;
            tvPokemonName.setText(pokemon.getName());

            Glide.with(itemView.getContext())
                    .load(currentPokemon.getPictureUrl())
                    .error(R.drawable.placeholder_image)
                    .into(ivPokemonPicture);
        }
    }
}