package net.bobbacon2.entity.block_entity;

import net.bobbacon.particles.ModParticles;
import net.bobbacon2.NightOfTheDeadDataGenerator;
import net.bobbacon2.block.FakeFluid;
import net.bobbacon2.block.FakeFluidInstance;
import net.bobbacon2.block.FakeFluidReceiver;
import net.bobbacon2.block.FakeFluids;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BloodPool extends BlockEntity implements FakeFluidReceiver {
    protected FakeFluidInstance fakeFluidInstance= new FakeFluidInstance(FakeFluids.EMPTY,0);
    public BloodPool(BlockPos pos, BlockState state) {
        super(ModBE.BLOOD_POOL, pos, state);
    }

    public float getLevel() {
        return Math.min(fakeFluidInstance.amount,3);
    }
    public int getColor() {
        return fakeFluidInstance.fluid.color;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stackInHand = player.getStackInHand(hand);
        if (stackInHand.isIn(NightOfTheDeadDataGenerator.MyTagGenerator.SOLUBLE)&&!fakeFluidInstance.isEmpty()){
            if (fakeFluidInstance.fluid==FakeFluids.CRUSHED_SOULS&&stackInHand.isOf(ModItems.VAMPIRITE)){
                fakeFluidInstance.transform(FakeFluids.LIQUID_EVIL);
                stackInHand.decrement(1);
                player.setStackInHand(hand,stackInHand);
                if (!world.isClient()){
                    ((ServerWorld)world).spawnParticles(ParticleTypes.SMOKE,pos.getX(),pos.getY(),pos.getZ(),8,1,0.4f,1,0);
                }
                world.playSound(player,pos,SoundEvents.BLOCK_FIRE_EXTINGUISH,SoundCategory.BLOCKS,1,0.8f);
                markDirtyAndSync();
                return ActionResult.SUCCESS;
            }
        }
        if (stackInHand.isOf(ModItems.BLOOD_BOTTLE)&& (fakeFluidInstance.isEmpty()||fakeFluidInstance.fluid==FakeFluids.BLOOD)&& fakeFluidInstance.amount<3){
            fakeFluidInstance= new FakeFluidInstance(FakeFluids.BLOOD,fakeFluidInstance.amount+1);
            stackInHand.decrement(1);
            player.setStackInHand(hand,stackInHand);
            player.giveItemStack(Items.GLASS_BOTTLE.getDefaultStack());
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.5F);

            markDirtyAndSync();

            return ActionResult.SUCCESS;
        }else if (stackInHand.isOf(Items.GLASS_BOTTLE)&& !fakeFluidInstance.isEmpty() && fakeFluidInstance.fluid==FakeFluids.BLOOD){
            fakeFluidInstance.amount--;
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
        fakeFluidInstance= new FakeFluidInstance(FakeFluids.EMPTY,0);
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
        float level = nbt.getFloat("level");
        Identifier id= Identifier.tryParse(nbt.getString("fluid_id"));
        FakeFluid fakeFluid= FakeFluids.FAKE_FLUIDS.get(id);
        fakeFluidInstance=new FakeFluidInstance(fakeFluid,level);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putFloat("level",fakeFluidInstance.amount);
        nbt.putString("fluid_id", String.valueOf(FakeFluids.FAKE_FLUIDS.getId(fakeFluidInstance.fluid)));
    }

    @Override
    public void receiveFluid(FakeFluidInstance fakeFluidInstance, World world, BlockPos pos) {
        this.fakeFluidInstance=fakeFluidInstance;
        markDirtyAndSync();
    }

    @Override
    public boolean canReceive(FakeFluidInstance fakeFluidInstance, World world, BlockPos pos) {
        return (getLevel()<3 && fakeFluidInstance.fluid==this.fakeFluidInstance.fluid)||this.fakeFluidInstance.isEmpty();
    }

    @Override
    public FakeFluidInstance getFluid(World world, BlockPos pos) {
        return fakeFluidInstance;
    }
}
