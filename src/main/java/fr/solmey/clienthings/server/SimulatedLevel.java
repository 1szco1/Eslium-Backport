package fr.solmey.clienthings.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import fr.solmey.clienthings.data.Data;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityAttributesS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySetHeadYawS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.util.Util;

public class SimulatedLevel {

    private static int fakeEntityId = Integer.MAX_VALUE;

    private static List<Entity> newEntities = new ArrayList<>();

    public static int newEntityId() {
        return fakeEntityId--;
    }

    public static void addEntity(ClientWorld clientWorld, Entity entity) {
        entity.setEntityId(newEntityId());
        newEntities.add(entity);

        clientWorld.addEntity(entity.getEntityId(), entity);
    }

    public static void tick(ClientWorld clientWorld) { // Tick server level
        for (Entity entity : newEntities) {
            clientWorld.removeEntity(entity.getEntityId());

            final List<Packet<?>> packets = new ArrayList<>();
            sendPairingData(entity, new Consumer<Packet<?>>() {
                @Override
                public void accept(Packet<?> packet) {
                    packets.add(packet);
                }
            });

            long timestamp = Util.getMeasuringTimeMs();
            for (Packet<?> packet : packets) {
                Data.predictedPackets.put(timestamp, packet);
            }
        }
        newEntities.clear();
    }

    private static void sendPairingData(Entity entity, Consumer<Packet<?>> broadcast) { // From ServerEntity.java
        Packet<?> packet = getAddEntityPacket(entity);
        broadcast.accept(packet);
        broadcast.accept(new EntityTrackerUpdateS2CPacket(entity.getEntityId(), entity.getDataTracker(), true));

        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;

            Collection<EntityAttributeInstance> attributes = livingEntity.getAttributes().getAttributesToSend();
            if (!attributes.isEmpty()) {
                broadcast.accept(new EntityAttributesS2CPacket(entity.getEntityId(), attributes));
            }

            List<Pair<EquipmentSlot, ItemStack>> slots = Lists.newArrayList();

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack itemStack = livingEntity.getEquippedStack(slot);
                if (!itemStack.isEmpty()) {
                    slots.add(Pair.of(slot, itemStack.copy()));
                }
            }

            if (!slots.isEmpty()) {
                broadcast.accept(new EntityEquipmentUpdateS2CPacket(entity.getEntityId(), slots));
            }
        }

        if (!entity.getPassengerList().isEmpty()) {
            broadcast.accept(new EntityPassengersSetS2CPacket(entity));
        }

        if (entity.getVehicle() != null) {
            broadcast.accept(new EntityPassengersSetS2CPacket(entity.getVehicle()));
        }

        broadcast.accept(new EntitySetHeadYawS2CPacket(entity, (byte) ((int) (entity.getHeadYaw() * 256.0F / 360.0F))));
    }

    private static Packet<?> getAddEntityPacket(Entity entity) {
        return new EntitySpawnS2CPacket(
            entity.getEntityId(),
            entity.getUuid(),
            entity.getX(),
            entity.getY(),
            entity.getZ(),
            entity.pitch,
            entity.yaw,
            entity.getType(),
            0,
            entity.getVelocity()
        );
    }
}
