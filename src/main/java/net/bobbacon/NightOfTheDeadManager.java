package net.bobbacon;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class NightOfTheDeadManager extends PersistentState {
    private static final String NAME = "night_of_the_dead_data";
    private boolean shouldPlayANightOfTheDead = false;
    private boolean isNightOfTheDead = false;
    private static final String SHOULD_PLAY_NIGHT_OF_THE_DEAD_KEY= "should_play_notd";
    private static final String IS_NIGHT_OF_THE_DEAD_KEY= "is_notd";

    public void setShouldPlayANightOfTheDead(boolean shouldPlayANightOfTheDead) {
        this.shouldPlayANightOfTheDead = shouldPlayANightOfTheDead;
        markDirty();
    }

    public void setNightOfTheDead(boolean nightOfTheDead) {
        isNightOfTheDead = nightOfTheDead;
        markDirty();
    }

    public boolean ShouldPlayANightOfTheDead() {
        return shouldPlayANightOfTheDead;
    }

    public boolean isNightOfTheDead() {
        return isNightOfTheDead;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean(IS_NIGHT_OF_THE_DEAD_KEY,isNightOfTheDead);
        nbt.putBoolean(SHOULD_PLAY_NIGHT_OF_THE_DEAD_KEY,shouldPlayANightOfTheDead);
        return nbt;
    }

    public static NightOfTheDeadManager fromNbt(NbtCompound nbt) {
        NightOfTheDeadManager data = new NightOfTheDeadManager();
        data.isNightOfTheDead = nbt.getBoolean(IS_NIGHT_OF_THE_DEAD_KEY);
        data.shouldPlayANightOfTheDead = nbt.getBoolean(SHOULD_PLAY_NIGHT_OF_THE_DEAD_KEY);
        return data;
    }
    public static NightOfTheDeadManager get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                NightOfTheDeadManager::fromNbt,
                NightOfTheDeadManager::new,
                NAME
        );
    }
}
