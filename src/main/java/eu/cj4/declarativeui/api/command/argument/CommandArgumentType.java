package eu.cj4.declarativeui.api.command.argument;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.impl.command.argument.*;
import eu.cj4.declarativeui.mixin.argument.ArgumentTypeInfosAccessor;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public record CommandArgumentType(ArgumentTypeInfo<?, ?> argumentTypeInfo, MapCodec<? extends CommandArgument<?>> codec) {
    public static final Codec<CommandArgument<?>> TYPED_CODEC;
    private static final Map<ArgumentTypeInfo<?, ?>, MapCodec<? extends CommandArgument<?>>> BY_TYPE_INFO = Maps.newHashMap();

    public static <T, T1> void registerCodec1(Class<? extends ArgumentType<T>> argumentClass, Function<T1, ArgumentType<T>> builder, MapCodec<T1> t1Codec) {
        registerCodec1(argumentClass, (buildContext, t1) -> builder.apply(t1), t1Codec);
    }

    public static <T, T1> void registerCodec1(Class<? extends ArgumentType<T>> argumentClass, BiFunction<CommandBuildContext, T1, ArgumentType<T>> builder, MapCodec<T1> t1Codec) {
        ArgumentTypeInfo<?, ?> argumentTypeInfo = ArgumentTypeInfosAccessor.getBY_CLASS().get(argumentClass);
        var mapCodec = Codec1Argument.create(argumentTypeInfo, builder, t1Codec);
        register(argumentClass, mapCodec);
    }

    public static <T, T1, T2> void registerCodec2(Class<? extends ArgumentType<T>> argumentClass, BiFunction<T1, T2, ArgumentType<T>> builder, MapCodec<T1> t1Codec, MapCodec<T2> t2Codec) {
        registerCodec2(argumentClass, (buildContext, t1, t2) -> builder.apply(t1, t2), t1Codec, t2Codec);
    }

    public static <T, T1, T2> void registerCodec2(Class<? extends ArgumentType<T>> argumentClass, Function3<CommandBuildContext, T1, T2, ArgumentType<T>> builder, MapCodec<T1> t1Codec, MapCodec<T2> t2Codec) {
        ArgumentTypeInfo<?, ?> argumentTypeInfo = ArgumentTypeInfosAccessor.getBY_CLASS().get(argumentClass);
        var mapCodec = Codec2Argument.create(argumentTypeInfo, builder, t1Codec, t2Codec);
        register(argumentClass, mapCodec);
    }

    public static <T> void registerContextAware(Class<? extends ArgumentType<T>> argumentClass, Function<CommandBuildContext, ArgumentType<T>> function) {
        ArgumentTypeInfo<?, ?> argumentTypeInfo = ArgumentTypeInfosAccessor.getBY_CLASS().get(argumentClass);
        var mapCodec = RecordCodecBuilder.build(RecordCodecBuilder.stable(new ContextAwareArgument<>(argumentTypeInfo, function)));
        register(argumentClass, mapCodec);
    }

    public static <T> void registerStable(Class<? extends ArgumentType<T>> argumentClass, ArgumentType<T> argumentType) {
        var mapCodec = RecordCodecBuilder.build(RecordCodecBuilder.stable(new StableArgument<>(ArgumentTypeInfos.byClass(argumentType), argumentType)));
        register(argumentClass, mapCodec);
    }

    public static <T> ArgumentTypeInfo<?, ?> register(Class<? extends ArgumentType<T>> argumentClass, MapCodec<? extends CommandArgument<T>> mapCodec) {
        var argumentTypeInfo = ArgumentTypeInfosAccessor.getBY_CLASS().get(argumentClass);
        BY_TYPE_INFO.put(argumentTypeInfo, mapCodec);
        return argumentTypeInfo;
    }

    static {
        TYPED_CODEC = BuiltInRegistries.COMMAND_ARGUMENT_TYPE.byNameCodec().dispatch(CommandArgument::getType, BY_TYPE_INFO::get);
    }
}
