package eu.cj4.declarativeui.api.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public interface CommandArgument<A> {
    ArgumentTypeInfo<?, ?> getType();

    ArgumentType<A> getArgumentType(CommandBuildContext buildContext);
}
