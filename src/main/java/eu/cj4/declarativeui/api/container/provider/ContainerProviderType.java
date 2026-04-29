package eu.cj4.declarativeui.api.container.provider;

import com.mojang.serialization.MapCodec;

public record ContainerProviderType(MapCodec<? extends ContainerProvider> codec) {
}
