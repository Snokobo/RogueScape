package com.RogueScape;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.util.Map;

public class OSRSItemLoader {
    private static Map<String, ItemData> items;

    static {
        try (InputStreamReader reader = new InputStreamReader(
                OSRSItemLoader.class.getResourceAsStream("/osrs_data/items.json"))) {
            items = new Gson().fromJson(reader,
                    new TypeToken<Map<String, ItemData>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ItemData getItem(String id) {
        return items.get(id);
    }
}