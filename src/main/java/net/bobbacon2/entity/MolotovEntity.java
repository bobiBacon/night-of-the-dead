package net.bobbacon2.entity;

import net.bobbacon2.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class MolotovEntity extends ThrownItemEntity implements FlyingItemEntity {
    public int explosionRadius;

    public MolotovEntity(double d, double e, double f, World world, int explosionRadius) {
        super(ModEntities.MOLOTOV, d, e, f, world);
        this.explosionRadius =explosionRadius;

    }

    public MolotovEntity(LivingEntity livingEntity, World world, int explosionRadius) {
        super(ModEntities.MOLOTOV, livingEntity, world);
        this.explosionRadius =explosionRadius;
    }

    public MolotovEntity(EntityType<? extends MolotovEntity> entityType, World world) {
        super(entityType, world);
    }




    @Override
    protected void onCollision(HitResult hitResult) {
        explode(hitResult.getPos().x,hitResult.getPos().y,hitResult.getPos().z);
    }

    @Override
    protected float getGravity() {
        return 0.05f;
    }
    public void explode(){
        explode(this.getX(),this.getY(),this.getZ());
    }

  public void explode(double colx, double coly, double colz){
        World world = getWorld();
        if (world.isClient){
            return;
        }

        int x= (int) Math.round(colx);
        int y= (int) Math.round(coly);
        int z= (int) Math.round(colz);

        ArrayList<BlockPos> blocks= new ArrayList<>();
        for (float i = 1.5f; i <explosionRadius; i++) {
            for (float j = 1.5f; j < explosionRadius; j++) {

                if (i*i+j*j <= explosionRadius*explosionRadius){
                    int x1 = (int) (x+Math.floor(i));
                    int y1 = (int) (z+Math.floor(j));
                    int x2 = (int) (x-Math.floor(i));
                    int y2 = (int) (z-Math.floor(j));
                    blocks.add(new BlockPos(x1, y,y1));
                    blocks.add(new BlockPos(x2, y,y1));
                    blocks.add(new BlockPos(x1, y,y2));
                    blocks.add(new BlockPos(x2, y,y2));
                }
            }
        }
        for (int i=-explosionRadius+1; i<explosionRadius; i++){
            blocks.add(new BlockPos(x +i, y, z));
            blocks.add(new BlockPos(x, y, z +i));
        }


        for (int i = 0; i < blocks.toArray().length; i++) {
            BlockPos pos= blocks.get(i);
            if (world.getBlockState(pos).isReplaceable()) { // Vérifie si le bloc peut être remplacé
                BlockPos under = new BlockPos(pos.getX(), pos.getY() -1, pos.getZ());
                if (world.getBlockState(under).isReplaceable()){
                    world.setBlockState(under, Blocks.FIRE.getDefaultState(), Block.NOTIFY_ALL);
                }
                world.setBlockState(pos, Blocks.FIRE.getDefaultState(), Block.NOTIFY_ALL);
            } else  {
                BlockPos over = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                if (world.getBlockState(over).isReplaceable()){
                    world.setBlockState(over, Blocks.FIRE.getDefaultState(), Block.NOTIFY_ALL);
                }
            }
        }
        int count = explosionRadius*explosionRadius*2;
        for (int i = 0; i <count; i++) {
            FireDrop fireDrop = new FireDrop((LivingEntity) this.getOwner(),world,colx,coly+1,colz);
            Random rand = new Random();
            int angle = (int) (rand.nextFloat()*360);
            fireDrop.setVelocity(Math.cos(Math.toRadians(angle))+(0.5F*this.getVelocity().getX()), 1.5, Math.sin(Math.toRadians(angle))+(0.5F*this.getVelocity().getZ()), 0.4F*rand.nextFloat()+0.3f, 2.0F);
            world.spawnEntity(fireDrop);

            world.addParticle(ParticleTypes.ASH,this.getX(),this.getY(),this.getZ(),0.3f*this.getVelocity().getX()+(rand.nextFloat()-0.5f)*2f,3,0.3f*this.getVelocity().getZ()+(rand.nextFloat()-0.5f)*2f);

        }

      world.playSound(null,this.getX(),this.getY(),this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,0.5F, world.getRandom().nextFloat() * 0.4F + 1.2F);
      world.playSound(null,this.getX(),this.getY(),this.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS,1.2F, world.getRandom().nextFloat() * 0.4F + 1.2F);
      world.playSound(null,this.getX(),this.getY(),this.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS,1.2F, world.getRandom().nextFloat() * 0.4F + 0.8F);



      this.discard();


    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MOLOTOV;
    }
}
