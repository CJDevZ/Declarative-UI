package eu.cj4.declarativeui.impl.command.action;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.action.CommandAction;
import eu.cj4.declarativeui.api.command.action.CommandActionType;
import eu.cj4.declarativeui.impl.menu.DeclaredMenu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collections;

public record OpenMenuAction(ResourceKey<DeclaredMenu> menu) implements CommandAction {
    public static final MapCodec<OpenMenuAction> CODEC = ResourceKey.codec(DeclarativeUIRegistries.MENU_REGISTRY).fieldOf("menu").xmap(OpenMenuAction::new, OpenMenuAction::menu);

    @Override
    public CommandActionType getType() {
        return CommandActionTypes.OPEN_MENU;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack sourceStack = context.getSource();
        ServerPlayer player = sourceStack.getPlayerOrException();
        Registry<DeclaredMenu> MENU_REGISTRY = sourceStack.registryAccess().lookupOrThrow(DeclarativeUIRegistries.MENU_REGISTRY);
        DeclaredMenu menu = MENU_REGISTRY.getValueOrThrow(this.menu);
        menu.open(sourceStack, Collections.singletonList(player));
        return 1;
    }
}
