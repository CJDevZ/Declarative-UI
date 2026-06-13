package eu.cj4.declarativeui.impl.command.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.CommandAction;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public final class CommandActionTypes {
    public static final Codec<CommandAction> TYPED_CODEC = DeclarativeUIBuiltInRegistries.COMMAND_ACTION_TYPE.byNameCodec().dispatch(CommandAction::codec, c -> c);

    private static void register(String name, MapCodec<? extends CommandAction> mapCodec) {
        Registry.register(DeclarativeUIBuiltInRegistries.COMMAND_ACTION_TYPE, Identifier.fromNamespaceAndPath(DeclarativeUI.MOD_ID, name), mapCodec);
    }

    public static void bootStrap() {
        register("function", FunctionCommandAction.MAP_CODEC);
        register("open_menu", OpenMenuCommandAction.MAP_CODEC);
    }
}
