package eu.cj4.declarativeui.impl.registry;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.CommandAction;
import eu.cj4.declarativeui.api.container.ContainerProvider;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.api.menu.slot.SlotProvider;
import eu.cj4.declarativeui.impl.command.DeclaredCommand;
import eu.cj4.declarativeui.impl.container.DeclaredContainer;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.impl.customclickaction.DeclaredCustomClickAction;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import static eu.cj4.declarativeui.impl.DeclarativeUI.MOD_ID;

public final class DeclarativeUIRegistries {
    public static final ResourceKey<Registry<MapCodec<? extends ContainerProvider>>> CONTAINER_PROVIDER_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "container_provider_type"));
    public static final ResourceKey<Registry<MapCodec<? extends SlotProvider>>> SLOT_PROVIDER_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "slot_provider_type"));
    public static final ResourceKey<Registry<MapCodec<? extends CommandAction>>> COMMAND_ACTION_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "command_action_type"));
    public static final ResourceKey<Registry<MapCodec<? extends ClickAction>>> CLICK_ACTION_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "click_action_type"));
    public static final ResourceKey<Registry<MapCodec<? extends Menu>>> MENU_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "menu_type"));
    public static final ResourceKey<Registry<MapCodec<? extends Slot>>> SLOT_TYPE = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "slot_type"));

    public static final ResourceKey<Registry<Menu>> MENU = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "menu"));
    public static final ResourceKey<Registry<DeclaredContainer>> CONTAINER = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "container"));
    public static final ResourceKey<Registry<DeclaredCommand>> COMMAND = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "command"));
    public static final ResourceKey<Registry<DeclaredCustomClickAction>> CUSTOM_CLICK_ACTION = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MOD_ID, "custom_click_action"));
}
