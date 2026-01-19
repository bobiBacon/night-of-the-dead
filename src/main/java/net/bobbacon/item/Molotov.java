package net.bobbacon.item;

import net.bobbacon.entity.FireDrop;
import net.bobbacon.entity.MolotovEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class Molotov extends Item {
    private static final String Lit_KEY = "lit";
    private static final String TIME_KEY = "REMAINING_TIME_KEY";
    private static final int DefaultTime = 160;
    public int explosionRadius = 3;

    public Molotov(Settings settings) {
        super(settings);
    }
    public Molotov(Settings settings, int explosionRadius) {
        this(settings);
        this.explosionRadius = explosionRadius;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (isLit(itemStack)) {
            if (!world.isClient) {
                MolotovEntity molotovEntity = new MolotovEntity(user, world,explosionRadius);
                molotovEntity.setItem(itemStack);
                molotovEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, explosionRadius/2f-0.5f, 1.0F);
                world.spawnEntity(molotovEntity);
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            itemStack.decrement(1);
            return TypedActionResult.success(itemStack, world.isClient());
        } else if (user.getStackInHand(Hand.OFF_HAND).isOf(Items.FLINT_AND_STEEL)) {
            world.playSound(null,user.getX(),user.getY(),user.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS,1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
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
        if (world.isClient()){
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
    public static Molotov getMolotovFrom(ItemStack stack){
        if (stack.getItem() instanceof Molotov molotov){
            return molotov;
        }
        return (Molotov) ModItems.MOLOTOV;
    }

    @NotNull
    private static MolotovEntity getMolotovEntity(ItemStack stack, World world, Entity entity) {
        MolotovEntity molotovEntity;
        if (entity instanceof ItemEntity itemEntity){
            molotovEntity = new MolotovEntity(itemEntity.getX(),itemEntity.getY(),itemEntity.getZ(), world, getMolotovFrom(stack).explosionRadius);
        }
        else {
            molotovEntity = new MolotovEntity((LivingEntity) entity, world, getMolotovFrom(stack).explosionRadius);
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

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        if (world.isClient){
            return;
        }
        if (stack.getOrCreateNbt().getBoolean("from_refining") ? world.random.nextFloat()*10f>9f : world.random.nextFloat()*5f>4f){
            world.createExplosion(null,player.getX(),player.getY(),player.getZ(),1,true, World.ExplosionSourceType.BLOCK);
            for (int i = 0; i <6; i++) {
                FireDrop fireDrop = new FireDrop( null, world, player.getX(),player.getY()+1.0,player.getZ());

                int angle = (int) (world.random.nextFloat() * 2 * Math.PI);
                fireDrop.setVelocity(Math.cos(angle), 1.5, Math.sin(angle), 0.4F * world.random.nextFloat() + 0.3f, 2.0F);
                world.spawnEntity(fireDrop);
            }
            stack.decrement(1);
        }
    }
}

