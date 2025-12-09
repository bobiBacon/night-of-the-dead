package net.bobbacon;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
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

		FabricDefaultAttributeRegistry.register(
				EntityType.ZOMBIE,
				ZombieEntity.createZombieAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0)
		);
	}
}