package eu.cj4.declarativeui.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.command.action.CommandAction;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import eu.cj4.declarativeui.impl.command.action.CommandActionTypes;
import eu.cj4.declarativeui.impl.command.argument.CommandArgumentTypes;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionLevel;
import org.jspecify.annotations.Nullable;

import java.util.*;

public record DeclaredCommand(Optional<CommandAction> action, Map<String, Node> nodes, Optional<PermissionLevel> permissionLevel) {
    public static final Codec<DeclaredCommand> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CommandActionTypes.TYPED_CODEC.optionalFieldOf("action").forGetter(DeclaredCommand::action),
                    Codec.unboundedMap(Codec.STRING, Node.CODEC).optionalFieldOf("nodes", Collections.emptyMap()).forGetter(DeclaredCommand::nodes),
                    Codec.withAlternative(PermissionLevel.CODEC, PermissionLevel.INT_CODEC).optionalFieldOf("permission_level").forGetter(DeclaredCommand::permissionLevel)
            ).apply(instance, DeclaredCommand::new));

    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, String commandName) {
        return dispatcher.register(registerNode(Commands.literal(commandName), buildContext, this.action.orElse(null), this.nodes, this.permissionLevel.orElse(null)));
    }

    private static <T extends ArgumentBuilder<CommandSourceStack, T>> T registerNode(T argumentBuilder, CommandBuildContext buildContext, @Nullable CommandAction commandAction, Map<String, DeclaredCommand.Node> nodes, @Nullable PermissionLevel permissionLevel) {
        if (permissionLevel != null) {
            argumentBuilder.requires(Commands.hasPermission(new PermissionCheck.Require(new Permission.HasCommandLevel(permissionLevel))));
        }
        if (commandAction != null) {
            argumentBuilder.executes(commandAction);
        }
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            T registeredNode = node.register(buildContext, entry.getKey());
            argumentBuilder.then(registerNode(registeredNode, buildContext, node.action.orElse(null), node.nodes, node.permissionLevel.orElse(null)));
        }
        return argumentBuilder;
    }

    public record Node(Optional<CommandArgument<?>> argumentType, Optional<CommandAction> action, Map<String, Node> nodes, Optional<PermissionLevel> permissionLevel) {
        public static final Codec<Node> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance ->
                instance.group(
                        CommandArgumentTypes.TYPED_CODEC.optionalFieldOf("argument").forGetter(Node::argumentType),
                        CommandActionTypes.TYPED_CODEC.optionalFieldOf("action").forGetter(Node::action),
                        Codec.unboundedMap(Codec.STRING, Node.CODEC).optionalFieldOf("nodes", Collections.emptyMap()).forGetter(Node::nodes),
                        Codec.withAlternative(PermissionLevel.CODEC, PermissionLevel.INT_CODEC).optionalFieldOf("permission_level").forGetter(Node::permissionLevel)
                ).apply(instance, Node::new)));

        @SuppressWarnings("unchecked")
        public <T extends ArgumentBuilder<CommandSourceStack, T>> T register(CommandBuildContext buildContext, String name) {
            if (argumentType.isPresent()) {
                return (T) Commands.argument(name, this.argumentType.get().getArgumentType(buildContext));
            } else {
                return (T) Commands.literal(name);
            }
        }
    }
}
