package eu.cj4.declarativeui.api.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.command.CommandAction;
import eu.cj4.declarativeui.api.menu.slot.Slot;
import eu.cj4.declarativeui.api.menu.slot.ClickAction;
import eu.cj4.declarativeui.impl.menu.BookMenu;
import eu.cj4.declarativeui.impl.menu.HotbarMenu;
import eu.cj4.declarativeui.impl.menu.SearchMenu;
import eu.cj4.declarativeui.impl.menu.SimpleMenu;
import eu.cj4.declarativeui.impl.menu.slot.ClickEvent;
import eu.cj4.declarativeui.impl.registry.DeclarativeUIBuiltInRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Menu {
    MapCodec<? extends Menu> codec();

    Optional<Component> title();
    Component getDefaultTitle(RegistryAccess registryAccess);
    void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) throws CommandSyntaxException;

    static SimpleMenu simple(@Nullable Component title, net.minecraft.world.inventory.MenuType<?> menuType, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<Slot> slots, List<ClickAction> closeActions) {
        return new SimpleMenu(Optional.ofNullable(title), Holder.direct(menuType), manipulatePlayerSlots, lockPlayerInventory, slots, closeActions);
    }

    static SearchMenu search(@Nullable Component title, @Nullable String searchTag, boolean manipulatePlayerSlots, boolean lockPlayerInventory, List<Slot> slots, List<ClickEvent> searchActions, List<ClickAction> closeActions) {
        return new SearchMenu(Optional.ofNullable(title), searchTag, manipulatePlayerSlots, lockPlayerInventory, slots, searchActions, closeActions);
    }

    static BookMenu book(List<Component> pages) {
        return new BookMenu(pages);
    }

    static HotbarMenu hotbar(List<Slot> slots, List<ClickAction> closeActions) {
        return new HotbarMenu(slots, closeActions);
    }

    static <T extends Menu> MapCodec<T> register(Identifier id, MapCodec<T> mapCodec) {
        return Registry.register(DeclarativeUIBuiltInRegistries.MENU_TYPE, id, mapCodec);
    }
}
