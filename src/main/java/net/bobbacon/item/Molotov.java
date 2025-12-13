package net.bobbacon.item;

import net.bobbacon.entity.FireDrop;
import net.bobbacon.entity.ModEntities;
import net.bobbacon.entity.MolotovEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Molotov extends Item {
    private static final String Lit_KEY = "lit";
    private static final String TIME_KEY = "REMAINING_TIME_KEY";
    private static final int DefaultTime = 160;

    public Molotov(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (isLit(itemStack)) {
            if (!world.isClient) {
                MolotovEntity molotovEntity = new MolotovEntity(user, world);
                molotovEntity.setItem(itemStack);
                molotovEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, 1F, 1.0F);
                world.spawnEntity(molotovEntity);
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            itemStack.decrement(1);
            return TypedActionResult.success(itemStack, world.isClient());
        } else if (user.getStackInHand(Hand.OFF_HAND).isOf(Items.FLINT_AND_STEEL)) {
            if (!world.isClient) {
                setLit(itemStack, true);
            }
            user.swingHand(Hand.OFF_HAND,!world.isClient);
            return TypedActionResult.fail(itemStack);
        }
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient){
            return;
        }
        if (isLit(stack)){
            if (!selected){
                setLit(stack,false);
                return;
            }
            int time = getRemainingTime(stack);
            time--;
            if (time<=0){
                    stack.decrement(1);
                MolotovEntity molotovEntity = getMolotovEntity(stack, world, entity);
                world.spawnEntity(molotovEntity);
                    molotovEntity.explode();
                    if (entity instanceof ItemEntity){
                        entity.discard();
                    }else {
                        entity.setOnFireFor(10);
                    }

            } else {
                setRemainingTime(stack,time);
            }
        }
    }

    @NotNull
    private static MolotovEntity getMolotovEntity(ItemStack stack, World world, Entity entity) {
        MolotovEntity molotovEntity;
        if (entity instanceof ItemEntity itemEntity){
            molotovEntity = new MolotovEntity(itemEntity.getX(),itemEntity.getY(),itemEntity.getZ(), world);
        }
        else {
            molotovEntity = new MolotovEntity((LivingEntity) entity, world);
        }
        molotovEntity.setItem(stack);
        molotovEntity.setPos(entity.getX(), entity.getY(), entity.getZ());
        return molotovEntity;
    }


    public static boolean isLit(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.getBoolean(Lit_KEY);
    }

    public static void setLit(ItemStack stack, boolean lit) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putBoolean(Lit_KEY, lit);
        setRemainingTime(stack,DefaultTime);
    }
    public static int getRemainingTime(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null){
            setRemainingTime(stack,DefaultTime);
            return DefaultTime;
        }
        return nbtCompound.getInt(TIME_KEY);
    }

    public static void setRemainingTime(ItemStack stack, int time) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt(TIME_KEY,time);
    }


    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return !oldStack.getOrCreateNbt().getBoolean(Lit_KEY);
    }

}

