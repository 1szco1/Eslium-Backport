package net.solmey.eslium.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.solmey.eslium.rollback.InteractionManager;
import net.solmey.eslium.server.SimulatedInventory;
import net.solmey.eslium.util.Entities;

public class Data {

    public static long timestampNanoNextServerTick = -1;

    public static List<Packet<?>> sentPackets = new ArrayList<>();
    public static Multimap<Long, Packet<ClientGamePacketListener>> predictedPackets = ArrayListMultimap.create();
    public static Multimap<Long, Packet<ClientGamePacketListener>> extractedToBeValidedPackets = ArrayListMultimap.create();

    public static boolean hasPendingState() {
        return !sentPackets.isEmpty() || !predictedPackets.isEmpty()
                || !extractedToBeValidedPackets.isEmpty();
    }

    // Drop everything that belongs to a connection which is no longer current.
    public static void clearAll() {
        timestampNanoNextServerTick = -1;
        sentPackets.clear();
        predictedPackets.clear();
        extractedToBeValidedPackets.clear();
        InteractionManager.clear();
        SimulatedInventory.reset();
        Entities.clearAll();
    }

    public static void extractPackets(Multimap<Long, Packet<ClientGamePacketListener>> multimap) {
        Multimap<Long, Packet<ClientGamePacketListener>> tempMultimap = ArrayListMultimap.create();
        tempMultimap.putAll(multimap);

        multimap.clear();

        Iterator<Map.Entry<Long, Packet<ClientGamePacketListener>>> iterator = tempMultimap.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Packet<ClientGamePacketListener>> entry = iterator.next();
            extractPacket(multimap, entry.getKey(), entry.getValue());
        }
    }

    private static void extractPacket(Multimap<Long, Packet<ClientGamePacketListener>> multimap, long timestamp,
                                      Packet<ClientGamePacketListener> packet) {
        if (packet instanceof ClientboundBundlePacket bundlePacket) {
            for (Packet<?> subPacket : bundlePacket.subPackets()) {
                if (subPacket instanceof Packet<?> p) {
                    extractPacket(multimap, timestamp, (Packet<ClientGamePacketListener>) p);
                }
            }
        } else {
            multimap.put(timestamp, packet);
        }
    }
}
