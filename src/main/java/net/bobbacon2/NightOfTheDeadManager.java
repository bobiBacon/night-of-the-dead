package net.bobbacon2;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class NightOfTheDeadManager extends PersistentState {
    private static final String NAME = "night_of_the_dead_data";
    private boolean shouldPlayANightOfTheDead = false;
    private boolean isNightOfTheDead = false;
    private int deathsSinceLastNOTD=0;
    private static final String SHOULD_PLAY_NIGHT_OF_THE_DEAD_KEY= "should_play_notd";
    private static final String IS_NIGHT_OF_THE_DEAD_KEY= "is_notd";
    private static final String DEATHS_SINCE_LAST_NOTD_KEY= "deaths_since_last_notd";

    void setShouldPlayANightOfTheDead(boolean shouldPlayANightOfTheDead) {
        this.shouldPlayANightOfTheDead = shouldPlayANightOfTheDead;
        markDirty();
    }

    void setNightOfTheDead(boolean nightOfTheDead) {
        isNightOfTheDead = nightOfTheDead;
        markDirty();
    }

    boolean ShouldPlayANightOfTheDead() {
        return shouldPlayANightOfTheDead;
    }

    boolean isNightOfTheDead() {
        return isNightOfTheDead;
    }

    public void addDeath() {
        this.deathsSinceLastNOTD++;
        markDirty();
    }
    public void resetDeathCount() {
        this.deathsSinceLastNOTD=0;
        markDirty();
    }

    public int getDeathsSinceLastNOTD() {
        return deathsSinceLastNOTD;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean(IS_NIGHT_OF_THE_DEAD_KEY,isNightOfTheDead);
        nbt.putBoolean(SHOULD_PLAY_NIGHT_OF_THE_DEAD_KEY,shouldPlayANightOfTheDead);
        nbt.putInt(DEATHS_SINCE_LAST_NOTD_KEY,deathsSinceLastNOTD);
        return nbt;
    }

    static NightOfTheDeadManager fromNbt(NbtCompound nbt) {
        NightOfTheDeadManager data = new NightOfTheDeadManager();
        data.isNightOfTheDead = nbt.getBoolean(IS_NIGHT_OF_THE_DEAD_KEY);
        data.shouldPlayANightOfTheDead = nbt.getBoolean(SHOULD_PLAY_NIGHT_OF_THE_DEAD_KEY);
        data.deathsSinceLastNOTD= nbt.getInt(DEATHS_SINCE_LAST_NOTD_KEY);
        return data;
    }
    static NightOfTheDeadManager get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                NightOfTheDeadManager::fromNbt,
                NightOfTheDeadManager::new,
                NAME
        );
    }
}
