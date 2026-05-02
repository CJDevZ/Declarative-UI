package eu.cj4.declarativeui.api.command.action;

import com.mojang.brigadier.Command;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.impl.command.action.FunctionCommandAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuCommandAction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface CommandAction extends Command<CommandSourceStack> {
    CommandActionType getType();

    static FunctionCommandAction function(ResourceLocation functionId) {
        return new FunctionCommandAction(functionId);
    }

    static OpenMenuCommandAction openMenu(ResourceKey<Menu> menu) {
        return new OpenMenuCommandAction(menu);
    }
    
    static CommandActionType register(ResourceLocation id, MapCodec<? extends CommandAction> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.COMMAND_ACTION_TYPE, id, new CommandActionType(mapCodec));
    }
}
