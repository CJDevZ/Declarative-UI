package eu.cj4.declarativeui.api.container;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import org.jspecify.annotations.Nullable;

public interface NamespacedContainerHolder {
    @Nullable Container declarative_ui$namespacedContainer(Identifier containerLocation);

    static NamespacedContainerHolder of(ServerPlayer serverPlayer) {
        return (NamespacedContainerHolder) serverPlayer;
    }
}
