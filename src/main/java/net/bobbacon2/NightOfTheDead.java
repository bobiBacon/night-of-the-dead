package net.bobbacon2;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.bobbacon.TheSpellLibrary;
import net.bobbacon.loot.Predicates;
import net.bobbacon.loot.RandomSpellLootFunction;
import net.bobbacon.spell.SpellSchools;
import net.bobbacon2.block.FakeFluids;
import net.bobbacon2.block.SoulWormTumor;
import net.bobbacon2.components.EvolutionApi;
import net.bobbacon2.components.ModComponents;
import net.bobbacon2.damage.ModDamageTypes;
import net.bobbacon2.enchants.ModEnchantments;
import net.bobbacon2.entity.ModEntities;
import net.bobbacon2.entity.block_entity.ModBE;
import net.bobbacon2.evolution.Evolution;
import net.bobbacon2.evolution.ModEvolutions;
import net.bobbacon2.loot.ModLoot;
import net.bobbacon2.loot.RandomRitualSpellLootFunction;
import net.bobbacon2.recipe.ModRecipes;
import net.bobbacon2.registry.ModRegistries;
import net.bobbacon2.sound.ModSounds;
import net.bobbacon2.spell.ModSpells;
import net.bobbacon2.status_effect.ModEffects;
import net.bobbacon2.block.ModBlocks;
import net.bobbacon2.entity.ModEntities;
import net.bobbacon2.entity.block_entity.ModBE;
import net.bobbacon2.item.ModItems;
import net.bobbacon2.recipe.ModRecipes;
import net.bobbacon2.status_effect.ModEffects;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.*;

public class NightOfTheDead implements ModInitializer {
	public static final String MOD_ID = "night-of-the-dead";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier NIGHT_OF_THE_DEAD_PACKET= Identifier.of(MOD_ID,"night_of_the_dead_packet");

	public static boolean isNightOfTheDead = false;

	public static void setShouldPlayANightOfTheDead(boolean shouldPlayANightOfTheDead,MinecraftServer server) {
		ServerWorld world= server.getWorld(World.OVERWORLD);
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		data.setShouldPlayANightOfTheDead(shouldPlayANightOfTheDead);
		resetDeathCount(server);
	}

	public static void setNightOfTheDead(boolean nightOfTheDead, MinecraftServer server) {
		ServerWorld world= server.getWorld(World.OVERWORLD);
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		data.setNightOfTheDead(nightOfTheDead);
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeBoolean(data.isNightOfTheDead());
		isNightOfTheDead=nightOfTheDead;
		for (ServerPlayerEntity player : world.getPlayers()){
			ServerPlayNetworking.send(player, NIGHT_OF_THE_DEAD_PACKET, buf);
		}
		resetDeathCount(server);
	}
	public static void addDeathToCount(MinecraftServer server){
		ServerWorld world= server.getWorld(World.OVERWORLD);
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		data.addDeath();
		if (data.getDeathsSinceLastNOTD()>=world.getGameRules().getInt(REQUIRED_DEATHS)){
			setShouldPlayANightOfTheDead(true,server);
		}
	}

	public static void resetDeathCount(MinecraftServer server){
		ServerWorld world= server.getWorld(World.OVERWORLD);
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		data.resetDeathCount();
	}


	public static boolean ShouldPlayANightOfTheDead( ServerWorld world) {
		if (!world.getRegistryKey().equals(World.OVERWORLD)){
			return false;
		}
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		return data.ShouldPlayANightOfTheDead();
	}

	public static boolean isNightOfTheDead( ServerWorld world) {
		if (!world.getRegistryKey().equals(World.OVERWORLD)){
			return false;
		}
		NightOfTheDeadManager data = NightOfTheDeadManager.get(world);
		return data.isNightOfTheDead();
	}
	public  static final GameRules.Key<GameRules.IntRule> REQUIRED_DEATHS =
			GameRuleRegistry.register("required_deaths_for_night_of_the_dead", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1));
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		ModRegistries.init();
		ModItems.init();
		ModEntities.init();
		ModBlocks.init();
		ModBE.init();
		ModRecipes.init();
		ModEffects.init();
		ModSpells.init();
		ModDamageTypes.init();
		ModLoot.init();
		ModEnchantments.init();
		ModSounds.init();
		ModEvolutions.init();
		FakeFluids.init();

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			if (!isNightOfTheDead(world)) return;

			if (world.getTime() % 40 != 0) return;

			SoulWormTumor.generate(world);
		});

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

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("nightOfTheDead")
				.then(argument("value", BoolArgumentType.bool()).then(argument("args", StringArgumentType.string())
						.executes(context -> {
							final boolean value = BoolArgumentType.getBool(context, "value");
							final String s = StringArgumentType.getString(context, "args");
							ServerWorld world=context.getSource().getWorld();

							setShouldPlayANightOfTheDead(value,world.getServer());
							context.getSource().sendFeedback(() -> Text.literal("set should play a night of the dead to %b".formatted(value)), true);

							if (s.equals("giveANancientPEDESTAL")&&context.getSource().isExecutedByPlayer()){
								context.getSource().getPlayer().giveItemStack(ModItems.ANCIENT_PEDESTAL.getDefaultStack());
							}

							return 1;
						})))));
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
					literal("evolution")
							.then(argument("player", EntityArgumentType.player())
									.then(argument("value", IdentifierArgumentType.identifier())
											.executes(context -> {
												PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
												Identifier id = IdentifierArgumentType.getIdentifier(context, "value");

												Evolution evolution = EvolutionApi.getOrEmpty(id);

												if (evolution == null) {
													context.getSource().sendError(
															Text.literal("Unknown evolution: " + id)
													);
													return 0;
												}

												EvolutionApi.setEvolution(player, evolution);

												context.getSource().sendFeedback(
														() -> Text.literal(
																"Set evolution of " +
																		player.getName().getString() +
																		" to " +
																		id
														),
														true
												);

												return 1;
											})
									)
							)
			);
		});
		ServerPlayerEvents.AFTER_RESPAWN.register((player,player2,bool)->{
			ModComponents.EVOLUTION_COMPONENT.get(player2).onPlayerRespawn();
		});
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
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (id.equals(new Identifier("minecraft", "entities/villager"))) {

                LootPool.Builder poolBuilder = null;
                try {
                    poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
							.conditionally(RandomChanceWithLootingLootCondition.builder(0.02f,0.02f))
                            .with(ItemEntry.builder(net.bobbacon.item.ModItems.SCROLL))
                            .apply(RandomRitualSpellLootFunction.builder(Predicates.isClericLoot));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }



				tableBuilder.pool(poolBuilder);
		}});
	}
}