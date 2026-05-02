package eu.cj4.declarativeui.impl.menu;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.MenuType;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public final class MenuTypes {
    public static final Codec<Menu> TYPED_CODEC = DeclarativeUIBuiltInRegistries.MENU_TYPE.byNameCodec().dispatch(Menu::getType, eu.cj4.declarativeui.api.menu.MenuType::codec);
    public static final MenuType SIMPLE;

    private static MenuType register(String name, MapCodec<? extends Menu> mapCodec) {
        return Menu.register(ResourceLocation.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
    }

    static {
        SIMPLE = register("simple", SimpleMenu.CODEC);
    }
}
