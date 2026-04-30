package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record DoubleArgument(double min, double max) implements CommandArgument<Double> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return CommandArgumentTypes.DOUBLE_ARGUMENT.argumentTypeInfo();
    }

    @Override
    public ArgumentType<Double> getArgumentType(CommandBuildContext buildContext) {
        return com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg(this.min, this.max);
    }
}
