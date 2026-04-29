package eu.cj4.declarativeui.impl.menu.slot.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.action.CommandAction;
import eu.cj4.declarativeui.api.command.action.CommandActionType;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public final class ClickActionTypes {
    public static final Codec<CommandAction> TYPED_CODEC;

    private static ClickActionType register(String name, MapCodec<? extends ClickAction> mapCodec) {
        return ClickAction.register(ResourceLocation.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.COMMAND_ACTION_TYPE.byNameCodec().dispatch(CommandAction::getType, CommandActionType::codec);
    }
}
