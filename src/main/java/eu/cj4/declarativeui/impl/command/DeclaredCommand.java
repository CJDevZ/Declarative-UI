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
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record DeclaredCommand(Optional<CommandAction> action, List<Node> nodes, Optional<Integer> permissionLevel) {
    public static final Codec<DeclaredCommand> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CommandActionTypes.TYPED_CODEC.optionalFieldOf("action").forGetter(DeclaredCommand::action),
                    Codec.withAlternative(Codec.list(Node.CODEC), Node.CODEC, Collections::singletonList).optionalFieldOf("nodes", Collections.emptyList()).forGetter(DeclaredCommand::nodes),
                    Codec.INT.optionalFieldOf("permission_level").forGetter(DeclaredCommand::permissionLevel)
            ).apply(instance, DeclaredCommand::new));

    public CommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, String commandName) {
        return dispatcher.register(registerNode(Commands.literal(commandName), buildContext, this.action.orElse(null), this.nodes, this.permissionLevel.orElse(null)));
    }

    private static <T extends ArgumentBuilder<CommandSourceStack, T>> T registerNode(T argumentBuilder, CommandBuildContext buildContext, @Nullable CommandAction commandAction, List<DeclaredCommand.Node> nodes, @Nullable Integer permissionLevel) {
        if (permissionLevel != null) {
            argumentBuilder.requires(source -> source.hasPermission(permissionLevel));
        }
        if (commandAction != null) {
            argumentBuilder.executes(commandAction);
        }
        for (DeclaredCommand.Node node : nodes) {
            T registeredNode = node.register(buildContext);
            argumentBuilder.then(registerNode(registeredNode, buildContext, node.action.orElse(null), node.nodes, node.permissionLevel.orElse(null)));
        }
        return argumentBuilder;
    }

    public record Node(String name, Optional<CommandArgument<?>> argumentType, Optional<CommandAction> action, List<Node> nodes, Optional<Integer> permissionLevel) {
        public static final Codec<Node> CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(Node::name),
                        CommandArgumentTypes.TYPED_CODEC.optionalFieldOf("argument").forGetter(Node::argumentType),
                        CommandActionTypes.TYPED_CODEC.optionalFieldOf("action").forGetter(Node::action),
                        Codec.list(Node.CODEC).optionalFieldOf("nodes", Collections.emptyList()).forGetter(Node::nodes),
                        Codec.INT.optionalFieldOf("permission_level").forGetter(Node::permissionLevel)
                ).apply(instance, Node::new)));

        @SuppressWarnings("unchecked")
        public <T extends ArgumentBuilder<CommandSourceStack, T>> T register(CommandBuildContext buildContext) {
            if (argumentType.isPresent()) {
                return (T) Commands.argument(this.name, this.argumentType.get().getArgumentType(buildContext));
            } else {
                return (T) Commands.literal(this.name);
            }
        }
    }
}
