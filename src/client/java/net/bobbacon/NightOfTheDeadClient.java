package net.bobbacon;

import net.bobbacon.render.entity.EntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class NightOfTheDeadClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRenderers.init();
	}
}