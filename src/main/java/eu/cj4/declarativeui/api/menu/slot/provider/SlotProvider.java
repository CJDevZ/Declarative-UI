package eu.cj4.declarativeui.api.menu.slot.provider;

import eu.cj4.declarativeui.impl.menu.slot.provider.AnimatedProvider;
import eu.cj4.declarativeui.impl.menu.slot.provider.EmptyProvider;
import eu.cj4.declarativeui.impl.menu.slot.provider.SimpleProvider;
import eu.cj4.declarativeui.impl.menu.slot.provider.TagProvider;
import eu.pb4.sgui.api.elements.GuiElement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface SlotProvider {
    SlotProviderType getType();

    GuiElement createElement(CommandSourceStack source, GuiElement.ClickCallback clickCallback);

    static @NonNull EmptyProvider empty() {
        return EmptyProvider.INSTANCE;
    }

    static @NonNull SimpleProvider simple(@Nullable ItemStackTemplate template) {
        return new SimpleProvider(Optional.ofNullable(template), Optional.empty());
    }

    static @NonNull SimpleProvider simple(@Nullable ItemStackTemplate template, LootItemFunction lootItemFunction) {
        return new SimpleProvider(Optional.ofNullable(template), Optional.of(lootItemFunction));
    }

    static @NonNull TagProvider tag(TagKey<Item> tag, int interval, boolean random) {
        return new TagProvider(tag, interval, random);
    }

    static @NonNull AnimatedProvider animated(List<SimpleProvider> stack, int interval, boolean random) {
        return new AnimatedProvider(stack, interval, random);
    }
}
