package net.bobbacon.ritual;

import net.bobbacon.NightOfTheDead;
import net.bobbacon.block.ModBlocks;
import net.bobbacon.entity.FireDrop;
import net.bobbacon.entity.MetalSupport;
import net.bobbacon.entity.ModEntities;
import net.bobbacon.entity.block_entity.AltarBE;
import net.bobbacon.item.ModItems;
import net.bobbacon.Accessors.LivingEntityAccessor;
import net.bobbacon.status_effect.ModEffects;
import net.bobbacon.utils.Utils;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CorruptionRitual extends Ritual {
    BlockPos pillar1;
    BlockPos pillar2;
    BlockPos pillar3;
    BlockPos pillar4;
    public List<ArrayList<FireballEntity>> fireballQueue= List.of(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
//    public ArrayList<FireballEntity> fireballQueue= new ArrayList<>();
    public CorruptionRitual(BlockPos center, World world) {
        super(center,world);
        definePillars(center);
        definePhases();
    }
    public CorruptionRitual(World world, NbtCompound nbt) {
        super(world,nbt);
        definePhases();
    }
    public void definePhases(){
        phases.add(new InsanityPhase());
        phases.add(new FirePhase());
        phases.add(new SkeletonPhase());
        phases.add(new InsanityPhase());
        phases.add(new BeatingPhase());
        phases.add(new BossPrePhase());
        phases.add(new BossPhase());
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
        ArrayList<FireballEntity> list= new ArrayList<>();
        world.collectEntitiesByType(EntityType.FIREBALL,new Box(center.up(40).west(25).south(26),center.up(41).east(26).north(25)),(entity)->entity.getVelocity().equals(new Vec3d(0,0,0)),list);
        for (FireballEntity fireball: list){
            fireball.setVelocity(0,-10,0,0.4f,0);
            fireball.powerY=-0.1;
        }
        getAltarBE().corrupt();
    }

    @Override
    public void abort() {
        getAltarBE().removeRitual();
        super.abort();
    }

    public class InsanityPhase implements Phase{

        @Override
        public void start() {
            for (PlayerEntity player: getPlayers()){
                player.addStatusEffect(new StatusEffectInstance(ModEffects.INSANITY,60,2));
            }
        }

        @Override
        public boolean tick(int time) {
            return time >=80;
        }
    }
    public class FirePhase implements Phase{

        @Override
        public void start() {
            lightPillar(pillar1);
            lightPillar(pillar2);
            lightPillar(pillar3);
            lightPillar(pillar4);

            Utils.lightOnFire(Utils.getRingForm(center,8,11),(ServerWorld) world);
        }

        @Override
        public boolean tick(int time) {
            return time>=10;
        }
    }
    public class SkeletonPhase implements Phase{

        @Override
        public void start() {
            for (int i = 0; i < 7; i++) {
                SkeletonEntity entity= new SkeletonEntity(EntityType.SKELETON,world);
                ItemStack stack= Items.NETHERITE_AXE.getDefaultStack();
                stack.addEnchantment(Enchantments.FIRE_ASPECT,2);
                stack.addEnchantment(Enchantments.KNOCKBACK,2);
                entity.equipStack(EquipmentSlot.MAINHAND, stack);
                entity.setEquipmentDropChance(EquipmentSlot.MAINHAND,0);
                entity.equipStack(EquipmentSlot.OFFHAND, stack.copy());
                entity.setEquipmentDropChance(EquipmentSlot.OFFHAND,0);
                entity.equipStack(EquipmentSlot.CHEST, Items.NETHERITE_CHESTPLATE.getDefaultStack());
                entity.setEquipmentDropChance(EquipmentSlot.CHEST,0);
                entity.equipStack(EquipmentSlot.HEAD, Items.NETHERITE_HELMET.getDefaultStack());
                entity.setEquipmentDropChance(EquipmentSlot.HEAD,0);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,-1,0,false,false));
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,30,1,false,false));

                spawnMobInRange(entity,3f,15);
            }
            for (int i = 0; i < 5; i++) {
                BlazeEntity blazeEntity=new BlazeEntity(EntityType.BLAZE,world);

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

                spawnMobInRange(blazeEntity,5f,15);

            }
        }


        @Override
        public boolean tick(int time) {
            if (time%20==0){
                Utils.lightOnFire(Utils.getRingForm(center,8,11),(ServerWorld) world);
                return entityCount <= 0 || !areEntitiesAlive();
            }
            return false;
        }
    }
    public class BeatingPhase implements Phase{

        @Override
        public void start() {

        }

        @Override
        public boolean tick(int time) {
            if (time%60==0){
                beat();
            }else if(time>=210&&time<230) {
                int angle=360*(time-210)/20;
                int angle2=angle+9;
                double z = 5f*Math.sin(Math.toRadians(angle))+center.getZ();
                double x=5f*Math.cos(Math.toRadians(angle))+center.getX();
                double y = center.getY()+4;
                world.playSound(null,x,y,z, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.HOSTILE,0.5F, world.getRandom().nextFloat() * 0.4F + 1.2F);

                ((ServerWorld)world).spawnParticles(ParticleTypes.FLAME,x,y,z,2,0,0,0,0.03);
                ((ServerWorld)world).spawnParticles(ParticleTypes.FLAME,5f*Math.cos(Math.toRadians(angle2))+center.getX(),y,5f*Math.sin(Math.toRadians(angle2))+center.getZ(),2,0,0,0,0.03);

            }
            return time>=239;
        }
        protected void beat(){
            Random rand = new Random();
            double x= center.getX()+0.5;
            double y= center.getY()+1;
            double z= center.getZ()+0.5;
            for (int i = 0; i < 20; i++) {
                ((ServerWorld)world).spawnParticles(ParticleTypes.FLAME,x,y,z,1,10*(rand.nextFloat()-0.5),rand.nextFloat()*0.7+0.3,10*(rand.nextFloat()-0.5),0.03);
            }
            world.playSound(null,x,y,z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE,4F, world.getRandom().nextFloat() * 0.4F + 1.2F);
            world.playSound(null,x,y,z, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.HOSTILE,10F, world.getRandom().nextFloat() * 0.2F + 0.2F);
        }
    }
    public class BossPrePhase implements Phase{
        BlockPos b1= new BlockPos(center.west(4).south(4));
        BlockPos b2= new BlockPos(center.east(4).south(4));
        BlockPos b3= new BlockPos(center.west(4).north(4));
        BlockPos b4= new BlockPos(center.east(4).north(4));
        BlockPos[] bossesPos= {b1,b2,b3,b4};

        @Override
        public void start() {

        }

        @Override
        public boolean tick(int time) {
            if (time>=241){
                return true;
            }
            if (time%60==0){
                new BeatingPhase().beat();
            }
            if ((time&1)==0){
                Random rand = new Random();

                for (BlockPos pos: bossesPos){
                    FireDrop fireDrop = new FireDrop(null,world,pos.getX(),pos.getY()+0.2,pos.getZ());
                    fireDrop.setVelocity(0, 4, 0, 0.4F*rand.nextFloat()+0.3f, 1.5F);
                    world.spawnEntity(fireDrop);
                }
            }
            return false;
        }
    }
    public class BossPhase implements Phase{
        BlockPos b1= new BlockPos(center.west(4).south(4));
        BlockPos b2= new BlockPos(center.east(4).south(4));
        BlockPos b3= new BlockPos(center.west(4).north(4));
        BlockPos b4= new BlockPos(center.east(4).north(4));
        BlockPos[] bossesPos= {b1,b2,b3,b4};
        @Override
        public void start() {
            for (int i = 0; i < 60; i++) {
                spawnFireBall();
            }
            for (BlockPos pos: bossesPos){
                WitherSkeletonEntity skeleton= new WitherSkeletonEntity(EntityType.WITHER_SKELETON,world);
                SkeletonHorseEntity horse= new SkeletonHorseEntity(EntityType.SKELETON_HORSE,world);

                horse.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,-1,0,false,false));
                EntityAttributeInstance speed =
                        horse.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (speed!=null){
                    speed.setBaseValue(0.5f);
                }
                horse.equipStack(EquipmentSlot.CHEST, Items.LEATHER_HORSE_ARMOR.getDefaultStack());
                horse.setEquipmentDropChance(EquipmentSlot.CHEST,0);
                horse.setPersistent();
                horse.setPos(pos.getX(),pos.getY(),pos.getZ());
                ((LivingEntityAccessor)horse).night_of_the_Dead$setComesFromRitual(true);
                world.spawnEntity(horse);

                ItemStack stack= Items.BOW.getDefaultStack();
                stack.addEnchantment(Enchantments.FLAME,2);
                stack.addEnchantment(Enchantments.PUNCH,2);
                skeleton.equipStack(EquipmentSlot.MAINHAND, stack);
                skeleton.equipStack(EquipmentSlot.CHEST, Items.NETHERITE_CHESTPLATE.getDefaultStack());
                skeleton.setEquipmentDropChance(EquipmentSlot.CHEST,0);
                skeleton.equipStack(EquipmentSlot.LEGS, Items.NETHERITE_LEGGINGS.getDefaultStack());
                skeleton.setEquipmentDropChance(EquipmentSlot.LEGS,0);
                skeleton.equipStack(EquipmentSlot.FEET, Items.NETHERITE_BOOTS.getDefaultStack());
                skeleton.setEquipmentDropChance(EquipmentSlot.FEET,0);
                skeleton.equipStack(EquipmentSlot.HEAD, Items.NETHERITE_HELMET.getDefaultStack());
                skeleton.setEquipmentDropChance(EquipmentSlot.HEAD,0);
                skeleton.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE,-1,0,false,false));
                improveFireRate(skeleton);
                spawnMob(skeleton,pos, 50);

                skeleton.startRiding(horse,true);
            }
        }

        @Override
        public boolean tick(int time) {
            if ((time%10)==0){
                if(shootFireBall()){
                    spawnFireBall();
                }
            }
            if (time%20==0){
                return entityCount <= 0 || !areEntitiesAlive();
            }
            return false;
        }
        private void spawnFireBall(){
            Random random= new Random();
            BlockPos pos= center.up(40).north( random.nextInt(-25,26)).east(random.nextInt(-25,26));
            FireballEntity fireball= new FireballEntity(EntityType.FIREBALL,world);
            fireball.explosionPower=2;
            fireball.setPosition(pos.toCenterPos());
            world.spawnEntity(fireball);
        }
        private boolean shootFireBall(){
            Random random= new Random();
            ArrayList<FireballEntity> list= new ArrayList<>();
            world.collectEntitiesByType(EntityType.FIREBALL,new Box(center.up(40).west(25).south(26),center.up(41).east(26).north(25)),(entity)->entity.getVelocity().equals(new Vec3d(0,0,0)),list,1);
            double x= random.nextGaussian();
            double y=-10;
            double z = random.nextGaussian();
            try {
                FireballEntity fireball= list.get(0);
                fireball.setVelocity(x,y,z,0.4f,0);
                fireball.powerX = x * 0.01;
                fireball.powerY = -0.1;
                fireball.powerZ = z * 0.01;
                return true;
            }catch (Exception e){
                NightOfTheDead.LOGGER.error(e.toString());
                return false;
            }

        }
        private static void improveFireRate(AbstractSkeletonEntity skeleton) {

            // Supprime les anciens BowAttackGoal
            skeleton.goalSelector.getGoals().removeIf(
                    entry -> entry.getGoal() instanceof BowAttackGoal
            );

            // Nouveau goal avec cadence plus élevée
            skeleton.goalSelector.add(
                    4,
                    new BowAttackGoal<>(
                            skeleton,
                            1.0D,   // vitesse déplacement
                            10,     // cooldown entre tirs (ticks) 🔥
                            20.0F   // portée
                    )
            );
        }
    }

}
