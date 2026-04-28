package eu.cj4.declarativeui.api.menu.slot.provider;

import eu.cj4.declarativeui.impl.providers.slot.AnimatedProvider;
import eu.cj4.declarativeui.impl.providers.slot.SimpleProvider;
import eu.cj4.declarativeui.impl.providers.slot.TagProvider;
import eu.pb4.sgui.api.elements.GuiElementInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface SlotProvider {
    SlotProviderType getType();

    GuiElementInterface createElement(CommandSourceStack source, GuiElementInterface.ClickCallback clickCallback);

    static @NotNull SimpleProvider simple(ItemStack stack) {
        return new SimpleProvider(stack, Optional.empty());
    }

    static @NotNull SimpleProvider simple(ItemStack stack, LootItemFunction lootItemFunction) {
        return new SimpleProvider(stack, Optional.of(lootItemFunction));
    }

    static @NotNull TagProvider tag(TagKey<Item> tag, int interval, boolean random) {
        return new TagProvider(tag, interval, random);
    }

    static @NotNull AnimatedProvider animated(List<SimpleProvider> stack, int interval, boolean random) {
        return new AnimatedProvider(stack, interval, random);
    }
}
