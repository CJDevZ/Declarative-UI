package eu.cj4.declarativeui.impl.menu;

import com.mojang.serialization.MapCodec;
import eu.cj4.declarativeui.api.menu.Menu;
import eu.cj4.declarativeui.api.menu.MenuType;
import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record BookMenu(WrittenBookContent content) implements Menu {
    public static final MapCodec<BookMenu> CODEC = WrittenBookContent.CONTENT_CODEC.sizeLimitedListOf(100).fieldOf("pages").xmap(
            BookMenu::new,
            book -> book.content.pages().stream().map(Filterable::raw).toList()
    );

    public BookMenu(List<Component> content) {
        List<Filterable<Component>> list = new ArrayList<>(content.size());
        for (Component component : content) {
            list.add(Filterable.passThrough(component));
        }
        this(new WrittenBookContent(Filterable.passThrough(""), "", 0, list, true));
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
    public void open(CommandSourceStack sourceStack, Collection<ServerPlayer> targets) {
        ItemStack book = new ItemStack(Items.WRITTEN_BOOK.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(DataComponents.WRITTEN_BOOK_CONTENT, this.content).build());
        for (ServerPlayer target : targets) {
            new BookGui(target, book).open();
        }
    }
}
