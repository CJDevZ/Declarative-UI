package eu.cj4.declarativeui.api.command;

import com.mojang.brigadier.Command;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.command.action.FunctionCommandAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuCommandAction;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public interface CommandAction extends Command<CommandSourceStack> {
    MapCodec<? extends CommandAction> codec();

    static FunctionCommandAction function(Identifier functionId) {
        return new FunctionCommandAction(functionId);
    }

    static OpenMenuCommandAction openMenu(ResourceKey<Menu> menu) {
        return new OpenMenuCommandAction(menu);
    }

    static <T extends CommandAction> MapCodec<T> register(Identifier id, MapCodec<T> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.COMMAND_ACTION_TYPE, id, mapCodec);
    }
}
