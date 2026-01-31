package com.RogueScape;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemRepository {

    private static final Map<String, ItemData> ITEMS = new HashMap<>();

    static {
        ITEMS.put("BRONZE_SWORD", new ItemData(
                "BRONZE_SWORD",
                "Bronze Sword",
                "https://oldschool.runescape.wiki/images/Bronze_sword.png"
        ));

        ITEMS.put("IRON_SWORD", new ItemData(
                "IRON_SWORD",
                "Iron Sword",
                "https://oldschool.runescape.wiki/images/Iron_sword.png"
        ));

        // Add more items here...
    }

    public static ItemData get(String id) {
        return ITEMS.get(id);
    }

    // <<< NEW METHOD >>>
    public static Collection<ItemData> getAllItems() {
        return ITEMS.values();
    }
}