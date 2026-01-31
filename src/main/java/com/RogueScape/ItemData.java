package com.RogueScape;

public class ItemData {
    private final String id;        // e.g., "BRONZE_SWORD"
    private final String name;      // "Bronze Sword"
    private final String imageUrl;  // URL to wiki icon
    private final String description; // optional

    public ItemData(String id, String name, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
}