package net.bobbacon;

import net.bobbacon.block.ModBlocks;
import net.bobbacon.entity.ModEntities;
import net.bobbacon.entity.block_entity.ModBE;
import net.bobbacon.item.ModItems;
import net.bobbacon.recipe.AlcoholBrewingRecipe;
import net.bobbacon.recipe.AlcoholBrewingRecipeSerializer;
import net.bobbacon.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NightOfTheDead implements ModInitializer {
	public static final String MOD_ID = "night-of-the-dead";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ModItems.init();
		ModEntities.init();
		ModBlocks.init();
		ModBE.init();
		ModRecipes.init();

		FabricDefaultAttributeRegistry.register(
				EntityType.ZOMBIE,
				ZombieEntity.createZombieAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0)
		);
		FabricDefaultAttributeRegistry.register(
				EntityType.HUSK,
				ZombieEntity.createZombieAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0)
		);
		FabricDefaultAttributeRegistry.register(
				EntityType.DROWNED,
				ZombieEntity.createZombieAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0)
		);
		FabricDefaultAttributeRegistry.register(
				EntityType.ZOMBIE_VILLAGER,
				ZombieEntity.createZombieAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
		);
		FabricDefaultAttributeRegistry.register(
				EntityType.SKELETON,
				AbstractSkeletonEntity.createAbstractSkeletonAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
		);
		FabricDefaultAttributeRegistry.register(
				EntityType.STRAY,
				StrayEntity.createAbstractSkeletonAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
		);


		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerWorld world) -> {
			if (entity instanceof CreeperEntity creeper){
				NbtCompound nbt = new NbtCompound();
				nbt.putByte("ExplosionRadius", (byte)8);
				creeper.readCustomDataFromNbt(nbt);
			}
		});


	}
}