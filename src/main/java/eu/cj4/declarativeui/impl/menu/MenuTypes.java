package eu.cj4.declarativeui.impl.menu;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public final class MenuTypes {
    public static final Codec<Menu> TYPED_CODEC = DeclarativeUIBuiltInRegistries.MENU_TYPE.byNameCodec().dispatch(Menu::codec, c -> c);

    private static void register(String name, MapCodec<? extends Menu> mapCodec) {
        Registry.register(DeclarativeUIBuiltInRegistries.MENU_TYPE, Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
        register("simple", SimpleMenu.MAP_CODEC);
        register("search", SearchMenu.MAP_CODEC);
        register("book", BookMenu.MAP_CODEC);
    }
}
