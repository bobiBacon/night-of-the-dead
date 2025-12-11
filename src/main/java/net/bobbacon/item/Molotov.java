package net.bobbacon.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;

public class Molotov extends Item {
    public Molotov(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            PotionEntity potionEntity = new MolotovEntity(world, user);
            potionEntity.setItem(itemStack);
            potionEntity.setVelocity(user, user.getPitch(), user.getYaw(), -20.0F, 1F, 1.0F);
            world.spawnEntity(potionEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }
    public static class MolotovEntity extends PotionEntity {
        public MolotovEntity(World world, LivingEntity owner) {
            super(world, owner);
        }

        @Override
        protected void onCollision(HitResult hitResult) {
            explode(hitResult.getPos().x,hitResult.getPos().y,hitResult.getPos().z);
            this.kill();
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

//            BlockPos pos = new BlockPos((int) x, (int) hitResult.getPos().y, (int) hitResult.getPos().z);

//            if (world.getBlockState(pos).isReplaceable()) { // Vérifie si le bloc peut être remplacé
//                world.setBlockState(pos, Blocks.FIRE.getDefaultState(), Block.NOTIFY_ALL);
//            }
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
    }
}

