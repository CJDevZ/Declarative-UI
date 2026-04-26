package eu.cj4.declarativeui.api.registry;

import eu.cj4.declarativeui.api.providers.CommandArgumentProvider;
import eu.cj4.declarativeui.impl.DeclarativeUI;
import eu.cj4.declarativeui.api.providers.ContainerProviderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class DeclarativeUIRegistries {
    public static final ResourceKey<Registry<ContainerProviderType>> CONTAINER_PROVIDER_TYPE = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(DeclarativeUI.MOD_ID, "container_provider_type"));
    public static final ResourceKey<Registry<CommandArgumentProvider>> COMMAND_ARGUMENT_PROVIDER = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(DeclarativeUI.MOD_ID, "command_argument_provider"));
}
