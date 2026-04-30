package net.bobbacon2.ritual;

import net.bobbacon.ritual.Phase;
import net.bobbacon.ritual.Ritual;
import net.bobbacon2.NightOfTheDeadDataGenerator;
import net.bobbacon2.block.ModBlocks;
import net.bobbacon2.entity.block_entity.BloodPool;
import net.bobbacon2.item.ModItems;
import net.bobbacon2.status_effect.ModEffects;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BasicBloodRitual extends  ItemAffectingRitual {
    public BasicBloodRitual(BlockPos center, World world) {
        super(center, world);
    }

    public BasicBloodRitual(World world, NbtCompound nbt) {
        super(world, nbt);
    }
    public void definePhases(){
        phases.add(new InsanityPhase());
        phases.add(new WitherPhase());
    }

    @Override
    public Ritual create(World world, NbtCompound nbtCompound) {
        return new BasicBloodRitual(world,nbtCompound);
    }

    @Override
    public boolean hasRitualSite() {

        boolean ground=true;
//        for (int i = -2; i <3 ; i++) {
//            for (int j = -2; j < 3; j++) {
//                BlockPos pos= center.down().east(i).north(j);
//                if (!world.getBlockState(pos).isOf(Blocks.DEEPSLATE_TILES)){
//                    ground=false;
//                }
//            }
//        }
        int pillarCount=0;
        if (isPillar(center.north(2).east(2)))pillarCount++;
        if (isPillar(center.north(2).west(2)))pillarCount++;
        if (isPillar(center.south(2).west(2)))pillarCount++;
        if (isPillar(center.south(2).east(2)))pillarCount++;

        boolean hasWall=false;
        if (isWall(center.north(2)))hasWall=true;
        if (isWall(center.west(2)))hasWall=true;
        if (isWall(center.south(2)))hasWall=true;
        if (isWall(center.east(2)))hasWall=true;

        int poolCount=getPools().size();



        return pillarCount>=2&&hasWall&& searchInArea(Blocks.SKELETON_SKULL)>=2 && searchInArea(Blocks.CANDLE)>=2 && poolCount>=2;
    }
    protected ArrayList<BloodPool> getPools(){
        ArrayList<BloodPool> pools= new ArrayList<>();
        BlockEntity blockEntity1 = world.getBlockEntity(center.north());
        if (blockEntity1 instanceof BloodPool pool && pool.getLevel()>=3)pools.add(pool);
        BlockEntity blockEntity2 = world.getBlockEntity(center.west());
        if (blockEntity2 instanceof BloodPool pool && pool.getLevel()>=3)pools.add(pool);
        BlockEntity blockEntity3 = world.getBlockEntity(center.south());
        if (blockEntity3 instanceof BloodPool pool && pool.getLevel()>=3)pools.add(pool);
        BlockEntity blockEntity4 = world.getBlockEntity(center.east());
        if (blockEntity4 instanceof BloodPool pool && pool.getLevel()>=3)pools.add(pool);
        return pools;
    }
    protected int searchInArea(Block block){
        int count=0;
        for (int i = -2; i <3 ; i++) {
            for (int j = -2; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    BlockPos pos=center.north(i).east(j).up(k);
                    if (world.getBlockState(pos).isOf(block)){
                        count++;
                    }
                }
            }
        }
        return count;
    }
    protected boolean isPillar(BlockPos pos){
        BlockPos up= pos.up();
        int stairsCount=0;
        if (world.getBlockState(pos.north()).isOf(Blocks.DEEPSLATE_BRICK_STAIRS))stairsCount++;
        if (world.getBlockState(pos.west()).isOf(Blocks.DEEPSLATE_BRICK_STAIRS))stairsCount++;
        if (world.getBlockState(pos.south()).isOf(Blocks.DEEPSLATE_BRICK_STAIRS))stairsCount++;
        if (world.getBlockState(pos.east()).isOf(Blocks.DEEPSLATE_BRICK_STAIRS))stairsCount++;
        return world.getBlockState(pos).isOf(Blocks.DEEPSLATE_BRICKS)&&world.getBlockState(up).isOf(Blocks.DEEPSLATE_BRICKS)&&stairsCount>=2;
    }
    protected boolean isWall(BlockPos pos){
        if (!world.getBlockState(pos).isOf(Blocks.DEEPSLATE_BRICKS)||!world.getBlockState(pos.up()).isOf(Blocks.DEEPSLATE_BRICKS)){
            return false;
        }
        int count=0;
        if (world.getBlockState(pos.north()).isOf(Blocks.DEEPSLATE_BRICKS))count++;
        if (world.getBlockState(pos.west()).isOf(Blocks.DEEPSLATE_BRICKS))count++;
        if (world.getBlockState(pos.south()).isOf(Blocks.DEEPSLATE_BRICKS))count++;
        if (world.getBlockState(pos.east()).isOf(Blocks.DEEPSLATE_BRICKS))count++;
        return count>=2;
    }

    @Override
    public boolean canAffect(ItemStack stack) {
        return stack.isIn(NightOfTheDeadDataGenerator.MyTagGenerator.CAN_USE_IN_BLOOD_RITUAL);
    }

    @Override
    public ItemStack applyEffect(ItemStack stack) {
        if (stack.isOf(Items.NETHERITE_INGOT)){
            return ModItems.VAMPIRITE.getDefaultStack();
        }
        return ItemStack.EMPTY;
    }
    public class InsanityPhase implements Phase {

        @Override
        public void start() {
            for (PlayerEntity player: getPlayers()){
                player.addStatusEffect(new StatusEffectInstance(ModEffects.INSANITY,60,2));
            }
            getPools().forEach(BloodPool::empty);
        }

        @Override
        public boolean tick(int time) {
            return time >=80;
        }
    }
    public class WitherPhase implements Phase {

        @Override
        public void start() {

        }

        @Override
        public boolean tick(int i) {
            if (i%80==0){
                getPlayers().forEach(player ->{
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER,80,4));
                });
            }
            return i>=320;
        }
    }
}
