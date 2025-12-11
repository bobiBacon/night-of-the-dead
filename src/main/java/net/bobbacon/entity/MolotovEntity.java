package net.bobbacon.entity;

import net.bobbacon.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class MolotovEntity extends ThrownItemEntity implements FlyingItemEntity {

    public MolotovEntity(double d, double e, double f, World world) {
        super(ModEntities.MOLOTOV, d, e, f, world);
    }

    public MolotovEntity(LivingEntity livingEntity, World world) {
        super(ModEntities.MOLOTOV, livingEntity, world);
    }

    public MolotovEntity(EntityType<? extends MolotovEntity> entityType, World world) {
        super(entityType, world);
    }




    @Override
    protected void onCollision(HitResult hitResult) {
        explode(hitResult.getPos().x,hitResult.getPos().y,hitResult.getPos().z);
        this.discard();
    }
    protected void explode(double colx,double coly,double colz){
        int radius= 4;
        World world = getWorld();
        if (world.isClient){
            return;
        }

        int x= (int) Math.round(colx);
        int y= (int) Math.round(coly);
        int z= (int) Math.round(colz);

        ArrayList<BlockPos> blocks= new ArrayList<>();
        for (float i = 1.5f; i <radius; i++) {
            for (float j = 1.5f; j < radius; j++) {

                if (i*i+j*j <= radius*radius){
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
        for (int i=-radius+1; i<radius; i++){
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

    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.MOLOTOV;
    }
}
