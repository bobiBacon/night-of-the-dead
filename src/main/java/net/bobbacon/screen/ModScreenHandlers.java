package net.bobbacon.screen;

import net.bobbacon.NightOfTheDead;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<RefiningScreenHandler> REFINERY_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(NightOfTheDead.MOD_ID,"refinery"),
                    new ExtendedScreenHandlerType<>(RefiningScreenHandler::new));
    public static void init(){

    }
}
