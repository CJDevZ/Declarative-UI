package eu.cj4.declarativeui.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.providers.CommandArgumentProviders;
import eu.cj4.declarativeui.mixin.CommandContextAccessor;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CustomCommandExecutor;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.FunctionCommand;

import java.util.*;

public record DeclaredCommand(Optional<ResourceLocation> function, List<Node> nodes, Optional<Integer> permissionLevel) {
    public static final Codec<DeclaredCommand> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("function").forGetter(DeclaredCommand::function),
                    Codec.withAlternative(Codec.list(Node.CODEC), Node.CODEC, Collections::singletonList).optionalFieldOf("nodes", Collections.emptyList()).forGetter(DeclaredCommand::nodes),
                    Codec.INT.optionalFieldOf("permission_level").forGetter(DeclaredCommand::permissionLevel)
            ).apply(instance, DeclaredCommand::new));

    public static class CommandFunctionExecutor extends CustomCommandExecutor.WithErrorHandling<CommandSourceStack> implements CustomCommandExecutor.CommandAdapter<CommandSourceStack> {
        private final ResourceLocation function;

        public CommandFunctionExecutor(ResourceLocation function) {
            this.function = function;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void runGuarded(CommandSourceStack sourceStack, ContextChain<CommandSourceStack> contextChain, ChainModifiers chainModifiers, ExecutionControl<CommandSourceStack> executionControl) throws CommandSyntaxException {
            sourceStack = sourceStack.withSuppressedOutput();
            CommandContext<CommandSourceStack> commandContext = contextChain.getTopContext().copyFor(sourceStack);
            Optional<CommandFunction<CommandSourceStack>> function = sourceStack.getServer().getFunctions().get(this.function);
            if (function.isPresent()) {
                var arguments = ((CommandContextAccessor<CommandSourceStack>) commandContext).getArguments();
                CompoundTag compoundTag = null;
                if (!arguments.isEmpty()) {
                    compoundTag = new CompoundTag();
                    String input = commandContext.getInput();
                    for (Map.Entry<String, ParsedArgument<CommandSourceStack, ?>> entry : arguments.entrySet()) {
                        compoundTag.putString(entry.getKey(), entry.getValue().getRange().get(input));
                    }
                }

                CommandSourceStack commandSourceStack2 = FunctionCommand.modifySenderForExecution(sourceStack);
                FunctionCommand.queueFunctions(Collections.singletonList(function.get()), compoundTag, sourceStack, commandSourceStack2, executionControl, null, chainModifiers);
            }
        }
    }

    public record Node(String name, Optional<ArgumentType<?>> argumentType, Optional<ResourceLocation> function, List<Node> nodes, Optional<Integer> permissionLevel) {
        public static final Codec<Node> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("name").forGetter(Node::name),
                        CommandArgumentProviders.TYPED_CODEC.optionalFieldOf("argument").forGetter(Node::argumentType),
                        ResourceLocation.CODEC.optionalFieldOf("function").forGetter(Node::function),
                        Codec.list(Node.CODEC).optionalFieldOf("nodes", Collections.emptyList()).forGetter(Node::nodes),
                        Codec.INT.optionalFieldOf("permission_level").forGetter(Node::permissionLevel)
                ).apply(instance, Node::new));

        @SuppressWarnings("unchecked")
        public <T extends ArgumentBuilder<CommandSourceStack, T>> T register(CommandBuildContext buildContext) {
            if (argumentType.isPresent()) {
                return (T) Commands.argument(this.name, this.argumentType.get());
            } else {
                return (T) Commands.literal(this.name);
            }
        }
    }
}
