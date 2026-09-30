package eu.cj4.declarativeui.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public final class DeclarativeUICommand {
    public static final DynamicCommandExceptionType ERROR_INVALID_MENU = new DynamicCommandExceptionType((object) -> Component.literal(String.format("Unknown menu: %s", object)));
    private final HolderLookup.RegistryLookup<Menu> MENU_LOOKUP;

    public DeclarativeUICommand(CommandBuildContext buildContext) {
        MENU_LOOKUP = buildContext.lookupOrThrow(DeclarativeUIRegistries.MENU);
    }

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var declarativeUI = Commands.literal("declarative_ui").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
        var menuCMD = Commands.argument("menu", ResourceKeyArgument.key(DeclarativeUIRegistries.MENU)).executes(this::openMenu);
        declarativeUI.then(Commands.literal("open").then(Commands.argument("targets", EntityArgument.players()).then(menuCMD)));
        dispatcher.register(declarativeUI);
    }

    private int openMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        ResourceKey<Menu> menuResourceKey = ResourceKeyArgument.getRegistryKey(context, "menu", DeclarativeUIRegistries.MENU, ERROR_INVALID_MENU);
        Menu menu = MENU_LOOKUP.getOrThrow(menuResourceKey).value();

        menu.open(context.getSource(), targets);

        return 1;
    }
}
