package eu.cj4.declarativeui.api.command.action;

import com.mojang.brigadier.Command;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.command.action.FunctionCommandAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuCommandAction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public interface CommandAction extends Command<CommandSourceStack> {
    CommandActionType getType();

    static FunctionCommandAction function(Identifier functionId) {
        return new FunctionCommandAction(functionId);
    }

    static OpenMenuCommandAction openMenu(ResourceKey<Menu> menu) {
        return new OpenMenuCommandAction(menu);
    }
}
