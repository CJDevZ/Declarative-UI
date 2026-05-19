package eu.cj4.declarativeui.api.menu.slot.action;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public record ClickActionType(MapCodec<? extends ClickAction> codec) {
    public static ClickActionType register(ResourceLocation id, MapCodec<? extends ClickAction> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE, id, new ClickActionType(mapCodec));
    }
}
