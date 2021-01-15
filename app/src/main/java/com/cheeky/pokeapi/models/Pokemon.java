package com.cheeky.pokeapi.models;

public class Pokemon {
    private int id;
    private String name;
    private String url;

    public String getName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        //I would initialise the id field in the constructor, but the #Gson.fromJson method doesn't invoke the constructor
        if (this.id == 0) {
            String[] parts = this.url.split("/");
            this.id = Integer.parseInt(parts[parts.length - 1]);
        }

        return this.id;
    }

    public String getPictureUrl() {
        //The Id of the pokeapi.co skips by 9194 after 807, rather than being sequential
        //This uses a fallback for the broken images
        if (this.getId() <= 807) {
            return String.format("https://pokeres.bastionbot.org/images/pokemon/%s.png", this.id);
        } else {
            return String.format("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/%s.png", this.id);
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }
}