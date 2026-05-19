package eu.cj4.declarativeui.api.menu;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public record MenuType(MapCodec<? extends Menu> codec) {
    public static MenuType register(ResourceLocation id, MapCodec<? extends Menu> codec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.MENU_TYPE, id, new MenuType(codec));
    }
}
