package eu.cj4.declarativeui.api.menu.slot.action;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public record ClickActionType(MapCodec<? extends ClickAction> codec) {
    public static ClickActionType register(Identifier id, MapCodec<? extends ClickAction> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE, id, new ClickActionType(mapCodec));
    }
}
