package eu.cj4.declarativeui.api.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class LazyEnumCodec<S extends Enum<?>> implements Codec<S> {
    private final Codec<S> codec;

    protected LazyEnumCodec(S[] stringRepresentables, Function<S, String> nameFunction, ToIntFunction<S> toIntFunction) {
        this.codec = ExtraCodecs.orCompressed(Codec.stringResolver(nameFunction, StringRepresentable.createNameLookup(stringRepresentables, nameFunction)), ExtraCodecs.idResolverCodec(toIntFunction, (i) -> i >= 0 && i < stringRepresentables.length ? stringRepresentables[i] : null, -1));
    }

    public static <S extends Enum<?>> LazyEnumCodec<S> fromEnum(S[] values) {
        return new LazyEnumCodec<>(values, s -> s.name().toLowerCase(Locale.ROOT), Enum::ordinal);
    }

    public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> dynamicOps, T object) {
        return this.codec.decode(dynamicOps, object);
    }

    public <T> DataResult<T> encode(S stringRepresentable, DynamicOps<T> dynamicOps, T object) {
        return this.codec.encode(stringRepresentable, dynamicOps, object);
    }
}
