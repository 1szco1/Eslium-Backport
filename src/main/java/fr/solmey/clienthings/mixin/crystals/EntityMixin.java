package fr.solmey.clienthings.mixin.crystals;

import fr.solmey.clienthings.config.JsonConfig;
import fr.solmey.clienthings.util.Entities;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (JsonConfig.config.crystals.enabled && JsonConfig.shouldWork(JsonConfig.config.crystals.servers)) {
            Entity entity = (Entity) (Object) this;
            if (entity instanceof EndCrystalEntity) {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;

                if (player != null && source.getAttacker() == player) {
                    if (!((EntityAccessor) entity).invokeIsAlwaysInvulnerableTo(source) && entity.isAlive()) {
                        if (entity.isAttackable()) {
                            if (!entity.handleAttack(player)) {
                                ItemStack itemStack = player.getMainHandStack();

                                StatusEffectInstance weakness = player.getStatusEffect(StatusEffects.WEAKNESS);
                                float weaknessAttack = weakness == null ? 0.0F : 4.0F * (weakness.getAmplifier() + 1);

                                StatusEffectInstance strength = player.getStatusEffect(StatusEffects.STRENGTH);
                                float strengthAttack = strength == null ? 0.0F : 3.0F * (strength.getAmplifier() + 1);

                                float[] attackDamage = new float[1];
                                attackDamage[0] = 0.0F;

                                // 1.16.5: MiningToolItem and SwordItem have getAttackDamage(), not ToolItem
                                if (itemStack.getItem() instanceof SwordItem) {
                                    attackDamage[0] = ((SwordItem) itemStack.getItem()).getAttackDamage();
                                } else if (itemStack.getItem() instanceof MiningToolItem) {
                                    attackDamage[0] = ((MiningToolItem) itemStack.getItem()).getAttackDamage();
                                }

                                int sharpnessLevel = EnchantmentHelper.getLevel(Enchantments.SHARPNESS, itemStack);
                                float sharpnessBonus = 0;
                                if (sharpnessLevel != 0)
                                    sharpnessBonus = (sharpnessLevel - 1) * 0.5F + 1;

                                float f = attackDamage[0] + strengthAttack - weaknessAttack;
                                float g = sharpnessBonus;
                                float h = Entities.attackCooldownProgress;
                                f *= 0.2F + h * h * 0.8F;
                                g *= h;

                                if (f > 0.0F || g > 0.0F) {
                                    if (Entities.getType(entity) == Entities.FAKE) {
                                        Entities.remove(entity);
                                        Entities.set(System.currentTimeMillis(), entity, entity, Entities.TO_DESTROY);
                                    } else {
                                        Entities.remove(entity);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
