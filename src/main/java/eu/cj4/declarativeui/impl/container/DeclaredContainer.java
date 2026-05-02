package eu.cj4.declarativeui.impl.container;

import com.mojang.serialization.Codec;

public record DeclaredContainer(int size) {
    public static final Codec<DeclaredContainer> CODEC = Codec.INT.fieldOf("size").codec().xmap(DeclaredContainer::new, DeclaredContainer::size);

    public PlayerContainer createContainer() {
        return new PlayerContainer(this.size);
    }
}
