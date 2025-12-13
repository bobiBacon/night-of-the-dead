package net.bobbacon;

import net.bobbacon.item.ModItems;
import net.bobbacon.render.entity.EntityRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
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
	}
}