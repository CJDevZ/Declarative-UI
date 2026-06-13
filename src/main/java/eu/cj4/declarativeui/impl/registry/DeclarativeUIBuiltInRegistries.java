package eu.cj4.declarativeui.impl.registry;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.CommandAction;
import eu.cj4.declarativeui.api.container.ContainerProvider;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.cj4.declarativeui.api.menu.slot.SlotProvider;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;

public final class DeclarativeUIBuiltInRegistries {
    public static final Registry<MapCodec<? extends Menu>> MENU_TYPE;
    public static final Registry<MapCodec<? extends Slot>> SLOT_TYPE;
    public static final Registry<MapCodec<? extends ContainerProvider>> CONTAINER_PROVIDER_TYPE;
    public static final Registry<MapCodec<? extends SlotProvider>> SLOT_PROVIDER_TYPE;
    public static final Registry<MapCodec<? extends CommandAction>> COMMAND_ACTION_TYPE;
    public static final Registry<MapCodec<? extends ClickAction>> CLICK_ACTION_TYPE;

    public static void bootStrap() {
    }

    static {
        MENU_TYPE = FabricRegistryBuilder.create(DeclarativeUIRegistries.MENU_TYPE).buildAndRegister();
        SLOT_TYPE = FabricRegistryBuilder.create(DeclarativeUIRegistries.SLOT_TYPE).buildAndRegister();
        CONTAINER_PROVIDER_TYPE = FabricRegistryBuilder.create(DeclarativeUIRegistries.CONTAINER_PROVIDER_TYPE).buildAndRegister();
        SLOT_PROVIDER_TYPE = FabricRegistryBuilder.create(DeclarativeUIRegistries.SLOT_PROVIDER_TYPE).buildAndRegister();
        COMMAND_ACTION_TYPE = FabricRegistryBuilder.create(DeclarativeUIRegistries.COMMAND_ACTION_TYPE).buildAndRegister();
        CLICK_ACTION_TYPE = FabricRegistryBuilder.create(DeclarativeUIRegistries.CLICK_ACTION_TYPE).buildAndRegister();
    }
}
