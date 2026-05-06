package eu.cj4.declarativeui.impl.command.action;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ContextChain;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.action.CommandAction;
import eu.cj4.declarativeui.api.command.action.CommandActionType;
import eu.cj4.declarativeui.mixin.CommandContextAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.execution.ChainModifiers;
import net.minecraft.commands.execution.CustomCommandExecutor;
import net.minecraft.commands.execution.ExecutionControl;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.FunctionCommand;
import net.minecraft.server.permissions.LevelBasedPermissionSet;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class FunctionCommandAction extends CustomCommandExecutor.WithErrorHandling<CommandSourceStack> implements CustomCommandExecutor.CommandAdapter<CommandSourceStack>, CommandAction {
    public static final MapCodec<FunctionCommandAction> CODEC = Identifier.CODEC.fieldOf("function").xmap(FunctionCommandAction::new, FunctionCommandAction::function);

    private final Identifier function;

    public FunctionCommandAction(Identifier function) {
        this.function = function;
    }

    @Override
    public CommandActionType getType() {
        return CommandActionTypes.FUNCTION;
    }

    public Identifier function() {
        return this.function;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void runGuarded(CommandSourceStack sourceStack, ContextChain<CommandSourceStack> contextChain, ChainModifiers chainModifiers, ExecutionControl<CommandSourceStack> executionControl) throws CommandSyntaxException {
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

            CommandSourceStack commandSourceStack2 = sourceStack.withSuppressedOutput().withPermission(LevelBasedPermissionSet.GAMEMASTER);
            FunctionCommand.queueFunctions(Collections.singletonList(function.get()), compoundTag, sourceStack, commandSourceStack2, executionControl, null, chainModifiers);
        }
    }
}
