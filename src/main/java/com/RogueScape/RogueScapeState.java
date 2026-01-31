package com.RogueScape;

import net.runelite.api.Skill;
import net.runelite.api.gameval.ItemID;

import java.util.HashSet;
import java.util.Set;

public class RogueScapeState {
    public Set<String> unlockedItems = new HashSet<>();
    public Set<String> unlockedSkills = new HashSet<>();
    public Set<String> unlockedQuests = new HashSet<>();

    public RogueScapeState() {
        // starting unlocks (example)
        unlockedSkills.add("ATTACK");
        unlockedSkills.add("HITPOINTS");
        unlockedItems.add("COINS");
        unlockedItems.add("POTATO");
    }

    public boolean isUnlocked(UnlockOption option) {
        switch (option.getType()) {
            case ITEM:
                return unlockedItems.contains(option.getId());
            case SKILL:
                return unlockedSkills.contains(option.getId());
            case QUEST:
                return unlockedQuests.contains(option.getId());
        }
        return false;
    }
}