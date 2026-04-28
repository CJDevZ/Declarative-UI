package eu.cj4.declarativeui.api.registry;

import eu.cj4.declarativeui.api.menu.slot.provider.SlotProviderType;
import eu.cj4.declarativeui.api.providers.ContainerProviderType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;

public final class DeclarativeUIBuiltInRegistries {
    public static final Registry<ContainerProviderType> CONTAINER_PROVIDER_TYPE;
    public static final Registry<SlotProviderType> SLOT_PROVIDER_TYPE;

    public static void bootStrap() {
    }

    static {
        CONTAINER_PROVIDER_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.CONTAINER_PROVIDER_TYPE).buildAndRegister();
        SLOT_PROVIDER_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.SLOT_PROVIDER_TYPE).buildAndRegister();
    }
}
