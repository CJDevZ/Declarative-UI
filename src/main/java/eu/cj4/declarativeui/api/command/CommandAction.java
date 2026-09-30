package eu.cj4.declarativeui.api.command;

import com.mojang.brigadier.Command;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.command.action.FunctionCommandAction;
import eu.cj4.declarativeui.impl.command.action.OpenMenuCommandAction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

public interface CommandAction extends Command<CommandSourceStack> {
    MapCodec<? extends CommandAction> codec();

    static FunctionCommandAction function(Identifier functionId) {
        return new FunctionCommandAction(functionId);
    }

    static OpenMenuCommandAction openMenu(Holder<Menu> menu) {
        return new OpenMenuCommandAction(menu);
    }
}
