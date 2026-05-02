package eu.cj4.declarativeui.impl.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.command.argument.CommandArgument;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;

public class Codec2Argument<T, T1, T2> implements CommandArgument<T> {
    private final ArgumentTypeInfo<?, ?> argumentTypeInfo;
    private final T1 t1;
    private final T2 t2;
    private final Function3<CommandBuildContext, T1, T2, ArgumentType<T>> builder;

    protected Codec2Argument(ArgumentTypeInfo<?, ?> argumentTypeInfo, T1 t1, T2 t2, Function3<CommandBuildContext, T1, T2, ArgumentType<T>> builder) {
        this.argumentTypeInfo = argumentTypeInfo;
        this.t1 = t1;
        this.t2 = t2;
        this.builder = builder;
    }

    public T1 getT1() {
        return this.t1;
    }
    public T2 getT2() {
        return this.t2;
    }

    @Override
    public ArgumentTypeInfo<?, ?> getType() {
        return this.argumentTypeInfo;
    }

    @Override
    public ArgumentType<T> getArgumentType(CommandBuildContext buildContext) {
        return this.builder.apply(buildContext, this.t1, this.t2);
    }

    public static <T, T1, T2> MapCodec<Codec2Argument<T, T1, T2>> create(ArgumentTypeInfo<?, ?> argumentTypeInfo, Function3<CommandBuildContext, T1, T2, ArgumentType<T>> builder, MapCodec<T1> t1Codec, MapCodec<T2> t2Codec) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                t1Codec.forGetter(Codec2Argument::getT1),
                t2Codec.forGetter(Codec2Argument::getT2)
        ).apply(instance, (t1, t2) -> new Codec2Argument<>(argumentTypeInfo, t1, t2, builder)));
    }
}
