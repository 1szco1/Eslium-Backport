package fr.solmey.clienthings.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.network.Packet;

public class Data {

    public static long timestampNanoNextServerTick = -1;

    public static List<Packet<?>> sentPackets = new ArrayList<>();
    public static Multimap<Long, Packet<?>> predictedPackets = ArrayListMultimap.create();
    public static Multimap<Long, Packet<?>> extractedToBeValidedPackets = ArrayListMultimap.create();

    public static void extractPackets(Multimap<Long, Packet<?>> multimap) {
        Multimap<Long, Packet<?>> tempMultimap = ArrayListMultimap.create();
        tempMultimap.putAll(multimap);

        multimap.clear();

        Iterator<Map.Entry<Long, Packet<?>>> iterator = tempMultimap.entries().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Long, Packet<?>> entry = iterator.next();

            extractPacket(multimap, entry.getKey(), entry.getValue());
        }
    }

    private static void extractPacket(Multimap<Long, Packet<?>> multimap, long timestamp, Packet<?> packet) {
        // 1.16.5 has no bundle packets, so a packet is stored directly.
        multimap.put(timestamp, packet);
    }
}
