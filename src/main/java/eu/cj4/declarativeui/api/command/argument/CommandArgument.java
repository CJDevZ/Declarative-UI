package eu.cj4.declarativeui.api.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.command.argument.CommandArgumentTypes;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

import java.util.function.Function;

public interface CommandArgument<A> {
    ArgumentTypeInfo<?, ?> getType();

    ArgumentType<A> getArgumentType(CommandBuildContext buildContext);

    static <T> CommandArgumentType register(Class<? extends ArgumentType<T>> argumentClass, MapCodec<? extends CommandArgument<T>> mapCodec) {
        return CommandArgumentTypes.register(argumentClass, mapCodec);
    }

    static <T> void registerContextAware(Class<? extends ArgumentType<T>> argumentClass, Function<CommandBuildContext, ArgumentType<T>> function) {
        CommandArgumentTypes.registerContextAware(argumentClass, function);
    }

    static <T> void registerStable(Class<? extends ArgumentType<T>> argumentClass, ArgumentType<T> argumentType) {
        CommandArgumentTypes.registerStable(argumentClass, argumentType);
    }
}
