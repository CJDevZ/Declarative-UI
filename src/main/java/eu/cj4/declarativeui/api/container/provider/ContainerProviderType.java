package eu.cj4.declarativeui.api.container.provider;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public record ContainerProviderType(MapCodec<? extends ContainerProvider> codec) {
    public static ContainerProviderType register(Identifier id, MapCodec<? extends ContainerProvider> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.CONTAINER_PROVIDER_TYPE, id, new ContainerProviderType(mapCodec));
    }
}
