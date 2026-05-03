package eu.cj4.declarativeui.impl.command.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.action.CommandAction;
import eu.cj4.declarativeui.api.command.action.CommandActionType;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import net.minecraft.resources.Identifier;

public final class CommandActionTypes {
    public static final Codec<CommandAction> TYPED_CODEC;
    public static final CommandActionType FUNCTION;
    public static final CommandActionType OPEN_MENU;

    private static CommandActionType register(String name, MapCodec<? extends CommandAction> mapCodec) {
        return CommandAction.register(Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
    }

    static {
        TYPED_CODEC = DeclarativeUIBuiltInRegistries.COMMAND_ACTION_TYPE.byNameCodec().dispatch(CommandAction::getType, CommandActionType::codec);
        FUNCTION = register("function", FunctionCommandAction.CODEC);
        OPEN_MENU = register("open_menu", OpenMenuCommandAction.CODEC);
    }
}
