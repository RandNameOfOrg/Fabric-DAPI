package ru.daniil10295.DAPI.client;

public class CommandArgument {
    public String name;
    public String defualtValue;
    public int permissionLevel = 0;
    public boolean required = true;
    public String value;

    protected void do_init(String name, String defualtValue, int permissionLevel, boolean required) {
        this.name = name;
        this.defualtValue = defualtValue;
        this.permissionLevel = permissionLevel;
        this.required = required;
        value = defualtValue;
    }

    public CommandArgument(String name, String defualtValue) {
        do_init(name, defualtValue, permissionLevel, required);
    }

    public CommandArgument(String name, String defualtValue, int permissionLevel) {
        do_init(name, defualtValue, permissionLevel, required);
    }

    public CommandArgument(String name, String defualtValue, int permissionLevel, boolean required) {
        do_init(name, defualtValue, permissionLevel, required);
    }

    public CommandArgument(String name, String defualtValue, boolean required) {
        do_init(name, defualtValue, permissionLevel, required);
    }
}
