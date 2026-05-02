package eu.cj4.declarativeui.api.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.command.argument.CommandArgumentTypes;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface CommandArgument<A> {
    ArgumentTypeInfo<?, ?> getType();

    ArgumentType<A> getArgumentType(CommandBuildContext buildContext);

    static <T> ArgumentTypeInfo<?, ?> register(Class<? extends ArgumentType<T>> argumentClass, MapCodec<? extends CommandArgument<T>> mapCodec) {
        return CommandArgumentTypes.register(argumentClass, mapCodec);
    }

    static <T, T1> void registerCodec1(Class<? extends ArgumentType<T>> argumentClass, Function<T1, ArgumentType<T>> builder, MapCodec<T1> t1Codec) {
        CommandArgumentTypes.registerCodec1(argumentClass, builder, t1Codec);
    }

    static <T, T1> void registerCodec1(Class<? extends ArgumentType<T>> argumentClass, BiFunction<CommandBuildContext, T1, ArgumentType<T>> builder, MapCodec<T1> t1Codec) {
        CommandArgumentTypes.registerCodec1(argumentClass, builder, t1Codec);
    }

    static <T, T1, T2> void registerCodec2(Class<? extends ArgumentType<T>> argumentClass, BiFunction<T1, T2, ArgumentType<T>> builder, MapCodec<T1> t1Codec, MapCodec<T2> t2Codec) {
        CommandArgumentTypes.registerCodec2(argumentClass, builder, t1Codec, t2Codec);
    }

    static <T, T1, T2> void registerCodec2(Class<? extends ArgumentType<T>> argumentClass, Function3<CommandBuildContext, T1, T2, ArgumentType<T>> builder, MapCodec<T1> t1Codec, MapCodec<T2> t2Codec) {
        CommandArgumentTypes.registerCodec2(argumentClass, builder, t1Codec, t2Codec);
    }

    static <T> void registerContextAware(Class<? extends ArgumentType<T>> argumentClass, Function<CommandBuildContext, ArgumentType<T>> function) {
        CommandArgumentTypes.registerContextAware(argumentClass, function);
    }

    static <T> void registerStable(Class<? extends ArgumentType<T>> argumentClass, ArgumentType<T> argumentType) {
        CommandArgumentTypes.registerStable(argumentClass, argumentType);
    }
}
