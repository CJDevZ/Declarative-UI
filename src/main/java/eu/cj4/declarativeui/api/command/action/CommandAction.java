package eu.cj4.declarativeui.api.command.action;

import com.mojang.brigadier.Command;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.menu.DeclaredMenu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.impl.command.action.FunctionAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuAction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface CommandAction extends Command<CommandSourceStack> {
    CommandActionType getType();

    static FunctionAction function(ResourceLocation functionId) {
        return new FunctionAction(functionId);
    }

    static OpenMenuAction openMenu(ResourceKey<DeclaredMenu> menu) {
        return new OpenMenuAction(menu);
    }
    
    static CommandActionType register(ResourceLocation id, MapCodec<? extends CommandAction> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.COMMAND_ACTION_TYPE, id, new CommandActionType(mapCodec));
    }
}
