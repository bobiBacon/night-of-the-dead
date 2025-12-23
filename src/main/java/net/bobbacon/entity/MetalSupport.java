package net.bobbacon.entity;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MetalSupport extends ItemFrameEntity {
    public MetalSupport(EntityType<? extends ItemFrameEntity> entityType, World world) {
        super(entityType, world);
    }

    public MetalSupport(World world, BlockPos pos, Direction facing) {
        this(ModEntities.METAL_SUPPORT,world, pos, facing);
    }

    public MetalSupport(EntityType<? extends ItemFrameEntity> type, World world, BlockPos pos, Direction facing) {
        super(type, world, pos, facing);
    }
    protected ItemStack getAsItemStack() {
        return new ItemStack(ModItems.METAL_SUPPORT);
    }

    @Override
    public void setHeldItemStack(ItemStack value, boolean update) {
        if (value.isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(NightOfTheDead.MOD_ID, "hookable")))) {
            super.setHeldItemStack(value, update);
        }
    }
}
