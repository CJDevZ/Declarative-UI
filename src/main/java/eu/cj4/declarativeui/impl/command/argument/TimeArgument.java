package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import eu.cj4.declarativeui.mixin.argument.StringArgumentTypeAccessor;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record TimeArgument(int min) implements CommandArgument<Integer> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return CommandArgumentTypes.TIME_ARGUMENT.argumentTypeInfo();
    }

    @Override
    public ArgumentType<Integer> getArgumentType(CommandBuildContext buildContext) {
        return net.minecraft.commands.arguments.TimeArgument.time(this.min);
    }
}
