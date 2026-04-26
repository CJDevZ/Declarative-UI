package eu.cj4.declarativeui.api.registry;

import eu.cj4.declarativeui.api.providers.CommandArgumentProvider;
import eu.cj4.declarativeui.api.providers.ContainerProviderType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;

public final class DeclarativeUIBuiltInRegistries {
    public static final Registry<ContainerProviderType> CONTAINER_PROVIDER_TYPE;
    public static final Registry<CommandArgumentProvider> COMMAND_ARGUMENT_PROVIDER;

    public static void bootStrap() {
    }

    static {
        CONTAINER_PROVIDER_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.CONTAINER_PROVIDER_TYPE).buildAndRegister();
        COMMAND_ARGUMENT_PROVIDER = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.COMMAND_ARGUMENT_PROVIDER).buildAndRegister();
    }
}
