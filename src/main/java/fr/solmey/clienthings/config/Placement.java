package fr.solmey.clienthings.config;

public class Placement {
    public Boolean enabled = true;
    public Servers servers = new Servers();
    public Boolean dynamic = true;
    public Integer fixedWindowMs = 1000;
    public Integer floorMs = 50;
    public Integer capMs = 300;
}
