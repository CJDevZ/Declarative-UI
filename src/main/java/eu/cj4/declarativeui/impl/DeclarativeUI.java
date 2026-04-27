package eu.cj4.declarativeui.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import eu.cj4.declarativeui.api.command.DeclaredCommand;
import eu.cj4.declarativeui.api.command.ItemCommands;
import eu.cj4.declarativeui.api.container.DeclaredContainer;
import eu.cj4.declarativeui.api.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.api.menu.DeclaredMenu;
import eu.cj4.declarativeui.api.registry.DeclarativeUIRegistries;
import eu.cj4.declarativeui.impl.providers.CommandArgumentProviders;
import eu.cj4.declarativeui.impl.providers.ContainerProviders;
import eu.cj4.declarativeui.impl.providers.SlotProviders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeclarativeUI implements ModInitializer {

    public static final String MOD_ID = "declarative_ui";

    private static final DynamicCommandExceptionType ERROR_INVALID_MENU = new DynamicCommandExceptionType((object) -> Component.literal(String.format("Unknown menu: %s", object)));
    public static final DynamicCommandExceptionType ERROR_INVALID_CONTAINER = new DynamicCommandExceptionType((object) -> Component.literal(String.format("Unknown container: %s", object)));

    @Override
    public void onInitialize() {
        DeclarativeUIBuiltInRegistries.bootStrap();
        DynamicRegistries.register(DeclarativeUIRegistries.MENU_REGISTRY, DeclaredMenu.DIRECT_CODEC);
        DynamicRegistries.register(DeclarativeUIRegistries.CONTAINER_REGISTRY, DeclaredContainer.CODEC);
        DynamicRegistries.register(DeclarativeUIRegistries.COMMAND_REGISTRY, DeclaredCommand.CODEC);

        SlotProviders.bootStrap();
        ContainerProviders.bootStrap();
        CommandArgumentProviders.bootStrap();

        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
            var declarativeUI = Commands.literal("declarative_ui").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));
            var menuCMD = Commands.argument("menu", ResourceKeyArgument.key(DeclarativeUIRegistries.MENU_REGISTRY)).executes(DeclarativeUI::openMenu);
            declarativeUI.then(Commands.literal("open").then(Commands.argument("targets", EntityArgument.players()).then(menuCMD)));
            dispatcher.register(declarativeUI);

            var itemCommand = dispatcher.getRoot().getChild("item");
            var itemModify = itemCommand.getChild("modify");
            var itemReplace = itemCommand.getChild("replace");
            ItemCommands.register(buildContext, itemModify, itemReplace);

            Set<String> knownCommands = dispatcher.getRoot().getChildren().stream().map(CommandNode::getName).collect(Collectors.toSet());
            buildContext.lookupOrThrow(DeclarativeUIRegistries.COMMAND_REGISTRY).listElements().forEach(reference -> {
                String commandName = reference.key().location().toString();

                DeclaredCommand declaredCommand = reference.value();
                var node = registerNode(Commands.literal(commandName), buildContext, declaredCommand.function().orElse(null), declaredCommand.nodes(), declaredCommand.permissionLevel().orElse(null));
                var redirect = dispatcher.register(node);

                String path = reference.key().location().getPath();
                if (!knownCommands.contains(path)) {
                    knownCommands.add(path);
                    dispatcher.register(Commands.literal(path).redirect(redirect).executes(redirect.getCommand()));
                }
            });
        });
    }

    private static <T extends ArgumentBuilder<CommandSourceStack, T>> T registerNode(T argumentBuilder, CommandBuildContext buildContext, @Nullable ResourceLocation functionResource, List<DeclaredCommand.Node> nodes, @Nullable Integer permissionLevel) {
        if (permissionLevel != null) {
            argumentBuilder.requires(source -> source.hasPermission(permissionLevel));
        }
        if (functionResource != null) {
            System.out.println(argumentBuilder.build().getName());
            argumentBuilder.executes(new DeclaredCommand.CommandFunctionExecutor(functionResource));
        }
        for (DeclaredCommand.Node node : nodes) {
            T registeredNode = node.register(buildContext);
            argumentBuilder.then(registerNode(registeredNode, buildContext, node.function().orElse(null), node.nodes(), node.permissionLevel().orElse(null)));
        }
        return argumentBuilder;
    }

    private static int openMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
        ResourceKey<DeclaredMenu> menuResourceKey = ResourceKeyArgument.getRegistryKey(context, "menu", DeclarativeUIRegistries.MENU_REGISTRY, ERROR_INVALID_MENU);

        CommandSourceStack sourceStack = context.getSource();
        Registry<DeclaredMenu> MENU_REGISTRY = sourceStack.registryAccess().lookupOrThrow(DeclarativeUIRegistries.MENU_REGISTRY);
        DeclaredMenu menu = MENU_REGISTRY.getValueOrThrow(menuResourceKey);
        menu.open(sourceStack, targets);

        return 1;
    }
}
