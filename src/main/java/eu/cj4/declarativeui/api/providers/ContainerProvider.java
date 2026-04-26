package eu.cj4.declarativeui.api.providers;

import eu.cj4.declarativeui.api.NamespacedContainerHolder;
import net.minecraft.server.level.ServerPlayer;

public interface ContainerProvider {
    ContainerProviderType getType();

    NamespacedContainerHolder getNamespacedContainerHolder(ServerPlayer serverPlayer);
}
