package eu.cj4.declarativeui.api.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.cj4.declarativeui.api.menu.slot.action.ClickAction;
import eu.cj4.declarativeui.impl.menu.SearchMenu;
import eu.cj4.declarativeui.impl.menu.SimpleMenu;
import eu.cj4.declarativeui.impl.menu.container.provider.DeclaredContainerProvider;
import eu.cj4.declarativeui.impl.menu.slot.ClickEvent;
import eu.cj4.declarativeui.impl.menu.slot.DeclaredSlot;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Menu {
    MenuType getType();

    Optional<Component> title();
    Component getDefaultTitle(RegistryAccess registryAccess);
    void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) throws CommandSyntaxException;

    static SimpleMenu simple(@Nullable Component title, net.minecraft.world.inventory.MenuType<?> menuType, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<DeclaredSlot> slots, List<DeclaredContainerProvider> containers, List<ClickAction> closeActions) {
        return new SimpleMenu(Optional.ofNullable(title), Holder.direct(menuType), manipulatePlayerSlots, lockPlayerInventory, slots, containers, closeActions);
    }

    static SearchMenu search(@Nullable Component title, @Nullable String searchTag, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<DeclaredSlot> slots, List<DeclaredContainerProvider> containers, List<ClickEvent> searchActions, List<ClickAction> closeActions) {
        return new SearchMenu(Optional.ofNullable(title), searchTag, manipulatePlayerSlots, lockPlayerInventory, slots, containers, searchActions, closeActions);
    }
}
