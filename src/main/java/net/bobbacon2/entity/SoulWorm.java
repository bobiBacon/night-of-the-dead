package net.bobbacon2.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SoulWorm extends MobEntity implements Monster {
    List<SoulWormPart> parts= new ArrayList<>();
    protected SoulWorm(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        parts.add(new SoulWormPart())
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return null;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return null;
    }
}
