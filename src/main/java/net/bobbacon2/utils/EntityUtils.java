package net.bobbacon2.utils;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class EntityUtils {
    public static void addAdditionModifier(PlayerEntity player, EntityAttribute entityAttribute, UUID id, String name, float value) {
        EntityAttributeInstance attribute = player.getAttributeInstance(entityAttribute);

        if (attribute != null && attribute.getModifier(id) == null) {
            attribute.addPersistentModifier(
                    new EntityAttributeModifier(
                            id,
                            name,
                            value,
                            EntityAttributeModifier.Operation.ADDITION
                    )
            );
        }
    }
    public static void removeModifier(PlayerEntity player, EntityAttribute entityAttribute,UUID id){
        EntityAttributeInstance attribute = player.getAttributeInstance(entityAttribute);

        if (attribute != null) {
            attribute.removeModifier(id);
        }
    }
}
