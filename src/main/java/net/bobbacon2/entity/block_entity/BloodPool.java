package net.bobbacon2.entity.block_entity;

import net.bobbacon2.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BloodPool extends BlockEntity {
    protected int level=0;
    public BloodPool(BlockPos pos, BlockState state) {
        super(ModBE.BLOOD_POOL, pos, state);
    }

    public int getLevel() {
        return level;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stackInHand = player.getStackInHand(hand);
        if (stackInHand.isOf(ModItems.BLOOD_BOTTLE)&&level<3){
            level++;
            stackInHand.decrement(1);
            player.setStackInHand(hand,stackInHand);
            player.giveItemStack(Items.GLASS_BOTTLE.getDefaultStack());
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.5F);

            markDirtyAndSync();

            return ActionResult.SUCCESS;
        }else if (stackInHand.isOf(Items.GLASS_BOTTLE)&&level>0){
            level--;
            stackInHand.decrement(1);
            player.setStackInHand(hand,stackInHand);
            player.giveItemStack(ModItems.BLOOD_BOTTLE.getDefaultStack());
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
    public void empty(){
        level=0;
        markDirtyAndSync();
    }
    @Override
    @Nullable
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    public void markDirtyAndSync() {
        markDirty();

        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        level = nbt.getInt("blood_level");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("blood_level",level);
    }
}
