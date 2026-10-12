package net.solmey.eslium.config;

public class Placement {

    public boolean enabled = true;
    public Servers servers = new Servers();
    public boolean dynamic = true;
    public int fixedWindowMs = 1000;
    public int floorMs = 50;
    public int capMs = 300;
}
