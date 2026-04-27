package eu.cj4.declarativeui.api.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eu.cj4.declarativeui.api.menu.slot.DeclaredRedirect;
import eu.cj4.declarativeui.impl.providers.ContainerProviders;

import java.util.List;

public record DeclaredContainerProvider(ContainerProvider provider, List<DeclaredRedirect> redirects, boolean viewOnly) {
    public static final Codec<DeclaredContainerProvider> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ContainerProviders.TYPED_CODEC.fieldOf("provider").forGetter(DeclaredContainerProvider::provider),
                    Codec.list(DeclaredRedirect.CODEC).fieldOf("redirects").forGetter(DeclaredContainerProvider::redirects),
                    Codec.BOOL.optionalFieldOf("view_only", false).forGetter(DeclaredContainerProvider::viewOnly)
            ).apply(instance, DeclaredContainerProvider::new));
}
