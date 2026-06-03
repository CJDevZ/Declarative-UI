package eu.cj4.declarativeui.impl.registry;

import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.impl.command.DeclaredCommand;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.api.command.action.CommandActionType;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProviderType;
import eu.cj4.declarativeui.api.container.provider.ContainerProviderType;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.MenuType;
import eu.cj4.declarativeui.impl.customclickaction.DeclaredCustomClickAction;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import static eu.cj4.declarativeui.impl.DeclarativeUI.MOD_ID;

public final class DeclarativeUIRegistries {
    public static final ResourceKey<Registry<ContainerProviderType>> CONTAINER_PROVIDER_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "container_provider_type"));
    public static final ResourceKey<Registry<SlotProviderType>> SLOT_PROVIDER_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "slot_provider_type"));
    public static final ResourceKey<Registry<CommandActionType>> COMMAND_ACTION_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "command_action_type"));
    public static final ResourceKey<Registry<ClickActionType>> CLICK_ACTION_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "click_action_type"));
    public static final ResourceKey<Registry<MenuType>> MENU_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "menu_type"));

    public static final ResourceKey<Registry<Menu>> MENU = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "menu"));
    public static final ResourceKey<Registry<DeclaredContainer>> CONTAINER = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "container"));
    public static final ResourceKey<Registry<DeclaredCommand>> COMMAND = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "command"));
    public static final ResourceKey<Registry<DeclaredCustomClickAction>> CUSTOM_CLICK_ACTION = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "custom_click_action"));
}
