package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.List;

public final class ClickActionTypes {
    public static final Codec<ClickAction> TYPED_CODEC;
    public static final Codec<List<ClickAction>> LIST_CODEC;

    private static void register(String name, MapCodec<? extends ClickAction> mapCodec) {
        Registry.register(DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE, Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
        register("function", FunctionClickAction.MAP_CODEC);
        register("open_menu", OpenMenuClickAction.MAP_CODEC);
        register("close_menu", CloseMenuClickAction.MAP_CODEC);
        register("refresh_menu", RefreshMenuClickAction.MAP_CODEC);
        register("refresh_menu_title", RefreshMenuTitleClickAction.MAP_CODEC);
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE.byNameCodec().dispatch(ClickAction::codec, c -> c);
        LIST_CODEC = Codec.withAlternative(Codec.list(TYPED_CODEC), TYPED_CODEC, Collections::singletonList);
    }
}
