package net.solmey.eslium.config;

public class Config {

    public boolean enabled = true;
    public boolean optout = false;
    public boolean debug = false;
    public boolean experimental = false;
    public int latencyMargin = 25;
    public int version = 1;
    public boolean safeMode = false;
    public int simulatedDesync = 100;

    public Consumables consumables = new Consumables();
    public Cooldowns cooldowns = new Cooldowns();
    public Crystals crystals = new Crystals();
    public Elytras elytras = new Elytras();
    public Firework firework = new Firework();
    public Minecart minecart = new Minecart();
    public Pose pose = new Pose();
    public Swap swap = new Swap();
    public Weapons weapons = new Weapons();
    public Windcharge windcharge = new Windcharge();
    public Throwables throwables = new Throwables();
    public Placeables placeables = new Placeables();
    public Interactables interactables = new Interactables();
    public Tools tools = new Tools();
    public Combat combat = new Combat();
    public Redstone redstone = new Redstone();
    public Anchors anchors = new Anchors();
    public Placement placement = new Placement();
}
