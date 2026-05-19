package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.List;

public final class ClickActionTypes {
    public static final Codec<ClickAction> TYPED_CODEC;
    public static final Codec<List<ClickAction>> LIST_CODEC;
    public static final ClickActionType FUNCTION;
    public static final ClickActionType OPEN_MENU;
    public static final ClickActionType CLOSE_MENU;
    public static final ClickActionType REFRESH_MENU;
    public static final ClickActionType REFRESH_MENU_TITLE;

    private static ClickActionType register(String name, MapCodec<? extends ClickAction> mapCodec) {
        return ClickActionType.register(Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.CLICK_ACTION_TYPE.byNameCodec().dispatch(ClickAction::getType, ClickActionType::codec);
        LIST_CODEC = Codec.withAlternative(Codec.list(TYPED_CODEC), TYPED_CODEC, Collections::singletonList);
        FUNCTION = register("function", FunctionClickAction.CODEC);
        OPEN_MENU = register("open_menu", OpenMenuClickAction.CODEC);
        CLOSE_MENU = register("close_menu", CloseMenuClickAction.CODEC);
        REFRESH_MENU = register("refresh_menu", RefreshMenuClickAction.CODEC);
        REFRESH_MENU_TITLE = register("refresh_menu_title", RefreshMenuTitleClickAction.CODEC);
    }
}
