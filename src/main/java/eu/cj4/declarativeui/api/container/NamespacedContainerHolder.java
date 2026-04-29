package eu.cj4.declarativeui.api.container;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import org.jetbrains.annotations.Nullable;

public interface NamespacedContainerHolder {
    @Nullable Container declarative_ui$namespacedContainer(ResourceLocation containerLocation);
}
