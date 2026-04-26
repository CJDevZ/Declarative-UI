package eu.cj4.declarativeui.api.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.container.DeclaredContainer;
import eu.cj4.declarativeui.api.menu.slot.DeclaredRedirect;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.impl.providers.ContainerProviders;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

import java.util.List;

public record DeclaredContainerProvider(ContainerProvider provider, ResourceKey<DeclaredContainer> container, List<DeclaredRedirect> redirects, boolean viewOnly) {
    public static final Codec<DeclaredContainerProvider> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ContainerProviders.TYPED_CODEC.fieldOf("provider").forGetter(DeclaredContainerProvider::provider),
                    ResourceKey.codec(DeclarativeUI.CONTAINER_REGISTRY).fieldOf("container").forGetter(DeclaredContainerProvider::container),
                    Codec.list(DeclaredRedirect.CODEC).fieldOf("redirects").forGetter(DeclaredContainerProvider::redirects),
                    Codec.BOOL.optionalFieldOf("view_only", false).forGetter(DeclaredContainerProvider::viewOnly)
            ).apply(instance, DeclaredContainerProvider::new));

    public Container getContainer(ServerPlayer serverPlayer) {
        return provider.getNamespacedContainerHolder(serverPlayer).declarative_ui$namespacedContainer(container);
    }
}
