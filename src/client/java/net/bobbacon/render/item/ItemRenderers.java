package net.bobbacon.render.item;

import net.bobbacon.item.ModItems;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

public class ItemRenderers {
    public static void init(){
        BuiltinItemRendererRegistry.INSTANCE.register(
                ModItems.SCROLL,
                new ScrollItemRenderer()
        );
    }
}
