package net.bobbacon.ritual;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.entity.MetalSupport;
import net.bobbacon.entity.ModEntities;
import net.bobbacon.entity.block_entity.AltarBE;
import net.bobbacon.item.ModItems;
import net.bobbacon.status_effect.ModEffects;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class CorruptionRitual extends Ritual {
    BlockPos pillar1;
    BlockPos pillar2;
    BlockPos pillar3;
    BlockPos pillar4;

    public CorruptionRitual(BlockPos center, World world) {
        super(center,world);
        maxPhases=3;
        definePillars(center);
    }
    public CorruptionRitual(World world, NbtCompound nbt) {
        super(world,nbt);
        maxPhases=3;
    }

    @Override
    protected void tick() {
        if (!started){
            return;
        }
        NightOfTheDead.LOGGER.info("tick in corruption ritual, phase: "+phase);
        if ((time==80&&phase==1)||(time==90&&phase==2)){
            NightOfTheDead.LOGGER.info("triggered by time");

            nextPhase();
        }
//        if (phase==0){
//            return;
//        } else if (phase == 1) {
//            performPhase1();
//        }else if (phase == 2) {
//            performPhase2();
//        }else if (phase == 3) {
//            performPhase3();
//        }
        switch (phase){
            case 0: return;
            case 1: {
                performPhase1();
                break;
            }
            case 2: {
                performPhase2();
                break;
            }
            case 3: {
                performPhase3();
                break;
            }
        }
        super.tick();
    }

    protected void performPhase3() {
        NightOfTheDead.LOGGER.info("perform phase 3");
        if (shouldInitPhase()){
            for (int i = 0; i < 7; i++) {
                SkeletonEntity entity= new SkeletonEntity(EntityType.SKELETON,world);
                BlockPos pos= center.west(Math.round(world.random.nextFloat()*6f-3f)).north(Math.round(world.random.nextFloat()*6f-3f));
                if (world.getBlockState(pos.up()).isSolidBlock(world,pos.up())){
                    pos=pos.north();
                }
                entity.setPos(pos.getX(),pos.getY(),pos.getZ());
                ItemStack stack= Items.NETHERITE_AXE.getDefaultStack();
                stack.addEnchantment(Enchantments.FIRE_ASPECT,2);
                stack.addEnchantment(Enchantments.KNOCKBACK,2);
                entity.equipStack(EquipmentSlot.MAINHAND, stack);
                entity.equipStack(EquipmentSlot.OFFHAND, stack.copy());
                entity.equipStack(EquipmentSlot.CHEST, Items.NETHERITE_CHESTPLATE.getDefaultStack());
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,-1,0,false,false));
//                while (world.getBlockState(pos).isSolidBlock(world,pos)){
//
//                }
                world.spawnEntity(entity);
                RitualManager.get((ServerWorld) world).entityMapping.put(entity.getUuid(),this.id);
            }
            for (int i = 0; i < 3; i++) {
                BlazeEntity blazeEntity=new BlazeEntity(EntityType.BLAZE,world);
                BlockPos pos= center.west(Math.round(world.random.nextFloat()*10f-5f)).north(Math.round(world.random.nextFloat()*10f-5f));
                if (world.getBlockState(pos.up()).isSolidBlock(world,pos.up())){
                    pos=pos.north();
                }
                blazeEntity.setPos(pos.getX(),pos.getY(),pos.getZ());
                EntityAttributeInstance maxHealth =
                        blazeEntity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                EntityAttributeInstance knockback =
                        blazeEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);

                if (maxHealth != null) {
                    maxHealth.setBaseValue(40.0D);
                    blazeEntity.setHealth(40.0F);
                }
                if (knockback!=null){
                    knockback.setBaseValue(5f);
                }
                world.spawnEntity(blazeEntity);
                RitualManager.get((ServerWorld) world).entityMapping.put(blazeEntity.getUuid(),this.id);
            }
            entityCount=10;
        }
        if (time%20==0){
            if (entityCount<=0){
                NightOfTheDead.LOGGER.info("triggered by killing");
                nextPhase();
            }
        }
    }

    protected void performPhase1(){
        if (shouldInitPhase()){
            NightOfTheDead.LOGGER.info("init phase 1");
            List<PlayerEntity> list= world.getEntitiesByType(EntityType.PLAYER,new Box(center.west(8).south(8).down(2), center.east(8).north(8).up(4)), entity -> !entity.isSpectator());
            for (PlayerEntity player: list){
                player.addStatusEffect(new StatusEffectInstance(ModEffects.INSANITY,60,2));
            }
        }
    }
    protected void performPhase2(){
        if (shouldInitPhase()){
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
        boolean bl= true;
        for (int i = -3; i <=3; i++) {
            for (int j = -3; j <= 3; j++) {
                BlockPos pos=center.down().east(i).south(j);
                if (!world.getBlockState(pos).isSolidBlock(world,pos)){
                    NightOfTheDead.LOGGER.info("Block "+pos.toString()+" is not solid");
                    bl=false;
                }
                if (!pos.up().equals(pillar1)&&!pos.up().equals(pillar2)&&!pos.up().equals(pillar3)&&!pos.up().equals(pillar4)&&!pos.up().equals(center)){
                    for (int k = 1; k <4; k++) {
                        BlockPos pos1=pos.up(k);
                        if (!world.getBlockState(pos1).isOf(Blocks.AIR)){
                            NightOfTheDead.LOGGER.info("Block "+pos.toString()+" is not air");
                            bl=false;
                        }
                    }
                }

            }
        }
        NightOfTheDead.LOGGER.info("Air and ground: "+bl);
        return altar && p1 && p2 && p3 && p4 && hasBlood()&&bl;
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
    public AltarBE getAltarBE(){
        if (world.getBlockEntity(center) instanceof AltarBE be){
            return be;
        }
        return null;
    }

    @Override
    protected void complete() {
        super.complete();
        getAltarBE().corrupt();

    }

    @Override
    public void abort() {
        getAltarBE().removeRitual();
        super.abort();
    }
}
