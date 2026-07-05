package com.example.text_game;

public class Location {
    private String name;
    private String description;
    private String picture;

    public Location(String name, String description, String picture) {

        this.name = name;
        this.description = description;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }
}
