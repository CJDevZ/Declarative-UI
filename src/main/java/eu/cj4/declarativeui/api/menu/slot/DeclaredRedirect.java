package eu.cj4.declarativeui.api.menu.slot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record DeclaredRedirect(int slot, int containerSlot, Optional<Boolean> viewOnly) {
    public static final Codec<DeclaredRedirect> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("slot").forGetter(DeclaredRedirect::slot),
                    Codec.INT.fieldOf("container_slot").forGetter(DeclaredRedirect::containerSlot),
                    Codec.BOOL.optionalFieldOf("view_only").forGetter(DeclaredRedirect::viewOnly)
            ).apply(instance, DeclaredRedirect::new));
}
