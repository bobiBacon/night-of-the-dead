package net.bobbacon;

import net.bobbacon.item.ModItems;
import net.bobbacon.render.entity.EntityRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.util.Identifier;

public class NightOfTheDeadClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRenderers.init();
		ModelPredicateProviderRegistry.register(
				ModItems.MOLOTOV,
				new Identifier("lit"),
				(stack, world, entity, seed) ->
						stack.getOrCreateNbt().getBoolean("lit") ? 1.0F : 0.0F
		);
		ModelPredicateProviderRegistry.register(
				ModItems.FIERY_MOLOTOV,
				new Identifier("lit"),
				(stack, world, entity, seed) ->
						stack.getOrCreateNbt().getBoolean("lit") ? 1.0F : 0.0F
		);
		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex) -> {

					if (NightOfTheDeadClientManager.isNightOfTheDead) {
						return 0xFF3300; // rouge sang
					}
					NightOfTheDead.LOGGER.warn("this is not working");
					return BiomeColors.getWaterColor(world, pos);

				},
				Blocks.WATER,
				Blocks.BUBBLE_COLUMN,
				Blocks.WATER_CAULDRON
		);
		ClientPlayNetworking.registerGlobalReceiver(
				NightOfTheDead.NIGHT_OF_THE_DEAD_PACKET,
				(client, handler, buf, responseSender) -> {
					boolean active = buf.readBoolean();
					client.execute(() -> {
						NightOfTheDeadClientManager.isNightOfTheDead = active;
						MinecraftClient.getInstance().worldRenderer.reload();
					});
				}
		);
	}
}