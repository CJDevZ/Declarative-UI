package eu.cj4.declarativeui.impl;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import eu.cj4.declarativeui.api.command.DeclaredCommand;
import eu.cj4.declarativeui.api.container.DeclaredContainer;
import eu.cj4.declarativeui.api.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.api.menu.DeclaredMenu;
import eu.cj4.declarativeui.impl.providers.CommandArgumentProviders;
import eu.cj4.declarativeui.impl.providers.ContainerProviders;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DeclarativeUI implements ModInitializer {

    public static final String MOD_ID = "declarative_ui";
    public static final ResourceKey<Registry<DeclaredMenu>> UI_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "menu"));
    public static final ResourceKey<Registry<DeclaredContainer>> CONTAINER_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "container"));
    public static final ResourceKey<Registry<DeclaredCommand>> COMMAND_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "command"));
    private static final DynamicCommandExceptionType ERROR_INVALID_MENU = new DynamicCommandExceptionType((object) -> Component.literal(String.format("Unknown menu: %s", object)));

    @Override
    public void onInitialize() {
        DeclarativeUIBuiltInRegistries.bootStrap();
        DynamicRegistries.register(UI_REGISTRY, DeclaredMenu.DIRECT_CODEC);
        DynamicRegistries.register(CONTAINER_REGISTRY, DeclaredContainer.CODEC);
        DynamicRegistries.register(COMMAND_REGISTRY, DeclaredCommand.CODEC);

        ContainerProviders.bootStrap();
        CommandArgumentProviders.bootStrap();

        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
            var declarativeUI = Commands.literal("declarative_ui").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS));

            var menuCMD = Commands.argument("menu", ResourceKeyArgument.key(DeclarativeUI.UI_REGISTRY)).executes(DeclarativeUI::openMenu);

            declarativeUI.then(Commands.literal("open")
                    .then(Commands.argument("target", EntityArgument.player()).then(menuCMD))
            );

            var declarativeUIContainer = Commands.literal("container");

            declarativeUI.then(declarativeUIContainer);
            dispatcher.register(declarativeUI);

            Set<String> knownCommands = dispatcher.getRoot().getChildren().stream().map(CommandNode::getName).collect(Collectors.toSet());
            buildContext.lookupOrThrow(COMMAND_REGISTRY).listElements().forEach(reference -> {
                String commandName = reference.key().location().toString();

                DeclaredCommand declaredCommand = reference.value();
                var commandBuilder = Commands.literal(commandName);
                var node = registerNode(commandBuilder, buildContext, declaredCommand.function().orElse(null), declaredCommand.nodes(), declaredCommand.permissionLevel().orElse(null));
                var redirect = dispatcher.register((LiteralArgumentBuilder<CommandSourceStack>) node);

                String path = reference.key().location().getPath();
                if (!knownCommands.contains(path)) {
                    // TODO: Fix redirect not working
                    dispatcher.register(Commands.literal(path).redirect(redirect));
                }
            });
        });
    }

    private static <T extends ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T> registerNode(ArgumentBuilder<CommandSourceStack, T> argumentBuilder, CommandBuildContext buildContext, @Nullable ResourceLocation functionResource, List<DeclaredCommand.Node> nodes, @Nullable Integer permissionLevel) {
        if (permissionLevel != null) {
            argumentBuilder.requires(source -> source.hasPermission(permissionLevel));
        }
        if (functionResource != null) {
            System.out.println(argumentBuilder.build().getName());
            argumentBuilder.executes(new DeclaredCommand.CommandFunctionExecutor(functionResource));
        }
        for (DeclaredCommand.Node node : nodes) {
            var registeredNode = node.register(buildContext);
            argumentBuilder.then(registerNode(registeredNode, buildContext, node.function().orElse(null), node.nodes(), node.permissionLevel().orElse(null)));
        }
        return argumentBuilder;
    }

    private static int openMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        ResourceKey<DeclaredMenu> menuResourceKey = ResourceKeyArgument.getRegistryKey(context, "menu", UI_REGISTRY, ERROR_INVALID_MENU);

        CommandSourceStack sourceStack = context.getSource();
        Registry<DeclaredMenu> MENU_REGISTRY = sourceStack.registryAccess().lookupOrThrow(DeclarativeUI.UI_REGISTRY);
        DeclaredMenu menu = MENU_REGISTRY.getValueOrThrow(menuResourceKey);
        menu.open(sourceStack, target);

        return 1;
    }
}
