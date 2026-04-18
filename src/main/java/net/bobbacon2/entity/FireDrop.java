package net.bobbacon2.entity;

import net.bobbacon2.utils.GeometryUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireDrop extends ProjectileEntity {
    public boolean eternal= false;


    public FireDrop(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    public FireDrop(LivingEntity owner, World world, double x, double y, double z, boolean eternal) {
        super(ModEntities.FIRE_DROP, world);
        this.setPosition(x, y, z);
        this.setOwner(owner);
        setEternal(eternal);

    }

    public void setEternal(boolean eternal) {
        this.eternal = eternal;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        Vec3d vec3d = this.getVelocity();
        getWorld().addParticle(ParticleTypes.FLAME,this.getX(),this.getY(),this.getZ(),0,0.02,0);
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        this.onCollision(hitResult);
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        this.updateRotation();
        float g = 0.99F;
        float h = 0.06F;
        if (this.getWorld().getStatesInBox(this.getBoundingBox()).noneMatch(AbstractBlock.AbstractBlockState::isAir)&&this.getWorld().getStatesInBox(this.getBoundingBox()).noneMatch((state)-> state.isOf(Blocks.FIRE))) {
            this.discard();
        } else if (this.isInsideWaterOrBubbleColumn()) {
            this.discard();
        } else {
            this.setVelocity(vec3d.multiply(0.93F));
            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0, -0.03F, 0.0));
            }

            this.setPosition(d, e, f);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.isFireImmune()) {
            entityHitResult.getEntity().setOnFireFor(eternal? 50:15);
        }
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (hitResult.getType()== HitResult.Type.BLOCK){
            World world = getWorld();
            if (world.isClient){
                return;
            }
            world.playSound(null,this.getX(),this.getY(),this.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS,1.0F, world.getRandom().nextFloat() * 0.4F + 1F);

            BlockHitResult blockHitResult = (BlockHitResult)hitResult; 
            BlockPos pos =new BlockPos((int) Math.floor(hitResult.getPos().x), (int) Math.floor(hitResult.getPos().y), (int) Math.floor(hitResult.getPos().z));
            BlockPos pos2 = pos;
//            switch (blockHitResult.getSide()){
//                case UP -> {
//                    pos2= pos.up();
//                }
//                case DOWN -> {
//                    pos2= pos.down();
//                }
//                case NORTH -> {
//                    pos2= pos.north();
//                }
//                case SOUTH -> {
//                    pos2=pos.south();
//                }
//                case EAST -> {
//                    pos2= pos.east();
//                }
//                case WEST -> {
//                    pos2= pos.west();
//                }
//            }
//            if (world.getBlockState(pos2).isReplaceable()){
//                world.setBlockState(pos2, Blocks.FIRE.getDefaultState());
//            }

            if (world.getBlockState(pos).isReplaceable()) { // Vérifie si le bloc peut être remplacé
                BlockPos under = new BlockPos(pos.getX(), pos.getY() -1, pos.getZ());
                if (world.getBlockState(under).isReplaceable()){
                    GeometryUtils.lightOnFire(under,world,eternal);
                }
                GeometryUtils.lightOnFire(pos,world,eternal);

            } else{
                BlockPos over = pos.up();
                if (world.getBlockState(over).isReplaceable()){
                    GeometryUtils.lightOnFire(over,world,eternal);
                }
            }
        }
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }
}
