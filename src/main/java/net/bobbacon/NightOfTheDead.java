package net.bobbacon;

import net.bobbacon.block.ModBlocks;
import net.bobbacon.entity.ModEntities;
import net.bobbacon.entity.block_entity.ModBE;
import net.bobbacon.item.ModItems;
import net.bobbacon.recipe.ModRecipes;
import net.bobbacon.registry.ModRegistries;
import net.bobbacon.spell.SpellType;
import net.bobbacon.status_effect.ModEffects;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NightOfTheDead implements ModInitializer {
	public static final String MOD_ID = "night-of-the-dead";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier NIGHT_OF_THE_DEAD_PACKET= Identifier.of(MOD_ID,"night_of_the_dead_packet");

	public static boolean isNightOfTheDead = false;

	public static void setShouldPlayANightOfTheDead(boolean shouldPlayANightOfTheDead, ServerWorld world) {
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		data.setShouldPlayANightOfTheDead(shouldPlayANightOfTheDead);
	}

	public static void setNightOfTheDead(boolean nightOfTheDead, ServerWorld world) {
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		data.setNightOfTheDead(nightOfTheDead);
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(data.isNightOfTheDead());
		isNightOfTheDead=nightOfTheDead;
		for (ServerPlayerEntity player : world.getPlayers()){
			ServerPlayNetworking.send(player, NIGHT_OF_THE_DEAD_PACKET, buf);
		}
	}

	public static boolean ShouldPlayANightOfTheDead( ServerWorld world) {
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		return data.ShouldPlayANightOfTheDead();
	}

	public static boolean isNightOfTheDead( ServerWorld world) {
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		return data.isNightOfTheDead();
	}


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ModRegistries.init();
		SpellType.init();
		ModItems.init();
		ModEntities.init();
		ModBlocks.init();
		ModBE.init();
		ModRecipes.init();
		ModEffects.init();


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
		FabricDefaultAttributeRegistry.register(
				EntityType.WITHER_SKELETON,
				AbstractSkeletonEntity.createAbstractSkeletonAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0)
		);


		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerWorld world) -> {
			if (entity instanceof CreeperEntity creeper){
				NbtCompound nbt = new NbtCompound();
				nbt.putByte("ExplosionRadius", (byte)6);
				creeper.readCustomDataFromNbt(nbt);
			}
		});
//		ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((BlockEntity::setWorld));
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server)->{
			ServerPlayerEntity player = handler.getPlayer();
			ServerWorld world = player.getServerWorld();

			PacketByteBuf buf = PacketByteBufs.create();
			isNightOfTheDead = isNightOfTheDead(world);
			buf.writeBoolean(isNightOfTheDead);

			ServerPlayNetworking.send(player, NIGHT_OF_THE_DEAD_PACKET, buf);
		});
		EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult)->{
			World world = player.getWorld();
			if (world.isClient){
				return ActionResult.FAIL;
			}
			if (isNightOfTheDead((ServerWorld) world)||(ShouldPlayANightOfTheDead((ServerWorld)world)&&world.getTimeOfDay()<13000)){
				return ActionResult.FAIL;
			}
            return ActionResult.PASS;
        });
	}
}