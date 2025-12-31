package net.bobbacon.entity.block_entity;

import net.bobbacon.item.ModItems;
import net.bobbacon.ritual.CorruptionRitual;
import net.bobbacon.ritual.RitualManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AltarBE extends BlockEntity {
    private DefaultedList<ItemStack> items= DefaultedList.ofSize(1,ItemStack.EMPTY);
    protected static final UUID NULL_ID= UUID.fromString("00000000-0000-0000-0000-000000000000");
    private static final String RITUAL_KEY = "ritual";
    public UUID ritualId= NULL_ID;

    public AltarBE(BlockPos pos, BlockState state) {
        super(ModBE.ALTAR_BE, pos, state);
    }

    public boolean tryCastRitual(){
        CorruptionRitual ritual= new CorruptionRitual(pos,world);
        if (getStack().isOf(Items.ECHO_SHARD)&&ritual.tryStart()){
            ritualId=ritual.id;
            markDirty();
            return true;
        }
        return false;
    }
    public boolean canCastRitual(){
        CorruptionRitual ritual= new CorruptionRitual(pos,world);
        return ritual.hasRitualSite();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt,items);
        ritualId = nbt.getUuid(RITUAL_KEY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.items, true);
        nbt.putUuid(RITUAL_KEY,ritualId);
    }
    public ItemStack getStack() {
        return items.get(0);
    }
    public void setStack(ItemStack stack){
        items.set(0,stack);
    }


    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack playerStack= player.getStackInHand(hand);
        ItemStack stack= this.getStack();
        if (stack.isEmpty()&&playerStack.isOf(Items.ECHO_SHARD)){
            this.setStack(playerStack.split(1));
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        } else if (playerStack.isEmpty() || ItemStack.canCombine(playerStack, stack)) {
            if (hasRitual()){
                player.damage(world.getDamageSources().create(DamageTypes.MAGIC),3f);
                return ActionResult.SUCCESS;
            }
            player.giveItemStack(stack);
            stack.decrement(1);
            setStack(stack);
            markDirtyAndSync();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
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
    public void corrupt(){
        setStack(new ItemStack(ModItems.CORRUPTED_SHARD));
        markDirtyAndSync();
    }

    public void removeRitual() {
        ritualId=NULL_ID;
        markDirty();
    }
    public boolean hasRitual(){
        return !ritualId.equals(NULL_ID);
    }
}
