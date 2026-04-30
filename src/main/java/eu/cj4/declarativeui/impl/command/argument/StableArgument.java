package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record StableArgument<T>(ArgumentTypeInfo<?, ?> argumentTypeInfo, ArgumentType<T> argumentType) implements CommandArgument<T> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return this.argumentTypeInfo;
    }

    @Override
    public ArgumentType<T> getArgumentType(CommandBuildContext buildContext) {
        return this.argumentType;
    }
}
