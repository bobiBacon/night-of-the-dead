package net.bobbacon2.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SoulWormHead extends SoulWormPart {
    protected SoulWormHead(SoulWorm parent, Vec3d startingPoint) {
        super(parent,startingPoint);
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }


    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

}
