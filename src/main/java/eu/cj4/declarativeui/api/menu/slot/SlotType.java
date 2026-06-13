package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public record SlotType(MapCodec<? extends Slot> codec) {
    public static SlotType register(Identifier id, MapCodec<? extends Slot> codec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.SLOT_TYPE, id, new SlotType(codec));
    }
}
