package net.bobbacon2.entity.block_entity;

import net.bobbacon2.block.FakeFLuidDrainable;
import net.bobbacon2.block.FakeFluidReceiver;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ManaExtractor extends BlockEntity {
    FakeFLuidDrainable toDrain;
    FakeFluidReceiver receiver;
    public ManaExtractor(BlockPos pos, BlockState state) {
        super(ModBE.MANA_EXTRACTOR, pos, state);
    }
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
    @Override
    @Nullable
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    public static void tick(World world, BlockPos blockPos, BlockState state, ManaExtractor be) {
        if (world.getTime()%40==0&&be.canExtract()){
            be.extract();
        }
    }

    private void extract() {
        receiver.receiveFluid(toDrain.drain(getExtractingPos(),world),world,getReceivingPos());
    }

    private boolean canExtract() {
        BlockPos extraction= getExtractingPos();
        BlockState extractionState = world.getBlockState(extraction);
        BlockPos receiverPos= getReceivingPos();
        BlockState receiverState = world.getBlockState(receiverPos);
        boolean b= false;
        if (extractionState.getBlock() instanceof FakeFLuidDrainable drainable&&
                drainable.canDrain(extraction,world)&&
                receiverState.getBlock() instanceof FakeFluidReceiver receiver&&
                receiver.canReceive(drainable.getFluidThatCanBeDrained(extraction,world),world,receiverPos)){
            b=true;
            toDrain=drainable;
            this.receiver= receiver;
        }else {
            toDrain=null;
            this.receiver=null;
        }
        return b;
    }
    private BlockPos getAdjacentPos(boolean opposite){
        Direction direction = world.getBlockState(pos).get(Properties.HORIZONTAL_FACING);
        switch (opposite?direction.getOpposite():direction){

            case EAST -> {
                return pos.east();
            }
            case WEST -> {
                return pos.west();
            }
            case SOUTH -> {
                return pos.south();
            }
            default -> {
                return pos.north();
            }
        }
    }
    public BlockPos getExtractingPos(){
        return getAdjacentPos(false);
    }
    public BlockPos getReceivingPos(){
        return getAdjacentPos(true);
    }

}
