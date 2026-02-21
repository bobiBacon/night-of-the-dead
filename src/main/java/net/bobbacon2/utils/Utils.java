package net.bobbacon2.utils;

import net.bobbacon2.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<BlockPos> getCircle(BlockPos center, int radius){
        int x= center.getX();
        int y= center.getY();
        int z= center.getZ();

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
        return blocks;
    }
    public static List<BlockPos> getRingForm(BlockPos center, int innerRadius, int outerRadius){
        if (outerRadius==innerRadius){
            return new ArrayList<>();
        }
        if (innerRadius==0){
            return getCircle(center,outerRadius);
        }
        if (outerRadius<innerRadius){
            int i=innerRadius;
            innerRadius= outerRadius;
            outerRadius=i;
        }
        List<BlockPos> inner = getCircle(center,innerRadius);
        List<BlockPos> outer = getCircle(center,outerRadius);
        for (BlockPos pos: inner){
            outer.remove(pos);
        }
        return outer;
    }
    public static void lightOnFire(List<BlockPos> blocks, ServerWorld world, boolean eternal){
        for (int i = 0; i < blocks.toArray().length; i++) {
            BlockPos pos= blocks.get(i);
            if (world.getBlockState(pos).isReplaceable()) {
                BlockPos under = new BlockPos(pos.getX(), pos.getY() -1, pos.getZ());
                if (world.getBlockState(under).isReplaceable()){
                    lightOnFire(under,world,eternal);
                }
                lightOnFire(pos,world,eternal);
            } else  {
                BlockPos over = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                if (world.getBlockState(over).isReplaceable()){
                    lightOnFire(over,world,eternal);
                }
            }
        }
    }
    public static void lightOnFire(BlockPos pos, World world, boolean eternal){
        if (eternal){
            world.setBlockState(pos, ModBlocks.ETERNAL_FIRE.getDefaultState(), Block.NOTIFY_ALL);
        }else {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState(), Block.NOTIFY_ALL);
        }
    }
}
