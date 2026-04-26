package eu.cj4.declarativeui.api;

import eu.cj4.declarativeui.api.container.DeclaredContainer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import org.jetbrains.annotations.Nullable;

public interface NamespacedContainerHolder {
    @Nullable Container declarative_ui$namespacedContainer(ResourceKey<DeclaredContainer> resourceKey);
}
