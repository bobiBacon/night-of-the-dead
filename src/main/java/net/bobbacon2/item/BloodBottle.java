package net.bobbacon2.item;

import net.bobbacon2.accessors.PlayerAccessor;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class BloodBottle extends Alcohol{

    protected ArrayList<StatusEffectInstance> vampireEffects;
    public BloodBottle(Settings settings, StatusEffectInstance... effects) {
        super(settings, effects);
    }

    public BloodBottle setVampireEffects(StatusEffectInstance... vampireEffects) {
        this.vampireEffects = new ArrayList<>(List.of(vampireEffects));
        return this;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }
        if (!world.isClient) {
            if (user.getGroup()== EntityGroup.UNDEAD){
                applyEffects(((BloodBottle)stack.getItem()).vampireEffects,user);

            }else {
                applyEffects(((BloodBottle)stack.getItem()).effects,user);

            }
            if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                if (!playerEntity.getAbilities().creativeMode) {
                    stack=decrement(stack,serverPlayerEntity);
                }
            }
        }
        user.emitGameEvent(GameEvent.DRINK);

        return stack;

    }
}
