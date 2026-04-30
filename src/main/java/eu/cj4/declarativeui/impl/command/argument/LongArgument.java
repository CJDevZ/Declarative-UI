package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record LongArgument(long min, long max) implements CommandArgument<Long> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return CommandArgumentTypes.LONG_ARGUMENT.argumentTypeInfo();
    }

    @Override
    public ArgumentType<Long> getArgumentType(CommandBuildContext buildContext) {
        return com.mojang.brigadier.arguments.LongArgumentType.longArg(this.min, this.max);
    }
}
