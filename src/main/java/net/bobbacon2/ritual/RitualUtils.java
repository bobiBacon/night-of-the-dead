package net.bobbacon2.ritual;

import net.bobbacon2.entity.MetalSupport;
import net.bobbacon2.entity.ModEntities;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class RitualUtils {
    public static List<MetalSupport> getSupportsInArea(World world, BlockPos center, int size){
        return world.getEntitiesByType(ModEntities.METAL_SUPPORT,new Box(center.west(size/2).south(size/2),center.up(size/2).east(size/2).north(size/2)), entity ->true);
    }
}
