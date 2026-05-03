package eu.cj4.declarativeui.impl.container;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;

public record DeclaredContainer(int size) {
    public static final Codec<DeclaredContainer> CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("size").codec().xmap(DeclaredContainer::new, DeclaredContainer::size);

    public PlayerContainer createContainer() {
        return new PlayerContainer(this.size);
    }
}
