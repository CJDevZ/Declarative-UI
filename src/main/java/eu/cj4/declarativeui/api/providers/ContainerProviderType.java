package eu.cj4.declarativeui.api.providers;

import com.mojang.serialization.MapCodec;

public record ContainerProviderType(MapCodec<? extends ContainerProvider> codec) {
}
