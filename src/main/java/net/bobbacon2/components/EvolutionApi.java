package net.bobbacon2.components;

import net.bobbacon2.evolution.Evolution;
import net.bobbacon2.evolution.ModEvolutions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class EvolutionApi {
    public static Evolution getEvolution(PlayerEntity player) {
        return getEvolutionComponent(player).getEvolution();
    }

    @NotNull
    private static EvolutionComponent getEvolutionComponent(PlayerEntity player) {
        return ModComponents.EVOLUTION_COMPONENT.get(player);
    }

    public static void setEvolution(PlayerEntity player,Evolution evolution) {
        getEvolutionComponent(player).setEvolution(evolution);
    }
    public static Evolution getOrEmpty(Identifier id){
        Evolution evolution=ModEvolutions.EVOLUTIONS.get(id);
        return evolution==null? ModEvolutions.EMPTY: evolution;
    }
    public static Identifier getId(Evolution evolution){
        Identifier id= ModEvolutions.EVOLUTIONS.getId(evolution);
        return id==null? ModEvolutions.EVOLUTIONS.getId(ModEvolutions.EMPTY): id;
    }
}
