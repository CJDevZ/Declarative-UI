package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record IntegerArgument(int min, int max) implements CommandArgument<Integer> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return CommandArgumentTypes.INTEGER_ARGUMENT.argumentTypeInfo();
    }

    @Override
    public ArgumentType<Integer> getArgumentType(CommandBuildContext buildContext) {
        return com.mojang.brigadier.arguments.IntegerArgumentType.integer(this.min, this.max);
    }
}
