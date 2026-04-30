package eu.cj4.declarativeui.impl;

import com.mojang.brigadier.tree.CommandNode;
import eu.cj4.declarativeui.impl.command.DeclaredCommand;
import eu.cj4.declarativeui.impl.command.action.CommandActionTypes;
import eu.cj4.declarativeui.impl.command.DeclarativeUICommand;
import eu.cj4.declarativeui.impl.command.ItemCommands;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.impl.menu.slot.action.ClickActionTypes;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import eu.cj4.declarativeui.impl.menu.DeclaredMenu;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIRegistries;
import eu.cj4.declarativeui.impl.command.argument.CommandArgumentTypes;
import eu.cj4.declarativeui.impl.container.provider.ContainerProviders;
import eu.cj4.declarativeui.impl.menu.slot.provider.SlotProviders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.commands.Commands;

import java.util.Set;
import java.util.stream.Collectors;

public class DeclarativeUI implements ModInitializer {

    public static final String MOD_ID = "declarative_ui";

    @Override
    public void onInitialize() {
        DeclarativeUIBuiltInRegistries.bootStrap();
        DynamicRegistries.register(DeclarativeUIRegistries.MENU_REGISTRY, DeclaredMenu.DIRECT_CODEC);
        DynamicRegistries.register(DeclarativeUIRegistries.CONTAINER_REGISTRY, DeclaredContainer.CODEC);
        DynamicRegistries.register(DeclarativeUIRegistries.COMMAND_REGISTRY, DeclaredCommand.CODEC);

        SlotProviders.bootStrap();
        ContainerProviders.bootStrap();
        CommandArgumentTypes.bootStrap();
        CommandActionTypes.bootStrap();
        ClickActionTypes.bootStrap();

        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) -> {
            DeclarativeUICommand.register(dispatcher);
            ItemCommands.register(dispatcher, buildContext);

            Set<String> knownCommands = dispatcher.getRoot().getChildren().stream().map(CommandNode::getName).collect(Collectors.toSet());
            buildContext.lookupOrThrow(DeclarativeUIRegistries.COMMAND_REGISTRY).listElements().forEach(reference -> {
                String commandName = reference.key().location().toString();
                var node = reference.value().register(dispatcher, buildContext, commandName);

                String path = reference.key().location().getPath();
                if (!knownCommands.contains(path)) {
                    knownCommands.add(path);
                    dispatcher.register(Commands.literal(path).redirect(node).executes(node.getCommand()));
                }
            });
        });
    }
}
