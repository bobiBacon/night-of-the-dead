package net.bobbacon2.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.bobbacon.spell.Mana;
import net.bobbacon.spell.SpellRegistry;
import net.bobbacon.spell.SpellSchool;
import net.bobbacon2.evolution.Evolution;
import net.bobbacon2.evolution.ModEvolutions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class EvolutionComponent implements AutoSyncedComponent, ServerTickingComponent {

    private final PlayerEntity player;
    private Evolution evolution= ModEvolutions.EMPTY;

    public EvolutionComponent(PlayerEntity player) {
        this.player = player;
    }


     Evolution getEvolution() {

        return evolution;
    }

     void setEvolution(@NotNull Evolution evolution) {
        this.evolution.onRemove(player);
        this.evolution = evolution;
        evolution.onApply(player);
     }

    @Override
    public void readFromNbt(NbtCompound tag) {
        Identifier evolutionId= Identifier.tryParse(tag.getString("evolutionId"));
        evolution= EvolutionApi.getOrEmpty(evolutionId);


    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putString("evolutionId",EvolutionApi.getId(evolution).toString());
    }

    @Override
    public void serverTick() {
        evolution.tick(player);
    }
    public void onPlayerRespawn(){
        evolution.onRespawn(player);
    }
}
