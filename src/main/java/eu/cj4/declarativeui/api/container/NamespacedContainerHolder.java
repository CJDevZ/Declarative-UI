package eu.cj4.declarativeui.api.container;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import org.jspecify.annotations.Nullable;

public interface NamespacedContainerHolder {
    @Nullable Container declarative_ui$namespacedContainer(Identifier containerLocation);
}
