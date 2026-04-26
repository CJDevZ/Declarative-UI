package eu.cj4.declarativeui.api.container;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record DeclaredContainer(int size) {
    public static final Codec<DeclaredContainer> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("size").forGetter(DeclaredContainer::size)
            ).apply(instance, DeclaredContainer::new));
}
