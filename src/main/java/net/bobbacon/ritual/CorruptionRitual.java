package net.bobbacon.ritual;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.entity.MetalSupport;
import net.bobbacon.entity.ModEntities;
import net.bobbacon.item.ModItems;
import net.bobbacon.status_effect.ModEffects;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class CorruptionRitual extends Ritual {
    BlockPos pillar1;
    BlockPos pillar2;
    BlockPos pillar3;
    BlockPos pillar4;

    public CorruptionRitual(BlockPos center, World world) {
        super(center,world);
        maxPhases=5;
        definePillars(center);
    }
    public CorruptionRitual(World world, NbtCompound nbt) {
        super(world,nbt);
        maxPhases=5;
    }

    @Override
    protected void tick() {
        if (!started){
            return;
        }
        NightOfTheDead.LOGGER.info("tick in corruption ritual, phase: "+phase);
        if (time==80){
            nextPhase();
        }
        switch (phase){
            case 0: return;
            case 1: performPhase1();
            case 2: performPhase2();
        }
        super.tick();
    }
    protected void performPhase1(){
        if (ShouldInitPhase()){
            NightOfTheDead.LOGGER.info("init phase 1");
            List<PlayerEntity> list= world.getEntitiesByType(EntityType.PLAYER,new Box(center.west(8).south(8).down(2), center.east(8).north(8).up(4)), entity -> !entity.isSpectator());
            for (PlayerEntity player: list){
                player.addStatusEffect(new StatusEffectInstance(ModEffects.INSANITY,60,2));
            }
        }
    }
    protected void performPhase2(){
        if (ShouldInitPhase()){
            NightOfTheDead.LOGGER.info("init phase 2");

            lightPillar(pillar1);
            lightPillar(pillar2);
            lightPillar(pillar3);
            lightPillar(pillar4);
        }
    }
    protected void lightPillar(BlockPos base){
        BlockPos pos = base.up(3);
        NightOfTheDead.LOGGER.info("trying to light: " + pos);
        if (world.getBlockState(pos).isReplaceable()){
            world.setBlockState(pos, AbstractFireBlock.getState(world, pos));
            world.playSound(null,pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.HOSTILE,1f,0.8f+world.random.nextFloat()*0.4f);
        }
    }

    protected void definePillars(BlockPos center){
        pillar1= center.east(2).north(2);
        pillar2= center.west(2).north(2);
        pillar3= center.east(2).south(2);
        pillar4= center.west(2).south(2);

    }

    @Override
    public boolean hasRitualSite() {
        NightOfTheDead.LOGGER.info("has ritual site?");

        boolean altar= getAltar().isOf(ModBlocks.ALTAR);
        NightOfTheDead.LOGGER.info("altar? : "+ altar);

        boolean p1= isPillar(pillar1);
        boolean p2= isPillar(pillar2);
        boolean p3= isPillar(pillar3);
        boolean p4= isPillar(pillar4);
        NightOfTheDead.LOGGER.info("pillar 1? : "+ p1);
        NightOfTheDead.LOGGER.info("pillar 2? : "+ p2);
        NightOfTheDead.LOGGER.info("pillar 3? : "+ p3);
        NightOfTheDead.LOGGER.info("pillar 4? : "+ p4);


        return altar && p1 && p2 && p3 && p4 && hasBlood();
    }
    public List<MetalSupport> getSupportsInArea(){
        return world.getEntitiesByType(ModEntities.METAL_SUPPORT,new Box(center.west(3).south(3),center.up(3).east(3).north(3)), entity ->true);
    }
    public boolean hasBlood(){
        NightOfTheDead.LOGGER.info("has blood?");

        List<MetalSupport> list = getSupportsInArea();
        if (list.size()<8){
            NightOfTheDead.LOGGER.info("support number : "+ list.size());
            return false;
        }
        int i=0;
        for (MetalSupport support: getSupportsInArea()){
            if (support.getHeldItemStack().isOf(ModItems.BLOOD_BOTTLE)){
                i++;
            }
        }
        NightOfTheDead.LOGGER.info("blood? : "+ (i>=8));

        return i>=8;

    }
    public boolean isPillar(BlockPos base){
//        NightOfTheDead.LOGGER.info("block 1 : "+ world.getBlockState(base));
//        NightOfTheDead.LOGGER.info("block 2 : "+ world.getBlockState(base.up()));
//        NightOfTheDead.LOGGER.info("block 3 : "+ world.getBlockState(base.up(2)));


        return world.getBlockState(base).isOf(Blocks.POLISHED_BLACKSTONE_BRICKS) &&
                world.getBlockState(base.up()).isOf(Blocks.CHISELED_POLISHED_BLACKSTONE)&&
                world.getBlockState(base.up(2)).isOf(Blocks.NETHERRACK);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        definePillars(this.center);
    }

    public BlockState getAltar(){
        return world.getBlockState(center);
    }
}
