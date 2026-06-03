package eu.cj4.declarativeui.impl.menu;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.MenuType;
import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.*;

public record BookMenu(List<Component> pages) implements Menu {
    public static final MapCodec<BookMenu> CODEC = WrittenBookContent.CONTENT_CODEC.sizeLimitedListOf(100).fieldOf("pages").xmap(
            BookMenu::new,
            BookMenu::pages
    );

    private static List<Filterable<Component>> convertToFilterable(List<Component> pages, CommandSourceStack sourceStack) throws CommandSyntaxException {
        List<Filterable<Component>> list = new ArrayList<>(pages.size());
        for (Component component : pages) {
            list.add(Filterable.passThrough(ComponentUtils.updateForEntity(sourceStack, component, sourceStack.getEntity(), 0)));
        }
        return Collections.unmodifiableList(list);
    }

    @Override
    public MenuType getType() {
        return MenuTypes.BOOK;
    }

    @Override
    public Optional<Component> title() {
        return Optional.empty();
    }

    @Override
    public Component getDefaultTitle(RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) throws CommandSyntaxException {
        List<Filterable<Component>> filterables = convertToFilterable(this.pages, sourceStack);
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(Filterable.passThrough(""), "", 0, filterables, true)).build());
        for (ServerPlayer target : targets) {
            new BookGui(target, book).open();
        }
    }
}
