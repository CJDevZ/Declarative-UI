package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import eu.cj4.declarativeui.mixin.argument.StringArgumentTypeAccessor;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public record StringArgument(StringArgumentType.StringType type) implements CommandArgument<String> {
    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return CommandArgumentTypes.STRING_ARGUMENT.argumentTypeInfo();
    }

    @Override
    public ArgumentType<String> getArgumentType(CommandBuildContext buildContext) {
        return StringArgumentTypeAccessor.create(this.type);
    }
}
