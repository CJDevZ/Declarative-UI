package eu.cj4.declarativeui.impl.registry;

import eu.cj4.declarativeui.api.command.action.CommandActionType;
import eu.cj4.declarativeui.api.menu.slot.SlotType;
import eu.cj4.declarativeui.api.menu.slot.action.ClickActionType;
import eu.cj4.declarativeui.api.menu.slot.provider.SlotProviderType;
import eu.cj4.declarativeui.api.container.provider.ContainerProviderType;
import eu.cj4.declarativeui.api.menu.MenuType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;

public final class DeclarativeUIBuiltInRegistries {
    public static final Registry<MenuType> MENU_TYPE;
    public static final Registry<SlotType> SLOT_TYPE;
    public static final Registry<ContainerProviderType> CONTAINER_PROVIDER_TYPE;
    public static final Registry<SlotProviderType> SLOT_PROVIDER_TYPE;
    public static final Registry<CommandActionType> COMMAND_ACTION_TYPE;
    public static final Registry<ClickActionType> CLICK_ACTION_TYPE;

    public static void bootStrap() {
    }

    static {
        MENU_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.MENU_TYPE).buildAndRegister();
        SLOT_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.SLOT_TYPE).buildAndRegister();
        CONTAINER_PROVIDER_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.CONTAINER_PROVIDER_TYPE).buildAndRegister();
        SLOT_PROVIDER_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.SLOT_PROVIDER_TYPE).buildAndRegister();
        COMMAND_ACTION_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.COMMAND_ACTION_TYPE).buildAndRegister();
        CLICK_ACTION_TYPE = FabricRegistryBuilder.createSimple(DeclarativeUIRegistries.CLICK_ACTION_TYPE).buildAndRegister();
    }
}
