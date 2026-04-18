package net.bobbacon2.utils;

import net.minecraft.world.World;

public class TimeUtils {
    public static void executeWithTimeInterval(int interval, World world, Runnable runnable){
        if(world.getTime()%60==0){
            runnable.run();
        }
    }
}
