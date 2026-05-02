package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

import java.util.function.BiFunction;

public class Codec1Argument<T, T1> implements CommandArgument<T> {
    private final ArgumentTypeInfo<?, ?> argumentTypeInfo;
    private final T1 t1;
    private final BiFunction<CommandBuildContext, T1, ArgumentType<T>> builder;

    protected Codec1Argument(ArgumentTypeInfo<?, ?> argumentTypeInfo, T1 t1, BiFunction<CommandBuildContext, T1, ArgumentType<T>> builder) {
        this.argumentTypeInfo = argumentTypeInfo;
        this.t1 = t1;
        this.builder = builder;
    }

    public T1 getT1() {
        return this.t1;
    }

    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return this.argumentTypeInfo;
    }

    @Override
    public ArgumentType<T> getArgumentType(CommandBuildContext buildContext) {
        return this.builder.apply(buildContext, this.t1);
    }

    public static <T, T1> MapCodec<Codec1Argument<T, T1>> create(ArgumentTypeInfo<?, ?> argumentTypeInfo, BiFunction<CommandBuildContext, T1, ArgumentType<T>> builder, MapCodec<T1> t1Codec) {
        return t1Codec.xmap(t1 -> new Codec1Argument<>(argumentTypeInfo, t1, builder), Codec1Argument::getT1);
    }
}
