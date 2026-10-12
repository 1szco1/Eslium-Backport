package net.solmey.eslium.mixin.placeables;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorStandItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;
import net.solmey.eslium.config.ConfigManager;
import net.solmey.eslium.util.Entities;

@Mixin(ArmorStandItem.class)
public class ArmorStandItemMixin {
    @Inject(method = "useOn", at = @At("TAIL"), cancellable = true)
    private void eslium$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (ConfigManager.getConfig() == null) return;
        if (ConfigManager.getConfig().placeables.enabled && ConfigManager.shouldWork(ConfigManager.getConfig().placeables.servers)) {
            if (cir.getReturnValue() == InteractionResult.SUCCESS && context.getLevel() instanceof ClientLevel clientLevel) {
                Vec3 hit = context.getClickLocation();
                ArmorStand armorStand = new ArmorStand(clientLevel, hit.x, hit.y, hit.z);
                Player player = context.getPlayer();
                armorStand.setYRot(player == null ? 0.0F : player.getYRot());

                int fakeId = Entities.getNextFakeId();
                armorStand.setId(fakeId);
                clientLevel.addEntity(armorStand);
                Entities.set(System.currentTimeMillis(), armorStand, armorStand, Entities.FAKE);
            }
        }
    }
}
