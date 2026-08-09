package fr.solmey.clienthings.config;

public class Config {
    public Boolean enabled = true;
    public Boolean optout = false;
    public Boolean debug = false;
    public Boolean experimental = false;
    public Integer latencyMargin = 25;
    public Integer version = 1;
    public Boolean safeMode = false;
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
}
