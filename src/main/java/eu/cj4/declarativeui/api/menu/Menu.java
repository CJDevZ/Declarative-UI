package eu.cj4.declarativeui.api.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.impl.menu.SimpleMenu;
import eu.cj4.declarativeui.impl.menu.container.provider.DeclaredContainerProvider;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredSlot;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Menu {
    MenuType getType();

    void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) throws CommandSyntaxException;

    static SimpleMenu simple(@Nullable Component title, net.minecraft.world.inventory.MenuType<?> menuType, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<DeclaredSlot> slots, List<DeclaredContainerProvider> containers, List<ClickAction> closeActions) {
        return new SimpleMenu(Optional.ofNullable(title), Holder.direct(menuType), manipulatePlayerSlots, lockPlayerInventory, slots, containers, closeActions);
    }

    static MenuType register(ResourceLocation id, MapCodec<? extends Menu> codec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.MENU_TYPE, id, new MenuType(codec));
    }
}
