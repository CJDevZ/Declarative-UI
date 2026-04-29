package eu.cj4.declarativeui.impl.registry;

import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.impl.command.DeclaredCommand;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.impl.menu.DeclaredMenu;
import eu.cj4.declarativeui.api.command.action.CommandActionType;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProviderType;
import eu.cj4.declarativeui.api.container.provider.ContainerProviderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static eu.cj4.declarativeui.impl.DeclarativeUI.MOD_ID;

public final class DeclarativeUIRegistries {
    public static final ResourceKey<Registry<ContainerProviderType>> CONTAINER_PROVIDER_TYPE = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "container_provider_type"));
    public static final ResourceKey<Registry<SlotProviderType>> SLOT_PROVIDER_TYPE = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "slot_provider_type"));
    public static final ResourceKey<Registry<CommandActionType>> COMMAND_ACTION_TYPE = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "command_action_type"));
    public static final ResourceKey<Registry<ClickActionType>> CLICK_ACTION_TYPE = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "click_action_type"));

    public static final ResourceKey<Registry<DeclaredMenu>> MENU_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "menu"));
    public static final ResourceKey<Registry<DeclaredContainer>> CONTAINER_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "container"));
    public static final ResourceKey<Registry<DeclaredCommand>> COMMAND_REGISTRY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MOD_ID, "command"));
}
