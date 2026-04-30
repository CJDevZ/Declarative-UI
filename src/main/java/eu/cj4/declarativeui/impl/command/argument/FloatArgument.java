package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record FloatArgument(float min, float max) implements CommandArgument<Float> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return CommandArgumentTypes.FLOAT_ARGUMENT.argumentTypeInfo();
    }

    @Override
    public ArgumentType<Float> getArgumentType(CommandBuildContext buildContext) {
        return com.mojang.brigadier.arguments.FloatArgumentType.floatArg(this.min, this.max);
    }
}
