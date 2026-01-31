package com.RogueScape;

public class UnlockOption
{
    private String id;
    private String name;
    private UnlockType type;

    public UnlockOption(String id, String name, UnlockType type)
    {
        this.id = id;
        this.name = name;
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

    public UnlockType getType()
    {
        return type;
    }
}
