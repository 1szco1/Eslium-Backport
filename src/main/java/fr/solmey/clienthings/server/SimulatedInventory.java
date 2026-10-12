package fr.solmey.clienthings.server;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class SimulatedInventory {

    public static List<ItemStack> serverInventory = new ArrayList<>();
    private static List<ItemStack> realInventory = new ArrayList<>();

    public static void startServerTick(PlayerEntity player) {
        saveRealInventory(player);
        restoreServerInventory(player);
    }

    public static void endServerTick(PlayerEntity player) {
        restoreRealInventory(player);
    }

    public static void saveServerInventory(PlayerEntity player) {
        serverInventory.clear();
        DefaultedList<ItemStack> items = player.inventory.main;
        for (int i = 0; i < items.size(); i++) {
            serverInventory.add(items.get(i).copy());
        }
    }

    private static void restoreServerInventory(PlayerEntity player) {
        DefaultedList<ItemStack> items = player.inventory.main;
        for (int i = 0; i < serverInventory.size() && i < items.size(); i++) {
            items.set(i, serverInventory.get(i));
        }
    }

    private static void saveRealInventory(PlayerEntity player) {
        realInventory.clear();
        DefaultedList<ItemStack> items = player.inventory.main;
        for (int i = 0; i < items.size(); i++) {
            realInventory.add(items.get(i));
        }
    }

    private static void restoreRealInventory(PlayerEntity player) {
        DefaultedList<ItemStack> items = player.inventory.main;
        for (int i = 0; i < realInventory.size() && i < items.size(); i++) {
            items.set(i, realInventory.get(i));
        }
    }
}
