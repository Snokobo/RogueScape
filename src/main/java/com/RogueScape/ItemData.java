package com.RogueScape;

public class ItemData {
    private String id;
    private String name;
    private String imageUrl;

    // No-arg constructor needed for Gson
    public ItemData() {}

    // Optional: full constructor
    public ItemData(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}