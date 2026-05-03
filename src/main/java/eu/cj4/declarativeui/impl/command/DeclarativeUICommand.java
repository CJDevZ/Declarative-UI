package eu.cj4.declarativeui.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.cj4.declarativeui.mixin.ResourceKeyArgumentAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public final class DeclarativeUICommand {
    public static final DynamicCommandExceptionType ERROR_INVALID_MENU = new DynamicCommandExceptionType((object) -> Component.literal(String.format("Unknown menu: %s", object)));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var declarativeUI = Commands.literal("declarative_ui").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
        var menuCMD = Commands.argument("menu", ResourceKeyArgument.key(DeclarativeUIRegistries.MENU)).executes(DeclarativeUICommand::openMenu);
        declarativeUI.then(Commands.literal("open").then(Commands.argument("targets", EntityArgument.players()).then(menuCMD)));
        dispatcher.register(declarativeUI);
    }

    private static int openMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        Holder.Reference<Menu> menuReference = ResourceKeyArgumentAccessor.callResolveKey(context, "menu", DeclarativeUIRegistries.MENU, ERROR_INVALID_MENU);

        CommandSourceStack sourceStack = context.getSource();
        Menu menu = menuReference.value();
        menu.open(sourceStack, targets);

        return 1;
    }
}
