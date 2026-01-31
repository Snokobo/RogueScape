package com.RogueScape;

public class UnlockOption
{
    private final String id;
    private final String name;
    private final String description;
    private final UnlockType type;

    public UnlockOption(String id, String name, String description, UnlockType type)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public UnlockType getType()
    {
        return type;
    }
}
