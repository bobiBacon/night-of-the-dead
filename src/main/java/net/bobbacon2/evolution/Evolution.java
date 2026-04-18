package net.bobbacon2.evolution;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.function.Consumer;

public abstract class Evolution {
    public abstract void applyAttributeModifiers(PlayerEntity player);
    public abstract void removeAttributeModifiers(PlayerEntity player);
    public boolean burnsInDaylight(){
        return false;
    }
    public EntityGroup getGroup(){
        return EntityGroup.DEFAULT;
    }
    public abstract void tick(PlayerEntity player);
    public void onRemove(PlayerEntity player){
        removeAttributeModifiers(player);
    }
    public void onApply(PlayerEntity player){
        applyAttributeModifiers(player);
    }

    public void onRespawn(PlayerEntity player){
        onRemove(player);
        onApply(player);
    }

}
