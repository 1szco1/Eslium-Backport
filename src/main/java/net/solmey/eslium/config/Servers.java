package net.solmey.eslium.config;

public class Servers {

    public boolean CUSTOM = true;
    public boolean MODDED = true;
    public boolean PLUGIN = true;
    public boolean VANILLA = true;

    public boolean any() {
        return CUSTOM || MODDED || PLUGIN || VANILLA;
    }
}
